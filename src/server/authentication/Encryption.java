//************************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved
//************************************************************

package com.cisco.ettx.admin.authentication;

import java.io.*;
import com.cisco.ettx.admin.authentication.CryptoHelper;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Encryption 
{

    public static void initLogger()
    {
	try {
	    String ETTX_ROOT = System.getProperty("ETTX_ROOT");
	    StringBuffer lpath = new StringBuffer(ETTX_ROOT);
	    lpath.append("/config/server-log4j.prop");
            String log4jConfigFile = lpath.toString();
	    PropertyConfigurator.configure(log4jConfigFile);
	} catch (Exception e) {
	    System.out.println("Unexpected Exception: " + e);
	}

    }
    public static void main(String[] args) 
	throws Exception
    {
	//initLogger();

	String result = null;

	if (args.length != 2) {
	    System.out.println("Usage: Encryption -e | -d string");
	    return;
	}

	 String ettxDir = System.getProperty("ETTX_ROOT");
	CryptoHelper crypto = new CryptoHelper(ettxDir);
	if ("-e".equals(args[0]))
	    result = crypto.encryption(args[1]);
	else if ("-d".equals(args[0]))
	    result = crypto.decryption(args[1]);
	else 
	    System.out.println("Usage: Encryption -e | -d string");

	System.out.println(result);
    }
}
