__doc__ = """

$Revision: 3.4 $

This version of the SMILES desalting script will remove common salts and
solvents from SMILES strings using an internal library. SMILES, SDF and Maestro
input and output formats can be used. However, the script will internally
convert all structures to canonical SMILES prior to removing the salt.

If a salt(s) is detected and the salt(s) is unknown, both the compound and the
salt(s) will be written to a separate file. The user has the option to override
this behavior and simply return the largest molecule in the SMILES string. If a
single entry contains duplicate molecules, the first molecule will be retained.

Execute this as:

$SCHRODINGER/run desalit_smiles.py <file> [options]

Copyright Schrodinger, LLC. All rights reserved.
"""
# Contributors: Chris Higgs

from schrodinger import structure
from schrodinger.utils import cmdline
import schrodinger.infra.canvas as canvas
from schrodinger.utils.fileutils import splitext, get_structure_file_format
import sys, os, time, subprocess, tempfile, re, shutil, fileinput

canvasConvert = os.path.join(os.environ['SCHRODINGER'], 'utilities', 'canvasConvert')
sdconvert = os.path.join(os.environ['SCHRODINGER'], 'utilities', 'sdconvert')

#########################
def parse_command_line():

    usage = """
$SCHRODINGER/run desalt_smiles.py <file> [options]"""

    description ="""This version of the SMILES desalting script will remove
common salts and solvents from SMILES strings using an internal library.
SMILES, SDF and Maestro input and output formats can be used. However, the
script will internally convert all structures to canonical SMILES prior to
removing the salt.

If a salt(s) is detected and the salt(s) is unknown, both the compound and the
salt(s) will be written to a separate file. The user has the option to override
this behavior and simply return the largest molecule in the SMILES string. If
a single entry contains duplicate molecules, the first molecule will be retained.
"""
    _version = '$Revision: 3.4 $'

    parser = cmdline.SingleDashOptionParser(usage=usage, description=description, version_source=_version)

    parser.add_option( "-l", dest="override", action="store_true", default=False, help="For SMILES strings containing unknown salts, return the largest single molecule in the SMILES string. The default behavior is to write the entire entry to a separate file.")
    parser.add_option( "-name", type="string", dest="name", default="", help="Field in a SD or Maestro file that can be used to overwrite the name of molecules.")
    parser.add_option( "-o", type="string", dest="output_file", default="", metavar="file", help="Desalted structure output file. Valid formats are SMILES, Maestro or SDF format.")

    (options, args) = parser.parse_args()

    # Check for input files and output options

    if len(args) == 0:
        print "No input file specified."
        parser.print_help()
        sys.exit(1)
    elif len(args) >= 2:
        print "Cannot specify multiple input files."
        parser.print_help()
        sys.exit(1)
    elif not os.path.exists(args[0]):
        print "\nError: Input file '%s' not found.\n" % args[0]
        sys.exit(1)

    out = False
    out_format = ""

    in_format = get_structure_file_format(args[0])

    if in_format != "smiles" and in_format != "sd" and in_format != "maestro":
        print "\nError: Unrecognized file format.\n"
        sys.exit(1)

    if options.output_file != "":
        if os.path.exists(options.output_file):
            print "\nError: Output file '%s' already exists.\n" % options.output_file
            sys.exit(1)

        out_format = get_structure_file_format(options.output_file)

        if out_format == "smiles":
            out = True
        elif out_format == "sd":
            out = True
        elif out_format == "maestro":
            out = True
        elif out == False:
            print ""
            print "Not a valid output format"
            print ""
            parser.print_help()
            sys.exit(1)
    elif options.output_file == "":
        out_format = in_format

    return(options, args, in_format, out_format, out)

