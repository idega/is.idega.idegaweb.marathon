package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.core.messaging.MessagingSettings;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

public class SendPledgeLink extends RunBlock {
	
	private final static int ACTION_VIEW_FORM = 1;
	private final static int ACTION_FORM_SUBMIT = 2;

	private final static String PARAMETER_FORM_SUBMIT = "cp_sbmt";

	private final static String PARAMETER_RUN_YEAR = "param_run_year";
	private final static String PARAMETER_TO_ADDRESSES = "param_to_addresses";

	private final static String KEY_PREFIX = "sendPledgeLink.";
	private final static String KEY_RUN = KEY_PREFIX + "run";
	private final static String KEY_PLEDGE = KEY_PREFIX + "to_addresses";
	private final static String KEY_MUST_SELECT_RUN = KEY_PREFIX + "must_select_run";
	private final static String KEY_UPDATE = KEY_PREFIX + "update";

	private final static String KEY_PLEDGELINK_SENT = KEY_PREFIX + "pledge_link_sent";
	private final static String KEY_PLEDGELINK_SENT_TEXT = KEY_PREFIX + "pledge_link_sent_text";

	private final static String DEFAULT_RUN = "Run";	
	private final static String DEFAULT_PLEDGE = "Pledge";	
	private final static String DEFAULT_MUST_SELECT_RUN = "You must select run";
	private final static String DEFAULT_UPDATE = "Update";
	

	private final static String DEFAULT_PLEDGELINK_SENT = "Pledgelink sent";
	private final static String DEFAULT_PLEDGELINK_SENT_TEXT = "Your pledgelink has been sent.";

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
				sendPledgeLink(iwc);
				break;
		}
	}

	private void drawForm(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_FORM_SUBMIT, Boolean.TRUE.toString());
		form.setID("sendPledgeLinkForm");
		form.setStyleClass("sendPledgeLinkForm");
		
		Layer header = new Layer(Layer.DIV);
		header.setStyleClass("header");
		form.add(header);
		
		Heading1 heading = new Heading1(this.iwrb.getLocalizedString(KEY_PREFIX + "send_pledgelink", "Send pledgelink"));
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
		if (runsYears != null && !runsYears.isEmpty()) {
			
			DropdownMenu runDropdown = new DropdownMenu(PARAMETER_RUN_YEAR);
			runDropdown.addMenuElement("-1", iwrb.getLocalizedString("run_year_ddd.select_run", "Select run..."));
			runDropdown.keepStatusOnAction(true);
			
			paragraph.add(new Text(this.iwrb.getLocalizedString(KEY_PREFIX + "sent_pledgelink_helper_text", "Please enter the emails of the recipients for the pledgelink.")));
			
			Iterator runIt = runsYears.iterator();
			Group year = null;
			while (runIt.hasNext()) {
				year = (Group)runIt.next();
				Group run = (Group)year.getParentNode();
				runDropdown.addMenuElement(year.getPrimaryKey().toString(), localize(run.getName(),run.getName()) + " " + year.getName());
			}
			
			TextInput toAddresses = new TextInput(PARAMETER_TO_ADDRESSES);	
			toAddresses.keepStatusOnAction(true);
			
			if (runsYears.size() == 1) {
				runDropdown.setSelectedElement(year.getPrimaryKey().toString());
				runDropdown.setDisabled(true);
				HiddenInput hiddenRunYear = new HiddenInput(PARAMETER_RUN_YEAR, year.getPrimaryKey().toString());
				hiddenRunYear.keepStatusOnAction(true);
				form.add(hiddenRunYear);
			}			

			Layer formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			Label label = new Label(this.iwrb.getLocalizedString(KEY_RUN, DEFAULT_RUN), runDropdown);
			formItem.add(label);
			formItem.add(runDropdown);
			section.add(formItem);
	
			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label(this.iwrb.getLocalizedString(KEY_PLEDGE, DEFAULT_PLEDGE), toAddresses);
			formItem.add(label);
			formItem.add(toAddresses);
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
	
	private void sendPledgeLink(IWContext iwc) {
		
		String runYear = iwc.getParameter(PARAMETER_RUN_YEAR);
		String toAddresses = iwc.getParameter(PARAMETER_TO_ADDRESSES);

		boolean hasErrors = false;
		Collection errors = new ArrayList();
		
		if (runYear != null && runYear.equals("-1")) {
			hasErrors = true;
			errors.add(this.iwrb.getLocalizedString(KEY_MUST_SELECT_RUN, DEFAULT_MUST_SELECT_RUN));
		}

		if (!hasErrors) {
			try {
				MessagingSettings messagingSetting = getIWApplicationContext().getIWMainApplication().getMessagingSettings();
				Participant participant = getRunBusiness(iwc).getRunObjByUserIDandYearID(iwc.getCurrentUserId(), Integer.parseInt(runYear));
				Object[] args = { String.valueOf(participant.getUser().getName()), localize(participant.getRunTypeGroup().getName(), participant.getRunTypeGroup().getName()) };
				String emailBodyString =  MessageFormat.format(localize(KEY_PREFIX + "email_body_string","Dear recipient\r\n{0} has decided to send you this pledgelink.\r\nTo select the runner you can press the following link:\r\nhttp://www.marathon.is/pages/aheit/?prm_participant_id=" + participant.getPrimaryKey() + "&prm_action=2\r\n\r\nBest regards,\r\n{1}"), args);
				com.idega.util.SendMail.send(messagingSetting.getFromMailAddress(),toAddresses,"","",messagingSetting.getSMTPMailServer(),localize(KEY_PREFIX + "pledgelink", "Pledgelink"),emailBodyString);
			}
			catch (Exception e) {
				e.printStackTrace();
				hasErrors = true;
				errors.add(this.iwrb.getLocalizedString(KEY_PREFIX + "sending_pledgelink_failed", "Sending pledgelink failed"));
			}
		}

		if (!hasErrors) {
			Form form = new Form();
			form.setID("sendPledgeLinkForm");
			form.setStyleClass("sendPledgeLinkForm");

			Layer header = new Layer(Layer.DIV);
			header.setStyleClass("header");
			form.add(header);
			
			Heading1 heading = new Heading1(this.iwrb.getLocalizedString(KEY_PREFIX + "send_pledgelink", "Send pledgelink"));
			header.add(heading);
			
			Layer layer = new Layer(Layer.DIV);
			layer.setStyleClass("receipt");
			
			Layer image = new Layer(Layer.DIV);
			image.setStyleClass("receiptImage");
			layer.add(image);
			
			heading = new Heading1(this.iwrb.getLocalizedString(KEY_PLEDGELINK_SENT, DEFAULT_PLEDGELINK_SENT));
			layer.add(heading);
			
			Paragraph paragraph = new Paragraph();
			paragraph.add(new Text(this.iwrb.getLocalizedString(KEY_PLEDGELINK_SENT_TEXT, DEFAULT_PLEDGELINK_SENT_TEXT)));
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
