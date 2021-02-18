package org.lila_systems.mypet.controller;

import org.lila_systems.mypet.db.ConexionMySQL;
import org.lila_systems.mypet.model.Cliente;
import org.lila_systems.mypet.model.Persona;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ControllerCliente {

    public int insert(Cliente c) throws Exception {
        //Definimos la instruccion  SQL dentro de un String Java:
        String sql = "{call insertCliente(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        //Nos conectamos a la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        //Utilizando la conexion con MySQL, creamos un objecto que nos permita invocar
        //el Stored Procedure que hara la insercion en las tablas producto y mercancia
        CallableStatement cstmt = conn.prepareCall(sql);

        //Llenamos los datos del producto de acuerdo con los parametros que pide el Stored Procedure
        cstmt.setString(1, c.getPersona().getNombre());
        cstmt.setString(2, c.getPersona().getApellidoPaterno());
        cstmt.setString(3, c.getPersona().getApellidoMaterno());
        cstmt.setString(4, c.getPersona().getFechaNacimiento());
        cstmt.setString(5, c.getPersona().getCalle());
        cstmt.setString(6, c.getPersona().getNumero());
        cstmt.setString(7, c.getPersona().getColonia());
        cstmt.setInt(8, c.getPersona().getCp());
        cstmt.setString(9, c.getPersona().getCiudad());
        cstmt.setString(10, c.getPersona().getEstado());
        cstmt.setString(11, c.getPersona().getTel1());
        cstmt.setString(12, c.getPersona().getTel2());

        cstmt.setString(13, c.getCorreo());
        cstmt.setString(14, c.getContrasenia());

        //rEGISTRAMOS LOS PARAMETROS DE SALIDA:
        cstmt.registerOutParameter(15, Types.INTEGER);
        cstmt.registerOutParameter(16, Types.INTEGER);

        //Ejecutamos la consulta:
        cstmt.execute();

        //Recuperamos los ID's generados:
        c.getPersona().setId(cstmt.getInt(15));
        c.setId(cstmt.getInt(16));

        //Cerramos los objectos
        cstmt.close();
        connMySQL.close();

        //Devolvemos el ID de la Mercancia generado:
        return c.getId();

    }

    public void update(Cliente c) throws Exception {

        String sql = "{call updateCliente(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);

        //Lenamos los datos del Producto de acuerdo con los paremtros que pide
        //el Stored Procedure:
        cstmt.setInt(1, c.getPersona().getId());
        cstmt.setString(2, c.getPersona().getNombre());
        cstmt.setString(3, c.getPersona().getApellidoPaterno());
        cstmt.setString(4, c.getPersona().getApellidoMaterno());
        cstmt.setString(5, c.getPersona().getFechaNacimiento());
        cstmt.setString(6, c.getPersona().getCalle());
        cstmt.setString(7, c.getPersona().getNumero());
        cstmt.setString(8, c.getPersona().getColonia());
        cstmt.setInt(9, c.getPersona().getCp());
        cstmt.setString(10, c.getPersona().getCiudad());
        cstmt.setString(11, c.getPersona().getEstado());
        cstmt.setString(12, c.getPersona().getTel1());
        cstmt.setString(13, c.getPersona().getTel2());

        cstmt.setString(14, c.getCorreo());
        cstmt.setString(15, c.getContrasenia());
        
        //Ejecutamos la consulta
        cstmt.execute();
        
        //cerramos los objectos BD:
        
        cstmt.close();
        connMySQL.close();
        
    }
    
    public void delete(int id) throws Exception{
        //Definimos la consulta SQL: 
        String sql = "UPDATE persona SET estatus = 0 WHERE idPersona = " + id;
        //Generamos un objecto de Conexion:
        ConexionMySQL connMySQL = new ConexionMySQL();
        //Abrimos la conexion con MySQL:
        Connection conn = connMySQL.open();
        //Generamos un objecto que nos permite ejecutar la sentencia SQL:
        Statement stmt = conn.createStatement();
        //Ejecutamos la instruccion SQL:
        stmt.execute(sql);
        // Cerramos los objectos de conexion con la BD:
        stmt.close();        
        connMySQL.close();
    }
    
    public Cliente findById(int id) throws Exception {
        return null;

    }
    
    /**
     * Este metodo sirve para generar un objecto de tipo Mercancia
     * a partir de un objecto ResultSet en la posicion que tenga actualmente
     * indicada
     * @param rs
     * @return
     * @throws Exception 
     */
    public Cliente fill(ResultSet rs) throws Exception{
        Cliente c = new Cliente();
        Persona p =new Persona();
        
        //Llenamos los datos de Cliente:
        p.setId(rs.getInt("idPersona")); 
        p.setNombre(rs.getString("nombre"));
        p.setApellidoPaterno(rs.getString("apellidoPaterno"));
        p.setApellidoMaterno(rs.getString("apellidoMaterno"));
        p.setFechaNacimiento(rs.getString("fechaNacimiento"));
        p.setEstado(rs.getString("estado"));
        p.setCalle(rs.getString("calle"));
        p.setColonia(rs.getString("colonia"));
        p.setCiudad(rs.getString("ciudad"));
        p.setCp(rs.getInt("cp"));
        p.setTel1(rs.getString("tel1"));
        p.setTel2(rs.getString("tel2"));
        //p.setEstatus(rs.getInt("estatus"));
        p.setStatus(rs.getInt("status"));
        
        
        c.setId(rs.getInt("idCliente"));
        c.setContrasenia(rs.getString("contrasenia"));
        c.setCorreo(rs.getString("correo"));
        c.setPersona(p);
        
        return c;
        
        
    }
    
    public List<Cliente> getAll(String filtro) throws Exception{
        //Definimos la consulta SQL:
        String sql = "select * from persona inner join cliente on persona.idPersona=cliente.idPersona;";
        //Abrimos una conexion con MySQL:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        //Generamos un objecto que nos permita ejecutar la consulta:  
        // de manera segura, a diferencia de un Statement:
        PreparedStatement pstmt = conn.prepareStatement(sql);        
        //Ejecutamos la consulta y obtenemos sus resultados:
        ResultSet rs = pstmt.executeQuery();
        
        //Declarmos una lista dinamica para guardar los objectos que
        // generaremos al recorrer los resultados devuletos por la BD:
        List<Cliente> clientes = new ArrayList<>();
        
        Cliente c = null;
        
        //Recurremos el conjunto de registros devuletos por BD:        
        while(rs.next()){
            //Obtnemos un objecto de tipo Mercancia con los datos que se 
            //encuentran en el registro actual, devuleto por la BD:
            c=fill(rs);
            
            //Agregamos el objecto a la lista:
            clientes.add(c);
        }
        
        //Devolvemos la lista de objectos consultados:
        return clientes;
        
        
    }
}
