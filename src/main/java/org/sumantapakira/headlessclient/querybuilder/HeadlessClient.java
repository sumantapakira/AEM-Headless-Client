package org.sumantapakira.headlessclient.querybuilder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HeadlessClient {

	final String endpoint;
	final String userName;
	final String password;
	final String query;

	public HeadlessClient(HeadlessClientBuilder headlessClientBuilder) {
		this.endpoint = headlessClientBuilder.endpoint;
		this.query = headlessClientBuilder.query;
		this.userName = headlessClientBuilder.userName;
		this.password = headlessClientBuilder.password;
	}

	public String getEndpoint() {
		return this.endpoint;
	}

	public String getQuery() {
		return this.query;
	}

	public String getBasicAuthHeaderVal() {
		try {
			return "Basic " + Base64.getEncoder()
					.encodeToString((this.userName + ":" + this.password).getBytes(StandardCharsets.ISO_8859_1.name()));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Charset StandardCharsets.ISO_8859_1 unexpectedly does not exist", e);
		}
	}

	@Override
	public String toString() {
		return "HeadlessClient [endpoint=" + endpoint + ", userName=" + userName + ", password=" + password + "]";
	}

	public static class HeadlessClientBuilder {
		String endpoint;
		String userName;
		String password;
		String query;

		public HeadlessClientBuilder withEndpoint(String endpoint) {
			this.endpoint = endpoint;
			return this;
		}

		public HeadlessClientBuilder withBasicAuth(String userName, String password) {
			this.userName = userName;
			this.password = password;
			return this;
		}

		public HeadlessClientBuilder withQuery(String query) {
			this.query = query;
			return this;
		}

		public HeadlessClient build() {
			return new HeadlessClient(this);
		}

	}
}
