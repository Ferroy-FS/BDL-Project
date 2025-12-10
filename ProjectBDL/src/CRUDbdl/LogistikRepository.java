package CRUDbdl;

import java.util.List;
import java.util.Optional; // Diperlukan untuk Optional

public interface LogistikRepository {
    List<Logistik> findAllLogistik();
    
    // --- YANG DITAMBAHKAN ---
    Optional<Logistik> findById(String idInventaris); 
    Logistik save(Logistik logistik);
    
}