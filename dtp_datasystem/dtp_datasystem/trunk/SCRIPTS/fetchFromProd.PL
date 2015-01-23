#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $destConn = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1, 'AutoCommit' => 0});
my $srcConn = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

#my $srcConn = DBI->connect("DBI:Oracle:ops\$oradis", "kunkelm", "donkie", {'RaiseError' => 1} );

#$dbh->{AutoCommit}    = 0;
#$dbh->{RaiseError}    = 1;
#$dbh->{ora_check_sql} = 0;
#$dbh->{RowCacheSize}  = 16;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  #    #   #####  #    #   #####
##  ##  ##     #     #  #      #
##  # ## #     #      ##       #
##  #    #     #      ##       #
##  #    #     #     #  #      #
##  #    #     #    #    #     #

# table: prod_mtxt;
my $dropStmt = "drop table if exists prod_mtxt";
my $createStmt = "create table prod_mtxt (nsc int, mtxt varchar(4096))";
my $insertStmt = "insert into prod_mtxt(nsc, mtxt) values (?,?)";
#my $selectStmt = "select cmpd.nsc, ct.text 
#from ops\$oradis.dis_cmpd cmpd, ops\$oradis.cmpd_topic ct 
#where cmpd.prefix = 'S'
#and cmpd.cmpd_id = ct.cmpd_id
#and ct.topic = 'mtxt'";
my $selectStmt = "select nsc, mtxt from src_mtxt";

# drop and recreate
my $destHndl = $destConn -> prepare($dropStmt);
$destHndl -> execute();
$destHndl = $destConn -> prepare($createStmt);
$destHndl -> execute();
# prep insert statement
$destHndl = $destConn -> prepare($insertStmt);
# select and bind
my $srcHndl = $srcConn -> prepare($selectStmt);
$srcHndl -> execute();
my $nsc; my $mtxt;
$srcHndl -> bind_columns(\$nsc, \$mtxt);
# do inserts
while($srcHndl -> fetch()){	
	print "nsc: $nsc, mtxt: $mtxt\n";	
	$destHndl -> execute( $nsc, $mtxt );
}
# commit
$destConn -> commit();

##     #    #    #  #    #  ######  #    #   #####   ####   #####    #   #
##     #    ##   #  #    #  #       ##   #     #    #    #  #    #    # #
##     #    # #  #  #    #  #####   # #  #     #    #    #  #    #     #
##     #    #  # #  #    #  #       #  # #     #    #    #  #####      #
##     #    #   ##   #  #   #       #   ##     #    #    #  #   #      #
##     #    #    #    ##    ######  #    #     #     ####   #    #     #

# table: prod_inventory;
$dropStmt = "drop table if exists prod_inventory";
$createStmt = "create table prod_inventory (nsc int, inventory double precision)";
$insertStmt = "insert into prod_inventory(nsc, inventory) values (?,?)";
$selectStmt = "select nsc, mg as inventory 
from ops\$oradis.dis_nscs 
where prefix = 'S'";

# drop and recreate
$destHndl = $destConn -> prepare($dropStmt);
$destHndl -> execute();
$destHndl = $destConn -> prepare($createStmt);
$destHndl -> execute();
# prep insert statement
$destHndl = $destConn -> prepare($insertStmt);
# select and bind
$srcHndl = $srcConn -> prepare($selectStmt);
$srcHndl -> execute();
my $inventory;
$srcHndl -> bind_columns(\$nsc, \$inventory);
# do inserts
while($srcHndl -> fetch()){		
	$destHndl -> execute( $nsc, $inventory );
}
# commit
$destConn -> commit();

##   ####   #    #  ######  #    #          #    #    ##    #    #  ######
##  #    #  #    #  #       ##  ##          ##   #   #  #   ##  ##  #
##  #       ######  #####   # ## #          # #  #  #    #  # ## #  #####
##  #       #    #  #       #    #          #  # #  ######  #    #  #
##  #    #  #    #  #       #    #          #   ##  #    #  #    #  #
##   ####   #    #  ######  #    #          #    #  #    #  #    #  ######

