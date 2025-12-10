package CRUDbdl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AsetRepositoryImpl implements AsetRepository {
    
    // --- Mock Data Storage (Menggantikan Database) ---
    private final List<Aset> daftarAset;

    public AsetRepositoryImpl() {
        this.daftarAset = new ArrayList<>();
        
        // 1. Aset A001 (8 Parameter)
        Aset aset001 = new Aset("A001", "Laptop Kerja 1", "Elektronik", "Ruang Server", "Afrizal", 12500000.0, 9500000.0, new Date());
        aset001.setRiwayatPenyusutan(new ArrayList<>()); 
        daftarAset.add(aset001);
        
        // 2. Aset A002 (8 Parameter)
        Date tanggalPerolehan002 = new Date(System.currentTimeMillis() - 31536000000L); 
        Aset aset002 = new Aset("A002", "Mesin Printer", "Peralatan Kantor", "Lobby Utama", "Andi", 5000000.0, 4800000.0, tanggalPerolehan002);
        
        // --- BUAT DATA RIWAYAT PENYUSUTAN ---
        List<RiwayatPenyusutan> riwayatAset002 = new ArrayList<>();
        // Riwayat 1 (6 bulan lalu)
        riwayatAset002.add(new RiwayatPenyusutan(
            new Date(System.currentTimeMillis() - 15768000000L), 
            "Garis Lurus", 
            100000.0, 
            4900000.0)
        );
        // Riwayat 2 (Hari ini)
        riwayatAset002.add(new RiwayatPenyusutan(
            new Date(), 
            "Garis Lurus", 
            100000.0, 
            4800000.0)
        );
        
        aset002.setRiwayatPenyusutan(riwayatAset002);
        daftarAset.add(aset002);
        
        // 3. Aset A003 (8 Parameter)
        Aset aset003 = new Aset("A003", "Meja Kayu Jati", "Perabotan", "Ruang Meeting", "Budi", 3000000.0, 2500000.0, new Date());
        
        // --- TAMBAH RIWAYAT MOCK UNTUK A003 ---
        List<RiwayatPenyusutan> riwayatAset003 = new ArrayList<>();
        riwayatAset003.add(new RiwayatPenyusutan(new Date(System.currentTimeMillis() - 7776000000L), "Garis Lurus", 100000.0, 2900000.0));
        aset003.setRiwayatPenyusutan(riwayatAset003);
        
        daftarAset.add(aset003);
    } 

    
    @Override
    public Aset save(Aset aset) {
        Optional<Aset> existing = findById(aset.getKodeAset());
        
        if (existing.isPresent()) {
            deleteById(aset.getKodeAset());
            daftarAset.add(aset);
        } else {
            daftarAset.add(aset);
        }
        return aset;
    }
    
    @Override
    public List<Aset> findAll() {
        return new ArrayList<>(daftarAset);
    }
    
    @Override
    public Optional<Aset> findById(String kodeAset) {
        return daftarAset.stream()
                         .filter(a -> a.getKodeAset().equals(kodeAset))
                         .findFirst();
    }
    
    @Override
    public Aset getOne(String kodeAset) throws IllegalArgumentException {
        return findById(kodeAset)
               .orElseThrow(() -> new IllegalArgumentException("Aset dengan kode " + kodeAset + " tidak ditemukan."));
    }
    
    @Override
    public void deleteById(String kodeAset) {
        daftarAset.removeIf(aset -> aset.getKodeAset().equals(kodeAset));
    }
    
    @Override
    public void prosesPenyusutan() {
        System.out.println("Proses penyusutan dummy berhasil dijalankan.");
        for (Aset aset : daftarAset) {
            if (aset.getNilaiBuku() > 1000) {
                // Simulasi penyusutan 5%
                aset.setNilaiBuku(aset.getNilaiBuku() * 0.95);
            }
        }
    }
    
    @Override
    public String getNextId() {
        return "A" + String.format("%03d", daftarAset.size() + 1);
    }
}