package is.idega.idegaweb.marathon.presentation.user.runoverview;

import is.idega.idegaweb.marathon.IWBundleStarter;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.DistanceHome;
import is.idega.idegaweb.marathon.data.Participant;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;
import javax.faces.context.FacesContext;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.contact.data.Email;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.CreditCardNumber;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.8 $
 *
 * Last modified: $Date: 2007/12/30 15:27:52 $ by $Author: civilis $
 *
 */
public class DistanceChangeWizardBean {
	
	private String participantId;
	private Participant participant;
	private RunBusiness runBusiness;
	private String newDistanceId;
	private String newDistanceName;
	private String cardHolderName;
	private String cardHolderEmail;
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
		cardHolderEmail = null;
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
		
//		TODO: how to get it?
		String referenceNumber = "123";
		
		try {
			if(false) {
			
				String properties = getRunBusiness().authorizePayment(getCardHolderName(), getCreditCardNumber().getFullNumber(CoreConstants.EMPTY), String.valueOf(expirationDate.getMonth()), String.valueOf(expirationDate.getYear()), getCcvNumber(), getDistanceChangePrice().getPrice().floatValue(), getDistanceChangePrice().getCurrencyLabel(), referenceNumber);

				
				getRunBusiness().finishPayment(properties);
			}
			
			String newDistanceId = getNewDistanceId();
			Distance distance = getDistanceByGroupId(newDistanceId);
			Participant participant = getParticipant();
			participant.setRunDistanceGroup(distance);
			participant.store();
			setParticipantId(participant.getPrimaryKey().toString());
			
		} catch(CreditCardAuthorizationException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ":Distance change. Exception while paying for distance change:", e);
//			TODO: add msg about error
			
		} catch (Exception e) {

			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ":Distance change. Exception while changing distance:", e);
//			TODO: add msg about error
		}
	}
}