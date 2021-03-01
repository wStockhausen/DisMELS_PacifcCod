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

/**
 * IBM function to convert standard length (mm) to dry weight (mg) for YSL-stage
 * Pacific cod. DW here does not include the yolk-sac, unlike DW for the egg stage.
 * 
 * @author William Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)

public class IBMFunction_YSL_ConvertSLtoDW extends AbstractIBMFunction {
    public static final String DEFAULT_type = "Conversion";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Convert standard length to dry weight";
    /** function description */
    public static final String DEFAULT_descr = "Convert standard length to dry weight";
    /** full description */
    public static final String DEFAULT_fullDescr = 
        "\n\t**************************************************************************"+
        "\n\t* This function converts standard length to dry weight for Pacific cod YSL larvae."+
        "\n\t* "+
        "\n\t* "+
        "\n\t* @author William Stockhausen"+
        "\n\t* "+
        "\n\t* Variables:"+
        "\n\t*      sl - Double value of standard length (mm)"+
        "\n\t* Value:"+
        "\n\t*      dw - Double - dry weight (mg); does not include yolk-sac"+
        "\n\t* Calculation:"+
        "\n\t*     dw = 1000*exp(-25.448732  +  7.039122*ln(sl) + (0.3866485^2)/2);"+
        "\n\t* "+
        "\n\t*  Citation:"+
        "\n\t* Stockhausen, unpublished; based on data from Hurst et al. 2010."+
        "\n\t**************************************************************************";
    /** number of settable parameters */
    public static final int numParams = 0;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;
    public IBMFunction_YSL_ConvertSLtoDW(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
    }
    
    @Override
    public Object clone() {
        IBMFunction_YSL_ConvertSLtoDW clone = new IBMFunction_YSL_ConvertSLtoDW();
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
     * Convert standard length (mm) to wet weight (mg). 
     * 
     * @param o - standard length (in mm) as Double.
     * 
     * @return Double - dry weight in mg (does not include yolk-sac)
     * 
     */
    @Override
    public Object calculate(Object o) {
        double lnSL = Math.log((Double) o);
        double dw = 1000*Math.exp(-25.448732  +  7.039122*lnSL + Math.pow(0.3866485,2.0)/2);
        return (Double) dw;
    }
    
}
