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
@Table(name = "drug_tracker_set")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "DrugTrackerSet.findAll", query = "SELECT d FROM DrugTrackerSet d"),
  @NamedQuery(name = "DrugTrackerSet.findById", query = "SELECT d FROM DrugTrackerSet d WHERE d.id = :id"),
  @NamedQuery(name = "DrugTrackerSet.findBySetName", query = "SELECT d FROM DrugTrackerSet d WHERE d.setName = :setName")})
public class DrugTrackerSet implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "id", nullable = false)
  private Long id;
  @Basic(optional = false)
  @Column(name = "set_name", nullable = false, length = 1024)
  private String setName;
  @ManyToMany(mappedBy = "drugTrackerSetCollection")
  private Collection<DrugTracker> drugTrackerCollection;

  public DrugTrackerSet() {
  }

  public DrugTrackerSet(Long id) {
    this.id = id;
  }

  public DrugTrackerSet(Long id, String setName) {
    this.id = id;
    this.setName = setName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSetName() {
    return setName;
  }

  public void setSetName(String setName) {
    this.setName = setName;
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
    if (!(object instanceof DrugTrackerSet)) {
      return false;
    }
    DrugTrackerSet other = (DrugTrackerSet) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "jpa.entity.DrugTrackerSet[ id=" + id + " ]";
  }
  
}
