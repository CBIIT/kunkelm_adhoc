// license-header java merge-point
//
/**
 * @author Generated on 07/26/2013 05:52:35-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::PptpModel::mwk.pptp::vo::BackingListVO
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
 * TODO: Model Documentation for class BackingListVO
 */
public class BackingListVO
    implements Serializable, Comparable<BackingListVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -6046986144767125634L;

    // Class attributes
    /** TODO: Model Documentation for attribute id */
    protected Long id;
    /** TODO: Model Documentation for attribute plainLabel */
    protected String plainLabel;
    /** TODO: Model Documentation for attribute fancyLabel */
    protected String fancyLabel;

    /** Default Constructor with no properties */
    public BackingListVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param plainLabelIn String
     * @param fancyLabelIn String
     */
    public BackingListVO(final String plainLabelIn, final String fancyLabelIn)
    {
        this.plainLabel = plainLabelIn;
        this.fancyLabel = fancyLabelIn;
    }

    /**
     * Constructor with all properties
     * @param idIn Long
     * @param plainLabelIn String
     * @param fancyLabelIn String
     */
    public BackingListVO(final Long idIn, final String plainLabelIn, final String fancyLabelIn)
    {
        this.id = idIn;
        this.plainLabel = plainLabelIn;
        this.fancyLabel = fancyLabelIn;
    }

    /**
     * Copies constructor from other BackingListVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public BackingListVO(final BackingListVO otherBean)
    {
        this.id = otherBean.getId();
        this.plainLabel = otherBean.getPlainLabel();
        this.fancyLabel = otherBean.getFancyLabel();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final BackingListVO otherBean)
    {
        if (null != otherBean)
        {
            this.setId(otherBean.getId());
            this.setPlainLabel(otherBean.getPlainLabel());
            this.setFancyLabel(otherBean.getFancyLabel());
        }
    }

    /**
     * TODO: Model Documentation for attribute id
     * Get the id Attribute
     * @return id Long
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * 
     * @param value Long
     */
    public void setId(final Long value)
    {
        this.id = value;
    }

    /**
     * TODO: Model Documentation for attribute plainLabel
     * Get the plainLabel Attribute
     * @return plainLabel String
     */
    public String getPlainLabel()
    {
        return this.plainLabel;
    }

    /**
     * 
     * @param value String
     */
    public void setPlainLabel(final String value)
    {
        this.plainLabel = value;
    }

    /**
     * TODO: Model Documentation for attribute fancyLabel
     * Get the fancyLabel Attribute
     * @return fancyLabel String
     */
    public String getFancyLabel()
    {
        return this.fancyLabel;
    }

    /**
     * 
     * @param value String
     */
    public void setFancyLabel(final String value)
    {
        this.fancyLabel = value;
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
        BackingListVO rhs = (BackingListVO) object;
        return new EqualsBuilder()
            .append(this.getId(), rhs.getId())
            .append(this.getPlainLabel(), rhs.getPlainLabel())
            .append(this.getFancyLabel(), rhs.getFancyLabel())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final BackingListVO object)
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
            .append(this.getId(), object.getId())
            .append(this.getPlainLabel(), object.getPlainLabel())
            .append(this.getFancyLabel(), object.getFancyLabel())
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
            .append(this.getId())
            .append(this.getPlainLabel())
            .append(this.getFancyLabel())
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
            .append("id", this.getId())
            .append("plainLabel", this.getPlainLabel())
            .append("fancyLabel", this.getFancyLabel())
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

        final BackingListVO that = (BackingListVO)thatObject;

        return
            equal(this.getId(), that.getId())
            && equal(this.getPlainLabel(), that.getPlainLabel())
            && equal(this.getFancyLabel(), that.getFancyLabel())
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

    // BackingListVO value-object java merge-point
}