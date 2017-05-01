package edu.nkfust.tianming.ihealth;

import java.util.List;

/**
 * Created by Tianming on 2017/3/30.
 */

public class User {
    private String password;
    private List<Record> record;
    public User(){
    }
    public User(String password){
        this.password=password;
    }
    public User(String password,List<Record> record){
        this.password=password;
        this.record=record;
    }
    public String getPassword(){
        return password;
    }
    public List<Record> getRecord(){
        return record;
    }
}
