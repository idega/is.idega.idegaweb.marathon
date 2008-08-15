package is.idega.idegaweb.marathon;

import is.idega.idegaweb.marathon.presentation.Charities;
import is.idega.idegaweb.marathon.presentation.CharitiesRunYear;
import is.idega.idegaweb.marathon.presentation.RunDistanceEditor;
import is.idega.idegaweb.marathon.presentation.RunEditor;
import is.idega.idegaweb.marathon.presentation.RunYearEditor;

import java.util.ArrayList;
import java.util.Collection;

import com.idega.core.view.DefaultViewNode;
import com.idega.core.view.ViewManager;
import com.idega.core.view.ViewNode;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.workspace.view.WorkspaceApplicationNode;
import com.idega.workspace.view.WorkspaceClassViewNode;

public class MarathonViewManager {
	
	private ViewNode marathonViewNode;
	private IWMainApplication iwma;
	
	public static MarathonViewManager getInstance(IWMainApplication iwma) {
		MarathonViewManager instance = (MarathonViewManager) iwma.getAttribute("marathonviewmanager");
		if(instance==null){
			instance = new MarathonViewManager();
			instance.iwma=iwma;
			iwma.setAttribute("marathonviewmanager",instance);
		}
		return instance;
	}
	
	public ViewManager getViewManager(){
		return ViewManager.getInstance(this.iwma);
	}
	
	
	public ViewNode getMarathonViewNode(){
		IWBundle iwb = this.iwma.getBundle("is.idega.idegaweb.marathon");
		if(this.marathonViewNode==null){
			this.marathonViewNode = initalizeMarathonNode(iwb);
		}
		return this.marathonViewNode;
	}
	
	private ViewNode initalizeMarathonNode(IWBundle iwb) {
		ViewManager viewManager = ViewManager.getInstance(this.iwma);
		ViewNode workspace = viewManager.getWorkspaceRoot();
		
		Collection roles = new ArrayList();
		roles.add("marathon_admin");
		
		DefaultViewNode marathonNode = new WorkspaceApplicationNode("marathon",workspace,roles);
		marathonNode.setName("#{localizedStrings['is.idega.idegaweb.marathon']['Marathon']}");

		DefaultViewNode setupNode = new WorkspaceClassViewNode("setup",marathonNode);
		setupNode.setName("#{localizedStrings['is.idega.idegaweb.marathon']['Setup']}");
		
		WorkspaceClassViewNode runs = new WorkspaceClassViewNode("runs",setupNode);
		runs.setName("#{localizedStrings['is.idega.idegaweb.marathon']['Runs']}");
		runs.setComponentClass(RunEditor.class);
		
		WorkspaceClassViewNode years = new WorkspaceClassViewNode("runYears",setupNode);
		years.setName("#{localizedStrings['is.idega.idegaweb.marathon']['Years']}");
		years.setComponentClass(RunYearEditor.class);
		
		WorkspaceClassViewNode distances = new WorkspaceClassViewNode("runDistances",setupNode);
		distances.setName("#{localizedStrings['is.idega.idegaweb.marathon']['Distances']}");
		distances.setComponentClass(RunDistanceEditor.class);

		WorkspaceClassViewNode charities = new WorkspaceClassViewNode("charities",setupNode);
		charities.setName("#{localizedStrings['is.idega.idegaweb.marathon']['Charities']}");
		charities.setComponentClass(Charities.class);

		WorkspaceClassViewNode runCharities = new WorkspaceClassViewNode("runCharities",setupNode);
		runCharities.setName("#{localizedStrings['is.idega.idegaweb.marathon']['RunCharities']}");
		runCharities.setComponentClass(CharitiesRunYear.class);
		
		return marathonNode;
	}


}
