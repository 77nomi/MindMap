package user;

import util.connectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

public class userWork {

    public static boolean login(String username, String password, NowUser nowUser) {
        password = md5Hex(password);
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectDB.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            rs = stmt.executeQuery();

            if (rs.next()) {
                int userid = rs.getInt("userid");
                nowUser.setUsername(username);
                nowUser.setUserid(userid);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    //0:用户名已存在
    //1:注册失败
    //2:注册成功
    public int register(String username, String password) {
        password = md5Hex(password);
        String query0 = "SELECT COUNT(*) FROM user WHERE username = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectDB.getConnection();
            stmt = conn.prepareStatement(query0);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1) != 0) {
                    return 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        String query = "INSERT INTO user (username, password) VALUES (?, ?)";

        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            int result = stmt.executeUpdate();

            if (result > 0) {
                return 2;
            } else {
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}