package CRUDbdl;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Collections;

public class Aset {
    private String kodeAset;
    private String namaAset;
    private String kategori;    
    private String lokasi;      
    private String penanggungJawab; 
    private double hargaAwal;
    private double nilaiBuku;   
    // Properti 'kondisi' DIHAPUS
    private Date tanggalPerolehan;
    
    private List<RiwayatPenyusutan> riwayatPenyusutan; 

    public Aset() {
        this.riwayatPenyusutan = Collections.emptyList();
    }

    // CONSTRUCTOR FINAL (8 Parameter - Tanpa String 'kondisi')
    public Aset(String kodeAset, String namaAset, String kategori, String lokasi, 
                String penanggungJawab, double hargaAwal, double nilaiBuku, Date tanggalPerolehan) {
        this.kodeAset = kodeAset;
        this.namaAset = namaAset;
        this.kategori = kategori;
        this.lokasi = lokasi;
        this.penanggungJawab = penanggungJawab;
        this.hargaAwal = hargaAwal;
        this.nilaiBuku = nilaiBuku;
        // this.kondisi DIHILANGKAN
        this.tanggalPerolehan = tanggalPerolehan;
        this.riwayatPenyusutan = Collections.emptyList();
    }
    
    // Helper untuk format Rupiah
    public static String formatRupiah(double nilai) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return format.format(nilai);
    }
    
    // Helper untuk format Tanggal
    public static String formatTanggal(Date tanggal) {
        if (tanggal == null) return "-";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        return sdf.format(tanggal);
    }
    
    // --- GETTERS & SETTERS ---
    public String getKodeAset() { return kodeAset; }
    public void setKodeAset(String kodeAset) { this.kodeAset = kodeAset; }
    public String getNamaAset() { return namaAset; }
    public void setNamaAset(String namaAset) { this.namaAset = namaAset; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }
    public String getPenanggungJawab() { return penanggungJawab; }
    public void setPenanggungJawab(String penanggungJawab) { this.penanggungJawab = penanggungJawab; }
    public double getHargaAwal() { return hargaAwal; }
    public void setHargaAwal(double hargaAwal) { this.hargaAwal = hargaAwal; }
    public double getNilaiBuku() { return nilaiBuku; }
    public void setNilaiBuku(double nilaiBuku) { this.nilaiBuku = nilaiBuku; }
    // Getter/Setter 'kondisi' DIHAPUS
    public Date getTanggalPerolehan() { return tanggalPerolehan; }
    public void setTanggalPerolehan(Date tanggalPerolehan) { this.tanggalPerolehan = tanggalPerolehan; }
    public List<RiwayatPenyusutan> getRiwayatPenyusutan() { return riwayatPenyusutan; }
    public void setRiwayatPenyusutan(List<RiwayatPenyusutan> riwayatPenyusutan) { this.riwayatPenyusutan = riwayatPenyusutan; }
}