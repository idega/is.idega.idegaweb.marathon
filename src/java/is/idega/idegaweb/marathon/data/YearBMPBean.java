/*
 * $Id: YearBMPBean.java,v 1.4 2007/06/04 13:48:18 tryggvil Exp $
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
 * Last modified: $Date: 2007/06/04 13:48:18 $ by $Author: tryggvil $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public class YearBMPBean extends GroupBMPBean  implements Year,Group{

	public static final String METADATA_RUN_DATE = "run_date";
	public static final String METADATA_LAST_REGISTRATION_DATE = "last_registration_date";
	public static final String METADATA_ENABLE_CHARITY = "enable_charity";

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
	
	
}