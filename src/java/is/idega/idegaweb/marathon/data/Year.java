package is.idega.idegaweb.marathon.data;


import com.idega.user.data.Group;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface Year extends IDOEntity, Group {
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
	 * @see is.idega.idegaweb.marathon.data.YearBMPBean#seCharityEnabled
	 */
	public void setCharityEnabled(boolean charityEnabled);

	public int getPledgedBySponsorPerKilometer();
	public void setPledgedBySponsorPerKilometer(int amount);
}