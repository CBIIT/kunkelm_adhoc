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
 * <p>Java class for participant_flow_struct complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="participant_flow_struct">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="recruitment_details" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pre_assignment_details" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
 *         &lt;element name="period_list">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="period" type="{}period_struct" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "participant_flow_struct", propOrder = {
    "recruitmentDetails",
    "preAssignmentDetails",
    "groupList",
    "periodList"
})
public class ParticipantFlowStruct {

    @XmlElement(name = "recruitment_details")
    protected String recruitmentDetails;
    @XmlElement(name = "pre_assignment_details")
    protected String preAssignmentDetails;
    @XmlElement(name = "group_list", required = true)
    protected ParticipantFlowStruct.GroupList groupList;
    @XmlElement(name = "period_list", required = true)
    protected ParticipantFlowStruct.PeriodList periodList;

    /**
     * Gets the value of the recruitmentDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecruitmentDetails() {
        return recruitmentDetails;
    }

    /**
     * Sets the value of the recruitmentDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecruitmentDetails(String value) {
        this.recruitmentDetails = value;
    }

    /**
     * Gets the value of the preAssignmentDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreAssignmentDetails() {
        return preAssignmentDetails;
    }

    /**
     * Sets the value of the preAssignmentDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreAssignmentDetails(String value) {
        this.preAssignmentDetails = value;
    }

    /**
     * Gets the value of the groupList property.
     * 
     * @return
     *     possible object is
     *     {@link ParticipantFlowStruct.GroupList }
     *     
     */
    public ParticipantFlowStruct.GroupList getGroupList() {
        return groupList;
    }

    /**
     * Sets the value of the groupList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParticipantFlowStruct.GroupList }
     *     
     */
    public void setGroupList(ParticipantFlowStruct.GroupList value) {
        this.groupList = value;
    }

    /**
     * Gets the value of the periodList property.
     * 
     * @return
     *     possible object is
     *     {@link ParticipantFlowStruct.PeriodList }
     *     
     */
    public ParticipantFlowStruct.PeriodList getPeriodList() {
        return periodList;
    }

    /**
     * Sets the value of the periodList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParticipantFlowStruct.PeriodList }
     *     
     */
    public void setPeriodList(ParticipantFlowStruct.PeriodList value) {
        this.periodList = value;
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
     *         &lt;element name="period" type="{}period_struct" maxOccurs="unbounded"/>
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
        "period"
    })
    public static class PeriodList {

        @XmlElement(required = true)
        protected List<PeriodStruct> period;

        /**
         * Gets the value of the period property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the period property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPeriod().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PeriodStruct }
         * 
         * 
         */
        public List<PeriodStruct> getPeriod() {
            if (period == null) {
                period = new ArrayList<PeriodStruct>();
            }
            return this.period;
        }

    }

}