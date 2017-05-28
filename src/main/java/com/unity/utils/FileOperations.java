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

	public void writeToFile(String project) {

		File file = new File(System.getProperty("user.home"), "projects.txt");

		try {
			if (!file.exists()) {
				System.out.println("Creating new file.");
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file, true);

			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(new JSONObject(project).toString() + "\n");
			bufferedWriter.close();

			System.out.println("File updated");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void getAllProjectsFromFile() {
		map = new HashMap<>();
		JSONObject project;
		String file = "/projects.txt";
		String line = null;

		try {
			// ClassLoader classLoader = getClass().getClassLoader();
			FileReader fileReader = new FileReader(System.getProperty("user.home") + file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				project = new JSONObject(line);
				map.put(project.getInt("id"), project);
			}
			bufferedReader.close();
			// return map;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		// return null;
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
		if (map != null && id != null && map.containsKey(id)) {
			return map.get(id);
		} else if (country != null && number != null && keyword != null) {
			return noIdSearch(map, country, number, keyword);
		} else {
			return highestPrice(getProjectsSortedOnCost());
		}
	}

	public static void main(String s[]) {
		// new FileOperations().writeToFile("{hello}");
		// System.out.println(new FileOperations().getProject(null, "usa", 25,
		// "movie").toString());
		System.out.println(new FileOperations().getProject(null, null, null, null).toString());
		// System.out.println(new FileOperations().getProject(2, "FINLAND", 11,
		// "games").toString());

	}

}
