package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class PledgeBusinessHomeImpl extends IBOHomeImpl implements PledgeBusinessHome {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -8555730524234407364L;

	public Class getBeanInterfaceClass() {
		return PledgeBusiness.class;
	}

	public PledgeBusiness create() throws CreateException {
		return (PledgeBusiness) super.createIBO();
	}
}