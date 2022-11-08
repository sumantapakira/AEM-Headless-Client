package org.sumantapakira.headlessclient.error;

public interface ErrorClassification {

    /**
     * This is called to create a representation of the error classification
     * that can be put into the `extensions` map of the graphql error under the key 'classification'
     * when {@link GraphQLError#toSpecification()} is called
     *
     * @param error the error associated with this classification
     *
     * @return an object representation of this error classification
     */
    @SuppressWarnings("unused")
    default Object toSpecification(GraphQLError error) {
        return String.valueOf(this);
    }
}
