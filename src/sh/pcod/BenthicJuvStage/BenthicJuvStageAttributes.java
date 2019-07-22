/*
 * BenthicJuvStageAttributes.java
 *
 * Updated 10/11/2018:
 *   Added "attached" as attribute due to changes in DisMELS framework
 *
 */

package sh.pcod.BenthicJuvStage;

import java.util.*;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import sh.pcod.AbstractNonEggStageAttributes;
import wts.models.DisMELS.framework.AbstractLHSAttributes;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttribute;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttributeBoolean;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttributeDouble;

/**
 * DisMELS class representing attributes for the Pacific cod benthic juvenile stage.
 * @TODO: remove PROP_density and PROP_devStage(?) and associated variables.
 */
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageAttributesInterface.class)
public class BenthicJuvStageAttributes extends AbstractNonEggStageAttributes {
    
    /** Number of new attributes defined by this class */
    public static final int numNewAttributes = 1;
    public static final String PROP_hsi = "habitat suitability index";
    
    /** these fields HIDE static fields from superclass and should incorporate ALL information from superclases */
    protected static final int numAttributes = AbstractNonEggStageAttributes.numAttributes+numNewAttributes;
    protected static final Set<String> keys = new LinkedHashSet<>(2*numAttributes);
    protected static final Map<String,IBMAttribute> mapAttributes = new HashMap<>(2*numAttributes);
    protected static final String[] aKeys      = new String[numAttributes-1];//does not include typeName
    protected static final Class[]  classes    = new Class[numAttributes];
    protected static final String[] shortNames = new String[numAttributes];
   
    private static final Logger logger = Logger.getLogger(BenthicJuvStageAttributes.class.getName());
    
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     */
    public BenthicJuvStageAttributes(){
        super("NULL");
        finishInstantiation();
    }
    
    /**
     * Creates a new attributes instance with type name 'typeName'.
     */
    public BenthicJuvStageAttributes(String typeName) {
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
            keys.addAll(AbstractNonEggStageAttributes.keys);//add from superclass
            mapAttributes.putAll(AbstractNonEggStageAttributes.mapAttributes);//add from superclass
            String key;
            key = PROP_hsi; keys.add(key); mapAttributes.put(key,new IBMAttributeDouble(key,"hsi"));
            
            Iterator<String> it = keys.iterator();
            int j = 0; it.next();//skip typeName
            while (it.hasNext()) aKeys[j++] = it.next();
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
        BenthicJuvStageAttributes clone = new BenthicJuvStageAttributes(typeName);
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
    public BenthicJuvStageAttributes createInstance(final String[] strv) {
        BenthicJuvStageAttributes atts = new BenthicJuvStageAttributes(strv[0]);//this sets atts.typeName
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
        ArrayList a = super.getArrayList();
        for (String key: keys) a.add(getValue(key));
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
