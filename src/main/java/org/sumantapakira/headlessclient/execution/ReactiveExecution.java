package org.sumantapakira.headlessclient.execution;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.reactive.client.ReactiveRequest;
import org.eclipse.jetty.reactive.client.ReactiveResponse;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.sumantapakira.headlessclient.querybuilder.HeadlessClient;

import io.reactivex.Single;

public class ReactiveExecution<T> implements ExecutionStrategy {

	T t;
	HeadlessClient headlessClient = null;

	public ReactiveExecution(T t) {
		this.t = t;
	}

	@Override
	public ExecutionResult executeReactive() {

		if (t instanceof HeadlessClient) {
			headlessClient = (HeadlessClient) t;
		}
		SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
		HttpClient httpClient = new HttpClient(sslContextFactory);
		try {
			httpClient.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Request request = httpClient.POST(headlessClient.getEndpoint());
		request.header(HttpHeader.AUTHORIZATION, headlessClient.getBasicAuthHeaderVal());
		request.header(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		ReactiveRequest reactiveRequest = ReactiveRequest.newBuilder(request)
				.content(
						ReactiveRequest.Content.fromString(AsyncExecution.prepareQuery(headlessClient.getQuery(), null),
								MediaType.APPLICATION_JSON_VALUE, UTF_8))
				.build();
		Publisher<String> publisher = reactiveRequest.response(ReactiveResponse.Content.asString());

		String responseContent = Single.fromPublisher(publisher).blockingGet();
		System.out.println(responseContent);

		/*
		 * Comment out if you want to Print the events Publisher<ReactiveRequest.Event>
		 * requestEvents = reactiveRequest.requestEvents();
		 * Publisher<ReactiveResponse.Event> responseEvents =
		 * reactiveRequest.responseEvents();
		 * 
		 * List<Type> requestEventTypes = new ArrayList<>();
		 * List<ReactiveResponse.Event.Type> responseEventTypes = new ArrayList<>();
		 * 
		 * Flowable.fromPublisher(requestEvents) .map(ReactiveRequest.Event::getType)
		 * .subscribe(requestEventTypes::add);
		 * 
		 * Flowable.fromPublisher(responseEvents) .map(ReactiveResponse.Event::getType)
		 * .subscribe(responseEventTypes::add);
		 * 
		 * Single<ReactiveResponse> response =
		 * Single.fromPublisher(reactiveRequest.response());
		 * 
		 * System.out.println(requestEventTypes.size());
		 */

		return null;
	}

}
