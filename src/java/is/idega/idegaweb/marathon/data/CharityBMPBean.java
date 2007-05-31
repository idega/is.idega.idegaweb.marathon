package is.idega.idegaweb.marathon.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.SelectQuery;

public class CharityBMPBean extends GenericEntity implements Charity {

	protected static final String ENTITY_NAME = "ic_charity_organization";
	protected static final String COLUMN_NAME_NAME = "name";
	protected static final String COLUMN_NAME_PERSONAL_ID = "personal_id";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_NAME, "Name", true, true, String.class);
		addAttribute(COLUMN_NAME_PERSONAL_ID, "Personal ID", true, true, String.class);
	}

	public String getName() {
		return (String) getColumnValue(COLUMN_NAME_NAME);
	}

	public String getPersonalID() {
		return (String) getColumnValue(COLUMN_NAME_PERSONAL_ID);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME_NAME, name);
	}

	public void setPersonalID(String personalID) {
		setColumn(COLUMN_NAME_PERSONAL_ID, personalID);
	}
	
	public Collection ejbFindAllCharities() throws FinderException {
		SelectQuery query = idoSelectQuery();
		return idoFindPKsByQuery(query);
	}
}