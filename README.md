# Async and Reactive Headless-Client
Async and reactive support

Below example query only AEM

	public static void main(String[] args) {
		String param = "Rolex";
		String query = "{\n"
				+ "  rockstartHomePageModelList (variation: \"iot\", filter: {\n"
				+ "    	title: {\n"
				+ "       _expressions: [ {value: \"" +param+ "\", _operator: EQUALS}]\n"
				+ "      }\n"
				+ "  	}) {\n"
				+ "    items {\n"
				+ "      pagecontent\n"
				+ "      price\n"
				+ "     }\n"
				+ "  }\n"
				+ "}";
		
		HeadlessClient headlessClient = new HeadlessClient.HeadlessClientBuilder()
				.withEndpoint("http://localhost:4502/content/graphql/global/endpoint.json")
				.withBasicAuth("admin", "admin")
				.withQuery(query)
				.build();
		ExecutionContext context = null;
		
		AsyncExecution<HeadlessClient> asyncExecution = new AsyncExecution<HeadlessClient>(headlessClient);
		context = new ExecutionContext(asyncExecution);
		CompletableFuture<HttpResponse<String>> response = context.executeStrategyAsync();
		
		response.thenAccept(pageResponse -> {
	        String responseBody = pageResponse.body();
	        try {
	        	JsonNode json = new ObjectMapper().readTree(responseBody);
				System.out.println("text >> " +json.get("data").get("rockstartHomePageModelList").get("items"));
			}catch (IOException e) {
				e.printStackTrace();
			}
	    });
		
		ReactiveExecution<HeadlessClient> reactiveExecution = new ReactiveExecution<HeadlessClient>(headlessClient);
		 context = new ExecutionContext(reactiveExecution);
		 context.executeStrategyReactive();
	    

	}


And below example query both AEM and Magento


        import java.io.IOException;
	import java.net.http.HttpResponse;
	import java.util.concurrent.CompletableFuture;
	import org.sumantapakira.headlessclient.execution.AsyncExecution;
	import org.sumantapakira.headlessclient.execution.ExecutionContext;
	import org.sumantapakira.headlessclient.execution.ReactiveExecution;
	import org.sumantapakira.headlessclient.querybuilder.HeadlessClient;
	import com.fasterxml.jackson.databind.JsonNode;
	import com.fasterxml.jackson.databind.ObjectMapper;

	public class ClientQueryBothAEMMagento {
		final static String AEM_ENDPOINT = "http://localhost:4502/content/graphql/global/endpoint.json";
		final static String MAGENTO_ENDPOINT = "https://master-7rqtwti-qwpucsuh23jsc.us-4.magentosite.cloud/graphql";

		public static void main(String[] args) {
		final String param = "Rolex";
		final String aemQuery = "{\n"
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


		final String magentoQuery = "{products(filter:{name:{match:\""+param+"\"}})"
				+ 	"{items {name price_range"
				+ 		"{minimum_price"
				+ 			"{regular_price"
				+ "				{value currency}"
				+ 			"}"
				+ 		"}"
				+ 	"}"
				+ "}"
				+ "}";

		HeadlessClient aemHeadlessClient = new HeadlessClient.HeadlessClientBuilder()
				.withEndpoint(AEM_ENDPOINT)
				.withBasicAuth("admin", "admin")
				.withQuery(aemQuery)
				.build();
		ExecutionContext context = null;

		AsyncExecution<HeadlessClient> asyncExecution = new AsyncExecution<HeadlessClient>(aemHeadlessClient);
		context = new ExecutionContext(asyncExecution);
		CompletableFuture<HttpResponse<String>> response = context.executeStrategyAsync();

		response.thenAccept(pageResponse -> {
			String responseBody = pageResponse.body();
			try {
				JsonNode json = new ObjectMapper().readTree(responseBody);
				System.out.println("AEM Content >> " +json.get("data").get("rockstartHomePageModelList").get("items").get(0).get("pagecontent").asText());
				HeadlessClient magentoHeadlessClient = new HeadlessClient.HeadlessClientBuilder()
						.withEndpoint(MAGENTO_ENDPOINT)
						.withQuery(magentoQuery)
						.build();
				AsyncExecution<HeadlessClient> asyncExecution1 = new AsyncExecution<HeadlessClient>(magentoHeadlessClient);
				ExecutionContext magentoContext = new ExecutionContext(asyncExecution1);
				CompletableFuture<HttpResponse<String>> magentoResponse = magentoContext.executeStrategyAsync();
				magentoResponse.thenAccept(productData -> {
					String productDataBody = productData.body();
					try {
						JsonNode json1 = new ObjectMapper().readTree(productDataBody);
						System.out.println("Magento data >> "+json1.get("data").get("products"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}catch (IOException e) {
				e.printStackTrace();
			}
		});

		ReactiveExecution<HeadlessClient> reactiveExecution = new ReactiveExecution<HeadlessClient>(aemHeadlessClient);
		context = new ExecutionContext(reactiveExecution);
		context.executeStrategyReactive();


	}

}

