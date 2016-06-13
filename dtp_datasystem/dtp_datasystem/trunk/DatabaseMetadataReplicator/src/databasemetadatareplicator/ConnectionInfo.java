/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author mwkunkel
 */
public class ConnectionInfo {

    public String connectionName;
    public String dbUrl;
    public String dbUser;
    public String dbPass;

    public ConnectionInfo(String connectionName, String dbUrl, String dbUser, String dbPass) {
        this.connectionName = connectionName;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }

    public void asProperties() {
        System.out.println("#     ---------------" + connectionName + "---------------");
        System.out.println(connectionName + "_dbUrl=" + dbUrl);
        System.out.println(connectionName + "_dbUser=" + dbUser);
        System.out.println(connectionName + "_dbPass=" + dbPass);
    }
}
