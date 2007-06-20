package is.idega.idegaweb.marathon.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;

/**
 * <p>
 * Refactoring of the older "RunBlock" to a more generic structure.
 * </p>
 *  Last modified: $Date: 2007/06/20 10:32:23 $ by $Author: tryggvil $
 * 
 * @author <a href="mailto:tryggvil@idega.com">tryggvil</a>
 * @version $Revision: 1.4 $
 */
public class StepsBlock extends Block {

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.marathon";
	public static final String STYLENAME_FORM_ELEMENT = "FormElement";
	public static final String STYLENAME_HEADER = "Header";
	public static final String STYLENAME_TEXT = "Text";
	public static final String STYLENAME_SMALL_TEXT = "SmallText";
	public static final String STYLENAME_LINK = "Link";
	public static final String STYLENAME_INTERFACE = "Interface";
	public static final String STYLENAME_INTERFACE_BUTTON = "InterfaceButton";
	public static final String STYLENAME_CHECKBOX = "CheckBox";
	
	public static final String STYLENAME_STEPSHEADER= "stepsHeader";
	public static final String STYLENAME_STEPNAME= "stepName";
	public static final String STYLENAME_STEPCURRENT= "stepCurrent";
	public static final String STYLENAME_STEPSFOOTER = "stepsFooter";
	
	protected IWResourceBundle iwrb = null;
	protected IWBundle iwb = null;
	
	protected Map stepsMap;
	
	protected static final String PARAMETER_ACTION = "prm_action";
	protected static final String PARAMETER_FROM_ACTION = "prm_from_action";
	
	protected static final int ACTION_START=0;
	protected static final int ACTION_PREVIOUS = -1;
	protected static final int ACTION_NEXT = 1;

	public StepsBlock() {
		super();
	}

	public void _main(IWContext iwc) throws Exception {
		setResourceBundle(getResourceBundle(iwc));
		setBundle(getBundle(iwc));
		super._main(iwc);
	}

	public Map getStyleNames() {
		HashMap map = new HashMap();
		String[] styleNames = { STYLENAME_HEADER, STYLENAME_TEXT, STYLENAME_SMALL_TEXT, STYLENAME_LINK, STYLENAME_LINK + ":hover", STYLENAME_INTERFACE, STYLENAME_CHECKBOX, STYLENAME_INTERFACE_BUTTON,  };
		String[] styleValues = { "", "", "", "", "", "", "", "" };
	
		for (int a = 0; a < styleNames.length; a++) {
			map.put(styleNames[a], styleValues[a]);
		}
	
		return map;
	}

	public String localize(String textKey, String defaultText) {
		if (this.iwrb == null) {
			return defaultText;
		}
		return this.iwrb.getLocalizedString(textKey, defaultText);
	}

	public Text getHeader(String s) {
		return getStyleText(s, STYLENAME_HEADER);
	}

	public Text getText(String text) {
		return getStyleText(text, STYLENAME_TEXT);
	}

	public Text getSmallText(String text) {
		return getStyleText(text, STYLENAME_SMALL_TEXT);
	}

	public Link getLink(String text) {
		return getStyleLink(new Link(text), STYLENAME_LINK);
	}

	public InterfaceObject getStyledInterface(InterfaceObject obj) {
		return (InterfaceObject) setStyle(obj, STYLENAME_INTERFACE);
	}

	protected CheckBox getCheckBox(String name, String value) {
		return (CheckBox) setStyle(new CheckBox(name,value),STYLENAME_CHECKBOX);
	}

	protected RadioButton getRadioButton(String name, String value) {
		return (RadioButton) setStyle(new RadioButton(name,value),STYLENAME_CHECKBOX);
	}

	protected GenericButton getButton(GenericButton button) {
		button.setHeight("20");
		return (GenericButton) setStyle(button,STYLENAME_INTERFACE_BUTTON);
	}

	protected IWBundle getBundle() {
		return this.iwb;
	}

	protected void setBundle(IWBundle bundle) {
		this.iwb = bundle;
	}

	protected IWResourceBundle getResourceBundle() {
		return this.iwrb;
	}

	protected void setResourceBundle(IWResourceBundle resourceBundle) {
		this.iwrb = resourceBundle;
	}
		
		
	protected Map getStepsMap(IWContext iwc){
		if(stepsMap==null){
			stepsMap=new TreeMap();
		}
		return stepsMap;
	}
		
	protected void addStep(IWContext iwc, int stepId,String stepKey){
		Map stepsMap = getStepsMap(iwc);
		stepsMap.put(new Integer(stepId),stepKey);
	}
	
