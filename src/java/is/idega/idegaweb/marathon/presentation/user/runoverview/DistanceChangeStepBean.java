package is.idega.idegaweb.marathon.presentation.user.runoverview;

import is.idega.idegaweb.marathon.IWBundleStarter;
import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.data.Participant;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.data.Group;
import com.idega.util.CoreConstants;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.8 $
 *
 * Last modified: $Date: 2007/12/31 11:18:53 $ by $Author: civilis $
 *
 */
public class DistanceChangeStepBean {

	private DistanceChangeWizardBean wizardBean;
	private boolean wizardMode;
	
	/**
	 * List<SelectItem>
	 */
	private List runDistances;

	public String getChosenDistanceName() {
		
		IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
		
		return iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc)
		.getLocalizedString(getWizardBean().getParticipant().getRunDistanceGroup().getName(), getWizardBean().getParticipant().getRunDistanceGroup().getName());
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
			selectItem.setLabel(iwrb.getLocalizedString("run_year_ddd.select_distance", "Select distance..."));
			runDistances.add(selectItem);
			
			try {
				Collection distancesGroups = getWizardBean().getRunBusiness().getDistancesMap(getWizardBean().getParticipant().getRunTypeGroup(), getWizardBean().getParticipant().getRunDistanceGroup().getYear().getYearString());
				List distances = new ArrayList(distancesGroups.size());
				ConverterUtility converterUtility = ConverterUtility.getInstance();
				
				for (Iterator distancesGroupsIterator = distancesGroups.iterator(); distancesGroupsIterator.hasNext();)
					distances.add(converterUtility.convertGroupToDistance((Group) distancesGroupsIterator.next()));

				List disallowedDistances = getWizardBean().getRunBusiness().getDisallowedDistancesPKs(getWizardBean().getParticipant().getUser(), distances);
				
				for (Iterator iterator = distancesGroups.iterator(); iterator.hasNext();) {
					
					Group distanceGroup = (Group) iterator.next();
					
					selectItem = new SelectItem();
					
					if(disallowedDistances.contains(distanceGroup.getPrimaryKey().toString())) {
						
						selectItem.setValue("-1");
						selectItem.setLabel(new StringBuffer(iwrb.getLocalizedString(distanceGroup.getName(), distanceGroup.getName()))
		    			.append(CoreConstants.SPACE)
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
		
		if(runDistances != null)
			this.runDistances = runDistances;
	}
	
	public DistanceChangeWizardBean getWizardBean() {
		return wizardBean;
	}

	public void setWizardBean(DistanceChangeWizardBean wizardBean) {
		this.wizardBean = wizardBean;
	}
	
	public void validateDistanceChange(FacesContext context, UIComponent toValidate, Object value) {

		FacesMessage message = null;
		
		try {
			IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
			
			String valueStr = (String)value;
			
			if(getWizardBean().getParticipant().getRunDistanceGroup().getPrimaryKey().toString().equals(valueStr)) {
				
				message = new FacesMessage("dist_ch.err.distancesEquals", "You have chosen distance you're already registered for");
				
			} else if("-1".equals(valueStr)) {
				message = new FacesMessage(iwrb.getLocalizedString("dist_ch.err.chooseAllowedDistance", "Please choose allowed distance"));
				
			} else {
			
				Collection distancesGroups = getWizardBean().getRunBusiness().getDistancesMap(getWizardBean().getParticipant().getRunTypeGroup(), getWizardBean().getParticipant().getRunDistanceGroup().getYear().getYearString());
				List distances = new ArrayList(distancesGroups.size());
				ConverterUtility converterUtility = ConverterUtility.getInstance();
				
				for (Iterator distancesGroupsIterator = distancesGroups.iterator(); distancesGroupsIterator.hasNext();)
					distances.add(converterUtility.convertGroupToDistance((Group) distancesGroupsIterator.next()));
				
				List disallowedDistances = getWizardBean().getRunBusiness().getDisallowedDistancesPKs(getWizardBean().getParticipant().getUser(), distances);
				
				if(disallowedDistances.contains(valueStr))
					message = new FacesMessage(iwrb.getLocalizedString("dist_ch.err.distanceNotAllowed", "Chosen distance is not allowed for you"));
			}
			
		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Exception while validating distance change select input choice", e);
			message = new FacesMessage("dist_ch.err.errorValidatingDistanceChoice", "Sorry, error occured while validating your distance choice. Please try again.");
		}
		
		if(message != null) {
			((UIInput)toValidate).setValid(false);
			context.addMessage(toValidate.getClientId(context), message);
		}
	}
	
	public void validateCCVNumber(FacesContext context, UIComponent toValidate, Object value) {
		
		if(!((String)value).matches("[0-9]{3}")) {
			((UIInput)toValidate).setValid(false);
			FacesMessage message = new FacesMessage("dist_ch.err.ccvIncorrect", "CCV number should be a digit number");
			context.addMessage(toValidate.getClientId(context), message);
		}
	}
	
	public void validateCardExpiresDate(FacesContext context, UIComponent toValidate, Object value) {

	}
}