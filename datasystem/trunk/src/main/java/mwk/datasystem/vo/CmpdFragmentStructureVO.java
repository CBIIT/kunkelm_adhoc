// license-header java merge-point
//
/**
 * @author Generated on 09/01/2013 19:57:04-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::DCTDDataModel::mwk.dctddata::vo::CmpdFragmentStructureVO
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
 * TODO: Model Documentation for class CmpdFragmentStructureVO
 */
public class CmpdFragmentStructureVO
    implements Serializable, Comparable<CmpdFragmentStructureVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 4921683347718066877L;

    // Class attributes
    /** TODO: Model Documentation for attribute smiles */
    protected String smiles;
    /** TODO: Model Documentation for attribute inchi */
    protected String inchi;
    /** TODO: Model Documentation for attribute mol */
    protected String mol;
    /** TODO: Model Documentation for attribute inchiAux */
    protected String inchiAux;
    /** TODO: Model Documentation for attribute ctab */
    protected String ctab;
    /** TODO: Model Documentation for attribute id */
    protected Long id;

    /**
     * Constructor taking only required properties
     */
    public CmpdFragmentStructureVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor with all properties
     * @param smilesIn String
     * @param inchiIn String
     * @param molIn String
     * @param inchiAuxIn String
     * @param ctabIn String
     * @param idIn Long
     */
    public CmpdFragmentStructureVO(final String smilesIn, final String inchiIn, final String molIn, final String inchiAuxIn, final String ctabIn, final Long idIn)
    {
        this.smiles = smilesIn;
        this.inchi = inchiIn;
        this.mol = molIn;
        this.inchiAux = inchiAuxIn;
        this.ctab = ctabIn;
        this.id = idIn;
    }

    /**
     * Copies constructor from other CmpdFragmentStructureVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public CmpdFragmentStructureVO(final CmpdFragmentStructureVO otherBean)
    {
        this.smiles = otherBean.getSmiles();
        this.inchi = otherBean.getInchi();
        this.mol = otherBean.getMol();
        this.inchiAux = otherBean.getInchiAux();
        this.ctab = otherBean.getCtab();
        this.id = otherBean.getId();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final CmpdFragmentStructureVO otherBean)
    {
        if (null != otherBean)
        {
            this.setSmiles(otherBean.getSmiles());
            this.setInchi(otherBean.getInchi());
            this.setMol(otherBean.getMol());
            this.setInchiAux(otherBean.getInchiAux());
            this.setCtab(otherBean.getCtab());
            this.setId(otherBean.getId());
        }
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
     * TODO: Model Documentation for attribute inchi
     * Get the inchi Attribute
     * @return inchi String
     */
    public String getInchi()
    {
        return this.inchi;
    }

    /**
     * 
     * @param value String
     */
    public void setInchi(final String value)
    {
        this.inchi = value;
    }

    /**
     * TODO: Model Documentation for attribute mol
     * Get the mol Attribute
     * @return mol String
     */
    public String getMol()
    {
        return this.mol;
    }

    /**
     * 
     * @param value String
     */
    public void setMol(final String value)
    {
        this.mol = value;
    }

    /**
     * TODO: Model Documentation for attribute inchiAux
     * Get the inchiAux Attribute
     * @return inchiAux String
     */
    public String getInchiAux()
    {
        return this.inchiAux;
    }

    /**
     * 
     * @param value String
     */
    public void setInchiAux(final String value)
    {
        this.inchiAux = value;
    }

    /**
     * TODO: Model Documentation for attribute ctab
     * Get the ctab Attribute
     * @return ctab String
     */
    public String getCtab()
    {
        return this.ctab;
    }

    /**
     * 
     * @param value String
     */
    public void setCtab(final String value)
    {
        this.ctab = value;
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
        CmpdFragmentStructureVO rhs = (CmpdFragmentStructureVO) object;
        return new EqualsBuilder()
            .append(this.getSmiles(), rhs.getSmiles())
            .append(this.getInchi(), rhs.getInchi())
            .append(this.getMol(), rhs.getMol())
            .append(this.getInchiAux(), rhs.getInchiAux())
            .append(this.getCtab(), rhs.getCtab())
            .append(this.getId(), rhs.getId())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final CmpdFragmentStructureVO object)
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
            .append(this.getSmiles(), object.getSmiles())
            .append(this.getInchi(), object.getInchi())
            .append(this.getMol(), object.getMol())
            .append(this.getInchiAux(), object.getInchiAux())
            .append(this.getCtab(), object.getCtab())
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
            .append(this.getSmiles())
            .append(this.getInchi())
            .append(this.getMol())
            .append(this.getInchiAux())
            .append(this.getCtab())
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
            .append("smiles", this.getSmiles())
            .append("inchi", this.getInchi())
            .append("mol", this.getMol())
            .append("inchiAux", this.getInchiAux())
            .append("ctab", this.getCtab())
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

        final CmpdFragmentStructureVO that = (CmpdFragmentStructureVO)thatObject;

        return
            equal(this.getSmiles(), that.getSmiles())
            && equal(this.getInchi(), that.getInchi())
            && equal(this.getMol(), that.getMol())
            && equal(this.getInchiAux(), that.getInchiAux())
            && equal(this.getCtab(), that.getCtab())
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

    // CmpdFragmentStructureVO value-object java merge-point
}