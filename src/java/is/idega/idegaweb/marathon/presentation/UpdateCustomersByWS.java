package is.idega.idegaweb.marathon.presentation;

import java.util.Collection;
import java.util.Iterator;

import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.ParticipantHome;
import is.idega.idegaweb.marathon.glitnirws.MarathonWS2Client;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;

public class UpdateCustomersByWS extends RunBlock {

	private final static int ACTION_VIEW_FORM = 1;
	private final static int ACTION_FORM_SUBMIT = 2;
	
	private final static String PARAMETER_FORM_SUBMIT = "cp_sbmt";
	
	private final static String KEY_PREFIX = "updateCustomers.";
	
	private final static String KEY_UPDATE = KEY_PREFIX + "update";
	
	private final static String DEFAULT_UPDATE = "Update";

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

		int action = parseAction(iwc);
		switch (action) {
			case ACTION_VIEW_FORM:
				drawForm(iwc);
				break;
			case ACTION_FORM_SUBMIT:
				updateCustomers(iwc);
				break;
		}
	}

	private void drawForm(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_FORM_SUBMIT, Boolean.TRUE.toString());
		form.setID("updateCustomersForm");
		form.setStyleClass("updateCustomersForm");
		
		Layer header = new Layer(Layer.DIV);
		header.setStyleClass("header");
		form.add(header);
		
		Heading1 heading = new Heading1(this.iwrb.getLocalizedString(KEY_PREFIX + "update_customers", "Update customers"));
		header.add(heading);
		
		Layer contents = new Layer(Layer.DIV);
		contents.setStyleClass("formContents");
		form.add(contents);
		
		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		contents.add(section);
		
		Paragraph paragraph = new Paragraph();
		paragraph.add(new Text(this.iwrb.getLocalizedString(KEY_PREFIX + "update_customers_helper_text", "Please select the desired run and year.")));
		section.add(paragraph);
		
		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonLayer");
		contents.add(buttonLayer);
		
		Layer span = new Layer(Layer.SPAN);
		span.add(new Text(this.iwrb.getLocalizedString(KEY_UPDATE, DEFAULT_UPDATE)));
		Link send = new Link(span);
		send.setStyleClass("sendLink");
		send.setToFormSubmit(form);
		buttonLayer.add(send);
	
		add(form);
	}

	private void updateCustomers(IWContext iwc) {
        try {
        	MarathonWS2Client wsClient = new MarathonWS2Client(getIWApplicationContext().getIWMainApplication());
        	ParticipantHome partHome = (ParticipantHome) IDOLookup.getHome(Participant.class);
			Collection participants = partHome.findAllByRunGroupIdAndYear(4,2007);
			if (participants != null && !participants.isEmpty()) {
				Iterator partIt = participants.iterator();
				while (partIt.hasNext()) {
					Participant participant = (Participant)partIt.next();
					if (participant != null && participant.getUser() != null && participant.getUser().getPersonalID() != null && getUserBusiness(iwc).hasValidIcelandicSSN(participant.getUser())) {
						if(wsClient.erIVidskiptumVidGlitni(participant.getUser().getPersonalID())){
							participant.setCustomer(true);
						}
						else{
							participant.setCustomer(false);
						}
						participant.store();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}