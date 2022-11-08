package org.sumantapakira.headlessclient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.sumantapakira.headlessclient.execution.AsyncExecution;
import org.sumantapakira.headlessclient.execution.ExecutionContext;
import org.sumantapakira.headlessclient.execution.ExecutionResult;
import org.sumantapakira.headlessclient.execution.ReactiveExecution;
import org.sumantapakira.headlessclient.querybuilder.HeadlessClient;

public class ClientQueryOnlyAEM {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		String param = "Rolex";
		String query = "{\n"
				+ "  rockstartHomePageModelList (variation: \"iot\", filter: {\n"
				+ "    	title: {\n"
				+ "       _expressions: [ {value: \"" +param+ "\", _operator: EQUALS}]\n"
				+ "      }\n"
				+ "  	}) {\n"
				+ "    items {\n"
				+ "      pagecontent\n"
				+ "     }\n"
				+ "  }\n"
				+ "}";
		
		HeadlessClient headlessClient = new HeadlessClient.HeadlessClientBuilder()
				.withEndpoint("http://localhost:4502/content/graphql/global/endpoint.json")
				.withBasicAuth("admin", "admin")
				.withQuery(query)
				.build();
		AsyncExecution<HeadlessClient> asyncExecution = new AsyncExecution<HeadlessClient>(headlessClient);
		ExecutionContext context = new ExecutionContext(asyncExecution);
		CompletableFuture<ExecutionResult> response = context.executeStrategyAsync();
			
		response.whenComplete((executionResult, ex)->{
		    if (ex != null) {
		        System.out.println("Error occurred");
		        ex.printStackTrace();
		      } else {
		        System.out.println("Response :: " + executionResult.getData());
		      }
		});
		
		ReactiveExecution<HeadlessClient> reactiveExecution = new ReactiveExecution<HeadlessClient>(headlessClient);
		 context = new ExecutionContext(reactiveExecution);
		 context.executeStrategyReactive();
	}

}
