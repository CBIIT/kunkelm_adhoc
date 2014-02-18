/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mwkunkel
 */
@Entity
@Table(name = "drug_tracker_target")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "DrugTrackerTarget.findAll", query = "SELECT d FROM DrugTrackerTarget d"),
  @NamedQuery(name = "DrugTrackerTarget.findById", query = "SELECT d FROM DrugTrackerTarget d WHERE d.id = :id"),
  @NamedQuery(name = "DrugTrackerTarget.findByTarget", query = "SELECT d FROM DrugTrackerTarget d WHERE d.target = :target")})
public class DrugTrackerTarget implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "id", nullable = false)
  private Long id;
  @Basic(optional = false)
  @Column(name = "target", nullable = false, length = 1024)
  private String target;
  @ManyToMany(mappedBy = "drugTrackerTargetCollection")
  private Collection<DrugTracker> drugTrackerCollection;

  public DrugTrackerTarget() {
  }

  public DrugTrackerTarget(Long id) {
    this.id = id;
  }

  public DrugTrackerTarget(Long id, String target) {
    this.id = id;
    this.target = target;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  @XmlTransient
  public Collection<DrugTracker> getDrugTrackerCollection() {
    return drugTrackerCollection;
  }

  public void setDrugTrackerCollection(Collection<DrugTracker> drugTrackerCollection) {
    this.drugTrackerCollection = drugTrackerCollection;
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
    if (!(object instanceof DrugTrackerTarget)) {
      return false;
    }
    DrugTrackerTarget other = (DrugTrackerTarget) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "jpa.entity.DrugTrackerTarget[ id=" + id + " ]";
  }
  
}
