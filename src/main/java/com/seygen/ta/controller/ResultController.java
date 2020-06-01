package com.seygen.ta.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seygen.smx.util.DataUtility;

@RestController
@RequestMapping(value = "/result")
public class ResultController {

	public HashMap<Object, Object> respObj = new HashMap<>();
	public JSONParser parser = new JSONParser();
	public JSONObject jsonObj = null;
	public JSONArray jsonArray = null;

	@CrossOrigin
	@PostMapping(value = "/getAppResult")
	public ResponseEntity<HashMap<Object, Object>> getAppResult(@RequestBody HashMap<Object, Object> reqObj)
			throws ParseException {

		ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

		String appId = (String) reqObj.get("appId");
		String stDate = (String) reqObj.get("stDate");
		String endDate = (String) reqObj.get("endDate");

		String path = System.getProperty("TA_HOME") + File.separator + appId + File.separator;
		String fileName = appId + "_result.json";
		String appResultData = DataUtility.getDatafromFile(path + fileName);

		if (!appResultData.isEmpty()) {
			jsonObj = (JSONObject) parser.parse(appResultData);
			jsonArray = (JSONArray) jsonObj.get("result");
			respObj.put("response", "success");
			respObj.put("data", jsonArray);
			return new ResponseEntity<HashMap<Object, Object>>(respObj, HttpStatus.OK);
		}

		respObj.put("response", "Failed with exception");
		respObj.put("errorMessage", "Data File is empty or not found");
		respObj.put("data", dataList);
		return new ResponseEntity<HashMap<Object, Object>>(respObj, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@CrossOrigin
	@RequestMapping(value = "/getParentResult/{appId}/{pId}")
	public ResponseEntity<HashMap<Object, Object>> getParentResult(@PathVariable("appId") String appId,
			@PathVariable("pId") String pId) throws ParseException {

		ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		String path = System.getProperty("TA_HOME") + File.separator + appId + File.separator + pId + File.separator;

		String fileName = pId + "_result.json";
		String parentResultData = DataUtility.getDatafromFile(path + fileName);

		if (!parentResultData.isEmpty()) {
			jsonObj = (JSONObject) parser.parse(parentResultData);
			jsonArray = (JSONArray) jsonObj.get("result");

			/*for (Object oneObj : jsonArray) {
				HashMap<String, String> hMap = new HashMap<>();
				String cId = (String) ((JSONObject) oneObj).get("cTCRunId");
				String startDate = (String) ((JSONObject) oneObj).get("startDate");
				String status = (String) ((JSONObject) oneObj).get("status");
				hMap.put("cId", cId);
				hMap.put("startDate", startDate);
				hMap.put("status", status);
				dataList.add(hMap);
			}*/

			respObj.put("response", "success");
			respObj.put("data", jsonArray);
			return new ResponseEntity<HashMap<Object, Object>>(respObj, HttpStatus.OK);
		}

		respObj.put("response", "Failed with exception");
		respObj.put("errorMessage", "Data File is empty or not found");
		respObj.put("data", dataList);
		return new ResponseEntity<HashMap<Object, Object>>(respObj, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@CrossOrigin
	@RequestMapping(value = "/getChildResult/{appId}/{pId}/{cId}")
	public ResponseEntity<HashMap<Object, Object>> getChildResult(@PathVariable("appId") String appId,
			@PathVariable("pId") String pId, @PathVariable("cId") String cId) throws ParseException {

		String path = System.getProperty("TA_HOME") + File.separator + appId + File.separator + pId + File.separator
				+ cId + File.separator;

		String fileName = cId + "_result.json";
		String childResultData = DataUtility.getDatafromFile(path + fileName);

		if (!childResultData.isEmpty()) {
			jsonObj = (JSONObject) parser.parse(childResultData);
			respObj.put("response", "success");
			respObj.put("data", jsonObj);
			return new ResponseEntity<HashMap<Object, Object>>(respObj, HttpStatus.OK);
		}

		respObj.put("response", "Failed with exception");
		respObj.put("errorMessage", "Data File is empty or not found");
		respObj.put("data", jsonObj);
		return new ResponseEntity<HashMap<Object, Object>>(respObj, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
