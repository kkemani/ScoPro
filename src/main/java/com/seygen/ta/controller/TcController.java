package com.seygen.ta.controller;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seygen.ta.domain.ApplicationDetailsMapper;
import com.seygen.ta.domain.TestCaseDef;
import com.seygen.ta.testengine.TARunnable;
import com.seygen.smx.util.DataUtility;

@RestController
@RequestMapping(value = "/tc")
public class TcController {

	public HashMap<Object, Object> respObj = new HashMap<>();
	public JSONParser parser = new JSONParser();
	public JSONObject jsonObj = null;
	public JSONArray jsonArray = null;

	@CrossOrigin
	@GetMapping(value = "/getTcList/{appId}")
	public ResponseEntity<List<TestCaseDef>> getTcList(@PathVariable("appId") String appId) {

		ApplicationDetailsMapper apDetailMapper = new ApplicationDetailsMapper(appId);
		List<TestCaseDef> tcList = apDetailMapper.getTestCaseList();
		// System.out.println("tcList---"+tcList);

		if (!tcList.isEmpty()) {
			return new ResponseEntity<List<TestCaseDef>>(tcList, HttpStatus.OK);
		}
		return new ResponseEntity<List<TestCaseDef>>(tcList, HttpStatus.BAD_REQUEST);
	}

