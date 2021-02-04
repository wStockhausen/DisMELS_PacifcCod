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
import wts.models.DisMELS.framework.IBMFunctions.IBMGrowthFunctionInterface;

/**
 * IBM function to calculate temperature-dependent YSL growth rate using
 *   rate = (0.0179 + (0.015 * t) - (0.0001 * T * T)) in mm/d
 * where T is temperature (deg C).This is the corrected version of the 
 * Hurst et al. (2010) equation.
 * 
 * @author WilliamStockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMGrowthFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)

public class IBMFunction_GrowthRateSL_YSL extends AbstractIBMFunction implements IBMGrowthFunctionInterface {
    public static final String DEFAULT_type = "Growth";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Growth rate (mm/d) in standard length for Pacific cod YSL";
    /** function description */
    public static final String DEFAULT_descr = "Growth rate (mm/d) in standard length for Pacific cod YSL";
    /** full description */
    public static final String DEFAULT_fullDescr = 
        "\n\t**************************************************************************"+
        "\n\t* This function provides an implementation of the corrected Hurst et al. (2010)"+
        "\n\t* temperature-dependent function for growth in standard length of Pacific cod YSL."+
        "\n\t* "+
        "\n\t* "+
        "\n\t* @author William Stockhausen"+
        "\n\t* "+
        "\n\t* Variables:"+
        "\n\t*      t - Double value of temperature (deg C)"+
        "\n\t* Value:"+
        "\n\t*      r - Double - growth rate (mm/d)"+
        "\n\t* Calculation:"+
        "\n\t*     r = 0.0179 + (0.015 * t) - (0.0001 * t * t)"+
        "\n\t* "+
        "\n\t*  Citation:"+
        "\n\t* Hurst et al. 2010."+
        "\n\t**************************************************************************";
    /** number of settable parameters */
    public static final int numParams = 0;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;
    public IBMFunction_GrowthRateSL_YSL(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
    }
    
    @Override
    public Object clone() {
        IBMFunction_GrowthRateSL_YSL clone = new IBMFunction_GrowthRateSL_YSL();
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
     * Calculates growth rate in standard length (mm.d) based on input temperature. 
     * 
     * @param o - Double with value for in situ temperature in deg C.
     * 
     * @return Double - growth rate (mm/d)
     * 
     */
    @Override
    public Object calculate(Object o) {
        double t = (Double) o;
        double r = (0.0179 + (0.015 * t) - (0.0001 * t * t));
        return (Double) r;
    }
    
}
