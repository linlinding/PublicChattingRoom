package com.trust.server.view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map; 

import com.trust.server.database.*;
import com.trust.server.model.*;
import com.trust.server.table.TableModel;

public class ServerMainView extends JFrame implements ActionListener,Runnable {

	public static boolean createTableFlag=false;
	JLabel logoImage;
	JPanel JpanelNorth;
	JPanel JpanelSouth;
	
	TableModel tableModel;
	JTable onlineUserTable;
	JScrollPane centerScoll;
	JPanel JpanelCenter;
	//COTableModel coTableModel;
	
	JButton launchServerButton;
	JButton stopServerButton;
	ServerConClient serverConclient;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerMainView serverMainView=new ServerMainView();
		Thread thead=new Thread(serverMainView);
		thead.start();
		
	}
	
	public ServerMainView()
	{
		super("Server");
		
		logoImage=new JLabel(new ImageIcon("image/server.jpg"));
		//logoImage.setPreferredSize(new Dimension(100,250));
		JpanelNorth=new JPanel();
		JpanelNorth.add(logoImage);
		
		// init table
		tableModel=new TableModel();
		onlineUserTable=new JTable(tableModel);
		
		////////////////////////
		onlineUserTable.setRowHeight(40);
		onlineUserTable.setAutoscrolls(true);
		onlineUserTable.getColumnModel().getColumn(0).setCellRenderer(new JTableCellRender());
		//////////////////////////////
		centerScoll=new JScrollPane(onlineUserTable);
		centerScoll.setSize(new Dimension(700,300));
		JpanelCenter=new JPanel();
		JpanelCenter.add(centerScoll);
		
		
		
		launchServerButton=new JButton("launch");
		launchServerButton.setPreferredSize(new Dimension(100,30));
		launchServerButton.addActionListener(this);
		
		stopServerButton=new JButton("refresh");
		stopServerButton.setPreferredSize(new Dimension(100,30));
		stopServerButton.addActionListener(this);
		
		JpanelSouth=new JPanel();
		JpanelSouth.add(launchServerButton);
		JpanelSouth.add(stopServerButton);
		
		
		
		this.add(JpanelNorth,"North");
		this.add(JpanelCenter,"West");
		this.add(JpanelSouth, "South");
		this.setSize(460,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
				
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==launchServerButton)
		{
			//create a new thread
			serverConclient=new ServerConClient();
			//start a new thread 
			serverConclient.start();	

		}
		if(e.getSource()==stopServerButton)
		{
			tableModel=new TableModel();
			onlineUserTable.setModel(tableModel);
			onlineUserTable.setRowHeight(40);
			onlineUserTable.setAutoscrolls(true);
			onlineUserTable.getColumnModel().getColumn(0).setCellRenderer(new JTableCellRender());
		}
	}
	//////////////////////////////////////////////

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("server is listening");
		while(true){
			//System.out.println("server is in while true");
			if(ServerMainView.createTableFlag)
			{
				System.out.println("server starts to create a new table");
				tableModel=new TableModel();
				onlineUserTable.setModel(tableModel);
				onlineUserTable.setRowHeight(40);
				onlineUserTable.setAutoscrolls(true);
				onlineUserTable.getColumnModel().getColumn(0).setCellRenderer(new JTableCellRender());
				ServerMainView.createTableFlag=false;
				System.out.println("recreate table successfully");
			}	
		}
		
	}
}

class JTableCellRender extends JLabel implements TableCellRenderer {  
	  
    @Override  
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {  
        return (JLabel)value;  
    }  
}
