package com.cisco.ettx.admin.auditlog;

//**************************************************
// Copyright (c) 2002 Cisco Systems, Inc.
// All rights reserved.
// Author : Chandrika R
// File : AuditLogger.java
// Desc : Class which logs events from other components
//**************************************************
import org.apache.log4j.*;
import com.tibco.tibrv.Tibrv;
import com.tibco.tibrv.TibrvMsgCallback;
import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvTransport;
import com.tibco.tibrv.TibrvRvdTransport;
import com.tibco.tibrv.TibrvCmQueueTransport;
import java.util.Vector;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.StringTokenizer;

public class AuditLogger extends Thread {

	private Logger logger; //Logger for audit logging
	private static Logger myLogger = Logger.getLogger(AuditLogger.class);
    	private TibrvRvdTransport transport = null;
	private Vector list = new Vector();

	private boolean m_running = true;

	private static String AUDIT_LOG_CONFIG_FILE = "auditlog.config";


	public AuditLogger(String ettxRoot)  throws Exception {
		initialize();
		initializeListeners(ettxRoot);
	}

    	private  synchronized void initialize() throws TibrvException {
		Tibrv.open();
	    	transport = new TibrvRvdTransport();
		logger = Logger.getLogger("com.cisco.ettx.admin.auditLog.eventLog");
	}

	public void initializeListeners(String ettxRoot) throws Exception {
		//read from config file
		File file = new File(ettxRoot,"config");
		file = new File(file,AUDIT_LOG_CONFIG_FILE);
		myLogger.debug("Initializing from config file " + file.getPath());
		FileInputStream in = new FileInputStream(file.getPath());
		Properties prop = new Properties();
		prop.load(in);
		String listeners = prop.getProperty("auditlog.listeners");
		StringTokenizer t = new StringTokenizer(listeners,",");
		while (t.hasMoreElements()) {
			String listener = (String)t.nextElement();
			//Get subject name
			String subject = prop.getProperty("auditlog."+listener+".subject");
			String parser = prop.getProperty("auditlog."+listener+".parser");
			EventParser p = null;
			if (parser != null) {
				//Get the associated class name for the parser
				try {
					p = mapNameToParser(parser);
					p.doConfigure(listener,prop);
				}
				catch (Exception ex) {
                         		myLogger.error("Unable to map parser name " + parser + "to a class. Exception : " + ex.toString());
                         		throw ex;
				}
			}
			register(listener,subject,p);
		}
	}

	private void register(String listener,
		String subject,EventParser parser) throws Exception {
		
		myLogger.debug("Registering " + subject + " for listener " + listener);
		EventHandler eventHandler = new EventHandler(
			logger,listener,parser);
		list.add(new TibrvListener(Tibrv.defaultQueue(),
					  eventHandler,
					  transport,
					  subject,null));
	}

	public class EventHandler implements TibrvMsgCallback {
		private Logger logger = null;
		public String listenerName = null;
		EventParser parser = null;  //Not implemented parser yet

		public EventHandler(Logger llogger,String listener,
			EventParser p) {
			logger = llogger;
			listenerName = listener;
			parser = p;
		}

    		public void onMsg(TibrvListener listener, TibrvMsg tib_msg) {
			String send_subject = tib_msg.getSendSubject();
			myLogger.debug("In AuditLogger.onMsg " + send_subject);
			if (parser != null) {
				parser.logMessage(logger,tib_msg);
			}
			else {
				//No parser for the message. just send it 
				logger.info(tib_msg.toString());
			}
		}
	}

	EventParser mapNameToParser(String parserName) throws Exception {
                   Class cls = Class.forName(parserName);
                   return (EventParser)cls.newInstance();
	}

	    /**
	     * TIB event processing loop
	     */
	    public void run()
	    {
		logger.debug("Started TIB event processing for audit log..");
		// Loop until we are told not to
		while (m_running)
		{
		    try
		    {
			// Check for TIB events pausing every 5 seconds to
			// check if we are still in a run state.
			Tibrv.defaultQueue().timedDispatch(5.0);
		    }
		    catch (TibrvException te)
		    {
			throw new RuntimeException(te.toString());
		    }
		    catch (InterruptedException ie)
		    {
			throw new RuntimeException(ie.toString());
		    }
		}
	    }

	    /**
	     * Running state accessor
	     * @return current run state
	     */
	    public boolean isRunning()
	    {
		return m_running;
	    }

	    /**
	     * Reset the runing state flag so that the run method can
	     * be re-used.
	     */
	    public void reset()
	    {
		m_running = true;
	    }

	    /**
	     * Halt the message processing loop
	     */
	    public void halt()
	    {
		m_running = false;
	    }


}
