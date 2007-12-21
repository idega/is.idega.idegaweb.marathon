package is.idega.idegaweb.marathon.presentation.user.runoverview;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Participant;

import java.rmi.RemoteException;
import javax.faces.context.FacesContext;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/12/21 17:19:19 $ by $Author: civilis $
 *
 */
public class DistanceChangeWizardBean {
	
	private String participantId;
	private Participant participant;
	private RunBusiness runBusiness;
	private String newDistanceId;
	
	public String getNewDistanceId() {
		
		if(newDistanceId == null)
			newDistanceId = getParticipant().getRunDistanceGroup().getPrimaryKey().toString();
		
		return newDistanceId;
	}

	public void setNewDistanceId(String newDistanceId) {
		this.newDistanceId = newDistanceId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		
		participant = null;
		newDistanceId = null;
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
}