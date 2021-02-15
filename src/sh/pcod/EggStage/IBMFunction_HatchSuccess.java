/*
 * IBMFunction_HatchSuccess.java
 */
package sh.pcod.EggStage;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMMortalityFunctionInterface;

/**
 *
 * @author WilliamStockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMMortalityFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)

public class IBMFunction_HatchSuccess extends AbstractIBMFunction implements IBMMortalityFunctionInterface {
    public static final String DEFAULT_type = "Mortality";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Pacific cod egg hatch sucess rate";
    /** function description */
    public static final String DEFAULT_descr = "temperature-dependent hatch sucess for Pacific cod";
    /** full description */
    public static final String DEFAULT_fullDescr = 
        "\n\t**************************************************************************"+
        "\n\t* This function provides an implementation of the Laurel and Rogers (2020)"+
        "\n\t* temperature-dependent function for hatch success of Pacific cod eggs."+
        "\n\t* "+
        "\n\t* "+
        "\n\t* @author William Stockhausen"+
        "\n\t* "+
        "\n\t* Variables:"+
        "\n\t*      t - Double value of temperature (deg C)"+
        "\n\t* Value:"+
        "\n\t*      h - Double - fraction successfully hatched"+
        "\n\t* Calculation:"+
        "\n\t*     h = 0.453/(1.0+(((t-4.192)/2.125)^2))"+
        "\n\t* "+
        "\n\t*  Citation:"+
        "\n\t* Laurel, BJ and LA Rogers. 2020. Loss of spawning habitat and prerecruits"+
        "\n\t* of Pacific cod during a Gulf of Alaska heatwave. CJFAS."+
        "\n\t* dx.doi.org/10.1139/cjfas-2019-0238."+
        "\n\t**************************************************************************";
    /** number of settable parameters */
    public static final int numParams = 0;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;
    public IBMFunction_HatchSuccess(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
    }
    
    @Override
    public Object clone() {
        IBMFunction_HatchSuccess clone = new IBMFunction_HatchSuccess();
        clone.setFunctionType(getFunctionType());
        clone.setFunctionName(getFunctionName());
        clone.setDescription(getDescription());
        clone.setFullDescription(getFullDescription());
        return clone;
    }

    @Override
    public boolean setParameterValue(String param,Object value){
        //no parameters to set
        return true;
    }
    
    /**
     * Calculates egg hatch success based on input temperature. 
     * 
     * @param o - Double with value for in situ temperature in deg C.
     * 
     * @return Double - fractional hatching (survival) of eggs
     * 
     */
    @Override
    public Object calculate(Object o) {
        double t = (Double) o;
        double h = 0.453/(1.0+(Math.pow((t-4.192)/2.125, 2.0)));
        if (t>11.0) h = 0.0;
        return (Double) h;
    }
    
}
