package is.idega.idegaweb.marathon.business;


import is.idega.idegaweb.marathon.data.CharityHome;
import java.util.Collection;
import is.idega.idegaweb.marathon.data.Charity;
import javax.ejb.EJBException;
import com.idega.business.IBOService;
import java.rmi.RemoteException;

public interface CharityBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.marathon.business.CharityBusinessBean#getCharities
	 */
	public Collection getAllCharities() throws EJBException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.CharityBusinessBean#getCharityByOrganisationalID
	 */
	public Charity getCharityByOrganisationalID(String organizationalId) throws EJBException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.CharityBusinessBean#getCharitiesByRunYearID
	 */
	public Collection getCharitiesByRunYearID(Integer runYearID) throws EJBException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.CharityBusinessBean#getCharityHome
	 */
	public CharityHome getCharityHome() throws RemoteException;
}