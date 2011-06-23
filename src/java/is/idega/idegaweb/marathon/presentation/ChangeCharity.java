package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ContestantRequest;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ContestantServiceLocator;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.IContestantService;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.Login;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.UpdateContestantRequest;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.data.IDOException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public class ChangeCharity extends RunBlock {

	private final static int ACTION_VIEW_FORM = 1;
	private final static int ACTION_FORM_SUBMIT = 2;

	private final static String PARAMETER_FORM_SUBMIT = "cp_sbmt";

	private final static String PARAMETER_RUN_YEAR = "param_run_year";
	private final static String PARAMETER_CHARITY = "param_charity";

	private final static String KEY_PREFIX = "changeCharity.";
	private final static String KEY_RUN = KEY_PREFIX + "run";
	private final static String KEY_CHARITY = KEY_PREFIX + "charity";
	private final static String KEY_MUST_SELECT_RUN = KEY_PREFIX + "must_select_run";
	private final static String KEY_UPDATE = KEY_PREFIX + "update";

	private final static String KEY_CHARITY_SAVED = KEY_PREFIX + "charity_saved";
	private final static String KEY_CHARITY_SAVED_TEXT = KEY_PREFIX + "charity_saved_text";

	private final static String DEFAULT_RUN = "Run";	
	private final static String DEFAULT_CHARITY = "Charity";	
	private final static String DEFAULT_MUST_SELECT_RUN = "You must select run";
	private final static String DEFAULT_UPDATE = "Update";
	

	private final static String DEFAULT_CHARITY_SAVED = "Charity organization saved";
	private final static String DEFAULT_CHARITY_SAVED_TEXT = "Your choice for charity organization has been saved.";

	private User user = null;
		
	protected int parseAction (final IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_FORM_SUBMIT)) {
			return ACTION_FORM_SUBMIT;
		}
		else {
			return ACTION_VIEW_FORM;
		}
	}
	
	public void main(IWContext iwc) {
		if (!iwc.isLoggedOn()) {
			return;
		}
		this.iwrb = getResourceBundle(iwc);
		this.user = iwc.getCurrentUser();

		int action = parseAction(iwc);
		switch (action) {
			case ACTION_VIEW_FORM:
				drawForm(iwc);
				break;
			case ACTION_FORM_SUBMIT:
				updateCharity(iwc);
				break;
		}
	}

	private void drawForm(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_FORM_SUBMIT, Boolean.TRUE.toString());
		form.setID("changeCharityForm");
		form.setStyleClass("changeCharityForm");
		
		Layer header = new Layer(Layer.DIV);
		header.setStyleClass("header");
		form.add(header);
		
		Heading1 heading = new Heading1(this.iwrb.getLocalizedString(KEY_PREFIX + "change_charity", "Change charity"));
		header.add(heading);
		
		Layer contents = new Layer(Layer.DIV);
		contents.setStyleClass("formContents");
		form.add(contents);
		
		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		contents.add(section);
		
		Paragraph paragraph = new Paragraph();
		section.add(paragraph);
		
		Collection runsYears = null;
		try {
			runsYears = getRunBusiness(iwc).getRunGroupOfTypeForUser(iwc.getCurrentUser(), IWMarathonConstants.GROUP_TYPE_RUN_YEAR);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Collection runsYearThatCanBeChanged = new ArrayList();
		if (runsYears != null && !runsYears.isEmpty()) {
			Iterator it = runsYears.iterator();
			Group yearGroup = null;
			IWTimestamp now = new IWTimestamp();
			while (it.hasNext()) {
				yearGroup = (Group) it.next();
				
				try {
					Year year = ConverterUtility.getInstance().convertGroupToYear(yearGroup);

					if (year.isCharityEnabled()) {
						IWTimestamp lastChangeDate = new IWTimestamp(year.getRunDate());
						lastChangeDate.addDays(3);
						if (now.isEarlierThan(lastChangeDate)) {
							Participant participant = getRunBusiness(iwc).getParticipantByRunAndYear(iwc.getCurrentUser(), (Group) yearGroup.getParentNode(), yearGroup, false);
							int count = getPledgeBusiness(iwc).getPledgeHome().getNumberOfPledgesByParticipants(participant);
							if (count < 1) {
								runsYearThatCanBeChanged.add(yearGroup);
							}
						}
					}
				} catch (FinderException e) {
				} catch (RemoteException e) {
				} catch (IDOException e) {
				}
			}
		}
		
		if (!runsYearThatCanBeChanged.isEmpty()) {
			
			DropdownMenu runDropdown = new DropdownMenu(PARAMETER_RUN_YEAR);
			runDropdown.addMenuElement("-1", iwrb.getLocalizedString("run_year_ddd.select_run", "Select run..."));
			runDropdown.keepStatusOnAction(true);
			CharitiesForRunDropDownMenu charityDropdown = null;
			
			paragraph.add(new Text(this.iwrb.getLocalizedString(KEY_PREFIX + "change_charity_helper_text", "Please select the desired charity.")));
			
			Iterator runIt = runsYearThatCanBeChanged.iterator();
			Group year = null;
			while (runIt.hasNext()) {
				year = (Group)runIt.next();
				Group run = (Group)year.getParentNode();
				runDropdown.addMenuElement(year.getPrimaryKey().toString(), localize(run.getName(),run.getName()) + " " + year.getName());
			}
			
			charityDropdown = new CharitiesForRunDropDownMenu(PARAMETER_CHARITY);	
			charityDropdown.keepStatusOnAction(true);
			
			if (runsYearThatCanBeChanged.size() == 1) {
				runDropdown.setSelectedElement(year.getPrimaryKey().toString());
				runDropdown.setDisabled(true);
				HiddenInput hiddenRunYear = new HiddenInput(PARAMETER_RUN_YEAR, year.getPrimaryKey().toString());
				hiddenRunYear.keepStatusOnAction(true);
				form.add(hiddenRunYear);
				charityDropdown.setRunYearID((Integer) year.getPrimaryKey());
				Participant participant = null;
				try {
					participant = getRunBusiness(iwc).getRunObjByUserIDandYearID(iwc.getCurrentUserId(), ((Integer)year.getPrimaryKey()).intValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (participant != null && participant.getCharityId() != null) {
					charityDropdown.setSelectedElement(participant.getCharityId());
				}
			} else {
				charityDropdown.setRunYearID(new Integer(-1));
				RemoteScriptHandler rsh = new RemoteScriptHandler(runDropdown, charityDropdown);
				try {
					rsh.setRemoteScriptCollectionClass(YearMenuCharityMenuInputCollectionHandler.class);
				}
				catch (InstantiationException e) {
					e.printStackTrace();
				}
				catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				add(rsh);
			}			

			Layer formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			Label label = new Label(this.iwrb.getLocalizedString(KEY_RUN, DEFAULT_RUN), runDropdown);
			formItem.add(label);
			formItem.add(runDropdown);
			section.add(formItem);
	
			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label(this.iwrb.getLocalizedString(KEY_CHARITY, DEFAULT_CHARITY), charityDropdown);
			formItem.add(label);
			formItem.add(charityDropdown);
			section.add(formItem);
			
			Layer clearLayer = new Layer(Layer.DIV);
			clearLayer.setStyleClass("Clear");
			section.add(clearLayer);
	
			Layer buttonLayer = new Layer(Layer.DIV);
			buttonLayer.setStyleClass("buttonLayer");
			contents.add(buttonLayer);
			
			Layer span = new Layer(Layer.SPAN);
			span.add(new Text(this.iwrb.getLocalizedString(KEY_UPDATE, DEFAULT_UPDATE)));
			Link send = new Link(span);
			send.setStyleClass("sendLink");
			send.setToFormSubmit(form);
			buttonLayer.add(send);
		} else {
			paragraph.add(new Text(this.iwrb.getLocalizedString(KEY_PREFIX + "no_runs_found_for_user", "No active runs were found for the user")));
		}
		add(form);
	}
	
	private void updateCharity(IWContext iwc) {
		
		String runYear = iwc.getParameter(PARAMETER_RUN_YEAR);
		String charity = iwc.getParameter(PARAMETER_CHARITY);

		boolean hasErrors = false;
		Collection errors = new ArrayList();
		
		if (runYear != null && runYear.equals("-1")) {
			hasErrors = true;
			errors.add(this.iwrb.getLocalizedString(KEY_MUST_SELECT_RUN, DEFAULT_MUST_SELECT_RUN));
		}

		if (!hasErrors) {
			try {
				Participant participant = getRunBusiness(iwc).getRunObjByUserIDandYearID(iwc.getCurrentUserId(), Integer.parseInt(runYear));
				participant.setCharityId(charity);
				participant.store();
				
				if (participant.getCharityId() != null && !"".equals(participant.getCharityId())) {
					try {
						String passwd = getIWApplicationContext()
							.getIWMainApplication().getSettings().getProperty("hlaupastyrkur_passwd", "");
						String userID = getIWApplicationContext()
							.getIWMainApplication().getSettings().getProperty("hlaupastyrkur_userid", "");
						ContestantServiceLocator locator = new ContestantServiceLocator();
						IContestantService port = locator
								.getBasicHttpBinding_IContestantService(new URL(
										"http://www.hlaupastyrkur.is/services/contestantservice.svc"));
						
						UpdateContestantRequest request = new UpdateContestantRequest(participant.getRunDistanceGroup().getName(), new Login(passwd, userID), participant.getCharityId(), null, null, participant.getUser().getPersonalID(), Boolean.TRUE);
						port.updateContestant(request);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				
				getUserBusiness(iwc).callAllUserGroupPluginAfterUserCreateOrUpdateMethod(this.user);
			}
			catch (Exception e) {
				hasErrors = true;
				errors.add(this.iwrb.getLocalizedString(KEY_PREFIX + "charity_update_failed", "Charity update failed"));
			}
		}
			// Ok to update charity
		if (!hasErrors) {
			Form form = new Form();
			form.setID("changeCharityForm");
			form.setStyleClass("changeCharityForm");

			Layer header = new Layer(Layer.DIV);
			header.setStyleClass("header");
			form.add(header);
			
			Heading1 heading = new Heading1(this.iwrb.getLocalizedString(KEY_PREFIX + "change_charity", "Change charity"));
			header.add(heading);
			
			Layer layer = new Layer(Layer.DIV);
			layer.setStyleClass("receipt");
			
			Layer image = new Layer(Layer.DIV);
			image.setStyleClass("receiptImage");
			layer.add(image);
			
			heading = new Heading1(this.iwrb.getLocalizedString(KEY_CHARITY_SAVED, DEFAULT_CHARITY_SAVED));
			layer.add(heading);
			
			Paragraph paragraph = new Paragraph();
			paragraph.add(new Text(this.iwrb.getLocalizedString(KEY_CHARITY_SAVED_TEXT, DEFAULT_CHARITY_SAVED_TEXT)));
			layer.add(paragraph);
			
			ICPage userHomePage = null;
			try {
				UserBusiness ub = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
				userHomePage = ub.getHomePageForUser(this.user);
			}
			catch (FinderException fe) {
				//No page found...
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
			
			if (userHomePage != null) {
				Layer span = new Layer(Layer.SPAN);
				span.add(new Text(this.iwrb.getLocalizedString("my_page", "My page")));
				Link link = new Link(span);
				link.setStyleClass("homeLink");
				link.setPage(userHomePage);
				paragraph.add(new Break(2));
				paragraph.add(link);
			}
			
			form.add(layer);
			add(form);
		}
		else {
			showErrors(iwc, errors);
			drawForm(iwc);
		}
	}
}
