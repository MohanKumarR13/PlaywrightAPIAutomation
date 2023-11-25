package com.qa.api.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class CreateUserTestWithJsonFileTest {
	Playwright playwright;
	APIRequest request;
	APIRequestContext apiRequestContext;
	static String emailId;

	@BeforeTest
	public void setUp() {
		playwright = Playwright.create();
		request = playwright.request();
		apiRequestContext = request.newContext();
	}

	@Test
	public void createUserPostCallTest() throws IOException {

		// Get Json Files
		byte[] fileBytes = null;
		File file = new File(".\\data\\user.json");
		fileBytes = Files.readAllBytes(file.toPath());
		// Post Call Create A User
		APIResponse apiPostResponse = apiRequestContext.post("https://gorest.co.in/public/v2/users",
				RequestOptions.create().setHeader("Content-Type", "application/json")
						.setHeader("Authorization",
								"Bearer 5a7beb36440e727057c39182bd8659e18635352553a8c9329a8623c0404225f5")
						.setData(fileBytes));
		System.out.println(apiPostResponse.status());
		Assert.assertEquals(apiPostResponse.status(), 201);
		Assert.assertEquals(apiPostResponse.statusText(), "Created");
		System.out.println(apiPostResponse.text());

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonPostResponse = objectMapper.readTree(apiPostResponse.body());
		String jsonPrettyResponse = jsonPostResponse.toPrettyString();
		System.out.println(jsonPrettyResponse);
//Capture Id From the Post Json Response
		String userId = jsonPostResponse.get("id").asText();
		System.out.println("User ID :" + userId);
		// Get Call Fetch the same user by id
		APIResponse getResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users/" + userId,
				RequestOptions.create().setHeader("Authorization",
						"Bearer 5a7beb36440e727057c39182bd8659e18635352553a8c9329a8623c0404225f5"));
		Assert.assertEquals(getResponse.status(), 200);
		Assert.assertEquals(getResponse.statusText(), "OK");
		System.out.println(getResponse.text());
		Assert.assertTrue(getResponse.text().contains(userId));
		Assert.assertTrue(getResponse.text().contains("Johnny"));

	}

	public static String getRandomEmail() {
		emailId = "test" + System.currentTimeMillis() + "@gmail.com";
		return emailId;
	}

	@AfterTest
	public void tearDown() {
		playwright.close();
	}

}
