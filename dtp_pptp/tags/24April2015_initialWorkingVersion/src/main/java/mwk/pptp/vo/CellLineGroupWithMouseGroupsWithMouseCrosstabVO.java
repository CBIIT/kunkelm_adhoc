// license-header java merge-point
//
/**
 * @author Generated on 07/26/2013 05:52:34-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::PptpModel::mwk.pptp::vo::CellLineGroupWithMouseGroupsWithMouseCrosstabVO
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
 * TODO: Model Documentation for class CellLineGroupWithMouseGroupsWithMouseCrosstabVO
 */
public class CellLineGroupWithMouseGroupsWithMouseCrosstabVO
    extends CellLineGroupVO
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -8264193722823150100L;

    // Class attributes
    /** TODO: Model Documentation for attribute mouseGroupsWithMouseCrosstabs */
    protected Collection<MouseGroupWithMouseCrosstabVO> mouseGroupsWithMouseCrosstabs;

    /** Default Constructor with no properties */
    public CellLineGroupWithMouseGroupsWithMouseCrosstabVO()
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
    public CellLineGroupWithMouseGroupsWithMouseCrosstabVO(final String cellLineIn, final String compoundIn, final String cellLineNameIn, final String compoundNameIn, final String panelNameIn, final String cellTypeNameIn, final String scheduleIn, final String concentrationUnitIn)
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
     * @param mouseGroupsWithMouseCrosstabsIn Collection<MouseGroupWithMouseCrosstabVO>
     */
    public CellLineGroupWithMouseGroupsWithMouseCrosstabVO(final String cellLineIn, final Long cellLineGroupIdIn, final String compoundIn, final String cellLineNameIn, final String compoundNameIn, final String panelNameIn, final String cellTypeNameIn, final Integer cellTypeSortOrderIn, final Integer cellLineSortOrderIn, final Integer testNumberIn, final String scheduleIn, final Double doseIn, final String concentrationUnitIn, final Collection<MouseGroupWithMouseCrosstabVO> mouseGroupsWithMouseCrosstabsIn)
    {
        super(cellLineIn, cellLineGroupIdIn, compoundIn, cellLineNameIn, compoundNameIn, panelNameIn, cellTypeNameIn, cellTypeSortOrderIn, cellLineSortOrderIn, testNumberIn, scheduleIn, doseIn, concentrationUnitIn);
        this.mouseGroupsWithMouseCrosstabs = mouseGroupsWithMouseCrosstabsIn;
    }

    /**
     * Copies constructor from other CellLineGroupWithMouseGroupsWithMouseCrosstabVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public CellLineGroupWithMouseGroupsWithMouseCrosstabVO(final CellLineGroupWithMouseGroupsWithMouseCrosstabVO otherBean)
    {
        super(otherBean);
        this.mouseGroupsWithMouseCrosstabs = otherBean.getMouseGroupsWithMouseCrosstabs();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final CellLineGroupWithMouseGroupsWithMouseCrosstabVO otherBean)
    {
        if (null != otherBean)
        {
            super.copy(otherBean);
            this.setMouseGroupsWithMouseCrosstabs(otherBean.getMouseGroupsWithMouseCrosstabs());
        }
    }

    /**
     * TODO: Model Documentation for attribute mouseGroupsWithMouseCrosstabs
     * Get the mouseGroupsWithMouseCrosstabs Attribute
     * @return mouseGroupsWithMouseCrosstabs Collection<MouseGroupWithMouseCrosstabVO>
     */
    public Collection<MouseGroupWithMouseCrosstabVO> getMouseGroupsWithMouseCrosstabs()
    {
        if (this.mouseGroupsWithMouseCrosstabs == null)
        {
            this.mouseGroupsWithMouseCrosstabs = new ArrayList<MouseGroupWithMouseCrosstabVO>();
        }
        return this.mouseGroupsWithMouseCrosstabs;
    }

    /**
     * 
     * @param value Collection<MouseGroupWithMouseCrosstabVO>
     */
    public void setMouseGroupsWithMouseCrosstabs(final Collection<MouseGroupWithMouseCrosstabVO> value)
    {
        this.mouseGroupsWithMouseCrosstabs = value;
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
        CellLineGroupWithMouseGroupsWithMouseCrosstabVO rhs = (CellLineGroupWithMouseGroupsWithMouseCrosstabVO) object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.getMouseGroupsWithMouseCrosstabs(), rhs.getMouseGroupsWithMouseCrosstabs())
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
        if (!(object instanceof CellLineGroupWithMouseGroupsWithMouseCrosstabVO))
        {
            return -1;
        }
        CellLineGroupWithMouseGroupsWithMouseCrosstabVO myClass = (CellLineGroupWithMouseGroupsWithMouseCrosstabVO)object;
        return new CompareToBuilder()
            .appendSuper(super.compareTo(object))
            .append(this.getMouseGroupsWithMouseCrosstabs(), myClass.getMouseGroupsWithMouseCrosstabs())
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
            .append(this.getMouseGroupsWithMouseCrosstabs())
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
            .append("mouseGroupsWithMouseCrosstabs", this.getMouseGroupsWithMouseCrosstabs())
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

        final CellLineGroupWithMouseGroupsWithMouseCrosstabVO that = (CellLineGroupWithMouseGroupsWithMouseCrosstabVO)thatObject;

        return super.equalProperties(that)
            && equal(this.getMouseGroupsWithMouseCrosstabs(), that.getMouseGroupsWithMouseCrosstabs())
        ;
    }

    // CellLineGroupWithMouseGroupsWithMouseCrosstabVO value-object java merge-point
}