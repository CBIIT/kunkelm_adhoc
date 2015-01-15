#!/usr/bin/perl

use strict;
use warnings;


use DBI;

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "start time $theTime\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 0, 'PrintError' => 1});

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

#     #         #     #         #    #          #######
##    #         #     #         #   #           #      
# #   #         #     #         #  #            #      
#  #  #         #     #         ###             #####  
#   # #         #     #         #  #            #
#    ##         #     #         #   #           #      
#     #          #####          #    #          #######

my @tablesToNuke = (
	"cmpd",                         
	"cmpd_fragment",                
	"cmpd_fragment_p_chem",         
	"cmpd_fragment_structure",
	"cmpd_known_salt",              
	"cmpd_list",                    
	"cmpd_list_member",             
	"cmpd_lists2share_with_users",  
	"cmpd_related",                 
	"cmpd_relation_type",           
	"cmpd_table",
        "nsc_cmpd",
	"nsc_cmpd_type"); 

foreach my $tableToNuke(@tablesToNuke){
	
	my $tempSql = qq[truncate table $tableToNuke cascade];

	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	
	
}

# set up for cmpd_types

my @cmpdTypes = (
	"one_strc",
	"one_strc_salt",
	"one_lbl",
	"multi_lbl",
	"one_strc_one_lbl",
	"one_strc_multi_lbl",
        "two_strc_no_lbl_one_salt",
	"two_strc_any_lbl_one_salt",
	"two_strc_any_lbl_two_salt",
	"two_strc_any_lbl",
	"multi_strc_any_lbl"
	);

my $typeCnt = 0;

foreach my $cmpdType(@cmpdTypes){
	
	$typeCnt++;
	my $tempSql = qq[
	insert into nsc_cmpd_type (id, nsc_cmpd_type, cmpd_type_description) 
	values ( $typeCnt, '$cmpdType', '$cmpdType' )];

	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	
		
}


# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 

 #####   #####    ####   #####            ####    ####    #####  #####
 #    #  #    #  #    #  #    #          #    #  #          #    #    #
 #    #  #    #  #    #  #    #          #        ####      #    #    #
 #    #  #####   #    #  #####           #            #     #    #####
 #    #  #   #   #    #  #               #    #  #    #     #    #   #
 #####   #    #   ####   #                ####    ####      #    #    #

# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 

#####    ####   #    #  ######  #    #
#    #  #    #  #    #  #       ##  ##
#    #  #       ######  #####   # ## #
#####   #       #    #  #       #    #
#       #    #  #    #  #       #    #
#        ####   #    #  ######  #    #

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
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

 ####    #####  #####   #    #   ####    #####  #    #  #####   ######
#          #    #    #  #    #  #    #     #    #    #  #    #  #
 ####      #    #    #  #    #  #          #    #    #  #    #  #####
     #     #    #####   #    #  #          #    #    #  #####   #
#    #     #    #   #   #    #  #    #     #    #    #  #   #   #
 ####      #    #    #   ####    ####      #     ####   #    #  ######

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
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

 ####   #    #  #####   #####           ######  #####     ##     ####
#    #  ##  ##  #    #  #    #          #       #    #   #  #   #    #
#       # ## #  #    #  #    #          #####   #    #  #    #  #
#       #    #  #####   #    #          #       #####   ######  #  ###
#    #  #    #  #       #    #          #       #   #   #    #  #    #
 ####   #    #  #       #####           #       #    #  #    #   ####

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
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

#####     ##    #####   ######  #    #   #####
#    #   #  #   #    #  #       ##   #     #
#    #  #    #  #    #  #####   # #  #     #
#####   ######  #####   #       #  # #     #
#       #    #  #   #   #       #   ##     #
#       #    #  #    #  ######  #    #     #

## parent_fragment

my $tempSql = qq[drop table if exists temp_parent_fragment];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
create table temp_parent_fragment
as
select * from rs3_from_plp_frags
where nsc in (
select nsc from nsc_to_load
)
and (nsc, fragmentindex) in  
(
select nsc, fragmentindex from one_strc
union
select nsc, fragmentindex from two_strc_no_lbl_one_salt where salt_smiles is null and can_smi != '[RH]'
union
select nsc, fragmentindex from two_strc_any_lbl_one_salt where salt_smiles is null and can_smi != '[RH]'
union
select nsc, fragmentindex from one_strc_one_lbl where can_smi != '[RH]'
union 
select nsc, fragmentindex from one_strc_multi_lbl where can_smi != '[RH]'
)];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[create index tpf_nsc on temp_parent_fragment(nsc)];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

    #    #####   ######  #    #   #####           ####    #####  #####
    #    #    #  #       ##   #     #            #          #    #    #
    #    #    #  #####   # #  #     #             ####      #    #    #
    #    #    #  #       #  # #     #                 #     #    #####
    #    #    #  #       #   ##     #            #    #     #    #   #
    #    #####   ######  #    #     #             ####      #    #    #

 #####   ######   ####    ####   #####            ####    #####  #####
 #    #  #       #       #    #  #    #          #          #    #    #
 #    #  #####    ####   #       #    #           ####      #    #    #
 #    #  #            #  #       #####                #     #    #####
 #    #  #       #    #  #    #  #   #           #    #     #    #   #
 #####   ######   ####    ####   #    #           ####      #    #    #


my $tempSql = qq[drop table if exists temp_ident_descr];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
create table temp_ident_descr(
nsc int,
identifier_string varchar(1024),
descriptor_string varchar(1024)
)];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' one_strc' , 'S'||uni.nsc||' one_strc'
from(
select distinct nsc from one_strc where nsc in (select nsc from nsc_to_load)
) as uni];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	





