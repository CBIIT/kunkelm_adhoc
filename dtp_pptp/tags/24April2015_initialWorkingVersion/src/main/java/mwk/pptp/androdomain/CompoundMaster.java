// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: hibernate/HibernateEntity.vsl in andromda-hibernate-cartridge.
//
package mwk.pptp.androdomain;

import java.io.Serializable;

/**
 * 
 */
// HibernateEntity.vsl annotations merge-point
public abstract class CompoundMaster
    implements Serializable, Comparable<CompoundMaster>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -93079890162922993L;

    // Generate 8 attributes
    private String pptpIdentifier;

    /**
     * 
     * @return this.pptpIdentifier String
     */
    public String getPptpIdentifier()
    {
        return this.pptpIdentifier;
    }

    /**
     * 
     * @param pptpIdentifierIn String
     */
    public void setPptpIdentifier(String pptpIdentifierIn)
    {
        this.pptpIdentifier = pptpIdentifierIn;
    }

    private Double solidDose;

    /**
     * 
     * @return this.solidDose Double
     */
    public Double getSolidDose()
    {
        return this.solidDose;
    }

    /**
     * 
     * @param solidDoseIn Double
     */
    public void setSolidDose(Double solidDoseIn)
    {
        this.solidDose = solidDoseIn;
    }

    private String concentrationUnit;

    /**
     * 
     * @return this.concentrationUnit String
     */
    public String getConcentrationUnit()
    {
        return this.concentrationUnit;
    }

    /**
     * 
     * @param concentrationUnitIn String
     */
    public void setConcentrationUnit(String concentrationUnitIn)
    {
        this.concentrationUnit = concentrationUnitIn;
    }

    private String treatmentRoute;

    /**
     * 
     * @return this.treatmentRoute String
     */
    public String getTreatmentRoute()
    {
        return this.treatmentRoute;
    }

    /**
     * 
     * @param treatmentRouteIn String
     */
    public void setTreatmentRoute(String treatmentRouteIn)
    {
        this.treatmentRoute = treatmentRouteIn;
    }

    private String schedule;

    /**
     * 
     * @return this.schedule String
     */
    public String getSchedule()
    {
        return this.schedule;
    }

    /**
     * 
     * @param scheduleIn String
     */
    public void setSchedule(String scheduleIn)
    {
        this.schedule = scheduleIn;
    }

    private String vehicle;

    /**
     * 
     * @return this.vehicle String
     */
    public String getVehicle()
    {
        return this.vehicle;
    }

    /**
     * 
     * @param vehicleIn String
     */
    public void setVehicle(String vehicleIn)
    {
        this.vehicle = vehicleIn;
    }

    private Double allDose;

    /**
     * 
     * @return this.allDose Double
     */
    public Double getAllDose()
    {
        return this.allDose;
    }

    /**
     * 
     * @param allDoseIn Double
     */
    public void setAllDose(Double allDoseIn)
    {
        this.allDose = allDoseIn;
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

    // Generate 0 associations
    /**
     * Returns <code>true</code> if the argument is an CompoundMaster instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof CompoundMaster))
        {
            return false;
        }
        final CompoundMaster that = (CompoundMaster)object;
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
     * Constructs new instances of {@link CompoundMaster}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link CompoundMaster}.
         * @return new CompoundMasterImpl()
         */
        public static CompoundMaster newInstance()
        {
            return new CompoundMasterImpl();
        }

        /**
         * Constructs a new instance of {@link CompoundMaster}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param pptpIdentifier String
         * @param concentrationUnit String
         * @param treatmentRoute String
         * @param schedule String
         * @param vehicle String
         * @return newInstance
         */
        public static CompoundMaster newInstance(String pptpIdentifier, String concentrationUnit, String treatmentRoute, String schedule, String vehicle)
        {
            final CompoundMaster entity = new CompoundMasterImpl();
            entity.setPptpIdentifier(pptpIdentifier);
            entity.setConcentrationUnit(concentrationUnit);
            entity.setTreatmentRoute(treatmentRoute);
            entity.setSchedule(schedule);
            entity.setVehicle(vehicle);
            return entity;
        }

        /**
         * Constructs a new instance of {@link CompoundMaster}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param pptpIdentifier String
         * @param solidDose Double
         * @param concentrationUnit String
         * @param treatmentRoute String
         * @param schedule String
         * @param vehicle String
         * @param allDose Double
         * @return newInstance CompoundMaster
         */
        public static CompoundMaster newInstance(String pptpIdentifier, Double solidDose, String concentrationUnit, String treatmentRoute, String schedule, String vehicle, Double allDose)
        {
            final CompoundMaster entity = new CompoundMasterImpl();
            entity.setPptpIdentifier(pptpIdentifier);
            entity.setSolidDose(solidDose);
            entity.setConcentrationUnit(concentrationUnit);
            entity.setTreatmentRoute(treatmentRoute);
            entity.setSchedule(schedule);
            entity.setVehicle(vehicle);
            entity.setAllDose(allDose);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(CompoundMaster o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
            if (this.getPptpIdentifier() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPptpIdentifier().compareTo(o.getPptpIdentifier()));
            }
            if (this.getSolidDose() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSolidDose().compareTo(o.getSolidDose()));
            }
            if (this.getConcentrationUnit() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getConcentrationUnit().compareTo(o.getConcentrationUnit()));
            }
            if (this.getTreatmentRoute() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTreatmentRoute().compareTo(o.getTreatmentRoute()));
            }
            if (this.getSchedule() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSchedule().compareTo(o.getSchedule()));
            }
            if (this.getVehicle() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getVehicle().compareTo(o.getVehicle()));
            }
            if (this.getAllDose() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getAllDose().compareTo(o.getAllDose()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}