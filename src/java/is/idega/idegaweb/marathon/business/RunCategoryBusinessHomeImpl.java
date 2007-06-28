package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class RunCategoryBusinessHomeImpl extends IBOHomeImpl implements RunCategoryBusinessHome {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -655359167974718783L;

	public Class getBeanInterfaceClass() {
		return RunCategoryBusiness.class;
	}

	public RunCategoryBusiness create() throws CreateException {
		return (RunCategoryBusiness) super.createIBO();
	}
}