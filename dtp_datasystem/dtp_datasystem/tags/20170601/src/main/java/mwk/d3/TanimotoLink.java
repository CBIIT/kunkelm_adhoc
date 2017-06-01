/*
 
 
 
 */
package mwk.d3;

import mwk.datasystem.vo.CmpdVO;

/**
 *
 * @author mcwkunkel
 */
public class TanimotoLink {

    public int source;
    public int target;
    
    public Double ap;// atompairbv_fp_tanimoto
    public Double fm;// featmorganbv_fp_tanimoto
    public Double l;// layered_fp_tanimoto
    public Double mc;// maccs_fp_tanimoto
    public Double m;// morganbv_fp_tanimoto
    public Double r;// rdkit_fp_tanimoto
    public Double to;// torsionbv_fp_tanimoto

    public String color;

    public TanimotoLink() {
    }

}
