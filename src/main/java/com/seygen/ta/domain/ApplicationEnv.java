package com.seygen.ta.domain;

import java.util.Hashtable;

import org.json.simple.JSONObject;

public class ApplicationEnv {

	public String envName;
	protected Hashtable<String, String> envProperties;

	public ApplicationEnv() {
		envProperties = new Hashtable<>();
	}

	public ApplicationEnv(JSONObject oneObj, String envName) {
		setEnvName(envName);
		String CONTENT_TYPE = (String) ((JSONObject) oneObj).get("CONTENT_TYPE");
		String SMX_BASE_URL = (String) ((JSONObject) oneObj).get("SMX_BASE_URL");
		String DEST_URL = (String) ((JSONObject) oneObj).get("DEST_URL");

		Hashtable<String, String> envProps = new Hashtable<>();
		envProps.put("CONTENT_TYPE", CONTENT_TYPE);
		envProps.put("SMX_BASE_URL", SMX_BASE_URL);
		envProps.put("DEST_URL", DEST_URL);

		setEnvProperties(envProps);
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public Hashtable<String, String> getEnvProperties() {
		return envProperties;
	}

	public void setEnvProperties(Hashtable<String, String> envProperties) {
		this.envProperties = envProperties;
	}
}
