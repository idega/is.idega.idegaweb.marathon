package is.idega.idegaweb.marathon.business;


import is.idega.idegaweb.marathon.data.CharityHome;
import java.util.Collection;
import javax.ejb.EJBException;
import com.idega.business.IBOService;
import java.rmi.RemoteException;

public interface CharityBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.marathon.business.CharityBusinessBean#getCharities
	 */
	public Collection getCharities() throws EJBException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.CharityBusinessBean#getCharityHome
	 */
	public CharityHome getCharityHome() throws RemoteException;
}