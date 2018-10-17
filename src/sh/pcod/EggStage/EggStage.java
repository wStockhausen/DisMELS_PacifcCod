/*
 * EggStage.java
 *   devStage = 0
 *
 * Revised 10/11/2018:
 *   Added "attached" as new attribute (necessary with updated DisMELS).
 *   Revised for compatibility with DisMELS (branch DisMELS_2.0SnowCrab).
 * Revised 10/15/2018:
 *   Removed all Parameter function categories except "mortality" because they
 *     were not being used (development is hard-wired, eggs don't move).
 *
 */

package sh.pcod.EggStage;

import com.vividsolutions.jts.geom.Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.IBMFunctions.Mortality.ConstantMortalityRate;
import wts.models.DisMELS.IBMFunctions.Mortality.InversePowerLawMortalityRate;
import wts.models.DisMELS.framework.*;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.roms.model.Interpolator3D;
import wts.roms.model.LagrangianParticle;

/**
 *
 * @author William Stockhausen
 * @author Sarah Hinckley
 */
@ServiceProvider(service=LifeStageInterface.class)
public class EggStage extends AbstractLHS {
    
        //Static fields    
            //  Static fields new to this class
    /* flag to do debug operations */
    public static boolean debug = false;
    /* Class for attributes SH_NEW*/
    public static final String attributesClass = 
            sh.pcod.EggStage.EggStageAttributes.class.getName();
    /* Class for parameters */
    public static final String parametersClass = 
            sh.pcod.EggStage.EggStageParameters.class.getName();
    /* Class for feature type for point positions */
    public static final String pointFTClass = 
            wts.models.DisMELS.framework.LHSPointFeatureType.class.getName();
    /* Classes for next LHS-SH_NEW */
    public static final String[] nextLHSClasses = new String[]{ 
            sh.pcod.EggStage.EggStage.class.getName(),
            sh.pcod.YSLStage.YSLStage.class.getName() };
    /* Classes for spawned LHS */
    public static final String[] spawnedLHSClasses = new String[]{};
    
    /* string identifying environmental field with copepod densities */
    private static final String Cop = "Cop";
    /* string identifying environmental field with euphausiid densities */
    private static final String Eup = "Eup";
    /* string identifying environmental field with neocalanus densities */
    private static final String NCa = "NCa";
    
    //Instance fields
            //  Fields hiding ones from superclass
    /* life stage atrbutes object */
    protected EggStageAttributes atts = null;
    /* life stage parameters object */
    protected EggStageParameters params = null;
    
    //  Fields new to class
        //fields that reflect parameter values
    protected boolean isSuperIndividual;
    protected double  horizRWP;
    protected double  minStageDuration;
    protected double  maxStageDuration;
    protected double  minStageSize;
    protected double  stageTransRate;
    protected boolean useRandomTransitions;
    
        //fields that reflect (new) attribute values
    /** flag indicating individual is attached to bottom */
    protected boolean attached = true;
    /** development stage,0=egg,1=ysl,2=fdl,3=FDLpf,4=Epijuv, 5=BenthicJuv */
    protected double devStage = 0;
    /** egg diameter (mm) */
    protected double diam = 0;
    /** density of egg [kg/m^3] */
    protected double density;
    /** in situ temperature (deg C) */
    protected double temperature = 0;
    /** in situ copepod density mg/m^3, dry wt */
    protected double copepod = 0; 
     /** in situ euphausiid density mg/m^3, dry wt */
    protected double euphausiid = 0;
     /** in situ neocalanoid density mg/m^3, dry wt */
    protected double neocalanus = 0;
    protected double salinity = 0;
    /** in situ water density */
    protected double rho = 0;
    /**growth in egg diameter mm/d */
    protected double gD = 0;
    /**Egg stage duration and progression through stage*/
    protected double stagedur = 0;
    protected double stageratio = 0;
    
            //other fields
    /** number of individuals transitioning to next stage */
    private double numTrans;  
    
    /** IBM function selected for mortality */
    private IBMFunctionInterface fcnMortality = null; 
    
    private static final Logger logger = Logger.getLogger(EggStage.class.getName());
    
