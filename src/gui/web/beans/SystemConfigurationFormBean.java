package com.cisco.ettx.admin.gui.web.beans;

import java.util.Vector;
import java.util.StringTokenizer;
import org.apache.struts.action.*;

import org.apache.log4j.Logger;
import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import com.cisco.ettx.admin.common.*;
import com.cisco.ettx.admin.gui.web.helper.SystemConfigHelper;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;

public final class SystemConfigurationFormBean  extends ActionForm {

    private Logger logger = Logger.getLogger(SystemConfigurationFormBean.class);

    // create device wizard getter/setters
	private SystemConfiguration systemConfig = new SystemConfiguration();
	private String confirmPassword = new String();
	public static final String UNABLE_TO_EXECUTE_SERVICE = "Unable to execute service";


    public SystemConfigurationFormBean()
    {
    }

    public void setConfirmPassword(String inconfirmPassword) {
        this.confirmPassword= inconfirmPassword; 
    }
        
    public String getConfirmPassword() {
        return(this.confirmPassword);          
    }	

    public void setSystemConfig(SystemConfiguration insystemConfig) {
        this.systemConfig= insystemConfig; 
    }
        
    public SystemConfiguration getSystemConfig() {
        return(systemConfig);
    }	

	public String getLeaseExportSch() {
		if (systemConfig.getLeaseHistorySchOn() == true) {
			return "on";
		}
		return "off";
	}

	public void setLeaseExportSch(String value) {
		boolean leaseExportOn = false;
		if (value.equals("on")) {
			leaseExportOn = true;
		}
		systemConfig.setLeaseHistorySchOn(leaseExportOn);
	}

	public void saveNewConfiguration(String sessionID) throws AdminServicesException
	{
	    logger.debug("Saving new configuration");

		SystemConfigHelper.setSystemConfig(sessionID,systemConfig);
	}

	public void loadingSystemConfiguration(String sessionID) throws AdminServicesException
	{
	    logger.debug("Loading SystemConfiguration from server");
		logger.debug("Get Configuration Service...");
		systemConfig =  SystemConfigHelper.getSystemConfig(sessionID);

		confirmPassword = systemConfig.getSpePassword();
	}
		
	    
}
