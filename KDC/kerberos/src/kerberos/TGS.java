package kerberos;

import java.net.*;
import java.io.*;

public class TGS {
	public String serverid = "777777777";
	public String serverkey = "8877665544332211";
	public String tgskey = "8765432187654321";
	public String sessionkeyclientserver;
	public String result;
	public String tgsid = "888888888";
	public time a = new time();
	
	ServerSocket TGSsocket;

	public TGS() {
		sessiongeneration aeskey = new sessiongeneration();
		sessionkeyclientserver = aeskey.aeskey();
		
		try {
			TGSsocket = new ServerSocket(9101);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void answer(){
			try {
				//ServerSocket TGSsocket = new ServerSocket(9101);
			
					Socket TGS = TGSsocket.accept();
					DataInputStream recevie = new DataInputStream(TGS.getInputStream());
					String stringfromclient = recevie.readUTF();
					String unprocess[] = stringfromclient.split("@");
					String serveridfromclient = unprocess[0];
					String tickettgs = unprocess[1];
					String auth = unprocess[2];
					AES aes = new AES();
					String tickettgsprocess = aes.Decrypt(tickettgs, tgskey);
					String split[] = tickettgsprocess.split("@");
					String sessionkeytgsclient = split[0];
					String clientidfromAS = split[1];
					System.out.println("clientidfromAS>>>>>>>"+clientidfromAS);
					String TGSidfromAS = split[2];
					String clientidfromclient = aes.Decrypt(auth, sessionkeytgsclient);
					System.out.println("clientidfromclient>>>>>>"+clientidfromclient);
					if(clientidfromAS.equals(clientidfromclient)&&TGSidfromAS.equals(tgsid)&&serveridfromclient.equals(serverid)){
						String ticketserverunprocess = sessionkeyclientserver + "@" + clientidfromclient + "@" + serverid;
						String ticketserver = aes.Encrypt(ticketserverunprocess, serverkey);
						String transmitunprocess = sessionkeyclientserver + "@" + serverid + "@" + ticketserver;
						result = aes.Encrypt(transmitunprocess, sessionkeytgsclient);
					}else{
						result = "unauthorized client";
					}
					DataOutputStream output = new DataOutputStream(TGS.getOutputStream());
					output.writeUTF(result);
					TGS.close();
			
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
	}
}
