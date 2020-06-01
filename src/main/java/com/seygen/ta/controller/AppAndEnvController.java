package com.seygen.ta.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seygen.ta.domain.Application;
import com.seygen.ta.domain.ApplicationDetailsMapper;
import com.seygen.ta.domain.ApplicationEnv;
import com.seygen.smx.util.DataUtility;

@RestController
@RequestMapping(value = "/application")
public class AppAndEnvController {

	public static List<Application> appList = new ArrayList<>();
	public static HashMap<String, String> responseMap = new HashMap<>();

	static {
		try {
			String sysDir = System.getProperty("TA_HOME");
			String appData = DataUtility.getDatafromFile(sysDir + File.separator + "app_list.json");
			// System.out.println("appData " + appData);

			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(appData);
			JSONArray appJsonArray = (JSONArray) jsonObj.get("apps");
			for (Object obj : appJsonArray) {
				Application appVO = new Application((JSONObject) obj);
				appList.add(appVO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@CrossOrigin
	@GetMapping(value = "/appList")
	public static ResponseEntity<List<Application>> getAppList() {
		if (!appList.isEmpty()) {
			return new ResponseEntity<List<Application>>(appList, HttpStatus.OK);
		}
		return new ResponseEntity<List<Application>>(appList, HttpStatus.BAD_REQUEST);
	}

	@CrossOrigin
	@GetMapping(value = "/envList/{appId}")
	public static ResponseEntity<List<ApplicationEnv>> getEnvList(@PathVariable("appId") String appId) {

		ApplicationDetailsMapper apDetailMapper = new ApplicationDetailsMapper(appId);
		List<ApplicationEnv> envList = apDetailMapper.getEnvironmentList();
		// System.out.println("EnvList---"+envList);

		if (!envList.isEmpty()) {
			return new ResponseEntity<List<ApplicationEnv>>(envList, HttpStatus.OK);
		}
		return new ResponseEntity<List<ApplicationEnv>>(envList, HttpStatus.BAD_REQUEST);
	}

}
