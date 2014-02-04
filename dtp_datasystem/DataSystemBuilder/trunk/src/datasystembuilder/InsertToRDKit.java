/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasystembuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Process good .mol fragments into rdkit resolve against known salts
 *
 * @author mwkunkel
 */
public class InsertToRDKit {

  public static int BATCH_FETCH_SIZE = 1000;

  /**
   *
   * @param postgresConn
   * @param postgresInsertConn
   * @throws Exception
   */
  public static void processFragmentsToRdkit(final Connection postgresConn, Connection postgresInsertConn) throws Exception {

    Statement postgresStmt = null;

    //PreparedStatement postgresPrepStmt = null;
    //ResultSet rs = null;
    try {

      postgresStmt = postgresConn.createStatement();

      //rdkit_mol_frag
      String[] sqlArray = new String[]{
        /*
         * 
    --MWK 24Dec2013

drop index if exists mol_frag_frag_id;
create index mol_frag_frag_id on mol_frag(frag_id);

drop table if exists rdkit_mol_frag_validity;
create table rdkit_mol_frag_validity as select frag_id, nsc, strc_id, is_valid_ctab(ctab::cstring) from mol_frag;

-- nsc 93391 with strc_id 489218 is problematic in that it passes is_valid_ctab, but fails mol_from_ctab
update rdkit_mol_frag_validity set is_valid_ctab = 'f' where nsc = 93391;
create index rdkit_mol_frag_validity_frag_id on rdkit_mol_frag_validity(frag_id);

-- failed fragments, for forensics
drop table if exists rdkit_mol_frag_fail;
create table rdkit_mol_frag_fail as select mf.frag_id, mf.prefix, mf.nsc, mf.strc_id, mol_from_ctab(ctab::cstring), mf, ctab from mol_frag mf, rdkit_mol_frag_validity v where mf.frag_id = v.frag_id and v.is_valid_ctab = 'f';

-- process valid fragments        
drop table if exists rdkit_mol_frag;
create table rdkit_mol_frag as select mf.frag_id, mf.prefix, mf.nsc, mf.strc_id, mol_from_ctab(ctab::cstring) as frag_mol, mf, ctab from mol_frag mf, rdkit_mol_frag_validity v where mf.frag_id = v.frag_id and v.is_valid_ctab = 't';
alter table rdkit_mol_frag add smiles varchar(2048);
update rdkit_mol_frag set smiles = mol_to_smiles(frag_mol);

-- instead of updating rdkit_mol_frag, create temp
-- to do rdkit property calcs for frag
-- and fetch InChI from mol_frag

drop table if exists temp;

create table temp as select rdkit_mol_frag.*,
mol_amw(frag_mol) as mw,
mol_hba(frag_mol) as hba,
mol_hbd(frag_mol) as hbd,
mol_numheavyatoms(frag_mol) as count_heavy,
mol_numrings(frag_mol) as count_rings,
mol_tpsa(frag_mol) as tpsa,
mol_frag.inchi
from rdkit_mol_frag, mol_frag
where rdkit_mol_frag.frag_id = mol_frag.frag_id;

alter table rdkit_mol_frag add inchi text;
update rdkit_mol_frag
set inchi = mol_frag.inchi 
from mol_frag
where rdkit_mol_frag.frag_id = mol_frag.frag_id;



--
-- --alter table rdkit_mol_frag rename to rdkit_backup;
-- --create table rdkit_mol_frag as select * from temp;     
--

-- index the structures
create index rdkit_mol_frag_mol_idx on rdkit_mol_frag using gist(frag_mol);
create index rdkit_mol_frag_frag_id_idx on rdkit_mol_frag(frag_id);
create index rdkit_mol_frag_strc_id_idx on rdkit_mol_frag(strc_id);
create index rdkit_mol_frag_smiles on rdkit_mol_frag(smiles);

-- do the salts
alter table rdkit_mol_frag add known_salt boolean;
alter table rdkit_mol_frag add known_salt_name varchar(1024);
update rdkit_mol_frag set known_salt = 't', known_salt_name = salt_name from known_salt where rdkit_mol_frag.smiles = known_salt.smiles;

-- create holding areas for single fragment and two-fragment where one is a salt

drop table if exists strc_id_one_frag;
create table strc_id_one_frag as select strc_id from rdkit_mol_frag rdk group by strc_id having count(*) = 1;
create index siof_strc_id on strc_id_one_frag(strc_id);
--
drop table if exists strc_id_one_frag_not_salt;
create table strc_id_one_frag_not_salt as select rdk.strc_id from rdkit_mol_frag rdk, strc_id_one_frag siof where rdk.strc_id = siof.strc_id and rdk.known_salt is null;
create index siofns_strc_id on strc_id_one_frag_not_salt(strc_id);
--
drop table if exists one_frag;
create table one_frag as select * from rdkit_mol_frag rdk, strc_id_one_frag_not_salt siofns where rdk.strc_id = siofns.strc_id;
--
select count(*) from strc_id_one_frag;
select count(*) from strc_id_one_frag_not_salt;
select count(*) from one_frag;

-- two fragment molecules

drop table if exists strc_id_two_frag;
create table strc_id_two_frag as select strc_id from rdkit_mol_frag rdk group by strc_id having count(*) = 2;
create index sitf_strc_id on strc_id_two_frag(strc_id);
--
drop table if exists two_frag;
create table two_frag as select rdk.* from rdkit_mol_frag rdk, strc_id_two_frag sitf where rdk.strc_id = sitf.strc_id;
create index tf_strc_id on two_frag(strc_id);
--
drop table if exists strc_id_two_frag_one_salt;
create table strc_id_two_frag_one_salt as select strc_id from two_frag group by strc_id having count(known_salt) = 1;
create index sitfos_strc_id on strc_id_two_frag_one_salt(strc_id);
--
drop table if exists two_frag_one_salt;
create table two_frag_one_salt as select rdk.* from rdkit_mol_frag rdk, strc_id_two_frag_one_salt sitfos where rdk.strc_id = sitfos.strc_id;
create index tfos_strc_id on two_frag_one_salt(strc_id);
--
select count(*) from strc_id_two_frag;
select count(*) from two_frag;
select count(distinct strc_id) from two_frag;
select count(*) from strc_id_two_frag_one_salt;
select count(*) from two_frag_one_salt;
select count(distinct strc_id) from two_frag_one_salt;

--combine the one- and two-fragment parents into a single table

-- MWK 24Dec2013 bring in InChI from CDK

drop table if exists parent_with_salt;
create table parent_with_salt 
as 
select one_frag.*, 'free base' as matched_salt 
from one_frag 
union 
select tfos.*, tfos_salt.known_salt_name as matched_salt
from two_frag_one_salt tfos, two_frag_one_salt tfos_salt
where tfos.strc_id = tfos_salt.strc_id
and tfos.known_salt is null
and tfos_salt.known_salt = 't';

--INDEX IT!
create index parent_with_salt_frag_mol_idx on parent_with_salt using gist(frag_mol);
--
select count(distinct strc_id) from one_frag;
select count(distinct strc_id) from two_frag_one_salt;
select count(distinct strc_id) from parent_with_salt;
--
drop table if exists related;
create table related (relation varchar(32), ref_nsc int, rel_nsc int);

drop table if exist temp;
create table temp as select smiles, matched_salt, count(*) 
from parent_with_salt
group by smiles, matched_salt 
having count(*) > 1;

--TESTOSTERONE
select frag_mol from rdkit_mol_frag where nsc = 755838;

select nsc, matched_salt, smiles from rdkit_mol_frag where frag_mol @= mol_from_smiles('C[C@]12CCC3C(CCC4=CC(=O)CC[C@@]43C)C1CC[C@H]2O');
   nsc   |                            smiles                            
---------+--------------------------------------------------------------
   26499 | C[C@]12CC[C@H]3[C@@H](CCC4=CC(=O)CC[C@@]43C)[C@@H]1CC[C@H]2O
    9700 | CC12CCC3C(CCC4=CC(=O)CCC43C)C1CCC2O
   50917 | C[C@@]12CC[C@@H]3[C@H](CCC4=CC(=O)CC[C@]43C)[C@H]1CC[C@H]2O
  755838 | C[C@]12CCC3C(CCC4=CC(=O)CC[C@@]43C)C1CC[C@H]2O
 2509410 | CC12CCC3C(CCC4=CC(=O)CCC43C)C1CCC2O
  523833 | CC12CCC3C(CCC4=CC(=O)CCC43C)C1CCC2O
  764937 | C[C@]12CC[C@H]3[C@H](CCC4=CC(=O)CC[C@@]43C)[C@@H]1CC[C@@H]2O
(7 rows)

26499
755838
9700
2509410
50917
523833

Time: 163.674 ms

select nsc, inchi from mol_frag where nsc in (26499, 755838, 9700, 2509410, 50917, 523833);





         */
        
      };

      for (int i = 0; i < sqlArray.length; i++) {
        String sqlString = sqlArray[i];
        System.out.println(sqlString);
        System.out.println();
        postgresStmt.execute(sqlString);
        postgresConn.commit();
      }

      postgresStmt.close();
      postgresStmt = null;

    } catch (Exception e) {
      System.out.println("Caught Exception " + e);
      e.printStackTrace();
      throw new Exception("Exception:" + e);
    } finally {
      if (postgresStmt != null) {
        try {
          postgresStmt.close();
          postgresStmt = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing postgresStmt");
        }
      }
//      if (postgresPrepStmt != null) {
//        try {
//          postgresPrepStmt.close();
//          postgresPrepStmt = null;
//        } catch (SQLException ex) {
//          System.out.println("Error in closing updateStmt");
//        }
//      }
//      if (rs != null) {
//        try {
//          rs.close();
//          rs = null;
//        } catch (SQLException ex) {
//          System.out.println("Error in closing postgresStmt");
//        }
//      }
    }
  }

