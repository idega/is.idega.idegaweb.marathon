package is.idega.idegaweb.marathon.presentation.user;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.presentation.UserTab;
import com.idega.util.ListUtil;

public class UserRelayTab extends UserTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.marathon";

	private static final String TAB_NAME = "user_relay_tab_name";
	private static final String DEFAULT_TAB_NAME = "Relay group";

	private static final String PARAMETER_SHIRT_SIZES_PER_RUN = "shirt_sizes_per_run";

	private TextInput relayLeg;

	private TextInput rel1SSN;
	private TextInput rel1Name;
	private TextInput rel1Email;
	private DropdownMenu rel1ShirtSize;
	private TextInput rel1Leg;

	private TextInput rel2SSN;
	private TextInput rel2Name;
	private TextInput rel2Email;
	private DropdownMenu rel2ShirtSize;
	private TextInput rel2Leg;

	private TextInput rel3SSN;
	private TextInput rel3Name;
	private TextInput rel3Email;
	private DropdownMenu rel3ShirtSize;
	private TextInput rel3Leg;

	private Table t;

	private Text relayLegText;

	private Text relSSNText;
	private Text relNameText;
	private Text relEmailText;
	private Text relShirtSizeText;
	private Text relLegText;

	private String relayLegFieldName;

	private String rel1SSNFieldName;
	private String rel1NameFieldName;
	private String rel1EmailFieldName;
	private String rel1ShirtSizeFieldName;
	private String rel1LegFieldName;

	private String rel2SSNFieldName;
	private String rel2NameFieldName;
	private String rel2EmailFieldName;
	private String rel2ShirtSizeFieldName;
	private String rel2LegFieldName;

	private String rel3SSNFieldName;
	private String rel3NameFieldName;
	private String rel3EmailFieldName;
	private String rel3ShirtSizeFieldName;
	private String rel3LegFieldName;

	public UserRelayTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void initializeFieldNames() {
		this.relayLegFieldName = "rel_leg";

		this.rel1SSNFieldName = "rel1_ssn";
		this.rel1NameFieldName = "rel1_name";
		this.rel1EmailFieldName = "rel1_email";
		this.rel1ShirtSizeFieldName = "rel1_shirt";
		this.rel1LegFieldName = "rel1_leg";

		this.rel2SSNFieldName = "rel2_ssn";
		this.rel2NameFieldName = "rel2_name";
		this.rel2EmailFieldName = "rel2_email";
		this.rel2ShirtSizeFieldName = "rel2_shirt";
		this.rel2LegFieldName = "rel2_leg";

		this.rel3SSNFieldName = "rel3_ssn";
		this.rel3NameFieldName = "rel3_name";
		this.rel3EmailFieldName = "rel3_email";
		this.rel3ShirtSizeFieldName = "rel3_shirt";
		this.rel3LegFieldName = "rel3_leg";
	}

	public void initializeFieldValues() {
		this.fieldValues = new Hashtable();
		this.fieldValues.put(this.relayLegFieldName, "");
		this.fieldValues.put(this.rel1SSNFieldName, "");
		this.fieldValues.put(this.rel1NameFieldName, "");
		this.fieldValues.put(this.rel1EmailFieldName, "");
		this.fieldValues.put(this.rel1ShirtSizeFieldName, "");
		this.fieldValues.put(this.rel1LegFieldName, "");

		this.fieldValues.put(this.rel2SSNFieldName, "");
		this.fieldValues.put(this.rel2NameFieldName, "");
		this.fieldValues.put(this.rel2EmailFieldName, "");
		this.fieldValues.put(this.rel2ShirtSizeFieldName, "");
		this.fieldValues.put(this.rel2LegFieldName, "");

		this.fieldValues.put(this.rel3SSNFieldName, "");
		this.fieldValues.put(this.rel3NameFieldName, "");
		this.fieldValues.put(this.rel3EmailFieldName, "");
		this.fieldValues.put(this.rel3ShirtSizeFieldName, "");
		this.fieldValues.put(this.rel3LegFieldName, "");
	}

	public void updateFieldsDisplayStatus() {
		this.relayLeg.setValue((String) this.fieldValues
				.get(this.relayLegFieldName));

		this.rel1SSN.setValue((String) this.fieldValues
				.get(this.rel1SSNFieldName));
		this.rel1Name.setValue((String) this.fieldValues
				.get(this.rel1NameFieldName));
		this.rel1Email.setValue((String) this.fieldValues
				.get(this.rel1EmailFieldName));
		this.rel1ShirtSize.setSelectedOption((String) this.fieldValues
				.get(this.rel1ShirtSizeFieldName));
		this.rel1Leg.setValue((String) this.fieldValues
				.get(this.rel1LegFieldName));

		this.rel2SSN.setValue((String) this.fieldValues
				.get(this.rel2SSNFieldName));
		this.rel2Name.setValue((String) this.fieldValues
				.get(this.rel2NameFieldName));
		this.rel2Email.setValue((String) this.fieldValues
				.get(this.rel2EmailFieldName));
		this.rel2ShirtSize.setSelectedOption((String) this.fieldValues
				.get(this.rel2ShirtSizeFieldName));
		this.rel2Leg.setValue((String) this.fieldValues
				.get(this.rel2LegFieldName));

		this.rel3SSN.setValue((String) this.fieldValues
				.get(this.rel3SSNFieldName));
		this.rel3Name.setValue((String) this.fieldValues
				.get(this.rel3NameFieldName));
		this.rel3Email.setValue((String) this.fieldValues
				.get(this.rel3EmailFieldName));
		this.rel3ShirtSize.setSelectedOption((String) this.fieldValues
				.get(this.rel3ShirtSizeFieldName));
		this.rel3Leg.setValue((String) this.fieldValues
				.get(this.rel3LegFieldName));
	}

	private boolean isGroupRelayDistanceGroup() {
		if (getGroupID() > 0) {
			Group group = getGroup();
			if (group != null
					&& group.getGroupType().equals(
							IWMarathonConstants.GROUP_TYPE_RUN_GROUP)) {
				Group distanceGroup = (Group) group.getParentNode();
				if (distanceGroup != null
						&& distanceGroup.getGroupType().equals(
								IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE)) {
					Distance distance;
					try {
						distance = ConverterUtility.getInstance()
								.convertGroupToDistance(distanceGroup);
						return distance.isRelayDistance();
					} catch (FinderException e) {
					}
				}
			}
		}

		return false;
	}

	private Participant getParticipantEntry() {
		Group group = getGroup();
		Group distance = (Group) group.getParentNode();
		Group year = (Group) distance.getParentNode();
		Group run = (Group) year.getParentNode();

		try {
			IWContext iwc = IWContext.getInstance();

			return getRunBusiness(iwc).getParticipantByRunAndYear(getUser(),
					run, year, false);
		} catch (RemoteException e) {
		} catch (FinderException e) {
		}

		return null;
	}

	public void initializeFields() {
		this.relayLeg = new TextInput(this.relayLegFieldName);

		this.rel1SSN = new TextInput(this.rel1SSNFieldName);
		this.rel1Name = new TextInput(this.rel1NameFieldName);
		this.rel1Email = new TextInput(this.rel1EmailFieldName);
		this.rel1ShirtSize = new DropdownMenu(this.rel1ShirtSizeFieldName);
		this.rel1Leg = new TextInput(this.rel1LegFieldName);

		this.rel2SSN = new TextInput(this.rel2SSNFieldName);
		this.rel2Name = new TextInput(this.rel2NameFieldName);
		this.rel2Email = new TextInput(this.rel2EmailFieldName);
		this.rel2ShirtSize = new DropdownMenu(this.rel2ShirtSizeFieldName);
		this.rel2Leg = new TextInput(this.rel2LegFieldName);

		this.rel3SSN = new TextInput(this.rel3SSNFieldName);
		this.rel3Name = new TextInput(this.rel3NameFieldName);
		this.rel3Email = new TextInput(this.rel3EmailFieldName);
		this.rel3ShirtSize = new DropdownMenu(this.rel3ShirtSizeFieldName);
		this.rel3Leg = new TextInput(this.rel3LegFieldName);

		this.rel1ShirtSize.addOption(new SelectOption("", -1));
		this.rel2ShirtSize.addOption(new SelectOption("", -1));
		this.rel3ShirtSize.addOption(new SelectOption("", -1));
	}

	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		this.relayLegText = new Text(iwrb.getLocalizedString(
				this.relayLegFieldName, "Leg"));
		this.relayLegText.setBold();

		this.relSSNText = new Text(iwrb.getLocalizedString(
				this.rel1SSNFieldName, "SSN"));
		this.relSSNText.setBold();
		this.relNameText = new Text(iwrb.getLocalizedString(
				this.rel1NameFieldName, "Name"));
		this.relNameText.setBold();
		this.relEmailText = new Text(iwrb.getLocalizedString(
				this.rel1EmailFieldName, "Email"));
		this.relEmailText.setBold();
		this.relShirtSizeText = new Text(iwrb.getLocalizedString(
				this.rel1ShirtSizeFieldName, "Shirt size"));
		this.relShirtSizeText.setBold();
		this.relLegText = new Text(iwrb.getLocalizedString(
				this.rel1LegFieldName, "Leg"));
		this.relLegText.setBold();
	}

	public void lineUpFields() {
		empty();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		if (!isGroupRelayDistanceGroup()) {
			add(new Text(iwrb.getLocalizedString("not_a_relay_distance", "Not a relay distance")));
		} else {
			this.t = new Table(2, 2);
			this.t.setCellpadding(5);
			this.t.setCellspacing(0);

			this.t.add(this.relayLegText, 1, 1);
			this.t.add(this.relayLeg, 2, 1);

			this.t.mergeCells(1, 2, 2, 2);

			Table innerTable = new Table(4, 6);
			innerTable.setCellpadding(5);
			innerTable.setCellspacing(0);

			innerTable.add(iwrb.getLocalizedString("relay_partner", "Partner"),
					1, 1);
			innerTable.add(this.relSSNText, 1, 2);
			innerTable.add(this.relNameText, 1, 3);
			innerTable.add(this.relEmailText, 1, 4);
			innerTable.add(this.relShirtSizeText, 1, 5);
			innerTable.add(this.relLegText, 1, 6);

			innerTable.add("1", 2, 1);
			innerTable.add(this.rel1SSN, 2, 2);
			innerTable.add(this.rel1Name, 2, 3);
			innerTable.add(this.rel1Email, 2, 4);
			innerTable.add(this.rel1ShirtSize, 2, 5);
			innerTable.add(this.rel1Leg, 2, 6);

			innerTable.add("2", 3, 1);
			innerTable.add(this.rel2SSN, 3, 2);
			innerTable.add(this.rel2Name, 3, 3);
			innerTable.add(this.rel2Email, 3, 4);
			innerTable.add(this.rel2ShirtSize, 3, 5);
			innerTable.add(this.rel2Leg, 3, 6);

			innerTable.add("3", 4, 1);
			innerTable.add(this.rel3SSN, 4, 2);
			innerTable.add(this.rel3Name, 4, 3);
			innerTable.add(this.rel3Email, 4, 4);
			innerTable.add(this.rel3ShirtSize, 4, 5);
			innerTable.add(this.rel3Leg, 4, 6);

			this.t.add(innerTable, 1, 2);

			add(this.t);
		}
	}

	public boolean collect(IWContext iwc) {
		return false;
	}

	public boolean store(IWContext iwc) {
		return false;
	}

	public void initFieldContents() {
		if (isGroupRelayDistanceGroup()) {
			Participant participant = getParticipantEntry();

			if (participant != null) {
				this.fieldValues = new Hashtable();
				this.fieldValues
						.put(this.relayLegFieldName,
								participant.getRelayLeg() == null ? ""
										: participant.getRelayLeg());
				this.fieldValues.put(this.rel1SSNFieldName,
						participant.getRelayPartner1SSN() == null ? ""
								: participant.getRelayPartner1SSN());
				this.fieldValues.put(this.rel1NameFieldName,
						participant.getRelayPartner1Name() == null ? ""
								: participant.getRelayPartner1Name());
				this.fieldValues.put(this.rel1EmailFieldName,
						participant.getRelayPartner1Email() == null ? ""
								: participant.getRelayPartner1Email());
				this.fieldValues.put(this.rel1ShirtSizeFieldName,
						participant.getRelayPartner1ShirtSize() == null ? ""
								: participant.getRelayPartner1ShirtSize());
				this.fieldValues.put(this.rel1LegFieldName,
						participant.getRelayPartner1Leg() == null ? ""
								: participant.getRelayPartner1Leg());

				this.fieldValues.put(this.rel2SSNFieldName,
						participant.getRelayPartner2SSN() == null ? ""
								: participant.getRelayPartner2SSN());
				this.fieldValues.put(this.rel2NameFieldName,
						participant.getRelayPartner2Name() == null ? ""
								: participant.getRelayPartner2Name());
				this.fieldValues.put(this.rel2EmailFieldName,
						participant.getRelayPartner2Email() == null ? ""
								: participant.getRelayPartner2Email());
				this.fieldValues.put(this.rel2ShirtSizeFieldName,
						participant.getRelayPartner2ShirtSize() == null ? ""
								: participant.getRelayPartner2ShirtSize());
				this.fieldValues.put(this.rel2LegFieldName,
						participant.getRelayPartner2Leg() == null ? ""
								: participant.getRelayPartner2Leg());

				this.fieldValues.put(this.rel3SSNFieldName,
						participant.getRelayPartner3SSN() == null ? ""
								: participant.getRelayPartner3SSN());
				this.fieldValues.put(this.rel3NameFieldName,
						participant.getRelayPartner3Name() == null ? ""
								: participant.getRelayPartner3Name());
				this.fieldValues.put(this.rel3EmailFieldName,
						participant.getRelayPartner3Email() == null ? ""
								: participant.getRelayPartner3Email());
				this.fieldValues.put(this.rel3ShirtSizeFieldName,
						participant.getRelayPartner3ShirtSize() == null ? ""
								: participant.getRelayPartner3ShirtSize());
				this.fieldValues.put(this.rel3LegFieldName,
						participant.getRelayPartner3Leg() == null ? ""
								: participant.getRelayPartner3Leg());

				this.rel1ShirtSize = new DropdownMenu(
						this.rel1ShirtSizeFieldName);
				this.rel2ShirtSize = new DropdownMenu(
						this.rel2ShirtSizeFieldName);
				this.rel3ShirtSize = new DropdownMenu(
						this.rel3ShirtSizeFieldName);

				IWContext iwc = IWContext.getInstance();
				IWResourceBundle iwrb = getResourceBundle(iwc);

				Group group = getGroup();
				Group distanceGroup = (Group) group.getParentNode();

				String shirtSizeMetadata = distanceGroup
						.getMetaData(PARAMETER_SHIRT_SIZES_PER_RUN);
				List shirtSizes = null;
				if (shirtSizeMetadata != null) {
					shirtSizes = ListUtil
							.convertCommaSeparatedStringToList(shirtSizeMetadata);
				}

				this.rel1ShirtSize.addOption(new SelectOption("", -1));
				this.rel2ShirtSize.addOption(new SelectOption("", -1));
				this.rel3ShirtSize.addOption(new SelectOption("", -1));

				if (shirtSizes != null) {
					Iterator shirtIt = shirtSizes.iterator();
					while (shirtIt.hasNext()) {
						String shirtSizeKey = (String) shirtIt.next();
						this.rel1ShirtSize.addMenuElement(
								shirtSizeKey,
								iwrb.getLocalizedString("shirt_size."
										+ shirtSizeKey, shirtSizeKey));
						this.rel2ShirtSize.addMenuElement(
								shirtSizeKey,
								iwrb.getLocalizedString("shirt_size."
										+ shirtSizeKey, shirtSizeKey));
						this.rel3ShirtSize.addMenuElement(
								shirtSizeKey,
								iwrb.getLocalizedString("shirt_size."
										+ shirtSizeKey, shirtSizeKey));
					}
				}

				lineUpFields();
			}
		}
		
		updateFieldsDisplayStatus();

	}

	public RunBusiness getRunBusiness(IWApplicationContext iwc) {
		RunBusiness business = null;
		if (business == null) {
			try {
				business = (RunBusiness) com.idega.business.IBOLookup
						.getServiceInstance(iwc, RunBusiness.class);
			} catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return business;
	}
}