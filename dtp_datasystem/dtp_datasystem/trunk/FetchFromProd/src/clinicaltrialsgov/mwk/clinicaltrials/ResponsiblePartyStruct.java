//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.10 at 01:01:25 PM EDT 
//


package clinicaltrialsgov.mwk.clinicaltrials;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for responsible_party_struct complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responsible_party_struct">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name_title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organization" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="responsible_party_type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="investigator_affiliation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="investigator_full_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="investigator_title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responsible_party_struct", propOrder = {
    "nameTitle",
    "organization",
    "responsiblePartyType",
    "investigatorAffiliation",
    "investigatorFullName",
    "investigatorTitle"
})
public class ResponsiblePartyStruct {

    @XmlElement(name = "name_title")
    protected String nameTitle;
    protected String organization;
    @XmlElement(name = "responsible_party_type")
    protected String responsiblePartyType;
    @XmlElement(name = "investigator_affiliation")
    protected String investigatorAffiliation;
    @XmlElement(name = "investigator_full_name")
    protected String investigatorFullName;
    @XmlElement(name = "investigator_title")
    protected String investigatorTitle;

    /**
     * Gets the value of the nameTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameTitle() {
        return nameTitle;
    }

    /**
     * Sets the value of the nameTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameTitle(String value) {
        this.nameTitle = value;
    }

    /**
     * Gets the value of the organization property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Sets the value of the organization property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganization(String value) {
        this.organization = value;
    }

    /**
     * Gets the value of the responsiblePartyType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponsiblePartyType() {
        return responsiblePartyType;
    }

    /**
     * Sets the value of the responsiblePartyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponsiblePartyType(String value) {
        this.responsiblePartyType = value;
    }

    /**
     * Gets the value of the investigatorAffiliation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvestigatorAffiliation() {
        return investigatorAffiliation;
    }

    /**
     * Sets the value of the investigatorAffiliation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvestigatorAffiliation(String value) {
        this.investigatorAffiliation = value;
    }

    /**
     * Gets the value of the investigatorFullName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvestigatorFullName() {
        return investigatorFullName;
    }

    /**
     * Sets the value of the investigatorFullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvestigatorFullName(String value) {
        this.investigatorFullName = value;
    }

    /**
     * Gets the value of the investigatorTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvestigatorTitle() {
        return investigatorTitle;
    }

    /**
     * Sets the value of the investigatorTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvestigatorTitle(String value) {
        this.investigatorTitle = value;
    }

}
