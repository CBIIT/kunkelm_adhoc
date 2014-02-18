// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: hibernate/HibernateEntity.vsl in andromda-hibernate-cartridge.
//
package mwk.datasystem.domain;

import java.io.Serializable;

/**
 * 
 */
// HibernateEntity.vsl annotations merge-point
public abstract class CmpdSet
    implements Serializable, Comparable<CmpdSet>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 5236261034937113446L;

    // Generate 2 attributes
    private String setName;

    /**
     * 
     * @return this.setName String
     */
    public String getSetName()
    {
        return this.setName;
    }

    /**
     * 
     * @param setNameIn String
     */
    public void setSetName(String setNameIn)
    {
        this.setName = setNameIn;
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
    /**
     * Returns <code>true</code> if the argument is an CmpdSet instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof CmpdSet))
        {
            return false;
        }
        final CmpdSet that = (CmpdSet)object;
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
     * Constructs new instances of {@link CmpdSet}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link CmpdSet}.
         * @return new CmpdSetImpl()
         */
        public static CmpdSet newInstance()
        {
            return new CmpdSetImpl();
        }


        /**
         * Constructs a new instance of {@link CmpdSet}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param setName String
         * @return newInstance CmpdSet
         */
        public static CmpdSet newInstance(String setName)
        {
            final CmpdSet entity = new CmpdSetImpl();
            entity.setSetName(setName);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(CmpdSet o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
            if (this.getSetName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSetName().compareTo(o.getSetName()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}