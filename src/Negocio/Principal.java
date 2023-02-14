package Negocio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;

public class Principal extends Conexion{
    ArrayList<String> tablas = new ArrayList<>();
    Map<String, List<String>> relationships = new HashMap<>();
    FileWriter fw;
  
  public void obtenerTablas(String BD) throws SQLException{
        connection = getConnection(BD);
        ps = connection.prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES");
        rs = ps.executeQuery();
        while (rs.next()) {
            tablas.add(rs.getString("TABLE_NAME"));
        }
        ps.close();
        rs.close();
        connection.close();
    }
  
    public Map buscarRelaciones(String BD) throws SQLException{
        connection = getConnection(BD);
        for (String tabla : tablas) {
            if(!"sysdiagrams".equals(tabla)){
                ps = connection.prepareStatement("EXEC sp_fkeys " + tabla);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String tablePK = rs.getString("PKTABLE_NAME");
                    String tableFK = rs.getString("FKTABLE_NAME");
                    String clave = rs.getString("FKCOLUMN_NAME");
                    String pkTable = tableFK + " - " + clave;
                    List<String> pkTables = relationships.getOrDefault(tablePK, new ArrayList<>());
                    pkTables.add(pkTable);
                    relationships.put(tablePK, pkTables);
                }
            }
        }
        ps.close();
        rs.close();
        connection.close();
        return relationships; 
 }
    
    public Map posiblesRelaciones(String BD) throws SQLException{
        connection = getConnection(BD);
        ps = connection.prepareStatement("SELECT column_name , table_name FROM INFORMATION_SCHEMA.COLUMNS");
        rs=ps.executeQuery();
        Map<String, List<String>> tables_columns = new HashMap<>();
        while (rs.next()) {
            String table = rs.getString("table_name");
            String column = rs.getString("column_name");
            List<String> columns = tables_columns.getOrDefault(table, new ArrayList<>());
            columns.add(column);
            tables_columns.put(table, columns);

        }
        
        //Encontrar las posibles relaciones
        Map<String, List<String>> posibles = new HashMap<>();
        for (int i = 0; i < tablas.size();i++) {
            for (int j = tablas.size()-1; j > 0 ;j--) {
                if(!tablas.get(i).equals(tablas.get(j)))
                    for (String ls : tables_columns.get(tablas.get(i))) {
                        for (String ls2 : tables_columns.get(tablas.get(j))) {
                            if(ls.equals(ls2)){         
                                String pkTable = tablas.get(j) + " - " + ls;
                                List<String> pkTables = posibles.getOrDefault(tablas.get(i), new ArrayList<>());
                                pkTables.add(pkTable);
                                posibles.put(tablas.get(i), pkTables);
                        }
                    }
                }
            }
        }
        ps.close();
        rs.close();
        connection.close();
        return posibles;
    }
    
    public void CrearLog(String contenido, String BD){
        SimpleDateFormat formato = new SimpleDateFormat("HH.mm.ss dd-MM-yyyy", Locale.getDefault());
        Date Ahora = new Date();
        String name = formato.format(Ahora);
        String archivo = "c:\\"+BD+"logs_"+name+".txt";
        File fichero = new File (archivo);
        try {
            if (fichero.createNewFile()){
                fw = new FileWriter(fichero);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(contenido);
                bw.close();
                JOptionPane.showMessageDialog(null, "Se creo el log: " + archivo);
            }
            else
              JOptionPane.showMessageDialog(null, "No se ha podido crear el log: " + archivo);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
 }