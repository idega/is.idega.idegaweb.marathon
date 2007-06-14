/*
 * $Id: YearBMPBean.java,v 1.7 2007/06/14 01:15:31 sigtryggur Exp $
 * Created on May 22, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import java.sql.Timestamp;

import com.idega.user.data.Group;
import com.idega.user.data.GroupBMPBean;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2007/06/14 01:15:31 $ by $Author: sigtryggur $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
 */
public class YearBMPBean extends GroupBMPBean  implements Year,Group{

	public static final String METADATA_RUN_DATE = "run_date";
	public static final String METADATA_LAST_REGISTRATION_DATE = "last_registration_date";
	public static final String METADATA_ENABLE_CHARITY = "enable_charity";
	public static final String METADATA_PLEDGED_PER_KILOMETER_BY_SPONSOR = "sponsor_pledge_amount";
	public static final String METADATA_PLEDGED_PER_KILOMETER_BY_SPONSOR_GROUP = "sponsor_pledge_amount_group";

	//private int pledgedBySponsorPerKilometer;
	
	public Timestamp getRunDate() {
		String date = getMetaData(METADATA_RUN_DATE);
		if (date != null) {
			IWTimestamp stamp = new IWTimestamp(date);
			return stamp.getTimestamp();
		}
		return null;
	}

	public Timestamp getLastRegistrationDate() {
		String date = getMetaData(METADATA_LAST_REGISTRATION_DATE);
		if (date != null) {
			IWTimestamp stamp = new IWTimestamp(date);
			return stamp.getTimestamp();
		}
		return null;
	}

	public void setRunDate(Timestamp date) {
		IWTimestamp stamp = new IWTimestamp(date);
		setMetaData(METADATA_RUN_DATE, stamp.toSQLString(), "java.sql.Timestamp");
	}

	public void setLastRegistrationDate(Timestamp date) {
		IWTimestamp stamp = new IWTimestamp(date);
		setMetaData(METADATA_LAST_REGISTRATION_DATE, stamp.toSQLString(), "java.sql.Timestamp");
	}
	
	public boolean isCharityEnabled() {
		String enable = this.getMetaData(METADATA_ENABLE_CHARITY);
		if (enable != null) {
			return new Boolean(enable).booleanValue();
		}
		return false;
	}

	public void setCharityEnabled(boolean charityEnabled) {
		setMetaData(METADATA_ENABLE_CHARITY, String.valueOf(charityEnabled), "java.lang.Boolean");
	}

	public int getPledgedBySponsorPerKilometer() {
		String sAmount = getMetaData(METADATA_PLEDGED_PER_KILOMETER_BY_SPONSOR);
		int amount=-1;
		try{
			amount = Integer.parseInt(sAmount);
		}
		catch(Exception e){}
		return amount;
	}

	public void setPledgedBySponsorPerKilometer(int pledgedBySponsorPerKilometer) {
		setMetaData(METADATA_PLEDGED_PER_KILOMETER_BY_SPONSOR,String.valueOf(pledgedBySponsorPerKilometer));
	}
	
	public int getPledgedBySponsorGroupPerKilometer() {
		String sAmount = getMetaData(METADATA_PLEDGED_PER_KILOMETER_BY_SPONSOR_GROUP);
		int amount=-1;
		try{
			amount = Integer.parseInt(sAmount);
		}
		catch(Exception e){}
		return amount;
	}

	public void setPledgedBySponsorGroupPerKilometer(int pledgedBySponsorGroupPerKilometer) {
		setMetaData(METADATA_PLEDGED_PER_KILOMETER_BY_SPONSOR_GROUP,String.valueOf(pledgedBySponsorGroupPerKilometer));
	}

	public String getYearString() {
		return getName();
	}
	
	
}