	@CrossOrigin
	@PostMapping(value = "/runTestCase")
	public ResponseEntity<HashMap<Object, Object>> runTestCase(@RequestBody HashMap<Object, Object> reqObj) {

		String appId = (String) reqObj.get("appId");
		String envName = (String) reqObj.get("envName");

		String path = System.getProperty("TA_HOME") + File.separator + appId + File.separator;

		/*
		 * String xml = "<GWMessage>\r\n" + "	<header>\r\n" +
		 * "		<GWMessageId>20160107</GWMessageId>\r\n" +
		 * "		<GWMessageSchemaVersion>1.0</GWMessageSchemaVersion>\r\n" +
		 * "		<GWSource>OMSe8</GWSource>\r\n" +
		 * "		<GWDestination>NEU</GWDestination>\r\n" + "		<GWEchoBack>\r\n" +
		 * "			<GWEchoBackType>\r\n" +
		 * "				<RequestType>Order</RequestType>\r\n" +
		 * "			</GWEchoBackType>\r\n" + "		</GWEchoBack>\r\n" +
		 * "	</header>\r\n" + "	<body>\r\n" +
		 * "		<Request xmlns=\"https://dpom.rr.com/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"https://dpom.rr.com/XMLSchema http://dpom-texas.rr.com/XMLSchema/Order.xsd\">\r\n"
		 * + "			<Header>\r\n" + "				<SRC>TWC</SRC>\r\n" +
		 * "				<DEST>NEU</DEST>\r\n" +
		 * "				<XactionType>Order</XactionType>\r\n" +
		 * "			</Header>\r\n" + "			<NewConnection>\r\n" +
		 * "				<LSRFormData>\r\n" +
		 * "					<CCNA>MK1</CCNA>\r\n" +
		 * "					<PON>{PON}</PON>\r\n" +
		 * "					<VER>03</VER>\r\n" +
		 * "					<ATN>2622604056</ATN>\r\n" +
		 * "					<DTSent>2012-07-25T07:31:25-05:00</DTSent>\r\n" +
		 * "					<DDD>2011-03-07</DDD>\r\n" +
		 * "					<DDDTimeZone>US/Central</DDDTimeZone>\r\n" +
		 * "					<REQTYP>CB</REQTYP>\r\n" +
		 * "					<ACT>V</ACT>\r\n" + "					<MI>C</MI>\r\n"
		 * + "					<SUP>2</SUP>\r\n" +
		 * "					<EXP>0</EXP>\r\n" +
		 * "					<ONSP>3777</ONSP>\r\n" +
		 * "					<OLSP>3777</OLSP>\r\n" +
		 * "					<TOA>R</TOA>\r\n" +
		 * "					<TOS>2BFN</TOS>\r\n" +
		 * "					<BAN1>B27</BAN1>\r\n" +
		 * "					<INIT>Paul Miller</INIT>\r\n" +
		 * "					<TelNo>7047313447</TelNo>\r\n" +
		 * "					<LSRRemarks/>\r\n" + "				</LSRFormData>\r\n"
		 * + "			</NewConnection>\r\n" + "		</Request>\r\n" + "	</body>\r\n"
		 * + "</GWMessage>";
		 * 
		 * String myXmlEncodeString =
		 * Base64.getEncoder().encodeToString(xml.getBytes());
		 * System.out.println("myXmlEncodeString --  "+myXmlEncodeString);
		 * 
		 * // Get encoded string from json Object //String myXmlEncodeString = (String)
		 * reqObj.get("xmlData");
		 * 
		 * // Get bytes from string byte[] byteArray =
		 * Base64.getDecoder().decode(myXmlEncodeString); String decodedXmlString = new
		 * String(byteArray);
		 * System.out.println("decodedString ====== "+decodedXmlString);
		 */

		ArrayList<HashMap<String, String>> testCasesList = (ArrayList<HashMap<String, String>>) reqObj.get("testCase");

		System.out.println("appId -- " + appId + " envName -- " + envName);
		System.out.println("testCasesList -- " + testCasesList + " Size -- " + testCasesList.size());

		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
		String testRunId = String.valueOf(sd.format(new Date()));
		ArrayList<HashMap<String, String>> tcDataList = new ArrayList<HashMap<String, String>>();

		String pTestRunId = "P-" + testRunId;

		try {
			File pTestRunIdDir = new File(path + pTestRunId);
			System.out.println(pTestRunIdDir);
			System.out.println("Directory exists :" + pTestRunIdDir.exists() + pTestRunIdDir.isDirectory());
			if (!pTestRunIdDir.exists()) {
				boolean isDirCreated = pTestRunIdDir.mkdir();
				System.out.println(isDirCreated);
			}

			for (HashMap<String, String> obj : testCasesList) {

				HashMap<String, String> hMap = new HashMap<>();
				String tcId = obj.get("tcId");
				String cTestRunId = testRunId + "-" + tcId;

				System.out.println("tcId---" + tcId);
				hMap.put("tcId", tcId);
				hMap.put("pTestRunId", pTestRunId);
				hMap.put("cTestRunId", cTestRunId);

				try {
					File cTestRunIdDir = new File(path + pTestRunId + File.separator + cTestRunId);
					System.out.println(cTestRunIdDir);
					System.out.println("Directory exists :" + cTestRunIdDir.exists() + cTestRunIdDir.isDirectory());
					if (!cTestRunIdDir.exists()) {
						boolean isDirCreated = cTestRunIdDir.mkdir();
						System.out.println(isDirCreated);

						String fileName = pTestRunId + "_result.json";
						String childPath = path + pTestRunId + File.separator;
						String status = "Pending";
						generateParentResFile(childPath, fileName, cTestRunId, testRunId, status, envName);

						TARunnable taR = new TARunnable();
						taR.setTCDetails(appId, envName, tcId, pTestRunId, cTestRunId);
						Thread t = new Thread(taR);
						t.start();

						try {
							Thread.sleep(15000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				tcDataList.add(hMap);
			}

			String fileName = appId + "_result.json";
			genOrUpdateAppResultFile(path, fileName, pTestRunId, testRunId, envName);

		} catch (Exception e) {
			e.printStackTrace();
		}

		respObj.put("result", "Success");
		respObj.put("appId", appId);
		respObj.put("envName", envName);
		respObj.put("testCase", tcDataList);
		return new ResponseEntity<HashMap<Object, Object>>(respObj, HttpStatus.OK);
	}

	public void generateParentResFile(String path, String fileName, String cTestRunId, String stDate, String status,
			String envName) {

		try (FileWriter file = new FileWriter(path + fileName)) {

			JSONObject jsonObj = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject appResFileData = new JSONObject();

			jsonObj.put("cTCRunId", cTestRunId);
			jsonObj.put("envName", envName);
			jsonObj.put("startDate", stDate);
			jsonObj.put("status", status);
			jsonArray.add(jsonObj);
			appResFileData.put("result", jsonArray);
			file.write(appResFileData.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void genOrUpdateAppResultFile(String path, String fileName, String pTestRunId, String testRunId,
			String envName) {
		File appFileDir = new File(path + fileName);
		System.out.println("Directory exists :" + appFileDir.exists() + appFileDir.isDirectory());
		if (appFileDir.exists()) {
			String data = DataUtility.getDatafromFile(path + fileName);
			updateAppResFile(path, fileName, data, pTestRunId, testRunId, envName);
		} else {
			generateAppResFile(path, fileName, pTestRunId, testRunId, envName);
		}
	}

	public void updateAppResFile(String path, String fileName, String appResDataStr, String pTestRunId,
			String testRunId, String envName) {
		try (FileWriter file = new FileWriter(path + fileName)) {

			JSONObject jsonObj = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			HashMap<String, String> hMap = new HashMap<>();
			JSONObject appResFileData = new JSONObject();

			jsonObj = (JSONObject) parser.parse(appResDataStr);
			jsonArray = (JSONArray) jsonObj.get("result");
			hMap.put("parentId", pTestRunId);
			hMap.put("envName", envName);
			hMap.put("startDate", testRunId);
			jsonArray.add(hMap);
			appResFileData.put("result", jsonArray);
			file.write(appResFileData.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateAppResFile(String path, String fileName, String pTestRunId, String testRunId, String envName) {
		try (FileWriter file = new FileWriter(path + fileName)) {

			JSONObject jsonObj = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONObject appResFileData = new JSONObject();

			jsonObj.put("parentId", pTestRunId);
			jsonObj.put("envName", envName);
			jsonObj.put("startDate", testRunId);
			jsonArray.add(jsonObj);
			appResFileData.put("result", jsonArray);
			file.write(appResFileData.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
