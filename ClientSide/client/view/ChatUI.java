package com.trust.client.view;


import javax.swing.*;

import com.trust.client.model.ClientConServer;
import com.trust.common.Message;

import kerberos.AES;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ChatUI extends JFrame implements ActionListener, Runnable{
	JButton sendButton;
	JButton resetButton;
	JTextArea ReceiveMessage;
	JTextField SendMessage;
	JPanel JpanelNorth;
	JPanel JpanelCenter;
	JPanel JpanelWest;
	String ownerId;
	String friend;
	

	public ChatUI(String ownerId, String friend,String myIconPath){
		
		super("I'm " + ownerId +", I'm chatting online.");
		
		this.ownerId = ownerId;
		this.friend = friend;
		
		sendButton=new JButton("Send");
		sendButton.setPreferredSize(new Dimension(100,40));
		sendButton.addActionListener(this);
		
		resetButton=new JButton("Clear");
		resetButton.setPreferredSize(new Dimension(100,40));
		resetButton.addActionListener(this);
		
		ReceiveMessage = new JTextArea();

		SendMessage = new JTextField(15);
		
		//////////////////////////////////
		ReceiveMessage.setLineWrap(true);
		
		JpanelCenter = new JPanel();
		JpanelCenter.add(SendMessage);
		JpanelCenter.add(sendButton);
		JpanelCenter.add(resetButton);
		
		JpanelWest=new JPanel(new GridLayout(1,4));
		JpanelWest.add(new JLabel(new ImageIcon(myIconPath)));
			
		this.add(ReceiveMessage, "Center");
		this.add(JpanelCenter, "South");
		this.add(JpanelWest,"West");
		this.setIconImage(new ImageIcon(myIconPath).getImage());
		this.setSize(500,350);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		////////////////////////
		this.add(new JScrollPane(ReceiveMessage),"Center");
		
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == resetButton){
			System.out.println("click reset");
			SendMessage.setText(null);
		
		}
		
		if(e.getSource() == sendButton){
			
			System.out.println("click send");
			Message m = new Message();
			/*m.setSender(ownerId);
			m.setGetter(friend);
			m.setContext(SendMessage.getText());*/
			
			// message encryption
			AES aes = new AES();
			try {
				m.setSender(AES.Encrypt(ownerId,ClientLoginUI.sessionkey));
				m.setGetter(AES.Encrypt(friend,ClientLoginUI.sessionkey));
				m.setContext(AES.Encrypt(SendMessage.getText(),ClientLoginUI.sessionkey));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			this.ReceiveMessage.append("\t\t" + "I say: " + SendMessage.getText() + "\r\n");
			//ReceiveMessage.setHorizontalAlignment(JTextField.RIGHT);
			System.out.println("chatUI send: " + ownerId + ": " + SendMessage.getText());
			
			try{
				ObjectOutputStream oos = new ObjectOutputStream(ClientConServer.clientSocket.getOutputStream());
				oos.writeObject(m);
				System.out.println("sended encrypted message>>>>>"+m.getContext());
			}catch(Exception a){
				a.printStackTrace();
			}
			
			
			
		}
		
	}
	
	public void run(){
		while(true){
			try{
				//keep reading the message which comes from the sender's ServerClientThread
				ObjectInputStream ois = new ObjectInputStream(ClientConServer.clientSocket.getInputStream());
				
				Message m = (Message)ois.readObject();
				//Decryption message
				System.out.println("readMessage>>>>>"+m.getContext());
				m.setSender(AES.Decrypt(m.getSender(),ClientLoginUI.sessionkey));
				System.out.println("decrpted sender"+m.getSender());
				m.setGetter(AES.Decrypt(m.getGetter(),ClientLoginUI.sessionkey));
				System.out.println("decrpted getter"+m.getGetter());
				m.setContext(AES.Decrypt(m.getContext(),ClientLoginUI.sessionkey));
				System.out.println("plainText>>>>>>>"+m.getContext());
				System.out.println("ownerId>>>>>>>"+ownerId);
				
				if(m.getSender().equals(ownerId)){
					System.out.println("no need to display");
				}else{
					this.ReceiveMessage.append(m.getSender() + " says: " + m.getContext() + "\r\n");
				}
							
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
}
