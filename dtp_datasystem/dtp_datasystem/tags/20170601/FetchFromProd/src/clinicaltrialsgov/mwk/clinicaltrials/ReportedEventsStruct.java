//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.10 at 01:01:25 PM EDT 
//


package clinicaltrialsgov.mwk.clinicaltrials;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for reported_events_struct complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reported_events_struct">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="time_frame" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="group_list">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="group" type="{}group_struct" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="serious_events" type="{}events_struct" minOccurs="0"/>
 *         &lt;element name="other_events" type="{}events_struct" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reported_events_struct", propOrder = {
    "timeFrame",
    "desc",
    "groupList",
    "seriousEvents",
    "otherEvents"
})
public class ReportedEventsStruct {

    @XmlElement(name = "time_frame")
    protected String timeFrame;
    protected String desc;
    @XmlElement(name = "group_list", required = true)
    protected ReportedEventsStruct.GroupList groupList;
    @XmlElement(name = "serious_events")
    protected EventsStruct seriousEvents;
    @XmlElement(name = "other_events")
    protected EventsStruct otherEvents;

    /**
     * Gets the value of the timeFrame property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeFrame() {
        return timeFrame;
    }

    /**
     * Sets the value of the timeFrame property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeFrame(String value) {
        this.timeFrame = value;
    }

    /**
     * Gets the value of the desc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets the value of the desc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesc(String value) {
        this.desc = value;
    }

    /**
     * Gets the value of the groupList property.
     * 
     * @return
     *     possible object is
     *     {@link ReportedEventsStruct.GroupList }
     *     
     */
    public ReportedEventsStruct.GroupList getGroupList() {
        return groupList;
    }

    /**
     * Sets the value of the groupList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportedEventsStruct.GroupList }
     *     
     */
    public void setGroupList(ReportedEventsStruct.GroupList value) {
        this.groupList = value;
    }

    /**
     * Gets the value of the seriousEvents property.
     * 
     * @return
     *     possible object is
     *     {@link EventsStruct }
     *     
     */
    public EventsStruct getSeriousEvents() {
        return seriousEvents;
    }

    /**
     * Sets the value of the seriousEvents property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventsStruct }
     *     
     */
    public void setSeriousEvents(EventsStruct value) {
        this.seriousEvents = value;
    }

    /**
     * Gets the value of the otherEvents property.
     * 
     * @return
     *     possible object is
     *     {@link EventsStruct }
     *     
     */
    public EventsStruct getOtherEvents() {
        return otherEvents;
    }

    /**
     * Sets the value of the otherEvents property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventsStruct }
     *     
     */
    public void setOtherEvents(EventsStruct value) {
        this.otherEvents = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="group" type="{}group_struct" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "group"
    })
    public static class GroupList {

        @XmlElement(required = true)
        protected List<GroupStruct> group;

        /**
         * Gets the value of the group property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the group property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGroup().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GroupStruct }
         * 
         * 
         */
        public List<GroupStruct> getGroup() {
            if (group == null) {
                group = new ArrayList<GroupStruct>();
            }
            return this.group;
        }

    }

}
