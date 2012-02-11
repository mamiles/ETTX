
package com.cisco.ettx.admin.collengine;

public class DataCollectionException extends Exception {
	public static final String UNKNOWN_TASK = "Unknown Task";
	public static final String UNKNOWN_ERROR = "Unknown Error";
	public static final String UNABLE_TO_EXECUTE_COMMAND = "Unable to execute Command";
	public static final String HANDLER_CLASS_EXCEPTION = "Unable to map handler to class";
	public static final String UNABLE_TO_LOGIN_SMS = "Unable to login to SMS";
	public static final String PARSE_ERROR = "Unable to parse XML file";
	public static final String NO_VALUE_FOR_ATTR = "Unable to map attribute to a value";
	public static final String MISSING_CONFIG = "Either configuration files or directories missing";
	public static final String END_OF_RECORDS = "End of Records";
	public static final String NO_SUCH_RECORD = "No records found matching the query";
	public static final String NO_SUCH_METHOD = "No such method.Invalid task configuration";
	public static final String NO_SUCH_SERVICE = "No such service.Invalid task configuration";
	public static final String INSUFFICIENT_DATA_FOR_TASK = "Insufficient data for task";
	public static final String UNABLE_TO_EXECUTE_TASK = "Unable to execute task";
	public static final String UNABLE_TO_SET_VALUE = "Unable to set value.Invalid task configuration";
	public static final String UNABLE_TO_CONNECT_TO_DEVICE = "Unable to connect to access switch";
	public static final String INVALID_EDGE_ROUTER_ID = "No Edge Router for Switch";
	public static final String UNKNOWN_SUBSCRIBER = "No such subscriber in database";
	public static final String NO_SERVICE_FOR_SUBSCRIBER = "Subscriber not subscribed to any service";

	public static int ERROR = 0;
	public static int INFO = 1;

	private int type = ERROR;

	public DataCollectionException(String msg) {
		super(msg);
		type = ERROR;
	}

	public DataCollectionException(String msg,int ltype) {
		super(msg);
		type = ltype;
	}

	public int getType() {
		return type;
	}
}
