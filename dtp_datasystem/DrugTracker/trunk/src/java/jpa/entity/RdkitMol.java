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
@Table(name = "rdkit_mol")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "RdkitMol.findAll", query = "SELECT r FROM RdkitMol r"),
  @NamedQuery(name = "RdkitMol.findById", query = "SELECT r FROM RdkitMol r WHERE r.id = :id"),
  @NamedQuery(name = "RdkitMol.findByNsc", query = "SELECT r FROM RdkitMol r WHERE r.nsc = :nsc"),
  @NamedQuery(name = "RdkitMol.findByMol", query = "SELECT r FROM RdkitMol r WHERE r.mol = :mol")})
public class RdkitMol implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "nsc")
  private Integer nsc;
  @Basic(optional = false)
  @Column(name = "mol", nullable = false, length = 1024)
  private String mol;
  @OneToOne(mappedBy = "rdkitMolFk")
  private StandardizedSmiles standardizedSmiles;

  public RdkitMol() {
  }

  public RdkitMol(Long id) {
    this.id = id;
  }

  public RdkitMol(Long id, String mol) {
    this.id = id;
    this.mol = mol;
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

  public String getMol() {
    return mol;
  }

  public void setMol(String mol) {
    this.mol = mol;
  }

  public StandardizedSmiles getStandardizedSmiles() {
    return standardizedSmiles;
  }

  public void setStandardizedSmiles(StandardizedSmiles standardizedSmiles) {
    this.standardizedSmiles = standardizedSmiles;
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
    if (!(object instanceof RdkitMol)) {
      return false;
    }
    RdkitMol other = (RdkitMol) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "jpa.entity.RdkitMol[ id=" + id + " ]";
  }
  
}
