#!/usr/bin/perl

use strict;

my $fileName = "schrodingerSaltList.txt";

print "Processing $fileName ---------------------------------------\n";

open (INPUT, "< $fileName") or die "Can't open $fileName\n";

my $extension = ".out";
my $outputFile = "$fileName$extension";

print "Outputting to $outputFile\n";

open (OUTPUT, "> $outputFile") or die "Can't open $outputFile\n";

while (<INPUT>) {
	my $thisLine = $_;
	chomp($thisLine);	
	my @splitLine = split(/","/, $thisLine);
	
	my $identifier = $splitLine[-1];
	$identifier =~ s/"//g;
	
	foreach my $saltSmiles(@splitLine){

		$saltSmiles =~ s/"//g;		
		
		if ($saltSmiles eq $identifier)		{
		} else {
			print qq["$saltSmiles","$identifier",\n];
			print OUTPUT qq["$saltSmiles","$identifier",\n];
		}
	}
	
}

close INPUT;
close OUTPUT;

print "Done\n";


