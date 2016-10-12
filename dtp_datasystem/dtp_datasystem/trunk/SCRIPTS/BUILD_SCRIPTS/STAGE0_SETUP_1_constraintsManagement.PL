#!/usr/bin/perl

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

my @datasystemTableList = getDatasystemTableList();

my $commaJoinedTableList = join("',\n'", @datasystemTableList);

# TO DROP CONSTRAINTS

my $sql = qq[
########################## drop table if exists drop_constraint_statements
];

my $sth = $dbh->prepare($sql);
print "$sql\n\n";
########################## $sth->execute() or die "Couldn't execute statement: $sql" . $sth->errstr;	

print "DANGER! WARNING! drop_constraint_statements already exists!\n";
print "DANGER! WARNING! drop_constraint_statements already exists!\n";
print "DANGER! WARNING! drop_constraint_statements already exists!\n";
print "DANGER! WARNING! drop_constraint_statements already exists!\n";
print "DANGER! WARNING! drop_constraint_statements already exists!\n";
print "DANGER! WARNING! drop_constraint_statements already exists!\n";
print "DANGER! WARNING! drop_constraint_statements already exists!\n";
print "DANGER! WARNING! drop_constraint_statements already exists!\n";
print "DANGER! WARNING! drop_constraint_statements already exists!\n";
print "DANGER! WARNING! drop_constraint_statements already exists!\n";
print "DANGER! WARNING! drop_constraint_statements already exists!\n";

my $sql = qq[
create table drop_constraint_statements(stmt varchar)
];

my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql" . $sth->errstr;	

my $sql = qq[
insert into drop_constraint_statements(stmt)
SELECT 'ALTER TABLE '||nspname||'.'||relname||' DROP CONSTRAINT '||conname||' cascade;' as statement 
FROM pg_constraint 
INNER JOIN pg_class ON conrelid = pg_class.oid 
INNER JOIN pg_namespace ON pg_namespace.oid = pg_class.relnamespace
WHERE conname not like '%pkey'
AND relname in (
'$commaJoinedTableList'
) 
ORDER BY CASE WHEN contype = 'f' THEN 0 ELSE 1 END, contype, nspname, relname, conname
];

my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql" . $sth->errstr;	

## CREATE CONSTRAINTS

my $sql = qq[
drop table if exists create_constraint_statements
];

my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql" . $sth->errstr;	

my $sql = qq[
create table create_constraint_statements(stmt varchar)
];

my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql" . $sth->errstr;	

my $sql = qq[
insert into create_constraint_statements(stmt)
SELECT 'ALTER TABLE '||nspname||'.'||relname||' ADD CONSTRAINT '||conname||' '||pg_get_constraintdef(pg_constraint.oid)||';' as statement
FROM pg_constraint
INNER JOIN pg_class ON conrelid = pg_class.oid
INNER JOIN pg_namespace ON pg_namespace.oid = pg_class.relnamespace
WHERE conname not like '%pkey'
AND relname in (
'$commaJoinedTableList'
)
ORDER BY CASE WHEN contype = 'f' THEN 0 ELSE 1 END DESC, contype DESC, nspname DESC, relname DESC, conname DESC
];

my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql" . $sth->errstr;	

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";
print "finish: $finishDate\n";


