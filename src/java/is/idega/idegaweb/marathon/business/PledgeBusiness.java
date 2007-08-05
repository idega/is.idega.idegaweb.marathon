package is.idega.idegaweb.marathon.business;

import java.util.Collection;
import is.idega.idegaweb.marathon.data.PledgeHome;
import javax.ejb.EJBException;
import com.idega.business.IBOService;
import com.idega.data.IDOCreateException;
import java.rmi.RemoteException;

public interface PledgeBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.marathon.business.PledgeBusinessBean#getCharities
	 */
	public Collection getPledges() throws EJBException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.PledgeBusinessBean#getCharities
	 */
	public Collection getPledgesForUser(Integer userID) throws EJBException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.PledgeBusinessBean#saveParticipants
	 */
	public Collection saveParticipants(Collection pledgeHolders) throws IDOCreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.PledgeBusinessBean#getPledgeHome
	 */
	public PledgeHome getPledgeHome() throws RemoteException;
}