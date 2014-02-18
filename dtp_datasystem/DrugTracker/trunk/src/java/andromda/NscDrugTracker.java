// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: hibernate/HibernateEntity.vsl in andromda-hibernate-cartridge.
//
package andromda;

import java.util.Collection;

/**
 * 
 */
// HibernateEntity.vsl annotations merge-point
public abstract class NscDrugTracker
    extends DrugTrackerImpl
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -8786813709166955247L;

    // Generate 2 attributes
    private Integer nsc;

    /**
     * 
     * @return this.nsc Integer
     */
    public Integer getNsc()
    {
        return this.nsc;
    }

    /**
     * 
     * @param nscIn Integer
     */
    public void setNsc(Integer nscIn)
    {
        this.nsc = nscIn;
    }

    private String prefix;

    /**
     * 
     * @return this.prefix String
     */
    public String getPrefix()
    {
        return this.prefix;
    }

    /**
     * 
     * @param prefixIn String
     */
    public void setPrefix(String prefixIn)
    {
        this.prefix = prefixIn;
    }

    // Generate 1 associations
    private StandardizedSmiles standardizedSmiles;

    /**
     * 
     * @return this.standardizedSmiles StandardizedSmiles
     */
    public StandardizedSmiles getStandardizedSmiles()
    {
        return this.standardizedSmiles;
    }

    /**
     * 
     * @param standardizedSmilesIn StandardizedSmiles
     */
    public void setStandardizedSmiles(StandardizedSmiles standardizedSmilesIn)
    {
        this.standardizedSmiles = standardizedSmilesIn;
    }

    /**
     * This entity does not have any identifiers
     * but since it extends the <code>DrugTrackerImpl</code> class
     * it will simply delegate the call up there.
     *
     * @see mwk.drugtracker.domain.DrugTracker#equals(Object)
     */
    @Override
    public boolean equals(Object object)
    {
        return super.equals(object);
    }

    /**
     * This entity does not have any identifiers
     * but since it extends the <code>DrugTrackerImpl</code> class
     * it will simply delegate the call up there.
     *
     * @see mwk.drugtracker.domain.DrugTracker#hashCode()
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    /**
     * Constructs new instances of {@link NscDrugTracker}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link NscDrugTracker}.
         * @return new NscDrugTrackerImpl()
         */
        public static NscDrugTracker newInstance()
        {
            return new NscDrugTrackerImpl();
        }


        /**
         * Constructs a new instance of {@link NscDrugTracker}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param agent String
         * @param originator String
         * @param cas String
         * @param drugTrackerPlates Collection<DrugTrackerPlate>
         * @param drugTrackerAliases Collection<DrugTrackerAlias>
         * @param drugTrackerTargets Collection<DrugTrackerTarget>
         * @param drugTrackerSets Collection<DrugTrackerSet>
         * @param nsc Integer
         * @param prefix String
         * @param standardizedSmiles StandardizedSmiles
         * @return newInstance NscDrugTracker
         */
        public static NscDrugTracker newInstance(String agent, String originator, String cas, Collection<DrugTrackerPlate> drugTrackerPlates, Collection<DrugTrackerAlias> drugTrackerAliases, Collection<DrugTrackerTarget> drugTrackerTargets, Collection<DrugTrackerSet> drugTrackerSets, Integer nsc, String prefix, StandardizedSmiles standardizedSmiles)
        {
            final NscDrugTracker entity = new NscDrugTrackerImpl();
            entity.setAgent(agent);
            entity.setOriginator(originator);
            entity.setCas(cas);
            entity.setDrugTrackerPlates(drugTrackerPlates);
            entity.setDrugTrackerAliases(drugTrackerAliases);
            entity.setDrugTrackerTargets(drugTrackerTargets);
            entity.setDrugTrackerSets(drugTrackerSets);
            entity.setNsc(nsc);
            entity.setPrefix(prefix);
            entity.setStandardizedSmiles(standardizedSmiles);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(NscDrugTracker o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
            if (this.getNsc() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getNsc().compareTo(o.getNsc()));
            }
            if (this.getPrefix() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPrefix().compareTo(o.getPrefix()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}