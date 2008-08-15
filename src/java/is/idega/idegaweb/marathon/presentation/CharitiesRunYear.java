package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.Group;

public class CharitiesRunYear extends RunBlock {
	private static final String PRM_ACTION = "marathon_prm_action";
	private static final String PARAMETER_CHARITY = "charity_id";
	private static final String PARAMETER_MARATHON_PK = "prm_run_pk";
	private static final String PARAMETER_MARATHON_YEAR_PK = "prm_run_year_pk";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_SAVE = 2;

	public void main(IWContext iwc) throws Exception {
		init(iwc);
	}

	protected void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
		case ACTION_VIEW:
			showList(iwc);
			break;

		case ACTION_SAVE:
			save(iwc);
			showList(iwc);
			break;
		}
	}

	public void save(IWContext iwc) throws java.rmi.RemoteException,
			CreateException, NumberFormatException, FinderException {

		if (iwc.isParameterSet(PARAMETER_MARATHON_YEAR_PK)) {
			String yearID = iwc.getParameter(PARAMETER_MARATHON_YEAR_PK);
			Group selectedYear = getRunBusiness(iwc).getRunGroupByGroupId(Integer.valueOf(yearID));
			Collection charities = this.getRunBusiness(iwc).getCharityBusiness().getCharitiesByRunYearID(new Integer(yearID));

			Iterator it = charities.iterator(); 
			while (it.hasNext()) {
				Charity charity = (Charity) it.next();
				charity.removeFromGroup(selectedYear);
			}
			
			String newCharitiesID[] = iwc.getParameterValues(PARAMETER_CHARITY);
			for (int i = 0; i < newCharitiesID.length; i++) {
				String newCharityID = newCharitiesID[i];
				Charity charity = this.getRunBusiness(iwc).getCharityBusiness().getCharityHome().findByPrimaryKey(new Integer(newCharityID));
				charity.addToGroup(selectedYear);
			}
		}
		
	}

	protected int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PRM_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PRM_ACTION));
		}
		return ACTION_VIEW;
	}

	public void showList(IWContext iwc) throws RemoteException, FinderException {
		Form form = new Form();

		Table2 table = new Table2();
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setCellHorizontalAlignment(Table2.HORIZONTAL_ALIGNMENT_LEFT);

		Collection runs = getRunBusiness(iwc).getRuns();
		Iterator runIt = runs.iterator();
		DropdownMenu runDropDown = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_MARATHON_PK));
		runDropDown.addMenuElement("", localize("select_run", "Select run..."));
		while (runIt.hasNext()) {
			Group run = (Group) runIt.next();
			runDropDown.addMenuElement(run.getPrimaryKey().toString(),
					localize(run.getName(), run.getName()));
		}
		runDropDown.setToSubmit();
		cell.add(runDropDown);

		if (iwc.isParameterSet(PARAMETER_MARATHON_PK)) {
			String runID = iwc.getParameter(PARAMETER_MARATHON_PK);
			runDropDown.setSelectedElement(runID);
			Group run = getRunBusiness(iwc).getRunGroupByGroupId(
					Integer.valueOf(runID));
			String[] yearType = { IWMarathonConstants.GROUP_TYPE_RUN_YEAR };
			Collection years = run.getChildGroups(yearType, true);
			Iterator yearIt = years.iterator();
			DropdownMenu yearDropDown = (DropdownMenu) getStyledInterface(new DropdownMenu(
					PARAMETER_MARATHON_YEAR_PK));
			yearDropDown.addMenuElement("", localize("select_year",
					"Select year"));
			while (yearIt.hasNext()) {
				Group year = (Group) yearIt.next();
				yearDropDown.addMenuElement(year.getPrimaryKey().toString(),
						localize(year.getName(), year.getName()));
			}
			yearDropDown.setToSubmit();
			group.createRow().createCell().add(yearDropDown);

			Collection charities = null;
			Collection allCharities = null;
			if (iwc.isParameterSet(PARAMETER_MARATHON_YEAR_PK)) {
				String yearID = iwc.getParameter(PARAMETER_MARATHON_YEAR_PK);
				yearDropDown.setSelectedElement(yearID);

				charities = this.getRunBusiness(iwc).getCharityBusiness().getCharitiesByRunYearID(new Integer(yearID));
				allCharities = this.getRunBusiness(iwc).getCharityBusiness().getAllCharities();
				
				group.createRow().createCell().setHeight("20");
				row = group.createRow();
				cell = row.createHeaderCell();
				cell
						.setCellHorizontalAlignment(Table2.HORIZONTAL_ALIGNMENT_LEFT);
				cell.add(new Text(""));
				cell = row.createHeaderCell();
				cell
						.setCellHorizontalAlignment(Table2.HORIZONTAL_ALIGNMENT_LEFT);
				cell.add(new Text(localize("name", "Name")));
				cell = row.createHeaderCell();
				cell
						.setCellHorizontalAlignment(Table2.HORIZONTAL_ALIGNMENT_LEFT);
				cell.add(new Text(localize("organizational_id", "organizational_id")));

				
				
				group = table.createBodyRowGroup();

				if (allCharities != null) {
					Iterator iter = allCharities.iterator();
					while (iter.hasNext()) {
						row = group.createRow();
						Charity charity = (Charity) iter.next();
						try {
							CheckBox check = new CheckBox(PARAMETER_CHARITY, charity.getPrimaryKey().toString());
							check.setChecked(false);
							if (charities != null) {
								if (charities.contains(charity)) {
									check.setChecked(true);
								}
							}

							row.createCell().add(check);
							row.createCell().add(new Text(charity.getName()));
							row.createCell().add(new Text(charity.getOrganizationalID()));
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		}
		form.add(table);
		form.add(new Break());
		if (iwc.isParameterSet(PARAMETER_MARATHON_YEAR_PK)) {
			form.maintainParameter(PARAMETER_MARATHON_PK);
			form.maintainParameter(PARAMETER_MARATHON_YEAR_PK);
			SubmitButton newLink = (SubmitButton) getButton(new SubmitButton(
					localize("save", "Save"), PRM_ACTION,
					String.valueOf(ACTION_SAVE)));
			form.add(newLink);
		}
		add(form);
	}
}