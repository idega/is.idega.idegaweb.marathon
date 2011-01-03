package is.idega.idegaweb.marathon.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface DistancesForRunHome extends IDOHome {
	public DistancesForRun create() throws CreateException;

	public DistancesForRun findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAllByRun(Run run) throws FinderException;
}