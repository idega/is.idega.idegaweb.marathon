package is.idega.idegaweb.marathon.webservice.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;
import javax.ejb.FinderException;
import java.util.Collection;

public class WebServiceLoginSessionHomeImpl extends IDOFactory implements
		WebServiceLoginSessionHome {
	public Class getEntityInterfaceClass() {
		return WebServiceLoginSession.class;
	}

	public WebServiceLoginSession create() throws CreateException {
		return (WebServiceLoginSession) super.createIDO();
	}

	public WebServiceLoginSession findByPrimaryKey(Object pk)
			throws FinderException {
		return (WebServiceLoginSession) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByUser(User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((WebServiceLoginSessionBMPBean) entity)
				.ejbFindAllByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public WebServiceLoginSession findByUniqueID(String id)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((WebServiceLoginSessionBMPBean) entity)
				.ejbFindByUniqueID(id);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}