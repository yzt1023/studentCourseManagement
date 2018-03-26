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

//课程成绩查看
public class StudentGrade extends JPanel implements ActionListener{
	JTable table;
	Connection con=null;
	Statement stmt=null;
	ResultSet rs=null;
	JScrollPane scroll;
	JPanel panel=new JPanel();
	JButton btnDisplay = new JButton("刷新");
	String[] columnNames;
	Object[][] columnValues;
	DefaultTableModel tableModel;
	
	StudentGrade(){
		columnNames = new String []{"课程号","课程名","成绩","学分","任课教师"};
		setLayout(new BorderLayout(0, 0));
		getAllCourses();
		tableModel=new DefaultTableModel(columnValues,columnNames);
		table=new JTable(tableModel);
		scroll=new JScrollPane(table);
		this.add(scroll, BorderLayout.CENTER);
		panel.add(btnDisplay);
		btnDisplay.addActionListener(this);
		this.add(panel,BorderLayout.NORTH);
	}
	
	public void connDB() { // 连接数据库
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			con = DriverManager
					.getConnection(
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
	
	public void getAllCourses(){
		connDB();
		int count=0,i = 0;
		try{
			rs=stmt.executeQuery("select count(*) from sc where sno='"+LoginFrame.loginName+"'");
			rs.next();
			count=rs.getInt(1);
		}catch(SQLException e){
			e.printStackTrace();
		}
		columnValues = new Object[count][5];
		try {
			rs = stmt.executeQuery("select c.cno,cname,grade,credit,tname from c,sc "
					+ "where sc.cno=c.cno and sno='" + LoginFrame.loginName+"'");
			while (rs.next()) {
				columnValues[i][0] = rs.getString("cno");
				columnValues[i][1] = rs.getString("cname");
				columnValues[i][2] = rs.getString("grade");
				columnValues[i][3] = rs.getInt("credit");
				columnValues[i][4] = rs.getString("tname");
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeDB();
	}
		
	@Override
	public void actionPerformed(ActionEvent arg0) {
		getAllCourses();
		tableModel.setDataVector(columnValues, columnNames);
		tableModel.fireTableDataChanged();
	}
}
