package is.idega.idegaweb.marathon.presentation.user.runoverview;

import is.idega.idegaweb.marathon.IWBundleStarter;
import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.data.Participant;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.data.Group;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/12/21 15:05:02 $ by $Author: civilis $
 *
 */
public class DistanceChangeStepBean {

	private DistanceChangeWizardBean wizardBean;
	
	private boolean wizardMode;
	private String newDistanceId;
	
	/**
	 * List<SelectItem>
	 */
	private List runDistances;

	public String getNewDistanceId() {
		
		if(newDistanceId == null)
			newDistanceId = getWizardBean().getParticipant().getRunDistanceGroup().getPrimaryKey().toString();
		
		return newDistanceId;
	}

	public void setNewDistanceId(String newDistanceId) {
		this.newDistanceId = newDistanceId;
	}

	public String getRunLabel() {

		IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
		Participant participant = getWizardBean().getParticipant();
		
		return iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc)	
		.getLocalizedString(participant.getRunTypeGroup().getName(), participant.getRunTypeGroup().getName()) + " " + participant.getRunYearGroup().getName();
	}

	public boolean isWizardMode() {
		return wizardMode;
	}

	public void setWizardMode(boolean wizardMode) {
		this.wizardMode = wizardMode;
	}

	public List getRunDistances() {
		
		if(runDistances == null) {
			
			IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
			runDistances = new ArrayList();
			
			SelectItem selectItem = new SelectItem();
			
			selectItem.setValue("-1");
			selectItem.setLabel(iwrb.getLocalizedString("run_year_ddd.select_distance","Select distance..."));
			runDistances.add(selectItem);
			
			try {
				Collection distancesGroups = getWizardBean().getRunBiz().getDistancesMap(getWizardBean().getParticipant().getRunTypeGroup(), getWizardBean().getParticipant().getRunDistanceGroup().getYear().getYearString());
				List distances = new ArrayList(distancesGroups.size());
				ConverterUtility converterUtility = ConverterUtility.getInstance();
				
				for (Iterator distancesGroupsIterator = distancesGroups.iterator(); distancesGroupsIterator.hasNext();)
					distances.add(converterUtility.convertGroupToDistance((Group) distancesGroupsIterator.next()));

				List disallowedDistances = getWizardBean().getRunBiz().getDisallowedDistancesPKs(getWizardBean().getParticipant().getUser(), distances);
				
				for (Iterator iterator = distancesGroups.iterator(); iterator.hasNext();) {
					
					Group distanceGroup = (Group) iterator.next();
					
					selectItem = new SelectItem();
					
					if(disallowedDistances.contains(distanceGroup.getPrimaryKey().toString())) {
						
						selectItem.setValue("-1");
						selectItem.setLabel(new StringBuffer(iwrb.getLocalizedString(distanceGroup.getName(), distanceGroup.getName()))
		    			.append(" ")
		    			.append(iwrb.getLocalizedString("runDistance.choiceNotAvailableBecauseOfAge", "(Not available for your age)"))
		    			.toString());
						
					} else {
						
						selectItem.setValue(distanceGroup.getPrimaryKey().toString());
						selectItem.setLabel(iwrb.getLocalizedString(distanceGroup.getName(), distanceGroup.getName()));
					}
					
					runDistances.add(selectItem);
				}
			} catch (RemoteException e) {
				throw new RuntimeException(e);
			} catch (FinderException e) {
				throw new RuntimeException(e);
			}
		}
		
		return runDistances;
	}

	public void setRunDistances(List runDistances) {
		this.runDistances = runDistances;
	}
	
	public DistanceChangeWizardBean getWizardBean() {
		return wizardBean;
	}

	public void setWizardBean(DistanceChangeWizardBean wizardBean) {
		this.wizardBean = wizardBean;
	}
}