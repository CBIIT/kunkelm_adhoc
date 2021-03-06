// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: hibernate/HibernateEntity.vsl in andromda-hibernate-cartridge.
//
package mwk.pptp.androdomain;

import java.io.Serializable;

/**
 * 
 */
// HibernateEntity.vsl annotations merge-point
public abstract class MouseCrosstab
    implements Serializable, Comparable<MouseCrosstab>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 5575708683986679495L;

    // Generate 11 attributes
    private Double mouse1;

    /**
     * 
     * @return this.mouse1 Double
     */
    public Double getMouse1()
    {
        return this.mouse1;
    }

    /**
     * 
     * @param mouse1In Double
     */
    public void setMouse1(Double mouse1In)
    {
        this.mouse1 = mouse1In;
    }

    private Double mouse2;

    /**
     * 
     * @return this.mouse2 Double
     */
    public Double getMouse2()
    {
        return this.mouse2;
    }

    /**
     * 
     * @param mouse2In Double
     */
    public void setMouse2(Double mouse2In)
    {
        this.mouse2 = mouse2In;
    }

    private Double mouse3;

    /**
     * 
     * @return this.mouse3 Double
     */
    public Double getMouse3()
    {
        return this.mouse3;
    }

    /**
     * 
     * @param mouse3In Double
     */
    public void setMouse3(Double mouse3In)
    {
        this.mouse3 = mouse3In;
    }

    private Double mouse4;

    /**
     * 
     * @return this.mouse4 Double
     */
    public Double getMouse4()
    {
        return this.mouse4;
    }

    /**
     * 
     * @param mouse4In Double
     */
    public void setMouse4(Double mouse4In)
    {
        this.mouse4 = mouse4In;
    }

    private Double mouse5;

    /**
     * 
     * @return this.mouse5 Double
     */
    public Double getMouse5()
    {
        return this.mouse5;
    }

    /**
     * 
     * @param mouse5In Double
     */
    public void setMouse5(Double mouse5In)
    {
        this.mouse5 = mouse5In;
    }

    private Double mouse6;

    /**
     * 
     * @return this.mouse6 Double
     */
    public Double getMouse6()
    {
        return this.mouse6;
    }

    /**
     * 
     * @param mouse6In Double
     */
    public void setMouse6(Double mouse6In)
    {
        this.mouse6 = mouse6In;
    }

    private Double mouse7;

    /**
     * 
     * @return this.mouse7 Double
     */
    public Double getMouse7()
    {
        return this.mouse7;
    }

    /**
     * 
     * @param mouse7In Double
     */
    public void setMouse7(Double mouse7In)
    {
        this.mouse7 = mouse7In;
    }

    private Double mouse8;

    /**
     * 
     * @return this.mouse8 Double
     */
    public Double getMouse8()
    {
        return this.mouse8;
    }

    /**
     * 
     * @param mouse8In Double
     */
    public void setMouse8(Double mouse8In)
    {
        this.mouse8 = mouse8In;
    }

    private Double mouse9;

    /**
     * 
     * @return this.mouse9 Double
     */
    public Double getMouse9()
    {
        return this.mouse9;
    }

    /**
     * 
     * @param mouse9In Double
     */
    public void setMouse9(Double mouse9In)
    {
        this.mouse9 = mouse9In;
    }

    private Double mouse10;

    /**
     * 
     * @return this.mouse10 Double
     */
    public Double getMouse10()
    {
        return this.mouse10;
    }

    /**
     * 
     * @param mouse10In Double
     */
    public void setMouse10(Double mouse10In)
    {
        this.mouse10 = mouse10In;
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
    private MouseGroup mouseGroup;

    /**
     * 
     * @return this.mouseGroup MouseGroup
     */
    public MouseGroup getMouseGroup()
    {
        return this.mouseGroup;
    }

    /**
     * 
     * @param mouseGroupIn MouseGroup
     */
    public void setMouseGroup(MouseGroup mouseGroupIn)
    {
        this.mouseGroup = mouseGroupIn;
    }

    /**
     * Returns <code>true</code> if the argument is an MouseCrosstab instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof MouseCrosstab))
        {
            return false;
        }
        final MouseCrosstab that = (MouseCrosstab)object;
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
     * Constructs new instances of {@link MouseCrosstab}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link MouseCrosstab}.
         * @return new MouseCrosstabImpl()
         */
        public static MouseCrosstab newInstance()
        {
            return new MouseCrosstabImpl();
        }

        /**
         * Constructs a new instance of {@link MouseCrosstab}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param mouseGroup MouseGroup
         * @return newInstance
         */
        public static MouseCrosstab newInstance(MouseGroup mouseGroup)
        {
            final MouseCrosstab entity = new MouseCrosstabImpl();
            entity.setMouseGroup(mouseGroup);
            return entity;
        }

        /**
         * Constructs a new instance of {@link MouseCrosstab}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param mouse1 Double
         * @param mouse2 Double
         * @param mouse3 Double
         * @param mouse4 Double
         * @param mouse5 Double
         * @param mouse6 Double
         * @param mouse7 Double
         * @param mouse8 Double
         * @param mouse9 Double
         * @param mouse10 Double
         * @param mouseGroup MouseGroup
         * @return newInstance MouseCrosstab
         */
        public static MouseCrosstab newInstance(Double mouse1, Double mouse2, Double mouse3, Double mouse4, Double mouse5, Double mouse6, Double mouse7, Double mouse8, Double mouse9, Double mouse10, MouseGroup mouseGroup)
        {
            final MouseCrosstab entity = new MouseCrosstabImpl();
            entity.setMouse1(mouse1);
            entity.setMouse2(mouse2);
            entity.setMouse3(mouse3);
            entity.setMouse4(mouse4);
            entity.setMouse5(mouse5);
            entity.setMouse6(mouse6);
            entity.setMouse7(mouse7);
            entity.setMouse8(mouse8);
            entity.setMouse9(mouse9);
            entity.setMouse10(mouse10);
            entity.setMouseGroup(mouseGroup);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(MouseCrosstab o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
            if (this.getMouse1() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMouse1().compareTo(o.getMouse1()));
            }
            if (this.getMouse2() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMouse2().compareTo(o.getMouse2()));
            }
            if (this.getMouse3() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMouse3().compareTo(o.getMouse3()));
            }
            if (this.getMouse4() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMouse4().compareTo(o.getMouse4()));
            }
            if (this.getMouse5() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMouse5().compareTo(o.getMouse5()));
            }
            if (this.getMouse6() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMouse6().compareTo(o.getMouse6()));
            }
            if (this.getMouse7() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMouse7().compareTo(o.getMouse7()));
            }
            if (this.getMouse8() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMouse8().compareTo(o.getMouse8()));
            }
            if (this.getMouse9() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMouse9().compareTo(o.getMouse9()));
            }
            if (this.getMouse10() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMouse10().compareTo(o.getMouse10()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}