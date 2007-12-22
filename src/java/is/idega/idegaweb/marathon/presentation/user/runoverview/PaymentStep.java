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
import com.idega.presentation.wizard.Wizard;
import com.idega.presentation.wizard.WizardStep;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/12/22 13:54:19 $ by $Author: civilis $
 *
 */
public class PaymentStep extends IWBaseComponent implements WizardStep {
	
	private static final long serialVersionUID = 983517329599024600L;
	static final String stepIdentifier = "PaymentStep";
	private Wizard wizard;

	private static final String valueAtt = "value";
	private static final String divTag = "div";
	private static final String containerFacet = "container";
	
	public String getIdentifier() {
		
		return stepIdentifier;
	}

	public UIComponent getStepComponent(FacesContext context, Wizard wizard) {
		
		PaymentStep step = new PaymentStep();
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
		hidden.setValueBinding(valueAtt, application.createValueBinding(DistanceChangeWizard.distanceChangeStepBean_wizardModeExp));
		container.getChildren().add(hidden);
		
		container.getChildren().add(getCreditCardInformationArea(context, iwrb));
		
		HtmlCommandButton prevButton = wizard.getPreviousButton(context, this);
		container.getChildren().add(prevButton);
		
		HtmlCommandButton justButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		container.getChildren().add(justButton);
		
		getFacets().put(containerFacet, container);
	}
	
	private UIComponent getCreditCardInformationArea(FacesContext context, IWResourceBundle iwrb) {
		
		Application application = context.getApplication();
		HtmlTag ccidiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		ccidiv.setId(context.getViewRoot().createUniqueId());
		ccidiv.setStyleClass("creditCardInformation");
		ccidiv.setValue(divTag);
		
		HtmlTag div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue(divTag);
		div.setStyleClass("header");
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
		
//		card holder name
		HtmlOutputLabel label = (HtmlOutputLabel)application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		label.setValue(iwrb.getLocalizedString("run_reg.card_holder", "Card holder"));
		ccidiv.getChildren().add(label);
		
		HtmlInputText cardHolderName = (HtmlInputText)application.createComponent(HtmlInputText.COMPONENT_TYPE);
		cardHolderName.setId(context.getViewRoot().createUniqueId());
		cardHolderName.setValueBinding(valueAtt, application.createValueBinding(DistanceChangeWizard.distanceChangeWizardBean_cardHolderNameExp));
		ccidiv.getChildren().add(cardHolderName);
		
//		card holder email
		label = (HtmlOutputLabel)application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		label.setValue(iwrb.getLocalizedString("run_reg.card_holder", "Card holder"));
		ccidiv.getChildren().add(label);
		
		HtmlInputText cardHolderEmail = (HtmlInputText)application.createComponent(HtmlInputText.COMPONENT_TYPE);
		cardHolderEmail.setId(context.getViewRoot().createUniqueId());
		cardHolderEmail.setValueBinding(valueAtt, application.createValueBinding(DistanceChangeWizard.distanceChangeWizardBean_cardHolderEmailExp));
		ccidiv.getChildren().add(cardHolderEmail);
		
		
		
		
		
		label.setFor(cardHolderName.getId());
		
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