  /*
   * No longer needed.  RDKit postgres now seems to handle stereo 
   */
  public static void processFragmentsStripStereo(final Connection postgresConn, Connection postgresInsertConn) throws Exception {

    Statement postgresStmt = null;
    PreparedStatement postgresPrepStmt = null;
    ResultSet rs = null;

    try {

      postgresStmt = postgresConn.createStatement();

      String[] sqlArray = new String[]{
        "drop table if exists rdkit_mol_frag_strip_stereo",
        "create table rdkit_mol_frag_strip_stereo(frag_id int, smiles varchar(4096), smiles_strip_stereo varchar(4096))"
      };

      for (int i = 0; i < sqlArray.length; i++) {
        String sqlString = sqlArray[i];
        System.out.println(sqlString);
        postgresStmt.execute(sqlString);
        postgresConn.commit();
      }

      String insertString = "insert into rdkit_mol_frag_strip_stereo(frag_id, smiles, smiles_strip_stereo) values (?,?,?)";
      postgresPrepStmt = postgresInsertConn.prepareStatement(insertString);

      // go through the mol_smiles, remove stereo, create new frag and then output the canonical form of that
      String sqlString = "select id, frag_mol as smiles from rdkit_mol_frag";
      rs = postgresStmt.executeQuery(sqlString);

      rs.setFetchDirection(ResultSet.FETCH_FORWARD);
      rs.setFetchSize(BATCH_FETCH_SIZE);

      int id;
      String smiles;
      String smilesNoStereo;
      int fragCnt = 0;

      while (rs.next()) {
        fragCnt++;
        id = rs.getInt("id");
        smiles = rs.getString("smiles");
        //strip stereo
        smilesNoStereo = smiles.replaceAll("@", "").replaceAll("/", "-").replaceAll("\\\\", "-");
        //only process if stripping made a difference
        //if (!smilesNoStereo.equals(smiles)) {

        postgresPrepStmt.setInt(1, id);
        postgresPrepStmt.setString(2, smiles);
        postgresPrepStmt.setString(3, smilesNoStereo);

        postgresPrepStmt.addBatch();
        if (fragCnt > BATCH_FETCH_SIZE) {
          postgresPrepStmt.executeBatch();
          postgresInsertConn.commit();
          fragCnt = 0;
        }
        //}
      }
//stragglers
      postgresPrepStmt.executeBatch();
      postgresInsertConn.commit();

      // check the smiles_strip_stereo for validity, join back to the original fragments
      sqlArray = new String[]{
        // determine invalid smiles_strip_stereo
        "drop table if exists no_smiles_valid_by_rdkit",
        "create table no_smiles_valid_by_rdkit as select id, smiles_strip_stereo, is_valid_smiles(smiles_strip_stereo::cstring) as valid from rdkit_mol_frag_strip_stereo",
        // get rid of the invalid ones
        "delete from rdkit_mol_frag_strip_stereo where id in (select id from no_smiles_valid_by_rdkit where valid = 'f')",
        // join the rdkit_mol and rdkit_mol_no_stereo into a single table
        "drop table if exists rdkit_frag",
        "create table rdkit_frag as select r.id, r.prefix, r.nsc, r.strc_id, r.mf, r.frag_mol, mol_from_smiles(noster.smiles_strip_stereo::cstring) as frag_mol_no_stereo, r.ctab from rdkit_mol_frag r left outer join rdkit_mol_frag_strip_stereo noster on r.id = noster.id",
        // create indexes
        "create index rdkit_frag_mol_idx on rdkit_frag using gist(frag_mol)",
        "create index rdkit_frag_mol_no_stereo_idx on rdkit_frag using gist(frag_mol_no_stereo)",
        "create index rdkit_frag_id_idx on rdkit_frag(frag_id)",
        "create index rdkit_frag_strc_id_idx on rdkit_frag(strc_id)"
      };

      for (int i = 0; i < sqlArray.length; i++) {
        sqlString = sqlArray[i];
        System.out.println(sqlString);
        postgresStmt.execute(sqlString);
        postgresConn.commit();
      }

      postgresStmt.close();
      postgresStmt = null;

      postgresPrepStmt.close();
      postgresPrepStmt = null;

      rs.close();
      rs = null;

      /* TESTOSTERONE TESTS
       select frag_mol, nsc from rdkit_frag where frag_mol = (select frag_mol from rdkit_frag where nsc = 755838);
       select frag_mol_no_stereo, nsc from rdkit_frag where frag_mol_no_stereo = (select frag_mol_no_stereo from rdkit_frag where nsc = 755838);
       */
    } catch (Exception e) {
      System.out.println("Caught Exception " + e);
      e.printStackTrace();
      throw new Exception("Exception:" + e);
    } finally {
      if (postgresStmt != null) {
        try {
          postgresStmt.close();
          postgresStmt = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing postgresStmt");
        }
      }
      if (postgresPrepStmt != null) {
        try {
          postgresPrepStmt.close();
          postgresPrepStmt = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing updateStmt");
        }
      }
      if (rs != null) {
        try {
          rs.close();
          rs = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing postgresStmt");
        }
      }
    }
  }
}
