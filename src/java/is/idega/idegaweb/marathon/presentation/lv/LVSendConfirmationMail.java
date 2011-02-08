package is.idega.idegaweb.marathon.presentation.lv;

import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.presentation.RunBlock;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

import com.idega.core.contact.data.Email;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.NoEmailFoundException;

public class LVSendConfirmationMail extends RunBlock {
	public static final String ACTION_SEND_EMAIL = "send_email";

	public void main(IWContext iwc) throws Exception {
		if (!iwc.isInEditMode()) {
			if (iwc.isParameterSet(ACTION_SEND_EMAIL)) {
				Collection participants = getRunBusiness(iwc)
						.getConfirmedParticipants();
				if (participants != null && !participants.isEmpty()) {
					Iterator it = participants.iterator();
					System.out.println("count = " + participants.size());
					while (it.hasNext()) {
						Participant participant = (Participant) it.next();

						Email email = null;
						try {
							email = getUserBusiness(iwc).getUsersMainEmail(
									participant.getUser());
						} catch (NoEmailFoundException nefe) {
						} catch (RemoteException e) {
						}

						if (email != null) {
							Object[] args = {
									participant.getUser().getName(),
									participant.getPrimaryKey().toString(),
									email.getEmailAddress()
									 };
							String subject = null;
							String body = null;
							//if (participant.getc)
							subject = iwrb.getLocalizedString("lv_conf.registration_email_subject",
									"Your registration has been received.");
							body = MessageFormat.format(iwrb.getLocalizedString(
									"lv_conf.registration_email_body",
									"Your registration has been received."), args);
							getRunBusiness(iwc).sendMessage(email.getEmailAddress(), subject,
									body);
							//getRunBusiness(iwc).sendMessage("palli@illuminati.is", subject,
							//		body);

						} else {
							System.out
									.println("Couldn't find email for participant "
											+ participant.getPrimaryKey()
													.toString());
						}
					}
				}

			}

			Form form = new Form();
			SubmitButton button = new SubmitButton(ACTION_SEND_EMAIL,
					"Send emails");
			form.add(button);
			this.add(form);
		}
	}
}
