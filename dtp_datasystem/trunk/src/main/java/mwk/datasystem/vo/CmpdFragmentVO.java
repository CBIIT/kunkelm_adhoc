// license-header java merge-point
//
/**
 * @author Generated on 09/01/2013 19:57:04-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::DCTDDataModel::mwk.dctddata::vo::CmpdFragmentVO
 * STEREOTYPE:   ValueObject
 */
package mwk.datasystem.vo;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * TODO: Model Documentation for class CmpdFragmentVO
 */
public class CmpdFragmentVO
    implements Serializable, Comparable<CmpdFragmentVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -3631833082557776743L;

    // Class attributes
    /** TODO: Model Documentation for attribute id */
    protected Long id;
    /** TODO: Model Documentation for attribute cmpdFragmentStructure */
    protected CmpdFragmentStructureVO cmpdFragmentStructure;
    /** TODO: Model Documentation for attribute cmpdFragmentPChem */
    protected CmpdFragmentPChemVO cmpdFragmentPChem;

    /** Default Constructor with no properties */
    public CmpdFragmentVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param cmpdFragmentStructureIn CmpdFragmentStructureVO
     * @param cmpdFragmentPChemIn CmpdFragmentPChemVO
     */
    public CmpdFragmentVO(final CmpdFragmentStructureVO cmpdFragmentStructureIn, final CmpdFragmentPChemVO cmpdFragmentPChemIn)
    {
        this.cmpdFragmentStructure = cmpdFragmentStructureIn;
        this.cmpdFragmentPChem = cmpdFragmentPChemIn;
    }

    /**
     * Constructor with all properties
     * @param idIn Long
     * @param cmpdFragmentStructureIn CmpdFragmentStructureVO
     * @param cmpdFragmentPChemIn CmpdFragmentPChemVO
     */
    public CmpdFragmentVO(final Long idIn, final CmpdFragmentStructureVO cmpdFragmentStructureIn, final CmpdFragmentPChemVO cmpdFragmentPChemIn)
    {
        this.id = idIn;
        this.cmpdFragmentStructure = cmpdFragmentStructureIn;
        this.cmpdFragmentPChem = cmpdFragmentPChemIn;
    }

    /**
     * Copies constructor from other CmpdFragmentVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public CmpdFragmentVO(final CmpdFragmentVO otherBean)
    {
        this.id = otherBean.getId();
        this.cmpdFragmentStructure = otherBean.getCmpdFragmentStructure();
        this.cmpdFragmentPChem = otherBean.getCmpdFragmentPChem();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final CmpdFragmentVO otherBean)
    {
        if (null != otherBean)
        {
            this.setId(otherBean.getId());
            this.setCmpdFragmentStructure(otherBean.getCmpdFragmentStructure());
            this.setCmpdFragmentPChem(otherBean.getCmpdFragmentPChem());
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
     * TODO: Model Documentation for attribute cmpdFragmentStructure
     * Get the cmpdFragmentStructure Attribute
     * @return cmpdFragmentStructure CmpdFragmentStructureVO
     */
    public CmpdFragmentStructureVO getCmpdFragmentStructure()
    {
        return this.cmpdFragmentStructure;
    }

    /**
     * 
     * @param value CmpdFragmentStructureVO
     */
    public void setCmpdFragmentStructure(final CmpdFragmentStructureVO value)
    {
        this.cmpdFragmentStructure = value;
    }

    /**
     * TODO: Model Documentation for attribute cmpdFragmentPChem
     * Get the cmpdFragmentPChem Attribute
     * @return cmpdFragmentPChem CmpdFragmentPChemVO
     */
    public CmpdFragmentPChemVO getCmpdFragmentPChem()
    {
        return this.cmpdFragmentPChem;
    }

    /**
     * 
     * @param value CmpdFragmentPChemVO
     */
    public void setCmpdFragmentPChem(final CmpdFragmentPChemVO value)
    {
        this.cmpdFragmentPChem = value;
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
        CmpdFragmentVO rhs = (CmpdFragmentVO) object;
        return new EqualsBuilder()
            .append(this.getId(), rhs.getId())
            .append(this.getCmpdFragmentStructure(), rhs.getCmpdFragmentStructure())
            .append(this.getCmpdFragmentPChem(), rhs.getCmpdFragmentPChem())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final CmpdFragmentVO object)
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
            .append(this.getCmpdFragmentStructure(), object.getCmpdFragmentStructure())
            .append(this.getCmpdFragmentPChem(), object.getCmpdFragmentPChem())
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
            .append(this.getCmpdFragmentStructure())
            .append(this.getCmpdFragmentPChem())
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
            .append("cmpdFragmentStructure", this.getCmpdFragmentStructure())
            .append("cmpdFragmentPChem", this.getCmpdFragmentPChem())
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

        final CmpdFragmentVO that = (CmpdFragmentVO)thatObject;

        return
            equal(this.getId(), that.getId())
            && equal(this.getCmpdFragmentStructure(), that.getCmpdFragmentStructure())
            && equal(this.getCmpdFragmentPChem(), that.getCmpdFragmentPChem())
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

    // CmpdFragmentVO value-object java merge-point
}