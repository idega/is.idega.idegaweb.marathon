package is.idega.idegaweb.marathon.presentation.user.runoverview;

import is.idega.idegaweb.marathon.IWBundleStarter;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.htmlTag.HtmlTag;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.UICreditCardNumber;
import com.idega.presentation.ui.UIDateInput;
import com.idega.presentation.wizard.Wizard;
import com.idega.presentation.wizard.WizardStep;
import com.idega.util.IWTimestamp;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/12/27 12:52:26 $ by $Author: civilis $
 *
 */
public class UIPaymentStep extends IWBaseComponent implements WizardStep {
	
	public static final String COMPONENT_TYPE = "idega_PaymentStep";
	
	private static final long serialVersionUID = 983517329599024600L;
	static final String stepIdentifier = "PaymentStep";
	private Wizard wizard;

	private static final String valueAtt = "value";
	private static final String divTag = "div";
	private static final String containerFacet = "container";
	
	private static final String assetsCartStyleClass = UIDistanceChangeWizard.distanceChangeWizard_cssPrefix+"assetsCart";
	private static final String creditCardInformationStyleClass = UIDistanceChangeWizard.distanceChangeWizard_cssPrefix+"creditCardInformation";
	private static final String headerStyleClass = "header";
	private static final String contentsStyleClass = "contents";
	private static final String entryStyleClass = "entry";
	private static final String ccvNumberStyleClass = "ccvNumber";
	private static final String credCardNrStyleClass = "credCardNr";
	private static final String ccnStyleClass = entryStyleClass+" "+credCardNrStyleClass;
	
	
	
	public String getIdentifier() {
		
		return stepIdentifier;
	}

	public UIComponent getStepComponent(FacesContext context, Wizard wizard) {
		
		UIPaymentStep step = (UIPaymentStep)context.getApplication().createComponent(COMPONENT_TYPE);
		step.setId(context.getViewRoot().createUniqueId());
		step.setWizard(wizard);
		return step;
	}
	
	/**
	 * @Override
	 */
	protected void initializeComponent(FacesContext context) {
		
		
		Application application = context.getApplication();
		IWContext iwc = IWContext.getIWContext(context);
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		
		HtmlTag container = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		container.setId(context.getViewRoot().createUniqueId());
		container.setValue(divTag);
		
		HtmlInputHidden hidden = (HtmlInputHidden)application.createComponent(HtmlInputHidden.COMPONENT_TYPE);
		hidden.setId(context.getViewRoot().createUniqueId());
		hidden.setValueBinding(valueAtt, application.createValueBinding(UIDistanceChangeWizard.distanceChangeStepBean_wizardModeExp));
		container.getChildren().add(hidden);
		
		container.getChildren().add(getAssetsCartArea(context, iwrb));
		container.getChildren().add(getCreditCardInformationArea(context, iwrb));
		
		HtmlCommandButton prevButton = wizard.getPreviousButton(context, this);
		prevButton.setId(context.getViewRoot().createUniqueId());
		container.getChildren().add(prevButton);
		
		HtmlCommandButton justButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		justButton.setId(context.getViewRoot().createUniqueId());
		container.getChildren().add(justButton);
		
		getFacets().put(containerFacet, container);
	}
	
