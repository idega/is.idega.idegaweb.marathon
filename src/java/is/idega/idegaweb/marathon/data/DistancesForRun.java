package is.idega.idegaweb.marathon.data;


import com.idega.data.IDOEntity;

public interface DistancesForRun extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.marathon.data.DistancesForRunBMPBean#getDistanceName
	 */
	public String getDistanceName();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistancesForRunBMPBean#getRun
	 */
	public Run getRun();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistancesForRunBMPBean#getIsDeleted
	 */
	public boolean getIsDeleted();

	/**
	 * @see is.idega.idegaweb.marathon.data.DistancesForRunBMPBean#setDistanceName
	 */
	public void setDistanceName(String name);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistancesForRunBMPBean#setRun
	 */
	public void setRun(Run run);

	/**
	 * @see is.idega.idegaweb.marathon.data.DistancesForRunBMPBean#setIsDeleted
	 */
	public void setIsDeleted(boolean isDeleted);
}