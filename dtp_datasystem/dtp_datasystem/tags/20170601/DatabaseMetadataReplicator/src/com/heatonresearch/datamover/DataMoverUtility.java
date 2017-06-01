package com.heatonresearch.datamover;

import java.sql.*;
import java.io.*;

import com.heatonresearch.datamover.db.Database;
import com.heatonresearch.datamover.db.DatabaseException;
import com.heatonresearch.datamover.db.MySQL;

/**
 * A utility to copy data from two databases, as specified by
 * a configuration file. The configuration file is of the 
 * format:
 * 
 * sourceDriver=com.mysql.jdbc.Driver
 * sourceURL=jdbc:mysql://127.0.0.1/test?user=root
 * targetDriver=com.mysql.jdbc.Driver
 * targetURL=jdbc:mysql://127.0.0.1/test2?user=root
 * 
 * @author Jeff Heaton (http://www.heatonresearch.com)
 *
 */
public class DataMoverUtility
{
  private String sourceDriver = "";
  private String sourceURL = "";
  private String targetDriver = "";
  private String targetURL = "";  
  
  public void loadFile(String str)
  {
    try
    {
      FileReader f = new FileReader( new File(str) );
      BufferedReader r = new BufferedReader(f);
      
      String line = "";
      while( (line=r.readLine())!=null)
      {
        int i = line.indexOf('=');
        if( i==-1)
          continue;
        String cmd = line.substring(0,i).trim();
        String arg = line.substring(i+1).trim();
        if( cmd.equalsIgnoreCase("sourceDriver"))
        {
          sourceDriver = arg;
        }
        else if( cmd.equalsIgnoreCase("sourceURL"))
        {
          sourceURL = arg;
        }
        else if( cmd.equalsIgnoreCase("targetDriver"))
        {
          targetDriver = arg;
        }
        else if( cmd.equalsIgnoreCase("targetURL"))
        {
          targetURL = arg;
        }        
      }
      
    } catch (IOException e)
    {
      System.out.println("Can't load: " + str);
    }
    
  }
  
  public void run()
  {
    try
    { 
      DataMover mover = new DataMover();
      
      Database source = new MySQL();
      source.connect(sourceDriver,sourceURL);
      
      Database target = new MySQL();
      target.connect(targetDriver,targetURL);
      
      mover.setSource(source);
      mover.setTarget(target);
      mover.exportDatabse();
      
      source.close();
      target.close();
      
    } catch (DatabaseException e)
    {
      e.printStackTrace();
    }
  }
  
  public static void main(String args[]) throws SQLException
  {
    
    if( args.length<1)
    {
      System.out.println("Usage:\n\njava DataMoverUtility <config file>");
    }
    else
    {
     DataMoverUtility utility = new DataMoverUtility();
     utility.loadFile(args[0]);
     utility.run();
    }
    
  }
}
