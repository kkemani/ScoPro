package com.seygen.ta.domain;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.seygen.smx.util.DataUtility;

public class ApplicationDetailsMapper {

	protected String appName;
	protected static Hashtable<String, ApplicationEnv> appEnvMapper;
	protected static Hashtable<String, TestCaseDef> appTestCaseMapper;
	public static String TA_HOME = System.getProperty("TA_HOME");
	protected String appHome = null;

	public ApplicationDetailsMapper() {
		init();
	}

	public ApplicationDetailsMapper(String appName) {
		this.appName = appName;
		init();
		appHome = TA_HOME + File.separator + this.appName;
		loadEnvDetails();
		loadTCDetails();

	}

	public synchronized void init() {
		if (appEnvMapper == null)
			appEnvMapper = new Hashtable<>();
		if (appTestCaseMapper == null)
			appTestCaseMapper = new Hashtable<>();

	}

	private void loadTCDetails() {
		try {
			String tcfileName = appHome + File.separator + "tc_list.json";
			String tcData = DataUtility.getDatafromFile(tcfileName);
			// read the json file and for each Test Case create a Test Case Def
			// add to the appTestCaseMapper with tcId as key and testCaseDef object as value

			// System.out.println("tcData " + tcData);
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(tcData);
			JSONArray tcJsonArray = (JSONArray) jsonObj.get("tc_list");

			for (Object oneObj : tcJsonArray) {
				// System.out.println("oneObj " + oneObj);
				TestCaseDef tcDefObj = new TestCaseDef((JSONObject) oneObj, appName);
				appTestCaseMapper.put(tcDefObj.getTcId(), tcDefObj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ApplicationEnv getApplicationEnv(String envName) {
		return this.appEnvMapper.get(envName);
	}

	public TestCaseDef getTestCaseDef(String tcName) {
		return this.appTestCaseMapper.get(tcName);
	}

	private void loadEnvDetails() {
		try {
			String envfileName = appHome + File.separator + "env_list.json";
			String envData = DataUtility.getDatafromFile(envfileName);
			// read the json file and for each Env create a ApplicationEnv object
			// add to the appEnvMapper with envName as key and ApplicationEnv as value

			// System.out.println("envData " + envData);
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(envData);

			JSONArray envNameList = (JSONArray) jsonObj.get("envList");
			JSONArray envObjJsonArray = (JSONArray) jsonObj.get("environments");

			for (Object oneObj : envObjJsonArray) {
				// System.out.println("oneObj " + oneObj);

				for (Object envNameListObj : envNameList) {
					String envName = (String) ((JSONObject) envNameListObj).get("name");
					JSONObject envObj = (JSONObject) ((JSONObject) oneObj).get(envName);
					System.out.println("envObj--" + envObj);

					if (envObj != null) {
						ApplicationEnv appEnv = new ApplicationEnv(envObj, envName);
						appEnvMapper.put(envName, appEnv);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ApplicationEnv> getEnvironmentList() {
		return appEnvMapper.values().stream().collect(Collectors.toList());
	}

	public List<TestCaseDef> getTestCaseList() {
		return appTestCaseMapper.values().stream().collect(Collectors.toList());
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
		appHome = TA_HOME + File.separatorChar + this.appHome;
		loadEnvDetails();
		loadTCDetails();
	}
}
