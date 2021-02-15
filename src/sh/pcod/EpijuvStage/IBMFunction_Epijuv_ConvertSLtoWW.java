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

/**
 * IBM function to convert standard length (mm) to wet weight (mg).
 * 
 * @author WilliamStockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)

public class IBMFunction_Epijuv_ConvertSLtoWW extends AbstractIBMFunction {
    public static final String DEFAULT_type = "Conversion";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Convert standard length to wet weight";
    /** function description */
    public static final String DEFAULT_descr = "Convert standard length to wet weight";
    /** full description */
    public static final String DEFAULT_fullDescr = 
        "\n\t**************************************************************************"+
        "\n\t* This function converts standard length to total length for Pacific cod epipelagic juveiles."+
        "\n\t* "+
        "\n\t* "+
        "\n\t* @author William Stockhausen"+
        "\n\t* "+
        "\n\t* Variables:"+
        "\n\t*      sl - Double value of standard length (mm)"+
        "\n\t* Value:"+
        "\n\t*      ww - Double - wet weight (mg)"+
        "\n\t* Calculation:"+
        "\n\t*     ww = 1000*exp(-17.7329551 + 6.7316061*ln(sl) - 0.5682575 * ln(sl)^2 + (0.09793041^2)/2);"+
        "\n\t* "+
        "\n\t*  Citation:"+
        "\n\t* Stockhausen, unpublished; based on data from Hurst et al. 2010."+
        "\n\t**************************************************************************";
    /** number of settable parameters */
    public static final int numParams = 0;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;
    public IBMFunction_Epijuv_ConvertSLtoWW(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
    }
    
    @Override
    public Object clone() {
        IBMFunction_Epijuv_ConvertSLtoWW clone = new IBMFunction_Epijuv_ConvertSLtoWW();
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
     * @return Double - wet weight in mg
     * 
     */
    @Override
    public Object calculate(Object o) {
        double lnSL = Math.log((Double) o);
        double ww = 1000*Math.exp(-17.7329551 + 6.7316061*lnSL - 0.5682575 * lnSL*lnSL + Math.pow(0.09793041,2)/2);
        return (Double) ww;
    }
    
}