    /**
     * Creates a new instance of EggStage.  
     *  This constructor should be used ONLY to obtain
     *  the class names of the associated classes.
     * DO NOT DELETE THIS CONSTRUCTOR!!
     */
    public EggStage() {
        super("");
        super.atts = atts;
        super.params = params;
    }
    
    /**
     * Creates a new instance of SimplePelagicLHS with the given typeName.
     * A new id number is calculated in the superclass and assigned to
     * the new instance's id, parentID, and origID. 
     * 
     * The attributes are vanilla.  Initial attribute values should be set,
     * then initialize() should be called to initialize all instance variables.
     * DO NOT DELETE THIS CONSTRUCTOR!!
     */
    public EggStage(String typeName) 
                throws InstantiationException, IllegalAccessException {
        super(typeName);
        atts = new EggStageAttributes(typeName);
        atts.setValue(EggStageAttributes.PROP_id,id);
        atts.setValue(EggStageAttributes.PROP_parentID,id);
        atts.setValue(EggStageAttributes.PROP_origID,id);
        params = (EggStageParameters) LHS_Factory.createParameters(typeName);
        super.atts = atts;
        super.params = params;
        setParameters(params);
    }

    /**
     * Creates a new instance of LHS with type name and
     * attribute values given by input String array.
     * 
     * Side effects:
     *  1. Calls createInstance(LifeStageAttributesInterface), with associated effects,
     *  based on creating an attributes instance from the string array.
     * /
     * @param strv - attributes as string array
     * @return - instance of LHS
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    @Override
    public EggStage createInstance(String[] strv) 
                        throws InstantiationException, IllegalAccessException {
        LifeStageAttributesInterface theAtts = LHS_Factory.createAttributes(strv);
        EggStage lhs = createInstance(theAtts);
        return lhs;
    }

    /**
     * Creates a new instance of this LHS with attributes (including type name) 
     * corresponding to the input attributes instance.
     * 
     * Side effects:
     *  1. If theAtts id attribute is "-1", then a new (unique) id value is created 
     *  for the new LHS instance.
     *  2. If theAtts parentID attribute is "-1", then it is set to the value for id.
     *  3. If theAtts origID attribute is "-1", then it is set to the value for id.
     *  4. initialize() is called to initialize variables and convert position
     *   attributes.
     * /
     * @param theAtts - attributes instance
     * @return - instance of LHS
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    @Override
    public EggStage createInstance(LifeStageAttributesInterface theAtts)
                        throws InstantiationException, IllegalAccessException {
        EggStage lhs = null;
        if (theAtts instanceof EggStageAttributes) {
            lhs = new EggStage(theAtts.getTypeName());
            long newID = lhs.id;//save id of new instance
            lhs.setAttributes(theAtts);
            if (lhs.atts.getID()==-1) {
                //constructing new individual, so reset id values to those of new
                lhs.id = newID;
                lhs.atts.setValue(EggStageAttributes.PROP_id,newID);
            }
            newID = (Long) lhs.atts.getValue(EggStageAttributes.PROP_parentID);
            if (newID==-1) {
                lhs.atts.setValue(EggStageAttributes.PROP_parentID,newID);
            }
            newID = (Long) lhs.atts.getValue(EggStageAttributes.PROP_origID);
            if (newID==-1) {
                lhs.atts.setValue(EggStageAttributes.PROP_origID,newID);
            }
        }
        lhs.initialize();//initialize instance variables
        return lhs;
    }

    /**
     *  Returns the associated attributes.  
     */
    @Override
    public EggStageAttributes getAttributes() {
        return atts;
    }

