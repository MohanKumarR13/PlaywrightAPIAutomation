package com.qa.api.tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;

public class ApiResponseHeadersTest {
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
	public void apiResponseHeadersTest() throws IOException {
		
		APIResponse apiResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users");
		int statusCode = apiResponse.status();
		System.out.println("Response Status Code :" + statusCode);
		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(apiResponse.ok(), true);

		Map<String, String> headersMap = apiResponse.headers();
		headersMap.forEach((k, v) -> System.out.println(k + " :" + v));
		System.out.println("Total Response Headers :" + headersMap.size());
		Assert.assertEquals(headersMap.get("server"), "cloudflare");
		Assert.assertEquals(headersMap.get("content-type"), "application/json; charset=utf-8");

		// Using List
		List<HttpHeader> headersList = apiResponse.headersArray();
		for (HttpHeader e : headersList) {
			System.out.println(e.name + " : " + e.value);
		}

	}

	@AfterTest
	public void tearDown() {
		playwright.close();
	}

}
