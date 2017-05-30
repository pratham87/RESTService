package com.unity.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class FileOperations extends Rules {
	Map<Integer, JSONObject> map;

	/**
	 * This method will create projects.txt file at ${user.home} location if it
	 * doesn't exist. If it exists then it will write valid projects (Strings)
	 * to it.
	 */
	public boolean writeToFile(String projectJSON) throws IOException {
		File file = new File(System.getProperty("user.home"), "projects.txt");
		BufferedWriter bufferedWriter = null;

		try {
			if (!file.exists()) {
				file.createNewFile();
				logger.info("Creating new file.");
			}
			FileWriter fileWriter = new FileWriter(file, true);

			bufferedWriter = new BufferedWriter(fileWriter);
			JSONObject project = new JSONObject(projectJSON);
			if (isValidProject(project)) {
				bufferedWriter.write(project.toString() + "\n");
				logger.info("File updated");
				System.out.println("File updated");
				return true;
			}

		} catch (IOException e) {
			logger.error("Error writing project to file", e);
			e.printStackTrace();
		} finally {
			bufferedWriter.close();
		}
		return false;
	}

	/**
	 * This method will get all the projects from the file and save it to
	 * HashMap. map is global.
	 */
	public void getAllProjectsFromFile() {
		map = new HashMap<>();
		JSONObject project;
		String file = "/projects.txt";
		String line = null;

		try {
			FileReader fileReader = new FileReader(System.getProperty("user.home") + file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				project = new JSONObject(line);
				if (!project.isNull("id") && !(project.getInt("id") < 0)) {
					map.put(project.getInt("id"), project);
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			logger.error("Error finding file", ex);
			ex.printStackTrace();
		} catch (IOException ex) {
			logger.error("Error reading file", ex);
			ex.printStackTrace();
		}
	}

	/**
	 * This method gets the values in map as a List<JSONObject>, filters all the
	 * projects based on expiryDate, enabled and projectURL params. Then it
	 * sorts the list based on the projectCost param and returns the list.
	 */
	public List<JSONObject> getProjectsSortedOnCost() {
		List<JSONObject> projects = map.values().stream()
				.filter(p -> isExpired(p) && isEnabled(p) && checkProjectURL(p)).collect(Collectors.toList());
		projects.sort((p1, p2) -> (int) p2.getDouble("projectCost") - (int) p1.getDouble("projectCost"));
		return projects;
	}

	/**
	 * Rule 1 covered - Rules listed in README.md file. requestProject API calls
	 * this method to get the project from the projects.txt file.
	 */
	public JSONObject getProject(Integer id, String country, Integer number, String keyword) {
		getAllProjectsFromFile();
		List<JSONObject> projects = getProjectsSortedOnCost();

		// Return project with respective Id
		if (id != null) {
			if (map != null && map.containsKey(id)) {
				return getResult(map.get(id));

			} else {
				return new JSONObject().put("message", "no project found");
			}
			// Return project with country, number and keyword (& operation)
		} else if (country != null || number != null || keyword != null) {
			return noIdSearch(projects, country, number, keyword);
		} else {
			// If all parameters are null then return project with highest
			// projectCost
			return highestPrice(projects);
		}
	}

	// Driver method: For testing methods.
	public static void main(String s[]) {
		// new FileOperations().writeToFile("{hello}");
		System.out.println(new FileOperations().getProject(null, "usa", null, null).toString());

	}

}
