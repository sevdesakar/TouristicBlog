package com.example.tasarim.Models;

import android.widget.RelativeLayout;

public class categoryModel {
    String sehir, sehirId, resimUrl;

    public categoryModel(String sehir, String sehirId, String resimUrl){
        this.sehir= sehir;
        this.sehirId= sehirId;
        this.resimUrl= resimUrl;
        
        
    }
    public categoryModel(){}

    public String getSehir() {
        return sehir;
    }

    public void setSehir(String sehir) {
        this.sehir = sehir;
    }

    public String getSehirId() {
        return sehirId;
    }

    public void setSehirId(String sehirId) {
        this.sehirId = sehirId;
    }

    public String getResimUrl() {
        return resimUrl;
    }

    public void setResimUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }
}

