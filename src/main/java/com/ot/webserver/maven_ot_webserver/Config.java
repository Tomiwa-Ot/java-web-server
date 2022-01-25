package com.ot.webserver.maven_ot_webserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Config {
	
	private String username = null;
	
	public String getUsername() {
		return username;
	}

	private String password = null;
	
	public String getPassword() {
		return password;
	}

	public static int PORT = 8080;
	
	public static final int PORT_MIN = 1;
	
	public static final int PORT_MAX = 65535;
	
	public static int WORKER_THREADS = 20;
	
	public static int CONNECTION_QUEUE = 30;
	
	public static enum METHODS {
		HEAD,
		GET
	}
	
	@SuppressWarnings("serial")
	public static final Map<Integer, String> STATUS_CODES = new HashMap<Integer, String>(){{
		put(200, "OK");
		put(401, "AUTHORIZATION REQUIRED");
		put(404, "NOT FOUND");
		put(405, "METHOD NOT ALLOWED");
	}};
	
	private static final Config instance = new Config();
	
	public static Config getInstance() {
		return instance;
	}
	
	public void loadConfigurations() throws FileNotFoundException, IOException {
		JSONParser jsonParser = new JSONParser();
		File properties = new File("src/main/resources/properties.json");
		FileReader reader = new FileReader(properties);
		try {
			Object obj = jsonParser.parse(reader);
			JSONObject jsonObject = (JSONObject) obj;
			username = (String) jsonObject.get("username");
			password = (String) jsonObject.get("password");
			PORT = (int) ((long) jsonObject.get("port"));
			WORKER_THREADS = (int) ((long) jsonObject.get("worker_threads"));
			CONNECTION_QUEUE = (int) ((long) jsonObject.get("connection_queue"));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

}
