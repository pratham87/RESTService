package com.unity.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Rules {

	public static boolean isExpired(JSONObject project) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy HH:mm:ss");
		LocalDateTime expiryDate = LocalDateTime.parse(project.getString("expiryDate "), formatter);
		LocalDateTime now = LocalDateTime.now();
		return expiryDate.isAfter(now);
	}

	public boolean isEnabled(JSONObject project) {
		return project.optBoolean("enabled", false);
	}

	public boolean checkProjectURL(JSONObject project) {
		if (project.getString("projectUrl") != null) {
			return true;
		}
		return false;
	}

	public boolean isValidProject(JSONObject project) {
		try {

			if (!(project.length() < 1) && !project.isNull("id") && !(project.getInt("id") < 0)
					&& ((Integer) project.getInt("id") instanceof Integer) && !project.isNull("projectName")
					&& (project.getString("projectName") instanceof String) && !project.isNull("expiryDate ")
					&& (project.getString("expiryDate ") instanceof String) && !project.isNull("projectCost")
					&& !(project.getDouble("projectCost") < 0)
					&& ((Double) project.getDouble("projectCost") instanceof Double) && !project.isNull("projectUrl")
					&& (project.getString("projectUrl") instanceof String)) {
				return true;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	public JSONObject getResult(JSONObject project) {
		JSONObject result = new JSONObject();
		result.put("projectName", project.getString("projectName"));
		result.put("projectCost", project.getDouble("projectCost"));
		result.put("projectUrl", project.getString("projectUrl"));
		return result;
	}

	/**
	 * Rule1: If project id is not sent in request url parameters then it should
	 * select all the matching projects based on url parameters and return the
	 * one with highest cost.
	 * 
	 * Rule2: Url parameters should be considered as AND operator and not OR.
	 * Ex. if country=usa&number=30 then it should select all project which has
	 * USA in their targetCountries and number is 30 or above in their
	 * targetedKeys number.
	 * 
	 * Rule 3: Service should return {“message”:”no project found”} if any of
	 * the parameter is not matched.
	 * 
	 */
	public JSONObject noIdSearch(List<JSONObject> projects, String country, Integer number, String keyword) {
		if (projects != null && projects.size() != 0) {
			for (JSONObject project : projects) {
				JSONArray countries = project.getJSONArray("targetCountries");
				for (int i = 0; i < countries.length(); i++) {
					if (countries.getString(i).equalsIgnoreCase(country)) {

						if (number != null) {
							JSONArray targetKeys = project.getJSONArray("targetKeys");

							for (int j = 0; j < targetKeys.length(); j++) {
								JSONObject targetKey = targetKeys.getJSONObject(j);

								if (targetKey.getInt("number") >= number) {

									if (keyword != null) {

										if (targetKey.getString("keyword").equalsIgnoreCase(keyword)) {
											return getResult(project);
										}

									} else {
										return getResult(project);
									}

								}

							}
						} else {
							return getResult(project);
						}

					}
				}
			}
		}
		return new JSONObject().put("message", "no project found");
	}

	/**
	 * Service should return a project with highest price if no url parameter is
	 * sent in request. Example url - http:\\localhost:5000\requestProject
	 * 
	 */
	public JSONObject highestPrice(List<JSONObject> projects) {
		if (projects != null && projects.size() != 0) {
			return getResult(projects.get(0));
		}
		return new JSONObject().put("message", "no project found");
	}

}
