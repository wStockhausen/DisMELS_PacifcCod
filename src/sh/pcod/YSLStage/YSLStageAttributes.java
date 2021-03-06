/*
 * YSLStageAttributes.java
 *
 * Updated 10/11/2018:
 *   Added "attached" as attribute due to changes in DisMELS framework
 * 20210208: 1. extends AbstractLarvalAttributes.
 * 20210209: 1. added PROPs for YSA, PNR, and PrNotFed.
 *
 */

package sh.pcod.YSLStage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import sh.pcod.AbstractLarvalAttributes;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttribute;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttributeDouble;
import static wts.models.DisMELS.framework.LifeStageAttributesInterface.PROP_typeName;
import static wts.models.DisMELS.framework.LifeStageDataInterface.cc;

/**
 * DisMELS class representing attributes for Pacific cod yolk-sac larvae.
 */
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageAttributesInterface.class)
public class YSLStageAttributes extends AbstractLarvalAttributes {
    
    /** Number of new attributes defined by this class */
    public static final int numNewAttributes = 3;
    /** key for the habitat suitability attribute */
    public static final String PROP_progYSA = "indicator for yolk-sac absorption";
    /** key for the total length attribute */
    public static final String PROP_progPNR  = "indicator for point-of-no return";
    /** key for the probability of NOT having fed attribute */
    public static final String PROP_prNotFed  = "probability of not having fed";
    
    /** these fields HIDE static fields from superclass and should incorporate ALL information from superclasses */
    protected static final int numAttributes = AbstractLarvalAttributes.numAttributes+numNewAttributes;
    protected static final Set<String> keys = new LinkedHashSet<>(2*numAttributes);
    protected static final Map<String,IBMAttribute> mapAttributes = new HashMap<>(2*numAttributes);
    protected static final String[] aKeys      = new String[numAttributes-1];//does not include typeName
    protected static final Class[]  classes    = new Class[numAttributes];
    protected static final String[] shortNames = new String[numAttributes];
   
    private static final Logger logger = Logger.getLogger(YSLStageAttributes.class.getName());
    
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     */
    public YSLStageAttributes(){
        super("NULL");
        finishInstantiation();
    }
    
    /**
     * Creates a new attributes instance with type name 'typeName'.
     */
    public YSLStageAttributes(String typeName) {
        super(typeName);
        finishInstantiation();
    }
    
    /**
     * This method adds default values for the new attributes to the superclass field "mapValues".
     * 
     * When the first instance of this class is created, this method also fills in 
     * the static fields "keys" and "mapAttributes" with keys and attributes from the 
     * superclass and from this class.
     */
    private void finishInstantiation(){
        if (keys.isEmpty()){
            //set static field information
            keys.addAll(AbstractLarvalAttributes.keys);//add from superclass
            mapAttributes.putAll(AbstractLarvalAttributes.mapAttributes);//add from superclass
            String key;
            key = PROP_progYSA;  keys.add(key); mapAttributes.put(key,new IBMAttributeDouble(key,"progYSA"));
            key = PROP_progPNR;  keys.add(key); mapAttributes.put(key,new IBMAttributeDouble(key,"progPNR"));
            key = PROP_prNotFed; keys.add(key); mapAttributes.put(key,new IBMAttributeDouble(key,"prNotFed"));
            
            Iterator<String> it = keys.iterator();
            int j = 0; it.next();//skip typeName
            while (it.hasNext()) aKeys[j++] = it.next();
        }
        //set instance information
        Map<String,Object> tmpMapValues = new HashMap<>(2*numAttributes);
        tmpMapValues.putAll(mapValues);//copy from super
        tmpMapValues.put(PROP_progYSA,  new Double(0));//add to superclass values
        tmpMapValues.put(PROP_progPNR,  new Double(0));
        tmpMapValues.put(PROP_prNotFed, new Double(1));
        mapValues = tmpMapValues;//assign to super
    }
    
    /**
     * Returns a deep copy of the instance.  Values are copied.  
     * Any listeners on 'this' are not(?) copied, so these need to be hooked up.
     * @return - the clone.
     */
    @Override
    public Object clone() {
        YSLStageAttributes clone = new YSLStageAttributes(typeName);
        for (String key: keys) clone.setValue(key,this.getValue(key));
        return clone;
    }

    /**
     * Returns a new instance constructed from the values of the string[].
     * The first value in the string vector must be the type name.
     * Values are set internally by calling setValues(strv) on the new instance.
     * @param strv - vector of values (as Strings) 
     * @return - the new instance
     */
    @Override
    public YSLStageAttributes createInstance(final String[] strv) {
        YSLStageAttributes atts = new YSLStageAttributes(strv[0]);//this sets atts.typeName
        atts.setValues(strv);
        return atts;
    }
    

    /**
     * Returns the attribute values as an ArrayList (including typeName).
     * 
     * @return 
     */
    @Override
    public ArrayList getArrayList() {
        ArrayList a = new ArrayList(keys.size());
        a.add(typeName);
        Iterator<String> it = keys.iterator();
        it.next();//skip PROP_typeName
        while (it.hasNext()) a.add(getValue(it.next()));
        return a;
    }

