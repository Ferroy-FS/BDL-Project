// File: CRUDbdl/AsetRepository.java
package CRUDbdl;

import java.util.List;
import java.util.Optional;

public interface AsetRepository {
    
    // C - Create / U - Update
    Aset save(Aset aset); 
    
    // R - Read All
    List<Aset> findAll();
    
    // R - Read by ID
    Optional<Aset> findById(String kodeAset);
    Aset getOne(String kodeAset) throws IllegalArgumentException;
    
    // D - Delete
    void deleteById(String kodeAset);
    
    // Operasi Khusus
    void prosesPenyusutan();
    
    // Helper
    String getNextId();
}