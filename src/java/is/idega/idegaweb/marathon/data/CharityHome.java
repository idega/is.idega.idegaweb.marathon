package is.idega.idegaweb.marathon.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface CharityHome extends IDOHome {
	public Charity create() throws CreateException;

	public Charity findByPrimaryKey(Object pk) throws FinderException;
	public java.util.Collection findAllCharities() throws javax.ejb.FinderException;
}