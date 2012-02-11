package com.cisco.ettx.admin.gui.web.beans;

import java.lang.Integer;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import org.apache.struts.action.*;

public final class LeaseHistoryQueryFormBean  extends ActionForm {

    // create device wizard getter/setters
    private String loginID = new String();
    private String ipAddress = new String();
    private String startDay = new String();
    private String startMonth = new String();
    private String startYear = new String();
    private String startTime = new String();
    private String endDay = new String();
    private String endMonth = new String();
    private String endYear = new String();
    private String endTime = new String();
	public static final String SUBSCRIBER = "Subscriber";
	public static final String IP_ADDRESS = "IP Address";
    private String currentLayer = new String (SUBSCRIBER);


    public LeaseHistoryQueryFormBean() {
	//create list
	setInitialValues();
	System.out.println (startDay + " " + startMonth + " " + startYear);
    }
        
    public void setLoginID (String loginID) {
        this.loginID = loginID; 
    }
    
    public String getLoginID() {
        return loginID;
    }

    public void setIpAddress (String ipAddress) {
        this.ipAddress = ipAddress; 
    }
    
    public String getIpAddress() {
        return ipAddress;
    }

    public void setStartDay(String inDay) {
        this.startDay= inDay; 
    }
    
    public String getStartDay() {
        return(this.startDay);          
    }
        
    public void setStartMonth(String inStartMonth) {
        this.startMonth = inStartMonth;  
    }
            
    public String getStartMonth() {
        return(this.startMonth);          
    }

    public void setStartYear(String instartYear) {
        this.startYear= instartYear; 
    }
        
    public String getStartYear() {
        return(this.startYear);          
    }	

    public void setStartTime(String instartTime) {
        this.startTime= instartTime; 
    }
        
    public String getStartTime() {
        return(this.startTime);          
    }	

    public void setEndDay(String inendDay) {
        this.endDay= inendDay; 
    }
        
    public String getEndDay() {
        return(this.endDay);          
    }	

    public String getEndMonth() {
        return(this.endMonth);          
    }	

    public void setEndMonth(String dir) {
        this.endMonth = dir;
    }	

    public String getEndYear() {
	return endYear;
    }

    public void setEndYear(String year) {
	endYear = year;
    }

    public void setEndTime(String inendTime) {
        this.endTime= inendTime; 
    }
        
    public String getEndTime() {
        return(this.endTime);          
    }	

    public String getCurrentLayer() {
	return currentLayer;
    }

    public void setCurrentLayer(String inLayer) {
	currentLayer = inLayer;
    }

	public Calendar getStartPeriod() {
		return getDate(startYear,startMonth,startDay,startTime);
	}

	private Calendar getDate(String year,String month,String day,String time) {
		System.out.println("Converting year " + year + " month " + month + " Day " + day + " time " + time);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime (new Date());
		Integer value = Integer.valueOf(year);
		calendar.set(Calendar.YEAR,value.intValue()-1900);
		value = Integer.valueOf(month);
		calendar.set(Calendar.MONTH,value.intValue()-1);
		value = Integer.valueOf(day);
		calendar.set(Calendar.DAY_OF_MONTH,value.intValue());
		StringTokenizer t = new StringTokenizer(time,":");
		value = Integer.valueOf((String)t.nextToken());
		calendar.set(Calendar.HOUR_OF_DAY,value.intValue());
		value = Integer.valueOf((String)t.nextToken());
		calendar.set(Calendar.MINUTE,value.intValue());
		return calendar;

	}

	public Calendar getEndPeriod() {
		return getDate(endYear,endMonth,endDay,endTime);
	}

    private void setInitialValues() {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime (new Date());
	
	startYear = Integer.toString (calendar.get (Calendar.YEAR));
	startMonth = Integer.toString(calendar.get (Calendar.MONTH));
	startDay = Integer.toString (calendar.get (Calendar.DAY_OF_MONTH));
			startTime = "00:00:00";

	endYear = Integer.toString (calendar.get (Calendar.YEAR));
	endMonth = Integer.toString(calendar.get (Calendar.MONTH));
	endDay = Integer.toString (calendar.get (Calendar.DAY_OF_MONTH));
			endTime = "23:59:59";
    }
}
