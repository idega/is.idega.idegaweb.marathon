package is.idega.idegaweb.marathon.presentation.crew;

import is.idega.idegaweb.marathon.IWBundleStarter;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Participant;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
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
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/01/10 10:44:01 $ by $Author: civilis $
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
					if(crewLabel == null)
						continue;
					
					String runLabel = 
						iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc)	
						.getLocalizedString(participant.getRunTypeGroup().getName(), participant.getRunTypeGroup().getName()) + " " + participant.getRunYearGroup().getName();
					
					String distanceLabel = 
						iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc)
						.getLocalizedString(participant.getRunDistanceGroup().getName(), participant.getRunDistanceGroup().getName());
					
					Map crew = new HashMap(4);
					crew.put(UICrewsOverviewList.crew_label, crewLabel);
					crew.put(UICrewsOverviewList.crew_runLabel, runLabel);
					crew.put(UICrewsOverviewList.crew_distance, distanceLabel);
					crew.put(UICrewsOverviewList.crew_pidOnclick, new StringBuffer("document.getElementById('").append(UICrewsOverviewList.crewsOverviewListBean_participantId).append("').value='").append(participant.getPrimaryKey().toString()).append("';"));
					crews.add(crew);
				}
			}
			
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		};
	
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