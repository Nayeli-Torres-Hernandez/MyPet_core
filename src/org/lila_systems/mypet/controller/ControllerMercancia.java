package org.lila_systems.mypet.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.lila_systems.mypet.db.ConexionMySQL;
import org.lila_systems.mypet.model.Mercancia;
import org.lila_systems.mypet.model.Producto;

public class ControllerMercancia {

    public int insert(Mercancia m) throws Exception {
        //Definimos las instrucciones SQL dentro de un String JAVA
        String sql = "{call insertProductoMercancia(?, ?, ?, ?, "
                                                 + "?, ?, ?, ?, "
                                                 + "?, ?)}";
        //Nos conectamos a la BD
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        //Creamos un objeto con la conexion de MYsql
        //Invocamos el stored procedure que hara la insercion de las tablas
        // producto y mercancia
        CallableStatement cstmt = conn.prepareCall(sql);
        //Llenamos los datos del producto de acuerdo  con los productos que pide
        //el Stored Procedure
        cstmt.setString(1, m.getProducto().getNombre());
        cstmt.setInt(2, m.getProducto().getExistencias());
        cstmt.setDouble(3, m.getProducto().getPrecioCompra());
        cstmt.setString(4, m.getProducto().getFoto());

        cstmt.setString(5, m.getCodigoBarras());
        cstmt.setString(6, m.getDescripcion());
        cstmt.setString(7, m.getModelo());
        cstmt.setString(8, m.getMarca());
        
        //Registramos los parametros de Salida
        cstmt.registerOutParameter(9, Types.INTEGER);
        cstmt.registerOutParameter(10, Types.INTEGER);
        //Ejecutamos la consulta
        cstmt.execute();
        //Recuperamos los ID´s generados
        m.getProducto().setId(cstmt.getInt(9));
        m.setId(cstmt.getInt(10));
        
        //Establecemos el precio de Venta de acuerdo con las reglas de negocio
        m.getProducto().setPrecioVenta(m.getProducto().getPrecioCompra() * 2);
        
        cstmt.close();
        connMySQL.close();
        
        return m.getId();
    }
    
    public void update(Mercancia m) throws Exception{
        String sql = "{call updateProductoMercancia(?,?," + //ID de Producto Y Mercancia
                                                    "?,?,?," + //Datos de Producto
                                                    "?,?,?,?,?)}"; //Datos de Mercancia
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);
        
        //Llenamos los datos del Prodcuto de acuerdo con los parámetros que pide
        //el Stored procedure
        cstmt.setInt(1, m.getProducto().getId());
        cstmt.setInt(2, m.getId());
        
        cstmt.setString(3, m.getProducto().getNombre());
        cstmt.setInt(4, m.getProducto().getExistencias());
        cstmt.setDouble(5, m.getProducto().getPrecioCompra());
        
        cstmt.setString(6, m.getCodigoBarras());
        cstmt.setString(7, m.getDescripcion());
        cstmt.setString(8, m.getModelo());
        cstmt.setString(9, m.getMarca());
        cstmt.setString(10, m.getProducto().getFoto());
        
        //Ejecutamos la consulta;
        cstmt.execute();
         
        //Cerramos los objetos de la BD;
        cstmt.close();
        connMySQL.close();
    }
    
    public void delete(int id) throws Exception {
        //Definimos la consulta SQL
        String sql = "UPDATE producto SET estatus = 0 WHERE idProducto = " + id;
        //Generemos un objeto de conexion con MySQL
        ConexionMySQL connMySQL = new ConexionMySQL();
        //Abrimos la conexion con MySQL
        Connection conn = connMySQL.open();
        //Generamos el objeto que nos permita ejecutar la sentencia SQL
        Statement stmt = conn.createStatement();
        
        //Ejecutamos la instruccion SQL 
        stmt.execute(sql);
        
        //Cerramos los objetos de conexion 
        stmt.close();
        connMySQL.close();
    }
    
    public Mercancia findById(int id) throws Exception {
        return null;
    }
    
    public List<Mercancia> getAll(String filtro) throws Exception {
        //Definimos la consulta SQL
        String sql = "SELECT * FROM v_mercancias VM ";
        
        //Abrimos una conexion con MySQL 
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        //Generamos un objeto que nos permita ejecutar la consulta
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        //Ejecutamos la consulta y obtenemos sus resultados 
        ResultSet rs = pstmt.executeQuery();
        
        //Creamos una lista dinamica para guardar los objetos que generamos
        //al recorrer los resultados devueltos por la BD 
        List<Mercancia> mercancias = new ArrayList<>();
        
        Mercancia m = null;
        
        //Recorremos el conjunto de registros devueltos por la BD
        while (rs.next()){
            
            //Obtenemos un objeto de tipo Mercancía con los datos que se 
            //encuentran en el registro actual, devuelto por la BD:
            m = fill(rs);
            
            //agregamos el objeto a la lista:
            mercancias.add(m);
        }
        //Devolvemos la lista de objetos consultados:
        return mercancias;
    }
    
    /**
     * Este método sirve para generar un objeto de tipo mercancia a partir de un objeto
     * Resulset en la posicion que tenga actualmente indicada.
     * @param rs
     * @return
     * @throws Exception 
     */
    public Mercancia fill(ResultSet rs) throws Exception {
        Mercancia m = new Mercancia();
        Producto p = new Producto();
        
        //Llenamos los datos de Producto 
        p.setId(rs.getInt("idProducto"));
        p.setExistencias(rs.getInt("existencias"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecioCompra(rs.getDouble("precioCompra"));
        p.setPrecioVenta(rs.getDouble("precioVenta"));
        p.setStatus(rs.getInt("estatus"));
        p.setFoto(rs.getString("foto"));
        
        m.setId(rs.getInt("idMercancia"));
        m.setCodigoBarras(rs.getString("codigoBarras"));
        m.setDescripcion(rs.getString("descripcion"));
        m.setMarca(rs.getString("marca"));
        m.setModelo(rs.getString("modelo"));
        m.setProducto(p);
        
        return m;
    }
    
}
