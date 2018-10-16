/*
 * YSLStageParameters.java
 *
 * Revised on 10/11/2018:
 *   Removed FCAT_Development and development functions category.
 *   Removed EggAscensionRate function as option for vertical movement.
 *
 */

package sh.pcod.YSLStage;

import java.beans.PropertyChangeSupport;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.IBMFunctions.Mortality.ConstantMortalityRate;
import wts.models.DisMELS.IBMFunctions.Mortality.InversePowerLawMortalityRate;
import wts.models.DisMELS.IBMFunctions.Movement.DielVerticalMigration_FixedDepthRanges;
import wts.models.DisMELS.IBMFunctions.SwimmingBehavior.ConstantMovementRateFunction;
import wts.models.DisMELS.framework.AbstractLHSParameters;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameter;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameterBoolean;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameterDouble;
import wts.models.DisMELS.framework.LifeStageParametersInterface;

/**
 * DisMELS class representing parameters for Pacific cod eggs
 * for the GOA IERP Modeling project.
 * 
 * This class uses the IBMParameters/IBMFunctions approach to specifying stage-specific parameters.
 * 
 * @author William Stockhausen/Sarah Hinckley
 */
@ServiceProvider(service=LifeStageParametersInterface.class)
public class YSLStageParameters extends AbstractLHSParameters {
    
    public static final long serialVersionUID = 1L;
    
    /** the number of IBMParameter objects defined in the class */
    public static final int numParams = 5;
    public static final String PARAM_isSuperIndividual      = "is a super-individual?";
    public static final String PARAM_horizRWP               = "horizontal random walk parameter [m^2]/[s]";
    public static final String PARAM_minStageDuration       = "min stage duration [d]";
    public static final String PARAM_maxStageDuration       = "max stage duration [d]";
    public static final String PARAM_useRandomTransitions   = "use random transitions";
    /** The 'keys' used to store the ibm parameters */
    protected static final Set<String> setOfParamKeys = new LinkedHashSet<>(2*numParams);
    
    /** the number of IBMFunction categories defined in the class */
    public static final int numFunctionCats = 3;
    public static final String FCAT_Mortality        = "mortality";
    public static final String FCAT_VerticalMovement = "vertical movement";
    public static final String FCAT_VerticalVelocity = "vertical velocity";    
    /** The 'keys' used to store the ibm functions */
    protected static final Set<String> setOfFunctionCategories = new LinkedHashSet<>(2*numFunctionCats);
    
    private static final Logger logger = Logger.getLogger(YSLStageParameters.class.getName());
    
    /** Utility field used by bound properties.  */
    private transient PropertyChangeSupport propertySupport;
    
    /**
     * Creates a new instance of YSLStageParameters.
     */
    public YSLStageParameters() {
        super("",numParams,numFunctionCats);
        createMapToParameters();
        createMapToPotentialFunctions();
        propertySupport =  new PropertyChangeSupport(this);
    }
    
    /**
     * Creates a new instance of YSLStageParameters
     */
    public YSLStageParameters(String typeName) {
        super(typeName,numParams,numFunctionCats);
        createMapToParameters();
        createMapToPotentialFunctions();
        propertySupport =  new PropertyChangeSupport(this);
    }
    
    /**
     * This creates the basic parameters mapParams.
     */
    @Override
    protected final void createMapToParameters() {
        String key;
        key = PARAM_isSuperIndividual;    setOfParamKeys.add(key); mapParams.put(key,new IBMParameterBoolean(key,key,false));
        key = PARAM_horizRWP;             setOfParamKeys.add(key); mapParams.put(key,new IBMParameterDouble(key,key,new Double(0)));
        key = PARAM_minStageDuration;     setOfParamKeys.add(key); mapParams.put(key,new IBMParameterDouble(key,key,new Double(0)));
        key = PARAM_maxStageDuration;     setOfParamKeys.add(key); mapParams.put(key,new IBMParameterDouble(key,key,new Double(365)));
        key = PARAM_useRandomTransitions; setOfParamKeys.add(key); mapParams.put(key,new IBMParameterBoolean(key,key,false));
    }

