package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.data.Charity;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

public class Charities extends RunBlock {
	//private static final String PRM_ACTION = "marathon_prm_action";
	private static final String PARAMETER_CHARITY = "charity_id";

	private static final String PARAMETER_NAME = "name";
	private static final String PARAMETER_ORGANIZATIONAL_ID = "org_id";
	private static final String PARAMETER_DESCRIPTION = "description";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;

	public void main(IWContext iwc) throws Exception {
		init(iwc);
	}

	protected void init(IWContext iwc) throws Exception {
		try {
		switch (parseAction(iwc)) {
		case ACTION_VIEW:
			showList(iwc);
			break;

		case ACTION_NEW:
			showEditor(iwc);
			break;

		case ACTION_EDIT:
			showEditor(iwc);
			break;

		case ACTION_SAVE:
			save(iwc);
			showList(iwc);
			break;
		}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		
		return ACTION_VIEW;
	}

	public void save(IWContext iwc) throws java.rmi.RemoteException,
			CreateException, NumberFormatException, FinderException {
		String charityID = iwc.getParameter(PARAMETER_CHARITY);
		Charity charity = null;
		if (charityID == null) {
			charity = this.getRunBusiness(iwc).getCharityBusiness()
					.getCharityHome().create();
		} else {
			charity = this.getRunBusiness(iwc).getCharityBusiness()
					.getCharityHome().findByPrimaryKey(new Integer(charityID));
		}

		if (charity != null) {
			charity.setName(iwc.getParameter(PARAMETER_NAME));
			charity.setOrganizationalID(iwc
					.getParameter(PARAMETER_ORGANIZATIONAL_ID));
			charity.setDescription(iwc.getParameter(PARAMETER_DESCRIPTION));
			charity.store();
		}
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
		cell = row.createHeaderCell();
		cell.setCellHorizontalAlignment(Table2.HORIZONTAL_ALIGNMENT_LEFT);
		cell.add(new Text(localize("description", "Description")));
		
		group = table.createBodyRowGroup();

		if (charities != null && !charities.isEmpty()) {
			Iterator it = charities.iterator();
			while (it.hasNext()) {
				Charity charity = (Charity) it.next();
				Link edit = new Link(getEditIcon(localize("edit", "Edit")));
				edit.addParameter(PARAMETER_CHARITY, charity.getPrimaryKey()
						.toString());
				edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);

				row = group.createRow();
				row.createCell().add(new Text(charity.getName()));
				row.createCell().add(new Text(charity.getOrganizationalID()));
				row.createCell().add(new Text(charity.getDescription()));
				row.createCell().add(edit);
			}
		}

		form.add(table);
		form.add(new Break());
		SubmitButton newLink = (SubmitButton) getButton(new SubmitButton(
				localize("new_charity", "New charity"), PARAMETER_ACTION,
				String.valueOf(ACTION_NEW)));
		form.add(newLink);
		add(form);
	}

	public void showEditor(IWContext iwc) throws java.rmi.RemoteException {
		String charityID = iwc.getParameter(PARAMETER_CHARITY);
		Form form = new Form();
		form.maintainParameter(PARAMETER_CHARITY);

		TextInput nameInput = new TextInput(PARAMETER_NAME);

		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		Label label = new Label(localize("charity_tab.name", "Name"), nameInput);
		layer.add(label);
		layer.add(nameInput);
		form.add(layer);
		form.add(new Break());

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		TextInput orgIDInput = new TextInput(PARAMETER_ORGANIZATIONAL_ID);
		Label orgIDLabel = new Label(localize("charity_tab.org_id",
				"Organizational id"), orgIDInput);
		layer.add(orgIDLabel);
		layer.add(orgIDInput);
		form.add(layer);
		form.add(new Break());

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		TextArea descInput = new TextArea(PARAMETER_DESCRIPTION, 100, 10);
		descInput.setMaximumCharacters(1000);
		Label descLabel = new Label(localize("charity_tab.description",
				"Description"), descInput);
		layer.add(descLabel);
		layer.add(descInput);
		form.add(layer);
		form.add(new Break());

		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize(
				"save", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(
				localize("cancel", "Cancel"), PARAMETER_ACTION, String
						.valueOf(ACTION_VIEW)));

		form.add(save);
		form.add(cancel);

		if (charityID != null) {
			Charity selectedCharity = null;
			try {
				selectedCharity = this.getRunBusiness(iwc).getCharityBusiness()
						.getCharityHome().findByPrimaryKey(
								new Integer(charityID));
				nameInput.setValue(selectedCharity.getName());
				orgIDInput.setValue(selectedCharity.getOrganizationalID());
				descInput.setValue(selectedCharity.getDescription());
			} catch (FinderException e) {
			}
		}

		add(form);
	}

}