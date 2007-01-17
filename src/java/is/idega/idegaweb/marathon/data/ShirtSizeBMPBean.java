package is.idega.idegaweb.marathon.data;

import com.idega.data.GenericEntity;

public class ShirtSizeBMPBean extends GenericEntity implements ShirtSize {

	private static final String ENTITY_NAME = "run_shirt_size";
	private static String COLUMN_NAME = "name";
	private static String COLUMN_DESCRIPTION = "description";
	private static String COLUMN_PARENT_CATEGORY_ID = "parent_category_id";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		this.addAttribute(getIDColumnName(), "Size", String.class, 30);

		this.setAsPrimaryKey(getIDColumnName(), true);
		this.addAttribute(COLUMN_NAME, "Name", String.class, 1000);
		this.addAttribute(COLUMN_DESCRIPTION, "Description", String.class, 1000);
		this.addAttribute(COLUMN_PARENT_CATEGORY_ID, "Parent Category ID", true, true, Integer.class, "many-to-one", this.getClass());
		this.addManyToOneRelationship(COLUMN_PARENT_CATEGORY_ID, ShirtSize.class);
		this.setNullable(COLUMN_PARENT_CATEGORY_ID, true);
		
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

}
