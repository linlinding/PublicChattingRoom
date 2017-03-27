package com.trust.serverCrypto;

import java.io.DataInputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	public String serverid = "777777777";
	public String serverkey = "8877665544332211";
	ServerSocket Serversocket;
	
/*	public void run(){
		while(true){
			String sessionkey=answer();
			System.out.println(sessionkey);
		}		
	}*/
	
	public Server() {
		try {
			Serversocket = new ServerSocket(9102);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String answer()
	{
		String result = null;
		String sessionkeyserverclient = null;
			
			try {
				//ServerSocket Serversocket = new ServerSocket(9102);
				Socket Server = Serversocket.accept();
				DataInputStream recevie = new DataInputStream(Server.getInputStream());
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
				DataOutputStream output = new DataOutputStream(Server.getOutputStream());
				output.writeUTF(result);
				Server.close();
			 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
			
			 return sessionkeyserverclient;
		}
		

	

}
