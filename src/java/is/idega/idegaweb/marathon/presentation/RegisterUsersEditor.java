package is.idega.idegaweb.marathon.presentation;


import java.rmi.RemoteException;

import javax.ejb.CreateException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

public class RegisterUsersEditor extends RunBlock {
	
	private static final String PRM_ACTION = "marathon_prm_action";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_MARATHON_PK = "prm_run_pk";
	private static final String PARAMETER_MARATHON_YEAR_PK = "prm_run_year_pk";
	private static final String PARAMETER_MARATHON_DISTANCE_PK = "prm_run_distance_pk";
	
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_NEW = 2;
	private static final int ACTION_SAVE = 3;
	
	public void main(IWContext iwc) throws Exception {
		init(iwc);
	}
	
	protected void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				showList(iwc);
				break;

			case ACTION_NEW:
				showEditor(iwc);
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

		ActiveRunDropDownMenu runDropdown = (ActiveRunDropDownMenu) getStyledInterface(new ActiveRunDropDownMenu(PARAMETER_MARATHON_PK));
		runDropdown.setAsNotEmpty(localize("run_reg.must_select_run", "You have to select a run"));
		runDropdown.setToSubmit();
		cell.add(runDropdown);
		
		if (iwc.isParameterSet(PARAMETER_MARATHON_PK)) {
			String runID = iwc.getParameter(PARAMETER_MARATHON_PK);
			runDropdown.setSelectedElement(runID);
			DistanceDropDownMenu distanceDropdown = (DistanceDropDownMenu) getStyledInterface(new DistanceDropDownMenu(PARAMETER_MARATHON_DISTANCE_PK));
			distanceDropdown.setToSubmit();
			group.createRow().createCell().add(distanceDropdown);
			
			if (iwc.isParameterSet(PARAMETER_MARATHON_DISTANCE_PK)) {
				String distanceID = iwc.getParameter(PARAMETER_MARATHON_DISTANCE_PK);
				distanceDropdown.setSelectedElement(distanceID);
				group.createRow().createCell().setHeight("20");
				row = group.createRow();
				cell = row.createHeaderCell();
				cell.setCellHorizontalAlignment(Table2.HORIZONTAL_ALIGNMENT_LEFT);
				cell.add(new Text(localize("register_user", "Register user")));
				
				group = table.createBodyRowGroup();
				
				TextInput name = new TextInput(PARAMETER_NAME);
				
				Layer layer = new Layer(Layer.DIV);
				layer.setStyleClass(STYLENAME_FORM_ELEMENT);
				Label label = new Label(localize("run_tab.name", "Name"), name);
				layer.add(label);
				layer.add(name);
				form.add(layer);
				form.add(new Break());
				
				SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PRM_ACTION, String.valueOf(ACTION_SAVE)));
				SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize("cancel", "Cancel"), PRM_ACTION, String.valueOf(ACTION_VIEW)));

				form.add(save);
				form.add(cancel);
				
				if (distanceID != null) {
					//if needed to load data
				}
			}
		}
		form.add(table);
		form.add(new Break());
		if (iwc.isParameterSet(PARAMETER_MARATHON_YEAR_PK)) {
			SubmitButton newLink = (SubmitButton) getButton(new SubmitButton(localize("new_distance", "New distance"), PRM_ACTION, String.valueOf(ACTION_NEW)));
			form.add(newLink);
		}
		add(form);
	}
	
	public void showEditor(IWContext iwc) throws java.rmi.RemoteException {
		Form form = new Form();
		
		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PRM_ACTION, String.valueOf(ACTION_SAVE)));
		SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize("cancel", "Cancel"), PRM_ACTION, String.valueOf(ACTION_VIEW)));

		form.add(save);
		form.add(cancel);
		add(form);
	}

	public void save(IWContext iwc) throws java.rmi.RemoteException, CreateException {
		
	}
	
	protected int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PRM_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PRM_ACTION));
		}
		return ACTION_VIEW;
	}
}