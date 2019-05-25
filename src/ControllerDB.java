/*
*       Designed + implemented by Mengseng Soravath
*       Dec 2 2018
*       
*       The controller DB class is implemented to seperate between GUI and Database system  
*       so that the software is more maintainable, reusable, and efficiency.
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author soravath
 */
public class ControllerDB {
    
    //initialize varible 
    private MainBook mainbook; 
    String output;
    
    PreparedStatement p = null;
    Connection con = null;
    ResultSet rs;
    String sql, sqlCustomer, sqlCustomerReturn;
    StringBuilder str = new StringBuilder();
        
    
    
    
   public void setMainBook(MainBook mainbook) {
        this.mainbook = mainbook;
    }
   
   // open connection
    public void connection(){  
        
        try{
            //load the JDBC driver
        Class.forName("org.sqlite.JDBC");
		//Class.forName("C:\Users\soravath\Desktop\BookDataBase\src\sqlite-jdbc-3.20.0");
        }
        catch(Exception e){
            System.out.println("Exception 1 "+ e);//
        }
        //Connection con = null;
        
        try{
            
            // create the connection to the database
            String url = "jdbc:sqlite:C:/Users/soravath/Desktop/BookDataBase/src/Book.db";
            con = DriverManager.getConnection(url);
        }
        catch(SQLException e){
            System.out.println("Exception 2 "+ e.getMessage());
        }
        
             

    }      
    
        
    //close connection
    public void closeConnection(){
        
        try{                        
            con.close();       
        }catch(SQLException e){
              System.out.println("Exception 4"+ e);
            }
    } 
    

   
   //Search by Title
    public void executeQSearchByTitle(){
       
        connection();
        
          
        sql = "select isbn, title, location, publish_year, release_year, "+
              "status from book b where b.title like '%"+ 
               mainbook.TextFieldSearchByTitle() +"%' " ; 
        
        try{
            p = con.prepareStatement(sql);
            p.clearParameters();
            
            rs = p.executeQuery();
            
            //clear string buffer
            str.delete(0,str.length());
                    // display the output
                    output = "ISBN"+"\tTitle" + "\t\tLocation"+"\tPublish Year" + "\tRelease Year" + "\tStatus" +  "\n";
                    str.append(output);
                    output = "===="+"\t=====" + "\t\t========"+"\t======= ====" + "\t======= ====" + "\t======" +  "\n";
                    str.append(output);
                    
                    while(rs.next()){
                                             
                       output = rs.getString(1)+ "\t"+rs.getString(2)+"\t"+ rs.getString(3)+ "\t" + rs.getString(4)+
                               "\t"+ rs.getString(5)+ "\t"+ rs.getString(6)+"\n";  
                       str.append(output);
                    }
                    
                    rs.close();
                    p.close();
                    //con.close();
        }
        catch(SQLException e){
            System.out.println("Exception 3 "+ e);
        }      
    
        
        mainbook.displayText(str.toString());
        //mainbook.displayText(mainbook.TextFieldSearchByTitle());
      closeConnection();  
    }//executeQSearchByTitle
    
    //search by author
    public void executeQSearchByAuthor(){
    connection();
    
    sql = "select b.isbn, title, location, publish_year, release_year,"+
          " status, a.occupation from author a, book b, author_book ab where a.aname = '"
            +mainbook.TextFieldSearchByAuthor()+"' and ab.isbn = b.isbn and ab.aname = a.aname"; 
 
        
        try{
            p = con.prepareStatement(sql);
            p.clearParameters();
            
            rs = p.executeQuery();
            
            //clear string buffer
            str.delete(0,str.length());
            
                    output = "ISBN"+"\tTitle" + "\t\tLocation"+"\tPublish Year" + "\tRelease Year" + "\tStatus" + "\tOccupation" + "\n";
                    str.append(output);
                    output = "===="+"\t=====" + "\t\t========"+"\t======= ====" + "\t======= ====" + "\t======" + "\t==========" + "\n";
                    str.append(output);
                    
                    while(rs.next()){
                                             
                       output = rs.getString(1)+ "\t"+rs.getString(2)+"\t"+ rs.getString(3)+ "\t" + rs.getString(4)+
                               "\t"+ rs.getString(5)+ "\t"+ rs.getString(6)+ "\t"+ rs.getString(7)+"\n";  
                       str.append(output);
                    }
                    
                    rs.close();
                    p.close();
                    //con.close();
        }
        catch(SQLException e){
            System.out.println("Exception 3 "+ e);
        }      
    
        
        mainbook.displayText(str.toString());
    
    closeConnection();  
    }//executeQSearchByAuthor

