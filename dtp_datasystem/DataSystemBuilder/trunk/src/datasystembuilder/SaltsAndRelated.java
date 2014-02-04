/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasystembuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import net.sf.jniinchi.INCHI_RET;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.smiles.SmilesParser;

/**
 *
 * @author mwkunkel
 */
public class SaltsAndRelated {

  private static int BATCH_FETCH_SIZE = 250;
  // common salts from NCGC Canonical Forms java code
  private static String[] distinctSalts = {
    "[RuH2+2]", "[RuH2+2]",
    "[Li+]", "[Li+]",
    "[LiH]", "[LiH]",
    "[Zr+2]", "[Zr+2]",
    "[Mo]", "[Mo]",
    "[Ru]", "[Ru]",
    "[NaH]", "[NaH]",
    "[Mg+2]", "[Mg+2]",
    "[MgH+]", "[MgH+]",
    "[MgH2]", "[MgH2]",
    "[Al]", "[Al]",
    "[Al-]", "[Al-]",
    "[Al+3]", "[Al+3]",
    "[SiH6+2]", "[SiH6+2]",
    "[K+]", "[K+]",
    "[Ca+2]", "[Ca+2]",
    "[KH]", "[KH]",
    "[KH2-]", "[KH2-]",
    "[CaH+]", "[CaH+]",
    "[CaH2]", "[CaH2]",
    "[Sc]", "[Sc]",
    "[Sc+3]", "[Sc+3]",
    "[Fe+]", "[Fe+]",
    "[Fe+3]", "[Fe+3]",
    "[Co+3]", "[Co+3]",
    "[Ni]", "[Ni]",
    "[Co+2]", "[Co+2]",
    "[Ni+2]", "[Ni+2]",
    "[Cu]", "[Cu]",
    "[Cu+2]", "[Cu+2]",
    "[Cu+]", "[Cu+]",
    "[Zn+2]", "[Zn+2]",
    "[ZnH2]", "[ZnH2]",
    "[Ga]", "[Ga]",
    "[Ga+3]", "[Ga+3]",
    "[AsH6+3]", "[AsH6+3]",
    "[Rb]", "[Rb]",
    "[Rb+]", "[Rb+]",
    "[Sr+2]", "[Sr+2]",
    "[Y+3]", "[Y+3]",
    "[Zr]", "[Zr]",
    "[Ag-]", "[Ag-]",
    "[Ag+]", "[Ag+]",
    "[PdH2+2]", "[PdH2+2]",
    "[PdH4]", "[PdH4]",
    "[Cd+2]", "[Cd+2]",
    "[CdH2]", "[CdH2]",
    "[In]", "[In]",
    "[In+3]", "[In+3]",
    "[SnH2+2]", "[SnH2+2]",
    "[Sb+3]", "[Sb+3]",
    "[SnH4]", "[SnH4]",
    "[Cs+]", "[Cs+]",
    "[Ba+2]", "[Ba+2]",
    "[La]", "[La]",
    "[La+3]", "[La+3]",
    "[Ce+3]", "[Ce+3]",
    "[Pr+3]", "[Pr+3]",
    "[Nd+3]", "[Nd+3]",
    "[Sm+3]", "[Sm+3]",
    "[Eu+3]", "[Eu+3]",
    "[Gd]", "[Gd]",
    "[Gd+3]", "[Gd+3]",
    "[Tb+3]", "[Tb+3]",
    "[Dy+3]", "[Dy+3]",
    "[Ho+3]", "[Ho+3]",
    "[Er+3]", "[Er+3]",
    "[Tm+3]", "[Tm+3]",
    "[Yb+3]", "[Yb+3]",
    "[Lu]", "[Lu]",
    "[Lu+3]", "[Lu+3]",
    "[Ta]", "[Ta]",
    "[W]", "[W]",
    "[Ir+]", "[Ir+]",
    "[Au+]", "[Au+]",
    "[PtH2+2]", "[PtH2+2]",
    "[PtH4]", "[PtH4]",
    "[Hg+2]", "[Hg+2]",
    "[HgH+]", "[HgH+]",
    "[HgH2]", "[HgH2]",
    "[Tl+]", "[Tl+]",
    "[Bi+3]", "[Bi+3]",
    "[PbH2+2]", "[PbH2+2]",
    "[PbH4]", "[PbH4]",
    "[BiH3]", "[BiH3]",
    "[Ti]", "[Ti]",
    "[Ti+2]", "[Ti+2]",
    "[V]", "[V]",
    "[Cr+3]", "[Cr+3]",
    "[O-]C([C@H](O)[C@H]([C@@H]([C@H](O)C([O-])=O)O)O)=O", "SACCHARATE/GLUCARATE",
    "[Mn+2]", "[Mn+2]",
    "[Fe]", "[Fe]",
    "[Fe+2]", "[Fe+2]",
    "[Ru+2]", "[Ru+2]",
    "[O-]C(=O)/C=C/C([O-])=O", "FUMARIC ACID/FUMARATE/MALEATE<<<<<<<<<<<<<<<<",
    "O=C([O-])/C=C/C(O)=O", "FUMARIC ACID",
    "OC(C[C@H](O)C(O)=O)=O", "MALIC ACID/MALATE",
    "NCCCC[C@H](N)C(=O)[O-]", "LYSINATE",
    "OC(=O)[C@@H]([C@@H](O)C([O-])=O)O", "TARTARIC ACID",
    "OC[C@H]1OC(O)[C@H](O)[C@@H](O)[C@@H]1O", "GLUCOSIDE",
    "O=C([O-])[C@@H]([C@@H](O)[C@@H]([C@@H](CO)O)O)O", "GLUCONATE/GLUCONIC ACID",
    "CNC[C@H](O)[C@H]([C@@H]([C@H](O)CO)O)O", "N-METHYLGLUCAMINE/MEGLUMINE",
    "CCCC[C@@H]1CC[C@@H](C(=O)[O-])CC1", "TRANS-4-BUTYLCYCLOHEXANECARBOXYLATE",
    "O=C([O-])[C@@H]([C@@H]([C@@H]([C@@H](O)[C@@H](CO)O)O)O)O", "GLUCOHEPTONATE",
    "O=[N+]([O-])O", "NITRATE",
    "N", "AMMONIA",
    "[OH-]", "WATER",
    "[NH4+]", "AMMONIA",
    "O", "WATER",
    "[RuH+]", "[RuH+]",
    "[Rh]", "[Rh]",
    "[Rh+]", "[Rh+]",
    "[RhH]", "[RhH]",
    "[Na+]", "[Na+]",
    "I", "I",
    "[F-]", "[F-]",
    "F", "F",
    "[Cl-]", "[Cl-]",
    "Cl", "Cl",
    "[H][H]", "MISC",
    "CO", "METHANOL",
    "CC#N", "ACETONITRILE*",
    "C[CH]C", "ISOPROPYL",
    "CCN", "ETHYLAMINE",
    "[Br-]", "[Br-]",
    "Br", "Br",
    "[I-]", "[I-]",
    "O=C[O-]", "FORMATE/FORMIC ACID",
    "O=CO", "FORMIC ACID",
    "CCO", "ETHANOL",
    "COC", "DIMETHYLETHER*",
    "CC[NH3+]", "ETHYLAMMONIUM",
    "[N-]=C=S", "THIOCYANATE",
    "N#C[S-]", "THIOCYANATE",
    "N#CS", "THIOCYANATE",
    "ClCCl", "METHYLENE CHLORIDE*",
    "CC(C)=O", "ACETONE",
    "CC(=O)[O-]", "ACETATE/ACETIC ACID",
    "NCCN", "ETHYLENEDIAMINE",
    "CC(C)O", "2-PROPANOL",
    "CC(=O)O", "ACETIC ACID",
    "CCCO", "1-PROPANOL",
    "NC(=O)[O-]", "CARBAMATE",
    "O=C([O-])[O-]", "CARBONATE",
    "NCCO", "ETHANOLAMINE",
    "C[N+](=O)[O-]", "NITROMETHANE*",
    "O=C([O-])O", "CARBONATE",
    "OB(O)O", "BORATE",
    "OCCO", "ETHYLENE GLYCOL*",
    "[NH3+]CCO", "MEA",
    "O=C(O)O", "CARBONATE",
    "O=[N+]([O-])[O-]", "NITRATE/MONONITRATE",
    "CS(C)=O", "DMSO",
    "[O-]P([O-])[O-]", "PHOSPHITE",
    "O=S([O-])[O-]", "SULPHITE",
    "O=S(O)O", "SULPHITE",
    "ClCCCl", "1,2-DICHLOROETHANE*",
    "ClC(Cl)Cl", "CHLOROFORM*",
    "NC1CC1", "CYCLOPROPYLAMINE",
    "CCCCC", "PENTANE*",
    "CCC(=O)C", "2-BUTANONE*",
    "O=CC([O-])=O", "GLYOXYLATE",
    "CC(C)(C)N", "TERT-BUTYLAMINE",
    "CCNCC", "DIETHYLAMINE",
    //"CC(C)(C)N", "T-BUTYLAMINE",ends up as duplicate in rdkit
    "CN(C)C=O", "DIMETHYLFORMAMIDE",
    "CCC(=O)[O-]", "ISOPROPIONATE/PROPIONATE",
    "CCC(O)C", "2-BUTANOL*",
    "CCOCC", "DIETHYLETHER",
    "CCC(=O)O", "PROPIONIC ACID*",
    "CCCCO", "1-BUTANOL*",
    "NCCCN", "DIAMINOPROPANE",
    "CC(C)(C)O", "T-BUTYL ALCOHOL*",
    "CC[NH2+]CC", "DIETHYLAMMONIUM",
    "O=C([O-])CO", "GLYCOLATE",
    "OCCCO", "1,3-PROPANEDIOL",
    "O=C(CO)O", "GLYCOLIC ACID ",
    "F[B-](F)(F)F", "TETRAFLUROBORATE",
    "O=P([O-])([O-])[O-]", "ORTHOPHOSPHATE/PHOSPHATE",
    "O=P([O-])([O-])O", "PHOSPHATE",
    "CS(=O)(=O)[O-]", "METHANESULPHONATE/METHANSULPHONIC ACID",
    "O=[S+]([O-])([O-])[O-]", "SULPHURIC ACID",
    "CS(=O)(=O)O", "METHANSULPHONIC ACID/MESYLATE",
    "O=P([O-])(O)O", "PHOSPHATE",
    "[O-]S([O-])(=O)=O", "SULPHATE/SULPHURIC ACID",
    //"O=P(O)(O)O", "DIHYDROGEN PHOSPHATE",  ends up as duplicate in rdkit
    "O=S(=O)([O-])O", "SULPHURIC ACID",
    "NS(=O)(=O)O", "SULPHAMIC ACID*",
    "O=P(O)(O)O", "PHOSPHATE",
    "O=[S+]([O-])([O-])O", "SULPHURIC ACID",
    "O=[S+]([O-])(O)O", "SULPHURIC ACID",
    "OS(O)(=O)=O", "SULPHATE/SULPHURIC ACID",
    "O=S(=O)([O-])F", "FLUOROSULPHONATE",
    "[O-][Cl+3]([O-])([O-])[O-]", "PERCHLORATE",
    "[O-][Cl+3]([O-])([O-])O", "PERCHLORATE",
    "ClC(Cl)(Cl)Cl", "CARBON TETRACHLORIDE*",
    "C1CCOC1", "THF",
    "CCCCCC", "HEXANE*",
    "CCCC(=O)[O-]", "BUTYLATE/BUTYRATE/BUTANOATE",
    "CC(=O)N(C)C", "DIMETHYLACETAMIDE",
    "CC(C)C(=O)[O-]", "BUTYRIC ACID/ISOBUTYRATE",
    "COC(C)(C)C", "MTBE*",
    "[O-]C(=O)C([O-])=O", "OXALIC ACID/OXALATE",
    "O=C([O-])C(O)=O", "OXALIC ACID",
    "CC(O)C(=O)[O-]", "LACTATE/LACTIC ACID",
    "OCCCCO", "1,4-BUTANEDIOL",
    "CC(C(=O)O)O", "LACTIC ACID",
    "COCCOC", "DME*",
    "CCOC(=O)O", "ETHYL CARBONATE",
    "OC(=O)C(O)=O", "OXALATE/OXALIC ACID",
    "OCC(O)CO", "GLYCERIN*",
    "CCS(=O)(=O)[O-]", "ETHANESULPHONATE",
    "CCS(=O)(=O)O", "ESYLATE",
    "COS(=O)(=O)[O-]", "METHYLSULPHURIC ACID",
    "CNS(=O)(=O)O", "N-METHYLSULPHAMIC ACID*",
    "COS(=O)(=O)O", "METHYLSULPHATE/METHYLSULPHURIC ACID",
    "c1ccccc1", "BENZENE",
    "c1ccncc1", "PYRIDINE",
    "c1cc[nH+]cc1", "PYRIDINE",
    "C1CCCCC1", "CYCLOHEXANE",
    "C1CCNCC1", "PIPERIDINE",
    "C1CNCCN1", "PIPERAZINE ",
    "C1CC[NH2+]CC1", "PIPERIDINE",
    "C1NCCOC1", "MORPHOLINE",
    "C1COCCO1", "1,4-DIOXANE",
    "C1[NH2+]CCOC1", "MORPHOLINE",
    "CCCCCCC", "HEPTANE",
    "CCCCC(=O)[O-]", "VALERATE",
    "CCN(CC)CC", "TRIETHYLAMINE",
    "CC(C)(C)C(=O)[O-]", "TRIMETHYLACETATE/PIVALATE",
    "CC[NH+](CC)CC", "TRIETHYLAMINE",
    "[O-]C(=O)CC([O-])=O", "MALONATE",
    "O=C([O-])CC(O)=O", "MALONIC ACID",
    "C[N+](C)(C)CCO", "CHOLINE",
    "OC(=O)CC(O)=O", "MALONATE/MALONIC ACID",
    "OCCNCCO", "DIETHANOLAMINE",
    "O=C([O-])C(F)(F)F", "TRIFLUOROACETATE/TFA",
    "O=C(O)C(F)(F)F", "TFA",
    "O=S(=O)([O-])CCO", "2-HYDROXYETHANESULPHONATE",
    "CCOS(=O)(=O)[O-]", "ETHYLSULPHURIC ACID",
    "CCNS(=O)(=O)O", "N-ETHYLSULPHAMIC ACID*",
    "CCO[S+](=O)([O-])[O-]", "ETHYLSULPHURIC ACID",
    "OCCS(O)(=O)=O", "ISETHIONATE/z2-HYDROXYETHANESULPHONIC ACID",
    "CCOS(=O)(=O)O", "ETHYLSULPHURIC ACID",
    "Cc1ccccc1", "TOLUENE",
    "Cc1ccncc1", "4-PICOLINE",
    "[NH3+]c1ccccc1", "ANILINE",
    "NC1CCCCC1", "CYCLOHEXYLAMINE",
    "CN1C(=O)CCC1", "NMP*",
    "[NH3+]C1CCCCC1", "CYCLOHEXYLAMINE",
    "CN1CCOCC1", "N-METHYLMORPHOLINE",
    "C[NH+]1CCOCC1", "N-METHYLMORPHOLINE",
    "Clc1ccccc1", "CHLOROBENZENE*",
    "CC(C)CCC(=O)[O-]", "ISOCAPROATE",
    "CCCCCC(=O)[O-]", "HEXANOATE/CAPROATE",
    "CC(C)(C)CC(=O)[O-]", "3,3-DIMETHYLBUTYRATE",
    "[CH2]OC(=O)C(C)(C)C", "(PIVALOYLOXY)METHYL",
    "CC(=O)CCC([O-])=O", "LEVULINATE/4-OXOPENTANOATE",
    "[O-]C(CCC([O-])=O)=O", "SUCCINATE",
    "CN(C)CCC(=O)[O-]", "N,N-DIMETHYL-BETA-ALANINATE",
    "CC(=O)NCC([O-])=O", "N-ACETYLGLYCINATE/ACETURATE",
    "OC(=O)C=CC(O)=O", "MALEIC ACID/MALEATE/FUMARIC ACID/FUMARATE",
    "CC(=O)OC(C)(C)C", "T-BUTYL ACETATE",
    "CN(C)CCC(=O)O", "N,N-DIMETHYL-BETA-ALANINE",
    "O=C([O-])CCC(O)=O", "SUCCINIC ACID",
    "OC(CCC(O)=O)=O", "SUCCINIC ACID/SUCCINATE",
    "CCCNS(O)(=O)=O", "N-PROPYLSULPHAMIC ACID*",
    "N#Cc1ccccc1", "BENZONITRILE",
    "Cc1ccc(C)cc1", "XYLENE (PARA)",
    "Cc1cccc(C)c1", "XYLENE (META)",
    "Cc1ccccc1C", "XYLENE (ORTHO)",
    "O=C([O-])c1occc1", "FUROATE",
    "CCN1CCCCC1", "N-ETHYLPIPERIDINE*",
    "CN1CCN(C)CC1", "N.NPRIME-DIMETHYLPIPERAZINE",
    "OCCN1CCCC1", "1-PYRROLIDINEETHANOL",
    "CCCCCCC(=O)[O-]", "ENANTHATE/HEPTANOATE",
    "CC(=CC(O)=O)C(O)=O", "METHYLMALEIC ACID*",
    "CC[N+](CC)(CC)CC", "TEA",
    "OC(C=C(C(O)=O)O)=O", "HYDROXYMALEIC ACID*",
    "[O-]C(CC(O)C([O-])=O)=O", "MALATE",
    "OC(C(O)CC([O-])=O)=O", "MALIC ACID",
    "OC(CC(O)C([O-])=O)=O", "MALIC ACID",
    "NC(CC(O)=O)C(O)=O", "ASPARTIC ACID*",
    "COCCOCCOC", "DIGLYME*",
    "[O-]P([O-])(OP([O-])([O-])=O)=O", "DIPHOSPHATE",
    "O=C([O-])c1ccccc1", "BENZOIC ACID/BENZOATE",
    "O=C([O-])c1ccncc1", "ISONICOTINATE/ISONICOTINIC ACID",
    "O=C([O-])c1cccnc1", "NICOTINATE/NICOTINIC ACID*",
    "O=C(O)c1ccccc1", "BENZOIC ACID",
    "O=C(O)c1cccnc1", "NICOTINIC ACID*",
    "O=C(O)C1CCCCC1", "CYCLOHEXANCARBOXYLIC ACID* ",
    "O=C1CC(=O)NC(=O)N1", "BARBITURATE",
    "O=C(O)C1NC(=O)CC1", "PYROGLUTAMIC ACID",
    "CCCCCCCC(=O)[O-]", "OCTANOATE",
    "CCCCC(C(=O)[O-])CC", "ETHYLHEXANOATE",
    "CCC(CC([O-])=O)C([O-])=O", "ETHYLSUCCINATE",
    "[O-]C(=O)CCC(C([O-])=O)=O", "2-OXOGLUTARATE",
    "[O-]C(CCCCC([O-])=O)=O", "ADIPATE",
    "CCCCCCCC(=O)O", "OCTANOIC ACID",
    "OC(CCCCC(O)=O)=O", "ADIPIC ACID",
    "OC(=O)CCC(C(O)=O)=O", "OXOGLUTARIC ACID",
    "NCCCCC(N)C(=O)O", "LYSINE",
    "NC(CCC(O)=O)C(O)=O", "GLUTAMIC ACID*",
    "[O-]C(=O)C(O)C(C([O-])=O)O", "TARTRATE",
    "OCCN(CCO)CCO", "TRIETHANOLAMINE/2,2,2-NITRILOTRIETHANOL",
    "OC(C(O)C(O)=O)C(O)=O", "TARTRATE/TARTARIC ACID",
    "CN(C)P(N(C)C)N(C)C", "HMPT*",
    "[O-]S(CCS([O-])(=O)=O)(=O)=O", "1,2-ETHANEDISULPHONATE",
    "OS(CCS(O)(=O)=O)(=O)=O", "ETHANE-1,2-DISULPHONIC ACID*",
    "O=C(O)Cc1ccccc1", "PHENYLACETIC ACID",
    "O=C([O-])c1c(O)cccc1", "SALICYLATE/HYDROXYBENZOATE/SALICYLIC ACID",
    "O=C(O)Cc1ncccc1", "PYRIDYLACETATE",
    "O=C(O)c1c(O)cccc1", "SALICYLIC ACID",
    "O=C([O-])CCC1CCCC1", "CYCLOPENTANEPROPIONATE/CYPIONATE",
    "c1ccc(S([O-])(=O)=O)cc1", "BENZENESULPHONATE/BENZENESULPHONIC ACID",
    "c1ccc(S(O)(=O)=O)cc1", "BENZENESULPHONIC ACID",
    "[O-]c1cc(Cl)c(Cl)cc1Cl", "2,4,5-TRICHLOROPHENOLATE",
    "OC(CCCCCC(O)=O)=O", "PIMELIC ACID*",
    "CN(C)P(=O)(N(C)C)N(C)C", "HMPA*",
    "O=C([O-])C=Cc1ccccc1", "CINNAMATE",
    "O=C(O)C=Cc1ccccc1", "CINNAMIC ACID*",
    "O=C([O-])CCc1ccccc1", "3-PHENYLPROPIONATE",
    "O=C(O)CCc1ccccc1", "PHENYLPROPIONATE",
    "O=C([O-])C(O)c1ccccc1", "MANDELATE",
    "O=C(O)C(O)c1ccccc1", "MANDELIC ACID*",
    "NOc1ccccc1C(=O)[O-]", "AMINOSALICYLATE",
    "Nc1ccc(C(=O)O)c(O)c1", "4-AMINOSALICYLIC ACID*",
    "O=C([O-])c1c(O)c(O)ccc1", "DIHYDROXYBENZOATE",
    "O=C([O-])CCC1CCCCC1", "CYCLOHEXANEPROPIONATE",
    "O=C([O-])c1cc(=O)[nH]c(=O)[nH]1", "OROTIC ACID/OROTATE",
    "O=C(O)c1cc(=O)[nH]c(=O)[nH]1", "OROTIC ACID",
    "O=C(O)CCC1CCCCC1", "CYCLOHEXYLPROPIONATE",
    "O=C(O)C1CC(=O)NC(=O)N1", "OROTIC ACID",
    "Cc1ccc([S+](=O)([O-])[O-])cc1", "P-TOLUENESULPHONIC ACID",
    "Cc1ccc(S(=O)(=O)[O-])cc1", "P-TOLUENESULPHONIC ACID/P-TOLUENESULPHONATE",
    //"Cc1ccc(S(=O)(=O)O)cc1", "TOSILATE", ends up as duplicate in rdkit
    "Cc1ccccc1S(O)(=O)=O", "2-METHYLBENZENESULPHONIC",
    "Cc1ccc(S(=O)(=O)O)cc1", "TOSYLATE/P-TOLUENESULPHONIC ACID",
    "Cc1cccc(S(O)(=O)=O)c1", "3-METHYLBENZENESULPHONIC ACID*",
    "Nc1ccc(S(=O)(=O)O)cc1", "SULPHANILIC ACID",
    "C1CCC(NS([O-])(=O)=O)CC1", "N-CYCLOHEXYLSULPHAMATE",
    "C1CCC(NS(O)(=O)=O)CC1", "N-CYCLOHEXYLSULPHAMIC ACID*",
    "O=S(=O)([O-])c1ccc(Cl)cc1", "P-CHLOROBENZENESULPHONATE",
    "O=S(=O)(O)c1ccc(Cl)cc1", "CLOSILATE",
    "CCCCCCCCCC(=O)[O-]", "DECANOATE",
    "CCCCCCCCCC(=O)O", "DECANOIC ACID*",
    "OC(CCCCCCC(O)=O)=O", "SUBERIC ACID*",
    "N=C(N)NCCCC(N)C(=O)O", "ARGININE",
    "[O-]C(=O)c1cccc(C([O-])=O)c1", "ISOPHTHALATE",
    "[O-]C(=O)c1ccccc1C([O-])=O", "PHTHALATE",
    "[O-]C(=O)c1ccccc1[N+]([O-])=O", "NITROBENZOATE",
    "OC(=O)c1ccccc1C(O)=O", "PHTHALIC ACID*",
    "COc1c(OC)c(OC)ccc1", "3,4,5-TRIMETHOXYBENZENE",
    "[O-]C(C1C=CCCC1C([O-])=O)=O", "TETRAHYDROPHTHALATE",
    "O=C1OC(C(CO)O)C(O)=C1O", "ASCORBIC ACID/ASCORBATE",
    "CC12CCC(C(=O)[O-])(C=C1)CC2", "4-METHYLBICYCLO[2,2,2]OCT-2-ENE-1-CARBOXYLATE",
    "CCCCCCCCC=CC(=O)[O-]", "UNDECYLENATE",
    "C=CCCCCCCCCC(=O)[O-]", "UNDEC-10-ENOATE",
    "CCCCCCCCCCC(=O)[O-]", "UNDECANOATE",
    "OC(CCCCCCCC(O)=O)=O", "AZELAIC ACID*",
    "[O-]C(=O)CC(O)(C([O-])=O)CC([O-])=O", "CITRATE",
    "OC(CC(O)(CC(O)=O)C(O)=O)=O", "DIHYDROGEN CITRATE/CITRIC ACID",
    "c1ccc(C(NCC([O-])=O)=O)cc1", "HIPPURATE",
    "CC(Oc1ccccc1C([O-])=O)=O", "ACETYLSALICYLATE",
    "OC(CC(c1ccccc1O)=O)=O", "SALICYLOYLACETATE",
    "CC(Oc1ccccc1C(O)=O)=O", "2-ACETOXYBENZOIC ACID*",
    "Cc1cc(C)c(S(O)(=O)=O)c(C)c1", "MESITYLENESULPHONIC ACID",
    "O=C(O)c1ccc(S([O-])(=O)=O)cc1", "P-CARBOXYBENZENESULPHONATE",
    "C1CCC(NC2CCCCC2)CC1", "DICYCLOHEXYLAMINE",
    "C1CCC([NH2+]C2CCCCC2)CC1", "DICYCLOHEXYLAMMONIUM",
    "O=C(O)C12CC3CC(CC(C3)C1)C2", "ADAMANTANECARBOXYLIC ACID",
    "CCCCCCCCCCCC(=O)[O-]", "LAURATE",
    "CCCCCCCCCCCC(=O)O", "DODECANOIC ACID*",
    "O=C([O-])c1ccccc1OS(O)(=O)=O", "SULPHOSALICYLATE",
    "O=C([O-])c1c(O)c2c(cccc2)cc1", "1-HYDROXY-2-NAPHTHOATE",
    "O=C([O-])c1c(O)ccc2ccccc21", "HYDROXYNAPHTHOATE",
    "c1cc2ccc(S([O-])(=O)=O)cc2cc1", "2-NAPHTHALENESULPHONATE/2-NAPHTHALENESULPHONIC ACID",
    "CC(C)(C(O)C(NCCC([O-])=O)=O)CO", "PANTOTHENATE",
    "[O-]C(=O)c1cccc([N+]([O-])=O)c1[N+]([O-])=O", "DINITROBENZOATE",
    "COc1cc2ccccc2cc1C(=O)[O-]", "3-METHOXY-2-NAPHTHOATE",
    "CC1(C)C2CCC1(CS([O-])(=O)=O)C(=O)C2", "CAMPHORSULPHONATE",
    "[O-]c1c([N+]([O-])=O)cc([N+]([O-])=O)cc1[N+]([O-])=O", "PICRIC ACID",
    "[O-][N+](=O)c1cc([N+]([O-])=O)c(O)c([N+]([O-])=O)c1", "PICRIC ACID/PICRATE",
    "O=C(O)c1c(Oc2ccccc2)cccc1", "2-PHENOXYBENZOIC ACID*",
    "Cn1c(=O)c2[nH]c(Cl)nc2n(C(=O)[O-])c1=O", "8-CHLOROTHEOPHYLLINATE",
    "CCCCCCCCCCCCOS(O)(=O)=O", "LAURYLSULPHATE/N-DODECYLSULPHATE/DODECYLSULPHURIC ACID*",
    "CCCCCCCCCCCCCCCC(=O)[O-]", "PALMITATE",
    "O=C([O-])c1ccccc1C(=O)c1ccc(O)cc1", "O-(4-HYDROXYBENZOYL)BENZOATE",
    "Cc1cc(=O)oc2c1cc(O)c(OCC(O)=O)c2", "[(6-HYDROXY-4-METHYL-2-OXO-2H-1-BENZOPYRAN-7-YL)OXY]ACETATE",
    "Oc1cc2oc(=O)cc(CS([O-])(=O)=O)c2cc1O", "6,7-DIHYDROXYCOUMARIN-4-METHANESULPHONATE",
    "[O-]S(c1cccc2c(S([O-])(=O)=O)cccc12)(=O)=O", "1,5-NAPHTHALENEDISULPHONATE",
    "OS(c1cccc2c(S(O)(=O)=O)cccc12)(=O)=O", "1,5-NAPHTHALENE-DISULPHONIC ACID*",
    "CCCCCCCCCCCCCCOP(=O)(O)O", "TETRADECYL HYDROGEN PHOSPHATE",
    "Cn1c2ncn(CCS([O-])(=O)=O)c2c(=O)n(C)c1=O", "1,2,3,6-TETRAHYDRO-1,3-DIMETHYL-2,6-DIOXOPURINE-7-ETHANESULPHONATE",
    "CCCCCCCCC=CCCCCCCCC(=O)[O-]", "OLEATE",
    "CCCCCCCCCCCCCCCCCC(=O)[O-]", "STEARATE",
    "[O-]C(=O)CN(CCN(CC([O-])=O)CC([O-])=O)CC([O-])=O", "ETHYLENEDIAMINETETRAACETATE",
    "Cn1c2ncn(CCCS([O-])(=O)=O)c2c(=O)n(C)c1=O", "1,2,3,6-TETRAHYDRO-1,3-DIMETHYL-2,6-DIOXOPURINE-7-PROPANESULPHONATE",
    "Oc1ccccc1C(OCOC(c1c(O)cccc1)=O)=O", "METHYLENEDISALICYLATE",
    "CCCCCCCCCCCCCCCCCCOP(=O)(O)O", "OCTADECYL HYDROGEN PHOSPHATE",
    "CCCCCCCCCCCCCCCCCC(C(O)C([O-])=O)=O", "STEAROYL-GLYCOLATE",
    "c1ccc(-c2c(O)cc(C(=O)c3c(C([O-])=O)cccc3)cc2)cc1", "O-[(2-HYDROXY-4-BIPHENYLYL)-CARBONYL]BENZOATE",
    "CC(C)(C)c1ccc2c(S([O-])(=O)=O)c(C(C)(C)C)ccc2c1S([O-])(=O)=O", "2,6-DI-TERT-BUTYL-1,5-NAPHTHALENEDISULPHONATE",
    "CC(C)(C)c1cc2c(cc(C(C)(C)C)cc2S([O-])(=O)=O)c(S([O-])(=O)=O)c1", "3,7-DI-TERT-BUTYL-1,5-NAPHTHALENEDISULPHONATE",
    "[O-]C(=O)c1cc2ccccc2c(Cc2c(O)c(C([O-])=O)cc3ccccc32)c1O", "PAMOATE/EMBONATE",
    "Oc1c(Cc2c(O)c(C(O)=O)cc3c2cccc3)c2ccccc2cc1C(O)=O", "4,4-METHYLENEBIS[3-HYDROXY-2-NAPHTHOATE]"
  };

