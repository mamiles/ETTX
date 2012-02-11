package com.cisco.ettx.admin.server.leasehistory;

public class LeaseHistoryException extends Exception {
	public static final String SINGLETON_VIOLATION = "Singleton_Violation";
	public static final String ERROR_READING_XML_FILE = "Error Reading XML File";
	public static final String COMPONENT_READ_ERROR = "Component Read Error";
	public static final String UNABLE_TO_EXECUTE_COMMAND = "Unable to execute command";
	public static final String LEASE_HISTORY_EXTRACT_ERROR = "Lease History Extract Error";
	public static final String INVALID_FILE_NAME = "Invalid Lease History File Name or I/O Error";
	public static final String FILE_NAME_EXISTS = "Cannot Write File - Lease History File Name already exists";
	public LeaseHistoryException(String msg) {
		super(msg);
	}
}
