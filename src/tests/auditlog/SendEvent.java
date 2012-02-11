package com.cisco.ettx.admin.tests.auditlog;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : AuditLogger.java
// Desc : Class which sends an event to the system
//**************************************************

import java.util.*;
import com.tibco.tibrv.*;
import java.io.*;

public class SendEvent
{

    String service = null;
    String network = null;
    String daemon  = null;

    String FIELD_NAME = "CNS.SPE.EVENT";

    public SendEvent(String args[])
    {
        // parse arguments for possible optional
        // parameters. These must precede the subject
        // and message strings
        int i = get_InitParams(args);

        // we must have at least one subject and one message
        if (i > args.length-2)
            usage();

        // open Tibrv in native implementation
        try
        {
            Tibrv.open(Tibrv.IMPL_NATIVE);
        }
        catch (TibrvException e)
        {
            System.err.println("Failed to open Tibrv in native implementation:");
            e.printStackTrace();
            System.exit(0);
        }

        // Create RVD transport
        TibrvTransport transport = null;
        try
        {
            transport = new TibrvRvdTransport(service,network,daemon);
        }
        catch (TibrvException e)
        {
            System.err.println("Failed to create TibrvRvdTransport:");
            e.printStackTrace();
            System.exit(0);
        }

        // Create the message
        TibrvMsg msg = new TibrvMsg();

        // Set send subject into the message
        try
        {
            msg.setSendSubject(args[i++]);
        }
        catch (TibrvException e) {
            System.err.println("Failed to set send subject:");
            e.printStackTrace();
            System.exit(0);
        }

        try
        {
            // Send one message for each parameter
            while (i < args.length)
            {
		try {
			FileReader reader = new FileReader(args[i]);
			BufferedReader bufReader = new BufferedReader(reader);
			StringBuffer buf = new StringBuffer();
			String line = bufReader.readLine();
			while (line != null) {
				buf.append(line);
				line = bufReader.readLine();
			}
                	System.out.println("Publishing: subject="+msg.getSendSubject()+
				"value=" + buf.toString());
                	msg.update(FIELD_NAME,buf.toString());
                	transport.send(msg);
                	i++;
		}
		catch (FileNotFoundException ex) {
			System.err.println("Unable to read file " + args[i]);
			ex.printStackTrace();
		}
		catch (IOException ex) {
			System.err.println("Unable to read file " + args[i]);
			ex.printStackTrace();
			
		}
            }
        }
        catch (TibrvException e)
        {
            System.err.println("Error sending a message:");
            e.printStackTrace();
            System.exit(0);
        }

        // Close Tibrv, it will cleanup all underlying memory, destroy
        // transport and guarantee delivery.
        try
        {
            Tibrv.close();
        }
        catch(TibrvException e)
        {
            System.err.println("Exception dispatching default queue:");
            e.printStackTrace();
            System.exit(0);
        }

    }

    // print usage information and quit
    void usage()
    {
        System.err.println("Usage: java tibrvsend [-service service] [-network network]");
        System.err.println("            [-daemon daemon] <subject> <messages>");
        System.exit(-1);
    }

    int get_InitParams(String[] args)
    {
        int i=0;
        while(i < args.length-1 && args[i].startsWith("-"))
        {
            if (args[i].equals("-service"))
            {
                service = args[i+1];
                i += 2;
            }
            else
            if (args[i].equals("-network"))
            {
                network = args[i+1];
                i += 2;
            }
            else
            if (args[i].equals("-daemon"))
            {
                daemon = args[i+1];
                i += 2;
            }
            else
                usage();
        }
        return i;
    }

    public static void main(String args[])
    {
        new SendEvent(args);
    }

}
