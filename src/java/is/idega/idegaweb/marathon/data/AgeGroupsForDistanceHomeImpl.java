package is.idega.idegaweb.marathon.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class AgeGroupsForDistanceHomeImpl extends IDOFactory implements
		AgeGroupsForDistanceHome {
	public Class getEntityInterfaceClass() {
		return AgeGroupsForDistance.class;
	}

	public AgeGroupsForDistance create() throws CreateException {
		return (AgeGroupsForDistance) super.createIDO();
	}

	public AgeGroupsForDistance findByPrimaryKey(Object pk)
			throws FinderException {
		return (AgeGroupsForDistance) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByDistance(DistancesForRun distance)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AgeGroupsForDistanceBMPBean) entity)
				.ejbFindAllByDistance(distance);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}