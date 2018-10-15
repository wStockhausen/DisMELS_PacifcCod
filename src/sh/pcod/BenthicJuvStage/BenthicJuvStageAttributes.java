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
    
    private static final Logger logger = Logger.getLogger(BenthicJuvStageAttributes.class.getName());
    
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     */
    public BenthicJuvStageAttributes(){
        super("NULL");
    }
    
    /**
     * Creates a new attributes instance with type name 'typeName'.
     */
    public BenthicJuvStageAttributes(String typeName) {
        super(typeName);
    }
    
    /**
     * Returns a deep copy of the instance.  Values are copied.  
     * Any listeners on 'this' are not(?) copied, so these need to be hooked up.
     * @return - the clone.
     */
    @Override
    public Object clone() {
        BenthicJuvStageAttributes clone = new BenthicJuvStageAttributes(typeName);
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
    public BenthicJuvStageAttributes createInstance(final String[] strv) {
        BenthicJuvStageAttributes atts = new BenthicJuvStageAttributes(strv[0]);//this sets atts.typeName
        atts.setValues(strv);
        return atts;
    }
    
}
