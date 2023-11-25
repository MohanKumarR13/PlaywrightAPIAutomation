package com.qa.api.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.api.data.User;
import com.api.data.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class DeleteUserApiTest {
	Playwright playwright;
	APIRequest request;
	static APIRequestContext apiRequestContext;
	static String emailId;
	static String userID;
	static Users users;
	static User user;
	static APIResponse apiGetResponse;
	static ObjectMapper objectMapper;
	static APIResponse apiPutResponse;
	static String gettResponseText;

	@BeforeTest
	public void setUp() {
		playwright = Playwright.create();
		request = playwright.request();
		apiRequestContext = request.newContext();
	}

	@Test
	public void createUserTest() throws IOException {

		// Create Users Object Using Builder Pattern
		users = Users.builder().name("Fury").email(getRandomEmail()).gender("male").status("active").build();
// Post Call Create A User
		APIResponse apiPostResponse = apiRequestContext.post("https://gorest.co.in/public/v2/users",
				RequestOptions.create().setHeader("Content-Type", "application/json")
						.setHeader("Authorization",
								"Bearer 5a7beb36440e727057c39182bd8659e18635352553a8c9329a8623c0404225f5")
						.setData(users));
		System.out.println(apiPostResponse.status());
		Assert.assertEquals(apiPostResponse.status(), 201);
		Assert.assertEquals(apiPostResponse.statusText(), "Created");
		String text = apiPostResponse.text();
		System.out.println(text);
//Convert response text/json to POJO--Deserialization
		objectMapper = new ObjectMapper();
		user = objectMapper.readValue(text, User.class);
		System.out.println("---Actual Users From Response---");
		System.out.println(user);
		Assert.assertEquals(user.getName(), users.getName());
		Assert.assertEquals(user.getEmail(), users.getEmail());
		Assert.assertEquals(user.getGender(), users.getGender());
		Assert.assertEquals(user.getStatus(), users.getStatus());
		Assert.assertNotNull(user.getId());

	}

	@Test
	public static void updateUser() throws JsonMappingException, JsonProcessingException {
		userID = user.getId();
		System.out.println("New User ID is :" + userID);

		// Update Active to Inactive
		user.setStatus("inactive");
		user.setName("Nick Fury");
		System.out.println("---Updating User---");
		apiPutResponse = apiRequestContext.put("https://gorest.co.in/public/v2/users/" + userID,
				RequestOptions.create().setHeader("Content-Type", "application/json")
						.setHeader("Authorization",
								"Bearer 5a7beb36440e727057c39182bd8659e18635352553a8c9329a8623c0404225f5")
						.setData(users));
		System.out.println(apiPutResponse.status());
		System.out.println(apiPutResponse.statusText());

		Assert.assertEquals(apiPutResponse.status(), 200);

		String putResponseText = apiPutResponse.text();
		System.out.println("Updated User " + putResponseText);
		Users apiPutUser = objectMapper.readValue(putResponseText, Users.class);
		Assert.assertEquals(apiPutUser.getId(), userID);
		Assert.assertEquals(apiPutUser.getStatus(), users.getStatus());
		Assert.assertEquals(apiPutUser.getName(), users.getName());

		// Get Updated User

		apiGetResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users/" + userID, RequestOptions.create()
				.setHeader("Authorization", "Bearer 5a7beb36440e727057c39182bd8659e18635352553a8c9329a8623c0404225f5"));
		System.out.println(apiGetResponse.url());
		int statusCode = apiGetResponse.status();
		System.out.println("Get Response Status Code " + statusCode);
		Assert.assertEquals(statusCode, 200);

		Assert.assertEquals(apiGetResponse.ok(), true);

		gettResponseText = apiGetResponse.statusText();
		System.out.println("Get User " + gettResponseText);
		String getResponseText = apiGetResponse.text();
		System.out.println(getResponseText);
		Users apiGetUser = objectMapper.readValue(getResponseText, Users.class);
		Assert.assertEquals(apiGetUser.getId(), userID);
		Assert.assertEquals(apiGetUser.getStatus(), users.getStatus());
		Assert.assertEquals(apiGetUser.getName(), users.getName());

		System.out.println(user.getId());

		// Delete User

		APIResponse deleteResponse = apiRequestContext.delete("https://gorest.co.in/public/v2/users/" + userID,
				RequestOptions.create().setHeader("Authorization",
						"Bearer 5a7beb36440e727057c39182bd8659e18635352553a8c9329a8623c0404225f5"));
		System.out.println(apiGetResponse.url());
		System.out.println(deleteResponse.status());
		System.out.println("Delete Response Status " + deleteResponse.statusText());
		Assert.assertEquals(deleteResponse.status(), 204);
		System.out.println("Delete User Response Body" + deleteResponse.text());

		APIResponse apiResponse = apiRequestContext.delete("https://gorest.co.in/public/v2/users/" + userID,
				RequestOptions.create().setHeader("Authorization",
						"Bearer 5a7beb36440e727057c39182bd8659e18635352553a8c9329a8623c0404225f5"));
		System.out.println(apiResponse.text());
		int status = apiResponse.status();
		System.out.println("Response Status Code" + status);
		Assert.assertEquals(status, 404);
		Assert.assertEquals(apiResponse.statusText(), "Not Found");
		Assert.assertTrue(apiResponse.text().contains("Resource not found"));

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
