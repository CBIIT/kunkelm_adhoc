#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
use DATA_SYSTEM_BUILD_PARAMETERS qw(getCategoryCriteriaList getQualityControlCriteriaList getCategoryList getDatasystemTableList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 0, 'PrintError' => 1});

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

# set up for cmpd_types

my @cmpdTypes = getCategoryList();

my $typeCnt = 0;

foreach my $cmpdType(@cmpdTypes){
	
	$typeCnt++;
	my $tempSql = qq[
	insert into nsc_cmpd_type (id, nsc_cmpd_type, cmpd_type_description) 
	values ( $typeCnt, '$cmpdType', '$cmpdType' )
	];
	
	print "$tempSql\n\n";
	my $sth = $dbh->prepare($tempSql);
	###################################### $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
	
}

my $tempSql = qq[
drop index if exists nsc_cmpd_type_cmpd_type
];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
###################################### $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
create index nsc_cmpd_type_cmpd_type on nsc_cmpd_type(nsc_cmpd_type)
];	

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
###################################### $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

# set up for cmpd_fragment_type;

my $tempSql = qq[insert into cmpd_fragment_type(id, fragment_type, fragment_type_description)
values (1, 'not yet categorized', 'placeholder, eventually parent, salt, largest, etc.')
];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
###################################### $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 

##      _                 
##   __| |_ __ ___  _ __  
##  / _` | '__/ _ \| '_ \ 
## | (_| | | | (_) | |_) |
##  \__,_|_|  \___/| .__/ 
##                 |_|    
##                      _             _       _       
##   ___ ___  _ __  ___| |_ _ __ __ _(_)_ __ | |_ ___ 
##  / __/ _ \| '_ \/ __| __| '__/ _` | | '_ \| __/ __|
## | (_| (_) | | | \__ \ |_| | | (_| | | | | | |_\__ \
##  \___\___/|_| |_|___/\__|_|  \__,_|_|_| |_|\__|___/

# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

my $tempSql = qq[
insert into cmpd_legacy_cmpd(id,nsc,prod_molecular_formula,prod_formula_weight,prod_ctab)
select nsc, nsc, molecular_formula, molecular_weight, ctab
from prod_legacy_cmpd
];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
###################################### $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

#my $tempSql = qq[
#update cmpd_legacy_cmpd leg
#set gif512 = kek.gif
#from kekule_images kek
#where leg.nsc = kek.nsc
#];

#my $sth = $dbh->prepare($tempSql);
#print "$tempSql\n\n";
###################################### $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #####    ####   #    #  ######  #    #
## #    #  #    #  #    #  #       ##  ##
## #    #  #       ######  #####   # ## #
## #####   #       #    #  #       #    #
## #       #    #  #    #  #       #    #
## #        ####   #    #  ######  #    #

my $tempSql = qq[
insert into cmpd_fragment_p_chem(
id,
molecular_weight,   
molecular_formula,
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
the_a_log_p        
)
select
fragmentindex,
mw,          
mf, 
logd,                     
num_h_acceptors,  
num_h_donors,     
molecular_surfacearea,              
molecular_solubility,                
num_rings,               
num_atoms,               
num_bonds,               
num_singlebonds,        
num_doublebonds,        
num_triplebonds,        
num_rotatablebonds,     
num_hydrogens,      
num_metalatoms,         
num_atoms,
num_positiveatoms,      
num_negativeatoms,      
num_ringbonds,          
num_stereoatoms,        
num_stereobonds,        
num_ringassemblies,     
num_aromaticbonds,     
num_aromaticrings,      
fc,             
alogp 
from rs3_from_plp_frags
where labelarray is null
and nsc in (
select nsc from nsc_to_load
)];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  ####    #####  #####   #    #   ####    #####  #    #  #####   ######
## #          #    #    #  #    #  #    #     #    #    #  #    #  #
##  ####      #    #    #  #    #  #          #    #    #  #    #  #####
##      #     #    #####   #    #  #          #    #    #  #####   #
## #    #     #    #   #   #    #  #    #     #    #    #  #   #   #
##  ####      #    #    #   ####    ####      #     ####   #    #  ######

$t = time;
my $currentDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$currentDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start inserts to cmpd_fragment_structure: $currentDate\n";

my $tempSql = qq[
insert into cmpd_fragment_structure (
id,
inchi,
inchi_aux,
ctab,
can_smi,
can_taut,
can_taut_strip_stereo
)
select
frags.fragmentindex,
inchi, 
inchi_auxinfo, 
ctab.ctab,
can_smi,
can_taut,
null -- can_taut_strip_stereo 
from rs3_from_plp_frags frags 
	left outer join rs3_from_plp_frags_ctab ctab on frags.fragmentindex = ctab.fragmentindex
where labelarray is null
and frags.nsc in (
select nsc from nsc_to_load
)
];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

