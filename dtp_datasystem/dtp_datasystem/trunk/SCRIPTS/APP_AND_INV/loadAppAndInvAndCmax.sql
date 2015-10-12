drop table if exists app_and_inv;

create table app_and_inv(
generic_name varchar,
preferred_name varchar,
originator varchar,
nsc int,
cas varchar,
target varchar,
other_targets varchar,
app_or_inv varchar,
project_code varchar
);

\copy app_and_inv from ./appAndInv.09OCT2015.csv csv header

-- stats

drop table if exists temp;

create table temp
as
select target, regexp_replace(lower(target), '[ -]','') as fixed, count(*) 
from app_and_inv
group by target
order by target;

drop table if exists temp2;

create table temp2
as 
select fixed, array_to_string(array_agg(distinct target), ','), count(distinct target) 
from temp
group by fixed
order by count(distinct target) desc, fixed asc;

drop table if exists temp3;

create table temp3
as
select '--->'||target||'<---'
from app_and_inv
where regexp_replace(lower(target), '[ -]','') in (
	select fixed from temp2
	where "count" > 1
)
order by target;

\copy temp3 to /tmp/app_and_inv_target_formatting.csv csv header

--
--
--
--
--
create table cmax_small_mol(
sort_code varchar,
nsc varchar,
generic_names varchar,
brand_names varchar,
target_moa varchar,
mw varchar,
dose varchar,
dose_unit varchar,
route varchar,
infusion varchar,
dose_schedule varchar,
cmax_um varchar,
cmax_mol_per_liter varchar,
cmax_g_per_liter varchar,
cmax_raw_units varchar,
tmax varchar,
t_half varchar,
auc varchar,
cl varchar,
vd varchar,
protein_binding varchar,
css_ave varchar,
initial_us_approv varchar,
refs varchar,
indications varchar,
notes varchar,
filler1 varchar,
filler2 varchar,
filler3 varchar,
filler4 varchar,
filler5 varchar,
filler6 varchar,
filler7 varchar,
filler8 varchar,
filler9 varchar,
unit_list varchar,
route_list varchar);

create table cmax_biologicals(
sort_code varchar,
nsc varchar,
generic_names varchar,
brand_names varchar,
target_moa varchar,
class varchar,
mw varchar,
dose varchar,
dose_unit varchar,
route varchar,
infusion varchar,
dose_schedule varchar,
cmax_um varchar,
cmax_mol_per_liter varchar,
cmax_g_per_liter varchar,
cmax_raw_units varchar,
tmax varchar,
t_half varchar,
auc varchar,
cl varchar,
vss varchar,
refs varchar,
initial_us_approv varchar,
indications varchar,
notes varchar,
filler1 varchar,
filler2 varchar,
filler3 varchar,
filler4 varchar,
filler5 varchar,
filler6 varchar,
filler7 varchar,
filler8 varchar,
filler9 varchar,
filler10 varchar,
unit_list varchar,
route_list varchar);

\copy cmax_small_mol from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/SCRIPTS/APP_AND_INV/Cmax02.03.15.smallmol.csv csv header
\copy cmax_biologicals from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/SCRIPTS/APP_AND_INV/Cmax02.03.15.biologicals.csv csv header

-- validate expected numeric-only columns

alter table cmax_small_mol alter column sort_code type int using (sort_code::int);
alter table cmax_small_mol alter column nsc type int using (nsc::int);
alter table cmax_small_mol alter column cmax_um type double precision using(cmax_um::double precision);
alter table cmax_small_mol alter column cmax_mol_per_liter type double precision using(cmax_mol_per_liter::double precision);;
alter table cmax_small_mol alter column cmax_g_per_liter type double precision using(cmax_g_per_liter::double precision);;

alter table cmax_biologicals alter column sort_code type int using (sort_code::int);
alter table cmax_biologicals alter column nsc type int using (nsc::int);
alter table cmax_biologicals alter column cmax_um type double precision using(cmax_um::double precision);
alter table cmax_biologicals alter column cmax_mol_per_liter type double precision using(cmax_mol_per_liter::double precision);;
alter table cmax_biologicals alter column cmax_g_per_liter type double precision using(cmax_g_per_liter::double precision);;



















-- for resolving against existing tables



--alter table nsc_ident alter column drug_name drop not null;
--alter table nsc_ident alter column target drop not null;
--
--update nsc_ident
--set drug_name = null, target = null;
--
----generic_name
--
--update nsc_ident
--set drug_name = app_and_inv.generic_name, target = app_and_inv.target
--from app_and_inv
--where nsc_ident.nsc = app_and_inv.nsc;
--
----if no generic then try preferred_name
--
--update nsc_ident
--set drug_name = app_and_inv.preferred_name
--from app_and_inv
--where nsc_ident.drug_name is null
--and nsc_ident.nsc = app_and_inv.nsc;
--
--select count(*) from nsc_ident where drug_name is null;
--