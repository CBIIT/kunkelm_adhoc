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
@Table(name = "drug_tracker_alias")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "DrugTrackerAlias.findAll", query = "SELECT d FROM DrugTrackerAlias d"),
  @NamedQuery(name = "DrugTrackerAlias.findById", query = "SELECT d FROM DrugTrackerAlias d WHERE d.id = :id"),
  @NamedQuery(name = "DrugTrackerAlias.findByAlias", query = "SELECT d FROM DrugTrackerAlias d WHERE d.alias = :alias")})
public class DrugTrackerAlias implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "id", nullable = false)
  private Long id;
  @Basic(optional = false)
  @Column(name = "alias", nullable = false, length = 1024)
  private String alias;
  @ManyToMany(mappedBy = "drugTrackerAliasCollection")
  private Collection<DrugTracker> drugTrackerCollection;

  public DrugTrackerAlias() {
  }

  public DrugTrackerAlias(Long id) {
    this.id = id;
  }

  public DrugTrackerAlias(Long id, String alias) {
    this.id = id;
    this.alias = alias;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
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
    if (!(object instanceof DrugTrackerAlias)) {
      return false;
    }
    DrugTrackerAlias other = (DrugTrackerAlias) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "jpa.entity.DrugTrackerAlias[ id=" + id + " ]";
  }
  
}
