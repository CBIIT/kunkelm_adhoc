/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author mwkunkel
 */
public class SerializeDeSerialize<T> {

  private String fileName;

  public SerializeDeSerialize(String fileName) {
    this.fileName = fileName;
  }
  
  public void serialize(T t) {

    FileOutputStream fos = null;
    ObjectOutputStream oos = null;

    try {
      fos = new FileOutputStream(this.fileName);
      oos = new ObjectOutputStream(fos);
      oos.writeObject(t);
      oos.close();
      oos = null;
      fos.close();
      fos = null;
    } catch (IOException i) {
      i.printStackTrace();
    } finally {
      try {
        if (oos != null) {
          oos.close();
        }
        if (fos != null) {
          fos.close();
        }
      } catch (IOException iOException) {
        // silent fail
      }
    }

  }

  public T deserialize() {

    T rtn = null;

    FileInputStream fileIn = null;
    ObjectInputStream in = null;

    try {
      fileIn = new FileInputStream(this.fileName);
      in = new ObjectInputStream(fileIn);
      rtn = (T) in.readObject();
      in.close();
      in = null;
      fileIn.close();
      fileIn = null;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
        if (fileIn != null) {
          fileIn.close();
        }
      } catch (IOException iOException) {
        // silent fail
      }
    }
    return rtn;
  }

}
