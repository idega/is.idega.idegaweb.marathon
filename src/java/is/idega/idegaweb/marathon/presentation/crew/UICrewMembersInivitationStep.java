package is.idega.idegaweb.marathon.presentation.crew;

import java.io.IOException;

import is.idega.idegaweb.marathon.IWBundleStarter;

import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlMessage;
import javax.faces.component.html.HtmlMessages;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlDataTable;
import org.apache.myfaces.custom.column.HtmlSimpleColumn;
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
 * Last modified: $Date: 2008/01/10 18:56:26 $ by $Author: civilis $
 *
 */
public class UICrewMembersInivitationStep extends IWBaseComponent implements WizardStep {

	private static final long serialVersionUID = 7070707428550344301L;
	public static final String COMPONENT_TYPE = "idega_CrewMembersInivitationStep";
	static final String stepIdentifier = "CrewMembersInivitationStep";
	private static final String containerFacet = "container";
	
	public static final String member_var = "member";
	public static final String member_name = "name";
	public static final String member_nameExp = "#{member.name}";
	public static final String member_isOwner = "isOwner";
	public static final String member_isOwnerExp = "#{member.isOwner}";
	public static final String member_modifyOnclick = "modifyOnclick";
	public static final String member_modifyOnclickExp = "#{member.modifyOnclick}";
	
	public static final String searchResult_var = "searchResult";
	public static final String sr_participantName = "participantName";
	public static final String sr_participantNameExp = "#{searchResult.participantName}";
	public static final String sr_participantNumber = "participantNumber";
	public static final String sr_participantNumberExp = "#{searchResult.participantNumber}";
	public static final String sr_modifyOnclick = "modifyOnclick";
	public static final String sr_modifyOnclickExp = "#{searchResult.modifyOnclick}";
	
	public static final String crewMembersInvitationBean_memberDeleteParticipantIdParam = 			"cmib_mdpi";
	public static final String crewMembersInvitationBean_memberAddParticipantIdParam = 				"cmib_mapi";
	
	private static final String crewMembersInivitationStyleClass = "crewMembersInivitation";
	private static final String errorStyleClass = "error";
	private static final String headerStyleClass = "header";
	private static final String entryStyleClass = "entry";
	private static final String labelStyleClass = "label";
	private static final String subentryStyleClass = "subentry";
	private static final String onclickAtt = "onclick";
	private static final String renderedAtt = "rendered";
	private static final String forceIdAtt = "forceId";
	
	private Wizard wizard;

	public void setWizard(Wizard wizard) {
		this.wizard = wizard;
	}

	public String getIdentifier() {
		return stepIdentifier;
	}

	public UIComponent getStepComponent(FacesContext context, Wizard wizard) {
		
		UICrewMembersInivitationStep step = (UICrewMembersInivitationStep)context.getApplication().createComponent(COMPONENT_TYPE);
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
		containerDiv.setStyleClass(crewMembersInivitationStyleClass);
		containerDiv.setId(context.getViewRoot().createUniqueId());
		containerDiv.setValue(divTag);
		
		HtmlInputHidden hidden = (HtmlInputHidden)application.createComponent(HtmlInputHidden.COMPONENT_TYPE);
		hidden.setId(context.getViewRoot().createUniqueId());
		hidden.setConverter(application.createConverter(Integer.class));
		hidden.setValueBinding(valueAtt, application.createValueBinding(UICrewRegistrationWizard.crewEditWizardBean_modeExp));
		containerDiv.getChildren().add(hidden);
		
		org.apache.myfaces.component.html.ext.HtmlInputHidden extHidden = (org.apache.myfaces.component.html.ext.HtmlInputHidden)application.createComponent(org.apache.myfaces.component.html.ext.HtmlInputHidden.COMPONENT_TYPE);
		extHidden.setId(crewMembersInvitationBean_memberDeleteParticipantIdParam);
		extHidden.setValueBinding(valueAtt, application.createValueBinding(UICrewRegistrationWizard.crewMembersInvitationBean_deleteMemberParticipantIdExp));
		extHidden.setValueBinding(forceIdAtt, application.createValueBinding(UICrewRegistrationWizard.crewMembersInvitationBean_forceIdHackExp));
		containerDiv.getChildren().add(extHidden);
		
		extHidden = (org.apache.myfaces.component.html.ext.HtmlInputHidden)application.createComponent(org.apache.myfaces.component.html.ext.HtmlInputHidden.COMPONENT_TYPE);
		extHidden.setId(crewMembersInvitationBean_memberAddParticipantIdParam);
		extHidden.setValueBinding(valueAtt, application.createValueBinding(UICrewRegistrationWizard.crewMembersInvitationBean_addMemberParticipantIdExp));
		extHidden.setValueBinding(forceIdAtt, application.createValueBinding(UICrewRegistrationWizard.crewMembersInvitationBean_forceIdHackExp));
		containerDiv.getChildren().add(extHidden);
		
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
		text.setValueBinding(valueAtt, application.createValueBinding(UICrewRegistrationWizard.crewMembersInvitationBean_headerExp));
		div.getChildren().add(text);
		
		containerDiv.getChildren().add(createMembersListArea(context));
		
		HtmlInputText searchParticipantsInput = (HtmlInputText)application.createComponent(HtmlInputText.COMPONENT_TYPE);
		searchParticipantsInput.setId(context.getViewRoot().createUniqueId());
		searchParticipantsInput.setValueBinding(valueAtt, application.createValueBinding(UICrewRegistrationWizard.crewMembersInvitationBean_searchStringExp));
		containerDiv.getChildren().add(searchParticipantsInput);
		
		HtmlCommandButton searchButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		searchButton.setId(context.getViewRoot().createUniqueId());
		searchButton.setValue("Search");
		searchButton.setAction(application.createMethodBinding(UICrewRegistrationWizard.crewMembersInvitationBean_searchExp, null));
		containerDiv.getChildren().add(searchButton);
		
		containerDiv.getChildren().add(createSearchArea(context));
		
		HtmlCommandButton prev = wizard.getPreviousButton(context, this);
		containerDiv.getChildren().add(prev);
		
		getFacets().put(containerFacet, containerDiv);
	}
	
