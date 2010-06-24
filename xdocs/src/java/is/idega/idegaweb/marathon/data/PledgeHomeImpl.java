package is.idega.idegaweb.marathon.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.util.IWTimestamp;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import com.idega.data.IDOException;
import java.util.Collection;

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

	public Collection findAllPledgesForUser(int userID)
			throws IDORelationshipException, FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((PledgeBMPBean) entity)
				.ejbFindAllPledgesForUser(userID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getNumberOfPledgesByParticipants(Participant participant)
			throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((PledgeBMPBean) entity)
				.ejbHomeGetNumberOfPledgesByParticipants(participant);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAllByDateAndCharity(IWTimestamp date, String charityID)
			throws IDORelationshipException, FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((PledgeBMPBean) entity).ejbFindAllByDateAndCharity(
				date, charityID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}