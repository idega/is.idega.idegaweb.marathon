package is.idega.idegaweb.marathon.data;


import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class PledgeHomeImpl extends IDOFactory implements PledgeHome {
	public Class getEntityInterfaceClass() {
		return Pledge.class;
	}

	public Pledge create() throws CreateException {
		return (Pledge) super.createIDO();
	}

	public Pledge findByPrimaryKey(Object pk) throws FinderException {
		return (Pledge) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllPledges() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((PledgeBMPBean) entity).ejbFindAllPledges();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}