package com.ot.webserver.maven_ot_webserver.response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Response {
	
	private int statusCode;
	private String statusCodeMessage;
	private static final Logger logger = LogManager.getLogger(Response.class);
	
	public Response(int statusCode, String statusCodeMessage) {
		this.statusCode = statusCode;
		this.statusCodeMessage = statusCodeMessage;
	}
	
	public void responseView() {
		
	}

}
