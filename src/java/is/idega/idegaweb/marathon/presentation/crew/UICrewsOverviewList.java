package is.idega.idegaweb.marathon.presentation.crew;

import is.idega.idegaweb.marathon.IWBundleStarter;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlDataTable;
import org.apache.myfaces.component.html.ext.HtmlInputHidden;
import org.apache.myfaces.custom.column.HtmlSimpleColumn;
import org.apache.myfaces.custom.htmlTag.HtmlTag;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.7 $
 *
 * Last modified: $Date: 2008/01/14 21:05:56 $ by $Author: civilis $
 *
 */
public class UICrewsOverviewList extends IWBaseComponent {

	public static final String COMPONENT_TYPE = "idega_CrewsOverviewList";
	public static final String EDIT_CREW_ID = "ecid";
	
	public static final String crewsOverviewListBean_participantIdParam = 					"colb_pid";
	
	
	public static final String crew_var = 					"crew";
	public static final String crew_label = 				"label";
	public static final String crew_labelExp = 				"#{crew.label}";
	public static final String crew_runLabel = 				"runLabel";
	public static final String crew_runLabelExp = 			"#{crew.runLabel}";
	public static final String crew_distance = 				"distance";
	public static final String crew_distanceExp = 			"#{crew.distance}";
	public static final String crew_pidOnclick = 			"pidOnclick";
	public static final String crew_pidOnclickExp = 		"#{crew.pidOnclick}";
	public static final String crew_renderedEdit = 			"renderedEdit";
	public static final String crew_renderedEditExp = 		"#{crew.renderedEdit}";
	public static final String crew_renderedAcceptInvitation = 		"renderedAcceptInvitation";
	public static final String crew_renderedAcceptInvitationExp = 	"#{crew.renderedAcceptInvitation}";
	public static final String crew_renderedRejectInvitation = 		"renderedRejectInvitation";
	public static final String crew_renderedRejectInvitationExp = 	"#{crew.renderedRejectInvitation}";
	
	
	private static final String onclickAtt = "onclick";
	private static final String renderedAtt = "rendered";
	private static final String forceIdAtt = "forceId";
	private static final String containerFacet = "container";
	
	/**
	 * @Override
	 */
	protected void initializeComponent(FacesContext context) {
	
		Application application = context.getApplication();
		IWContext iwc = IWContext.getIWContext(context);
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		
		HtmlForm form = (HtmlForm)context.getApplication().createComponent(HtmlForm.COMPONENT_TYPE);
		form.setId(context.getViewRoot().createUniqueId());
		getFacets().put(containerFacet, form);
		
		
		HtmlInputHidden hidden = (HtmlInputHidden)application.createComponent(HtmlInputHidden.COMPONENT_TYPE);
		hidden.setId(crewsOverviewListBean_participantIdParam);
		hidden.setValueBinding(valueAtt, application.createValueBinding(UICrewsOverview.crewEditWizardBean_participantIdExp));
		hidden.setValueBinding(forceIdAtt, application.createValueBinding(UICrewsOverview.crewsOverviewListBean_forceIdHackExp));
		form.getChildren().add(hidden);
		
		HtmlTag containerDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		containerDiv.setId(context.getViewRoot().createUniqueId());
		containerDiv.setValue(divTag);
		containerDiv.setStyleClass("marathonCrewsOverviewList");
		form.getChildren().add(containerDiv);
		
		HtmlTag div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue(divTag);
		div.setStyleClass("headerContainer");
		containerDiv.getChildren().add(div);
		
		HtmlTag header = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		header.setId(context.getViewRoot().createUniqueId());
		header.setValue(divTag);
		header.setStyleClass("header");
		div.getChildren().add(header);
		
		HtmlOutputText headerText = (HtmlOutputText)context.getApplication().createComponent(HtmlOutputText.COMPONENT_TYPE);
		headerText.setValue("Crews view");
		header.getChildren().add(headerText);		
		
		HtmlCommandLink startNewCrewRegLink = (HtmlCommandLink)application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
		startNewCrewRegLink.setId(context.getViewRoot().createUniqueId());
		startNewCrewRegLink.setValue(iwrb.getLocalizedString("crew.overviewList.createNewCrew", "Create new crew"));
		startNewCrewRegLink.setAction(application.createMethodBinding(UICrewsOverview.crewManageBean_startNewCrewRegistrationExp, null));
		startNewCrewRegLink.setStyleClass("createNewLink");
		
		div.getChildren().add(startNewCrewRegLink);
		
		div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue(divTag);
		div.setStyleClass("contentsContainer");
		containerDiv.getChildren().add(div);
		
		HtmlDataTable crewsTable = (HtmlDataTable)application.createComponent(HtmlDataTable.COMPONENT_TYPE);
		crewsTable.setSortable(true);
		crewsTable.setId(context.getViewRoot().createUniqueId());
		crewsTable.setVar(crew_var);
		crewsTable.setValueBinding(valueAtt, context.getApplication().createValueBinding(UICrewsOverview.crewsOverviewListBean_crewsOverviewListExp));
		crewsTable.setCellpadding("0");
		crewsTable.setCellspacing("0");
		
		crewsTable.getChildren().add(createColumn(context, crew_labelExp, iwrb.getLocalizedString("crew.overviewList.crewLabel", "Crew label")));
		crewsTable.getChildren().add(createColumn(context, crew_runLabelExp, iwrb.getLocalizedString("crew.overviewList.runLabel", "Run label")));
		crewsTable.getChildren().add(createColumn(context, crew_distanceExp, iwrb.getLocalizedString("crew.overviewList.distanceLabel", "Distance label")));
		
		HtmlTag buttonsContainer = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		buttonsContainer.setId(context.getViewRoot().createUniqueId());
		buttonsContainer.setValue(divTag);
		buttonsContainer.setStyleClass("links");
		
		addLinks(context, buttonsContainer);
		
		crewsTable.getChildren().add(createColumn(context, buttonsContainer, " "));
		
		div.getChildren().add(crewsTable);
	}
	
