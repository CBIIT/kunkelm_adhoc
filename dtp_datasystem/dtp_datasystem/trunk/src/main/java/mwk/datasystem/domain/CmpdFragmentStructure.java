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
public abstract class CmpdFragmentStructure
    implements Serializable, Comparable<CmpdFragmentStructure>
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 7866210453678709014L;

    // Generate 6 attributes
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

    private String ctab;

    /**
     * 
     * @return this.ctab String
     */
    public String getCtab()
    {
        return this.ctab;
    }

    /**
     * 
     * @param ctabIn String
     */
    public void setCtab(String ctabIn)
    {
        this.ctab = ctabIn;
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
     * Returns <code>true</code> if the argument is an CmpdFragmentStructure instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof CmpdFragmentStructure))
        {
            return false;
        }
        final CmpdFragmentStructure that = (CmpdFragmentStructure)object;
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
     * Constructs new instances of {@link CmpdFragmentStructure}.
     */
    public static final class Factory
    {
        /**
         * Constructs a new instance of {@link CmpdFragmentStructure}.
         * @return new CmpdFragmentStructureImpl()
         */
        public static CmpdFragmentStructure newInstance()
        {
            return new CmpdFragmentStructureImpl();
        }


        /**
         * Constructs a new instance of {@link CmpdFragmentStructure}, taking all possible properties
         * (except the identifier(s))as arguments.
         * @param smiles String
         * @param inchi String
         * @param mol String
         * @param inchiAux String
         * @param ctab String
         * @return newInstance CmpdFragmentStructure
         */
        public static CmpdFragmentStructure newInstance(String smiles, String inchi, String mol, String inchiAux, String ctab)
        {
            final CmpdFragmentStructure entity = new CmpdFragmentStructureImpl();
            entity.setSmiles(smiles);
            entity.setInchi(inchi);
            entity.setMol(mol);
            entity.setInchiAux(inchiAux);
            entity.setCtab(ctab);
            return entity;
        }
    }

    /**
     * @see Comparable#compareTo
     */
    public int compareTo(CmpdFragmentStructure o)
    {
        int cmp = 0;
        if (this.getId() != null)
        {
            cmp = this.getId().compareTo(o.getId());
        }
        else
        {
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
            if (this.getCtab() != null)
            {
                cmp = (cmp != 0 ? cmp : this.getCtab().compareTo(o.getCtab()));
            }
        }
        return cmp;
    }
// HibernateEntity.vsl merge-point
}