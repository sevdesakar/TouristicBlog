package com.example.tasarim.Models;

public class anasayfaModel {
    private String isim,resimUrl,postId,yazi,nedir,sehirId;
    private float puan;


    public anasayfaModel(String isim, String resimUrl, float puan, String postId, String yazi, String nedir, String sehirId) {
        this.isim = isim;
        this.resimUrl = resimUrl;
        this.puan = puan;
        this.yazi=yazi;
        this.nedir=nedir;
        this.sehirId=sehirId;

    }
    public anasayfaModel(){}

    public String getSehirId() {
        return sehirId;
    }

    public void setSehirId(String sehirId) {
        this.sehirId = sehirId;
    }

    public String getNedir() {
        return nedir;
    }

    public void setNedir(String nedir) {
        this.nedir = nedir;
    }

    public String getYazi() {
        return yazi;
    }

    public void setYazi(String yazi) {
        this.yazi = yazi;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getResimUrl() {
        return resimUrl;
    }

    public void setResimUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }

    public float getPuan() {
        return puan;
    }

    public void setPuan(float puan) {
        this.puan = puan;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
