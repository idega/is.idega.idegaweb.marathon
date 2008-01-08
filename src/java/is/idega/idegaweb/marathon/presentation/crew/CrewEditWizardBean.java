package is.idega.idegaweb.marathon.presentation.crew;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/01/08 19:20:24 $ by $Author: civilis $
 *
 */
public class CrewEditWizardBean {

	private Integer mode;
	public static final Integer newCrewMode = new Integer(1);
	public static final Integer editCrewMode = new Integer(2);
	Integer crewEditId;

	public Integer getCrewEditId() {
		return crewEditId;
	}

	public void setCrewEditId(Integer crewEditId) {
		this.crewEditId = crewEditId;
	}
	
	public Integer getMode() {
		return mode == null ? new Integer(0) : mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}
	
	public boolean isNewCrewMode() {
		
		return newCrewMode.equals(getMode());
	}
	
	public boolean isEditCrewMode() {
		
		return editCrewMode.equals(getMode());
	}
}