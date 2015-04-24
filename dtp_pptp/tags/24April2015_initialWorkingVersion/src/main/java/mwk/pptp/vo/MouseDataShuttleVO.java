// license-header java merge-point
//
/**
 * @author Generated on 07/26/2013 05:52:35-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::PptpModel::mwk.pptp::vo::MouseDataShuttleVO
 * STEREOTYPE:   ValueObject
 */
package mwk.pptp.vo;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * TODO: Model Documentation for class MouseDataShuttleVO
 */
public class MouseDataShuttleVO
    implements Serializable, Comparable<MouseDataShuttleVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 3372258120204907464L;

    // Class attributes
    /** TODO: Model Documentation for attribute mouseNumber */
    protected Integer mouseNumber;
    /** TODO: Model Documentation for attribute groupRole */
    protected String groupRole;
    /** TODO: Model Documentation for attribute day */
    protected Integer day;
    /** TODO: Model Documentation for attribute value */
    protected Double value;

    /** Default Constructor with no properties */
    public MouseDataShuttleVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param groupRoleIn String
     */
    public MouseDataShuttleVO(final String groupRoleIn)
    {
        this.groupRole = groupRoleIn;
    }

    /**
     * Constructor with all properties
     * @param mouseNumberIn Integer
     * @param groupRoleIn String
     * @param dayIn Integer
     * @param valueIn Double
     */
    public MouseDataShuttleVO(final Integer mouseNumberIn, final String groupRoleIn, final Integer dayIn, final Double valueIn)
    {
        this.mouseNumber = mouseNumberIn;
        this.groupRole = groupRoleIn;
        this.day = dayIn;
        this.value = valueIn;
    }

    /**
     * Copies constructor from other MouseDataShuttleVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public MouseDataShuttleVO(final MouseDataShuttleVO otherBean)
    {
        this.mouseNumber = otherBean.getMouseNumber();
        this.groupRole = otherBean.getGroupRole();
        this.day = otherBean.getDay();
        this.value = otherBean.getValue();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final MouseDataShuttleVO otherBean)
    {
        if (null != otherBean)
        {
            this.setMouseNumber(otherBean.getMouseNumber());
            this.setGroupRole(otherBean.getGroupRole());
            this.setDay(otherBean.getDay());
            this.setValue(otherBean.getValue());
        }
    }

    /**
     * TODO: Model Documentation for attribute mouseNumber
     * Get the mouseNumber Attribute
     * @return mouseNumber Integer
     */
    public Integer getMouseNumber()
    {
        return this.mouseNumber;
    }

    /**
     * 
     * @param value Integer
     */
    public void setMouseNumber(final Integer value)
    {
        this.mouseNumber = value;
    }

    /**
     * TODO: Model Documentation for attribute groupRole
     * Get the groupRole Attribute
     * @return groupRole String
     */
    public String getGroupRole()
    {
        return this.groupRole;
    }

    /**
     * 
     * @param value String
     */
    public void setGroupRole(final String value)
    {
        this.groupRole = value;
    }

    /**
     * TODO: Model Documentation for attribute day
     * Get the day Attribute
     * @return day Integer
     */
    public Integer getDay()
    {
        return this.day;
    }

    /**
     * 
     * @param value Integer
     */
    public void setDay(final Integer value)
    {
        this.day = value;
    }

    /**
     * TODO: Model Documentation for attribute value
     * Get the value Attribute
     * @return value Double
     */
    public Double getValue()
    {
        return this.value;
    }

    /**
     * 
     * @param value Double
     */
    public void setValue(final Double value)
    {
        this.value = value;
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
        MouseDataShuttleVO rhs = (MouseDataShuttleVO) object;
        return new EqualsBuilder()
            .append(this.getMouseNumber(), rhs.getMouseNumber())
            .append(this.getGroupRole(), rhs.getGroupRole())
            .append(this.getDay(), rhs.getDay())
            .append(this.getValue(), rhs.getValue())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final MouseDataShuttleVO object)
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
        return new CompareToBuilder()
            .append(this.getMouseNumber(), object.getMouseNumber())
            .append(this.getGroupRole(), object.getGroupRole())
            .append(this.getDay(), object.getDay())
            .append(this.getValue(), object.getValue())
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
            .append(this.getMouseNumber())
            .append(this.getGroupRole())
            .append(this.getDay())
            .append(this.getValue())
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
            .append("mouseNumber", this.getMouseNumber())
            .append("groupRole", this.getGroupRole())
            .append("day", this.getDay())
            .append("value", this.getValue())
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
    public boolean equalProperties(final Object thatObject)
    {
        if (thatObject == null || !this.getClass().isAssignableFrom(thatObject.getClass()))
        {
            return false;
        }

        final MouseDataShuttleVO that = (MouseDataShuttleVO)thatObject;

        return
            equal(this.getMouseNumber(), that.getMouseNumber())
            && equal(this.getGroupRole(), that.getGroupRole())
            && equal(this.getDay(), that.getDay())
            && equal(this.getValue(), that.getValue())
        ;
    }

    /**
     * This is a convenient helper method which is able to detect whether or not two values are equal. Two values
     * are equal when they are both {@code null}, are arrays of the same length with equal elements or are
     * equal objects (this includes {@link java.util.Collection} and {@link java.util.Map} instances).
     *
     * <p/>Note that for array, collection or map instances the comparison runs one level deep.
     *
     * @param first the first object to compare, may be {@code null}
     * @param second the second object to compare, may be {@code null}
     * @return this method will return {@code true} in case both objects are equal as explained above;
     *      in all other cases this method will return {@code false}
     */
    protected static boolean equal(final Object first, final Object second)
    {
        final boolean equal;

        if (first == null)
        {
            equal = (second == null);
        }
        else if (first.getClass().isArray() && (second != null) && second.getClass().isArray())
        {
            equal = Arrays.equals((Object[])first, (Object[])second);
        }
        else // note that the following also covers java.util.Collection and java.util.Map
        {
            equal = first.equals(second);
        }

        return equal;
    }

    // MouseDataShuttleVO value-object java merge-point
}