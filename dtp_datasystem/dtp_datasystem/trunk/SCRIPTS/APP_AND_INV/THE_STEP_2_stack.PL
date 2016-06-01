#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');

use DBI;

sub ltrim { my $s = shift; $s =~ s/^\s+//;       return $s };
sub rtrim { my $s = shift; $s =~ s/\s+$//;       return $s };
sub  trim { my $s = shift; if ($s){ $s =~ s/^\s+|\s+$//g; } else { $s = '' } return $s };

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "--start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

my @sqlList = ();
my $sql;

my @multiEntryFields = ("alias_names", "other_targets");
my @singleEntryFields = ("generic_name", "preferred_name", "originator", "primary_target", "type", "project_code");
my $whichField;

#  ####   ######   #####  #    #  #####
# #       #          #    #    #  #    #
#  ####   #####      #    #    #  #    #
#      #  #          #    #    #  #####
# #    #  #          #    #    #  #
#  ####   ######     #     ####   #

push @sqlList, qq[drop table if exists fields_and_entries];

push @sqlList, qq[create table fields_and_entries( nsc int, cas varchar, field_name varchar, entry varchar )];

foreach $sql(@sqlList){
	my $sth = $dbh->prepare($sql);
        print "$sql\n\n";
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;		
}


# #    #  #    #  #        #####     #    ######  #    #   #####  #####    #   #
# ##  ##  #    #  #          #       #    #       ##   #     #    #    #    # #
# # ## #  #    #  #          #       #    #####   # #  #     #    #    #     #
# #    #  #    #  #          #       #    #       #  # #     #    #####      #
# #    #  #    #  #          #       #    #       #   ##     #    #   #      #
# #    #   ####   ######     #       #    ######  #    #     #    #    #     #

foreach $whichField(@multiEntryFields){
	
	$sql = qq[select nsc, cas, $whichField from app_and_inv where $whichField is not null and length($whichField) > 0];
	my $sth = $dbh->prepare($sql);
	print "$sql\n\n";
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $nsc;
        my $trimmedNsc;

        my $cas;
        my $trimmedCas;

	my $val;

	my @splitVal;
	my $indivVal;
	my $trimmedVal;
	
	$sth->bind_columns(\($nsc, $cas, $val));
	while ($sth->fetch){    

                $trimmedCas = trim($cas);

		@splitVal = split(';', $val);
		foreach $indivVal(@splitVal){
			$trimmedVal = trim($indivVal);
                        if (length($trimmedVal) > 0){
			$dbh->do('insert into fields_and_entries (nsc, cas, field_name, entry) values (?,?,?,?)',
				undef,
				$nsc, $trimmedCas, $whichField, $trimmedVal);  
                }
    }    
  }
}

foreach $whichField(@singleEntryFields){
	
	$sql = qq[select nsc, cas, $whichField from app_and_inv where $whichField is not null and length($whichField) > 0];
	my $sth = $dbh->prepare($sql);
	print "$sql\n\n";
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $nsc;
        my $cas;
	my $val;
	my $trimmedVal;
	my @vals = ();
	
	$sth->bind_columns(\($nsc, $cas, $val));
	
	while ($sth->fetch){    
		$trimmedVal = trim($val);    	
		$dbh->do('insert into fields_and_entries (nsc, cas, field_name, entry) values (?,?,?,?)',
  		undef,
  		$nsc, $cas, $whichField, $trimmedVal);  
  }  
  
}



# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "--start : $startDate\n";
print "--finish: $finishDate\n";

