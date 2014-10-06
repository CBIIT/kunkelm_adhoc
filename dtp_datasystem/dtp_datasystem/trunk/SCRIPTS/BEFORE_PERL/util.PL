#!/usr/bin/perl

use strict;

my @allTbls = qw(
one_strc
one_strc_salt
one_lbl
multi_lbl
one_strc_one_lbl
one_strc_multi_lbl
two_strc_any_lbl_one_salt
two_strc_any_lbl_two_salt
two_strc_any_lbl
multi_strc_any_lbl
	);

open (OUTPUT, ">/tmp/output.sql");

print OUTPUT "drop table if exists qc;\n";

print OUTPUT "create table qc (source varchar(1024), comparator varchar(1024), target varchar(1024), the_count int);\n";

# counts from all categories

for my $cnt(0 .. @allTbls - 1){

			my $heredoc = <<END;

    drop table if exists temp;

create table temp
as
select nsc, mf, mw, sum(molecular_weight) as fw
from $allTbls[$cnt]
group by 1, 2, 3;

insert into qc 
select '$allTbls[$cnt]', 'count nsc', 'count nsc', count(distinct nsc) 
from temp;

insert into qc
select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 0', count(distinct nsc) 
from temp 
where round(mw) = round(fw);

insert into qc
select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 2', count(distinct nsc) 
from temp 
where abs( round(mw) - round(fw) ) < 2;

insert into qc
select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 5', count(distinct nsc) 
from temp 
where abs( round(mw) - round(fw) ) < 5;

insert into qc
select '$allTbls[$cnt]', 'count nsc', 'mf W99', count(distinct nsc) 
from temp 
where mf  = 'W99';

insert into qc
select '$allTbls[$cnt]', 'count nsc', 'mf with .', count(distinct nsc) 
from temp 
where mf  like '%.%';

insert into qc
select '$allTbls[$cnt]', 'count nsc', 'mf ~ .*\(.*\)[xn0-9]', count(distinct nsc) 
from temp 
where mf ~ '.*\\(.*\\)[xn0-9]';

END
			
			print OUTPUT $heredoc;


}

# overlaps of categories

print OUTPUT "drop table if exists qc_overlap;\n";

print OUTPUT "create table qc_overlap (source varchar(1024), target varchar(1024), the_count int);\n";

for my $cnt(0 .. @allTbls - 1){	
	for my $innerCnt($cnt .. @allTbls - 1){
		
		if ($cnt != $innerCnt){
		
                print "'$allTbls[$cnt]', 'intersect', '$allTbls[$innerCnt]'\n";
	
			my $heredoc = <<END;

insert into qc_overlap
select '$allTbls[$cnt]', '$allTbls[$innerCnt]',
count(i.*)
from (
select nsc from $allTbls[$cnt]
intersect
select nsc from $allTbls[$innerCnt]
) as i;

END
			
			print OUTPUT $heredoc;
			
			
		}
	}			
}
			
			close OUTPUT;
			

