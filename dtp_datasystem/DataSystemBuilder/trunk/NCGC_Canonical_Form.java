/*
			 PUBLIC DOMAIN NOTICE
		     NIH Chemical Genomics Center
	       National Human Genome Research Institute

This software/database is a "United States Government Work" under the
terms of the United States Copyright Act.  It was written as part of
the author's official duties as United States Government employee and
thus cannot be copyrighted.  This software/database is freely
available to the public for use. The NIH Chemical Genomics Center
(NCGC) and the U.S. Government have not placed any restriction on its
use or reproduction. 

Although all reasonable efforts have been taken to ensure the accuracy
and reliability of the software and data, the NCGC and the U.S.
Government do not and cannot warrant the performance or results that
may be obtained by using this software or data. The NCGC and the U.S.
Government disclaim all warranties, express or implied, including
warranties of performance, merchantability or fitness for any
particular purpose.

Please cite the authors in any work or product based on this material.

*/

package gov.nih.ncgc.chem;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import chemaxon.formats.MolImporter;
import chemaxon.marvin.calculations.TautomerizationPlugin;
import chemaxon.marvin.calculations.pKaPlugin;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.reaction.Reactor;
import chemaxon.struc.Molecule;

public class CanonicalForm {

    static private TautomerizationPlugin tautomerPlugin;
    static private Reactor charge1Reactor;
    static private Reactor charge2Reactor;
    static private Reactor waterReactor;
    static private Reactor imidazoleHReactor;
    static private Reactor imidazoleH2Reactor;
    static private pKaPlugin pkaPlugin;

