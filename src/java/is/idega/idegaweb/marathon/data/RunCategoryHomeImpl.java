package is.idega.idegaweb.marathon.data;


import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;
import com.idega.data.IDORelationshipException;

public class RunCategoryHomeImpl extends IDOFactory implements RunCategoryHome {
	public Class getEntityInterfaceClass() {
		return RunCategory.class;
	}

	public RunCategory create() throws CreateException {
		return (RunCategory) super.createIDO();
	}

	public RunCategory findByPrimaryKey(Object pk) throws FinderException {
		return (RunCategory) super.findByPrimaryKeyIDO(pk);
	}
	
	public Collection findAllCategories() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((RunCategoryBMPBean) entity).ejbFindAllCategories();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findCategoriesByRunYearID(Integer pk) throws IDORelationshipException, FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((RunCategoryBMPBean) entity).ejbFindCategoriesByRunYearID(pk);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}