    /**
     * Sets the values of the associated attributes object to those in the input
     * String[]. This does NOT change the typeNameof the LHS instance (or the 
     * associated LHSAttributes instance) on which the method is called.
     * Attribute values are set using SimpleBenthicLHSAttributes.setValues(String[]).
     * Side effects:
     *  1. If th new id attribute is not "-1", then its value for id replaces the 
     *      current value for the lhs.
     *  2. If the new parentID attribute is "-1", then it is set to the value for id.
     *  3. If the new origID attribute is "-1", then it is set to the value for id.
     *  4. initialize() is called to initialize variables and convert position
     *   attributes.
     * /
     * @param strv - attribute values as String[]
     */
    @Override
    public void setAttributes(String[] strv) {
        long aid;
        atts.setValues(strv);
        aid = atts.getValue(EggStageAttributes.PROP_id, id);
        if (aid==-1) {
            //change atts id to lhs id
            atts.setValue(EggStageAttributes.PROP_id, id);
        } else {
            //change lhs id to atts id
            id = aid;
        }
        aid = atts.getValue(EggStageAttributes.PROP_parentID, id);
        if (aid==-1) {
            atts.setValue(EggStageAttributes.PROP_parentID, id);
        }
        aid = atts.getValue(EggStageAttributes.PROP_origID, id);
        if (aid==-1) {
            atts.setValue(EggStageAttributes.PROP_origID, id);
        }
        initialize();//initialize instance variables
    }

    /**
     * Sets the attributes for the instance by copying values from the input.
     * This does NOT change the typeName of the LHS instance (or the associated 
     * LHSAttributes instance) on which the method is called.
     * Note that ALL attributes are copied, so id, parentID, and origID are copied
     * as well. 
     *  Side effects:
     *      updateVariables() is called to update instance variables.
     *      Instance field "id" is also updated.
     * @param newAtts - should be instance of SimplePelagicLHSAttributes
     */
    @Override
    public void setAttributes(LifeStageAttributesInterface newAtts) {
        if (newAtts instanceof EggStageAttributes) {
            EggStageAttributes spAtts = (EggStageAttributes) newAtts;
            for (String key: atts.getKeys()) atts.setValue(key,spAtts.getValue(key));
        } else {
            //TODO: should throw an error here
            logger.info("setAttributes(): no match for attributes type:"+newAtts.toString());
        }
        id = atts.getValue(EggStageAttributes.PROP_id, id);
        updateVariables();
    }
    
    /**
     *  Sets the associated attributes object. Use this after creating an LHS instance
     * as an "output" from another LHS that is functioning as an ordinary individual.
     */
    @Override
    public void setInfoFromIndividual(LifeStageInterface oldLHS){
        /** 
         * Since this is a single individual making a transition, we need to:
         *  1) copy the attributes from the old LHS (id's should remain as for old LHS)
         *  2) set age in stage = 0
         *  3) set active and alive to true
         *  5) copy the Lagrangian Particle from the old LHS
         *  6) start a new track from the current position for the oldLHS
         *  7) update local variables
         */
        LifeStageAttributesInterface oldAtts = oldLHS.getAttributes();            
        setAttributes(oldAtts);
        
        //reset some attributes
        atts.setValue(EggStageAttributes.PROP_ageInStage, 0.0);//reset age in stage
        atts.setValue(EggStageAttributes.PROP_active,true);    //set active to true
        atts.setValue(EggStageAttributes.PROP_alive,true);     //set alive to true
        id = atts.getID(); //reset id for current LHS to one from old LHS

        //copy LagrangianParticle information
        this.setLagrangianParticle(oldLHS.getLagrangianParticle());
        //start track at last position of oldLHS track
        this.startTrack(oldLHS.getLastPosition(COORDINATE_TYPE_PROJECTED),COORDINATE_TYPE_PROJECTED);
        this.startTrack(oldLHS.getLastPosition(COORDINATE_TYPE_GEOGRAPHIC),COORDINATE_TYPE_GEOGRAPHIC);
        //update local variables to capture changes made here
        updateVariables();
    }
    
