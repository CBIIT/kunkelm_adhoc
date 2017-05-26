/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.util.ArrayList;

/**
 *
 * @author mwkunkel
 */
public class DdlInfo {

    public ArrayList<String> createTableStatements;
    public ArrayList<String> dropTableStatements;

    public ArrayList<String> createIndexStatements;
    public ArrayList<String> dropIndexStatements;

    public ArrayList<String> createConstraintStatements;
    public ArrayList<String> dropConstraintStatements;

    public ArrayList<String> createSequenceStatements;
    public ArrayList<String> dropSequenceStatements;

    public ArrayList<String> remnantStatements;

    public DdlInfo() {
        createTableStatements = new ArrayList<String>();
        dropTableStatements = new ArrayList<String>();

        createIndexStatements = new ArrayList<String>();
        dropIndexStatements = new ArrayList<String>();

        createConstraintStatements = new ArrayList<String>();
        dropConstraintStatements = new ArrayList<String>();

        createSequenceStatements = new ArrayList<String>();
        dropSequenceStatements = new ArrayList<String>();

        remnantStatements = new ArrayList<String>();
    }

    public void printBean(String beanName, String what) {

        if (what.equals("createTableStatements")) {
            System.out.println(beanName + " " + what);
            System.out.println("createTableStatements");
            for (String s : createTableStatements) {
                System.out.println(s);
            }
        }

        if (what.equals("dropTableStatements")) {
            System.out.println(beanName + " " + what);
            System.out.println("dropTableStatements");
            for (String s : dropTableStatements) {
                System.out.println(s);
            }
        }

        if (what.equals("createIndexStatements")) {
            System.out.println(beanName + " " + what);
            System.out.println("createIndexStatements");
            for (String s : createIndexStatements) {
                System.out.println(s);
            }
        }

        if (what.equals("dropIndexStatements")) {
            System.out.println(beanName + " " + what);
            System.out.println("dropIndexStatements");
            for (String s : dropIndexStatements) {
                System.out.println(s);
            }
        }

        if (what.equals("createConstraintStatements")) {
            System.out.println(beanName + " " + what);
            System.out.println("createConstraintStatements");
            for (String s : createConstraintStatements) {
                System.out.println(s);
            }
        }

        if (what.equals("dropConstraintStatements")) {
            System.out.println(beanName + " " + what);
            System.out.println("dropConstraintStatements");
            for (String s : dropConstraintStatements) {
                System.out.println(s);
            }
        }

        if (what.equals("createSequenceStatements")) {
            System.out.println(beanName + " " + what);
            System.out.println("createSequenceStatements");
            for (String s : createSequenceStatements) {
                System.out.println(s);
            }
        }

        if (what.equals("dropSequenceStatements")) {
            System.out.println(beanName + " " + what);
            System.out.println("dropSequenceStatements");
            for (String s : dropSequenceStatements) {
                System.out.println(s);
            }
        }

        if (what.equals("")) {
            System.out.println(beanName + " " + what);
            System.out.println("remnantStatements");
            for (String s : remnantStatements) {
                System.out.println(s);
            }
        }
    }

}
/*

compare tables

compare sequences

dataSystem tables

dataSystem sequences

*/
