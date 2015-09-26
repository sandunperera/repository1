/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import entity.Employee;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Chinthaka
 */
public class EmployeeDao {
    
    public static void add(Employee employee) throws DaoException{
       CommonDao.insert(employee);
    }
    
     public static ObservableList getAll(){ return CommonDao.select("Employee.findAll"); } 
    
    public static Employee getByName(String name){
        HashMap hmap = new HashMap();
        hmap.put("name", name);
        ObservableList list =CommonDao.select("Employee.findByName",hmap);
        
         if(list.size()!=0) return(Employee) list.get(0);
          else
              return null;
    }
    
     public static Employee getById(int id){
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        ObservableList list =CommonDao.select("Employee.findById",hmap);
        
         if(list.size()!=0) return(Employee) list.get(0);
          else
              return null;
    }
    
      public static Employee getByNic(String nic){
        HashMap hmap = new HashMap();
        hmap.put("nic", nic);
        ObservableList list =CommonDao.select("Employee.findByNic",hmap);
        
         if(list.size()!=0) return(Employee) list.get(0);
          else
              return null;
    }
    
}
