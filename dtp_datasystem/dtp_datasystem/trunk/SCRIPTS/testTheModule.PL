use strict;
use warnings;

use lib('./');
use CATEGORYPARAMETERS qw(getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

use Data::Dumper;

my @qcCritList = getQualityControlCriteriaList();
#print join(",\n", @qcCritList);

my @catList = getCategoryList();
#print join(",\n", @catList);

my @catCritList = getCategoryCriteriaList();
#print join(",\n", @catCritList);

my @hashList;

foreach my $thisCatCrit (@catCritList){

	# print $thisCatCrit . "\n";
	
	my ($category,	$count_frags,	$count_lbl,	$count_strc,	$salt_count) = split(/\t/, $thisCatCrit);
	
	#print "category: " . $category . "\n";	
	#print "count_frags: " . $count_frags . "\n";	
	#print "count_lbl: " . $count_lbl . "\n";	
	#print "count_strc: " . $count_strc . "\n";	
	#print "salt_count:" . $salt_count . "\n";
	
	push @hashList, {	
		"category" => $category,	
		"count_frags" => $count_frags,	
		"count_lbl" => $count_lbl,
		"count_strc" => $count_strc,	
		"salt_count" => $salt_count
	};
	
	#print Dumper(%ctg);
	
}

foreach my $hash_ref (@hashList) {
	foreach (keys %{$hash_ref}) {
		print "$_ => ${$hash_ref}{$_}\t";
	}
	print "\n";
}

foreach my $hashRef (@hashList){
	
	print "category: " . $hashRef->{"category"} . ", ";	
	print "count_frags: " . $hashRef->{"count_frags"} . ", ";
	print "count_lbl: " . $hashRef->{"count_lbl"} . ", ";
	print "count_strc: " . $hashRef->{"count_strc"} . ", ";	
	print "salt_count: " . $hashRef->{"salt_count"} . "\n";
	
	
}
