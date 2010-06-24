package is.idega.idegaweb.marathon.data;


import com.idega.data.IDOEntity;
import java.util.Locale;
import com.idega.user.data.Group;

public interface Distance extends Group {
	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#isUseChip
	 */
	public boolean isUseChip();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setUseChip
	 */
	public void setUseChip(boolean useChip);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#isAskQuestions
	 */
	public boolean isAskQuestions();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setAskQuestions
	 */
	public void setAskQuestions(boolean askQuestions);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#getPrice
	 */
	public float getPrice(Locale locale);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setPriceInISK
	 */
	public void setPriceInISK(float price);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setPriceInEUR
	 */
	public void setPriceInEUR(float price);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#getChildrenPrice
	 */
	public float getChildrenPrice(Locale locale);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setChildrenPriceInISK
	 */
	public void setChildrenPriceInISK(float price);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setChildrenPriceInEUR
	 */
	public void setChildrenPriceInEUR(float price);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#getPriceForTransport
	 */
	public float getPriceForTransport(Locale locale);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setPriceForTransportInISK
	 */
	public void setPriceForTransportInISK(float price);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setPriceForTransportInEUR
	 */
	public void setPriceForTransportInEUR(float price);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#isFamilyDiscount
	 */
	public boolean isFamilyDiscount();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setFamilyDiscount
	 */
	public void setFamilyDiscount(boolean discount);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#isAllowsGroups
	 */
	public boolean isAllowsGroups();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setAllowsGroups
	 */
	public void setAllowsGroups(boolean allowsGroups);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#getNextAvailableParticipantNumber
	 */
	public int getNextAvailableParticipantNumber();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setNextAvailableParticipantNumber
	 */
	public void setNextAvailableParticipantNumber(int number);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#getNumberOfSplits
	 */
	public int getNumberOfSplits();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setNumberOfSplits
	 */
	public void setNumberOfSplits(int number);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#getDistanceInKms
	 */
	public int getDistanceInKms();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#getYear
	 */
	public Year getYear();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#getMinimumAgeForDistance
	 */
	public int getMinimumAgeForDistance();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setMinimumAgeForDistance
	 */
	public void setMinimumAgeForDistance(int minimumAgeForDistance);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#getMaximumAgeForDistance
	 */
	public int getMaximumAgeForDistance();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setMaximumAgeForDistance
	 */
	public void setMaximumAgeForDistance(int maximumAgeForDistance);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#getMinimumParticipantNumberForDistance
	 */
	public int getMinimumParticipantNumberForDistance();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setMinimumParticipantNumberForDistance
	 */
	public void setMinimumParticipantNumberForDistance(
			int minimumParticipantForDistance);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#getMaximumParticipantNumberForDistance
	 */
	public int getMaximumParticipantNumberForDistance();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setMaximumParticipantNumberForDistance
	 */
	public void setMaximumParticipantNumberForDistance(
			int maximumParticipantForDistance);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#isRelayDistance
	 */
	public boolean isRelayDistance();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistanceBMPBean#setIsRelayDistance
	 */
	public void setIsRelayDistance(boolean isRelayDistance);
}