  /**
   *
   * @param postgresConn
   * @param postgresInsertConn
   * @throws Exception
   */
  public static void prepareSaltDefinitions(Connection postgresConn, Connection postgresInsertConn) throws Exception {

    Statement postgresStmt = null;
    PreparedStatement postgresPrepStmt = null;
    ResultSet rs = null;

    try {
      postgresStmt = postgresConn.createStatement();

      String sqlString = "drop table if exists known_salt";
      System.out.println(sqlString);
      postgresStmt.execute(sqlString);
      postgresConn.commit();

      sqlString = "create table known_salt(id int, salt_name varchar(1024), salt_mol mol, source_smiles varchar(1024), smiles varchar(1024), inchi varchar(1024))";
      System.out.println(sqlString);
      postgresStmt.execute(sqlString);
      postgresConn.commit();

      String saltName = "notSet";
      String sourceSmiles = "notSet";
      String inchi;

      String insertString = "insert into known_salt(id, salt_name, source_smiles, inchi) values (?,?,?,?)";

      postgresPrepStmt = postgresInsertConn.prepareStatement(insertString);
      for (int i = 0; i < distinctSalts.length; i++) {
        if (i % 2 == 0) {
          saltName = distinctSalts[i + 1];
          sourceSmiles = distinctSalts[i];
          inchi = makeInchi(sourceSmiles);
          System.out.println("name: " + saltName + " saltSmiles: " + sourceSmiles + " inchi: " + inchi);
          postgresPrepStmt.setInt(1, i);
          postgresPrepStmt.setString(2, saltName);
          postgresPrepStmt.setString(3, sourceSmiles);
          postgresPrepStmt.setString(4, inchi);
          postgresPrepStmt.addBatch();
        }
        postgresPrepStmt.executeBatch();
        postgresInsertConn.commit();
      }

      sqlString = "update known_salt set salt_mol = mol_from_smiles(source_smiles::cstring) where is_valid_smiles(source_smiles::cstring) = 't'";
      System.out.println(sqlString);
      postgresStmt.execute(sqlString);
      postgresConn.commit();

      sqlString = "update known_salt set smiles = mol_to_smiles(salt_mol)";
      System.out.println(sqlString);
      postgresStmt.execute(sqlString);
      postgresConn.commit();

      String[] sqlArray = new String[]{
        "create index known_salt_mol_idx on known_salt using gist(salt_mol)",
        "create index known_salt_smiles on known_salt(smiles)"
      };

      for (int i = 0; i < sqlArray.length; i++) {
        sqlString = sqlArray[i];
        System.out.println(sqlString);
        postgresStmt.execute(sqlString);
        postgresConn.commit();
      }

      postgresPrepStmt.close();
      postgresPrepStmt = null;

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      postgresConn.commit();
      postgresInsertConn.commit();
      if (rs != null) {
        try {
          rs.close();
          rs = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing rs");
        }
      }
      if (postgresStmt != null) {
        try {
          postgresStmt.close();
          postgresStmt = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing postgresStmt");
        }
      }
      if (postgresPrepStmt != null) {
        try {
          postgresPrepStmt.close();
          postgresPrepStmt = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing postgresPrepStmt");
        }
      }
    }

  }

