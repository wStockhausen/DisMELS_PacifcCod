/*
 * FDLpfStageAttributes.java
 */

package sh.pcod.FDLpfStage;

import java.util.*;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import sh.pcod.FDLStage.FDLStageAttributes;
import wts.models.DisMELS.framework.AbstractLHSAttributes;

import wts.models.DisMELS.framework.IBMAttributes.IBMAttribute;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttributeDouble;

/**
 * DisMELS class representing attributes for Pacific cod postflexion feeding larvae.
 * @TODO: remove PROP_density and PROP_devStage(?) and associated variables.
 */
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageAttributesInterface.class)
public class FDLpfStageAttributes extends AbstractLHSAttributes {
    
    /** Number of attributes defined by this class (including typeName) */
    public static final int numNewAttributes = 6;
   //SH_NEW
    public static final String PROP_devStage = FDLStageAttributes.PROP_devStage;
    //public static final String PROP_diameter = "egg diameter";
    //SH_NEW
    public static final String PROP_length = "standard length";
    
    public static final String PROP_density  = "egg density";
    public static final String PROP_temperature = "temperature deg C";
    public static final String PROP_salinity    = "salinity";
    public static final String PROP_rho         = "in situ density";
    
    protected static final Set<String> newKeys = new LinkedHashSet<>((int)(2*numNewAttributes));
    protected static final Set<String> allKeys = new LinkedHashSet<>((int)(2*(numAttributes+numNewAttributes)));
    protected static final Map<String,IBMAttribute> mapAllAttributes = new HashMap<>((int)(2*(numAttributes+numNewAttributes)));
    protected static final String[] aKeys      = new String[numAttributes+numNewAttributes-1];//does not include typeName
    protected static final Class[]  classes    = new Class[numAttributes+numNewAttributes];
    protected static final String[] shortNames = new String[numAttributes+numNewAttributes];
   
    private static final Logger logger = Logger.getLogger(FDLpfStageAttributes.class.getName());
    
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     */
    public FDLpfStageAttributes(){
        super("NULL");
        finishInstantiation();
    }
    
    /**
     * Creates a new attributes instance with type name 'typeName'.
     */
    public FDLpfStageAttributes(String typeName) {
        super(typeName);
        finishInstantiation();
    }
    
