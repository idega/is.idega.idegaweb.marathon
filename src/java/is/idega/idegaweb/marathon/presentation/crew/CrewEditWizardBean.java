package is.idega.idegaweb.marathon.presentation.crew;

import is.idega.idegaweb.marathon.IWBundleStarter;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Participant;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.6 $
 *
 * Last modified: $Date: 2008/01/12 17:15:16 $ by $Author: civilis $
 *
 */
public class CrewEditWizardBean {

	private Integer mode;
	public static final Integer newCrewMode = new Integer(1);
	public static final Integer editCrewMode = new Integer(2);
	private Integer crewEditId;
	private List crewMembersInvitationSearchResults;
	private CrewParticipant crewParticipant;
	private RunBusiness runBusiness;

	public Integer getCrewEditId() {
		return crewEditId;
	}

	public void setCrewEditId(Integer crewEditId) {
		this.crewEditId = crewEditId;
	}
	
	public Integer getMode() {
		return mode == null ? new Integer(0) : mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}
	
	public boolean isNewCrewMode() {
		
		return newCrewMode.equals(getMode());
	}
	
	public boolean isEditCrewMode() {
		
		return editCrewMode.equals(getMode());
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

	public String getParticipantId() {
		
		if(getCrewParticipant() == null)
			return null;
		
		return String.valueOf(getCrewParticipant().getParticipantId());
	}

	public void setParticipantId(String participantId) {

		if(participantId == null || CoreConstants.EMPTY.equals(participantId))
			return;
		
		try {
			Participant participant = getRunBusiness().getParticipantByPrimaryKey(Integer.parseInt(participantId));
			setCrewParticipant(new CrewParticipant(participant));
			
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception while retrieving participant by participantId provided: "+participantId, e);
		}
	}
	
	public String getRunLabel() {

		IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
		Participant participant = getCrewParticipant().getParticipant();
		
		return iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc)	
		.getLocalizedString(participant.getRunTypeGroup().getName(), participant.getRunTypeGroup().getName()) + " " + participant.getRunYearGroup().getName();
	}

	public List getCrewMembersInvitationSearchResults() {
		return crewMembersInvitationSearchResults;
	}

	public void setCrewMembersInvitationSearchResults(
			List crewMembersInvitationSearchResults) {
		this.crewMembersInvitationSearchResults = crewMembersInvitationSearchResults;
	}

	public CrewParticipant getCrewParticipant() {
		return crewParticipant;
	}

	public void setCrewParticipant(CrewParticipant crewParticipant) {
		crewMembersInvitationSearchResults = null;
		this.crewParticipant = crewParticipant;
	}
}