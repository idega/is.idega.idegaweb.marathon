/*
 * Created on Aug 17, 2004
 *
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import java.sql.Timestamp;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.TimestampInput;
import com.idega.user.presentation.UserGroupTab;
import com.idega.util.IWTimestamp;


/**
 * @author birna
 *
 */
public class RunYearTab extends UserGroupTab{
	
	private static final String PARAMETER_RUN_DATE = "run_date";
	private static final String PARAMETER_LAST_REGISTRATION_DATE = "last_registration_date";
	
	private TimestampInput runDate;
	private TimestampInput lastRegistrationDate;
	
	private Text runDateText;
	private Text lastRegistrationDateText;
	
	public RunYearTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString("run_tab.year_name", "Year info"));
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			String runDate = iwc.getParameter(PARAMETER_RUN_DATE);
			String lastRegistrationDate = iwc.getParameter(PARAMETER_LAST_REGISTRATION_DATE);
			
			if (runDate != null) {
				this.fieldValues.put(PARAMETER_RUN_DATE, new IWTimestamp(runDate).getTimestamp());
			}
			if (lastRegistrationDate != null) {
				this.fieldValues.put(PARAMETER_LAST_REGISTRATION_DATE, new IWTimestamp(lastRegistrationDate).getTimestamp());
			}
			
			updateFieldsDisplayStatus();
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initFieldContents()
	 */
	public void initFieldContents() {
		try {
			Year year = ConverterUtility.getInstance().convertGroupToYear(new Integer(getGroupId()));
			
			if (year.getRunDate() != null) {
				this.fieldValues.put(PARAMETER_RUN_DATE, year.getRunDate());
			}
			if (year.getLastRegistrationDate() != null) {
				this.fieldValues.put(PARAMETER_LAST_REGISTRATION_DATE, year.getLastRegistrationDate());
			}

			updateFieldsDisplayStatus();
		}
		catch (Exception e) {
			System.err.println("RunDistanceTab error initFieldContents, GroupId : " + getGroupId());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFieldNames()
	 */
	public void initializeFieldNames() {
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFields()
	 */
	public void initializeFields() {
		this.runDate = new TimestampInput(PARAMETER_RUN_DATE);
		this.lastRegistrationDate = new TimestampInput(PARAMETER_LAST_REGISTRATION_DATE);
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		this.fieldValues.put(PARAMETER_RUN_DATE, new IWTimestamp().getTimestamp());
		this.fieldValues.put(PARAMETER_LAST_REGISTRATION_DATE, new IWTimestamp().getTimestamp());
		
		updateFieldsDisplayStatus();
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		this.runDateText = new Text(iwrb.getLocalizedString("run_tab.run_date", "Run date"));
		this.runDateText.setBold();
		
		this.lastRegistrationDateText = new Text(iwrb.getLocalizedString("run_tab.last_registration_date", "Last registration date"));
		this.lastRegistrationDateText.setBold();
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#lineUpFields()
	 */
	public void lineUpFields() {
		resize(1, 1);
		setCellpadding(0);
		setCellspacing(0);
		
		Table table = new Table(2, 2);
		table.setCellpadding(5);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_RIGHT);

		table.add(this.runDateText, 1, 1);
		table.add(this.runDate, 2, 1);
		
		table.add(this.lastRegistrationDateText, 1, 2);
		table.add(this.lastRegistrationDate, 2, 2);
		
		add(table, 1, 1);
	}
	
	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {
		try {
			if (getGroupId() > -1) {
				Year year = ConverterUtility.getInstance().convertGroupToYear(new Integer(getGroupId()));
				
				year.setRunDate((Timestamp) this.fieldValues.get(PARAMETER_RUN_DATE));
				year.setLastRegistrationDate((Timestamp) this.fieldValues.get(PARAMETER_LAST_REGISTRATION_DATE));

				year.store();
			}
		}
		catch (Exception e) {
			//return false;
			e.printStackTrace(System.err);
			throw new RuntimeException("update group exception");
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		this.runDate.setTimestamp((Timestamp) this.fieldValues.get(PARAMETER_RUN_DATE));
		this.lastRegistrationDate.setTimestamp((Timestamp) this.fieldValues.get(PARAMETER_LAST_REGISTRATION_DATE));
	}
	
	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}
}