# table: prod_chem_name;
$dropStmt = "drop table if exists prod_chem_name";
$createStmt = "create table prod_chem_name (nsc int, chem_name varchar(1024), chem_name_type varchar(1024))";
$insertStmt = "insert into prod_chem_name(nsc, chem_name, chem_name_type) values (?,?,?,?)";
$selectStmt = "select cmpd.nsc, cn.name, cn.name_type 
from ops\$oradis.dis_cmpd cmpd, ops\$oradis.cmpd_chem_name cn 
where cmpd.prefix = 'S'
and cmpd.cmpd_id = cn.cmpd_id";

# drop and recreate
$destHndl = $destConn -> prepare($dropStmt);
$destHndl -> execute();
$destHndl = $destConn -> prepare($createStmt);
$destHndl -> execute();
# prep insert statement
$destHndl = $destConn -> prepare($insertStmt);
# select and bind
$srcHndl = $srcConn -> prepare($selectStmt);
$srcHndl -> execute();
my $name, my $name_type;
$srcHndl -> bind_columns(\$nsc, \$name, \$name_type);
# do inserts
while($srcHndl -> fetch()){		
	$destHndl -> execute($nsc, $name, $name_type);
}
# commit
$destConn -> commit();

##  #    #  ######
##  #    #  #
##  ######  #####
##  #    #  #
##  #    #  #
##  #    #  #

# table: prod_count_hf;
$dropStmt = "drop table if exists prod_count_hf";
$createStmt = "create table prod_count_hf(nsc int, the_count int)";
$insertStmt = "insert into prod_count_hf(nsc, the_count) values (?,?)";
$selectStmt = "select ed.nsc, count(*) as the_count 
from invivo.exp_decode ed, ops\$oradis.dis_cmpd c 
where ed.assay_type = 9 
and c.prefix = 'S' 
and ed.prefix = c.prefix 
and ed.nsc = c.nsc 
group by ed.nsc";

# drop and recreate
$destHndl = $destConn -> prepare($dropStmt);
$destHndl -> execute();
$destHndl = $destConn -> prepare($createStmt);
$destHndl -> execute();
# prep insert statement
$destHndl = $destConn -> prepare($insertStmt);
# select and bind
$srcHndl = $srcConn -> prepare($selectStmt);
$srcHndl -> execute();
my $the_count;
$srcHndl -> bind_columns(\$nsc, \$the_count);
# do inserts
while($srcHndl -> fetch()){		
	$destHndl -> execute($nsc, $the_count);
}
# commit
$destConn -> commit();

##                          #####    ###
##  #    #   ####      #   #     #  #   #
##  ##   #  #    #     #   #       # #   #
##  # #  #  #          #   ######  #  #  #
##  #  # #  #          #   #     # #   # #
##  #   ##  #    #     #   #     #  #   #
##  #    #   ####      #    #####    ###

# table: prod_count_nci60;
$dropStmt = "drop table if exists prod_count_nci60";
$createStmt = "create table prod_count_nci60(nsc int, the_count int)";
$insertStmt = "insert into prod_count_nci60(nsc, the_count) values (?,?)";
$selectStmt = "select ctl.nsc, count(distinct ctl.expid) the_count 
from c_tline ctl, ops\$oradis.dis_cmpd c 
where c.prefix = 'S' 
and ctl.prefix = c.prefix 
and ctl.nsc = c.nsc 
group by ctl.nsc";

# drop and recreate
$destHndl = $destConn -> prepare($dropStmt);
$destHndl -> execute();
$destHndl = $destConn -> prepare($createStmt);
$destHndl -> execute();
# prep insert statement
$destHndl = $destConn -> prepare($insertStmt);
# select and bind
$srcHndl = $srcConn -> prepare($selectStmt);
$srcHndl -> execute();
# no declarations
$srcHndl -> bind_columns(\$nsc, \$the_count);
# do inserts
while($srcHndl -> fetch()){		
	$destHndl -> execute($nsc, $the_count);
}
# commit
$destConn -> commit();

