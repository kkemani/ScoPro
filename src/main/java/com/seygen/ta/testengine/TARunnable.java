package com.seygen.ta.testengine;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Hashtable;

import com.seygen.smx.util.DataUtility;
import com.seygen.ta.domain.ApplicationDetailsMapper;
import com.seygen.ta.domain.ApplicationEnv;
import com.seygen.ta.domain.TestCaseDef;
import com.seygen.testmanagement.TCRunnerAndValidator;

public class TARunnable implements Runnable {

	public String appName;
	public String envName;
	public String tcId;
	public String pTestRunId;
	public String cTestRunId;
	public String runnerClass;
	public TestCaseDef testCaseDef;
	Hashtable<String, String> envProperties = null;
	ApplicationDetailsMapper apDetailMapper = null;
	protected String appHome = null;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
		appHome = ApplicationDetailsMapper.TA_HOME + File.separator + appName;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {

		this.envName = envName;
		ApplicationEnv env = apDetailMapper.getApplicationEnv(this.envName);
		this.envProperties = env.getEnvProperties();
	}

	public String getTcId() {
		return tcId;
	}

	public void setTcId(String tcId) {
		this.tcId = tcId;
		this.testCaseDef = apDetailMapper.getTestCaseDef(this.tcId);
	}

	public TARunnable() {
		envProperties = new Hashtable<>();
		//apDetailMapper = new ApplicationDetailsMapper(this.appName);
		// TODO Auto-generated constructor stub
	}

	public void setTCDetails(String appName, String envName, String tcId, String pTestRunId, String cTestRunId) {
		setAppName(appName);
		apDetailMapper = new ApplicationDetailsMapper(this.appName);
		setEnvName(envName);
		setTcId(tcId);
		this.pTestRunId = pTestRunId;
		this.cTestRunId = cTestRunId;
	}

	@Override
	public void run() {

		String runnerClass = this.testCaseDef.getRunner();
		String TC_DIR = appHome + File.separator + getTcId() ;
		
		System.out.println("TC_DIR---"+TC_DIR);
		
		String tcFileName = appHome + File.separator + getTcId() + File.separator + this.testCaseDef.tcTemplateFileName;
		// using reflection --- construct this class …
		String tcFileData = DataUtility.getDatafromFile(tcFileName);
		// SMXTestCaseDef smxTCDef = new SMXTestCaseDef(tcFileData);
		
		System.out.println("runnerClass --- "+runnerClass);
				
		try {
			Class clazz = Class.forName(runnerClass);
			Class[] classArgs = new Class[3];
			classArgs[0] = appName.getClass();
			classArgs[1] = pTestRunId.getClass();
			classArgs[2] = cTestRunId.getClass();

			Constructor apConstructor = clazz.getConstructor(classArgs);
			//Constructor apConstructor = clazz.getConstructor();

			Object[] args = new Object[3];
			args[0] = appName;
			args[1] = pTestRunId;
			args[2] = cTestRunId;

			Object objClazz = apConstructor.newInstance(args);
			//Object objClazz = apConstructor.newInstance();
			System.out.println("objClazz----- "+objClazz);
			
			TCRunnerAndValidator tcRunnerValidator = (TCRunnerAndValidator) objClazz;
			this.envProperties.put("TC_DIR", TC_DIR);
			this.envProperties.put("APP_DIR", this.appHome);
			tcRunnerValidator.setProperties(this.envProperties);
			
			String fileLocation = tcFileName;
			com.seygen.testmanagement.TestCaseDef tcDef = tcRunnerValidator.getTestCaseDefinition(TC_DIR, this.tcId,
					fileLocation);
			System.out.println("before result -- ");
			int result = tcRunnerValidator.runTestCase(tcDef);
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}

			tcRunnerValidator.buildTestContext();
			tcRunnerValidator.validateTC(tcDef);
		} catch (ClassNotFoundException exClassNotFound) {
			System.out.println(" Here...2 " + exClassNotFound.getMessage());
			exClassNotFound.printStackTrace();
		} catch (NoSuchMethodException methodNotFound) {
			System.out.println(" Here...3 " + methodNotFound.getMessage());
			methodNotFound.printStackTrace();
		} catch (java.lang.reflect.InvocationTargetException invocTgtExcep) {
			System.out.println(" Here...4 " + invocTgtExcep.getMessage());
			invocTgtExcep.printStackTrace();
		} catch (InstantiationException exInstantiation) {
			System.out.println(" Here...5 " + exInstantiation.getMessage());
			exInstantiation.printStackTrace();
		} catch (IllegalAccessException exIllegalAccess) {
			System.out.println(" Here...6 " + exIllegalAccess.getMessage());
			exIllegalAccess.printStackTrace();
		}
	}
	
/*	public static void main(String[] args) {
		
		String parent_dir = "C:\\Users\\Ashish\\TA_HOME";
		String LogLocation = "C:\\Users\\Ashish\\TA_HOME\\TEST_BM\\Logs";
		System.setProperty("TA_HOME", parent_dir);
		System.setProperty("LogLocation", LogLocation);
		
		TARunnable taR = new TARunnable(); 
		taR.setTCDetails("TEST_BM", "UAT", "TC01", "P-20200527061715", "20200527061715-TC01");
		Thread t = new Thread(taR);
		t.start();
        
    }*/
	
}
