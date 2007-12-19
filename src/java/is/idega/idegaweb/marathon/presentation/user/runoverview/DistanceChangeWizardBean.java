package is.idega.idegaweb.marathon.presentation.user.runoverview;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/12/19 21:49:49 $ by $Author: civilis $
 *
 */
public class DistanceChangeWizardBean {
	
	private String distanceId;
	private boolean wizardMode;

	public String getDistanceId() {
		return distanceId;
	}

	public void setDistanceId(String distanceId) {
		this.distanceId = distanceId;
	}

	public boolean isWizardMode() {
		return wizardMode;
	}

	public void setWizardMode(boolean wizardMode) {
		this.wizardMode = wizardMode;
	}
}