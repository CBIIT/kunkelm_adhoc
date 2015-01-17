makeSaltList.PL

#!/usr/bin/perl

## delete from cmpd_list_member where cmpd_list_fk in (select id from cmpd_list where list_name like 'SOLVENT LIST%');
## delete from cmpd_list where list_name like 'SOLVENT LIST%';

use strict;
use warnings;

my $solvents = 'false';
my $saltLabel = 'SALT LIST';

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});


my $tempSql;

#    #  #    #  #    #  ######
##   #  #    #  #   #   #
# #  #  #    #  ####    #####
#  # #  #    #  #  #    #
#   ##  #    #  #   #   #
#    #   ####   #    #  ######

$tempSql = qq[delete from cmpd_list_member where cmpd_list_fk in (select id from cmpd_list where list_name like '$saltLabel%')];
my $sth = $dbh->prepare($tempSql);
#print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;   

$tempSql = qq[delete from cmpd_list where list_name like 'saltLabel%'];
my $sth = $dbh->prepare($tempSql);
#print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;   

if ($solvents eq 'true'){
   
    # get the distinct salt_smiles FOR SOLVENTS ONLY
   
    $tempSql = qq[
    select distinct salt_smiles
    from two_strc_any_lbl_one_salt, unique_salts
    where salt_smiles is not null
    and salt_smiles = unique_salts.can_taut_strip_stereo
    and unique_salts.sources = 'Schrodinger solvent'
    limit 10
    ];
   
} else {
   
    # get the distinct salt_smiles FOR NON-SOLVENTS ONLY
   
    $tempSql = qq[
    select distinct salt_smiles
    from two_strc_any_lbl_one_salt, unique_salts
    where salt_smiles is not null
    and salt_smiles = unique_salts.can_taut_strip_stereo
    and unique_salts.sources not like '%Schrodinger solvent%'
    limit 10
    ];
   
}

my $sth = $dbh->prepare($tempSql);
#print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;   

my @saltSmilesList;   
while(my $ref = $sth->fetchrow_hashref()) {       
    push (@saltSmilesList, $ref->{'salt_smiles'});
}

# cycle through them

foreach my $saltSmiles(@saltSmilesList){
   
    print "salt_smiles: $saltSmiles";
   
    # nsc for saltSmiles
    # use table temp
   
    $tempSql = qq[
    drop table if exists temp
    ];
    my $sth = $dbh->prepare($tempSql);
    #print "$tempSql\n\n";
    $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;   
   
    $tempSql = qq[
    create table temp as
    select distinct nsc
    from two_strc_any_lbl_one_salt
    where salt_smiles = '$saltSmiles'
    ];
    my $sth = $dbh->prepare($tempSql);
    #print "$tempSql\n\n";
    $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;   
   
    # create cmpd_list
   
    # get the id
   
    my $cmpdListId;
   
    $tempSql = qq[select nextval('cmpd_list_seq')];
    my $sth = $dbh->prepare($tempSql);
    #print "$tempSql\n\n";
    $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;   
    while (my @row = $sth->fetchrow_array) {
        $cmpdListId = $row[0];
        print "cmpdListId: $cmpdListId\n";
    }
   
    $tempSql = qq[
    insert into cmpd_list
    (
    id,
    list_name,
    date_created,
    list_owner,
    share_with,
    cmpd_list_id,
    count_list_members,
    anchor_smiles,
    list_comment,
    anchor_comment
    )
   
    select
   
    $cmpdListId,
    '$saltLabel '|| unique_salts.names,
    now(),
    'kunkelm',
    'kunkelm',
    round(random() * 9223372036854775807),
    count(temp.*),
    '$saltSmiles',
    unique_salts.names,
    '$saltLabel $saltSmiles'
    from temp, unique_salts
    where '$saltSmiles' = can_taut_strip_stereo
    group by names
    ];
    my $sth = $dbh->prepare($tempSql);
    print "$tempSql\n\n";
    $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;   
   
    # now the cmpd_list_members
   
    $tempSql = qq[
    insert into cmpd_list_member
    (   
    id,
    list_member_comment,
    cmpd_fk,
    cmpd_list_fk
    )
   
    select
   
    nextval('cmpd_list_member_seq'),
    null,
    nsc,
    $cmpdListId
    from temp
    ];
    my $sth = $dbh->prepare($tempSql);
    #print "$tempSql\n\n";
    $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;   
   
}

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";