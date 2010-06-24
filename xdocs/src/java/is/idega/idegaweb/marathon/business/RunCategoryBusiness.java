package is.idega.idegaweb.marathon.business;


import java.util.Collection;
import javax.ejb.EJBException;
import com.idega.business.IBOService;
import is.idega.idegaweb.marathon.data.RunCategoryHome;
import java.rmi.RemoteException;

public interface RunCategoryBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.marathon.business.RunCategoryBusinessBean#getAllCategories
	 */
	public Collection getAllCategories() throws EJBException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunCategoryBusinessBean#getCategoriesByRunYearID
	 */
	public Collection getCategoriesByRunYearID(Integer runYearID) throws EJBException, RemoteException;

	/**
	 * @see is.idega.idegaweb.marathon.business.RunCategoryBusinessBean#getRunCategoryHome
	 */
	public RunCategoryHome getRunCategoryHome() throws RemoteException;
}