/*
 * $Id: YearBMPBean.java,v 1.11 2007/06/28 13:34:57 tryggvil Exp $
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
 * Last modified: $Date: 2007/06/28 13:34:57 $ by $Author: tryggvil $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.11 $
 */
public class YearBMPBean extends GroupBMPBean  implements Year,Group{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 187432696291773052L;
	public static final String METADATA_RUN_DATE = "run_date";
	public static final String METADATA_LAST_REGISTRATION_DATE = "last_registration_date";
	public static final String METADATA_SPONSORED_RUN = "sponsored_run";
	public static final String METADATA_ENABLE_CHARITY = "enable_charity";
	public static final String METADATA_PLEDGED_PER_KILOMETER_BY_SPONSOR = "sponsor_pledge_amount";
	public static final String METADATA_PLEDGED_PER_KILOMETER_BY_SPONSOR_GROUP = "sponsor_pledge_amount_group";
	public static final String METADATA_PLEDGE_CURRENCY = "pledge_currency";
	public static final String METADATA_MINIMUM_AGE_FOR_RUN = "minimum_age_for_run";

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
	
	public boolean isSponsoredRun() {
		String sponsoredRun = this.getMetaData(METADATA_SPONSORED_RUN);
		if (sponsoredRun != null) {
			return new Boolean(sponsoredRun).booleanValue();
		}
		return false;
	}

	public void setSponsoredRun(boolean sponsoredRun) {
		setMetaData(METADATA_SPONSORED_RUN, String.valueOf(sponsoredRun), "java.lang.Boolean");
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

	public String getPledgeCurrency() {
		return getMetaData(METADATA_PLEDGE_CURRENCY);
	}

	public void setPledgeCurrency(String pledgeCurrency) {
		setMetaData(METADATA_PLEDGE_CURRENCY, pledgeCurrency);
	}

	
	public int getMinimumAgeForRun() {
		String sMinimumAge = getMetaData(METADATA_MINIMUM_AGE_FOR_RUN);
		int minimumAge=-1;
		try{
			minimumAge = Integer.parseInt(sMinimumAge);
		}
		catch(Exception e){}
		return minimumAge;
	}

	public void setMinimumAgeForRun(int minimumAgeForRun) {
		setMetaData(METADATA_MINIMUM_AGE_FOR_RUN, String.valueOf(minimumAgeForRun));
	}

	public String getYearString() {
		return getName();
	}
	
	
}