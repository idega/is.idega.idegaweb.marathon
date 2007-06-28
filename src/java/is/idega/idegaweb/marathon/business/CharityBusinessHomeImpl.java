package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class CharityBusinessHomeImpl extends IBOHomeImpl implements CharityBusinessHome {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4861723826577856207L;

	public Class getBeanInterfaceClass() {
		return CharityBusiness.class;
	}

	public CharityBusiness create() throws CreateException {
		return (CharityBusiness) super.createIBO();
	}
}