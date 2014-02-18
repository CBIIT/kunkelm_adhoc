// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: hibernate/HibernateEntity.vsl in andromda-hibernate-cartridge.
//
package andromda;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * 
 */
// HibernateEntity.vsl annotations merge-point
public abstract class DrugTrackerTarget
    implements Serializable, Comparable<DrugTrackerTarget>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 3501511909697140102L;

    // Generate 2 attributes
    private String target;

    /**
     * 
     * @return this.target String
     */
    public String getTarget()
    {
        return this.target;
    }

    /**
     * 
     * @param targetIn String
     */
    public void setTarget(String targetIn)
    {
        this.target = targetIn;
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
    private Collection<DrugTracker> drugTrackers = new HashSet<DrugTracker>();

    /**
     * 
     * @return this.drugTrackers Collection<DrugTracker>
     */
    public Collection<DrugTracker> getDrugTrackers()
    {
        return this.drugTrackers;
    }

    /**
     * 
     * @param drugTrackersIn Collection<DrugTracker>
     */
    public void setDrugTrackers(Collection<DrugTracker> drugTrackersIn)
    {
        this.drugTrackers = drugTrackersIn;
    }

    /**
     * 
     * @param elementToAdd DrugTracker
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addDrugTrackers(DrugTracker elementToAdd)
    {
        return this.drugTrackers.add(elementToAdd);
    }

    /**
     * 
     * @param elementToRemove DrugTracker
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeDrugTrackers(DrugTracker elementToRemove)
    {
        return this.drugTrackers.remove(elementToRemove);
    }

    /**
     * Returns <code>true</code> if the argument is an DrugTrackerTarget instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof DrugTrackerTarget))
        {
            return false;
        }
        final DrugTrackerTarget that = (DrugTrackerTarget)object;
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
     * Constructs new instances of {@link DrugTrackerTarget}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link DrugTrackerTarget}.
         * @return new DrugTrackerTargetImpl()
         */
        public static DrugTrackerTarget newInstance()
        {
            return new DrugTrackerTargetImpl();
        }

        /**
         * Constructs a new instance of {@link DrugTrackerTarget}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param target String
         * @return newInstance
         */
        public static DrugTrackerTarget newInstance(String target)
        {
            final DrugTrackerTarget entity = new DrugTrackerTargetImpl();
            entity.setTarget(target);
            return entity;
        }

        /**
         * Constructs a new instance of {@link DrugTrackerTarget}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param target String
         * @param drugTrackers Collection<DrugTracker>
         * @return newInstance DrugTrackerTarget
         */
        public static DrugTrackerTarget newInstance(String target, Collection<DrugTracker> drugTrackers)
        {
            final DrugTrackerTarget entity = new DrugTrackerTargetImpl();
            entity.setTarget(target);
            entity.setDrugTrackers(drugTrackers);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(DrugTrackerTarget o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
            if (this.getTarget() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTarget().compareTo(o.getTarget()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}