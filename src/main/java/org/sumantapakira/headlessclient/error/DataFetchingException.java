package org.sumantapakira.headlessclient.error;

public class DataFetchingException extends RuntimeException implements GraphQLError{

    String msg;
    
    public DataFetchingException(String msg) {
        this.msg = msg;
    }
    
    @Override
    public String getMessage() {
       return this.msg;
    }

    @Override
    public ErrorClassification getErrorType() {
       return ErrorType.DataFetchingException;
    }

}
