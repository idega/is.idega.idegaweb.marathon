package is.idega.idegaweb.marathon.presentation.crew;

import java.io.IOException;

import is.idega.idegaweb.marathon.IWBundleStarter;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlMessage;
import javax.faces.component.html.HtmlMessages;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectOneMenu;
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
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/01/08 19:20:24 $ by $Author: civilis $
 *
 */
public class UICrewManageStep extends IWBaseComponent implements WizardStep {

	private static final long serialVersionUID = 4219702637513606900L;
	public static final String COMPONENT_TYPE = "idega_CrewManageStep";
	static final String stepIdentifier = "CrewManageStep";
	private static final String containerFacet = "container";
	private static final String renderedAtt = "rendered";
	
	private static final String crewManageStyleClass = "crewManage";
	private static final String entryStyleClass = "entry";
	private static final String subentryStyleClass = "subentry";
	private static final String errorStyleClass = "error";
	private static final String headerStyleClass = "header";
	
	private Wizard wizard;

	public void setWizard(Wizard wizard) {
		this.wizard = wizard;
	}

	public String getIdentifier() {
		return stepIdentifier;
	}

	public UIComponent getStepComponent(FacesContext context, Wizard wizard) {
		
		UICrewManageStep step = (UICrewManageStep)context.getApplication().createComponent(COMPONENT_TYPE);
		step.setId(context.getViewRoot().createUniqueId());
		step.setRendered(true);
		step.setWizard(wizard);
		return step;
	}
	
	protected void initializeComponent(FacesContext context) {
		
		Application application = context.getApplication();
		IWContext iwc = IWContext.getIWContext(context);
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		
		HtmlTag containerDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		containerDiv.setStyleClass(crewManageStyleClass);
		containerDiv.setId(context.getViewRoot().createUniqueId());
		containerDiv.setValue(divTag);
		
		HtmlInputHidden hidden = (HtmlInputHidden)application.createComponent(HtmlInputHidden.COMPONENT_TYPE);
		hidden.setId(context.getViewRoot().createUniqueId());
		hidden.setConverter(application.createConverter(Integer.class));
		hidden.setValueBinding(valueAtt, application.createValueBinding(UICrewRegistrationWizard.crewEditWizardBean_modeExp));
		containerDiv.getChildren().add(hidden);
		
		HtmlMessages messages = (HtmlMessages)application.createComponent(HtmlMessages.COMPONENT_TYPE);
		messages.setId(context.getViewRoot().createUniqueId());
		containerDiv.getChildren().add(messages);
		
		HtmlTag div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue(divTag);
		div.setStyleClass(headerStyleClass);
		containerDiv.getChildren().add(div);
		
		HtmlOutputText text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValueBinding(valueAtt, application.createValueBinding(UICrewRegistrationWizard.crewManageBean_crewManageHeaderValueExp));
		div.getChildren().add(text);
		
//		crew label
		HtmlTag entry = createEntry(context, "Crew label: ", HtmlInputText.COMPONENT_TYPE, null, UICrewRegistrationWizard.crewManageBean_crewLabelExp, null, true);
		containerDiv.getChildren().add(entry);
		
//		run chooser
		HtmlSelectOneMenu runChooser = (HtmlSelectOneMenu)application.createComponent(HtmlSelectOneMenu.COMPONENT_TYPE);
		runChooser.setId(context.getViewRoot().createUniqueId());
		UISelectItems selectItems = (UISelectItems)application.createComponent(UISelectItems.COMPONENT_TYPE);
		selectItems.setId(context.getViewRoot().createUniqueId());
		selectItems.setValueBinding(valueAtt, application.createValueBinding(UICrewRegistrationWizard.crewManageBean_runsExp));
		runChooser.getChildren().add(selectItems);
		
		entry = createEntry(context, "Choose run: ", HtmlSelectOneMenu.COMPONENT_TYPE, runChooser, UICrewRegistrationWizard.crewManageBean_runIdExp, UICrewRegistrationWizard.crewManageBean_validateRunSelectionExp, true);
		containerDiv.getChildren().add(entry);
		
		HtmlCommandButton createCrewButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		createCrewButton.setId(context.getViewRoot().createUniqueId());
		createCrewButton.setValue("Create crew");
		containerDiv.getChildren().add(createCrewButton);
		
		HtmlCommandButton next = wizard.getNextButton(context, this);
		next.setValue("Manage crew members");
		containerDiv.getChildren().add(next);
		
		getFacets().put(containerFacet, containerDiv);
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

	/**
	 * @Override
	 */
	public boolean isRendered() {
		
		return IWContext.getIWContext(FacesContext.getCurrentInstance()).isLoggedOn();
	}
	
	protected HtmlTag createEntry(FacesContext context, String labelStr, String inputComponentType, UIComponent uiinput, String valueBindingExp, String validatorMethodExp, boolean required) {
		
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
		span.setStyleClass(subentryStyleClass);
		entryDiv.getChildren().add(span);
		
		if(uiinput == null) {
			
			uiinput = application.createComponent(inputComponentType);
			uiinput.setId(context.getViewRoot().createUniqueId());
		}
		
		uiinput.setValueBinding(valueAtt, application.createValueBinding(valueBindingExp));
		span.getChildren().add(uiinput);
		label.setFor(uiinput.getId());
		
		if(uiinput instanceof UIInput) {
			
			((UIInput)uiinput).setRequired(required);
			
			if(validatorMethodExp != null) {
				((UIInput)uiinput).setValidator(application.createMethodBinding(validatorMethodExp, new Class[] {FacesContext.class, UIComponent.class, Object.class}));
			}
			
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
	
}