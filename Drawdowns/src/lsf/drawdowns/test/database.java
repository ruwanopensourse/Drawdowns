package lsf.drawdowns.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public class database {
	
	String url="jdbc:mysql://localhost:3306/2010.5.20";
    String username="root";
    String password="";
    Connection con=null;
    PreparedStatement pst=null;
    ResultSet set = null;
	
	public void select() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		try{
			try{
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			}catch(Exception e){
				System.out.println(e);
			}
		}catch(Exception e1){
			System.out.println(e1);
		}
		
		//Query and it's connections are include under that
		
		con= (Connection)DriverManager.getConnection(url,username,password);
        String query="SELECT * FROM table1";
        pst= (PreparedStatement) con.prepareStatement(query);
        
        
        set=pst.executeQuery(query);
        JSONArray jsonarray=new JSONArray();
       
        
        int lenght=set.getMetaData().getColumnCount();
        while(set.next()){
        	JSONObject jsonobj=new JSONObject();
        	for(int i=0;i<lenght;i++){
        		try{
        			jsonobj.put(set.getMetaData().getColumnLabel(i+1).toLowerCase(),set.getObject(i+1));
        		}catch(Exception e){
        			System.out.println(e);
        		}
        	}
        	//jsonarray.put(jsonobj);
        	jsonarray.put(jsonobj);
        }
        System.out.println(jsonarray.toString());
	}
    
    
}
