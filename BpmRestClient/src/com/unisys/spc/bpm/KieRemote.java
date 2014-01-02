package com.unisys.spc.bpm;
import java.net.URL;
import java.util.Map;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jbpm.process.audit.VariableInstanceLog;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.services.client.api.RemoteRestRuntimeFactory;
import org.kie.services.client.api.RestRequestHelper;
import org.kie.services.client.serialization.jaxb.impl.audit.AbstractJaxbHistoryObject;
import org.kie.services.client.serialization.jaxb.impl.audit.JaxbHistoryLogList;
import org.kie.services.client.serialization.jaxb.impl.audit.JaxbVariableInstanceLog;
import org.kie.services.client.serialization.jaxb.impl.process.JaxbProcessInstanceWithVariablesResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

public class KieRemote
{

   public static void main(String[] args) throws Exception
   {

      // https://github.com/droolsjbpm/droolsjbpm-integration/blob/master/kie-remote/kie-services-client/src/test/java/org/kie/services/client/api/LiveServerTest.java
	   /*
	    * 
	    * java -cp c:\apps\jbpm-clients\CommissionVMRESTClient-2.8.xxxxx-jar-with-dependencies.jar ;C:\data\m2_repo\org\jboss\resteasy\resteasy-jaxb-provider\3.0.3.Final\resteasy-jaxb-provider-3.0.3.Final.jar
	    * 	<classname> 
	    * 	<jbpm-server>              													--args[0]
	    * 	<port>																		--args[1] 
	    * 	<deploymentId> 																--args[2]
	    * 	<processId>	Note: processId for start, processInstanceId for history		--args[3]
        * 	<operation>																	--args[4]
	    * 	<process-specific-parameter-1-name>=<process-specific-parameter-1-value>	--args[5]
	    * 	<process-specific-parameter-2-name>=<process-specific-parameter-2-value>	--args[6]
        * 	..
        * 	<process-specific-parameter-n-name>=<process-specific-parameter-n-value>	--args[n]
	

        * So to call CommissionVM, here is call

        * java -cp c:\apps\jbpm-clients\CommissionVMRESTClient-2.8.xxxxx-jar-with-dependencies.jar  ;C:\data\m2_repo\org\jboss\resteasy\resteasy-jaxb-provider\3.0.3.Final\resteasy-jaxb-provider-3.0.3.Final.jar
	    * 	KieRemote 
        * 	ustr-erl-2038 
        * 	8080 
        * 	com.unisys.spc.experimental.automation:CommissionVM2:2.8.0 
        * 	com.unisys.spc.experimental.jbpm.bpmn.commissionVM
        *   start 
        * 	template=CentOS memorysize=4096 cmdClass=com.unisys.spc.experimental.automation.CommissionVMCommand
        *   requestClass=com.unisys.spc.spc.jbpm.async.SPCInitializeRequestCommand
	    */
      String parameters = "";
      for (int i = 5; i < args.length; i++)

      {
    	 if (i == 5)
    	 {
    		 parameters = "?map_"; 
    	 }
    	 else
         {
            parameters = parameters + "&map_";
         }

         parameters = parameters + args[i];

      }
      // String deploymentId = "org.jbpm.example:async-examples:1.0";
      // String deploymentId = "com.unisys.spc.experimental.automation:CommissionVM:2.8.1";
      String deploymentId = args[2];
      String processId = args[3];
      String operation = args[4];
      // String port = "8080";
      // if (args.length >= 6) { port = args[1]; }

      URL deploymentUrl;
      String userId = "admin";
      String password = "admin";
      deploymentUrl = new URL("http://" + args[0] + ":" + args[1] + "/jbpm-console/");
      boolean like_BZ_994905 = false;
      ClientRequestFactory requestFactory;

      if (like_BZ_994905)
      {
         DefaultHttpClient httpClient = new DefaultHttpClient();

         httpClient.getCredentialsProvider().setCredentials(
               new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
               new UsernamePasswordCredentials(userId, password));
         ClientExecutor clientExecutor = new ApacheHttpClient4Executor(httpClient);
         requestFactory = new ClientRequestFactory(clientExecutor, ResteasyProviderFactory.getInstance());
      }
      else
      {
         requestFactory = RestRequestHelper.createRestRequestFactory(deploymentUrl.toString(), userId, password, 600000);
      }      

      // Create (start process) request
      // String urlString = "http://localhost:8080/jbpm-console/rest/runtime/org.jbpm.example:async-examples:1.0/process/CheckWeather/start";
      // String urlString = "http://localhost:8080/jbpm-console/rest/runtime/com.unisys.spc.experimental.automation:CommissionVM:2.8.1/process/CommissionVM.CommissionVM/start";
      String urlString = null;
      if (operation.equalsIgnoreCase("start")) {
	      urlString = "http://" + args[0] + ":" + args[1] + "/jbpm-console/rest/runtime/" + deploymentId
	            + "/withvars/process/" + processId + "/start";
	      //    + "/process/"          + processId + "/start";

      }
      else if (operation.equalsIgnoreCase("history"))
      {
	      urlString = "http://" + args[0] + ":" + args[1] + "/jbpm-console/rest/runtime/" + deploymentId
		            + "/history/instance/" + processId + "/variable"; // returns JaxbHistoryLogList(List<AuditEvent>())
      }
      else
      {
    	  String errMessage = String.format("%s is an unrecognized operation", operation);
          System.out.println(errMessage);
          return;
      }
      urlString = urlString + parameters;

      ClientRequest restRequest = requestFactory.createRequest(urlString);            

      if (operation.equalsIgnoreCase("start"))
      {
          // Post, get response, check status response, and get info
          ClientResponse<?> responseObj = checkResponse(restRequest.post());
          //JaxbProcessInstanceResponse processInstanceResponse = (JaxbProcessInstanceResponse)responseObj.getEntity(JaxbProcessInstanceResponse.class);
          //ProcessInstance processInstance = processInstanceResponse.getResult();
          JaxbProcessInstanceWithVariablesResponse processVariables = (JaxbProcessInstanceWithVariablesResponse)responseObj.getEntity(JaxbProcessInstanceWithVariablesResponse.class); // valid with /withvars/process/<procDefId>/start
          ProcessInstance processInstance = processVariables.getProcessInstance(); 
       	  long procInstId =  processInstance.getId();
       	  System.out.println("Printing process instance id: " + procInstId);
       	  int  procInstState = processInstance.getState();
       	  //processInstance.getProcessName() is causing an UnsupportedOperationException
       	  //Uncomment the following after resolving the exception
       	  //String procName = processInstance.getProcessName();
       	  String procId = processInstance.getProcessId();
          Document document = DocumentHelper.createDocument();
          Element root = document.addElement("ProcessInstance");
          root.addAttribute("ProcessInstanceId", Long.toString(processInstance.getId()));
          Element elementProcInstState = root.addElement("ProcessInstanceState");
          elementProcInstState.setText(Integer.toString(processInstance.getState()));
          //Element elementProcName = root.addElement("ProcessName");
          //elementProcName.setText(processInstance.getProcessName());
          Element elementProcessId = root.addElement("ProcessId");
          elementProcessId.setText(processInstance.getProcessId());
          Element elementProcessVariables = root.addElement("ProcessVariables");
          for (Map.Entry<String, String> entry : processVariables.getVariables().entrySet())
          {
        	  Element elementProcessVariable = elementProcessVariables.addElement(entry.getKey());
        	  elementProcessVariable.setText(entry.getValue());
          }
          XMLWriter output = new XMLWriter();
          output.write(document);
          output.close();
          responseObj.releaseConnection();
          //System.out.println("ProcessInstance Id from KieClass: "+processInstance.getId());
      }
      else if (operation.equalsIgnoreCase("history"))
      {
          // issue get, get response, check status response, and get info
          restRequest.accept("application/xml");
    	  ClientResponse<?> responseObj = checkResponse(restRequest.get());
          JaxbHistoryLogList logList = (JaxbHistoryLogList)responseObj.getEntity(JaxbHistoryLogList.class);
          for (AbstractJaxbHistoryObject<?> log  : logList.getHistoryLogList())
          {
        	  String s = log.toString();
        	  JaxbVariableInstanceLog jvil = (JaxbVariableInstanceLog) log; 
        	  String variableId = jvil.getVariableId();
        	  String oldValue = jvil.getOldValue();
        	  String value = jvil.getValue();
          }
          // INCOMPLETE. create XML document like the /start code path
      }
   }

   private static ClientResponse<?> checkResponse(ClientResponse<?> responseObj) throws Exception
   {
      responseObj.resetStream();
      int status = responseObj.getStatus();
      if (status != 200)
      {
         System.out.println("Response with exception:\n" + responseObj.getEntity(String.class));
         // assertEquals("Status OK", 200, status);
      }
      return responseObj;
   }
}