  /*
   * This is fallow.  See RDKit.sql
   */
  public static void doRelated(Connection postgresConn, Connection postgresInsertConn) {

    Statement postgresStmt = null;
    Statement postgresStmt2 = null;

    ResultSet rs = null;
    ResultSet rs2 = null;

    PreparedStatement prepStmt = null;

    try {

      postgresStmt = postgresConn.createStatement();
      postgresStmt2 = postgresConn.createStatement();

      String[] sqlArray = new String[]{
        "drop table if exists related",
        "create table related (relation varchar(1024), ref_nsc int, ref_salt varchar(1024), rel_nsc int, rel_salt varchar(1024))"
      };

      for (int i = 0; i < sqlArray.length; i++) {
        String sqlStr = sqlArray[i];
        System.out.println(sqlStr);
        postgresStmt.execute(sqlStr);
        postgresConn.commit();
      }

      String prepStmtStr = "insert into related(relation, ref_nsc, ref_salt, rel_nsc, rel_salt) values(?,?,?,?,?)";
      prepStmt = postgresInsertConn.prepareStatement(prepStmtStr);

      String sqlString = "select nsc, smiles, matched_salt from parent_with_salt";

      rs = postgresStmt.executeQuery(sqlString);

      rs.setFetchDirection(ResultSet.FETCH_FORWARD);
      rs.setFetchSize(BATCH_FETCH_SIZE);
      ArrayList<IMolecule> fragmentList = new ArrayList<IMolecule>();

      while (rs.next()) {

        int nsc = rs.getInt("nsc");
        String smiles = rs.getString("smiles");
        String known_salt_name = rs.getString("matched_salt");

        String relString = "select nsc, matched_salt from parent_with_salt where frag_mol @= mol_from_smiles('" + smiles + "')";
        System.out.println("for nsc: " + nsc + " " + relString);

        rs2 = postgresStmt2.executeQuery(relString);

        // temporary list for managing all possible relations
        ArrayList<NscSaltPair> nscList = new ArrayList<NscSaltPair>();

        while (rs2.next()) {
          int thisNsc = rs2.getInt("nsc");
          String thisSaltName = rs2.getString("matched_salt");
          nscList.add(new NscSaltPair(thisNsc, thisSaltName));
        }

        for (NscSaltPair ref : nscList) {
          for (NscSaltPair rel : nscList) {
            prepStmt.setString(1, "mix");
            prepStmt.setInt(2, ref.nsc);
            prepStmt.setString(3, ref.matchedSalt);
            prepStmt.setInt(4, rel.nsc);
            prepStmt.setString(5, rel.matchedSalt);
            prepStmt.execute();
            postgresInsertConn.commit();
          }
        }

        // remove already-managed nsc from the list

      }

      rs.close();
      rs2.close();

      postgresStmt.close();
      postgresStmt2.close();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (postgresStmt != null) {
        try {
          postgresStmt.close();
          postgresStmt = null;
        } catch (SQLException ex) {
        }
      }
      if (prepStmt != null) {
        try {
          prepStmt.close();
          prepStmt = null;
        } catch (SQLException ex) {
        }
      }
      if (rs != null) {
        try {
          rs.close();
          rs = null;
        } catch (SQLException ex) {
        }
      }
    }
  }

