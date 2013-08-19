/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structureservlet;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mwkunkel
 */
@Entity
@Table(name = "structure")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Structure.findAll", query = "SELECT s FROM Structure s"),
    @NamedQuery(name = "Structure.findById", query = "SELECT s FROM Structure s WHERE s.id = :id"),
    @NamedQuery(name = "Structure.findByNsc", query = "SELECT s FROM Structure s WHERE s.nsc = :nsc"),
    @NamedQuery(name = "Structure.findBySmiles", query = "SELECT s FROM Structure s WHERE s.smiles = :smiles")})
public class Structure implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "nsc")
    private Integer nsc;
    @Column(name = "smiles")
    private String smiles;

    public Structure() {
    }

    public Structure(Long id) {
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

    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = smiles;
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
        if (!(object instanceof Structure)) {
            return false;
        }
        Structure other = (Structure) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "structureservlet.Structure[ id=" + id + " ]";
    }
    
}
