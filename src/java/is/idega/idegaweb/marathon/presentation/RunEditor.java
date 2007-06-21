package is.idega.idegaweb.marathon.presentation;


import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;

public class RunEditor extends RunBlock {
	
	private static final String PARAMETER_MARATHON_PK = "prm_run_pk";
	private static final String PARAMETER_RUN = "prm_run";
	private static final String PARAMETER_RUN_HOME_PAGE = "prm_run_home_page";
	private static final String PARAMETER_RUN_INFORMATION_PAGE = "prm_run_information_page";
	private static final String PARAMETER_ENGLISH_RUN_INFORMATION_PAGE = "prm_english_run_information_page";

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
				showEditor(iwc);
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
				try {
					Link edit = new Link(getEditIcon(localize("edit", "Edit")));
					edit.addParameter(PARAMETER_MARATHON_PK, run.getPrimaryKey().toString());
					edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);
								
					cell = row.createCell();
					cell.add(new Text(run.getName()));
					row.createCell().add(edit);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
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
		String runID = iwc.getParameter(PARAMETER_MARATHON_PK);
		Form form = new Form();
		form.maintainParameter(PARAMETER_MARATHON_PK);
		TextInput runInput = new TextInput(PARAMETER_RUN);
		
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		Label label = new Label(localize("run_tab.run", "Run"), runInput);
		layer.add(label);
		layer.add(runInput);
		form.add(layer);
		form.add(new Break());
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		TextInput runHomePageInput = new TextInput(PARAMETER_RUN_HOME_PAGE);
		Label runHomePageLabel = new Label(localize("run_tab.run_home_page", "Home page"), runHomePageInput);
		layer.add(runHomePageLabel);
		layer.add(runHomePageInput);
		form.add(layer);
		form.add(new Break());
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		TextInput runInformationPageInput = new TextInput(PARAMETER_RUN_INFORMATION_PAGE);
		Label runInformationPageLabel = new Label(localize("run_tab.run_information_page", "Information page"), runInformationPageInput);
		layer.add(runInformationPageLabel);
		layer.add(runInformationPageInput);
		form.add(layer);
		form.add(new Break());
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		TextInput englishRunInformationPageInput = new TextInput(PARAMETER_ENGLISH_RUN_INFORMATION_PAGE);
		Label englishRunInformationPageLabel = new Label(localize("run_tab.english_run_information_page", "English information page"), englishRunInformationPageInput);
		layer.add(englishRunInformationPageLabel);
		layer.add(englishRunInformationPageInput);
		form.add(layer);
		form.add(new Break());


		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize("cancel", "Cancel"), PARAMETER_ACTION, String.valueOf(ACTION_VIEW)));

		form.add(save);
		form.add(cancel);
		
		if (runID != null) {
			//Group selectedRun = getRunBusiness(iwc).getRunGroupByGroupId(Integer.valueOf(runID.toString()));
			Run selectedRun = null;
			try {
				selectedRun = ConverterUtility.getInstance().convertGroupToRun(new Integer(runID));
				runInput.setValue(selectedRun.getName());
				runInput.setDisabled(true);
				runHomePageInput.setValue(selectedRun.getRunHomePage());
				runInformationPageInput.setValue(selectedRun.getRunInformationPage());
				englishRunInformationPageInput.setValue(selectedRun.getEnglishRunInformationPage());
			} catch (FinderException e) {
				//run not found
			}
		}
		
		add(form);
	}

	public void save(IWContext iwc) throws java.rmi.RemoteException, CreateException {
		String runID = iwc.getParameter(PARAMETER_MARATHON_PK);
		Run run = null;
		if (runID == null) {
			String runName = iwc.getParameter(PARAMETER_RUN);
			Group newRun = getGroupBiz(iwc).createGroup(runName, "", IWMarathonConstants.GROUP_TYPE_RUN);
			runID = newRun.getPrimaryKey().toString();
		}
		if (runID != null) {
			try {
				run = ConverterUtility.getInstance().convertGroupToRun(new Integer(runID));
			} 
			catch (FinderException e){
				//no distance found, nothing saved
			}
			if (run != null) { 
				run.setRunHomePage(iwc.getParameter(PARAMETER_RUN_HOME_PAGE));
				run.setRunInformationPage(iwc.getParameter(PARAMETER_RUN_INFORMATION_PAGE));
				run.setEnglishRunInformationPage(iwc.getParameter(PARAMETER_ENGLISH_RUN_INFORMATION_PAGE));
				run.store();
			}
		}
			
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