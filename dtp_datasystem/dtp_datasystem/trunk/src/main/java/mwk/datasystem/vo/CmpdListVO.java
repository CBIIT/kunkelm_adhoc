// license-header java merge-point
//
/**
 * @author Generated on 09/01/2013 19:57:03-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::DCTDDataModel::mwk.dctddata::vo::CmpdListVO
 * STEREOTYPE:   ValueObject
 */
package mwk.datasystem.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * TODO: Model Documentation for class CmpdListVO
 */
public class CmpdListVO
    implements Serializable, Comparable<CmpdListVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 1731314636099904546L;

    // Class attributes
    /** TODO: Model Documentation for attribute listName */
    protected String listName;
    /** TODO: Model Documentation for attribute dateCreated */
    protected Date dateCreated;
    /** TODO: Model Documentation for attribute listOwner */
    protected String listOwner;
    /** TODO: Model Documentation for attribute shareWith */
    protected String shareWith;
    /** TODO: Model Documentation for attribute id */
    protected Long id;
    /** TODO: Model Documentation for attribute cmpdListMembers */
    protected Collection<CmpdListMemberVO> cmpdListMembers;
    /** TODO: Model Documentation for attribute cmpdListId */
    protected Long cmpdListId;
    /** TODO: Model Documentation for attribute countListMembers */
    protected Integer countListMembers;
    /** TODO: Model Documentation for attribute isSelected */
    protected Boolean isSelected;

    /** Default Constructor with no properties */
    public CmpdListVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param listNameIn String
     * @param dateCreatedIn Date
     * @param listOwnerIn String
     * @param shareWithIn String
     */
    public CmpdListVO(final String listNameIn, final Date dateCreatedIn, final String listOwnerIn, final String shareWithIn)
    {
        this.listName = listNameIn;
        this.dateCreated = dateCreatedIn;
        this.listOwner = listOwnerIn;
        this.shareWith = shareWithIn;
    }

    /**
     * Constructor with all properties
     * @param listNameIn String
     * @param dateCreatedIn Date
     * @param listOwnerIn String
     * @param shareWithIn String
     * @param idIn Long
     * @param cmpdListMembersIn Collection<CmpdListMemberVO>
     * @param cmpdListIdIn Long
     * @param countListMembersIn Integer
     * @param isSelectedIn Boolean
     */
    public CmpdListVO(final String listNameIn, final Date dateCreatedIn, final String listOwnerIn, final String shareWithIn, final Long idIn, final Collection<CmpdListMemberVO> cmpdListMembersIn, final Long cmpdListIdIn, final Integer countListMembersIn, final Boolean isSelectedIn)
    {
        this.listName = listNameIn;
        this.dateCreated = dateCreatedIn;
        this.listOwner = listOwnerIn;
        this.shareWith = shareWithIn;
        this.id = idIn;
        this.cmpdListMembers = cmpdListMembersIn;
        this.cmpdListId = cmpdListIdIn;
        this.countListMembers = countListMembersIn;
        this.isSelected = isSelectedIn;
    }

    /**
     * Copies constructor from other CmpdListVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public CmpdListVO(final CmpdListVO otherBean)
    {
        this.listName = otherBean.getListName();
        this.dateCreated = otherBean.getDateCreated();
        this.listOwner = otherBean.getListOwner();
        this.shareWith = otherBean.getShareWith();
        this.id = otherBean.getId();
        this.cmpdListMembers = otherBean.getCmpdListMembers();
        this.cmpdListId = otherBean.getCmpdListId();
        this.countListMembers = otherBean.getCountListMembers();
        this.isSelected = otherBean.getIsSelected();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final CmpdListVO otherBean)
    {
        if (null != otherBean)
        {
            this.setListName(otherBean.getListName());
            this.setDateCreated(otherBean.getDateCreated());
            this.setListOwner(otherBean.getListOwner());
            this.setShareWith(otherBean.getShareWith());
            this.setId(otherBean.getId());
            this.setCmpdListMembers(otherBean.getCmpdListMembers());
            this.setCmpdListId(otherBean.getCmpdListId());
            this.setCountListMembers(otherBean.getCountListMembers());
            this.setIsSelected(otherBean.getIsSelected());
        }
    }

    /**
     * TODO: Model Documentation for attribute listName
     * Get the listName Attribute
     * @return listName String
     */
    public String getListName()
    {
        return this.listName;
    }

    /**
     * 
     * @param value String
     */
    public void setListName(final String value)
    {
        this.listName = value;
    }

    /**
     * TODO: Model Documentation for attribute dateCreated
     * Get the dateCreated Attribute
     * @return dateCreated Date
     */
    public Date getDateCreated()
    {
        return this.dateCreated;
    }

    /**
     * 
     * @param value Date
     */
    public void setDateCreated(final Date value)
    {
        this.dateCreated = value;
    }

    /**
     * TODO: Model Documentation for attribute listOwner
     * Get the listOwner Attribute
     * @return listOwner String
     */
    public String getListOwner()
    {
        return this.listOwner;
    }

    /**
     * 
     * @param value String
     */
    public void setListOwner(final String value)
    {
        this.listOwner = value;
    }

    /**
     * TODO: Model Documentation for attribute shareWith
     * Get the shareWith Attribute
     * @return shareWith String
     */
    public String getShareWith()
    {
        return this.shareWith;
    }

    /**
     * 
     * @param value String
     */
    public void setShareWith(final String value)
    {
        this.shareWith = value;
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
     * TODO: Model Documentation for attribute cmpdListMembers
     * Get the cmpdListMembers Attribute
     * @return cmpdListMembers Collection<CmpdListMemberVO>
     */
    public Collection<CmpdListMemberVO> getCmpdListMembers()
    {
        if (this.cmpdListMembers == null)
        {
            this.cmpdListMembers = new ArrayList<CmpdListMemberVO>();
        }
        return this.cmpdListMembers;
    }

    /**
     * 
     * @param value Collection<CmpdListMemberVO>
     */
    public void setCmpdListMembers(final Collection<CmpdListMemberVO> value)
    {
        this.cmpdListMembers = value;
    }

    /**
     * TODO: Model Documentation for attribute cmpdListId
     * Get the cmpdListId Attribute
     * @return cmpdListId Long
     */
    public Long getCmpdListId()
    {
        return this.cmpdListId;
    }

    /**
     * 
     * @param value Long
     */
    public void setCmpdListId(final Long value)
    {
        this.cmpdListId = value;
    }

    /**
     * TODO: Model Documentation for attribute countListMembers
     * Get the countListMembers Attribute
     * @return countListMembers Integer
     */
    public Integer getCountListMembers()
    {
        return this.countListMembers;
    }

    /**
     * 
     * @param value Integer
     */
    public void setCountListMembers(final Integer value)
    {
        this.countListMembers = value;
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
        CmpdListVO rhs = (CmpdListVO) object;
        return new EqualsBuilder()
            .append(this.getListName(), rhs.getListName())
            .append(this.getDateCreated(), rhs.getDateCreated())
            .append(this.getListOwner(), rhs.getListOwner())
            .append(this.getShareWith(), rhs.getShareWith())
            .append(this.getId(), rhs.getId())
            .append(this.getCmpdListMembers(), rhs.getCmpdListMembers())
            .append(this.getCmpdListId(), rhs.getCmpdListId())
            .append(this.getCountListMembers(), rhs.getCountListMembers())
            .append(this.getIsSelected(), rhs.getIsSelected())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final CmpdListVO object)
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
            .append(this.getListName(), object.getListName())
            .append(this.getDateCreated(), object.getDateCreated())
            .append(this.getListOwner(), object.getListOwner())
            .append(this.getShareWith(), object.getShareWith())
            .append(this.getId(), object.getId())
            .append(this.getCmpdListMembers(), object.getCmpdListMembers())
            .append(this.getCmpdListId(), object.getCmpdListId())
            .append(this.getCountListMembers(), object.getCountListMembers())
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
            .append(this.getListName())
            .append(this.getDateCreated())
            .append(this.getListOwner())
            .append(this.getShareWith())
            .append(this.getId())
            .append(this.getCmpdListMembers())
            .append(this.getCmpdListId())
            .append(this.getCountListMembers())
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
            .append("listName", this.getListName())
            .append("dateCreated", this.getDateCreated())
            .append("listOwner", this.getListOwner())
            .append("shareWith", this.getShareWith())
            .append("id", this.getId())
            .append("cmpdListMembers", this.getCmpdListMembers())
            .append("cmpdListId", this.getCmpdListId())
            .append("countListMembers", this.getCountListMembers())
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

        final CmpdListVO that = (CmpdListVO)thatObject;

        return
            equal(this.getListName(), that.getListName())
            && equal(this.getDateCreated(), that.getDateCreated())
            && equal(this.getListOwner(), that.getListOwner())
            && equal(this.getShareWith(), that.getShareWith())
            && equal(this.getId(), that.getId())
            && equal(this.getCmpdListMembers(), that.getCmpdListMembers())
            && equal(this.getCmpdListId(), that.getCmpdListId())
            && equal(this.getCountListMembers(), that.getCountListMembers())
            && equal(this.getIsSelected(), that.getIsSelected())
        ;
    }

    /**
     * This is a convenient helper method which is able to detect whether or not two values are equal. Two values
     * are equal when they are both {@code null}, are arrays of the same length with equal elements or are
     * equal objects (this includes {@link Collection} and {@link java.util.Map} instances).
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
        else // note that the following also covers Collection and java.util.Map
        {
            equal = first.equals(second);
        }

        return equal;
    }

    // CmpdListVO value-object java merge-point
}