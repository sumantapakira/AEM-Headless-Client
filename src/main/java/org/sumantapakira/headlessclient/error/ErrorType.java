package org.sumantapakira.headlessclient.error;

public enum ErrorType implements ErrorClassification {
    InvalidSyntax,
    ValidationError,
    DataFetchingException,
    NullValueInNonNullableField,
    OperationNotSupported,
    ExecutionAborted
}