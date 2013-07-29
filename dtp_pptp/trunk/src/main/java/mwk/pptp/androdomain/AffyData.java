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
public abstract class AffyData
    implements Serializable, Comparable<AffyData>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 6739183152408171234L;

    // Generate 6 attributes
    private Integer statPairs;

    /**
     * 
     * @return this.statPairs Integer
     */
    public Integer getStatPairs()
    {
        return this.statPairs;
    }

    /**
     * 
     * @param statPairsIn Integer
     */
    public void setStatPairs(Integer statPairsIn)
    {
        this.statPairs = statPairsIn;
    }

    private Integer statPairsUsed;

    /**
     * 
     * @return this.statPairsUsed Integer
     */
    public Integer getStatPairsUsed()
    {
        return this.statPairsUsed;
    }

    /**
     * 
     * @param statPairsUsedIn Integer
     */
    public void setStatPairsUsed(Integer statPairsUsedIn)
    {
        this.statPairsUsed = statPairsUsedIn;
    }

    private Double signal;

    /**
     * 
     * @return this.signal Double
     */
    public Double getSignal()
    {
        return this.signal;
    }

    /**
     * 
     * @param signalIn Double
     */
    public void setSignal(Double signalIn)
    {
        this.signal = signalIn;
    }

    private String detection;

    /**
     * 
     * @return this.detection String
     */
    public String getDetection()
    {
        return this.detection;
    }

    /**
     * 
     * @param detectionIn String
     */
    public void setDetection(String detectionIn)
    {
        this.detection = detectionIn;
    }

    private Double detectionPValue;

    /**
     * 
     * @return this.detectionPValue Double
     */
    public Double getDetectionPValue()
    {
        return this.detectionPValue;
    }

    /**
     * 
     * @param detectionPValueIn Double
     */
    public void setDetectionPValue(Double detectionPValueIn)
    {
        this.detectionPValue = detectionPValueIn;
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

    // Generate 2 associations
    private AffyIdentifierType affyIdentifierType;

    /**
     * 
     * @return this.affyIdentifierType AffyIdentifierType
     */
    public AffyIdentifierType getAffyIdentifierType()
    {
        return this.affyIdentifierType;
    }

    /**
     * 
     * @param affyIdentifierTypeIn AffyIdentifierType
     */
    public void setAffyIdentifierType(AffyIdentifierType affyIdentifierTypeIn)
    {
        this.affyIdentifierType = affyIdentifierTypeIn;
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

    /**
     * Returns <code>true</code> if the argument is an AffyData instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof AffyData))
        {
            return false;
        }
        final AffyData that = (AffyData)object;
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
     * Constructs new instances of {@link AffyData}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link AffyData}.
         * @return new AffyDataImpl()
         */
        public static AffyData newInstance()
        {
            return new AffyDataImpl();
        }

        /**
         * Constructs a new instance of {@link AffyData}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param detection String
         * @param affyIdentifierType AffyIdentifierType
         * @param cellLineType CellLineType
         * @return newInstance
         */
        public static AffyData newInstance(String detection, AffyIdentifierType affyIdentifierType, CellLineType cellLineType)
        {
            final AffyData entity = new AffyDataImpl();
            entity.setDetection(detection);
            entity.setAffyIdentifierType(affyIdentifierType);
            entity.setCellLineType(cellLineType);
            return entity;
        }

        /**
         * Constructs a new instance of {@link AffyData}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param statPairs Integer
         * @param statPairsUsed Integer
         * @param signal Double
         * @param detection String
         * @param detectionPValue Double
         * @param affyIdentifierType AffyIdentifierType
         * @param cellLineType CellLineType
         * @return newInstance AffyData
         */
        public static AffyData newInstance(Integer statPairs, Integer statPairsUsed, Double signal, String detection, Double detectionPValue, AffyIdentifierType affyIdentifierType, CellLineType cellLineType)
        {
            final AffyData entity = new AffyDataImpl();
            entity.setStatPairs(statPairs);
            entity.setStatPairsUsed(statPairsUsed);
            entity.setSignal(signal);
            entity.setDetection(detection);
            entity.setDetectionPValue(detectionPValue);
            entity.setAffyIdentifierType(affyIdentifierType);
            entity.setCellLineType(cellLineType);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(AffyData o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
            if (this.getStatPairs() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getStatPairs().compareTo(o.getStatPairs()));
            }
            if (this.getStatPairsUsed() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getStatPairsUsed().compareTo(o.getStatPairsUsed()));
            }
            if (this.getSignal() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSignal().compareTo(o.getSignal()));
            }
            if (this.getDetection() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDetection().compareTo(o.getDetection()));
            }
            if (this.getDetectionPValue() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDetectionPValue().compareTo(o.getDetectionPValue()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}