my $tempSql = qq[
insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' two_strc_no_lbl_one_salt' , 'S'||uni.nsc||' two_strc_no_lbl_one_salt'
from(
select distinct nsc from two_strc_no_lbl_one_salt where nsc in (select nsc from nsc_to_load)
) as uni];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	







my $tempSql = qq[
insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' two_strc_any_lbl_one_salt' , 'S'||uni.nsc||' two_strc_any_lbl_one_salt'
from(
select distinct nsc from two_strc_any_lbl_one_salt where nsc in (select nsc from nsc_to_load)
) as uni];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' one_strc_one_lbl' , 'S'||uni.nsc||' one_strc_one_lbl'
from(
select distinct nsc from one_strc_one_lbl where nsc in (select nsc from nsc_to_load)
) as uni];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' one_strc_multi_lbl' , 'S'||uni.nsc||' one_strc_multi_lbl'
from(
select distinct nsc from one_strc_multi_lbl where nsc in (select nsc from nsc_to_load)
) as uni];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' one_lbl' , 'S'||uni.nsc||' one_lbl'
from(
select distinct nsc from one_lbl where nsc in (select nsc from nsc_to_load)
) as uni];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' multi_lbl' , 'S'||uni.nsc||' multi_lbl'
from(
select distinct nsc from multi_lbl where nsc in (select nsc from nsc_to_load)
) as uni];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' multi_strc_any_lbl' , 'S'||uni.nsc||' multi_strc_any_lbl'
from(
select distinct nsc from multi_strc_any_lbl where nsc in (select nsc from nsc_to_load)
) as uni];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' two_strc_any_lbl_two_salt' , 'S'||uni.nsc||' two_strc_any_lbl_two_salt'
from(
select distinct nsc from two_strc_any_lbl_two_salt where nsc in (select nsc from nsc_to_load)
) as uni];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[
insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' two_strc_any_lbl' , 'S'||uni.nsc||' two_strc_any_lbl'
from(
select distinct nsc from two_strc_any_lbl where nsc in (select nsc from nsc_to_load)
) as uni];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[create index tid_nsc on temp_ident_descr(nsc)];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

#    #   ####    ####             ##     ####    ####
##   #  #       #    #           #  #   #    #  #    #
# #  #   ####   #               #    #  #       #
#  # #       #  #               ######  #  ###  #  ###
#   ##  #    #  #    #          #    #  #    #  #    #
#    #   ####    ####           #    #   ####    ####

my $tempSql = qq[drop table if exists temp_nsc_agg];
print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

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
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

my $tempSql = qq[create index tna_nsc on temp_nsc_agg(nsc)];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

#    #   ####    ####            ####   #    #  #####   #####
##   #  #       #    #          #    #  ##  ##  #    #  #    #
# #  #   ####   #               #       # ## #  #    #  #    #
#  # #       #  #               #       #    #  #####   #    #
#   ##  #    #  #    #          #    #  #    #  #       #    #
#    #   ####    ####  #######   ####   #    #  #       #####

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
n.nsc,
'NSC ' || 'S' || n.nsc || ' name: ' || n.name,
n.nsc,
'S',
n.nsc,
'fake', --n.conf,!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
'fake', --n.distribution,!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
n.cas,
tna.count_fragments,
'fake', --n.discreet,!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
tid.identifier_string, --identifier_string,
tid.descriptor_string, --descriptor_string,
tna.molecular_weight,
tna.molecular_formula,
nct.id, --nsc_cmpd_type_fk
tpf.fragmentindex, --cmpd_parent_fragment_fk
null, -- cba.id, --cmpd_bio_assay_fk,
null, -- ci.id, --cmpd_inventory_fk,
null, -- annot.id, --cmpd_annotation_fk,
null -- cmpd_legacy_cmpd_fk

from rs3_from_plp_nsc n
left outer join temp_ident_descr tid on n.nsc = tid.nsc
left outer join temp_nsc_agg tna on n.nsc = tna.nsc
left outer join temp_parent_fragment tpf on n.nsc = tpf.nsc
-- left outer join cmpd_bio_assay cba on n.nsc = cba.id
-- left outer join cmpd_inventory ci on n.nsc = ci.id
--left outer join cmpd_annotation annot on n.nsc = annot.id
left outer join nsc_to_load ntl on n.nsc = ntl.nsc,

nsc_cmpd_type nct

where ntl.cmpd_type = nct.nsc_cmpd_type];

print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

 ####   #    #  #####   #####
#    #  ##  ##  #    #  #    #
#       # ## #  #    #  #    #
#       #    #  #####   #    #
#    #  #    #  #       #    #
 ####   #    #  #       #####

my $tempSql = qq[insert into cmpd(id) select nsc from nsc_to_load];
print "$tempSql\n\n";
my $sth = $dbh->prepare($tempSql);
$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 
# MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY MANAGE EXTERNALLY 

 ####   #####   ######    ##     #####           ####    ####    #####  #####
#    #  #    #  #        #  #      #            #    #  #          #    #    #
#       #    #  #####   #    #     #            #        ####      #    #    #
#       #####   #       ######     #            #            #     #    #####
#    #  #   #   #       #    #     #            #    #  #    #     #    #   #
 ####   #    #  ######  #    #     #             ####    ####      #    #    #

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

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "finish time $theTime\n";
