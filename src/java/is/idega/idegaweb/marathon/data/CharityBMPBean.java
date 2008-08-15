package is.idega.idegaweb.marathon.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.Group;
import com.idega.user.data.GroupBMPBean;

public class CharityBMPBean extends GenericEntity implements Charity {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 6827105404436456030L;
	protected static final String ENTITY_NAME = "run_charity_organization";
	protected static final String COLUMN_NAME_NAME = "name";
	protected static final String COLUMN_NAME_ORGANIZATIONAL_ID = "organizational_id";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_NAME, "Name", true, true, String.class);
		addAttribute(COLUMN_NAME_ORGANIZATIONAL_ID, "Organizational ID", true, true, String.class);

		this.addManyToManyRelationShip(Group.class);
	}

	public String getName() {
		return (String) getColumnValue(COLUMN_NAME_NAME);
	}

	public String getOrganizationalID() {
		return (String) getColumnValue(COLUMN_NAME_ORGANIZATIONAL_ID);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME_NAME, name);
	}

	public void setOrganizationalID(String organizationalId) {
		setColumn(COLUMN_NAME_ORGANIZATIONAL_ID, organizationalId);
	}
	
	public void removeFromGroup(Group group) {
		try {
			super.idoRemoveFrom(group);
		} catch (IDORemoveRelationshipException e) {
			e.printStackTrace();
		}
	}

	public void addToGroup(Group group) {
		try {
			super.idoAddTo(group);
		} catch (IDOAddRelationshipException e) {
			e.printStackTrace();
		}
	}
	
	public Collection ejbFindAllCharities() throws FinderException {
		SelectQuery query = idoSelectPKQuery();
		query.addOrder(new Table(this), COLUMN_NAME_NAME, true);
		return idoFindPKsByQuery(query);
	}
	
	public Object ejbFindCharityByOrganizationalId(String organizationalId) throws FinderException {
		Table table = new Table(this);
		Column orgId = new Column(table, COLUMN_NAME_ORGANIZATIONAL_ID);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(orgId, MatchCriteria.EQUALS, organizationalId));
		
		return idoFindOnePKByQuery(query);
	}

	public Collection ejbFindCharitiesByRunYearID(Integer runYearID) throws IDORelationshipException, FinderException {
		Table table = new Table(this);
		Table runYearTable = new Table(Group.class);
		Column charityIDColumn = new Column(table, getIDColumnName());
		Column runYearIDColumn = new Column(runYearTable, GroupBMPBean.getColumnNameGroupID());
		
		SelectQuery query = new SelectQuery(table);
		query.addManyToManyJoin(table, runYearTable);
		query.addColumn(charityIDColumn);
		query.addCriteria(new MatchCriteria(runYearIDColumn, MatchCriteria.EQUALS, runYearID));
		query.addOrder(table, COLUMN_NAME_NAME, true);

		return this.idoFindPKsByQuery(query);
	}
}