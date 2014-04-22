// license-header java merge-point
//
/**
 * @author Generated on 04/15/2014 17:22:57-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::DataSystemModel::mwk.datasystem::vo::RelatedCompoundVO
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
 * TODO: Model Documentation for class RelatedCompoundVO
 */
public class RelatedCompoundVO
    implements Serializable, Comparable<RelatedCompoundVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 1280560525687224687L;

    // Class attributes
    /** TODO: Model Documentation for attribute nsc */
    protected Integer nsc;
    /** TODO: Model Documentation for attribute relatedNsc */
    protected Integer relatedNsc;
    /** TODO: Model Documentation for attribute relation */
    protected String relation;
    /** TODO: Model Documentation for attribute id */
    protected Long id;

    /** Default Constructor with no properties */
    public RelatedCompoundVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param relationIn String
     */
    public RelatedCompoundVO(final String relationIn)
    {
        this.relation = relationIn;
    }

    /**
     * Constructor with all properties
     * @param nscIn Integer
     * @param relatedNscIn Integer
     * @param relationIn String
     * @param idIn Long
     */
    public RelatedCompoundVO(final Integer nscIn, final Integer relatedNscIn, final String relationIn, final Long idIn)
    {
        this.nsc = nscIn;
        this.relatedNsc = relatedNscIn;
        this.relation = relationIn;
        this.id = idIn;
    }

    /**
     * Copies constructor from other RelatedCompoundVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public RelatedCompoundVO(final RelatedCompoundVO otherBean)
    {
        this.nsc = otherBean.getNsc();
        this.relatedNsc = otherBean.getRelatedNsc();
        this.relation = otherBean.getRelation();
        this.id = otherBean.getId();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final RelatedCompoundVO otherBean)
    {
        if (null != otherBean)
        {
            this.setNsc(otherBean.getNsc());
            this.setRelatedNsc(otherBean.getRelatedNsc());
            this.setRelation(otherBean.getRelation());
            this.setId(otherBean.getId());
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
     * TODO: Model Documentation for attribute relatedNsc
     * Get the relatedNsc Attribute
     * @return relatedNsc Integer
     */
    public Integer getRelatedNsc()
    {
        return this.relatedNsc;
    }

    /**
     * 
     * @param value Integer
     */
    public void setRelatedNsc(final Integer value)
    {
        this.relatedNsc = value;
    }

    /**
     * TODO: Model Documentation for attribute relation
     * Get the relation Attribute
     * @return relation String
     */
    public String getRelation()
    {
        return this.relation;
    }

    /**
     * 
     * @param value String
     */
    public void setRelation(final String value)
    {
        this.relation = value;
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
        RelatedCompoundVO rhs = (RelatedCompoundVO) object;
        return new EqualsBuilder()
            .append(this.getNsc(), rhs.getNsc())
            .append(this.getRelatedNsc(), rhs.getRelatedNsc())
            .append(this.getRelation(), rhs.getRelation())
            .append(this.getId(), rhs.getId())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final RelatedCompoundVO object)
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
            .append(this.getRelatedNsc(), object.getRelatedNsc())
            .append(this.getRelation(), object.getRelation())
            .append(this.getId(), object.getId())
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
            .append(this.getRelatedNsc())
            .append(this.getRelation())
            .append(this.getId())
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
            .append("relatedNsc", this.getRelatedNsc())
            .append("relation", this.getRelation())
            .append("id", this.getId())
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

        final RelatedCompoundVO that = (RelatedCompoundVO)thatObject;

        return
            equal(this.getNsc(), that.getNsc())
            && equal(this.getRelatedNsc(), that.getRelatedNsc())
            && equal(this.getRelation(), that.getRelation())
            && equal(this.getId(), that.getId())
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

    // RelatedCompoundVO value-object java merge-point
}