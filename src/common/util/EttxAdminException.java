//***********************************************
// Copyright (c) 2001, 2002 Cisco Systems, Inc.
// All rights reserved
//***********************************************

package com.cisco.ettx.admin.common;

public class EttxAdminException extends Exception
{
    public String componentName;
    public String functionName;
    public int errorCode;
    public String errorMsg;

    public EttxAdminException(String a_component, String a_function, int a_errorCode, String a_errorMsg)
    {
	this.componentName = a_component;
	this.functionName = a_function;
	this.errorCode = a_errorCode;
	this.errorMsg = a_errorMsg;
    }

    public EttxAdminException(String a_component, String a_function, int a_errorCode, Exception e)
    {
	this.componentName = a_component;
	this.functionName = a_function;
	this.errorCode = a_errorCode;
	this.errorMsg = e.getMessage();
    }

    public String getMessage() 
    {
	return this.errorMsg;
    }

    public String toString()
    {
	StringBuffer buffer = new StringBuffer("EttxAdminException:\n");
	buffer.append("componentName=");
	buffer.append(this.componentName);
	buffer.append("\n");
	buffer.append("functionName=");
	buffer.append(this.functionName);
	buffer.append("\n");
	buffer.append("errorCode=");
	buffer.append(this.errorCode);
	buffer.append("\n");
	buffer.append("errorMsg=");
	buffer.append(this.errorMsg);
	buffer.append("\n");
	return buffer.toString();
    }
}
