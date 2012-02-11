package com.cisco.ettx.admin.gui.web.beans;

import java.util.Vector;
import org.apache.struts.action.*;

public class PostalAddressMapFormBean  extends ActionForm {

    // create device wizard getter/setters
    protected String postalAddress = new String();
    protected String postalState = new String();
    protected String postalCity = new String();
    protected String postalCode = new String();
    protected String portIdentifier = new String();

	public PostalAddressMapFormBean() {
		super();
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

    public void setPostalCity(String inCity) {
        this.postalCity= inCity; 
    }
        
    public String getPostalCity() {
        return(this.postalCity);          
    }	

    public void setPostalCode(String inCode) {
        this.postalCode= inCode; 
    }
        
    public String getPostalCode() {
        return(this.postalCode);          
    }	

    public void setPortIdentifier(String inIdentifier) {
        this.portIdentifier= inIdentifier; 
    }
        
    public String getPortIdentifier() {
        return(this.portIdentifier);          
    }	

	public void resetValues() {
		postalAddress = new String();
		postalState = new String();
		postalCode = new String();
		postalCity = new String();
		portIdentifier = new String();
	}
}
