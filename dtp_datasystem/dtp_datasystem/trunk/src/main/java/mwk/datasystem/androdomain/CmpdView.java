// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: hibernate/HibernateEntity.vsl in andromda-hibernate-cartridge.
//
package mwk.datasystem.androdomain;

import java.io.Serializable;

/**
 * 
 */
// HibernateEntity.vsl annotations merge-point
public abstract class CmpdView
    implements Serializable, Comparable<CmpdView>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -4463222987001780916L;

    // Generate 30 attributes
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

    private String smiles;

    /**
     * 
     * @return this.smiles String
     */
    public String getSmiles()
    {
        return this.smiles;
    }

    /**
     * 
     * @param smilesIn String
     */
    public void setSmiles(String smilesIn)
    {
        this.smiles = smilesIn;
    }

    private String inchi;

    /**
     * 
     * @return this.inchi String
     */
    public String getInchi()
    {
        return this.inchi;
    }

    /**
     * 
     * @param inchiIn String
     */
    public void setInchi(String inchiIn)
    {
        this.inchi = inchiIn;
    }

    private String mol;

    /**
     * 
     * @return this.mol String
     */
    public String getMol()
    {
        return this.mol;
    }

    /**
     * 
     * @param molIn String
     */
    public void setMol(String molIn)
    {
        this.mol = molIn;
    }

    private String inchiAux;

    /**
     * 
     * @return this.inchiAux String
     */
    public String getInchiAux()
    {
        return this.inchiAux;
    }

    /**
     * 
     * @param inchiAuxIn String
     */
    public void setInchiAux(String inchiAuxIn)
    {
        this.inchiAux = inchiAuxIn;
    }

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

    private Long nscCmpdId;

    /**
     * 
     * @return this.nscCmpdId Long
     */
    public Long getNscCmpdId()
    {
        return this.nscCmpdId;
    }

    /**
     * 
     * @param nscCmpdIdIn Long
     */
    public void setNscCmpdId(Long nscCmpdIdIn)
    {
        this.nscCmpdId = nscCmpdIdIn;
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

    private String cmpdOwner;

    /**
     * 
     * @return this.cmpdOwner String
     */
    public String getCmpdOwner()
    {
        return this.cmpdOwner;
    }

    /**
     * 
     * @param cmpdOwnerIn String
     */
    public void setCmpdOwner(String cmpdOwnerIn)
    {
        this.cmpdOwner = cmpdOwnerIn;
    }

    private Long adHocCmpdId;

    /**
     * 
     * @return this.adHocCmpdId Long
     */
    public Long getAdHocCmpdId()
    {
        return this.adHocCmpdId;
    }

    /**
     * 
     * @param adHocCmpdIdIn Long
     */
    public void setAdHocCmpdId(Long adHocCmpdIdIn)
    {
        this.adHocCmpdId = adHocCmpdIdIn;
    }

    private String formattedTargetsString;

    /**
     * 
     * @return this.formattedTargetsString String
     */
    public String getFormattedTargetsString()
    {
        return this.formattedTargetsString;
    }

    /**
     * 
     * @param formattedTargetsStringIn String
     */
    public void setFormattedTargetsString(String formattedTargetsStringIn)
    {
        this.formattedTargetsString = formattedTargetsStringIn;
    }

    private String formattedSetsString;

    /**
     * 
     * @return this.formattedSetsString String
     */
    public String getFormattedSetsString()
    {
        return this.formattedSetsString;
    }

    /**
     * 
     * @param formattedSetsStringIn String
     */
    public void setFormattedSetsString(String formattedSetsStringIn)
    {
        this.formattedSetsString = formattedSetsStringIn;
    }

    private String formattedProjectsString;

    /**
     * 
     * @return this.formattedProjectsString String
     */
    public String getFormattedProjectsString()
    {
        return this.formattedProjectsString;
    }

    /**
     * 
     * @param formattedProjectsStringIn String
     */
    public void setFormattedProjectsString(String formattedProjectsStringIn)
    {
        this.formattedProjectsString = formattedProjectsStringIn;
    }

    private String formattedPlatesString;

    /**
     * 
     * @return this.formattedPlatesString String
     */
    public String getFormattedPlatesString()
    {
        return this.formattedPlatesString;
    }

    /**
     * 
     * @param formattedPlatesStringIn String
     */
    public void setFormattedPlatesString(String formattedPlatesStringIn)
    {
        this.formattedPlatesString = formattedPlatesStringIn;
    }

    private String formattedAliasesString;

    /**
     * 
     * @return this.formattedAliasesString String
     */
    public String getFormattedAliasesString()
    {
        return this.formattedAliasesString;
    }

    /**
     * 
     * @param formattedAliasesStringIn String
     */
    public void setFormattedAliasesString(String formattedAliasesStringIn)
    {
        this.formattedAliasesString = formattedAliasesStringIn;
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

    // Generate 0 associations
    /**
     * Returns <code>true</code> if the argument is an CmpdView instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof CmpdView))
        {
            return false;
        }
        final CmpdView that = (CmpdView)object;
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
     * Constructs new instances of {@link CmpdView}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link CmpdView}.
         * @return new CmpdViewImpl()
         */
        public static CmpdView newInstance()
        {
            return new CmpdViewImpl();
        }


        /**
         * Constructs a new instance of {@link CmpdView}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param mw Double
         * @param mf String
         * @param alogp Double
         * @param logd Double
         * @param hba Integer
         * @param hbd Integer
         * @param sa Double
         * @param psa Double
         * @param smiles String
         * @param inchi String
         * @param mol String
         * @param inchiAux String
         * @param name String
         * @param nscCmpdId Long
         * @param prefix String
         * @param nsc Integer
         * @param conf String
         * @param distribution String
         * @param cas String
         * @param nci60 Integer
         * @param hf Integer
         * @param xeno Integer
         * @param cmpdOwner String
         * @param adHocCmpdId Long
         * @param formattedTargetsString String
         * @param formattedSetsString String
         * @param formattedProjectsString String
         * @param formattedPlatesString String
         * @param formattedAliasesString String
         * @return newInstance CmpdView
         */
        public static CmpdView newInstance(Double mw, String mf, Double alogp, Double logd, Integer hba, Integer hbd, Double sa, Double psa, String smiles, String inchi, String mol, String inchiAux, String name, Long nscCmpdId, String prefix, Integer nsc, String conf, String distribution, String cas, Integer nci60, Integer hf, Integer xeno, String cmpdOwner, Long adHocCmpdId, String formattedTargetsString, String formattedSetsString, String formattedProjectsString, String formattedPlatesString, String formattedAliasesString)
        {
            final CmpdView entity = new CmpdViewImpl();
            entity.setMw(mw);
            entity.setMf(mf);
            entity.setAlogp(alogp);
            entity.setLogd(logd);
            entity.setHba(hba);
            entity.setHbd(hbd);
            entity.setSa(sa);
            entity.setPsa(psa);
            entity.setSmiles(smiles);
            entity.setInchi(inchi);
            entity.setMol(mol);
            entity.setInchiAux(inchiAux);
            entity.setName(name);
            entity.setNscCmpdId(nscCmpdId);
            entity.setPrefix(prefix);
            entity.setNsc(nsc);
            entity.setConf(conf);
            entity.setDistribution(distribution);
            entity.setCas(cas);
            entity.setNci60(nci60);
            entity.setHf(hf);
            entity.setXeno(xeno);
            entity.setCmpdOwner(cmpdOwner);
            entity.setAdHocCmpdId(adHocCmpdId);
            entity.setFormattedTargetsString(formattedTargetsString);
            entity.setFormattedSetsString(formattedSetsString);
            entity.setFormattedProjectsString(formattedProjectsString);
            entity.setFormattedPlatesString(formattedPlatesString);
            entity.setFormattedAliasesString(formattedAliasesString);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(CmpdView o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
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
            if (this.getSmiles() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getSmiles().compareTo(o.getSmiles()));
            }
            if (this.getInchi() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getInchi().compareTo(o.getInchi()));
            }
            if (this.getMol() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getMol().compareTo(o.getMol()));
            }
            if (this.getInchiAux() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getInchiAux().compareTo(o.getInchiAux()));
            }
            if (this.getName() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getName().compareTo(o.getName()));
            }
            if (this.getNscCmpdId() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getNscCmpdId().compareTo(o.getNscCmpdId()));
            }
            if (this.getPrefix() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getPrefix().compareTo(o.getPrefix()));
            }
            if (this.getNsc() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getNsc().compareTo(o.getNsc()));
            }
            if (this.getConf() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getConf().compareTo(o.getConf()));
            }
            if (this.getDistribution() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getDistribution().compareTo(o.getDistribution()));
            }
            if (this.getCas() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getCas().compareTo(o.getCas()));
            }
            if (this.getNci60() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getNci60().compareTo(o.getNci60()));
            }
            if (this.getHf() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getHf().compareTo(o.getHf()));
            }
            if (this.getXeno() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getXeno().compareTo(o.getXeno()));
            }
            if (this.getCmpdOwner() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getCmpdOwner().compareTo(o.getCmpdOwner()));
            }
            if (this.getAdHocCmpdId() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getAdHocCmpdId().compareTo(o.getAdHocCmpdId()));
            }
            if (this.getFormattedTargetsString() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFormattedTargetsString().compareTo(o.getFormattedTargetsString()));
            }
            if (this.getFormattedSetsString() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFormattedSetsString().compareTo(o.getFormattedSetsString()));
            }
            if (this.getFormattedProjectsString() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFormattedProjectsString().compareTo(o.getFormattedProjectsString()));
            }
            if (this.getFormattedPlatesString() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFormattedPlatesString().compareTo(o.getFormattedPlatesString()));
            }
            if (this.getFormattedAliasesString() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getFormattedAliasesString().compareTo(o.getFormattedAliasesString()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}