package com.seygen.ta.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seygen.ta.domain.UserVO;
import com.seygen.smx.util.DataUtility;

@RestController
@RequestMapping(value = "/login")
public class LoginController {

	public static List<UserVO> userList = new ArrayList<>();
	public static HashMap<String, String> responseMap = new HashMap<>();

	static {
		try {
			String sysDir = System.getProperty("TA_HOME");
			String userData = DataUtility.getDatafromFile(sysDir + File.separator + "user_list.json");
			// System.out.println("userData == "+userData);

			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(userData);
			JSONArray userJsonArray = (JSONArray) jsonObj.get("users");
			for (Object obj : userJsonArray) {
				UserVO userVO = new UserVO((JSONObject) obj);
				userList.add(userVO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@CrossOrigin
	@GetMapping(value = "/test")
	public static String test() {
		return "ashish login";
	}

	@CrossOrigin
	@PostMapping(value = "/loginPost")
	public static ResponseEntity<HashMap<String, String>> doLoginPost(@RequestBody Map<String, String> reqMap) {
		String userId = reqMap.get("userId");
		String password = reqMap.get("password");
		try {
			if (!userList.isEmpty()) {
				for (UserVO userVO : userList) {
					System.out.println("doLoginPost :: inside try " + userId.equals(userVO.getUserId()));
					if (userId.equals(userVO.getUserId()) && password.equals(userVO.getPassword())) {
						responseMap.put("response", "success");
						return new ResponseEntity<HashMap<String, String>>(responseMap, HttpStatus.OK);
					}
				}
			}
		} catch (Exception e) {
			responseMap.put("response", "Failed with exception");
			return new ResponseEntity<HashMap<String, String>>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		responseMap.put("response", "Bad Credentials");
		return new ResponseEntity<HashMap<String, String>>(responseMap, HttpStatus.BAD_REQUEST);
	}

}
