#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

my $db = "microxeno";
my $user = "microxeno_user";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=$db;host=ncidb-d115-d.nci.nih.gov;port=5473", "microxeno", "M1cr0x0293025en0", {'RaiseError' => 1});

my @readOnlyTables = qw(
affymetrix_data           
affymetrix_identifier     
ea_lookup                 
flat_avg_data             
flat_data                 
hg_u133_plus2             
import_rma                
tumor                     
tumor_type                
unigene236              
	);

my @writableTables = qw(
	);

my @forbiddenSequences = qw(
 affymetrix_identifier_seq 
 flat_avg_data_seq         
 flat_data_seq             
 hibernate_sequence  
	);

my @usableSequences = qw(
	);

my @sqlList = ();

my @allTables = ();
push @allTables, @readOnlyTables;
push @allTables, @writableTables;

my @allSequences = ();
push @allSequences, @forbiddenSequences;
push @allSequences, @usableSequences;

 #####   ######  #    #   ####   #    #  ######
 #    #  #       #    #  #    #  #   #   #
 #    #  #####   #    #  #    #  ####    #####
 #####   #       #    #  #    #  #  #    #
 #   #   #        #  #   #    #  #   #   #
 #    #  ######    ##     ####   #    #  ######

foreach my $tbl(@allTables){
	push @sqlList, "revoke all on table $tbl from $user";
}

foreach my $seq(@allSequences){
	push @sqlList, "revoke all on sequence $seq from $user";
}

  #####  #####   #                ####   #####     ##    #    #   #####   ####
    #    #    #  #               #    #  #    #   #  #   ##   #     #    #
    #    #####   #               #       #    #  #    #  # #  #     #     ####
    #    #    #  #               #  ###  #####   ######  #  # #     #         #
    #    #    #  #               #    #  #   #   #    #  #   ##     #    #    #
    #    #####   ######           ####   #    #  #    #  #    #     #     ####


foreach my $tbl(@readOnlyTables){
	push @sqlList, "grant select on table $tbl to $user";
}
	
foreach my $tbl(@writableTables){
	push @sqlList, "grant select, insert, update on table $tbl to $user";
}

  ####   ######   ####            ####   #####     ##    #    #   #####   ####
 #       #       #    #          #    #  #    #   #  #   ##   #     #    #
  ####   #####   #    #          #       #    #  #    #  # #  #     #     ####
      #  #       #  # #          #  ###  #####   ######  #  # #     #         #
 #    #  #       #   #           #    #  #   #   #    #  #   ##     #    #    #
  ####   ######   ### #           ####   #    #  #    #  #    #     #     ####

  foreach my $seq(@usableSequences){
  		push @sqlList, "grant usage, select on sequence $seq to $user";
  }
  

foreach my $sqlStmt(@sqlList){
	my $sth = $dbh->prepare($sqlStmt);
	print "$sqlStmt\n\n";
	$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;	
}

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