################
def salt_list():
    """
    """

    salts = []

    anionic_salts = [
    "O=C(C)O", "[O-]C(=O)C",																	# Acetic acid
    "O=C(Oc1ccccc1C(=O)O)C",																	# 2-Acetoxybenzoic acid*
    "O=C(O)C12CC3CC(C1)CC(C2)C3"				,												# Adamantanecarboxylic acid
    "O=C(O)CCCCC(=O)O",																		# Adipic acid
    "O=C(O)c1ccc(cc1O)N",																	# 4-Aminosalicylic acid*
    "OCC(O)C(OC1=O)C(O)=C1O",																	# Ascorbic acid
    "O=C(O)CC(N)C(=O)O",																	# Aspartic acid*
    "O=C(O)CCCCCCCC(=O)O",																	# Azelaic acid*
    "O=S(=O)(O)c1ccccc1", "[O-]S(=O)(=O)c1ccccc1",														# Benzenesulfonic acid
    "O=C(O)c1ccccc1", "[O-]C(=O)c1ccccc1",															# Benzoic acid
    "[O-]C(=O)C(C)C",																		# Butyric acid
    "O=C([O-])O", "OC(=O)O", "[O-]C([O-])=O",															# Carbonate
    "O=C(O)\C=C\c1ccccc1",																	# Cinnamic acid*
    "O=C(O)CC(O)(C(=O)O)CC(=O)O",																# Citric acid
    "O=C(O)C1CCCCC1",																		# Cyclohexancarboxylic acid* 
    "O=C(O)CCCCCCCCC",																		# Decanoic acid*
    "O=C(O)CCCCCCCCCCC",																	# Dodecanoic acid*
    "O=S(=O)(OCCCCCCCCCCCC)O",																	# Dodecylsulfuric acid*
    "O=S(=O)(O)CCS(O)(=O)=O",																	# Ethane-1,2-disulfonic acid*
    "CCOS([O-])(=O)=O", "CCOS(=O)(=O)O", "CCO[S+]([O-])([O-])=O",												# Ethylsulfuric acid
    "O=C[O-]", "OC=O",																		# Formic acid
    "O=C(O)/C=C/C(=O)O", "[O-]C(=O)/C=C/C(=O)O", "[O-]C(=O)/C=C/C([O-])=O",											# Fumaric acid
    "[O-]C(=O)[C@H](O)[C@H](O)[C@H](O)[C@@H](O)CO", "[O-]C(=O)[C@H](O)[C@@H](O)[C@@H](O)[C@H](O)CO",								# Gluconic acid
    "O=C(O)CCC(N)C(=O)O",																	# Glutamic acid*
    "O=C(O)CO",																			# Glycolic acid 
    "F", "[F-]", "Cl", "[Cl-]", "Br", "[Br-]", "I", "[I-]",													# Halogens
    "O=S(=O)(O)CCO",																		# 2-Hydroxyethanesulfonic acid
    "OC(=O)\C=C(\O)C(O)=O",																	# Hydroxymaleic acid*
    "[O-]C(=O)c1ccncc1",																	# Isonicotinic acid
    "O=C(O)C(C)O", "[O-]C(=O)[C@H](C)O", "[O-]C(=O)[C@@H](C)O",													# Lactic acid
    "O=C(O)/C=C\C(=O)O",																	# Maleic acid
    "[O-]C(=O)CC(O)C(=O)O", "[O-]C(=O)C(O)CC(=O)O", "O=C(O)[C@@H](O)CC(=O)O",											# Malic acid
    "[O-]C(=O)CC(=O)O", "O=C(O)CC(=O)O",															# Malonic acid
    "O=C(O)C(O)c1ccccc1",																	# Mandelic acid*
    "Cc1c(S(=O)(=O)O)c(C)cc(C)c1",																# Mesitylenesulfonic acid
    "CS(=O)(=O)O", "C[S-](=O)(=O)=O", "CS([O-])(=O)=O",														# Methansulfonic acid
    "O=S(=O)(O)c1ccccc1C",																	# 2-Methylbenzenesulfonic
    "O=S(=O)(O)c1cccc(c1)C",																	# 3-Methylbenzenesulfonic acid*
    "O=C(O)\C=C(/C(=O)O)C",																	# Methylmaleic acid*
    "COS([O-])(=O)=O", "COS(=O)(=O)O",																# Methylsulfuric acid
    "O=S(=O)(O)c1cccc2c1cccc2S(=O)(=O)O",															# 1,5-Naphthalene-disulfonic acid*
    "c1cccc(c12)ccc(c2)S([O-])(=O)=O",																# 2-Naphthalenesulfonic acid
    "O=S(=O)(O)NC1CCCCC1",																	# N-cyclohexylsulfamic acid*
    "O=S(=O)(O)NCC",																		# N-ethylsulfamic acid*
    "[O-]C(=O)c1cccnc1", "O=C(O)c1cccnc1",															# Nicotinic acid*
    "O=[N+]([O-])O", "O=N(=O)O", "[O-][N+]([O-])=O", "O=N([O-])=O",												# Nitrate
    "O=S(=O)(O)NC",																		# N-methylsulfamic acid*
    "O=C(O)c2ccccc2Oc1ccccc1",																	# 2-Phenoxybenzoic acid*
    "iCCCNS(O)(=O)=O",																		# N-propylsulfamic acid*
    "O=C(O)CCCCCCC",																		# Octanoic acid
    "[O-]C(=O)c1cc(=O)[nH]c(=O)[nH]1", "O=C(O)c1cc(=O)[nH]c(=O)[nH]1", "O=C1CC(C(=O)O)NC(=O)N1",								# Orotic acid
    "O=C(O)C(=O)O", "[O-]C(=O)C(=O)O", "[O-]C(=O)C([O-])=O",													# Oxalic acid
    "O=C(O)C(=O)CCC(=O)O",																	# Oxoglutaric acid
    "[O-][Cl](=O)(=O)=O", "O=[Cl](=O)(=O)O", "O=[Cl-](=O)(=O)=O",												# Perchlorate
    "O=C(O)Cc1ccccc1",																		# Phenylacetic acid
    "O=P(O)(O)O", "O=[P-](O)(O)O", "[O-]P(=O)(O)O", "O=[P](=O)(=O)O", "[O-]P([O-])(=O)O",									# Phosphate
    "[O-][N+](=O)c(c1O)cc([N+]([O-])=O)cc1[N+]([O-])=O", "[O-]c1c([N+]([O-])=O)cc([N+]([O-])=O)cc1[N+]([O-])=O",						# Picric acid
    "O=C(O)CCCCCC(=O)O",																	# Pimelic acid*
    "O=C(O)CC",																			# Propionic acid*
    "O=C(O)c1ccccc1C(=O)O",																	# Phthalic acid*
    "O=C(O)C(N1)CCC1=O", "O=C(O)[C@@H](N1)CCC1=O",														# Pyroglutamic acid
    "O=C(O)c1c(O)cccc1", "[O-]C(=O)c1c(O)cccc1",														# Salicylic acid
    "O=C(O)CCCCCCC(=O)O",																	# Suberic acid*
    "O=C(O)CCC(=O)O", "[O-]C(=O)CCC(=O)O",															# Succinic acid
    "O=S(=O)(O)N",																		# Sulfamic acid*
    "Nc1ccc(cc1)S(=O)(=O)O",																	# Sulfanilic acid
    "O=S(=O)(O)O", "[O-]S(=O)(=O)O", "[O-]S([O-])(=O)=O", "O=[S-2](=O)(=O)=O", "[O-][S](=O)(=O)=O", "[O-][S+](=O)(O)O", "[O-][S+]([O-])([O-])=O", "[O-][S+]([O-])(=O)O",	# Sulfuric acid
    "[O-]C(=O)C(O)C(O)C(=O)O", "O=C(O)C(O)C(O)C(=O)O", "O=C(O)[C@H](O)[C@@H](O)C(=O)O", "O=C(O)[C@@H](O)[C@H](O)C(=O)O", "O=C(O)[C@@H](O)[C@@H](O)C(=O)O", "[O-]C(=O)[C@H](O)[C@@H](O)C(=O)O",	# Tartaric acid
    "F[B-](F)(F)F",																		# Tetrafluroborate
    "Cc1ccc(cc1)S(=O)(=O)O", "Cc1ccc(S([O-])(=O)=O)cc1", "Cc1ccc([S+]([O-])([O-])=O)cc1",									# p-toluenesulfonic acid
    "O=C(C(F)(F)F)O", "[O-]C(=O)C(F)(F)F",															# TFA
    "SC#N", "S=C=[N-]", "[S-]C#N"																# Thiocyanate
    ]

    cationic_salts = [
    "[NH4+]", "N",																		# Ammonia
    "O=C(O)C(N)CCCNC(=N)N",																	# Arginine
    "NC1CC1",																			# Cyclopropylamine
    "NCCCN",																			# Diaminopropane
    "N1(C)CCN(C)CC1",																		# N.N'-dimethylpiperazine
    "C1CCCCC1NC2CCCCC2",																	# Dicyclohexylamine
    "NCCN",																			# Ethylenediamine
    "[Li+]", "[LiH]", "[Na+]", "[NaH]", "[Mg+2]", "[MgH+]", "[MgH2]", "[Al+3]", "[Al]", "[Al-]", "[SiH6+2]", "[K+]", "[KH]", "[KH2-]", "[Ca+2]", "[CaH+]", "[CaH2]", "[Sc+3]", "[Sc]", "[Ti+2]", "[Ti]", "[V]", "[Cr]", "[Cr+3]", "[Mn+2]", "[Mn]",         # Elements
    "[Fe]", "[Fe+2]", "[Fe+3]", "[Fe+]", "[Co+2]", "[Co+3]", "[Co]", "[Ni+2]", "[Ni]", "[Cu+2]", "[Cu+]", "[Cu]", "[Zn+2]", "[ZnH2]", "[Ga]", "[Ga+3]", "[AsH6+3]", "[Rb+]", "[Rb]", "[Sr+2]", "[Y]", "[Y+3]", "[Zr+2]", "[Zr]", "[Mo]", "[RuH+]",          # Elements
    "[RuH2+2]", "[Ru+2]", "[Ru]", "[Rh+]", "[Rh]", "[RhH]", "[PdH4]", "[PdH2+2]", "[Ag+]", "[Ag-]", "[Ag]", "[Cd+2]", "[CdH2]", "[In]", "[In+3]", "[SnH4]", "[SnH2+2]", "[Sb+3]", "[Cs+]", "[Cs]", "[Ba+2]", "[Ba]", "[La]", "[La+3]", "[Ce+3]",            # Elements
    "[Ce]", "[Pr+3]", "[Pr]", "[Nd]", "[Nd+3]", "[Sm+3]", "[Sm]", "[Eu+3]", "[Eu]", "[Gd]", "[Gd+3]", "[Tb+3]", "[Tb]", "[Dy+3]", "[Dy]", "[Ho]", "[Ho+3]", "[Er+3]", "[Er]", "[Tm+3]", "[Tm]", "[Yb+3]", "[Lu]", "[Lu+3]", "[Ta]", "[W]", "[Ir+]",         # Elements
    "[PtH4]", "[PtH2+2]", "[Au+]", "[Hg+2]", "[HgH2]", "[HgH+]", "[Tl+]", "[Tl]", "[PbH4]", "[PbH2+2]", "[Bi+3]", "[BiH3]",					# Elements 
    "N1(CC)CCCCC1",																		# N-ethylpiperidine*
    "O=C(O)C(N)CCCCN",																		# Lysine
    "CC[N+](CC)(CC)CC"																		# TEA
    ]
    
    questionable_salts = []
    # questionable salt names:

    solvents = [
    "CC(=O)C",                                                                          # Acetone
    "N#CC",                                                                             # Acetonitrile*
    "[NH3+]c1ccccc1",                                                                   # Aniline
    "c1ccccc1",                                                                         # Benzene
    "OCCCC",                                                                            # 1-butanol*
    "OC(C)CC",                                                                          # 2-butanol*
    "O=C(C)CC",                                                                         # 2-butanone*
    "OC(C)(C)C",                                                                        # t-butyl alcohol*
    "N#Cc1ccccc1",                                                                      # Benzonitrile
    "OCCCCO",                                                                           # 1,4-Butanediol
    "ClC(Cl)(Cl)Cl",                                                                    # Carbon tetrachloride*
    "Clc1ccccc1",                                                                       # Chlorobenzene*
    "ClC(Cl)Cl",                                                                        # Chloroform*
    "C1CCCCC1",                                                                         # Cyclohexane
    "ClCCCl",                                                                           # 1,2-dichloroethane*
    "NC1CCCCC1", "[NH3+]C1CCCCC1",                                                      # Cyclohexylamine
    "CC(=O)N(C)C",                                                                      # Dimethylacetamide
    "O=CN(C)C",                                                                         # Dimethylformamide
    "CCOCC",                                                                            # Diethylether
    "COCCOCCOC",                                                                        # Diglyme*
    "COCCOC",                                                                           # DME*
    "O(C)C",                                                                            # Dimethylether*
    "O=CN(C)C",                                                                         # DMF*
    "C1COCCO1",                                                                         # 1,4-Dioxane
    "CS(=O)C",                                                                          # DMSO
    "OCC",                                                                              # Ethanol
    "OCCO",                                                                             # Ethylene glycol*
    "OCC(O)CO",                                                                         # Glycerin*
    "CCCCCCC",                                                                          # Heptane
    "O=P(N(C)C)(N(C)C)N(C)C",                                                           # HMPA*
    "N(P(N(C)C)N(C)C)(C)C",                                                             # HMPT*
    "CCCCCC",                                                                           # Hexane*
    "NCCO", "[NH3+]CCO",                                                                # MEA
    "CO",                                                                               # Methanol
    "ClCCl",                                                                            # Methylene Chloride*
    "CN1CCOCC1", "C[NH+]1CCOCC1",                                                       # N-Methylmorpholine
    "C1COCCN1", "C1COCC[NH2+]1",                                                        # Morpholine
    "O(C(C)(C)C)C",                                                                     # MTBE*
    "[O-][N+](=O)C",                                                                    # Nitromethane*
    "O=C1N(C)CCC1",                                                                     # NMP*
    "CCCCC",                                                                            # Pentane*
    "Cc1ccncc1",                                                                        # 4-Picoline
    "C1CNCCN1",                                                                         # Piperazine 
    "C1CC[NH2+]CC1", "C1CCNCC1",                                                        # Piperidine
    "OCCCO",                                                                            # 1,3-Propanediol
    "CCCO",                                                                             # 1-Propanol
    "CC(C)O",                                                                           # 2-Propanol
    "c1cc[nH+]cc1", "c1ccncc1",                                                         # Pyridine
    "C1CCOC1",                                                                          # THF
    "Cc1ccccc1",                                                                        # Toluene
    "CCN(CC)CC", "CC[NH+](CC)CC",                                                       # Triethylamine
    "O", "[OH-]",                                                                       # Water
    "Cc1c(C)cccc1",                                                                     # Xylene (ortho)
    "Cc1cc(C)ccc1",                                                                     # Xylene (meta)
    "Cc1ccc(C)cc1"                                                                      # Xylene (para)
    ]
    
    misc = [
    "[H][H]", "[H+]"
    ]

    salts.extend(anionic_salts)
    salts.extend(cationic_salts)
    salts.extend(questionable_salts)
    salts.extend(solvents)
    salts.extend(misc)

    return(salts)

