package is.idega.idegaweb.marathon.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface ShirtSizeHome extends IDOHome {
	public ShirtSize create() throws CreateException;

	public ShirtSize findByPrimaryKey(Object pk) throws FinderException;
}