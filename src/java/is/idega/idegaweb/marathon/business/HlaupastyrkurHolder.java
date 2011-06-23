package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HlaupastyrkurHolder {
	private Runner runner;
	private String login;
	private String password;
	
	private boolean isTeam;
	
	private List team;
	
	public boolean isTeam() {
		return isTeam;
	}
	public void setTeam(boolean isTeam) {
		this.isTeam = isTeam;
	}
	public Runner getRunner() {
		return runner;
	}
	public void setRunner(Runner runner) {
		this.runner = runner;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
	
	public void addToTeam(TeamMember member) {
		isTeam = true;
		if (team == null) {
			team = new ArrayList();
		}
		
		team.add(member);
	}
	
	public TeamMember[] getMembers() {
		if (team != null && !team.isEmpty()) {
			TeamMember ret[] = new TeamMember[team.size()];
			Iterator it = team.iterator();
			int counter = 0;
			while (it.hasNext()) {
				ret[counter++] = (TeamMember) it.next();
			}
			
			return ret;
		}
		
		return null;
	}
}