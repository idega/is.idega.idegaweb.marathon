package is.idega.idegaweb.marathon.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class DistancesForRunHomeImpl extends IDOFactory implements
		DistancesForRunHome {
	public Class getEntityInterfaceClass() {
		return DistancesForRun.class;
	}

	public DistancesForRun create() throws CreateException {
		return (DistancesForRun) super.createIDO();
	}

	public DistancesForRun findByPrimaryKey(Object pk) throws FinderException {
		return (DistancesForRun) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByRun(Run run) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((DistancesForRunBMPBean) entity).ejbFindAllByRun(run);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}