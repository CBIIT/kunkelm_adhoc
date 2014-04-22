// license-header java merge-point
//
/**
 * @author Generated on 04/15/2014 17:22:57-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::DataSystemModel::mwk.datasystem::vo::CmpdListMemberVO
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
 * TODO: Model Documentation for class CmpdListMemberVO
 */
public class CmpdListMemberVO
    implements Serializable, Comparable<CmpdListMemberVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 6964840337709156868L;

    // Class attributes
    /** TODO: Model Documentation for attribute cmpd */
    protected CmpdVO cmpd;
    /** TODO: Model Documentation for attribute id */
    protected Long id;
    /** TODO: Model Documentation for attribute isSelected */
    protected Boolean isSelected;

    /** Default Constructor with no properties */
    public CmpdListMemberVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param cmpdIn CmpdVO
     */
    public CmpdListMemberVO(final CmpdVO cmpdIn)
    {
        this.cmpd = cmpdIn;
    }

    /**
     * Constructor with all properties
     * @param cmpdIn CmpdVO
     * @param idIn Long
     * @param isSelectedIn Boolean
     */
    public CmpdListMemberVO(final CmpdVO cmpdIn, final Long idIn, final Boolean isSelectedIn)
    {
        this.cmpd = cmpdIn;
        this.id = idIn;
        this.isSelected = isSelectedIn;
    }

    /**
     * Copies constructor from other CmpdListMemberVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public CmpdListMemberVO(final CmpdListMemberVO otherBean)
    {
        this.cmpd = otherBean.getCmpd();
        this.id = otherBean.getId();
        this.isSelected = otherBean.getIsSelected();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final CmpdListMemberVO otherBean)
    {
        if (null != otherBean)
        {
            this.setCmpd(otherBean.getCmpd());
            this.setId(otherBean.getId());
            this.setIsSelected(otherBean.getIsSelected());
        }
    }

    /**
     * TODO: Model Documentation for attribute cmpd
     * Get the cmpd Attribute
     * @return cmpd CmpdVO
     */
    public CmpdVO getCmpd()
    {
        return this.cmpd;
    }

    /**
     * 
     * @param value CmpdVO
     */
    public void setCmpd(final CmpdVO value)
    {
        this.cmpd = value;
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
     * TODO: Model Documentation for attribute isSelected
     * Get the isSelected Attribute
     * @return isSelected Boolean
     */
    public Boolean getIsSelected()
    {
        return this.isSelected;
    }

    /**
     * 
     * Duplicates getBoolean method, for use as Jaxb2 compatible object
     * Get the isSelected Attribute
     * @return isSelected Boolean
     */
    @Deprecated
    public Boolean isIsSelected()
    {
        return this.isSelected;
    }

    /**
     * 
     * @param value Boolean
     */
    public void setIsSelected(final Boolean value)
    {
        this.isSelected = value;
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
        CmpdListMemberVO rhs = (CmpdListMemberVO) object;
        return new EqualsBuilder()
            .append(this.getCmpd(), rhs.getCmpd())
            .append(this.getId(), rhs.getId())
            .append(this.getIsSelected(), rhs.getIsSelected())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final CmpdListMemberVO object)
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
            .append(this.getCmpd(), object.getCmpd())
            .append(this.getId(), object.getId())
            .append(this.getIsSelected(), object.getIsSelected())
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
            .append(this.getCmpd())
            .append(this.getId())
            .append(this.getIsSelected())
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
            .append("cmpd", this.getCmpd())
            .append("id", this.getId())
            .append("isSelected", this.getIsSelected())
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

        final CmpdListMemberVO that = (CmpdListMemberVO)thatObject;

        return
            equal(this.getCmpd(), that.getCmpd())
            && equal(this.getId(), that.getId())
            && equal(this.getIsSelected(), that.getIsSelected())
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

    // CmpdListMemberVO value-object java merge-point
}