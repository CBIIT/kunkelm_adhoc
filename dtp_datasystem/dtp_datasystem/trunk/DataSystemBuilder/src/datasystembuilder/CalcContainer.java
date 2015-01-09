/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasystembuilder;

/**
 *
 * @author mwkunkel
 */
public class CalcContainer {

    Double alogp;
    Integer hba;
    Integer hbd;
    Double psa;
    String mf;
    Double mw;
    //
    Integer countAtoms;
    Integer countHeavyAtoms;
    Integer countHydrogens;
    Integer countHalogens;
    Integer countRings;
    Integer countBonds;

    /**
     *
     */
    public CalcContainer() {
        alogp = 0.0;
        hba = 0;
        hbd = 0;
        psa = 0.0;
        mf = "notSet";
        mw = 0.0;
    }
}
