// license-header java merge-point
//
/**
 * @author Generated on 07/26/2013 05:52:34-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::PptpModel::mwk.pptp::vo::SummaryVO
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
 * TODO: Model Documentation for class SummaryVO
 */
public class SummaryVO
    implements Serializable, Comparable<SummaryVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -6913306983212022495L;

    // Class attributes
    /** TODO: Model Documentation for attribute theN1 */
    protected Integer theN1;
    /** TODO: Model Documentation for attribute theD1 */
    protected Integer theD1;
    /** TODO: Model Documentation for attribute theE1 */
    protected Integer theE1;
    /** TODO: Model Documentation for attribute theN2 */
    protected Integer theN2;
    /** TODO: Model Documentation for attribute countMouseEvents */
    protected Integer countMouseEvents;
    /** TODO: Model Documentation for attribute medianDaysToEvent */
    protected Double medianDaysToEvent;
    /** TODO: Model Documentation for attribute thePValueFlag */
    protected String thePValueFlag;
    /** TODO: Model Documentation for attribute thePValue */
    protected Double thePValue;
    /** TODO: Model Documentation for attribute efsTOverCFlag */
    protected String efsTOverCFlag;
    /** TODO: Model Documentation for attribute efsTOverC */
    protected Double efsTOverC;
    /** TODO: Model Documentation for attribute medianRTVFlag */
    protected String medianRTVFlag;
    /** TODO: Model Documentation for attribute medianRTV */
    protected Double medianRTV;
    /** TODO: Model Documentation for attribute dayForTOverC */
    protected Integer dayForTOverC;
    /** TODO: Model Documentation for attribute TOverC */
    protected Double TOverC;
    /** TODO: Model Documentation for attribute countPD */
    protected Integer countPD;
    /** TODO: Model Documentation for attribute countPD1 */
    protected Integer countPD1;
    /** TODO: Model Documentation for attribute countPD2 */
    protected Integer countPD2;
    /** TODO: Model Documentation for attribute countSD */
    protected Integer countSD;
    /** TODO: Model Documentation for attribute countPR */
    protected Integer countPR;
    /** TODO: Model Documentation for attribute countCR */
    protected Integer countCR;
    /** TODO: Model Documentation for attribute countMCR */
    protected Integer countMCR;
    /** TODO: Model Documentation for attribute responseMedianScore */
    protected Double responseMedianScore;
    /** TODO: Model Documentation for attribute overallGroupMedianResponse */
    protected String overallGroupMedianResponse;
    /** TODO: Model Documentation for attribute groupRole */
    protected String groupRole;
    /** TODO: Model Documentation for attribute survivingPercent */
    protected Integer survivingPercent;
    /** TODO: Model Documentation for attribute averageRTVAtDayForTOverC */
    protected Double averageRTVAtDayForTOverC;

    /** Default Constructor with no properties */
    public SummaryVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param overallGroupMedianResponseIn String
     * @param groupRoleIn String
     */
    public SummaryVO(final String overallGroupMedianResponseIn, final String groupRoleIn)
    {
        this.overallGroupMedianResponse = overallGroupMedianResponseIn;
        this.groupRole = groupRoleIn;
    }

    /**
     * Constructor with all properties
     * @param theN1In Integer
     * @param theD1In Integer
     * @param theE1In Integer
     * @param theN2In Integer
     * @param countMouseEventsIn Integer
     * @param medianDaysToEventIn Double
     * @param thePValueFlagIn String
     * @param thePValueIn Double
     * @param efsTOverCFlagIn String
     * @param efsTOverCIn Double
     * @param medianRTVFlagIn String
     * @param medianRTVIn Double
     * @param dayForTOverCIn Integer
     * @param TOverCIn Double
     * @param countPDIn Integer
     * @param countPD1In Integer
     * @param countPD2In Integer
     * @param countSDIn Integer
     * @param countPRIn Integer
     * @param countCRIn Integer
     * @param countMCRIn Integer
     * @param responseMedianScoreIn Double
     * @param overallGroupMedianResponseIn String
     * @param groupRoleIn String
     * @param survivingPercentIn Integer
     * @param averageRTVAtDayForTOverCIn Double
     */
    public SummaryVO(final Integer theN1In, final Integer theD1In, final Integer theE1In, final Integer theN2In, final Integer countMouseEventsIn, final Double medianDaysToEventIn, final String thePValueFlagIn, final Double thePValueIn, final String efsTOverCFlagIn, final Double efsTOverCIn, final String medianRTVFlagIn, final Double medianRTVIn, final Integer dayForTOverCIn, final Double TOverCIn, final Integer countPDIn, final Integer countPD1In, final Integer countPD2In, final Integer countSDIn, final Integer countPRIn, final Integer countCRIn, final Integer countMCRIn, final Double responseMedianScoreIn, final String overallGroupMedianResponseIn, final String groupRoleIn, final Integer survivingPercentIn, final Double averageRTVAtDayForTOverCIn)
    {
        this.theN1 = theN1In;
        this.theD1 = theD1In;
        this.theE1 = theE1In;
        this.theN2 = theN2In;
        this.countMouseEvents = countMouseEventsIn;
        this.medianDaysToEvent = medianDaysToEventIn;
        this.thePValueFlag = thePValueFlagIn;
        this.thePValue = thePValueIn;
        this.efsTOverCFlag = efsTOverCFlagIn;
        this.efsTOverC = efsTOverCIn;
        this.medianRTVFlag = medianRTVFlagIn;
        this.medianRTV = medianRTVIn;
        this.dayForTOverC = dayForTOverCIn;
        this.TOverC = TOverCIn;
        this.countPD = countPDIn;
        this.countPD1 = countPD1In;
        this.countPD2 = countPD2In;
        this.countSD = countSDIn;
        this.countPR = countPRIn;
        this.countCR = countCRIn;
        this.countMCR = countMCRIn;
        this.responseMedianScore = responseMedianScoreIn;
        this.overallGroupMedianResponse = overallGroupMedianResponseIn;
        this.groupRole = groupRoleIn;
        this.survivingPercent = survivingPercentIn;
        this.averageRTVAtDayForTOverC = averageRTVAtDayForTOverCIn;
    }

    /**
     * Copies constructor from other SummaryVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public SummaryVO(final SummaryVO otherBean)
    {
        this.theN1 = otherBean.getTheN1();
        this.theD1 = otherBean.getTheD1();
        this.theE1 = otherBean.getTheE1();
        this.theN2 = otherBean.getTheN2();
        this.countMouseEvents = otherBean.getCountMouseEvents();
        this.medianDaysToEvent = otherBean.getMedianDaysToEvent();
        this.thePValueFlag = otherBean.getThePValueFlag();
        this.thePValue = otherBean.getThePValue();
        this.efsTOverCFlag = otherBean.getEfsTOverCFlag();
        this.efsTOverC = otherBean.getEfsTOverC();
        this.medianRTVFlag = otherBean.getMedianRTVFlag();
        this.medianRTV = otherBean.getMedianRTV();
        this.dayForTOverC = otherBean.getDayForTOverC();
        this.TOverC = otherBean.getTOverC();
        this.countPD = otherBean.getCountPD();
        this.countPD1 = otherBean.getCountPD1();
        this.countPD2 = otherBean.getCountPD2();
        this.countSD = otherBean.getCountSD();
        this.countPR = otherBean.getCountPR();
        this.countCR = otherBean.getCountCR();
        this.countMCR = otherBean.getCountMCR();
        this.responseMedianScore = otherBean.getResponseMedianScore();
        this.overallGroupMedianResponse = otherBean.getOverallGroupMedianResponse();
        this.groupRole = otherBean.getGroupRole();
        this.survivingPercent = otherBean.getSurvivingPercent();
        this.averageRTVAtDayForTOverC = otherBean.getAverageRTVAtDayForTOverC();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final SummaryVO otherBean)
    {
        if (null != otherBean)
        {
            this.setTheN1(otherBean.getTheN1());
            this.setTheD1(otherBean.getTheD1());
            this.setTheE1(otherBean.getTheE1());
            this.setTheN2(otherBean.getTheN2());
            this.setCountMouseEvents(otherBean.getCountMouseEvents());
            this.setMedianDaysToEvent(otherBean.getMedianDaysToEvent());
            this.setThePValueFlag(otherBean.getThePValueFlag());
            this.setThePValue(otherBean.getThePValue());
            this.setEfsTOverCFlag(otherBean.getEfsTOverCFlag());
            this.setEfsTOverC(otherBean.getEfsTOverC());
            this.setMedianRTVFlag(otherBean.getMedianRTVFlag());
            this.setMedianRTV(otherBean.getMedianRTV());
            this.setDayForTOverC(otherBean.getDayForTOverC());
            this.setTOverC(otherBean.getTOverC());
            this.setCountPD(otherBean.getCountPD());
            this.setCountPD1(otherBean.getCountPD1());
            this.setCountPD2(otherBean.getCountPD2());
            this.setCountSD(otherBean.getCountSD());
            this.setCountPR(otherBean.getCountPR());
            this.setCountCR(otherBean.getCountCR());
            this.setCountMCR(otherBean.getCountMCR());
            this.setResponseMedianScore(otherBean.getResponseMedianScore());
            this.setOverallGroupMedianResponse(otherBean.getOverallGroupMedianResponse());
            this.setGroupRole(otherBean.getGroupRole());
            this.setSurvivingPercent(otherBean.getSurvivingPercent());
            this.setAverageRTVAtDayForTOverC(otherBean.getAverageRTVAtDayForTOverC());
        }
    }

    /**
     * TODO: Model Documentation for attribute theN1
     * Get the theN1 Attribute
     * @return theN1 Integer
     */
    public Integer getTheN1()
    {
        return this.theN1;
    }

    /**
     * 
     * @param value Integer
     */
    public void setTheN1(final Integer value)
    {
        this.theN1 = value;
    }

    /**
     * TODO: Model Documentation for attribute theD1
     * Get the theD1 Attribute
     * @return theD1 Integer
     */
    public Integer getTheD1()
    {
        return this.theD1;
    }

    /**
     * 
     * @param value Integer
     */
    public void setTheD1(final Integer value)
    {
        this.theD1 = value;
    }

    /**
     * TODO: Model Documentation for attribute theE1
     * Get the theE1 Attribute
     * @return theE1 Integer
     */
    public Integer getTheE1()
    {
        return this.theE1;
    }

    /**
     * 
     * @param value Integer
     */
    public void setTheE1(final Integer value)
    {
        this.theE1 = value;
    }

    /**
     * TODO: Model Documentation for attribute theN2
     * Get the theN2 Attribute
     * @return theN2 Integer
     */
    public Integer getTheN2()
    {
        return this.theN2;
    }

    /**
     * 
     * @param value Integer
     */
    public void setTheN2(final Integer value)
    {
        this.theN2 = value;
    }

    /**
     * TODO: Model Documentation for attribute countMouseEvents
     * Get the countMouseEvents Attribute
     * @return countMouseEvents Integer
     */
    public Integer getCountMouseEvents()
    {
        return this.countMouseEvents;
    }

    /**
     * 
     * @param value Integer
     */
    public void setCountMouseEvents(final Integer value)
    {
        this.countMouseEvents = value;
    }

    /**
     * TODO: Model Documentation for attribute medianDaysToEvent
     * Get the medianDaysToEvent Attribute
     * @return medianDaysToEvent Double
     */
    public Double getMedianDaysToEvent()
    {
        return this.medianDaysToEvent;
    }

    /**
     * 
     * @param value Double
     */
    public void setMedianDaysToEvent(final Double value)
    {
        this.medianDaysToEvent = value;
    }

    /**
     * TODO: Model Documentation for attribute thePValueFlag
     * Get the thePValueFlag Attribute
     * @return thePValueFlag String
     */
    public String getThePValueFlag()
    {
        return this.thePValueFlag;
    }

    /**
     * 
     * @param value String
     */
    public void setThePValueFlag(final String value)
    {
        this.thePValueFlag = value;
    }

    /**
     * TODO: Model Documentation for attribute thePValue
     * Get the thePValue Attribute
     * @return thePValue Double
     */
    public Double getThePValue()
    {
        return this.thePValue;
    }

    /**
     * 
     * @param value Double
     */
    public void setThePValue(final Double value)
    {
        this.thePValue = value;
    }

    /**
     * TODO: Model Documentation for attribute efsTOverCFlag
     * Get the efsTOverCFlag Attribute
     * @return efsTOverCFlag String
     */
    public String getEfsTOverCFlag()
    {
        return this.efsTOverCFlag;
    }

    /**
     * 
     * @param value String
     */
    public void setEfsTOverCFlag(final String value)
    {
        this.efsTOverCFlag = value;
    }

    /**
     * TODO: Model Documentation for attribute efsTOverC
     * Get the efsTOverC Attribute
     * @return efsTOverC Double
     */
    public Double getEfsTOverC()
    {
        return this.efsTOverC;
    }

    /**
     * 
     * @param value Double
     */
    public void setEfsTOverC(final Double value)
    {
        this.efsTOverC = value;
    }

    /**
     * TODO: Model Documentation for attribute medianRTVFlag
     * Get the medianRTVFlag Attribute
     * @return medianRTVFlag String
     */
    public String getMedianRTVFlag()
    {
        return this.medianRTVFlag;
    }

    /**
     * 
     * @param value String
     */
    public void setMedianRTVFlag(final String value)
    {
        this.medianRTVFlag = value;
    }

    /**
     * TODO: Model Documentation for attribute medianRTV
     * Get the medianRTV Attribute
     * @return medianRTV Double
     */
    public Double getMedianRTV()
    {
        return this.medianRTV;
    }

    /**
     * 
     * @param value Double
     */
    public void setMedianRTV(final Double value)
    {
        this.medianRTV = value;
    }

    /**
     * TODO: Model Documentation for attribute dayForTOverC
     * Get the dayForTOverC Attribute
     * @return dayForTOverC Integer
     */
    public Integer getDayForTOverC()
    {
        return this.dayForTOverC;
    }

    /**
     * 
     * @param value Integer
     */
    public void setDayForTOverC(final Integer value)
    {
        this.dayForTOverC = value;
    }

    /**
     * TODO: Model Documentation for attribute TOverC
     * Get the TOverC Attribute
     * @return TOverC Double
     */
    public Double getTOverC()
    {
        return this.TOverC;
    }

    /**
     * 
     * @param value Double
     */
    public void setTOverC(final Double value)
    {
        this.TOverC = value;
    }

    /**
     * TODO: Model Documentation for attribute countPD
     * Get the countPD Attribute
     * @return countPD Integer
     */
    public Integer getCountPD()
    {
        return this.countPD;
    }

    /**
     * 
     * @param value Integer
     */
    public void setCountPD(final Integer value)
    {
        this.countPD = value;
    }

    /**
     * TODO: Model Documentation for attribute countPD1
     * Get the countPD1 Attribute
     * @return countPD1 Integer
     */
    public Integer getCountPD1()
    {
        return this.countPD1;
    }

    /**
     * 
     * @param value Integer
     */
    public void setCountPD1(final Integer value)
    {
        this.countPD1 = value;
    }

    /**
     * TODO: Model Documentation for attribute countPD2
     * Get the countPD2 Attribute
     * @return countPD2 Integer
     */
    public Integer getCountPD2()
    {
        return this.countPD2;
    }

    /**
     * 
     * @param value Integer
     */
    public void setCountPD2(final Integer value)
    {
        this.countPD2 = value;
    }

    /**
     * TODO: Model Documentation for attribute countSD
     * Get the countSD Attribute
     * @return countSD Integer
     */
    public Integer getCountSD()
    {
        return this.countSD;
    }

    /**
     * 
     * @param value Integer
     */
    public void setCountSD(final Integer value)
    {
        this.countSD = value;
    }

    /**
     * TODO: Model Documentation for attribute countPR
     * Get the countPR Attribute
     * @return countPR Integer
     */
    public Integer getCountPR()
    {
        return this.countPR;
    }

    /**
     * 
     * @param value Integer
     */
    public void setCountPR(final Integer value)
    {
        this.countPR = value;
    }

    /**
     * TODO: Model Documentation for attribute countCR
     * Get the countCR Attribute
     * @return countCR Integer
     */
    public Integer getCountCR()
    {
        return this.countCR;
    }

    /**
     * 
     * @param value Integer
     */
    public void setCountCR(final Integer value)
    {
        this.countCR = value;
    }

    /**
     * TODO: Model Documentation for attribute countMCR
     * Get the countMCR Attribute
     * @return countMCR Integer
     */
    public Integer getCountMCR()
    {
        return this.countMCR;
    }

    /**
     * 
     * @param value Integer
     */
    public void setCountMCR(final Integer value)
    {
        this.countMCR = value;
    }

    /**
     * TODO: Model Documentation for attribute responseMedianScore
     * Get the responseMedianScore Attribute
     * @return responseMedianScore Double
     */
    public Double getResponseMedianScore()
    {
        return this.responseMedianScore;
    }

    /**
     * 
     * @param value Double
     */
    public void setResponseMedianScore(final Double value)
    {
        this.responseMedianScore = value;
    }

    /**
     * TODO: Model Documentation for attribute overallGroupMedianResponse
     * Get the overallGroupMedianResponse Attribute
     * @return overallGroupMedianResponse String
     */
    public String getOverallGroupMedianResponse()
    {
        return this.overallGroupMedianResponse;
    }

    /**
     * 
     * @param value String
     */
    public void setOverallGroupMedianResponse(final String value)
    {
        this.overallGroupMedianResponse = value;
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
     * TODO: Model Documentation for attribute survivingPercent
     * Get the survivingPercent Attribute
     * @return survivingPercent Integer
     */
    public Integer getSurvivingPercent()
    {
        return this.survivingPercent;
    }

    /**
     * 
     * @param value Integer
     */
    public void setSurvivingPercent(final Integer value)
    {
        this.survivingPercent = value;
    }

    /**
     * TODO: Model Documentation for attribute averageRTVAtDayForTOverC
     * Get the averageRTVAtDayForTOverC Attribute
     * @return averageRTVAtDayForTOverC Double
     */
    public Double getAverageRTVAtDayForTOverC()
    {
        return this.averageRTVAtDayForTOverC;
    }

    /**
     * 
     * @param value Double
     */
    public void setAverageRTVAtDayForTOverC(final Double value)
    {
        this.averageRTVAtDayForTOverC = value;
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
        SummaryVO rhs = (SummaryVO) object;
        return new EqualsBuilder()
            .append(this.getTheN1(), rhs.getTheN1())
            .append(this.getTheD1(), rhs.getTheD1())
            .append(this.getTheE1(), rhs.getTheE1())
            .append(this.getTheN2(), rhs.getTheN2())
            .append(this.getCountMouseEvents(), rhs.getCountMouseEvents())
            .append(this.getMedianDaysToEvent(), rhs.getMedianDaysToEvent())
            .append(this.getThePValueFlag(), rhs.getThePValueFlag())
            .append(this.getThePValue(), rhs.getThePValue())
            .append(this.getEfsTOverCFlag(), rhs.getEfsTOverCFlag())
            .append(this.getEfsTOverC(), rhs.getEfsTOverC())
            .append(this.getMedianRTVFlag(), rhs.getMedianRTVFlag())
            .append(this.getMedianRTV(), rhs.getMedianRTV())
            .append(this.getDayForTOverC(), rhs.getDayForTOverC())
            .append(this.getTOverC(), rhs.getTOverC())
            .append(this.getCountPD(), rhs.getCountPD())
            .append(this.getCountPD1(), rhs.getCountPD1())
            .append(this.getCountPD2(), rhs.getCountPD2())
            .append(this.getCountSD(), rhs.getCountSD())
            .append(this.getCountPR(), rhs.getCountPR())
            .append(this.getCountCR(), rhs.getCountCR())
            .append(this.getCountMCR(), rhs.getCountMCR())
            .append(this.getResponseMedianScore(), rhs.getResponseMedianScore())
            .append(this.getOverallGroupMedianResponse(), rhs.getOverallGroupMedianResponse())
            .append(this.getGroupRole(), rhs.getGroupRole())
            .append(this.getSurvivingPercent(), rhs.getSurvivingPercent())
            .append(this.getAverageRTVAtDayForTOverC(), rhs.getAverageRTVAtDayForTOverC())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final SummaryVO object)
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
            .append(this.getTheN1(), object.getTheN1())
            .append(this.getTheD1(), object.getTheD1())
            .append(this.getTheE1(), object.getTheE1())
            .append(this.getTheN2(), object.getTheN2())
            .append(this.getCountMouseEvents(), object.getCountMouseEvents())
            .append(this.getMedianDaysToEvent(), object.getMedianDaysToEvent())
            .append(this.getThePValueFlag(), object.getThePValueFlag())
            .append(this.getThePValue(), object.getThePValue())
            .append(this.getEfsTOverCFlag(), object.getEfsTOverCFlag())
            .append(this.getEfsTOverC(), object.getEfsTOverC())
            .append(this.getMedianRTVFlag(), object.getMedianRTVFlag())
            .append(this.getMedianRTV(), object.getMedianRTV())
            .append(this.getDayForTOverC(), object.getDayForTOverC())
            .append(this.getTOverC(), object.getTOverC())
            .append(this.getCountPD(), object.getCountPD())
            .append(this.getCountPD1(), object.getCountPD1())
            .append(this.getCountPD2(), object.getCountPD2())
            .append(this.getCountSD(), object.getCountSD())
            .append(this.getCountPR(), object.getCountPR())
            .append(this.getCountCR(), object.getCountCR())
            .append(this.getCountMCR(), object.getCountMCR())
            .append(this.getResponseMedianScore(), object.getResponseMedianScore())
            .append(this.getOverallGroupMedianResponse(), object.getOverallGroupMedianResponse())
            .append(this.getGroupRole(), object.getGroupRole())
            .append(this.getSurvivingPercent(), object.getSurvivingPercent())
            .append(this.getAverageRTVAtDayForTOverC(), object.getAverageRTVAtDayForTOverC())
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
            .append(this.getTheN1())
            .append(this.getTheD1())
            .append(this.getTheE1())
            .append(this.getTheN2())
            .append(this.getCountMouseEvents())
            .append(this.getMedianDaysToEvent())
            .append(this.getThePValueFlag())
            .append(this.getThePValue())
            .append(this.getEfsTOverCFlag())
            .append(this.getEfsTOverC())
            .append(this.getMedianRTVFlag())
            .append(this.getMedianRTV())
            .append(this.getDayForTOverC())
            .append(this.getTOverC())
            .append(this.getCountPD())
            .append(this.getCountPD1())
            .append(this.getCountPD2())
            .append(this.getCountSD())
            .append(this.getCountPR())
            .append(this.getCountCR())
            .append(this.getCountMCR())
            .append(this.getResponseMedianScore())
            .append(this.getOverallGroupMedianResponse())
            .append(this.getGroupRole())
            .append(this.getSurvivingPercent())
            .append(this.getAverageRTVAtDayForTOverC())
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
            .append("theN1", this.getTheN1())
            .append("theD1", this.getTheD1())
            .append("theE1", this.getTheE1())
            .append("theN2", this.getTheN2())
            .append("countMouseEvents", this.getCountMouseEvents())
            .append("medianDaysToEvent", this.getMedianDaysToEvent())
            .append("thePValueFlag", this.getThePValueFlag())
            .append("thePValue", this.getThePValue())
            .append("efsTOverCFlag", this.getEfsTOverCFlag())
            .append("efsTOverC", this.getEfsTOverC())
            .append("medianRTVFlag", this.getMedianRTVFlag())
            .append("medianRTV", this.getMedianRTV())
            .append("dayForTOverC", this.getDayForTOverC())
            .append("TOverC", this.getTOverC())
            .append("countPD", this.getCountPD())
            .append("countPD1", this.getCountPD1())
            .append("countPD2", this.getCountPD2())
            .append("countSD", this.getCountSD())
            .append("countPR", this.getCountPR())
            .append("countCR", this.getCountCR())
            .append("countMCR", this.getCountMCR())
            .append("responseMedianScore", this.getResponseMedianScore())
            .append("overallGroupMedianResponse", this.getOverallGroupMedianResponse())
            .append("groupRole", this.getGroupRole())
            .append("survivingPercent", this.getSurvivingPercent())
            .append("averageRTVAtDayForTOverC", this.getAverageRTVAtDayForTOverC())
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

        final SummaryVO that = (SummaryVO)thatObject;

        return
            equal(this.getTheN1(), that.getTheN1())
            && equal(this.getTheD1(), that.getTheD1())
            && equal(this.getTheE1(), that.getTheE1())
            && equal(this.getTheN2(), that.getTheN2())
            && equal(this.getCountMouseEvents(), that.getCountMouseEvents())
            && equal(this.getMedianDaysToEvent(), that.getMedianDaysToEvent())
            && equal(this.getThePValueFlag(), that.getThePValueFlag())
            && equal(this.getThePValue(), that.getThePValue())
            && equal(this.getEfsTOverCFlag(), that.getEfsTOverCFlag())
            && equal(this.getEfsTOverC(), that.getEfsTOverC())
            && equal(this.getMedianRTVFlag(), that.getMedianRTVFlag())
            && equal(this.getMedianRTV(), that.getMedianRTV())
            && equal(this.getDayForTOverC(), that.getDayForTOverC())
            && equal(this.getTOverC(), that.getTOverC())
            && equal(this.getCountPD(), that.getCountPD())
            && equal(this.getCountPD1(), that.getCountPD1())
            && equal(this.getCountPD2(), that.getCountPD2())
            && equal(this.getCountSD(), that.getCountSD())
            && equal(this.getCountPR(), that.getCountPR())
            && equal(this.getCountCR(), that.getCountCR())
            && equal(this.getCountMCR(), that.getCountMCR())
            && equal(this.getResponseMedianScore(), that.getResponseMedianScore())
            && equal(this.getOverallGroupMedianResponse(), that.getOverallGroupMedianResponse())
            && equal(this.getGroupRole(), that.getGroupRole())
            && equal(this.getSurvivingPercent(), that.getSurvivingPercent())
            && equal(this.getAverageRTVAtDayForTOverC(), that.getAverageRTVAtDayForTOverC())
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

    // SummaryVO value-object java merge-point
}