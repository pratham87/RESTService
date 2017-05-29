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

	public boolean writeToFile(String projectJSON) throws IOException {

		File file = new File(System.getProperty("user.home"), "projects.txt");
		BufferedWriter bufferedWriter = null;

		try {
			if (!file.exists()) {
				System.out.println("Creating new file.");
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file, true);

			bufferedWriter = new BufferedWriter(fileWriter);
			JSONObject project = new JSONObject(projectJSON);
			if (isValidProject(project)) {
				bufferedWriter.write(project.toString() + "\n");
				System.out.println("File updated");
				return true;
			}
			// bufferedWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedWriter.close();
		}
		return false;
	}

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
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public List<JSONObject> getProjectsSortedOnCost() {
		List<JSONObject> projects = map.values().stream()
				.filter(p -> isExpired(p) && isEnabled(p) && checkProjectURL(p)).collect(Collectors.toList());
		projects.sort((p1, p2) -> (int) p2.getDouble("projectCost") - (int) p1.getDouble("projectCost"));
		return projects;
	}

	/**
	 * Service should always return the matching project id if it is sent in
	 * request. For ex. If request is
	 * http:\\localhost:5000\requestProject?projectid=1&country=usa&number=29
	 * then it should return a project with matching id regardless of any other
	 * rule.
	 */
	public JSONObject getProject(Integer id, String country, Integer number, String keyword) {
		getAllProjectsFromFile();
		List<JSONObject> projects = getProjectsSortedOnCost();

		if (id != null) {
			if (map != null && map.containsKey(id)) {
				return getResult(map.get(id));

			} else {
				return new JSONObject().put("message", "no project found");
			}
		} else if (country != null && number != null && keyword != null
				|| country != null && number != null && keyword == null
				|| country != null && number == null && keyword == null) {
			return noIdSearch(projects, country, number, keyword);
		} else {
			return highestPrice(projects);
		}
	}

	public static void main(String s[]) {
		// new FileOperations().writeToFile("{hello}");
		// System.out.println(new FileOperations().getProject(null, "usa", 25,
		// "movie").toString());
		System.out.println(new FileOperations().getProject(1, null, null, null).toString());
		// System.out.println(new FileOperations().getProject(2, "FINLAND", 11,
		// "games").toString());

	}

}
