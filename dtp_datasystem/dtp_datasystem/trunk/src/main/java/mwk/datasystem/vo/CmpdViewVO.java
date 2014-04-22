// license-header java merge-point
//
/**
 * @author Generated on 04/15/2014 17:22:57-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::DataSystemModel::mwk.datasystem::vo::CmpdViewVO
 * STEREOTYPE:   ValueObject
 */
package mwk.datasystem.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * TODO: Model Documentation for class CmpdViewVO
 */
public class CmpdViewVO
    implements Serializable, Comparable<CmpdViewVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 7018256021460482100L;

    // Class attributes
    /** TODO: Model Documentation for attribute mw */
    protected Double mw;
    /** TODO: Model Documentation for attribute mf */
    protected String mf;
    /** TODO: Model Documentation for attribute alogp */
    protected Double alogp;
    /** TODO: Model Documentation for attribute logd */
    protected Double logd;
    /** TODO: Model Documentation for attribute hba */
    protected Integer hba;
    /** TODO: Model Documentation for attribute hbd */
    protected Integer hbd;
    /** TODO: Model Documentation for attribute sa */
    protected Double sa;
    /** TODO: Model Documentation for attribute psa */
    protected Double psa;
    /** TODO: Model Documentation for attribute smiles */
    protected String smiles;
    /** TODO: Model Documentation for attribute inchi */
    protected String inchi;
    /** TODO: Model Documentation for attribute mol */
    protected String mol;
    /** TODO: Model Documentation for attribute inchiAux */
    protected String inchiAux;
    /** TODO: Model Documentation for attribute name */
    protected String name;
    /** TODO: Model Documentation for attribute nscCmpdId */
    protected Long nscCmpdId;
    /** TODO: Model Documentation for attribute prefix */
    protected String prefix;
    /** TODO: Model Documentation for attribute nsc */
    protected Integer nsc;
    /** TODO: Model Documentation for attribute conf */
    protected String conf;
    /** TODO: Model Documentation for attribute distribution */
    protected String distribution;
    /** TODO: Model Documentation for attribute cas */
    protected String cas;
    /** TODO: Model Documentation for attribute nci60 */
    protected Integer nci60;
    /** TODO: Model Documentation for attribute hf */
    protected Integer hf;
    /** TODO: Model Documentation for attribute xeno */
    protected Integer xeno;
    /** TODO: Model Documentation for attribute cmpdOwner */
    protected String cmpdOwner;
    /** TODO: Model Documentation for attribute adHocCmpdId */
    protected Long adHocCmpdId;
    /** TODO: Model Documentation for attribute targets */
    protected List<String> targets;
    /** TODO: Model Documentation for attribute sets */
    protected List<String> sets;
    /** TODO: Model Documentation for attribute projects */
    protected List<String> projects;
    /** TODO: Model Documentation for attribute plates */
    protected List<String> plates;
    /** TODO: Model Documentation for attribute aliases */
    protected List<String> aliases;
    /** TODO: Model Documentation for attribute id */
    protected Long id;
    /** TODO: Model Documentation for attribute inventory */
    protected Double inventory;

    /** Default Constructor with no properties */
    public CmpdViewVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param prefixIn String
     * @param confIn String
     * @param distributionIn String
     * @param cmpdOwnerIn String
     */
    public CmpdViewVO(final String prefixIn, final String confIn, final String distributionIn, final String cmpdOwnerIn)
    {
        this.prefix = prefixIn;
        this.conf = confIn;
        this.distribution = distributionIn;
        this.cmpdOwner = cmpdOwnerIn;
    }

    /**
     * Constructor with all properties
     * @param mwIn Double
     * @param mfIn String
     * @param alogpIn Double
     * @param logdIn Double
     * @param hbaIn Integer
     * @param hbdIn Integer
     * @param saIn Double
     * @param psaIn Double
     * @param smilesIn String
     * @param inchiIn String
     * @param molIn String
     * @param inchiAuxIn String
     * @param nameIn String
     * @param nscCmpdIdIn Long
     * @param prefixIn String
     * @param nscIn Integer
     * @param confIn String
     * @param distributionIn String
     * @param casIn String
     * @param nci60In Integer
     * @param hfIn Integer
     * @param xenoIn Integer
     * @param cmpdOwnerIn String
     * @param adHocCmpdIdIn Long
     * @param targetsIn List<String>
     * @param setsIn List<String>
     * @param projectsIn List<String>
     * @param platesIn List<String>
     * @param aliasesIn List<String>
     * @param idIn Long
     * @param inventoryIn Double
     */
    public CmpdViewVO(final Double mwIn, final String mfIn, final Double alogpIn, final Double logdIn, final Integer hbaIn, final Integer hbdIn, final Double saIn, final Double psaIn, final String smilesIn, final String inchiIn, final String molIn, final String inchiAuxIn, final String nameIn, final Long nscCmpdIdIn, final String prefixIn, final Integer nscIn, final String confIn, final String distributionIn, final String casIn, final Integer nci60In, final Integer hfIn, final Integer xenoIn, final String cmpdOwnerIn, final Long adHocCmpdIdIn, final List<String> targetsIn, final List<String> setsIn, final List<String> projectsIn, final List<String> platesIn, final List<String> aliasesIn, final Long idIn, final Double inventoryIn)
    {
        this.mw = mwIn;
        this.mf = mfIn;
        this.alogp = alogpIn;
        this.logd = logdIn;
        this.hba = hbaIn;
        this.hbd = hbdIn;
        this.sa = saIn;
        this.psa = psaIn;
        this.smiles = smilesIn;
        this.inchi = inchiIn;
        this.mol = molIn;
        this.inchiAux = inchiAuxIn;
        this.name = nameIn;
        this.nscCmpdId = nscCmpdIdIn;
        this.prefix = prefixIn;
        this.nsc = nscIn;
        this.conf = confIn;
        this.distribution = distributionIn;
        this.cas = casIn;
        this.nci60 = nci60In;
        this.hf = hfIn;
        this.xeno = xenoIn;
        this.cmpdOwner = cmpdOwnerIn;
        this.adHocCmpdId = adHocCmpdIdIn;
        this.targets = targetsIn;
        this.sets = setsIn;
        this.projects = projectsIn;
        this.plates = platesIn;
        this.aliases = aliasesIn;
        this.id = idIn;
        this.inventory = inventoryIn;
    }

    /**
     * Copies constructor from other CmpdViewVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public CmpdViewVO(final CmpdViewVO otherBean)
    {
        this.mw = otherBean.getMw();
        this.mf = otherBean.getMf();
        this.alogp = otherBean.getAlogp();
        this.logd = otherBean.getLogd();
        this.hba = otherBean.getHba();
        this.hbd = otherBean.getHbd();
        this.sa = otherBean.getSa();
        this.psa = otherBean.getPsa();
        this.smiles = otherBean.getSmiles();
        this.inchi = otherBean.getInchi();
        this.mol = otherBean.getMol();
        this.inchiAux = otherBean.getInchiAux();
        this.name = otherBean.getName();
        this.nscCmpdId = otherBean.getNscCmpdId();
        this.prefix = otherBean.getPrefix();
        this.nsc = otherBean.getNsc();
        this.conf = otherBean.getConf();
        this.distribution = otherBean.getDistribution();
        this.cas = otherBean.getCas();
        this.nci60 = otherBean.getNci60();
        this.hf = otherBean.getHf();
        this.xeno = otherBean.getXeno();
        this.cmpdOwner = otherBean.getCmpdOwner();
        this.adHocCmpdId = otherBean.getAdHocCmpdId();
        this.targets = otherBean.getTargets();
        this.sets = otherBean.getSets();
        this.projects = otherBean.getProjects();
        this.plates = otherBean.getPlates();
        this.aliases = otherBean.getAliases();
        this.id = otherBean.getId();
        this.inventory = otherBean.getInventory();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final CmpdViewVO otherBean)
    {
        if (null != otherBean)
        {
            this.setMw(otherBean.getMw());
            this.setMf(otherBean.getMf());
            this.setAlogp(otherBean.getAlogp());
            this.setLogd(otherBean.getLogd());
            this.setHba(otherBean.getHba());
            this.setHbd(otherBean.getHbd());
            this.setSa(otherBean.getSa());
            this.setPsa(otherBean.getPsa());
            this.setSmiles(otherBean.getSmiles());
            this.setInchi(otherBean.getInchi());
            this.setMol(otherBean.getMol());
            this.setInchiAux(otherBean.getInchiAux());
            this.setName(otherBean.getName());
            this.setNscCmpdId(otherBean.getNscCmpdId());
            this.setPrefix(otherBean.getPrefix());
            this.setNsc(otherBean.getNsc());
            this.setConf(otherBean.getConf());
            this.setDistribution(otherBean.getDistribution());
            this.setCas(otherBean.getCas());
            this.setNci60(otherBean.getNci60());
            this.setHf(otherBean.getHf());
            this.setXeno(otherBean.getXeno());
            this.setCmpdOwner(otherBean.getCmpdOwner());
            this.setAdHocCmpdId(otherBean.getAdHocCmpdId());
            this.setTargets(otherBean.getTargets());
            this.setSets(otherBean.getSets());
            this.setProjects(otherBean.getProjects());
            this.setPlates(otherBean.getPlates());
            this.setAliases(otherBean.getAliases());
            this.setId(otherBean.getId());
            this.setInventory(otherBean.getInventory());
        }
    }

    /**
     * TODO: Model Documentation for attribute mw
     * Get the mw Attribute
     * @return mw Double
     */
    public Double getMw()
    {
        return this.mw;
    }

    /**
     * 
     * @param value Double
     */
    public void setMw(final Double value)
    {
        this.mw = value;
    }

    /**
     * TODO: Model Documentation for attribute mf
     * Get the mf Attribute
     * @return mf String
     */
    public String getMf()
    {
        return this.mf;
    }

    /**
     * 
     * @param value String
     */
    public void setMf(final String value)
    {
        this.mf = value;
    }

    /**
     * TODO: Model Documentation for attribute alogp
     * Get the alogp Attribute
     * @return alogp Double
     */
    public Double getAlogp()
    {
        return this.alogp;
    }

    /**
     * 
     * @param value Double
     */
    public void setAlogp(final Double value)
    {
        this.alogp = value;
    }

    /**
     * TODO: Model Documentation for attribute logd
     * Get the logd Attribute
     * @return logd Double
     */
    public Double getLogd()
    {
        return this.logd;
    }

    /**
     * 
     * @param value Double
     */
    public void setLogd(final Double value)
    {
        this.logd = value;
    }

    /**
     * TODO: Model Documentation for attribute hba
     * Get the hba Attribute
     * @return hba Integer
     */
    public Integer getHba()
    {
        return this.hba;
    }

    /**
     * 
     * @param value Integer
     */
    public void setHba(final Integer value)
    {
        this.hba = value;
    }

    /**
     * TODO: Model Documentation for attribute hbd
     * Get the hbd Attribute
     * @return hbd Integer
     */
    public Integer getHbd()
    {
        return this.hbd;
    }

    /**
     * 
     * @param value Integer
     */
    public void setHbd(final Integer value)
    {
        this.hbd = value;
    }

    /**
     * TODO: Model Documentation for attribute sa
     * Get the sa Attribute
     * @return sa Double
     */
    public Double getSa()
    {
        return this.sa;
    }

    /**
     * 
     * @param value Double
     */
    public void setSa(final Double value)
    {
        this.sa = value;
    }

    /**
     * TODO: Model Documentation for attribute psa
     * Get the psa Attribute
     * @return psa Double
     */
    public Double getPsa()
    {
        return this.psa;
    }

    /**
     * 
     * @param value Double
     */
    public void setPsa(final Double value)
    {
        this.psa = value;
    }

    /**
     * TODO: Model Documentation for attribute smiles
     * Get the smiles Attribute
     * @return smiles String
     */
    public String getSmiles()
    {
        return this.smiles;
    }

    /**
     * 
     * @param value String
     */
    public void setSmiles(final String value)
    {
        this.smiles = value;
    }

    /**
     * TODO: Model Documentation for attribute inchi
     * Get the inchi Attribute
     * @return inchi String
     */
    public String getInchi()
    {
        return this.inchi;
    }

    /**
     * 
     * @param value String
     */
    public void setInchi(final String value)
    {
        this.inchi = value;
    }

    /**
     * TODO: Model Documentation for attribute mol
     * Get the mol Attribute
     * @return mol String
     */
    public String getMol()
    {
        return this.mol;
    }

    /**
     * 
     * @param value String
     */
    public void setMol(final String value)
    {
        this.mol = value;
    }

    /**
     * TODO: Model Documentation for attribute inchiAux
     * Get the inchiAux Attribute
     * @return inchiAux String
     */
    public String getInchiAux()
    {
        return this.inchiAux;
    }

    /**
     * 
     * @param value String
     */
    public void setInchiAux(final String value)
    {
        this.inchiAux = value;
    }

    /**
     * TODO: Model Documentation for attribute name
     * Get the name Attribute
     * @return name String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * 
     * @param value String
     */
    public void setName(final String value)
    {
        this.name = value;
    }

    /**
     * TODO: Model Documentation for attribute nscCmpdId
     * Get the nscCmpdId Attribute
     * @return nscCmpdId Long
     */
    public Long getNscCmpdId()
    {
        return this.nscCmpdId;
    }

    /**
     * 
     * @param value Long
     */
    public void setNscCmpdId(final Long value)
    {
        this.nscCmpdId = value;
    }

    /**
     * TODO: Model Documentation for attribute prefix
     * Get the prefix Attribute
     * @return prefix String
     */
    public String getPrefix()
    {
        return this.prefix;
    }

    /**
     * 
     * @param value String
     */
    public void setPrefix(final String value)
    {
        this.prefix = value;
    }

    /**
     * TODO: Model Documentation for attribute nsc
     * Get the nsc Attribute
     * @return nsc Integer
     */
    public Integer getNsc()
    {
        return this.nsc;
    }

    /**
     * 
     * @param value Integer
     */
    public void setNsc(final Integer value)
    {
        this.nsc = value;
    }

    /**
     * TODO: Model Documentation for attribute conf
     * Get the conf Attribute
     * @return conf String
     */
    public String getConf()
    {
        return this.conf;
    }

    /**
     * 
     * @param value String
     */
    public void setConf(final String value)
    {
        this.conf = value;
    }

    /**
     * TODO: Model Documentation for attribute distribution
     * Get the distribution Attribute
     * @return distribution String
     */
    public String getDistribution()
    {
        return this.distribution;
    }

    /**
     * 
     * @param value String
     */
    public void setDistribution(final String value)
    {
        this.distribution = value;
    }

    /**
     * TODO: Model Documentation for attribute cas
     * Get the cas Attribute
     * @return cas String
     */
    public String getCas()
    {
        return this.cas;
    }

    /**
     * 
     * @param value String
     */
    public void setCas(final String value)
    {
        this.cas = value;
    }

    /**
     * TODO: Model Documentation for attribute nci60
     * Get the nci60 Attribute
     * @return nci60 Integer
     */
    public Integer getNci60()
    {
        return this.nci60;
    }

    /**
     * 
     * @param value Integer
     */
    public void setNci60(final Integer value)
    {
        this.nci60 = value;
    }

    /**
     * TODO: Model Documentation for attribute hf
     * Get the hf Attribute
     * @return hf Integer
     */
    public Integer getHf()
    {
        return this.hf;
    }

    /**
     * 
     * @param value Integer
     */
    public void setHf(final Integer value)
    {
        this.hf = value;
    }

    /**
     * TODO: Model Documentation for attribute xeno
     * Get the xeno Attribute
     * @return xeno Integer
     */
    public Integer getXeno()
    {
        return this.xeno;
    }

    /**
     * 
     * @param value Integer
     */
    public void setXeno(final Integer value)
    {
        this.xeno = value;
    }

    /**
     * TODO: Model Documentation for attribute cmpdOwner
     * Get the cmpdOwner Attribute
     * @return cmpdOwner String
     */
    public String getCmpdOwner()
    {
        return this.cmpdOwner;
    }

    /**
     * 
     * @param value String
     */
    public void setCmpdOwner(final String value)
    {
        this.cmpdOwner = value;
    }

    /**
     * TODO: Model Documentation for attribute adHocCmpdId
     * Get the adHocCmpdId Attribute
     * @return adHocCmpdId Long
     */
    public Long getAdHocCmpdId()
    {
        return this.adHocCmpdId;
    }

    /**
     * 
     * @param value Long
     */
    public void setAdHocCmpdId(final Long value)
    {
        this.adHocCmpdId = value;
    }

    /**
     * TODO: Model Documentation for attribute targets
     * Get the targets Attribute
     * @return targets List<String>
     */
    public List<String> getTargets()
    {
        if (this.targets == null)
        {
            this.targets = new ArrayList<String>();
        }
        return this.targets;
    }

    /**
     * 
     * @param value List<String>
     */
    public void setTargets(final List<String> value)
    {
        this.targets = value;
    }

    /**
     * TODO: Model Documentation for attribute sets
     * Get the sets Attribute
     * @return sets List<String>
     */
    public List<String> getSets()
    {
        if (this.sets == null)
        {
            this.sets = new ArrayList<String>();
        }
        return this.sets;
    }

    /**
     * 
     * @param value List<String>
     */
    public void setSets(final List<String> value)
    {
        this.sets = value;
    }

    /**
     * TODO: Model Documentation for attribute projects
     * Get the projects Attribute
     * @return projects List<String>
     */
    public List<String> getProjects()
    {
        if (this.projects == null)
        {
            this.projects = new ArrayList<String>();
        }
        return this.projects;
    }

    /**
     * 
     * @param value List<String>
     */
    public void setProjects(final List<String> value)
    {
        this.projects = value;
    }

    /**
     * TODO: Model Documentation for attribute plates
     * Get the plates Attribute
     * @return plates List<String>
     */
    public List<String> getPlates()
    {
        if (this.plates == null)
        {
            this.plates = new ArrayList<String>();
        }
        return this.plates;
    }

    /**
     * 
     * @param value List<String>
     */
    public void setPlates(final List<String> value)
    {
        this.plates = value;
    }

    /**
     * TODO: Model Documentation for attribute aliases
     * Get the aliases Attribute
     * @return aliases List<String>
     */
    public List<String> getAliases()
    {
        if (this.aliases == null)
        {
            this.aliases = new ArrayList<String>();
        }
        return this.aliases;
    }

    /**
     * 
     * @param value List<String>
     */
    public void setAliases(final List<String> value)
    {
        this.aliases = value;
    }

    /**
     * TODO: Model Documentation for attribute id
     * Get the id Attribute
     * @return id Long
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * 
     * @param value Long
     */
    public void setId(final Long value)
    {
        this.id = value;
    }

    /**
     * TODO: Model Documentation for attribute inventory
     * Get the inventory Attribute
     * @return inventory Double
     */
    public Double getInventory()
    {
        return this.inventory;
    }

    /**
     * 
     * @param value Double
     */
    public void setInventory(final Double value)
    {
        this.inventory = value;
    }

    /**
     * @param object to compare this object against
     * @return boolean if equal
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object==null || object.getClass() != this.getClass())
        {
             return false;
        }
        // Check if the same object instance
        if (object==this)
        {
            return true;
        }
        CmpdViewVO rhs = (CmpdViewVO) object;
        return new EqualsBuilder()
            .append(this.getMw(), rhs.getMw())
            .append(this.getMf(), rhs.getMf())
            .append(this.getAlogp(), rhs.getAlogp())
            .append(this.getLogd(), rhs.getLogd())
            .append(this.getHba(), rhs.getHba())
            .append(this.getHbd(), rhs.getHbd())
            .append(this.getSa(), rhs.getSa())
            .append(this.getPsa(), rhs.getPsa())
            .append(this.getSmiles(), rhs.getSmiles())
            .append(this.getInchi(), rhs.getInchi())
            .append(this.getMol(), rhs.getMol())
            .append(this.getInchiAux(), rhs.getInchiAux())
            .append(this.getName(), rhs.getName())
            .append(this.getNscCmpdId(), rhs.getNscCmpdId())
            .append(this.getPrefix(), rhs.getPrefix())
            .append(this.getNsc(), rhs.getNsc())
            .append(this.getConf(), rhs.getConf())
            .append(this.getDistribution(), rhs.getDistribution())
            .append(this.getCas(), rhs.getCas())
            .append(this.getNci60(), rhs.getNci60())
            .append(this.getHf(), rhs.getHf())
            .append(this.getXeno(), rhs.getXeno())
            .append(this.getCmpdOwner(), rhs.getCmpdOwner())
            .append(this.getAdHocCmpdId(), rhs.getAdHocCmpdId())
            .append(this.getTargets(), rhs.getTargets())
            .append(this.getSets(), rhs.getSets())
            .append(this.getProjects(), rhs.getProjects())
            .append(this.getPlates(), rhs.getPlates())
            .append(this.getAliases(), rhs.getAliases())
            .append(this.getId(), rhs.getId())
            .append(this.getInventory(), rhs.getInventory())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final CmpdViewVO object)
    {
        if (object==null)
        {
            return -1;
        }
        // Check if the same object instance
        if (object==this)
        {
            return 0;
        }
        return new CompareToBuilder()
            .append(this.getMw(), object.getMw())
            .append(this.getMf(), object.getMf())
            .append(this.getAlogp(), object.getAlogp())
            .append(this.getLogd(), object.getLogd())
            .append(this.getHba(), object.getHba())
            .append(this.getHbd(), object.getHbd())
            .append(this.getSa(), object.getSa())
            .append(this.getPsa(), object.getPsa())
            .append(this.getSmiles(), object.getSmiles())
            .append(this.getInchi(), object.getInchi())
            .append(this.getMol(), object.getMol())
            .append(this.getInchiAux(), object.getInchiAux())
            .append(this.getName(), object.getName())
            .append(this.getNscCmpdId(), object.getNscCmpdId())
            .append(this.getPrefix(), object.getPrefix())
            .append(this.getNsc(), object.getNsc())
            .append(this.getConf(), object.getConf())
            .append(this.getDistribution(), object.getDistribution())
            .append(this.getCas(), object.getCas())
            .append(this.getNci60(), object.getNci60())
            .append(this.getHf(), object.getHf())
            .append(this.getXeno(), object.getXeno())
            .append(this.getCmpdOwner(), object.getCmpdOwner())
            .append(this.getAdHocCmpdId(), object.getAdHocCmpdId())
            .append(this.getTargets(), object.getTargets())
            .append(this.getSets(), object.getSets())
            .append(this.getProjects(), object.getProjects())
            .append(this.getPlates(), object.getPlates())
            .append(this.getAliases(), object.getAliases())
            .append(this.getId(), object.getId())
            .append(this.getInventory(), object.getInventory())
            .toComparison();
    }

    /**
     * @return int hashCode value
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(1249046965, -82296885)
            .append(this.getMw())
            .append(this.getMf())
            .append(this.getAlogp())
            .append(this.getLogd())
            .append(this.getHba())
            .append(this.getHbd())
            .append(this.getSa())
            .append(this.getPsa())
            .append(this.getSmiles())
            .append(this.getInchi())
            .append(this.getMol())
            .append(this.getInchiAux())
            .append(this.getName())
            .append(this.getNscCmpdId())
            .append(this.getPrefix())
            .append(this.getNsc())
            .append(this.getConf())
            .append(this.getDistribution())
            .append(this.getCas())
            .append(this.getNci60())
            .append(this.getHf())
            .append(this.getXeno())
            .append(this.getCmpdOwner())
            .append(this.getAdHocCmpdId())
            .append(this.getTargets())
            .append(this.getSets())
            .append(this.getProjects())
            .append(this.getPlates())
            .append(this.getAliases())
            .append(this.getId())
            .append(this.getInventory())
            .toHashCode();
    }

    /**
     * @return String representation of object
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
            .append("mw", this.getMw())
            .append("mf", this.getMf())
            .append("alogp", this.getAlogp())
            .append("logd", this.getLogd())
            .append("hba", this.getHba())
            .append("hbd", this.getHbd())
            .append("sa", this.getSa())
            .append("psa", this.getPsa())
            .append("smiles", this.getSmiles())
            .append("inchi", this.getInchi())
            .append("mol", this.getMol())
            .append("inchiAux", this.getInchiAux())
            .append("name", this.getName())
            .append("nscCmpdId", this.getNscCmpdId())
            .append("prefix", this.getPrefix())
            .append("nsc", this.getNsc())
            .append("conf", this.getConf())
            .append("distribution", this.getDistribution())
            .append("cas", this.getCas())
            .append("nci60", this.getNci60())
            .append("hf", this.getHf())
            .append("xeno", this.getXeno())
            .append("cmpdOwner", this.getCmpdOwner())
            .append("adHocCmpdId", this.getAdHocCmpdId())
            .append("targets", this.getTargets())
            .append("sets", this.getSets())
            .append("projects", this.getProjects())
            .append("plates", this.getPlates())
            .append("aliases", this.getAliases())
            .append("id", this.getId())
            .append("inventory", this.getInventory())
            .toString();
    }

    /**
     * Compares the properties of this instance to the properties of the argument. This method will return
     * {@code false} as soon as it detects that the argument is {@code null} or not of the same type as
     * (or a sub-type of) this instance's type.
     *
     * <p/>For array, collection or map properties the comparison will be done one level deep, in other words:
     * the elements will be compared using the {@code equals()} operation.
     *
     * <p/>Note that two properties will be considered equal when both values are {@code null}.
     *
     * @param thatObject the object containing the properties to compare against this instance
     * @return this method will return {@code true} in case the argument has the same type as this class, or is a
     *      sub-type of this class and all properties as found on this class have equal values when queried on that
     *      argument instance; in all other cases this method will return {@code false}
     */
    public boolean equalProperties(final Object thatObject)
    {
        if (thatObject == null || !this.getClass().isAssignableFrom(thatObject.getClass()))
        {
            return false;
        }

        final CmpdViewVO that = (CmpdViewVO)thatObject;

        return
            equal(this.getMw(), that.getMw())
            && equal(this.getMf(), that.getMf())
            && equal(this.getAlogp(), that.getAlogp())
            && equal(this.getLogd(), that.getLogd())
            && equal(this.getHba(), that.getHba())
            && equal(this.getHbd(), that.getHbd())
            && equal(this.getSa(), that.getSa())
            && equal(this.getPsa(), that.getPsa())
            && equal(this.getSmiles(), that.getSmiles())
            && equal(this.getInchi(), that.getInchi())
            && equal(this.getMol(), that.getMol())
            && equal(this.getInchiAux(), that.getInchiAux())
            && equal(this.getName(), that.getName())
            && equal(this.getNscCmpdId(), that.getNscCmpdId())
            && equal(this.getPrefix(), that.getPrefix())
            && equal(this.getNsc(), that.getNsc())
            && equal(this.getConf(), that.getConf())
            && equal(this.getDistribution(), that.getDistribution())
            && equal(this.getCas(), that.getCas())
            && equal(this.getNci60(), that.getNci60())
            && equal(this.getHf(), that.getHf())
            && equal(this.getXeno(), that.getXeno())
            && equal(this.getCmpdOwner(), that.getCmpdOwner())
            && equal(this.getAdHocCmpdId(), that.getAdHocCmpdId())
            && equal(this.getTargets(), that.getTargets())
            && equal(this.getSets(), that.getSets())
            && equal(this.getProjects(), that.getProjects())
            && equal(this.getPlates(), that.getPlates())
            && equal(this.getAliases(), that.getAliases())
            && equal(this.getId(), that.getId())
            && equal(this.getInventory(), that.getInventory())
        ;
    }

    /**
     * This is a convenient helper method which is able to detect whether or not two values are equal. Two values
     * are equal when they are both {@code null}, are arrays of the same length with equal elements or are
     * equal objects (this includes {@link java.util.Collection} and {@link java.util.Map} instances).
     *
     * <p/>Note that for array, collection or map instances the comparison runs one level deep.
     *
     * @param first the first object to compare, may be {@code null}
     * @param second the second object to compare, may be {@code null}
     * @return this method will return {@code true} in case both objects are equal as explained above;
     *      in all other cases this method will return {@code false}
     */
    protected static boolean equal(final Object first, final Object second)
    {
        final boolean equal;

        if (first == null)
        {
            equal = (second == null);
        }
        else if (first.getClass().isArray() && (second != null) && second.getClass().isArray())
        {
            equal = Arrays.equals((Object[])first, (Object[])second);
        }
        else // note that the following also covers java.util.Collection and java.util.Map
        {
            equal = first.equals(second);
        }

        return equal;
    }

    // CmpdViewVO value-object java merge-point
}