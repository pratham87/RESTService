package com.unity.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Rules {
	final static Logger logger = Logger.getLogger(Rules.class);

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
	public JSONObject noIdSearch2(List<JSONObject> projects, String country, Integer number, String keyword) {
		try {
			if (projects != null && projects.size() != 0) {
				for (JSONObject project : projects) {

					// Get countries
					if (!project.isNull("targetCountries")) {
						JSONArray countries = project.getJSONArray("targetCountries");
						for (int i = 0; i < countries.length(); i++) {
							if (countries.getString(i).equalsIgnoreCase(country)) {

								// Get number
								if (number != null) {
									JSONArray targetKeys = project.getJSONArray("targetKeys");

									for (int j = 0; j < targetKeys.length(); j++) {
										JSONObject targetKey = targetKeys.getJSONObject(j);

										if (targetKey.getInt("number") >= number) {

											// Get keyword
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new JSONObject().put("message", "no project found");
	}

	public JSONObject noIdSearch(List<JSONObject> projects, String country, Integer number, String keyword) {
		if (projects != null && projects.size() != 0) {
			int c = -1, n = -1, k = -1;
			if (country != null) {
				c = 0;
			}
			if (number != null) {
				n = 0;
			}

			if (keyword != null) {
				k = 0;
			}
			for (JSONObject project : projects) {
				if (c == 0) {
					if (checkCountry(project, country)) {
						c = 1;
					}
				}
				if (n == 0) {
					if (checkNumber(project, number)) {
						n = 1;
					}
				}

				if (k == 0) {
					if (checkKeyword(project, keyword)) {
						k = 1;
					}
				}

				if ((c == 1 || c == -1) && (n == 1 || n == -1) && (k == 1 || k == -1)) {
					return getResult(project);
				}

				// Reset the values
				if (c != -1) {
					c = 0;
				}
				if (n != -1) {
					n = 0;
				}
				if (k != -1) {
					k = 0;
				}

			}
		}
		return new JSONObject().put("message", "no project found");
	}

	public boolean checkCountry(JSONObject project, String country) {
		JSONArray countries = project.getJSONArray("targetCountries");
		for (int i = 0; i < countries.length(); i++) {
			if (countries.getString(i).equalsIgnoreCase(country)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkNumber(JSONObject project, int number) {
		JSONArray targetKeys = project.getJSONArray("targetKeys");

		for (int j = 0; j < targetKeys.length(); j++) {
			JSONObject targetKey = targetKeys.getJSONObject(j);

			if (targetKey.getInt("number") >= number) {
				return true;
			}

		}
		return false;
	}

	public boolean checkKeyword(JSONObject project, String keyword) {
		JSONArray targetKeys = project.getJSONArray("targetKeys");

		for (int j = 0; j < targetKeys.length(); j++) {
			JSONObject targetKey = targetKeys.getJSONObject(j);

			if (targetKey.getString("keyword").equalsIgnoreCase(keyword)) {
				return true;
			}
		}
		return false;
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
