/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mwkunkel
 */
@Entity
@Table(name = "nsc_drug_tracker")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "NscDrugTracker.findAll", query = "SELECT n FROM NscDrugTracker n"),
  @NamedQuery(name = "NscDrugTracker.findById", query = "SELECT n FROM NscDrugTracker n WHERE n.id = :id"),
  @NamedQuery(name = "NscDrugTracker.findByNsc", query = "SELECT n FROM NscDrugTracker n WHERE n.nsc = :nsc"),
  @NamedQuery(name = "NscDrugTracker.findByPrefix", query = "SELECT n FROM NscDrugTracker n WHERE n.prefix = :prefix")})
public class NscDrugTracker implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "nsc")
  private Integer nsc;
  @Column(name = "prefix", length = 1024)
  private String prefix;
  @JoinColumn(name = "standardized_smiles_fk", referencedColumnName = "id")
  @ManyToOne
  private StandardizedSmiles standardizedSmilesFk;
  @JoinColumn(name = "id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  @OneToOne(optional = false)
  private DrugTracker drugTracker;

  public NscDrugTracker() {
  }

  public NscDrugTracker(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getNsc() {
    return nsc;
  }

  public void setNsc(Integer nsc) {
    this.nsc = nsc;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public StandardizedSmiles getStandardizedSmilesFk() {
    return standardizedSmilesFk;
  }

  public void setStandardizedSmilesFk(StandardizedSmiles standardizedSmilesFk) {
    this.standardizedSmilesFk = standardizedSmilesFk;
  }

  public DrugTracker getDrugTracker() {
    return drugTracker;
  }

  public void setDrugTracker(DrugTracker drugTracker) {
    this.drugTracker = drugTracker;
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
    if (!(object instanceof NscDrugTracker)) {
      return false;
    }
    NscDrugTracker other = (NscDrugTracker) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "jpa.entity.NscDrugTracker[ id=" + id + " ]";
  }
  
}
