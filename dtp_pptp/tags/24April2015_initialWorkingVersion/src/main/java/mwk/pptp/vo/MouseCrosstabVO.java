// license-header java merge-point
//
/**
 * @author Generated on 07/26/2013 05:52:34-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::PptpModel::mwk.pptp::vo::MouseCrosstabVO
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
 * TODO: Model Documentation for class MouseCrosstabVO
 */
public class MouseCrosstabVO
    implements Serializable, Comparable<MouseCrosstabVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -4389254240650695451L;

    // Class attributes
    /** TODO: Model Documentation for attribute mouse1 */
    protected Double mouse1;
    /** TODO: Model Documentation for attribute mouse2 */
    protected Double mouse2;
    /** TODO: Model Documentation for attribute mouse3 */
    protected Double mouse3;
    /** TODO: Model Documentation for attribute mouse4 */
    protected Double mouse4;
    /** TODO: Model Documentation for attribute mouse5 */
    protected Double mouse5;
    /** TODO: Model Documentation for attribute mouse6 */
    protected Double mouse6;
    /** TODO: Model Documentation for attribute mouse7 */
    protected Double mouse7;
    /** TODO: Model Documentation for attribute mouse8 */
    protected Double mouse8;
    /** TODO: Model Documentation for attribute mouse9 */
    protected Double mouse9;
    /** TODO: Model Documentation for attribute mouse10 */
    protected Double mouse10;

    /**
     * Constructor taking only required properties
     */
    public MouseCrosstabVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param mouse1In Double
     * @param mouse2In Double
     * @param mouse3In Double
     * @param mouse4In Double
     * @param mouse5In Double
     * @param mouse6In Double
     * @param mouse7In Double
     * @param mouse8In Double
     * @param mouse9In Double
     * @param mouse10In Double
     */
    public MouseCrosstabVO(final Double mouse1In, final Double mouse2In, final Double mouse3In, final Double mouse4In, final Double mouse5In, final Double mouse6In, final Double mouse7In, final Double mouse8In, final Double mouse9In, final Double mouse10In)
    {
        this.mouse1 = mouse1In;
        this.mouse2 = mouse2In;
        this.mouse3 = mouse3In;
        this.mouse4 = mouse4In;
        this.mouse5 = mouse5In;
        this.mouse6 = mouse6In;
        this.mouse7 = mouse7In;
        this.mouse8 = mouse8In;
        this.mouse9 = mouse9In;
        this.mouse10 = mouse10In;
    }

    /**
     * Copies constructor from other MouseCrosstabVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public MouseCrosstabVO(final MouseCrosstabVO otherBean)
    {
        this.mouse1 = otherBean.getMouse1();
        this.mouse2 = otherBean.getMouse2();
        this.mouse3 = otherBean.getMouse3();
        this.mouse4 = otherBean.getMouse4();
        this.mouse5 = otherBean.getMouse5();
        this.mouse6 = otherBean.getMouse6();
        this.mouse7 = otherBean.getMouse7();
        this.mouse8 = otherBean.getMouse8();
        this.mouse9 = otherBean.getMouse9();
        this.mouse10 = otherBean.getMouse10();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final MouseCrosstabVO otherBean)
    {
        if (null != otherBean)
        {
            this.setMouse1(otherBean.getMouse1());
            this.setMouse2(otherBean.getMouse2());
            this.setMouse3(otherBean.getMouse3());
            this.setMouse4(otherBean.getMouse4());
            this.setMouse5(otherBean.getMouse5());
            this.setMouse6(otherBean.getMouse6());
            this.setMouse7(otherBean.getMouse7());
            this.setMouse8(otherBean.getMouse8());
            this.setMouse9(otherBean.getMouse9());
            this.setMouse10(otherBean.getMouse10());
        }
    }

    /**
     * TODO: Model Documentation for attribute mouse1
     * Get the mouse1 Attribute
     * @return mouse1 Double
     */
    public Double getMouse1()
    {
        return this.mouse1;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse1(final Double value)
    {
        this.mouse1 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse2
     * Get the mouse2 Attribute
     * @return mouse2 Double
     */
    public Double getMouse2()
    {
        return this.mouse2;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse2(final Double value)
    {
        this.mouse2 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse3
     * Get the mouse3 Attribute
     * @return mouse3 Double
     */
    public Double getMouse3()
    {
        return this.mouse3;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse3(final Double value)
    {
        this.mouse3 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse4
     * Get the mouse4 Attribute
     * @return mouse4 Double
     */
    public Double getMouse4()
    {
        return this.mouse4;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse4(final Double value)
    {
        this.mouse4 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse5
     * Get the mouse5 Attribute
     * @return mouse5 Double
     */
    public Double getMouse5()
    {
        return this.mouse5;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse5(final Double value)
    {
        this.mouse5 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse6
     * Get the mouse6 Attribute
     * @return mouse6 Double
     */
    public Double getMouse6()
    {
        return this.mouse6;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse6(final Double value)
    {
        this.mouse6 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse7
     * Get the mouse7 Attribute
     * @return mouse7 Double
     */
    public Double getMouse7()
    {
        return this.mouse7;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse7(final Double value)
    {
        this.mouse7 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse8
     * Get the mouse8 Attribute
     * @return mouse8 Double
     */
    public Double getMouse8()
    {
        return this.mouse8;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse8(final Double value)
    {
        this.mouse8 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse9
     * Get the mouse9 Attribute
     * @return mouse9 Double
     */
    public Double getMouse9()
    {
        return this.mouse9;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse9(final Double value)
    {
        this.mouse9 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse10
     * Get the mouse10 Attribute
     * @return mouse10 Double
     */
    public Double getMouse10()
    {
        return this.mouse10;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse10(final Double value)
    {
        this.mouse10 = value;
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
        MouseCrosstabVO rhs = (MouseCrosstabVO) object;
        return new EqualsBuilder()
            .append(this.getMouse1(), rhs.getMouse1())
            .append(this.getMouse2(), rhs.getMouse2())
            .append(this.getMouse3(), rhs.getMouse3())
            .append(this.getMouse4(), rhs.getMouse4())
            .append(this.getMouse5(), rhs.getMouse5())
            .append(this.getMouse6(), rhs.getMouse6())
            .append(this.getMouse7(), rhs.getMouse7())
            .append(this.getMouse8(), rhs.getMouse8())
            .append(this.getMouse9(), rhs.getMouse9())
            .append(this.getMouse10(), rhs.getMouse10())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final MouseCrosstabVO object)
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
            .append(this.getMouse1(), object.getMouse1())
            .append(this.getMouse2(), object.getMouse2())
            .append(this.getMouse3(), object.getMouse3())
            .append(this.getMouse4(), object.getMouse4())
            .append(this.getMouse5(), object.getMouse5())
            .append(this.getMouse6(), object.getMouse6())
            .append(this.getMouse7(), object.getMouse7())
            .append(this.getMouse8(), object.getMouse8())
            .append(this.getMouse9(), object.getMouse9())
            .append(this.getMouse10(), object.getMouse10())
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
            .append(this.getMouse1())
            .append(this.getMouse2())
            .append(this.getMouse3())
            .append(this.getMouse4())
            .append(this.getMouse5())
            .append(this.getMouse6())
            .append(this.getMouse7())
            .append(this.getMouse8())
            .append(this.getMouse9())
            .append(this.getMouse10())
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
            .append("mouse1", this.getMouse1())
            .append("mouse2", this.getMouse2())
            .append("mouse3", this.getMouse3())
            .append("mouse4", this.getMouse4())
            .append("mouse5", this.getMouse5())
            .append("mouse6", this.getMouse6())
            .append("mouse7", this.getMouse7())
            .append("mouse8", this.getMouse8())
            .append("mouse9", this.getMouse9())
            .append("mouse10", this.getMouse10())
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

        final MouseCrosstabVO that = (MouseCrosstabVO)thatObject;

        return
            equal(this.getMouse1(), that.getMouse1())
            && equal(this.getMouse2(), that.getMouse2())
            && equal(this.getMouse3(), that.getMouse3())
            && equal(this.getMouse4(), that.getMouse4())
            && equal(this.getMouse5(), that.getMouse5())
            && equal(this.getMouse6(), that.getMouse6())
            && equal(this.getMouse7(), that.getMouse7())
            && equal(this.getMouse8(), that.getMouse8())
            && equal(this.getMouse9(), that.getMouse9())
            && equal(this.getMouse10(), that.getMouse10())
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

    // MouseCrosstabVO value-object java merge-point
}