###############################################
def mol_to_smiles(in_format, input_file, name):
    """
    """

    tempFile = tempfile.mktemp()

    if in_format == "maestro" or in_format == "sd":
        tempInFile = tempfile.mktemp()
        tempInFile = tempInFile + ".mae"
        writer = structure.StructureWriter(tempInFile)

    if in_format != "smiles" and name == "":
        for i, st in enumerate(structure.StructureReader(input_file)):
            if st.property['s_m_title'] == "":
                i +=1
                st.property['s_m_title'] = "Untitled Molecule" + str(i)
                writer.append(st)
            else:
                writer.append(st)
        writer.close()

    elif in_format == "maestro" and name != "":
        for i, st in enumerate(structure.StructureReader(input_file)):
           if st.property[name] == "":
               i +=1
               st.property['s_m_title'] = "Untitled Molecule" + str(i)
               writer.append(st)
           else:
               st.property['s_m_title'] = st.property[name]
               writer.append(st)
        writer.close()

    elif in_format == "sd" and name != "":
        tmpfile = 'temp.log'
        fh = open(tmpfile,'w')
        tempMaeFile = input_file.strip(".sdf") + "_temp.mae"
        exe = [sdconvert, '-isd', input_file, '-title', name, '-omae', tempMaeFile]
        subprocess.call(exe, stdout=fh, stderr=fh)
        fh.close()
        os.remove(tmpfile)
        for i, st in enumerate(structure.StructureReader(tempMaeFile)):
            if st.property['s_m_title'] == "":
                i +=1
                st.property['s_m_title'] = "Untitled Molecule" + str(i)
                writer.append(st)
            else:
                writer.append(st)
        writer.close()
        os.remove(tempMaeFile)

    elif in_format == "smiles":
        tempSmiFile = tempfile.mktemp()
        tmp = open(tempSmiFile,'w')

    tmpfile = 'temp.log'
    fh = open(tmpfile,'w')

    if in_format != "smiles":
        exe = [canvasConvert, '-imae', tempInFile, '-id', '-osmi', tempFile]
        subprocess.call(exe, stdout=fh, stderr=fh)
        fh.close()
        os.remove(tempInFile)
    elif in_format == "smiles":
        for i, lines in enumerate(fileinput.FileInput(input_file)):
            if len(lines.split()) != 1:
                tmp.write(lines)
            else:
                i +=1
                tmp.write(lines.strip("\n") + " Untitled Molecule" + str(i) + "\n")
        tmp.close()

        shutil.copy(tempSmiFile, tempFile)
        fh.close()
        os.remove(tempSmiFile)

    os.remove(tmpfile)

    return(tempFile)