    static private String[] commonSalts = {
	"CC([O-])=O", "ACETATE",
	"CC(=O)NCC([O-])=O", "ACETURATE",
	"CC(=O)NCC([O-])=O", "N-ACETYLGLYCINATE",
	"CC(=O)Oc1ccccc1C([O-])=O", "ACETYLSALICYLATE",
	"[O-]C(=O)CCCCC([O-])=O", "ADIPATE",
	"NOc1ccccc1C([O-])=O", "AMINOSALICYLATE",
	"N[C@@H](CCCNC(N)=N)C(O)=O", "ARGININE",
	"OC[C@H](O)[C@H]1OC(=O)C(O)=C1O", "ASCORBATE",
	"O=C1CC(=O)NC(=O)N1", "BARBITURATE",
	"[O-]S(=O)(=O)c1ccccc1", "BENZENESULFONATE",
	"[O-]C(=O)c1ccccc1", "BENZOATE",
	"OC(C(O)C(O)=O)C(O)=O", "BITARTRATE",
	"OB(O)O", "BORATE",
	"CCCC([O-])=O", "butanoate",
	"CC(=O)OC(C)(C)C", "t-BUTYL ACETATE",
	"CC(C)(C)N", "t-BUTYLAMINE",
	"CCCC([O-])=O", "BUTYLATE",
	"CCCC([O-])=O", "BUTYRATE",
	"CC1(C)C2CCC1(CS([O-])(=O)=O)C(=O)C2", "CAMPHOR-10-SULFONATE",
	"CCCCCC([O-])=O", "CAPROATE",
	"NC([O-])=O", "CARBAMATE",
	"[O-]C([O-])=O", "CARBONATE",
	"[O-]S(=O)(=O)c1ccc(Cl)cc1", "p-CHLOROBENZENESULFONATE",
	"[O-]S(=O)(=O)c1ccc(Cl)cc1", "p-CHLOROBENZENESULPHONATE",
	"Cn1c(=O)n(C([O-])=O)c2nc(Cl)[nH]c2c1=O", "8-CHLOROTHEOPHYLLINATE",
	"C[N+](C)(C)CCO", "CHOLINE",
	"[O-]C(=O)\\C=C\\c1ccccc1", "CINNAMATE",
	"OC(CC([O-])=O)(CC([O-])=O)C([O-])=O", "CITRATE",
	"OS(=O)(=O)c1ccc(Cl)cc1", "CLOSILATE",
	"[O-]C(=O)CCC1CCCCC1", "CYCLOHEXANEPROPIONATE",
	"[NH3+]C1CCCCC1", "CYCLOHEXYLAMMONIUM",
	"OC(=O)CCC1CCCCC1", "CYCLOHEXYLPROPIONATE",
	"[O-]S(=O)(=O)NC1CCCCC1", "N-CYCLOHEXYLSULFAMATE",
	"[O-]S(=O)(=O)NC1CCCCC1", "N-CYCLOHEXYLSULPHAMATE",
	"[O-]C(=O)CCC1CCCC1", "CYCLOPENTANEPROPIONATE",
	"[O-]C(=O)CCC1CCCC1", "CYPIONATE",
	"CCCCCCCC([O-])=O", "OCTANOATE",
	"CCCCCCCCCC([O-])=O", "DECANOATE",
	"C1CCC(CC1)[NH2+]C2CCCCC2", "DICYCLOHEXYLAMMONIUM",
	"OCCNCCO", "DIETHANOLAMINE",
	"CCNCC", "DIETHYLAMINE",
	"CC[NH2+]CC", "DIETHYLAMMONIUM",
	"OC(=O)CC(O)(CC(O)=O)C(O)=O", "DIHYDROGEN CITRATE",
	"OP(O)(O)=O", "DIHYDROGEN PHOSPHATE",
	"Oc1cccc(C([O-])=O)c1O", "DIHYDROXYBENZOATE",
	"CN(C)CCC(O)=O", "N,N-DIMETHYL-beta-ALANINE",
	"[O-]C(=O)c1cccc(c1[N+]([O-])=O)[N+]([O-])=O", "DINITROBENZOATE",
	"[O-]P([O-])(=O)OP([O-])([O-])=O", "DIPHOSPHATE",
	"Oc1c(cc2ccccc2c1Cc3c(O)c(cc4ccccc34)C([O-])=O)C([O-])=O", "EMBONATE",
	"CCCCCCC([O-])=O", "ENANTHATE",
	"CCS(O)(=O)=O", "ESYLATE",
	"[O-]S(=O)(=O)CCS([O-])(=O)=O", "1,2-ETHANEDISULFONATE",
	"[O-]S(=O)(=O)CCS([O-])(=O)=O", "1,2-ETHANEDISULPHONATE",
	"CCS([O-])(=O)=O", "ETHANESULFONATE",
	"CCS([O-])(=O)=O", "ETHANESULPHONATE",
	"NCCO", "ETHANOLAMINE",
	"CCN", "ETHYLAMINE",
	"CC[NH3+]", "ETHYLAMMONIUM",
	"NCCN", "ETHYLENEDIAMINE",
	"CCCCC(CC)C([O-])=O", "ETHYLHEXANOATE",
	"CCC(CC([O-])=O)C([O-])=O", "ETHYLSUCCINATE",
	"[O-]S(F)(=O)=O", "FLUOROSULFONATE",
	"[O-]S(F)(=O)=O", "FLUOROSULPHONATE",
	"[O-]C=O", "FORMATE",
	"[O-]C(=O)\\C=C\\C([O-])=O", "FUMARATE",
	"[O-]C(=O)c1ccco1", "FUROATE",
	"O[C@@H]([C@H](O)[C@@H](O)C([O-])=O)[C@H](O)C([O-])=O", "GLUCARATE",
	"OC[C@@H](O)[C@H](O)[C@@H](O)[C@@H](O)[C@@H](O)C([O-])=O", "GLUCOHEPTONATE",
	"OC[C@@H](O)[C@@H](O)[C@H](O)[C@@H](O)C([O-])=O", "GLUCONATE",
	"OC[C@H]1OC(O)[C@H](O)[C@@H](O)[C@@H]1O", "GLUCOSIDE",
	"OCC([O-])=O", "GLYCOLATE",
	"[O-]C(=O)C=O", "GLYOXYLATE",
	"CCCCCCC([O-])=O", "HEPTANOATE",
	"CCCCCC([O-])=O", "HEXANOATE",
	"[O-]C(=O)CNC(=O)c1ccccc1", "HIPPURATE",
	"OC(=O)\\C=C\\C(O)=O", "FUMARATE",
	"OC(CC(O)=O)C(O)=O", "MALATE",
	"OC(=O)\\C=C/C(O)=O", "MALEATE",
	"OC(=O)CC(O)=O", "MALONATE",
	"OC(=O)C(O)=O", "OXALATE",
	"OC(=O)CCC(O)=O", "SUCCINATE",
	"OS(O)(=O)=O", "SULFATE",
	"OS(O)=O", "SULFITE",
	"OS(O)(=O)=O", "SULPHATE",
	"OS(O)=O", "SULPHITE",
	"OC(C(O)C(O)=O)C(O)=O", "TARTRATE",
	"Oc1ccccc1C([O-])=O", "HYDROXYBENZOATE",
	"Oc1ccc(cc1)C(=O)c2ccccc2C([O-])=O", "o-(4-HYDROXYBENZOYL)BENZOATE",
	"OCCS([O-])(=O)=O", "2-HYDROXYETHANESULFONATE",
	"OCCS([O-])(=O)=O", "2-HYDROXYETHANESULPHONATE",
	"Oc1ccc2ccccc2c1C([O-])=O", "HYDROXYNAPHTHOATE",
	"OCCS(O)(=O)=O", "ISETHIONATE",
	"CC(C)C([O-])=O", "ISOBUTYRATE",
	"CC(C)CCC([O-])=O", "ISOCAPROATE",
	"[O-]C(=O)c1ccncc1", "ISONICOTINATE",
	"[O-]C(=O)c1cccc(c1)C([O-])=O", "ISOPHTHALATE",
	"CCC([O-])=O", "ISOPROPIONATE",
	"C[CH]C", "ISOPROPYL",
	"CC(O)C([O-])=O", "LACTATE",
	"CCCCCCCCCCCC([O-])=O", "LAURATE",
	"CCCCCCCCCCCCOS(O)(=O)=O", "LAURILSULFATE",
	"CCCCCCCCCCCCOS(O)(=O)=O", "LAURILSULPHATE",
	"CCCCCCCCCCCCOS(O)(=O)=O", "LAURYLSULFATE",
	"CCCCCCCCCCCCOS(O)(=O)=O", "LAURYLSULPHATE",
	"CC(=O)CCC([O-])=O", "LEVULINATE",
	"NCCCC[C@H](N)C([O-])=O", "LYSINATE",
	"OC(CC([O-])=O)C([O-])=O", "MALATE",
	"[O-]C(=O)\\C=C/C([O-])=O", "MALEATE",
	"[O-]C(=O)CC([O-])=O", "MALONATE",
	"OC(C([O-])=O)c1ccccc1", "MANDELATE",
	"CNC[C@H](O)[C@@H](O)[C@H](O)[C@H](O)CO", "MEGLUMINE",
	"CS(O)(=O)=O", "MESYLATE",
	"CS([O-])(=O)=O", "METHANESULFONATE",
	"CS([O-])(=O)=O", "METHANESULPHONATE",
	"CC12CCC(CC1)(C=C2)C([O-])=O", "4-METHYLBICYCLO[2.2.2]OCT-2-ENE-1-CARBOXYLATE",
	"OC(=O)c1cc2ccccc2c(Cc3c(O)c(cc4ccccc34)C(O)=O)c1O", "4,4.-METHYLENEBIS(3-HYDROXY-2-NAPHTHOATE)",
	"Oc1ccccc1C(=O)OCOC(=O)c2ccccc2O", "METHYLENEDISALICYLATE",
	"CNC[C@H](O)[C@@H](O)[C@H](O)[C@H](O)CO", "N-METHYLGLUCAMINE",
	"COS(O)(=O)=O", "METHYLSULFATE",
	"COS(O)(=O)=O", "METHYLSULPHATE",
	"COS(O)(=O)=O", "METILSULFATE",
	"COS(O)(=O)=O", "METILSULPHATE",
	"[O-]C(=O)c1ccccc1", "MONOBENZOATE",
	"[O-][N+]([O-])=O", "MONONITRATE",
	"[O-]S(=O)(=O)c1cccc2c(cccc12)S([O-])(=O)=O", "1,5-NAPHTHALENEDISULFONATE",
	"[O-]S(=O)(=O)c1cccc2c(cccc12)S([O-])(=O)=O", "1,5-NAPHTHALENEDISULPHONATE",
	"[O-]S(=O)(=O)c1ccc2ccccc2c1", "2-NAPHTHALENESULFONATE",
	"[O-]S(=O)(=O)c1ccc2ccccc2c1", "2-NAPHTHALENESULPHONATE",
	"[O-]C(=O)c1cccnc1", "NICOTINATE",
	"[O-][N+]([O-])=O", "NITRATE",
	"[O-]C(=O)c1ccccc1[N+]([O-])=O", "NITROBENZOATE",
	"CCCCCCCC\\C=C/CCCCCCCC([O-])=O", "OLEATE",
	"[O-]C(=O)c1cc(=O)[nH]c(=O)[nH]1", "OROTATE",
	"[O-]P([O-])([O-])=O", "ORTHOPHOSPHATE",
	"[O-]C(=O)C([O-])=O", "OXALATE",
	"CC(=O)CCC([O-])=O", "4-OXOPENTANOATE",
	"CCCCCCCCCCCCCCCC([O-])=O", "PALMITATE",
	"Oc1c(cc2ccccc2c1Cc3c(O)c(cc4ccccc34)C([O-])=O)C([O-])=O", "PAMOATE",
	"CC(C)(CO)C(O)C(=O)NCCC([O-])=O", "PANTOTHENATE",
	"[O-]Cl(=O)(=O)=O", "PERCHLORATE",
	"OC(=O)CCc1ccccc1", "PHENYLPROPIONATE",
	"[O-]P([O-])([O-])=O", "PHOSPHATE",
	"[O-]P([O-])[O-]", "PHOSPHITE",
	"[O-]C(=O)c1ccccc1C([O-])=O", "PHTHALATE",
	"Oc1c(cc(cc1[N+]([O-])=O)[N+]([O-])=O)[N+]([O-])=O", "PICRATE",
	"CC(C)(C)C([O-])=O", "PIVALATE",
	"CCC([O-])=O", "PROPIONATE",
	"OC(=O)Cc1ccccn1", "PYRIDYLACETATE",
	"OCCN1CCCC1", "1-PYRROLIDINEETHANOL",
	"O[C@@H]([C@H](O)[C@@H](O)C([O-])=O)[C@H](O)C([O-])=O", "SACCHARATE",
	"Oc1ccccc1C([O-])=O", "SALICYLATE",
	"OC(=O)CC(=O)c1ccccc1O", "SALICYLOYLACETATE",
	"CCCCCCCCCCCCCCCCCC([O-])=O", "STEARATE",
	"[O-]C(=O)CCC([O-])=O", "SUCCINATE",
	"[O-]S([O-])(=O)=O", "SULFATE",
	"[O-]S([O-])=O", "SULFITE",
	"OS(=O)(=O)Oc1ccccc1C([O-])=O", "SULFOSALICYLATE",
	"[O-]S([O-])(=O)=O", "SULPHATE",
	"[O-]S([O-])=O", "SULPHITE",
	"OS(=O)(=O)Oc1ccccc1C([O-])=O", "SULPHOSALICYLATE",
	"OC(C(O)C([O-])=O)C([O-])=O", "TARTRATE",
	"CCCCCCCCCCCCCCOP(O)(O)=O", "TETRADECYL HYDROGEN PHOSPHATE",
	"[O-]C(=O)C1CCC=CC1C([O-])=O", "TETRAHYDROPHTHALATE",
	"[S-]C#N", "THIOCYANATE",
	"Cc1ccc(cc1)S([O-])(=O)=O", "p-TOLUENESULFONATE",
	"Cc1ccc(cc1)S([O-])(=O)=O", "p-TOLUENESULPHONATE",
	"Cc1ccc(cc1)S(O)(=O)=O", "TOSILATE",
	"Cc1ccc(cc1)S(O)(=O)=O", "TOSYLATE",
	"OCCN(CCO)CCO", "TRIETHANOLAMINE",
	"[O-]C(=O)C(F)(F)F", "TRIFLUOROACETATE",
	"CC(C)(C)C([O-])=O", "TRIMETHYLACETATE",
	"CCCCCCCCCCC([O-])=O", "UNDECANOATE",
	"[O-]C(=O)CCCCCCCCC=C", "UNDEC-10-ENOATE",
	"CCCCCCCC\\C=C/C([O-])=O", "UNDECYLENATE",
	"CCCCC([O-])=O", "VALERATE",
	"CC(=O)NCC([O-])=O", "N-Acetylglycinate",
	"N[C]1(N)=CC=C(\\C=C\\c2ccccc2)C(=C1)S(=O)(=O)OS([O-])(=O)=O", "4,4-Diaminostilbene-2,2-disulfonate",
	"[O-]S(=O)(=O)c1ccccc1", "Benzenesulfonate",
	"CC(C)(C)c1cc(c2cc(cc(c2c1)S([O-])(=O)=O)C(C)(C)C)S([O-])(=O)=O", "3,7-di-tert-butyl-1,5-naphthalenedisulfonate",
	"CC1(C)C2CCC1(CS([O-])(=O)=O)C(=O)C2", "Camphorsulfonate",
	"OC(=O)c1ccc(cc1)S([O-])(=O)=O", "p-Carboxybenzenesulfonate",
	"CC12CCC(CC1)(C=C2)C([O-])=O", "4-Methylbicyclo[2.2.2]oct-2-ene-1-carboxylate",
	"[O-]C(=O)CCC1CCCC1", "Cyclopentanepropionate",
	"[O-]S(=O)(=O)c1ccc(Cl)cc1", "p-Chlorobenzenesulfonate",
	"Cc1cc(=O)oc2cc(OCC(O)=O)c(O)cc12", "[(6-hydroxy-4-methyl-2-oxo-2H-1-benzopyran-7-yl)oxy]acetate",
	"Oc1cc2oc(=O)cc(CS([O-])(=O)=O)c2cc1O", "6,7-Dihydroxycoumarin-4-methanesulfonate",
	"CC(C)(C)c1ccc2c(c(ccc2c1S([O-])(=O)=O)C(C)(C)C)S([O-])(=O)=O", "2,6-di-tert-butyl-1,5-naphthalenedisulfonate",
	"OCCNCCO", "Diethanolamine",
	"[O-]S(=O)(=O)CCS([O-])(=O)=O", "1,2-Ethanedisulfonate",
	"OC(=O)c1cc2ccccc2c(Cc3c(O)c(cc4ccccc34)C(O)=O)c1O", "4,4-Methylenebis(3-hydroxy-2-naphthoate)",
	"CCCCCCC([O-])=O", "Heptanoate",
	"CCS([O-])(=O)=O", "Ethanesulfonate",
	"Oc1cc(ccc1-c2ccccc2)C(=O)c3ccccc3C([O-])=O", "o-[(2-hydroxy-4-biphenylyl)-carbonyl]benzoate",
	"OC[C@@H](O)[C@H](O)[C@@H](O)[C@@H](O)[C@@H](O)C([O-])=O", "Glucoheptonate",
	"Oc1ccc(cc1)C(=O)c2ccccc2C([O-])=O", "o-(4-hydroxybenzoyl)benzoate",
	"OCCS([O-])(=O)=O", "2-Hydroxyethanesulfonate",
	"CCCCCCCCCCCCOS(O)(=O)=O", "n-Dodecylsulfate",
	"COc1cccc(OC)c1OC", "3,4,5-Trimethoxybenzene",
	"CS([O-])(=O)=O", "Methanesulfonate",
	"COc1cc2ccccc2cc1C([O-])=O", "3-methoxy-2-naphthoate",
	"COS(O)(=O)=O", "Methylsulfate",
	"[O-]S(=O)(=O)c1cccc2c(cccc12)S([O-])(=O)=O", "1,5-Naphthalenedisulfonate",
	"[O-]S(=O)(=O)c1ccc2ccccc2c1", "2-Naphthalenesulfonate",
	"NCCO", "Ethanolamine",
	"[O-]C(=O)CCC(=O)C([O-])=O", "2-Oxoglutarate",
	"CC(C)(C)C([O-])=O", "Trimethylacetate",
	"[CH2]OC(=O)C(C)(C)C", "(Pivaloyloxy)methyl",
	"CCCCCCCCCCCCCCCCCC(=O)C(O)C([O-])=O", "Stearoyl-glycolate",
	"CC(=O)OC(C)(C)C", "t-butyl acetate",
	"Cn1c(=O)n(C([O-])=O)c2nc(Cl)[nH]c2c1=O", "8-Chlorotheophyllinate",
	"Cn1c2ncn(CCCS([O-])(=O)=O)c2c(=O)n(C)c1=O", "1,2,3,6-Tetrahydro-1,3-dimethyl-2,6-dioxopurine-7-propanesulfonate",
	"Cn1c2ncn(CCS([O-])(=O)=O)c2c(=O)n(C)c1=O", "1,2,3,6-Tetrahydro-1,3-dimethyl-2,6-dioxopurine-7-ethanesulfonate",
	"Cc1ccc(cc1)S([O-])(=O)=O", "p-Toluenesulfonate",
	"[O-]c1cc(Cl)c(Cl)cc1Cl", "2,4,5-Trichlorophenolate",
	"OCCN(CCO)CCO", "Triethanolamine",
	"CC(=O)NCC([O-])=O", "N-acetylglycinate",
	"[O-]S(=O)(=O)c1ccccc1", "benzenesulfonate",
	"CCCC[C@H]1CC[C@@H](CC1)C([O-])=O", "trans-4-butylcyclohexanecarboxylate",
	"CC1(C)C2CCC1(CS([O-])(=O)=O)C(=O)C2", "camphorsulfonate",
	"CCCCCC([O-])=O", "hexanoate",
	"[O-]S(=O)(=O)c1ccc(Cl)cc1", "p-chlorobenzenesulfonate",
	"CC12CCC(CC1)(C=C2)C([O-])=O", "4-methylbicyclo[2,2,2]oct-2-ene-1-carboxylate",
	"[O-]C(=O)CCC1CCCC1", "cyclopentanepropionate",
	"CN(C)CCC([O-])=O", "N,N-dimethyl-beta-alaninate",
	"OCCNCCO", "diethanolamine",
	"CCCCCCCCCCCCCCCCCCOP(O)(O)=O", "octadecyl hydrogen phosphate",
	"NCCN", "ethylenediamine",
	"[O-]C(=O)CN(CCN(CC([O-])=O)CC([O-])=O)CC([O-])=O", "ethylenediaminetetraacetate",
	"[O-]S(=O)(=O)CCS([O-])(=O)=O", "1,2-ethanedisulfonate",
	"CCCCCCC([O-])=O", "heptanoate",
	"OCCN1CCCC1", "1-pyrrolidineethanol",
	"CC(C)(C)N", "tert-butylamine",
	"CCS([O-])(=O)=O", "ethanesulfonate",
	"CCOC(O)=O", "ethyl carbonate",
	"CCCCCCCCCCCCCCOP(O)(O)=O", "TETRADECYL HYDROGEN PHOSPHATE",
	"Oc1ccc(cc1)C(=O)c2ccccc2C([O-])=O", "o-(p-hydroxybenzoyl)benzoate",
	"OCCS([O-])(=O)=O", "2-hydroxyethanesulfonate",
	"CNC[C@H](O)[C@@H](O)[C@H](O)[C@H](O)CO", "N-methylglucamine",
	"CS([O-])(=O)=O", "methanesulfonate",
	"[O-]S(=O)(=O)c1ccc2ccccc2c1", "2-naphthalenesulfonate",
	"OC(=O)c1cc2ccccc2c(Cc3c(O)c(cc4ccccc34)C(O)=O)c1O", "4,4-methylenebis[3-hydroxy-2-naphthoate]",
	"[O-]C(=O)CCc1ccccc1", "3-phenylpropionate",
	"CC(=O)OC(C)(C)C", "tert-butyl acetate",
	"CC(C)(C)CC([O-])=O", "3,3-dimethylbutyrate",
	"Cc1ccc(cc1)S([O-])(=O)=O", "p-toluenesulfonate",
	"[O-]C(=O)C(F)(F)F", "trifluoroacetate",
	"OCCN(CCO)CCO", "triethanolamine",
	"OCCN(CCO)CCO", "2,2,2-nitrilotriethanol",
	"Oc1c(ccc2ccccc12)C([O-])=O", "1-hydroxy-2-naphthoate"
    };

