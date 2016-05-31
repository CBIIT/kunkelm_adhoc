drop table if exists rs3_from_plp_400K_frags;
drop table if exists rs3_from_plp_remainder_frags;






create table rs3_from_plp_400K_frags(
NSC int,
MF varchar,
MW double precision,
CAS varchar,
Name varchar,
Comment varchar,
fragmentIndex int,
labelArray varchar,
smiles varchar,
can_smi varchar,
can_taut varchar,
can_taut_strip_stereo varchar,
InChI varchar,
InChI_AuxInfo text,
Molecular_Formula varchar,
Molecular_Weight double precision,
Num_Atoms int,
Num_Bonds int,
Num_Hydrogens int,
Num_PositiveAtoms int,
Num_NegativeAtoms int,
Num_RingBonds int,
Num_RotatableBonds int,
Num_AromaticBonds int,
Num_Rings int,
Num_AromaticRings int,
Num_RingAssemblies int,
Num_MetalAtoms int,
Num_StereoAtoms int,
Num_StereoBonds int,
Num_SingleBonds int,
Num_DoubleBonds int,
Num_TripleBonds int,
FormalCharge int,
CoordDimension int,
IsChiral boolean,
ALogP double precision,
LogD double precision,
Molecular_Solubility double precision,
Molecular_SurfaceArea double precision,
Num_H_Acceptors int,
Num_H_Donors int,
InChI_Message varchar,
CanonicalTautomer varchar,
NumberOfTautomers int,
ErrorText varchar,
ErrorDetails varchar,
TooManyTautomers varchar
);






create table rs3_from_plp_remainder_frags(
NSC int,
MF varchar,
MW double precision,
CAS varchar,
Name varchar,
Comment varchar,
fragmentIndex int,
labelArray varchar,
smiles varchar,
can_smi varchar,
can_taut varchar,
can_taut_strip_stereo varchar,
InChI varchar,
InChI_AuxInfo text,
Molecular_Formula varchar,
Molecular_Weight double precision,
Num_Atoms int,
Num_Bonds int,
Num_Hydrogens int,
Num_PositiveAtoms int,
Num_NegativeAtoms int,
Num_RingBonds int,
Num_RotatableBonds int,
Num_AromaticBonds int,
Num_Rings int,
Num_AromaticRings int,
Num_RingAssemblies int,
Num_MetalAtoms int,
Num_StereoAtoms int,
Num_StereoBonds int,
Num_SingleBonds int,
Num_DoubleBonds int,
Num_TripleBonds int,
FormalCharge int,
CoordDimension int,
IsChiral boolean,
ALogP double precision,
LogD double precision,
Molecular_Solubility double precision,
Molecular_SurfaceArea double precision,
Num_H_Acceptors int,
Num_H_Donors int,
CanonicalTautomer varchar,
NumberOfTautomers int,
InChI_Message varchar,
ErrorText varchar,
ErrorDetails varchar,
TooManyTautomers varchar
);






\copy rs3_from_plp_400k_frags from /home/mwkunkel/rs3_from_plp_400K_frags.csv csv header quote as '"' null as ''
\copy rs3_from_plp_remainder_frags from /home/mwkunkel/rs3_from_plp_remainder_frags.csv csv header quote as '"' null as ''






drop table if exists rs3_from_plp_frags;






create table rs3_from_plp_frags(
NSC int,
MF varchar,
MW double precision,
CAS varchar,
Name varchar,
Comment varchar,
fragmentIndex int,
labelArray varchar,
smiles varchar,
can_smi varchar,
can_taut varchar,
can_taut_strip_stereo varchar,
InChI varchar,
InChI_AuxInfo text,
Molecular_Formula varchar,
Molecular_Weight double precision,
Num_Atoms int,
Num_Bonds int,
Num_Hydrogens int,
Num_PositiveAtoms int,
Num_NegativeAtoms int,
Num_RingBonds int,
Num_RotatableBonds int,
Num_AromaticBonds int,
Num_Rings int,
Num_AromaticRings int,
Num_RingAssemblies int,
Num_MetalAtoms int,
Num_StereoAtoms int,
Num_StereoBonds int,
Num_SingleBonds int,
Num_DoubleBonds int,
Num_TripleBonds int,
FormalCharge int,
CoordDimension int,
IsChiral boolean,
ALogP double precision,
LogD double precision,
Molecular_Solubility double precision,
Molecular_SurfaceArea double precision,
Num_H_Acceptors int,
Num_H_Donors int,
CanonicalTautomer varchar,
NumberOfTautomers int,
InChI_Message varchar,
ErrorText varchar,
ErrorDetails varchar,
TooManyTautomers varchar
);






insert into rs3_from_plp_frags
select
NSC,
MF,
MW,
CAS,
Name,
Comment,
fragmentIndex,
labelArray,
smiles,
can_smi,
can_taut,
can_taut_strip_stereo,
InChI,
InChI_AuxInfo,
Molecular_Formula,
Molecular_Weight,
Num_Atoms,
Num_Bonds,
Num_Hydrogens,
Num_PositiveAtoms,
Num_NegativeAtoms,
Num_RingBonds,
Num_RotatableBonds,
Num_AromaticBonds,
Num_Rings,
Num_AromaticRings,
Num_RingAssemblies,
Num_MetalAtoms,
Num_StereoAtoms,
Num_StereoBonds,
Num_SingleBonds,
Num_DoubleBonds,
Num_TripleBonds,
FormalCharge,
CoordDimension,
IsChiral,
ALogP,
LogD,
Molecular_Solubility,
Molecular_SurfaceArea,
Num_H_Acceptors,
Num_H_Donors,
CanonicalTautomer,
NumberOfTautomers,
InChI_Message,
ErrorText,
ErrorDetails,
TooManyTautomers
from rs3_from_plp_400k_frags;







insert into rs3_from_plp_frags
select
NSC,
MF,
MW,
CAS,
Name,
Comment,
fragmentIndex,
labelArray,
smiles,
can_smi,
can_taut,
can_taut_strip_stereo,
InChI,
InChI_AuxInfo,
Molecular_Formula,
Molecular_Weight,
Num_Atoms,
Num_Bonds,
Num_Hydrogens,
Num_PositiveAtoms,
Num_NegativeAtoms,
Num_RingBonds,
Num_RotatableBonds,
Num_AromaticBonds,
Num_Rings,
Num_AromaticRings,
Num_RingAssemblies,
Num_MetalAtoms,
Num_StereoAtoms,
Num_StereoBonds,
Num_SingleBonds,
Num_DoubleBonds,
Num_TripleBonds,
FormalCharge,
CoordDimension,
IsChiral,
ALogP,
LogD,
Molecular_Solubility,
Molecular_SurfaceArea,
Num_H_Acceptors,
Num_H_Donors,
CanonicalTautomer,
NumberOfTautomers,
InChI_Message,
ErrorText,
ErrorDetails,
TooManyTautomers
from rs3_from_plp_remainder_frags;



