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
import org.lila_systems.mypet.model.Persona;
import org.lila_systems.mypet.model.Empleado;

public class ControllerEmpleado {
    
    public int insert(Empleado e1) throws Exception {
        //Definimos las instrucciones SQL dentro de un String JAVA
        String sql = "{call insertEmpleado(?,?,?,?,?,?,?,?,?,?,?,?,"
                                        + "?,?,"
                                        + "?,?)}";
        //Nos conectamos a la BD
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        //Creamos un objeto con la conexion de MYsql
        //Invocamos el stored procedure que hara la insercion de las tablas
        // persona y empleado
        CallableStatement cstmt = conn.prepareCall(sql);
        //Llenamos los datos del producto de acuerdo  con los productos que pide
        //el Stored Procedure
        cstmt.setString(1, e1.getEmpleado().getNombre());
        cstmt.setString(2, e1.getEmpleado().getApellidoPaterno());
        cstmt.setString(3, e1.getEmpleado().getApellidoMaterno());
        cstmt.setString(4, e1.getEmpleado().getFechaNacimiento());
        cstmt.setString(5, e1.getEmpleado().getCalle());
        cstmt.setString(6, e1.getEmpleado().getNumero());
        cstmt.setString(7, e1.getEmpleado().getColonia());
        cstmt.setInt(8, e1.getEmpleado().getCp());
        cstmt.setString(9, e1.getEmpleado().getCiudad());
        cstmt.setString(10, e1.getEmpleado().getEstado());
        cstmt.setString(11, e1.getEmpleado().getTel1());
        cstmt.setString(12, e1.getEmpleado().getTel2());
        
        cstmt.setString(13, e1.getCorreo());
        cstmt.setString(14, e1.getContrasenia());
        
        //Registramos los parametros de Salida
        cstmt.registerOutParameter(15, Types.INTEGER);
        cstmt.registerOutParameter(16, Types.INTEGER);
        //Ejecutamos la consulta
        cstmt.execute();
        //Recuperamos los ID´s generados
        e1.getEmpleado().setId(cstmt.getInt(15));
        e1.setId(cstmt.getInt(16));
        
        cstmt.close();
        connMySQL.close();
        
        return e1.getId();
    }
    
    public void update(Empleado e1) throws Exception{
        String sql = "{call updateEmpleado(?,?," //2
                                        + "?,?,?,?,?,?,?,?,?,?,?,"//11
                                        + "?,?)}";//2
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);
        
        //Llenamos los datos del Prodcuto de acuerdo con los parámetros que pide
        //el Stored procedure
        cstmt.setInt(1, e1.getEmpleado().getId());
        //cstmt.setInt(2, e1.getId());
        
        cstmt.setString(2, e1.getEmpleado().getNombre());
        cstmt.setString(3, e1.getEmpleado().getApellidoPaterno());
        cstmt.setString(4, e1.getEmpleado().getApellidoMaterno());
        cstmt.setString(5, e1.getEmpleado().getFechaNacimiento());
        cstmt.setString(6, e1.getEmpleado().getCalle());
        cstmt.setString(7, e1.getEmpleado().getNumero());
        cstmt.setString(8, e1.getEmpleado().getColonia());
        cstmt.setInt(9, e1.getEmpleado().getCp());
        cstmt.setString(10, e1.getEmpleado().getCiudad());
        cstmt.setString(11, e1.getEmpleado().getEstado());
        cstmt.setString(12, e1.getEmpleado().getTel1());
        cstmt.setString(13, e1.getEmpleado().getTel2());
        
        cstmt.setString(14, e1.getCorreo());
        cstmt.setString(15, e1.getContrasenia());
        
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
    
    public Empleado findById(int id) throws Exception {
        return null;
    }
    
    public List<Empleado> getAll(String filtro) throws Exception {
        //Definimos la consulta SQL
        String sql = "SELECT * FROM v_empleado_persona";
        
        //Abrimos una conexion con MySQL
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        //Generamos un objeto que nos permita ejecutar la consulta
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        //Ejecutamos la consulta y obtenemos sus resultados 
        ResultSet rs = pstmt.executeQuery();
        
        //Creamos una lista dinamica para guardar los objetos que generamos
        //al recorrer los resultados devueltos por la BD 
        List<Empleado> empleados = new ArrayList<>();
        
        Empleado e1 = null;
        
        //Recorremos el conjunto de registros devueltos por la BD
        while (rs.next()){
            
            //Obtenemos un objeto de tipo Mercancía con los datos que se 
            //encuentran en el registro actual, devuelto por la BD:
            e1 = fill(rs);
                        
            //agregamos el objeto a la lista:
            empleados.add(e1);
        }
        //Devolvemos la lista de objetos consultados:
        return empleados;
    }
    
    /**
     * Este método sirve para generar un objeto de tipo mercancia a partir de un objeto
     * Resulset en la posicion que tenga actualmente indicada.
     * @param rs
     * @return
     * @throws Exception 
     */
    public Empleado fill(ResultSet rs) throws Exception {
        Empleado e1 = new Empleado();
        Persona p = new Persona();
        
        //Llenamos los datos de Persona 
        p.setId(rs.getInt("idPersona"));
        p.setNombre(rs.getString("nombre"));
        p.setApellidoPaterno(rs.getString("apellidoPaterno"));
        p.setApellidoMaterno(rs.getString("apellidoMaterno"));
        p.setFechaNacimiento(rs.getString("fechaNacimiento"));
        p.setCalle(rs.getString("calle"));
        p.setNumero(rs.getString("numero"));
        p.setColonia(rs.getString("colonia"));
        p.setCp(rs.getInt("cp"));
        p.setCiudad(rs.getString("ciudad"));
        p.setEstado(rs.getString("estado"));
        p.setTel1(rs.getString("tel1"));
        p.setTel2(rs.getString("tel2"));
        p.setStatus(rs.getInt("estatus"));
        
        e1.setId(rs.getInt("idEmpleado"));
        e1.setCorreo(rs.getString("correo"));
        e1.setContrasenia(rs.getString("contrasenia"));        
        e1.setEmpleado(p);
        
        return e1;
    }
    
}