package is.idega.idegaweb.marathon.data;


import com.idega.data.IDOEntity;

public interface RunCategory extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.marathon.data.RunCategoryBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunCategoryBMPBean#setName
	 */
	public void setName(String name);
}