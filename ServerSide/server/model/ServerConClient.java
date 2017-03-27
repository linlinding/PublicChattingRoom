package com.trust.server.model;

import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.net.*;
import java.awt.image.BufferedImage;
import java.io.*;
import com.trust.common.*;
import com.trust.server.database.*;
import com.trust.server.view.ServerMainView;
import com.trust.serverCrypto.AES;
import com.trust.serverCrypto.Server;


public class ServerConClient extends Thread{
	ServerSocket serverSocket;
	Socket server2ClientSocket;
	Account account;
	BufferedImage image;
	String sessionKey;
	
	public String serverid = "777777777";
	public String serverkey = "8877665544332211";
	ServerSocket Serversocket;
	
	public void run()
	{	
		//9898 port for listening 
		startServer();
		serverCon2Client();
	}	
	
	
		
	//start Server
	public void startServer()
	{
		try {
			serverSocket=new ServerSocket(9898);	
			System.out.println("start server");
			} catch (Exception e) {
				e.printStackTrace();
			}  	
	}
	
	
	
	//Server communicate to Client
	public void serverCon2Client()
	{
		try{
			while(true)
			{			
				server2ClientSocket=serverSocket.accept();
				DataInputStream requestFlag=new DataInputStream(server2ClientSocket.getInputStream());
				String requestTypt=requestFlag.readUTF();
				
				if(requestTypt.equals("signup"))
				{
					//sign up
					//thread
					//obtain account object from client
					ObjectInputStream obInput=new ObjectInputStream(server2ClientSocket.getInputStream());
					System.out.println("obtain object input stream");
					account=(Account)obInput.readObject();
					System.out.println("obtain account object");
				
					Message replyMessage=new Message();
					
					if(account.getTag().equals("new"))
					{
						String userName=account.getUserId();
						String userPassword=account.getPassword();
						String userIconPath=account.getUserIcon();
						String sql="insert into UserInfo values('"+userName+"','"+userPassword+"','"+userIconPath+"')";
						DataBaseBackground dbBack=new DataBaseBackground();	
						boolean insertFlag=dbBack.insertDB(userName, userPassword,userIconPath);
						//System.out.println("complete insert at server side");
						if(insertFlag)
						{
							try{			
							
							/////!!!!!!!!
							System.out.println(ServerMainView.createTableFlag);
							ServerMainView.createTableFlag=true;
							System.out.println(ServerMainView.createTableFlag);	
							
							replyMessage.setMessageType("2");
							System.out.println("insert successfully");
							/////////////////
							ObjectOutputStream obOutput=new ObjectOutputStream(server2ClientSocket.getOutputStream());
							obOutput.writeObject(replyMessage);
							
							//read image and store it at local
							System.out.println("server starts reading image io");
							image=ImageIO.read(server2ClientSocket.getInputStream());
							//image = new BufferedImage(1, 1, 1);
							System.out.println("server read image stream from client");
							File file=new File(userIconPath);
							file.createNewFile();
							ImageIO.write(image, "png", file);
							System.out.println("complete write file to server");
							
							}catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							replyMessage.setMessageType("3");
							System.out.println("insert fail!!!");
							ObjectOutputStream obOutput=new ObjectOutputStream(server2ClientSocket.getOutputStream());
							obOutput.writeObject(replyMessage);
						}
						
					/*	ObjectOutputStream obOutput=new ObjectOutputStream(server2ClientSocket.getOutputStream());
						obOutput.writeObject(replyMessage);*/
						//close socket to client
						closeServerSocket();
					}
					
				}
				else if (requestTypt.equals("login"))
				{		
					sessionKey=answer(server2ClientSocket);
				//	System.out.println("sessionKey>>>>>"+sessionKey);
					
					
				    //thread
					//obtain account object from client
					ObjectInputStream obInput=new ObjectInputStream(server2ClientSocket.getInputStream());
					//System.out.println("obtain object input stream");
					account=(Account)obInput.readObject();
					//System.out.println("obtain account object");
				
					Message replyMessage=new Message();
					
					AES aes = new AES();
					String decryptedTag=aes.Decrypt(account.getTag(),sessionKey);
					System.out.println(decryptedTag);
					account.setTag(decryptedTag);
					String decryptedName=aes.Decrypt(account.getUserId(),sessionKey);
					System.out.println(decryptedName);
					account.setUserId(decryptedName);
					String decryptedPwd=aes.Decrypt(account.getPassword(),sessionKey);
					System.out.println(decryptedPwd);
					account.setPassword(decryptedPwd);
					String decryptedImgPath=aes.Decrypt(account.getUserIcon(),sessionKey);
					System.out.println(decryptedImgPath);
					account.setUserIcon(decryptedImgPath);
					System.out.println("sessionKey to "+decryptedName+">>>>>"+sessionKey);
					
					if(account.getTag().equals("old"))
					{//old user log in
						
						String userName=account.getUserId();
						String userPassword=account.getPassword();
						DataBaseBackground dbBack=new DataBaseBackground();
						boolean queryDB=dbBack.queryDB(userName, userPassword);
						if(queryDB)
						{//log in successfully
							
							replyMessage.setMessageType("1");
							System.out.println("server sends vaild client");
							ObjectOutputStream obOutput=new ObjectOutputStream(server2ClientSocket.getOutputStream());
							obOutput.writeObject(replyMessage);
													
							//After log in successfully, we build a new thread to make this thread keep connecting with the client, 
							//so that we can send message to server from the client.
							
							ServerClientThread scct = new ServerClientThread(server2ClientSocket,sessionKey);
							
							ManageClientThread.addClientThread(userName, scct);//store its username and thread though map in the server
							
							scct.start();
							System.out.println("start a new thread to connect with client");					
						}
						
						else 
						{//log in failed
							
							replyMessage.setMessageType("0");
							System.out.println("server sends invaild client");
							ObjectOutputStream obOutput=new ObjectOutputStream(server2ClientSocket.getOutputStream());
							obOutput.writeObject(replyMessage);
							closeServerSocket();
						}						
						
					}
										
				}
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    //close server
	public void closeServerSocket()
	{
		try {
			server2ClientSocket.close();
			System.out.println("server socket close");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String answer(Socket server2ClientSocket)
	{
		String result = null;
		String sessionkeyserverclient = null;
			
			try {
				//ServerSocket Serversocket = new ServerSocket(9102);
				//Socket Server = Serversocket.accept();
				DataInputStream recevie = new DataInputStream(server2ClientSocket.getInputStream());
				String stringfromclient = recevie.readUTF();
				String unprocess[] = stringfromclient.split("@");
				String ticketserver = unprocess[0];
				String auth = unprocess[1];
				AES aes = new AES();
				String stringfromTGS = aes.Decrypt(ticketserver, serverkey);
				String split[] = stringfromTGS.split("@");
				sessionkeyserverclient = split[0];
				String clientidfromTGS = split[1];
				String serveridfromTGS = split[2];
				String clientidfromclient = aes.Decrypt(auth,sessionkeyserverclient);
				if(clientidfromTGS.equals(clientidfromclient)&&serveridfromTGS.equals(serverid)){
					System.out.println(sessionkeyserverclient);
					result = aes.Encrypt(serverid, sessionkeyserverclient);
				}else{
					result = "unauthorized client";
				}
				DataOutputStream output = new DataOutputStream(server2ClientSocket.getOutputStream());
				output.writeUTF(result);
				//server2ClientSocket.close();
			 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
			
			 return sessionkeyserverclient;
		}
	
		
	
	

}


