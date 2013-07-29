// license-header java merge-point
//
/**
 * @author Generated on 07/26/2013 05:52:35-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::PptpModel::mwk.pptp::vo::CellLineGroupWithMouseGroupsWithMousesVO
 * STEREOTYPE:   ValueObject
 */
package mwk.pptp.vo;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * TODO: Model Documentation for class CellLineGroupWithMouseGroupsWithMousesVO
 */
public class CellLineGroupWithMouseGroupsWithMousesVO
    extends CellLineGroupVO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -7500765814783079750L;

    // Class attributes
    /** TODO: Model Documentation for attribute mouseGroupsWithMouses */
    protected Collection<MouseGroupWithMousesVO> mouseGroupsWithMouses;

    /** Default Constructor with no properties */
    public CellLineGroupWithMouseGroupsWithMousesVO()
    {
        super();
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
    public CellLineGroupWithMouseGroupsWithMousesVO(final String cellLineIn, final String compoundIn, final String cellLineNameIn, final String compoundNameIn, final String panelNameIn, final String cellTypeNameIn, final String scheduleIn, final String concentrationUnitIn)
    {
        super(cellLineIn, compoundIn, cellLineNameIn, compoundNameIn, panelNameIn, cellTypeNameIn, scheduleIn, concentrationUnitIn);
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
     * @param mouseGroupsWithMousesIn Collection<MouseGroupWithMousesVO>
     */
    public CellLineGroupWithMouseGroupsWithMousesVO(final String cellLineIn, final Long cellLineGroupIdIn, final String compoundIn, final String cellLineNameIn, final String compoundNameIn, final String panelNameIn, final String cellTypeNameIn, final Integer cellTypeSortOrderIn, final Integer cellLineSortOrderIn, final Integer testNumberIn, final String scheduleIn, final Double doseIn, final String concentrationUnitIn, final Collection<MouseGroupWithMousesVO> mouseGroupsWithMousesIn)
    {
        super(cellLineIn, cellLineGroupIdIn, compoundIn, cellLineNameIn, compoundNameIn, panelNameIn, cellTypeNameIn, cellTypeSortOrderIn, cellLineSortOrderIn, testNumberIn, scheduleIn, doseIn, concentrationUnitIn);
        this.mouseGroupsWithMouses = mouseGroupsWithMousesIn;
    }

    /**
     * Copies constructor from other CellLineGroupWithMouseGroupsWithMousesVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public CellLineGroupWithMouseGroupsWithMousesVO(final CellLineGroupWithMouseGroupsWithMousesVO otherBean)
    {
        super(otherBean);
        this.mouseGroupsWithMouses = otherBean.getMouseGroupsWithMouses();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final CellLineGroupWithMouseGroupsWithMousesVO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setMouseGroupsWithMouses(otherBean.getMouseGroupsWithMouses());
        }
    }

    /**
     * TODO: Model Documentation for attribute mouseGroupsWithMouses
     * Get the mouseGroupsWithMouses Attribute
     * @return mouseGroupsWithMouses Collection<MouseGroupWithMousesVO>
     */
    public Collection<MouseGroupWithMousesVO> getMouseGroupsWithMouses()
    {
        if (this.mouseGroupsWithMouses == null)
        {
            this.mouseGroupsWithMouses = new ArrayList<MouseGroupWithMousesVO>();
        }
        return this.mouseGroupsWithMouses;
    }

    /**
     * 
     * @param value Collection<MouseGroupWithMousesVO>
     */
    public void setMouseGroupsWithMouses(final Collection<MouseGroupWithMousesVO> value)
    {
        this.mouseGroupsWithMouses = value;
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
        CellLineGroupWithMouseGroupsWithMousesVO rhs = (CellLineGroupWithMouseGroupsWithMousesVO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getMouseGroupsWithMouses(), rhs.getMouseGroupsWithMouses())
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
        if (!(object instanceof CellLineGroupWithMouseGroupsWithMousesVO))
        {
            return -1;
        }
        CellLineGroupWithMouseGroupsWithMousesVO myClass = (CellLineGroupWithMouseGroupsWithMousesVO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getMouseGroupsWithMouses(), myClass.getMouseGroupsWithMouses())
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
            .appendSuper(super.hashCode())
            .append(this.getMouseGroupsWithMouses())
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
            .append("mouseGroupsWithMouses", this.getMouseGroupsWithMouses())
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
    @Override
    public boolean equalProperties(final Object thatObject)
    {
        if (thatObject == null || !this.getClass().isAssignableFrom(thatObject.getClass()))
        {
            return false;
        }

        final CellLineGroupWithMouseGroupsWithMousesVO that = (CellLineGroupWithMouseGroupsWithMousesVO)thatObject;

        return super.equalProperties(that)
            && equal(this.getMouseGroupsWithMouses(), that.getMouseGroupsWithMouses())
        ;
    }

    // CellLineGroupWithMouseGroupsWithMousesVO value-object java merge-point
}