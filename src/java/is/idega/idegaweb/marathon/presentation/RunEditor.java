package is.idega.idegaweb.marathon.presentation;


import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
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
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;

public class RunEditor extends RunBlock {
	
	private static final String PARAMETER_ACTION = "marathon_prm_action";
	private static final String PARAMETER_RUN = "prm_run";

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
				
		Collection runs = getRunBusiness(iwc).getRuns();
		
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setCellHorizontalAlignment(Table2.HORIZONTAL_ALIGNMENT_LEFT);
		cell.add(new Text(localize("existing_runs", "Existing runs")));
		group = table.createBodyRowGroup();
		int iRow = 1;
		
		if (runs != null) {
			Iterator iter = runs.iterator();
			Group run;
			while (iter.hasNext()) {
				row = group.createRow();
				run = (Group) iter.next();
				cell = row.createCell();
				cell.add(new Text(run.getName()));
				iRow++;
			}
		}
		form.add(table);
		form.add(new Break());
		SubmitButton newLink = (SubmitButton) getButton(new SubmitButton(localize("new_run", "New run"), PARAMETER_ACTION, String.valueOf(ACTION_NEW)));
		form.add(newLink);
		add(form);
	}
	
	public void showEditor(IWContext iwc) throws java.rmi.RemoteException {
		Form form = new Form();
		TextInput run = new TextInput(PARAMETER_RUN);
		
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		Label label = new Label(localize("run_tab.run", "Run"), run);
		layer.add(label);
		layer.add(run);
		form.add(layer);
		form.add(new Break());
		
		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize("cancel", "Cancel"), PARAMETER_ACTION, String.valueOf(ACTION_VIEW)));

		form.add(save);
		form.add(cancel);
		add(form);
	}

	public void save(IWContext iwc) throws java.rmi.RemoteException, CreateException {
		String runName = iwc.getParameter(PARAMETER_RUN);
		getGroupBiz(iwc).createGroup(runName, "", IWMarathonConstants.GROUP_TYPE_RUN);
		
	}
	
	protected int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}
	
	private GroupBusiness getGroupBiz(IWContext iwc) {
		GroupBusiness business = null;
		try {
			business = (GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
		} catch (IBOLookupException e) {
			business = null;
		}
		return business;
	}
}