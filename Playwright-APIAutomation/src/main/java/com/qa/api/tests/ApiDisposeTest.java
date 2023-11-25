package com.qa.api.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;

public class ApiDisposeTest {
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
	public void disposeResponseTest() throws IOException {
		// Request-1
		APIResponse apiResponse = apiRequestContext.get("https://gorest.co.in/public/v2/users");
		int statusCode = apiResponse.status();
		System.out.println("Response Status Code :" + statusCode);
		Assert.assertEquals(statusCode, 200);
		Assert.assertEquals(apiResponse.ok(), true);

		String statusResponseText = apiResponse.statusText();
		System.out.println(statusResponseText);

		System.out.println("---Print API Response with plain text---");
		System.out.println(apiResponse.text());

		apiResponse.dispose();
		// Will Dispose the only response body but the status code,url,status,text will
		// reamin same
		System.out.println("---Print API Response After Dispose with plain text---");
		try {
			System.out.println(apiResponse.text());
		} catch (PlaywrightException e) {
			System.out.println("Api response body is disposed");
		}
		int status = apiResponse.status();
		System.out.println("Response Status Code After Dispose:" + status);

		String statusResponse = apiResponse.statusText();
		System.out.println(statusResponse);

		System.out.println("Response Url :" + apiResponse.url());

		// Request-2
		APIResponse apiResponses = apiRequestContext.get("https://gorest.co.in/api/users/2");
		System.out.println("Get Response Body for Second Request");
		System.out.println("Status Code :" + apiResponses.status());
		System.out.println("Response Body :" + apiResponses.text());

//Request context url
		apiRequestContext.dispose();
		System.out.println("Response Body 1 :" + apiResponse.text());

		System.out.println("Response Body 2 :" + apiResponses.text());

	}

	@AfterTest
	public void tearDown() {
		playwright.close();
	}

}
