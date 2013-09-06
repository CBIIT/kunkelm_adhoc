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
public abstract class CmpdProject
    implements Serializable, Comparable<CmpdProject>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 4691884644037899978L;

    // Generate 3 attributes
    private String projectCode;

    /**
     * 
     * @return this.projectCode String
     */
    public String getProjectCode()
    {
        return this.projectCode;
    }

    /**
     * 
     * @param projectCodeIn String
     */
    public void setProjectCode(String projectCodeIn)
    {
        this.projectCode = projectCodeIn;
    }

    private String projectName;

    /**
     * 
     * @return this.projectName String
     */
    public String getProjectName()
    {
        return this.projectName;
    }

    /**
     * 
     * @param projectNameIn String
     */
    public void setProjectName(String projectNameIn)
    {
        this.projectName = projectNameIn;
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
     * Returns <code>true</code> if the argument is an CmpdProject instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof CmpdProject))
        {
            return false;
        }
        final CmpdProject that = (CmpdProject)object;
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
     * Constructs new instances of {@link CmpdProject}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link CmpdProject}.
         * @return new CmpdProjectImpl()
         */
        public static CmpdProject newInstance()
        {
            return new CmpdProjectImpl();
        }


        /**
         * Constructs a new instance of {@link CmpdProject}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param projectCode String
         * @param projectName String
         * @return newInstance CmpdProject
         */
        public static CmpdProject newInstance(String projectCode, String projectName)
        {
            final CmpdProject entity = new CmpdProjectImpl();
            entity.setProjectCode(projectCode);
            entity.setProjectName(projectName);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(CmpdProject o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
            if (this.getProjectCode() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getProjectCode().compareTo(o.getProjectCode()));
            }
            if (this.getProjectName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getProjectName().compareTo(o.getProjectName()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}