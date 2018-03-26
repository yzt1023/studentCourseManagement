package student;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

//登录密码修改
public class ChangePassword extends JPanel implements ActionListener,Listener {
	JButton btnAlter = new JButton("修改密码");
	JPanel panel=new JPanel();
	JTable table;
	JScrollPane scroll;
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	Object [][] columnValues;
	String[] columnNames;
	DefaultTableModel tableModel;

	ChangePassword() {// 构造方法
		connDB();
		btnAlter.addActionListener(this);
		setLayout(new BorderLayout(0, 0));
		panel.add(btnAlter);
		add(panel, BorderLayout.NORTH);
		
		columnValues = new Object[1][2];
		columnNames = new String []{ "用户名", "密码" };
		try {
			rs = stmt.executeQuery("select * from s where logn='"
					+ LoginFrame.loginName + "'");
			rs.next();
			columnValues[0][0]=rs.getString("logn");
			columnValues[0][1]=rs.getString("pswd");
			tableModel = new DefaultTableModel(columnValues, columnNames);
			table=new JTable(tableModel);
			scroll = new JScrollPane(table);
			this.add(scroll);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeDB();
	}

	public void connDB() { // 连接数据库
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			con = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433; DatabaseName=student",
					"sa", "123");
			stmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void closeDB() // 关闭连接
	{
		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void actionPerformed(ActionEvent e) {
		new InputNumber("密码",this);
	}

	@Override
	public void refreshUI() {
		// TODO Auto-generated method stub
	}

	@Override
	public void getMessage(String message) {
		connDB();
		try{
			stmt.executeUpdate("update s set pswd='"+message+
					"' where logn='"+columnValues[0][0]+"'");
			columnValues[0][1]=message;
			JOptionPane.showMessageDialog(null, "密码修改成功！");
		}catch (SQLException e) {
			e.printStackTrace();
		}
		tableModel.setDataVector(columnValues, columnNames);
		tableModel.fireTableDataChanged();
		closeDB();
	}
}