  public static class NscSaltPair {

    public int nsc;
    public String matchedSalt;

    public NscSaltPair(int nsc, String knownSaltName) {
      this.nsc = nsc;
      this.matchedSalt = matchedSalt;
    }
  }

  private static String makeInchi(String smilesString) {

    String inchi = "";
    String inchiAuxInfo = "";

    try {

      SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

      IMolecule m = sp.parseSmiles(smilesString);

      InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(m);

      INCHI_RET ret = gen.getReturnStatus();
      
      if (ret == INCHI_RET.WARNING) {
        inchi = gen.getInchi();
        inchiAuxInfo = "InChI warning: " + ret.toString() + " [" + gen.getMessage() + "]";
      } else if (ret != INCHI_RET.OKAY) {
        inchi = "InChI failed: " + ret.toString() + " [" + gen.getMessage() + "]";
        inchiAuxInfo = "InChI failed: " + ret.toString() + " [" + gen.getMessage() + "]";
      } else {
        inchi = gen.getInchi();
        inchiAuxInfo = gen.getAuxInfo();
      }

      System.out.println("inchi: " + inchi);
      System.out.println("inchiAuxInfo: " + inchiAuxInfo);


    } catch (Exception e) {
      e.printStackTrace();
    }

    return inchi;

  }
}