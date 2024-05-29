package view;
import core.MapJDBC;
import core.MindMap;
import ui.Display;
import ui.Control;

import user.NowUser;
import user.userWork;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class choicemaps extends JFrame implements ActionListener {
    private MindMap mindMap;
    private Display display;
    private NowUser nowUser;
    private JButton buttons[];
    private MapJDBC mapjdbc;
    private Map MAP;

    public choicemaps(NowUser nowUser) {
        this.nowUser = nowUser;
        this.mapjdbc = new MapJDBC();
    }


    public  void choice() {
        JFrame jFrame=new JFrame("选择页面");//设置一个主要框架，命名为登录
        jFrame.setSize(1600,800);//窗口大小
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.setLayout(null);

        // 选择进行操作的思维导图

        Map maps = mapjdbc.getMaps(nowUser);
        this.MAP = maps;
        List<String> titles = new ArrayList<>(maps.keySet());

        // 创建 JTextField 实例
        // JTextField textField = new JTextField();

        JButton[] btns = new JButton[titles.size()];
        int i;
        int startY = 300; // 初始X坐标
        int step=40;//每次增加的步长
        for (i=0;i<titles.size();i++) {
            btns[i] = new JButton(titles.get(i));
            btns[i].addActionListener(this);
            jFrame.add(btns[i]);
            btns[i].setForeground(new Color(0x023BF6));
            btns[i].setFont(new Font("黑体", Font.PLAIN,15));
            btns[i].setBorderPainted(false);
            btns[i].setBounds(650,startY+i*step,150,30);
        }
        this.buttons = btns;



        JLabel mindmapProduce=new JLabel("请选择进行操作的思维导图:");
        mindmapProduce.setForeground(new Color(0x373D93));
        mindmapProduce.setFont(new Font("黑体",Font.PLAIN,15));
        mindmapProduce.setBounds(620,250,800,60);
        jFrame.add(mindmapProduce);


        JButton newmap = new JButton("新建思维导图");
        newmap.addActionListener(this);
        newmap.setForeground(new Color(0x023BF6));
        //   newmap.setBackground(new Color(0x73B25D));
        newmap.setFont(new Font("黑体", Font.PLAIN,15));
        newmap.setBorderPainted(false);
        newmap.setBounds(650,550,150,30);
        jFrame.add(newmap);


        jFrame.setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource(); // 获取触发事件的按钮
        String mapTitle = clickedButton.getText();
        System.out.println(mapTitle);
        if(Objects.equals(mapTitle, "新建思维导图")){
            mindMap = new MindMap();
        }
        else{
            int mapId = (int) MAP.get(mapTitle);
            MapJDBC mapJDBC = new MapJDBC();
            mindMap = mapJDBC.loadMap(mapTitle, mapId);
        }
        display = new Display(mindMap,nowUser);
        display.show();
    }
}
