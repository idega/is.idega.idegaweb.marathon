package is.idega.idegaweb.marathon.presentation.user.runoverview;

import is.idega.idegaweb.marathon.IWBundleStarter;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlMessage;
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
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2007/12/27 16:03:47 $ by $Author: civilis $
 *
 */
public class UIPaymentStep extends IWBaseComponent implements WizardStep {
	
	public static final String COMPONENT_TYPE = "idega_PaymentStep";
	
	private static final long serialVersionUID = 983517329599024600L;
	static final String stepIdentifier = "PaymentStep";
	private Wizard wizard;

	private static final String valueAtt = "value";
	private static final String divTag = "div";
	private static final String spanTag = "span";
	private static final String containerFacet = "container";
	
	private static final String assetsCartStyleClass = UIDistanceChangeWizard.distanceChangeWizard_cssPrefix+"assetsCart";
	private static final String creditCardInformationStyleClass = UIDistanceChangeWizard.distanceChangeWizard_cssPrefix+"creditCardInformation";
	private static final String headerStyleClass = "header";
	private static final String contentsStyleClass = "contents";
	private static final String entryStyleClass = "entry";
	private static final String ccvNumberStyleClass = "ccvNumber";
	private static final String errorStyleClass = "error";
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
		
		contentsDiv.getChildren().add(createEntry(context, iwrb.getLocalizedString("run_reg.card_holder", "Card holder"), HtmlInputText.COMPONENT_TYPE, null, UIDistanceChangeWizard.distanceChangeWizardBean_cardHolderNameExp, UIDistanceChangeWizard.distanceChangeStepBean_validateCardholderNameExp));
		contentsDiv.getChildren().add(createEntry(context, iwrb.getLocalizedString("run_reg.card_holder_email", "Cardholder email"), HtmlInputText.COMPONENT_TYPE, null, UIDistanceChangeWizard.distanceChangeWizardBean_cardHolderEmailExp, null));
		
//		ccn
		HtmlTag entry = createEntry(context, iwrb.getLocalizedString("run_reg.card_number", "Card number"), UICreditCardNumber.COMPONENT_TYPE, null, UIDistanceChangeWizard.distanceChangeWizardBean_creditCardNumberExp, null);
		entry.setStyleClass(ccnStyleClass);
		contentsDiv.getChildren().add(entry);
		
//		ccv
		HtmlInputText ccvNumber = (HtmlInputText)application.createComponent(HtmlInputText.COMPONENT_TYPE);
		ccvNumber.setId(context.getViewRoot().createUniqueId());
		ccvNumber.setSize(3);
		ccvNumber.setMaxlength(3);
		ccvNumber.setStyleClass(ccvNumberStyleClass);
		
		contentsDiv.getChildren().add(createEntry(context, iwrb.getLocalizedString("run_reg.ccv_number", "CCV number"), UICreditCardNumber.COMPONENT_TYPE, ccvNumber, UIDistanceChangeWizard.distanceChangeWizardBean_creditCardNumberExp, null));

//		card expires
		UIDateInput dateInput = (UIDateInput)application.createComponent(UIDateInput.COMPONENT_TYPE);
		dateInput.setId(context.getViewRoot().createUniqueId());
		dateInput.setRendered(true);
		dateInput.setYearRange(IWTimestamp.RightNow().getYear(), IWTimestamp.RightNow().getYear()+10);
		
		contentsDiv.getChildren().add(createEntry(context, iwrb.getLocalizedString("run_reg.card_expires", "Card expires"), UIDateInput.COMPONENT_TYPE, dateInput, UIDistanceChangeWizard.distanceChangeWizardBean_cardExpirationDateExp, null));
		
		return ccidiv;
	}
	
	private HtmlTag createEntry(FacesContext context, String labelStr, String inputComponentType, UIComponent uiinput, String valueBindingExp, String validatorMethodExp) {
		
		Application application = context.getApplication();
		
		HtmlTag entryDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		entryDiv.setId(context.getViewRoot().createUniqueId());
		entryDiv.setStyleClass(entryStyleClass);
		entryDiv.setValue(divTag);
		
//		label
		HtmlOutputLabel label = (HtmlOutputLabel)application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		label.setId(context.getViewRoot().createUniqueId());
		label.setValue(labelStr);
		entryDiv.getChildren().add(label);

//		input
		HtmlTag span = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		span.setId(context.getViewRoot().createUniqueId());
		span.setValue(spanTag);
		span.setStyleClass("subentry");
		entryDiv.getChildren().add(span);
		
		if(uiinput == null) {
			
			uiinput = application.createComponent(inputComponentType);
			uiinput.setId(context.getViewRoot().createUniqueId());
		}
		
		uiinput.setValueBinding(valueAtt, application.createValueBinding(valueBindingExp));
		span.getChildren().add(uiinput);
		label.setFor(uiinput.getId());
		
		if(validatorMethodExp != null && (uiinput instanceof UIInput)) {
			
			((UIInput)uiinput).setValidator(application.createMethodBinding(validatorMethodExp, new Class[] {FacesContext.class, UIComponent.class, Object.class}));
			
			HtmlTag errSpan = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
			errSpan.setId(context.getViewRoot().createUniqueId());
			errSpan.setStyleClass(errorStyleClass);
			errSpan.setValue(spanTag);
			span.getChildren().add(errSpan);
			
			HtmlMessage errMsg = (HtmlMessage)application.createComponent(HtmlMessage.COMPONENT_TYPE);
			errMsg.setId(context.getViewRoot().createUniqueId());
			errMsg.setFor(uiinput.getId());
			errSpan.getChildren().add(errMsg);
		}
		
		return entryDiv;
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