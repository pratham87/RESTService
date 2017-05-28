package com.unity.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

@Path("/client")
public class UnityClient {

	@GET
	@Path("/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response acClient(@PathParam("param") String api) {
		StringBuilder builder = new StringBuilder();
		String string = "", output = "", text = "";
		try {

			InputStream input = getClass().getResourceAsStream("/request.txt");
			InputStreamReader inputReader = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(inputReader);
			String line;
			while ((line = br.readLine()) != null) {
				string += line + "\n";
			}

			System.out.println("\n\n JSON Request: \n" + string);

			JSONObject jsonObject = new JSONObject(string);
			System.out.println(jsonObject);

			try {
				URL url = new URL("http://localhost:8080/Unity/" + api);
				URLConnection connection = (URLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setRequestProperty("charset", "utf-8");
				connection.setRequestProperty("Content-Type", "application/json");
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
				out.write(jsonObject.toString());
				out.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				while ((output = in.readLine()) != null) {
					builder.append(output);
				}

				text = builder.toString();

				System.out.println(text + "\n Unity Service Invoked Successfully..");
				in.close();
			} catch (Exception e) {
				System.out.println("\nError while calling Service");
				System.out.println(e);
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(201).entity(text).build();
	}

}
