package is.idega.idegaweb.marathon.presentation.user.runoverview;

import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.presentation.RunBlock;

import java.util.Collection;
import java.util.Iterator;


import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;

public class UIUserRunOverviewList extends RunBlock {

	private final static String KEY_PREFIX = "userRunOverview.";
	public final static String PARTICIPANT_PARAM = "urol_prtcpnt";

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
		
		Form form = new Form();
		layer.add(getRunOverviewTable(iwc));
		form.add(layer);
		add(form);
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
		cell.add(new Text(getResourceBundle().getLocalizedString(KEY_PREFIX + "charity", "Charity")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("runnerChangeDistanceCellHeader");
		cell.setStyleClass("lastColumn");
		cell.add(new Text(getResourceBundle().getLocalizedString("blahblah", "Change distance")));
		
		group = table.createBodyRowGroup();
		int iRow = 1;
		
		Collection participants = null;
		try {
			participants = getRunBusiness(iwc).getParticipantsByUser(iwc.getCurrentUser());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (participants != null && !participants.isEmpty()) {
			Iterator iter = participants.iterator();
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
				cell.setStyleClass("runnerRun");
				cell.add(new Text(getResourceBundle().getLocalizedString(participant.getRunTypeGroup().getName(),participant.getRunTypeGroup().getName()) + " " + participant.getRunYearGroup().getName()));
				
				cell = row.createCell();
				cell.setStyleClass("runnerDistance");
				cell.add(new Text(getResourceBundle().getLocalizedString(participant.getRunDistanceGroup().getName())));
				
				cell = row.createCell();
				cell.setStyleClass("runnerParticipantNumber");
				cell.add(new Text(String.valueOf(participant.getParticipantNumber())));
				
				String charityString = "";
				if (participant.getCharityId() != null) {
					try {
						Charity charity = getCharityBusiness(iwc).getCharityByOrganisationalID(participant.getCharityId());
						charityString = charity.getName();
					} catch (Exception e) {
						//charity not found
						System.err.println(e.getMessage());
					}
				}
				
				cell = row.createCell();
				cell.setStyleClass("runnerCharity");
				cell.add(new Text(charityString));
	
				boolean addNonBrakingSpace = true;
	
				if (addNonBrakingSpace) {
					cell.add(Text.getNonBrakingSpace());
				}
				
				cell = row.createCell();
				cell.setStyleClass("runnerChangeDistanceCell");
				cell.setStyleClass("lastColumn");
				Link link = getLink(localize("edit", "Edit"));
				link.addParameter(PARTICIPANT_PARAM, participant.getPrimaryKey().toString());
				cell.add(link);
	
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
