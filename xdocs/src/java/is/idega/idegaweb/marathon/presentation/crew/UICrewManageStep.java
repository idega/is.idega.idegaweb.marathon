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
import com.idega.presentation.CSSSpacer;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.wizard.Wizard;
import com.idega.presentation.wizard.WizardStep;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.8 $
 *
 * Last modified: $Date: 2008/01/15 09:20:50 $ by $Author: civilis $
 *
 */
public class UICrewManageStep extends IWBaseComponent implements WizardStep {

	private static final long serialVersionUID = 4219702637513606900L;
	public static final String COMPONENT_TYPE = "idega_CrewManageStep";
	static final String stepIdentifier = "CrewManageStep";
	private static final String containerFacet = "container";
	private static final String renderedAtt = "rendered";
	
	private static final String crewManageStyleClass = "marathonCrewManage";
	private static final String entryStyleClass = "entry";
	private static final String labelStyleClass = "label";
	private static final String errorStyleClass = "error";
	private static final String errorsStyleClass = "errors";
	private static final String headerStyleClass = "header";
	private static final String buttonsStyleClass = "buttons";
	private static final String deleteCrewStyleClass = "deleteCrew";
	private static final String subentryLabelStyleClass = "subentryLabel";
	private static final String subentryInputStyleClass = "subentryInput";
	private static final String subentryErrorStyleClass = "subentryError";
	
	
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
		hidden.setValueBinding(valueAtt, application.createValueBinding(UICrewsOverview.crewEditWizardBean_modeExp));
		containerDiv.getChildren().add(hidden);
		
		
		HtmlTag div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue(divTag);
		div.setStyleClass(headerStyleClass);
		containerDiv.getChildren().add(div);
		
		HtmlOutputText text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValueBinding(valueAtt, application.createValueBinding(UICrewsOverview.crewManageBean_crewManageHeaderValueExp));
		div.getChildren().add(text);
		
		div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue(divTag);
		div.setStyleClass(errorsStyleClass);
		containerDiv.getChildren().add(div);
		
		HtmlMessages messages = (HtmlMessages)application.createComponent(HtmlMessages.COMPONENT_TYPE);
		messages.setId(context.getViewRoot().createUniqueId());
		div.getChildren().add(messages);
		
//		crew label
		HtmlTag entry = createEntry(context, iwrb.getLocalizedString("crew.manage.crewLabel", "Crew label: "), HtmlInputText.COMPONENT_TYPE, null, UICrewsOverview.crewManageBean_crewLabelForOwnerExp, null, true);
		containerDiv.getChildren().add(entry);
		
		CSSSpacer spacer = new CSSSpacer();
		containerDiv.getChildren().add(spacer);
		
//		<choose run or chosen run>
		
//		choose run
		HtmlSelectOneMenu runChooser = (HtmlSelectOneMenu)application.createComponent(HtmlSelectOneMenu.COMPONENT_TYPE);
		runChooser.setId(context.getViewRoot().createUniqueId());
		
		UISelectItems selectItems = (UISelectItems)application.createComponent(UISelectItems.COMPONENT_TYPE);
		selectItems.setId(context.getViewRoot().createUniqueId());
		selectItems.setValueBinding(valueAtt, application.createValueBinding(UICrewsOverview.crewManageBean_runsExp));
		runChooser.getChildren().add(selectItems);
		runChooser.setValueBinding(renderedAtt, application.createValueBinding(UICrewsOverview.crewEditWizardBean_newCrewModeExp));
		
		entry = createEntry(context, iwrb.getLocalizedString("crew.manage.chooseRun", "Choose run: "), HtmlSelectOneMenu.COMPONENT_TYPE, runChooser, UICrewsOverview.crewManageBean_runIdForOwnerExp, UICrewsOverview.crewManageBean_validateRunSelectionExp, true);
		containerDiv.getChildren().add(entry);
		entry.setValueBinding(renderedAtt, application.createValueBinding(UICrewsOverview.crewEditWizardBean_newCrewModeExp));
		
		spacer = new CSSSpacer();
		containerDiv.getChildren().add(spacer);
		
//		chosen run
		
		entry = createEntry(context, iwrb.getLocalizedString("crew.manage.crewRun", "Crew run: "), HtmlOutputText.COMPONENT_TYPE, null, UICrewsOverview.crewEditWizardBean_runLabelExp, null, false);
		containerDiv.getChildren().add(entry);
		entry.setValueBinding(renderedAtt, application.createValueBinding(UICrewsOverview.crewEditWizardBean_editCrewModeExp));
		spacer = new CSSSpacer();
		containerDiv.getChildren().add(spacer);
		
//		</choose run or chosen run>
		
		
		div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue(divTag);
		div.setStyleClass(buttonsStyleClass);
		containerDiv.getChildren().add(div);
		