    static public Molecule getCanonicalTautomer(Molecule mol) throws PluginException {
	if (tautomerPlugin == null) {
	    tautomerPlugin = new TautomerizationPlugin();

//	    set plugin params
	    Properties params = new Properties();
//	    params.setProperty("single", "true");
	    params.setProperty("pH", "7.4");
//	    params.setProperty("type", "structure");
//	    params.setProperty("max", "1");
	    params.setProperty("canonical", "true");
//	    params.setProperty("dominants", "true");
	    tautomerPlugin.setParameters(params);
	}

//	mol.dearomatize();
//	tautomerPlugin.standardize(mol);
	tautomerPlugin.setMolecule(mol);
	try {
	    tautomerPlugin.run();
	    if (tautomerPlugin.getStructureCount() > 0) {
		Molecule taut = tautomerPlugin.getStructure(0);
		taut.aromatize();
		String canonical = taut.toFormat("smiles:u");
		int best = 0;
		for (int i=0; i<tautomerPlugin.getStructureCount(); i++) {
		    taut = tautomerPlugin.getStructure(i);
		    //taut.aromatize();
		    //System.out.println(mol.getName()+i+"\t"+taut.toFormat("smiles:u")+"\t"+tautomerPlugin.getRemark());
		    if (taut.toFormat("smiles:u").compareTo(canonical) < 0) {
			canonical = taut.toFormat("smiles:u");
			best = i;
		    }
		}
		return tautomerPlugin.getStructure(best);
	    }
	} catch (Exception e) {
//	    Illegal argument exception */
//	    java.lang.IllegalArgumentException: Illegal value -1 specified for the number of
//	    implicit hydrogens.
//	    at chemaxon.struc.MolAtom.setNonQueryImplicitHcount(MolAtom.java:1674)
//	    at chemaxon.struc.MolAtom.setImplicitHcount(MolAtom.java:1652)
//	    at chemaxon.calculations.Tautomerization.addNchg(Tautomerization.java:41
//	    69)
//	    at chemaxon.calculations.Tautomerization.calcChargeConjugation(Tautomeri
//	    zation.java:4096)
//	    at chemaxon.calculations.Tautomerization.setTauOrder(Tautomerization.jav
//	    a:3939)
//	    at chemaxon.calculations.Tautomerization.calculateDACouples(Tautomerizat
//	    ion.java:3529)
//	    at chemaxon.calculations.Tautomerization.createDACouples(Tautomerization
//	    .java:3422)
//	    at chemaxon.marvin.calculations.MultiformPlugin.run(MultiformPlugin.java
//	    :266)
//	    at gov.nih.ncgc.chem.CanonicalForm.getCanonicalTautomer(CanonicalForm.ja
//	    va:38)
//	    at gov.nih.ncgc.chem.CanonicalForm.main(CanonicalForm.java:149)


//	    ArrayIndexOutOfBoundsException */
	    ;
	}
	return mol;
    }

