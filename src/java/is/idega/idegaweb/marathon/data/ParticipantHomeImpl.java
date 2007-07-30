package is.idega.idegaweb.marathon.data;


import com.idega.user.data.Group;
import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class ParticipantHomeImpl extends IDOFactory implements ParticipantHome {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 6690853790390881396L;

	public Class getEntityInterfaceClass() {
		return Participant.class;
	}

	public Participant create() throws CreateException {
		return (Participant) super.createIDO();
	}

	public Participant findByPrimaryKey(Object pk) throws FinderException {
		return (Participant) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getNextAvailableParticipantNumber(Object distancePK, int min, int max) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ParticipantBMPBean) entity).ejbHomeGetNextAvailableParticipantNumber(distancePK, min, max);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfParticipantsByDistance(Object distancePK, int min, int max) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ParticipantBMPBean) entity).ejbHomeGetNumberOfParticipantsByDistance(distancePK, min, max);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getCountByDistanceAndNumber(Object distancePK, int number) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ParticipantBMPBean) entity).ejbHomeGetCountByDistanceAndNumber(distancePK, number);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getCountByDistanceAndGroupName(Object distancePK, String groupName) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ParticipantBMPBean) entity).ejbHomeGetCountByDistanceAndGroupName(distancePK, groupName);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAllByDistanceAndGroup(Group distance, Group runGroup) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity).ejbFindAllByDistanceAndGroup(distance, runGroup);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Participant findByUserIDandDistanceID(int userID, int distanceID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ParticipantBMPBean) entity).ejbFindByUserIDandDistanceID(userID, distanceID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Participant findByDistanceAndParticipantNumber(Object distancePK, int participantNumber) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ParticipantBMPBean) entity).ejbFindByDistanceAndParticipantNumber(distancePK, participantNumber);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByYearAndTeamName(Object yearPK, String teamName) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity).ejbFindByYearAndTeamName(yearPK, teamName);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Participant findByUserAndRun(User user, Group run, Group year) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ParticipantBMPBean) entity).ejbFindByUserAndRun(user, run, year);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByUserAndParentGroup(int userID, int runGroupID, int yearGroupID, int distanceGroupID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity).ejbFindByUserAndParentGroup(userID, runGroupID, yearGroupID, distanceGroupID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByUserID(int userID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity).ejbFindByUserID(userID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllWithoutChipNumber(int distanceIDtoIgnore) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity).ejbFindAllWithoutChipNumber(distanceIDtoIgnore);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByRunGroupIdAndYearGroupId(int runId, int yearId) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity).ejbFindAllByRunGroupIdAndYearGroupId(runId, yearId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByRunGroupIdAndYear(int runId, int year) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity).ejbFindAllByRunGroupIdAndYear(runId, year);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}