package com.trust.server.database;

import java.sql.*;


public class DataBaseBackground {
	
	//define components for sql server
		PreparedStatement ps=null;
		Connection ct=null;
		ResultSet rs=null;
		
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			//DataBaseBackground test=new DataBaseBackground();
		}
		
		
	public  DataBaseBackground(){
		try{
			//set driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			ct=DriverManager.getConnection
					("jdbc:sqlserver://127.0.0.1:1433;databaseName=ChatUserManager;integratedSecurity=true");
			ps=ct.prepareStatement("select * from UserInfo");
			rs=ps.executeQuery();
			
			while(rs.next())
			{
				System.out.println(rs.getString(1));
				System.out.println(rs.getString(2));
				
			}
						
		}catch(Exception e){
			e.printStackTrace();
		}finally{
							
			try {
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				if(ct!=null)
					ct.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
	}
	
	public boolean insertDB (String username, String password, String iconPath)
	{
		boolean authFlag=false;
		try{
			//set driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			ct=DriverManager.getConnection
					("jdbc:sqlserver://127.0.0.1:1433;databaseName=ChatUserManager;integratedSecurity=true");
			ps=ct.prepareStatement("select * from UserInfo where username='"+username+"'");
			rs=ps.executeQuery();
			boolean flag=rs.next();
			if(flag)
			{
				System.out.println("same name has been found");
			}
			else
			{		ps=ct.prepareStatement("insert into UserInfo values ('"+username+"','"+password+"','"+iconPath+"')");			
					int insertflag=ps.executeUpdate();
					if(insertflag!=0)
					{
						authFlag=true;
					}
					System.out.println("complete insert!!!");
			}				
		}catch(Exception e){
			e.printStackTrace();
		}finally{
							
			try {
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				if(ct!=null)
					ct.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return authFlag;
	}
	
	public boolean queryDB(String username, String password)
	{
		boolean authFlag=false;
		try{
			//set driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			ct=DriverManager.getConnection
					("jdbc:sqlserver://127.0.0.1:1433;databaseName=ChatUserManager;integratedSecurity=true");
			ps=ct.prepareStatement("select * from UserInfo where userName='"+username+"' and pwd='"+password+"'");
			rs=ps.executeQuery();

			//rs.next();
			boolean flag=rs.next();
			if(flag)
			{
				if(rs.getString(1).equals(username)&&rs.getString(2).equals(password))
				{
					authFlag=true;
				}
				else
					authFlag=false;
			}
			
							
		}catch(Exception e){
			e.printStackTrace();
		}finally{
							
			try {
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				if(ct!=null)
					ct.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return authFlag;
	}
	
}