	protected UIComponent createMembersListArea(FacesContext context) {
		
		Application application = context.getApplication();
		
		HtmlTag containerDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		containerDiv.setId(context.getViewRoot().createUniqueId());
		containerDiv.setValue(divTag);
		containerDiv.setStyleClass("membersList");
		
		HtmlDataTable membersTable = (HtmlDataTable)application.createComponent(HtmlDataTable.COMPONENT_TYPE);
		membersTable.setId(context.getViewRoot().createUniqueId());
		membersTable.setVar(member_var);
		membersTable.setValueBinding(valueAtt, context.getApplication().createValueBinding(UICrewRegistrationWizard.crewMembersInvitationBean_membersListExp));
		
		membersTable.getChildren().add(createColumn(context, member_nameExp, "Member name"));
//		is owner
		membersTable.getChildren().add(createColumn(context, member_isOwnerExp, " "));
		
		HtmlCommandLink deleteMemberLink = (HtmlCommandLink)application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
		deleteMemberLink.setId(context.getViewRoot().createUniqueId());
		deleteMemberLink.setValue("Delete member");
		deleteMemberLink.setValueBinding(onclickAtt, application.createValueBinding(member_modifyOnclickExp));
		deleteMemberLink.setAction(application.createMethodBinding(UICrewRegistrationWizard.crewMembersInvitationBean_deleteMemberExp, null));
		
		membersTable.getChildren().add(createColumn(context, deleteMemberLink, " "));
		containerDiv.getChildren().add(membersTable);
		
		return containerDiv;
	}
	
	protected UIComponent createSearchArea(FacesContext context) {
		
		Application application = context.getApplication();
		
		HtmlTag containerDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		containerDiv.setId(context.getViewRoot().createUniqueId());
		containerDiv.setValue(divTag);
		containerDiv.setStyleClass("searchArea");
		
		HtmlDataTable searchResultsTable = (HtmlDataTable)application.createComponent(HtmlDataTable.COMPONENT_TYPE);
		searchResultsTable.setId(context.getViewRoot().createUniqueId());
		
		searchResultsTable.setVar(searchResult_var);
		searchResultsTable.setValueBinding(renderedAtt, context.getApplication().createValueBinding(UICrewRegistrationWizard.crewMembersInvitationBean_searchResultListRenderedExp));
		searchResultsTable.setValueBinding(valueAtt, context.getApplication().createValueBinding("#{crewMembersInvitationBean.searchResults}"));
		
		searchResultsTable.getChildren().add(createColumn(context, sr_participantNameExp, "Member name"));
		searchResultsTable.getChildren().add(createColumn(context, sr_participantNumberExp, "Member participant number"));
		
		HtmlCommandLink addMemberLink = (HtmlCommandLink)application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
		addMemberLink.setId(context.getViewRoot().createUniqueId());
		addMemberLink.setValue("Add crew member");
		addMemberLink.setValueBinding(onclickAtt, application.createValueBinding(sr_modifyOnclickExp));
		addMemberLink.setAction(application.createMethodBinding(UICrewRegistrationWizard.crewMembersInvitationBean_addMemberExp, null));
		
		searchResultsTable.getChildren().add(createColumn(context, addMemberLink, " "));
		containerDiv.getChildren().add(searchResultsTable);
		
		return containerDiv;
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

	protected UIColumn createColumn(FacesContext context, UIComponent child, String headerText) {
		
		HtmlSimpleColumn column = (HtmlSimpleColumn)context.getApplication().createComponent(HtmlSimpleColumn.COMPONENT_TYPE);
		column.getChildren().add(child);
		
		if(headerText != null) {
		
			HtmlOutputText text = (HtmlOutputText)context.getApplication().createComponent(HtmlOutputText.COMPONENT_TYPE);
			text.setValue(headerText);
			column.setHeader(text);
		}
		
		return column;
	}
	
	protected UIColumn createColumn(FacesContext context, String textExpression, String headerText) {
	
		HtmlOutputText text = (HtmlOutputText)context.getApplication().createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setValueBinding(valueAtt, context.getApplication().createValueBinding(textExpression));
		
		return createColumn(context, text, headerText);
	}
}