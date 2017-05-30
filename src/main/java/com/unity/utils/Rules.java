package com.unity.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Rules {
	final static Logger logger = Logger.getLogger(Rules.class);

	/**
	 * This method validates JSONObject's (projects) based on expiryDate param.
	 * Returns true if project is not expired else false.
	 */
	public boolean isExpired(JSONObject project) {
		try {
			if (!project.isNull("expiryDate ")) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy HH:mm:ss");
				LocalDateTime expiryDate = LocalDateTime.parse(project.getString("expiryDate "), formatter);
				LocalDateTime now = LocalDateTime.now();
				return expiryDate.isAfter(now);
			}

		} catch (DateTimeParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * This method validates JSONObject's (projects) based on enabled param.
	 * Returns true if project is enabled else default will be false.
	 */
	public boolean isEnabled(JSONObject project) {
		return project.optBoolean("enabled", false);
	}

	/**
	 * This method validates JSONObject's (projects) based on projectUrl param.
	 * Returns true if project has a projectUrl else false.
	 */
	public boolean checkProjectURL(JSONObject project) {
		try {
			if (project.getString("projectUrl") != null) {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * This method validates every project before writing it to the projects.txt
	 * file. It validates based on projectId, projectName, expiryDate,
	 * projectCost and projectUrl.
	 */
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

	/**
	 * This method is used to get the final output JSONObject. It accepts a
	 * JSONObject, gets projectName, projectCost and projectUrl params and
	 * return a new JSONObject.
	 */
	public JSONObject getResult(JSONObject project) {
		try {
			JSONObject result = new JSONObject();
			result.put("projectName", project.getString("projectName"));
			result.put("projectCost", project.getDouble("projectCost"));
			result.put("projectUrl", project.getString("projectUrl"));
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Rule 3,4,5 covered - Rules listed in README.md file.
	 * 
	 */
	public JSONObject noIdSearch(List<JSONObject> projects, String country, Integer number, String keyword) {
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new JSONObject().put("message", "no project found");
	}

	/**
	 * This method checks if the project (JSONObject) has the given country or
	 * not. Returns true if it finds the country in the project else false.
	 */
	public boolean checkCountry(JSONObject project, String country) {
		try {
			JSONArray countries = project.getJSONArray("targetCountries");
			for (int i = 0; i < countries.length(); i++) {
				if (countries.getString(i).equalsIgnoreCase(country)) {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * This method checks if the project (JSONObject) has the given number or
	 * not. Returns true if it finds the number in the project else false.
	 */
	public boolean checkNumber(JSONObject project, int number) {
		try {
			JSONArray targetKeys = project.getJSONArray("targetKeys");

			for (int j = 0; j < targetKeys.length(); j++) {
				JSONObject targetKey = targetKeys.getJSONObject(j);

				if (targetKey.getInt("number") >= number) {
					return true;
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * This method checks if the project (JSONObject) has the given keyword or
	 * not. Returns true if it finds the keyword in the project else false.
	 */
	public boolean checkKeyword(JSONObject project, String keyword) {
		try {
			JSONArray targetKeys = project.getJSONArray("targetKeys");

			for (int j = 0; j < targetKeys.length(); j++) {
				JSONObject targetKey = targetKeys.getJSONObject(j);

				if (targetKey.getString("keyword").equalsIgnoreCase(keyword)) {
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Rule 2 covered - Rules listed in README.md file.
	 * 
	 */
	public JSONObject highestPrice(List<JSONObject> projects) {
		try {
			if (projects != null && projects.size() != 0) {
				return getResult(projects.get(0));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new JSONObject().put("message", "no project found");
	}

}
