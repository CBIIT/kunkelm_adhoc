/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mwkunkel
 */
public class GrantBean {

    public List<String> selectTable;
    public List<String> selectInsertTable;
    public List<String> selectInsertUpdateTable;
    public List<String> forbiddenTable;

    public List<String> selectSequence;
    public List<String> selectUseSequence;
    public List<String> selectUseUpdateSequence;
    public List<String> forbiddenSequence;

    public GrantBean() {
        selectTable = new ArrayList<String>();
        selectInsertTable = new ArrayList<String>();
        selectInsertUpdateTable = new ArrayList<String>();
        forbiddenTable = new ArrayList<String>();

        selectSequence = new ArrayList<String>();
        selectUseSequence = new ArrayList<String>();
        selectUseUpdateSequence = new ArrayList<String>();
        forbiddenSequence = new ArrayList<String>();
    }

}
