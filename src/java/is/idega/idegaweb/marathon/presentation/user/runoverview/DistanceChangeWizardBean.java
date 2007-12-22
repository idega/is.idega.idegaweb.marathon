package is.idega.idegaweb.marathon.presentation.user.runoverview;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Participant;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.faces.context.FacesContext;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.contact.data.Email;
import com.idega.presentation.IWContext;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2007/12/22 13:54:19 $ by $Author: civilis $
 *
 */
public class DistanceChangeWizardBean {
	
	private String participantId;
	private Participant participant;
	private RunBusiness runBusiness;
	private String newDistanceId;
	private String cardHolderName;
	private String cardHolderEmail;
	
	public String getNewDistanceId() {
		
		if(newDistanceId == null)
			newDistanceId = getParticipant().getRunDistanceGroup().getPrimaryKey().toString();
		
		return newDistanceId;
	}

	public void setNewDistanceId(String newDistanceId) {
		
		if(newDistanceId != null)
			this.newDistanceId = newDistanceId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		
		participant = null;
		newDistanceId = null;
		cardHolderName = null;
		this.participantId = participantId;
	}

	public Participant getParticipant() {

		if(participant == null) {
			
			try {
				participant = getRunBusiness().getParticipantByPrimaryKey(new Integer(getParticipantId()).intValue());
				
			} catch (RemoteException e) {
				throw new RuntimeException(e);
			}
		}
		
		return participant;
	}
	
	public RunBusiness getRunBusiness() {
		
		if(runBusiness == null) {
			
			try {
				runBusiness = (RunBusiness) IBOLookup.getServiceInstance(IWContext.getIWContext(FacesContext.getCurrentInstance()), RunBusiness.class);
			} catch (IBOLookupException e) {
				throw new RuntimeException(e);
			}
		}
		
		return runBusiness;
	}

	public String getCardHolderName() {
		
		if(cardHolderName == null)
			cardHolderName = getParticipant().getUser().getName();
		
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		
		if(cardHolderName != null)
			this.cardHolderName = cardHolderName;
	}
	
	public String getCardHolderEmail() {
		
		if(cardHolderEmail == null) {
			
			Collection emails = getParticipant().getUser().getEmails();
			
			if(emails != null && !emails.isEmpty())
				cardHolderEmail = ((Email)emails.iterator().next()).getEmailAddress();
		}
		
		return cardHolderEmail;
	}

	public void setCardHolderEmail(String cardHolderEmail) {
		
		if(cardHolderEmail != null)
			this.cardHolderEmail = cardHolderEmail;
	}
}