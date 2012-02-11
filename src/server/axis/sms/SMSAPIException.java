package com.cisco.ettx.admin.server.axis.sms;

public class SMSAPIException extends Exception {
	public static final String UNABLE_TO_EXECUTE_SERVICE = "Unable to execute service";
	public static final String UNABLE_TO_CONNECT_TO_SMS = "Unable to connect to SMS for service";
	public static final String NO_URL_FOR_SMS = "Unable to find SMS URL";
	public static final String INSUFFICIENT_DATA_FOR_SERVICE = "Insufficient data to perform the service";
	public static final String INVALID_AUTH = "Insufficient credentials for the service";
	public static final String NO_PORT_INFO = "Unable to get switch port details for subscriber";

	public SMSAPIException(String msg) {
		super(msg);
	}
}
