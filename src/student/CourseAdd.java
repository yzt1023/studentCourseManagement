package student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

//用于课程信息管理中增加或修改某条记录的界面
class CourseAdd extends JFrame implements ActionListener{
	
	JLabel lcno = new JLabel("课程号：");
	JLabel lcname = new JLabel("课程名：");
	JLabel lcredit = new JLabel("学分数：");
	JLabel lcdept = new JLabel("院系：");
	JLabel ltname = new JLabel("任课教师：");
	
	JTextField tcno = new JTextField(10);
	JTextField tcname = new JTextField(10);
	JTextField tcredit = new JTextField(10);
	JComboBox<String> cbcdept = new JComboBox<String>();
	JTextField ttname = new JTextField(10);
	
	JButton btnOK = new JButton("确     定");
	JButton btnCancel = new JButton("取     消");
	
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	Listener listener;

	public CourseAdd(Listener listener) {// 构造方法
		this.listener=listener;
		setTitle("增加");
		setBounds(900, 200, 300, 300);
		cbcdept.addItem("计科系");
		cbcdept.addItem("物理系");
		cbcdept.addItem("数学系");
		cbcdept.addItem("外语系");
		getContentPane().setLayout(null);
		lcno.setBounds(55, 10, 60, 20);
		getContentPane().add(lcno);
		tcno.setBounds(115, 10, 120, 20);
		getContentPane().add(tcno);
		lcname.setBounds(55, 40, 60, 20);
		getContentPane().add(lcname);
		tcname.setBounds(115, 40, 120, 20);
		getContentPane().add(tcname);
		lcredit.setBounds(55, 70, 60, 20);
		getContentPane().add(lcredit);
		tcredit.setBounds(115, 70, 120, 20);
		getContentPane().add(tcredit);
		lcdept.setBounds(55, 100, 60, 20);
		getContentPane().add(lcdept);
		cbcdept.setBounds(115, 100, 120, 20);
		getContentPane().add(cbcdept);
		ltname.setBounds(55, 130, 75, 20);
		getContentPane().add(ltname);
		ttname.setBounds(125, 130, 110, 20);
		getContentPane().add(ttname);
		btnOK.setBounds(70, 160, 160, 30);
		getContentPane().add(btnOK);
		btnCancel.setBounds(70, 200, 160, 30);
		getContentPane().add(btnCancel);
		btnOK.addActionListener(this);
		btnCancel.addActionListener(this);
		this.setVisible(true);
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
		if (e.getActionCommand() == "确     定") {
			if(getTitle()=="修改"){
				update();
			}else{
				insert();
			}
		}
		if (e.getActionCommand() == "取     消") {
			this.setVisible(false);
		}
	}
	
	public void update(){//更新记录
		String kch = tcno.getText();
		String kcm = tcname.getText();
        int xfs = 0;
		String yx = cbcdept.getSelectedItem().toString();
		String rkjs = ttname.getText();
		connDB();
		try{
			xfs=Integer.parseInt((String)tcredit.getText());
			if(kcm.equals("")||rkjs.equals("")||xfs==0){
				JOptionPane.showMessageDialog(null, "课程名、学分数、授课教师不能有空值！");
			}else{
				String sql="update c set cname='"+kcm+"',credit='"+xfs+
						"',cdept='"+yx+"',tname='"+rkjs+"' where cno='"+kch+"'";
				try {
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "修改成功！");
					listener.refreshUI();
					setVisible(false);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}catch (NumberFormatException e) {// 判断年龄是否为数字
			JOptionPane.showMessageDialog(null, "学分数必须是整数！");
			tcredit.setText("");
		}
		closeDB();
	}

	public void insert() { // 插入记录
		String kch = tcno.getText();
		String kcm = tcname.getText();
        int xfs = 0;
		String yx = cbcdept.getSelectedItem().toString();
		String rkjs = ttname.getText();
		connDB();
		try{
			xfs=Integer.parseInt((String)tcredit.getText());
			if(kch.equals("")||kcm.equals("")||rkjs.equals("")||xfs==0){
				JOptionPane.showMessageDialog(null, "课程号、课程名、学分数、授课教师不能有空值！");
			}else{
				String sql="insert into c values('"+kch+"','"+kcm+"','"+xfs
						+"','"+yx+"','"+rkjs+"')";
				try {
					stmt.executeUpdate(sql);
					JOptionPane.showMessageDialog(null, "增加成功！");
					listener.refreshUI();
					setVisible(false);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "课程号已存在！");
					e.printStackTrace();
				}
			}
		}catch (NumberFormatException e) {// 判断年龄是否为数字
			JOptionPane.showMessageDialog(null, "学分数必须是整数！");
			tcredit.setText("");
		}
		closeDB();
	}
}