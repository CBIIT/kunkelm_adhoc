#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
use DATA_SYSTEM_BUILD_PARAMETERS qw(getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 0, 'PrintError' => 1});

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #     #         #     #         #    #          #######
## ##    #         #     #         #   #           #      
## # #   #         #     #         #  #            #      
## #  #  #         #     #         ###             #####  
## #   # #         #     #         #  #            #
## #    ##         #     #         #   #           #      
## #     #          #####          #    #          #######

# do NOT nuke cmpd_known_salt
# it is loaded early in the build process	

my @tablesToNuke = (
	"cmpd",                         
	"cmpd_fragment",                
	"cmpd_fragment_p_chem",         
	"cmpd_fragment_structure",
	"cmpd_list",                    
	"cmpd_list_member",
	"cmpd_related",                 
	"cmpd_relation_type",           
	"cmpd_table",
	"nsc_cmpd",
	"nsc_cmpd_type"); 

foreach my $tableToNuke(@tablesToNuke){
	
	my $tempSql = qq[truncate table $tableToNuke cascade];
	
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
	
}

# set up for cmpd_types

my @cmpdTypes = getCategoryList();

my $typeCnt = 0;

foreach my $cmpdType(@cmpdTypes){
	
	$typeCnt++;
	my $tempSql = qq[
	insert into nsc_cmpd_type (id, nsc_cmpd_type, cmpd_type_description) 
	values ( $typeCnt, '$cmpdType', '$cmpdType' )];
	
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
	
}

my $tempSql = qq[
	drop index if exists nsc_cmpd_type_cmpd_type
];	
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	


	my $tempSql = qq[
	create index nsc_cmpd_type_cmpd_type on nsc_cmpd_type(nsc_cmpd_type)
];	
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

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
molecular_weight,          
molecular_formula, 
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
(num_atoms - num_hydrogens),         
num_positiveatoms,      
num_negativeatoms,      
num_ringbonds,          
num_stereoatoms,        
num_stereobonds,        
num_ringassemblies,     
num_aromaticbonds,     
num_aromaticrings,      
formalcharge,             
alogp 
from rs3_from_plp_frags
where can_smi != '[RH]' 
and nsc in (
select nsc from nsc_to_load
)];
print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
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
can_taut_strip_stereo 
from rs3_from_plp_frags frags, rs3_from_plp_ctab ctab
where can_smi != '[RH]'
and frags.nsc in (
select nsc from nsc_to_load
)
and frags.fragmentindex = ctab.fragmentindex];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

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
cmpd_fragment_structure_fk,
cmpd_known_salt_fk,
cmpd_fragment_p_chem_fk,
nsc_cmpd_fk
)
select
fragmentindex,
1.0,
fragmentindex,
null, --cmpd_known_salt_fk,!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
fragmentindex,
nsc
from rs3_from_plp_frags 
where can_smi != '[RH]'
and nsc in (
select nsc from nsc_to_load
)];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
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

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
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

select nsc, fragmentindex from no_lbl_one_strc_no_salt

union

select nsc, fragmentindex from no_lbl_two_strc_one_salt where salt_smiles is null

)];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[create index tpf_nsc on temp_parent_fragment(nsc)];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
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

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
create table temp_ident_descr(
nsc int,
identifier_string varchar(1024),
descriptor_string varchar(1024)
)];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

## cycle through the categories

my @categories = getCategoryList();

foreach my $categ (@categories){
	
	my $tempSql = qq[
	insert into temp_ident_descr
	select uni.nsc, 'S'||uni.nsc||' $categ' , 'S'||uni.nsc||' $categ'
	from(
	select distinct nsc from $categ where nsc in (select nsc from nsc_to_load)
	) as uni];
	
	print "$tempSql\n\n";
	my $sth = $dbh->prepare($tempSql);
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
		
}

my $tempSql = qq[create index tid_nsc on temp_ident_descr(nsc)];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
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
print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
create table temp_nsc_agg
as
select
nsc,
count(molecular_weight) as count_fragments,
sum(molecular_weight) as molecular_weight,
array_to_string(array_agg(molecular_formula), '.') as molecular_formula
from 
rs3_from_plp_frags
where can_smi != '[RH]'
and nsc in (
select nsc from nsc_to_load    
)
group by nsc];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[create index tna_nsc on temp_nsc_agg(nsc)];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
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

##vacuum analyze rs3_from_plp_nsc;
##vacuum analyze temp_parent_fragment;
##vacuum analyze prod_cmpd;
##vacuum analyze cmpd_bio_assay;
##vacuum analyze cmpd_inventory;
##vacuum analyze cmpd_annotation;
##vacuum analyze temp_nsc_agg;
##vacuum analyze temp_ident_descr;
##vacuum analyze nsc_to_load;
##vacuum analyze nsc_cmpd_type;

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
molecular_weight,
molecular_formula,
nsc_cmpd_type_fk,
cmpd_parent_fragment_fk,
cmpd_bio_assay_fk,
cmpd_inventory_fk,
cmpd_annotation_fk,
cmpd_legacy_cmpd_fk
)
select
ntl.nsc,
'NSC ' || 'S' || ntl.nsc,
ntl.nsc,
'S',
ntl.nsc,
pc.conf,
pc.distribution_code,
pc.cas,
tna.count_fragments,
'fake', --n.discreet,!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
tid.identifier_string, --identifier_string,
tid.descriptor_string, --descriptor_string,
tna.molecular_weight,
tna.molecular_formula,
nct.id, --nsc_cmpd_type_fk
tpf.fragmentindex, --cmpd_parent_fragment_fk
cba.id, --cmpd_bio_assay_fk,
ci.id, --cmpd_inventory_fk,
annot.id, --cmpd_annotation_fk,
null -- cmpd_legacy_cmpd_fk

from nsc_to_load ntl
left outer join temp_ident_descr tid on ntl.nsc = tid.nsc
left outer join temp_nsc_agg tna on ntl.nsc = tna.nsc
left outer join temp_parent_fragment tpf on ntl.nsc = tpf.nsc
left outer join prod_cmpd pc on ntl.nsc = pc.nsc
left outer join cmpd_bio_assay cba on ntl.nsc = cba.id
left outer join cmpd_inventory ci on ntl.nsc = ci.id
left outer join cmpd_annotation annot on ntl.nsc = annot.id,

nsc_cmpd_type nct

where ntl.cmpd_type = nct.nsc_cmpd_type];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
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
print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
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
