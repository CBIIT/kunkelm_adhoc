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
public abstract class CmpdListMember
    implements Serializable, Comparable<CmpdListMember>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -2161843131792164045L;

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

    // Generate 2 associations
    private Cmpd cmpd;

    /**
     * 
     * @return this.cmpd Cmpd
     */
    public Cmpd getCmpd()
    {
        return this.cmpd;
    }

    /**
     * 
     * @param cmpdIn Cmpd
     */
    public void setCmpd(Cmpd cmpdIn)
    {
        this.cmpd = cmpdIn;
    }

    private CmpdList cmpdList;

    /**
     * 
     * @return this.cmpdList CmpdList
     */
    public CmpdList getCmpdList()
    {
        return this.cmpdList;
    }

    /**
     * 
     * @param cmpdListIn CmpdList
     */
    public void setCmpdList(CmpdList cmpdListIn)
    {
        this.cmpdList = cmpdListIn;
    }

    /**
     * Returns <code>true</code> if the argument is an CmpdListMember instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof CmpdListMember))
        {
            return false;
        }
        final CmpdListMember that = (CmpdListMember)object;
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
     * Constructs new instances of {@link CmpdListMember}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link CmpdListMember}.
         * @return new CmpdListMemberImpl()
         */
        public static CmpdListMember newInstance()
        {
            return new CmpdListMemberImpl();
        }


        /**
         * Constructs a new instance of {@link CmpdListMember}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param cmpd Cmpd
         * @param cmpdList CmpdList
         * @return newInstance CmpdListMember
         */
        public static CmpdListMember newInstance(Cmpd cmpd, CmpdList cmpdList)
        {
            final CmpdListMember entity = new CmpdListMemberImpl();
            entity.setCmpd(cmpd);
            entity.setCmpdList(cmpdList);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(CmpdListMember o)
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