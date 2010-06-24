package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class RunBusinessHomeImpl extends IBOHomeImpl implements RunBusinessHome {
	public Class getBeanInterfaceClass() {
		return RunBusiness.class;
	}

	public RunBusiness create() throws CreateException {
		return (RunBusiness) super.createIBO();
	}
}