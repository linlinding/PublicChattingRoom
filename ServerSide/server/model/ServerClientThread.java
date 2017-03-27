package com.trust.server.model;

import java.net.*;
import java.util.Iterator;
import java.util.Set;


import java.io.*;


import com.trust.common.*;
import com.trust.serverCrypto.*;


import java.awt.*;



public class ServerClientThread extends Thread{
	Socket clientSocket;
	String sessionkey;
	
	public ServerClientThread(Socket s,String sessionkey){
		this.clientSocket = s;
		this.sessionkey=sessionkey;
	}
	
	

	public void run(){//new thread
		while(true){//receive the message which comes from client
			try{
				System.out.println("receive message thread started");
				
				ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
				Message m = (Message)ois.readObject();
				
				System.out.println("SCThread receive: " + m.getSender() + "    says:   " + m.getContext());
			
				String plaintext=AES.Decrypt(m.getContext(),sessionkey);
				System.out.println("plaintext to server>>>>>>"+plaintext );
				
				//forward the message to the receiver
				String sender = m.getSender();
				String receiver = "";
				
				System.out.println("sct message sender: " + sender + ",  sct receiver:" + "null.");
				
				Set<String> set = ManageClientThread.getKeySets();
				System.out.println("scthread key set: " + set);
				
				Iterator<String> it = set.iterator();
				while(it.hasNext()){//get the receiver name2 clients at present
					String cur = it.next();
					if(!cur.equals(sender)){ 
						System.out.println("test cur: " + cur);
						receiver = cur;
						System.out.println("the target user: " + receiver);
						
						ServerClientThread  sct = ManageClientThread.getClientThread(receiver);//get the receiver's thread
						
						//forward message to the receiver from the sender
						ObjectOutputStream oos = new ObjectOutputStream(sct.clientSocket.getOutputStream());
						oos.writeObject(m);
					}
				}
				
				
				
				
			} catch (Exception e){
				e.printStackTrace();
			}
			
		}
	}
}
