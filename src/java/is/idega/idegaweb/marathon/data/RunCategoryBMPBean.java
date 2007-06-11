package is.idega.idegaweb.marathon.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.Group;
import com.idega.user.data.GroupBMPBean;

public class RunCategoryBMPBean extends GenericEntity implements RunCategory {
	
	protected static final String ENTITY_NAME = "run_category";
	protected static final String COLUMN_NAME_NAME = "name";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME_NAME, "Name", true, true, String.class);

		this.addManyToManyRelationShip(Group.class);
	}

	public String getName() {
		return (String) getColumnValue(COLUMN_NAME_NAME);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME_NAME, name);
	}
	
	public Collection ejbFindAllCategories() throws FinderException {
		SelectQuery query = idoSelectPKQuery();
		query.addOrder(new Table(this), COLUMN_NAME_NAME, true);
		return idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindCategoriesByRunYearID(Integer runYearID) throws IDORelationshipException, FinderException {
		Table table = new Table(this);
		Table runYearTable = new Table(Group.class);
		Column categoriesIDColumn = new Column(table, getIDColumnName());
		Column runYearIDColumn = new Column(runYearTable, GroupBMPBean.getColumnNameGroupID());
		
		SelectQuery query = new SelectQuery(table);
		query.addManyToManyJoin(table, runYearTable);
		query.addColumn(categoriesIDColumn);
		query.addCriteria(new MatchCriteria(runYearIDColumn, MatchCriteria.EQUALS, runYearID));
		query.addOrder(table, COLUMN_NAME_NAME, true);

		return this.idoFindPKsByQuery(query);
	}
}