##  #    #  ######  #    #   ####
##   #  #   #       ##   #  #    #
##    ##    #####   # #  #  #    #
##    ##    #       #  # #  #    #
##   #  #   #       #   ##  #    #
##  #    #  ######  #    #   ####

# table: prod_count_xeno;
$dropStmt = "drop table if exists prod_count_xeno";
$createStmt = "create table prod_count_xeno(nsc int, the_count int)";
$insertStmt = "insert into prod_count_xeno(nsc, the_count) values (?,?)";
$selectStmt = "select ed.nsc, count(*) as the_count 
from invivo.exp_decode ed, ops\$oradis.dis_cmpd c 
where ed.assay_type in (3, 5) 
and c.prefix = 'S' 
and ed.prefix = c.prefix 
and ed.nsc = c.nsc 
group by ed.nsc";

# drop and recreate
$destHndl = $destConn -> prepare($dropStmt);
$destHndl -> execute();
$destHndl = $destConn -> prepare($createStmt);
$destHndl -> execute();
# prep insert statement
$destHndl = $destConn -> prepare($insertStmt);
# select and bind
$srcHndl = $srcConn -> prepare($selectStmt);
$srcHndl -> execute();
# no declarations
$srcHndl -> bind_columns(\$nsc, \$the_count);
# do inserts
while($srcHndl -> fetch()){		
	$destHndl -> execute($nsc, $the_count);
}
# commit
$destConn -> commit();

##  #####   #         ##     #####  ######   ####
##  #    #  #        #  #      #    #       #
##  #    #  #       #    #     #    #####    ####
##  #####   #       ######     #    #            #
##  #       #       #    #     #    #       #    #
##  #       ######  #    #     #    ######   ####

# table: prod_plated_sets;
$dropStmt = "drop table if exists prod_plated_sets";
$createStmt = "create table prod_plated_sets (nsc int, plate_set varchar(1024))";
$insertStmt = "insert into prod_plated_sets(nsc, plate_set) values (?,?)";
$selectStmt = "select distinct nsc, plateset as plate_set 
from ops\$oradis.oradis_dis_well";

# drop and recreate
$destHndl = $destConn -> prepare($dropStmt);
$destHndl -> execute();
$destHndl = $destConn -> prepare($createStmt);
$destHndl -> execute();
# prep insert statement
$destHndl = $destConn -> prepare($insertStmt);
# select and bind
$srcHndl = $srcConn -> prepare($selectStmt);
$srcHndl -> execute();
my $plate_set;
$srcHndl -> bind_columns(\$nsc, \$plate_set);
# do inserts
while($srcHndl -> fetch()){		
	$destHndl -> execute($nsc, $plate_set);
}
# commit
$destConn -> commit();

##  #####   #####    ####        #  ######   ####    #####   ####
##  #    #  #    #  #    #       #  #       #    #     #    #
##  #    #  #    #  #    #       #  #####   #          #     ####
##  #####   #####   #    #       #  #       #          #         #
##  #       #   #   #    #  #    #  #       #    #     #    #    #
##  #       #    #   ####    ####   ######   ####      #     ####

# table: prod_projects;
$dropStmt = "drop table if exists prod_projects";
$createStmt = "create table prod_projects (nsc int, project_code varchar(1024), description varchar(1024))";
$insertStmt = "insert into prod_projects(nsc, project_code, description) values (?,?,?)";
$selectStmt = "select cp.nsc, cp.projectcode as project_code, lu.description 
from ops\$oradis.chem_project cp, ops\$oradis.lookup_project lu 
where cp.projectcode = lu.projectcode";

# drop and recreate
$destHndl = $destConn -> prepare($dropStmt);
$destHndl -> execute();
$destHndl = $destConn -> prepare($createStmt);
$destHndl -> execute();
# prep insert statement
$destHndl = $destConn -> prepare($insertStmt);
# select and bind
$srcHndl = $srcConn -> prepare($selectStmt);
$srcHndl -> execute();
my $project_code, my $description;
$srcHndl -> bind_columns(\$nsc, \$project_code, \$description);
# do inserts
while($srcHndl -> fetch()){		
	$destHndl -> execute($nsc, $project_code, $description);
}
# commit
$destConn -> commit();

