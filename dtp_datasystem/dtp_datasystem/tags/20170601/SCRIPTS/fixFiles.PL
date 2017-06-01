#!/usr/bin/perl

use strict;
use warnings;

my $inputFile = "/home/mwkunkel/rs3_from_plp_problems.csv";
my $outputFile = "/tmp/crap.csv";

print "Reading from:  $inputFile\n";
print "Outputting to: $outputFile\n";

open (INPUT, "< $inputFile") or die "Can't open $inputFile\n";
open (OUTPUT, "> $outputFile") or die "Can't open $outputFile\n";

my @colIdxList = ();
#@colIdxList = qw[0 1 2 3 5609 5610 5611 5612 5613 5614 5615 5616 5617 5618 5619 5620 5621 5622 5623 5624 5625 5626 5627 5628 5629 5630 5631 5632 5633 5634 5635 5636 5637 5638 5639 5640 5641 5642 5643 5644 5645 5646 5647];

@colIdxList = qw[0 5614];


while (<INPUT>) {
	
	my $thisLine = $_;
		
	my @splitLine = split(/\t/, $thisLine);
	
	#for my $i ( 0 .. $#colHeaders) {		
	#	if (substr($colHeaders[$i],0,4) ne "STRC"){			
	#		print "$i $colHeaders[$i]\n";
	#		push @colIdxList, $i;
	#	}		
	#}
	
	my $outline = join ",", @splitLine[ @colIdxList ];
	
	print "$outline\n";
	print OUTPUT "$outline\n";
	
	#$outline = join ",", @colIdxList;	
	#print "$outline\n";
	#print OUTPUT "$outline\n";
		
}


close INPUT;	
close OUTPUT;

#rename($outputFile, $inputFile);

print "Done\n";

