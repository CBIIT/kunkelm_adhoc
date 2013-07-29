// license-header java merge-point
//
/**
 * @author Generated on 07/26/2013 05:52:34-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::PptpModel::mwk.pptp::vo::CellLineGroupVO
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
 * TODO: Model Documentation for class CellLineGroupVO
 */
public class CellLineGroupVO
    implements Serializable, Comparable<CellLineGroupVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 2357659722209753428L;

    // Class attributes
    /** TODO: Model Documentation for attribute cellLine */
    protected String cellLine;
    /** TODO: Model Documentation for attribute cellLineGroupId */
    protected Long cellLineGroupId;
    /** TODO: Model Documentation for attribute compound */
    protected String compound;
    /** TODO: Model Documentation for attribute cellLineName */
    protected String cellLineName;
    /** TODO: Model Documentation for attribute compoundName */
    protected String compoundName;
    /** TODO: Model Documentation for attribute panelName */
    protected String panelName;
    /** TODO: Model Documentation for attribute cellTypeName */
    protected String cellTypeName;
    /** TODO: Model Documentation for attribute cellTypeSortOrder */
    protected Integer cellTypeSortOrder;
    /** TODO: Model Documentation for attribute cellLineSortOrder */
    protected Integer cellLineSortOrder;
    /** TODO: Model Documentation for attribute testNumber */
    protected Integer testNumber;
    /** TODO: Model Documentation for attribute schedule */
    protected String schedule;
    /** TODO: Model Documentation for attribute dose */
    protected Double dose;
    /** TODO: Model Documentation for attribute concentrationUnit */
    protected String concentrationUnit;

    /** Default Constructor with no properties */
    public CellLineGroupVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param cellLineIn String
     * @param compoundIn String
     * @param cellLineNameIn String
     * @param compoundNameIn String
     * @param panelNameIn String
     * @param cellTypeNameIn String
     * @param scheduleIn String
     * @param concentrationUnitIn String
     */
    public CellLineGroupVO(final String cellLineIn, final String compoundIn, final String cellLineNameIn, final String compoundNameIn, final String panelNameIn, final String cellTypeNameIn, final String scheduleIn, final String concentrationUnitIn)
    {
        this.cellLine = cellLineIn;
        this.compound = compoundIn;
        this.cellLineName = cellLineNameIn;
        this.compoundName = compoundNameIn;
        this.panelName = panelNameIn;
        this.cellTypeName = cellTypeNameIn;
        this.schedule = scheduleIn;
        this.concentrationUnit = concentrationUnitIn;
    }

    /**
     * Constructor with all properties
     * @param cellLineIn String
     * @param cellLineGroupIdIn Long
     * @param compoundIn String
     * @param cellLineNameIn String
     * @param compoundNameIn String
     * @param panelNameIn String
     * @param cellTypeNameIn String
     * @param cellTypeSortOrderIn Integer
     * @param cellLineSortOrderIn Integer
     * @param testNumberIn Integer
     * @param scheduleIn String
     * @param doseIn Double
     * @param concentrationUnitIn String
     */
    public CellLineGroupVO(final String cellLineIn, final Long cellLineGroupIdIn, final String compoundIn, final String cellLineNameIn, final String compoundNameIn, final String panelNameIn, final String cellTypeNameIn, final Integer cellTypeSortOrderIn, final Integer cellLineSortOrderIn, final Integer testNumberIn, final String scheduleIn, final Double doseIn, final String concentrationUnitIn)
    {
        this.cellLine = cellLineIn;
        this.cellLineGroupId = cellLineGroupIdIn;
        this.compound = compoundIn;
        this.cellLineName = cellLineNameIn;
        this.compoundName = compoundNameIn;
        this.panelName = panelNameIn;
        this.cellTypeName = cellTypeNameIn;
        this.cellTypeSortOrder = cellTypeSortOrderIn;
        this.cellLineSortOrder = cellLineSortOrderIn;
        this.testNumber = testNumberIn;
        this.schedule = scheduleIn;
        this.dose = doseIn;
        this.concentrationUnit = concentrationUnitIn;
    }

    /**
     * Copies constructor from other CellLineGroupVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public CellLineGroupVO(final CellLineGroupVO otherBean)
    {
        this.cellLine = otherBean.getCellLine();
        this.cellLineGroupId = otherBean.getCellLineGroupId();
        this.compound = otherBean.getCompound();
        this.cellLineName = otherBean.getCellLineName();
        this.compoundName = otherBean.getCompoundName();
        this.panelName = otherBean.getPanelName();
        this.cellTypeName = otherBean.getCellTypeName();
        this.cellTypeSortOrder = otherBean.getCellTypeSortOrder();
        this.cellLineSortOrder = otherBean.getCellLineSortOrder();
        this.testNumber = otherBean.getTestNumber();
        this.schedule = otherBean.getSchedule();
        this.dose = otherBean.getDose();
        this.concentrationUnit = otherBean.getConcentrationUnit();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final CellLineGroupVO otherBean)
    {
        if (null != otherBean)
        {
            this.setCellLine(otherBean.getCellLine());
            this.setCellLineGroupId(otherBean.getCellLineGroupId());
            this.setCompound(otherBean.getCompound());
            this.setCellLineName(otherBean.getCellLineName());
            this.setCompoundName(otherBean.getCompoundName());
            this.setPanelName(otherBean.getPanelName());
            this.setCellTypeName(otherBean.getCellTypeName());
            this.setCellTypeSortOrder(otherBean.getCellTypeSortOrder());
            this.setCellLineSortOrder(otherBean.getCellLineSortOrder());
            this.setTestNumber(otherBean.getTestNumber());
            this.setSchedule(otherBean.getSchedule());
            this.setDose(otherBean.getDose());
            this.setConcentrationUnit(otherBean.getConcentrationUnit());
        }
    }

    /**
     * TODO: Model Documentation for attribute cellLine
     * Get the cellLine Attribute
     * @return cellLine String
     */
    public String getCellLine()
    {
        return this.cellLine;
    }

    /**
     * 
     * @param value String
     */
    public void setCellLine(final String value)
    {
        this.cellLine = value;
    }

    /**
     * TODO: Model Documentation for attribute cellLineGroupId
     * Get the cellLineGroupId Attribute
     * @return cellLineGroupId Long
     */
    public Long getCellLineGroupId()
    {
        return this.cellLineGroupId;
    }

    /**
     * 
     * @param value Long
     */
    public void setCellLineGroupId(final Long value)
    {
        this.cellLineGroupId = value;
    }

    /**
     * TODO: Model Documentation for attribute compound
     * Get the compound Attribute
     * @return compound String
     */
    public String getCompound()
    {
        return this.compound;
    }

    /**
     * 
     * @param value String
     */
    public void setCompound(final String value)
    {
        this.compound = value;
    }

    /**
     * TODO: Model Documentation for attribute cellLineName
     * Get the cellLineName Attribute
     * @return cellLineName String
     */
    public String getCellLineName()
    {
        return this.cellLineName;
    }

    /**
     * 
     * @param value String
     */
    public void setCellLineName(final String value)
    {
        this.cellLineName = value;
    }

    /**
     * TODO: Model Documentation for attribute compoundName
     * Get the compoundName Attribute
     * @return compoundName String
     */
    public String getCompoundName()
    {
        return this.compoundName;
    }

    /**
     * 
     * @param value String
     */
    public void setCompoundName(final String value)
    {
        this.compoundName = value;
    }

    /**
     * TODO: Model Documentation for attribute panelName
     * Get the panelName Attribute
     * @return panelName String
     */
    public String getPanelName()
    {
        return this.panelName;
    }

    /**
     * 
     * @param value String
     */
    public void setPanelName(final String value)
    {
        this.panelName = value;
    }

    /**
     * TODO: Model Documentation for attribute cellTypeName
     * Get the cellTypeName Attribute
     * @return cellTypeName String
     */
    public String getCellTypeName()
    {
        return this.cellTypeName;
    }

    /**
     * 
     * @param value String
     */
    public void setCellTypeName(final String value)
    {
        this.cellTypeName = value;
    }

    /**
     * TODO: Model Documentation for attribute cellTypeSortOrder
     * Get the cellTypeSortOrder Attribute
     * @return cellTypeSortOrder Integer
     */
    public Integer getCellTypeSortOrder()
    {
        return this.cellTypeSortOrder;
    }

    /**
     * 
     * @param value Integer
     */
    public void setCellTypeSortOrder(final Integer value)
    {
        this.cellTypeSortOrder = value;
    }

    /**
     * TODO: Model Documentation for attribute cellLineSortOrder
     * Get the cellLineSortOrder Attribute
     * @return cellLineSortOrder Integer
     */
    public Integer getCellLineSortOrder()
    {
        return this.cellLineSortOrder;
    }

    /**
     * 
     * @param value Integer
     */
    public void setCellLineSortOrder(final Integer value)
    {
        this.cellLineSortOrder = value;
    }

    /**
     * TODO: Model Documentation for attribute testNumber
     * Get the testNumber Attribute
     * @return testNumber Integer
     */
    public Integer getTestNumber()
    {
        return this.testNumber;
    }

    /**
     * 
     * @param value Integer
     */
    public void setTestNumber(final Integer value)
    {
        this.testNumber = value;
    }

    /**
     * TODO: Model Documentation for attribute schedule
     * Get the schedule Attribute
     * @return schedule String
     */
    public String getSchedule()
    {
        return this.schedule;
    }

    /**
     * 
     * @param value String
     */
    public void setSchedule(final String value)
    {
        this.schedule = value;
    }

    /**
     * TODO: Model Documentation for attribute dose
     * Get the dose Attribute
     * @return dose Double
     */
    public Double getDose()
    {
        return this.dose;
    }

    /**
     * 
     * @param value Double
     */
    public void setDose(final Double value)
    {
        this.dose = value;
    }

    /**
     * TODO: Model Documentation for attribute concentrationUnit
     * Get the concentrationUnit Attribute
     * @return concentrationUnit String
     */
    public String getConcentrationUnit()
    {
        return this.concentrationUnit;
    }

    /**
     * 
     * @param value String
     */
    public void setConcentrationUnit(final String value)
    {
        this.concentrationUnit = value;
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
        CellLineGroupVO rhs = (CellLineGroupVO) object;
        return new EqualsBuilder()
            .append(this.getCellLine(), rhs.getCellLine())
            .append(this.getCellLineGroupId(), rhs.getCellLineGroupId())
            .append(this.getCompound(), rhs.getCompound())
            .append(this.getCellLineName(), rhs.getCellLineName())
            .append(this.getCompoundName(), rhs.getCompoundName())
            .append(this.getPanelName(), rhs.getPanelName())
            .append(this.getCellTypeName(), rhs.getCellTypeName())
            .append(this.getCellTypeSortOrder(), rhs.getCellTypeSortOrder())
            .append(this.getCellLineSortOrder(), rhs.getCellLineSortOrder())
            .append(this.getTestNumber(), rhs.getTestNumber())
            .append(this.getSchedule(), rhs.getSchedule())
            .append(this.getDose(), rhs.getDose())
            .append(this.getConcentrationUnit(), rhs.getConcentrationUnit())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final CellLineGroupVO object)
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
            .append(this.getCellLine(), object.getCellLine())
            .append(this.getCellLineGroupId(), object.getCellLineGroupId())
            .append(this.getCompound(), object.getCompound())
            .append(this.getCellLineName(), object.getCellLineName())
            .append(this.getCompoundName(), object.getCompoundName())
            .append(this.getPanelName(), object.getPanelName())
            .append(this.getCellTypeName(), object.getCellTypeName())
            .append(this.getCellTypeSortOrder(), object.getCellTypeSortOrder())
            .append(this.getCellLineSortOrder(), object.getCellLineSortOrder())
            .append(this.getTestNumber(), object.getTestNumber())
            .append(this.getSchedule(), object.getSchedule())
            .append(this.getDose(), object.getDose())
            .append(this.getConcentrationUnit(), object.getConcentrationUnit())
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
            .append(this.getCellLine())
            .append(this.getCellLineGroupId())
            .append(this.getCompound())
            .append(this.getCellLineName())
            .append(this.getCompoundName())
            .append(this.getPanelName())
            .append(this.getCellTypeName())
            .append(this.getCellTypeSortOrder())
            .append(this.getCellLineSortOrder())
            .append(this.getTestNumber())
            .append(this.getSchedule())
            .append(this.getDose())
            .append(this.getConcentrationUnit())
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
            .append("cellLine", this.getCellLine())
            .append("cellLineGroupId", this.getCellLineGroupId())
            .append("compound", this.getCompound())
            .append("cellLineName", this.getCellLineName())
            .append("compoundName", this.getCompoundName())
            .append("panelName", this.getPanelName())
            .append("cellTypeName", this.getCellTypeName())
            .append("cellTypeSortOrder", this.getCellTypeSortOrder())
            .append("cellLineSortOrder", this.getCellLineSortOrder())
            .append("testNumber", this.getTestNumber())
            .append("schedule", this.getSchedule())
            .append("dose", this.getDose())
            .append("concentrationUnit", this.getConcentrationUnit())
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

        final CellLineGroupVO that = (CellLineGroupVO)thatObject;

        return
            equal(this.getCellLine(), that.getCellLine())
            && equal(this.getCellLineGroupId(), that.getCellLineGroupId())
            && equal(this.getCompound(), that.getCompound())
            && equal(this.getCellLineName(), that.getCellLineName())
            && equal(this.getCompoundName(), that.getCompoundName())
            && equal(this.getPanelName(), that.getPanelName())
            && equal(this.getCellTypeName(), that.getCellTypeName())
            && equal(this.getCellTypeSortOrder(), that.getCellTypeSortOrder())
            && equal(this.getCellLineSortOrder(), that.getCellLineSortOrder())
            && equal(this.getTestNumber(), that.getTestNumber())
            && equal(this.getSchedule(), that.getSchedule())
            && equal(this.getDose(), that.getDose())
            && equal(this.getConcentrationUnit(), that.getConcentrationUnit())
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

    // CellLineGroupVO value-object java merge-point
}