package org.sumantapakira.headlessclient.error;

import java.io.Serializable;
import java.util.List;

public interface GraphQLError extends Serializable {

    /**
     * @return a description of the error intended for the developer as a guide to understand and correct the error
     */
    String getMessage();

  
    /**
     * @return an object classifying this error
     */
    ErrorClassification getErrorType();

    default List<Object> getPath() {
        return null;
    }

  

}
