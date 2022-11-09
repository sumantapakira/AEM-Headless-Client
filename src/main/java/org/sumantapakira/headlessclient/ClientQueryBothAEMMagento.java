package org.sumantapakira.headlessclient;

import java.util.concurrent.CompletableFuture;

import org.sumantapakira.headlessclient.execution.AsyncExecution;
import org.sumantapakira.headlessclient.execution.ExecutionContext;
import org.sumantapakira.headlessclient.execution.ExecutionResult;
import org.sumantapakira.headlessclient.execution.ReactiveExecution;
import org.sumantapakira.headlessclient.querybuilder.HeadlessClient;

public class ClientQueryBothAEMMagento {
    final static String AEM_ENDPOINT = "http://localhost:4502/content/graphql/global/endpoint.json";
    final static String MAGENTO_ENDPOINT = "https://master-7rqtwti-qwpucsuh23jsc.us-4.magentosite.cloud/graphql";

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

}
