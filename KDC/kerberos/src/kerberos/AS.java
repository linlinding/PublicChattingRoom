package kerberos;

import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.*;

public class AS {
	public String clientid ;
	public String tgsid = "888888888";
	public String clientkey = "1234567812345678";
	public String tgskey = "8765432187654321";
	public String sessionkeytgsclient ;
	public String result;
	public String begintime;
	public String endtime;
	public time a = new time();
	
	ServerSocket ASsocket;
	
/*	public static void main(String[] args) {
		boolean clientIdflag=queryClientId("ruirui");
		System.out.println(flag);
		
	}*/
	
	
	
	public AS() {
		sessiongeneration aeskey = new sessiongeneration();
		sessionkeytgsclient = aeskey.aeskey();
		begintime = a.begintime();
		endtime = a.endtime();// TODO Auto-generated constructor stub
		try {
			ASsocket = new ServerSocket(9100);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void answer(){
			try {
				//ServerSocket ASsocket = new ServerSocket(9100);
					
						Socket AS = ASsocket.accept();
						DataInputStream recevie = new DataInputStream(AS.getInputStream());
						String stringfromclient = recevie.readUTF();
						String process[] = stringfromclient.split("@");
						String clientidfromsocket = process[0];
						String tgsidfromsocket = process[1];
						String begintimefromclient = process[2];
						System.out.println(clientidfromsocket);
						boolean clientIdflag=queryClientId(clientidfromsocket);
						//query clientId from Database
						System.out.println(clientIdflag);
						if (tgsidfromsocket.equals(tgsid)&&clientIdflag){
							String tickettgsunprocess = sessionkeytgsclient + "@" + clientidfromsocket + "@" + tgsid;
							AES aes = new AES();
							String tickettgs = aes.Encrypt(tickettgsunprocess, tgskey);
							System.out.println("the TGS ticket in AS is " + tickettgs);
							String resultunprocess = sessionkeytgsclient + "@" + tgsid + "@" + tickettgs;
							result = aes.Encrypt(resultunprocess, clientkey);
						}else{
							result = "unauthorized client";
						}
						System.out.println("the data send to client from AS is " + result);
						DataOutputStream output = new DataOutputStream(AS.getOutputStream());
						output.writeUTF(result);
						AS.close();				
								
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	
	public static boolean queryClientId(String clientId){
		
		boolean authFlag=false;
		//define components for sql server
		PreparedStatement ps=null;
		Connection ct=null;
		ResultSet rs=null;
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			ct=DriverManager.getConnection
					("jdbc:sqlserver://127.0.0.1:1433;databaseName=ChatUserManager;integratedSecurity=true");
			//clientId=username
			ps=ct.prepareStatement("select * from UserInfo where userName='"+clientId+"'");
			rs=ps.executeQuery();
			rs.next();
			if(rs.getString(1).equals(clientId))
			{
				authFlag=true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
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


