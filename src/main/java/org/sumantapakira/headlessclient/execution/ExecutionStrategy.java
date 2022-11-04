package org.sumantapakira.headlessclient.execution;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public interface ExecutionStrategy {

	public default  CompletableFuture<HttpResponse<String>> executeAsync(){
		return null;
	}
	public abstract ExecutionResult executeReactive();
}