$t = time;
my $currentDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$currentDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "finish inserts to cmpd_fragment_structure: $currentDate\n";

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  ####   #    #  #####   #####           ######  #####     ##     ####
## #    #  ##  ##  #    #  #    #          #       #    #   #  #   #    #
## #       # ## #  #    #  #    #          #####   #    #  #    #  #
## #       #    #  #####   #    #          #       #####   ######  #  ###
## #    #  #    #  #       #    #          #       #   #   #    #  #    #
##  ####   #    #  #       #####           #       #    #  #    #   ####

my $tempSql = qq[
insert into cmpd_fragment(
id,
stoichiometry,
cmpd_fragment_type_fk,
cmpd_fragment_structure_fk,
cmpd_known_salt_fk,
cmpd_fragment_p_chem_fk,
nsc_cmpd_fk
)
select
fragmentindex,
1.0,
1, -- hard coded insert of cmpd_fragment_type id = 1
fragmentindex,
salt_id,
fragmentindex,
nsc
from rs3_from_plp_frags 
where labelarray is null
and nsc in (
select nsc from nsc_to_load
)];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #####     ##    #####   ######  #    #   #####
## #    #   #  #   #    #  #       ##   #     #
## #    #  #    #  #    #  #####   # #  #     #
## #####   ######  #####   #       #  # #     #
## #       #    #  #   #   #       #   ##     #
## #       #    #  #    #  ######  #    #     #

## parent_fragment

my $tempSql = qq[drop table if exists temp_parent_fragment];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

# only UNAMBIGUOUS parents
# any combo of labels, but only a single strc or a single strc with a single salt

my $tempSql = qq[
create table temp_parent_fragment
as
select * from rs3_from_plp_frags
where nsc in (
select nsc from nsc_to_load
)
and (nsc, fragmentindex) in  
(

-- if there is only one strc, fetch the fragment without labelarray (labels)

select nsc, fragmentindex from comb_cat where category like '%one_strc_no_salt' and labelarray is null    

-- if there are two strc and one salt, fetch parent as fragment without labelarray that is NOT the smiles

union

select nsc, fragmentindex from comb_cat where category like '%two_strc_one_salt' and labelarray is null and salt_smiles is null     

)
];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[create index temp_par_frag_nsc on temp_parent_fragment(nsc)];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	


##  ####     ##    #        #####
## #        #  #   #          #
##  ####   #    #  #          #
##      #  ######  #          #
## #    #  #    #  #          #
##  ####   #    #  ######     #

## salt_fragment

my $tempSql = qq[drop table if exists temp_salt_fragment];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

# any combo of labels, but only two strc with a single salt

my $tempSql = qq[
create table temp_salt_fragment
as
select * from rs3_from_plp_frags
where nsc in (
select nsc from nsc_to_load
)
and (nsc, fragmentindex) in  
(
select nsc, fragmentindex from comb_cat where category like '%two_strc_one_salt' and labelarray is null and salt_smiles is NOT null
)
];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[create index temp_salt_frag_nsc on temp_salt_fragment(nsc)];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##     #    #####   ######  #    #   #####           ####    #####  #####
##     #    #    #  #       ##   #     #            #          #    #    #
##     #    #    #  #####   # #  #     #             ####      #    #    #
##     #    #    #  #       #  # #     #                 #     #    #####
##     #    #    #  #       #   ##     #            #    #     #    #   #
##     #    #####   ######  #    #     #             ####      #    #    #
## 
##  #####   ######   ####    ####   #####            ####    #####  #####
##  #    #  #       #       #    #  #    #          #          #    #    #
##  #    #  #####    ####   #       #    #           ####      #    #    #
##  #    #  #            #  #       #####                #     #    #####
##  #    #  #       #    #  #    #  #   #           #    #     #    #   #
##  #####   ######   ####    ####   #    #           ####      #    #    #

my $tempSql = qq[drop table if exists temp_ident_descr];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
create table temp_ident_descr(
nsc int,
identifier_string varchar(1024),
descriptor_string varchar(1024)
)];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";

$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
insert into temp_ident_descr
select nsc, 'S'||nsc||' '||cmpd_type , 'S'||nsc||' '||cmpd_type
from nsc_to_load
];	
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[create index tid_nsc on temp_ident_descr(nsc)];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #    #   ####    ####             ##     ####    ####
## ##   #  #       #    #           #  #   #    #  #    #
## # #  #   ####   #               #    #  #       #
## #  # #       #  #               ######  #  ###  #  ###
## #   ##  #    #  #    #          #    #  #    #  #    #
## #    #   ####    ####           #    #   ####    ####

