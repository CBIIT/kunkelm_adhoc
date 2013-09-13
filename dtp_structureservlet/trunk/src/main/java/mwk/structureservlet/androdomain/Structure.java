// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: hibernate/HibernateEntity.vsl in andromda-hibernate-cartridge.
//
package mwk.structureservlet.androdomain;

import java.io.Serializable;

/**
 * 
 */
// HibernateEntity.vsl annotations merge-point
public abstract class Structure
    implements Serializable, Comparable<Structure>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 5476086814005296027L;

    // Generate 3 attributes
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

    private String smiles;

    /**
     * 
     * @return this.smiles String
     */
    public String getSmiles()
    {
        return this.smiles;
    }

    /**
     * 
     * @param smilesIn String
     */
    public void setSmiles(String smilesIn)
    {
        this.smiles = smilesIn;
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
     * Returns <code>true</code> if the argument is an Structure instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof Structure))
        {
            return false;
        }
        final Structure that = (Structure)object;
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
     * Constructs new instances of {@link Structure}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link Structure}.
         * @return new StructureImpl()
         */
        public static Structure newInstance()
        {
            return new StructureImpl();
        }

        /**
         * Constructs a new instance of {@link Structure}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param smiles String
         * @return newInstance
         */
        public static Structure newInstance(String smiles)
        {
            final Structure entity = new StructureImpl();
            entity.setSmiles(smiles);
            return entity;
        }

        /**
         * Constructs a new instance of {@link Structure}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param nsc Integer
         * @param smiles String
         * @return newInstance Structure
         */
        public static Structure newInstance(Integer nsc, String smiles)
        {
            final Structure entity = new StructureImpl();
            entity.setNsc(nsc);
            entity.setSmiles(smiles);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(Structure o)
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
            if (this.getSmiles() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSmiles().compareTo(o.getSmiles()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}