/*
 * $Id: RunBMPBean.java,v 1.28 2007/06/07 23:30:19 tryggvil Exp $
 * Created on May 22, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import is.idega.idegaweb.marathon.business.RunBusiness;
import java.rmi.RemoteException;
import java.util.Map;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.user.data.GroupBMPBean;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2007/06/07 23:30:19 $ by $Author: tryggvil $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.28 $
 */
public class RunBMPBean extends GroupBMPBean  implements Run{

	private static final String METADATA_FAMILY_DISCOUNT = "family_discount";

	public float getFamilyDiscount() {
		String discount = this.getMetaData(METADATA_FAMILY_DISCOUNT);
		if (discount != null) {
			return Float.parseFloat(discount);
		}
		return 0;
	}

	public void setFamilyDiscount(float discount) {
		setMetaData(METADATA_FAMILY_DISCOUNT, String.valueOf(discount), "java.lang.Float");
	}

	public Year getCurrentRegistrationYear() {
		IWTimestamp thisYearStamp = IWTimestamp.RightNow();
		String yearString = String.valueOf(thisYearStamp.getYear());
		IWTimestamp nextYearStamp = IWTimestamp.RightNow();
		nextYearStamp.addYears(1);
		String nextYearString = String.valueOf(nextYearStamp.getYear());
		
		String runnerYearString = yearString;
		boolean finished = false;
		IWContext iwc = IWContext.getInstance();
		Map yearMap;
		Year theReturn = null;
		try {
			yearMap = getRunBusiness(iwc).getYearsMap(this);
			Year year = (Year) yearMap.get(yearString);
			theReturn = year;
			if (year != null && year.getLastRegistrationDate() != null) {
				if (thisYearStamp.isLaterThanOrEquals(new IWTimestamp(year.getLastRegistrationDate()))) {
					finished = true;
				}
			}
			Year nextYear = (Year) yearMap.get(nextYearString);
			if (finished && nextYear != null) {
				runnerYearString = nextYearString;
				theReturn=nextYear;
			}

		}
		catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return theReturn;
	}

	private RunBusiness getRunBusiness(IWContext iwc) {
		try {
			return (RunBusiness) IDOLookup.getServiceInstance(iwc, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new RuntimeException(e);
		}
	}
}