package com.unity.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class FileOperationsTest {
	FileOperations fileOperations;
	Map<Integer, JSONObject> testMap;

	@BeforeTest
	public void setMap() {
		String project1 = "{\"projectCost\": 25.5, \"expiryDate \": \"07202017 00:00:00\", \"enabled\": true, \"projectUrl\": \"http://www.unity3d.com\"}";
		String project2 = "{\"projectCost\": 5.5, \"expiryDate \": \"07202017 00:00:00\", \"enabled\": true, \"projectUrl\": \"http://www.unity3d.com\"}";
		String project3 = "{\"projectCost\": 55.7, \"expiryDate \": \"07202017 00:00:00\", \"enabled\": true, \"projectUrl\": \"http://www.unity3d.com\"}";
		testMap = new HashMap<>();
		testMap.put(1, new JSONObject(project1));
		testMap.put(2, new JSONObject(project2));
		testMap.put(3, new JSONObject(project3));
	}

	@Test
	public void getProjectsSortedOnCostTest() {
		fileOperations = new FileOperations();
		fileOperations.map = testMap;
		List<JSONObject> list = fileOperations.getProjectsSortedOnCost();

		Assert.assertEquals(list.get(0).get("projectCost"), 55.7);
		Assert.assertEquals(list.get(1).get("projectCost"), 25.5);
		Assert.assertEquals(list.get(2).get("projectCost"), 5.5);
	}
}
