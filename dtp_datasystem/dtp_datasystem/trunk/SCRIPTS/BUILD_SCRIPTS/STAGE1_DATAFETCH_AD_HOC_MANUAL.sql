drop table if exists rs3_from_plp_frags_400;
drop table if exists rs3_from_plp_frags_remainder;

create table rs3_from_plp_frags_400(
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
CanonicalTautomer varchar,
NumberOfTautomers int,
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
Charge_Tautomer varchar,
Error_Text varchar
);

create table rs3_from_plp_frags_remainder(
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
Charge_Tautomer varchar,
Error_Text varchar
);

\copy rs3_from_plp_frags_400 from /home/mwkunkel/rs3_from_plp_400k_frags.csv csv header null as ''
\copy rs3_from_plp_frags_remainder from /home/mwkunkel/rs3_from_plp_remainder_frags.csv csv header null as ''

--
--
-- consolidate
--
--

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
InChI_Message varchar
);

insert into rs3_from_plp_frags(
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
InChI_Message
)
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
InChI_Message
from rs3_from_plp_frags_400;

insert into rs3_from_plp_frags(
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
InChI_Message
)
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
InChI_Message
from rs3_from_plp_frags_remainder;

--
--
-- tautomers
--
--

drop table if exists rs3_tautomer_failures_400;
drop table if exists rs3_tautomer_failures_remainder;

create table rs3_tautomer_failures_400(
NSC int,
MF varchar,
MW double precision,
CAS varchar,
Name varchar,
Comment varchar,
fragmentIndex int,
labelArray varchar,
Smiles varchar,
can_smi varchar,
TooManyTautomers varchar
);

create table rs3_tautomer_failures_remainder(
NSC int,
MF varchar,
MW double precision,
CAS varchar,
Name varchar,
fragmentIndex int,
labelArray varchar,
Smiles varchar,
can_smi varchar,
TooManyTautomers varchar,
Comment varchar
);

\copy rs3_tautomer_failures_400 from rs3_from_plp_400k_tautomer_failures.csv csv header
\copy rs3_tautomer_failures_remainder from rs3_from_plp_remainder_tautomer_failures.csv csv header

--
--
-- consolidate tautomers
--
--

drop table if exists rs3_tautomer_failures;

create table rs3_tautomer_failures(
NSC int,
MF varchar,
MW double precision,
CAS varchar,
Name varchar,
fragmentIndex int,
labelArray varchar,
Smiles varchar,
can_smi varchar,
TooManyTautomers varchar,
Comment varchar
);

insert into rs3_tautomer_failures(
NSC,
MF,
MW,
CAS,
Name,
fragmentIndex,
labelArray,
Smiles,
can_smi,
TooManyTautomers,
Comment
)
select
NSC,
MF,
MW,
CAS,
Name,
fragmentIndex,
labelArray,
Smiles,
can_smi,
TooManyTautomers,
Comment
from rs3_tautomer_failures_400;

insert into rs3_tautomer_failures(
NSC,
MF,
MW,
CAS,
Name,
fragmentIndex,
labelArray,
Smiles,
can_smi,
TooManyTautomers,
Comment
)
select
NSC,
MF,
MW,
CAS,
Name,
fragmentIndex,
labelArray,
Smiles,
can_smi,
TooManyTautomers,
Comment
from rs3_tautomer_failures_remainder;

--
--
-- tautomer analysis
--
--

drop table if exists temp;

create table temp
as
select nsc, fragmentIndex from rs3_tautomer_failures
except
select nsc, fragmentIndex from rs3_from_plp_frags;

drop table if exists temp2;

create table temp2
as
select nsc, fragmentIndex from temp
intersect
select nsc, fragmentIndex from rs3_from_plp_frags;



drop table if exists temp2;

create table temp2
as
select frags.nsc as frag_nsc, array_agg(frags.fragmentIndex) as frag_fragindexes, array_agg(temp.fragmentIndex) as temp_fragindexes
from rs3_from_plp_frags frags, temp
where frags.nsc = temp.nsc
group by frags.nsc;

select * 
from temp2
where frag_fragindexes != temp_fragindexes;