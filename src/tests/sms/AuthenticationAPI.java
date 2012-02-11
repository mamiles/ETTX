/**
 * Class: SecurityAPI
 *
 * Created: 09 December 2002
 *
 * Copyright (c) 2002 by Cisco Systems, Inc. All rights reserved.
 *
 * @author Paul Curren
 *
 **/

package com.cisco.cns.security.soap.authentication;
import com.cisco.cns.security.soap.common.*;
import com.cisco.cns.security.soap.common.*;
import java.util.Properties;
import java.io.FileInputStream;
import org.apache.log4j.Logger;

/**
 * The API to Security SPI implementation.
 *
 * @author <a href="mailto:pcurren@cisco.com">Paul Curren</a>
 */
public class AuthenticationAPI {

	private Properties prop = null;
	private String configUserName = null;
	private String configPassword = null;
	private String csrUserName = null;
	private String csrPassword = null;
	private static Logger logger = Logger.getLogger(AuthenticationAPI.class);

    public SResponse login(String username, String password)
        throws Exception {

        if ((username == null) || (username.length() == 0))  {
		throw new NotAuthorizedException();
	}
	if (prop == null) {
		prop = new Properties();
		FileInputStream f = new FileInputStream("spe.properties");
		prop.load(f);
		configUserName = prop.getProperty("spe.userName");
		configPassword = prop.getProperty("spe.password");
		csrUserName = prop.getProperty("csr.userName");
		csrPassword = prop.getProperty("csr.password");
	}

	if (username.equals(configUserName)) {
		if (!password.equals(configPassword)) 
			throw new NotAuthorizedException();
	}
	else if (username.equals(csrUserName)) {
		if (!password.equals(csrPassword)) 
			throw new NotAuthorizedException();
	}
	else throw new NotAuthorizedException();
	SecurityToken token = new SecurityToken();
	token.setToken(username.getBytes());
	SResponse resp = new SResponse();
	resp.setToken(token);
	resp.setStatus(EResponseStatus.SUCCESS);
	return resp;
    }

    public void logout(SecurityToken token) {
	}

    public void checkPermission(SecurityToken token,SecurityToken userToken,
		SPermission[] permissions)
        throws Exception {

	if (userToken == null) throw new NotAuthorizedException();
	String username = new String(userToken.getToken());
	if (prop == null) {
		prop = new Properties();
		FileInputStream f = new FileInputStream("spe.properties");
		prop.load(f);
		configUserName = prop.getProperty("spe.userName");
		configPassword = prop.getProperty("spe.password");
		csrUserName = prop.getProperty("csr.userName");
		csrPassword = prop.getProperty("csr.password");
	}
	logger.info("Checking permission for " + username);
	if (username.equals(csrUserName)) {
		return;
	}
	logger.info("User was not a CSR user " + csrUserName);
	throw new InsufficientPrivilegeException();
    }

}
