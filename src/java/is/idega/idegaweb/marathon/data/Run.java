/*
 * $Id: Run.java,v 1.18 2007/06/21 09:44:42 sigtryggur Exp $
 * Created on May 22, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import com.idega.user.data.Group;


/**
 * Last modified: $Date: 2007/06/21 09:44:42 $ by $Author: sigtryggur $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.18 $
 */
public interface Run extends Group {

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getFamilyDiscount
	 */
	public float getFamilyDiscount();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setFamilyDiscount
	 */
	public void setFamilyDiscount(float discount);

	public Year getCurrentRegistrationYear();
	
	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunHomePage
	 */
	public String getRunHomePage();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setRunHomePage
	 */
	public void setRunHomePage(String runHomePage);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunInformationPage
	 */
	public String getRunInformationPage();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setRunInformationPage
	 */
	public void setRunInformationPage(String runInformationPage);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getEnglishRunInformationPage
	 */
	public String getEnglishRunInformationPage();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setEnglishRunInformationPage
	 */
	public void setEnglishRunInformationPage(String englishRunInformationPage);
}