    /**
     * Returns the attributes values (not including typeName) as an Object[].
     * 
     * @return 
     */
    @Override
    public Object[] getAttributes() {
        Object[] atts = new Object[numAttributes-1];
        int j = 0;
        Iterator<String> it = keys.iterator();
        it.next();//skip PROP_typeName
        while (it.hasNext()) atts[j++] = getValue(it.next()); 
        return atts;
    }
    
   /**
     * Returns a CSV string representation of the attribute values.
     * 
     *@return - CSV string attribute values
     */
    @Override
    public String getCSV() {
        String str = typeName;
        Iterator<String> it = keys.iterator();
        it.next();//skip typeName
        while (it.hasNext()) {
            String key = it.next();
            str = str+cc+getValueAsString(key);
        }
        return str;
    }
                
    /**
     * Returns the comma-delimited string corresponding to the attributes
     * to be used as a header for a csv file.  
     * Use getCSV() to get the string of actual attribute values.
     *
     *@return - String of CSV header names
     */
    @Override
    public String getCSVHeader() {
        Iterator<String> it = keys.iterator();
        String str = it.next();//typeName
        while (it.hasNext()) str = str+cc+it.next();
        return str;
    }
                
    /**
     * Returns the comma-delimited string corresponding to the attributes
     * to be used as a header for a csv file.  
     *
     *@return - String of CSV header names (short style)
     */
    @Override
    public String getCSVHeaderShortNames() {
        Iterator<String> it = keys.iterator();
        String str = mapAttributes.get(it.next()).shortName;//this is "typeName"
        while (it.hasNext())  str = str+cc+mapAttributes.get(it.next()).shortName;
        return str;
    }
    
    /**
     * Returns Class types for all attributes (including typeName) as a Class[]
     * in the order the allKeys are defined.
     * 
     * @return 
     */
    @Override
    public Class[] getClasses() {
        if (classes[0]==null){
            int j = 0;
            for (String key: keys){
                classes[j++] = mapAttributes.get(key).getValueClass();
            }
        }
        return classes;
    }

    /**
     * Returns keys for all attributes excluding typeName as a String[]
     * in the order the keys are defined.
     * 
     * @return 
     */
    @Override
    public String[] getKeys() {        
        return aKeys;
    }

    /**
     * Returns short names for all attributes (including typeName) as a String[]
     * in the order the allKeys are defined.
     * 
     * @return 
     */
    @Override
    public String[] getShortNames() {
        if (shortNames[0]==null){
            int j = 0;
            for (String key: keys){
                shortNames[j++] = mapAttributes.get(key).shortName;
            }
        }
        return shortNames;
    }
    
    /**
     * Sets attribute values to those of input String[].
     * @param strv - String[] of attribute values.
     */
    @Override
    public void setValues(final String[] strv) {
        int j = 1;
        try {
            Iterator<String> it = keys.iterator();
            it.next();//skip typeName
            while (it.hasNext()) setValueFromString(it.next(),strv[j++]);
        } catch (java.lang.IndexOutOfBoundsException ex) {
            //@TODO: should throw an exception here that identifies the problem
            String[] aKeys = new String[keys.size()];
            aKeys = keys.toArray(aKeys);
                String str = "Missing attribute value for "+aKeys[j]+".\n"+
                             "Prior values are ";
                for (int i=0;i<(j);i++) str = str+strv[i]+" ";
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        str,
                        "Error setting attribute values:",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                throw ex;
        } catch (java.lang.NumberFormatException ex) {
            String[] aKeys = new String[keys.size()];
            aKeys = keys.toArray(aKeys);
            String str = "Bad attribute value for "+aKeys[j-2]+".\n"+
                         "Value was '"+strv[j-1]+"'.\n"+
                         "Entry was '";
            try {
                for (int i=0;i<(strv.length-1);i++) {
                    if ((strv[i]!=null)&&(!strv[i].isEmpty())) {
                        str = str+strv[i]+", ";
                    } else {
                        str = str+"<missing_value>, ";
                    }
                }
                if ((strv[strv.length-1]!=null)&&(!strv[strv.length-1].isEmpty())) {
                    str = str+strv[strv.length-1]+"'.";
                } else {
                    str = str+"<missing_value>'.";
                }
            }  catch (java.lang.IndexOutOfBoundsException ex1) {
                //do nothing
            }
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    str,
                    "Error setting attribute values:",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            throw ex;
        }
    }
    
    @Override
    public String getValueAsString(String key){
        Object val = getValue(key);
        IBMAttribute att = mapAttributes.get(key);
        att.setValue(val);
        String str = att.getValueAsString();
        return str;
    }
    
    @Override
    public void setValueFromString(String key, String value) throws NumberFormatException {
        if (!key.equals(PROP_typeName)){
            IBMAttribute att = mapAttributes.get(key);
            att.parseValue(value);
            setValue(key,att.getValue());
        }
    }
}
