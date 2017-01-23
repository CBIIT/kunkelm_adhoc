drop table if exists cmax_small_mol;

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

drop table if exists cmax_biologicals;

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

\copy cmax_small_mol from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/SCRIPTS/CMAX/cmax.04july2015.smallmol.csv csv header
\copy cmax_biologicals from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/SCRIPTS/CMAX/cmax.04july2015.biologicals.csv csv header

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

drop table if exists app_and_inv_with_cmax;

create table app_and_inv_with_cmax
as
select a.nsc, a.primary_target, sm.target_moa as small_mol_target_moa, b.target_moa as biologicals_target_moa
from app_and_inv a 
    left join cmax_small_mol sm on a.nsc = sm.nsc
    left join cmax_biologicals b on a.nsc = b.nsc
order by a.nsc;

\copy app_and_inv_with_cmax to /tmp/app_and_inv_with_cmax.csv csv header
