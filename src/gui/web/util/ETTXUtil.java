

package com.cisco.ettx.admin.gui.web.util;

import org.apache.struts.action.*;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import org.apache.log4j.Logger;
import javax.servlet.http.*;
import com.cisco.ettx.admin.common.AdminServicesException;
import com.cisco.ettx.admin.common.AdminServicesNotification;


public class ETTXUtil {
	private static String BASE_NAME ="com.cisco.ettx.admin.i18n.ettx";
	private static String USER_LOCALE_LANG = "userLocaleLanguage";
	private static String USER_LOCALE_COUNTRY = "userLocaleCountry";
	private static String DEFAULT_LANG = "en";
	private static String DEFAULT_COUNTRY = "US";
	private static ResourceBundle bundle = null;
	private static String MESSAGE = "SERVER.FAILURE";
	private static Logger logger = Logger.getLogger(ETTXUtil.class);
		//REVISIT how do we localize this message need UII support

    	public static final String SESSION_AUTHENTICATION = "AuthenticationSessionID";
    	public static final String AUTHENTICATION_ERROR = "AuthenticationError";
	private static String UNKNOWN_ERROR = "Unknown Error";

	public static ActionError convertToError(Exception ex) {
		return new ActionError(MESSAGE,localizeMessage(ex));
	}

	public static Locale getLocale() {
		String lang = System.getProperty(USER_LOCALE_LANG);
		String country = System.getProperty(USER_LOCALE_COUNTRY);
		if (lang == null) {
			logger.warn("No locale set for system Using default..");
			lang = DEFAULT_LANG;
			country = DEFAULT_COUNTRY;
		}
		return new Locale(lang,country);
		

	}

	public static String localizeMessage(Exception ex) {
		if (bundle == null) {
			bundle = ResourceBundle.getBundle(BASE_NAME,getLocale());
		}
		String message = null;
		String key =  null;
		if (ex instanceof AdminServicesException) {
			AdminServicesException e = (AdminServicesException)ex;
			key = e.getErrorCode();
		}
		else if (ex instanceof AdminServicesNotification) {
			AdminServicesNotification e = (AdminServicesNotification)ex;
			key = e.getNotification();
		}
		else {
			key = ex.getMessage();
		}
		try {
			message = bundle.getString(key);
		}
		catch (MissingResourceException e) {
			logger.warn("Resource missing for key " + key);
			message = key;
		}
		if (message == null) message = UNKNOWN_ERROR;
		return message;
	}

	public static String localizeMessage(String key) {
		if (bundle == null) {
			bundle = ResourceBundle.getBundle(BASE_NAME,getLocale());
		}
		String message;
		try {
			message = bundle.getString(key);
		}
		catch (MissingResourceException e) {
			logger.warn("Resource missing for key " + key);
			message = key;
		}
		return message;
	}

	public static String getSessionID(HttpServletRequest request) {
	    HttpSession session = request.getSession();
		return (String)session.getAttribute(SESSION_AUTHENTICATION);
	}

	public static void setSessionID(HttpServletRequest request,String sessionID) {
	    HttpSession session = request.getSession();
		session.setAttribute(SESSION_AUTHENTICATION,sessionID);
	}

}
