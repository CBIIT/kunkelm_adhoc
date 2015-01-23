#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
use CATEGORYPARAMETERS qw(getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $pgh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});
my $orh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

#my $orh = DBI->connect("DBI:Oracle:ops\$oradis", "kunkelm", "donkie", {'RaiseError' => 1} );

#$dbh->{AutoCommit}    = 0;
#$dbh->{RaiseError}    = 1;
#$dbh->{ora_check_sql} = 0;
#$dbh->{RowCacheSize}  = 16;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

# for each fetch, a list with table name, drop statement, create statment, 
# insert statment, select statement 

# running list for individual fetches
my @allFetchesList = ();

push @allFetchesList,
[

"prod_mtxt",

qq[drop table if exists prod_mtxt],

qq[create table prod_mtxt (nsc int, mtxt varchar(4096))],

qq[insert into prod_mtxt(nsc, mtxt) values (?,?)],

qq[select cmpd.nsc, ct.text as mtxt 
from ops\$oradis.dis_cmpd cmpd, ops\$oradis.cmpd_topic ct 
where cmpd.prefix = 's'
and cmpd.cmpd_id = ct.cmpd_id
and ct.topic = 'mtxt']

];

push @allFetchesList, 

[

"prod_inventory",

qq[drop table if exists prod_inventory],

qq[create table prod_inventory (nsc int, inventory double precision)],

qq[insert into prod_inventory(nsc, inventory) values (?,?)],

qq[select nsc, mg as inventory 
from ops\$oradis.dis_nscs 
where prefix = 's']

];

push @allFetchesList,

[

"prod_chem_name",

qq[drop table if exists prod_chem_name],

qq[create table prod_chem_name (cmpd_id bigint, nsc int, chem_name varchar(1024), chem_name_type varchar(1024))],

qq[insert into prod_chem_name(cmpd_id, nsc, chem_name, chem_name_type) values (?,?,?,?)],

qq[select cmpd.cmpd_id, cmpd.nsc, cn.name, cn.name_type 
from ops\$oradis.dis_cmpd cmpd, ops\$oradis.cmpd_chem_name cn 
where cmpd.prefix = 's'
and cmpd.cmpd_id = cn.cmpd_id]

];

push @allFetchesList,

[

"prod_count_hf",

qq[drop table if exists prod_count_hf],

qq[create table prod_count_hf(nsc int, the_count int)],

qq[insert into prod_count_hf(nsc, the_count) values (?,?)],

qq[select ed.nsc, count(*) the_count 
from invivo.exp_decode ed, ops\$oradis.dis_cmpd c 
where ed.assay_type = 9 
and c.prefix = 'S' 
and ed.prefix = c.prefix 
and ed.nsc = c.nsc 
group by ed.nsc]

];

push @allFetchesList, 

[

"prod_count_nci60",

qq[drop table if exists prod_count_nci60],

qq[create table prod_count_nci60(nsc int, the_count int)],

qq[insert into prod_count_nci60(nsc, the_count) values (?,?)],

qq[select ctl.nsc, count(distinct ctl.expid) the_count 
from c_tline ctl, ops\$oradis.dis_cmpd c 
where c.prefix = 'S' 
and ctl.prefix = c.prefix 
and ctl.nsc = c.nsc 
group by ctl.nsc]

];

push @allFetchesList, 

[

"prod_count_xeno",

qq[drop table if exists prod_count_xeno],

qq[create table prod_count_xeno(nsc int, the_count int)], 

qq[insert into prod_count_xeno(nsc, the_count) values (?,?)],

qq[select ed.nsc, count(*) the_count 
from invivo.exp_decode ed, ops\$oradis.dis_cmpd c 
where ed.assay_type in (3, 5) 
and c.prefix = 'S' 
and ed.prefix = c.prefix 
and ed.nsc = c.nsc 
group by ed.nsc]

];

push @allFetchesList, 

[

"prod_plated_sets",

qq[drop table if exists prod_plated_sets],

qq[create table prod_plated_sets (nsc int, plate_set varchar(1024))],

qq[insert into prod_plated_sets(nsc, plate_set) values (?,?)],

qq[select distinct nsc, plateset as plate_set 
from ops\$oradis.oradis_dis_well]

];

push @allFetchesList, 

[

"prod_projects",

qq[drop table if exists prod_projects],

qq[create table prod_projects (nsc int, project_code varchar(1024), description varchar(1024))],

qq[insert into prod_projects(nsc, project_code, description) values (?,?,?)],

qq[select cp.nsc, cp.projectcode as project_code, lu.description 
from ops\$oradis.chem_project cp, ops\$oradis.lookup_project lu 
where cp.projectcode = lu.projectcode]

];

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

foreach my $ref(@allFetchesList){
	
	my ($tableName, $dropStmt, $createStmt, $insertStmt, $selectStmt) = @$ref;
	
	print qq[# table: $tableName;\n];
	print qq[\$dropStmt = "$dropStmt";\n];
	print qq[\$createStmt = "$createStmt";\n];
	print qq[\$insertStmt = "$insertStmt";\n];
	print qq[\$selectStmt = "$selectStmt";\n\n\n];
	
	
# 	eval{
#		my $psh=$pgh->prepare($insertStmt);
#		my $osh=$orh->prepare($selStmt);
#		$osh->execute();
#		$bindStmt;
#		#$osh->bind_columns( \( $f1, $f2, $f3, ... ) );
#		while($osh->fetch)
#		{
#			$executeStmt;
#			#$psh->execute( $f1, $f2, $f3, ... );
#		}
#		$pgh->commit;
#	}
		
}





## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

my @idxList = ();

push @idxList, qq[drop index if exists prod_mtxt_nsc
];

push @idxList, qq[create index prod_mtxt_nsc on prod_mtxt(nsc)
];

push @idxList, qq[drop index if exists prod_inventory_nsc
];

push @idxList, qq[create index prod_inventory_nsc on prod_inventory(nsc)
];

push @idxList, qq[drop index if exists prod_chem_name_cmpd_id
];

push @idxList, qq[create index prod_chem_name_cmpd_id on prod_chem_name(cmpd_id)
];

push @idxList, qq[drop index if exists prod_chem_name_nsc
];

push @idxList, qq[create index prod_chem_name_nsc on prod_chem_name(nsc)
];

push @idxList, qq[drop index if exists prod_count_hf_nsc
];

push @idxList, qq[create index prod_count_hf_nsc on prod_count_hf(nsc)
];

push @idxList, qq[drop index if exists prod_count_nci60_nsc
];

push @idxList, qq[create index prod_count_nci60_nsc on prod_count_nci60(nsc)
];

push @idxList, qq[drop index if exists prod_count_xeno_nsc
];

push @idxList, qq[create index prod_count_xeno_nsc on prod_count_xeno(nsc)
];

# finally, build the combined prod_bio_counts table

my @sqlList = ();

push @sqlList, qq[drop table if exists prod_bio_counts
];

push @sqlList, qq[create table prod_bio_counts 
as select 
prod_src.nsc, 
prod_count_nci60.the_count as prod_count_nci60, 
prod_count_hf.the_count as prod_count_hf, 
prod_count_xeno.the_count as prod_count_xeno 
from prod_src 
left outer join prod_count_nci60 on prod_src.nsc = prod_count_nci60.nsc 
left outer join prod_count_hf on prod_src.nsc = prod_count_hf.nsc 
left outer join prod_count_xeno on prod_src.nsc = prod_count_xeno.nsc
];

