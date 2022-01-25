package com.ot.webserver.maven_ot_webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Authenticator {

	private static ArrayList<String> headers = new ArrayList<String>();
	
	public static void authenticate(Socket s, String username, String password) {
//		if(username.equals(Config.username) && password.equals(Config.password)) {
//			
//		}
		// extract header authorzation basic base64
	}
	
	public static void responseView(Socket socket) throws IOException {
		responseHeaders(200);
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		for(String header : headers) {
			outputStream.writeBytes(header + "\r\n");
		}
		outputStream.writeBytes("\r\n");
		outputStream.writeBytes("<!DOCTYPE html><html><head><title>Java Web Server</title></head><body></body></html>");
		outputStream.writeBytes("\r\n");
		outputStream.flush();
	}
	
	public static void responseHeaders(int statusCode) {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		headers.add("HTTP/1.0 " + Integer.toString(statusCode) + " " + Config.STATUS_CODES.get(statusCode));
		headers.add("Content-Type: text/html");
		headers.add("Date: " + formatter.format(new Date(System.currentTimeMillis())));
		headers.add("Server: Simple Java Web Server");
		headers.add("WWW-Authenticate: Basic");
	}
}
