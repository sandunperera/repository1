

package dao;

import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class CommonDao {
    
    public static void insert(Object object) throws DaoException{
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            session.save(object);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
            System.out.println("Cann't Insert");
        
    
    
    DaoException exception = new DaoException();
    exception.setMessage("Backend Error -" + e.getMessage());
    
    throw exception;
        }
}
    
    public static ObservableList select(String nameQuery){
        
        ObservableList obList = FXCollections.observableArrayList();
        List list = null;
         Session session = HibernateUtil.getSessionFactory().openSession();
         Transaction transaction = null;
     
     try {         
         transaction = session.beginTransaction();
         Query query = session.getNamedQuery(nameQuery);
         list = (List)query.list();    
         
          for (Object element : list) { obList.add(element); }
     } catch (HibernateException e) {
         System.out.println(e.getMessage());         
         if(transaction !=null){
         transaction.rollback();
         }
     }
     finally{
         session.close();
     }
        return obList;
     }
    
    public static ObservableList select(String nameQuery, HashMap properties){
         
        ObservableList obList = FXCollections.observableArrayList(); 
        List list = null;
         Session session = HibernateUtil.getSessionFactory().openSession();
         Transaction transaction = null;
     
     try {
         
         transaction = session.beginTransaction();
         Query query = session.getNamedQuery(nameQuery).setProperties(properties);
         
         list = (List)query.list();  
          for (Object element : list) { obList.add(element); }
          
     } catch (HibernateException e) {
         System.out.println(e.getMessage());
         
         if(transaction !=null){
         transaction.rollback();
         }
     }
     finally{
         session.close();
     }
        return obList;
     } 
    
    public static void update(Object object){
    
     Session session = HibernateUtil.getSessionFactory().openSession();
     Transaction transaction = null;
     
     try {
        transaction = session.beginTransaction();
        session.update(object);
        transaction.commit();
        
     } catch (HibernateException e) {
         System.out.println(e.getMessage());
         if(transaction !=null){
         transaction.rollback();
         }
     }    
    }
    
    public static void delete(Object object){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
        transaction = session.beginTransaction();
        session.delete(object);
        transaction.commit();
        
     } catch (HibernateException e) {
         System.out.println(e.getMessage());
            if(transaction !=null){
               transaction.rollback();
            }
        }
    }
    
}
