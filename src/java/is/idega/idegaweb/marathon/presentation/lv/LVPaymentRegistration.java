package is.idega.idegaweb.marathon.presentation.lv;

import com.idega.presentation.IWContext;
import com.idega.util.LocaleUtil;

import is.idega.idegaweb.marathon.presentation.RunBlock;

public class LVPaymentRegistration extends RunBlock {
	private boolean isIcelandic = false;

	public void main(IWContext iwc) throws Exception {
		if (!iwc.isInEditMode()) {
			this.isIcelandic = iwc.getCurrentLocale().equals(
					LocaleUtil.getIcelandicLocale());
			loadCurrentStep(iwc, parseAction(iwc));
		}
	}

	private void loadCurrentStep(IWContext iwc, int action) {
		
	}
}