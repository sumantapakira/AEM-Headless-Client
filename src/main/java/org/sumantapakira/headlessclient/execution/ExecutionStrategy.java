package org.sumantapakira.headlessclient.execution;

import java.util.concurrent.CompletableFuture;

public interface ExecutionStrategy {

   /**
	 * The default service which invokes asynchronous calls
	 * @return either error or data which you can use in the future. 
	 */
	public default  CompletableFuture<ExecutionResult> executeAsync(){
		return null;
	}
	/** Invokes the reactive way. 
	 * @return either error or data as per GraphQl spec
	 */
	public abstract ExecutionResult executeReactive();
}
