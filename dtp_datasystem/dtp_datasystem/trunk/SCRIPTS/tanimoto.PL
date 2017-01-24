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
my $dbh = DBI->connect("DBI:Pg:dbname=oncology;host=ncidb-d115-d;port=5474", "oncology", "OnC0L029302802K1t", {'RaiseError' => 0});

#--------------------------------------------------------------------------------------------fp

my @fpList;
push @fpList, "atompairbv_fp";                                                                           
#push @fpList, "avalon_fp";                              
push @fpList, "featmorganbv_fp";                                                      
push @fpList, "layered_fp";                                                                              
push @fpList, "maccs_fp";                                                                                
push @fpList, "morganbv_fp";                                                          
push @fpList, "rdkit_fp";
push @fpList, "torsionbv_fp";

#--------------------------------------------------------------------------------------------define all pair

my $sql;
my $sth;

$sql = "drop table if exists all_pair_nsc";
print "$sql\n";
$sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

$sql = "create table all_pair_nsc as select u.nsc as uno, d.nsc as duo from rdkit_mol u, rdkit_mol d where u.nsc < d.nsc";
print "$sql\n";
$sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

#--------------------------------------------------------------------------------------------do fp

foreach my $fp(@fpList){
	
	$sql = "drop table if exists temp_" . $fp;
	print "$sql\n";
	$sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;
	
	$sql = "create table temp_" . $fp . " as select nsc, $fp(mol) as fp from rdkit_mol";
	print "$sql\n";
	$sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;
	
	#
	#
	# updating rdkit_mol
	#
	#
	
	$sql = "alter table rdkit_mol drop column if exists " . $fp;
	print "$sql\n";
	$sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;
		
	$sql = "alter table rdkit_mol add " . $fp . " bfp";
	print "$sql\n";
	$sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;
	
	$sql = "update rdkit_mol set " . $fp . " = $fp(mol)";
	print "$sql\n";
	$sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;
		
}

#--------------------------------------------------------------------------------------------tanimoto

foreach my $fp(@fpList){
	print "-------------fp: $fp \n";
	$sql = "drop table if exists temp_tanimoto_" . $fp;
	print "$sql\n";
	$sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;
	
	$sql = "create table temp_tanimoto_" . $fp . " as select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto from temp_" . $fp . " uno, temp_" . $fp . " duo where uno.nsc < duo.nsc";
	print "$sql\n";
	$sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;
}

# check on counts
#foreach my $fp(@fpList){
#	print "-------------fp: $fp \n";
#	$sql = "select count(*) from temp_tanimoto_" . $fp;
#	print "$sql\n";
#	$sth = $dbh->prepare($sql);
#	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;
#}

#--------------------------------------------------------------------------------------------crosstab

$sql = "drop table if exists temp_tanimoto_collate";
print "$sql\n";
$sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

print "$sql--------------------------------------------\n";
print "$sql--------------------------------------------\n";
print "$sql--------------------------------------------\n";

$sql = "create table temp_tanimoto_collate as select all_pair_nsc.uno, all_pair_nsc.duo ";

foreach my $fp(@fpList){	
	$sql = $sql . "\n, temp_tanimoto_" . $fp . ".tanimoto as " . $fp . "_tanimoto";	
}

$sql = $sql . "\n from all_pair_nsc ";

foreach my $fp(@fpList){	
	$sql = $sql . "\n left outer join temp_tanimoto_" . $fp . " on all_pair_nsc.uno = temp_tanimoto_" . $fp . ".nsc1 and all_pair_nsc.duo = temp_tanimoto_" . $fp . ".nsc2";
}

print "$sql\n";
$sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

#--------------------------------------------------------------------------------------------stacked

$sql = "drop table if exists temp_tanimoto_stacked";
print "$sql\n";$sth = $dbh->prepare($sql);
$sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

$sql = "create table temp_tanimoto_stacked( uno int, duo int, fp varchar, tanimoto double precision )";
print "$sql\n";$sth = $dbh->prepare($sql);
$sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

foreach my $fp(@fpList){
	# print "$fp";
	$sql = "insert into temp_tanimoto_stacked select nsc1, nsc2, '$fp', tanimoto from temp_tanimoto_" . $fp . " where nsc1 < nsc2";
	print "$sql\n";$sth = $dbh->prepare($sql);
	$sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
}

#    #    ##    #    #  #    #    ##    #
##  ##   #  #   ##   #  #    #   #  #   #
# ## #  #    #  # #  #  #    #  #    #  #
#    #  ######  #  # #  #    #  ######  #
#    #  #    #  #   ##  #    #  #    #  #
#    #  #    #  #    #   ####   #    #  ######

#    #    ##    #    #  #    #    ##    #
##  ##   #  #   ##   #  #    #   #  #   #
# ## #  #    #  # #  #  #    #  #    #  #
#    #  ######  #  # #  #    #  ######  #
#    #  #    #  #   ##  #    #  #    #  #
#    #  #    #  #    #   ####   #    #  ######

#\copy temp_tanimoto_collate to /tmp/temp_tanimoto_collate.csv csv header
#drop table if exists temp;
#create table temp as select * from tanimoto_scores limit 0;
#alter table temp drop id;
#\copy temp from /tmp/temp_tanimoto_collate.csv csv header
#drop sequence if exists temp_seq;
#create sequence temp_seq;
#insert into tanimoto_scores
#select nextval('temp_seq'), * from temp;

#    #    ##    #    #  #    #    ##    #
##  ##   #  #   ##   #  #    #   #  #   #
# ## #  #    #  # #  #  #    #  #    #  #
#    #  ######  #  # #  #    #  ######  #
#    #  #    #  #   ##  #    #  #    #  #
#    #  #    #  #    #   ####   #    #  ######

#    #    ##    #    #  #    #    ##    #
##  ##   #  #   ##   #  #    #   #  #   #
# ## #  #    #  # #  #  #    #  #    #  #
#    #  ######  #  # #  #    #  ######  #
#    #  #    #  #   ##  #    #  #    #  #
#    #  #    #  #    #   ####   #    #  ######

######  #    #  #####    ####   #####    #####
#        #  #   #    #  #    #  #    #     #
#####     ##    #    #  #    #  #    #     #
#         ##    #####   #    #  #####      #
#        #  #   #       #    #  #   #      #
######  #    #  #        ####   #    #     #

# hard-code statement for temp_tanimoto_collate
$sql = "\\copy temp_tanimoto_collate to /tmp/temp_tanimoto_collate.csv csv header";
print "$sql\n";$sth = $dbh->prepare($sql);
#$sth = $dbh->prepare($sql);
#$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	


foreach my $fp(@fpList){	            
        $sql = "\\copy temp_tanimoto_" . $fp . " to /tmp/temp_tanimoto_" . $fp . ".csv csv header";
        print "$sql\n";$sth = $dbh->prepare($sql);
	#$sth = $dbh->prepare($sql);
	#$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	        
}

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";
print "finish: $finishDate\n";


