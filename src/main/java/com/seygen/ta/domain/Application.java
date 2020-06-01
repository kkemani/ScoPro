package com.seygen.ta.domain;

import org.json.simple.JSONObject;

public class Application {
	public String appId;
	public String appDescription;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	public String toString() {
		return "Application [appId=" + getAppId() + ", appDescription=" + getAppDescription() + "]";
	}

	public Application(JSONObject jsonObj) {
		setAppId((String) ((JSONObject) jsonObj).get("AppId"));
		setAppDescription((String) ((JSONObject) jsonObj).get("AppDesc"));
	}
}
