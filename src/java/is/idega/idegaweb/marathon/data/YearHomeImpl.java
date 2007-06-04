package is.idega.idegaweb.marathon.data;


import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;

public class YearHomeImpl extends IDOFactory implements YearHome {
	public Class getEntityInterfaceClass() {
		return Year.class;
	}

	public Year create() throws CreateException {
		return (Year) super.createIDO();
	}

	public Year findByPrimaryKey(Object pk) throws FinderException {
		return (Year) super.findByPrimaryKeyIDO(pk);
	}
}