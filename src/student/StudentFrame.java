package student;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//学生登录界面
class StudentFrame extends JFrame{
	JTabbedPane tabPane=new JTabbedPane();
	StudentDetail detail=new StudentDetail();
	StudentGrade grade=new StudentGrade();
	ChangePassword cpassword=new ChangePassword();
	CourseSelect scourse=new CourseSelect();

	StudentFrame() {
		super("学生信息管理系统");
		add("Center", tabPane);
		this.setBounds(200, 200, 500, 345);
		tabPane.add("个人信息查看", detail);
		tabPane.add("课程成绩查看", grade);
		tabPane.add("登录密码修改", cpassword);
		tabPane.add("学生选课系统", scourse);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
}
