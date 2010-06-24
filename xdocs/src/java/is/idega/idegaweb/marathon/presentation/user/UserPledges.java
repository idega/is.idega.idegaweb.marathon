package is.idega.idegaweb.marathon.presentation.user;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Pledge;
import is.idega.idegaweb.marathon.data.PledgeHome;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.presentation.RunBlock;

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
import com.idega.presentation.text.Heading2;
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

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString(KEY_PREFIX
				+ "user_pledges", "User pledges"));
		headerLayer.add(heading);
		Heading2 subheading = new Heading2(
				this.iwrb
						.getLocalizedString(KEY_PREFIX
								+ "user_pledges_subheading",
								"Here you can see the amounts people have pledged on your behalf."));
		headerLayer.add(subheading);

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
			runRegistrations = getRunBusiness(iwc).getParticipantsByUser(
					iwc.getCurrentUser());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (runRegistrations != null && !runRegistrations.isEmpty()) {
			Iterator regIt = runRegistrations.iterator();
			while (regIt.hasNext()) {
				Participant participant = (Participant) regIt.next();
				Group yearGroup = participant.getRunYearGroup();
				Year year = null;
				Run run = null;
				try {
					year = ConverterUtility.getInstance().convertGroupToYear(
							yearGroup);
					run = ConverterUtility.getInstance().convertGroupToRun(
							participant.getRunTypeGroup());
				} catch (FinderException e) {
					// Run not found
				}

				boolean showYear = false;
				if (year != null && run != null) {
					if (run.getCurrentRegistrationYear() != null) {
						if (run.getCurrentRegistrationYear().equals(year)) {
							showYear = true;
						}
					} else {
						IWTimestamp now = new IWTimestamp();
						if (year.getName().equals(now.getDateString("yyyy"))) {
							showYear = true;
						}
					}
				}

				if (year != null
						&& showYear
						&& year.isSponsoredRun()
						&& (participant.isSponsoredRunner() || participant
								.isCustomer())
						&& participant.getCharityId() != null) {
					Pledge pledge = createPledge();
					/**
					 * @TODO Get info from bank about how this is supposed to be and then insert code again.
					 */
					/*if (pledge != null) {
						pledge.setCardholderName(getResourceBundle()
								.getLocalizedString(KEY_PREFIX + "sponsor",
										"Sponsor"));
						int distanceInKms = participant.getRunDistanceGroup()
								.getDistanceInKms();
						if (participant.isSponsoredRunner()) {
							pledge.setAmountPayed(String.valueOf(year
									.getPledgedBySponsorGroupPerKilometer()
									* distanceInKms));
						} else if (participant.isCustomer()) {
							pledge.setAmountPayed(String.valueOf(year
									.getPledgedBySponsorPerKilometer()
									* distanceInKms));
						}
						pledge.setOrganizationalID(participant.getCharityId());
						pledges.add(pledge);
					}*/
				}
			}
		}

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.setStyleClass("pledgesPledger");
		cell.add(new Text(getResourceBundle().getLocalizedString(
				KEY_PREFIX + "pledger", "Pledger")));

		cell = row.createHeaderCell();
		cell.setStyleClass("pledgesCharity");
		cell.add(new Text(getResourceBundle().getLocalizedString(
				KEY_PREFIX + "charity", "Charity")));

		cell = row.createHeaderCell();
		cell.setStyleClass("pledgesDate");
		cell.add(new Text(getResourceBundle().getLocalizedString(
				KEY_PREFIX + "date", "Date")));

		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setStyleClass("pledgesAmount");
		cell.add(new Text(getResourceBundle().getLocalizedString(
				KEY_PREFIX + "amount", "Amount")));

		group = table.createBodyRowGroup();
		int iRow = 1;

		Iterator iter = pledges.iterator();
		while (iter.hasNext()) {
			Pledge userPledge = (Pledge) iter.next();

			boolean showEntry = true;
			String timeStampString = "";
			if (userPledge.getPaymentTimestamp() != null) {
				IWTimestamp created = new IWTimestamp(userPledge
						.getPaymentTimestamp());
				IWTimestamp now = new IWTimestamp();

				if (now.getYear() != created.getYear()) {
					showEntry = false;
				}
				
				timeStampString = created.getLocaleDate(iwc.getCurrentLocale(),
						IWTimestamp.SHORT);
			}

			if (showEntry) {
				row = group.createRow();
				if (iRow == 1) {
					row.setStyleClass("firstRow");
				} else if (!iter.hasNext()) {
					row.setStyleClass("lastRow");
				}

				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.setStyleClass("pledgesPledger");
				cell.add(new Text(userPledge.getCardholderName()));

				String charityString = "";
				try {
					Charity charity = getCharityBusiness(iwc)
							.getCharityByOrganisationalID(
									userPledge.getOrganizationalID());
					charityString = charity.getName();
				} catch (Exception e) {
					// charity not found
					System.err.println(e.getMessage());
				}

				cell = row.createCell();
				cell.setStyleClass("pledgesCharity");
				cell.add(new Text(charityString));

				cell = row.createCell();
				cell.setStyleClass("pledgesDate");
				cell.add(new Text(timeStampString));

				cell = row.createCell();
				cell.setStyleClass("lastColumn");
				cell.setStyleClass("pledgesAmount");
				cell.add(new Text(
						formatAmount(iwc, userPledge.getAmountPayed())));

				boolean addNonBrakingSpace = true;

				if (addNonBrakingSpace) {
					cell.add(Text.getNonBrakingSpace());
				}

				if (iRow % 2 == 0) {
					row.setStyleClass("evenRow");
				} else {
					row.setStyleClass("oddRow");
				}
				iRow++;
			}
		}
		return table;
	}

	private Pledge createPledge() {
		Pledge pledge = null;
		try {
			PledgeHome pledgeHome = (PledgeHome) IDOLookup
					.getHome(Pledge.class);
			pledge = pledgeHome.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pledge;
	}

	protected Collection getPledges(IWContext iwc) {
		try {
			Collection pledges = getPledgeBusiness(iwc).getPledgesForUser(
					iwc.getCurrentUserId());
			if (pledges == null) {
				pledges = new ArrayList();
			}
			return pledges;
		} catch (RemoteException re) {
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