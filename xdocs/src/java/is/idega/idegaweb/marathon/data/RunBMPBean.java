/*
 * $Id: RunBMPBean.java,v 1.34 2009/01/13 09:38:28 palli Exp $
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
 * Last modified: $Date: 2009/01/13 09:38:28 $ by $Author: palli $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.34 $
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

	private static final String METADATA_RUN_REGISTRATION_RECEIPT_GREETING = "run_receipt_greeting";
	private static final String METADATA_RUN_REGISTRATION_RECEIPT_GREETING_ENGLISH = "run_receipt_greeting_en";
	
	private static final String METADATA_RUN_REGISTRATION_RECEIPT_INFO = "run_receipt_info";
	private static final String METADATA_RUN_REGISTRATION_RECEIPT_INFO_ENGLISH = "run_receipt_info_en";

	
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

	public void setRunRegistrationReceiptGreeting(String greeting) {
		setMetaData(METADATA_RUN_REGISTRATION_RECEIPT_GREETING, greeting);
	}
	
	public String getRunRegistrationReceiptGreeting() {
		return this.getMetaData(METADATA_RUN_REGISTRATION_RECEIPT_GREETING);
	}

	public void setRunRegistrationReceiptGreetingEnglish(String greeting) {
		setMetaData(METADATA_RUN_REGISTRATION_RECEIPT_GREETING_ENGLISH, greeting);
	}
	
	public String getRunRegistrationReceiptGreetingEnglish() {
		return this.getMetaData(METADATA_RUN_REGISTRATION_RECEIPT_GREETING_ENGLISH);
	}

	public void setRunRegistrationReceiptInfo(String info) {
		setMetaData(METADATA_RUN_REGISTRATION_RECEIPT_INFO, info);
	}
	
	public String getRunRegistrationReceiptInfo() {
		return this.getMetaData(METADATA_RUN_REGISTRATION_RECEIPT_INFO);
	}

	public void setRunRegistrationReceiptInfoEnglish(String info) {
		setMetaData(METADATA_RUN_REGISTRATION_RECEIPT_INFO_ENGLISH, info);
	}
	
	public String getRunRegistrationReceiptInfoEnglish() {
		return this.getMetaData(METADATA_RUN_REGISTRATION_RECEIPT_INFO_ENGLISH);
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
			if (finished && nextYear != null && nextYear.getLastRegistrationDate() != null && thisYearStamp.isEarlierThan(new IWTimestamp(nextYear.getLastRegistrationDate()))) {
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