    /**
     *  Sets the associated attributes object. Use this after creating an LHS instance
     * as an "output" from another LHS that is functioning as a super individual.
     */
    @Override
    public void setInfoFromSuperIndividual(LifeStageInterface oldLHS, double numTrans) {
        /** 
         * Since the old LHS instance is a super individual, only a part 
         * (numTrans) of it transitioned to the current LHS. Thus, we need to:
         *          1) copy most attribute values from old stage
         *          2) make sure id for this LHS is retained, not changed
         *          3) assign old LHS id to this LHS as parentID
         *          4) copy old LHS origID to this LHS origID
         *          5) set number in this LHS to numTrans
         *          6) reset age in stage to 0
         *          7) set active and alive to true
         *          9) copy the Lagrangian Particle from the old LHS
         *         10) start a new track from the current position for the oldLHS
         *         11) update local variables to match attributes
         */
        //copy some variables that should not change
        long idc = id;
        
        //copy the old attribute values
        LifeStageAttributesInterface oldAtts = oldLHS.getAttributes();            
        setAttributes(oldAtts);
        
        //reset some attributes and variables
        id = idc;
        atts.setValue(EggStageAttributes.PROP_id,idc);//reset id to one for current LHS
        atts.setValue(EggStageAttributes.PROP_parentID,
                      oldAtts.getValue(LifeStageAttributesInterface.PROP_id));//copy old id to parentID
        atts.setValue(EggStageAttributes.PROP_number, numTrans);//set number to numTrans
        atts.setValue(EggStageAttributes.PROP_ageInStage, 0.0); //reset age in stage
        atts.setValue(EggStageAttributes.PROP_active,true);     //set active to true
        atts.setValue(EggStageAttributes.PROP_alive,true);      //set alive to true
            
        //copy LagrangianParticle information
        this.setLagrangianParticle(oldLHS.getLagrangianParticle());
        //start track at last position of oldLHS track
        this.startTrack(oldLHS.getLastPosition(COORDINATE_TYPE_PROJECTED),COORDINATE_TYPE_PROJECTED);
        this.startTrack(oldLHS.getLastPosition(COORDINATE_TYPE_GEOGRAPHIC),COORDINATE_TYPE_GEOGRAPHIC);
        //update local variables to capture changes made here
        updateVariables();
    }

    /**
     *  Returns the associated parameters.  
     */
    @Override
    public EggStageParameters getParameters() {
        return params;
    }

    /**
     * Sets the parameters for the instance to a cloned version of the input.
     * @param newParams - should be instance of EggStageParameters
     */
    @Override
    public void setParameters(LifeStageParametersInterface newParams) {
        if (newParams instanceof EggStageParameters) {
            params = (EggStageParameters) newParams;
            super.params = params;
            setParameterValues();
            fcnMortality = params.getSelectedIBMFunctionForCategory(EggStageParameters.FCAT_Mortality);
        } else {
            //TODO: throw some error
        }
    }
    
    /*
     * Copy the values from the params map to the param variables.
     */
    private void setParameterValues() {
        isSuperIndividual = 
                params.getValue(EggStageParameters.PARAM_isSuperIndividual,isSuperIndividual);
        horizRWP = 
                params.getValue(EggStageParameters.PARAM_horizRWP,horizRWP);
        minStageDuration = 
                params.getValue(EggStageParameters.PARAM_minStageDuration,minStageDuration);
        maxStageDuration = 
                params.getValue(EggStageParameters.PARAM_maxStageDuration,maxStageDuration);
        useRandomTransitions = 
                params.getValue(EggStageParameters.PARAM_useRandomTransitions,true);
    }
    