my $tempSql = qq[drop table if exists temp_nsc_agg];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
create table temp_nsc_agg
as
select
nsc,
num_fragments as count_fragments,
molecular_weight as formula_weight,
molecular_formula_fragments as formula_molecular_formula,
formalcharge as formal_charge
from 
rs3_from_plp_nsc
where nsc in (
select nsc from nsc_to_load    
)
];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[create index tna_nsc on temp_nsc_agg(nsc)];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #    #   ####    ####            ####   #    #  #####   #####
## ##   #  #       #    #          #    #  ##  ##  #    #  #    #
## # #  #   ####   #               #       # ## #  #    #  #    #
## #  # #       #  #               #       #    #  #####   #    #
## #   ##  #    #  #    #          #    #  #    #  #       #    #
## #    #   ####    ####  #######   ####   #    #  #       #####


################
################ DROP INDEXES!
################

## drop index nsc_cmpd_cas_idx;
## drop index nsc_cmpd_form_mol_form_idx;
## drop index nsc_cmpd_nsc_idx;
## drop index nsc_cmpd_par_mol_form_idx;

my $tempSql = qq[
insert into nsc_cmpd(
id,
name,
nsc_cmpd_id,
prefix,
nsc,
conf,
distribution,
cas,
count_fragments,
discreet,
identifier_string,
descriptor_string,
formula_molecular_formula,
formula_weight,
parent_molecular_formula,
parent_molecular_weight,
prod_molecular_formula,
prod_formula_weight,
nsc_cmpd_type_fk,
cmpd_parent_fragment_fk,
cmpd_salt_fragment_fk,
cmpd_bio_assay_fk,
cmpd_inventory_fk,
cmpd_annotation_fk,
cmpd_legacy_cmpd_fk,
formal_charge
)
select
ntl.nsc,
'NSC ' || 'S' || ntl.nsc,
ntl.nsc,
'S',
ntl.nsc,
CASE WHEN leg.conf IS NULL THEN 'unknown' ELSE leg.conf END,
CASE WHEN leg.distribution_code IS NULL THEN 'unknown' ELSE leg.distribution_code END,
leg.cas,
tna.count_fragments,
'unknown',		-- CASE when leg.discreet IS NULL THEN 'unknown' ELSE leg.discreet END,
tid.identifier_string,
tid.descriptor_string,

tna.formula_molecular_formula,
tna.formula_weight,

temp_par_frag.mf,
temp_par_frag.mw,

leg.molecular_formula, --prod_molecular_formula
leg.molecular_weight, --prod_formula_weight

nct.id, 		-- nsc_cmpd_type_fk
temp_par_frag.fragmentindex, 	-- cmpd_parent_fragment_fk
temp_salt_frag.fragmentindex, -- cmpd_salt_fragment_fk
cba.id, 		-- cmpd_bio_assay_fk,
ci.id, 			-- cmpd_inventory_fk,
annot.id, 	-- cmpd_annotation_fk,
leg.nsc, 			  -- cmpd_legacy_cmpd_fk,
tna.formal_charge

from nsc_to_load ntl
left outer join temp_ident_descr tid on ntl.nsc = tid.nsc
left outer join temp_nsc_agg tna on ntl.nsc = tna.nsc
left outer join temp_parent_fragment temp_par_frag on ntl.nsc = temp_par_frag.nsc
left outer join temp_salt_fragment temp_salt_frag on ntl.nsc = temp_salt_frag.nsc
left outer join prod_legacy_cmpd leg on ntl.nsc = leg.nsc
left outer join cmpd_bio_assay cba on ntl.nsc = cba.id
left outer join cmpd_inventory ci on ntl.nsc = ci.id
left outer join cmpd_annotation annot on ntl.nsc = annot.id, 

nsc_cmpd_type nct

where ntl.cmpd_type = nct.nsc_cmpd_type
];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  ####   #    #  #####   #####
## #    #  ##  ##  #    #  #    #
## #       # ## #  #    #  #    #
## #       #    #  #####   #    #
## #    #  #    #  #       #    #
##  ####   #    #  #       #####

my $tempSql = qq[insert into cmpd(id) select nsc from nsc_to_load];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 

##                               _
##  _ __ ___  ___ _ __ ___  __ _| |_ ___ 
## | '__/ _ \/ __| '__/ _ \/ _` | __/ _ \
## | | |  __/ (__| | |  __/ (_| | ||  __/
## |_|  \___|\___|_|  \___|\__,_|\__\___|
##                                       
##                      _             _       _       
##   ___ ___  _ __  ___| |_ _ __ __ _(_)_ __ | |_ ___ 
##  / __/ _ \| '_ \/ __| __| '__/ _` | | '_ \| __/ __|
## | (_| (_) | | | \__ \ |_| | | (_| | | | | | |_\__ \
##  \___\___/|_| |_|___/\__|_|  \__,_|_|_| |_|\__|___/

# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 

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
