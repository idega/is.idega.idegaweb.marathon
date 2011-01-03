package is.idega.idegaweb.marathon.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

public class DistancesForRunBMPBean extends GenericEntity implements
		DistancesForRun {

	protected static final String ENTITY_NAME = "run_distances_pr_run";
	protected static final String COLUMN_DISTANCE_NAME = "distance_name";
	protected static final String COLUMN_RUN = "run_id";
	protected static final String COLUMN_DELETED = "deleted";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_DISTANCE_NAME, "Name", String.class);
		addManyToOneRelationship(COLUMN_RUN, Run.class);
		addAttribute(COLUMN_DELETED, "Deleted", Boolean.class);
	}
	
	//getters
	public String getDistanceName() {
		return getStringColumnValue(COLUMN_DISTANCE_NAME);
	}
	
	public Run getRun() {
		return (Run) getColumnValue(COLUMN_RUN);
	}
	
	public boolean getIsDeleted() {
		return getBooleanColumnValue(COLUMN_DELETED, false);
	}
	
	//setters
	public void setDistanceName(String name) {
		setColumn(COLUMN_DISTANCE_NAME, name);
	}
	
	public void setRun(Run run) {
		setColumn(COLUMN_RUN, run);
	}
	
	public void setIsDeleted(boolean isDeleted) {
		setColumn(COLUMN_DELETED, isDeleted);
	}
	
	//ejb
	public Collection ejbFindAllByRun(Run run) throws FinderException {
		Table table = new Table(this);
		Column runId = new Column(table, COLUMN_RUN);
		Column deleted = new Column(table, COLUMN_DELETED);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(runId, MatchCriteria.EQUALS, run));
		query.addCriteria(new MatchCriteria(deleted, MatchCriteria.EQUALS, false));
		
		System.out.println("query = " + query.toString());
		
		return this.idoFindPKsByQuery(query);
	}
}