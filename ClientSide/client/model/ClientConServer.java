package com.trust.client.model;

import java.util.*;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import com.trust.common.*;

public class ClientConServer {
	public static Socket clientSocket;
	BufferedImage image;
	

	
public ClientConServer()
{
	try {
		clientSocket=new Socket("127.0.0.1",9898);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
}



	//send auth request
	public boolean sendLoginInfo2Server(Object account)
	{
		boolean authFlag=false;
		try{
			//System.out.println("begin connection");
			//clientSocket=new Socket("127.0.0.1",9898);
			//System.out.println("send to Server port");
			//create object output stream
		/*	DataOutputStream requestFlag=new DataOutputStream(clientSocket.getOutputStream());
			requestFlag.writeUTF("login");*/
			ObjectOutputStream obOutput=new ObjectOutputStream(clientSocket.getOutputStream());
			obOutput.writeObject(account);
			//System.out.println("client send account object output stream");
			

			
			//create object input stream waiting server's response
			ObjectInputStream obInput=new ObjectInputStream(clientSocket.getInputStream());
			Message authResponse=(Message)obInput.readObject();
			if(authResponse.getMessageType().equals("1"))
			{
				//1 log in succeed
				authFlag=true;
				//System.out.println("authflag is true");
			}
			else if(authResponse.getMessageType().equals("0"))
			{
				//0 log in fail
				obInput.close();   //????
				clientSocket.close();   //warning no close
				System.out.println("Client obtains error message and closes stream");
			}

		}catch(Exception e){
				e.printStackTrace();
		}finally{
			
		}
		return authFlag;		
	}
	
	//send sign up
	//change account to object!!!!
	public boolean sendSignUpInfo(Account account,String localImagePath)
	{
		boolean signUpFlag=false;
		try{
			//clientSocket=new Socket("127.0.0.1",9898);
			DataOutputStream requestFlag=new DataOutputStream(clientSocket.getOutputStream());
			requestFlag.writeUTF("signup");
			ObjectOutputStream obOutput=new ObjectOutputStream(clientSocket.getOutputStream());
			obOutput.writeObject(account);
			System.out.println("has already sent sign up info to server");
						
			//System.out.println("client has sent image to server");
			
			ObjectInputStream obInput=new ObjectInputStream(clientSocket.getInputStream());
			Message signUpResponse=(Message)obInput.readObject();
			System.out.println("client gets response message from server");
						
			if(signUpResponse.getMessageType().equals("2"))
			{
				//2 sign up succeed
				signUpFlag=true;
				System.out.println("authflag is true");
				
				//send image to server
				File imgLocalfile=new File(localImagePath);
				image=ImageIO.read(imgLocalfile);

				ImageIO.write(image, "png", clientSocket.getOutputStream());
			}
			else if (signUpResponse.getMessageType().equals("3"))
			{
				//3 sign up fail
				obInput.close();   //????
				clientSocket.close();   //warning no close
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}		
		return signUpFlag;
	}

}
