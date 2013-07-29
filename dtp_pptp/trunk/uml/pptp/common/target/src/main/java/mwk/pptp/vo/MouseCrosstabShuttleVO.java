// license-header java merge-point
//
/**
 * @author Generated on 07/27/2013 08:57:19-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::PptpModel::mwk.pptp::vo::MouseCrosstabShuttleVO
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
 * TODO: Model Documentation for class MouseCrosstabShuttleVO
 */
public class MouseCrosstabShuttleVO
    implements Serializable, Comparable<MouseCrosstabShuttleVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 8048815257540442956L;

    // Class attributes
    /** TODO: Model Documentation for attribute mouse1 */
    protected Double mouse1;
    /** TODO: Model Documentation for attribute mouse2 */
    protected Double mouse2;
    /** TODO: Model Documentation for attribute mouse3 */
    protected Double mouse3;
    /** TODO: Model Documentation for attribute mouse4 */
    protected Double mouse4;
    /** TODO: Model Documentation for attribute mouse5 */
    protected Double mouse5;
    /** TODO: Model Documentation for attribute mouse6 */
    protected Double mouse6;
    /** TODO: Model Documentation for attribute mouse7 */
    protected Double mouse7;
    /** TODO: Model Documentation for attribute mouse8 */
    protected Double mouse8;
    /** TODO: Model Documentation for attribute mouse9 */
    protected Double mouse9;
    /** TODO: Model Documentation for attribute mouse10 */
    protected Double mouse10;
    /** TODO: Model Documentation for attribute day */
    protected Integer day;
    /** TODO: Model Documentation for attribute compoundName */
    protected String compoundName;
    /** TODO: Model Documentation for attribute panelName */
    protected String panelName;
    /** TODO: Model Documentation for attribute cellLineName */
    protected String cellLineName;
    /** TODO: Model Documentation for attribute groupRole */
    protected String groupRole;
    /** TODO: Model Documentation for attribute compound */
    protected String compound;
    /** TODO: Model Documentation for attribute cellLine */
    protected String cellLine;
    /** TODO: Model Documentation for attribute cellTypeName */
    protected String cellTypeName;
    /** TODO: Model Documentation for attribute cellTypeSortOrder */
    protected Integer cellTypeSortOrder;
    /** TODO: Model Documentation for attribute cellLineSortOrder */
    protected Integer cellLineSortOrder;
    /** TODO: Model Documentation for attribute testNumber */
    protected Integer testNumber;

    /** Default Constructor with no properties */
    public MouseCrosstabShuttleVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param compoundNameIn String
     * @param panelNameIn String
     * @param cellLineNameIn String
     * @param groupRoleIn String
     * @param compoundIn String
     * @param cellLineIn String
     * @param cellTypeNameIn String
     */
    public MouseCrosstabShuttleVO(final String compoundNameIn, final String panelNameIn, final String cellLineNameIn, final String groupRoleIn, final String compoundIn, final String cellLineIn, final String cellTypeNameIn)
    {
        this.compoundName = compoundNameIn;
        this.panelName = panelNameIn;
        this.cellLineName = cellLineNameIn;
        this.groupRole = groupRoleIn;
        this.compound = compoundIn;
        this.cellLine = cellLineIn;
        this.cellTypeName = cellTypeNameIn;
    }

    /**
     * Constructor with all properties
     * @param mouse1In Double
     * @param mouse2In Double
     * @param mouse3In Double
     * @param mouse4In Double
     * @param mouse5In Double
     * @param mouse6In Double
     * @param mouse7In Double
     * @param mouse8In Double
     * @param mouse9In Double
     * @param mouse10In Double
     * @param dayIn Integer
     * @param compoundNameIn String
     * @param panelNameIn String
     * @param cellLineNameIn String
     * @param groupRoleIn String
     * @param compoundIn String
     * @param cellLineIn String
     * @param cellTypeNameIn String
     * @param cellTypeSortOrderIn Integer
     * @param cellLineSortOrderIn Integer
     * @param testNumberIn Integer
     */
    public MouseCrosstabShuttleVO(final Double mouse1In, final Double mouse2In, final Double mouse3In, final Double mouse4In, final Double mouse5In, final Double mouse6In, final Double mouse7In, final Double mouse8In, final Double mouse9In, final Double mouse10In, final Integer dayIn, final String compoundNameIn, final String panelNameIn, final String cellLineNameIn, final String groupRoleIn, final String compoundIn, final String cellLineIn, final String cellTypeNameIn, final Integer cellTypeSortOrderIn, final Integer cellLineSortOrderIn, final Integer testNumberIn)
    {
        this.mouse1 = mouse1In;
        this.mouse2 = mouse2In;
        this.mouse3 = mouse3In;
        this.mouse4 = mouse4In;
        this.mouse5 = mouse5In;
        this.mouse6 = mouse6In;
        this.mouse7 = mouse7In;
        this.mouse8 = mouse8In;
        this.mouse9 = mouse9In;
        this.mouse10 = mouse10In;
        this.day = dayIn;
        this.compoundName = compoundNameIn;
        this.panelName = panelNameIn;
        this.cellLineName = cellLineNameIn;
        this.groupRole = groupRoleIn;
        this.compound = compoundIn;
        this.cellLine = cellLineIn;
        this.cellTypeName = cellTypeNameIn;
        this.cellTypeSortOrder = cellTypeSortOrderIn;
        this.cellLineSortOrder = cellLineSortOrderIn;
        this.testNumber = testNumberIn;
    }

    /**
     * Copies constructor from other MouseCrosstabShuttleVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public MouseCrosstabShuttleVO(final MouseCrosstabShuttleVO otherBean)
    {
        this.mouse1 = otherBean.getMouse1();
        this.mouse2 = otherBean.getMouse2();
        this.mouse3 = otherBean.getMouse3();
        this.mouse4 = otherBean.getMouse4();
        this.mouse5 = otherBean.getMouse5();
        this.mouse6 = otherBean.getMouse6();
        this.mouse7 = otherBean.getMouse7();
        this.mouse8 = otherBean.getMouse8();
        this.mouse9 = otherBean.getMouse9();
        this.mouse10 = otherBean.getMouse10();
        this.day = otherBean.getDay();
        this.compoundName = otherBean.getCompoundName();
        this.panelName = otherBean.getPanelName();
        this.cellLineName = otherBean.getCellLineName();
        this.groupRole = otherBean.getGroupRole();
        this.compound = otherBean.getCompound();
        this.cellLine = otherBean.getCellLine();
        this.cellTypeName = otherBean.getCellTypeName();
        this.cellTypeSortOrder = otherBean.getCellTypeSortOrder();
        this.cellLineSortOrder = otherBean.getCellLineSortOrder();
        this.testNumber = otherBean.getTestNumber();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final MouseCrosstabShuttleVO otherBean)
    {
        if (null != otherBean)
        {
            this.setMouse1(otherBean.getMouse1());
            this.setMouse2(otherBean.getMouse2());
            this.setMouse3(otherBean.getMouse3());
            this.setMouse4(otherBean.getMouse4());
            this.setMouse5(otherBean.getMouse5());
            this.setMouse6(otherBean.getMouse6());
            this.setMouse7(otherBean.getMouse7());
            this.setMouse8(otherBean.getMouse8());
            this.setMouse9(otherBean.getMouse9());
            this.setMouse10(otherBean.getMouse10());
            this.setDay(otherBean.getDay());
            this.setCompoundName(otherBean.getCompoundName());
            this.setPanelName(otherBean.getPanelName());
            this.setCellLineName(otherBean.getCellLineName());
            this.setGroupRole(otherBean.getGroupRole());
            this.setCompound(otherBean.getCompound());
            this.setCellLine(otherBean.getCellLine());
            this.setCellTypeName(otherBean.getCellTypeName());
            this.setCellTypeSortOrder(otherBean.getCellTypeSortOrder());
            this.setCellLineSortOrder(otherBean.getCellLineSortOrder());
            this.setTestNumber(otherBean.getTestNumber());
        }
    }

    /**
     * TODO: Model Documentation for attribute mouse1
     * Get the mouse1 Attribute
     * @return mouse1 Double
     */
    public Double getMouse1()
    {
        return this.mouse1;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse1(final Double value)
    {
        this.mouse1 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse2
     * Get the mouse2 Attribute
     * @return mouse2 Double
     */
    public Double getMouse2()
    {
        return this.mouse2;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse2(final Double value)
    {
        this.mouse2 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse3
     * Get the mouse3 Attribute
     * @return mouse3 Double
     */
    public Double getMouse3()
    {
        return this.mouse3;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse3(final Double value)
    {
        this.mouse3 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse4
     * Get the mouse4 Attribute
     * @return mouse4 Double
     */
    public Double getMouse4()
    {
        return this.mouse4;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse4(final Double value)
    {
        this.mouse4 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse5
     * Get the mouse5 Attribute
     * @return mouse5 Double
     */
    public Double getMouse5()
    {
        return this.mouse5;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse5(final Double value)
    {
        this.mouse5 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse6
     * Get the mouse6 Attribute
     * @return mouse6 Double
     */
    public Double getMouse6()
    {
        return this.mouse6;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse6(final Double value)
    {
        this.mouse6 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse7
     * Get the mouse7 Attribute
     * @return mouse7 Double
     */
    public Double getMouse7()
    {
        return this.mouse7;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse7(final Double value)
    {
        this.mouse7 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse8
     * Get the mouse8 Attribute
     * @return mouse8 Double
     */
    public Double getMouse8()
    {
        return this.mouse8;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse8(final Double value)
    {
        this.mouse8 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse9
     * Get the mouse9 Attribute
     * @return mouse9 Double
     */
    public Double getMouse9()
    {
        return this.mouse9;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse9(final Double value)
    {
        this.mouse9 = value;
    }

    /**
     * TODO: Model Documentation for attribute mouse10
     * Get the mouse10 Attribute
     * @return mouse10 Double
     */
    public Double getMouse10()
    {
        return this.mouse10;
    }

    /**
     * 
     * @param value Double
     */
    public void setMouse10(final Double value)
    {
        this.mouse10 = value;
    }

    /**
     * TODO: Model Documentation for attribute day
     * Get the day Attribute
     * @return day Integer
     */
    public Integer getDay()
    {
        return this.day;
    }

    /**
     * 
     * @param value Integer
     */
    public void setDay(final Integer value)
    {
        this.day = value;
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
     * TODO: Model Documentation for attribute groupRole
     * Get the groupRole Attribute
     * @return groupRole String
     */
    public String getGroupRole()
    {
        return this.groupRole;
    }

    /**
     * 
     * @param value String
     */
    public void setGroupRole(final String value)
    {
        this.groupRole = value;
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
        MouseCrosstabShuttleVO rhs = (MouseCrosstabShuttleVO) object;
        return new EqualsBuilder()
            .append(this.getMouse1(), rhs.getMouse1())
            .append(this.getMouse2(), rhs.getMouse2())
            .append(this.getMouse3(), rhs.getMouse3())
            .append(this.getMouse4(), rhs.getMouse4())
            .append(this.getMouse5(), rhs.getMouse5())
            .append(this.getMouse6(), rhs.getMouse6())
            .append(this.getMouse7(), rhs.getMouse7())
            .append(this.getMouse8(), rhs.getMouse8())
            .append(this.getMouse9(), rhs.getMouse9())
            .append(this.getMouse10(), rhs.getMouse10())
            .append(this.getDay(), rhs.getDay())
            .append(this.getCompoundName(), rhs.getCompoundName())
            .append(this.getPanelName(), rhs.getPanelName())
            .append(this.getCellLineName(), rhs.getCellLineName())
            .append(this.getGroupRole(), rhs.getGroupRole())
            .append(this.getCompound(), rhs.getCompound())
            .append(this.getCellLine(), rhs.getCellLine())
            .append(this.getCellTypeName(), rhs.getCellTypeName())
            .append(this.getCellTypeSortOrder(), rhs.getCellTypeSortOrder())
            .append(this.getCellLineSortOrder(), rhs.getCellLineSortOrder())
            .append(this.getTestNumber(), rhs.getTestNumber())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final MouseCrosstabShuttleVO object)
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
            .append(this.getMouse1(), object.getMouse1())
            .append(this.getMouse2(), object.getMouse2())
            .append(this.getMouse3(), object.getMouse3())
            .append(this.getMouse4(), object.getMouse4())
            .append(this.getMouse5(), object.getMouse5())
            .append(this.getMouse6(), object.getMouse6())
            .append(this.getMouse7(), object.getMouse7())
            .append(this.getMouse8(), object.getMouse8())
            .append(this.getMouse9(), object.getMouse9())
            .append(this.getMouse10(), object.getMouse10())
            .append(this.getDay(), object.getDay())
            .append(this.getCompoundName(), object.getCompoundName())
            .append(this.getPanelName(), object.getPanelName())
            .append(this.getCellLineName(), object.getCellLineName())
            .append(this.getGroupRole(), object.getGroupRole())
            .append(this.getCompound(), object.getCompound())
            .append(this.getCellLine(), object.getCellLine())
            .append(this.getCellTypeName(), object.getCellTypeName())
            .append(this.getCellTypeSortOrder(), object.getCellTypeSortOrder())
            .append(this.getCellLineSortOrder(), object.getCellLineSortOrder())
            .append(this.getTestNumber(), object.getTestNumber())
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
            .append(this.getMouse1())
            .append(this.getMouse2())
            .append(this.getMouse3())
            .append(this.getMouse4())
            .append(this.getMouse5())
            .append(this.getMouse6())
            .append(this.getMouse7())
            .append(this.getMouse8())
            .append(this.getMouse9())
            .append(this.getMouse10())
            .append(this.getDay())
            .append(this.getCompoundName())
            .append(this.getPanelName())
            .append(this.getCellLineName())
            .append(this.getGroupRole())
            .append(this.getCompound())
            .append(this.getCellLine())
            .append(this.getCellTypeName())
            .append(this.getCellTypeSortOrder())
            .append(this.getCellLineSortOrder())
            .append(this.getTestNumber())
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
            .append("mouse1", this.getMouse1())
            .append("mouse2", this.getMouse2())
            .append("mouse3", this.getMouse3())
            .append("mouse4", this.getMouse4())
            .append("mouse5", this.getMouse5())
            .append("mouse6", this.getMouse6())
            .append("mouse7", this.getMouse7())
            .append("mouse8", this.getMouse8())
            .append("mouse9", this.getMouse9())
            .append("mouse10", this.getMouse10())
            .append("day", this.getDay())
            .append("compoundName", this.getCompoundName())
            .append("panelName", this.getPanelName())
            .append("cellLineName", this.getCellLineName())
            .append("groupRole", this.getGroupRole())
            .append("compound", this.getCompound())
            .append("cellLine", this.getCellLine())
            .append("cellTypeName", this.getCellTypeName())
            .append("cellTypeSortOrder", this.getCellTypeSortOrder())
            .append("cellLineSortOrder", this.getCellLineSortOrder())
            .append("testNumber", this.getTestNumber())
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

        final MouseCrosstabShuttleVO that = (MouseCrosstabShuttleVO)thatObject;

        return
            equal(this.getMouse1(), that.getMouse1())
            && equal(this.getMouse2(), that.getMouse2())
            && equal(this.getMouse3(), that.getMouse3())
            && equal(this.getMouse4(), that.getMouse4())
            && equal(this.getMouse5(), that.getMouse5())
            && equal(this.getMouse6(), that.getMouse6())
            && equal(this.getMouse7(), that.getMouse7())
            && equal(this.getMouse8(), that.getMouse8())
            && equal(this.getMouse9(), that.getMouse9())
            && equal(this.getMouse10(), that.getMouse10())
            && equal(this.getDay(), that.getDay())
            && equal(this.getCompoundName(), that.getCompoundName())
            && equal(this.getPanelName(), that.getPanelName())
            && equal(this.getCellLineName(), that.getCellLineName())
            && equal(this.getGroupRole(), that.getGroupRole())
            && equal(this.getCompound(), that.getCompound())
            && equal(this.getCellLine(), that.getCellLine())
            && equal(this.getCellTypeName(), that.getCellTypeName())
            && equal(this.getCellTypeSortOrder(), that.getCellTypeSortOrder())
            && equal(this.getCellLineSortOrder(), that.getCellLineSortOrder())
            && equal(this.getTestNumber(), that.getTestNumber())
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

    // MouseCrosstabShuttleVO value-object java merge-point
}