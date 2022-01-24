package com.ot.webserver.maven_ot_webserver;

import java.util.HashMap;
import java.util.Map;

public class Config {
	
	public static int PORT = 8080;
	
	public static final int PORT_MIN = 1;
	
	public static final int PORT_MAX = 65535;
	
	public static final int WORKER_THREADS = 20;
	
	public static final int CONNECTION_QUEUE = 30;
	
	public static enum METHODS {
		GET
	}
	
	@SuppressWarnings("serial")
	public static final Map<Integer, String> STATUS_CODES = new HashMap<Integer, String>(){{
		put(200, "OK");
		put(401, "AUTHORIZATION REQUIRED");
		put(404, "NOT FOUND");
		put(405, "METHOD NOT ALLOWED");
	}};

}
