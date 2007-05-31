package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.CharityHome;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;

public class CharityBusinessBean extends IBOServiceBean implements CharityBusiness {
	
	private CharityHome charityHome;
	
	public Collection getCharities() throws EJBException, RemoteException {
		try {
			return getCharityHome().findAllCharities();
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public CharityHome getCharityHome() {
		if (this.charityHome == null) {
			try {
				this.charityHome = (CharityHome) IDOLookup.getHome(Charity.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return this.charityHome;
	}

}
