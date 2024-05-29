package ui;

import user.NowUser;
import view.choicemaps;
import view.login;

public class Main {
    public static void main(String[] args) {
        NowUser nowuser = new NowUser();
        login loginView = new login(nowuser);
        loginView.init();

        Control control = new Control(nowuser);
       //  运行用户界面交互
       control.Init();
    }
}