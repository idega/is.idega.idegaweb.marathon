package is.idega.idegaweb.marathon.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface YearHome extends IDOHome {
	public Year create() throws CreateException;

	public Year findByPrimaryKey(Object pk) throws FinderException;
}