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
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mwkunkel
 */
@Entity
@Table(name = "standardized_smiles", uniqueConstraints = {
  @UniqueConstraint(columnNames = {"rdkit_mol_fk"})})
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "StandardizedSmiles.findAll", query = "SELECT s FROM StandardizedSmiles s"),
  @NamedQuery(name = "StandardizedSmiles.findById", query = "SELECT s FROM StandardizedSmiles s WHERE s.id = :id"),
  @NamedQuery(name = "StandardizedSmiles.findByName", query = "SELECT s FROM StandardizedSmiles s WHERE s.name = :name"),
  @NamedQuery(name = "StandardizedSmiles.findByNsc", query = "SELECT s FROM StandardizedSmiles s WHERE s.nsc = :nsc"),
  @NamedQuery(name = "StandardizedSmiles.findByStructureSource", query = "SELECT s FROM StandardizedSmiles s WHERE s.structureSource = :structureSource"),
  @NamedQuery(name = "StandardizedSmiles.findBySmilesFromProd", query = "SELECT s FROM StandardizedSmiles s WHERE s.smilesFromProd = :smilesFromProd"),
  @NamedQuery(name = "StandardizedSmiles.findByProdCanSmi", query = "SELECT s FROM StandardizedSmiles s WHERE s.prodCanSmi = :prodCanSmi"),
  @NamedQuery(name = "StandardizedSmiles.findByRemovedSalts", query = "SELECT s FROM StandardizedSmiles s WHERE s.removedSalts = :removedSalts"),
  @NamedQuery(name = "StandardizedSmiles.findByParentCanSmi", query = "SELECT s FROM StandardizedSmiles s WHERE s.parentCanSmi = :parentCanSmi"),
  @NamedQuery(name = "StandardizedSmiles.findByTautCanSmi", query = "SELECT s FROM StandardizedSmiles s WHERE s.tautCanSmi = :tautCanSmi"),
  @NamedQuery(name = "StandardizedSmiles.findByTautNostereoCanSmi", query = "SELECT s FROM StandardizedSmiles s WHERE s.tautNostereoCanSmi = :tautNostereoCanSmi"),
  @NamedQuery(name = "StandardizedSmiles.findByXeno", query = "SELECT s FROM StandardizedSmiles s WHERE s.xeno = :xeno"),
  @NamedQuery(name = "StandardizedSmiles.findByHf", query = "SELECT s FROM StandardizedSmiles s WHERE s.hf = :hf"),
  @NamedQuery(name = "StandardizedSmiles.findByNci60", query = "SELECT s FROM StandardizedSmiles s WHERE s.nci60 = :nci60"),
  @NamedQuery(name = "StandardizedSmiles.findByDistribution", query = "SELECT s FROM StandardizedSmiles s WHERE s.distribution = :distribution"),
  @NamedQuery(name = "StandardizedSmiles.findByConf", query = "SELECT s FROM StandardizedSmiles s WHERE s.conf = :conf"),
  @NamedQuery(name = "StandardizedSmiles.findByInventory", query = "SELECT s FROM StandardizedSmiles s WHERE s.inventory = :inventory"),
  @NamedQuery(name = "StandardizedSmiles.findByMw", query = "SELECT s FROM StandardizedSmiles s WHERE s.mw = :mw"),
  @NamedQuery(name = "StandardizedSmiles.findByMf", query = "SELECT s FROM StandardizedSmiles s WHERE s.mf = :mf"),
  @NamedQuery(name = "StandardizedSmiles.findByAlogp", query = "SELECT s FROM StandardizedSmiles s WHERE s.alogp = :alogp"),
  @NamedQuery(name = "StandardizedSmiles.findByLogd", query = "SELECT s FROM StandardizedSmiles s WHERE s.logd = :logd"),
  @NamedQuery(name = "StandardizedSmiles.findByHba", query = "SELECT s FROM StandardizedSmiles s WHERE s.hba = :hba"),
  @NamedQuery(name = "StandardizedSmiles.findByHbd", query = "SELECT s FROM StandardizedSmiles s WHERE s.hbd = :hbd"),
  @NamedQuery(name = "StandardizedSmiles.findBySa", query = "SELECT s FROM StandardizedSmiles s WHERE s.sa = :sa"),
  @NamedQuery(name = "StandardizedSmiles.findByPsa", query = "SELECT s FROM StandardizedSmiles s WHERE s.psa = :psa"),
  @NamedQuery(name = "StandardizedSmiles.findByCas", query = "SELECT s FROM StandardizedSmiles s WHERE s.cas = :cas"),
  @NamedQuery(name = "StandardizedSmiles.findByPrefix", query = "SELECT s FROM StandardizedSmiles s WHERE s.prefix = :prefix"),
  @NamedQuery(name = "StandardizedSmiles.findByCountRelated", query = "SELECT s FROM StandardizedSmiles s WHERE s.countRelated = :countRelated"),
  @NamedQuery(name = "StandardizedSmiles.findByRelated", query = "SELECT s FROM StandardizedSmiles s WHERE s.related = :related")})