    static public Molecule setCharges(Molecule mol) throws Exception {
	if (pkaPlugin == null) {
	    pkaPlugin = new pKaPlugin();
//	    set plugin params
	    Properties params = new Properties();
	    params.setProperty("type", "pKa");
	    params.setProperty("mode", "macro");
	    params.setProperty("upper", "7.4");
	    params.setProperty("lower", "7.4");
	    params.setProperty("step", "0.0");
	    pkaPlugin.setParameters(params);
	    pkaPlugin.setpH(7.4);
	}
	pkaPlugin.standardize(mol);
	pkaPlugin.setMolecule(mol);
	try {
	    pkaPlugin.run();
	} catch (Exception e) {e.printStackTrace();}
	if (pkaPlugin.getMsCount() > 0)
	    return pkaPlugin.getMsMolecule(0);
	return mol;
    }

    static public Molecule fixZwitterionCharges(Molecule mol) throws Exception {
	if (charge1Reactor == null) {
	    charge1Reactor = new Reactor();
	    charge1Reactor.setReaction
	    (MolImporter.importMol("[*+:1][*-:2]>>[*:1]=[*:2]"));
	    charge1Reactor.setTransform(true);
	}
	if (charge2Reactor == null) {
	    charge2Reactor = new Reactor();
	    charge2Reactor.setReaction
	    (MolImporter.importMol("[*+:1]=[*-:2]>>[*:1]#[*:2]"));
	    charge2Reactor.setTransform(true);
	}
	charge1Reactor.setReactant(mol);
	charge1Reactor.react();
	charge2Reactor.setReactant(mol);
	charge2Reactor.react();
	return mol;
    }

