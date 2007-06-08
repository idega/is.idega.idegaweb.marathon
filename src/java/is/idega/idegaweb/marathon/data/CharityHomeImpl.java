package is.idega.idegaweb.marathon.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;
import com.idega.data.IDORelationshipException;

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

	public Charity findCharityByOrganizationalId(String organizationalId) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CharityBMPBean) entity).ejbFindCharityByOrganizationalId(organizationalId);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
	
	public Collection findCharitiesByRunYearID(Integer pk) throws IDORelationshipException, FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CharityBMPBean) entity).ejbFindCharitiesByRunYearID(pk);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}