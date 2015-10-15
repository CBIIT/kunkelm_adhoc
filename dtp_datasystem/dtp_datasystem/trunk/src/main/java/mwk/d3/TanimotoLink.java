/*
 
 
 
 */
package mwk.d3;

import mwk.datasystem.vo.CmpdVO;

/**
 *
 * @author mcwkunkel
 */
public class TanimotoLink {

    public int s;
    public int t;

    public String ap;//atompairbv_fp_tanimoto
    public String fm;//featmorganbv_fp_tanimoto
    public String l;//layered_fp_tanimoto
    public String mc;//maccs_fp_tanimoto
    public String m;//morganbv_fp_tanimoto
    public String r;//rdkit_fp_tanimoto
    public String to;//torsionbv_fp_tanimoto

    public String color;

    public TanimotoLink() {
    }

}
