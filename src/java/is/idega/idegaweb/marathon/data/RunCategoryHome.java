package is.idega.idegaweb.marathon.data;


import java.util.Collection;

import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;

import javax.ejb.FinderException;

public interface RunCategoryHome extends IDOHome {
	public RunCategory create() throws CreateException;

	public RunCategory findByPrimaryKey(Object pk) throws FinderException;
	
	public Collection findAllCategories() throws FinderException;

	public Collection findCategoriesByRunYearID(Integer runYearID) throws IDORelationshipException, FinderException;

}