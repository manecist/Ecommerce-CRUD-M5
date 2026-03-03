package com.bootcamp.configuracion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DatabaseConnection
 * Clase para conectarse a base de datos mysql
 */
public class DataBaseConnection {
    // uso de singleton, usar solo 1 instancia en el objeto
    /* instance */
    private static DataBaseConnection instance;
    /* url */
    private String url;
    /* user */
    private String user;
    /* password */
    private String password;
    /* driver */
    private String driver;

    /**
     * Establece el objeto de conexion para conectarse a la base de datos
     */
    private DataBaseConnection(){
        loadProperties();
        loadDriver();
    }

    /**
     * Carga las propiedades
     */
    private void loadProperties(){
        try (InputStream input= getClass()
                .getClassLoader()
                .getResourceAsStream("database.properties")){

            // si es nulo manda error
            if ( input == null){
                throw new RuntimeException("No se encontró el archivo database.properties");
            }

            Properties props = new Properties();
            props.load(input);

            // creo el constructor en try en base a mis propiedades de database.properties
            this.url = props.getProperty("db.url");
            this.user = props.getProperty("db.user");
            this.password = props.getProperty("db.password");
            this.driver = props.getProperty("db.driver");

        }catch (IOException e){
            throw new RuntimeException("Error cargado database.properties");
        }
    }

    /**
     * Carga driver MySQL
     */
    private void loadDriver(){
        try{
            Class.forName(driver);
        }catch (ClassNotFoundException e){
            throw new RuntimeException("No se pudo cargar el driver: " + driver, e);
        }
    }

    /**
     * Genera unica instancia del objeto de conexion
     * @return
     */
    public static synchronized DataBaseConnection getInstance(){
        if (instance == null){
            instance = new DataBaseConnection(); // si es nulo crea la database la conexión
        }
        return instance;
    }

    /**
     * establece un puente de comunicación entre tu programa de Java y base de datos
     * @return pasando url, user, password
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url,user,password);
    }

}
