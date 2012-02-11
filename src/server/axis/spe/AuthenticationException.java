package com.cisco.ettx.admin.server.axis.spe;

public class AuthenticationException extends Exception {
	public static final String UNABLE_TO_EXECUTE_SERVICE = "Unable to execute service";
	public static final String AUTHENTICATION_FAILURE = "Authentication failed at SPE";
	public static final String NOT_AUTHORIZED = "Authentication failed at SPE";
	public static final String UNABLE_TO_CONNECT_TO_SPE = "Unable to connect to SPE for authentication";
	public static final String NO_URL_FOR_SPE = "Unable to find SPE URL";

	public AuthenticationException(String msg) {
		super(msg);
	}
}
