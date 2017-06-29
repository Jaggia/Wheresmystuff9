package com.example.jaggia.wheresmystuff9.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by James on 6/14/2017.
 *
 * Represents a single User which has an id, username, and password
 *
 * We are passing this object in a bundle between intents, so we implement
 * the Parcelable interface.
 *
 */

public class User implements Parcelable{
    //static variable to create unique id
    private static int Next_ID;

    /**unique id number*/
    private int _id;
    /**The users real life name*/
    private String _name;
    /**unique username*/
    private String _username;
    /**the user's password*/
    private String _password;
    /*ErrorCode returned when creating the user*/
    //private ErrorCode _errorCode;

    private boolean _isAdmin;

    /**no parameter constructor for controller*/
    public User(){
        this("default name" , "default username" , "default password");
    }

    /** makes a new User
     *
     * @param name the user's real life name
     * @param username the user's public name
     * @param password the user's password
     * _id is a unique identifier for the user
     */
    public User(String name, String username, String password){

        _id = Next_ID++;
        _name = name;
        _username = username;
        _password = password;
        _isAdmin = false;
    }
    /** makes a new User
     *
     * @param name the user's real life name
     * @param username the user's public name
     * @param password the user's password
     * @param isAdmin boolean controlling whether the user is an Admin
     * _id is a unique identifier for the user
     */
    public User(String name, String username, String password, boolean isAdmin){
        this(name, username,password);
        _isAdmin = isAdmin;
    }

    /** ********************************************
     * Getters and Setters
     * @return the user's id
     */
    public int getId(){
        return _id;
    }

    /**
     *
     * @return the user's username
     */
    public String getUsername(){
        return _username;
    }
    /**
     *
     * @return the user's password
     */
    public String getPassword(){
        return _password;
    }
    /**
     *
     * @return the user's real name
     */
    public String getName() {return _name;};
    //public ErrorCode getErrorCode() {return _errorCode; }
    /**
     *
     * @param username name to set as the user's username
     */
    public void setUsername(String username){
        _username = username;
    }
    /**
     *
     * @param password name to set as the user's password
     */
    public void setPassword(String password){
        _password = password;
    }
    /**
     *
     * @param name name to set as the user's real name
     */
    public void setName(String name){_name = name; };

    /* ********************************************
     * Overridden methods
     * toString(), describeContents(), writeToParcel()
     *
     */
    //prints the user's username
    @Override
    public String toString(){
        return _username;
    }
    //needed to implement parcel
    @Override
    public int describeContents(){
        return 0;
    }
    //method for parcel
    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(_name);
        dest.writeString(_username);
        dest.writeString(_password);
        dest.writeInt(_id);
    }

    //method for parcel
    private User(Parcel in){
        _name = in.readString();
        _username = in.readString();
        _password = in.readString();
        _id = in.readInt();
    }

    //method for parcel
    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
