package is.idega.idegaweb.marathon.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.Gender;

public class AgeGroupsForDistanceBMPBean extends GenericEntity implements
		AgeGroupsForDistance {

	protected static final String ENTITY_NAME = "run_age_group_pr_distance";
	protected static final String COLUMN_DISTANCE = "distance_id";
	protected static final String COLUMN_AGE_FROM = "age_from";
	protected static final String COLUMN_AGE_TO = "age_to";
	protected static final String COLUMN_GENDER = "gender_id";
	protected static final String COLUMN_DELETED = "deleted";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_DISTANCE, DistancesForRun.class);
		addAttribute(COLUMN_AGE_FROM, "age from", Integer.class);
		addAttribute(COLUMN_AGE_TO, "age to", Integer.class);
		addManyToOneRelationship(COLUMN_GENDER, Gender.class);
		addAttribute(COLUMN_DELETED, "Deleted", Boolean.class);
	}
	
	//getters
	public DistancesForRun getDistance() {
		return (DistancesForRun) getColumnValue(COLUMN_DISTANCE);
	}
	
	public int getAgeFrom() {
		return getIntColumnValue(COLUMN_AGE_FROM, -1);
	}
	
	public int getAgeTo() {
		return getIntColumnValue(COLUMN_AGE_TO, -1);
	}
	
	public Gender getGender() {
		return (Gender) getColumnValue(COLUMN_GENDER);
	}
	
	//setters
	public void setDistance(DistancesForRun distance) {
		setColumn(COLUMN_DISTANCE, distance);
	}
	
	public void setAgeFrom(int ageFrom) {
		setColumn(COLUMN_AGE_FROM, ageFrom);
	}
	
	public void setAgeTo(int ageTo) {
		setColumn(COLUMN_AGE_TO, ageTo);
	}
	
	public void setGender(Gender gender) {
		setColumn(COLUMN_GENDER, gender);
	}
	
	//ejb
	public Collection ejbFindAllByDistance(DistancesForRun distance) throws FinderException {
		Table table = new Table(this);
		Column distanceId = new Column(table, COLUMN_DISTANCE);
		Column deleted = new Column(table, COLUMN_DELETED);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(distanceId, MatchCriteria.EQUALS, distance));
		query.addCriteria(new MatchCriteria(deleted, MatchCriteria.EQUALS, false));
		
		System.out.println("query = " + query.toString());
		
		return this.idoFindPKsByQuery(query);
	}
}