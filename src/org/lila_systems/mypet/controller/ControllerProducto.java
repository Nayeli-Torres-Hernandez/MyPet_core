package org.lila_systems.mypet.controller;

public class ControllerProducto {
    /*public int insert(Mercancia m) throws Exception{
        //
        String sql = "{call insertProductoMercancia(?, ?, ?, ?, " + //Datos productos
                                                   "?, ?, ?, ?, " + //Datos mercancia 
                                                    "?, ?)}"; //Valores de retorno
        //Nos conectamos a la BD
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        //Utilizando la conexion con MySQL, creamos un objeto que nos permita 
        //invocar el Stored Procedure que hara la insercion en las tablas
        //producto y mercancia
        
        CallableStatement cstmt = conn.prepareCall(sql);
        
        //Llenamos los datos del producto de acuerdo con los parametros que pide
        //el Stored Procedure
        cstmt.setString(1, m.getProducto().getNombre());
        cstmt.setInt(2, m.getProducto().getExistencias());
        cstmt.setDouble(3, m.getProducto().getPrecioCompra() );
        cstmt.setString(4, m.getProducto().getFoto());
        
        cstmt.setString(5, m.getCodigoBarras());
        cstmt.setString(6, m.getDescripcion());
        cstmt.setString(7, m.getModelo());
        cstmt.setString(8, m.getMarca());
        
        //Registramos los parametros de salida
        cstmt.registerOutParameter(9, Types.INTEGER);
        cstmt.registerOutParameter(10, Types.INTEGER);
        
       //Registramos los parametros de salida
       cstmt.execute();
       
       //Recuperamos los ID's generados
       m.getProducto().setId(cstmt.getInt(9));
       m.setId(cstmt.getInt(10));
       
       //Establecemos el precio de venta de acuerdo con las reglas de negocio
       m.getProducto().setPrecioVenta(m.getProducto().getPrecioCompra()*2);
       
       //Cerramos los objetos de BD
       cstmt.close();
       connMySQL.close();
       
       //Devolvemos el ID de la Mercancia generado
       return m.getId();
    }
    
    public void update(Mercancia m) throws Exception{
        String sql = "{call updateProductoMercancia(?, ?, " + //ID de producto y mercancia
                                                   "?, ?, ? " + // Dtaos de producto
                                                   "?, ?, ?, ?)}"; // Datos de mercancia
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);
        
        //Llenamos los datos del producto de acuerdo con los parametros que pide
        //el Strored Procedure
        cstmt.setInt(1, m.getProducto().getId());
        cstmt.setInt(2, m.getId());
        
        cstmt.setString(3, m.getProducto().getNombre());
        cstmt.setInt(4, m.getProducto().getExistencias());
        cstmt.setDouble(5, m.getProducto().getPrecioCompra());
        
        cstmt.setString(6, m.getCodigoBarras());
        cstmt.setString(7, m.getDescripcion());
        cstmt.setString(8, m.getModelo());
        cstmt.setString (9, m.getMarca());
        
        //Ejecutamos la consulta
        cstmt.execute();
        
        //Cerramos los objetos de BD
        cstmt.close();
        connMySQL.close();
    }
    
    public void delete(int id) throws Exception{
        
    }
    
    public Mercancia findById(int id) throws Exception{
        return null;
        
    }
    
    public List<Mercancia> getAll(String filtro) throws Exception{
        return null;
        
    }

    public Mercancia fill(ResultSet rs) throws Exception{
        return null;
        
    }
    */
}