package com.ot.webserver.maven_ot_webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Handles HTTP basic authentication
 * 
 * @author tomiwa
 *
 */
public class Authenticator {

	private static ArrayList<String> headers = new ArrayList<String>();
	
	/**
	 * Validate if authorization credentials are valid
	 * 
	 * @param s	       The incoming connection object
	 * @param base64   The authorization header value 
	 * 				   extracted from the connection
	 *                 object
	 * @return         Returns true if authentication
	 *                 is successful and false if 
	 *                 otherwise
	 */
	public static boolean authenticate(Socket s, String base64) {
		String username = new String(Base64.getDecoder().decode(base64)).split(":")[0];
		String password = new String(Base64.getDecoder().decode(base64)).split(":")[1];
		if(username.equals(Config.username) && BCrypt.verifyer().verify(password.toCharArray(), Config.password).verified) {
			return true;
		} else {
			try {
				failedAuthenticationView(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	/**
	 * Builds the view for a failed basic authentication
	 * 
	 * @param socket The connection to send the response to
	 * @throws IOException
	 */
	public static void failedAuthenticationView(Socket socket) throws IOException {
		failedAuthenticationHeaders(401);
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		for(String header : headers) {
			outputStream.writeBytes(header + "\r\n");
		}
		outputStream.writeBytes("\r\n");
		outputStream.writeBytes("<!DOCTYPE html><html><head><title>Java Web Server</title></head><body><h1> 401 " + Config.STATUS_CODES.get(401) + "</h1></body></html>");
		outputStream.writeBytes("\r\n");
		outputStream.flush();
	}
	
	/**
	 * Build the headers for a failed basic authentication
	 * 
	 * @param statusCode The HTTP response code
	 */
	public static void failedAuthenticationHeaders(int statusCode) {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		headers.add("HTTP/1.0 " + Integer.toString(statusCode) + " " + Config.STATUS_CODES.get(statusCode));
		headers.add("Content-Type: text/html");
		headers.add("Date: " + formatter.format(new Date(System.currentTimeMillis())));
		headers.add("Server: Simple Java Web Server");
	}
	
	/**
	 * 
	 * @param socket The incoming request
	 * @throws IOException
	 */
	public static void responseView(Socket socket) throws IOException {
		responseHeaders(401);
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		for(String header : headers) {
			outputStream.writeBytes(header + "\r\n");
		}
		outputStream.writeBytes("\r\n");
		outputStream.writeBytes("<!DOCTYPE html><html><head><title>Java Web Server</title></head><body></body></html>");
		outputStream.writeBytes("\r\n");
		outputStream.flush();
	}
	
	/**
	 * Builds a HTTP header that requests for basic authentication
	 * 
	 * @param statusCode The HTTP response code
	 */
	public static void responseHeaders(int statusCode) {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		headers.add("HTTP/1.0 " + Integer.toString(statusCode) + " " + Config.STATUS_CODES.get(statusCode));
		headers.add("Content-Type: text/html");
		headers.add("Date: " + formatter.format(new Date(System.currentTimeMillis())));
		headers.add("Server: Simple Java Web Server");
		headers.add("WWW-Authenticate: Basic");
	}
}
