package is.idega.idegaweb.marathon.data;


import com.idega.user.data.Group;
import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import com.idega.user.data.User;

public interface ParticipantHome extends IDOHome {

	public Participant create() throws CreateException;

	public Participant findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public int getNextAvailableParticipantNumber(Object distancePK, int min, int max) throws IDOException;

	public int getNumberOfParticipantsByDistance(Object distancePK, int min, int max) throws IDOException;

	public int getCountByDistanceAndNumber(Object distancePK, int number) throws IDOException;

	public int getCountByDistanceAndGroupName(Object distancePK, String groupName) throws IDOException;

	public Collection findAllByDistanceAndGroup(Group distance, Group runGroup) throws FinderException;

	public Participant findByUserIDandDistanceID(int userID, int distanceID) throws FinderException;

	public Participant findByDistanceAndParticpantNumber(Object distancePK, int participantNumber) throws FinderException;

	public Participant findByUserAndRun(User user, Group run, Group year) throws FinderException;

	public Collection findByUserAndParentGroup(int userID, int runGroupID, int yearGroupID, int distanceGroupID) throws FinderException;

	public Collection findByUserID(int userID) throws FinderException;

	public Collection findAllWithoutChipNumber(int distanceIDtoIgnore) throws FinderException;

	public Collection findAllByRunGroupIdAndYearGroupId(int runId, int yearId) throws FinderException;

	public Collection findAllByRunGroupIdAndYear(int runId, int year) throws FinderException;
}