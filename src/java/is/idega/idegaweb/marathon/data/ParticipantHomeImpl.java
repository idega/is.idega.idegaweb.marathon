package is.idega.idegaweb.marathon.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;
import javax.ejb.FinderException;
import com.idega.data.IDOException;
import java.util.Collection;
import com.idega.user.data.Group;

public class ParticipantHomeImpl extends IDOFactory implements ParticipantHome {
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

	public int getNextAvailableParticipantNumber(Object distancePK, int min,
			int max) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ParticipantBMPBean) entity)
				.ejbHomeGetNextAvailableParticipantNumber(distancePK, min, max);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getNumberOfParticipantsByDistance(Object distancePK, int min,
			int max) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ParticipantBMPBean) entity)
				.ejbHomeGetNumberOfParticipantsByDistance(distancePK, min, max);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getCountByDistanceAndNumber(Object distancePK, int number)
			throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ParticipantBMPBean) entity)
				.ejbHomeGetCountByDistanceAndNumber(distancePK, number);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getCountByDistanceAndGroupName(Object distancePK,
			String groupName) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((ParticipantBMPBean) entity)
				.ejbHomeGetCountByDistanceAndGroupName(distancePK, groupName);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAllByDistanceAndGroup(Group distance, Group runGroup)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity)
				.ejbFindAllByDistanceAndGroup(distance, runGroup);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Participant findByUserIDandYearID(int userID, int yearID)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ParticipantBMPBean) entity).ejbFindByUserIDandYearID(
				userID, yearID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Participant findByUserIDandDistanceID(int userID, int distanceID)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ParticipantBMPBean) entity).ejbFindByUserIDandDistanceID(
				userID, distanceID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Participant findByYearAndParticipantNumberAndName(Object yearPK,
			int participantNumber, String fullName) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ParticipantBMPBean) entity)
				.ejbFindByYearAndParticipantNumberAndName(yearPK,
						participantNumber, fullName);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByYearAndFullNameOrPersonalIdOrParticipantNumberOrParentGroup(
			Object yearPK, String searchQuery) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity)
				.ejbFindByYearAndFullNameOrPersonalIdOrParticipantNumberOrParentGroup(
						yearPK, searchQuery);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByYearAndCrewInOrCrewInvitationParticipantId(
			Object yearPK, Integer crewParticipantId) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity)
				.ejbFindByYearAndCrewInOrCrewInvitationParticipantId(yearPK,
						crewParticipantId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Participant findByDistanceAndParticipantNumber(Object distancePK,
			int participantNumber) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ParticipantBMPBean) entity)
				.ejbFindByDistanceAndParticipantNumber(distancePK,
						participantNumber);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByYearAndTeamName(Object yearPK, String teamName)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity)
				.ejbFindByYearAndTeamName(yearPK, teamName);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Participant findByUserAndRun(User user, Group run, Group year)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ParticipantBMPBean) entity).ejbFindByUserAndRun(user,
				run, year);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Participant findByPartnerAndRun(String partnerPersonalID, Group run,
			Group year, int partnerNumber) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ParticipantBMPBean) entity).ejbFindByPartnerAndRun(
				partnerPersonalID, run, year, partnerNumber);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findByUserAndParentGroup(int userID, int runGroupID,
			int yearGroupID, int distanceGroupID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity)
				.ejbFindByUserAndParentGroup(userID, runGroupID, yearGroupID,
						distanceGroupID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByUserID(int userID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity).ejbFindByUserID(userID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllWithoutChipNumber(int distanceIDtoIgnore)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity)
				.ejbFindAllWithoutChipNumber(distanceIDtoIgnore);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllByRunGroupIdAndYearGroupId(int runId, int yearId)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity)
				.ejbFindAllByRunGroupIdAndYearGroupId(runId, yearId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public boolean findCrewLabelAlreadyExistsForRun(int runId, int yearId,
			String crewLabel) throws FinderException {
		 IDOEntity entity = this.idoCheckOutPooledEntity();
		 boolean crewLabelExists = ((ParticipantBMPBean) entity)
		 .ejbFindCrewLabelAlreadyExistsForRun(runId, yearId, crewLabel);
		 this.idoCheckInPooledEntity(entity);
		 return crewLabelExists;
	}

	public Collection findAllByRunGroupIdAndYear(int runId, int year)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ParticipantBMPBean) entity)
				.ejbFindAllByRunGroupIdAndYear(runId, year);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}