    static public Molecule removeWater(Molecule mol) throws Exception {
	if (waterReactor == null) {
	    waterReactor = new Reactor();
	    waterReactor.setReaction
	    (MolImporter.importMol("[OH2X2]>>"));
	    waterReactor.setTransform(true);
	}
	waterReactor.setReactant(mol);
	waterReactor.react();
	return mol;
    }

    /* ancient attempt to work around poorly drawn imadazoles */
    static public Molecule removeTautomerHydrogens(Molecule mol)
    throws Exception {
	if (imidazoleHReactor == null) {
	    imidazoleHReactor = new Reactor();
	    imidazoleHReactor.setReaction
	    (MolImporter.importMol
		    ("[nH1:1]1aaaa1>>[nH0:1]1aaaa1"));
	    imidazoleHReactor.setTransform(true);
	}
	imidazoleHReactor.setReactant(mol);
	imidazoleHReactor.react();
	if (imidazoleH2Reactor == null) {
	    imidazoleH2Reactor = new Reactor();
	    imidazoleH2Reactor.setReaction
	    (MolImporter.importMol
		    ("a1[nH1:1]aaa1>>a1[nH0:1]aaa1"));
	    imidazoleH2Reactor.setTransform(true);
	}
	imidazoleH2Reactor.setReactant(mol);
	imidazoleH2Reactor.react();
	return mol;
    }

