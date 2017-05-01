package edu.nkfust.tianming.ihealth;

/**
 * Created by Tianming on 2017/4/4.
 */

public class Record {
    private String codeName;
    private String codeContent;
    public Record(){

    }
    public Record(String codeName, String codeContent){
        this.codeName = codeName;
        this.codeContent = codeContent;
    }
    public String getCodeName(){
        return codeName;
    }
    public String getCodeContent(){
        return codeContent;
    }
}
