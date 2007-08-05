package is.idega.idegaweb.marathon.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public class PledgeBMPBean extends GenericEntity implements Pledge {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 5020328074040406111L;
	protected static final String ENTITY_NAME = "run_pledge";
	protected static final String COLUMN_NAME_PARTICIPANT_ID = "participant_id";
	protected static final String COLUMN_NAME_ORGANIZATIONAL_ID = "organizational_id";
	protected static final String COLUMN_NAME_CARDHOLDER_NAME = "cardholder_name";
	protected static final String COLUMN_NAME_AMOUNT_PAYED = "amount_payed";
	protected static final String COLUMN_NAME_PAYMENT_TIMESTAMP = "payment_timestamp";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_NAME_PARTICIPANT_ID, "Participant ID", Participant.class);
		addAttribute(COLUMN_NAME_ORGANIZATIONAL_ID, "Organizational ID", true, true, String.class);
		addAttribute(COLUMN_NAME_CARDHOLDER_NAME, "Cardholder name", true, true, String.class);
		addAttribute(COLUMN_NAME_AMOUNT_PAYED, "Amount payed", true, true, String.class);
		addAttribute(COLUMN_NAME_PAYMENT_TIMESTAMP, "Payment timestamp", true, true, String.class);
	}
	
	public int getParticipantID() {
		return getIntColumnValue(COLUMN_NAME_PARTICIPANT_ID);
	}
	
	public Participant getParticipant() {
		return (Participant) getColumnValue(COLUMN_NAME_PARTICIPANT_ID);
	}


	public String getOrganizationalID() {
		return (String) getColumnValue(COLUMN_NAME_ORGANIZATIONAL_ID);
	}

	public String getCardholderName() {
		return (String) getColumnValue(COLUMN_NAME_CARDHOLDER_NAME);
	}

	public String getAmountPayed() {
		return (String) getColumnValue(COLUMN_NAME_AMOUNT_PAYED);
	}

	public String getPaymentTimestamp() {
		return (String) getColumnValue(COLUMN_NAME_PAYMENT_TIMESTAMP);
	}
	
	public void setParticipantID(int participantID) {
		setColumn(COLUMN_NAME_PARTICIPANT_ID, participantID);
	}
	
	public void setParticipant(Participant participant) {
		setColumn(COLUMN_NAME_PARTICIPANT_ID, participant);
	}

	public void setOrganizationalID(String organizationalID) {
		setColumn(COLUMN_NAME_ORGANIZATIONAL_ID, organizationalID);
	}

	public void setCardholderName(String cardholderName) {
		setColumn(COLUMN_NAME_CARDHOLDER_NAME, cardholderName);
	}

	public void setAmountPayed(String amountPayed) {
		setColumn(COLUMN_NAME_AMOUNT_PAYED, amountPayed);
	}

	public void setPaymentTimestamp(String paymentTimestamp) {
		setColumn(COLUMN_NAME_PAYMENT_TIMESTAMP, paymentTimestamp);
	}

	public Collection ejbFindAllPledges() throws FinderException {
		SelectQuery query = idoSelectQuery();
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindAllPledgesForUser(Integer userID) throws IDORelationshipException, FinderException {
		Table table = new Table(this);
		Table runTable = new Table(Participant.class);
		Column pledgesIDColumn = new Column(table, getIDColumnName());
		Column userIDColumn = new Column(runTable, ParticipantBMPBean.getColumnNameUserID());
		
		SelectQuery query = new SelectQuery(table);
		query.addManyToManyJoin(table, runTable);
		query.addColumn(pledgesIDColumn);
		query.addCriteria(new MatchCriteria(userIDColumn, MatchCriteria.EQUALS, userID));
		//query.addOrder(table, COLUMN_NAME_PAYMENT_TIMESTAMP, true);
		return this.idoFindPKsByQuery(query);
	}
}