#################################################
def parse_smi_file(format, input_file, tempFile):
    """
    """

    f = open(tempFile,'r')
    data = f.read()
    f.close()

    remove_blanks = re.sub("\n\s*\n*", "\n", data)

    tempFile2 = tempfile.mktemp()

    f = open(tempFile2,'w')
    f.write(remove_blanks)
    f.close()

    ff = open(tempFile2,'r')
    lines = ff.readlines()
    lineCount = len(lines)
    ff.close
    
    smiles = []
    titles = []

    for line in lines:
        l = line.split()
        smiles.append(l[0])
        title = l[1:]
        new_title = ""

        for t in title:
            new_title = new_title+" "+t
        titles.append(new_title)

    return(smiles, titles, lineCount, tempFile2)

#########################
def split_smiles(smiles):
    """
    """

    split_smiles = []

    for cmpds in smiles:
        split_smiles.append(cmpds.split("."))

    return(split_smiles)

###############################################
def smiles_to_canonical(split_smiles, titles):
    """
    """

    unique_smiles = []
    sg = canvas.ChmMmctSmilesGenerator()
    bad_index = []
    bad_smiles = []
    bad_titles = []
    good_smiles = []
    unique_titles = []

    try:
        lic = canvas.ChmLicenseFull()
    except:
        sys.stderr.write("Unable to checkout CANVAS_FULL license\n")
        sys.exit(1)

    if not lic.isValid():
        sys.stderr.write("CANVAS_FULL license is not valid\n")
        sys.exit(1)

    for i, ss in enumerate(split_smiles):
        current_title = titles[i]

        for s in ss:
            try:
                test = sg.canonicalize(s)
            except:
                bad_index.append(i)
                bad_smiles.append(ss)
                bad_titles.append(current_title)

    for j, smi in enumerate(split_smiles):
        if j not in bad_index:
            good_smiles.append(smi)

    for k, ti in enumerate(titles):
        if k not in bad_index:
            unique_titles.append(ti)

    for gs in good_smiles:
        single_smiles = []

        for g in gs:
            single_smiles.append(sg.canonicalize(g))
        unique_smiles.append(single_smiles)

    return(unique_smiles, unique_titles, bad_smiles, bad_titles)