    static public boolean saltException(Molecule mol) {
//	exception only for multicomponent molecules
	if (mol.toFormat("smiles").indexOf(".") == -1)
	    return false;

	Molecule molclone = mol.cloneMolecule();
	Molecule[] pieces = molclone.convertToFrags();
	int maxatomcount = 0;
	boolean haswierdelement = false;
	for (int i=0; i<pieces.length; i++) {
	    int atomcount = pieces[i].getAtomCount();
	    for (int j=0; j<atomcount; j++) {
		int atno = pieces[i].getAtom(j).getAtno();
		if (atno == 8)
		    atomcount--;
		else if (atno > 20 && atno != 34 && atno != 35 && atno != 53)
		    haswierdelement = true;
	    }
	    if (atomcount > maxatomcount)
		maxatomcount = atomcount;
	}
	if (haswierdelement || maxatomcount < 5)
	    return true;
	return false;
    }

    static public Molecule removeCommonSalts(Molecule mol) {
	String[] sa = mol.toFormat("smiles:au").split("[.]");
	String outmol = "";
	for (int i=0; i<sa.length; i++) {
	    boolean commonsalt = false;
	    if (sa[i].length() == 1)
		commonsalt = true;
	    else if (sa[i].charAt(0)=='[' && sa[i].charAt(sa[i].length()-1)==']' && sa[i].indexOf("]") == sa[i].length()-1)
		commonsalt = true;
	    for (int j=0; j<commonSalts.length; j++)
		if (j%2 == 0)
		    if (sa[i].equals(commonSalts[j]))
			commonsalt = true;
	    if (!commonsalt) {
		if (outmol.length() > 0)
		    outmol += ".";
		outmol += sa[i];
	    }
	}
	if (outmol.length() > 0)
	    try {
		return MolImporter.importMol(outmol);
	    } catch (Exception e) {e.printStackTrace();}
	    return mol;
    }

