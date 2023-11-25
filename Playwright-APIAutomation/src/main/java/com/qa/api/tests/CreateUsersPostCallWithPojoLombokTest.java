package com.qa.api.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.api.data.User;
import com.api.data.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class CreateUsersPostCallWithPojoLombokTest {
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
	public void createUserTest() throws IOException {

		// Create Users Object Using Builder Pattern
	Users users=Users.builder().name("Nick")
		.email(getRandomEmail()).gender("male").status("active").build();
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
		ObjectMapper objectMapper = new ObjectMapper();
		User user = objectMapper.readValue(text, User.class);
		System.out.println("---Actual Users From Response---");
		System.out.println(user);
		Assert.assertEquals(user.getName(), users.getName());
		Assert.assertEquals(user.getEmail(), users.getEmail());
		Assert.assertEquals(user.getGender(), users.getGender());
		Assert.assertEquals(user.getStatus(), users.getStatus());
		Assert.assertNotNull(user.getId());

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
