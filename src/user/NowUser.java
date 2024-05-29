package user;

public class NowUser {
    private int userid;
    private String username;

    public NowUser(){};

    public NowUser(int userid,String username){
        this.userid = userid;
        this.username = username;
    }

    public void setUserid(int userid){
        this.userid = userid;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public int getUserid(){
        return userid;
    }
    public String getUsername(){
        return username;
    }
}
