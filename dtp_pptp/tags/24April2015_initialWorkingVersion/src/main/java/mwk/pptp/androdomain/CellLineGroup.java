// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: hibernate/HibernateEntity.vsl in andromda-hibernate-cartridge.
//
package mwk.pptp.androdomain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * 
 */
// HibernateEntity.vsl annotations merge-point
public abstract class CellLineGroup
    implements Serializable, Comparable<CellLineGroup>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 5512814982132579749L;

    // Generate 3 attributes
    private Double dose;

    /**
     * 
     * @return this.dose Double
     */
    public Double getDose()
    {
        return this.dose;
    }

    /**
     * 
     * @param doseIn Double
     */
    public void setDose(Double doseIn)
    {
        this.dose = doseIn;
    }

    private Integer testNumber;

    /**
     * 
     * @return this.testNumber Integer
     */
    public Integer getTestNumber()
    {
        return this.testNumber;
    }

    /**
     * 
     * @param testNumberIn Integer
     */
    public void setTestNumber(Integer testNumberIn)
    {
        this.testNumber = testNumberIn;
    }

    private Long id;

    /**
     * 
     * @return this.id Long
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * 
     * @param idIn Long
     */
    public void setId(Long idIn)
    {
        this.id = idIn;
    }

    // Generate 10 associations
    private ImplantSiteType implantSiteType;

    /**
     * 
     * @return this.implantSiteType ImplantSiteType
     */
    public ImplantSiteType getImplantSiteType()
    {
        return this.implantSiteType;
    }

    /**
     * 
     * @param implantSiteTypeIn ImplantSiteType
     */
    public void setImplantSiteType(ImplantSiteType implantSiteTypeIn)
    {
        this.implantSiteType = implantSiteTypeIn;
    }

    private Collection<MouseGroup> mouseGroups = new HashSet<MouseGroup>();

    /**
     * 
     * @return this.mouseGroups Collection<MouseGroup>
     */
    public Collection<MouseGroup> getMouseGroups()
    {
        return this.mouseGroups;
    }

    /**
     * 
     * @param mouseGroupsIn Collection<MouseGroup>
     */
    public void setMouseGroups(Collection<MouseGroup> mouseGroupsIn)
    {
        this.mouseGroups = mouseGroupsIn;
    }

    /**
     * 
     * @param elementToAdd MouseGroup
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addMouseGroups(MouseGroup elementToAdd)
    {
        return this.mouseGroups.add(elementToAdd);
    }

    /**
     * 
     * @param elementToRemove MouseGroup
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeMouseGroups(MouseGroup elementToRemove)
    {
        return this.mouseGroups.remove(elementToRemove);
    }

    private Collection<Summary> summaries = new HashSet<Summary>();

    /**
     * 
     * @return this.summaries Collection<Summary>
     */
    public Collection<Summary> getSummaries()
    {
        return this.summaries;
    }

    /**
     * 
     * @param summariesIn Collection<Summary>
     */
    public void setSummaries(Collection<Summary> summariesIn)
    {
        this.summaries = summariesIn;
    }

    /**
     * 
     * @param elementToAdd Summary
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addSummaries(Summary elementToAdd)
    {
        return this.summaries.add(elementToAdd);
    }

    /**
     * 
     * @param elementToRemove Summary
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeSummaries(Summary elementToRemove)
    {
        return this.summaries.remove(elementToRemove);
    }

    private ScheduleType scheduleType;

    /**
     * 
     * @return this.scheduleType ScheduleType
     */
    public ScheduleType getScheduleType()
    {
        return this.scheduleType;
    }

    /**
     * 
     * @param scheduleTypeIn ScheduleType
     */
    public void setScheduleType(ScheduleType scheduleTypeIn)
    {
        this.scheduleType = scheduleTypeIn;
    }

    private CellLineType cellLineType;

    /**
     * 
     * @return this.cellLineType CellLineType
     */
    public CellLineType getCellLineType()
    {
        return this.cellLineType;
    }

    /**
     * 
     * @param cellLineTypeIn CellLineType
     */
    public void setCellLineType(CellLineType cellLineTypeIn)
    {
        this.cellLineType = cellLineTypeIn;
    }

    private VehicleType vehicleType;

    /**
     * 
     * @return this.vehicleType VehicleType
     */
    public VehicleType getVehicleType()
    {
        return this.vehicleType;
    }

    /**
     * 
     * @param vehicleTypeIn VehicleType
     */
    public void setVehicleType(VehicleType vehicleTypeIn)
    {
        this.vehicleType = vehicleTypeIn;
    }

    private MouseType mouseType;

    /**
     * 
     * @return this.mouseType MouseType
     */
    public MouseType getMouseType()
    {
        return this.mouseType;
    }

    /**
     * 
     * @param mouseTypeIn MouseType
     */
    public void setMouseType(MouseType mouseTypeIn)
    {
        this.mouseType = mouseTypeIn;
    }

    private TreatmentRouteType treatmentRouteType;

    /**
     * 
     * @return this.treatmentRouteType TreatmentRouteType
     */
    public TreatmentRouteType getTreatmentRouteType()
    {
        return this.treatmentRouteType;
    }

    /**
     * 
     * @param treatmentRouteTypeIn TreatmentRouteType
     */
    public void setTreatmentRouteType(TreatmentRouteType treatmentRouteTypeIn)
    {
        this.treatmentRouteType = treatmentRouteTypeIn;
    }

    private MtdStudy mtdStudy;

    /**
     * 
     * @return this.mtdStudy MtdStudy
     */
    public MtdStudy getMtdStudy()
    {
        return this.mtdStudy;
    }

    /**
     * 
     * @param mtdStudyIn MtdStudy
     */
    public void setMtdStudy(MtdStudy mtdStudyIn)
    {
        this.mtdStudy = mtdStudyIn;
    }

    private ConcentrationUnitType concentrationUnitType;

    /**
     * 
     * @return this.concentrationUnitType ConcentrationUnitType
     */
    public ConcentrationUnitType getConcentrationUnitType()
    {
        return this.concentrationUnitType;
    }

    /**
     * 
     * @param concentrationUnitTypeIn ConcentrationUnitType
     */
    public void setConcentrationUnitType(ConcentrationUnitType concentrationUnitTypeIn)
    {
        this.concentrationUnitType = concentrationUnitTypeIn;
    }

    /**
     * Returns <code>true</code> if the argument is an CellLineGroup instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof CellLineGroup))
        {
            return false;
        }
        final CellLineGroup that = (CellLineGroup)object;
        if (this.id == null || that.getId() == null || !this.id.equals(that.getId()))
        {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers.
     */
    @Override
    public int hashCode()
    {
        int hashCode = 0;
        hashCode = 29 * hashCode + (this.id == null ? 0 : this.id.hashCode());

        return hashCode;
    }

    /**
     * Constructs new instances of {@link CellLineGroup}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link CellLineGroup}.
         * @return new CellLineGroupImpl()
         */
        public static CellLineGroup newInstance()
        {
            return new CellLineGroupImpl();
        }

        /**
         * Constructs a new instance of {@link CellLineGroup}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param implantSiteType ImplantSiteType
         * @param scheduleType ScheduleType
         * @param cellLineType CellLineType
         * @param vehicleType VehicleType
         * @param mouseType MouseType
         * @param treatmentRouteType TreatmentRouteType
         * @param mtdStudy MtdStudy
         * @param concentrationUnitType ConcentrationUnitType
         * @return newInstance
         */
        public static CellLineGroup newInstance(ImplantSiteType implantSiteType, ScheduleType scheduleType, CellLineType cellLineType, VehicleType vehicleType, MouseType mouseType, TreatmentRouteType treatmentRouteType, MtdStudy mtdStudy, ConcentrationUnitType concentrationUnitType)
        {
            final CellLineGroup entity = new CellLineGroupImpl();
            entity.setImplantSiteType(implantSiteType);
            entity.setScheduleType(scheduleType);
            entity.setCellLineType(cellLineType);
            entity.setVehicleType(vehicleType);
            entity.setMouseType(mouseType);
            entity.setTreatmentRouteType(treatmentRouteType);
            entity.setMtdStudy(mtdStudy);
            entity.setConcentrationUnitType(concentrationUnitType);
            return entity;
        }

        /**
         * Constructs a new instance of {@link CellLineGroup}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param dose Double
         * @param testNumber Integer
         * @param implantSiteType ImplantSiteType
         * @param mouseGroups Collection<MouseGroup>
         * @param summaries Collection<Summary>
         * @param scheduleType ScheduleType
         * @param cellLineType CellLineType
         * @param vehicleType VehicleType
         * @param mouseType MouseType
         * @param treatmentRouteType TreatmentRouteType
         * @param mtdStudy MtdStudy
         * @param concentrationUnitType ConcentrationUnitType
         * @return newInstance CellLineGroup
         */
        public static CellLineGroup newInstance(Double dose, Integer testNumber, ImplantSiteType implantSiteType, Collection<MouseGroup> mouseGroups, Collection<Summary> summaries, ScheduleType scheduleType, CellLineType cellLineType, VehicleType vehicleType, MouseType mouseType, TreatmentRouteType treatmentRouteType, MtdStudy mtdStudy, ConcentrationUnitType concentrationUnitType)
        {
            final CellLineGroup entity = new CellLineGroupImpl();
            entity.setDose(dose);
            entity.setTestNumber(testNumber);
            entity.setImplantSiteType(implantSiteType);
            entity.setMouseGroups(mouseGroups);
            entity.setSummaries(summaries);
            entity.setScheduleType(scheduleType);
            entity.setCellLineType(cellLineType);
            entity.setVehicleType(vehicleType);
            entity.setMouseType(mouseType);
            entity.setTreatmentRouteType(treatmentRouteType);
            entity.setMtdStudy(mtdStudy);
            entity.setConcentrationUnitType(concentrationUnitType);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(CellLineGroup o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
            if (this.getDose() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDose().compareTo(o.getDose()));
            }
            if (this.getTestNumber() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTestNumber().compareTo(o.getTestNumber()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}