// license-header java merge-point
//
/**
 * @author Generated on 04/27/2013 15:22:29-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::CompareModel::mwk.compare::vo::StructureVO
 * STEREOTYPE:   ValueObject
 */
package mwk.structureservlet.vo;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO: Model Documentation for class StructureVO
 */
public class StructureVO
    implements Serializable, Comparable<StructureVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 2448396915338035575L;

    // Class attributes
    /** TODO: Model Documentation for attribute nsc */
    protected Integer nsc;
    /** TODO: Model Documentation for attribute smiles */
    protected String smiles;

    /** Default Constructor with no properties */
    public StructureVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param smilesIn String
     */
    public StructureVO(final String smilesIn)
    {
        this.smiles = smilesIn;
    }

    /**
     * Constructor with all properties
     * @param nscIn Integer
     * @param smilesIn String
     */
    public StructureVO(final Integer nscIn, final String smilesIn)
    {
        this.nsc = nscIn;
        this.smiles = smilesIn;
    }

    /**
     * Copies constructor from other StructureVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public StructureVO(final StructureVO otherBean)
    {
        this.nsc = otherBean.getNsc();
        this.smiles = otherBean.getSmiles();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final StructureVO otherBean)
    {
        if (null != otherBean)
        {
            this.setNsc(otherBean.getNsc());
            this.setSmiles(otherBean.getSmiles());
        }
    }

    /**
     * TODO: Model Documentation for attribute nsc
     * Get the nsc Attribute
     * @return nsc Integer
     */
    public Integer getNsc()
    {
        return this.nsc;
    }

    /**
     * 
     * @param value Integer
     */
    public void setNsc(final Integer value)
    {
        this.nsc = value;
    }

    /**
     * TODO: Model Documentation for attribute smiles
     * Get the smiles Attribute
     * @return smiles String
     */
    public String getSmiles()
    {
        return this.smiles;
    }

    /**
     * 
     * @param value String
     */
    public void setSmiles(final String value)
    {
        this.smiles = value;
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
        StructureVO rhs = (StructureVO) object;
        return new EqualsBuilder()
            .append(this.getNsc(), rhs.getNsc())
            .append(this.getSmiles(), rhs.getSmiles())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final StructureVO object)
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
            .append(this.getNsc(), object.getNsc())
            .append(this.getSmiles(), object.getSmiles())
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
            .append(this.getNsc())
            .append(this.getSmiles())
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
            .append("nsc", this.getNsc())
            .append("smiles", this.getSmiles())
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

        final StructureVO that = (StructureVO)thatObject;

        return
            equal(this.getNsc(), that.getNsc())
            && equal(this.getSmiles(), that.getSmiles())
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

    // StructureVO value-object java merge-point
}