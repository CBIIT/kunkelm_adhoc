#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');

use DBI;

sub ltrim { my $s = shift; $s =~ s/^\s+//;       return $s };
sub rtrim { my $s = shift; $s =~ s/\s+$//;       return $s };
sub  trim { my $s = shift; $s =~ s/^\s+|\s+$//g; return $s };

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "--start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

my @sqlList = ();
my $sql;

my @multiEntryFields = ("generic_name", "preferred_name", "alias_names", "other_targets");
my @singleEntryFields = ("originator", "primary_target");
my $whichField;

#  ####   ######   #####  #    #  #####
# #       #          #    #    #  #    #
#  ####   #####      #    #    #  #    #
#      #  #          #    #    #  #####
# #    #  #          #    #    #  #
#  ####   ######     #     ####   #

# drop table if exists fields_and_entries;
# create table fields_and_entries( field_name varchar, entry varchar );

# #    #  #    #  #        #####     #    ######  #    #   #####  #####    #   #
# ##  ##  #    #  #          #       #    #       ##   #     #    #    #    # #
# # ## #  #    #  #          #       #    #####   # #  #     #    #    #     #
# #    #  #    #  #          #       #    #       #  # #     #    #####      #
# #    #  #    #  #          #       #    #       #   ##     #    #   #      #
# #    #   ####   ######     #       #    ######  #    #     #    #    #     #

goto skipToHere;

foreach $whichField(@multiEntryFields){
	
	$sql = qq[select $whichField from app_and_inv where $whichField is not null];
	my $sth = $dbh->prepare($sql);
	# print "$sql\n\n";
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $val;
	my @splitVal;
	my $indivVal;
	my $trimmedVal;
	my @vals = ();
	
	$sth->bind_columns(\($val));
	while ($sth->fetch){    
            @splitVal = split(';', $val);
            foreach $indivVal(@splitVal){
            $trimmedVal = trim($indivVal);    	
            push @vals, $trimmedVal;
    }    
  }
  
  print "\n";
  print "\n";
  foreach $val(@vals){
  	print "$whichField: $val\n";
  	$dbh->do('insert into fields_and_entries (field_name, entry) values (?,?)',
  		undef,
  		$whichField, $val);  
  }
  
}



foreach $whichField(@singleEntryFields){

$sql = qq[select $whichField from app_and_inv where $whichField is not null];
	my $sth = $dbh->prepare($sql);
	# print "$sql\n\n";
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $val;
	my $trimmedVal;
	my @vals = ();
	
	$sth->bind_columns(\($val));

	while ($sth->fetch){    
            $trimmedVal = trim($val);    	
            push @vals, $trimmedVal;
        }  

 print "\n";
  print "\n";
  foreach $val(@vals){
  	print "$whichField: $val\n";
  	$dbh->do('insert into fields_and_entries (field_name, entry) values (?,?)',
  		undef,
  		$whichField, $val);  
  }
  
  }
	

#  ####    ####
# #    #  #    #
# #    #  #
# #  # #  #
# #   #   #    #
#  ### #   ####

skipToHere:

# NOW, cycle back through all fields

my @allFields = ("generic_name", "preferred_name", "alias_names", "other_targets", "originator", "primary_target");

foreach $whichField(@allFields){

push @sqlList, qq[drop table if exists temp;];
	
	push @sqlList, qq[create table temp
	as
	select entry, regexp_replace(lower(entry), '[ -]','') as fixed, count(*) 
	from fields_and_entries
        where field_name = '$whichField'
	group by entry
	order by entry;];
	
	push @sqlList, qq[drop table if exists temp2;];
	
	push @sqlList, qq[create table temp2
	as 
	select fixed, array_to_string(array_agg(distinct entry), ','), count(distinct entry) 
	from temp
	group by fixed
	order by count(distinct entry) desc, fixed asc;];
	
	push @sqlList, qq[drop table if exists temp3;];
	
	push @sqlList, qq[create table temp3
	as
	select '--->'||entry||'<---'
	from fields_and_entries
	where field_name = '$whichField'
        and regexp_replace(lower(entry), '[ -]','') in (
            select fixed from temp2
            where "count" > 1
	)
	order by entry;];
	
	push @sqlList, qq[\\copy temp3 to /tmp/app_and_inv_$whichField\_formatting.csv csv header];
	
}

foreach $sql(@sqlList){
	print "$sql\n\n";
}

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "--start : $startDate\n";
print "--finish: $finishDate\n";

