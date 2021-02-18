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
import org.lila_systems.mypet.model.Mascota;
import org.lila_systems.mypet.model.Producto;

public class ControllerMascota {

    private ConexionMySQL conn = new ConexionMySQL();

    public int insert(Mascota m) throws Exception {
        //datos de mascota dentro de la base de datos valores de retorno 
        String sql = "{call insertProductoAnimal(?,?,?,?,?,?,"//Datos de la tabla producto
                + "?,?,?)";//datos de la tabla animal

        //Conectamos a la base de datos 
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        //utiliando la conexión mysql, creamos un objeto que nos permita invocar 
        //el procedimiento almacenado que hará la inserción en las tablas de producto y animal
        CallableStatement cstmt = conn.prepareCall(sql);

        //llenamos los datos de la mascota de acuerdo con los parametrosque pide
        //el procedimiento almacenado 
        //Datos de la tabla producto
        cstmt.setString(1, m.getProducto().getNombre());
        cstmt.setInt(2, m.getProducto().getExistencias());
        cstmt.setDouble(3, m.getProducto().getPrecioCompra());

        //datos de la tabla mascota
        cstmt.setString(4, m.getRaza());
        cstmt.setString(5, m.getEspecie());
        cstmt.setString(6, m.getFechaNacimiento());
        cstmt.setString(7, m.getFechaLlegada());

        //registramos los parametros de salida 
        cstmt.registerOutParameter(8, Types.INTEGER);
        cstmt.registerOutParameter(9, Types.INTEGER);

        //ejecutamos la consulta
        cstmt.execute();

        //recuperamos los id generados 
        m.getProducto().setId(cstmt.getInt(8));
        m.setId(cstmt.getInt(9));

        //establecemos las reglas con base a las reglas de negocio 
        m.getProducto().setPrecioVenta(m.getProducto().getPrecioCompra() * 2);

        //cerramos los objetos de la base de datos
        cstmt.close();
        connMySQL.close();

        //devolvemos el id de la mercancia generada
        return m.getId();
    }

    public void update(Mascota m) throws Exception {
        //ID de producto y mascota 
        String sql = "{call updateProductoAnimal(?,?,?," //datos de la tabla producto
                + "?,?,?,?,?)}"; //datos de la tabla animal
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);

        //llenamos los datos de las mascotas de acuerdo con los parametros que 
        //pide el procedimiento almacenado
        cstmt.setInt(1, m.getProducto().getId());
        //cstmt.setInt(2, m.getId());

        //Datos de la tabla producto
        cstmt.setString(2, m.getProducto().getNombre());
        cstmt.setInt(3, m.getProducto().getExistencias());
        cstmt.setDouble(4, m.getProducto().getPrecioCompra());

        //Datos de la tabla animal
        cstmt.setString(5, m.getRaza());
        cstmt.setString(6, m.getEspecie());
        cstmt.setString(7, m.getFechaNacimiento());
        cstmt.setString(8, m.getFechaLlegada());

        //ejecutamos la consulta
        cstmt.execute();

        //cerramos los objetos concexion
        cstmt.close();
        connMySQL.close();
    }

    public void delete(int id) throws Exception {
        //definimos la consulta SQL
        String sql = "UPDATE producto SET estatus = 0 WHERE idProducto = " + id;

        //generamos un objeto de conexion con MySQL
        ConexionMySQL connMySQL = new ConexionMySQL();

        //abrimos la conexion con MySQL
        Connection conn = connMySQL.open();

        //generamos el objeto que nos permitirá ejecutar la sentencia SQL
        Statement stmt = conn.createStatement();

        //ejecutamos la instrucción SQL 
        stmt.execute(sql);

        //cerramos la conexión SQL
        stmt.close();
        connMySQL.close();
    }

    public List<Mascota> getAll(String filtro) throws Exception {
        //Generamos la consulta SQL
        /*
            Aqui se opto por inner join ya que la vista no trae precio compra y por falta de tiempo se utilizo de forma        
         */
        String sql = "select * from producto inner join animal on producto.idProducto=animal.idProducto";

        //abrimos una conexion con MySQL
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        //generamos un objeto que nos permita ejecutar la consulta 
        //de manera segura, a diferencia de un Statement
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //ejecutamos la consulta y obtenemos los resultados 
        ResultSet rs = pstmt.executeQuery();

        //Declaramos la lista dinámica´para guardar los objetos que 
        //generamos al recorrer los resultados de la consulta sql
        List<Mascota> mascotas = new ArrayList<>();
        Mascota m = null;

        //Recorremos el conjunto de registros devuelto por la base de datos 
        while (rs.next()) {
            //Obenemos un objeto de tipo mercancía con los datos que se 
            //encuentran en el registro actual, devuelto por la base de datos 
            m = fill(rs);

            //agrefuamos el objeto a la lista
            mascotas.add(m);
        }
        //Devolvemos la lista de objetos consultados
        return mascotas;
    }

    /**
     * Este método me sirve para generar un objeto de tipo Mercancia, a partir
     * de un objeto ResutSet en la posición que tenga actualmente
     *
     * @param rs
     * @retun null
     * @throws Exception
     */
    public Mascota fill(ResultSet rs) throws Exception {
        Mascota m = new Mascota();
        Producto p = new Producto();

        //Llenamos los datos de Producto
        p.setId(rs.getInt("IdProducto"));
        p.setExistencias(rs.getInt("existencias"));
        p.setNombre(rs.getString("nombre")); //Le pusiste mombre
        p.setPrecioCompra(rs.getDouble("precioCompra"));
        p.setPrecioVenta(rs.getDouble("precioVenta"));   //La consulta no te regresa precio de venta con la vista
        p.setStatus(rs.getInt("estatus"));

        //llenamos los datos de mascota 
        m.setId(rs.getInt("idAnimal"));  //Es idAnimal pusiste idMascota
        m.setRaza(rs.getString("raza"));
        m.setEspecie(rs.getString("especie"));
        m.setFechaNacimiento(rs.getString("fechaNacimiento"));
        m.setFechaLlegada(rs.getString("fechaLlegada"));
        m.setProducto(p); //Te falto agregar el producto a mercancia
        return m;
    }
}
