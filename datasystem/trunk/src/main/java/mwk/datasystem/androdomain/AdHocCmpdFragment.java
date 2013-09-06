// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: hibernate/HibernateEntity.vsl in andromda-hibernate-cartridge.
//
package mwk.datasystem.androdomain;

import java.io.Serializable;

/**
 * 
 */
// HibernateEntity.vsl annotations merge-point
public abstract class AdHocCmpdFragment
    implements Serializable, Comparable<AdHocCmpdFragment>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -7847272252448003730L;

    // Generate 1 attributes
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

    // Generate 4 associations
    private AdHocCmpdFragmentPChem adHocCmpdFragmentPChem;

    /**
     * 
     * @return this.adHocCmpdFragmentPChem AdHocCmpdFragmentPChem
     */
    public AdHocCmpdFragmentPChem getAdHocCmpdFragmentPChem()
    {
        return this.adHocCmpdFragmentPChem;
    }

    /**
     * 
     * @param adHocCmpdFragmentPChemIn AdHocCmpdFragmentPChem
     */
    public void setAdHocCmpdFragmentPChem(AdHocCmpdFragmentPChem adHocCmpdFragmentPChemIn)
    {
        this.adHocCmpdFragmentPChem = adHocCmpdFragmentPChemIn;
    }

    private AdHocCmpdFragmentStructure adHocCmpdFragmentStructure;

    /**
     * 
     * @return this.adHocCmpdFragmentStructure AdHocCmpdFragmentStructure
     */
    public AdHocCmpdFragmentStructure getAdHocCmpdFragmentStructure()
    {
        return this.adHocCmpdFragmentStructure;
    }

    /**
     * 
     * @param adHocCmpdFragmentStructureIn AdHocCmpdFragmentStructure
     */
    public void setAdHocCmpdFragmentStructure(AdHocCmpdFragmentStructure adHocCmpdFragmentStructureIn)
    {
        this.adHocCmpdFragmentStructure = adHocCmpdFragmentStructureIn;
    }

    /**
     * Returns <code>true</code> if the argument is an AdHocCmpdFragment instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof AdHocCmpdFragment))
        {
            return false;
        }
        final AdHocCmpdFragment that = (AdHocCmpdFragment)object;
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
     * Constructs new instances of {@link AdHocCmpdFragment}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link AdHocCmpdFragment}.
         * @return new AdHocCmpdFragmentImpl()
         */
        public static AdHocCmpdFragment newInstance()
        {
            return new AdHocCmpdFragmentImpl();
        }


        /**
         * Constructs a new instance of {@link AdHocCmpdFragment}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param adHocCmpdFragmentPChem AdHocCmpdFragmentPChem
         * @param adHocCmpdFragmentStructure AdHocCmpdFragmentStructure
         * @return newInstance AdHocCmpdFragment
         */
        public static AdHocCmpdFragment newInstance(AdHocCmpdFragmentPChem adHocCmpdFragmentPChem, AdHocCmpdFragmentStructure adHocCmpdFragmentStructure)
        {
            final AdHocCmpdFragment entity = new AdHocCmpdFragmentImpl();
            entity.setAdHocCmpdFragmentPChem(adHocCmpdFragmentPChem);
            entity.setAdHocCmpdFragmentStructure(adHocCmpdFragmentStructure);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(AdHocCmpdFragment o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}