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
public abstract class MouseType
    implements Serializable, Comparable<MouseType>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -7369607654406947142L;

    // Generate 5 attributes
    private String strain;

    /**
     * 
     * @return this.strain String
     */
    public String getStrain()
    {
        return this.strain;
    }

    /**
     * 
     * @param strainIn String
     */
    public void setStrain(String strainIn)
    {
        this.strain = strainIn;
    }

    private String gender;

    /**
     * 
     * @return this.gender String
     */
    public String getGender()
    {
        return this.gender;
    }

    /**
     * 
     * @param genderIn String
     */
    public void setGender(String genderIn)
    {
        this.gender = genderIn;
    }

    private Integer age;

    /**
     * 
     * @return this.age Integer
     */
    public Integer getAge()
    {
        return this.age;
    }

    /**
     * 
     * @param ageIn Integer
     */
    public void setAge(Integer ageIn)
    {
        this.age = ageIn;
    }

    private String source;

    /**
     * 
     * @return this.source String
     */
    public String getSource()
    {
        return this.source;
    }

    /**
     * 
     * @param sourceIn String
     */
    public void setSource(String sourceIn)
    {
        this.source = sourceIn;
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
     * Returns <code>true</code> if the argument is an MouseType instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof MouseType))
        {
            return false;
        }
        final MouseType that = (MouseType)object;
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
     * Constructs new instances of {@link MouseType}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link MouseType}.
         * @return new MouseTypeImpl()
         */
        public static MouseType newInstance()
        {
            return new MouseTypeImpl();
        }

        /**
         * Constructs a new instance of {@link MouseType}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param strain String
         * @return newInstance
         */
        public static MouseType newInstance(String strain)
        {
            final MouseType entity = new MouseTypeImpl();
            entity.setStrain(strain);
            return entity;
        }

        /**
         * Constructs a new instance of {@link MouseType}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param strain String
         * @param gender String
         * @param age Integer
         * @param source String
         * @param cellLineGroups Collection<CellLineGroup>
         * @return newInstance MouseType
         */
        public static MouseType newInstance(String strain, String gender, Integer age, String source, Collection<CellLineGroup> cellLineGroups)
        {
            final MouseType entity = new MouseTypeImpl();
            entity.setStrain(strain);
            entity.setGender(gender);
            entity.setAge(age);
            entity.setSource(source);
            entity.setCellLineGroups(cellLineGroups);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(MouseType o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
            if (this.getStrain() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getStrain().compareTo(o.getStrain()));
            }
            if (this.getGender() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getGender().compareTo(o.getGender()));
            }
            if (this.getAge() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getAge().compareTo(o.getAge()));
            }
            if (this.getSource() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSource().compareTo(o.getSource()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}