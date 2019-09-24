package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import app.User;

public class IOStreamController implements Serializable {

	/**
     * Name of file we are writing to and reading from.
     */
	static String file = "usrList.ser";
	
	   /**
	   * Reads file and returns ArrayList of users.
	   * @return ArrayList<User> arraylist of users.
	   * @exception IOException On input error.
	   * @exception ClassNotFoundException Class of a serialized object cannot be found.
	   */
	@SuppressWarnings("unchecked")
	public static  ArrayList<User> read() throws IOException {
		ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(file));
		ArrayList<User> usrList;
		try {
			usrList = (ArrayList<User>) inStream.readObject();
			return usrList;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			inStream.close();
		}
	}
	   /**
	   * Writes User objects to file
	   * @param usrList Contains User objects that we want to save.
	   * @return Nothing
	   * @exception IOException on output error.
	   */
	public static void write(ArrayList<User> usrList) throws IOException {
		ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(file));
		outStream.writeObject(usrList);
		outStream.close();
	}

}
