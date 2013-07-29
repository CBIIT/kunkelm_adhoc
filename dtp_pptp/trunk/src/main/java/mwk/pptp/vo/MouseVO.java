// license-header java merge-point
//
/**
 * @author Generated on 07/26/2013 05:52:34-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::PptpModel::mwk.pptp::vo::MouseVO
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
 * TODO: Model Documentation for class MouseVO
 */
public class MouseVO
    implements Serializable, Comparable<MouseVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -20838210345652401L;

    // Class attributes
    /** TODO: Model Documentation for attribute mouseNumber */
    protected Integer mouseNumber;
    /** TODO: Model Documentation for attribute tumorVolume */
    protected Double tumorVolume;
    /** TODO: Model Documentation for attribute logTumorVolume */
    protected Double logTumorVolume;

    /**
     * Constructor taking only required properties
     */
    public MouseVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param mouseNumberIn Integer
     * @param tumorVolumeIn Double
     * @param logTumorVolumeIn Double
     */
    public MouseVO(final Integer mouseNumberIn, final Double tumorVolumeIn, final Double logTumorVolumeIn)
    {
        this.mouseNumber = mouseNumberIn;
        this.tumorVolume = tumorVolumeIn;
        this.logTumorVolume = logTumorVolumeIn;
    }

    /**
     * Copies constructor from other MouseVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public MouseVO(final MouseVO otherBean)
    {
        this.mouseNumber = otherBean.getMouseNumber();
        this.tumorVolume = otherBean.getTumorVolume();
        this.logTumorVolume = otherBean.getLogTumorVolume();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final MouseVO otherBean)
    {
        if (null != otherBean)
        {
            this.setMouseNumber(otherBean.getMouseNumber());
            this.setTumorVolume(otherBean.getTumorVolume());
            this.setLogTumorVolume(otherBean.getLogTumorVolume());
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
     * TODO: Model Documentation for attribute tumorVolume
     * Get the tumorVolume Attribute
     * @return tumorVolume Double
     */
    public Double getTumorVolume()
    {
        return this.tumorVolume;
    }

    /**
     * 
     * @param value Double
     */
    public void setTumorVolume(final Double value)
    {
        this.tumorVolume = value;
    }

    /**
     * TODO: Model Documentation for attribute logTumorVolume
     * Get the logTumorVolume Attribute
     * @return logTumorVolume Double
     */
    public Double getLogTumorVolume()
    {
        return this.logTumorVolume;
    }

    /**
     * 
     * @param value Double
     */
    public void setLogTumorVolume(final Double value)
    {
        this.logTumorVolume = value;
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
        MouseVO rhs = (MouseVO) object;
        return new EqualsBuilder()
            .append(this.getMouseNumber(), rhs.getMouseNumber())
            .append(this.getTumorVolume(), rhs.getTumorVolume())
            .append(this.getLogTumorVolume(), rhs.getLogTumorVolume())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final MouseVO object)
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
            .append(this.getTumorVolume(), object.getTumorVolume())
            .append(this.getLogTumorVolume(), object.getLogTumorVolume())
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
            .append(this.getTumorVolume())
            .append(this.getLogTumorVolume())
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
            .append("tumorVolume", this.getTumorVolume())
            .append("logTumorVolume", this.getLogTumorVolume())
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

        final MouseVO that = (MouseVO)thatObject;

        return
            equal(this.getMouseNumber(), that.getMouseNumber())
            && equal(this.getTumorVolume(), that.getTumorVolume())
            && equal(this.getLogTumorVolume(), that.getLogTumorVolume())
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

    // MouseVO value-object java merge-point
}