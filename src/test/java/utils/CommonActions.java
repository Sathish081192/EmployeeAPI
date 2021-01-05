package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.Cookie;

public class CommonActions {
	public static String propertyFilePath = "Resources/BaseConfigurations/api_config.properties";
	public static Properties property = new Properties();
	Cookie cookie_object=new Cookie();

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		CommonActions common=new CommonActions();
		String tst=common.encryptPassword("Gowtham1!");
		System.out.println(tst);
	}
	
	/*This method used for decrypyting the encrypted password*/
	public String decryptPassword(String encodedString) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		String decodedString = new String(decodedBytes);
		return decodedString;		
	}
	
	/*This method used for encrypting the password string */
	public String encryptPassword(String decodedString) {
		String encodedString = Base64.getEncoder().encodeToString(decodedString.getBytes());
		return encodedString;	
		
	}
	
	public String readvalueFromProperty(String keyword) {
		try {
			FileInputStream fileInput = new FileInputStream(new File(propertyFilePath));
			property.load(fileInput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String value=property.getProperty(keyword);
		return value;
	}
	
	public String createJSONForCookie() throws ParseException {
		String userName=readvalueFromProperty("username");
		String password=readvalueFromProperty("password");
		password=decryptPassword(password);
		cookie_object.setUsername(userName);
		cookie_object.setPassword(password);
		ObjectMapper mapper=new ObjectMapper();
		String CreatedJSON = null;
		try {
			CreatedJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cookie_object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(CreatedJSON);
     	return CreatedJSON;
		
	}
	
	public Response postMethodforCookie(String endpoint,String inputJSON) {		
		RequestSpecification request=RestAssured.given().contentType(ContentType.JSON).body(inputJSON);
		Response response=request.request(Method.POST,endpoint);
		return response;
	}
	
	public Response postMethod(String endpoint,String inputJSON,String sessionID) {		
		RequestSpecification request=RestAssured.given().contentType(io.restassured.http.ContentType.JSON).header("Cookie","JSESSIONID="+sessionID).body(inputJSON);
		Response response=request.request(Method.POST,endpoint);
		return response;
	}
	
	public Response getMethod(String endPoint) {
		String sessionID=null;
		RequestSpecification request=RestAssured.given().header("Cookie","JSESSIONID="+sessionID);
		Response response=request.request(Method.GET,endPoint);
		return response;
	}
	
	public Response putMethod(String endpoint,JSONObject parentjsonObject) {		
		String sessionID=null;
		RequestSpecification request=RestAssured.given().contentType(io.restassured.http.ContentType.JSON).header("Cookie","JSESSIONID="+sessionID).body(parentjsonObject.toJSONString());
		Response response=request.request(Method.PUT,endpoint);
		return response;
	}
	
	public Response deleteMethod(String endpoint,JSONObject parentjsonObject) {		
		String sessionID=null;
		RequestSpecification request=RestAssured.given().contentType(io.restassured.http.ContentType.JSON).header("Cookie","JSESSIONID="+sessionID).body(parentjsonObject.toJSONString());
		Response response=request.request(Method.DELETE,endpoint);
		return response;
	}

}