#######################################################
def remove_duplicates_in_single_entries(unique_smiles):
    """
    """

    duplicates = False
    no_duplicate_smiles = []

    for i, usmi in enumerate(unique_smiles):
        num_entries = len(usmi)
        unique = list(set(usmi))
        final_num_entries = len(unique)
        no_duplicate_smiles.append(unique)

        if num_entries != final_num_entries:
            duplicates = True

    return(no_duplicate_smiles, duplicates)
 
###########################################################
def remove_known_salt(no_duplicate_smiles, unique_titles):
    """
    """

    salts = salt_list()

    salt_only_smiles = []
    salt_only_titles = []
    final_smiles = []
    final_titles = []
    known_salt = []
    known_salt_titles = []

    for i, ss in enumerate(no_duplicate_smiles):

        tmp_smiles = []

        for s in ss:
            if s not in salts:
                tmp_smiles.append(s)
            elif s in salts:
                known_salt.append(s)
                known_salt_titles.append(unique_titles[i])

        if len(tmp_smiles) == 0:
            salt_only_smiles.append(unique_smiles[i])
            salt_only_titles.append(unique_titles[i])
        elif len(tmp_smiles) >= 1:
            final_smiles.append(tmp_smiles)
            final_titles.append(unique_titles[i])

    return(salt_only_smiles, salt_only_titles, final_smiles, final_titles, known_salt, known_salt_titles)

