// license-header java merge-point
//
/**
 * @author Generated on 07/27/2013 08:57:19-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::PptpModel::mwk.pptp::vo::MouseGroupWithMousesVO
 * STEREOTYPE:   ValueObject
 */
package mwk.pptp.vo;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * TODO: Model Documentation for class MouseGroupWithMousesVO
 */
public class MouseGroupWithMousesVO
    extends MouseGroupVO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -4123072563660901451L;

    // Class attributes
    /** TODO: Model Documentation for attribute mouses */
    protected Collection<MouseVO> mouses;

    /** Default Constructor with no properties */
    public MouseGroupWithMousesVO()
    {
        super();
    }

    /**
     * Constructor taking only required properties
     * @param groupRoleIn String
     */
    public MouseGroupWithMousesVO(final String groupRoleIn)
    {
        super(groupRoleIn);
    }

    /**
     * Constructor with all properties
     * @param dayIn Integer
     * @param groupRoleIn String
     * @param mousesIn Collection<MouseVO>
     */
    public MouseGroupWithMousesVO(final Integer dayIn, final String groupRoleIn, final Collection<MouseVO> mousesIn)
    {
        super(dayIn, groupRoleIn);
        this.mouses = mousesIn;
    }

    /**
     * Copies constructor from other MouseGroupWithMousesVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public MouseGroupWithMousesVO(final MouseGroupWithMousesVO otherBean)
    {
        super(otherBean);
        this.mouses = otherBean.getMouses();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final MouseGroupWithMousesVO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setMouses(otherBean.getMouses());
        }
    }

    /**
     * TODO: Model Documentation for attribute mouses
     * Get the mouses Attribute
     * @return mouses Collection<MouseVO>
     */
    public Collection<MouseVO> getMouses()
    {
        if (this.mouses == null)
        {
            this.mouses = new ArrayList<MouseVO>();
        }
        return this.mouses;
    }

    /**
     * 
     * @param value Collection<MouseVO>
     */
    public void setMouses(final Collection<MouseVO> value)
    {
        this.mouses = value;
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
        MouseGroupWithMousesVO rhs = (MouseGroupWithMousesVO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getMouses(), rhs.getMouses())
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
        if (!(object instanceof MouseGroupWithMousesVO))
        {
            return -1;
        }
        MouseGroupWithMousesVO myClass = (MouseGroupWithMousesVO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getMouses(), myClass.getMouses())
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
            .append(this.getMouses())
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
            .append("mouses", this.getMouses())
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

        final MouseGroupWithMousesVO that = (MouseGroupWithMousesVO)thatObject;

        return super.equalProperties(that)
            && equal(this.getMouses(), that.getMouses())
        ;
    }

    // MouseGroupWithMousesVO value-object java merge-point
}