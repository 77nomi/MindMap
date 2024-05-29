package ui;
import core.MapJDBC;
import core.MindMap;
import core.Node;
import user.NowUser;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import view.CustomPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Stack;
import java.util.List;


public class Display {
    private MindMap mindMap;
    private NowUser nowUser;
    private Node root;
    private JFrame frame;

    private int choice1=0;

    public Display(MindMap mindMap,NowUser nowUser) {
        this.root = mindMap.getRoot();
        this.mindMap = mindMap;
        this.nowUser = nowUser;
        this.frame = new JFrame("思维导图");
        frame.setSize(1600, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void show(){
        top();
        bottom();
        frame.setVisible(true);
    }

    public void top(){

        JPanel topPanel =     new JPanel(new BorderLayout());
        JPanel topLeftPanel = new JPanel();
        JLabel label = new JLabel(mindMap.getTitle());
        topLeftPanel.add(label);
        //右上
        //      JPanel topRightPanel = new JPanel(new GridLayout(1, 8));
        JPanel topRightPanel = new JPanel();

        //布局
        topRightPanel.add(new JLabel(new ImageIcon("display.png")));
        JButton display = new JButton("布局");
        topRightPanel.add(display);
        display.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 选项的字符串数组
                String[] options = {"左侧布局", "右侧布局", "自动布局"};

                // 显示带有选项的对话框
                choice1 = JOptionPane.showOptionDialog(
                        frame, // 父窗口
                        "请选择布局", // 提示文本
                        "Select an Option", // 对话框标题
                        JOptionPane.DEFAULT_OPTION, // 对话框类型
                        JOptionPane.INFORMATION_MESSAGE, // 图标类型
                        null, // 自定义图标
                        options, // 选项数组
                        options[0] // 默认选项
                );
                refresh();
            }
        });

        //修改标题
        topRightPanel.add(new JLabel(new ImageIcon("changeTitle.png")));
        JButton changeTitle = new JButton("修改标题");
        changeTitle.addActionListener(e -> {
            String newTitle = JOptionPane.showInputDialog(frame, "请输入新标题:");
            if (newTitle != null && !newTitle.isEmpty()) { // 如果用户输入了内容
                MapJDBC mapJDBC = new MapJDBC();
                mapJDBC.changeTitle(mindMap,newTitle);
                label.setText(mindMap.getTitle()); // 修改按钮文本为用户输入的内容
                JOptionPane.showMessageDialog(frame, "修改成功！"); // 提示用户修改成功
            } else {
                JOptionPane.showMessageDialog(frame, "修改失败!"); // 提示用户输入无效
            }


        });

        topRightPanel.add(changeTitle);
        JButton save = new JButton("保存");

