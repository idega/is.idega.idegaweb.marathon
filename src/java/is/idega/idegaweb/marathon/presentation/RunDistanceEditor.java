package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.util.ListUtil;
import com.idega.util.MiscUtil;

public class RunDistanceEditor extends RunBlock {
	
	private static final String PARAMETER_ACTION = "marathon_prm_action";
	private static final String PARAMETER_DISTANCE = "prm_distance";
	private static final String PARAMETER_USE_CHIP = "use_chip";
	private static final String PARAMETER_FAMILY_DISCOUNT = "family_discount";
	private static final String PARAMETER_ALLOWS_GROUPS = "allows_groups";
	private static final String PARAMETER_TRANSPORT_OFFERED = "transport_offered";
	private static final String PARAMETER_PRICE_ISK = "price_isk";
	private static final String PARAMETER_PRICE_EUR = "price_eur";
	private static final String PARAMETER_CHILDREN_PRICE_ISK = "children_price_isk";
	private static final String PARAMETER_CHILDREN_PRICE_EUR = "children_price_eur";
	private static final String PARAMETER_PRICE_FOR_TRANSPORT_ISK = "price_for_transport_isk";
	private static final String PARAMETER_PRICE_FOR_TRANSPORT_EUR = "price_for_transport_eur";
	private static final String PARAMETER_NUMBER_OF_SPLITS = "number_of_splits";
	private static final String PARAMETER_SHIRT_SIZES_PER_RUN = "shirt_sizes_per_run";
	private static final String PARAMETER_MARATHON_PK = "prm_run_pk";
	private static final String PARAMETER_MARATHON_YEAR_PK = "prm_run_year_pk";
	private static final String PARAMETER_MARATHON_DISTANCE_PK = "prm_run_distance_pk";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;
	
	public void main(IWContext iwc) throws Exception {
		init(iwc);
	}
	
	protected void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				showList(iwc);
				break;

			case ACTION_EDIT:
				String distanceID = iwc.getParameter(PARAMETER_MARATHON_DISTANCE_PK);
				showEditor(iwc, distanceID);
				break;

			case ACTION_NEW:
				showEditor(iwc, null);
				break;

