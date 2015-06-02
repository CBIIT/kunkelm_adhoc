// license-header java merge-point
//
/**
 * @author Generated on 05/29/2015 08:30:20-0400 Do not modify by hand!
 *
 * TEMPLATE: ValueObject.vsl in andromda-java-cartridge. MODEL CLASS:
 * AndroMDAModel::MicroXenoModel::mwk.microxeno::vo::PassageCollapser
 * STEREOTYPE: ValueObject
 */
package utils;

import java.io.Serializable;
import mwk.microxeno.vo.AffymetrixDataVO;
import mwk.microxeno.vo.AffymetrixIdentifierVO;
import mwk.microxeno.vo.TumorVO;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class to allow collation of replicates by passage
 */
public class ReplicateAggregator
        implements Serializable, Comparable<ReplicateAggregator> {

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -3508963671500748565L;

    // Class attributes
    protected AffymetrixIdentifierVO affymetrixIdentifier;
    /**
     * TODO: Model Documentation for attribute tumor
     */
    protected TumorVO tumor;
    /**
     * TODO: Model Documentation for attribute passage
     */
    protected String passage;
    /**
     * TODO: Model Documentation for attribute replicate
     */
    protected AffymetrixDataVO value;

    /**
     * Default Constructor with no properties
     */
    public ReplicateAggregator() {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     *
     * @param affymetrixIdentifierIn AffymetrixIdentifierVO
     * @param tumorIn TumorVO
     * @param passageIn String
     */
    public ReplicateAggregator(final AffymetrixIdentifierVO affymetrixIdentifierIn, 
            final TumorVO tumorIn, 
            final String passageIn, 
            final AffymetrixDataVO valueIn) {
        this.affymetrixIdentifier = affymetrixIdentifierIn;
        this.tumor = tumorIn;
        this.passage = passageIn;
        this.value = valueIn;
    }

    /**
     * TODO: Model Documentation for attribute affymetrixIdentifier Get the
     * affymetrixIdentifier Attribute
     *
     * @return affymetrixIdentifier AffymetrixIdentifierVO
     */
    public AffymetrixIdentifierVO getAffymetrixIdentifier() {
        return this.affymetrixIdentifier;
    }

    /**
     *
     * @param value AffymetrixIdentifierVO
     */
    public void setAffymetrixIdentifier(final AffymetrixIdentifierVO value) {
        this.affymetrixIdentifier = value;
    }

    /**
     * TODO: Model Documentation for attribute tumor Get the tumor Attribute
     *
     * @return tumor TumorVO
     */
    public TumorVO getTumor() {
        return this.tumor;
    }

    /**
     *
     * @param value TumorVO
     */
    public void setTumor(final TumorVO value) {
        this.tumor = value;
    }

    /**
     * TODO: Model Documentation for attribute passage Get the passage Attribute
     *
     * @return passage String
     */
    public String getPassage() {
        return this.passage;
    }

    /**
     *
     * @param value String
     */
    public void setPassage(final String value) {
        this.passage = value;
    }

    public AffymetrixDataVO getValue() {
        return value;
    }

    public void setValue(AffymetrixDataVO value) {
        this.value = value;
    }
   

    /**
     * @param object to compare this object against
     * @return boolean if equal
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object) {
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        // Check if the same object instance
        if (object == this) {
            return true;
        }
        ReplicateAggregator rhs = (ReplicateAggregator) object;
        return new EqualsBuilder()
                .append(this.getAffymetrixIdentifier(), rhs.getAffymetrixIdentifier())
                .append(this.getTumor(), rhs.getTumor())
                .append(this.getPassage(), rhs.getPassage())
                .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final ReplicateAggregator object) {
        if (object == null) {
            return -1;
        }
        // Check if the same object instance
        if (object == this) {
            return 0;
        }
        return new CompareToBuilder()
                .append(this.getAffymetrixIdentifier(), object.getAffymetrixIdentifier())
                .append(this.getTumor(), object.getTumor())
                .append(this.getPassage(), object.getPassage())
                .toComparison();
    }

    /**
     * @return int hashCode value
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1249046965, -82296885)
                .append(this.getAffymetrixIdentifier())
                .append(this.getTumor())
                .append(this.getPassage())
                .toHashCode();
    }

    /**
     * @return String representation of object
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("affymetrixIdentifier", this.getAffymetrixIdentifier())
                .append("tumor", this.getTumor())
                .append("passage", this.getPassage())
                .append("value", this.getValue())
                .toString();
    }

}
