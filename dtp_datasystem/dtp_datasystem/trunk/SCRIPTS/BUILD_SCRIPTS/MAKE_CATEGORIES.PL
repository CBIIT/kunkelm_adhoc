#!/usr/bin/perl

use strict;
use warnings;

my @cntLbls = qw(=0 =1 =2 >2);
my @cntStrc = qw(=0 =1 =2 >2);
my @cntSalt = qw(=0 =1 =2 >2);

my $lookup = {
	"=0" => "no",
	"=1" => "one",
	"=2" => "two",
	">2" => "multi"
};

foreach my $lbl(@cntLbls){	
	foreach my $strc(@cntStrc){  	
		foreach my $salt(@cntSalt){
			my $catName = "$lookup->{$lbl}_lbl_$lookup->{$strc}_strc_$lookup->{$salt}_salt";
			print "$catName >0 $lbl $strc $salt\n"    	
		}
		
	}
}