		HtmlCommandButton createCrewButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		createCrewButton.setId(context.getViewRoot().createUniqueId());
		createCrewButton.setValue(iwrb.getLocalizedString("crew.manage.createCrew", "Create crew"));
		createCrewButton.setAction(application.createMethodBinding(UICrewsOverview.crewManageBean_createCrewExp, null));
		createCrewButton.setValueBinding(renderedAtt, application.createValueBinding(UICrewsOverview.crewEditWizardBean_newCrewModeExp));
		div.getChildren().add(createCrewButton);
		
		HtmlCommandButton updateCrewButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		updateCrewButton.setId(context.getViewRoot().createUniqueId());
		updateCrewButton.setValue(iwrb.getLocalizedString("crew.manage.updateLabel", "Update crew"));
		updateCrewButton.setAction(application.createMethodBinding(UICrewsOverview.crewManageBean_updateCrewExp, null));
		updateCrewButton.setValueBinding(renderedAtt, application.createValueBinding(UICrewsOverview.crewEditWizardBean_editCrewModeExp));
		div.getChildren().add(updateCrewButton);
		
		HtmlCommandButton next = wizard.getNextButton(context, this);
		next.setValue(iwrb.getLocalizedString("crew.manage.manageMembers", "Manage crew members"));
		next.setValueBinding(renderedAtt, application.createValueBinding(UICrewsOverview.crewEditWizardBean_editCrewModeExp));
		div.getChildren().add(next);
		
		HtmlCommandButton viewCrewsListButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		viewCrewsListButton.setId(context.getViewRoot().createUniqueId());
		viewCrewsListButton.setValue(iwrb.getLocalizedString("crew.manage.viewCrewList", "View crews list"));
		viewCrewsListButton.setAction(application.createMethodBinding(UICrewsOverview.crewManageBean_viewCrewsListExp, null));
		viewCrewsListButton.setImmediate(true);
		div.getChildren().add(viewCrewsListButton);
		
		HtmlCommandButton deleteCrewButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		deleteCrewButton.setId(context.getViewRoot().createUniqueId());
		deleteCrewButton.setValue(iwrb.getLocalizedString("crew.manage.deleteCrew", "Delete crew"));
		deleteCrewButton.setAction(application.createMethodBinding(UICrewsOverview.crewManageBean_deleteCrewExp, null));
		deleteCrewButton.setValueBinding(renderedAtt, application.createValueBinding(UICrewsOverview.crewEditWizardBean_editCrewModeExp));
		deleteCrewButton.setStyleClass(deleteCrewStyleClass);
		div.getChildren().add(deleteCrewButton);
		
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
		
		HtmlTag div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setStyleClass(subentryLabelStyleClass);
		div.setValue(divTag);
		entryDiv.getChildren().add(div);
		
//		label
		if(entryValueComponent instanceof UIInput) {
			HtmlOutputLabel label = (HtmlOutputLabel)application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
			label.setId(context.getViewRoot().createUniqueId());
			label.setValue(labelStr);
			label.setFor(entryValueComponent.getId());
			div.getChildren().add(label);
			
		} else {
			
			HtmlTag span = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
			span.setId(context.getViewRoot().createUniqueId());
			span.setStyleClass(labelStyleClass);
			span.setValue(spanTag);
			div.getChildren().add(span);
			
			HtmlOutputText text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
			text.setId(context.getViewRoot().createUniqueId());
			text.setValue(labelStr);
			span.getChildren().add(text);
		}
		
//		input
		
		div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setStyleClass(subentryInputStyleClass);
		div.setValue(divTag);
		entryDiv.getChildren().add(div);
		
		entryValueComponent.setValueBinding(valueAtt, application.createValueBinding(valueBindingExp));
		div.getChildren().add(entryValueComponent);
		
		if(entryValueComponent instanceof UIInput) {
			
			div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
			div.setId(context.getViewRoot().createUniqueId());
			div.setStyleClass(subentryErrorStyleClass);
			div.setValue(divTag);
			entryDiv.getChildren().add(div);
			
			((UIInput)entryValueComponent).setRequired(required);
			
			if(validatorMethodExp != null) {
				((UIInput)entryValueComponent).setValidator(application.createMethodBinding(validatorMethodExp, new Class[] {FacesContext.class, UIComponent.class, Object.class}));
			}
			
			HtmlTag errSpan = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
			errSpan.setId(context.getViewRoot().createUniqueId());
			errSpan.setStyleClass(errorStyleClass);
			errSpan.setValue(divTag);
			div.getChildren().add(errSpan);
			
			HtmlMessage errMsg = (HtmlMessage)application.createComponent(HtmlMessage.COMPONENT_TYPE);
			errMsg.setId(context.getViewRoot().createUniqueId());
			errMsg.setFor(entryValueComponent.getId());
			errSpan.getChildren().add(errMsg);
		}
		
		return entryDiv;
	}
}