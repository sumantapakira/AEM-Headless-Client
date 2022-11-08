package org.sumantapakira.headlessclient.execution;

import org.sumantapakira.headlessclient.error.DataFetchingException;

import com.fasterxml.jackson.databind.JsonNode;

public class ExecutionResult {
    private final JsonNode response;
    private static final String DATA = "data";
    private static final String ERROR = "errors";
    private static final String MESSAGE = "message";
    private DataFetchingException dataFetchingException;

    public ExecutionResult(JsonNode response) {
        this.response = response;
        if (response.has(ERROR)) {
            dataFetchingException = new DataFetchingException(getErrorMessage());
        }
    }

    public boolean hasData() {
        return response.has(DATA) ? Boolean.TRUE : Boolean.FALSE;
    }

    public JsonNode getData() {
        return response.get(DATA);
    }

    public boolean hasError() {
        return response.has(ERROR) ? Boolean.TRUE : Boolean.FALSE;
    }

    public JsonNode getError() {
        return response.get(ERROR);
    }

    public String getErrorMessage() {
        return response.get(ERROR).findValue(MESSAGE).asText();
    }

    public DataFetchingException getGraphQLError() {
        return this.dataFetchingException;
    }

}
