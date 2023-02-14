package Negocio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Principal extends Conexion{
    ArrayList<String> tablas = new ArrayList<>();
    ArrayList<String> relaciones = new ArrayList<>();
    ArrayList<String> executions = new ArrayList<>();
    ArrayList<String> tables = new ArrayList<>();
    Map<String, List<String>> relationships = new HashMap<>();

  public String table1 = "";
  public String table2 = " ";
  public String PK_FK = " ";
  public String table= " ";
  public String column;
  
  public void crear() throws SQLException{
    connection = getConnection();
    ps = connection.prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES");
    rs = ps.executeQuery();
    System.out.println(rs);
    while (rs.next()) {
        tablas.add(rs.getString("TABLE_NAME"));
        System.out.println(rs.getString("TABLE_NAME"));
    }
        ps.close();
    rs.close();
    connection.close();
    }
    public void tab() throws SQLException{
        connection = getConnection();
        for (String tabla : tablas) {
            if(!"sysdiagrams".equals(tabla)){
                ps = connection.prepareStatement("EXEC sp_fkeys " + tabla);
                rs = ps.executeQuery();
                 while (rs.next()) {
                    table1 = rs.getString("PKTABLE_NAME");
                    table2 = rs.getString("FKTABLE_NAME");
                    PK_FK = rs.getString("FKCOLUMN_NAME");
                    String pkTable = table2 + " - " + PK_FK;
                    List<String> pkTables = relationships.getOrDefault(table1, new ArrayList<>());
                    pkTables.add(pkTable);
                    relationships.put(table1, pkTables);
                }
            }
        }
    
    System.out.println("***************Relaciones presentes**********************************");
    System.out.println("TABLA: TABLA RELACIONADA - ATRIBUTO");
    for (Map.Entry<String, List<String>> entry : relationships.entrySet()) {
                System.out.println("Tabla: " + entry.getKey() + " -> " + entry.getValue());
            }
    
            ps.close();
    rs.close();
    connection.close();
  }
    
    public void encontrarRelaciones() throws SQLException{
            
    connection = getConnection();
         ps = connection.prepareStatement("SELECT column_name , table_name FROM INFORMATION_SCHEMA.COLUMNS");
     rs=ps.executeQuery();
     System.out.println("*****************************************************************");
     
      Map<String, List<String>> Orelationships = new HashMap<>();
            while (rs.next()) {
                String table = rs.getString("table_name");
                String column = rs.getString("column_name");
                List<String> columns = Orelationships.getOrDefault(table, new ArrayList<>());
                columns.add(column);
                Orelationships.put(table, columns);
                
            }
            for (Map.Entry<String, List<String>> entry : Orelationships.entrySet()) {
                    System.out.println("Tabla " + entry.getKey() + "  " + entry.getValue());
            }
                    System.out.println("*****************************************************************");
        Map<String, List<String>> posibles = new HashMap<>();
            // Mostrar los resultados
            for (int i = 0; i < tablas.size();i++) {
                for (int j = tablas.size()-1; j > 0 ;j--) {
                    if(!tablas.get(i).equals(tablas.get(j)))
                        for (String ls : Orelationships.get(tablas.get(i))) {
                            for (String ls2 : Orelationships.get(tablas.get(j))) {
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
            for (Map.Entry<String, List<String>> entry : posibles.entrySet()) {
                        System.out.println("Tabla: " + entry.getKey() + " -> " + entry.getValue());                
                    }
            
                 System.out.println("*****************************************************************");

            boolean b = relationships.entrySet().stream().anyMatch(e ->  e.getValue().equals(posibles.get(e.getKey())));
            System.out.println("b");
            ps.close();
    rs.close();
    connection.close();
    }
    
    
 }
    