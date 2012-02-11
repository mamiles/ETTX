

package com.cisco.ettx.admin.server.axis.spe;

import org.apache.log4j.Logger;
import com.cisco.cns.security.soap.authentication.*;
import com.cisco.cns.security.soap.authorization.*;
import com.cisco.cns.security.soap.common.*;
import java.rmi.RemoteException;
import com.cisco.ettx.admin.common.SystemConfiguration;
import com.cisco.ettx.admin.config.SystemConfigManager;
import org.apache.axis.AxisFault;


public class AuthenticateHelper extends SPEAPI {
	private static Logger logger = Logger.getLogger(AuthenticateHelper.class);
	private static AuthenticationManager stub = null;
	private static AuthorizationManager authStub = null;
	private static SecurityToken rootToken = null;
	public static final String AUTHENTICATE_SERVICE = "Authentication";
	public static final String AUTHORIZATION_SERVICE = "Authorization";
	public static final String APPLICATION = "CiscoSMS";
	public static final String PERMISSION_TARGET = "SMC";
	public static final String PERMISSION_TYPE = "STRING";

	private static AuthenticationManager getStub() throws javax.xml.rpc.ServiceException,AuthenticationException {
		if (stub != null) return stub;
		try {
			AuthenticationManagerServiceLocator loc = new AuthenticationManagerServiceLocator();
			loc.setEngine(getEngine());
			stub = loc.getAuthentication(getURL(AUTHENTICATE_SERVICE));
			((AuthenticationSoapBindingStub)stub).setTimeout(getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to get the engine configuration for Axis",ex);
			throw new AuthenticationException(AuthenticationException.UNABLE_TO_EXECUTE_SERVICE);
		}
		return stub;
	}

	private static AuthorizationManager getAuthorizationStub() throws javax.xml.rpc.ServiceException,AuthenticationException {
		if (authStub != null) return authStub;
		try {
			AuthorizationManagerServiceLocator loc = new AuthorizationManagerServiceLocator();
			loc.setEngine(getEngine());
			authStub = loc.getAuthorization(getURL(AUTHORIZATION_SERVICE));
			((AuthorizationSoapBindingStub)authStub).setTimeout(getTimeout());
		}
		catch (org.apache.axis.ConfigurationException ex) {
			logger.error("Unable to get the engine configuration for Axis",ex);
			throw new AuthenticationException(AuthenticationException.UNABLE_TO_EXECUTE_SERVICE);
		}
		return authStub;
	}

	public static void logout(String sessionID) throws AuthenticationException {
		try {
			AuthenticationManager stub = getStub();
/*
			stub.unAuthenticate(getSecurityToken(sessionID));
*/
			stub.logout(getSecurityToken(sessionID));
			removeSecurityToken(sessionID);
		}
		catch (javax.xml.rpc.ServiceException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AuthenticationException(AuthenticationException.UNABLE_TO_EXECUTE_SERVICE);
		}
		catch (java.rmi.RemoteException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AuthenticationException(AuthenticationException.UNABLE_TO_EXECUTE_SERVICE);
		}
		
	}

	private static synchronized SecurityToken getRootToken() throws AuthenticationException {
		if (rootToken != null) return rootToken;
		try {
			logger.debug("Logging into SPE System");
			SystemConfiguration sysConfig = SystemConfigManager.getInstance().getSystemConfiguration();
			AuthenticationManager stub = getStub();
			SResponse resp = stub.login(sysConfig.getSpeUserID(),sysConfig.getSpePassword());
			if (resp.getStatus() != EResponseStatus.SUCCESS) {
				logger.error("Unable to authenticate user " +resp.getStatus());
				throw new AuthenticationException(AuthenticationException.AUTHENTICATION_FAILURE);
			}
			rootToken =  resp.getToken();
			return rootToken;
		}
		catch (NotAuthorizedException ex) {
			throw new AuthenticationException(AuthenticationException.AUTHENTICATION_FAILURE);
		}
		catch (InvalidUserOrCredentialException ex) {
			logger.error("Unable to authenticate user " ,ex);
			throw new AuthenticationException(AuthenticationException.AUTHENTICATION_FAILURE);
		}
		catch (javax.xml.rpc.ServiceException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AuthenticationException(AuthenticationException.UNABLE_TO_EXECUTE_SERVICE);
		}
		catch (AxisFault ex) {
			logger.error("Unable to connect to SPE",ex);
			throw new AuthenticationException(AuthenticationException.UNABLE_TO_CONNECT_TO_SPE);
		}
		catch (java.rmi.RemoteException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AuthenticationException(AuthenticationException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static String login(String username,String password) throws AuthenticationException {
		try {
			AuthenticationManager stub = getStub();
/*
			SResponse resp = stub.authenticate(getRootToken(),
				username,password);
*/
			SResponse resp = stub.login(username,password);
			if (resp.getStatus() != EResponseStatus.SUCCESS) {
				logger.error("Unable to authenticate user " + username + " Response was " + resp.getStatus());
				throw new AuthenticationException(AuthenticationException.AUTHENTICATION_FAILURE);
			}
			String sessionID = createSecurityToken(resp.getToken());
			logger.debug("Created session " + sessionID + " for token " + resp.getToken());
			return sessionID;
		}
		catch (NotAuthorizedException ex) {
			logger.error("User " + username + " cannot be authorized",ex);
			throw new AuthenticationException(AuthenticationException.AUTHENTICATION_FAILURE);
		}
		catch (InvalidUserOrCredentialException ex) {
			logger.error("Unable to authenticate user " + username ,ex);
			throw new AuthenticationException(AuthenticationException.AUTHENTICATION_FAILURE);
		}
		catch (javax.xml.rpc.ServiceException ex) {
			logger.error("2 Unable to get service executed",ex);
			throw new AuthenticationException(AuthenticationException.UNABLE_TO_EXECUTE_SERVICE);
		}
		catch (AxisFault ex) {
			logger.error("Unable to connect to SPE",ex);
			throw new AuthenticationException(AuthenticationException.UNABLE_TO_CONNECT_TO_SPE);
		}
		catch (java.rmi.RemoteException ex) {
			logger.error("3 Unable to get service executed",ex);
			throw new AuthenticationException(AuthenticationException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

	public static void validateSession(String sessionID) 
		throws AuthenticationException {
		if (getSecurityToken(sessionID) == null) {
			///For now lets verify if there is a valid token
			logger.error("There is no Security Token associated with the session " + sessionID);
			throw new AuthenticationException(AuthenticationException.NOT_AUTHORIZED);
		}
	}

	public static boolean checkPermission(String sessionID,String[] tokens) throws AuthenticationException {
		try {
			AuthorizationManager stub = getAuthorizationStub();
			SPermission[] permTokens = new SPermission[tokens.length];
			for (int i = 0; i < tokens.length; i++) {
				permTokens[i] = new SPermission();
				permTokens[i].setType(PERMISSION_TYPE);
				permTokens[i].setAccessRights(tokens[i]);
				permTokens[i].setTarget(PERMISSION_TARGET);
				permTokens[i].setApplication(APPLICATION);
			}
			SecurityToken secToken = getSecurityToken(sessionID);
			stub.checkPermission(secToken,secToken, permTokens);
			return true;
		}
		catch (InsufficientPrivilegeException ex) {
			logger.error("Not sufficient permission",ex);
			return false;
		}
		catch (javax.xml.rpc.ServiceException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AuthenticationException(AuthenticationException.UNABLE_TO_EXECUTE_SERVICE);
		}
		catch (java.rmi.RemoteException ex) {
			logger.error("Unable to get service executed",ex);
			throw new AuthenticationException(AuthenticationException.UNABLE_TO_EXECUTE_SERVICE);
		}
	}

}
