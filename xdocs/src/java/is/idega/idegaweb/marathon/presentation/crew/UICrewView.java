package is.idega.idegaweb.marathon.presentation.crew;

import is.idega.idegaweb.marathon.IWBundleStarter;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlDataTable;
import org.apache.myfaces.custom.column.HtmlSimpleColumn;
import org.apache.myfaces.custom.htmlTag.HtmlTag;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2008/01/14 21:05:57 $ by $Author: civilis $
 *
 */
public class UICrewView extends IWBaseComponent {

	public static final String COMPONENT_TYPE = "idega_CrewView";
	
	public static final String member_var = UICrewMembersInivitationStep.member_var;
	public static final String member_name = UICrewMembersInivitationStep.member_name;
	public static final String member_nameExp = UICrewMembersInivitationStep.member_nameExp;
	public static final String member_role = UICrewMembersInivitationStep.member_role;
	public static final String member_roleExp = UICrewMembersInivitationStep.member_roleExp;
	
	private static final String containerFacet = "container";
	private static final String crewVuewStyleClass = "marathonCrewView";
	private static final String headerStyleClass = "header";
	private static final String membersListStyleClass = "membersList";
	
	
	/**
	 * @Override
	 */
	protected void initializeComponent(FacesContext context) {
	
		Application application = context.getApplication();
		
		HtmlForm form = (HtmlForm)context.getApplication().createComponent(HtmlForm.COMPONENT_TYPE);
		form.setId(context.getViewRoot().createUniqueId());
		getFacets().put(containerFacet, form);
		
		HtmlTag containerDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		containerDiv.setStyleClass(crewVuewStyleClass);
		containerDiv.setId(context.getViewRoot().createUniqueId());
		containerDiv.setValue(divTag);
		form.getChildren().add(containerDiv);
		
		HtmlTag div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue(divTag);
		div.setStyleClass(headerStyleClass);
		containerDiv.getChildren().add(div);
		
		HtmlOutputText text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		
		text.setValueBinding(valueAtt, application.createValueBinding(UICrewsOverview.crewManageBean_crewViewHeaderExp));
		div.getChildren().add(text);
		
		containerDiv.getChildren().add(createMembersListArea(context));
	}
	
	protected UIComponent createMembersListArea(FacesContext context) {
		
		Application application = context.getApplication();
		IWContext iwc = IWContext.getIWContext(context);
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		
		HtmlTag containerDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		containerDiv.setId(context.getViewRoot().createUniqueId());
		containerDiv.setValue(divTag);
		containerDiv.setStyleClass(membersListStyleClass);
		
		HtmlDataTable membersTable = (HtmlDataTable)application.createComponent(HtmlDataTable.COMPONENT_TYPE);
		membersTable.setId(context.getViewRoot().createUniqueId());
		membersTable.setVar(member_var);
		membersTable.setValueBinding(valueAtt, context.getApplication().createValueBinding(UICrewsOverview.crewMembersInvitationBean_membersListExp));
		
		membersTable.getChildren().add(createColumn(context, member_nameExp, iwrb.getLocalizedString("crew.crewView.memberName", "Member name")));
		membersTable.getChildren().add(createColumn(context, member_roleExp, iwrb.getLocalizedString("crew.invitation.memberRole", "Member role")));
		
		containerDiv.getChildren().add(membersTable);
		
		HtmlCommandButton viewCrewsListButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		viewCrewsListButton.setId(context.getViewRoot().createUniqueId());
		viewCrewsListButton.setValue(iwrb.getLocalizedString("crew.crewView.viewCrewsList", "View crews list"));
		viewCrewsListButton.setAction(application.createMethodBinding(UICrewsOverview.crewManageBean_viewCrewsListExp, null));
		containerDiv.getChildren().add(viewCrewsListButton);
		
		return containerDiv;
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
	
	/**
	 * @Override
	 */
	public void encodeChildren(FacesContext context) throws IOException {
		
		renderChild(context, getFacet(containerFacet));
	}
	
	/**
	 * @Override
	 */
	public boolean getRendersChildren() {
		return true;
	}
}