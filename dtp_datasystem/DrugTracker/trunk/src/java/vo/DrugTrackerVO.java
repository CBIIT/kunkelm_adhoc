// license-header java merge-point
//
/**
 * @author Generated on 04/12/2013 11:05:31-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::DrugTrackerModel::mwk.drugtracker::vo::DrugTrackerVO
 * STEREOTYPE:   ValueObject
 */
package vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * TODO: Model Documentation for class DrugTrackerVO
 */
public class DrugTrackerVO
    implements Serializable, Comparable<DrugTrackerVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 8932307292446167034L;

    // Class attributes
    /** TODO: Model Documentation for attribute targets */
    protected Collection<String> targets;
    /** TODO: Model Documentation for attribute nsc */
    protected Long nsc;
    /** TODO: Model Documentation for attribute cas */
    protected String cas;
    /** TODO: Model Documentation for attribute drugTrackerAliases */
    protected Collection<String> drugTrackerAliases;
    /** TODO: Model Documentation for attribute originator */
    protected String originator;
    /** TODO: Model Documentation for attribute agent */
    protected String agent;
    /** TODO: Model Documentation for attribute structureSource */
    protected String structureSource;
    /** TODO: Model Documentation for attribute smilesFromProd */
    protected String smilesFromProd;
    /** TODO: Model Documentation for attribute prodCanSmi */
    protected String prodCanSmi;
    /** TODO: Model Documentation for attribute removedSalts */
    protected String removedSalts;
    /** TODO: Model Documentation for attribute parentCanSmi */
    protected String parentCanSmi;
    /** TODO: Model Documentation for attribute tautCanSmi */
    protected String tautCanSmi;
    /** TODO: Model Documentation for attribute tautNostereoCanSmi */
    protected String tautNostereoCanSmi;
    /** TODO: Model Documentation for attribute xeno */
    protected Long xeno;
    /** TODO: Model Documentation for attribute hf */
    protected Long hf;
    /** TODO: Model Documentation for attribute nci60 */
    protected Long nci60;
    /** TODO: Model Documentation for attribute distribution */
    protected String distribution;
    /** TODO: Model Documentation for attribute conf */
    protected String conf;
    /** TODO: Model Documentation for attribute inventory */
    protected Double inventory;
    /** TODO: Model Documentation for attribute drugTrackerSet */
    protected String drugTrackerSet;
    /** TODO: Model Documentation for attribute mw */
    protected Double mw;
    /** TODO: Model Documentation for attribute mf */
    protected String mf;
    /** TODO: Model Documentation for attribute alogp */
    protected Double alogp;
    /** TODO: Model Documentation for attribute logd */
    protected Double logd;
    /** TODO: Model Documentation for attribute hba */
    protected Long hba;
    /** TODO: Model Documentation for attribute hbd */
    protected Long hbd;
    /** TODO: Model Documentation for attribute sa */
    protected Double sa;
    /** TODO: Model Documentation for attribute psa */
    protected Double psa;
    /** TODO: Model Documentation for attribute prefix */
    protected String prefix;
    /** TODO: Model Documentation for attribute mdl_mol */
    protected String mdl_mol;
    /** TODO: Model Documentation for attribute plate */
    protected String plate;
    /** TODO: Model Documentation for attribute id */
    protected Long id;

    /** Default Constructor with no properties */
    public DrugTrackerVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param drugTrackerSetIn String
     */
    public DrugTrackerVO(final String drugTrackerSetIn)
    {
        this.drugTrackerSet = drugTrackerSetIn;
    }

    /**
     * Constructor with all properties
     * @param targetsIn Collection<String>
     * @param nscIn Long
     * @param casIn String
     * @param drugTrackerAliasesIn Collection<String>
     * @param originatorIn String
     * @param agentIn String
     * @param structureSourceIn String
     * @param smilesFromProdIn String
     * @param prodCanSmiIn String
     * @param removedSaltsIn String
     * @param parentCanSmiIn String
     * @param tautCanSmiIn String
     * @param tautNostereoCanSmiIn String
     * @param xenoIn Long
     * @param hfIn Long
     * @param nci60In Long
     * @param distributionIn String
     * @param confIn String
     * @param inventoryIn Double
     * @param drugTrackerSetIn String
     * @param mwIn Double
     * @param mfIn String
     * @param alogpIn Double
     * @param logdIn Double
     * @param hbaIn Long
     * @param hbdIn Long
     * @param saIn Double
     * @param psaIn Double
     * @param prefixIn String
     * @param mdl_molIn String
     * @param plateIn String
     * @param idIn Long
     */
    public DrugTrackerVO(final Collection<String> targetsIn, final Long nscIn, final String casIn, final Collection<String> drugTrackerAliasesIn, final String originatorIn, final String agentIn, final String structureSourceIn, final String smilesFromProdIn, final String prodCanSmiIn, final String removedSaltsIn, final String parentCanSmiIn, final String tautCanSmiIn, final String tautNostereoCanSmiIn, final Long xenoIn, final Long hfIn, final Long nci60In, final String distributionIn, final String confIn, final Double inventoryIn, final String drugTrackerSetIn, final Double mwIn, final String mfIn, final Double alogpIn, final Double logdIn, final Long hbaIn, final Long hbdIn, final Double saIn, final Double psaIn, final String prefixIn, final String mdl_molIn, final String plateIn, final Long idIn)
    {
        this.targets = targetsIn;
        this.nsc = nscIn;
        this.cas = casIn;
        this.drugTrackerAliases = drugTrackerAliasesIn;
        this.originator = originatorIn;
        this.agent = agentIn;
        this.structureSource = structureSourceIn;
        this.smilesFromProd = smilesFromProdIn;
        this.prodCanSmi = prodCanSmiIn;
        this.removedSalts = removedSaltsIn;
        this.parentCanSmi = parentCanSmiIn;
        this.tautCanSmi = tautCanSmiIn;
        this.tautNostereoCanSmi = tautNostereoCanSmiIn;
        this.xeno = xenoIn;
        this.hf = hfIn;
        this.nci60 = nci60In;
        this.distribution = distributionIn;
        this.conf = confIn;
        this.inventory = inventoryIn;
        this.drugTrackerSet = drugTrackerSetIn;
        this.mw = mwIn;
        this.mf = mfIn;
        this.alogp = alogpIn;
        this.logd = logdIn;
        this.hba = hbaIn;
        this.hbd = hbdIn;
        this.sa = saIn;
        this.psa = psaIn;
        this.prefix = prefixIn;
        this.mdl_mol = mdl_molIn;
        this.plate = plateIn;
        this.id = idIn;
    }

    /**
     * Copies constructor from other DrugTrackerVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public DrugTrackerVO(final DrugTrackerVO otherBean)
    {
        this.targets = otherBean.getTargets();
        this.nsc = otherBean.getNsc();
        this.cas = otherBean.getCas();
        this.drugTrackerAliases = otherBean.getDrugTrackerAliases();
        this.originator = otherBean.getOriginator();
        this.agent = otherBean.getAgent();
        this.structureSource = otherBean.getStructureSource();
        this.smilesFromProd = otherBean.getSmilesFromProd();
        this.prodCanSmi = otherBean.getProdCanSmi();
        this.removedSalts = otherBean.getRemovedSalts();
        this.parentCanSmi = otherBean.getParentCanSmi();
        this.tautCanSmi = otherBean.getTautCanSmi();
        this.tautNostereoCanSmi = otherBean.getTautNostereoCanSmi();
        this.xeno = otherBean.getXeno();
        this.hf = otherBean.getHf();
        this.nci60 = otherBean.getNci60();
        this.distribution = otherBean.getDistribution();
        this.conf = otherBean.getConf();
        this.inventory = otherBean.getInventory();
        this.drugTrackerSet = otherBean.getDrugTrackerSet();
        this.mw = otherBean.getMw();
        this.mf = otherBean.getMf();
        this.alogp = otherBean.getAlogp();
        this.logd = otherBean.getLogd();
        this.hba = otherBean.getHba();
        this.hbd = otherBean.getHbd();
        this.sa = otherBean.getSa();
        this.psa = otherBean.getPsa();
        this.prefix = otherBean.getPrefix();
        this.mdl_mol = otherBean.getMdl_mol();
        this.plate = otherBean.getPlate();
        this.id = otherBean.getId();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final DrugTrackerVO otherBean)
    {
        if (null != otherBean)
        {
            this.setTargets(otherBean.getTargets());
            this.setNsc(otherBean.getNsc());
            this.setCas(otherBean.getCas());
            this.setDrugTrackerAliases(otherBean.getDrugTrackerAliases());
            this.setOriginator(otherBean.getOriginator());
            this.setAgent(otherBean.getAgent());
            this.setStructureSource(otherBean.getStructureSource());
            this.setSmilesFromProd(otherBean.getSmilesFromProd());
            this.setProdCanSmi(otherBean.getProdCanSmi());
            this.setRemovedSalts(otherBean.getRemovedSalts());
            this.setParentCanSmi(otherBean.getParentCanSmi());
            this.setTautCanSmi(otherBean.getTautCanSmi());
            this.setTautNostereoCanSmi(otherBean.getTautNostereoCanSmi());
            this.setXeno(otherBean.getXeno());
            this.setHf(otherBean.getHf());
            this.setNci60(otherBean.getNci60());
            this.setDistribution(otherBean.getDistribution());
            this.setConf(otherBean.getConf());
            this.setInventory(otherBean.getInventory());
            this.setDrugTrackerSet(otherBean.getDrugTrackerSet());
            this.setMw(otherBean.getMw());
            this.setMf(otherBean.getMf());
            this.setAlogp(otherBean.getAlogp());
            this.setLogd(otherBean.getLogd());
            this.setHba(otherBean.getHba());
            this.setHbd(otherBean.getHbd());
            this.setSa(otherBean.getSa());
            this.setPsa(otherBean.getPsa());
            this.setPrefix(otherBean.getPrefix());
            this.setMdl_mol(otherBean.getMdl_mol());
            this.setPlate(otherBean.getPlate());
            this.setId(otherBean.getId());
        }
    }

    /**
     * TODO: Model Documentation for attribute targets
     * Get the targets Attribute
     * @return targets Collection<String>
     */
    public Collection<String> getTargets()
    {
        if (this.targets == null)
        {
            this.targets = new ArrayList<String>();
        }
        return this.targets;
    }

    /**
     * 
     * @param value Collection<String>
     */
    public void setTargets(final Collection<String> value)
    {
        this.targets = value;
    }

    /**
     * TODO: Model Documentation for attribute nsc
     * Get the nsc Attribute
     * @return nsc Long
     */
    public Long getNsc()
    {
        return this.nsc;
    }

    /**
     * 
     * @param value Long
     */
    public void setNsc(final Long value)
    {
        this.nsc = value;
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
     * TODO: Model Documentation for attribute drugTrackerAliases
     * Get the drugTrackerAliases Attribute
     * @return drugTrackerAliases Collection<String>
     */
    public Collection<String> getDrugTrackerAliases()
    {
        if (this.drugTrackerAliases == null)
        {
            this.drugTrackerAliases = new ArrayList<String>();
        }
        return this.drugTrackerAliases;
    }

    /**
     * 
     * @param value Collection<String>
     */
    public void setDrugTrackerAliases(final Collection<String> value)
    {
        this.drugTrackerAliases = value;
    }

    /**
     * TODO: Model Documentation for attribute originator
     * Get the originator Attribute
     * @return originator String
     */
    public String getOriginator()
    {
        return this.originator;
    }

    /**
     * 
     * @param value String
     */
    public void setOriginator(final String value)
    {
        this.originator = value;
    }

    /**
     * TODO: Model Documentation for attribute agent
     * Get the agent Attribute
     * @return agent String
     */
    public String getAgent()
    {
        return this.agent;
    }

    /**
     * 
     * @param value String
     */
    public void setAgent(final String value)
    {
        this.agent = value;
    }

    /**
     * TODO: Model Documentation for attribute structureSource
     * Get the structureSource Attribute
     * @return structureSource String
     */
    public String getStructureSource()
    {
        return this.structureSource;
    }

    /**
     * 
     * @param value String
     */
    public void setStructureSource(final String value)
    {
        this.structureSource = value;
    }

    /**
     * TODO: Model Documentation for attribute smilesFromProd
     * Get the smilesFromProd Attribute
     * @return smilesFromProd String
     */
    public String getSmilesFromProd()
    {
        return this.smilesFromProd;
    }

    /**
     * 
     * @param value String
     */
    public void setSmilesFromProd(final String value)
    {
        this.smilesFromProd = value;
    }

    /**
     * TODO: Model Documentation for attribute prodCanSmi
     * Get the prodCanSmi Attribute
     * @return prodCanSmi String
     */
    public String getProdCanSmi()
    {
        return this.prodCanSmi;
    }

    /**
     * 
     * @param value String
     */
    public void setProdCanSmi(final String value)
    {
        this.prodCanSmi = value;
    }

    /**
     * TODO: Model Documentation for attribute removedSalts
     * Get the removedSalts Attribute
     * @return removedSalts String
     */
    public String getRemovedSalts()
    {
        return this.removedSalts;
    }

    /**
     * 
     * @param value String
     */
    public void setRemovedSalts(final String value)
    {
        this.removedSalts = value;
    }

    /**
     * TODO: Model Documentation for attribute parentCanSmi
     * Get the parentCanSmi Attribute
     * @return parentCanSmi String
     */
    public String getParentCanSmi()
    {
        return this.parentCanSmi;
    }

    /**
     * 
     * @param value String
     */
    public void setParentCanSmi(final String value)
    {
        this.parentCanSmi = value;
    }

    /**
     * TODO: Model Documentation for attribute tautCanSmi
     * Get the tautCanSmi Attribute
     * @return tautCanSmi String
     */
    public String getTautCanSmi()
    {
        return this.tautCanSmi;
    }

    /**
     * 
     * @param value String
     */
    public void setTautCanSmi(final String value)
    {
        this.tautCanSmi = value;
    }

    /**
     * TODO: Model Documentation for attribute tautNostereoCanSmi
     * Get the tautNostereoCanSmi Attribute
     * @return tautNostereoCanSmi String
     */
    public String getTautNostereoCanSmi()
    {
        return this.tautNostereoCanSmi;
    }

    /**
     * 
     * @param value String
     */
    public void setTautNostereoCanSmi(final String value)
    {
        this.tautNostereoCanSmi = value;
    }

    /**
     * TODO: Model Documentation for attribute xeno
     * Get the xeno Attribute
     * @return xeno Long
     */
    public Long getXeno()
    {
        return this.xeno;
    }

    /**
     * 
     * @param value Long
     */
    public void setXeno(final Long value)
    {
        this.xeno = value;
    }

    /**
     * TODO: Model Documentation for attribute hf
     * Get the hf Attribute
     * @return hf Long
     */
    public Long getHf()
    {
        return this.hf;
    }

    /**
     * 
     * @param value Long
     */
    public void setHf(final Long value)
    {
        this.hf = value;
    }

    /**
     * TODO: Model Documentation for attribute nci60
     * Get the nci60 Attribute
     * @return nci60 Long
     */
    public Long getNci60()
    {
        return this.nci60;
    }

    /**
     * 
     * @param value Long
     */
    public void setNci60(final Long value)
    {
        this.nci60 = value;
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
     * TODO: Model Documentation for attribute drugTrackerSet
     * Get the drugTrackerSet Attribute
     * @return drugTrackerSet String
     */
    public String getDrugTrackerSet()
    {
        return this.drugTrackerSet;
    }

    /**
     * 
     * @param value String
     */
    public void setDrugTrackerSet(final String value)
    {
        this.drugTrackerSet = value;
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
     * @return hba Long
     */
    public Long getHba()
    {
        return this.hba;
    }

    /**
     * 
     * @param value Long
     */
    public void setHba(final Long value)
    {
        this.hba = value;
    }

    /**
     * TODO: Model Documentation for attribute hbd
     * Get the hbd Attribute
     * @return hbd Long
     */
    public Long getHbd()
    {
        return this.hbd;
    }

    /**
     * 
     * @param value Long
     */
    public void setHbd(final Long value)
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
     * TODO: Model Documentation for attribute mdl_mol
     * Get the mdl_mol Attribute
     * @return mdl_mol String
     */
    public String getMdl_mol()
    {
        return this.mdl_mol;
    }

    /**
     * 
     * @param value String
     */
    public void setMdl_mol(final String value)
    {
        this.mdl_mol = value;
    }

    /**
     * TODO: Model Documentation for attribute plate
     * Get the plate Attribute
     * @return plate String
     */
    public String getPlate()
    {
        return this.plate;
    }

    /**
     * 
     * @param value String
     */
    public void setPlate(final String value)
    {
        this.plate = value;
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
        DrugTrackerVO rhs = (DrugTrackerVO) object;
        return new EqualsBuilder()
            .append(this.getTargets(), rhs.getTargets())
            .append(this.getNsc(), rhs.getNsc())
            .append(this.getCas(), rhs.getCas())
            .append(this.getDrugTrackerAliases(), rhs.getDrugTrackerAliases())
            .append(this.getOriginator(), rhs.getOriginator())
            .append(this.getAgent(), rhs.getAgent())
            .append(this.getStructureSource(), rhs.getStructureSource())
            .append(this.getSmilesFromProd(), rhs.getSmilesFromProd())
            .append(this.getProdCanSmi(), rhs.getProdCanSmi())
            .append(this.getRemovedSalts(), rhs.getRemovedSalts())
            .append(this.getParentCanSmi(), rhs.getParentCanSmi())
            .append(this.getTautCanSmi(), rhs.getTautCanSmi())
            .append(this.getTautNostereoCanSmi(), rhs.getTautNostereoCanSmi())
            .append(this.getXeno(), rhs.getXeno())
            .append(this.getHf(), rhs.getHf())
            .append(this.getNci60(), rhs.getNci60())
            .append(this.getDistribution(), rhs.getDistribution())
            .append(this.getConf(), rhs.getConf())
            .append(this.getInventory(), rhs.getInventory())
            .append(this.getDrugTrackerSet(), rhs.getDrugTrackerSet())
            .append(this.getMw(), rhs.getMw())
            .append(this.getMf(), rhs.getMf())
            .append(this.getAlogp(), rhs.getAlogp())
            .append(this.getLogd(), rhs.getLogd())
            .append(this.getHba(), rhs.getHba())
            .append(this.getHbd(), rhs.getHbd())
            .append(this.getSa(), rhs.getSa())
            .append(this.getPsa(), rhs.getPsa())
            .append(this.getPrefix(), rhs.getPrefix())
            .append(this.getMdl_mol(), rhs.getMdl_mol())
            .append(this.getPlate(), rhs.getPlate())
            .append(this.getId(), rhs.getId())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final DrugTrackerVO object)
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
            .append(this.getTargets(), object.getTargets())
            .append(this.getNsc(), object.getNsc())
            .append(this.getCas(), object.getCas())
            .append(this.getDrugTrackerAliases(), object.getDrugTrackerAliases())
            .append(this.getOriginator(), object.getOriginator())
            .append(this.getAgent(), object.getAgent())
            .append(this.getStructureSource(), object.getStructureSource())
            .append(this.getSmilesFromProd(), object.getSmilesFromProd())
            .append(this.getProdCanSmi(), object.getProdCanSmi())
            .append(this.getRemovedSalts(), object.getRemovedSalts())
            .append(this.getParentCanSmi(), object.getParentCanSmi())
            .append(this.getTautCanSmi(), object.getTautCanSmi())
            .append(this.getTautNostereoCanSmi(), object.getTautNostereoCanSmi())
            .append(this.getXeno(), object.getXeno())
            .append(this.getHf(), object.getHf())
            .append(this.getNci60(), object.getNci60())
            .append(this.getDistribution(), object.getDistribution())
            .append(this.getConf(), object.getConf())
            .append(this.getInventory(), object.getInventory())
            .append(this.getDrugTrackerSet(), object.getDrugTrackerSet())
            .append(this.getMw(), object.getMw())
            .append(this.getMf(), object.getMf())
            .append(this.getAlogp(), object.getAlogp())
            .append(this.getLogd(), object.getLogd())
            .append(this.getHba(), object.getHba())
            .append(this.getHbd(), object.getHbd())
            .append(this.getSa(), object.getSa())
            .append(this.getPsa(), object.getPsa())
            .append(this.getPrefix(), object.getPrefix())
            .append(this.getMdl_mol(), object.getMdl_mol())
            .append(this.getPlate(), object.getPlate())
            .append(this.getId(), object.getId())
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
            .append(this.getTargets())
            .append(this.getNsc())
            .append(this.getCas())
            .append(this.getDrugTrackerAliases())
            .append(this.getOriginator())
            .append(this.getAgent())
            .append(this.getStructureSource())
            .append(this.getSmilesFromProd())
            .append(this.getProdCanSmi())
            .append(this.getRemovedSalts())
            .append(this.getParentCanSmi())
            .append(this.getTautCanSmi())
            .append(this.getTautNostereoCanSmi())
            .append(this.getXeno())
            .append(this.getHf())
            .append(this.getNci60())
            .append(this.getDistribution())
            .append(this.getConf())
            .append(this.getInventory())
            .append(this.getDrugTrackerSet())
            .append(this.getMw())
            .append(this.getMf())
            .append(this.getAlogp())
            .append(this.getLogd())
            .append(this.getHba())
            .append(this.getHbd())
            .append(this.getSa())
            .append(this.getPsa())
            .append(this.getPrefix())
            .append(this.getMdl_mol())
            .append(this.getPlate())
            .append(this.getId())
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
            .append("targets", this.getTargets())
            .append("nsc", this.getNsc())
            .append("cas", this.getCas())
            .append("drugTrackerAliases", this.getDrugTrackerAliases())
            .append("originator", this.getOriginator())
            .append("agent", this.getAgent())
            .append("structureSource", this.getStructureSource())
            .append("smilesFromProd", this.getSmilesFromProd())
            .append("prodCanSmi", this.getProdCanSmi())
            .append("removedSalts", this.getRemovedSalts())
            .append("parentCanSmi", this.getParentCanSmi())
            .append("tautCanSmi", this.getTautCanSmi())
            .append("tautNostereoCanSmi", this.getTautNostereoCanSmi())
            .append("xeno", this.getXeno())
            .append("hf", this.getHf())
            .append("nci60", this.getNci60())
            .append("distribution", this.getDistribution())
            .append("conf", this.getConf())
            .append("inventory", this.getInventory())
            .append("drugTrackerSet", this.getDrugTrackerSet())
            .append("mw", this.getMw())
            .append("mf", this.getMf())
            .append("alogp", this.getAlogp())
            .append("logd", this.getLogd())
            .append("hba", this.getHba())
            .append("hbd", this.getHbd())
            .append("sa", this.getSa())
            .append("psa", this.getPsa())
            .append("prefix", this.getPrefix())
            .append("mdl_mol", this.getMdl_mol())
            .append("plate", this.getPlate())
            .append("id", this.getId())
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

        final DrugTrackerVO that = (DrugTrackerVO)thatObject;

        return
            equal(this.getTargets(), that.getTargets())
            && equal(this.getNsc(), that.getNsc())
            && equal(this.getCas(), that.getCas())
            && equal(this.getDrugTrackerAliases(), that.getDrugTrackerAliases())
            && equal(this.getOriginator(), that.getOriginator())
            && equal(this.getAgent(), that.getAgent())
            && equal(this.getStructureSource(), that.getStructureSource())
            && equal(this.getSmilesFromProd(), that.getSmilesFromProd())
            && equal(this.getProdCanSmi(), that.getProdCanSmi())
            && equal(this.getRemovedSalts(), that.getRemovedSalts())
            && equal(this.getParentCanSmi(), that.getParentCanSmi())
            && equal(this.getTautCanSmi(), that.getTautCanSmi())
            && equal(this.getTautNostereoCanSmi(), that.getTautNostereoCanSmi())
            && equal(this.getXeno(), that.getXeno())
            && equal(this.getHf(), that.getHf())
            && equal(this.getNci60(), that.getNci60())
            && equal(this.getDistribution(), that.getDistribution())
            && equal(this.getConf(), that.getConf())
            && equal(this.getInventory(), that.getInventory())
            && equal(this.getDrugTrackerSet(), that.getDrugTrackerSet())
            && equal(this.getMw(), that.getMw())
            && equal(this.getMf(), that.getMf())
            && equal(this.getAlogp(), that.getAlogp())
            && equal(this.getLogd(), that.getLogd())
            && equal(this.getHba(), that.getHba())
            && equal(this.getHbd(), that.getHbd())
            && equal(this.getSa(), that.getSa())
            && equal(this.getPsa(), that.getPsa())
            && equal(this.getPrefix(), that.getPrefix())
            && equal(this.getMdl_mol(), that.getMdl_mol())
            && equal(this.getPlate(), that.getPlate())
            && equal(this.getId(), that.getId())
        ;
    }

    /**
     * This is a convenient helper method which is able to detect whether or not two values are equal. Two values
     * are equal when they are both {@code null}, are arrays of the same length with equal elements or are
     * equal objects (this includes {@link Collection} and {@link java.util.Map} instances).
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
        else // note that the following also covers Collection and java.util.Map
        {
            equal = first.equals(second);
        }

        return equal;
    }

    // DrugTrackerVO value-object java merge-point
}