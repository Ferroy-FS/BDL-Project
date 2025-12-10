package CRUDbdl;

// Kelas entitas untuk stok logistik (non-aset)
public class Logistik {
    private String idInventaris;
    private String kategori;
    private String namaBarang;
    private String satuan;
    private int stokSaatIni;
    private int stokMinimum;

    // Constructor, Getters, dan Setters
    public Logistik(String idInventaris, String kategori, String namaBarang, String satuan, int stokSaatIni, int stokMinimum) {
        this.idInventaris = idInventaris;
        this.kategori = kategori;
        this.namaBarang = namaBarang;
        this.satuan = satuan;
        this.stokSaatIni = stokSaatIni;
        this.stokMinimum = stokMinimum;
    }

    public String getIdInventaris() { return idInventaris; }
    public String getKategori() { return kategori; }
    public String getNamaBarang() { return namaBarang; }
    public String getSatuan() { return satuan; }
    public int getStokSaatIni() { return stokSaatIni; }
    public int getStokMinimum() { return stokMinimum; }
    // Setter-setter dihilangkan untuk kesederhanaan, asumsikan hanya tampilan.
public void setSatuan(String satuan) { 
    this.satuan = satuan; 
}
public void setStokMinimum(int stokMinimum) { 
    this.stokMinimum = stokMinimum; 
}
    // Metode untuk menghitung status
    public String getStatusKetersediaan() {
        if (stokSaatIni == 0) {
            return "HABIS";
        } else if (stokSaatIni <= stokMinimum) {
            return "MINIM";
        } else {
            return "CUKUP";
        }
    }
}