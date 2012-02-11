
package com.cisco.ettx.admin.common;

import java.util.Vector;
import java.beans.Beans;

public class SystemConfiguration extends Beans{
	String speUserID = null;
	String spePassword = null;
	int purgeDur;
	String exportDir;
	boolean leaseHistorySchOn;
	String leaseHistoryExportDir;
	int leaseHistoryExportDay;
	String leaseHistoryExportTime;

	public SystemConfiguration(SystemConfiguration in) {
		String speUserID = in.speUserID;
		String spePassword = in.spePassword;
		int purgeDur = in.purgeDur;
		String exportDir = in.exportDir;
		boolean leaseHistorySchOn = in.leaseHistorySchOn ;
		String leaseHistoryExportDir = in.leaseHistoryExportDir ;
		int leaseHistoryExportDay = in.leaseHistoryExportDay ;
		String leaseHistoryExportTime = in.leaseHistoryExportTime ;
	}

	public SystemConfiguration() {
	}

	public void setSpeUserID(String lid) {
		speUserID = lid;
	}

	public String getSpeUserID() {
		return speUserID;
	}

	public String getSpePassword() {
		return spePassword;
	}

	public void setSpePassword(String lspePassword) {
		spePassword = lspePassword;
	}

	public int getPurgeDur() {
		return purgeDur;
	}

	public void setPurgeDur(int dur) {
		purgeDur = dur;
	}

	public String getExportDir() {
		return exportDir;
	}

	public void setExportDir(String ldir) {
		exportDir = ldir;
	}

	public boolean getLeaseHistorySchOn() {
		return leaseHistorySchOn;
	}

	public void setLeaseHistorySchOn(boolean value) {
		leaseHistorySchOn = value;
	}

       public String getLeaseHistoryExportDir() {
                return leaseHistoryExportDir;
        }


       public void setLeaseHistoryExportDir(String dir) {
                leaseHistoryExportDir = dir;
        }

        public int getLeaseHistoryExportDay() {
                return leaseHistoryExportDay;
        }

        public void setLeaseHistoryExportDay(int day) {
                leaseHistoryExportDay = day;
        }

        public String getLeaseHistoryExportTime() {
                return leaseHistoryExportTime;
        }

        public void setLeaseHistoryExportTime(String ltime) {
                leaseHistoryExportTime = ltime;
        }

	

}
