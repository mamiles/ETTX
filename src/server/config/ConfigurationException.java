
package com.cisco.ettx.admin.config;

public class ConfigurationException extends Exception {
	public static final String SINGLETON_VIOLATION = "Singleton_Violation";
	public static final String NO_COMPONENT = "No Such Component";
	public static final String ERROR_READING_XML_FILE = "Error reading/parsing XML File";
	public static final String ERROR_WRITING_XML_FILE = "Error writing XML File";
	public static final String NO_VALUE_FOR_NODE = "Expected value for node";
	public static final String CANNOT_WRITE_TO_LEASE_HISTORY_EXPORT_DIR = "Lease History Export Directory is not writable";
	public static final String CANNOT_WRITE_TO_EVENT_EXPORT_DIR = "Event Log Directory is not writable";
	public ConfigurationException(String msg) {
		super(msg);
	}
}