    /**
     *  Provides a copy of the object.  The attributes and parameters
     *  are cloned in the process, so the clone is independent of the
     *  original.
     */
    @Override
    public Object clone() {
        EggStage clone = null;
        try {
            clone = (EggStage) super.clone();
            clone.setAttributes(atts);//this clones atts
            clone.setParameters(params);//this clones params
            clone.lp      = (LagrangianParticle) lp.clone();
            clone.track   = (ArrayList<Coordinate>) track.clone();
            clone.trackLL = (ArrayList<Coordinate>) trackLL.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return clone;
        
    }

    /**
     *
     * @param dt - time step in seconds
     * @return
     */
    @Override
    public List<LifeStageInterface> getMetamorphosedIndividuals(double dt) {
        double dtp = 0.25*(dt/86400);//use 1/4 timestep (converted from sec to d)
        output.clear();
        List<LifeStageInterface> nLHSs;
        //SH_NEW:
        if (((ageInStage+dtp)>=minStageDuration) && (stageratio >=1.0)) {
            if ((numTrans>0)||!isSuperIndividual){
                devStage=1;
                nLHSs = createNextLHS();
                if (nLHSs!=null) output.addAll(nLHSs);
            }
        }

        return output;
    }

    private List<LifeStageInterface> createNextLHS() {
        List<LifeStageInterface> nLHSs = null;
        try {
            //create LHS with "next" stage
            if (isSuperIndividual) {
                /** 
                 * Since this is LHS instance is a super individual, only a part 
                 * (numTrans) of it transitions to the next LHS. Thus, we need to:
                 *          1) create new LHS instance
                 *          2. assign new id to new instance
                 *          3) assign current LHS id to new LHS as parentID
                 *          4) copy current LHS origID to new LHS origID
                 *          5) set number in new LHS to numTrans for current LHS
                 *          6) reset numTrans in current LHS
                 */
                nLHSs = LHS_Factory.createNextLHSsFromSuperIndividual(typeName,this,numTrans);
                numTrans = 0.0;//reset numTrans to zero
            } else {
                /** 
                 * Since this is a single individual making a transition, we should
                 * "kill" the current LHS.  Also, the various IDs should remain
                 * the same in the new LHS since it's the same individual. Thus, 
                 * we need to:
                 *          1) create new LHS instance
                 *          2. assign current LHS id to new LHS id
                 *          3) assign current LHS parentID to new LHS parentID
                 *          4) copy current LHS origID to new LHS origID
                 *          5) kill current LHS
                 */
                nLHSs = LHS_Factory.createNextLHSsFromIndividual(typeName,this);
                alive  = false; //allow only 1 transition, so kill this stage
                active = false; //set stage inactive, also
            }
        } catch (IllegalAccessException | InstantiationException ex) {
            ex.printStackTrace();
        }
        return nLHSs;
    }
    
    @Override
    public String getReport() {
        updateAttributes();//make sure attributes are up to date
        atts.setValue(atts.PROP_track, getTrackAsString(COORDINATE_TYPE_GEOGRAPHIC));//
        return atts.getCSV();
    }

    @Override
    public String getReportHeader() {
        return atts.getCSVHeaderShortNames();
    }

    /**
     * Initializes instance variables to attribute values (via updateVariables()), 
     * then determines initial position for the lagrangian particle tracker
     * and resets the track,
     * sets horizType and vertType attributes to HORIZ_LL, VERT_H,
     * and finally calls updatePosition(), updateEnvVars(), and updateAttributes().
     */
    public void initialize() {
//        atts.setValue(SimplePelagicLHSAttributes.PARAM_id,id);//TODO: should do this beforehand!!
        updateVariables();//set instance variables to attribute values
        int hType,vType;
        hType=vType=-1;
        double xPos, yPos, zPos;
        xPos=yPos=zPos=0;
        hType      = atts.getValue(EggStageAttributes.PROP_horizType,hType);
        vType      = atts.getValue(EggStageAttributes.PROP_vertType,vType);
        xPos       = atts.getValue(EggStageAttributes.PROP_horizPos1,xPos);
        yPos       = atts.getValue(EggStageAttributes.PROP_horizPos2,yPos);
        zPos       = atts.getValue(EggStageAttributes.PROP_vertPos,zPos);
        time       = startTime;
        numTrans   = 0.0; //set numTrans to zero
        if (debug) logger.info(hType+cc+vType+cc+startTime+cc+xPos+cc+yPos+cc+zPos);
        if (i3d!=null) {
            double[] IJ = new double[] {xPos,yPos};
            try {
                if (hType==Types.HORIZ_XY) {
                    IJ = i3d.getGrid().computeIJfromXY(xPos,yPos);
                } else if (hType==Types.HORIZ_LL) {
                    IJ = i3d.getGrid().computeIJfromLL(yPos,xPos);
                }
            } catch(java.lang.ArrayIndexOutOfBoundsException ex) {
                logger.info("ArrayIndexOutOfBoundsException in EggStage.initialize() for id "+id);
                logger.info("--IJ info : "+hType+cc+vType+cc+startTime+cc+xPos+cc+yPos+cc+zPos);
                throw(ex);
            } catch(java.lang.NullPointerException ex) {
                logger.info("NullPointerException in EggStage.initialize() for id "+id);
                logger.info("--IJ info : "+hType+cc+vType+cc+startTime+cc+xPos+cc+yPos+cc+zPos);
                throw(ex);
            }
            double z = i3d.interpolateBathymetricDepth(IJ);
            if (debug) logger.info("Bathymetric depth = "+z);
            double ssh = i3d.interpolateSSH(IJ);

            double K = 0;  //set K = 0 (at bottom) as default
            if (vType==Types.VERT_K) {
                if (zPos<0) {K = 0;} else
                if (zPos>i3d.getGrid().getN()) {K = i3d.getGrid().getN();} else
                K = zPos;
            } else if (vType==Types.VERT_Z) {//depths negative
                if (zPos<-z) {K = 0;} else                     //at bottom
                if (zPos>ssh) {K = i3d.getGrid().getN();} else //at surface
                K = i3d.calcKfromZ(IJ[0],IJ[1],zPos);          //at requested depth
            } else if (vType==Types.VERT_H) {//depths positive
                if (zPos>z) {K = 0;} else                       //at bottom
                if (zPos<-ssh) {K = i3d.getGrid().getN();} else //at surface
                K = i3d.calcKfromZ(IJ[0],IJ[1],-zPos);          //at requested depth
            } else if (vType==Types.VERT_DH) {//distance off bottom
                if (zPos<0) {K = 0;} else                        //at bottom
                if (zPos>z+ssh) {K = i3d.getGrid().getN();} else //at surface
                K = i3d.calcKfromZ(IJ[0],IJ[1],-(z-zPos));       //at requested distance off bottom
            }
            lp.setIJK(IJ[0],IJ[1],K);
            //reset track array
            track.clear();
            trackLL.clear();
            //set horizType to lat/lon and vertType to depth
            atts.setValue(EggStageAttributes.PROP_horizType,Types.HORIZ_LL);
            atts.setValue(EggStageAttributes.PROP_vertType,Types.VERT_H);
            //interpolate initial position and environmental variables
            double[] pos = lp.getIJK();
            updatePosition(pos);
            interpolateEnvVars(pos);
            updateAttributes(); 
        }
    }
    
    @Override
    public void step(double dt) throws ArrayIndexOutOfBoundsException {
        //Pacific cod eggs are demersal, and assumed to be fixed in place
        //so location does not change
        double[] pos = lp.getIJK();
        double T = i3d.interpolateTemperature(pos);
        if(T<=0.0) T=0.01; 
        
        //SH_NEW-Prey Stuff  
        copepod    = i3d.interpolateValue(pos,Cop,Interpolator3D.INTERP_VAL);
        euphausiid = i3d.interpolateValue(pos,Eup,Interpolator3D.INTERP_VAL);
        neocalanus = i3d.interpolateValue(pos,NCa,Interpolator3D.INTERP_VAL);
       
        time += dt;
        
        //SH_NEW:{
        double dtday = dt/86400;
        gD = (0.104 + (0.024 * T) - (0.00002 * T * T));  // Hurst et al 2010, embryo eqn
        diam += (gD * dtday);
        stagedur = 46.597 - (4.079 * T);
        //stagedur = 44.4857 - (7.3857*T) + (0.4524*T*T);
        stageratio += (1.0 / stagedur)*dtday;
        
        updateNum(dt);
        updateAge(dt);
        updatePosition(pos);
        interpolateEnvVars(pos);
        //check for exiting grid
        if (i3d.isAtGridEdge(pos,tolGridEdge)){
            alive=false;
            active=false;
        }
        if (debug) {
            logger.info(toString());
        }
        updateAttributes(); //update the attributes object w/ nmodified values
    }
    
    /**
     *
     * @param dt - time step in seconds
     */
    private void updateAge(double dt) {
        age        += dt/86400;
        ageInStage += dt/86400;
        if (ageInStage>maxStageDuration) {
            alive = false;
            active = false;
        }
    }

    /**
     *
     * @param dt - time step in seconds
     */
    private void updateNum(double dt) {
        //{WTS_NEW 2012-07-26:
        double mortalityRate = 0.0D;//in unis of [days]^-1
        if (fcnMortality instanceof ConstantMortalityRate){
            mortalityRate = (Double)fcnMortality.calculate(null);
        } else 
        if (fcnMortality instanceof InversePowerLawMortalityRate){
        //SH_NEW
        //    mortalityRate = (Double)fcnMortality.calculate(diam);//using egg diameter as covariate for mortality
        }
        double totRate = mortalityRate;
        if ((ageInStage>=minStageDuration)) {
            totRate += stageTransRate;
            //apply mortality rate to previous number transitioning and
            //add in new transitioners
            numTrans = numTrans*Math.exp(-dt*mortalityRate/86400)+
                    (stageTransRate/totRate)*number*(1-Math.exp(-dt*totRate/86400));
        }
        number = number*Math.exp(-dt*totRate/86400);
        //}: WTS_NEW 2012-07-26
    }
    
    private void updatePosition(double[] pos) {
        bathym     = i3d.interpolateBathymetricDepth(pos);
        depth      = -i3d.calcZfromK(pos[0],pos[1],pos[2]);
        lat        = i3d.interpolateLat(pos);
        lon        = i3d.interpolateLon(pos);
        gridCellID = ""+Math.round(pos[0])+"_"+Math.round(pos[1]);
        updateTrack();
    }
    
    private void interpolateEnvVars(double[] pos) {
        temperature = i3d.interpolateTemperature(pos);
        salinity    = i3d.interpolateSalinity(pos);
        //interpolate in situ density field
        if (i3d.getPhysicalEnvironment().getField("rho")!=null) rho  = i3d.interpolateValue(pos,"rho");
        else rho = 0.0;
    }

    @Override
    public double getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(double newTime) {
        startTime = newTime;
        time      = startTime;
        atts.setValue(EggStageAttributes.PROP_startTime,startTime);
        atts.setValue(EggStageAttributes.PROP_time,time);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean b) {
        active = b;
        atts.setActive(b);
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setAlive(boolean b) {
        alive = b;
        atts.setAlive(b);
    }

    @Override
    public String getAttributesClassName() {
        return attributesClass;
    }

    @Override
    public String getParametersClassName() {
        return parametersClass;
    }

    @Override
    public String[] getNextLHSClassNames() {
        return nextLHSClasses;
    }

    @Override
    public String getPointFeatureTypeClassName() {
        return pointFTClass;
    }

    @Override
    public String[] getSpawnedLHSClassNames() {
        return spawnedLHSClasses;
    }

    @Override
    public List<LifeStageInterface> getSpawnedIndividuals() {
        output.clear();
        return output;
    }

    @Override
    public boolean isSuperIndividual() {
        return isSuperIndividual;
    }
    
    /**
     * Updates attribute values defined for this abstract class. 
     */
    @Override
    protected void updateAttributes() {
        super.updateAttributes();
        atts.setValue(EggStageAttributes.PROP_attached,attached);
        atts.setValue(EggStageAttributes.PROP_density,density);
        atts.setValue(EggStageAttributes.PROP_devStage,devStage);
        atts.setValue(EggStageAttributes.PROP_diameter,diam);
        atts.setValue(EggStageAttributes.PROP_rho,rho);
        atts.setValue(EggStageAttributes.PROP_salinity,salinity);
        atts.setValue(EggStageAttributes.PROP_temperature,temperature);
        atts.setValue(EggStageAttributes.PROP_copepod,copepod);
        atts.setValue(EggStageAttributes.PROP_euphausiid,euphausiid);
        atts.setValue(EggStageAttributes.PROP_neocalanus,neocalanus);
  
    }

    /**
     * Updates local variables from the attributes.  
     */
    @Override
    protected void updateVariables() {
        super.updateVariables();
        attached    = atts.getValue(EggStageAttributes.PROP_attached,attached);
        density     = atts.getValue(EggStageAttributes.PROP_density,density);
        devStage    = atts.getValue(EggStageAttributes.PROP_devStage,devStage);
        diam        = atts.getValue(EggStageAttributes.PROP_diameter,diam);
        rho         = atts.getValue(EggStageAttributes.PROP_rho,rho);
        salinity    = atts.getValue(EggStageAttributes.PROP_salinity,salinity);
        temperature = atts.getValue(EggStageAttributes.PROP_temperature,temperature);
        copepod     = atts.getValue(EggStageAttributes.PROP_copepod,copepod);
        euphausiid  = atts.getValue(EggStageAttributes.PROP_euphausiid,euphausiid);
        neocalanus  = atts.getValue(EggStageAttributes.PROP_neocalanus,neocalanus);
    }

}
