package org.lila_systems.mypet.controller;

import org.lila_systems.mypet.db.ConexionMySQL;
import org.lila_systems.mypet.model.Persona;
import org.lila_systems.mypet.model.Proveedor;
import java.util.List;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

public class ControllerProveedor {

    public int insert(Proveedor pr) throws Exception {
        //Definimos las instrucciones SQL dentro de un String JAVA
        String sql = "{call insertProveedor(?,?,?,?,?,?,?,?,?,?," //Datos de Persona
                + "?,?," //Datos de proveedor
                + "?,?)}";
        //Nos conectamos a la BD
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        //Creamos un objeto con la conexion de MYsql
        //Invocamos el stored procedure que hara la insercion de las tablas
        // persona y Proveedor
        CallableStatement cstmt = conn.prepareCall(sql);

        //Llenamos los datos de Persona de acuerdo con los datos que pide
        //el Stored Procedure
        cstmt.setString(1, pr.getProveedor().getNombre());
        cstmt.setString(2, pr.getProveedor().getApellidoPaterno());
        cstmt.setString(3, pr.getProveedor().getApellidoMaterno());
        cstmt.setString(4, pr.getProveedor().getCalle());
        cstmt.setString(5, pr.getProveedor().getNumero());
        cstmt.setString(6, pr.getProveedor().getColonia());
        cstmt.setInt(7, pr.getProveedor().getCp());
        cstmt.setString(8, pr.getProveedor().getCiudad());
        cstmt.setString(9, pr.getProveedor().getEstado());
        cstmt.setString(10, pr.getProveedor().getTel1());

        //Llenamos los datos de Proveedor de acuerdo con los datos que pide
        //el Stored Procedure
        cstmt.setString(11, pr.getRfc());
        cstmt.setString(12, pr.getRazonSocial());

        //Registramos los parametros de Salida
        cstmt.registerOutParameter(13, Types.INTEGER);
        cstmt.registerOutParameter(14, Types.INTEGER);

        //Ejecutamos la consulta
        cstmt.execute();

        //Recuperamos los ID´s generados
        pr.getProveedor().setId(cstmt.getInt(13));
        pr.setId(cstmt.getInt(14));

        cstmt.close();
        connMySQL.close();

        return pr.getId();
    }

    public void update(Proveedor pr) throws Exception {
        //Definimos las instrucciones SQL dentro de un String JAVA
        String sql = "{call updateProveedor(?,?," //2
                + "?,?,?,?,?,?,?,?,?," //11
                + "?,?)}";  //2
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);

        //Llenamos los datos del Prodcuto de acuerdo con los parámetros que pide
        //el Stored procedure
        cstmt.setInt(1, pr.getProveedor().getId());
        //cstmt.setInt(2, e1.getId());

        cstmt.setString(2, pr.getProveedor().getNombre());
        cstmt.setString(3, pr.getProveedor().getApellidoPaterno());
        cstmt.setString(4, pr.getProveedor().getApellidoMaterno());
        cstmt.setString(5, pr.getProveedor().getCalle());
        cstmt.setString(6, pr.getProveedor().getNumero());
        cstmt.setString(7, pr.getProveedor().getColonia());
        cstmt.setInt(8, pr.getProveedor().getCp());
        cstmt.setString(9, pr.getProveedor().getCiudad());
        cstmt.setString(10, pr.getProveedor().getEstado());
        cstmt.setString(11, pr.getProveedor().getTel1());

        cstmt.setString(12, pr.getRfc());
        cstmt.setString(13, pr.getRazonSocial());

        //Ejecutamos la consulta;
        cstmt.execute();

        //Cerramos los objetos de la BD;
        cstmt.close();
        connMySQL.close();
    }

    public void delete(int id) throws Exception {
        //Definimos la consulta SQL
        String sql = "UPDATE persona SET estatus = 0 WHERE idPersona = " + id;
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

    public Proveedor findById(int id) throws Exception {
        return null;
    }

    public List<Proveedor> getAll(String filtro) throws Exception {
        //Definimos la consulta SQL
        String sql = "SELECT * FROM v_proveedor_persona";

        //Abrimos una conexion con MySQL
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        //Generamos un objeto que nos permita ejecutar la consulta
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Ejecutamos la consulta y obtenemos sus resultados 
        ResultSet rs = pstmt.executeQuery();

        //Creamos una lista dinamica para guardar los objetos que generamos
        //al recorrer los resultados devueltos por la BD 
        List<Proveedor> proveedores = new ArrayList<>();

        Proveedor pr = null;

        //Recorremos el conjunto de registros devueltos por la BD
        while (rs.next()) {

            //Obtenemos un objeto de tipo Mercancía con los datos que se 
            //encuentran en el registro actual, devuelto por la BD:
            pr = fill(rs);

            //agregamos el objeto a la lista:
            proveedores.add(pr);
        }
        //Devolvemos la lista de objetos consultados:
        return proveedores;
    }

    /**
     * Este método sirve para generar un objeto de tipo proveedor a partir de un
     * objeto Resulset en la posicion que tenga actualmente indicada.
     *
     * @param rs
     * @return
     * @throws Exception
     */
    public Proveedor fill(ResultSet rs) throws Exception {
        Proveedor pr = new Proveedor();
        Persona p = new Persona();

        //Llenamos los datos de Persona 
        p.setId(rs.getInt("idPersona"));
        p.setNombre(rs.getString("nombre"));
        p.setApellidoPaterno(rs.getString("apellidoPaterno"));
        p.setApellidoMaterno(rs.getString("apellidoMaterno"));
        p.setCalle(rs.getString("calle"));
        p.setNumero(rs.getString("numero"));
        p.setColonia(rs.getString("colonia"));
        p.setCp(rs.getInt("cp"));
        p.setCiudad(rs.getString("ciudad"));
        p.setEstado(rs.getString("estado"));
        p.setTel1(rs.getString("tel1"));
        //p.setEstatus(rs.getInt("estatus"));
        p.setStatus(rs.getInt("estatus"));

        pr.setId(rs.getInt("idproveedor"));
        pr.setRfc(rs.getString("rfc"));
        pr.setRazonSocial(rs.getString("razonSocial"));
        pr.setProveedor(p);

        return pr;
    }

}
