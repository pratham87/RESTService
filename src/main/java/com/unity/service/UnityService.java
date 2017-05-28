package com.unity.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.unity.utils.FileOperations;

@Path("/")
public class UnityService {

	@POST
	@Path("/createProject")
	public Response createProject(String project) {
		new FileOperations().writeToFile(project);
		return Response.status(200).type(MediaType.APPLICATION_JSON).entity("campaign is successfully created").build();
	}

	@GET
	@Path("/requestProject")
	public Response requestProject(@QueryParam("projectid") Integer projectid, @QueryParam("country") String country,
			@QueryParam("number") Integer number, @QueryParam("keyword") String keyword) {
		JSONObject result = new FileOperations().getProject(projectid, country, number, keyword);
		return Response.status(200).entity(result.toString()).build();
	}

}
