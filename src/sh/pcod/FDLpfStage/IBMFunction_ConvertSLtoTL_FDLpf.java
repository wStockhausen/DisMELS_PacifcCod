/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sh.pcod.FDLpfStage;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;

/**
 * IBM function to convert standard length to total length.
 * 
 * @author WilliamStockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)

public class IBMFunction_ConvertSLtoTL_FDLpf extends AbstractIBMFunction {
    public static final String DEFAULT_type = "Conversion";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Convert standard length to total length for Pacific cod FDLpf";
    /** function description */
    public static final String DEFAULT_descr = "Convert standard length to total length for Pacific cod FDLpf";
    /** full description */
    public static final String DEFAULT_fullDescr = 
        "\n\t**************************************************************************"+
        "\n\t* This function converts standard length to total length for Pacific cod FDLpf."+
        "\n\t* "+
        "\n\t* "+
        "\n\t* @author William Stockhausen"+
        "\n\t* "+
        "\n\t* Variables:"+
        "\n\t*      sl - Double value of standard length (mm)"+
        "\n\t* Value:"+
        "\n\t*      tl - Double - total length (mm)"+
        "\n\t* Calculation:"+
        "\n\t*     tl = (sl + 0.5169)/0.9315;"+
        "\n\t* "+
        "\n\t*  Citation:"+
        "\n\t* Hurst et al. 2010."+
        "\n\t**************************************************************************";
    /** number of settable parameters */
    public static final int numParams = 0;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;
    public IBMFunction_ConvertSLtoTL_FDLpf(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
    }
    
    @Override
    public Object clone() {
        IBMFunction_ConvertSLtoTL_FDLpf clone = new IBMFunction_ConvertSLtoTL_FDLpf();
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
     * Convert standard length to total length. 
     * 
     * @param o - standard length as Double.
     * 
     * @return Double - total length
     * 
     */
    @Override
    public Object calculate(Object o) {
        double sl = (Double) o;
        double tl = (sl + 0.5169)/0.9315;
        return (Double) tl;
    }
    
}
