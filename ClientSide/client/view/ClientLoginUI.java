package com.trust.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;

import com.trust.client.model.*;
import com.trust.clientCrypto.authenticatorclient;
import com.trust.common.*;

import kerberos.AES;

public class ClientLoginUI extends JFrame implements ActionListener{

	//define login component
	JLabel logoImage;
	JPanel jpanelCenter;
	JPanel jpanelSouth;
	
	JLabel enterName;
	JTextField username;
	JLabel enterPassword;
	JPasswordField password;
	
	JButton loginButton;
	JButton signupButton;
	JButton resetButton;
	
	public static String sessionkey;
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientLoginUI clientLoginUI=new ClientLoginUI();
	}
	
	
	
	
	public ClientLoginUI()
	{
		
		super("Dating");
		
		//implement north
		logoImage=new JLabel(new ImageIcon("image/logo.jpg"));
		
		//implement center
		jpanelCenter=new JPanel(new GridLayout(2,2));
		enterName=new JLabel("Username",JLabel.CENTER);
		username=new JTextField();
		enterPassword=new JLabel("Password",JLabel.CENTER);
		password=new JPasswordField();
		jpanelCenter.add(enterName);
		jpanelCenter.add(username);
		jpanelCenter.add(enterPassword);
		jpanelCenter.add(password);
		
		//implement sourth
		jpanelSouth=new JPanel();
		
		loginButton=new JButton("Log in");
		loginButton.addActionListener(this);
		loginButton.setPreferredSize(new Dimension(100,40));
		
		signupButton=new JButton("Sign up");
		signupButton.addActionListener(this);
		signupButton.setPreferredSize(new Dimension(100,40));	
		
		resetButton=new JButton("Reset");
		resetButton.addActionListener(this);
		resetButton.setPreferredSize(new Dimension(100,40));
		
		jpanelSouth.add(loginButton);
		jpanelSouth.add(signupButton);
		jpanelSouth.add(resetButton);
		
		this.add(logoImage,"North");
		this.add(jpanelCenter,"Center");
		this.add(jpanelSouth,"South");
		this.setSize(500,400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource()==resetButton){
			System.out.println("click reset");
			username.setText(null);
			password.setText(null);
			
		}
		
		
		if(e.getSource()==loginButton)
		{
			System.out.println("click log in");
			ClientConServer clientConServer=new ClientConServer();
			DataOutputStream requestFlag;
			try {
				requestFlag = new DataOutputStream(ClientConServer.clientSocket.getOutputStream());
				requestFlag.writeUTF("login");
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			System.out.println("client start a new port for connecting to server ");
			//get an account object
			Account account=new Account();
					
									
			
			//get session key
			//userId+ASId+TGSId
			
			authenticatorclient clientRequestKey = new authenticatorclient(username.getText(),"888888888","777777777",ClientConServer.clientSocket);
			sessionkey=clientRequestKey.authentication();
			System.out.println("client obtains session key>>>>"+sessionkey);
			
			//encryption:
			AES aes = new AES();
			try {
				account.setTag(AES.Encrypt("old",sessionkey));
				account.setUserId(AES.Encrypt(username.getText(),sessionkey));
				account.setPassword(AES.Encrypt(new String(password.getPassword()),sessionkey));
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
					
			if(clientConServer.sendLoginInfo2Server(account))
			{
				
				System.out.println("log in succeed");
				JOptionPane.showMessageDialog(this,"log in succeed");
				this.dispose();
				
				//start chat, it's a thread so that it can keep monitoring if there's message comes from the sender or not
				String myIconPath="image/"+username.getText()+".jpg";
				System.out.println(myIconPath);
				ChatUI c = new ChatUI(username.getText(), "dummy",myIconPath);
				Thread t = new Thread(c);
				t.start();
							
			}
			else
			{
				JOptionPane.showMessageDialog(this,"username or password error");
			}
		}
		
		
		
		if(e.getSource()==signupButton)
		{
			SignUpUI signUp=new SignUpUI();
			
		}
		
	} 
	
	
	

}
