package is.idega.idegaweb.marathon.data;


import javax.ejb.CreateException;
import com.idega.util.IWTimestamp;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import com.idega.data.IDOException;
import java.util.Collection;

public interface PledgeHome extends IDOHome {
	public Pledge create() throws CreateException;

	public Pledge findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAllPledges() throws FinderException;

	public Collection findAllPledgesForUser(int userID)
			throws IDORelationshipException, FinderException;

	public int getNumberOfPledgesByParticipants(Participant participant)
			throws IDOException;

	public Collection findAllByDateAndCharity(IWTimestamp date, String charityID)
			throws IDORelationshipException, FinderException;
}