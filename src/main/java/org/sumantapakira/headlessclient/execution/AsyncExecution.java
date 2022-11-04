package org.sumantapakira.headlessclient.execution;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jetty.http.HttpHeader;
import org.springframework.http.MediaType;
import org.sumantapakira.headlessclient.querybuilder.HeadlessClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


public  class AsyncExecution<T> implements ExecutionStrategy{
	static final String AUTH_BASIC = "Basic";
	static final String SPACE = " ";
	static final String SLASH = "/";
	static final String METHOD_POST = "POST";
	static final String KEY_QUERY = "query";
	static final String KEY_VARIABLES = "variables";
	
	ExecutorService executorService = Executors.newFixedThreadPool(2);
	
	T t;
	public AsyncExecution(T t) {
		this.t=t;
	}
	
	
	public static String prepareQuery(String query, Map<String, Object> variables) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode queryNode = mapper.createObjectNode();
		queryNode.put(KEY_QUERY, query);
		if (variables != null) {
			queryNode.set(KEY_VARIABLES, mapper.valueToTree(variables));
		}
		return queryNode.toString();
	}
	
	
	@Override
	public CompletableFuture<HttpResponse<String>> executeAsync() {
		HttpRequest request;
		HeadlessClient headlessClient = null;
		CompletableFuture<HttpResponse<String>> response = null;
		if(t instanceof HeadlessClient) {
			headlessClient = (HeadlessClient) t;
		}
		try {
			String queryStr = prepareQuery(headlessClient.getQuery(), null);

			request = HttpRequest.newBuilder()
			  .uri(new URI(headlessClient.getEndpoint()))
			  .header(HttpHeader.CONTENT_TYPE.asString(), MediaType.APPLICATION_JSON_VALUE)
			  .header(HttpHeader.AUTHORIZATION.asString(), headlessClient.getBasicAuthHeaderVal())
			  .POST(HttpRequest.BodyPublishers.ofByteArray(queryStr.getBytes()))
			  .build();
			
			response = HttpClient.newBuilder()
					  .executor(executorService)
					  .build()
					  .sendAsync(request, HttpResponse.BodyHandlers.ofString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	return response;
	}

	
	
	@Override
	public ExecutionResult executeReactive() {
		throw new UnsupportedOperationException();
	}

}
