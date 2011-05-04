package is.idega.idegaweb.marathon.webservice.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.User;

public class WebServiceLoginSessionBMPBean extends GenericEntity implements
		WebServiceLoginSession {

	public final static String ENTITY_NAME = "ibr_ws_session";
	public final static String COLUMN_USER = "ic_user_id";
	public final static String COLUMN_CREATED = "created";
	public final static String COLUMN_LAST_ACCESS = "last_access";
	public final static String COLUMN_IS_CLOSED = "is_closed";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_USER, User.class);
		addUniqueIDColumn();
		addAttribute(COLUMN_CREATED, "created", Timestamp.class);
		addAttribute(COLUMN_LAST_ACCESS, "last access", Timestamp.class);
		addAttribute(COLUMN_IS_CLOSED, "is closed", Boolean.class);
	}

	// getters
	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);
	}

	public Timestamp getCreated() {
		return getTimestampColumnValue(COLUMN_CREATED);
	}

	public Timestamp getLastAccess() {
		return getTimestampColumnValue(COLUMN_LAST_ACCESS);
	}

	public boolean getIsClosed() {
		return getBooleanColumnValue(COLUMN_IS_CLOSED, false);
	}

	// setters
	public void setUser(User user) {
		setColumn(COLUMN_USER, user);
	}

	public void setCreated(Timestamp created) {
		setColumn(COLUMN_CREATED, created);
	}

	public void setLastAccess(Timestamp lastAccess) {
		setColumn(COLUMN_LAST_ACCESS, lastAccess);
	}

	public void setIsClosed(boolean isClosed) {
		setColumn(COLUMN_IS_CLOSED, isClosed);
	}

	// ejb
	public Collection ejbFindAllByUser(User user) throws FinderException {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(new Column(table, COLUMN_USER),
				MatchCriteria.EQUALS, user));
		query.addCriteria(new OR(new MatchCriteria(new Column(table,
				COLUMN_IS_CLOSED), MatchCriteria.IS, MatchCriteria.NULL),
				new MatchCriteria(new Column(table, COLUMN_IS_CLOSED),
						MatchCriteria.EQUALS, false)));

		// System.out.println("query = " + query.toString());

		return this.idoFindPKsByQuery(query);
	}

	public Object ejbFindByUniqueID(String id) throws FinderException {
		return idoFindOnePKByUniqueId(id);
	}
}