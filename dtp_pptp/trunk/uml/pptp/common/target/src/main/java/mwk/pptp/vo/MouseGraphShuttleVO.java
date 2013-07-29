// license-header java merge-point
//
/**
 * @author Generated on 07/27/2013 08:57:19-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::PptpModel::mwk.pptp::vo::MouseGraphShuttleVO
 * STEREOTYPE:   ValueObject
 */
package mwk.pptp.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * TODO: Model Documentation for class MouseGraphShuttleVO
 */
public class MouseGraphShuttleVO
    implements Serializable, Comparable<MouseGraphShuttleVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -2931532667295741635L;

    // Class attributes
    /** TODO: Model Documentation for attribute datas */
    protected Collection<MouseDataShuttleVO> datas;
    /** TODO: Model Documentation for attribute survivals */
    protected Collection<MouseSurvivalShuttleVO> survivals;
    /** TODO: Model Documentation for attribute rtvs */
    protected Collection<MouseRTVShuttleVO> rtvs;
    /** TODO: Model Documentation for attribute compound */
    protected String compound;
    /** TODO: Model Documentation for attribute compoundName */
    protected String compoundName;
    /** TODO: Model Documentation for attribute cellLine */
    protected String cellLine;
    /** TODO: Model Documentation for attribute cellLineName */
    protected String cellLineName;
    /** TODO: Model Documentation for attribute testNumber */
    protected Integer testNumber;
    /** TODO: Model Documentation for attribute panelType */
    protected String panelType;
    /** TODO: Model Documentation for attribute schedule */
    protected String schedule;
    /** TODO: Model Documentation for attribute concentrationUnit */
    protected String concentrationUnit;
    /** TODO: Model Documentation for attribute dose */
    protected Double dose;

    /** Default Constructor with no properties */
    public MouseGraphShuttleVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param compoundIn String
     * @param compoundNameIn String
     * @param cellLineIn String
     * @param cellLineNameIn String
     * @param panelTypeIn String
     * @param scheduleIn String
     * @param concentrationUnitIn String
     */
    public MouseGraphShuttleVO(final String compoundIn, final String compoundNameIn, final String cellLineIn, final String cellLineNameIn, final String panelTypeIn, final String scheduleIn, final String concentrationUnitIn)
    {
        this.compound = compoundIn;
        this.compoundName = compoundNameIn;
        this.cellLine = cellLineIn;
        this.cellLineName = cellLineNameIn;
        this.panelType = panelTypeIn;
        this.schedule = scheduleIn;
        this.concentrationUnit = concentrationUnitIn;
    }

    /**
     * Constructor with all properties
     * @param datasIn Collection<MouseDataShuttleVO>
     * @param survivalsIn Collection<MouseSurvivalShuttleVO>
     * @param rtvsIn Collection<MouseRTVShuttleVO>
     * @param compoundIn String
     * @param compoundNameIn String
     * @param cellLineIn String
     * @param cellLineNameIn String
     * @param testNumberIn Integer
     * @param panelTypeIn String
     * @param scheduleIn String
     * @param concentrationUnitIn String
     * @param doseIn Double
     */
    public MouseGraphShuttleVO(final Collection<MouseDataShuttleVO> datasIn, final Collection<MouseSurvivalShuttleVO> survivalsIn, final Collection<MouseRTVShuttleVO> rtvsIn, final String compoundIn, final String compoundNameIn, final String cellLineIn, final String cellLineNameIn, final Integer testNumberIn, final String panelTypeIn, final String scheduleIn, final String concentrationUnitIn, final Double doseIn)
    {
        this.datas = datasIn;
        this.survivals = survivalsIn;
        this.rtvs = rtvsIn;
        this.compound = compoundIn;
        this.compoundName = compoundNameIn;
        this.cellLine = cellLineIn;
        this.cellLineName = cellLineNameIn;
        this.testNumber = testNumberIn;
        this.panelType = panelTypeIn;
        this.schedule = scheduleIn;
        this.concentrationUnit = concentrationUnitIn;
        this.dose = doseIn;
    }

    /**
     * Copies constructor from other MouseGraphShuttleVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public MouseGraphShuttleVO(final MouseGraphShuttleVO otherBean)
    {
        this.datas = otherBean.getDatas();
        this.survivals = otherBean.getSurvivals();
        this.rtvs = otherBean.getRtvs();
        this.compound = otherBean.getCompound();
        this.compoundName = otherBean.getCompoundName();
        this.cellLine = otherBean.getCellLine();
        this.cellLineName = otherBean.getCellLineName();
        this.testNumber = otherBean.getTestNumber();
        this.panelType = otherBean.getPanelType();
        this.schedule = otherBean.getSchedule();
        this.concentrationUnit = otherBean.getConcentrationUnit();
        this.dose = otherBean.getDose();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final MouseGraphShuttleVO otherBean)
    {
        if (null != otherBean)
        {
            this.setDatas(otherBean.getDatas());
            this.setSurvivals(otherBean.getSurvivals());
            this.setRtvs(otherBean.getRtvs());
            this.setCompound(otherBean.getCompound());
            this.setCompoundName(otherBean.getCompoundName());
            this.setCellLine(otherBean.getCellLine());
            this.setCellLineName(otherBean.getCellLineName());
            this.setTestNumber(otherBean.getTestNumber());
            this.setPanelType(otherBean.getPanelType());
            this.setSchedule(otherBean.getSchedule());
            this.setConcentrationUnit(otherBean.getConcentrationUnit());
            this.setDose(otherBean.getDose());
        }
    }

    /**
     * TODO: Model Documentation for attribute datas
     * Get the datas Attribute
     * @return datas Collection<MouseDataShuttleVO>
     */
    public Collection<MouseDataShuttleVO> getDatas()
    {
        if (this.datas == null)
        {
            this.datas = new ArrayList<MouseDataShuttleVO>();
        }
        return this.datas;
    }

    /**
     * 
     * @param value Collection<MouseDataShuttleVO>
     */
    public void setDatas(final Collection<MouseDataShuttleVO> value)
    {
        this.datas = value;
    }

    /**
     * TODO: Model Documentation for attribute survivals
     * Get the survivals Attribute
     * @return survivals Collection<MouseSurvivalShuttleVO>
     */
    public Collection<MouseSurvivalShuttleVO> getSurvivals()
    {
        if (this.survivals == null)
        {
            this.survivals = new ArrayList<MouseSurvivalShuttleVO>();
        }
        return this.survivals;
    }

    /**
     * 
     * @param value Collection<MouseSurvivalShuttleVO>
     */
    public void setSurvivals(final Collection<MouseSurvivalShuttleVO> value)
    {
        this.survivals = value;
    }

    /**
     * TODO: Model Documentation for attribute rtvs
     * Get the rtvs Attribute
     * @return rtvs Collection<MouseRTVShuttleVO>
     */
    public Collection<MouseRTVShuttleVO> getRtvs()
    {
        if (this.rtvs == null)
        {
            this.rtvs = new ArrayList<MouseRTVShuttleVO>();
        }
        return this.rtvs;
    }

    /**
     * 
     * @param value Collection<MouseRTVShuttleVO>
     */
    public void setRtvs(final Collection<MouseRTVShuttleVO> value)
    {
        this.rtvs = value;
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
     * TODO: Model Documentation for attribute panelType
     * Get the panelType Attribute
     * @return panelType String
     */
    public String getPanelType()
    {
        return this.panelType;
    }

    /**
     * 
     * @param value String
     */
    public void setPanelType(final String value)
    {
        this.panelType = value;
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
        MouseGraphShuttleVO rhs = (MouseGraphShuttleVO) object;
        return new EqualsBuilder()
            .append(this.getDatas(), rhs.getDatas())
            .append(this.getSurvivals(), rhs.getSurvivals())
            .append(this.getRtvs(), rhs.getRtvs())
            .append(this.getCompound(), rhs.getCompound())
            .append(this.getCompoundName(), rhs.getCompoundName())
            .append(this.getCellLine(), rhs.getCellLine())
            .append(this.getCellLineName(), rhs.getCellLineName())
            .append(this.getTestNumber(), rhs.getTestNumber())
            .append(this.getPanelType(), rhs.getPanelType())
            .append(this.getSchedule(), rhs.getSchedule())
            .append(this.getConcentrationUnit(), rhs.getConcentrationUnit())
            .append(this.getDose(), rhs.getDose())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final MouseGraphShuttleVO object)
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
            .append(this.getDatas(), object.getDatas())
            .append(this.getSurvivals(), object.getSurvivals())
            .append(this.getRtvs(), object.getRtvs())
            .append(this.getCompound(), object.getCompound())
            .append(this.getCompoundName(), object.getCompoundName())
            .append(this.getCellLine(), object.getCellLine())
            .append(this.getCellLineName(), object.getCellLineName())
            .append(this.getTestNumber(), object.getTestNumber())
            .append(this.getPanelType(), object.getPanelType())
            .append(this.getSchedule(), object.getSchedule())
            .append(this.getConcentrationUnit(), object.getConcentrationUnit())
            .append(this.getDose(), object.getDose())
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
            .append(this.getDatas())
            .append(this.getSurvivals())
            .append(this.getRtvs())
            .append(this.getCompound())
            .append(this.getCompoundName())
            .append(this.getCellLine())
            .append(this.getCellLineName())
            .append(this.getTestNumber())
            .append(this.getPanelType())
            .append(this.getSchedule())
            .append(this.getConcentrationUnit())
            .append(this.getDose())
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
            .append("datas", this.getDatas())
            .append("survivals", this.getSurvivals())
            .append("rtvs", this.getRtvs())
            .append("compound", this.getCompound())
            .append("compoundName", this.getCompoundName())
            .append("cellLine", this.getCellLine())
            .append("cellLineName", this.getCellLineName())
            .append("testNumber", this.getTestNumber())
            .append("panelType", this.getPanelType())
            .append("schedule", this.getSchedule())
            .append("concentrationUnit", this.getConcentrationUnit())
            .append("dose", this.getDose())
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

        final MouseGraphShuttleVO that = (MouseGraphShuttleVO)thatObject;

        return
            equal(this.getDatas(), that.getDatas())
            && equal(this.getSurvivals(), that.getSurvivals())
            && equal(this.getRtvs(), that.getRtvs())
            && equal(this.getCompound(), that.getCompound())
            && equal(this.getCompoundName(), that.getCompoundName())
            && equal(this.getCellLine(), that.getCellLine())
            && equal(this.getCellLineName(), that.getCellLineName())
            && equal(this.getTestNumber(), that.getTestNumber())
            && equal(this.getPanelType(), that.getPanelType())
            && equal(this.getSchedule(), that.getSchedule())
            && equal(this.getConcentrationUnit(), that.getConcentrationUnit())
            && equal(this.getDose(), that.getDose())
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

    // MouseGraphShuttleVO value-object java merge-point
}