package com.qa.api.tests;

import java.io.IOException;
import java.util.Map;

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

public class GetApiCall {
	Playwright playwright;
	APIRequest request;
	APIRequestContext apiRequestContext;

	@BeforeTest
	public void setUp() {
		playwright = Playwright.create();
		request = playwright.request();
		apiRequestContext = request.newContext();
	}

	@Test
	public void getSpecificUserApiTest() throws IOException {
		APIResponse apiResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users",
				RequestOptions.create().setQueryParam("gender", "male").setQueryParam("status", "active"));
		int statusCode = apiResponse.status();
		System.out.println("Response Status Code :" + statusCode);
		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(apiResponse.ok(), true);

		String statusResponseText = apiResponse.statusText();
		System.out.println(statusResponseText);
		
		System.out.println("---Print API Response with plain text---");
		System.out.println(apiResponse.text());

		System.out.println("---Print API JSON Response---");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
		String jsonPrettyResponse = jsonResponse.toPrettyString();
		System.out.println(jsonPrettyResponse);
	}

	@Test
	public void getUsersApiTest() throws IOException {
		APIResponse apiResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users");
		int statusCode = apiResponse.status();
		System.out.println("Response Status Code :" + statusCode);
		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(apiResponse.ok(), true);

		String statusResponseText = apiResponse.statusText();
		System.out.println(statusResponseText);
		System.out.println("---Print API Response with plain text---");
		System.out.println(apiResponse.text());

		System.out.println("---Print API JSON Response---");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
		String jsonPrettyResponse = jsonResponse.toPrettyString();
		System.out.println(jsonPrettyResponse);

		System.out.println("---Print API Url---");
		System.out.println(apiResponse.url());

		System.out.println("---Print Response Header---");
		Map<String, String> headersMap = apiResponse.headers();
		System.out.println(headersMap);
		Assert.assertEquals(headersMap.get("content-type"), "application/json; charset=utf-8");
		Assert.assertEquals(headersMap.get("x-download-options"), "noopen");

	}

	@AfterTest
	public void tearDown() {
		playwright.close();
	}

}
