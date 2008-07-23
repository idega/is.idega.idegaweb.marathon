package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.data.Charity;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;

public class Charities extends RunBlock {
	private static final String PRM_ACTION = "marathon_prm_action";
	private static final String PARAMETER_CHARITY = "charity_id";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE_NEW = 4;
	private static final int ACTION_SAVE_EDIT = 5;

	public void main(IWContext iwc) throws Exception {
		init(iwc);
	}

	protected void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
		case ACTION_VIEW:
			showList(iwc);
			break;

		/*case ACTION_NEW:
			showNewEditor(iwc);
			break;

		case ACTION_EDIT:
			showEditor(iwc);
			break;

		case ACTION_SAVE_NEW:
			saveNew(iwc);
			showList(iwc);
			break;

		case ACTION_SAVE_EDIT:
			saveEdit(iwc);
			showList(iwc);
			break;*/
		}
	}

	protected int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PRM_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PRM_ACTION));
		}
		return ACTION_VIEW;
	}

	public void showList(IWContext iwc) throws RemoteException, FinderException {
		Form form = new Form();

		Table2 table = new Table2();
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);

		Collection charities = getRunBusiness(iwc).getCharityBusiness()
				.getCharityHome().findAllCharities();
		
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setCellHorizontalAlignment(Table2.HORIZONTAL_ALIGNMENT_LEFT);
		cell.add(new Text(localize("name", "Name")));
		cell = row.createHeaderCell();
		cell.setCellHorizontalAlignment(Table2.HORIZONTAL_ALIGNMENT_LEFT);
		cell.add(new Text(localize("organization_id", "Organization id")));
		
		
		if (charities != null && !charities.isEmpty()) {
			Iterator it = charities.iterator();
			while (it.hasNext()) {
				Charity charity = (Charity) it.next();
				Link edit = new Link(getEditIcon(localize("edit", "Edit")));
				edit.addParameter(PARAMETER_CHARITY, charity.getPrimaryKey().toString());
				edit.addParameter(PRM_ACTION, ACTION_EDIT);

				row = group.createRow();
				row.createCell().add(new Text(charity.getName()));
				row.createCell().add(new Text(charity.getOrganizationalID()));
				
			}
		}
	}
}