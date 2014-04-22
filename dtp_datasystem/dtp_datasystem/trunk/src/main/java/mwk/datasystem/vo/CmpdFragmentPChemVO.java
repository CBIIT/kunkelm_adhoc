// license-header java merge-point
//
/**
 * @author Generated on 04/15/2014 17:22:57-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::DataSystemModel::mwk.datasystem::vo::CmpdFragmentPChemVO
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
 * TODO: Model Documentation for class CmpdFragmentPChemVO
 */
public class CmpdFragmentPChemVO
    implements Serializable, Comparable<CmpdFragmentPChemVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -471580505164812697L;

    // Class attributes
    /** TODO: Model Documentation for attribute mw */
    protected Double mw;
    /** TODO: Model Documentation for attribute mf */
    protected String mf;
    /** TODO: Model Documentation for attribute alogp */
    protected Double alogp;
    /** TODO: Model Documentation for attribute logd */
    protected Double logd;
    /** TODO: Model Documentation for attribute hba */
    protected Integer hba;
    /** TODO: Model Documentation for attribute hbd */
    protected Integer hbd;
    /** TODO: Model Documentation for attribute sa */
    protected Double sa;
    /** TODO: Model Documentation for attribute psa */
    protected Double psa;
    /** TODO: Model Documentation for attribute id */
    protected Long id;

    /**
     * Constructor taking only required properties
     */
    public CmpdFragmentPChemVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param mwIn Double
     * @param mfIn String
     * @param alogpIn Double
     * @param logdIn Double
     * @param hbaIn Integer
     * @param hbdIn Integer
     * @param saIn Double
     * @param psaIn Double
     * @param idIn Long
     */
    public CmpdFragmentPChemVO(final Double mwIn, final String mfIn, final Double alogpIn, final Double logdIn, final Integer hbaIn, final Integer hbdIn, final Double saIn, final Double psaIn, final Long idIn)
    {
        this.mw = mwIn;
        this.mf = mfIn;
        this.alogp = alogpIn;
        this.logd = logdIn;
        this.hba = hbaIn;
        this.hbd = hbdIn;
        this.sa = saIn;
        this.psa = psaIn;
        this.id = idIn;
    }

    /**
     * Copies constructor from other CmpdFragmentPChemVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public CmpdFragmentPChemVO(final CmpdFragmentPChemVO otherBean)
    {
        this.mw = otherBean.getMw();
        this.mf = otherBean.getMf();
        this.alogp = otherBean.getAlogp();
        this.logd = otherBean.getLogd();
        this.hba = otherBean.getHba();
        this.hbd = otherBean.getHbd();
        this.sa = otherBean.getSa();
        this.psa = otherBean.getPsa();
        this.id = otherBean.getId();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final CmpdFragmentPChemVO otherBean)
    {
        if (null != otherBean)
        {
            this.setMw(otherBean.getMw());
            this.setMf(otherBean.getMf());
            this.setAlogp(otherBean.getAlogp());
            this.setLogd(otherBean.getLogd());
            this.setHba(otherBean.getHba());
            this.setHbd(otherBean.getHbd());
            this.setSa(otherBean.getSa());
            this.setPsa(otherBean.getPsa());
            this.setId(otherBean.getId());
        }
    }

    /**
     * TODO: Model Documentation for attribute mw
     * Get the mw Attribute
     * @return mw Double
     */
    public Double getMw()
    {
        return this.mw;
    }

    /**
     * 
     * @param value Double
     */
    public void setMw(final Double value)
    {
        this.mw = value;
    }

    /**
     * TODO: Model Documentation for attribute mf
     * Get the mf Attribute
     * @return mf String
     */
    public String getMf()
    {
        return this.mf;
    }

    /**
     * 
     * @param value String
     */
    public void setMf(final String value)
    {
        this.mf = value;
    }

    /**
     * TODO: Model Documentation for attribute alogp
     * Get the alogp Attribute
     * @return alogp Double
     */
    public Double getAlogp()
    {
        return this.alogp;
    }

    /**
     * 
     * @param value Double
     */
    public void setAlogp(final Double value)
    {
        this.alogp = value;
    }

    /**
     * TODO: Model Documentation for attribute logd
     * Get the logd Attribute
     * @return logd Double
     */
    public Double getLogd()
    {
        return this.logd;
    }

    /**
     * 
     * @param value Double
     */
    public void setLogd(final Double value)
    {
        this.logd = value;
    }

    /**
     * TODO: Model Documentation for attribute hba
     * Get the hba Attribute
     * @return hba Integer
     */
    public Integer getHba()
    {
        return this.hba;
    }

    /**
     * 
     * @param value Integer
     */
    public void setHba(final Integer value)
    {
        this.hba = value;
    }

    /**
     * TODO: Model Documentation for attribute hbd
     * Get the hbd Attribute
     * @return hbd Integer
     */
    public Integer getHbd()
    {
        return this.hbd;
    }

    /**
     * 
     * @param value Integer
     */
    public void setHbd(final Integer value)
    {
        this.hbd = value;
    }

    /**
     * TODO: Model Documentation for attribute sa
     * Get the sa Attribute
     * @return sa Double
     */
    public Double getSa()
    {
        return this.sa;
    }

    /**
     * 
     * @param value Double
     */
    public void setSa(final Double value)
    {
        this.sa = value;
    }

    /**
     * TODO: Model Documentation for attribute psa
     * Get the psa Attribute
     * @return psa Double
     */
    public Double getPsa()
    {
        return this.psa;
    }

    /**
     * 
     * @param value Double
     */
    public void setPsa(final Double value)
    {
        this.psa = value;
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
        CmpdFragmentPChemVO rhs = (CmpdFragmentPChemVO) object;
        return new EqualsBuilder()
            .append(this.getMw(), rhs.getMw())
            .append(this.getMf(), rhs.getMf())
            .append(this.getAlogp(), rhs.getAlogp())
            .append(this.getLogd(), rhs.getLogd())
            .append(this.getHba(), rhs.getHba())
            .append(this.getHbd(), rhs.getHbd())
            .append(this.getSa(), rhs.getSa())
            .append(this.getPsa(), rhs.getPsa())
            .append(this.getId(), rhs.getId())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final CmpdFragmentPChemVO object)
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
            .append(this.getMw(), object.getMw())
            .append(this.getMf(), object.getMf())
            .append(this.getAlogp(), object.getAlogp())
            .append(this.getLogd(), object.getLogd())
            .append(this.getHba(), object.getHba())
            .append(this.getHbd(), object.getHbd())
            .append(this.getSa(), object.getSa())
            .append(this.getPsa(), object.getPsa())
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
            .append(this.getMw())
            .append(this.getMf())
            .append(this.getAlogp())
            .append(this.getLogd())
            .append(this.getHba())
            .append(this.getHbd())
            .append(this.getSa())
            .append(this.getPsa())
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
            .append("mw", this.getMw())
            .append("mf", this.getMf())
            .append("alogp", this.getAlogp())
            .append("logd", this.getLogd())
            .append("hba", this.getHba())
            .append("hbd", this.getHbd())
            .append("sa", this.getSa())
            .append("psa", this.getPsa())
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

        final CmpdFragmentPChemVO that = (CmpdFragmentPChemVO)thatObject;

        return
            equal(this.getMw(), that.getMw())
            && equal(this.getMf(), that.getMf())
            && equal(this.getAlogp(), that.getAlogp())
            && equal(this.getLogd(), that.getLogd())
            && equal(this.getHba(), that.getHba())
            && equal(this.getHbd(), that.getHbd())
            && equal(this.getSa(), that.getSa())
            && equal(this.getPsa(), that.getPsa())
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

    // CmpdFragmentPChemVO value-object java merge-point
}