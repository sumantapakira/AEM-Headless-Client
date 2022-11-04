package org.sumantapakira.headlessclient.execution;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ExecutionContext {

	ExecutionStrategy executionStrategy;
	
	
	public ExecutionContext(ExecutionStrategy t) {
		this.executionStrategy=t;
	}
	
	public CompletableFuture<HttpResponse<String>> executeStrategyAsync() {
		return executionStrategy.executeAsync();
	}
	
	public void executeStrategyReactive() {
		executionStrategy.executeReactive();
	}
	
}
