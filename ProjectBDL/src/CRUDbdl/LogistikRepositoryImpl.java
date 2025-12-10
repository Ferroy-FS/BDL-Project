package CRUDbdl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

// Pastikan kelas LogistikRepositoryImpl mengimplementasikan LogistikRepository
public class LogistikRepositoryImpl implements LogistikRepository {
    
    // --- Mock Data Storage (Menggantikan Database) ---
    private final List<Logistik> daftarLogistik;

    public LogistikRepositoryImpl() {
        this.daftarLogistik = new ArrayList<>();
        // Mock Data Logistik
        // Catatan: Kelas Logistik perlu setidaknya memiliki setter untuk Satuan dan StokMinimum
        daftarLogistik.add(new Logistik("L001", "Alat Tulis", "Pulpen Hitam", "pcs", 150, 50));
        daftarLogistik.add(new Logistik("L002", "Kebersihan", "Sabun Cuci Tangan", "botol", 10, 20)); 
        daftarLogistik.add(new Logistik("L003", "Konsumsi", "Air Mineral 600ml", "dus", 0, 5)); 
        daftarLogistik.add(new Logistik("L004", "Alat Tulis", "Kertas A4", "rim", 80, 50));
        daftarLogistik.add(new Logistik("L005", "P3K", "Plester Luka", "box", 5, 10)); 
    }

    @Override
    public List<Logistik> findAllLogistik() {
        return new ArrayList<>(daftarLogistik);
    }
    
    // ==========================================================
    // --- METODE BARU UNTUK EDIT/UPDATE LOGISTIK ---
    // ==========================================================

    /**
     * Mencari item logistik berdasarkan ID Inventaris.
     */
    @Override
    public Optional<Logistik> findById(String idInventaris) {
         return daftarLogistik.stream()
                .filter(l -> l.getIdInventaris().equals(idInventaris))
                .findFirst();
    }

    /**
     * Menyimpan atau memperbarui item logistik yang sudah dimuat.
     * Dalam mock, ini mensimulasikan operasi UPDATE.
     */
    @Override
    public Logistik save(Logistik logistik) {
        Optional<Logistik> existing = findById(logistik.getIdInventaris());
        
        if (existing.isPresent()) {
            // Hapus yang lama (simulasi update di List)
            daftarLogistik.removeIf(l -> l.getIdInventaris().equals(logistik.getIdInventaris()));
        } 
        // Tambahkan objek baru (dengan data yang sudah diupdate dari form)
        daftarLogistik.add(logistik);
        
        System.out.println("Logistik " + logistik.getIdInventaris() + " berhasil diupdate/disimpan.");
        return logistik;
    }
}