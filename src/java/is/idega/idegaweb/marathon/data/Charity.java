package is.idega.idegaweb.marathon.data;


import com.idega.data.IDOEntity;

public interface Charity extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.marathon.data.CharityBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.marathon.data.CharityBMPBean#getPersonalID
	 */
	public String getOrganizationalID();

	/**
	 * @see is.idega.idegaweb.marathon.data.CharityBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.marathon.data.CharityBMPBean#setPersonalID
	 */
	public void setOrganizationalID(String personalID);
}