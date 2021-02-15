/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sh.pcod.BenthicJuvStage;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMGrowthFunctionInterface;

/**
 * IBM function to calculate temperature-dependent benthic juvenile growth rate using
 *   rate = -0.081 + (0.079*T) - (0.003*T^2) in mm/d for total length
 * where T is temperature (deg C). 
 * 
 * From Hurst et al. (2010) equation.
 * 
 * @author WilliamStockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMGrowthFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)

public class IBMFunction_BenthicJuv_GrowthRateTL extends AbstractIBMFunction implements IBMGrowthFunctionInterface {
    public static final String DEFAULT_type = "Growth";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Growth rate (mm/d) in total length for Pacific cod benthic juveniles";
    /** function description */
    public static final String DEFAULT_descr = "Growth rate (mm/d) in total length for Pacific cod benthic juveniles";
    /** full description */
    public static final String DEFAULT_fullDescr = 
        "\n\t**************************************************************************"+
        "\n\t* This function provides an implementation of the Hurst et al. (2010)"+
        "\n\t* temperature-dependent function for growth in total length of Pacific cod benthic juveniles."+
        "\n\t* "+
        "\n\t* "+
        "\n\t* @author William Stockhausen"+
        "\n\t* "+
        "\n\t* Variables:"+
        "\n\t*      t - Double value of temperature (deg C)"+
        "\n\t* Value:"+
        "\n\t*      r - Double - growth rate (mm/d for total length)"+
        "\n\t* Calculation:"+
        "\n\t*     r = -0.081 + (0.079*t) - (0.003*t*t)"+
        "\n\t* "+
        "\n\t*  Citation:"+
        "\n\t* Hurst et al. 2010."+
        "\n\t**************************************************************************";
    /** number of settable parameters */
    public static final int numParams = 0;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;
    public IBMFunction_BenthicJuv_GrowthRateTL(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
    }
    
    @Override
    public Object clone() {
        IBMFunction_BenthicJuv_GrowthRateTL clone = new IBMFunction_BenthicJuv_GrowthRateTL();
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
     * Calculates growth rate in total length (mm/d) based on input temperature. 
     * 
     * @param o - Double with value for in situ temperature in deg C.
     * 
     * @return Double - growth rate (mm/d for total length)
     * 
     */
    @Override
    public Object calculate(Object o) {
        double t = (Double) o;
        double r = -0.081 + (0.079*t) - (0.003*t*t);
        return (Double) r;
    }
    
}
