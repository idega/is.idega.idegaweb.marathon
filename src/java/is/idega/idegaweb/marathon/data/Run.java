package is.idega.idegaweb.marathon.data;


import com.idega.user.data.Group;

public interface Run extends Group {
	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getFamilyDiscount
	 */
	public float getFamilyDiscount();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setFamilyDiscount
	 */
	public void setFamilyDiscount(float discount);

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

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setRunRegistrationReceiptGreeting
	 */
	public void setRunRegistrationReceiptGreeting(String greeting);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunRegistrationReceiptGreeting
	 */
	public String getRunRegistrationReceiptGreeting();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setRunRegistrationReceiptGreetingEnglish
	 */
	public void setRunRegistrationReceiptGreetingEnglish(String greeting);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunRegistrationReceiptGreetingEnglish
	 */
	public String getRunRegistrationReceiptGreetingEnglish();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setRunRegistrationReceiptInfo
	 */
	public void setRunRegistrationReceiptInfo(String info);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunRegistrationReceiptInfo
	 */
	public String getRunRegistrationReceiptInfo();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setRunRegistrationReceiptInfoEnglish
	 */
	public void setRunRegistrationReceiptInfoEnglish(String info);

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getRunRegistrationReceiptInfoEnglish
	 */
	public String getRunRegistrationReceiptInfoEnglish();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getCurrentRegistrationYear
	 */
	public Year getCurrentRegistrationYear();
}