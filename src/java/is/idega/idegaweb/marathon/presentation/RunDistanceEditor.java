package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.Group;

public class RunDistanceEditor extends RunBlock {
	
	private static final String PARAMETER_ACTION = "marathon_prm_action";
	private static final String PARAMETER_MARATHON_PK = "prm_run_pk";
	private static final String PARAMETER_MARATHON_YEAR_PK = "prm_run_year_pk";
	private static final String PARAMETER_MARATHON_DISTANCE_PK = "prm_run_distance_pk";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;
	
	public void main(IWContext iwc) throws Exception {
		init(iwc);
	}
	
	protected void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				showList(iwc);
				break;

			case ACTION_EDIT:
				String distanceID = iwc.getParameter(PARAMETER_MARATHON_DISTANCE_PK);
				showEditor(iwc, distanceID);
				break;

			case ACTION_NEW:
				showEditor(iwc, null);
				break;

			case ACTION_SAVE:
				save(iwc);
				showList(iwc);
				break;
		}
	}

	public void showList(IWContext iwc) throws RemoteException {
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
		DropdownMenu runDropDown = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_MARATHON_PK)); 
		runDropDown.addMenuElement("", localize("select_run","Select run"));
		Group run = null;
		while (runIt.hasNext()) {
			run = (Group)runIt.next();
			runDropDown.addMenuElement(run.getPrimaryKey().toString(), localize(run.getName(),run.getName()));
		}
		runDropDown.setToSubmit();
		cell.add(runDropDown);
		
		if (iwc.isParameterSet(PARAMETER_MARATHON_PK)) {
			String runID = iwc.getParameter(PARAMETER_MARATHON_PK);
			runDropDown.setSelectedElement(runID);
			String[] yearType = {IWMarathonConstants.GROUP_TYPE_RUN_YEAR};
			Collection years = run.getChildGroups(yearType,true); 
			Iterator yearIt = years.iterator();
			DropdownMenu yearDropDown = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_MARATHON_YEAR_PK)); 
			yearDropDown.addMenuElement("", localize("select_year","Select year"));
			while (yearIt.hasNext()) {
				Group year = (Group)yearIt.next();
				yearDropDown.addMenuElement(year.getPrimaryKey().toString(), localize(year.getName(),year.getName()));
			}
			yearDropDown.setToSubmit();
			group.createRow().createCell().add(yearDropDown);
			
			Collection distances = null;
			if (iwc.isParameterSet(PARAMETER_MARATHON_YEAR_PK)) {
				String yearID = iwc.getParameter(PARAMETER_MARATHON_YEAR_PK);
				yearDropDown.setSelectedElement(yearID);
				Group selectedYear = getRunBusiness(iwc).getRunGroupByGroupId(Integer.valueOf(yearID));
			    String[] distanceType = {IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE};
			    distances = selectedYear.getChildGroups(distanceType,true);
			    group.createRow().createCell().setHeight("20");
				row = group.createRow();
				cell = row.createHeaderCell();
				cell.setCellHorizontalAlignment(Table2.HORIZONTAL_ALIGNMENT_LEFT);
				cell.add(new Text(localize("name", "Name")));
				group = table.createBodyRowGroup();
				int iRow = 1;
				
				if (distances != null) {
					Iterator iter = distances.iterator();
					while (iter.hasNext()) {
						row = group.createRow();
						Group distance = (Group) iter.next();
						try {
							Link edit = new Link(getEditIcon(localize("edit", "Edit")));
							edit.addParameter(PARAMETER_MARATHON_DISTANCE_PK, distance.getPrimaryKey().toString());
							edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);
										
							cell = row.createCell();
							cell.add(new Text(distance.getName()));
							row.createCell().add(edit);
						}
						catch (Exception ex) {
							ex.printStackTrace();
						}
						iRow++;
					}
				}
			}
		}
		form.add(table);
		form.add(new Break());
		if (iwc.isParameterSet(PARAMETER_MARATHON_YEAR_PK)) {
			SubmitButton newLink = (SubmitButton) getButton(new SubmitButton(localize("new_distance", "New distance"), PARAMETER_ACTION, String.valueOf(ACTION_NEW)));
			form.add(newLink);
		}
		add(form);
	}
	
	public void showEditor(IWContext iwc, String distanceID) throws java.rmi.RemoteException {
		System.out.println(distanceID);
	}

	public void save(IWContext iwc) throws java.rmi.RemoteException {
		
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}
}