##############################################################################################
def process_bad_smiles(salt_only_smiles, salt_only_titles, bad_smiles, bad_titles, base_name):
    """
    """

    bad_smi = ""

    if len(salt_only_smiles) > 0:
        bad_smi = base_name+"_bad_smiles.usmi"
        bad_cmpds = open(bad_smi, 'w')

        print "\n" "Warning: SMILES containing only known salts identifed. Entries written to %s" % (bad_smi)

        for i, s in enumerate(salt_only_smiles):
            for elements in s:
                bad_cmpds.write(str(elements)+str(salt_only_titles[i])+"\n")

    if len(bad_smiles) > 0:
        failed_smi = base_name+"_failed_smiles.usmi"
        failed_cmpds = open(failed_smi, 'w')

        print "\n" "Warning: Some SMILES failed to convert to canonical SMILES. Entries written to %s" % (failed_smi)

        for j, failed in enumerate(bad_smiles):
            for fail in failed:
                failed_cmpds.write(str(fail)+str(bad_titles[j])+"\n")

    return(bad_smi)

########################################################################
def process_identifed_salts(known_salt, known_salt_titles, base_name):
    """
    """

    if len(known_salt) > 0:
        salt_hits = base_name+"_salts.usmi"
        id_salts = open(salt_hits, 'w')

        for i, s in enumerate(known_salt):
            id_salts.write(str(s)+str(known_salt_titles[i])+"\n")

        id_salts.close()

