package is.idega.idegaweb.marathon.presentation.crew;

import is.idega.idegaweb.marathon.IWBundleStarter;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Participant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2008/01/11 19:30:02 $ by $Author: civilis $
 *
 */
public class CrewsOverviewListBean {
	
	private RunBusiness runBusiness;
	
	public DataModel getCrewsOverviewList() {
		
		List crews = new ArrayList();
		
		try {
			
			IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
			User ownerUser = iwc.getCurrentUser();
			
			RunBusiness runBusiness = getRunBusiness();
			Collection participants = runBusiness.getParticipantsByUser(ownerUser);
			
			if(participants != null) {
				
				for (Iterator iterator = participants.iterator(); iterator.hasNext();) {
					Participant participant = (Participant)iterator.next();
					
					String crewLabel = participant.getRunGroupName();
					
//					doesn't belong to a crew in this participation
					if(crewLabel == null && participant.getCrewInvitedParticipantId() == null)
						continue;
					
					if(crewLabel == null) {
						
						Participant p = runBusiness.getParticipantByPrimaryKey(participant.getCrewInvitedParticipantId().intValue());
						
						if(p == null || p.getRunGroupName() == null) {
							Logger.getLogger(getClass().getName()).log(Level.WARNING, "Owner participant not found for crewInvitedParticipantId set: "+participant.getCrewInvitedParticipantId()+" or crew name was not set for the owner participant");
							continue;
						}
						
						crewLabel = p.getRunGroupName();
					}
					
					String runLabel = 
						iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc)	
						.getLocalizedString(participant.getRunTypeGroup().getName(), participant.getRunTypeGroup().getName()) + " " + participant.getRunYearGroup().getName();
					
					String distanceLabel = 
						iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc)
						.getLocalizedString(participant.getRunDistanceGroup().getName(), participant.getRunDistanceGroup().getName());
					
					Map crew = new HashMap(7);
					crew.put(UICrewsOverviewList.crew_label, crewLabel);
					crew.put(UICrewsOverviewList.crew_runLabel, runLabel);
					crew.put(UICrewsOverviewList.crew_distance, distanceLabel);
					crew.put(UICrewsOverviewList.crew_pidOnclick, new StringBuffer("document.getElementById('").append(UICrewsOverviewList.crewsOverviewListBean_participantIdParam).append("').value='").append(participant.getPrimaryKey().toString()).append("';"));
					crew.put(UICrewsOverviewList.crew_renderedEdit, new Boolean(participant.isCrewOwner()));
					crew.put(UICrewsOverviewList.crew_renderedAcceptInvitation, new Boolean(participant.getCrewInvitedParticipantId() != null));
					crew.put(UICrewsOverviewList.crew_renderedRejectInvitation, new Boolean(participant.getCrewInvitedParticipantId() != null));
					
					crews.add(crew);
				}
			}
			
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception while resolving crews overview list", e);
		}
	
		return new ListDataModel(crews);
	}
	
	protected RunBusiness getRunBusiness() {
		
		if(runBusiness == null) {
			
			try {
				runBusiness = (RunBusiness) IBOLookup.getServiceInstance(IWContext.getIWContext(FacesContext.getCurrentInstance()), RunBusiness.class);
			} catch (IBOLookupException e) {
				throw new RuntimeException(e);
			}
		}
		
		return runBusiness;
	}
	
	public boolean isForceIdHack() {
		
		return true;
	}
}