	protected List getStepsList(IWContext iwc){
		Map stepsMap = getStepsMap(iwc);
		Set collection = stepsMap.keySet();
		List list = new ArrayList();
		for (Iterator iter = collection.iterator(); iter.hasNext();) {
			Object element = iter.next();
			list.add(element);
		}
		return list;
	}
	
	protected int getNextStep(IWContext iwc, int iCurrentStep){
		return getStepByOffset(iwc,iCurrentStep,1);
	}
	
	protected int getPreviousStep(IWContext iwc, int iCurrentStep){
		return getStepByOffset(iwc,iCurrentStep,-1);
	}
	
	protected int getStepByOffset(IWContext iwc, int iCurrentStep, int offset){
		Integer currentStep = new Integer(iCurrentStep);
		
		List stepsList = getStepsList(iwc);
		
		int indexCurrent = stepsList.indexOf(currentStep);
		int nextIndex = indexCurrent+offset;
		Object next = stepsList.get(nextIndex);
		
		return ((Integer)next).intValue();
	}
	protected int getTotalNumberOfSteps(IWContext iwc){
		List stepsList = getStepsList(iwc);
		return stepsList.size();
	}
	
	protected int getStepNumber(IWContext iwc,int iStepId){
		List stepsList = getStepsList(iwc);
		Integer stepObject = new Integer(iStepId);
		return stepsList.indexOf(stepObject)+1;
	}
	
	protected String getStepKey(IWContext iwc,int iStepId){
		Map stepsMap = getStepsMap(iwc);
		Integer stepObject = new Integer(iStepId);
		return (String) stepsMap.get(stepObject);
	}
	
	protected PresentationObject getStepsHeader(IWContext iwc,int stepId){
		
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_STEPSHEADER);
		int stepNumber = getStepNumber(iwc,stepId);
		int totalSteps = getTotalNumberOfSteps(iwc);
		
		String key = getStepKey(iwc,stepId);
		
		Text stepName = getHeader(key);
		Layer stepNameSpan = new Layer(Layer.SPAN);
		stepNameSpan.setStyleClass(STYLENAME_STEPNAME);
		stepNameSpan.add(stepName);
		layer.add(stepNameSpan);
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(localize("step", "Step")).append(" ").append(stepNumber).append(" ").append(localize("of", "of")).append(" ").append(totalSteps);
		Text stepCurrent = getHeader(buffer.toString());
		Layer stepCurrentSpan= new Layer(Layer.SPAN);
		stepCurrentSpan.setStyleClass(STYLENAME_STEPCURRENT);
		stepCurrentSpan.add(stepCurrent);
		layer.add(stepCurrentSpan);
		
		return layer;
	}
	
	protected PresentationObject getButtonsFooter(IWContext iwc){
		return getButtonsFooter(iwc,true,true);
	}
	
	protected PresentationObject getButtonsFooter(IWContext iwc,boolean addPreviousButton,boolean addNextButton){
		
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass(STYLENAME_STEPSFOOTER);
		
		if(addPreviousButton){
			layer.add(getPreviousButton());
		}
		
		if(addNextButton){
			layer.add(getNextButton());
		}
		
		return layer;
	}
	
	protected SubmitButton getNextButton(){
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_NEXT));
		return next;
	}
	
	protected SubmitButton getPreviousButton(){
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PREVIOUS));
		return previous;
	}
	
	protected int parseAction(IWContext iwc) throws RemoteException {
		
		/*int action = this.isIcelandic ? ACTION_STEP_PERSONLOOKUP : ACTION_STEP_PERSONALDETAILS;
		
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			try{
				action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
			}catch(Exception e){e.printStackTrace();}
		}*/
		
		initializeSteps(iwc);
		
		String sFromAction = iwc.getParameter(PARAMETER_FROM_ACTION);
		if(sFromAction!=null){
			int iFromAction = Integer.parseInt(sFromAction);
			String sAction = iwc.getParameter(PARAMETER_ACTION);
			int iAction = Integer.parseInt(sAction);
			if(iAction == ACTION_NEXT){
				return getNextStep(iwc, iFromAction);
			}
			else if(iAction == ACTION_PREVIOUS){
				return getPreviousStep(iwc, iFromAction);
			}
		}
		
		//ACTION_START is the default action:
		
		List stepsList = getStepsList(iwc);	
		Integer firstIndex = (Integer) stepsList.get(0);
		return firstIndex.intValue();
		
	}

	/**
	 * <p>
	 * Meant to be overridden in subclasses to add their steps
	 * </p>
	 * @param iwc
	 */
	protected void initializeSteps(IWContext iwc) {
		// TODO Auto-generated method stub
		
	}

}