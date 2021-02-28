/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pijner.dockerdemo.connector;

import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Prahar
 */
@SessionScoped
@Named("bean")
public class Bean implements Serializable {
    ArrayList<String> names;
    DBConnector dbc = null;

    public Bean() {
        dbc = new DBConnector();
        names = dbc.getNames();
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }
    
}