## #####      #     ####            ####   #    #  #####   #####
## #    #     #    #               #    #  ##  ##  #    #  #    #
## #    #     #     ####           #       # ## #  #    #  #    #
## #    #     #         #          #       #    #  #####   #    #
## #    #     #    #    #          #    #  #    #  #       #    #
## #####      #     ####  #######   ####   #    #  #       #####

# table: dis_cmpd;
$dropstmt = "drop table if exists prod_cmpd";
$createstmt = "create table prod_cmpd (nsc int, cmpd_id int, cas int, conf varchar(32), distribution_code varchar(32), mf varchar(1024), mw double precision)";
$insertstmt = "insert into prod_cmpd(nsc, cmpd_id, cas, conf, distribution_code, mf, mw) values(?,?,?,?,?,?,?)";
$selectstmt = "select nsc, cmpd_id, cas, conf, distribution_code, mf, mw 
from ops\$oradis.dis_cmpd 
where prefix = 'S' 
and nsc is not null 
and nsc != 0";

# drop and recreate
$destHndl = $destConn -> prepare($dropStmt);
$destHndl -> execute();
$destHndl = $destConn -> prepare($createStmt);
$destHndl -> execute();
# prep insert statement
$destHndl = $destConn -> prepare($insertStmt);
# select and bind
$srcHndl = $srcConn -> prepare($selectStmt);
$srcHndl -> execute();
my $cmpd_id, my $cas, my $conf, my $distribution_code, my $mf, my $mw;
$srcHndl -> bind_columns(\$nsc, \$cmpd_id, \$cas, \$conf, \$distribution_code, \$mf, \$mw);
# do inserts
while($srcHndl -> fetch()){		
	$destHndl -> execute($nsc, $cmpd_id, $cas, $conf, $distribution_code, $mf, $mw);
}
# commit
$destConn -> commit();


## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

my @idxList = ();

push @idxList, qq[drop index if exists prod_mtxt_nsc];
push @idxList, qq[create index prod_mtxt_nsc on prod_mtxt(nsc)];
push @idxList, qq[drop index if exists prod_inventory_nsc];
push @idxList, qq[create index prod_inventory_nsc on prod_inventory(nsc)];
push @idxList, qq[drop index if exists prod_chem_name_cmpd_id];
push @idxList, qq[create index prod_chem_name_cmpd_id on prod_chem_name(cmpd_id)];
push @idxList, qq[drop index if exists prod_chem_name_nsc];
push @idxList, qq[create index prod_chem_name_nsc on prod_chem_name(nsc)];
push @idxList, qq[drop index if exists prod_count_hf_nsc];
push @idxList, qq[create index prod_count_hf_nsc on prod_count_hf(nsc)];
push @idxList, qq[drop index if exists prod_count_nci60_nsc];
push @idxList, qq[create index prod_count_nci60_nsc on prod_count_nci60(nsc)];
push @idxList, qq[drop index if exists prod_count_xeno_nsc];
push @idxList, qq[create index prod_count_xeno_nsc on prod_count_xeno(nsc)];

# finally, build the combined prod_bio_counts table

my @sqlList = ();

push @sqlList, qq[drop table if exists prod_bio_counts];

push @sqlList, qq[create table prod_bio_counts 
as select 
prod_src.nsc, 
prod_count_nci60.the_count as prod_count_nci60, 
prod_count_hf.the_count as prod_count_hf, 
prod_count_xeno.the_count as prod_count_xeno 
from prod_src 
left outer join prod_count_nci60 on prod_src.nsc = prod_count_nci60.nsc 
left outer join prod_count_hf on prod_src.nsc = prod_count_hf.nsc 
left outer join prod_count_xeno on prod_src.nsc = prod_count_xeno.nsc];

# clean up
$srcConn->disconnect();
$destConn->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";

