package com.ot.webserver.maven_ot_webserver;

public class Authenticator {

	private String username;
	private String password;
	
	public Authenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public boolean authenticate() {
		if(this.username.equals(Config.username) && this.password.equals(Config.password)) {
			return true;
		}
		return false;
	}
}
