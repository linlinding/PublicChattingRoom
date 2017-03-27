package com.trust.clientCrypto;

import java.net.*;

import kerberos.AES;

import java.io.*;

public class Client {
	public String clientid;
	public String tgsid;
	public String serverid;
	public String clientkey = "1234567812345678";
	public String result;
	public String error = "unauthorized client";
	public Socket client2ServerSocket;
	public String begintime;
	public time a = new time();
	
	
	public Client(String userid, String tgsid, String serverid,Socket client2ServerSocket) {
		this.clientid = userid;
		this.tgsid = tgsid;
		this.serverid = serverid;
		this.client2ServerSocket=client2ServerSocket;
		begintime = a.begintime();
	}
	
	public String requestAS(){
			try {
				Socket Client = new Socket(InetAddress.getLocalHost(),9100);
				DataOutputStream output = new DataOutputStream(Client.getOutputStream());
				String transmit = clientid + "@" + tgsid + "@" + begintime;
				output.writeUTF(transmit);
				DataInputStream recevie = new DataInputStream(Client.getInputStream());
				String stringfromAS = recevie.readUTF();
				if (stringfromAS.equals(error)){
					result = error;
				}else{
					AES aes = new AES();
					result = aes.Decrypt(stringfromAS, clientkey);
				}
				Client.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
	}
	
	public String requestTGS(String fromAS){
		try {
			String unprocess[] = fromAS.split("@");
			String sessionkeytgsclient = unprocess[0];
			String tgsidfromAS = unprocess[1];
			String tickettgs = unprocess [2];
			AES aes = new AES();
			String authunprocess = clientid + "@" + InetAddress.getLocalHost() + "@" + begintime;
			String auth = aes.Encrypt(clientid, sessionkeytgsclient);
			String transmit = serverid + "@" + tickettgs + "@" + auth;
			Socket Client = new Socket(InetAddress.getLocalHost(),9101);
			DataOutputStream output = new DataOutputStream(Client.getOutputStream());
			if(!tgsidfromAS.equals(tgsid)){
				result = "unauthorized AS";
			}else{
				output.writeUTF(transmit);
				DataInputStream recevie = new DataInputStream(Client.getInputStream());
				String stringfromTGS = recevie.readUTF();
				if(!stringfromTGS.equals(error)){
					result = aes.Decrypt(stringfromTGS, sessionkeytgsclient);
					System.out.println("message from TGS to Client(sessionkey+serverid+ticket)"+result);
				}else {
					result = error;
				}
			}
			Client.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public String requestServer(String fromTGS) {
		try {
			String unprocess[] = fromTGS.split("@");
			String sessionkeyserverclient = unprocess[0];
			String serveridfromtgs = unprocess[1];
			String ticketserver = unprocess[2];
			if(serveridfromtgs.equals(serverid) ){
				AES aes = new AES();
				String auth = aes.Encrypt(clientid, sessionkeyserverclient);
				String transmit = ticketserver + "@" + auth;
				//change port num to 9898
				//Socket Client = new Socket(InetAddress.getLocalHost(),9898);
				DataOutputStream output = new DataOutputStream(client2ServerSocket.getOutputStream());
				output.writeUTF(transmit);
				System.out.println("client sends request sessionkey to server");
				DataInputStream recevie = new DataInputStream(client2ServerSocket.getInputStream());
				String stringfromServer = recevie.readUTF();
				//client2ServerSocket.close();
				if(stringfromServer.equals(error)) {
					result = error;
				}else { 
					String serveridfromserver = aes.Decrypt(stringfromServer, sessionkeyserverclient);
					if(serveridfromserver.equals(serverid)){
						result = sessionkeyserverclient;
						System.out.println("message from server to client(sessionkey)"+result);
					}else {
						result = "unauthorized server";
					}
				}
			}else{
				result = "unauthorized TGS";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
}