	protected void addLinks(FacesContext context, UIComponent parent) {
		
		Application application = context.getApplication();
		IWContext iwc = IWContext.getIWContext(context);
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		
		HtmlCommandLink link = (HtmlCommandLink)application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
		link.setId(context.getViewRoot().createUniqueId());
		link.setValue(iwrb.getLocalizedString("crew.overviewList.edit", "Edit"));
		link.setValueBinding(onclickAtt, application.createValueBinding(crew_pidOnclickExp));
		link.setValueBinding(renderedAtt, application.createValueBinding(crew_renderedEditExp));
		link.setAction(application.createMethodBinding(UICrewsOverview.crewManageBean_editCrewExp, null));
		parent.getChildren().add(link);
		
		link = (HtmlCommandLink)application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
		link.setId(context.getViewRoot().createUniqueId());
		link.setValue(iwrb.getLocalizedString("crew.overviewList.acceptInvitation", "Accept invitation"));
		link.setValueBinding(onclickAtt, application.createValueBinding(crew_pidOnclickExp));
		link.setValueBinding(renderedAtt, application.createValueBinding(crew_renderedAcceptInvitationExp));
		link.setAction(application.createMethodBinding(UICrewsOverview.crewManageBean_acceptInvitationExp, null));
		parent.getChildren().add(link);
		
		link = (HtmlCommandLink)application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
		link.setId(context.getViewRoot().createUniqueId());
		link.setValue(iwrb.getLocalizedString("crew.overviewList.rejectInvitation", "Reject invitation"));
		link.setValueBinding(onclickAtt, application.createValueBinding(crew_pidOnclickExp));
		link.setValueBinding(renderedAtt, application.createValueBinding(crew_renderedRejectInvitationExp));
		link.setAction(application.createMethodBinding(UICrewsOverview.crewManageBean_rejectInvitationExp, null));
		parent.getChildren().add(link);
		
		link = (HtmlCommandLink)application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
		link.setId(context.getViewRoot().createUniqueId());
		link.setValue(iwrb.getLocalizedString("crew.overviewList.viewCrew", "View crew"));
		link.setValueBinding(onclickAtt, application.createValueBinding(crew_pidOnclickExp));
		link.setAction(application.createMethodBinding(UICrewsOverview.crewManageBean_viewCrewViewExp, null));
		parent.getChildren().add(link);
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