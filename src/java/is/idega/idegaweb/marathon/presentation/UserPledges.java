package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Pledge;
import is.idega.idegaweb.marathon.data.PledgeHome;
import is.idega.idegaweb.marathon.data.Year;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Text;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

public class UserPledges extends RunBlock {

	private final static String KEY_PREFIX = "userPledges.";

	public void main(IWContext iwc) throws Exception {
		if (!iwc.isLoggedOn()) {
			add(new Text("No user logged on..."));
			return;
		}

		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("listElement");
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
		table.setStyleClass("listTable");
		table.setStyleClass("ruler");
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
		
		Collection pledges = getPledges(iwc);
		Collection runRegistrations = null;
		try {
			runRegistrations = getRunBusiness(iwc).getParticipantsByUser(iwc.getCurrentUser());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (runRegistrations != null && !runRegistrations.isEmpty()) {
			Iterator regIt = runRegistrations.iterator();
			while (regIt.hasNext()) {
				Participant participant = (Participant)regIt.next();
				Group yearGroup = participant.getRunYearGroup();
				Year year = null;
				try {
					year = ConverterUtility.getInstance().convertGroupToYear(yearGroup);
				} catch (FinderException e) {
					//Run not found
				}
				if (year != null && year.isSponsoredRun() && (participant.isSponsoredRunner() || participant.isCustomer())) {
					Pledge pledge = createPledge();
					if (pledge != null) {
						pledge.setCardholderName(getResourceBundle().getLocalizedString(KEY_PREFIX + "sponsor", "Sponsor"));
						int distanceInKms = participant.getRunDistanceGroup().getDistanceInKms();
						if (participant.isSponsoredRunner()) {
							pledge.setAmountPayed(String.valueOf(year.getPledgedBySponsorGroupPerKilometer()*distanceInKms));
						} else if (participant.isCustomer()) {
							pledge.setAmountPayed(String.valueOf(year.getPledgedBySponsorPerKilometer()*distanceInKms));
						}
						pledge.setOrganizationalID(participant.getCharityId());
						pledges.add(pledge);
					}
				}
			}
		}

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
			
			String timeStampString = "";
			if (userPledge.getPaymentTimestamp() != null) {
				IWTimestamp created = new IWTimestamp(userPledge.getPaymentTimestamp());
				timeStampString = created.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT);
			}
			
			cell = row.createCell();
			cell.setStyleClass("pledgesDate");
			cell.add(new Text(timeStampString));
			
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

	private Pledge createPledge() {
		Pledge pledge = null;
		try {
			PledgeHome pledgeHome = (PledgeHome) IDOLookup.getHome(Pledge.class);
			pledge = pledgeHome.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pledge;
	}

	protected Collection getPledges(IWContext iwc) {
		try {
			Collection pledges = getPledgeBusiness(iwc).getPledgesForUser(iwc.getCurrentUserId());
			if (pledges == null)  {
				pledges = new ArrayList();
			}
			return pledges;
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