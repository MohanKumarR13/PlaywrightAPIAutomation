//package com.qa.api.tests;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//
//import org.testng.Assert;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.Test;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.microsoft.playwright.APIRequest;
//import com.microsoft.playwright.APIRequestContext;
//import com.microsoft.playwright.APIResponse;
//import com.microsoft.playwright.Playwright;
//import com.microsoft.playwright.options.RequestOptions;
//
//public class TokenTest {
//	Playwright playwright;
//	APIRequest request;
//	APIRequestContext apiRequestContext;
//	static String emailId;
//
//	@BeforeTest
//	public void setUp() {
//		playwright = Playwright.create();
//		request = playwright.request();
//		apiRequestContext = request.newContext();
//	}
//
//	@Test
//	public void tokenTest() throws IOException {
//		
//		
//		// Get Json Files
//				byte[] fileBytes = null;
//				File file = new File(".\\data\\token.json");
//				fileBytes = Files.readAllBytes(file.toPath());// Post Call Create A User
//		APIResponse apiPostTokenResponse = apiRequestContext.post("https://postman-echo.com/basic-auth/",
//				RequestOptions.create().setHeader("Content-Type", "application/json").setData(fileBytes));
//		System.out.println(apiPostTokenResponse.status());
//		Assert.assertEquals(apiPostTokenResponse.status(), 200);
//		Assert.assertEquals(apiPostTokenResponse.statusText(), "OK");
//		System.out.println(apiPostTokenResponse.text());
//
//		ObjectMapper objectMapper = new ObjectMapper();
//		JsonNode jsonPostResponse = objectMapper.readTree(apiPostTokenResponse.body());
//		String jsonPrettyResponse = jsonPostResponse.toPrettyString();
//		System.out.println(jsonPrettyResponse);
////Capture Id From the Post Json Response
//		String tokenId = jsonPostResponse.get("id").asText();
//		Assert.assertNotNull(tokenId);
//
//	}
//
//	public static String getRandomEmail() {
//		emailId = "test" + System.currentTimeMillis() + "@gmail.com";
//		return emailId;
//	}
//
//	@AfterTest
//	public void tearDown() {
//		playwright.close();
//	}
//
//}
