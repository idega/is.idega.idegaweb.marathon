package is.idega.idegaweb.marathon.data;


import com.idega.data.IDOEntity;

public interface Charity extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.marathon.data.CharityBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.marathon.data.CharityBMPBean#getOrganizationalID
	 */
	public String getOrganizationalID();

	/**
	 * @see is.idega.idegaweb.marathon.data.CharityBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.marathon.data.CharityBMPBean#setOrganizationalID
	 */
	public void setOrganizationalID(String organizationalId);
}