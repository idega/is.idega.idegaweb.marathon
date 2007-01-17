package is.idega.idegaweb.marathon.data;


import com.idega.data.IDOEntity;

public interface ShirtSize extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.marathon.data.ShirtSizeBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.marathon.data.ShirtSizeBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see is.idega.idegaweb.marathon.data.ShirtSizeBMPBean#getParentCategorYID
	 */
	public int getParentCategorYID();

	/**
	 * @see is.idega.idegaweb.marathon.data.ShirtSizeBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.marathon.data.ShirtSizeBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see is.idega.idegaweb.marathon.data.ShirtSizeBMPBean#setParentCategorYID
	 */
	public void setParentCategorYID(int parent_id);
}