/**
 * EpijuvStageAttributes.java
 * 
 *<pre>
 * Revisions:
 * 20181011:  1. Added "attached" as attribute due to changes in DisMELS framework
 * 20190722:  1. Revised logic associated with static collections to reflect new 
 *               superclass paradigm
 * </pre>
 */

package sh.pcod.EpijuvStage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import sh.pcod.AbstractNonEggStageAttributes;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttribute;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttributeDouble;

/**
 * DisMELS class representing attributes for Pacific cod epipelagic juveniles.
*/
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageAttributesInterface.class)
public class EpijuvStageAttributes extends AbstractNonEggStageAttributes {
    
    /** Number of new attributes defined by this class */
    public static final int numNewAttributes = 1;
    /** key for the habitat suitability attribute */
    public static final String PROP_hsi = "habitat suitability index";
    
    // these fields HIDE static fields from superclass and should incorporate ALL information from superclasses
    /** total number of attributes (including superclasses */
    protected static final int numAttributes = AbstractNonEggStageAttributes.numAttributes+EpijuvStageAttributes.numNewAttributes;
    /** keys used to define attributes */
    protected static final Set<String> keys = new LinkedHashSet<>(2*EpijuvStageAttributes.numAttributes);
    /** map of keys to attributes */
    protected static final Map<String,IBMAttribute> mapAttributes = new HashMap<>(2*EpijuvStageAttributes.numAttributes);
    /** keys, not including typeName */
    protected static final String[] aKeys      = new String[EpijuvStageAttributes.numAttributes-1];
    /** array of classes associated with attributes */
    protected static final Class[]  classes    = new Class[EpijuvStageAttributes.numAttributes];
    /** array of Strings with short names for attributes */
    protected static final String[] shortNames = new String[EpijuvStageAttributes.numAttributes];
   
    /** flag indicating whether or not static collections have been created */
    private static boolean createKeys = true;
    /** logger for class */
    private static final Logger logger = Logger.getLogger(EpijuvStageAttributes.class.getName());
    
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     */
    public EpijuvStageAttributes(){
        super("NULL");
        finishInstantiation();
    }
    
    /**
     * Creates a new attributes instance with type name 'typeName'.
     */
    public EpijuvStageAttributes(String typeName) {
        super(typeName);
        finishInstantiation();
    }
    
    /**
     * This method adds default values for the new attributes to the superclass field "mapValues".
     * <p>
     * When the first instance of this class is created, this method also fills in 
     * the static fields "keys" and "mapAttributes" with keys and attributes from the 
     * superclass and from this class.
     */
    private void finishInstantiation(){
        if (createKeys){
            createKeys = false;//do this once only
            //set static field information from superclass
            EpijuvStageAttributes.keys.addAll(AbstractNonEggStageAttributes.keys);//add from superclass
            EpijuvStageAttributes.mapAttributes.putAll(AbstractNonEggStageAttributes.mapAttributes);//add from superclass
            //add static information from this class
            String key;
            key = PROP_hsi; EpijuvStageAttributes.keys.add(key); EpijuvStageAttributes.mapAttributes.put(key,new IBMAttributeDouble(key,"hsi"));
            
            Iterator<String> it = EpijuvStageAttributes.keys.iterator();
            int j = 0; it.next();//skip typeName
            while (it.hasNext()) EpijuvStageAttributes.aKeys[j++] = it.next();
        }
        //set instance information
        mapValues.put(PROP_hsi,new Double(-1));;//add to superclass values
    }

