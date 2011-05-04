package is.idega.idegaweb.marathon.webservice.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import com.idega.user.data.User;
import javax.ejb.FinderException;
import java.util.Collection;

public interface WebServiceLoginSessionHome extends IDOHome {
	public WebServiceLoginSession create() throws CreateException;

	public WebServiceLoginSession findByPrimaryKey(Object pk)
			throws FinderException;

	public Collection findAllByUser(User user) throws FinderException;

	public WebServiceLoginSession findByUniqueID(String id)
			throws FinderException;
}