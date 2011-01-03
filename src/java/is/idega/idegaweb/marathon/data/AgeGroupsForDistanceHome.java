package is.idega.idegaweb.marathon.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface AgeGroupsForDistanceHome extends IDOHome {
	public AgeGroupsForDistance create() throws CreateException;

	public AgeGroupsForDistance findByPrimaryKey(Object pk)
			throws FinderException;

	public Collection findAllByDistance(DistancesForRun distance)
			throws FinderException;
}