    /**
     * Returns a deep copy of the instance.  Values are copied.  
     * Any listeners on 'this' are not(?) copied, so these need to be hooked up.
     * @return - the clone.
     */
    @Override
    public Object clone() {
        FDLpfStageAttributes clone = new FDLpfStageAttributes(typeName);
        for (String key: allKeys) clone.setValue(key,this.getValue(key));
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
    public FDLpfStageAttributes createInstance(final String[] strv) {
        FDLpfStageAttributes atts = new FDLpfStageAttributes(strv[0]);//this sets atts.typeName
        atts.setValues(strv);
        return atts;
    }
    
    private void finishInstantiation(){
        if (newKeys.isEmpty()){
            //set static field information
            mapAllAttributes.putAll(AbstractLHSAttributes.mapAttributes);//add from superclass
            String key;
            key = PROP_devStage;   newKeys.add(key); mapAllAttributes.put(key,new IBMAttributeDouble(key,"devStage"));
           //key = PROP_diameter;   newKeys.add(key); mapAllAttributes.put(key,new IBMAttributeDouble(key,"diam"));
            //SH_NEW
            key = PROP_length;   newKeys.add(key); mapAllAttributes.put(key,new IBMAttributeDouble(key,"length"));                    
            key = PROP_density;    newKeys.add(key); mapAllAttributes.put(key,new IBMAttributeDouble(key,"density"));
            key = PROP_temperature;newKeys.add(key); mapAllAttributes.put(key,new IBMAttributeDouble(key,"temp"));
            key = PROP_salinity;   newKeys.add(key); mapAllAttributes.put(key,new IBMAttributeDouble(key,"sal"));
            key = PROP_rho;        newKeys.add(key); mapAllAttributes.put(key,new IBMAttributeDouble(key,"rho"));
            allKeys.addAll(AbstractLHSAttributes.keys);//add from superclass
            allKeys.addAll(newKeys);//add from this class
            Iterator<String> it = allKeys.iterator();
            int j = 0; it.next();//skip typeName
            while (it.hasNext()) aKeys[j++] = it.next();
        }
        //set instance information
        Map<String,Object> tmpMapValues = new HashMap<>((int)(2*(numNewAttributes+numAttributes)));
        tmpMapValues.putAll(mapValues);//copy from super
        tmpMapValues.put(PROP_devStage,   new Double(0));
       //tmpMapValues.put(PROP_diameter,   new Double(0));
        //SH_NEW
        tmpMapValues.put(PROP_length,   new Double(0));
        tmpMapValues.put(PROP_density,    new Double(0));
        tmpMapValues.put(PROP_temperature,new Double(-1));
        tmpMapValues.put(PROP_salinity,   new Double(-1));
        tmpMapValues.put(PROP_rho,        new Double(-1));
        mapValues = tmpMapValues;//assign to super
    }

    /**
     * Returns the attribute values as an ArrayList (including typeName).
     * 
     * @return 
     */
    @Override
    public ArrayList getArrayList() {
        ArrayList a = super.getArrayList();
        for (String key: newKeys) a.add(getValue(key));
        return a;
    }

    /**
     * Returns the attributes values (not including typeName) as an Object[].
     * 
     * @return 
     */
    @Override
    public Object[] getAttributes() {
        Object[] atts = new Object[numNewAttributes+AbstractLHSAttributes.numAttributes-1];
        int j = 0;
        Iterator<String> it = allKeys.iterator();
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
        String str = super.getCSV();
        Iterator<String> it = newKeys.iterator();
        while (it.hasNext()) str = str+cc+getValueAsString(it.next());
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
        String str = super.getCSVHeader();
        Iterator<String> it = newKeys.iterator();
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
        String str = super.getCSVHeaderShortNames();
        Iterator<String> it = newKeys.iterator();
        while (it.hasNext()) str = str+cc+mapAllAttributes.get(it.next()).shortName;
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
            for (String key: allKeys){
                classes[j++] = mapAllAttributes.get(key).getValueClass();
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
            for (String key: allKeys){
                shortNames[j++] = mapAllAttributes.get(key).shortName;
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
        super.setValues(strv);//set the standard attribute values
        //set the values of the new attributes
        int j = AbstractLHSAttributes.numAttributes;
        try {
            for (String key: newKeys) setValueFromString(key,strv[j++]);
        } catch (java.lang.IndexOutOfBoundsException ex) {
            //@TODO: should throw an exception here that identifies the problem
            String[] aKeys = new String[FDLpfStageAttributes.allKeys.size()];
            aKeys = FDLpfStageAttributes.allKeys.toArray(aKeys);
                String str = "Missing attribute value for "+aKeys[j-1]+".\n"+
                             "Prior values are ";
                for (int i=0;i<(j);i++) str = str+strv[i]+" ";
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        str,
                        "Error setting attribute values:",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                throw ex;
        } catch (java.lang.NumberFormatException ex) {
            String[] aKeys = new String[FDLpfStageAttributes.allKeys.size()];
            aKeys = FDLpfStageAttributes.allKeys.toArray(aKeys);
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
        IBMAttribute att = mapAllAttributes.get(key);
        att.setValue(val);
        String str = att.getValueAsString();
        return str;
    }
    
    @Override
    public void setValueFromString(String key, String value) throws NumberFormatException {
        if (!key.equals(PROP_typeName)){
            IBMAttribute att = mapAllAttributes.get(key);
            att.parseValue(value);
            setValue(key,att.getValue());
        }
    }
}
