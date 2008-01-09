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
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/01/09 16:27:41 $ by $Author: civilis $
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
	private static final String labelStyleClass = "label";
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
		
//		<choose run or chosen run>
		
//		choose run
		HtmlSelectOneMenu runChooser = (HtmlSelectOneMenu)application.createComponent(HtmlSelectOneMenu.COMPONENT_TYPE);
		runChooser.setId(context.getViewRoot().createUniqueId());
		
		UISelectItems selectItems = (UISelectItems)application.createComponent(UISelectItems.COMPONENT_TYPE);
		selectItems.setId(context.getViewRoot().createUniqueId());
		selectItems.setValueBinding(valueAtt, application.createValueBinding(UICrewRegistrationWizard.crewManageBean_runsExp));
		runChooser.getChildren().add(selectItems);
		runChooser.setValueBinding(renderedAtt, application.createValueBinding(UICrewRegistrationWizard.crewEditWizardBean_newCrewModeExp));
		
		entry = createEntry(context, "Choose run: ", HtmlSelectOneMenu.COMPONENT_TYPE, runChooser, UICrewRegistrationWizard.crewManageBean_runIdExp, UICrewRegistrationWizard.crewManageBean_validateRunSelectionExp, true);
		containerDiv.getChildren().add(entry);
		entry.setValueBinding(renderedAtt, application.createValueBinding(UICrewRegistrationWizard.crewEditWizardBean_newCrewModeExp));
		
//		chosen run
		
		entry = createEntry(context, "Crew run: ", HtmlOutputText.COMPONENT_TYPE, null, UICrewRegistrationWizard.crewEditWizardBean_runLabelExp, null, false);
		containerDiv.getChildren().add(entry);
		entry.setValueBinding(renderedAtt, application.createValueBinding(UICrewRegistrationWizard.crewEditWizardBean_editCrewModeExp));
		
//		</choose run or chosen run>
		
		HtmlCommandButton createCrewButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		createCrewButton.setId(context.getViewRoot().createUniqueId());
		createCrewButton.setValue("Create crew");
		createCrewButton.setAction(application.createMethodBinding(UICrewRegistrationWizard.crewManageBean_createCrewExp, null));
		createCrewButton.setValueBinding(renderedAtt, application.createValueBinding(UICrewRegistrationWizard.crewEditWizardBean_newCrewModeExp));
		containerDiv.getChildren().add(createCrewButton);
		
		HtmlCommandButton updateCrewButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		updateCrewButton.setId(context.getViewRoot().createUniqueId());
		updateCrewButton.setValue("Update crew");
		updateCrewButton.setAction(application.createMethodBinding(UICrewRegistrationWizard.crewManageBean_updateCrewExp, null));
		updateCrewButton.setValueBinding(renderedAtt, application.createValueBinding(UICrewRegistrationWizard.crewEditWizardBean_editCrewModeExp));
		containerDiv.getChildren().add(updateCrewButton);
		
		HtmlCommandButton next = wizard.getNextButton(context, this);
		next.setValue("Manage crew members");
		next.setValueBinding(renderedAtt, application.createValueBinding(UICrewRegistrationWizard.crewEditWizardBean_editCrewModeExp));
		containerDiv.getChildren().add(next);
		
		getFacets().put(containerFacet, containerDiv);
	}
	
	/**
	 * @Override
	 */
	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
		
		renderChild(context, getFacet(containerFacet));
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
	
	protected HtmlTag createEntry(FacesContext context, String labelStr, String inputComponentType, UIComponent entryValueComponent, String valueBindingExp, String validatorMethodExp, boolean required) {
		
		Application application = context.getApplication();
		
		HtmlTag entryDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		entryDiv.setId(context.getViewRoot().createUniqueId());
		entryDiv.setStyleClass(entryStyleClass);
		entryDiv.setValue(divTag);
		
		if(entryValueComponent == null) {
			
			entryValueComponent = application.createComponent(inputComponentType);
			entryValueComponent.setId(context.getViewRoot().createUniqueId());
		}
		
//		label
		if(entryValueComponent instanceof UIInput) {
			HtmlOutputLabel label = (HtmlOutputLabel)application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
			label.setId(context.getViewRoot().createUniqueId());
			label.setValue(labelStr);
			label.setFor(entryValueComponent.getId());
			entryDiv.getChildren().add(label);
			
		} else {
			
			HtmlTag span = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
			span.setId(context.getViewRoot().createUniqueId());
			span.setStyleClass(labelStyleClass);
			span.setValue(spanTag);
			entryDiv.getChildren().add(span);
			
			HtmlOutputText text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
			text.setId(context.getViewRoot().createUniqueId());
			text.setValue(labelStr);
			span.getChildren().add(text);
		}

//		input
		HtmlTag span = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		span.setId(context.getViewRoot().createUniqueId());
		span.setValue(spanTag);
		span.setStyleClass(subentryStyleClass);
		entryDiv.getChildren().add(span);
		
		entryValueComponent.setValueBinding(valueAtt, application.createValueBinding(valueBindingExp));
		span.getChildren().add(entryValueComponent);
		
		
		if(entryValueComponent instanceof UIInput) {
			
			((UIInput)entryValueComponent).setRequired(required);
			
			if(validatorMethodExp != null) {
				((UIInput)entryValueComponent).setValidator(application.createMethodBinding(validatorMethodExp, new Class[] {FacesContext.class, UIComponent.class, Object.class}));
			}
			
			HtmlTag errSpan = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
			errSpan.setId(context.getViewRoot().createUniqueId());
			errSpan.setStyleClass(errorStyleClass);
			errSpan.setValue(spanTag);
			span.getChildren().add(errSpan);
			
			HtmlMessage errMsg = (HtmlMessage)application.createComponent(HtmlMessage.COMPONENT_TYPE);
			errMsg.setId(context.getViewRoot().createUniqueId());
			errMsg.setFor(entryValueComponent.getId());
			errSpan.getChildren().add(errMsg);
		}
		
		return entryDiv;
	}
	
}