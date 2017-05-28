package com.unity.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class FileOperations {

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

	public Map<Integer, JSONObject> getAllProjectsFromFile() {
		Map<Integer, JSONObject> map = new TreeMap<>();
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
			return map;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public JSONObject getProject(int id, String country, int number, String keyword) {
		JSONObject result = new JSONObject();
		Map<Integer, JSONObject> map = getAllProjectsFromFile();
		if (map != null && map.containsKey(id)) {
			JSONObject project = map.get(id);
			JSONArray countries = project.getJSONArray("targetCountries");
			for (int i = 0; i < countries.length(); i++) {
				if (countries.getString(i).equalsIgnoreCase(country)) {
					JSONArray targetKeys = project.getJSONArray("targetKeys");

					for (int j = 0; j < targetKeys.length(); j++) {
						JSONObject targetKey = targetKeys.getJSONObject(j);

						if (targetKey.getInt("number") == number
								&& targetKey.getString("keyword").equalsIgnoreCase(keyword)) {

							result.put("projectName", project.getString("projectName"));
							result.put("projectCost", project.getDouble("projectCost"));
							result.put("projectUrl", project.getString("projectUrl"));

							return result;
						}

					}
				}

			}

		}

		return result.put("message", "no project found");
	}

	public static void main(String s[]) throws IOException {
		// new FileOperations().writeToFile("{hello}");

		System.out.println(new FileOperations().getProject(2, "FINLAND", 11, "games").toString());

	}

}
