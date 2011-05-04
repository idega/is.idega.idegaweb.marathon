package is.idega.idegaweb.marathon.webservice.data;


import java.sql.Timestamp;

import com.idega.data.IDOEntity;
import com.idega.data.UniqueIDCapable;
import com.idega.user.data.User;

public interface WebServiceLoginSession extends IDOEntity, UniqueIDCapable {
	/**
	 * @see is.idega.idegaweb.marathon.webservice.data.WebServiceLoginSessionBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see is.idega.idegaweb.marathon.webservice.data.WebServiceLoginSessionBMPBean#getCreated
	 */
	public Timestamp getCreated();

	/**
	 * @see is.idega.idegaweb.marathon.webservice.data.WebServiceLoginSessionBMPBean#getLastAccess
	 */
	public Timestamp getLastAccess();

	/**
	 * @see is.idega.idegaweb.marathon.webservice.data.WebServiceLoginSessionBMPBean#getIsClosed
	 */
	public boolean getIsClosed();

	/**
	 * @see is.idega.idegaweb.marathon.webservice.data.WebServiceLoginSessionBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see is.idega.idegaweb.marathon.webservice.data.WebServiceLoginSessionBMPBean#setCreated
	 */
	public void setCreated(Timestamp created);

	/**
	 * @see is.idega.idegaweb.marathon.webservice.data.WebServiceLoginSessionBMPBean#setLastAccess
	 */
	public void setLastAccess(Timestamp lastAccess);

	/**
	 * @see is.idega.idegaweb.marathon.webservice.data.WebServiceLoginSessionBMPBean#setIsClosed
	 */
	public void setIsClosed(boolean isClosed);
}