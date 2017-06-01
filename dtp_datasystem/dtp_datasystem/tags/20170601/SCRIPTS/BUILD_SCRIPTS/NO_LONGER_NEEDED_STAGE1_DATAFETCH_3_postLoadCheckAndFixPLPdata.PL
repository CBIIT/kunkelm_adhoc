#!/usr/bin/perl

# NO LONGER NEEDED SINCE ALL plp output files now include all NSC in a single run

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
use DATA_SYSTEM_BUILD_PARAMETERS qw(getDatasystemTableList getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 1});

my $sql = qq[
select min(fragmentindex), max(fragmentindex)  
from rs3_from_plp_frags 
where nsc <= 400000
];
my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
my $frags400_minFragIdx; my $frags400_maxFragIdx;
$sth->bind_columns(\$frags400_minFragIdx, \$frags400_maxFragIdx);
while ($sth->fetch()){
}

my $sql = qq[
select min(fragmentindex), max(fragmentindex)  
from rs3_from_plp_frags 
where nsc > 400000
];
my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
my $fragsRemain_minFragIdx; my $fragsRemain_maxFragIdx;
$sth->bind_columns(\$fragsRemain_minFragIdx, \$fragsRemain_maxFragIdx);
while ($sth->fetch()){
}


my $sql = qq[
select min(fragmentindex), max(fragmentindex)  
from rs3_from_plp_ctab
where nsc <= 400000
];
my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
my $ctab400_minFragIdx; my $ctab400_maxFragIdx;
$sth->bind_columns(\$ctab400_minFragIdx, \$ctab400_maxFragIdx);
while ($sth->fetch()){
}

my $sql = qq[
select min(fragmentindex), max(fragmentindex)  
from rs3_from_plp_ctab
where nsc > 400000
];
my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
my $ctabRemain_minFragIdx; my $ctabRemain_maxFragIdx;
$sth->bind_columns(\$ctabRemain_minFragIdx, \$ctabRemain_maxFragIdx);
while ($sth->fetch()){
}

print "frags400_minFragIdx: $frags400_minFragIdx frags400_maxFragIdx: $frags400_maxFragIdx\n";
print "fragsRemain_minFragIdx: $fragsRemain_minFragIdx fragsRemain_maxFragIdx: $fragsRemain_maxFragIdx\n\n\n";
print "ctab400_minFragIdx: $ctab400_minFragIdx ctab400_maxFragIdx: $ctab400_maxFragIdx\n";
print "ctabRemain_minFragIdx: $ctabRemain_minFragIdx ctabRemain_maxFragIdx: $ctabRemain_maxFragIdx\n\n\n";

## -- only update the fragmentindex if problems were found
##
##update rs3_from_plp_frags
##set fragmentindex = fragmentindex + 1000000
##where nsc > 400000;
##
##update rs3_from_plp_ctab
##set fragmentindex = fragmentindex + 1000000
##where nsc > 400000;
##

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";
print "finish: $finishDate\n";


