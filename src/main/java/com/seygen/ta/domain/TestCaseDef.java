package com.seygen.ta.domain;

import org.json.simple.JSONObject;

public class TestCaseDef {

	public String appId;
	public String tcId;
	public String tcFileName;
	public String tcTemplateFileName;
	public String runner;

	public TestCaseDef() {
		super();
	}

	public TestCaseDef(JSONObject oneObj, String appName) {

		setAppId(appName);
		setTcId((String) ((JSONObject) oneObj).get("tcId"));
		setTcFileName((String) ((JSONObject) oneObj).get("tcName"));
		setTcTemplateFileName((String) ((JSONObject) oneObj).get("tcTemplate"));
		setRunner((String) ((JSONObject) oneObj).get("tcRunner"));
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTcId() {
		return tcId;
	}

	public void setTcId(String tcId) {
		this.tcId = tcId;
	}

	public String getTcFileName() {
		return tcFileName;
	}

	public void setTcFileName(String tcFileName) {
		this.tcFileName = tcFileName;
	}

	public String getTcTemplateFileName() {
		return tcTemplateFileName;
	}

	public void setTcTemplateFileName(String tcTemplateFileName) {
		this.tcTemplateFileName = tcTemplateFileName;
	}

	public String getRunner() {
		return runner;
	}

	public void setRunner(String runner) {
		this.runner = runner;
	}

}}