	private UIComponent getAssetsCartArea(FacesContext context, IWResourceBundle iwrb) {
		
		Application application = context.getApplication();
		HtmlTag abdiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		abdiv.setId(context.getViewRoot().createUniqueId());
		abdiv.setStyleClass(assetsCartStyleClass);
		abdiv.setValue(divTag);
		
//		header
		HtmlTag div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue(divTag);
		div.setStyleClass(headerStyleClass);
		abdiv.getChildren().add(div);
		
		HtmlOutputText text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValue(iwrb.getLocalizedString("dist_ch.payingFor", "You need to pay for:"));
		div.getChildren().add(text);
		
//		cart area
		HtmlTag container = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		container.setId(context.getViewRoot().createUniqueId());
		container.setStyleClass(contentsStyleClass);
		container.setValue(divTag);
		abdiv.getChildren().add(container);
		
//		Change distance to entry
		HtmlTag entryDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		entryDiv.setId(context.getViewRoot().createUniqueId());
		entryDiv.setStyleClass(entryStyleClass);
		entryDiv.setValue(divTag);
		container.getChildren().add(entryDiv);
		
//		Change distance to entry label
		text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValue(iwrb.getLocalizedString("dist_ch.changeTo", "Change distance to:"));
		entryDiv.getChildren().add(text);
		
//		Change distance to entry value
		div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue(divTag);
		entryDiv.getChildren().add(div);
		
		text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValue("some selected distance");
		div.getChildren().add(text);
		
//		price entry
		entryDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		entryDiv.setId(context.getViewRoot().createUniqueId());
		entryDiv.setStyleClass(entryStyleClass);
		entryDiv.setValue(divTag);
		container.getChildren().add(entryDiv);
		
//		price entry label
		text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValue(iwrb.getLocalizedString("run_reg.price", "Price"));
		entryDiv.getChildren().add(text);
		
//		price entry value
		div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue(divTag);
		entryDiv.getChildren().add(div);
		
		text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValue("2343 EUR");
		div.getChildren().add(text);
		
		return abdiv;
	}
	