##################################################################################################################
def process_desalted_smiles(final_smiles, final_titles, base_name, override, out_format, bad_smiles, output_file):
    """
    """

    tempFile3 = tempfile.mktemp()
    out_usmi = open(tempFile3,'w')

    tempFile4 = tempfile.mktemp()
    unrecog_salts = open(tempFile4,'w')

    tmpfile3 = 'temp3.log'
    fh = open(tmpfile3,'w')

    tmpfile4 = 'temp4.log'
    fh2 = open(tmpfile4,'w')
 
    unrecognized = False

    if output_file == "":
        if out_format == "smiles":
            good_usmi = base_name+"_desalted_smiles.usmi"
            out_usmi = open(good_usmi, 'w')
        elif out_format == "sd":
            good_out_sdf = base_name+"_desalted_structures.sdf"
        elif out_format == "maestro":
            good_out_mae = base_name+"_desalted_structures.mae"
    elif output_file != "":
        if out_format == "smiles":
            good_usmi = options.output_file
            out_usmi = open(good_usmi, 'w')
        elif out_format == "sd":
            good_out_sdf = options.output_file
        elif out_format == "maestro":
            good_out_mae = options.output_file

    if override == False:
        if out_format == "smiles":
            unrecog = base_name+"_unrecognized_salts.usmi"
        elif out_format == "sd":
            unrecog = base_name+"_unrecognized_salts.sdf"
        elif out_format == "maestro":
            unrecog = base_name+"_unrecognized_salts.mae"

    for i, fs in enumerate(final_smiles):
        if len(fs) == 1:
            for f in fs:
                out_usmi.write(str(f)+str(final_titles[i])+"\n")
        if len(fs) >= 2 and override == True:
            (fix_smiles, fix_title) = retain_largest_molecule(fs, final_titles[i], bad_smiles)
            for fix in fix_smiles:
                out_usmi.write(str(fix)+str(fix_title)+"\n")
        if len(fs) >= 2 and override == False:
            for s in fs: 
                unrecognized = True
                unrecog_salts = open(unrecog, 'w')
                unrecog_salts.write(str(s)+str(final_titles[i])+"\n")

    out_usmi.close()

    if out_format == "smiles" and override == False:
        unrecog_salts.close()

    if out_format == "sd" and override == True:
        exe = ['sh', canvasConvert, '-ismi', tempFile3, '-osd', good_out_sdf]
        subprocess.call(exe, stdout=fh, stderr=fh)
        fh.close()
    elif out_format == "maestro" and override == True:
        exe = ['sh', canvasConvert, '-ismi', tempFile3, '-omae', good_out_mae]
        subprocess.call(exe, stdout=fh, stderr=fh)
        fh.close()
    elif out_format == "sd" and override == False:
        unrecog_salts.close()
        exe = ['sh', canvasConvert, '-ismi', tempFile3, '-osd', good_out_sdf]
        subprocess.call(exe, stdout=fh, stderr=fh)
        fh.close()
        if unrecognized == True:
            exe1 = ['sh', canvasConvert, '-ismi', tempFile4, '-osd', unrecog]
            subprocess.call(exe1, stdout=fh2, stderr=fh2)
            fh2.close()
    elif out_format == "maestro" and override == False:
        unrecog_salts.close() 
        exe = ['sh', canvasConvert, '-ismi', tempFile3, '-omae', good_out_mae]
        subprocess.call(exe, stdout=fh, stderr=fh)
        fh.close()
        if unrecognized == True:
            exe1 = ['sh', canvasConvert, '-ismi', tempFile4, '-omae', unrecog]
            subprocess.call(exe1, stdout=fh2, stderr=fh2)
            fh2.close()
    
    fh.close()
    fh2.close()
    os.remove(tmpfile3)
    os.remove(tmpfile4)

    return(tempFile3, tempFile4)