    @Override
    protected final void createMapToPotentialFunctions() {
        //create the set of function category keys for this class
        setOfFunctionCategories.add(FCAT_Mortality);
        setOfFunctionCategories.add(FCAT_VerticalMovement);
        setOfFunctionCategories.add(FCAT_VerticalVelocity);
        
        //create the map from function categories to potential functions in each category
        String cat; Map<String,IBMFunctionInterface> mapOfPotentialFunctions; IBMFunctionInterface ifi;
        cat = FCAT_Mortality;  
        mapOfPotentialFunctions = new LinkedHashMap<>(4); mapOfPotentialFunctionsByCategory.put(cat,mapOfPotentialFunctions);
        ifi = new ConstantMortalityRate(); mapOfPotentialFunctions.put(ifi.getFunctionName(),ifi);
        ifi = new InversePowerLawMortalityRate(); mapOfPotentialFunctions.put(ifi.getFunctionName(),ifi);
        
        cat = FCAT_VerticalMovement;  
        mapOfPotentialFunctions = new LinkedHashMap<>(4); mapOfPotentialFunctionsByCategory.put(cat,mapOfPotentialFunctions);
        ifi = new DielVerticalMigration_FixedDepthRanges(); mapOfPotentialFunctions.put(ifi.getFunctionName(),ifi);
        
        cat = FCAT_VerticalVelocity;  
        mapOfPotentialFunctions = new LinkedHashMap<>(2); mapOfPotentialFunctionsByCategory.put(cat,mapOfPotentialFunctions);
        ifi = new ConstantMovementRateFunction(); mapOfPotentialFunctions.put(ifi.getFunctionName(),ifi);
    }
        
    /**
     * Returns a deep copy of the instance.  Values are copied.  
     * Any listeners on 'this' are not(?) copied, so these need to be hooked up.
     * @return - the clone.
     */
    @Override
    public Object clone() {
        YSLStageParameters clone = null;
        try {
            clone = (YSLStageParameters) super.clone();
            for (String pKey: setOfParamKeys) {
                clone.setValue(pKey,this.getValue(pKey));
            }
            for (String fcKey: setOfFunctionCategories) {
                Set<String> fKeys = this.getIBMFunctionKeysByCategory(fcKey);
                IBMFunctionInterface sfi = this.getSelectedIBMFunctionForCategory(fcKey);
                for (String fKey: fKeys){
                    IBMFunctionInterface tfi = this.getIBMFunction(fcKey, fKey);
                    IBMFunctionInterface cfi = clone.getIBMFunction(fcKey,fKey);
                    Set<String> pKeys = tfi.getParameterNames();
                    for (String pKey: pKeys) {
                        cfi.setParameterValue(pKey, tfi.getParameter(pKey).getValue());
                    }
                    if (sfi==tfi) clone.setSelectedIBMFunctionForCategory(fcKey, fKey);
                }
            }
            clone.propertySupport = new PropertyChangeSupport(clone);
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return clone;
    }

    /**
     * This method is not supported in this implementation.
     *
     * @param strv - array of values (as Strings) used to create the new instance. 
     *              This should be typeName followed by parameter value (as Strings)
     *              in the same order as the keys.
     * @return 
     */
    @Override
    public YSLStageParameters createInstance(final String[] strv) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Returns a CSV string representation of the parameter values.
     * This method should be overriden by subclasses that add additional parameters, 
     * possibly calling super.getCSV() to get an initial csv string to which 
     * additional field values could be appended.
     * 
     *@return - CSV string parameter values
     */
    @Override
    public String getCSV() {
        String str = typeName;
        for (String key: setOfParamKeys) str = str+cc+getIBMParameter(key).getValueAsString();
        return str;
    }
                
    /**
     * Returns the comma-delimited string corresponding to the parameters
     * to be used as a header for a csv file.  
     * This should be overriden by subclasses that add additional parameters, 
     * possibly calling super.getCSVHeader() to get an initial header string 
     * to which additional field names could be appended.
     * Use getCSV() to get the string of actual parameter values.
     *
     *@return - String of CSV header names
     */
    @Override
    public String getCSVHeader() {
        String str = "LHS type name";
        for (String key: setOfParamKeys) str = str+cc+key;
        return str;
    }

    /**
     * Gets the parameter keys.
     * 
     * @return - keys as String array.
     */
    @Override
    public String[] getKeys(){
        String[] strv = new String[setOfParamKeys.size()];
        return setOfParamKeys.toArray(strv);
    }

    /**
     * Sets parameter value identified by the key and fires a property change.
     * @param key   - key identifying attribute to be set
     * @param value - value to set
     */
    @Override
    public void setValue(String key, Object value) {
        if (mapParams.containsKey(key)) {
            IBMParameter p = mapParams.get(key);
            Object old = p.getValue();
            p.setValue(value);
            propertySupport.firePropertyChange(key,old,value);
        }
    }

    /**
     * Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertySupport.addPropertyChangeListener(l);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertySupport.removePropertyChangeListener(l);
    }
}
