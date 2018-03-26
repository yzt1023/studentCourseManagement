package student;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;

class GradeInfo extends JPanel implements ActionListener,Listener{// 成绩信息管理
	
	JPanel panel = new JPanel();
	JButton btnAlter = new JButton("修改成绩");
	JButton btnSno = new JButton("查询学生");
	JButton btnCno = new JButton("查询课程");
	JButton btnDisplay = new JButton("刷新");
	JTable table;
	JScrollPane scroll;
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	Object[][] columnValues;
	String [] columnNames;
	DefaultTableModel tableModel;
	int c=-1;

	GradeInfo() {// 构造方法
		setLayout(new BorderLayout(0, 0));
		add(panel, BorderLayout.NORTH);
		panel.add(btnAlter);
		panel.add(btnSno);
		panel.add(btnCno);
		panel.add(btnDisplay);
		btnAlter.addActionListener(this);
		btnSno.addActionListener(this);
		btnCno.addActionListener(this);
		btnDisplay.addActionListener(this);
		columnNames=new String []{"学号","课程号","成绩"};
		getAllGrades();
		tableModel=new DefaultTableModel(columnValues,columnNames);
		table=new JTable(tableModel);
		scroll=new JScrollPane(table);
		add(scroll);
	}
	
	//获取选课列表
	public void getAllGrades(){
		int count=0,index=0;
		connDB();// 连接数据库
		try{
			rs=stmt.executeQuery("select count(*) from sc");
			rs.next();
			count=rs.getInt(1);
		}catch(SQLException e){
			e.printStackTrace();
		}
		columnValues=new Object[count][3];
		try{
			rs=stmt.executeQuery("select * from sc order by sno");
			while(rs.next()){
				columnValues[index][0]=rs.getString("sno");
				columnValues[index][1]=rs.getString("cno");
				columnValues[index][2]=rs.getInt("grade");
				index++;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		closeDB();
	}

	//刷新选课列表
	public void refresh(){
		getAllGrades();
		tableModel.setDataVector(columnValues, columnNames);
		tableModel.fireTableDataChanged();
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

	// 修改某个学生的成绩信息
	public void update() {
		int row = -1;
		row = table.getSelectedRow();
		if (row == -1) {// 判断要修改的信息是否被选中
			JOptionPane.showMessageDialog(null, "请选择要修改的记录！");
		} else {
			InputNumber grade=new InputNumber("成绩",this);
			grade.tno.setText(columnValues[row][2].toString());
			c=3;
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "修改成绩") {
			update();
		}
		if (e.getActionCommand() == "查询学生") {
			new InputNumber("学号",this);
			c=1;
		}
		if (e.getActionCommand() == "查询课程") {
			new InputNumber("课程号",this);
			c=2;
		}
		if (e.getActionCommand() == "刷新") {
			refresh();
		}
	}

	@Override
	public void refreshUI() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMessage(String message) {
		connDB();
		int count=0,index=0;
		if(c==2){
			try {
				rs = stmt.executeQuery("select count(*) from sc where cno='" +message+ "'");
				rs.next();
				count=rs.getInt(1);
			}catch(SQLException e){
				e.printStackTrace();
			}
			columnValues=new Object[count][3];
			try{
				rs = stmt.executeQuery("select * from sc where cno='" +message+ "' order by sno");
				while(rs.next()){
					columnValues[index][0] = rs.getString("sno");
					columnValues[index][1] = rs.getString("cno");
					columnValues[index][2] = rs.getInt("grade");
					index++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (columnValues[0][1] == null) {
				JOptionPane.showMessageDialog(null, "课程号不存在！");
				refresh();
			} else {
			    tableModel.setDataVector(columnValues, columnNames);
				tableModel.fireTableDataChanged();
			}
		}else if(c==1){
			try {
				rs = stmt.executeQuery("select count(*) from sc where sno='" +message+ "'");
				rs.next();
				count=rs.getInt(1);
			}catch(SQLException e){
				e.printStackTrace();
			}
			columnValues=new Object[count][3];
			try{
				rs = stmt.executeQuery("select * from sc where sno='" +message+ "' order by cno");
				while(rs.next()){
					columnValues[index][0] = rs.getString("sno");
					columnValues[index][1] = rs.getString("cno");
					columnValues[index][2] = rs.getInt("grade");
					index++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (columnValues[0][0] == null) {
				JOptionPane.showMessageDialog(null, "学号不存在！");
				refresh();
			} else {
			    tableModel.setDataVector(columnValues, columnNames);
				tableModel.fireTableDataChanged();
			}
		}else{
			int row = table.getSelectedRow();
			try{
				stmt.executeUpdate("update sc set grade='"+message+
						"' where sno='"+columnValues[row][0]+"' and cno='"+columnValues[row][1]+"'");
				columnValues[row][2]=message;
				JOptionPane.showMessageDialog(null, "成绩修改成功！");
			}catch (SQLException e) {
				e.printStackTrace();
			}
			tableModel.setDataVector(columnValues, columnNames);
			tableModel.fireTableDataChanged();
		}
		closeDB();
	}
}
