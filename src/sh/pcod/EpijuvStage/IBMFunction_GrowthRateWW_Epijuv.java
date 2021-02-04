/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sh.pcod.EpijuvStage;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMGrowthFunctionInterface;

/**
 * IBM function to calculate Epijuv growth rate using
 *   rate = (-0.998 + 0.579*T - 0.022*T^2)/100 in g/g/d wet weight weight,
 * where T is temperature in deg C.
 * 
 * From Hurst et al. (2010).
 * 
 * @author WilliamStockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMGrowthFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)

public class IBMFunction_GrowthRateWW_Epijuv extends AbstractIBMFunction implements IBMGrowthFunctionInterface {
    public static final String DEFAULT_type = "Growth";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Intrinsic growth rate (g/g/d) in wet weight for Pacific cod Epijuv";
    /** function description */
    public static final String DEFAULT_descr = "Intrinsic growth rate (g/g/d) in wet weight for Pacific cod Epijuv";
    /** full description */
    public static final String DEFAULT_fullDescr = 
        "\n\t**************************************************************************"+
        "\n\t* This function provides an implementation of the Hurst et al. (2010)"+
        "\n\t* temperature-dependent function for growth in wet weight for Pacific cod Epijuv."+
        "\n\t* "+
        "\n\t* "+
        "\n\t* @author William Stockhausen"+
        "\n\t* "+
        "\n\t* Variables:"+
        "\n\t*      t - Double value of temperature (deg C)"+
        "\n\t* Value:"+
        "\n\t*      r - Double - intrinsic growth rate for Epijuv wet weight (g/g/d)"+
        "\n\t* Calculation:"+
        "\n\t*     r = (-0.998 + 0.579*t - 0.022*t*t)/100; (original eq. in %/d)"+
        "\n\t* "+
        "\n\t*  Citation:"+
        "\n\t* Hurst et al. 2010."+
        "\n\t**************************************************************************";
    /** number of settable parameters */
    public static final int numParams = 0;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;
    public IBMFunction_GrowthRateWW_Epijuv(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
    }
    
    @Override
    public Object clone() {
        IBMFunction_GrowthRateWW_Epijuv clone = new IBMFunction_GrowthRateWW_Epijuv();
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
     * Calculates growth rate in wet weight (g/g/d) based on input temperature. 
     * 
     * @param o - Double with value for in situ temperature in deg C.
     * 
     * @return Double - growth rate (g/g//d in wet weight)
     * 
     */
    @Override
    public Object calculate(Object o) {
        double t = (Double) o;
        double r = (-0.998 + 0.579*t - 0.022*t*t)/100;//original in %/d
        return (Double) r;
    }
    
}
