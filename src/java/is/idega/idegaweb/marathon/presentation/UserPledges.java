package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.Pledge;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;

public class UserPledges extends RunBlock {

	private final static String KEY_PREFIX = "userPledges.";

	public void main(IWContext iwc) throws Exception {
		if (!iwc.isLoggedOn()) {
			add(new Text("No user logged on..."));
			return;
		}

		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("pledgeElement");
		layer.setID("userPledges");
		
		Layer headerLayer = new Layer(Layer.DIV);
		headerLayer.setStyleClass("header");
		layer.add(headerLayer);

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString(KEY_PREFIX + "user_pledges", "User pledges"));
		headerLayer.add(heading);

		layer.add(getPledgeTable(iwc));
		add(layer);
	}
	
	private Table2 getPledgeTable(IWContext iwc) {
		Table2 table = new Table2();
		table.setStyleClass("pledgeTable");
		table.setStyleClass("ruler");
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
		
		Collection pledges = getPledges(iwc);

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.setStyleClass("pledgesPledger");
		cell.add(new Text(getResourceBundle().getLocalizedString(KEY_PREFIX + "pledger", "Pledger")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("pledgesCharity");
		cell.add(new Text(getResourceBundle().getLocalizedString(KEY_PREFIX + "charity", "Charity")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("pledgesDate");
		cell.add(new Text(getResourceBundle().getLocalizedString(KEY_PREFIX + "date", "Date")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setStyleClass("pledgesAmount");
		cell.add(new Text(getResourceBundle().getLocalizedString(KEY_PREFIX + "amount", "Amount")));
		
		group = table.createBodyRowGroup();
		int iRow = 1;
		
		Iterator iter = pledges.iterator();
		while (iter.hasNext()) {
			row = group.createRow();
			Pledge userPledge = (Pledge) iter.next();
			if (iRow == 1) {
				row.setStyleClass("firstRow");
			}
			else if (!iter.hasNext()) {
				row.setStyleClass("lastRow");
			}

			IWTimestamp created = new IWTimestamp(userPledge.getPaymentTimestamp());
			
			cell = row.createCell();
			cell.setStyleClass("firstColumn");
			cell.setStyleClass("pledgesPledger");
			cell.add(new Text(userPledge.getCardholderName()));

			String charityString = "";
			try {
				Charity charity = getCharityBusiness(iwc).getCharityByOrganisationalID(userPledge.getOrganizationalID());
				charityString = charity.getName();
			} catch (Exception e) {
				//charity not found
				System.err.println(e.getMessage());
			}
			
			cell = row.createCell();
			cell.setStyleClass("pledgesCharity");
			cell.add(new Text(charityString));
			
			cell = row.createCell();
			cell.setStyleClass("pledgesDate");
			cell.add(new Text(created.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)));
			
			cell = row.createCell();
			cell.setStyleClass("lastColumn");
			cell.setStyleClass("pledgesAmount");
			cell.add(new Text(formatAmount(iwc, userPledge.getAmountPayed())));

			boolean addNonBrakingSpace = true;

			if (addNonBrakingSpace) {
				cell.add(Text.getNonBrakingSpace());
			}

			if (iRow % 2 == 0) {
				row.setStyleClass("evenRow");
			}
			else {
				row.setStyleClass("oddRow");
			}
			iRow++;
		}
		return table;
	}

	protected Collection getPledges(IWContext iwc) {
		try {
			return getPledgeBusiness(iwc).getPledges();
		}
		catch (RemoteException re) {
			log(re);
			return new ArrayList();
		}
	}

	private String formatAmount(IWContext iwc, String amount) {
		try {
			return formatAmount(iwc, Float.parseFloat(amount));
		} catch (Exception e) {
			e.printStackTrace();
			return amount;
		}
	}
	private String formatAmount(IWContext iwc, float amount) {
		return NumberFormat.getInstance(iwc.getCurrentLocale()).format(amount);
	}
}