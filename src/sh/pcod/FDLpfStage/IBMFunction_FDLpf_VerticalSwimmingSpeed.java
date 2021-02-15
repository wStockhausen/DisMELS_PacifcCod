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
 * IBM function to calculate temperature-dependent FDLpf vertical swimming speed using
 *   s = ((0.081221 + 0.043168*log10[t]) * TL^1.49652) in mm/s
 * where t is temperature (deg C) and TL is total length in mm.
 * 
 * @author WilliamStockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)

public class IBMFunction_FDLpf_VerticalSwimmingSpeed extends AbstractIBMFunction {
    public static final String DEFAULT_type = "Vertical swimming speed";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Vertical swimming speed (mm/s) for Pacific cod FDLpf as function of temperature and size";
    /** function description */
    public static final String DEFAULT_descr = "Vertical swimming speed (mm/s) for Pacific cod FDLpf as function of temperature and size";
    /** full description */
    public static final String DEFAULT_fullDescr = 
        "\n\t**************************************************************************"+
        "\n\t* This function provides an implementation of vertical swimming speed (mm/s)"+
        "\n\t* for Pacific cod FDLpf as a function of temperature and total length"+
        "\n\t* "+
        "\n\t* "+
        "\n\t* @author William Stockhausen"+
        "\n\t* "+
        "\n\t* Variables:"+
        "\n\t*      t - Double value of temperature (deg C)"+
        "\n\t*      z - Double value of total length (mm)"+
        "\n\t* Value:"+
        "\n\t*      s - Double - vertical swimming speed (mm/s)"+
        "\n\t* Calculation:"+
        "\n\t*     s = ((0.081221 + 0.043168 log10[t]) * TL^1.49652)"+
        "\n\t* "+
        "\n\t*  Citation:"+
        "\n\t* Hurst, pers. comm."+
        "\n\t**************************************************************************";
    /** number of settable parameters */
    public static final int numParams = 0;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;
    public IBMFunction_FDLpf_VerticalSwimmingSpeed(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
    }
    
    @Override
    public Object clone() {
        IBMFunction_FDLpf_VerticalSwimmingSpeed clone = new IBMFunction_FDLpf_VerticalSwimmingSpeed();
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
     * Calculates vertical swimming speed based on input temperature and total length (mm). 
     * 
     * @param o - Double[] with values 
     *        o[1] - in situ temperature in deg C
     *        o[2] - total length of fish (mm)
     * 
     * @return Double - vertical swimming speed (mm/s)
     * 
     */
    @Override
    public Object calculate(Object o) {
        Double[] vals = (Double[])o;
        double t  = vals[0];//temperature
        double tl = vals[1];//total length
        double s = (0.081221 + 0.043168*Math.log10(t)) * Math.pow(tl,1.49652);
        return (Double) s;
    }
    
}
