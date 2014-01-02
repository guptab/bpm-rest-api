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
		  	deploymentUrl = new URL("http://localhost:8080/jbpm-console/");
		  	boolean like_BZ_994905 = false;		  	
		  			  	
		  	RemoteRestRuntimeFactory restSessionFactory = new RemoteRestRuntimeFactory("com.sample:HelloDtoProcess:1.0.0", deploymentUrl, userId, password);
		    RuntimeEngine engine = restSessionFactory.newRuntimeEngine();      
		    KieSession ksession = engine.getKieSession();
		    		    
		    Map<String, Object> params = new HashMap<String, Object>();
		    HelloDto helloObj = new HelloDto();
		    helloObj.setGreeting("Hello");
		    helloObj.setRecipient("jBPM");
		    String dto = jaxbSerializationProvider.serialize(helloObj);
			params.put("helloDto1", dto);		    
		    ProcessInstance processIns = ksession.startProcess("com.sample.bpmn.hellodtoprocess", params);		
		    
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
