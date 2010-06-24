/*
 * $Id: GroupRegistration.java,v 1.7 2008/06/11 16:50:14 palli Exp $
 * Created on May 30, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

/**
 * Last modified: $Date: 2008/06/11 16:50:14 $ by $Author: palli $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
 */
public class GroupRegistration extends RunBlock {
	private static final String PARAMETER_PARTICIPANT_ENTRY = "prm_part_entry";
	private static final String PARAMETER_GROUP_NAME = "prm_group_name";
	private static final String PARAMETER_PARTICIPANT_NUMBER = "prm_participant_number";
	private static final String PARAMETER_PARTICIPANT_MAP = "prm_participant_map";
	private static final String PARAMETER_LIMIT_RUN_IDS = "run_ids";

	private static final int ACTION_STEP_ONE = 1;
	private static final int ACTION_STEP_TWO = 2;
	private static final int ACTION_STEP_THREE = 3;
	private static final int ACTION_SAVE = 4;

	private String groupName;
	private String[] participants;
	private Map participantMap;
	private String runIds;
	private boolean showAllThisYear;
	private Participant participant;

	public void main(IWContext iwc) throws Exception {
		if (!iwc.isInEditMode()) {
			switch (parseAction(iwc)) {
			case ACTION_STEP_ONE:
				stepOne(iwc);
				break;
			case ACTION_STEP_TWO:
				stepTwo(iwc);
				break;
			case ACTION_STEP_THREE:
				stepThree(iwc);
				break;
			case ACTION_SAVE:
				save(iwc);
				break;
			}
		}
	}

