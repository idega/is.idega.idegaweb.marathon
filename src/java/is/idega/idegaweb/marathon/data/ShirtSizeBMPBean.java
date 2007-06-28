package is.idega.idegaweb.marathon.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

public class ShirtSizeBMPBean extends GenericEntity implements ShirtSize {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -690527264263221825L;
	private static final String ENTITY_NAME = "run_shirt_size";
	private static String COLUMN_NAME = "name";
	private static String COLUMN_DESCRIPTION = "description";
	private static String COLUMN_PARENT_CATEGORY_ID = "parent_category_id";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		this.addAttribute(COLUMN_NAME, "Name", String.class, 100);
		this.addAttribute(COLUMN_DESCRIPTION, "Description", String.class, 1000);
		this.addAttribute(COLUMN_PARENT_CATEGORY_ID, "Parent Category ID", Integer.class);

		//cache this table
		getEntityDefinition().setBeanCachingActiveByDefault(true);
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}

	public int getParentCategorYID() {
		return getIntColumnValue(COLUMN_PARENT_CATEGORY_ID);
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}

	public void setParentCategorYID(int parent_id) {
		setColumn(COLUMN_PARENT_CATEGORY_ID, parent_id);
	}
	
	public Collection ejbFindAll() throws FinderException {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn());
		query.addCriteria(new MatchCriteria(table, COLUMN_PARENT_CATEGORY_ID, MatchCriteria.NOTEQUALS, -1));
		return idoFindPKsBySQL(query.toString());
	}

}
