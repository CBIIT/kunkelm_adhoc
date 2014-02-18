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
@Table(name = "ad_hoc_drug_tracker")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AdHocDrugTracker.findAll", query = "SELECT a FROM AdHocDrugTracker a"),
  @NamedQuery(name = "AdHocDrugTracker.findById", query = "SELECT a FROM AdHocDrugTracker a WHERE a.id = :id"),
  @NamedQuery(name = "AdHocDrugTracker.findByStructureSource", query = "SELECT a FROM AdHocDrugTracker a WHERE a.structureSource = :structureSource"),
  @NamedQuery(name = "AdHocDrugTracker.findBySmiles", query = "SELECT a FROM AdHocDrugTracker a WHERE a.smiles = :smiles"),
  @NamedQuery(name = "AdHocDrugTracker.findByRemovedSalts", query = "SELECT a FROM AdHocDrugTracker a WHERE a.removedSalts = :removedSalts"),
  @NamedQuery(name = "AdHocDrugTracker.findByMw", query = "SELECT a FROM AdHocDrugTracker a WHERE a.mw = :mw"),
  @NamedQuery(name = "AdHocDrugTracker.findByMf", query = "SELECT a FROM AdHocDrugTracker a WHERE a.mf = :mf"),
  @NamedQuery(name = "AdHocDrugTracker.findByAlogp", query = "SELECT a FROM AdHocDrugTracker a WHERE a.alogp = :alogp"),
  @NamedQuery(name = "AdHocDrugTracker.findByLogd", query = "SELECT a FROM AdHocDrugTracker a WHERE a.logd = :logd"),
  @NamedQuery(name = "AdHocDrugTracker.findByHba", query = "SELECT a FROM AdHocDrugTracker a WHERE a.hba = :hba"),
  @NamedQuery(name = "AdHocDrugTracker.findByHbd", query = "SELECT a FROM AdHocDrugTracker a WHERE a.hbd = :hbd"),
  @NamedQuery(name = "AdHocDrugTracker.findBySa", query = "SELECT a FROM AdHocDrugTracker a WHERE a.sa = :sa"),
  @NamedQuery(name = "AdHocDrugTracker.findByPsa", query = "SELECT a FROM AdHocDrugTracker a WHERE a.psa = :psa")})
public class AdHocDrugTracker implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "structure_source", length = 1024)
  private String structureSource;
  @Column(name = "smiles", length = 1024)
  private String smiles;
  @Column(name = "removed_salts", length = 1024)
  private String removedSalts;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "mw", precision = 17, scale = 17)
  private Double mw;
  @Column(name = "mf", length = 1024)
  private String mf;
  @Column(name = "alogp", precision = 17, scale = 17)
  private Double alogp;
  @Column(name = "logd", precision = 17, scale = 17)
  private Double logd;
  @Column(name = "hba")
  private Integer hba;
  @Column(name = "hbd")
  private Integer hbd;
  @Column(name = "sa", precision = 17, scale = 17)
  private Double sa;
  @Column(name = "psa", precision = 17, scale = 17)
  private Double psa;
  @JoinColumn(name = "id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
  @OneToOne(optional = false)
  private DrugTracker drugTracker;

  public AdHocDrugTracker() {
  }

  public AdHocDrugTracker(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStructureSource() {
    return structureSource;
  }

  public void setStructureSource(String structureSource) {
    this.structureSource = structureSource;
  }

  public String getSmiles() {
    return smiles;
  }

  public void setSmiles(String smiles) {
    this.smiles = smiles;
  }

  public String getRemovedSalts() {
    return removedSalts;
  }

  public void setRemovedSalts(String removedSalts) {
    this.removedSalts = removedSalts;
  }

  public Double getMw() {
    return mw;
  }

  public void setMw(Double mw) {
    this.mw = mw;
  }

  public String getMf() {
    return mf;
  }

  public void setMf(String mf) {
    this.mf = mf;
  }

  public Double getAlogp() {
    return alogp;
  }

  public void setAlogp(Double alogp) {
    this.alogp = alogp;
  }

  public Double getLogd() {
    return logd;
  }

  public void setLogd(Double logd) {
    this.logd = logd;
  }

  public Integer getHba() {
    return hba;
  }

  public void setHba(Integer hba) {
    this.hba = hba;
  }

  public Integer getHbd() {
    return hbd;
  }

  public void setHbd(Integer hbd) {
    this.hbd = hbd;
  }

  public Double getSa() {
    return sa;
  }

  public void setSa(Double sa) {
    this.sa = sa;
  }

  public Double getPsa() {
    return psa;
  }

  public void setPsa(Double psa) {
    this.psa = psa;
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
    if (!(object instanceof AdHocDrugTracker)) {
      return false;
    }
    AdHocDrugTracker other = (AdHocDrugTracker) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "jpa.entity.AdHocDrugTracker[ id=" + id + " ]";
  }
  
}
