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
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 0, 'PrintError' => 1});

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #    #  #    #  #    #  ######
## ##   #  #    #  #   #   #
## # #  #  #    #  ####    #####
## #  # #  #    #  #  #    #
## #   ##  #    #  #   #   #
## #    #   ####   #    #  ######

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## does NOT nuke cmpd_known_salt cmpd_legacy_cmpd             

my @tablesToNuke = qw[
cmpd                         
cmpd_alias                   
cmpd_alias_type              
cmpd_aliases2nsc_cmpds       
cmpd_annotation              
cmpd_bio_assay               
cmpd_fragment                
cmpd_fragment_p_chem         
cmpd_fragment_structure      
cmpd_fragment_type           
cmpd_inventory       
cmpd_legacy_cmpd        
cmpd_list                    
cmpd_list_member             
cmpd_named_set               
cmpd_named_sets2nsc_cmpds    
cmpd_plate                   
cmpd_plates2nsc_cmpds        
cmpd_project                 
cmpd_projects2nsc_cmpds      
cmpd_pub_chem_sid            
cmpd_pub_chem_sids2nsc_cmpds 
cmpd_related                 
cmpd_relation_type           
cmpd_table                   
cmpd_target                  
cmpd_targets2nsc_cmpds
nsc_cmpd
nsc_cmpd_type
];

foreach my $tableToNuke(@tablesToNuke){	
	my $tempSql = qq[
	delete from $tableToNuke
	];
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
}




## #     #         #     #         #    #          #######
## ##    #         #     #         #   #           #      
## # #   #         #     #         #  #            #      
## #  #  #         #     #         ###             #####  
## #   # #         #     #         #  #            #
## #    ##         #     #         #   #           #      
## #     #          #####          #    #          #######

# do NOT nuke cmpd_known_salt and cmpd_legacy_cmpd
# since they are fairly static and id = nsc

# my @tablesToNuke = getDatasystemTableList();

#@tablesToNuke = grep {$_ ne "cmpd_known_salt" and $_ ne "cmpd_legacy_cmpd" and $_ ne "nsc_to_load" and $_ ne "rdkit_validity" } @tablesToNuke;

#foreach my $tableToNuke(@tablesToNuke){	
#	my $tempSql = qq[truncate table $tableToNuke cascade];	
#	my $sth = $dbh->prepare($tempSql);
#	print "$tempSql\n\n";

#	#print "NUKE IS DISABLED!\n";
#	#print "NUKE IS DISABLED!\n";
#	#print "NUKE IS DISABLED!\n";
#	#print "NUKE IS DISABLED!\n";
#	#print "NUKE IS DISABLED!\n";

#	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;		
#}


# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";


