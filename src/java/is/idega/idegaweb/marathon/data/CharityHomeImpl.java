package is.idega.idegaweb.marathon.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class CharityHomeImpl extends IDOFactory implements CharityHome {
	public Class getEntityInterfaceClass() {
		return Charity.class;
	}

	public Charity create() throws CreateException {
		return (Charity) super.createIDO();
	}

	public Charity findByPrimaryKey(Object pk) throws FinderException {
		return (Charity) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllCharities() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CharityBMPBean) entity).ejbFindAllCharities();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Charity findCharityByOrganizationalId(String organizationalId)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CharityBMPBean) entity)
				.ejbFindCharityByOrganizationalId(organizationalId);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findCharitiesByRunYearID(Integer runYearID)
			throws IDORelationshipException, FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CharityBMPBean) entity)
				.ejbFindCharitiesByRunYearID(runYearID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}