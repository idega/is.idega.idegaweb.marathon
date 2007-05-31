package is.idega.idegaweb.marathon.data;


import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;

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
	
	public java.util.Collection findAllCharities()throws javax.ejb.FinderException{
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CharityBMPBean)entity).ejbFindAllCharities();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}