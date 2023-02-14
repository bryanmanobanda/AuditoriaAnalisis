
package Negocio;

import java.sql.SQLException;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        Principal principal = new Principal();
        principal.crear();
        principal.tab();
        principal.encontrarRelaciones();
    }
    
}