        save.addActionListener(e -> {
            MapJDBC mapJDBC = new MapJDBC();
            int flag = mapJDBC.save(mindMap,nowUser);
            if(flag==-1){
                JOptionPane.showMessageDialog(frame, "保存失败，请先修改思维导图标题", "错误", JOptionPane.ERROR_MESSAGE);
            }
            else if(flag==0)
            {
                JOptionPane.showMessageDialog(frame, "未知原因保存失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
            else if(flag==1)
            {
                JOptionPane.showMessageDialog(frame, "保存成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        topRightPanel.add(save);

        //back
        topRightPanel.add(new JLabel(new ImageIcon("exit.png")));
        JButton exit = new JButton("退出");
        exit.addActionListener(e -> {
            frame.dispose();
        });
        topRightPanel.add(exit);

        //导出

        JButton induce=new JButton("导出");
        topRightPanel.add(induce);
        induce.addActionListener(e-> {
            String[] options = {"JPG", "PNG"};

            // 显示选项对话框并获取用户的选择
            int choice = JOptionPane.showOptionDialog(null,
                    "请选择导出图片的格式：",
                    "导出图片",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]); // 默认选择第一个选项

            // 根据用户选择执行相应的操作
            if (choice == 0) {
                // 用户选择了JPG格式
                JOptionPane.showMessageDialog(null, "已选择导出为JPG格式。");
                try {
                    BufferedImage mapFinish = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics daotu = mapFinish.getGraphics();
                    frame.paint(daotu);
                    daotu.dispose();
                    ImageIO.write(mapFinish, "jpg", new File("C:\\Users\\86198\\Desktop\\1_Java\\Java\\src\\image1.jpg"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // 这里可以添加导出为JPG格式的代码
            } else if (choice == 1) {
                // 用户选择了PNG格式
                JOptionPane.showMessageDialog(null, "已选择导出为PNG格式。");
                try {
                    BufferedImage mapFinish = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics daotu = mapFinish.getGraphics();
                    frame.paint(daotu);
                    daotu.dispose();
                    ImageIO.write(mapFinish, "png", new File("C:\\Users\\86198\\Desktop\\1_Java\\Java\\src\\image2.png"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // 这里可以添加导出为PNG格式的代码
            } else {
                // 用户关闭了对话框或点击了取消按钮
                JOptionPane.showMessageDialog(null, "未选择任何格式。");
            }
        });

        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topRightPanel, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);
    }

    public <Queue> void bottom() {
        if (root == null) {
            return ;
        }

        JPanel bottomPanel = new JPanel(new BorderLayout()); //下半部分

        //左下
        JPanel leftBottomPanel = new JPanel();
        leftBottomPanel.setBackground(Color.WHITE);
        //左边占页面的3/4
        leftBottomPanel.setPreferredSize(new Dimension(frame.getWidth() * 3/ 4, frame.getHeight()));
        //右边占页面的1/4

        java.util.Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        JButton[] btns = new JButton[1000];
        int level=0;
        int leftstepy=0,rightstepy=0;
        java.util.Queue<Integer> qq = new LinkedList<>();
        while (!queue.isEmpty()) {
            int size = queue.size();//current的子节点个数
            int stepy=0;  //节点之间的间隔
            int yData=0;
            int flag = 1;
            int leftLevel=0,rightLevel=0;
            int leftyData = leftstepy;
            int rightyData = rightstepy;

            for (int i = 0; i < size;  i++) {
                Node current = queue.poll();
                stepy=750/(size+1);
                yData+=stepy;
                if (current != null) {
                    int nodeId = current.getNodeId();
                    btns[nodeId]=new JButton(current.getData());
                    btns[nodeId].setFont(new Font("黑体",Font.BOLD,15));
                    btns[nodeId].setBackground(Color.decode("#BCEDFF"));

                    if(choice1==0)
                        btns[nodeId].setBounds(20+150*level ,yData,120,30);
                    else if(choice1==1)
                        btns[nodeId].setBounds(1000-150*level ,yData,120,30);
                    else if(choice1==2){
                        if(nodeId==0){
                            btns[0].setBounds(500 ,350,120,30);
                            int firstLevel = current.getChildren().size();
                            leftLevel = firstLevel/2;
                            rightLevel = firstLevel/2;
                            if((leftLevel+rightLevel)!=firstLevel)
                                leftLevel++;
                            leftstepy=750/(leftLevel+1);
                            rightstepy=750/(rightLevel+1);
                            leftyData+=leftstepy;
                            rightyData+=rightstepy;
                        }
                        else if(current.getParent().getNodeId()==0){
                            if(flag==1){
                                btns[nodeId].setBounds(500-150*level ,leftyData,120,30);
                                leftyData+=leftstepy;
                                flag=0;
                                leftLevel+=current.getChildren().size();
                            }else{
                                btns[nodeId].setBounds(500+150*level ,rightyData,120,30);
                                rightyData+=rightstepy;
                                flag=1;
                                rightLevel+=current.getChildren().size();
                            }
                        }else{
                            int parentNodeId = current.getParent().getNodeId();
                            int x = btns[parentNodeId].getBounds().x;
                            if(x<500){
                                btns[nodeId].setBounds(500-150*level ,leftyData,120,30);
                                leftyData+=leftstepy;
                                leftLevel+=current.getChildren().size();
                            }else{
                                btns[nodeId].setBounds(500+150*level ,rightyData,120,30);
                                rightyData+=rightstepy;
                                rightLevel+=current.getChildren().size();
                            }
                        }
                    }

                    if(nodeId!=0)
                    {
                        int parentId = current.getParent().getNodeId();
                        CustomPanel customPanel = new CustomPanel(btns[parentId], btns[nodeId]);
                        customPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
                        frame.add(customPanel, BorderLayout.CENTER);
                    }

                    frame.add(btns[nodeId]);

                    btns[nodeId].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(!qq.isEmpty()){
                                btns[qq.poll()].setBackground(Color.decode("#BCEDFF"));
                            }
                            qq.offer(nodeId);
                            btns[nodeId].setBackground(Color.decode("#22A5D6"));

                            // 选项的字符串数组
                            String[] options = {"添加子节点", "添加兄弟节点", "修改节点内容", "删除该节点"};

                            // 显示带有选项的对话框
                            int choice = JOptionPane.showOptionDialog(
                                    leftBottomPanel, // 父窗口
                                    "请选择你要进行的操作", // 提示文本
                                    "Select an Option", // 对话框标题
                                    JOptionPane.DEFAULT_OPTION, // 对话框类型
                                    JOptionPane.INFORMATION_MESSAGE, // 图标类型
                                    null, // 自定义图标
                                    options, // 选项数组
                                    options[0] // 默认选项
                            );

                            // 根据选择执行不同的操作
                            switch (choice) {
                                //添加子节点
                                case 0 -> {
                                    String newData = JOptionPane.showInputDialog(frame, "请输入节点内容:");
                                    if (newData != null) { // 用户点击了确认按钮
                                        mindMap.addNode(nodeId, newData);
                                        refresh();
                                    }
                                }
                                //添加兄弟节点
                                case 1 -> {
                                    int parentId = current.getParent().getNodeId();
                                    String newData = JOptionPane.showInputDialog(frame, "请输入节点内容:");
                                    if(newData!=null) {
                                        mindMap.addNode(parentId, newData);
                                        refresh();
                                    }
                                }
                                //修改该节点内容
                                case 2 -> {
                                    String newData = JOptionPane.showInputDialog(frame, "请输入新的节点内容:");
                                    if(newData!=null) {
                                        mindMap.changeNodeData(nodeId, newData);
                                        refresh();
                                    }
                                }
                                //删除该节点及其所有子节点
                                case 3 -> {
                                    int dialogResult = JOptionPane.showConfirmDialog(frame, "确定要删除该节点吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                                    if (dialogResult == JOptionPane.YES_OPTION) {
                                        // 用户点击了“是”，执行删除节点的操作
                                        mindMap.removeNode(nodeId);
                                        refresh();
                                    }
                                }
                                default -> {
                                    btns[qq.poll()].setBackground(Color.decode("#BCEDFF"));
                                }
                            }
                        }
                    });
                }

                if (current != null) {
                    //遍历当前节点的所有子节点
                    for (Node child : current.getChildren()) {
                        queue.offer(child);
                    }
                }
            }
            level++;
            leftstepy=750/(leftLevel+1);
            rightstepy=750/(rightLevel+1);
        }


        JPanel rightPanel = new JPanel();
        Dimension rightPanelSize = new Dimension(400, frame.getHeight());
        rightPanel.setPreferredSize(rightPanelSize);
        rightPanel.setBackground(Color.decode("#FFF8BC"));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        Stack<Node> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            int size = stack.size();
            for (int i = 0; i < size; i++) {
                Node current = stack.pop();
                if (current != null) {
                    JPanel rowPanel = new JPanel(new BorderLayout());
                    rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.Y_AXIS));
                    rowPanel.setBackground(Color.decode("#FFF8BC"));
                    Node parent = current.getParent();
                    int times=0;
                    while (parent != null) {
                        times++;
                        parent = parent.getParent();
                    }
                    JLabel textLabel = new JLabel(current.getData());
                    Font labelFont = textLabel.getFont();
                    textLabel.setFont(new Font(labelFont.getName(), Font.PLAIN, 20));
                    textLabel.setBorder(BorderFactory.createEmptyBorder(0, 10+20*times, 0, 0));
                    rowPanel.add(textLabel,BorderLayout.EAST);
                    rightPanel.add(rowPanel,BorderLayout.CENTER);
                    List<Node> children = current.getChildren();
                    for (int j = children.size() - 1; j >= 0; j--) {
                        stack.push(children.get(j));
                    }
                }
            }
            rightPanel.add(Box.createVerticalStrut(15)); // 添加垂直间距，实现节点间的空白
        }



        bottomPanel.add(leftBottomPanel, BorderLayout.WEST);

        //  frame.add(bottomPanel, BorderLayout.CENTER);
        frame.add(bottomPanel);
        frame.add(rightPanel, BorderLayout.EAST);
    }

    public void refresh(){
        frame.getContentPane().removeAll();
        show();
        frame.repaint();    // 重新绘制
    }
}
