package com.tepav.reader.models;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 06/02/14
 * Time: 17:53
 */
public class Gunluk {

    private String gunluk_id;
    private String btitle;
    private String bcontent;
    private String bdate;
    private String pfullname;
    private String ptype;
    private String ptitle;
    private String pimage;

    public String getGunluk_id() {
        return gunluk_id;
    }

    public void setGunluk_id(String gunluk_id) {
        this.gunluk_id = gunluk_id;
    }

    public String getBtitle() {
        return btitle;
    }

    public void setBtitle(String btitle) {
        this.btitle = btitle;
    }

    public String getBcontent() {
        return bcontent;
    }

    public void setBcontent(String bcontent) {
        this.bcontent = bcontent;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getPfullname() {
        return pfullname;
    }

    public void setPfullname(String pfullname) {
        this.pfullname = pfullname;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getPtitle() {
        return ptitle;
    }

    public void setPtitle(String ptitle) {
        this.ptitle = ptitle;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }
}