	private UIComponent getCreditCardInformationArea(FacesContext context, IWResourceBundle iwrb) {
		
		Application application = context.getApplication();
		HtmlTag ccidiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		ccidiv.setId(context.getViewRoot().createUniqueId());
		ccidiv.setStyleClass(creditCardInformationStyleClass);
		ccidiv.setValue(divTag);
		
		HtmlTag div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue(divTag);
		div.setStyleClass(headerStyleClass);
		ccidiv.getChildren().add(div);
		
		HtmlOutputText text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValue(iwrb.getLocalizedString("run_reg.credit_card_information", "Credit card information"));
		div.getChildren().add(text);
		
//		Collection images = getRunBusiness(iwc).getCreditCardImages();
//		if (images != null) {
//			Iterator iterator = images.iterator();
//			while (iterator.hasNext()) {
//				Image image = (Image) iterator.next();
//				if (image != null) {
//					String imageURL = image.getURL();
//					if (imageURL != null && !imageURL.equals("")) {
//						image.setToolTip(imageURL.substring(imageURL.lastIndexOf('/')+1,imageURL.lastIndexOf('.')));
//					}
//				}
//				creditCardTable.add(image, 3, creditRow);
//				if (iterator.hasNext()) {
//					creditCardTable.add(Text.getNonBrakingSpace(), 3, creditRow);
//				}
//			}
//		}
		
		HtmlTag contentsDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		contentsDiv.setId(context.getViewRoot().createUniqueId());
		contentsDiv.setValue(divTag);
		contentsDiv.setStyleClass(contentsStyleClass);
		ccidiv.getChildren().add(contentsDiv);
		

//		card holder name
		HtmlTag entryDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		entryDiv.setId(context.getViewRoot().createUniqueId());
		entryDiv.setStyleClass(entryStyleClass);
		entryDiv.setValue(divTag);
		contentsDiv.getChildren().add(entryDiv);
		
//		card holder name label
		HtmlOutputLabel label = (HtmlOutputLabel)application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		label.setId(context.getViewRoot().createUniqueId());
		label.setValue(iwrb.getLocalizedString("run_reg.card_holder", "Card holder"));
		entryDiv.getChildren().add(label);

//		card holder name input
		HtmlInputText cardHolderName = (HtmlInputText)application.createComponent(HtmlInputText.COMPONENT_TYPE);
		cardHolderName.setId(context.getViewRoot().createUniqueId());
		cardHolderName.setValueBinding(valueAtt, application.createValueBinding(UIDistanceChangeWizard.distanceChangeWizardBean_cardHolderNameExp));
		entryDiv.getChildren().add(cardHolderName);
		label.setFor(cardHolderName.getId());

//		card holder email
		entryDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		entryDiv.setId(context.getViewRoot().createUniqueId());
		entryDiv.setStyleClass(entryStyleClass);
		entryDiv.setValue(divTag);
		contentsDiv.getChildren().add(entryDiv);
		
//		card holder email label
		label = (HtmlOutputLabel)application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		label.setId(context.getViewRoot().createUniqueId());
		label.setValue(iwrb.getLocalizedString("run_reg.card_holder_email", "Cardholder email"));
		entryDiv.getChildren().add(label);

//		card holder email input
		HtmlInputText cardHolderEmail = (HtmlInputText)application.createComponent(HtmlInputText.COMPONENT_TYPE);
		cardHolderEmail.setId(context.getViewRoot().createUniqueId());
		cardHolderEmail.setValueBinding(valueAtt, application.createValueBinding(UIDistanceChangeWizard.distanceChangeWizardBean_cardHolderEmailExp));
		entryDiv.getChildren().add(cardHolderEmail);
		label.setFor(cardHolderName.getId());

//		credit card number
		entryDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		entryDiv.setId(context.getViewRoot().createUniqueId());
		entryDiv.setStyleClass(ccnStyleClass);
		entryDiv.setValue(divTag);
		contentsDiv.getChildren().add(entryDiv);
		
//		credit card number label
		label = (HtmlOutputLabel)application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		label.setId(context.getViewRoot().createUniqueId());
		label.setValue(iwrb.getLocalizedString("run_reg.card_number", "Card number"));
		entryDiv.getChildren().add(label);

//		credit card number input
		UICreditCardNumber creditCardNumber = (UICreditCardNumber)application.createComponent(UICreditCardNumber.COMPONENT_TYPE);
		creditCardNumber.setId(context.getViewRoot().createUniqueId());
		creditCardNumber.setValueBinding(valueAtt, application.createValueBinding(UIDistanceChangeWizard.distanceChangeWizardBean_creditCardNumberExp));
		label.setFor(creditCardNumber.getId());
		entryDiv.getChildren().add(creditCardNumber);

//		ccv number
		entryDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		entryDiv.setId(context.getViewRoot().createUniqueId());
		entryDiv.setStyleClass(entryStyleClass);
		entryDiv.setValue(divTag);
		contentsDiv.getChildren().add(entryDiv);
		
//		ccv number label
		label = (HtmlOutputLabel)application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		label.setId(context.getViewRoot().createUniqueId());
		label.setValue(iwrb.getLocalizedString("run_reg.ccv_number", "CCV number"));
		entryDiv.getChildren().add(label);

//		ccv number input
		HtmlInputText ccvNumber = (HtmlInputText)application.createComponent(HtmlInputText.COMPONENT_TYPE);
		ccvNumber.setId(context.getViewRoot().createUniqueId());
		ccvNumber.setSize(3);
		ccvNumber.setMaxlength(3);
		ccvNumber.setStyleClass(ccvNumberStyleClass);
		ccvNumber.setValueBinding(valueAtt, application.createValueBinding(UIDistanceChangeWizard.distanceChangeWizardBean_ccvNumberExp));
		entryDiv.getChildren().add(ccvNumber);
		label.setFor(ccvNumber.getId());

//		card expires
		entryDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		entryDiv.setId(context.getViewRoot().createUniqueId());
		entryDiv.setStyleClass(entryStyleClass);
		entryDiv.setValue(divTag);
		contentsDiv.getChildren().add(entryDiv);
		
//		card expires label
		label = (HtmlOutputLabel)application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		label.setId(context.getViewRoot().createUniqueId());
		label.setValue(iwrb.getLocalizedString("run_reg.card_expires", "Card expires"));
		entryDiv.getChildren().add(label);

//		card expires input
		UIDateInput dateInput = (UIDateInput)application.createComponent(UIDateInput.COMPONENT_TYPE);
		dateInput.setId(context.getViewRoot().createUniqueId());
		dateInput.setRendered(true);
		dateInput.setValueBinding(valueAtt, application.createValueBinding(UIDistanceChangeWizard.distanceChangeWizardBean_cardExpirationDateExp));
		dateInput.setYearRange(IWTimestamp.RightNow().getYear(), IWTimestamp.RightNow().getYear()+10);
		entryDiv.getChildren().add(dateInput);
		label.setFor(dateInput.getId());
		
		return ccidiv;
	}
	
	public void setWizard(Wizard wizard) {
		this.wizard = wizard;
	}

	/**
	 * @Override
	 */
	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
		
		UIComponent container = getFacet(containerFacet);
		
		if(container != null) {
			
			container.setRendered(true);
			renderChild(context, container);
		}
	}
	
	/**
	 * @Override
	 */
	public boolean getRendersChildren() {
		return true;
	}
}