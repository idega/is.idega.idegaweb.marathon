package is.idega.idegaweb.marathon.presentation;

import java.rmi.RemoteException;
import java.util.Collection;

import com.idega.block.entity.business.EntityToPresentationObjectConverter;
import com.idega.block.entity.data.EntityPath;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWColor;

public class PledgeWizard extends RunBlock {
	
	private static final String PARAMETER_ACTION = "prm_action";
	//private static final String PARAMETER_FROM_ACTION = "prm_from_action";
	private static final String PARAMETER_USER_ID = "prm_user_id";
	private static final String PARAMETER_PERSONAL_ID = "prm_personal_id";
	private static final String PARAMETER_FIRST_NAME = "prm_first_name";
	private static final String PARAMETER_MIDDLE_NAME = "prm_middle_name";
	private static final String PARAMETER_LAST_NAME = "prm_last_name";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_CHARITY = "prm_charity";
	private static final String PARAMETER_SEARCH = "prm_search";
	
	private static int NUMBER_OF_ROWS = 10;

	private static final int ACTION_STEP_ONE = 1;
	private static final int ACTION_STEP_TWO = 2;
	private static final int ACTION_STEP_THREE = 3;
	private static final int ACTION_STEP_FOUR = 4;
	private static final int ACTION_SAVE = 5;
	private static final int ACTION_CANCEL = 6;
	
	
	public void main(IWContext iwc) throws Exception {

		switch (parseAction(iwc)) {
			case ACTION_STEP_ONE:
				stepOne(iwc);
				break;
			case ACTION_STEP_TWO:
				stepTwo(iwc);
				break;
			case ACTION_STEP_THREE:
				stepThree(iwc);
				break;
			case ACTION_STEP_FOUR:
				stepFour(iwc);
				break;
			case ACTION_SAVE:
				save(iwc, true);
				break;
			case ACTION_CANCEL:
				cancel(iwc);
				break;
		}
	}
	
