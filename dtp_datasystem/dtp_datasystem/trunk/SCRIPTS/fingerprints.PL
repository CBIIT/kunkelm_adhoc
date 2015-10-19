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

 ######  #####
 #       #    #
 #####   #    #
 #       #####
 #       #
 #       #

my @fpList;
push @fpList, "atompairbv_fp";                                                                           
# push @fpList, "avalon_fp";                              
push @fpList, "featmorganbv_fp";                                                      
push @fpList, "layered_fp";                                                                              
push @fpList, "maccs_fp";                                                                                
push @fpList, "morganbv_fp";                                                          
push @fpList, "rdkit_fp";
push @fpList, "torsionbv_fp";

 #####   ######  ######     #    #    #  ######
 #    #  #       #          #    ##   #  #
 #    #  #####   #####      #    # #  #  #####
 #    #  #       #          #    #  # #  #
 #    #  #       #          #    #   ##  #
 #####   ######  #          #    #    #  ######


   ##    #       #               #####     ##       #    #####
  #  #   #       #               #    #   #  #      #    #    #
 #    #  #       #               #    #  #    #     #    #    #
 ######  #       #               #####   ######     #    #####
 #    #  #       #               #       #    #     #    #   #
 #    #  ######  ###### #######  #       #    #     #    #    #


#drop table if exists all_pair_nsc;
#create table all_pair_nsc as select u.nsc as uno, d.nsc as duo from app_and_inv u, app_and_inv d where u.nsc < d.nsc;

 #####    ####           ######  #####
 #    #  #    #          #       #    #
 #    #  #    #          #####   #    #
 #    #  #    #          #       #####
 #    #  #    #          #       #
 #####    ####           #       #

my @sqlList;
my $sth;

foreach my $fp(@fpList){
	push @sqlList, "drop table if exists temp_" . $fp;
	push @sqlList, "create table temp_" . $fp . " as select nsc, $fp(mol) as fp from rdkit_mol where nsc in ( select nsc from app_and_inv )";
}

foreach my $sql(@sqlList){
  $sth = $dbh->prepare($sql);
  print "$sql\n\n";
#$sth->execute() or warn "Couldn't execute statement: $sql" . $sth->errstr;	
}



  #####    ##    #    #     #    #    #   ####    #####   ####
    #     #  #   ##   #     #    ##  ##  #    #     #    #    #
    #    #    #  # #  #     #    # ## #  #    #     #    #    #
    #    ######  #  # #     #    #    #  #    #     #    #    #
    #    #    #  #   ##     #    #    #  #    #     #    #    #
    #    #    #  #    #     #    #    #   ####      #     ####

@sqlList = ();

foreach my $fp(@fpList){
	print "$fp";
	push @sqlList, "drop table if exists temp_tanimoto_" . $fp;	
	push @sqlList, "create table temp_tanimoto_" . $fp . " as select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto from temp_" . $fp . " uno, temp_" . $fp . " duo where uno.nsc < duo.nsc";
}

foreach my $sql(@sqlList){
  $sth = $dbh->prepare($sql);
  print "$sql\n\n";
 #$sth->execute() or warn "Couldn't execute statement: $sql" . $sth->errstr;	
}

# check on counts

@sqlList = ();

foreach my $fp(@fpList){
	print "$fp";
	push @sqlList, "select count(*) from temp_tanimoto_" . $fp;		
}

foreach my $sql(@sqlList){
  my $sth = $dbh->prepare($sql);
  print "$sql\n\n";
 #$sth->execute() or warn "Couldn't execute statement: $sql" . $sth->errstr;	
}

  ####   #####    ####    ####    ####    #####    ##    #####
 #    #  #    #  #    #  #       #          #     #  #   #    #
 #       #    #  #    #   ####    ####      #    #    #  #####
 #       #####   #    #       #       #     #    ######  #    #
 #    #  #   #   #    #  #    #  #    #     #    #    #  #    #
  ####   #    #   ####    ####    ####      #    #    #  #####

@sqlList = ();

my $sql = "drop table if exists temp_tanimoto_collate";

$sql = "create table temp_tanimoto_collate as select all_pair_nsc.uno, all_pair_nsc.duo ";

foreach my $fp(@fpList){	
	$sql = $sql . "\n, temp_tanimoto_" . $fp . ".tanimoto as " . $fp . "_tanimoto";
}

$sql = $sql . "\n from all_pair_nsc ";

foreach my $fp(@fpList){	
	$sql = $sql . "\n left outer join temp_tanimoto_" . $fp . " on all_pair_nsc.uno = temp_tanimoto_" . $fp . ".nsc1 and all_pair_nsc.duo = temp_tanimoto_" . $fp . ".nsc2";
}

print "$sql\n\n";

$sth = $dbh->prepare($sql);
#$sth->execute() or warn "Couldn't execute statement: $sql" . $sth->errstr;	

  ####    #####    ##     ####   #    #  ######  #####
 #          #     #  #   #    #  #   #   #       #    #
  ####      #    #    #  #       ####    #####   #    #
      #     #    ######  #       #  #    #       #    #
 #    #     #    #    #  #    #  #   #   #       #    #
  ####      #    #    #   ####   #    #  ######  #####

@sqlList = ();

$sql = "drop table if exists temp_tanimoto_stacked";

push @sqlList, $sql;

$sql = "create table temp_tanimoto_stacked( uno int, duo int, fp varchar, tanimoto double precision )";

push @sqlList, $sql;

foreach my $fp(@fpList){
	print "$fp";
	push @sqlList, "insert into temp_tanimoto_stacked select nsc1, nsc2, '$fp', tanimoto from temp_tanimoto_" . $fp . " where nsc1 < nsc2";
}

foreach my $sql(@sqlList){
  $sth = $dbh->prepare($sql);
  print "$sql\n\n";
$sth = $dbh->prepare($sql);
$sth->execute() or warn "Couldn't execute statement: $sql" . $sth->errstr;	
}


 ######  #    #  #####    ####   #####    #####
 #        #  #   #    #  #    #  #    #     #
 #####     ##    #    #  #    #  #    #     #
 #         ##    #####   #    #  #####      #
 #        #  #   #       #    #  #   #      #
 ######  #    #  #        ####   #    #     #

@sqlList = ();

# hard-code statement for temp_tanimoto_collate
$sql = "\\copy temp_tanimoto_collate to /tmp/temp_tanimoto_collate.csv csv header";
push @sqlList, $sql;

foreach my $fp(@fpList){	            
        $sql = "\\copy temp_tanimoto_" . $fp . " to /tmp/temp_tanimoto_" . $fp . ".csv csv header";
        push @sqlList, $sql;
}

foreach my $sql(@sqlList){
  $sth = $dbh->prepare($sql);
  print "$sql\n\n";
  # Can't execute \copy over DBI...   just print these out
  #$sth->execute() or warn "Couldn't execute statement: $sql" . $sth->errstr;	
}


# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";
print "finish: $finishDate\n";