			case ACTION_SAVE:
				save(iwc);
				showList(iwc);
				break;
		}
	}

	public void showList(IWContext iwc) throws RemoteException {
		Form form = new Form();
		
		Table2 table = new Table2();
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
				
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setCellHorizontalAlignment(Table2.HORIZONTAL_ALIGNMENT_LEFT);

		Collection runs = getRunBusiness(iwc).getRuns();
		Iterator runIt = runs.iterator();
		DropdownMenu runDropDown = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_MARATHON_PK)); 
		runDropDown.addMenuElement("", localize("select_run","Select run"));
		while (runIt.hasNext()) {
			Group run = (Group)runIt.next();
			runDropDown.addMenuElement(run.getPrimaryKey().toString(), localize(run.getName(),run.getName()));
		}
		runDropDown.setToSubmit();
		cell.add(runDropDown);
		
		if (iwc.isParameterSet(PARAMETER_MARATHON_PK)) {
			String runID = iwc.getParameter(PARAMETER_MARATHON_PK);
			runDropDown.setSelectedElement(runID);
			Group run = getRunBusiness(iwc).getRunGroupByGroupId(Integer.valueOf(runID));
			String[] yearType = {IWMarathonConstants.GROUP_TYPE_RUN_YEAR};
			Collection years = run.getChildGroups(yearType,true); 
			Iterator yearIt = years.iterator();
			DropdownMenu yearDropDown = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_MARATHON_YEAR_PK)); 
			yearDropDown.addMenuElement("", localize("select_year","Select year"));
			while (yearIt.hasNext()) {
				Group year = (Group)yearIt.next();
				yearDropDown.addMenuElement(year.getPrimaryKey().toString(), localize(year.getName(),year.getName()));
			}
			yearDropDown.setToSubmit();
			group.createRow().createCell().add(yearDropDown);
			
			Collection distances = null;
			if (iwc.isParameterSet(PARAMETER_MARATHON_YEAR_PK)) {
				String yearID = iwc.getParameter(PARAMETER_MARATHON_YEAR_PK);
				yearDropDown.setSelectedElement(yearID);
				Group selectedYear = getRunBusiness(iwc).getRunGroupByGroupId(Integer.valueOf(yearID));
			    String[] distanceType = {IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE};
			    distances = selectedYear.getChildGroups(distanceType,true);
			    group.createRow().createCell().setHeight("20");
				row = group.createRow();
				cell = row.createHeaderCell();
				cell.setCellHorizontalAlignment(Table2.HORIZONTAL_ALIGNMENT_LEFT);
				cell.add(new Text(localize("name", "Name")));
				group = table.createBodyRowGroup();
				int iRow = 1;
				
				if (distances != null) {
					Iterator iter = distances.iterator();
					while (iter.hasNext()) {
						row = group.createRow();
						Group distance = (Group) iter.next();
						try {
							Link edit = new Link(getEditIcon(localize("edit", "Edit")));
							edit.addParameter(PARAMETER_MARATHON_PK, iwc.getParameter(PARAMETER_MARATHON_PK));
							edit.addParameter(PARAMETER_MARATHON_YEAR_PK, selectedYear.getPrimaryKey().toString());
							edit.addParameter(PARAMETER_MARATHON_DISTANCE_PK, distance.getPrimaryKey().toString());
							edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);
							
										
							cell = row.createCell();
							cell.add(new Text(localize(distance.getName(), distance.getName())));
							row.createCell().add(edit);
						}
						catch (Exception ex) {
							ex.printStackTrace();
						}
						iRow++;
					}
				}
			}
		}
		form.add(table);
		form.add(new Break());
		if (iwc.isParameterSet(PARAMETER_MARATHON_YEAR_PK)) {
			SubmitButton newLink = (SubmitButton) getButton(new SubmitButton(localize("new_distance", "New distance"), PARAMETER_ACTION, String.valueOf(ACTION_NEW)));
			form.add(newLink);
		}
		add(form);
	}
	
	public void showEditor(IWContext iwc, String distanceID) throws java.rmi.RemoteException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_MARATHON_PK);
		form.maintainParameter(PARAMETER_MARATHON_YEAR_PK);
		form.maintainParameter(PARAMETER_MARATHON_DISTANCE_PK);

		TextInput distance = new TextInput(PARAMETER_DISTANCE);
		TextInput priceISK = new TextInput(PARAMETER_PRICE_ISK);
		priceISK.setAsFloat("Not a valid price");
		TextInput priceEUR = new TextInput(PARAMETER_PRICE_EUR);
		priceEUR.setAsFloat("Not a valid price");
		TextInput childrenPriceISK = new TextInput(PARAMETER_CHILDREN_PRICE_ISK);
		childrenPriceISK.setAsFloat("Not a valid price");
		TextInput childrenPriceEUR = new TextInput(PARAMETER_CHILDREN_PRICE_EUR);
		childrenPriceEUR.setAsFloat("Not a valid price");
		TextInput priceForTransportISK = new TextInput(PARAMETER_PRICE_FOR_TRANSPORT_ISK);
		priceForTransportISK.setAsFloat("Not a valid price");

		TextInput priceForTransportEUR = new TextInput(PARAMETER_PRICE_FOR_TRANSPORT_EUR);
		priceForTransportEUR.setAsFloat("Not a valid price");
		
		CheckBox useChip = new CheckBox(PARAMETER_USE_CHIP);
		CheckBox familyDiscount = new CheckBox(PARAMETER_FAMILY_DISCOUNT);
		CheckBox allowsGroups = new CheckBox(PARAMETER_ALLOWS_GROUPS);
		CheckBox transportOffered = new CheckBox(PARAMETER_TRANSPORT_OFFERED);
		
		DropdownMenu numberOfSplits = new DropdownMenu(PARAMETER_NUMBER_OF_SPLITS);
		numberOfSplits.addMenuElement(0, "0");
		numberOfSplits.addMenuElement(1, "1");
		numberOfSplits.addMenuElement(2, "2");

		ShirtSizeSelectionBox shirtSizeSelectionBox = new ShirtSizeSelectionBox(PARAMETER_SHIRT_SIZES_PER_RUN);
		shirtSizeSelectionBox.initialize(IWContext.getInstance());
		
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		Label label = new Label(localize("run_tab.distance", "Distance"), distance);
		layer.add(label);
		layer.add(distance);
		form.add(layer);
		form.add(new Break());

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_tab.price_ISK", "Price ISK"), priceISK);
		layer.add(label);
		layer.add(priceISK);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_tab.price_EUR", "Price EUR"), priceEUR);
		layer.add(label);
		layer.add(priceEUR);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_tab.children_price_ISK", "Children price (ISK)"), childrenPriceISK);
		layer.add(label);
		layer.add(childrenPriceISK);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_tab.children_price_EUR", "Children price (EUR)"), childrenPriceEUR);
		layer.add(label);
		layer.add(childrenPriceEUR);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_tab.number_of_splits", "Number of splits"), numberOfSplits);
		layer.add(label);
		layer.add(numberOfSplits);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_tab.use_chip", "Uses chips"), useChip);
		layer.add(label);
		layer.add(useChip);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_tab.family_discount", "Uses family discount"), familyDiscount);
		layer.add(label);
		layer.add(familyDiscount);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_tab.allows_groups", "Allows groups"), allowsGroups);
		layer.add(label);
		layer.add(allowsGroups);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_tab.transport_offered", "Transport offered"), transportOffered);
		layer.add(label);
		layer.add(transportOffered);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_tab.price_for_transport_ISK", "Price for transport (ISK)"), priceForTransportISK);
		layer.add(label);
		layer.add(priceForTransportISK);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_tab.price_for_transport_EUR", "Price for transport (EUR)"), priceForTransportEUR);
		layer.add(label);
		layer.add(priceForTransportEUR);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("run_tab.shirt_sizes", "Shirt sizes"), shirtSizeSelectionBox);
		layer.add(label);
		layer.add(shirtSizeSelectionBox);
		form.add(layer);
		
		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize("cancel", "Cancel"), PARAMETER_ACTION, String.valueOf(ACTION_VIEW)));

		form.add(save);
		form.add(cancel);
		
		if (distanceID != null) {
			Group selectedDistance = getRunBusiness(iwc).getRunGroupByGroupId(Integer.valueOf(distanceID.toString()));
			distance.setValue(selectedDistance.getName());
			distance.setDisabled(true);
			priceISK.setValue(selectedDistance.getMetaData(PARAMETER_PRICE_ISK));
			priceEUR.setValue(selectedDistance.getMetaData(PARAMETER_PRICE_EUR));
			childrenPriceISK.setValue(selectedDistance.getMetaData(PARAMETER_CHILDREN_PRICE_ISK));
			childrenPriceEUR.setValue(selectedDistance.getMetaData(PARAMETER_CHILDREN_PRICE_EUR));
			numberOfSplits.setSelectedElement(selectedDistance.getMetaData(PARAMETER_NUMBER_OF_SPLITS));
			useChip.setChecked(new Boolean(selectedDistance.getMetaData(PARAMETER_USE_CHIP)).booleanValue());
			familyDiscount.setChecked(new Boolean(selectedDistance.getMetaData(PARAMETER_FAMILY_DISCOUNT)).booleanValue());
			allowsGroups.setChecked(new Boolean(selectedDistance.getMetaData(PARAMETER_ALLOWS_GROUPS)).booleanValue());
			transportOffered.setChecked(new Boolean(selectedDistance.getMetaData(PARAMETER_TRANSPORT_OFFERED)).booleanValue());
			priceForTransportISK.setValue(selectedDistance.getMetaData(PARAMETER_PRICE_FOR_TRANSPORT_ISK));
			priceForTransportEUR.setValue(selectedDistance.getMetaData(PARAMETER_PRICE_FOR_TRANSPORT_EUR));
			String shirtSizeMetadata = selectedDistance.getMetaData(PARAMETER_SHIRT_SIZES_PER_RUN);
			if (shirtSizeMetadata != null) {
				String[] shirtSizeMetadataArray = MiscUtil.str2array(shirtSizeMetadata,",");
				shirtSizeSelectionBox.setSelectedElements(shirtSizeMetadataArray);
			}
		}
		add(form);
	}

	public void save(IWContext iwc) throws java.rmi.RemoteException {
		String distanceID = iwc.getParameter(PARAMETER_MARATHON_DISTANCE_PK);
		Distance distance = null;
		if (distanceID == null) {
			Group year = null;
			try {
				String yearID = iwc.getParameter(PARAMETER_MARATHON_YEAR_PK);
				if (yearID != null && !yearID.equals("")) {
					int id = Integer.parseInt(yearID);
					year = getGroupBiz().getGroupByGroupID(id);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	Group group = null;
			try {
				String distanceString = iwc.getParameter(PARAMETER_DISTANCE);
				if (distanceString == null || "".equals(distanceString)) {
					group = getGroupBiz().createGroupUnder(distanceString, null, year);
					group.setGroupType(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
					group.store();
					distanceID = group.getPrimaryKey().toString();
				}
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (CreateException e) {
				e.printStackTrace();
			}
		}
		
		if (distanceID != null) {
			try {
				distance = ConverterUtility.getInstance().convertGroupToDistance(new Integer(distanceID));
			} 
			catch (FinderException e){
				//no distance found, nothing saved
			}
		}
		
		if (distance != null) { 
			distance.setUseChip(iwc.isParameterSet(PARAMETER_USE_CHIP));
			distance.setFamilyDiscount(iwc.isParameterSet(PARAMETER_FAMILY_DISCOUNT));
			distance.setAllowsGroups(iwc.isParameterSet(PARAMETER_ALLOWS_GROUPS));
			distance.setTransportOffered(iwc.isParameterSet(PARAMETER_TRANSPORT_OFFERED));
			
			String priceInISK = iwc.getParameter(PARAMETER_PRICE_ISK);
			if (priceInISK != null && priceInISK.length() > 0) {
				distance.setPriceInISK(new Float(priceInISK).floatValue());
			} else {
				distance.setPriceInISK(0);
			}
			String priceInEUR = iwc.getParameter(PARAMETER_PRICE_EUR);
			if (priceInEUR != null && priceInEUR.length() > 0) {
				distance.setPriceInEUR(new Float(priceInEUR).floatValue());
			} else {
				distance.setPriceInEUR(0);
			}
			String childrenPriceInISK = iwc.getParameter(PARAMETER_CHILDREN_PRICE_ISK);
			if (childrenPriceInISK != null && childrenPriceInISK.length() > 0) {
				distance.setChildrenPriceInISK(new Float(childrenPriceInISK).floatValue());
			} else {
				distance.setChildrenPriceInISK(0);
			}
			String childrenPriceInEUR = iwc.getParameter(PARAMETER_CHILDREN_PRICE_EUR);
			if (childrenPriceInEUR != null && childrenPriceInEUR.length() > 0) {
				distance.setChildrenPriceInEUR(new Float(childrenPriceInEUR).floatValue());
			} else {
				distance.setChildrenPriceInEUR(0);
			}
			String priceForTransportInISK = iwc.getParameter(PARAMETER_PRICE_FOR_TRANSPORT_ISK);
			if (priceForTransportInISK != null && priceForTransportInISK.length() > 0) {
				distance.setPriceForTransportInISK(new Float(priceForTransportInISK).floatValue());
			} else {
				distance.setPriceForTransportInISK(0);
			}
			String priceForTransportInEUR = iwc.getParameter(PARAMETER_PRICE_FOR_TRANSPORT_ISK);
			if (priceForTransportInEUR != null && priceForTransportInEUR.length() > 0) {
				distance.setPriceForTransportInEUR(new Float(priceForTransportInEUR).floatValue());
			} else {
				distance.setPriceForTransportInEUR(0);
			}
	
			distance.setNumberOfSplits(new Integer(iwc.getParameter(PARAMETER_NUMBER_OF_SPLITS)).intValue());
			String[] shirtSizesPerRun = iwc.getParameterValues(PARAMETER_SHIRT_SIZES_PER_RUN);
			if(shirtSizesPerRun!=null && shirtSizesPerRun.length != 0){
				List abbrList = ListUtil.convertStringArrayToList(shirtSizesPerRun);
				if(abbrList.isEmpty()){
					distance.setMetaData(PARAMETER_SHIRT_SIZES_PER_RUN, "");
				}
				else{
					String commaSeparated = ListUtil.convertListOfStringsToCommaseparatedString(abbrList);
					distance.setMetaData(PARAMETER_SHIRT_SIZES_PER_RUN, commaSeparated);
				}
			}
			distance.store();
		}
	}

	protected int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}
	
	private GroupBusiness getGroupBiz() throws IBOLookupException {
		GroupBusiness business = (GroupBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), GroupBusiness.class);
		return business;
	}
}