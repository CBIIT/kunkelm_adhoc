// license-header java merge-point
//
/**
 * @author Generated on 07/26/2013 05:52:34-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::PptpModel::mwk.pptp::vo::MouseGroupWithMouseCrosstabVO
 * STEREOTYPE:   ValueObject
 */
package mwk.pptp.vo;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * TODO: Model Documentation for class MouseGroupWithMouseCrosstabVO
 */
public class MouseGroupWithMouseCrosstabVO
    extends MouseGroupVO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -2515682033831462310L;

    // Class attributes
    /** TODO: Model Documentation for attribute mouseCrosstab */
    protected MouseCrosstabVO mouseCrosstab;

    /** Default Constructor with no properties */
    public MouseGroupWithMouseCrosstabVO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param groupRoleIn String
     * @param mouseCrosstabIn MouseCrosstabVO
     */
    public MouseGroupWithMouseCrosstabVO(final String groupRoleIn, final MouseCrosstabVO mouseCrosstabIn)
    {
        super(groupRoleIn);
        this.mouseCrosstab = mouseCrosstabIn;
    }

    /**
     * Constructor with all properties
     * @param dayIn Integer
     * @param groupRoleIn String
     * @param mouseCrosstabIn MouseCrosstabVO
     */
    public MouseGroupWithMouseCrosstabVO(final Integer dayIn, final String groupRoleIn, final MouseCrosstabVO mouseCrosstabIn)
    {
        super(dayIn, groupRoleIn);
        this.mouseCrosstab = mouseCrosstabIn;
    }

    /**
     * Copies constructor from other MouseGroupWithMouseCrosstabVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public MouseGroupWithMouseCrosstabVO(final MouseGroupWithMouseCrosstabVO otherBean)
    {
        super(otherBean);
        this.mouseCrosstab = otherBean.getMouseCrosstab();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final MouseGroupWithMouseCrosstabVO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setMouseCrosstab(otherBean.getMouseCrosstab());
        }
    }

    /**
     * TODO: Model Documentation for attribute mouseCrosstab
     * Get the mouseCrosstab Attribute
     * @return mouseCrosstab MouseCrosstabVO
     */
    public MouseCrosstabVO getMouseCrosstab()
    {
        return this.mouseCrosstab;
    }

    /**
     * 
     * @param value MouseCrosstabVO
     */
    public void setMouseCrosstab(final MouseCrosstabVO value)
    {
        this.mouseCrosstab = value;
    }

    /**
     * @param object to compare this object against
     * @return boolean if equal
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object==null || object.getClass() != this.getClass())
        {
             return false;
        }
        // Check if the same object instance
        if (object==this)
        {
            return true;
        }
        MouseGroupWithMouseCrosstabVO rhs = (MouseGroupWithMouseCrosstabVO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getMouseCrosstab(), rhs.getMouseCrosstab())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final MouseGroupVO object)
    {
        if (object==null)
        {
            return -1;
        }
        // Check if the same object instance
        if (object==this)
        {
            return 0;
        }
        if (!(object instanceof MouseGroupWithMouseCrosstabVO))
        {
            return -1;
        }
        MouseGroupWithMouseCrosstabVO myClass = (MouseGroupWithMouseCrosstabVO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getMouseCrosstab(), myClass.getMouseCrosstab())
            .toComparison();
    }

    /**
     * @return int hashCode value
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(1249046965, -82296885)
            .appendSuper(super.hashCode())
            .append(this.getMouseCrosstab())
            .toHashCode();
    }

    /**
     * @return String representation of object
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
            .append("day", this.getDay())
            .append("groupRole", this.getGroupRole())
            .append("mouseCrosstab", this.getMouseCrosstab())
            .toString();
    }

    /**
     * Compares the properties of this instance to the properties of the argument. This method will return
     * {@code false} as soon as it detects that the argument is {@code null} or not of the same type as
     * (or a sub-type of) this instance's type.
     *
     * <p/>For array, collection or map properties the comparison will be done one level deep, in other words:
     * the elements will be compared using the {@code equals()} operation.
     *
     * <p/>Note that two properties will be considered equal when both values are {@code null}.
     *
     * @param thatObject the object containing the properties to compare against this instance
     * @return this method will return {@code true} in case the argument has the same type as this class, or is a
     *      sub-type of this class and all properties as found on this class have equal values when queried on that
     *      argument instance; in all other cases this method will return {@code false}
     */
    @Override
    public boolean equalProperties(final Object thatObject)
    {
        if (thatObject == null || !this.getClass().isAssignableFrom(thatObject.getClass()))
        {
            return false;
        }

        final MouseGroupWithMouseCrosstabVO that = (MouseGroupWithMouseCrosstabVO)thatObject;

        return super.equalProperties(that)
            && equal(this.getMouseCrosstab(), that.getMouseCrosstab())
        ;
    }

    // MouseGroupWithMouseCrosstabVO value-object java merge-point
}