package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class RunCategoryBusinessHomeImpl extends IBOHomeImpl implements RunCategoryBusinessHome {
	public Class getBeanInterfaceClass() {
		return RunCategoryBusiness.class;
	}

	public RunCategoryBusiness create() throws CreateException {
		return (RunCategoryBusiness) super.createIBO();
	}
}