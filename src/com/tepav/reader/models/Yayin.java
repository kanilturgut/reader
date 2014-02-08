package com.tepav.reader.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 06/02/14
 * Time: 18:01
 */
public class Yayin implements Serializable {

    private String yayin_id;
    private String ytitle;
    private String ydate;
    private String yauthors;
    private String ycontent;
    private String ytype;
    private String ytype_id;
    private List<File> files;

    public String getYayin_id() {
        return yayin_id;
    }

    public void setYayin_id(String yayin_id) {
        this.yayin_id = yayin_id;
    }

    public String getYtitle() {
        return ytitle;
    }

    public void setYtitle(String ytitle) {
        this.ytitle = ytitle;
    }

    public String getYdate() {
        return ydate;
    }

    public void setYdate(String ydate) {
        this.ydate = ydate;
    }

    public String getYauthors() {
        return yauthors;
    }

    public void setYauthors(String yauthors) {
        this.yauthors = yauthors;
    }

    public String getYcontent() {
        return ycontent;
    }

    public void setYcontent(String ycontent) {
        this.ycontent = ycontent;
    }

    public String getYtype() {
        return ytype;
    }

    public void setYtype(String ytype) {
        this.ytype = ytype;
    }

    public String getYtype_id() {
        return ytype_id;
    }

    public void setYtype_id(String ytype_id) {
        this.ytype_id = ytype_id;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
