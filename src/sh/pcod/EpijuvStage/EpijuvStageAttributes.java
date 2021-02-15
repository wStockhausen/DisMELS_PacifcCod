/**
 * EpijuvStageAttributes.java
 * 
 *<pre>
 * Revisions:
 * 20181011:  1. Added "attached" as attribute due to changes in DisMELS framework
 * 20190722:  1. Revised logic associated with static collections to reflect new 
 *               superclass paradigm
 * 20210205:  1. Added total length and wet weight attributes.
 * 20210206:  1. Refactored to extend new AbstractJuvenileAttributes class rather than
 *                 old AbstractNonEggStageAttributes.
 * </pre>
 */

package sh.pcod.EpijuvStage;

import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import sh.pcod.AbstractJuvenileAttributes;

/**
 * DisMELS class representing attributes for Pacific cod epipelagic juveniles.
 * 
 * In its current implementation, this class simply provides a concrete realization
 * of the AbstractJuvenileAttributes class. It inherits all behavior from the abstract class
 * and adds nothing new.
 * 
 * @author William Stockhausen
*/
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageAttributesInterface.class)
public class EpijuvStageAttributes extends AbstractJuvenileAttributes {
    
    /** Number of new attributes defined by this class */
    public static final int numNewAttributes = 0;
    
    /** logger for class */
    private static final Logger logger = Logger.getLogger(EpijuvStageAttributes.class.getName());
    
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     */
    public EpijuvStageAttributes(){
        super("NULL");
//        finishInstantiation();
    }
    
    /**
     * Creates a new attributes instance with type name 'typeName'.
     */
    public EpijuvStageAttributes(String typeName) {
        super(typeName);
//        finishInstantiation();
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
}
