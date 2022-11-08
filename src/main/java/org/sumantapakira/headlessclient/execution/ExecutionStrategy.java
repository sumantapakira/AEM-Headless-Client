package org.sumantapakira.headlessclient.execution;

import java.util.concurrent.CompletableFuture;

public interface ExecutionStrategy {

	public default  CompletableFuture<ExecutionResult> executeAsync(){
		return null;
	}
	public abstract ExecutionResult executeReactive();
}
