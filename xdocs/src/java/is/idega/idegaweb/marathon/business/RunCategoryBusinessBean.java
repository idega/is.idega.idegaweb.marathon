package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.RunCategory;
import is.idega.idegaweb.marathon.data.RunCategoryHome;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;

public class RunCategoryBusinessBean extends IBOServiceBean implements RunCategoryBusiness {
	
/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3461778147719948691L;
private RunCategoryHome runCategoryHome;
	
	public Collection getAllCategories() throws EJBException {
		try {
			return getRunCategoryHome().findAllCategories();
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public Collection getCategoriesByRunYearID(Integer runYearID) throws EJBException {
		try {
			return getRunCategoryHome().findCategoriesByRunYearID(runYearID);
		}
		catch (FinderException fe) {
			return null;
		} catch (IDORelationshipException e) {
			return null;
		}
	}

	public RunCategoryHome getRunCategoryHome() {
		if (this.runCategoryHome == null) {
			try {
				this.runCategoryHome = (RunCategoryHome) IDOLookup.getHome(RunCategory.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return this.runCategoryHome;
	}
}
