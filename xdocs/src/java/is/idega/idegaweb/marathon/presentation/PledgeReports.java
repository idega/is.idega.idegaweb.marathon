package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableColumn;
import com.idega.presentation.TableColumnGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.database.ConnectionBroker;

public class PledgeReports extends Block {
	
	private static Logger logger = Logger.getLogger(PledgeReports.class.getName());
	
	private static final String[] REPORT_NAMES = {
												"Starfsmenn Reykjavikurmarathon",
												"Samt\u00F6lur \u00C1heita",
												"Heildar \u00C1heitat\u00F6lur",
												"Heildarfj\u00F6ldi keppenda",
												"Heildarfj\u00F6ldi starfsmanna",
												"Heildar\u00E1heitat\u00F6lur Latab\u00E6jarhlaup",
												"Sundurli\u00F0un \u00E1heitaupph\u00E6\u00F0a",
												"Starfsmanna\u00FArslit",
												"Samt\u00F6lur \u00C1heita 2",
												"Sundurli\u00F0un \u00E1heita"
												};
	private static final String[] REPORT_SQL = {
												"SELECT A_1.DISPLAY_NAME name, A_1.PERSONAL_ID personal_id, A_15.run_id participant_id, A_21.name department, A_18.short_name distance, A_8.address email, A_20.name charity, count(A_22.AMOUNT_PAYED)+1 pledge_count, case when sum(cast(A_22.AMOUNT_PAYED as real)) is null then 3000 * cast(A_18.short_name as real) else sum(cast (A_22.AMOUNT_PAYED as real)) + 3000 * cast(A_18.short_name as real) end pledge_sum FROM IC_USER A_1 LEFT JOIN IC_USER_EMAIL A_10 ON (A_1.IC_USER_ID=A_10.IC_USER_ID) LEFT JOIN IC_EMAIL A_8 ON (A_10.IC_EMAIL_ID=A_8.IC_EMAIL_ID) LEFT JOIN IC_GROUP_RELATION A_13 ON (A_1.IC_USER_ID=A_13.RELATED_IC_GROUP_ID) LEFT JOIN IC_GROUP A_14 ON (A_13.IC_GROUP_ID=A_14.IC_GROUP_ID) LEFT JOIN RUN A_15 ON (A_1.IC_USER_ID=A_15.IC_USER_ID) LEFT JOIN IC_GROUP A_18 ON (A_15.IC_GROUP_ID_DISTANCE=A_18.IC_GROUP_ID) LEFT JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) LEFT JOIN RUN_CATEGORY A_21 ON (A_15.RUN_CATEGORY_ID=A_21.RUN_CATEGORY_ID) LEFT JOIN RUN_PLEDGE A_22 ON (A_15.RUN_ID=A_22.PARTICIPANT_ID) WHERE A_13.GROUP_RELATION_STATUS = 'ST_ACTIVE' AND A_13.RELATIONSHIP_TYPE='GROUP_PARENT' AND A_14.IC_GROUP_ID = A_15.IC_GROUP_ID_GROUP AND A_15.IC_GROUP_ID_RUN=4 AND A_15.IC_GROUP_ID_YEAR=345972 AND A_15.SPONSORED_RUNNER ='Y' group by A_1.DISPLAY_NAME, A_1.PERSONAL_ID, A_15.run_id, A_21.name, A_18.short_name, A_8.address, A_20.name order by A_1.DISPLAY_NAME",
												"select charity, ssn, count(distinct run_id) runner_count, count(pledge_sum) pledge_count, sum(pledge_sum) pledge_sum from (SELECT A_15.run_id,A_20.name charity, A_20.organizational_id ssn, case when A_15.SPONSORED_RUNNER = 'Y' then 3000 * cast(A_18.short_name as real) else 500 * cast(A_18.short_name as real) end pledge_sum FROM IC_USER A_1 LEFT JOIN IC_GROUP_RELATION A_13 ON (A_1.IC_USER_ID=A_13.RELATED_IC_GROUP_ID) LEFT JOIN IC_GROUP A_14 ON (A_13.IC_GROUP_ID=A_14.IC_GROUP_ID) LEFT JOIN RUN A_15 ON (A_1.IC_USER_ID=A_15.IC_USER_ID) LEFT JOIN IC_GROUP A_18 ON (A_15.IC_GROUP_ID_DISTANCE=A_18.IC_GROUP_ID) LEFT JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) WHERE A_13.GROUP_RELATION_STATUS = 'ST_ACTIVE' AND A_13.RELATIONSHIP_TYPE='GROUP_PARENT' AND A_14.IC_GROUP_ID = A_15.IC_GROUP_ID_GROUP AND A_15.IC_GROUP_ID_RUN=4 AND A_15.IC_GROUP_ID_YEAR=345972 AND A_20.name is not null AND (A_15.SPONSORED_RUNNER ='Y' or A_15.IS_CUSTOMER = 'Y') and A_15.RUN_TIME is not null group by A_15.run_id, A_20.name, A_20.organizational_id, A_18.short_name, A_15.SPONSORED_RUNNER union all SELECT A_15.run_id, A_20.name charity, A_21.organizational_id ssn, A_21.amount_payed FROM RUN A_15 INNER JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) INNER JOIN RUN_PLEDGE A_21 on (A_15.RUN_ID=A_21.PARTICIPANT_ID) WHERE A_15.IC_GROUP_ID_RUN=4 AND A_15.IC_GROUP_ID_YEAR=345972 AND A_20.name is not null and A_15.RUN_TIME is not null) temp group by charity,ssn order by charity",
												"select count(distinct run_id) runner_count, count(pledge_sum) pledge_count, sum(pledge_sum) pledge_sum from (SELECT A_15.run_id,A_20.name charity, A_20.organizational_id ssn, case when A_15.SPONSORED_RUNNER = 'Y' then 3000 * cast(A_18.short_name as numeric) else 500 * cast(A_18.short_name as numeric) end pledge_sum FROM IC_USER A_1 LEFT JOIN IC_GROUP_RELATION A_13 ON (A_1.IC_USER_ID=A_13.RELATED_IC_GROUP_ID) LEFT JOIN IC_GROUP A_14 ON (A_13.IC_GROUP_ID=A_14.IC_GROUP_ID) LEFT JOIN RUN A_15 ON (A_1.IC_USER_ID=A_15.IC_USER_ID) LEFT JOIN IC_GROUP A_18 ON (A_15.IC_GROUP_ID_DISTANCE=A_18.IC_GROUP_ID) LEFT JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) WHERE A_13.GROUP_RELATION_STATUS = 'ST_ACTIVE' AND A_13.RELATIONSHIP_TYPE='GROUP_PARENT' AND A_14.IC_GROUP_ID = A_15.IC_GROUP_ID_GROUP AND A_15.IC_GROUP_ID_RUN=4 AND A_15.IC_GROUP_ID_YEAR=345972 AND A_20.name is not null AND (A_15.SPONSORED_RUNNER ='Y' or A_15.IS_CUSTOMER = 'Y') and A_15.RUN_TIME is not null group by A_15.run_id, A_20.name, A_20.organizational_id, A_18.short_name, A_15.SPONSORED_RUNNER union all SELECT A_15.run_id, A_20.name charity, A_21.organizational_id ssn, A_21.amount_payed FROM RUN A_15 INNER JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) INNER JOIN RUN_PLEDGE A_21 on (A_15.RUN_ID=A_21.PARTICIPANT_ID) WHERE A_15.IC_GROUP_ID_RUN=4 AND A_15.IC_GROUP_ID_YEAR=345972 AND A_20.name is not null and A_15.RUN_TIME is not null) temp",
												"select g.short_name distance, count(*) runner_count from run r, ic_group g where r.ic_group_id_distance = g.ic_group_id and r.ic_group_id_run = 4 and r.ic_group_id_year = 345972 group by g.short_name",
												"select count(distinct ic_user_id) runner_count, count(pledge_sum) pledge_count, sum(pledge_sum) pledge_sum from (SELECT A_15.ic_user_id,A_20.name charity, A_20.organizational_id ssn, 3000 * cast(A_18.short_name as numeric) pledge_sum FROM IC_USER A_1 LEFT JOIN IC_GROUP_RELATION A_13 ON (A_1.IC_USER_ID=A_13.RELATED_IC_GROUP_ID) LEFT JOIN IC_GROUP A_14 ON (A_13.IC_GROUP_ID=A_14.IC_GROUP_ID) LEFT JOIN RUN A_15 ON (A_1.IC_USER_ID=A_15.IC_USER_ID) LEFT JOIN IC_GROUP A_18 ON (A_15.IC_GROUP_ID_DISTANCE=A_18.IC_GROUP_ID) LEFT JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) WHERE A_13.GROUP_RELATION_STATUS = 'ST_ACTIVE' AND A_13.RELATIONSHIP_TYPE='GROUP_PARENT' AND A_14.IC_GROUP_ID = A_15.IC_GROUP_ID_GROUP AND A_15.IC_GROUP_ID_RUN=4 AND A_15.IC_GROUP_ID_YEAR=345972 AND A_15.SPONSORED_RUNNER ='Y' group by A_15.ic_user_id, A_20.name, A_20.organizational_id, A_18.short_name, A_15.SPONSORED_RUNNER union all SELECT A_15.ic_user_id, A_20.name charity, A_21.organizational_id ssn, A_21.amount_payed FROM RUN A_15 INNER JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) INNER JOIN RUN_PLEDGE A_21 on (A_15.RUN_ID=A_21.PARTICIPANT_ID) WHERE A_15.IC_GROUP_ID_RUN=4 AND A_15.IC_GROUP_ID_YEAR=345972 and A_15.SPONSORED_RUNNER ='Y') temp",
												"select count(distinct run_id) runner_count, count(pledge_sum) pledge_count, sum(pledge_sum) pledge_sum from (SELECT A_15.run_id,A_20.name charity, A_20.organizational_id ssn, cast(800 as numeric) pledge_sum FROM IC_USER A_1 LEFT JOIN IC_GROUP_RELATION A_13 ON (A_1.IC_USER_ID=A_13.RELATED_IC_GROUP_ID) LEFT JOIN IC_GROUP A_14 ON (A_13.IC_GROUP_ID=A_14.IC_GROUP_ID) LEFT JOIN RUN A_15 ON (A_1.IC_USER_ID=A_15.IC_USER_ID) LEFT JOIN IC_GROUP A_18 ON (A_15.IC_GROUP_ID_DISTANCE=A_18.IC_GROUP_ID) LEFT JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) WHERE A_13.GROUP_RELATION_STATUS = 'ST_ACTIVE' AND A_13.RELATIONSHIP_TYPE='GROUP_PARENT' AND A_14.IC_GROUP_ID = A_15.IC_GROUP_ID_GROUP AND A_15.IC_GROUP_ID_RUN=383348 AND A_15.IC_GROUP_ID_YEAR=383349 group by A_15.run_id, A_20.name, A_20.organizational_id, A_18.short_name union all SELECT A_15.run_id, A_20.name charity, A_21.organizational_id ssn, A_21.amount_payed FROM RUN A_15 INNER JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) INNER JOIN RUN_PLEDGE A_21 on (A_15.RUN_ID=A_21.PARTICIPANT_ID) WHERE A_15.IC_GROUP_ID_RUN=383348 AND A_15.IC_GROUP_ID_YEAR=383349) temp",
												"SELECT 'Áheit Glitnis á starfsmenn' as name,sum(3000 * cast(A_18.short_name as numeric)) pledge_sum FROM IC_USER A_1 LEFT JOIN IC_GROUP_RELATION A_13 ON (A_1.IC_USER_ID=A_13.RELATED_IC_GROUP_ID) LEFT JOIN IC_GROUP A_14 ON (A_13.IC_GROUP_ID=A_14.IC_GROUP_ID) LEFT JOIN RUN A_15 ON (A_1.IC_USER_ID=A_15.IC_USER_ID) LEFT JOIN IC_GROUP A_18 ON (A_15.IC_GROUP_ID_DISTANCE=A_18.IC_GROUP_ID) LEFT JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) WHERE A_13.GROUP_RELATION_STATUS = 'ST_ACTIVE' AND A_13.RELATIONSHIP_TYPE='GROUP_PARENT' AND A_14.IC_GROUP_ID = A_15.IC_GROUP_ID_GROUP AND A_15.IC_GROUP_ID_RUN=4 AND A_15.IC_GROUP_ID_YEAR=345972 AND A_15.SPONSORED_RUNNER ='Y' and A_15.run_time is not null union SELECT 'Áheit Glitnis á viðskiptavini',sum(500 * cast(A_18.short_name as numeric)) pledge_sum FROM IC_USER A_1 LEFT JOIN IC_GROUP_RELATION A_13 ON (A_1.IC_USER_ID=A_13.RELATED_IC_GROUP_ID) LEFT JOIN IC_GROUP A_14 ON (A_13.IC_GROUP_ID=A_14.IC_GROUP_ID) LEFT JOIN RUN A_15 ON (A_1.IC_USER_ID=A_15.IC_USER_ID) LEFT JOIN IC_GROUP A_18 ON (A_15.IC_GROUP_ID_DISTANCE=A_18.IC_GROUP_ID) LEFT JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) WHERE A_13.GROUP_RELATION_STATUS = 'ST_ACTIVE' AND A_13.RELATIONSHIP_TYPE='GROUP_PARENT' AND A_14.IC_GROUP_ID = A_15.IC_GROUP_ID_GROUP AND A_15.IC_GROUP_ID_RUN=4 AND A_15.IC_GROUP_ID_YEAR=345972 AND A_20.name is not null AND A_15.IS_CUSTOMER='Y' AND A_15.SPONSORED_RUNNER ='N' and A_15.run_time is not null union select 'Almenn áheit á starfsmenn',sum(cast(amount_payed as numeric)) from run r,run_pledge rp where r.run_id=rp.participant_id and r.IC_GROUP_ID_YEAR=345972 and sponsored_runner='Y' and r.charity_organizational_id is not null and r.run_time is not null union select 'Almenn áheit á viðskiptavini',sum(cast(amount_payed as numeric)) from run r,run_pledge rp where r.run_id=rp.participant_id and r.IC_GROUP_ID_YEAR=345972 and is_customer='Y' and sponsored_runner='N' and r.charity_organizational_id is not null and r.run_time is not null union select 'Almenn áheit á aðra',sum(cast(amount_payed as numeric)) from run r,run_pledge rp where r.run_id=rp.participant_id and r.IC_GROUP_ID_YEAR=345972 and is_customer='N' and sponsored_runner='N' and r.charity_organizational_id is not null and r.run_time is not null",
												"SELECT A_1.DISPLAY_NAME name, A_21.name department, A_18.short_name distance, CONVERT(VARCHAR(10),DATEADD(second, A_15.RUN_TIME,'1970-01-01'), 108) run_time FROM IC_USER A_1 LEFT JOIN IC_USER_EMAIL A_10 ON (A_1.IC_USER_ID=A_10.IC_USER_ID) LEFT JOIN IC_EMAIL A_8 ON (A_10.IC_EMAIL_ID=A_8.IC_EMAIL_ID) LEFT JOIN IC_GROUP_RELATION A_13 ON (A_1.IC_USER_ID=A_13.RELATED_IC_GROUP_ID) LEFT JOIN IC_GROUP A_14 ON (A_13.IC_GROUP_ID=A_14.IC_GROUP_ID) LEFT JOIN RUN A_15 ON (A_1.IC_USER_ID=A_15.IC_USER_ID) LEFT JOIN IC_GROUP A_18 ON (A_15.IC_GROUP_ID_DISTANCE=A_18.IC_GROUP_ID) LEFT JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) LEFT JOIN RUN_CATEGORY A_21 ON (A_15.RUN_CATEGORY_ID=A_21.RUN_CATEGORY_ID) WHERE A_13.GROUP_RELATION_STATUS = 'ST_ACTIVE' AND A_13.RELATIONSHIP_TYPE='GROUP_PARENT' AND A_14.IC_GROUP_ID = A_15.IC_GROUP_ID_GROUP AND A_15.IC_GROUP_ID_RUN=4 AND A_15.IC_GROUP_ID_YEAR=345972 AND A_15.SPONSORED_RUNNER ='Y' AND A_15.RUN_TIME not in ('-1','-2') group by A_1.DISPLAY_NAME, A_1.PERSONAL_ID, A_15.run_id, A_21.name, A_8.address, A_20.name, A_18.short_name, A_15.RUN_TIME order by A_18.short_name, A_15.RUN_TIME",
												"select charity, ssn, sum(employee_distance) sum_emp_distance, sum(customer_distance) sum_cust_distance, sum(pledge_sum) pledge_sum from (SELECT A_15.run_id,A_20.name charity, A_20.organizational_id ssn, case when A_15.SPONSORED_RUNNER = 'Y' then cast(A_18.short_name as real) else 0 end employee_distance, case when A_15.SPONSORED_RUNNER = 'N' then cast(A_18.short_name as real) else 0 end customer_distance, case when A_15.SPONSORED_RUNNER = 'Y' then 3000 * cast(A_18.short_name as real) else 500 * cast(A_18.short_name as real) end pledge_sum FROM IC_USER A_1 LEFT JOIN IC_GROUP_RELATION A_13 ON (A_1.IC_USER_ID=A_13.RELATED_IC_GROUP_ID) LEFT JOIN IC_GROUP A_14 ON (A_13.IC_GROUP_ID=A_14.IC_GROUP_ID) LEFT JOIN RUN A_15 ON (A_1.IC_USER_ID=A_15.IC_USER_ID) LEFT JOIN IC_GROUP A_18 ON (A_15.IC_GROUP_ID_DISTANCE=A_18.IC_GROUP_ID) LEFT JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) WHERE A_13.GROUP_RELATION_STATUS = 'ST_ACTIVE' AND A_13.RELATIONSHIP_TYPE='GROUP_PARENT' AND A_14.IC_GROUP_ID = A_15.IC_GROUP_ID_GROUP AND A_15.IC_GROUP_ID_RUN=4 AND A_15.IC_GROUP_ID_YEAR=345972 AND A_20.name is not null AND (A_15.SPONSORED_RUNNER ='Y' or A_15.IS_CUSTOMER = 'Y') and A_15.RUN_TIME is not null group by A_15.run_id, A_20.name, A_20.organizational_id, A_18.short_name, A_15.SPONSORED_RUNNER union all SELECT A_15.run_id, A_20.name charity, A_21.organizational_id ssn, 0, 0, A_21.amount_payed FROM RUN A_15 INNER JOIN RUN_CHARITY_ORGANIZATION A_20 ON (A_15.CHARITY_ORGANIZATIONAL_ID=A_20.ORGANIZATIONAL_ID) INNER JOIN RUN_PLEDGE A_21 on (A_15.RUN_ID=A_21.PARTICIPANT_ID) WHERE A_15.IC_GROUP_ID_RUN=4 AND A_15.IC_GROUP_ID_YEAR=345972 AND A_20.name is not null and A_15.RUN_TIME is not null) temp group by charity,ssn order by charity",
												"select p.cardholder_name payer, u.display_name runner, o.name charity, p.amount_payed from run r, run_pledge p, ic_user u, run_charity_organization o where r.ic_group_id_run = 4 and r.ic_group_id_year = 345972 and r.ic_user_id = u.ic_user_id and p.participant_id = r.run_id and p. organizational_id = o. organizational_id and not r.run_time is null order by runner, charity"
												};
	
