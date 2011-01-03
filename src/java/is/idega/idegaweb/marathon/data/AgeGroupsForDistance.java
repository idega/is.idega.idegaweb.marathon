package is.idega.idegaweb.marathon.data;


import com.idega.user.data.Gender;
import com.idega.data.IDOEntity;

public interface AgeGroupsForDistance extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.marathon.data.AgeGroupsForDistanceBMPBean#getDistance
	 */
	public DistancesForRun getDistance();

	/**
	 * @see is.idega.idegaweb.marathon.data.AgeGroupsForDistanceBMPBean#getAgeFrom
	 */
	public int getAgeFrom();

	/**
	 * @see is.idega.idegaweb.marathon.data.AgeGroupsForDistanceBMPBean#getAgeTo
	 */
	public int getAgeTo();

	/**
	 * @see is.idega.idegaweb.marathon.data.AgeGroupsForDistanceBMPBean#getGender
	 */
	public Gender getGender();

	/**
	 * @see is.idega.idegaweb.marathon.data.AgeGroupsForDistanceBMPBean#setDistance
	 */
	public void setDistance(DistancesForRun distance);

	/**
	 * @see is.idega.idegaweb.marathon.data.AgeGroupsForDistanceBMPBean#setAgeFrom
	 */
	public void setAgeFrom(int ageFrom);

	/**
	 * @see is.idega.idegaweb.marathon.data.AgeGroupsForDistanceBMPBean#setAgeTo
	 */
	public void setAgeTo(int ageTo);

	/**
	 * @see is.idega.idegaweb.marathon.data.AgeGroupsForDistanceBMPBean#setGender
	 */
	public void setGender(Gender gender);
}