	private void stepOne(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, ACTION_STEP_TWO);
		form.maintainParameter(PARAMETER_PARTICIPANT_NUMBER);

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(1, 4, "run_reg.group_registration",
				"Group registration"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize(
				"run_reg.group_information_text_step_1",
				"Information text 1...")), 1, row++);
		table.setHeight(row++, 6);

		DropdownMenu runDropdown = getRunDropdown(iwc);

		if (!runDropdown.isEmpty()) {
			Table choiceTable = new Table();
			choiceTable.setColumns(2);
			choiceTable.setCellpadding(2);
			choiceTable.setCellspacing(0);
			table.add(choiceTable, 1, row++);
			int iRow = 1;

			choiceTable
					.add(getHeader(localize(IWMarathonConstants.RR_PRIMARY_DD,
							"Run")
							+ "/"
							+ localize(IWMarathonConstants.RR_SECONDARY_DD,
									"Distance")), 1, iRow);
			choiceTable.add(runDropdown, 2, iRow);

			TextInput groupNameField = (TextInput) getStyledInterface(new TextInput(
					PARAMETER_GROUP_NAME));
			groupNameField.setAsNotEmpty(localize(
					"run_reg.group_name_not_empty",
					"Group name can not be empty"));

			choiceTable.setHeight(iRow++, 12);
			choiceTable.add(getHeader(localize("run_reg.group_name",
					"Group name")), 1, iRow);
			choiceTable.add(groupNameField, 2, iRow++);

			SubmitButton next = (SubmitButton) getButton(new SubmitButton(
					localize("next", "Next")));
			table.setHeight(row++, 18);
			table.add(next, 1, row);
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		} else {
			table.add(getHeader(localize("run_reg.group_no_run_available",
					"You are not registered in any events where you can create a group.")), 1, row);
		}

		add(form);
	}

	private void stepTwo(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, ACTION_STEP_THREE);
		form.maintainParameter(PARAMETER_GROUP_NAME);
		form.maintainParameter(PARAMETER_PARTICIPANT_ENTRY);

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(2, 4, "run_reg.group_registration",
				"Group registration"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize(
				"run_reg.group_information_text_step_2",
				"Information text 2...")), 1, row++);
		table.setHeight(row++, 6);

		Table choiceTable = new Table();
		choiceTable.setColumns(5);
		choiceTable.setCellpadding(2);
		choiceTable.setCellspacing(0);
		choiceTable.setWidth(1, "33%");
		choiceTable.setWidth(2, 6);
		choiceTable.setWidth(3, "33%");
		choiceTable.setWidth(4, 6);
		choiceTable.setWidth(5, "33%");
		choiceTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(choiceTable, 1, row++);
		int iRow = 1;

		for (int a = 1; a <= 5; a++) {
			TextInput participant = (TextInput) getStyledInterface(new TextInput(
					PARAMETER_PARTICIPANT_NUMBER));
			participant.setWidth(Table.HUNDRED_PERCENT);
			participant.setAsIntegers(localize(
					"run_reg.invalid_participant_number",
					"Invalid participant number"));
			if (this.participants != null && this.participants.length >= a) {
				participant.setContent(this.participants[a - 1]);
			}

			choiceTable.add(getHeader(localize("run_reg.partipant_nr",
					"Participant nr.")
					+ " " + String.valueOf(a)), 1, iRow);
			choiceTable.add(participant, 1, iRow);

			if (a != 5) {
				choiceTable.setHeight(iRow++, 3);
			}

			if (a == 1) {
				participant.setValue(this.participant.getParticipantNumber());
				participant.setDisabled(true);
				choiceTable.add(new HiddenInput(PARAMETER_PARTICIPANT_NUMBER,
						Integer.toString(this.participant
								.getParticipantNumber())), 1, iRow);
			}
		}

		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(
				localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String
				.valueOf(ACTION_STEP_ONE));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize(
				"next", "Next")));

		table.setHeight(row++, 18);
		table.add(previous, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}

	private void stepThree(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, ACTION_SAVE);
		form.maintainParameter(PARAMETER_GROUP_NAME);
		form.maintainParameter(PARAMETER_PARTICIPANT_ENTRY);

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(3, 4, "run_reg.group_registration_overview",
				"Group registration overview"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize(
				"run_reg.group_information_text_step_3",
				"Information text 3...")), 1, row++);
		table.setHeight(row++, 12);

		table.setCellpaddingLeft(1, row, 3);
		table.add(
				getHeader(localize("run_reg.group_name", "Group name") + ":"),
				1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getText(this.groupName), 1, row++);
		table.setHeight(row++, 12);

		Table runnerTable = new Table();
		runnerTable.setColumns(3);
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		runnerTable
				.add(getHeader(localize("run_reg.runner_name", "Runner name")),
						1, 1);
		runnerTable.add(getHeader(localize("run_reg.run", "Run")), 2, 1);
		runnerTable.add(getHeader(localize("run_reg.distance", "Distance")), 3,
				1);
		table.add(runnerTable, 1, row++);
		int runRow = 2;

		for (int a = 0; a < this.participants.length; a++) {
			String runner = this.participants[a];
			if (runner.length() > 0) {
				Participant participant = (Participant) this.participantMap
						.get(runner);

				if (participant != null) {
					runnerTable.add(new HiddenInput(
							PARAMETER_PARTICIPANT_NUMBER, runner), 1, runRow);
					runnerTable.add(new HiddenInput(PARAMETER_PARTICIPANT_MAP,
							participant.getPrimaryKey().toString()), 1, runRow);
					runnerTable.add(getText(participant.getUser().getName()),
							1, runRow);
					runnerTable.add(getText(localize(participant
							.getRunTypeGroup().getName(), participant
							.getRunTypeGroup().getName())), 2, runRow);
					runnerTable.add(getText(localize(participant
							.getRunDistanceGroup().getName(), participant
							.getRunDistanceGroup().getName())), 3, runRow++);
				}
			}
		}

		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(
				localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String
				.valueOf(ACTION_STEP_TWO));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize(
				"save", "Save")));

		table.setHeight(row++, 18);
		table.add(previous, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}

	private void save(IWContext iwc) {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;

		try {
			String[] runners = iwc
					.getParameterValues(PARAMETER_PARTICIPANT_MAP);
			getRunBusiness(iwc).addParticipantsToGroup(runners, this.groupName);
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}

		table.add(getPhasesTable(4, 4, "run_reg.group_registration_done",
				"Group registration completed"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize(
				"run_reg.group_information_text_step_4",
				"Information text 4...")), 1, row++);
		table.setHeight(row++, 18);

		table.add(getText(localize("run_reg.group_registered_for",
				"You have registered for the team competition in the ")), 1,
				row);
		table.add(getHeader(localize(this.participant.getRunTypeGroup()
				.getName(), this.participant.getRunTypeGroup().getName())), 1,
				row++);
		table.setHeight(row++, 16);

		table.add(getText(localize("run_reg.group_registered_name",
				"Your team is registered under the name ")), 1, row);
		table.add(getHeader(this.groupName), 1, row++);
		table.setHeight(row++, 16);

		table.add(getText(localize("run_reg.group_registered_distance",
				"Each team member has chosen to run ")), 1, row);
		table.add(getHeader(localize(this.participant.getRunDistanceGroup()
				.getName(), this.participant.getRunDistanceGroup().getName())),
				1, row++);
		table.setHeight(row++, 16);

		table.add(getText(localize("run_reg.group_registered_runners",
				"The runners in your team are")
				+ ":"), 1, row++);
		for (int a = 0; a < this.participants.length; a++) {
			String runner = this.participants[a];
			if (runner.length() > 0) {
				Participant participant = (Participant) this.participantMap
						.get(runner);

				if (participant != null) {
					table.add(getHeader(participant.getParticipantNumber()
							+ " - " + participant.getUser().getName()), 1,
							row++);
				}
			}
		}

		add(table);
	}

	protected int parseAction(IWContext iwc) {
		int action = ACTION_STEP_ONE;

		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}

		if (iwc.isParameterSet(PARAMETER_PARTICIPANT_ENTRY)) {
			try {
				this.participant = getRunBusiness(iwc)
						.getParticipantByPrimaryKey(
								new Integer(
										iwc
												.getParameter(PARAMETER_PARTICIPANT_ENTRY))
										.intValue());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		if (action == ACTION_STEP_TWO || action == ACTION_STEP_THREE
				|| action == ACTION_SAVE) {
			if (this.participant == null) {
				action = ACTION_STEP_ONE;
				getParentPage().setAlertOnLoad(
						localize("run_reg.no_run_selected",
								"Please select a run"));
				return action;

			}
		}

		if (iwc.isParameterSet(PARAMETER_GROUP_NAME)) {
			this.groupName = iwc.getParameter(PARAMETER_GROUP_NAME);
		}
		this.participants = iwc
				.getParameterValues(PARAMETER_PARTICIPANT_NUMBER);

		int numberOfParticipants = 0;
		if (action == ACTION_STEP_TWO || action == ACTION_SAVE) {
			try {
				Group year = ConverterUtility.getInstance().convertGroupToYear(
						participant.getRunYearGroup());
				Collection participantsByYearAndTeamName = getRunBusiness(iwc)
						.getParticipantsByYearAndTeamName(year.getPrimaryKey(),
								groupName);
				if (participantsByYearAndTeamName != null
						&& !participantsByYearAndTeamName.isEmpty()) {
					action = ACTION_STEP_ONE;
					getParentPage()
							.setAlertOnLoad(
									localize("run_reg.teamname_already_exists",
											"This teamname already exists in this run: ")
											+ groupName);
					return action;
				}
			} catch (FinderException e) {
				// No participants found with this teamname in this year of the
				// run, so it is OK to create new team with this name
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		if (action == ACTION_STEP_THREE || action == ACTION_SAVE) {
			this.participantMap = new HashMap();
			for (int i = 0; i < this.participants.length; i++) {
				String participant = this.participants[i];
				if (participant.length() > 0) {
					try {
						Participant runner = getRunBusiness(iwc)
								.getParticipantByDistanceAndParticipantNumber(
										this.participant.getRunDistanceGroup()
												.getPrimaryKey(),
										Integer.parseInt(participant));
						if (runner.getRunGroupName() != null) {
							action = ACTION_STEP_TWO;
							getParentPage()
									.setAlertOnLoad(
											localize(
													"run_reg.participant_already_in_a_group",
													"Participant is already in another group: ")
													+ participant);
							return action;
						}
						this.participantMap.put(participant, runner);
						numberOfParticipants++;
					} catch (RemoteException re) {
						throw new IBORuntimeException(re);
					} catch (FinderException fe) {
						action = ACTION_STEP_TWO;
						getParentPage()
								.setAlertOnLoad(
										localize(
												"run_reg.participant_not_found_in_distance",
												"Participant not found in distance: ")
												+ participant);
						return action;
					}
				}
			}
		}

		if (action == ACTION_STEP_THREE && numberOfParticipants < 3) {
			action = ACTION_STEP_TWO;
			getParentPage()
					.setAlertOnLoad(
							localize("run_reg.must_select_three",
									"You have to select a minimum of three to form a team."));
		}
		if (iwc.isParameterSet(PARAMETER_LIMIT_RUN_IDS)) {
			String runIds = iwc.getParameter(PARAMETER_LIMIT_RUN_IDS);
			setRunIds(runIds);
		}
		return action;
	}

	protected DropdownMenu getRunDropdown(IWContext iwc) {
		DropdownMenu runDropdown = null;
		Collection runsYears = null;
		try {
			runsYears = getRunBusiness(iwc).getRunGroupOfTypeForUser(
					iwc.getCurrentUser(),
					IWMarathonConstants.GROUP_TYPE_RUN_YEAR);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Collection participantEntries = new ArrayList();
		if (runsYears != null && !runsYears.isEmpty()) {
			Iterator it = runsYears.iterator();
			Group yearGroup = null;
			IWTimestamp now = new IWTimestamp();
			while (it.hasNext()) {
				yearGroup = (Group) it.next();

				try {
					Year year = ConverterUtility.getInstance()
							.convertGroupToYear(yearGroup);
					Group run = (Group) year.getParentNode();

					// getRunIdsArray()

					IWTimestamp lastChangeDate = new IWTimestamp(year
							.getRunDate());
					if (now.isEarlierThan(lastChangeDate)) {
						Participant participant = getRunBusiness(iwc)
								.getParticipantByRunAndYear(
										iwc.getCurrentUser(),
										(Group) yearGroup.getParentNode(),
										yearGroup);

						if (participant.getRunDistanceGroup().isAllowsGroups()) {
							participantEntries.add(participant);
						}
					}
				} catch (FinderException e) {
				} catch (RemoteException e) {
				}
			}
		}

		if (!participantEntries.isEmpty()) {
			runDropdown = new DropdownMenu(PARAMETER_PARTICIPANT_ENTRY);
			runDropdown.addMenuElement("-1", iwrb.getLocalizedString(
					"run_year_ddd.select_run", "Select run..."));
			runDropdown.keepStatusOnAction(true);

			Iterator it = participantEntries.iterator();
			Participant participant = null;
			while (it.hasNext()) {
				participant = (Participant) it.next();

				Group run = participant.getRunTypeGroup();
				Group year = participant.getRunYearGroup();
				runDropdown.addMenuElement(participant.getPrimaryKey()
						.toString(), localize(run.getName(), run.getName())
						+ " " + year.getName());
			}
		}

		String[] constrainedRunIds = getRunIdsArray();

		return runDropdown;
	}

	public String getRunIds() {
		return runIds;
	}

	public void setRunIds(String runIds) {
		this.runIds = runIds;
	}

	public String[] getRunIdsArray() {
		String runIds = getRunIds();
		if (runIds != null) {
			if (runIds.indexOf(",") != -1) {
				StringTokenizer tokenizer = new StringTokenizer(runIds, ",");
				List list = new ArrayList();
				while (tokenizer.hasMoreElements()) {
					list.add(tokenizer.nextElement());
				}
				return (String[]) list.toArray(new String[0]);
			} else {
				String[] array = { runIds };
				return array;
			}
		}

		return null;
	}

	public void setShowAllThisYear(boolean showAllThisYear) {
		this.showAllThisYear = showAllThisYear;
	}

	public boolean getShowAllThisYear() {
		return this.showAllThisYear;
	}
}