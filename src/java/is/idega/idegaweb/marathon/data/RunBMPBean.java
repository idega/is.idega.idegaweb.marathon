/*
 * $Id: RunBMPBean.java,v 1.33 2007/09/26 08:05:23 laddi Exp $
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

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.data.GroupBMPBean;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2007/09/26 08:05:23 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.33 $
 */
public class RunBMPBean extends GroupBMPBean  implements Run{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -8363802419171254109L;
	private static final String METADATA_FAMILY_DISCOUNT = "family_discount";
	private static final String METADATA_RUN_HOME_PAGE = "run_home_page";
	private static final String METADATA_RUN_INFORMATION_PAGE = "run_information_page";
	private static final String METADATA_ENGLISH_RUN_INFORMATION_PAGE = "english_run_information_page";

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

	public String getRunHomePage() {
		return this.getMetaData(METADATA_RUN_HOME_PAGE);
	}

	public void setRunHomePage(String runHomePage) {
		setMetaData(METADATA_RUN_HOME_PAGE, runHomePage);
	}
	
	public String getRunInformationPage() {
		return this.getMetaData(METADATA_RUN_INFORMATION_PAGE);
	}

	public void setRunInformationPage(String runInformationPage) {
		setMetaData(METADATA_RUN_INFORMATION_PAGE, runInformationPage);
	}

	public String getEnglishRunInformationPage() {
		return this.getMetaData(METADATA_ENGLISH_RUN_INFORMATION_PAGE);
	}

	public void setEnglishRunInformationPage(String englishRunInformationPage) {
		setMetaData(METADATA_ENGLISH_RUN_INFORMATION_PAGE, englishRunInformationPage);
	}

	public Year getCurrentRegistrationYear() {
		IWTimestamp thisYearStamp = IWTimestamp.RightNow();
		String yearString = String.valueOf(thisYearStamp.getYear());
		IWTimestamp nextYearStamp = IWTimestamp.RightNow();
		nextYearStamp.addYears(1);
		String nextYearString = String.valueOf(nextYearStamp.getYear());
		
//		String runnerYearString = yearString;
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
//				runnerYearString = nextYearString;
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
			return (RunBusiness) IBOLookup.getServiceInstance(iwc, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new RuntimeException(e);
		}
	}
}