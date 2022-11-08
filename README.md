# Async and Reactive Headless-Client

This client is used to query AEM and Magento in Async and reactive way to support the scalability.

Below example query only AEM

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

And below example query both AEM and Magento

	    public static void main(String[] args) {
        final String param = "Omega";
        final String aemQuery = "{\n"
                + "  rockstartHomePageModelList (variation: \"iot\", filter: {\n"
                + "    	title: {\n"
                + "       _expressions: [ {value: \"" + param + "\", _operator: EQUALS}]\n"
                + "      }\n"
                + "  	}) {\n"
                + "    items {\n"
                + "      pagecontent\n"
                + "     }\n"
                + "  }\n"
                + "}";

        final String magentoQuery = "{products(filter:{name:{match:\"" + param + "\"}})"
                + "{items {name price_range"
                + "{minimum_price"
                + "{regular_price"
                + "				{value currency}"
                + "}"
                + "}"
                + "}"
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
        CompletableFuture<ExecutionResult> response = context.executeStrategyAsync();

        response.thenAccept(pageResponse -> {
            System.out.println("AEM Content >> " + pageResponse.getData());
            HeadlessClient magentoHeadlessClient = new HeadlessClient.HeadlessClientBuilder()
                    .withEndpoint(MAGENTO_ENDPOINT)
                    .withQuery(magentoQuery)
                    .build();
            AsyncExecution<HeadlessClient> asyncExecution1 = new AsyncExecution<HeadlessClient>(magentoHeadlessClient);
            ExecutionContext magentoContext = new ExecutionContext(asyncExecution1);
            CompletableFuture<ExecutionResult> magentoResponse = magentoContext.executeStrategyAsync();
            magentoResponse.thenAccept(productData -> {
                try {
                    System.out.println("Magento data >> " + productData.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        });

        ReactiveExecution<HeadlessClient> reactiveExecution = new ReactiveExecution<HeadlessClient>(aemHeadlessClient);
        context = new ExecutionContext(reactiveExecution);
        context.executeStrategyReactive();

    }

# How to build

     mvn clean install
