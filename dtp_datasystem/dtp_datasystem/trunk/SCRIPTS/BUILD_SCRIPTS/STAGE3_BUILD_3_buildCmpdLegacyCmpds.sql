-- FIRST have to run Main.LoadKekuleRenderedImages.java

-- update any existing entries 
-- these had GIF from the old Kekule system

update cmpd_legacy_cmpd clc

  set id = clc.nsc,
      prod_molecular_formula = mf,
      prod_formula_weight = mw

from rs3_from_plp_nsc rs3
  where clc.nsc = rs3.nsc;

-- back fill any nsc that did NOT have GIF from Kekule?

drop table if exists temp;

create table temp
as
select nsc
from nsc_cmpd 
where nsc not in (
  select nsc from cmpd_legacy_cmpd
);

insert into cmpd_legacy_cmpd(
id,
nsc,
prod_molecular_formula,
prod_molecular_weight
)
select
nsc,
nsc,
mf,
mw
from rs3_from_plp_nsc;







insert into cmpd_legacy_cmpd(
id,
nsc,
prod_molecular_formula,
prod_molecular_weight
)
select
nsc,
nsc,
mf,
mw
from rs3_from_plp_nsc;


