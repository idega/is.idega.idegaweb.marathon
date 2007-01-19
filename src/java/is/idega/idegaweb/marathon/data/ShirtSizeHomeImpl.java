package is.idega.idegaweb.marathon.data;


import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;

public class ShirtSizeHomeImpl extends IDOFactory implements ShirtSizeHome {
	public Class getEntityInterfaceClass() {
		return ShirtSize.class;
	}

	public ShirtSize create() throws CreateException {
		return (ShirtSize) super.createIDO();
	}

	public ShirtSize findByPrimaryKey(Object pk) throws FinderException {
		return (ShirtSize) super.findByPrimaryKeyIDO(pk);
	}
	
	public Collection findAll() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((ShirtSizeBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}