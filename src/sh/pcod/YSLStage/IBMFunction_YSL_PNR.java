/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sh.pcod.YSLStage;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMMortalityFunctionInterface;

/**
 * IBM function to calculate YSL time to point-of-no return (in days) using
 *   PNR = 34.67 * exp(-0.126 * T)
 * from Laurel et al. (2008).
 * 
 * @author WilliamStockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMMortalityFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)

public class IBMFunction_YSL_PNR extends AbstractIBMFunction implements IBMMortalityFunctionInterface {
    public static final String DEFAULT_type = "Mortality";
    /** user-friendly function name */
    public static final String DEFAULT_name = "time to point-of-no return in days for Pacific cod YSL";
    /** function description */
    public static final String DEFAULT_descr = "time to point-of-no return in days for Pacific cod YSL";
    /** full description */
    public static final String DEFAULT_fullDescr = 
        "\n\t**************************************************************************"+
        "\n\t* This function provides an implementation of the Laurel et al. (2008)"+
        "\n\t* temperature-dependent function for time to point-of-no return (in days) for Pacific cod YSL."+
        "\n\t* "+
        "\n\t* "+
        "\n\t* @author William Stockhausen"+
        "\n\t* "+
        "\n\t* Variables:"+
        "\n\t*      t - Double value of temperature (deg C)"+
        "\n\t* Value:"+
        "\n\t*      PNR - Double - time to point-of-no return (in days)"+
        "\n\t* Calculation:"+
        "\n\t*     PNR = 34.67 * exp(-0.126 * T)"+
        "\n\t* "+
        "\n\t*  Citation:"+
        "\n\t* Laurel et al. 2008."+
        "\n\t**************************************************************************";
    /** number of settable parameters */
    public static final int numParams = 0;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;
    public IBMFunction_YSL_PNR(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
    }
    
    @Override
    public Object clone() {
        IBMFunction_YSL_PNR clone = new IBMFunction_YSL_PNR();
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
     * Calculates point-of-no return in days based on input temperature. 
     * 
     * @param o - Double with value for in situ temperature in deg C.
     * 
     * @return Double - time to point-of-no return (in days)
     * 
     */
    @Override
    public Object calculate(Object o) {
        double t = (Double) o;
        double PNR = 34.67 * Math.exp(-0.126 * t);
        return (Double) PNR;
    }
    
}
