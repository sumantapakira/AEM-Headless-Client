package org.sumantapakira.headlessclient.execution;

import java.util.concurrent.CompletableFuture;

public class ExecutionContext {

	ExecutionStrategy executionStrategy;
	
	
	public ExecutionContext(ExecutionStrategy t) {
		this.executionStrategy=t;
	}
	
	public CompletableFuture<ExecutionResult> executeStrategyAsync() {
		return executionStrategy.executeAsync();
	}
	
	public void executeStrategyReactive() {
		executionStrategy.executeReactive();
	}
	
}
