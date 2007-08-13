package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.Participant;

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

public class UserRunOverview extends RunBlock {

	private final static String KEY_PREFIX = "userRunOverview.";

	public void main(IWContext iwc) throws Exception {
		if (!iwc.isLoggedOn()) {
			add(new Text("No user logged on..."));
			return;
		}

		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("listElement");
		layer.setID("userRunOverview");
		
		Layer headerLayer = new Layer(Layer.DIV);
		headerLayer.setStyleClass("header");
		layer.add(headerLayer);

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString(KEY_PREFIX + "user_run_overview", "Run overview"));
		headerLayer.add(heading);

		layer.add(getRunOverviewTable(iwc));
		add(layer);
	}
	
	private Table2 getRunOverviewTable(IWContext iwc) {
		Table2 table = new Table2();
		table.setStyleClass("listTable");
		table.setStyleClass("ruler");
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
		
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.setStyleClass("runnerName");
		cell.add(new Text(getResourceBundle().getLocalizedString(KEY_PREFIX + "name", "Name")));	
		
		cell = row.createHeaderCell();
		cell.setStyleClass("runnerRun");
		cell.add(new Text(getResourceBundle().getLocalizedString(KEY_PREFIX + "run", "Run")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("runnerDistance");
		cell.add(new Text(getResourceBundle().getLocalizedString(KEY_PREFIX + "distance", "Distance")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("runnerParticipantNumber");
		cell.add(new Text(getResourceBundle().getLocalizedString(KEY_PREFIX + "participant_number", "Participant number")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("runnerCharity");
		cell.setStyleClass("lastColumn");
		cell.add(new Text(getResourceBundle().getLocalizedString(KEY_PREFIX + "charity", "Charity")));
		
		group = table.createBodyRowGroup();
		int iRow = 1;
		
		Collection runRegistrations = null;
		try {
			runRegistrations = getRunBusiness(iwc).getParticipantsByUser(iwc.getCurrentUser());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (runRegistrations != null && !runRegistrations.isEmpty()) {
			Iterator iter = runRegistrations.iterator();
			while (iter.hasNext()) {
				row = group.createRow();
				Participant participant = (Participant)iter.next();
				if (iRow == 1) {
					row.setStyleClass("firstRow");
				}
				else if (!iter.hasNext()) {
					row.setStyleClass("lastRow");
				}
	
				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.setStyleClass("runnerName");
				cell.add(new Text(participant.getUser().getName()));
	
				cell = row.createCell();
				cell.setStyleClass("runnerRun");
				cell.add(new Text(participant.getRunYearGroup().getName()));
				
				cell = row.createCell();
				cell.setStyleClass("runnerDistance");
				cell.add(new Text(participant.getRunDistanceGroup().getName()));
				
				cell = row.createCell();
				cell.setStyleClass("runnerParticipantNumber");
				cell.add(new Text(String.valueOf(participant.getParticipantNumber())));
				
				String charityString = "";
				try {
					Charity charity = getCharityBusiness(iwc).getCharityByOrganisationalID(participant.getCharityId());
					charityString = charity.getName();
				} catch (Exception e) {
					//charity not found
					System.err.println(e.getMessage());
				}
				
				cell = row.createCell();
				cell.setStyleClass("runnerCharity");
				cell.setStyleClass("lastColumn");
				cell.add(new Text(charityString));
	
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
		}
		return table;
	}
}
