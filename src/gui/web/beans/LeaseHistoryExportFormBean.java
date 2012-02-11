package com.cisco.ettx.admin.gui.web.beans;

import java.util.Vector;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.struts.action.*;
import com.cisco.ettx.admin.common.*;
import com.cisco.ettx.admin.gui.web.helper.*;
import com.cisco.ettx.admin.gui.web.util.ETTXUtil;
import org.apache.log4j.Logger;

public final class LeaseHistoryExportFormBean
	extends ActionForm {
	public static final String UNABLE_TO_EXECUTE_SERVICE = "Unable to execute service";
	private static Logger logger = Logger.getLogger(LeaseHistoryExportFormBean.class);
	// create device wizard getter/setters
	private String startDay = new String();
	private String startMonth = new String();
	private String startYear = new String();
	private String startTime = new String();
	private String endDay = new String();
	private String endMonth = new String();
	private String endYear = new String();
	private String endTime = new String();
	private String exportFileName = new String();
	private Vector cnrHostList = new Vector();
	private String cnrHost = new String();

	public LeaseHistoryExportFormBean() {
		//create list
	}

	public void setStartDay(String inDay) {
		this.startDay = inDay;
	}

	public String getStartDay() {
		return (this.startDay);
	}

	public void setStartMonth(String inStartMonth) {
		this.startMonth = inStartMonth;
	}

	public String getStartMonth() {
		return (this.startMonth);
	}

	public void setStartTime(String instartTime) {
		this.startTime = instartTime;
	}

	public String getStartTime() {
		return (this.startTime);
	}

	public String getEndMonth() {
		return (this.endMonth);
	}

	public void setEndMonth(String dir) {
		this.endMonth = dir;
	}

	public void setStartYear(String instartYear) {
		this.startYear = instartYear;
	}

	public String getStartYear() {
		return (this.startYear);
	}

	public void setEndDay(String inendDay) {
		this.endDay = inendDay;
	}

	public String getEndDay() {
		return (this.endDay);
	}

	public String getEndYear() {
		return endYear;
	}

	public void setEndYear(String year) {
		endYear = year;
	}

	public void setEndTime(String inendTime) {
		this.endTime = inendTime;
	}

	public String getEndTime() {
		return (this.endTime);
	}

	public void setCnrHost(String host) {
		cnrHost = host;
	}

	public String getCnrHost() {
		return cnrHost;
	}

	public void setCnrHostList(Vector hostList) {
		cnrHostList = hostList;
	}

	public Vector getCnrHostList() {
		return cnrHostList;
	}

	public void setExportFileName(String fileName) {
		exportFileName = fileName;
	}

	public String getExportFileName() {
		return exportFileName;
	}

	public Date getStartPeriod() {
		return getDate(startYear, startMonth, startDay, startTime);
	}

	public Date getEndPeriod() {
		return getDate(endYear, endMonth, endDay, endTime);
	}

	private Date getDate(String year, String month, String day, String time) {
		logger.debug("Converting year " + year + " month " + month + " Day " + day + " time " + time);

		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		StringTokenizer st = new StringTokenizer(time, ":");
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(st.nextToken()));
		cal.set(Calendar.MINUTE, Integer.parseInt(st.nextToken()));
		cal.set(Calendar.SECOND, Integer.parseInt(st.nextToken()));
		return cal.getTime();
	}

	public void setInitialValues(String sessionID) {
		cnrHostList = new Vector();
		ComponentHostData host = null;
		Vector componentHostData;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cnrHostList = LeaseHistoryExportHelper.getCNRHosts(sessionID);
			if (cnrHostList.size() == 0) {
				cnrHostList.addElement("none");
			}
			SystemConfiguration config = SystemConfigHelper.getSystemConfig(sessionID);
			Date currentDate = new Date();
			exportFileName = config.getLeaseHistoryExportDir() + "/manual-export-" + formatter.format(currentDate);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());

			startYear = Integer.toString(calendar.get(Calendar.YEAR));
			startMonth = Integer.toString(calendar.get(Calendar.MONTH));
			startDay = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
			startTime = "00:00:00";

			endYear = Integer.toString(calendar.get(Calendar.YEAR));
			endMonth = Integer.toString(calendar.get(Calendar.MONTH));
			endDay = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
			endTime = "23:59:59";
		}
		catch (AdminServicesException ex) {
			logger.error("Unable to get component config or system config", ex);
			// throw new java.rmi.RemoteException(UNABLE_TO_EXECUTE_SERVICE);
		}
	}

}