package com.berpesan.model;

/**
 * Created by itdel on 4/3/17.
 */

public class TrendingSpam {

    private String id_tren_spam;
    private String jlh_komentar;
    private String jlh_laporan;
    private String konten_spam;

    public TrendingSpam(String id_tren_spam, String jlh_komentar, String jlh_laporan, String konten_spam){
        this.id_tren_spam = id_tren_spam;
        this.jlh_komentar = jlh_komentar;
        this.jlh_laporan = jlh_laporan;
        this.konten_spam = konten_spam;
    }

    public String getId_tren_spam(){
        return id_tren_spam;
    }

    public String getJlh_laporan(){ return jlh_laporan;}

    public String getKonten_spam(){return konten_spam;}
}
