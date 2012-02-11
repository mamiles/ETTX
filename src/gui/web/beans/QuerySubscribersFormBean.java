package com.cisco.ettx.admin.gui.web.beans;

import java.util.Vector;
import org.apache.struts.action.*;

public class QuerySubscribersFormBean  extends ActionForm {

    // create device wizard getter/setters
    protected String givenName = new String();
    protected String familyName = new String();
    protected String loginID = new String();
    protected String homeNumber = new String();
    protected String mobileNumber = new String();
    protected String postalAddress = new String();
    protected String postalState = new String();
    protected String accountNumber = new String();
    protected String  service = new String();
    protected String switchDetails = new String();
    protected String macAddress = new String();
    protected String macAddressAlias = new String();
	public static final String SUBSCRIBER = "Subscriber";
	public static final String SERVICE = "Service";
	public static final String SWITCH = "Switch";
	public static final String MAC_ADDRESS = "MacAddress";
	protected String currentLayer = new String(SUBSCRIBER);
	protected Vector serviceList = new Vector();

	public QuerySubscribersFormBean() {
		super();
		resetValues();
	}

        
    public void setGivenName(String inSubsName) {
        this.givenName= inSubsName; 
    }
    
    public String getGivenName() {
        return(this.givenName);          
    }
        
    public void setFamilyName(String inSubsName) {
        this.familyName= inSubsName; 
    }
    
    public String getFamilyName() {
        return(this.familyName);          
    }
        
    public void setHomeNumber(String inHomeNumber) {
        this.homeNumber = inHomeNumber;  
    }
            
    public String getHomeNumber() {
        return(this.homeNumber);          
    }
        
    public void setMobileNumber(String inMobileNumber) {
        this.mobileNumber = inMobileNumber;  
    }
            
    public String getMobileNumber() {
        return(this.mobileNumber);          
    }

    public void setSwitchDetails(String inswitchDetails) {
        this.switchDetails= inswitchDetails; 
    }
        
    public String getSwitchDetails() {
        return(this.switchDetails);          
    }	

    public void setPostalAddress(String inaddress) {
        this.postalAddress= inaddress; 
    }
        
    public String getPostalAddress() {
        return(this.postalAddress);          
    }	

    public void setPostalState(String instate) {
        this.postalState= instate; 
    }
        
    public String getPostalState() {
        return(this.postalState);          
    }	

    public void setLoginID(String inloginID) {
        this.loginID= inloginID; 
    }
        
    public String getLoginID() {
        return(this.loginID);          
    }	

    public void setAccountNumber(String inaccountNumber) {
        this.accountNumber= inaccountNumber; 
    }
        
    public String getAccountNumber() {
        return(this.accountNumber);          
    }	

    public void setService(String inservice) {
        this.service= inservice; 
    }
        
    public String getService() {
        return(this.service);          
    }	

	public String getCurrentLayer() {
		return currentLayer;
	}

	public void setCurrentLayer(String inLayer) {
		currentLayer = inLayer;
	}

	public Vector getServiceList() {
		return serviceList;
	}

	public void setServiceList(Vector list) {
		serviceList = list;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String invalue) {
		macAddress = invalue;
	}

	public String getMacAddressAlias() {
		return macAddressAlias;
	}

	public void setMacAddressAlias(String invalue) {
		macAddressAlias = invalue;
	}

	public void resetValues() {
		service = new String();
		givenName = new String();
		familyName = new String();
		homeNumber = new String();
		mobileNumber = new String();
		postalAddress = new String();
		postalState = new String();
		switchDetails = new String();
		accountNumber = new String();
		macAddress = new String();
		macAddressAlias = new String();
	}
}