    /**
     * Returns a deep copy of the instance.  Values are copied.  
     * Any listeners on 'this' are not(?) copied, so these need to be hooked up.
     * @return - the clone.
     */
    @Override
    public Object clone() {
        EpijuvStageAttributes clone = new EpijuvStageAttributes(typeName);
        for (String key: EpijuvStageAttributes.keys) clone.setValue(key,this.getValue(key));
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
    public EpijuvStageAttributes createInstance(final String[] strv) {
        EpijuvStageAttributes atts = new EpijuvStageAttributes(strv[0]);//this sets atts.typeName
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
        ArrayList a = new ArrayList();
        for (String key: EpijuvStageAttributes.keys) a.add(getValue(key));
        return a;
    }

    /**
     * Returns Class types for all attributes (including typeName) as a Class[]
     * in the order the allKeys are defined.
     * 
     * @return 
     */
    @Override
    public Class[] getClasses() {
        if (EpijuvStageAttributes.classes[0]==null){
            int j = 0;
            for (String key: EpijuvStageAttributes.keys){
                EpijuvStageAttributes.classes[j++] = EpijuvStageAttributes.mapAttributes.get(key).getValueClass();
            }
        }
        return EpijuvStageAttributes.classes;
    }

    /**
     * Returns keys for all attributes excluding typeName as a String[]
     * in the order the keys are defined.
     * 
     * @return 
     */
    @Override
    public String[] getKeys() {        
        return EpijuvStageAttributes.aKeys;
    }

    /**
     * Returns short names for all attributes (including typeName) as a String[]
     * in the order the allKeys are defined.
     * 
     * @return 
     */
    @Override
    public String[] getShortNames() {
        if (EpijuvStageAttributes.shortNames[0]==null){
            int j = 0;
            for (String key: EpijuvStageAttributes.keys){
                EpijuvStageAttributes.shortNames[j++] = EpijuvStageAttributes.mapAttributes.get(key).shortName;
            }
        }
        return EpijuvStageAttributes.shortNames;
    }
    
   /**
     * Returns a CSV string representation of the attribute values.
     * 
     *@return - CSV string attribute values
     */
    @Override
    public String getCSV() {
        String str = typeName;
        Iterator<String> it = EpijuvStageAttributes.keys.iterator();
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
        Iterator<String> it = EpijuvStageAttributes.keys.iterator();
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
        Iterator<String> it = EpijuvStageAttributes.keys.iterator();
        String str = EpijuvStageAttributes.mapAttributes.get(it.next()).shortName;//this is "typeName"
        while (it.hasNext())  str = str+cc+EpijuvStageAttributes.mapAttributes.get(it.next()).shortName;
        return str;
    }
    
    /**
     * Sets attribute values to those of input String[].
     * @param strv - String[] of attribute values.
     */
    @Override
    public void setValues(final String[] strv) {
        int j = 1;
        try {
            Iterator<String> it = EpijuvStageAttributes.keys.iterator();
            it.next();//skip typeName
            while (it.hasNext()) setValueFromString(it.next(),strv[j++]);
        } catch (java.lang.IndexOutOfBoundsException ex) {
            //@TODO: should throw an exception here that identifies the problem
//            String[] aKeys = new String[keys.size()];
//            aKeys = keys.toArray(aKeys);
                String str = "Missing attribute value for "+EpijuvStageAttributes.aKeys[j]+".\n"+
                             "Prior values are ";
                for (int i=0;i<(j);i++) str = str+strv[i]+" ";
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        str,
                        "Error setting attribute values:",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                throw ex;
        } catch (java.lang.NumberFormatException ex) {
//            String[] aKeys = new String[keys.size()];
//            aKeys = keys.toArray(aKeys);
            String str = "Bad attribute value for "+EpijuvStageAttributes.aKeys[j-2]+".\n"+
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
        IBMAttribute att = EpijuvStageAttributes.mapAttributes.get(key);
        att.setValue(val);
        String str = att.getValueAsString();
        return str;
    }
    
    @Override
    public void setValueFromString(String key, String value) throws NumberFormatException {
        if (!key.equals(PROP_typeName)){
            IBMAttribute att = EpijuvStageAttributes.mapAttributes.get(key);
            att.parseValue(value);
            setValue(key,att.getValue());
        }
    }
}
