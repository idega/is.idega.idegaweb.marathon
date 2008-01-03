package is.idega.idegaweb.marathon.presentation.user.runoverview;

import is.idega.idegaweb.marathon.IWBundleStarter;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.DistanceHome;
import is.idega.idegaweb.marathon.data.Participant;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.CreditCardNumber;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.11 $
 *
 * Last modified: $Date: 2008/01/03 20:30:35 $ by $Author: civilis $
 *
 */
public class DistanceChangeWizardBean {
	
	private String participantId;
	private Participant participant;
	private RunBusiness runBusiness;
	private String newDistanceId;
	private String newDistanceName;
	private String cardHolderName;
	private CreditCardNumber creditCardNumber;
	private String ccvNumber;
	private Date cardExpirationDate;
	private Price distanceChangePrice;
	
	private static final String PROPERTY_DISTANCE_PRICE_ISK = "distance_change_price_ISK";
	private static final String PROPERTY_DISTANCE_PRICE_EUR = "distance_change_price_EUR";
	private static final String ISK_CURRENCY_LABEL = "ISK";
	private static final String EUR_CURRENCY_LABEL = "EUR";
	
	public String getNewDistanceId() {
		
		if(newDistanceId == null)
			newDistanceId = getParticipant().getRunDistanceGroup().getPrimaryKey().toString();
		
		return newDistanceId;
	}

	public void setNewDistanceId(String newDistanceId) {
		
		if(newDistanceId != null) {
			newDistanceName = null;
			this.newDistanceId = newDistanceId;
		}
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		participant = null;
		newDistanceId = null;
		newDistanceName = null;
		cardHolderName = null;
		creditCardNumber = null;
		ccvNumber = null;
		cardExpirationDate = null;
		distanceChangePrice = null;
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
	
	public CreditCardNumber getCreditCardNumber() {
		
		return creditCardNumber;
	}

	public void setCreditCardNumber(CreditCardNumber creditCardNumber) {
		
		if(creditCardNumber != null)
			this.creditCardNumber = creditCardNumber;
	}

	public String getCcvNumber() {
		return ccvNumber;
	}

	public void setCcvNumber(String ccvNumber) {
		
		if(ccvNumber != null)
			this.ccvNumber = ccvNumber;
	}

	public Date getCardExpirationDate() {
		
		return cardExpirationDate;
	}

	public void setCardExpirationDate(Date cardExpirationDate) {
		
		if(cardExpirationDate != null)
			this.cardExpirationDate = cardExpirationDate;
	}

	public String getNewDistanceName() {
		
		if(newDistanceName == null && getNewDistanceId() != null) {

			try {
				Distance newDistance = getDistanceByGroupId(getNewDistanceId());
				
				IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
				
				newDistanceName = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc)
				.getLocalizedString(newDistance.getName(), newDistance.getName());
				
			} catch (FinderException e) {
				Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Exception while retrieving distance group by group id", e);
			}
		}
		
		return newDistanceName;
	}

	public void setNewDistanceName(String newDistanceName) {
		this.newDistanceName = newDistanceName;
	}
	
	public Distance getDistanceByGroupId(String groupId) throws FinderException {
	
		try {
			DistanceHome home = (DistanceHome) IDOLookup.getHome(Distance.class);
			return (Distance) home.findByPrimaryKey(groupId);
		}
		catch (IDOLookupException ile) {
			throw new FinderException(ile.getMessage());
		}
	}
	
	public Price getDistanceChangePrice() {
		
		if(distanceChangePrice == null) {
			
			IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
			boolean isIcelandic = iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale());
			
			IWBundle bundle = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER);
			
			if (isIcelandic) {
				float price = Float.parseFloat(bundle.getProperty(PROPERTY_DISTANCE_PRICE_ISK, "0.01"));
				distanceChangePrice = new Price(new Float(price), ISK_CURRENCY_LABEL, iwc.getCurrentLocale());
				
			} else {
				
				float price = Float.parseFloat(bundle.getProperty(PROPERTY_DISTANCE_PRICE_EUR, "0.01"));
				distanceChangePrice = new Price(new Float(price), EUR_CURRENCY_LABEL, iwc.getCurrentLocale());
			}
		}
		
		return distanceChangePrice;
	}
	
	public void setDistanceChangePrice(Price distanceChangePrice) {
		
		//fixed price
	}
	
	public void submitDistanceChange() {
		
		IWTimestamp expirationDate = new IWTimestamp(getCardExpirationDate());
		
		String referenceNumber = getParticipant().getUser().getPersonalID().replaceAll("-", CoreConstants.EMPTY);
		
		try {
			String properties = getRunBusiness().authorizePayment(getCardHolderName(), getCreditCardNumber().getFullNumber(CoreConstants.EMPTY), String.valueOf(expirationDate.getMonth()), String.valueOf(expirationDate.getYear()), getCcvNumber(), getDistanceChangePrice().getPrice().floatValue(), getDistanceChangePrice().getCurrencyLabel(), referenceNumber);
			
			String newDistanceId = getNewDistanceId();
			Distance distance = getDistanceByGroupId(newDistanceId);
			Participant participant = getParticipant();
			participant.setRunDistanceGroup(distance);
			participant.store();
			setParticipantId(participant.getPrimaryKey().toString());
			
			getRunBusiness().finishPayment(properties);
			
		} catch(CreditCardAuthorizationException e) {
			
			FacesContext context = FacesContext.getCurrentInstance();
			IWContext iwc = IWContext.getIWContext(context);
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
			
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ":Distance change. Exception while paying for distance change: Exception message: "+e.getLocalizedMessage(iwrb), e);
			
			FacesMessage message = new FacesMessage(iwrb.getLocalizedString("dist_ch.err.authorizationException", "Internal error occurred while authorizing credit card. Please try again."));
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			
		} catch (Exception e) {

			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ":Distance change. Exception while changing distance:", e);
			
			FacesContext context = FacesContext.getCurrentInstance();
			IWContext iwc = IWContext.getIWContext(context);
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
			FacesMessage message = new FacesMessage(iwrb.getLocalizedString("dist_ch.err.distanceChangeException", "Internal error occurred while changing distance. Please try again."));
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
		}
	}
}