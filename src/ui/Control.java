package ui;

import core.MapJDBC;
import core.MindMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import user.*;

public class Control {
    private MindMap mindMap;
    private Display display;
    private userWork userWork;
    private NowUser nowUser;

    public Control(NowUser nowUser) {
        this.nowUser = nowUser;
    }

    // 登录注册
    public void Init(){
        this.userWork = new userWork();
        Scanner scanner = new Scanner(System.in);
        System.out.print("登录或注册（0或1）");
        int type = scanner.nextInt();
        if(type==1){
            System.out.print("输入账号密码");
            String username = scanner.next();
            String password = scanner.next();
            int res = userWork.register(username,password);
            if(res==0){
                System.out.println("用户已存在");
                Init();
            }else if(res==2){
                System.out.println("注册成功");
                Init();
            }else{
                System.out.println("注册失败，请重试");
                Init();
            }
        }else if(type==0){
            System.out.print("输入账号密码");
            String username = scanner.next();
            String password = scanner.next();
            if(userWork.login(username,password,nowUser)){
                System.out.println("登录成功");
                this.choose();
            }else{
                System.out.print("账号或密码错误");
                Init();
            }
        }else{
            System.out.println("错误");
            Init();
        }
    }

    // 选择进行操作的思维导图


    public void choose(){
        Scanner scanner = new Scanner(System.in);
        MapJDBC mapjdbc = new MapJDBC();
        Map<String, Integer> maps = mapjdbc.getMaps(nowUser);
        //获取思维导图的标题
        List<String> titles = new ArrayList<>(maps.keySet());

        for(String title : titles){
            System.out.println(title);
        }

        System.out.println("创建新思维导图");

        int index = scanner.nextInt();
        if(index==titles.size()){

            //创建新的思维导图
            this.mindMap = new MindMap();
//            this.display = new Display(mindMap);
            this.startUserInterface();
        }else if(index>=0 && index<titles.size()){
            //查看已经有的思维导图
            this.mindMap = mapjdbc.loadMap(titles.get(index),maps.get(titles.get(index)));
//            this.display = new Display(mindMap);
            this.startUserInterface();
        }else{
            System.out.println("错误");
        }
    }
//选择
    public void startUserInterface() {
        Scanner scanner = new Scanner(System.in);
        MapJDBC mapjdbc = new MapJDBC();
        String command;

        do {
            System.out.print("Enter a command (add, remove, display, changeTitle, save, exit):");
            command = scanner.next();

            switch (command) {
                case "add" -> {
                    System.out.print("Enter the parent node ID: ");
                    int parentId = scanner.nextInt();
                    System.out.print("Enter the new node data: ");
                    String newData = scanner.next();
                    mindMap.addNode(parentId, newData);
                }
                case "remove" -> {
                    System.out.print("Enter the node ID to remove: ");
                    int nodeIdToRemove = scanner.nextInt();
                    mindMap.removeNode(nodeIdToRemove);
                }
                case "display" -> display.show();
                //case "display"->System.out.println();
                case "changeTitle" -> {
                    String newTitle = scanner.next();
                    mindMap.setTitle(newTitle);
                }
                case "save" ->{
                    mapjdbc.save(mindMap,nowUser);
                }
                case "exit" -> System.out.println("Exiting program.");
                default -> System.out.println("Invalid command. Please try again.");
            }

        } while (!command.equals("exit"));

        scanner.close();
    }
}