	private int parseAction(IWContext iwc) throws RemoteException {
		int action;
		
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		} else {
			action = ACTION_STEP_ONE;
		}
		return action;
	}
	
	private void stepOne(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		//form.addParameter(PARAMETER_ACTION, ACTION_STEP_TWO);
		
		form.add(getPhasesTable(1, 4, "run_reg.make_pledge", "Make a pledge"));
		form.add(localize("run_reg.pledge_information_text_step_1", "Information text 1..."));
		
		TextInput input = new TextInput(PARAMETER_PERSONAL_ID);
		//input.setAsIcelandicSSNumber(localize("run_reg.not_valid_personal_id", "The personal ID you've entered is not valid"));
		input.setLength(10);
		input.setMaxlength(10);
		//input.setInFocusOnPageLoad(true);
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		Label label = new Label(localize("run_reg.personal_id", "Personal ID") + ":", input);
		layer.add(label);
		layer.add(input);
		form.add(layer);
		form.add(new Break());
		
		input = new TextInput(PARAMETER_FIRST_NAME);
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_reg.first_name", "First name") + ":", input);
		layer.add(label);
		layer.add(input);
		form.add(layer);
		form.add(new Break());
		
		input = new TextInput(PARAMETER_MIDDLE_NAME);
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_reg.middle_name", "Middle name") + ":", input);
		layer.add(label);
		layer.add(input);
		form.add(layer);
		form.add(new Break());
		
		input = new TextInput(PARAMETER_LAST_NAME);
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_reg.last_name", "Last name") + ":", input);
		layer.add(label);
		layer.add(input);
		form.add(layer);
		form.add(new Break());
		
		DropdownMenu charityDropDown = new CharitiesForRunDropDownMenu(PARAMETER_CHARITY);
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_reg.charity", "Charity") + ":", charityDropDown);
		layer.add(label);
		layer.add(charityDropDown);
		form.add(layer);
		form.add(new Break());

		if (iwc.isParameterSet(PARAMETER_SEARCH)) {
			
			String first = iwc.getParameter(PARAMETER_FIRST_NAME);
			String middle = iwc.getParameter(PARAMETER_MIDDLE_NAME);
			String last = iwc.getParameter(PARAMETER_LAST_NAME);
			String pid = iwc.getParameter(PARAMETER_PERSONAL_ID);
			try {
				UserHome userHome = (UserHome) IDOLookup.getHome(User.class);
				String[] group_id = {"346530"};
				//String group_name = "Glitnir starfsmenn ";
				Collection usersFound = userHome.findUsersByConditions(first, middle, last, pid, null, null, -1, -1, -1, -1, group_id, null, true, false);
				EntityBrowser browser = getBrowser(usersFound, iwc);
		        form.add(browser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		SubmitButton search = new SubmitButton(PARAMETER_SEARCH, localize("search", "Search"));
		form.add(search);
		
		SubmitButton next = new SubmitButton(localize("next", "Next"), PARAMETER_ACTION, String.valueOf(ACTION_STEP_TWO));
		form.add(next);
		add(form);
	}
	
	private void stepTwo(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		//form.addParameter(PARAMETER_ACTION, ACTION_STEP_TWO);
		
		form.add(getPhasesTable(2, 4, "run_reg.select_run", "Select run"));
		form.add(localize("run_reg.pledge_information_text_step_2", "Information text 2..."));
		
		User user = null;
		try {
			user = getUserBusiness(iwc).getUser(Integer.parseInt(iwc.getParameter(PARAMETER_USER_ID)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TextInput input = new TextInput(PARAMETER_PERSONAL_ID);
		//input.setAsIcelandicSSNumber(localize("run_reg.not_valid_personal_id", "The personal ID you've entered is not valid"));
		input.setLength(10);
		input.setMaxlength(10);
		//input.setInFocusOnPageLoad(true);
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		Label label = new Label(localize("run_reg.personal_id", "Personal ID") + ":", input);
		layer.add(label);
		layer.add(input);
		form.add(layer);
		form.add(new Break());
		input.setValue(user.getPersonalID());
		
		input = new TextInput(PARAMETER_NAME);
		//input.setAsIcelandicSSNumber(localize("run_reg.not_valid_personal_id", "The personal ID you've entered is not valid"));
		input.setLength(10);
		input.setMaxlength(10);
		//input.setInFocusOnPageLoad(true);
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_reg.name", "Name") + ":", input);
		layer.add(label);
		layer.add(input);
		form.add(layer);
		form.add(new Break());
		input.setValue(user.getName());

		SubmitButton next = new SubmitButton(localize("next", "Next"), PARAMETER_ACTION, String.valueOf(ACTION_STEP_TWO));
		form.add(next);
		add(form);
	}

	private void stepThree(IWContext iwc) {
	}

	private void stepFour(IWContext iwc) {
	}
	
	private void save(IWContext iwc, boolean doPayment) throws RemoteException {
		
	}
	
	private void cancel(IWContext iwc) {
		//iwc.removeSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
	}
	
	private EntityBrowser getBrowser(Collection entities, IWContext iwc)  {
	    // define checkbox button converter class
	    EntityToPresentationObjectConverter converterToChooseButton = new EntityToPresentationObjectConverter() {

	      public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
	        return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);  
	      } 

	      public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
	        User user = (User) entity;
	        RadioButton radioButton = new RadioButton(PARAMETER_USER_ID, user.getPrimaryKey().toString());
	        return radioButton;
	      }
	    };
	    // set default columns
	    String nameKey = User.class.getName()+".FIRST_NAME:" + User.class.getName()+".MIDDLE_NAME:"+User.class.getName()+".LAST_NAME";
	    String pinKey = User.class.getName()+".PERSONAL_ID";
	    //String charityKey = Charity.class.getName()+".NAME";
	    EntityBrowser browser = EntityBrowser.getInstanceUsingExternalForm();
	    browser.setAcceptUserSettingsShowUserSettingsButton(false, false);
	    browser.setDefaultNumberOfRows(NUMBER_OF_ROWS);
	    browser.setEntities("pledge_wizard", entities);
	    browser.setWidth(Table.HUNDRED_PERCENT);
	    //fonts
	    Text column = new Text();
	    column.setBold();
	    browser.setColumnTextProxy(column);
	    //   set color of rows
	    browser.setColorForEvenRows(IWColor.getHexColorString(246, 246, 247));
	    browser.setColorForOddRows("#FFFFFF");
	      
	    browser.setDefaultColumn(1, nameKey);
	    browser.setDefaultColumn(2, pinKey);
	    //browser.setDefaultColumn(3, charityKey);
	    browser.setMandatoryColumn(1, "Choose");
	    // set special converters
	    browser.setEntityToPresentationConverter("Choose", converterToChooseButton);
	    return browser;
	}
}