    public static void main(String[] args) {
	if (args.length < 1) {
	    System.err.println("USAGE: java CanonicalForm [input struct file] <output file>");
	    System.exit(1);
	}

	PrintStream outstream = System.out;
	if (args.length > 1) {
	    try{
		outstream = new PrintStream(new File(args[1]));
	    } catch (Exception e) {;}
	}

	try {
	    MolImporter mi = new MolImporter(args[0]);
	    Molecule mol = null;
	    mol = mi.read();
	    while (mol != null) {
		try {
		    String name = mol.getName();
		    String smiles = mol.toFormat("smiles");
		    if (smiles.length()<500) {
			mol.ungroupSgroups();
			mol.implicitizeHydrogens(0);
			mol = fixZwitterionCharges(mol);
			if (saltException(mol)) {
			    mol = removeWater(mol);
			}
			else {
			    mol = removeCommonSalts(mol);
			    mol = setCharges(mol);
			    mol = getCanonicalTautomer(mol);
			}
			mol.aromatize();
			mol.setName(name);
		    }
		    outstream.println(smiles + "\t" +
			    name + "\t" +
			    mol.toFormat("smiles:au") + "\t" +
			    mol.toFormat("smiles:0ua"));
		    outstream.flush();
		} catch (Exception e2) {e2.printStackTrace();}
		try {
		    mol = mi.read();
		} catch (IOException ioe) {
		    if (!ioe.getMessage().equals("SMILES string contains no atoms")) 
			throw ioe; 
		    else outstream.println();
		}
	    }
	} catch (Exception e) {e.printStackTrace();}
    }
}
