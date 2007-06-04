package is.idega.idegaweb.marathon.data;


import com.idega.data.IDOEntity;

public interface Pledge extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.marathon.data.PledgeBMPBean#getParticipantID
	 */
	public String getParticipantID();

	/**
	 * @see is.idega.idegaweb.marathon.data.PledgeBMPBean#getOrganizationalID
	 */
	public String getOrganizationalID();

	/**
	 * @see is.idega.idegaweb.marathon.data.PledgeBMPBean#getCardholderName
	 */
	public String getCardholderName();

	/**
	 * @see is.idega.idegaweb.marathon.data.PledgeBMPBean#getAmountPayed
	 */
	public String getAmountPayed();

	/**
	 * @see is.idega.idegaweb.marathon.data.PledgeBMPBean#getPaymentTimestamp
	 */
	public String getPaymentTimestamp();

	/**
	 * @see is.idega.idegaweb.marathon.data.PledgeBMPBean#setParticipantID
	 */
	public void setParticipantID(String participantID);

	/**
	 * @see is.idega.idegaweb.marathon.data.PledgeBMPBean#setOrganizationalID
	 */
	public void setOrganizationalID(String organizationalID);

	/**
	 * @see is.idega.idegaweb.marathon.data.PledgeBMPBean#setCardholderName
	 */
	public void setCardholderName(String cardholderName);

	/**
	 * @see is.idega.idegaweb.marathon.data.PledgeBMPBean#setAmountPayed
	 */
	public void setAmountPayed(String amountPayed);

	/**
	 * @see is.idega.idegaweb.marathon.data.PledgeBMPBean#setPaymentTimestamp
	 */
	public void setPaymentTimestamp(String paymentTimestamp);
}