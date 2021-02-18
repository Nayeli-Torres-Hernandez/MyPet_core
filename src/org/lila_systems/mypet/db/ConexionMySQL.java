/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lila_systems.mypet.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionMySQL {
    //Objeto gestiona la conexion 
    Connection conn;

    public Connection open() throws Exception {
        //Definimos las credenciales de conexion de la BD
        String usuario = "root";
        String contrasenia = "";
        //Definimos la URL de conexion con MySQL
        String url = "jdbc:mysql://localhost:3307/mypet";
        //Cargamos el Driver de MySQL
        Class.forName("com.mysql.jdbc.Driver");
        //Abrimos la conexion con la BD 
        conn = DriverManager.getConnection(url, usuario, contrasenia);
        //Devolvemos el objeto que gestiona la conexion con MySQL
        return conn;
    }
    
    public void close() {
        try {
            //Cerramos la conexion con la BD
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String [] args){
        try {
            ConexionMySQL connMySQL = new ConexionMySQL();
            connMySQL.open();
            System.out.println("Conexion con MySQL abierta");
            
            connMySQL.close();
            System.out.println("Conexion con MyQSL cerrada");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
