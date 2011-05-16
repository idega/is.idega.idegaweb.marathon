package is.idega.idegaweb.marathon.webservice.business;


import is.idega.idegaweb.marathon.webservice.server.Session;
import java.rmi.RemoteException;
import is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException;
import is.idega.idegaweb.marathon.webservice.server.CharityInformation;
import com.idega.business.IBOService;

public interface WebServiceBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.marathon.webservice.business.WebServiceBusinessBean#getCharityInformation
	 */
	public CharityInformation getCharityInformation(String personalID)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.webservice.business.WebServiceBusinessBean#authenticateUser
	 */
	public Session authenticateUser(String userName, String password)
			throws RemoteException;

	
	/**
	 * @see is.idega.idegaweb.marathon.webservice.business.WebServiceBusinessBean#updateUserPassword
	 */
	public boolean updateUserPassword(Session session, String personalID,
			String password) throws RemoteException, SessionTimedOutException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.webservice.business.WebServiceBusinessBean#updateUserCharity
	 */
	public boolean updateUserCharity(Session session, String personalID,
			String charityPersonalID) throws RemoteException,
			SessionTimedOutException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.webservice.business.WebServiceBusinessBean#updateUserEmail
	 */
	public boolean updateUserEmail(Session session, String personalID,
			String email) throws RemoteException, SessionTimedOutException,
			RemoteException;
}