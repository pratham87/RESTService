package com.unity.model;

import java.util.List;
import java.util.Map;

public class Project {
	private int id;
	private String projectName;
	private String creationDate;
	private String expiryDate;
	private boolean enabled;
	private List<String> targetCountries;
	private double projectCost;
	private String projectUrl;
	private Map<Integer, String> targetKeys;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getTargetCountries() {
		return targetCountries;
	}

	public void setTargetCountries(List<String> targetCountries) {
		this.targetCountries = targetCountries;
	}

	public double getProjectCost() {
		return projectCost;
	}

	public void setProjectCost(double projectCost) {
		this.projectCost = projectCost;
	}

	public String getProjectUrl() {
		return projectUrl;
	}

	public void setProjectUrl(String projectUrl) {
		this.projectUrl = projectUrl;
	}

	public Map<Integer, String> getTargetKeys() {
		return targetKeys;
	}

	public void setTargetKeys(Map<Integer, String> targetKeys) {
		this.targetKeys = targetKeys;
	}

}
