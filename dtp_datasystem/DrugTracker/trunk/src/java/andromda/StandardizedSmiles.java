// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: hibernate/HibernateEntity.vsl in andromda-hibernate-cartridge.
//
package andromda;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * 
 */
// HibernateEntity.vsl annotations merge-point
public abstract class StandardizedSmiles
    implements Serializable, Comparable<StandardizedSmiles>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 8480367146256011390L;

    // Generate 28 attributes
    private String name;

    /**
     * 
     * @return this.name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * 
     * @param nameIn String
     */
    public void setName(String nameIn)
    {
        this.name = nameIn;
    }

    private Integer nsc;

    /**
     * 
     * @return this.nsc Integer
     */
    public Integer getNsc()
    {
        return this.nsc;
    }

    /**
     * 
     * @param nscIn Integer
     */
    public void setNsc(Integer nscIn)
    {
        this.nsc = nscIn;
    }

    private String structureSource;

    /**
     * 
     * @return this.structureSource String
     */
    public String getStructureSource()
    {
        return this.structureSource;
    }

    /**
     * 
     * @param structureSourceIn String
     */
    public void setStructureSource(String structureSourceIn)
    {
        this.structureSource = structureSourceIn;
    }

    private String smilesFromProd;

    /**
     * 
     * @return this.smilesFromProd String
     */
    public String getSmilesFromProd()
    {
        return this.smilesFromProd;
    }

    /**
     * 
     * @param smilesFromProdIn String
     */
    public void setSmilesFromProd(String smilesFromProdIn)
    {
        this.smilesFromProd = smilesFromProdIn;
    }

    private String prodCanSmi;

    /**
     * 
     * @return this.prodCanSmi String
     */
    public String getProdCanSmi()
    {
        return this.prodCanSmi;
    }

    /**
     * 
     * @param prodCanSmiIn String
     */
    public void setProdCanSmi(String prodCanSmiIn)
    {
        this.prodCanSmi = prodCanSmiIn;
    }

    private String removedSalts;

    /**
     * 
     * @return this.removedSalts String
     */
    public String getRemovedSalts()
    {
        return this.removedSalts;
    }

    /**
     * 
     * @param removedSaltsIn String
     */
    public void setRemovedSalts(String removedSaltsIn)
    {
        this.removedSalts = removedSaltsIn;
    }

    private String parentCanSmi;

    /**
     * 
     * @return this.parentCanSmi String
     */
    public String getParentCanSmi()
    {
        return this.parentCanSmi;
    }

    /**
     * 
     * @param parentCanSmiIn String
     */
    public void setParentCanSmi(String parentCanSmiIn)
    {
        this.parentCanSmi = parentCanSmiIn;
    }

    private String tautCanSmi;

    /**
     * 
     * @return this.tautCanSmi String
     */
    public String getTautCanSmi()
    {
        return this.tautCanSmi;
    }

    /**
     * 
     * @param tautCanSmiIn String
     */
    public void setTautCanSmi(String tautCanSmiIn)
    {
        this.tautCanSmi = tautCanSmiIn;
    }

    private String tautNostereoCanSmi;

    /**
     * 
     * @return this.tautNostereoCanSmi String
     */
    public String getTautNostereoCanSmi()
    {
        return this.tautNostereoCanSmi;
    }

    /**
     * 
     * @param tautNostereoCanSmiIn String
     */
    public void setTautNostereoCanSmi(String tautNostereoCanSmiIn)
    {
        this.tautNostereoCanSmi = tautNostereoCanSmiIn;
    }

    private Integer xeno;

    /**
     * 
     * @return this.xeno Integer
     */
    public Integer getXeno()
    {
        return this.xeno;
    }

    /**
     * 
     * @param xenoIn Integer
     */
    public void setXeno(Integer xenoIn)
    {
        this.xeno = xenoIn;
    }

    private Integer hf;

    /**
     * 
     * @return this.hf Integer
     */
    public Integer getHf()
    {
        return this.hf;
    }

    /**
     * 
     * @param hfIn Integer
     */
    public void setHf(Integer hfIn)
    {
        this.hf = hfIn;
    }

    private Integer nci60;

    /**
     * 
     * @return this.nci60 Integer
     */
    public Integer getNci60()
    {
        return this.nci60;
    }

    /**
     * 
     * @param nci60In Integer
     */
    public void setNci60(Integer nci60In)
    {
        this.nci60 = nci60In;
    }

    private String distribution;

    /**
     * 
     * @return this.distribution String
     */
    public String getDistribution()
    {
        return this.distribution;
    }

    /**
     * 
     * @param distributionIn String
     */
    public void setDistribution(String distributionIn)
    {
        this.distribution = distributionIn;
    }

    private String conf;

    /**
     * 
     * @return this.conf String
     */
    public String getConf()
    {
        return this.conf;
    }

    /**
     * 
     * @param confIn String
     */
    public void setConf(String confIn)
    {
        this.conf = confIn;
    }

    private Double inventory;

    /**
     * 
     * @return this.inventory Double
     */
    public Double getInventory()
    {
        return this.inventory;
    }

    /**
     * 
     * @param inventoryIn Double
     */
    public void setInventory(Double inventoryIn)
    {
        this.inventory = inventoryIn;
    }

    private Double mw;

    /**
     * 
     * @return this.mw Double
     */
    public Double getMw()
    {
        return this.mw;
    }

    /**
     * 
     * @param mwIn Double
     */
    public void setMw(Double mwIn)
    {
        this.mw = mwIn;
    }

    private String mf;

    /**
     * 
     * @return this.mf String
     */
    public String getMf()
    {
        return this.mf;
    }

    /**
     * 
     * @param mfIn String
     */
    public void setMf(String mfIn)
    {
        this.mf = mfIn;
    }

    private Double alogp;

    /**
     * 
     * @return this.alogp Double
     */
    public Double getAlogp()
    {
        return this.alogp;
    }

    /**
     * 
     * @param alogpIn Double
     */
    public void setAlogp(Double alogpIn)
    {
        this.alogp = alogpIn;
    }

    private Double logd;

    /**
     * 
     * @return this.logd Double
     */
    public Double getLogd()
    {
        return this.logd;
    }

    /**
     * 
     * @param logdIn Double
     */
    public void setLogd(Double logdIn)
    {
        this.logd = logdIn;
    }

    private Integer hba;

    /**
     * 
     * @return this.hba Integer
     */
    public Integer getHba()
    {
        return this.hba;
    }

    /**
     * 
     * @param hbaIn Integer
     */
    public void setHba(Integer hbaIn)
    {
        this.hba = hbaIn;
    }

    private Integer hbd;

    /**
     * 
     * @return this.hbd Integer
     */
    public Integer getHbd()
    {
        return this.hbd;
    }

    /**
     * 
     * @param hbdIn Integer
     */
    public void setHbd(Integer hbdIn)
    {
        this.hbd = hbdIn;
    }

    private Double sa;

    /**
     * 
     * @return this.sa Double
     */
    public Double getSa()
    {
        return this.sa;
    }

    /**
     * 
     * @param saIn Double
     */
    public void setSa(Double saIn)
    {
        this.sa = saIn;
    }

    private Double psa;

    /**
     * 
     * @return this.psa Double
     */
    public Double getPsa()
    {
        return this.psa;
    }

    /**
     * 
     * @param psaIn Double
     */
    public void setPsa(Double psaIn)
    {
        this.psa = psaIn;
    }

    private String cas;

    /**
     * 
     * @return this.cas String
     */
    public String getCas()
    {
        return this.cas;
    }

    /**
     * 
     * @param casIn String
     */
    public void setCas(String casIn)
    {
        this.cas = casIn;
    }

    private String prefix;

    /**
     * 
     * @return this.prefix String
     */
    public String getPrefix()
    {
        return this.prefix;
    }

    /**
     * 
     * @param prefixIn String
     */
    public void setPrefix(String prefixIn)
    {
        this.prefix = prefixIn;
    }

    private Integer countRelated;

    /**
     * 
     * @return this.countRelated Integer
     */
    public Integer getCountRelated()
    {
        return this.countRelated;
    }

    /**
     * 
     * @param countRelatedIn Integer
     */
    public void setCountRelated(Integer countRelatedIn)
    {
        this.countRelated = countRelatedIn;
    }

    private String related;

    /**
     * 
     * @return this.related String
     */
    public String getRelated()
    {
        return this.related;
    }

    /**
     * 
     * @param relatedIn String
     */
    public void setRelated(String relatedIn)
    {
        this.related = relatedIn;
    }

    private Long id;

    /**
     * 
     * @return this.id Long
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * 
     * @param idIn Long
     */
    public void setId(Long idIn)
    {
        this.id = idIn;
    }

    // Generate 2 associations
    private RdkitMol rdkitMol;

    /**
     * 
     * @return this.rdkitMol RdkitMol
     */
    public RdkitMol getRdkitMol()
    {
        return this.rdkitMol;
    }

    /**
     * 
     * @param rdkitMolIn RdkitMol
     */
    public void setRdkitMol(RdkitMol rdkitMolIn)
    {
        this.rdkitMol = rdkitMolIn;
    }

    private Collection<NscDrugTracker> nscDrugTrackers = new HashSet<NscDrugTracker>();

    /**
     * 
     * @return this.nscDrugTrackers Collection<NscDrugTracker>
     */
    public Collection<NscDrugTracker> getNscDrugTrackers()
    {
        return this.nscDrugTrackers;
    }

    /**
     * 
     * @param nscDrugTrackersIn Collection<NscDrugTracker>
     */
    public void setNscDrugTrackers(Collection<NscDrugTracker> nscDrugTrackersIn)
    {
        this.nscDrugTrackers = nscDrugTrackersIn;
    }

    /**
     * 
     * @param elementToAdd NscDrugTracker
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean addNscDrugTrackers(NscDrugTracker elementToAdd)
    {
        return this.nscDrugTrackers.add(elementToAdd);
    }

    /**
     * 
     * @param elementToRemove NscDrugTracker
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     */
    public boolean removeNscDrugTrackers(NscDrugTracker elementToRemove)
    {
        return this.nscDrugTrackers.remove(elementToRemove);
    }

    /**
     * Returns <code>true</code> if the argument is an StandardizedSmiles instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof StandardizedSmiles))
        {
            return false;
        }
        final StandardizedSmiles that = (StandardizedSmiles)object;
        if (this.id == null || that.getId() == null || !this.id.equals(that.getId()))
        {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers.
     */
    @Override
    public int hashCode()
    {
        int hashCode = 0;
        hashCode = 29 * hashCode + (this.id == null ? 0 : this.id.hashCode());

        return hashCode;
    }

    /**
     * Constructs new instances of {@link StandardizedSmiles}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link StandardizedSmiles}.
         * @return new StandardizedSmilesImpl()
         */
        public static StandardizedSmiles newInstance()
        {
            return new StandardizedSmilesImpl();
        }

        /**
         * Constructs a new instance of {@link StandardizedSmiles}, taking all required and/or
         * read-only properties as arguments, except for identifiers.
         * @param prefix String
         * @return newInstance
         */
        public static StandardizedSmiles newInstance(String prefix)
        {
            final StandardizedSmiles entity = new StandardizedSmilesImpl();
            entity.setPrefix(prefix);
            return entity;
        }

        /**
         * Constructs a new instance of {@link StandardizedSmiles}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param name String
         * @param nsc Integer
         * @param structureSource String
         * @param smilesFromProd String
         * @param prodCanSmi String
         * @param removedSalts String
         * @param parentCanSmi String
         * @param tautCanSmi String
         * @param tautNostereoCanSmi String
         * @param xeno Integer
         * @param hf Integer
         * @param nci60 Integer
         * @param distribution String
         * @param conf String
         * @param inventory Double
         * @param mw Double
         * @param mf String
         * @param alogp Double
         * @param logd Double
         * @param hba Integer
         * @param hbd Integer
         * @param sa Double
         * @param psa Double
         * @param cas String
         * @param prefix String
         * @param countRelated Integer
         * @param related String
         * @param rdkitMol RdkitMol
         * @param nscDrugTrackers Collection<NscDrugTracker>
         * @return newInstance StandardizedSmiles
         */
        public static StandardizedSmiles newInstance(String name, Integer nsc, String structureSource, String smilesFromProd, String prodCanSmi, String removedSalts, String parentCanSmi, String tautCanSmi, String tautNostereoCanSmi, Integer xeno, Integer hf, Integer nci60, String distribution, String conf, Double inventory, Double mw, String mf, Double alogp, Double logd, Integer hba, Integer hbd, Double sa, Double psa, String cas, String prefix, Integer countRelated, String related, RdkitMol rdkitMol, Collection<NscDrugTracker> nscDrugTrackers)
        {
            final StandardizedSmiles entity = new StandardizedSmilesImpl();
            entity.setName(name);
            entity.setNsc(nsc);
            entity.setStructureSource(structureSource);
            entity.setSmilesFromProd(smilesFromProd);
            entity.setProdCanSmi(prodCanSmi);
            entity.setRemovedSalts(removedSalts);
            entity.setParentCanSmi(parentCanSmi);
            entity.setTautCanSmi(tautCanSmi);
            entity.setTautNostereoCanSmi(tautNostereoCanSmi);
            entity.setXeno(xeno);
            entity.setHf(hf);
            entity.setNci60(nci60);
            entity.setDistribution(distribution);
            entity.setConf(conf);
            entity.setInventory(inventory);
            entity.setMw(mw);
            entity.setMf(mf);
            entity.setAlogp(alogp);
            entity.setLogd(logd);
            entity.setHba(hba);
            entity.setHbd(hbd);
            entity.setSa(sa);
            entity.setPsa(psa);
            entity.setCas(cas);
            entity.setPrefix(prefix);
            entity.setCountRelated(countRelated);
            entity.setRelated(related);
            entity.setRdkitMol(rdkitMol);
            entity.setNscDrugTrackers(nscDrugTrackers);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(StandardizedSmiles o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
            if (this.getName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getName().compareTo(o.getName()));
            }
            if (this.getNsc() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getNsc().compareTo(o.getNsc()));
            }
            if (this.getStructureSource() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getStructureSource().compareTo(o.getStructureSource()));
            }
            if (this.getSmilesFromProd() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSmilesFromProd().compareTo(o.getSmilesFromProd()));
            }
            if (this.getProdCanSmi() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getProdCanSmi().compareTo(o.getProdCanSmi()));
            }
            if (this.getRemovedSalts() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRemovedSalts().compareTo(o.getRemovedSalts()));
            }
            if (this.getParentCanSmi() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getParentCanSmi().compareTo(o.getParentCanSmi()));
            }
            if (this.getTautCanSmi() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTautCanSmi().compareTo(o.getTautCanSmi()));
            }
            if (this.getTautNostereoCanSmi() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getTautNostereoCanSmi().compareTo(o.getTautNostereoCanSmi()));
            }
            if (this.getXeno() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getXeno().compareTo(o.getXeno()));
            }
            if (this.getHf() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getHf().compareTo(o.getHf()));
            }
            if (this.getNci60() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getNci60().compareTo(o.getNci60()));
            }
            if (this.getDistribution() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDistribution().compareTo(o.getDistribution()));
            }
            if (this.getConf() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getConf().compareTo(o.getConf()));
            }
            if (this.getInventory() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getInventory().compareTo(o.getInventory()));
            }
            if (this.getMw() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMw().compareTo(o.getMw()));
            }
            if (this.getMf() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMf().compareTo(o.getMf()));
            }
            if (this.getAlogp() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getAlogp().compareTo(o.getAlogp()));
            }
            if (this.getLogd() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getLogd().compareTo(o.getLogd()));
            }
            if (this.getHba() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getHba().compareTo(o.getHba()));
            }
            if (this.getHbd() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getHbd().compareTo(o.getHbd()));
            }
            if (this.getSa() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSa().compareTo(o.getSa()));
            }
            if (this.getPsa() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPsa().compareTo(o.getPsa()));
            }
            if (this.getCas() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getCas().compareTo(o.getCas()));
            }
            if (this.getPrefix() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPrefix().compareTo(o.getPrefix()));
            }
            if (this.getCountRelated() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getCountRelated().compareTo(o.getCountRelated()));
            }
            if (this.getRelated() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getRelated().compareTo(o.getRelated()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}