package com.unisys.spc.bpm;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.services.client.api.RemoteRestRuntimeFactory;
import org.kie.services.client.api.RestRequestHelper;
import org.kie.services.client.serialization.JaxbSerializationProvider;
import org.kie.services.client.serialization.JsonSerializationProvider;

import com.unisys.cloud.platform.common.dto.Lease;
import com.unisys.cloud.platform.common.dto.TimePeriod;
import com.unisys.cloud.platform.common.dto.TimePeriodUnit;
import com.unisys.spc.bpm.dto.BlueprintOid;
import com.unisys.spc.bpm.dto.FolderOid;
import com.unisys.spc.experimental.automation.HelloDto;

public class RestApi {

	private static JaxbSerializationProvider jaxbSerializationProvider = new JaxbSerializationProvider();
	private static JsonSerializationProvider jsonSerializationProvider = new JsonSerializationProvider();
	  public static void main(String[] args) throws Exception
	   {
		  	URL deploymentUrl;
		  	String userId = "admin";
		  	String password = "admin";
		  	deploymentUrl = new URL("http://ustr-erl-1841:8080/jbpm-console/");
		  	boolean like_BZ_994905 = false;		  	
		  			  	
		  	RemoteRestRuntimeFactory restSessionFactory = new RemoteRestRuntimeFactory("org.jbpm.example:async-examples:1.0", deploymentUrl, userId, password);
		    RuntimeEngine engine = restSessionFactory.newRuntimeEngine();      
		    KieSession ksession = engine.getKieSession();
		    		    
		    Map<String, Object> params = new HashMap<String, Object>();
		    HelloDto helloObj = new HelloDto();
		    helloObj.setGreeting("Hello");
		    helloObj.setRecipient("jBPM");
		    String dto = jaxbSerializationProvider.serialize(helloObj);
			params.put("helloDto1", dto);		    
		    ProcessInstance processIns = ksession.startProcess("async-examples.HelloDtoProcess", params);
		    
		  	
		  	/*
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
		      String urlString = null;
		      urlString = "http://localhost:8080/jbpm-console/rest/runtime/com.sample:HelloWorld:1.0"
			            + "/withvars/process/HelloWorld.HelloWorld/start";
		      ClientRequest restRequest = requestFactory.createRequest(urlString); 
		      
		      BlueprintOid blueprint = new BlueprintOid("ryerfew7787");
		      
		      //restRequest.body(MediaType.APPLICATION_JSON, blueprint);
		      
		      //restRequest.queryParameter("map_blueprintId", blueprint);
		      		                  
		    	  String body = jsonSerializationProvider.serialize(blueprint);            
		    	  restRequest.body(MediaType.APPLICATION_JSON, body);
			

		      ClientResponse<?> responseObj = checkResponse(restRequest.post());
		      
		      */
		    
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
