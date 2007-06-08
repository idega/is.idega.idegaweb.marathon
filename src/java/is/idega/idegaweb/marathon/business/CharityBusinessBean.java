package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.CharityHome;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;

public class CharityBusinessBean extends IBOServiceBean implements CharityBusiness {
	
	private CharityHome charityHome;
	
	public Collection getAllCharities() throws EJBException {
		try {
			return getCharityHome().findAllCharities();
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public Charity getCharityByOrganisationalID(String organizationalId) throws EJBException {
		try {
			return getCharityHome().findCharityByOrganizationalId(organizationalId);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public Collection getCharitiesByRunYearID(Integer runYearID) throws EJBException {
		try {
			return getCharityHome().findCharitiesByRunYearID(runYearID);
		}
		catch (FinderException fe) {
			return null;
		} catch (IDORelationshipException e) {
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