	protected static final int SHOW_REPORT_LIST = 0;
	protected static final int SHOW_REPORT = 1;
	
	protected static final String PARAMETER_ACTION = "prm_action";
	protected static final String PARAMETER_REPORT = "prm_report";
	
	protected IWResourceBundle iwrb;
	
	public void main(IWContext iwc) {
		setResourceBundle(getResourceBundle(iwc));
		switch (parseAction(iwc)) {
			case SHOW_REPORT_LIST:
				showReportsList(iwc);
				break;

			case SHOW_REPORT:
				showReport(iwc);
				break;
		}
	}
	
	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}
	
	protected IWResourceBundle getResourceBundle() {
		return this.iwrb;
	}

	protected void setResourceBundle(IWResourceBundle resourceBundle) {
		this.iwrb = resourceBundle;
	}
	
	protected void showReportsList(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, "");
		
		Layer casesSection = new Layer(Layer.DIV);
		casesSection.setStyleClass("formSection");
		
		for(int i = 0; i < REPORT_NAMES.length; i++) {
			Link reportLink = new Link(REPORT_NAMES[i]);
			reportLink.setParameter(PARAMETER_ACTION, new Integer(SHOW_REPORT).toString());
			reportLink.setParameter(PARAMETER_REPORT, "REPORT_" + i);
			
			casesSection.add(reportLink);
			casesSection.add(new Break(2));
		}
		
		form.add(casesSection);
		
		add(form);
	}
	
	public String localize(String textKey, String defaultText) {
		if (this.iwrb == null) {
			return defaultText;
		}
		return this.iwrb.getLocalizedString(textKey, defaultText);
	}
	
	protected void showReport(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, "");
		
		AddResource resource = AddResourceFactory.getInstance(iwc.getCurrentInstance());
		resource.addInlineStyleAtPosition(iwc.getCurrentInstance(), AddResource.HEADER_BEGIN, ".content {width:1090px !important;}");
		
		Layer casesSection = new Layer(Layer.DIV);
		casesSection.setStyleClass("formSection");
		
		SubmitButton reload = new SubmitButton("Sk\u00FDrslulisti", PARAMETER_ACTION, new Integer(SHOW_REPORT_LIST).toString());
		reload.setStyleClass("button");
		reload.setID("reportBackToList");
		casesSection.add(reload);

		Table2 table = new Table2();
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setStyleClass("adminTable");
		table.setStyleClass("ruler");
		table.setID("pledgeReportsBlock");

		TableColumnGroup columnGroup = table.createColumnGroup();
		TableColumn column = columnGroup.createColumn();
		column.setSpan(6);
		column = columnGroup.createColumn();
		column.setSpan(1);
		column.setWidth("12");
		
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		
		TableCell2 cell = null;
		
		String report = iwc.getParameter(PARAMETER_REPORT);
		
		int reportIndex = Integer.parseInt(report.substring(7));
		
		Connection conn = null;
		PreparedStatement Stmt = null;
		ResultSet RS = null;
		try {
			conn = ConnectionBroker.getConnection("default");
			Stmt = conn.prepareStatement(REPORT_SQL[reportIndex]);
			RS = Stmt.executeQuery();
			
			try {
				
				ResultSetMetaData rsmd = RS.getMetaData();
				String[] columnNames = new String[rsmd.getColumnCount()];
				
				for (int i=0; i<columnNames.length; i++) {
					String columnName = rsmd.getColumnName(i+1);
					columnNames[i] = columnName;
					cell = row.createHeaderCell();
					cell.setStyleClass("reportColumnHeader");
					cell.add(new Text(localize(columnName, columnName)));
				}
				
				group = table.createBodyRowGroup();
				int iRow = 1;
				
				while (RS.next()) {
					row = group.createRow();
					for (int i=0; i<columnNames.length; i++) {
						cell = row.createCell();
						cell.setStyleClass("reportColumn");
						cell.add(new Text(RS.getString(columnNames[i])));
						
					}
					
					if (iRow % 2 == 0) {
						row.setStyleClass("evenRow");
					}
					else {
						row.setStyleClass("oddRow");
					}
					
					iRow++;
				}
			} catch(SQLException sqle) {
				logger.log(Level.SEVERE, sqle.getMessage());
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				if (RS != null) {
					RS.close();
				}
				if (Stmt != null) {
					Stmt.close();
				}
			} catch(SQLException ex) {
				logger.log(Level.SEVERE, ex.getMessage());
			}
			if (conn != null) {
				ConnectionBroker.freeConnection("default", conn);
			}
		}
		
		casesSection.add(table);
		form.add(casesSection);
				
		add(form);
	}
	
	private int parseAction(IWContext iwc) {
		int action = SHOW_REPORT_LIST;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}

		return action;
	}

}
