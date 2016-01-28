#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 0, 'PrintError' => 1});

goto skipToHere;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  ####   #    #  #####   #####            #####    ##    #####   #       ######
## #    #  ##  ##  #    #  #    #             #     #  #   #    #  #       #
## #       # ## #  #    #  #    #             #    #    #  #####   #       #####
## #       #    #  #####   #    #             #    ######  #    #  #       #
## #    #  #    #  #       #    #             #    #    #  #    #  #       #
##  ####   #    #  #       #####  #######     #    #    #  #####   ######  ######

################
################ DROP INDEXES!
################

## drop index cmpd_table_cas_idx;
## drop index cmpd_table_nsc_idx;
## drop index cmpd_table_par_form_mol_form_idx;
## drop index cmpd_table_par_mol_form_idx;

my $tempSql = qq[truncate table cmpd_table];
print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

# temp_formatted_strings - aliases, plates, projects, sets, targets

my $tempSql = qq[drop table if exists temp_formatted_strings];
print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

## formatted_plates includes distinct because data are degenerate by set 
## (not tracking plate row/column in dctddb)
## In any case EVERYTHING has to be distinct to avoid Cartesian products on LOJ
## on one-to-many alias, project, etc.
## DISTINCT DISTINCT DISTINCT  

my $tempSql = qq[
create table temp_formatted_strings
as
select
nsc.id, 
nsc.nsc,
-- ALL MUST BE DISTINCT to avoid Cartesian products on multiple LOJ
string_agg(distinct alia.alias,'xxx') as formatted_aliases_string,
string_agg(distinct plat.plate_name,'xxx') as formatted_plates_string,
string_agg(distinct proj.project_code,'xxx') as formatted_projects_string,
string_agg(distinct cset.set_name,'xxx') as formatted_sets_string,
string_agg(distinct targ.target,'xxx') as formatted_targets_string
from
nsc_cmpd nsc
left outer join cmpd_aliases2nsc_cmpds alia2c    on (alia2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_alias alia     on (alia2c.cmpd_aliases_fk = alia.id)
left outer join cmpd_plates2nsc_cmpds plat2c     on (plat2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_plate plat     on (plat2c.cmpd_plates_fk = plat.id)
left outer join cmpd_projects2nsc_cmpds proj2c   on (proj2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_project proj   on (proj2c.cmpd_projects_fk = proj.id)
left outer join cmpd_named_sets2nsc_cmpds sets2c on (sets2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_named_set cset on (sets2c.cmpd_named_sets_fk = cset.id)
left outer join cmpd_targets2nsc_cmpds targ2c    on (targ2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_target targ    on (targ2c.cmpd_targets_fk = targ.id)
group by 
nsc.id, 
nsc.nsc];
print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[create index tfs_nsc on temp_formatted_strings(nsc)];
print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

## temp_formatted_fragments

my $tempSql = qq[drop table if exists temp_formatted_fragments];
print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
create table temp_formatted_fragments
as
select
nsc.id, 
nsc.nsc,
string_agg(strc.can_smi,'xxx') as formatted_fragments_string
from
nsc_cmpd nsc, cmpd_fragment frag, cmpd_fragment_structure strc
where nsc.id = frag.nsc_cmpd_fk
and frag.cmpd_fragment_structure_fk = strc.id
and nsc.nsc in (
select nsc from nsc_to_load
)
group by 
nsc.id, 
nsc.nsc];
print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[create index tff_nsc on temp_formatted_fragments(nsc)];
print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my @sqlList = ();

# INDEXES to speed up the joins below...
push @sqlList, qq[
drop index if exists cFrag_cFragStrcFk
];
push @sqlList, qq[
drop index if exists cFrag_cFragPchemFk
];
push @sqlList, qq[
drop index if exists cFrag_nCmpdFk
];
push @sqlList, qq[
drop index if exists nCmpd_nCmpdTypeFk
];
push @sqlList, qq[
drop index if exists nCmpd_cParFragFk
];
push @sqlList, qq[
drop index if exists nCmpd_cSaltFragFk
];
push @sqlList, qq[
drop index if exists nCmpd_cBioAssFk
];
push @sqlList, qq[
drop index if exists nCmpd_cInvFk
];
push @sqlList, qq[
drop index if exists nCmpd_cAnnotFk
];
push @sqlList, qq[
drop index if exists nCmpd_cLegCFk
];
push @sqlList, qq[
create index cFrag_cFragStrcFk on cmpd_fragment(cmpd_fragment_structure_fk)
]; 
push @sqlList, qq[
create index cFrag_cFragPchemFk on cmpd_fragment(cmpd_fragment_p_chem_fk)
]; 
push @sqlList, qq[
create index cFrag_nCmpdFk on cmpd_fragment(nsc_cmpd_fk)
];
push @sqlList, qq[
create index nCmpd_nCmpdTypeFk on nsc_cmpd(nsc_cmpd_type_fk)
];   
push @sqlList, qq[
create index nCmpd_cSaltFragFk on nsc_cmpd(cmpd_salt_fragment_fk)
]; 
push @sqlList, qq[
create index nCmpd_cParFragFk on nsc_cmpd(cmpd_parent_fragment_fk)
];  
push @sqlList, qq[
create index nCmpd_cBioAssFk on nsc_cmpd(cmpd_bio_assay_fk)
];        
push @sqlList, qq[
create index nCmpd_cInvFk on nsc_cmpd(cmpd_inventory_fk)
];        
push @sqlList, qq[
create index nCmpd_cAnnotFk on nsc_cmpd(cmpd_annotation_fk)
];       
push @sqlList, qq[
create index nCmpd_cLegCFk on nsc_cmpd(cmpd_legacy_cmpd_fk)
];  

foreach my $sql(@sqlList){
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
}




#
#
#
skipToHere:
#
#
#

  #####  #    #  ######             #    #    #   ####   ######  #####    #####
    #    #    #  #                  #    ##   #  #       #       #    #     #
    #    ######  #####              #    # #  #   ####   #####   #    #     #
    #    #    #  #                  #    #  # #       #  #       #####      #
    #    #    #  #                  #    #   ##  #    #  #       #   #      #
    #    #    #  ######             #    #    #   ####   ######  #    #     #

my $tempSql = qq[
insert into cmpd_table(
id,
prefix,
nsc,
cas,
name,
discreet,
conf,
distribution,
nsc_cmpd_id,
ad_hoc_cmpd_id,
original_ad_hoc_cmpd_id,
inventory,
nci60,
hf,
xeno,
formatted_targets_string,
formatted_sets_string,
formatted_projects_string,
formatted_plates_string,
formatted_aliases_string,
formatted_fragments_string,
pseudo_atoms,
salt_smiles,
salt_name,
salt_mf,
salt_mw,
parent_stoichiometry,
salt_stoichiometry,
inchi,
inchi_aux,
ctab,
can_smi,
can_taut,
can_taut_strip_stereo,
log_d,
count_hyd_bond_acceptors,
count_hyd_bond_donors,
surface_area,
solubility,
count_rings,
count_atoms,
count_bonds,
count_single_bonds,
count_double_bonds,
count_triple_bonds,
count_rotatable_bonds,
count_hydrogen_atoms,
count_metal_atoms,
count_heavy_atoms,
count_positive_atoms,
count_negative_atoms,
count_ring_bonds,
count_stereo_atoms,
count_stereo_bonds,
count_ring_assemblies,
count_aromatic_bonds,
count_aromatic_rings,
formal_charge,
the_a_log_p,
mtxt,
general_comment,
purity_comment,
stereochemistry_comment,
identifier_string,
descriptor_string,
nsc_cmpd_type,
formula_molecular_formula,
formula_weight,
parent_molecular_formula,
parent_molecular_weight,
prod_formula_weight,
prod_molecular_formula,
cmpd_formal_charge
)

select

nsc.id,
nsc.prefix,
nsc.nsc,
nsc.cas,
nsc.name,
nsc.discreet,
nsc.conf,
nsc.distribution,
nsc.nsc_cmpd_id,
null, --ad_hoc_cmpd_id,
null, --original_ad_hoc_cmpd_id,

-- inventory

invent.inventory,

-- bio_assay

bioass.nci60,
bioass.hf,
bioass.xeno,

-- from temp_formatted_strings

tfs.formatted_targets_string,
tfs.formatted_sets_string,
tfs.formatted_projects_string,
tfs.formatted_plates_string,
tfs.formatted_aliases_string,

-- temp_formatted_fragments

tff.formatted_fragments_string,

pseudo_atoms,

-- salts

salt.can_smi, --null, --salt_smiles,
salt.salt_name, --null, --salt_name,
salt.salt_mf, --null, --salt_mf,
salt.salt_mw, --null, --salt_mw,

1, --parent_stoichiometry,
1, --salt_stoichiometry,

-- structure -- PARENT only

strc.inchi,
strc.inchi_aux,
strc.ctab,
strc.can_smi,
strc.can_taut,
strc.can_taut_strip_stereo,

-- pchem -- PARENT only

pchem.log_d,
pchem.count_hyd_bond_acceptors,
pchem.count_hyd_bond_donors,
pchem.surface_area,
pchem.solubility,
pchem.count_rings,
pchem.count_atoms,
pchem.count_bonds,
pchem.count_single_bonds,
pchem.count_double_bonds,
pchem.count_triple_bonds,
pchem.count_rotatable_bonds,
pchem.count_hydrogen_atoms,
pchem.count_metal_atoms,
pchem.count_heavy_atoms,
pchem.count_positive_atoms,
pchem.count_negative_atoms,
pchem.count_ring_bonds,
pchem.count_stereo_atoms,
pchem.count_stereo_bonds,
pchem.count_ring_assemblies,
pchem.count_aromatic_bonds,
pchem.count_aromatic_rings,
pchem.formal_charge,
pchem.the_a_log_p,

-- annotation

annot.mtxt,
annot.general_comment,
annot.purity_comment,
annot.stereochemistry_comment,

nsc.identifier_string,
nsc.descriptor_string,

typ.nsc_cmpd_type,

nsc.formula_molecular_formula,
nsc.formula_weight,
nsc.parent_molecular_formula,
nsc.parent_molecular_weight,
nsc.prod_formula_weight,
nsc.prod_molecular_formula,
nsc.formal_charge

from 

nsc_cmpd nsc

-- need intermediate join based on cmpd_parent_fragment_fk!

left outer join cmpd_fragment frag on nsc.cmpd_parent_fragment_fk = frag.id
left outer join cmpd_fragment_p_chem pchem on frag.cmpd_fragment_p_chem_fk = pchem.id
left outer join cmpd_fragment_structure strc on frag.cmpd_fragment_structure_fk = strc.id

left outer join cmpd_annotation annot on nsc.cmpd_annotation_fk = annot.id
left outer join cmpd_inventory invent on nsc.cmpd_inventory_fk = invent.id
left outer join cmpd_bio_assay bioass on nsc.cmpd_bio_assay_fk = bioass.id

left outer join cmpd_fragment salt_frag on nsc.cmpd_salt_fragment_fk = salt_frag.id
left outer join cmpd_known_salt salt on salt_frag.cmpd_known_salt_fk = salt.id

-- temp tables

left outer join temp_formatted_strings tfs on nsc.nsc = tfs.nsc -- this is 1-to-1
left outer join temp_formatted_fragments tff on nsc.nsc = tff.nsc
left outer join temp_ident_descr temp_ident_descr on nsc.nsc = temp_ident_descr.nsc -- this is 1-to-1
left outer join nsc_cmpd_type typ on nsc.nsc_cmpd_type_fk = typ.id,

nsc_to_load load

where nsc.nsc = load.nsc
]; 

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";