// license-header java merge-point
//
/**
 * @author Generated on 07/27/2013 08:57:19-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::PptpModel::mwk.pptp::vo::MouseRTVShuttleVO
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
 * TODO: Model Documentation for class MouseRTVShuttleVO
 */
public class MouseRTVShuttleVO
    implements Serializable, Comparable<MouseRTVShuttleVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -4331989509396252297L;

    // Class attributes
    /** TODO: Model Documentation for attribute groupRole */
    protected String groupRole;
    /** TODO: Model Documentation for attribute day */
    protected Integer day;
    /** TODO: Model Documentation for attribute rtv */
    protected Double rtv;

    /** Default Constructor with no properties */
    public MouseRTVShuttleVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param groupRoleIn String
     */
    public MouseRTVShuttleVO(final String groupRoleIn)
    {
        this.groupRole = groupRoleIn;
    }

    /**
     * Constructor with all properties
     * @param groupRoleIn String
     * @param dayIn Integer
     * @param rtvIn Double
     */
    public MouseRTVShuttleVO(final String groupRoleIn, final Integer dayIn, final Double rtvIn)
    {
        this.groupRole = groupRoleIn;
        this.day = dayIn;
        this.rtv = rtvIn;
    }

    /**
     * Copies constructor from other MouseRTVShuttleVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public MouseRTVShuttleVO(final MouseRTVShuttleVO otherBean)
    {
        this.groupRole = otherBean.getGroupRole();
        this.day = otherBean.getDay();
        this.rtv = otherBean.getRtv();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final MouseRTVShuttleVO otherBean)
    {
        if (null != otherBean)
        {
            this.setGroupRole(otherBean.getGroupRole());
            this.setDay(otherBean.getDay());
            this.setRtv(otherBean.getRtv());
        }
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
     * TODO: Model Documentation for attribute rtv
     * Get the rtv Attribute
     * @return rtv Double
     */
    public Double getRtv()
    {
        return this.rtv;
    }

    /**
     * 
     * @param value Double
     */
    public void setRtv(final Double value)
    {
        this.rtv = value;
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
        MouseRTVShuttleVO rhs = (MouseRTVShuttleVO) object;
        return new EqualsBuilder()
            .append(this.getGroupRole(), rhs.getGroupRole())
            .append(this.getDay(), rhs.getDay())
            .append(this.getRtv(), rhs.getRtv())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final MouseRTVShuttleVO object)
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
            .append(this.getGroupRole(), object.getGroupRole())
            .append(this.getDay(), object.getDay())
            .append(this.getRtv(), object.getRtv())
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
            .append(this.getGroupRole())
            .append(this.getDay())
            .append(this.getRtv())
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
            .append("groupRole", this.getGroupRole())
            .append("day", this.getDay())
            .append("rtv", this.getRtv())
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

        final MouseRTVShuttleVO that = (MouseRTVShuttleVO)thatObject;

        return
            equal(this.getGroupRole(), that.getGroupRole())
            && equal(this.getDay(), that.getDay())
            && equal(this.getRtv(), that.getRtv())
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

    // MouseRTVShuttleVO value-object java merge-point
}