####################################################
def retain_largest_molecule(smi, title, bad_smiles):

    min_weight = 0
    largest = []

    bad_file = open(bad_smiles, 'a')

    for i, us in enumerate(smi):

        mol = canvas.ChmMol_fromSMILES(us)

        weight = round(mol.getMW(),2)

        if weight > min_weight:
            largest = [us]
            min_weight = weight
        elif weight == min_weight:
            largest.append(us)

    if len (largest) >=2:

        undetermined = largest
        largest = ""
        for u, und in enumerate(undetermined):
                bad_file.write(str(und)+str(title)+"\n")

        print ""
        print "It appears that an entry in%s contains two or more different molecules but have" % (title)
        print "identical molecular weights. It is not possible to determine the potential salt and"
        print "all entries for%s will be written to" % (title), (bad_smiles)

    return(largest, title)

#########################################################################################
def file_cleanup(converted_structures, no_blanks, temp_desalted_file, temp_unrecog_file):

    os.remove(converted_structures)
    os.remove(no_blanks)
    os.remove(temp_desalted_file)
    os.remove(temp_unrecog_file)

    return()

########################
#     Main Program     #
########################

starttime = time.clock()

(options, args, in_format, out_format, out)								= parse_command_line()
create_base_name											= args[0].split(".")[0]
override												= options.override
converted_structures											= mol_to_smiles(in_format, args[0], options.name)
(all_smiles, all_titles, smicount, no_blanks)								= parse_smi_file(in_format, args[0], converted_structures)
split_smiles												= split_smiles(all_smiles)
(unique_smiles, unique_titles, bad_smiles, bad_titles)							= smiles_to_canonical(split_smiles, all_titles)
(no_duplicate_smiles, contains_duplicates)								= remove_duplicates_in_single_entries(unique_smiles)
(salt_only_smiles, salt_only_titles, final_smiles, final_titles, known_salt, known_salt_titles)	= remove_known_salt(no_duplicate_smiles, unique_titles)
bad_smiles												= process_bad_smiles(salt_only_smiles, salt_only_titles, bad_smiles, bad_titles, create_base_name)
write_known_salt											= process_identifed_salts(known_salt, known_salt_titles, create_base_name)
(temp_desalted_file, temp_unrecog_file)									= process_desalted_smiles(final_smiles, final_titles, create_base_name, override, out_format, bad_smiles, options.output_file)

cleanup													= file_cleanup(converted_structures, no_blanks, temp_desalted_file, temp_unrecog_file)

if contains_duplicates == True:
    print ""
    print "Entries containing identical structures were detected."

stoptime = time.clock()
elapsed = stoptime-starttime

print ""
print "Calculations complete! %d SMILES strings processed in %s seconds.\n" % (smicount, elapsed)
