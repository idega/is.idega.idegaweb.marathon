package is.idega.idegaweb.marathon.data;


import java.sql.Timestamp;

import com.idega.user.data.Group;

public interface Year extends Group {
	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#getRunDate
	 */
	public Timestamp getRunDate();

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#getLastRegistrationDate
	 */
	public Timestamp getLastRegistrationDate();

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#setRunDate
	 */
	public void setRunDate(Timestamp date);

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#setLastRegistrationDate
	 */
	public void setLastRegistrationDate(Timestamp date);

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#isCharityEnabled
	 */
	public boolean isCharityEnabled();

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#setCharityEnabled
	 */
	public void setCharityEnabled(boolean charityEnabled);

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#isSponsoredRun
	 */
	public boolean isSponsoredRun();

	/**
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#setSponsoredRun
	 */
	public void setSponsoredRun(boolean sponsoredRun);

	public int getPledgedBySponsorPerKilometer();
	public void setPledgedBySponsorPerKilometer(int amount);
	
	public int getPledgedBySponsorGroupPerKilometer();
	public void setPledgedBySponsorGroupPerKilometer(int amount);

	public String getPledgeCurrency();
	public void setPledgeCurrency(String currency);

	public int getMinimumAgeForRun();
	public void setMinimumAgeForRun(int minimumAgeForRun);
	public int getMaximumAgeForRun();
	public void setMaximumAgeForRun(int maximumAgeForRun);
	
	public String getYearString();
}