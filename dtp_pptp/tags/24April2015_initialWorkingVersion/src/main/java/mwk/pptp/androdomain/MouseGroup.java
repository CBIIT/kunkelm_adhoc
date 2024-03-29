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
public abstract class MouseGroup
    implements Serializable, Comparable<MouseGroup>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 8124550845525401858L;

    // Generate 2 attributes
    private Integer day;

    /**
     * 
     * @return this.day Integer
     */
    public Integer getDay()
    {
        return this.day;
    }

    /**
     * 
     * @param dayIn Integer
     */
    public void setDay(Integer dayIn)
    {
        this.day = dayIn;
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

    // Generate 4 associations
    private MouseCrosstab mouseCrosstab;

    /**
     * 
     * @return this.mouseCrosstab MouseCrosstab
     */
    public MouseCrosstab getMouseCrosstab()
    {
        return this.mouseCrosstab;
    }

    /**
     * 
     * @param mouseCrosstabIn MouseCrosstab
     */
    public void setMouseCrosstab(MouseCrosstab mouseCrosstabIn)
    {
        this.mouseCrosstab = mouseCrosstabIn;
    }

    private CellLineGroup cellLineGroup;

    /**
     * 
     * @return this.cellLineGroup CellLineGroup
     */
    public CellLineGroup getCellLineGroup()
    {
        return this.cellLineGroup;
    }

    /**
     * 
     * @param cellLineGroupIn CellLineGroup
     */
    public void setCellLineGroup(CellLineGroup cellLineGroupIn)
    {
        this.cellLineGroup = cellLineGroupIn;
    }

    private Collection<Mouse> mice = new HashSet<Mouse>();

    /**
     * 
     * @return this.mice Collection<Mouse>
     */
    public Collection<Mouse> getMice()
    {
        return this.mice;
    }

    /**
     * 
     * @param miceIn Collection<Mouse>
     */
    public void setMice(Collection<Mouse> miceIn)
    {
        this.mice = miceIn;
    }

    /**
     * 
     * @param elementToAdd Mouse
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addMice(Mouse elementToAdd)
    {
        return this.mice.add(elementToAdd);
    }

    /**
     * 
     * @param elementToRemove Mouse
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeMice(Mouse elementToRemove)
    {
        return this.mice.remove(elementToRemove);
    }

    private GroupRoleType groupRoleType;

    /**
     * 
     * @return this.groupRoleType GroupRoleType
     */
    public GroupRoleType getGroupRoleType()
    {
        return this.groupRoleType;
    }

    /**
     * 
     * @param groupRoleTypeIn GroupRoleType
     */
    public void setGroupRoleType(GroupRoleType groupRoleTypeIn)
    {
        this.groupRoleType = groupRoleTypeIn;
    }

    /**
     * Returns <code>true</code> if the argument is an MouseGroup instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof MouseGroup))
        {
            return false;
        }
        final MouseGroup that = (MouseGroup)object;
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
     * Constructs new instances of {@link MouseGroup}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link MouseGroup}.
         * @return new MouseGroupImpl()
         */
        public static MouseGroup newInstance()
        {
            return new MouseGroupImpl();
        }

        /**
         * Constructs a new instance of {@link MouseGroup}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param cellLineGroup CellLineGroup
         * @param groupRoleType GroupRoleType
         * @return newInstance
         */
        public static MouseGroup newInstance(CellLineGroup cellLineGroup, GroupRoleType groupRoleType)
        {
            final MouseGroup entity = new MouseGroupImpl();
            entity.setCellLineGroup(cellLineGroup);
            entity.setGroupRoleType(groupRoleType);
            return entity;
        }

        /**
         * Constructs a new instance of {@link MouseGroup}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param day Integer
         * @param mouseCrosstab MouseCrosstab
         * @param cellLineGroup CellLineGroup
         * @param mice Collection<Mouse>
         * @param groupRoleType GroupRoleType
         * @return newInstance MouseGroup
         */
        public static MouseGroup newInstance(Integer day, MouseCrosstab mouseCrosstab, CellLineGroup cellLineGroup, Collection<Mouse> mice, GroupRoleType groupRoleType)
        {
            final MouseGroup entity = new MouseGroupImpl();
            entity.setDay(day);
            entity.setMouseCrosstab(mouseCrosstab);
            entity.setCellLineGroup(cellLineGroup);
            entity.setMice(mice);
            entity.setGroupRoleType(groupRoleType);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(MouseGroup o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
            if (this.getDay() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDay().compareTo(o.getDay()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}