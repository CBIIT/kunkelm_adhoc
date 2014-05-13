// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: hibernate/HibernateEntity.vsl in andromda-hibernate-cartridge.
//
package mwk.datasystem.domain;

import java.io.Serializable;

/**
 * 
 */
// HibernateEntity.vsl annotations merge-point
public abstract class AdHocCmpdFragmentPChem
    implements Serializable, Comparable<AdHocCmpdFragmentPChem>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 6017542782082896123L;

    // Generate 9 attributes
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

    // Generate 1 associations
    /**
     * Returns <code>true</code> if the argument is an AdHocCmpdFragmentPChem instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof AdHocCmpdFragmentPChem))
        {
            return false;
        }
        final AdHocCmpdFragmentPChem that = (AdHocCmpdFragmentPChem)object;
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
     * Constructs new instances of {@link AdHocCmpdFragmentPChem}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link AdHocCmpdFragmentPChem}.
         * @return new AdHocCmpdFragmentPChemImpl()
         */
        public static AdHocCmpdFragmentPChem newInstance()
        {
            return new AdHocCmpdFragmentPChemImpl();
        }


        /**
         * Constructs a new instance of {@link AdHocCmpdFragmentPChem}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param mw Double
         * @param mf String
         * @param alogp Double
         * @param logd Double
         * @param hba Integer
         * @param hbd Integer
         * @param sa Double
         * @param psa Double
         * @return newInstance AdHocCmpdFragmentPChem
         */
        public static AdHocCmpdFragmentPChem newInstance(Double mw, String mf, Double alogp, Double logd, Integer hba, Integer hbd, Double sa, Double psa)
        {
            final AdHocCmpdFragmentPChem entity = new AdHocCmpdFragmentPChemImpl();
            entity.setMw(mw);
            entity.setMf(mf);
            entity.setAlogp(alogp);
            entity.setLogd(logd);
            entity.setHba(hba);
            entity.setHbd(hbd);
            entity.setSa(sa);
            entity.setPsa(psa);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(AdHocCmpdFragmentPChem o)
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
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}