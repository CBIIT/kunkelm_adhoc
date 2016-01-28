drop table if exists temp_agg;

create table temp_agg 
as
select 
nsc, 
array_to_string(array_agg(molecular_formula), '.') as molecular_formula, 
sum(molecular_weight) as formula_weight, 
sum(formalcharge) as formal_charge,
array_to_string(array_agg(formalcharge), ',') as formal_charges
from comb_cat
where category like '%two_strc_one_salt'
and molecular_formula is not null  -- <<<<<<<<<<<<<<<<<<< this filters out labels
group by nsc;

create index temp_agg_nsc on temp_agg(nsc);

drop table if exists temp;

create table temp 
as
select 

ta.nsc, 
ta.molecular_formula, 
ta.formula_weight, 
rs3.mf as prod_mf, 
rs3.mw as prod_fw, 
ta.formula_weight - rs3.mw as diff_mw, 
rs3.formalcharge as formal_charge,
ta.formal_charges

from temp_agg ta 
  left outer join rs3_from_plp_nsc rs3 on ta.nsc = rs3.nsc
  left outer join rs3_from_plp_nsc frags on ta.nsc = frags.nsc;

--       _ _                   _   _           _                            
--  __ _| | |   __ _ _ __   __| | | |__   __ _| | ___   __ _  ___ _ __  ___ 
-- / _` | | |  / _` | '_ \ / _` | | '_ \ / _` | |/ _ \ / _` |/ _ \ '_ \/ __|
--| (_| | | | | (_| | | | | (_| | | | | | (_| | | (_) | (_| |  __/ | | \__ \
-- \__,_|_|_|  \__,_|_| |_|\__,_| |_| |_|\__,_|_|\___/ \__, |\___|_| |_|___/
--                                                     |___/                

select 

all_salts.formal_charge, 
--all_salts.formal_charges,
--all_salts.diff_mw,
all_salts."count" as all_salts_count, 
halogen_salts."count" as halogen_salts_count,
na_k_salts."count" as na_k_salts_count

from

(select 
formal_charge, 
--formal_charges, 
--diff_mw, 
count(*)
from temp
where diff_mw between -0.06 and 0.06
group by 
formal_charge 
--formal_charges, 
--diff_mw
order by 
formal_charge 
--formal_charges, 
--diff_mw
) as all_salts

left outer join

(select 
formal_charge, 
--formal_charges, 
--diff_mw, 
count(*)
from temp
where diff_mw between -0.06 and 0.06
and prod_mf ~ '(\.HCl|\.ClH|\.HBr|\.BrH|\.HI|\.IH|HCl\.|ClH\.|HBr\.|BrH\.|HI\.|IH)'
group by 
formal_charge 
--formal_charges, 
--diff_mw
order by 
formal_charge 
--formal_charges, 
--diff_mw
) as halogen_salts

on all_salts.formal_charge = halogen_salts.formal_charge
--and all_salts.formal_charges = halogen_salts.formal_charges
--and all_salts.diff_mw = halogen_salts.diff_mw

left outer join

(select 
formal_charge, 
--formal_charges, 
--diff_mw, 
count(*)
from temp
where diff_mw between -0.06 and 0.06
and prod_mf ~ '(\.Na|\.K|Na\.|K\.)'
group by 
formal_charge
--formal_charges, 
--diff_mw
order by 
formal_charge
--formal_charges, 
--diff_mw
) as na_k_salts

on all_salts.formal_charge = na_k_salts.formal_charge;
--and all_salts.formal_charges = na_k_salts.formal_charges
--and all_salts.diff_mw = na_k_salts.diff_mw;

diff_mw 0.8

 formal_charge | all_salts_count | halogen_salts_count | na_k_salts_count 
---------------+-----------------+---------------------+------------------
            -1 |            3955 |                3874 |                 
             0 |           17967 |                 171 |               27
             1 |            4861 |                     |             4606
             2 |             337 |                     |               29
             3 |              70 |                   1 |               19
             4 |              24 |                     |                4
             5 |              14 |                     |                 
             6 |              11 |                     |                1
             7 |               3 |                     |                 
(9 rows)

diff_mw 1.0

 formal_charge | all_salts_count | halogen_salts_count | na_k_salts_count 
---------------+-----------------+---------------------+------------------
            -1 |            9035 |                8033 |                 
             0 |           17978 |                 172 |               28
             1 |            4914 |                     |             4630
             2 |             353 |                     |               43
             3 |              85 |                   1 |               20
             4 |              25 |                     |                4
             5 |              15 |                     |                 
             6 |              11 |                     |                1
             7 |               3 |                     |                 
(9 rows)

diff_mw 1.2

 formal_charge | all_salts_count | halogen_salts_count | na_k_salts_count 
---------------+-----------------+---------------------+------------------
            -1 |           22548 |               20201 |                 
             0 |           17986 |                 172 |               28
             1 |            5011 |                   1 |             4691
             2 |             361 |                     |               46
             3 |              96 |                   1 |               20
             4 |              25 |                     |                4
             5 |              15 |                     |                 
             6 |              11 |                     |                1
             7 |               3 |                     |                 
(9 rows)

diff_mw 1.5

 formal_charge | all_salts_count | halogen_salts_count | na_k_salts_count 
---------------+-----------------+---------------------+------------------
            -1 |           34996 |               32545 |                 
             0 |           17991 |                 173 |               28
             1 |            5082 |                   1 |             4723
             2 |             373 |                     |               55
             3 |             109 |                   1 |               20
             4 |              26 |                     |                4
             5 |              16 |                     |                 
             6 |              11 |                     |                1
             7 |               3 |                     |                 
(9 rows)

diff_mw 2.0

 formal_charge | all_salts_count | halogen_salts_count | na_k_salts_count 
---------------+-----------------+---------------------+------------------
            -2 |               1 |                     |                 
            -1 |           35078 |               32622 |                 
             0 |           18023 |                 184 |               31
             1 |            5100 |                   1 |             4736
             2 |             388 |                     |               55
             3 |             113 |                   1 |               22
             4 |              48 |                     |                4
             5 |              16 |                     |                 
             6 |              11 |                     |                1
             7 |               3 |                     |                 
(10 rows)

                        
--  __ _| | |   __ _ _ __   __| | | |__   __ _| | ___   __ _  ___ _ __  ___ 
-- / _` | | |  / _` | '_ \ / _` | | '_ \ / _` | |/ _ \ / _` |/ _ \ '_ \/ __|
--| (_| | | | | (_| | | | | (_| | | | | | (_| | | (_) | (_| |  __/ | | \__ \
-- \__,_|_|_|  \__,_|_| |_|\__,_| |_| |_|\__,_|_|\___/ \__, |\___|_| |_|___/
--  

--   __                                                _    _        _     _      
--  / _|_ __ ___  _ __ ___     ___ _ __ ___  _ __   __| |  | |_ __ _| |__ | | ___ 
-- | |_| '__/ _ \| '_ ` _ \   / __| '_ ` _ \| '_ \ / _` |  | __/ _` | '_ \| |/ _ \
-- |  _| | | (_) | | | | | | | (__| | | | | | |_) | (_| |  | || (_| | |_) | |  __/
-- |_| |_|  \___/|_| |_| |_|  \___|_| |_| |_| .__/ \__,_|___\__\__,_|_.__/|_|\___|
--                                          |_|        |_____|                    
--                  _ _                _               _                  
--  ___  __ _ _ __ (_) |_ _   _    ___| |__   ___  ___| | __   ___  _ __  
-- / __|/ _` | '_ \| | __| | | |  / __| '_ \ / _ \/ __| |/ /  / _ \| '_ \ 
-- \__ \ (_| | | | | | |_| |_| | | (__| | | |  __/ (__|   <  | (_) | | | |
-- |___/\__,_|_| |_|_|\__|\__, |  \___|_| |_|\___|\___|_|\_\  \___/|_| |_|
--                        |___/                                           
--        _                    
--   __ _| |__   _____   _____ 
--  / _` | '_ \ / _ \ \ / / _ \
-- | (_| | |_) | (_) \ V /  __/
--  \__,_|_.__/ \___/ \_/ \___|
--                                                   

select 
all_salts.cmpd_formal_charge, 
all_salts."count" as all_salts_count, 
halogen_salts."count" as halogen_salts_count
from
(
select cmpd_formal_charge, count(*)
from cmpd_table
where salt_smiles is not null
and prod_formula_weight - formula_weight between -0.06 and 0.06
group by cmpd_formal_charge
order by cmpd_formal_charge
)
as all_salts,
(
select cmpd_formal_charge, count(*)
from cmpd_table
where salt_smiles is not null
and prod_formula_weight - formula_weight between -0.06 and 0.06
and prod_molecular_formula ~ '(\.HCl|\.ClH|\.HBr|\.BrH|\.HI|\.IH|HCl\.|ClH\.|HBr\.|BrH\.|HI\.|IH)'
group by cmpd_formal_charge
order by cmpd_formal_charge
) as halogen_salts
where all_salts.cmpd_formal_charge = halogen_salts.cmpd_formal_charge;

  cmpd_formal_charge | all_salts_count | halogen_salts_count 
--------------------+-----------------+---------------------
                 -1 |           22547 |               20196
                  0 |           17986 |                 172
                  1 |            5007 |                   1
                  3 |             101 |                   1

