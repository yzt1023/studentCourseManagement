package student;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//用于学生基本信息管理中查询时输入学号的界面
public class InputNumber extends JFrame implements ActionListener{
	JLabel ltitle = null;
	JTextField tno = new JTextField(8);
	JButton btnOK = new JButton("确定");
	JPanel p = new JPanel();
	String number = null;
	Listener listener;

	public InputNumber(String str,Listener listener) {// 构造方法
		ltitle = new JLabel(str);
		p.add(ltitle);
		p.add(tno);
		p.add(btnOK);
		add(p);
		setBounds(900, 280, 200, 160);
		btnOK.addActionListener(this);
		this.setResizable(false);
		this.setVisible(true);
		this.listener=listener;
	}

	public void actionPerformed(ActionEvent e) {
		number = tno.getText();// 取得当前输入学号的值
		if (number.equals("")) {// 判断是否输入了学号
			JOptionPane.showMessageDialog(null, ltitle.getText()+"不能为空，请重新输入！");
		}else{
			this.dispose();
			listener.getMessage(number);
		}
	}
}