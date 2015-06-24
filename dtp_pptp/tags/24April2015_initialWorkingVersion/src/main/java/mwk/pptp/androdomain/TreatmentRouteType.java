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
public abstract class TreatmentRouteType
    implements Serializable, Comparable<TreatmentRouteType>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 238727498678150504L;

    // Generate 2 attributes
    private String displayName;

    /**
     * 
     * @return this.displayName String
     */
    public String getDisplayName()
    {
        return this.displayName;
    }

    /**
     * 
     * @param displayNameIn String
     */
    public void setDisplayName(String displayNameIn)
    {
        this.displayName = displayNameIn;
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

    // Generate 1 associations
    private Collection<CellLineGroup> cellLineGroups = new HashSet<CellLineGroup>();

    /**
     * 
     * @return this.cellLineGroups Collection<CellLineGroup>
     */
    public Collection<CellLineGroup> getCellLineGroups()
    {
        return this.cellLineGroups;
    }

    /**
     * 
     * @param cellLineGroupsIn Collection<CellLineGroup>
     */
    public void setCellLineGroups(Collection<CellLineGroup> cellLineGroupsIn)
    {
        this.cellLineGroups = cellLineGroupsIn;
    }

    /**
     * 
     * @param elementToAdd CellLineGroup
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addCellLineGroups(CellLineGroup elementToAdd)
    {
        return this.cellLineGroups.add(elementToAdd);
    }

    /**
     * 
     * @param elementToRemove CellLineGroup
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeCellLineGroups(CellLineGroup elementToRemove)
    {
        return this.cellLineGroups.remove(elementToRemove);
    }

    /**
     * Returns <code>true</code> if the argument is an TreatmentRouteType instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof TreatmentRouteType))
        {
            return false;
        }
        final TreatmentRouteType that = (TreatmentRouteType)object;
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
     * Constructs new instances of {@link TreatmentRouteType}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link TreatmentRouteType}.
         * @return new TreatmentRouteTypeImpl()
         */
        public static TreatmentRouteType newInstance()
        {
            return new TreatmentRouteTypeImpl();
        }

        /**
         * Constructs a new instance of {@link TreatmentRouteType}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param displayName String
         * @return newInstance
         */
        public static TreatmentRouteType newInstance(String displayName)
        {
            final TreatmentRouteType entity = new TreatmentRouteTypeImpl();
            entity.setDisplayName(displayName);
            return entity;
        }

        /**
         * Constructs a new instance of {@link TreatmentRouteType}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param displayName String
         * @param cellLineGroups Collection<CellLineGroup>
         * @return newInstance TreatmentRouteType
         */
        public static TreatmentRouteType newInstance(String displayName, Collection<CellLineGroup> cellLineGroups)
        {
            final TreatmentRouteType entity = new TreatmentRouteTypeImpl();
            entity.setDisplayName(displayName);
            entity.setCellLineGroups(cellLineGroups);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(TreatmentRouteType o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
            if (this.getDisplayName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDisplayName().compareTo(o.getDisplayName()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}