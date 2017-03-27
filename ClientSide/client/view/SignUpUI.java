package com.trust.client.view;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.trust.client.model.ClientConServer;
import com.trust.common.Account;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.Socket;


public class SignUpUI extends JFrame implements ActionListener{

	JLabel userImage;
	
	JLabel userName;
	JTextField userText;
	JLabel password;
	JPasswordField pwdText;
	JPanel centerPanel;
	
	JLabel imagePath;
	JButton selectButton;
	JTextField imagePathText;
	JButton signUpButton;
	JPanel southPanel;
	
		
	JFileChooser chooseWindow;
	String filePath;
	
	File rdImageFile;
	File wrImageFile;
	
	Socket clientSocket=null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SignUpUI sign=new SignUpUI();
	}
	
	public SignUpUI()
	{
		userImage=new JLabel(new ImageIcon("image/signup.jpg"));
		
		userName=new JLabel("username",JLabel.CENTER);
		userText=new JTextField();
		password=new JLabel("password",JLabel.CENTER);
		pwdText=new JPasswordField();		

		
		centerPanel=new JPanel(new GridLayout(2,2));
		
		centerPanel.add(userName);
		centerPanel.add(userText);
		centerPanel.add(password);
		centerPanel.add(pwdText);
		
		selectButton=new JButton("select");
		selectButton.addActionListener(this);
		imagePathText=new JTextField(20);	
		signUpButton=new JButton("signUp");
		signUpButton.addActionListener(this);
		
		southPanel=new JPanel();
		southPanel.add(selectButton);
		southPanel.add(imagePathText);
		southPanel.add(signUpButton);
		
			
		this.add(userImage,"West");
		this.add(centerPanel, "Center");
		this.add(southPanel,"South");

		this.setSize(450,200);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==selectButton)
		{
			chooseWindow=new JFileChooser();
			chooseWindow.setDialogTitle("please select user image");
			chooseWindow.showOpenDialog(null);
			chooseWindow.setVisible(true);
			//get selected absolute path
			filePath=chooseWindow.getSelectedFile().getAbsolutePath();
			imagePathText.setText(filePath);
			System.out.println(filePath);
			
		}
		else if (e.getSource()==signUpButton)
		{
			try {				
				ClientConServer clientConServer=new ClientConServer();
				//get an account object
				Account account=new Account();
				account.setTag("new");
				account.setUserId(userText.getText());
				account.setPassword(new String(pwdText.getPassword()));
				
				String userImagePath="image"+"\\"+"\\"+userText.getText()+".jpg";
				System.out.println(userImagePath);
				account.setUserIcon(userImagePath);
				
				System.out.println(new String(account.getUserId()));
				System.out.println(new String(account.getPassword()));
				System.out.println(new String(account.getUserIcon()));
				
				if(clientConServer.sendSignUpInfo(account,filePath))
				{	
					System.out.println("client start store local image");
					JOptionPane.showMessageDialog(this,"sign up succeed");
					rdImageFile=new File(imagePathText.getText());
					BufferedImage image=ImageIO.read(rdImageFile);
					
					wrImageFile=new File(userImagePath);
					wrImageFile.createNewFile();
					
					ImageIO.write(image, "jpg", wrImageFile);
					System.out.println("save image successfully");	
					
					Icon userIcon=new ImageIcon(userImagePath);
					userImage.setIcon(userIcon);
					userImage.revalidate();
					System.out.println("complete repaint");		
										
				}
				else{
					JOptionPane.showMessageDialog(this,"sign up fail!  because this user already exists");
				}

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	

}