        //return
    public void executeQReturn() {
         connection();
         
         sql = "update book set status = 'available'where isbn  = " + mainbook.returnBook();

 
        
        try{
            p = con.prepareStatement(sql);
            p.clearParameters();
            
            p.executeUpdate();            
                  
                   //str.append("One book have beed return! ");        
 
                    p.close();
                    
        }
        catch(SQLException e){
            System.out.println("Exception 3 "+ e);
        }
        
    
        sqlCustomerReturn = "delete from borrow \n" +
        "where isbn =" + mainbook.returnBook();

 
        
        try{
            p = con.prepareStatement(sqlCustomerReturn);
            p.clearParameters();
            
            p.executeUpdate();
                   str.delete(0,str.length());
                  
                   str.append("One book has been returned! ");        
 
                    p.close();
                    
        }
        catch(SQLException e){
            System.out.println("Exception 4 "+ e);
        }
        
        mainbook.displayText(str.toString());
         
         closeConnection();  
    }
    
    public void executeQCheckOut() {
         connection();
         
         sql = "update book set status = 'Not available'where isbn =" + mainbook.CheckoutBook();

 
        
        try{
            p = con.prepareStatement(sql);
            p.clearParameters();
            
            p.executeUpdate();
            
                 
                   //str.append("One book have beed Checked out! "); 
                   
                    p.close();
                    //con.close();
        }
        catch(SQLException e){
            System.out.println("Exception 3 "+ e);
        }      
        
        
        sqlCustomer = "insert into borrow values("+mainbook.CheckoutBook()+",'"+mainbook.CustomerName()+"');";
 
                
        try{
            p = con.prepareStatement(sqlCustomer);
            p.clearParameters();
            
            p.executeUpdate();
                   str.delete(0,str.length());
                 
                   str.append("One book has been Checked out! "); 
                   
                    p.close();
                    //con.close();
        }
        catch(SQLException e){
            System.out.println("Exception 4 "+ e);
        }
       
        mainbook.displayText(str.toString());
         
         closeConnection();  
    }
    
    //search customer infor
    public void executeQsearchCustomerInfo(){
        
        connection();         
        sql = "select c.c_name, c.email, c.address, b.title from borrow br, book b, customer c where c.c_name = br.c_name and b.isbn = br.isbn and b.isbn = "+ mainbook.SearchCustomerInfo(); 
        
        try{
            p = con.prepareStatement(sql);
            p.clearParameters();
            
            rs = p.executeQuery();
            
            //clear string buffer
            str.delete(0,str.length());
                
                    //display the output
                    output = "Customer Name" + "\tCustomer email"+"\tCustomer address" +"\t\tBook title" +"\n";
                    str.append(output);
                    output = "======== ====" + "\t\t======== ====="+"\t======== =======" +"\t\t==== =====" +"\n";
                    str.append(output);
                    
                    while(rs.next()){
                                             
                       output =  rs.getString(1) + "\t" + rs.getString(2)+  "\t" + rs.getString(3) +  "\t" + rs.getString(4)+  "\n";  
                       str.append(output);
                    }
                    
                    rs.close();
                    p.close();
                    //con.close();
        }
        catch(SQLException e){
            System.out.println("Exception 3 "+ e);
        }
              
    
       
        mainbook.displayText(str.toString());
         
         closeConnection();
        
    }
    
}
