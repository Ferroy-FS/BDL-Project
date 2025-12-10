// File: CRUDbdl/RiwayatPenyusutan.java
package CRUDbdl;

import java.util.Date;

public class RiwayatPenyusutan {
    private Date tanggalHitung;
    private String metode;
    private double nilaiTurun;
    private double nilaiSisa;

    public RiwayatPenyusutan(Date tanggalHitung, String metode, double nilaiTurun, double nilaiSisa) {
        this.tanggalHitung = tanggalHitung;
        this.metode = metode;
        this.nilaiTurun = nilaiTurun;
        this.nilaiSisa = nilaiSisa;
    }
    
    public Date getTanggalHitung() { return tanggalHitung; }
    public String getMetode() { return metode; }
    public double getNilaiTurun() { return nilaiTurun; }
    public double getNilaiSisa() { return nilaiSisa; }
}