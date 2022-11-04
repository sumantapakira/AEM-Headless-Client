# AEM-Headless-Client
Async and reactive support


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

