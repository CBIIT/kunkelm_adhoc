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
@Table(name = "drug_tracker_plate")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "DrugTrackerPlate.findAll", query = "SELECT d FROM DrugTrackerPlate d"),
  @NamedQuery(name = "DrugTrackerPlate.findById", query = "SELECT d FROM DrugTrackerPlate d WHERE d.id = :id"),
  @NamedQuery(name = "DrugTrackerPlate.findByPlateName", query = "SELECT d FROM DrugTrackerPlate d WHERE d.plateName = :plateName")})
public class DrugTrackerPlate implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "id", nullable = false)
  private Long id;
  @Basic(optional = false)
  @Column(name = "plate_name", nullable = false, length = 1024)
  private String plateName;
  @ManyToMany(mappedBy = "drugTrackerPlateCollection")
  private Collection<DrugTracker> drugTrackerCollection;

  public DrugTrackerPlate() {
  }

  public DrugTrackerPlate(Long id) {
    this.id = id;
  }

  public DrugTrackerPlate(Long id, String plateName) {
    this.id = id;
    this.plateName = plateName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPlateName() {
    return plateName;
  }

  public void setPlateName(String plateName) {
    this.plateName = plateName;
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
    if (!(object instanceof DrugTrackerPlate)) {
      return false;
    }
    DrugTrackerPlate other = (DrugTrackerPlate) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "jpa.entity.DrugTrackerPlate[ id=" + id + " ]";
  }
  
}
