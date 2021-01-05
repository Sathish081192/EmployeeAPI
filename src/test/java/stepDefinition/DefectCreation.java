package stepDefinition;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;


import com.cucumber.listener.Reporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import pojo.Assignee;
import pojo.Fields;
import pojo.FieldsParent;
import pojo.IssueType;
import pojo.Priority;
import pojo.Project;
import responsePOJO.CookieResponse;
import responsePOJO.DefectResponse;
import responsePOJO.LoginInfo;
import responsePOJO.Session;
import utils.CommonActions;

public class DefectCreation {
	
	public String inputJSON;
	public String endPoint;
	public Response responseObj;
	public String jSessionID;
	String defectID;
	CommonActions common;
	Fields fields;
	Assignee assigneeObj;
	IssueType issuetypeObj;
	Priority priorityObj;
	Project projectObj;
	FieldsParent fieldsParentObj;
	Gson gson;
	
	@Before
	public void beforeScenario() {
		common=new CommonActions();
		fields=new Fields();
		assigneeObj=new Assignee();
		issuetypeObj=new IssueType();
		priorityObj=new Priority();
		projectObj=new Project();
		fieldsParentObj=new FieldsParent();
		gson=new GsonBuilder().create();
	}
	
	@After
	public void afterScenario(Scenario scenario) {
		//if (scenario.isFailed()) {
			//File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			//String screenshotpath="target/screenshots/"+scenario.getName()+"_"+comActions.getcurrentdateandtime()+".png";
			//File destination=new File(screenshotpath);
			//FileUtils.copyFile(src, destination);
			//Reporter.addScreenCaptureFromPath(destination.toString());
		//}
		Reporter.setSystemInfo("User Name", "Sathish Rajendran");
		Reporter.setSystemInfo("Application Name", "JIRA - Test Management");
		Reporter.setSystemInfo("Operating System Type", System.getProperty("os.name").toString());
		Reporter.setSystemInfo("Java Version", System.getProperty("java.version"));
		Reporter.setSystemInfo("Environment", "Production");
	}
	
	
	@Given("^User creates the JSON for Cookie Authentication$")
	public void user_creates_the_JSON_for_Cookie_Authentication() throws Throwable {
		inputJSON=common.createJSONForCookie();
		Reporter.addStepLog("Cookie Request JSON: "+inputJSON);
	}

	@When("^User hits Post Request for the Cookie Authentication with created JSON$")
	public void user_hits_Post_Request_for_the_Cookie_Authentication_with_created_JSON() throws Throwable {
		endPoint=common.readvalueFromProperty("cookie_Endpoint");
		Reporter.addStepLog("Cookie EndPoint: "+endPoint);
		responseObj=common.postMethodforCookie(endPoint, inputJSON);
	}

	@Then("^JSESSIONID should be generated Successfully as Output$")
	public void jsessionid_should_be_generated_Successfully_as_Output() throws Throwable {
	   String responsebody=responseObj.getBody().asString();
	   Assert.assertEquals(200, responseObj.getStatusCode());
	   Reporter.addStepLog("Cookie Response Body: "+responsebody);
	   JSONParser parser=new JSONParser();
	   JSONObject responsejson=(JSONObject) parser.parse(responsebody);
	   JSONObject session=(JSONObject) responsejson.get("session");
	   jSessionID=(String) session.get("value");
	}
	
	@Given("^User Create Input JSON for Defect Creation (.*?) and (.*?) and (.*?) and (.*?) and (.*?) and (.*?)$")
	public void user_Create_Input_JSON_for_Defect_Creation(String summary, String issuetype, String description, String project, String assignee, String priority) throws Throwable {
	   issuetypeObj.setName(issuetype);
	   projectObj.setKey(project);
	   assigneeObj.setName(assignee);
	   priorityObj.setName(priority);
	   fields.setSummary(summary);
	   fields.setIssuetype(issuetypeObj);
	   fields.setDescription(description);
	   fields.setProject(projectObj);
	   fields.setAssignee(assigneeObj);
	   fields.setPriority(priorityObj);
	   fieldsParentObj.setFields(fields);
	   
	   ObjectMapper mapper=new ObjectMapper();
	   inputJSON=mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fieldsParentObj);	
	   Reporter.addStepLog("Create Defect Request JSON: "+inputJSON);
	}

	@When("^User hits Post Request for Defect Creation with created JSON$")
	public void PostRequest_createDefect() throws Throwable {
		endPoint=common.readvalueFromProperty("createIssue_Endpoint");
		Reporter.addStepLog("Create Defect EndPoint: "+endPoint);
		responseObj=common.postMethod(endPoint, inputJSON, jSessionID);		
	}

	@Then("^New DefectID should be generated Successfully$")
	public void new_DefectID_should_be_generated_Successfully() throws Throwable {
		String responsebody=responseObj.getBody().asString();
		Reporter.addStepLog("Create Defect Response JSON: "+responsebody);
		   Assert.assertEquals(201, responseObj.getStatusCode());
		   //Reporter.addStepLog("Create Defect Response Body: "+responsebody);
		   JSONParser parser=new JSONParser();
		   JSONObject responsejson=(JSONObject) parser.parse(responsebody);
		   String defectID=(String) responsejson.get("key");
		   Reporter.addStepLog("Newly Created Defect ID: "+defectID);
	}
	
	@Then("^Cookie Response needs to be validated and JSESSION to be Retrieved$")
	public void cookieResponse() throws Throwable {
		String responsebody=responseObj.getBody().asString();
		Reporter.addStepLog("Create Defect Response JSON: "+responsebody);
		Assert.assertEquals(200, responseObj.getStatusCode());
	    Reporter.addStepLog("Create Defect Response Body: "+responsebody);	    
	    
	    CookieResponse cookie=gson.fromJson(responsebody, CookieResponse.class);
	    Session session=cookie.getSession();
	    LoginInfo loginInfo=cookie.getLoginInfo();
	    jSessionID=session.getValue();
	    System.out.println("Cookie Response validated Successfully and JSESSION ID: "+jSessionID);
	}
	
	@Then("^Defect Response should be validated and New DefectID should be generated Successfully$")
	public void new_DefectID() throws Throwable {
		String responsebody=responseObj.getBody().asString();
		Reporter.addStepLog("Create Defect Response JSON: "+responsebody);
		Assert.assertEquals(201, responseObj.getStatusCode());
		Reporter.addStepLog("Create Defect Response Body: "+responsebody);
		DefectResponse defectRes=gson.fromJson(responsebody, DefectResponse.class);
		defectID=defectRes.getKey();
		System.out.println("Defect Response validated Successfully and Defect ID: "+defectID);
		   
	}


}
