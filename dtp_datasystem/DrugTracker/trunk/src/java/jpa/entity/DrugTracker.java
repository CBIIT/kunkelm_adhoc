/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mwkunkel
 */
@Entity
@Table(name = "drug_tracker")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "DrugTracker.findAll", query = "SELECT d FROM DrugTracker d"),
  @NamedQuery(name = "DrugTracker.findById", query = "SELECT d FROM DrugTracker d WHERE d.id = :id"),
  @NamedQuery(name = "DrugTracker.findByAgent", query = "SELECT d FROM DrugTracker d WHERE d.agent = :agent"),
  @NamedQuery(name = "DrugTracker.findByOriginator", query = "SELECT d FROM DrugTracker d WHERE d.originator = :originator"),
  @NamedQuery(name = "DrugTracker.findByCas", query = "SELECT d FROM DrugTracker d WHERE d.cas = :cas")})
public class DrugTracker implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "agent", length = 1024)
  private String agent;
  @Column(name = "originator", length = 1024)
  private String originator;
  @Column(name = "cas", length = 1024)
  private String cas;
  @JoinTable(name = "drug_tracker_plates2drug_track", joinColumns = {
    @JoinColumn(name = "drug_trackers_fk", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
    @JoinColumn(name = "drug_tracker_plates_fk", referencedColumnName = "id", nullable = false)})
  @ManyToMany
  private Collection<DrugTrackerPlate> drugTrackerPlateCollection;
  @JoinTable(name = "drug_tracker_sets2drug_tracker", joinColumns = {
    @JoinColumn(name = "drug_trackers_fk", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
    @JoinColumn(name = "drug_tracker_sets_fk", referencedColumnName = "id", nullable = false)})
  @ManyToMany
  private Collection<DrugTrackerSet> drugTrackerSetCollection;
  @JoinTable(name = "drug_tracker_targets2drug_trac", joinColumns = {
    @JoinColumn(name = "drug_trackers_fk", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
    @JoinColumn(name = "drug_tracker_targets_fk", referencedColumnName = "id", nullable = false)})
  @ManyToMany
  private Collection<DrugTrackerTarget> drugTrackerTargetCollection;
  @JoinTable(name = "drug_tracker_aliases2drug_trac", joinColumns = {
    @JoinColumn(name = "drug_trackers_fk", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
    @JoinColumn(name = "drug_tracker_aliases_fk", referencedColumnName = "id", nullable = false)})
  @ManyToMany
  private Collection<DrugTrackerAlias> drugTrackerAliasCollection;
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "drugTracker")
  private NscDrugTracker nscDrugTracker;
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "drugTracker")
  private AdHocDrugTracker adHocDrugTracker;

  public DrugTracker() {
  }

  public DrugTracker(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAgent() {
    return agent;
  }

  public void setAgent(String agent) {
    this.agent = agent;
  }

  public String getOriginator() {
    return originator;
  }

  public void setOriginator(String originator) {
    this.originator = originator;
  }

  public String getCas() {
    return cas;
  }

  public void setCas(String cas) {
    this.cas = cas;
  }

  @XmlTransient
  public Collection<DrugTrackerPlate> getDrugTrackerPlateCollection() {
    return drugTrackerPlateCollection;
  }

  public void setDrugTrackerPlateCollection(Collection<DrugTrackerPlate> drugTrackerPlateCollection) {
    this.drugTrackerPlateCollection = drugTrackerPlateCollection;
  }

  @XmlTransient
  public Collection<DrugTrackerSet> getDrugTrackerSetCollection() {
    return drugTrackerSetCollection;
  }

  public void setDrugTrackerSetCollection(Collection<DrugTrackerSet> drugTrackerSetCollection) {
    this.drugTrackerSetCollection = drugTrackerSetCollection;
  }

  @XmlTransient
  public Collection<DrugTrackerTarget> getDrugTrackerTargetCollection() {
    return drugTrackerTargetCollection;
  }

  public void setDrugTrackerTargetCollection(Collection<DrugTrackerTarget> drugTrackerTargetCollection) {
    this.drugTrackerTargetCollection = drugTrackerTargetCollection;
  }

  @XmlTransient
  public Collection<DrugTrackerAlias> getDrugTrackerAliasCollection() {
    return drugTrackerAliasCollection;
  }

  public void setDrugTrackerAliasCollection(Collection<DrugTrackerAlias> drugTrackerAliasCollection) {
    this.drugTrackerAliasCollection = drugTrackerAliasCollection;
  }

  public NscDrugTracker getNscDrugTracker() {
    return nscDrugTracker;
  }

  public void setNscDrugTracker(NscDrugTracker nscDrugTracker) {
    this.nscDrugTracker = nscDrugTracker;
  }

  public AdHocDrugTracker getAdHocDrugTracker() {
    return adHocDrugTracker;
  }

  public void setAdHocDrugTracker(AdHocDrugTracker adHocDrugTracker) {
    this.adHocDrugTracker = adHocDrugTracker;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof DrugTracker)) {
      return false;
    }
    DrugTracker other = (DrugTracker) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "jpa.entity.DrugTracker[ id=" + id + " ]";
  }
  
}
