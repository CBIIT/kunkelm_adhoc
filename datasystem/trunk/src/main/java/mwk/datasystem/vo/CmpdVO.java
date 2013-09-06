// license-header java merge-point
//
/**
 * @author Generated on 09/01/2013 19:57:04-0400 Do not modify by hand!
 *
 * TEMPLATE:     ValueObject.vsl in andromda-java-cartridge.
 * MODEL CLASS:  AndroMDAModel::DCTDDataModel::mwk.dctddata::vo::CmpdVO
 * STEREOTYPE:   ValueObject
 */
package mwk.datasystem.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * TODO: Model Documentation for class CmpdVO
 */
public class CmpdVO
    implements Serializable, Comparable<CmpdVO>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = 2849999779598602057L;

    // Class attributes
    /** TODO: Model Documentation for attribute name */
    protected String name;
    /** TODO: Model Documentation for attribute nsc */
    protected Integer nsc;
    /** TODO: Model Documentation for attribute distribution */
    protected String distribution;
    /** TODO: Model Documentation for attribute conf */
    protected String conf;
    /** TODO: Model Documentation for attribute inventory */
    protected Double inventory;
    /** TODO: Model Documentation for attribute cas */
    protected String cas;
    /** TODO: Model Documentation for attribute targets */
    protected List<String> targets;
    /** TODO: Model Documentation for attribute aliases */
    protected List<String> aliases;
    /** TODO: Model Documentation for attribute prefix */
    protected String prefix;
    /** TODO: Model Documentation for attribute cmpdFragments */
    protected Collection<CmpdFragmentVO> cmpdFragments;
    /** TODO: Model Documentation for attribute id */
    protected Long id;
    /** TODO: Model Documentation for attribute cmpdBioAssay */
    protected CmpdBioAssayVO cmpdBioAssay;
    /** TODO: Model Documentation for attribute nscCmpdId */
    protected Long nscCmpdId;
    /** TODO: Model Documentation for attribute parentFragment */
    protected CmpdFragmentVO parentFragment;
    /** TODO: Model Documentation for attribute adHocCmpdId */
    protected Long adHocCmpdId;
    /** TODO: Model Documentation for attribute sets */
    protected List<String> sets;
    /** TODO: Model Documentation for attribute projects */
    protected List<String> projects;
    /** TODO: Model Documentation for attribute plates */
    protected List<String> plates;
    /** TODO: Model Documentation for attribute countCmpdFragments */
    protected Integer countCmpdFragments;
    /** TODO: Model Documentation for attribute isSelected */
    protected Boolean isSelected;

    /** Default Constructor with no properties */
    public CmpdVO()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param prefixIn String
     * @param cmpdBioAssayIn CmpdBioAssayVO
     * @param parentFragmentIn CmpdFragmentVO
     */
    public CmpdVO(final String prefixIn, final CmpdBioAssayVO cmpdBioAssayIn, final CmpdFragmentVO parentFragmentIn)
    {
        this.prefix = prefixIn;
        this.cmpdBioAssay = cmpdBioAssayIn;
        this.parentFragment = parentFragmentIn;
    }

    /**
     * Constructor with all properties
     * @param nameIn String
     * @param nscIn Integer
     * @param distributionIn String
     * @param confIn String
     * @param inventoryIn Double
     * @param casIn String
     * @param targetsIn List<String>
     * @param aliasesIn List<String>
     * @param prefixIn String
     * @param cmpdFragmentsIn Collection<CmpdFragmentVO>
     * @param idIn Long
     * @param cmpdBioAssayIn CmpdBioAssayVO
     * @param nscCmpdIdIn Long
     * @param parentFragmentIn CmpdFragmentVO
     * @param adHocCmpdIdIn Long
     * @param setsIn List<String>
     * @param projectsIn List<String>
     * @param platesIn List<String>
     * @param countCmpdFragmentsIn Integer
     * @param isSelectedIn Boolean
     */
    public CmpdVO(final String nameIn, final Integer nscIn, final String distributionIn, final String confIn, final Double inventoryIn, final String casIn, final List<String> targetsIn, final List<String> aliasesIn, final String prefixIn, final Collection<CmpdFragmentVO> cmpdFragmentsIn, final Long idIn, final CmpdBioAssayVO cmpdBioAssayIn, final Long nscCmpdIdIn, final CmpdFragmentVO parentFragmentIn, final Long adHocCmpdIdIn, final List<String> setsIn, final List<String> projectsIn, final List<String> platesIn, final Integer countCmpdFragmentsIn, final Boolean isSelectedIn)
    {
        this.name = nameIn;
        this.nsc = nscIn;
        this.distribution = distributionIn;
        this.conf = confIn;
        this.inventory = inventoryIn;
        this.cas = casIn;
        this.targets = targetsIn;
        this.aliases = aliasesIn;
        this.prefix = prefixIn;
        this.cmpdFragments = cmpdFragmentsIn;
        this.id = idIn;
        this.cmpdBioAssay = cmpdBioAssayIn;
        this.nscCmpdId = nscCmpdIdIn;
        this.parentFragment = parentFragmentIn;
        this.adHocCmpdId = adHocCmpdIdIn;
        this.sets = setsIn;
        this.projects = projectsIn;
        this.plates = platesIn;
        this.countCmpdFragments = countCmpdFragmentsIn;
        this.isSelected = isSelectedIn;
    }

    /**
     * Copies constructor from other CmpdVO
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public CmpdVO(final CmpdVO otherBean)
    {
        this.name = otherBean.getName();
        this.nsc = otherBean.getNsc();
        this.distribution = otherBean.getDistribution();
        this.conf = otherBean.getConf();
        this.inventory = otherBean.getInventory();
        this.cas = otherBean.getCas();
        this.targets = otherBean.getTargets();
        this.aliases = otherBean.getAliases();
        this.prefix = otherBean.getPrefix();
        this.cmpdFragments = otherBean.getCmpdFragments();
        this.id = otherBean.getId();
        this.cmpdBioAssay = otherBean.getCmpdBioAssay();
        this.nscCmpdId = otherBean.getNscCmpdId();
        this.parentFragment = otherBean.getParentFragment();
        this.adHocCmpdId = otherBean.getAdHocCmpdId();
        this.sets = otherBean.getSets();
        this.projects = otherBean.getProjects();
        this.plates = otherBean.getPlates();
        this.countCmpdFragments = otherBean.getCountCmpdFragments();
        this.isSelected = otherBean.getIsSelected();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final CmpdVO otherBean)
    {
        if (null != otherBean)
        {
            this.setName(otherBean.getName());
            this.setNsc(otherBean.getNsc());
            this.setDistribution(otherBean.getDistribution());
            this.setConf(otherBean.getConf());
            this.setInventory(otherBean.getInventory());
            this.setCas(otherBean.getCas());
            this.setTargets(otherBean.getTargets());
            this.setAliases(otherBean.getAliases());
            this.setPrefix(otherBean.getPrefix());
            this.setCmpdFragments(otherBean.getCmpdFragments());
            this.setId(otherBean.getId());
            this.setCmpdBioAssay(otherBean.getCmpdBioAssay());
            this.setNscCmpdId(otherBean.getNscCmpdId());
            this.setParentFragment(otherBean.getParentFragment());
            this.setAdHocCmpdId(otherBean.getAdHocCmpdId());
            this.setSets(otherBean.getSets());
            this.setProjects(otherBean.getProjects());
            this.setPlates(otherBean.getPlates());
            this.setCountCmpdFragments(otherBean.getCountCmpdFragments());
            this.setIsSelected(otherBean.getIsSelected());
        }
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
     * TODO: Model Documentation for attribute cmpdFragments
     * Get the cmpdFragments Attribute
     * @return cmpdFragments Collection<CmpdFragmentVO>
     */
    public Collection<CmpdFragmentVO> getCmpdFragments()
    {
        if (this.cmpdFragments == null)
        {
            this.cmpdFragments = new ArrayList<CmpdFragmentVO>();
        }
        return this.cmpdFragments;
    }

    /**
     * 
     * @param value Collection<CmpdFragmentVO>
     */
    public void setCmpdFragments(final Collection<CmpdFragmentVO> value)
    {
        this.cmpdFragments = value;
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
     * TODO: Model Documentation for attribute cmpdBioAssay
     * Get the cmpdBioAssay Attribute
     * @return cmpdBioAssay CmpdBioAssayVO
     */
    public CmpdBioAssayVO getCmpdBioAssay()
    {
        return this.cmpdBioAssay;
    }

    /**
     * 
     * @param value CmpdBioAssayVO
     */
    public void setCmpdBioAssay(final CmpdBioAssayVO value)
    {
        this.cmpdBioAssay = value;
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
     * TODO: Model Documentation for attribute parentFragment
     * Get the parentFragment Attribute
     * @return parentFragment CmpdFragmentVO
     */
    public CmpdFragmentVO getParentFragment()
    {
        return this.parentFragment;
    }

    /**
     * 
     * @param value CmpdFragmentVO
     */
    public void setParentFragment(final CmpdFragmentVO value)
    {
        this.parentFragment = value;
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
     * TODO: Model Documentation for attribute countCmpdFragments
     * Get the countCmpdFragments Attribute
     * @return countCmpdFragments Integer
     */
    public Integer getCountCmpdFragments()
    {
        return this.countCmpdFragments;
    }

    /**
     * 
     * @param value Integer
     */
    public void setCountCmpdFragments(final Integer value)
    {
        this.countCmpdFragments = value;
    }

    /**
     * TODO: Model Documentation for attribute isSelected
     * Get the isSelected Attribute
     * @return isSelected Boolean
     */
    public Boolean getIsSelected()
    {
        return this.isSelected;
    }

    /**
     * 
     * Duplicates getBoolean method, for use as Jaxb2 compatible object
     * Get the isSelected Attribute
     * @return isSelected Boolean
     */
    @Deprecated
    public Boolean isIsSelected()
    {
        return this.isSelected;
    }

    /**
     * 
     * @param value Boolean
     */
    public void setIsSelected(final Boolean value)
    {
        this.isSelected = value;
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
        CmpdVO rhs = (CmpdVO) object;
        return new EqualsBuilder()
            .append(this.getName(), rhs.getName())
            .append(this.getNsc(), rhs.getNsc())
            .append(this.getDistribution(), rhs.getDistribution())
            .append(this.getConf(), rhs.getConf())
            .append(this.getInventory(), rhs.getInventory())
            .append(this.getCas(), rhs.getCas())
            .append(this.getTargets(), rhs.getTargets())
            .append(this.getAliases(), rhs.getAliases())
            .append(this.getPrefix(), rhs.getPrefix())
            .append(this.getCmpdFragments(), rhs.getCmpdFragments())
            .append(this.getId(), rhs.getId())
            .append(this.getCmpdBioAssay(), rhs.getCmpdBioAssay())
            .append(this.getNscCmpdId(), rhs.getNscCmpdId())
            .append(this.getParentFragment(), rhs.getParentFragment())
            .append(this.getAdHocCmpdId(), rhs.getAdHocCmpdId())
            .append(this.getSets(), rhs.getSets())
            .append(this.getProjects(), rhs.getProjects())
            .append(this.getPlates(), rhs.getPlates())
            .append(this.getCountCmpdFragments(), rhs.getCountCmpdFragments())
            .append(this.getIsSelected(), rhs.getIsSelected())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final CmpdVO object)
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
            .append(this.getName(), object.getName())
            .append(this.getNsc(), object.getNsc())
            .append(this.getDistribution(), object.getDistribution())
            .append(this.getConf(), object.getConf())
            .append(this.getInventory(), object.getInventory())
            .append(this.getCas(), object.getCas())
            .append(this.getTargets(), object.getTargets())
            .append(this.getAliases(), object.getAliases())
            .append(this.getPrefix(), object.getPrefix())
            .append(this.getCmpdFragments(), object.getCmpdFragments())
            .append(this.getId(), object.getId())
            .append(this.getCmpdBioAssay(), object.getCmpdBioAssay())
            .append(this.getNscCmpdId(), object.getNscCmpdId())
            .append(this.getParentFragment(), object.getParentFragment())
            .append(this.getAdHocCmpdId(), object.getAdHocCmpdId())
            .append(this.getSets(), object.getSets())
            .append(this.getProjects(), object.getProjects())
            .append(this.getPlates(), object.getPlates())
            .append(this.getCountCmpdFragments(), object.getCountCmpdFragments())
            .append(this.getIsSelected(), object.getIsSelected())
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
            .append(this.getName())
            .append(this.getNsc())
            .append(this.getDistribution())
            .append(this.getConf())
            .append(this.getInventory())
            .append(this.getCas())
            .append(this.getTargets())
            .append(this.getAliases())
            .append(this.getPrefix())
            .append(this.getCmpdFragments())
            .append(this.getId())
            .append(this.getCmpdBioAssay())
            .append(this.getNscCmpdId())
            .append(this.getParentFragment())
            .append(this.getAdHocCmpdId())
            .append(this.getSets())
            .append(this.getProjects())
            .append(this.getPlates())
            .append(this.getCountCmpdFragments())
            .append(this.getIsSelected())
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
            .append("name", this.getName())
            .append("nsc", this.getNsc())
            .append("distribution", this.getDistribution())
            .append("conf", this.getConf())
            .append("inventory", this.getInventory())
            .append("cas", this.getCas())
            .append("targets", this.getTargets())
            .append("aliases", this.getAliases())
            .append("prefix", this.getPrefix())
            .append("cmpdFragments", this.getCmpdFragments())
            .append("id", this.getId())
            .append("cmpdBioAssay", this.getCmpdBioAssay())
            .append("nscCmpdId", this.getNscCmpdId())
            .append("parentFragment", this.getParentFragment())
            .append("adHocCmpdId", this.getAdHocCmpdId())
            .append("sets", this.getSets())
            .append("projects", this.getProjects())
            .append("plates", this.getPlates())
            .append("countCmpdFragments", this.getCountCmpdFragments())
            .append("isSelected", this.getIsSelected())
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

        final CmpdVO that = (CmpdVO)thatObject;

        return
            equal(this.getName(), that.getName())
            && equal(this.getNsc(), that.getNsc())
            && equal(this.getDistribution(), that.getDistribution())
            && equal(this.getConf(), that.getConf())
            && equal(this.getInventory(), that.getInventory())
            && equal(this.getCas(), that.getCas())
            && equal(this.getTargets(), that.getTargets())
            && equal(this.getAliases(), that.getAliases())
            && equal(this.getPrefix(), that.getPrefix())
            && equal(this.getCmpdFragments(), that.getCmpdFragments())
            && equal(this.getId(), that.getId())
            && equal(this.getCmpdBioAssay(), that.getCmpdBioAssay())
            && equal(this.getNscCmpdId(), that.getNscCmpdId())
            && equal(this.getParentFragment(), that.getParentFragment())
            && equal(this.getAdHocCmpdId(), that.getAdHocCmpdId())
            && equal(this.getSets(), that.getSets())
            && equal(this.getProjects(), that.getProjects())
            && equal(this.getPlates(), that.getPlates())
            && equal(this.getCountCmpdFragments(), that.getCountCmpdFragments())
            && equal(this.getIsSelected(), that.getIsSelected())
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

    // CmpdVO value-object java merge-point
}