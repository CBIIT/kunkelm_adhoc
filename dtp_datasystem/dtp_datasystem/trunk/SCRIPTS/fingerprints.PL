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
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 0});

my @fpList;
push @fpList, "atompairbv_fp";                                                                           
push @fpList, "avalon_fp";                              
push @fpList, "featmorganbv_fp";                                                      
push @fpList, "layered_fp";                                                                              
push @fpList, "maccs_fp";                                                                                
push @fpList, "morganbv_fp";                                                          
push @fpList, "rdkit_fp";
push @fpList, "torsionbv_fp";

# drop and create the fingerprints

my @sqlList;

foreach my $fp(@fpList){
	push @sqlList, "drop table if exists temp_" . $fp;
	push @sqlList, "create table temp_" . $fp . " as select nsc, $fp(mol) as fp from rdkit_mol where nsc in ( select nsc from app_and_inv )";
}

foreach my $sql(@sqlList){
  my $sth = $dbh->prepare($sql);
  print "$sql\n\n";
 $sth->execute() or warn "Couldn't execute statement: $sql" . $sth->errstr;	
}

# run the tanimoto

@sqlList = {};
foreach my $fp(@fpList){
	print "$fp";
	push @sqlList, "drop table if exists temp_tanimoto_" . $fp;	
	push @sqlList, "create table temp_tanimoto_" . $fp . " as select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto from temp_" . $fp . " uno, temp_" . $fp . " duo where uno.nsc < duo.nsc";
}

foreach my $sql(@sqlList){
  my $sth = $dbh->prepare($sql);
  print "$sql\n\n";
 $sth->execute() or warn "Couldn't execute statement: $sql" . $sth->errstr;	
}

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";
print "finish: $finishDate\n";


