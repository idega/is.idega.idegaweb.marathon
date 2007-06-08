package is.idega.idegaweb.marathon.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;

import javax.ejb.FinderException;

public interface CharityHome extends IDOHome {
	public Charity create() throws CreateException;

	public Charity findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAllCharities() throws FinderException;

	public Charity findCharityByOrganizationalId(String organizationalId) throws FinderException;
	
	public Collection findCharitiesByRunYearID(Integer runYearID) throws IDORelationshipException, FinderException;
}