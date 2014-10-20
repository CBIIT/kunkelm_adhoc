#!/usr/bin/perl

use strict;

open ( FILENAMES, "<./list.csv") or die "Can't open ./list.csv\n";

my @fileNameList;

while (<FILENAMES>) {
	my $fileName = $_;
	chomp($fileName);	
	$fileName =~ s/\015//g;
	push(@fileNameList, $fileName);
}

close FILENAMES;

for (my $listCnt = 0; $listCnt <= 19; $listCnt++) {
	
	open (my $OUTPUT, ">/tmp/randomFiles$listCnt.asx") or die "Can't open /tmp/randomFiles$listCnt.asx\n";
	
	print $OUTPUT "<asx version = \"3.0\" >\n";
	
	for (my $i = 0; $i <= 9; $i++) {
		
		my $thisRand = int(rand(@fileNameList));		
		
		my $out = qq[
		<entry>
		<title>title$i</title>
		<ref href = "$fileNameList[$thisRand]"/>
		</entry>
		];
		
		print $OUTPUT $out;
		
	}
	
	print $OUTPUT "</asx>\n";
	
	close $OUTPUT;
	
}

print "Done\n";


