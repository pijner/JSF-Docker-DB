/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pijner.dockerdemo.connector;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

// Imports for mySQL database connector
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 *
 * @author Prahar
 */
public final class DBConnector {
    private final String serverName;
    private final int portNumber;
    private final String user;
    private final String password;
    private final String databaseName;
    private MysqlDataSource dataSource;
    
    
    /**
     * Class constructor - sets up MySQL environment variables and initialized data source
     */
    public DBConnector(){
        this.serverName = "localhost";
        this.portNumber = 3305;
        this.user = "root";
        this.password = "password123";
        this.databaseName = "demoDB";
        connectDataSource();
    }
    
    /**
     * Function to initialize the data source which will be used to connect to the database
     */
    public void connectDataSource(){
        dataSource = new MysqlDataSource();
        
//        dataSource.setServerName(this.serverName);
//        dataSource.setPortNumber(this.portNumber);
//        dataSource.setDatabaseName(this.databaseName);
        
        // Alternate method to connect
         dataSource.setURL("jdbc:mysql://mysqlserver:3306/demoDB?allowPublicKeyRetrieval=true&useSSL=false");

        dataSource.setUser(this.user);
        dataSource.setPassword(this.password);
        System.out.println("Data source initialized");
    }   
    
    /**
     * Function to close result set, statement, and connection. Always call this after executing a query
     * @param rs ResultSet that reads the result of a query
     * @param st Statement that executes the query
     * @param cn Connection obtained from data source
     */
    private void close(ResultSet rs, Statement st, Connection cn){
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (st != null){
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (cn != null){
            try {
                cn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * function to read all the names from the dummy table
     * @return List of strings containing names
     */
    public ArrayList<String> getNames(){
        ArrayList<String> listOfNames = new ArrayList<>();
        
        Connection dbConnection = null;
        Statement dbStatement = null;
        ResultSet result = null;
        
        try {
            dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
            System.out.println("Connection established!");
            
            // Just read the names from the dummy table
            String query = "SELECT name FROM dummy;";
            result = dbStatement.executeQuery(query);
            
            while (result.next()){
                listOfNames.add(result.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(result, dbStatement, dbConnection);
        }
        
        return listOfNames;
    }
}
