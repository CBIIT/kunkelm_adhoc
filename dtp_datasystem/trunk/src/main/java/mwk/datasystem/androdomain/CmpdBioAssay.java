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
public abstract class CmpdBioAssay
    implements Serializable, Comparable<CmpdBioAssay>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -368057500139744467L;

    // Generate 4 attributes
    private Integer nci60;

    /**
     * 
     * @return this.nci60 Integer
     */
    public Integer getNci60()
    {
        return this.nci60;
    }

    /**
     * 
     * @param nci60In Integer
     */
    public void setNci60(Integer nci60In)
    {
        this.nci60 = nci60In;
    }

    private Integer hf;

    /**
     * 
     * @return this.hf Integer
     */
    public Integer getHf()
    {
        return this.hf;
    }

    /**
     * 
     * @param hfIn Integer
     */
    public void setHf(Integer hfIn)
    {
        this.hf = hfIn;
    }

    private Integer xeno;

    /**
     * 
     * @return this.xeno Integer
     */
    public Integer getXeno()
    {
        return this.xeno;
    }

    /**
     * 
     * @param xenoIn Integer
     */
    public void setXeno(Integer xenoIn)
    {
        this.xeno = xenoIn;
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
     * Returns <code>true</code> if the argument is an CmpdBioAssay instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof CmpdBioAssay))
        {
            return false;
        }
        final CmpdBioAssay that = (CmpdBioAssay)object;
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
     * Constructs new instances of {@link CmpdBioAssay}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link CmpdBioAssay}.
         * @return new CmpdBioAssayImpl()
         */
        public static CmpdBioAssay newInstance()
        {
            return new CmpdBioAssayImpl();
        }


        /**
         * Constructs a new instance of {@link CmpdBioAssay}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param nci60 Integer
         * @param hf Integer
         * @param xeno Integer
         * @return newInstance CmpdBioAssay
         */
        public static CmpdBioAssay newInstance(Integer nci60, Integer hf, Integer xeno)
        {
            final CmpdBioAssay entity = new CmpdBioAssayImpl();
            entity.setNci60(nci60);
            entity.setHf(hf);
            entity.setXeno(xeno);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(CmpdBioAssay o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
            if (this.getNci60() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getNci60().compareTo(o.getNci60()));
            }
            if (this.getHf() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getHf().compareTo(o.getHf()));
            }
            if (this.getXeno() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getXeno().compareTo(o.getXeno()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}