public class StandardizedSmiles implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "name", length = 1024)
  private String name;
  @Column(name = "nsc")
  private Integer nsc;
  @Column(name = "structure_source", length = 1024)
  private String structureSource;
  @Column(name = "smiles_from_prod", length = 1024)
  private String smilesFromProd;
  @Column(name = "prod_can_smi", length = 1024)
  private String prodCanSmi;
  @Column(name = "removed_salts", length = 1024)
  private String removedSalts;
  @Column(name = "parent_can_smi", length = 1024)
  private String parentCanSmi;
  @Column(name = "taut_can_smi", length = 1024)
  private String tautCanSmi;
  @Column(name = "taut_nostereo_can_smi", length = 1024)
  private String tautNostereoCanSmi;
  @Column(name = "xeno")
  private Integer xeno;
  @Column(name = "hf")
  private Integer hf;
  @Column(name = "nci60")
  private Integer nci60;
  @Column(name = "distribution", length = 1024)
  private String distribution;
  @Column(name = "conf", length = 1024)
  private String conf;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "inventory", precision = 17, scale = 17)
  private Double inventory;
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
  @Column(name = "cas", length = 1024)
  private String cas;
  @Basic(optional = false)
  @Column(name = "prefix", nullable = false, length = 1024)
  private String prefix;
  @Column(name = "count_related")
  private Integer countRelated;
  @Column(name = "related", length = 1024)
  private String related;
  @OneToMany(mappedBy = "standardizedSmilesFk")
  private Collection<NscDrugTracker> nscDrugTrackerCollection;
  @JoinColumn(name = "rdkit_mol_fk", referencedColumnName = "id")
  @OneToOne
  private RdkitMol rdkitMolFk;

  public StandardizedSmiles() {
  }

  public StandardizedSmiles(Long id) {
    this.id = id;
  }

  public StandardizedSmiles(Long id, String prefix) {
    this.id = id;
    this.prefix = prefix;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getNsc() {
    return nsc;
  }

  public void setNsc(Integer nsc) {
    this.nsc = nsc;
  }

  public String getStructureSource() {
    return structureSource;
  }

  public void setStructureSource(String structureSource) {
    this.structureSource = structureSource;
  }

  public String getSmilesFromProd() {
    return smilesFromProd;
  }

  public void setSmilesFromProd(String smilesFromProd) {
    this.smilesFromProd = smilesFromProd;
  }

  public String getProdCanSmi() {
    return prodCanSmi;
  }

  public void setProdCanSmi(String prodCanSmi) {
    this.prodCanSmi = prodCanSmi;
  }

  public String getRemovedSalts() {
    return removedSalts;
  }

  public void setRemovedSalts(String removedSalts) {
    this.removedSalts = removedSalts;
  }

  public String getParentCanSmi() {
    return parentCanSmi;
  }

  public void setParentCanSmi(String parentCanSmi) {
    this.parentCanSmi = parentCanSmi;
  }

  public String getTautCanSmi() {
    return tautCanSmi;
  }

  public void setTautCanSmi(String tautCanSmi) {
    this.tautCanSmi = tautCanSmi;
  }

  public String getTautNostereoCanSmi() {
    return tautNostereoCanSmi;
  }

  public void setTautNostereoCanSmi(String tautNostereoCanSmi) {
    this.tautNostereoCanSmi = tautNostereoCanSmi;
  }

  public Integer getXeno() {
    return xeno;
  }

  public void setXeno(Integer xeno) {
    this.xeno = xeno;
  }

  public Integer getHf() {
    return hf;
  }

  public void setHf(Integer hf) {
    this.hf = hf;
  }

  public Integer getNci60() {
    return nci60;
  }

  public void setNci60(Integer nci60) {
    this.nci60 = nci60;
  }

  public String getDistribution() {
    return distribution;
  }

  public void setDistribution(String distribution) {
    this.distribution = distribution;
  }

  public String getConf() {
    return conf;
  }

  public void setConf(String conf) {
    this.conf = conf;
  }

  public Double getInventory() {
    return inventory;
  }

  public void setInventory(Double inventory) {
    this.inventory = inventory;
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

  public String getCas() {
    return cas;
  }

  public void setCas(String cas) {
    this.cas = cas;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public Integer getCountRelated() {
    return countRelated;
  }

  public void setCountRelated(Integer countRelated) {
    this.countRelated = countRelated;
  }

  public String getRelated() {
    return related;
  }

  public void setRelated(String related) {
    this.related = related;
  }

  @XmlTransient
  public Collection<NscDrugTracker> getNscDrugTrackerCollection() {
    return nscDrugTrackerCollection;
  }

  public void setNscDrugTrackerCollection(Collection<NscDrugTracker> nscDrugTrackerCollection) {
    this.nscDrugTrackerCollection = nscDrugTrackerCollection;
  }

  public RdkitMol getRdkitMolFk() {
    return rdkitMolFk;
  }

  public void setRdkitMolFk(RdkitMol rdkitMolFk) {
    this.rdkitMolFk = rdkitMolFk;
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
    if (!(object instanceof StandardizedSmiles)) {
      return false;
    }
    StandardizedSmiles other = (StandardizedSmiles) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "jpa.entity.StandardizedSmiles[ id=" + id + " ]";
  }
  
}
