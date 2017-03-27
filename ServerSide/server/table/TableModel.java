package com.trust.server.table;


import java.awt.Component;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
public class TableModel extends AbstractTableModel{

	public Vector rowData;
	public Vector columnName;
	public ImageIcon userIcon;
	
	//define components for sql server
	PreparedStatement ps=null;
	Connection ct=null;
	ResultSet rs=null;
	
	public TableModel()
	{
		//define column name
		columnName=new Vector();
		columnName.add("imageIcon");
		columnName.add("userName");
		columnName.add("password");
		//columnName.add("state");
		//add rows
		rowData=new Vector();
		
		try{
			//set driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			ct=DriverManager.getConnection
					("jdbc:sqlserver://127.0.0.1:1433;databaseName=ChatUserManager;integratedSecurity=true");
			ps=ct.prepareStatement("select * from UserInfo");
			rs=ps.executeQuery();
			
			while(rs.next())
			{
				Vector hang=new Vector();
				
				String myIconPath="image/"+rs.getString(1)+".jpg";
				userIcon=new ImageIcon(myIconPath);			
				userIcon.setImage(userIcon.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
				JLabel userLabel=new JLabel(userIcon);
				//userLabel.setIcon(userIcon);
				hang.add(userLabel);
				//hang.add(rs.getString(3));
				hang.add(rs.getString(1));
				//hang.add(rs.getString(2))
				hang.add("****");
				
				rowData.add(hang);
			}
						
		}catch(Exception e){
			e.printStackTrace();
		}finally{
							
			try {
				if(rs!=null)
					rs.close();
				if(ps!=null)
					ps.close();
				if(ct!=null)
					ct.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return super.getColumnClass(columnIndex);
	}


	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return this.rowData.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return this.columnName.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return ((Vector)this.rowData.get(rowIndex)).get(columnIndex);
	}
	
	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return (String)this.columnName.get(column);
	}
	
	///////////////////////////////////////////////
	

}
