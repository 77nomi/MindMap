package view;


import user.NowUser;
import user.userWork;
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class login {
    NowUser nowuser;//定义一个登录者的对象

    public login(NowUser nowuser){
        this.nowuser = nowuser;
    }
    public void init(){
        //用户登录页面
        JFrame jFrame=new JFrame("用户登录");//设置一个主要框架，命名为登录
        jFrame.setSize(1600,9800);//窗口大小
        jFrame.setLayout(null);
        //添加标签“思维导图系统”
        JLabel mindmapProduce=new JLabel("思维导图生成系统");
        mindmapProduce.setForeground(new Color(0x373D93));
        mindmapProduce.setFont(new Font("黑体",Font.PLAIN,50));
        mindmapProduce.setBounds(580,100,800,100);
        jFrame.add(mindmapProduce);

        JLabel mindmapUser=new JLabel("用户名:");
        mindmapUser.setForeground(new Color(0x050505));
        mindmapUser.setFont(new Font("黑体", Font.PLAIN,30));
        mindmapUser.setBounds(500,200,200,100);
        jFrame.add(mindmapUser);

        //添加输入框【用户名输入框】
        JTextField user = new JTextField(20);
        user.setFont(new Font("黑体", Font.PLAIN,18));
        user.setSelectedTextColor(new Color(0x050505));
        user.setBounds(620,225,280,40);
        jFrame.add(user);

        //添加标签【密码】
        JLabel textPassword = new JLabel("密码  :");
        textPassword.setForeground(new Color(0x050505));
        textPassword.setFont(new Font("黑体", Font.PLAIN,30));
        textPassword.setBounds(500,300,200,100);
        jFrame.add(textPassword);

        //添加密码输入框【密码】
        JPasswordField password = new JPasswordField(20);
        password.setBounds(620,325,280,40);
        jFrame.add(password);

        //添加按钮【登录】
        JButton userLogin = new JButton("登录");
        userLogin.setForeground(new Color(0x023BF6));
        userLogin.setBackground(new Color(0x73B25D));
        userLogin.setFont(new Font("黑体", Font.PLAIN,20));
        userLogin.setBorderPainted(false);
        userLogin.setBounds(500,400,100,50);
        jFrame.add(userLogin);

        //添加按钮【注册】
        JButton userRegister = new JButton("注册");
        userRegister.setForeground(new Color(0x0029FF));
        userRegister.setBackground(new Color(0xECA871));
        userRegister.setFont(new Font("黑体", Font.PLAIN,20));
        userRegister.setBorderPainted(false);
        userRegister.setBounds(800,400,100,50);
        jFrame.add(userRegister);

        //设置相对位置
        jFrame.setLocationRelativeTo(null);

        //确保使用窗口关闭按钮，能够正常退出
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //禁止对窗口大小进行缩放处理
        //jFrame.setResizable(false);

        //设置可见
        jFrame.setVisible(true);

        //注册窗口
        //......
        JFrame zhuce=new JFrame("用户注册");//设置一个主要框架，命名为登录
        zhuce.setSize(1600,800);//窗口大小
        zhuce.setLayout(null);

        JLabel registertitle=new JLabel("新用户注册");
        registertitle.setForeground(new Color(0x373D93));
        registertitle.setFont(new Font("黑体",Font.PLAIN,50));
        registertitle.setBounds(580,100,800,100);
        zhuce.add(registertitle);

        JLabel newUser=new JLabel("新用户名:");
        newUser.setForeground(new Color(0x050505));
        newUser.setFont(new Font("黑体", Font.PLAIN,30));
        newUser.setBounds(480,200,200,100);
        zhuce.add(newUser);

        JTextField newusertext = new JTextField(20);
        newusertext.setFont(new Font("黑体", Font.PLAIN,18));
        newusertext.setSelectedTextColor(new Color(0x050505));
        newusertext.setBounds(620,225,280,40);
        zhuce.add(newusertext);

        JLabel newPassword = new JLabel("设置密码:");
        newPassword.setForeground(new Color(0x050505));
        newPassword.setFont(new Font("黑体", Font.PLAIN,30));
        newPassword.setBounds(480,300,200,100);
        zhuce.add(newPassword);

        JPasswordField newpwd = new JPasswordField(20);
        newpwd.setBounds(620,325,280,40);
        zhuce.add(newpwd);

        zhuce.setLocationRelativeTo(null);

        zhuce.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //按钮交互
        //设定条件login

        //添加按钮【确认】
        JButton confirm = new JButton("确认");
        confirm.setForeground(new Color(0x023BF6));
        confirm.setBackground(new Color(0x73B25D));
        confirm.setFont(new Font("黑体", Font.PLAIN,20));
        confirm.setBorderPainted(false);
        confirm.setBounds(500,400,100,50);
        zhuce.add(confirm);

        //添加按钮【取消】
        JButton cancel = new JButton("取消");
        cancel.setForeground(new Color(0x0029FF));
        cancel.setBackground(new Color(0xECA871));
        cancel.setFont(new Font("黑体", Font.PLAIN,20));
        cancel.setBorderPainted(false);
        cancel.setBounds(800,400,100,50);
        zhuce.add(cancel);

        //输入框监听板块
        //登录按钮监听
        userLogin.addActionListener((e->{
            String username = user.getText();
            String psw = password.getText();
            boolean flat= userWork.login(username,psw,this.nowuser);
            if(flat==true)
            {
                jFrame.setVisible(false);
                choicemaps cmaps=new choicemaps(nowuser);
                cmaps.choice();
                //new SiWeiDaoTu().SiWeiDaoTuInterface();
                //登陆界面传入用户ID给思维导图界面
            }else{
                JOptionPane.showMessageDialog(null, "信息有误，请重新输入", "提示", JOptionPane.WARNING_MESSAGE);
            }
            //System.out.println(flat);
        }));
        //监听注册按钮
        userRegister.addActionListener((e->{
            jFrame.setVisible((false));
            zhuce.setVisible(true);
        }));

        //确认按钮监听（即确定注册）
        confirm.addActionListener((e->{
            String username=newusertext.getText();
            String psw=newpwd.getText();
            int flat=new userWork().register(username,psw);
            if(flat==0)
            {
                JOptionPane.showMessageDialog(null, "用户已存在，请重新输入", "提示", JOptionPane.WARNING_MESSAGE);
            }
            if(flat==1)
            {
                JOptionPane.showMessageDialog(null, "注册失败，请重新注册", "提示", JOptionPane.WARNING_MESSAGE);

            }
            if(flat==2)
            {
                JOptionPane.showMessageDialog(null, "注册成功", "提示", JOptionPane.WARNING_MESSAGE);
                jFrame.setVisible(true);

            }
        }));

        //监听取消按钮
        cancel.addActionListener((e->{
            zhuce.setVisible(false);
            jFrame.setVisible(true);
        }));

    }
}
