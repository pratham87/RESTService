package com.unity.utils;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RulesTest {
	private Rules rules;

	@Test
	public void isExpiredProjectTest() {
		String json = "{\"expiryDate \": \"04202017 00:00:00\"}";
		rules = new Rules();

		Assert.assertEquals(false, rules.isExpired(new JSONObject(json)));
	}

	@Test
	public void isNotExpiredProjectTest() {
		String json = "{\"expiryDate \": \"07202017 00:00:00\"}";
		rules = new Rules();

		Assert.assertEquals(true, rules.isExpired(new JSONObject(json)));
	}

	@Test
	public void isEnabledTest() {
		String json = "{\"enabled\": true}";
		rules = new Rules();

		Assert.assertEquals(true, rules.isEnabled(new JSONObject(json)));
	}

	@Test
	public void isNotEnabledTest() {
		String json = "{\"enabled\": false}";
		rules = new Rules();

		Assert.assertEquals(false, rules.isEnabled(new JSONObject(json)));
	}

	@Test
	public void isEnabledDefaultValueAsFalseTest() {
		String json = "{\"enabled\": null}";
		rules = new Rules();

		Assert.assertEquals(false, rules.isEnabled(new JSONObject(json)));
	}

	@Test
	public void isEnabledInvalidValueTest() {
		String json = "{\"enabled\": \"hello\"}";
		rules = new Rules();

		Assert.assertEquals(false, rules.isEnabled(new JSONObject(json)));
	}

	@Test
	public void isEnabledIntegerValueTest() {
		String json = "{\"enabled\": 123}";
		rules = new Rules();

		Assert.assertEquals(false, rules.isEnabled(new JSONObject(json)));
	}

	@Test
	public void checkValidProjectURLTest() {
		String json = "{\"projectUrl\": \"http://www.unity3d.com\"}";
		rules = new Rules();

		Assert.assertEquals(true, rules.checkProjectURL(new JSONObject(json)));
	}

	@Test
	public void checkInvalidProjectURLTest() {
		String json = "{\"projectUrl\": null}";
		rules = new Rules();

		Assert.assertEquals(false, rules.checkProjectURL(new JSONObject(json)));
	}

	@Test
	public void checkProjectURLAsIntegerTest() {
		String json = "{\"projectUrl\": 123}";
		rules = new Rules();

		Assert.assertEquals(false, rules.checkProjectURL(new JSONObject(json)));
	}
}
