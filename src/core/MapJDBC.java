package core;

import user.NowUser;
import util.connectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MapJDBC {
    public Map<String, Integer> getMaps(NowUser nowUser) {
        Map<String, Integer> res = new HashMap<>();

        String sql = "SELECT title, mapid FROM maps WHERE userid = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = connectDB.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nowUser.getUserid());

            rs = stmt.executeQuery();

            while (rs.next()) {
                res.put(rs.getString("title"), rs.getInt("mapid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭数据库连接
            closeConn(conn, stmt);
        }

        return res;
    }

    public int save(MindMap mindMap, NowUser nowuser){
        int mapId= mindMap.getMapId();
        //mapId为-1表明思维导图是新建的
        if(mapId==-1){
            if(mindMap.getTitle()=="新建思维导图"){
                System.out.println("请重新命名思维导图后再试");
                return -1;
            }
            mapId = this.MapsSave(mindMap, nowuser);
            if(mapId == -1){
                System.out.println("保存失败");
                return 0;
            }
        }
        //思维导图不是新建的情况
        else{
            Connection conn = null;
            PreparedStatement stmt = null;

            try {
                String sql = "DELETE FROM nodes WHERE mapid = ?";
                conn = connectDB.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, mapId);
                int rowsAffected = stmt.executeUpdate();

                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                // 关闭数据库连接
                closeConn(conn, stmt);
            }
        }
        Node root = mindMap.getRoot();

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node current = queue.poll();
                if (current != null) {
                    this.NodesSave(mapId, current);
                }
                if (current != null) {
                    for (Node child : current.getChildren()) {
                        queue.offer(child);
                    }
                }
            }
        }
        mindMap.setIfChange(0);
        return 1;
    }

    public void NodesSave(int mapId, Node root) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int nodeId = root.getNodeId();
        int parentNodeId = -1;
        if (nodeId != 0) {
            Node parent = root.getParent();
            parentNodeId = parent.getNodeId();
        }
        String data = root.getData();

        try {
            conn = connectDB.getConnection();
            String sql = "INSERT INTO nodes (nodeid, mapid, parentNodeId, data) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, stmt.RETURN_GENERATED_KEYS);
            stmt.setInt(1, nodeId);
            stmt.setInt(2, mapId);
            stmt.setInt(3, parentNodeId);
            stmt.setString(4, data);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Creating map failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭数据库连接
            closeConn(conn, stmt);
        }
    }

    public int MapsSave(MindMap mindMap, NowUser nowuser) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int mapId = -1;

        try {
            String sql = "INSERT INTO maps (title, userid) VALUES (?, ?)";
            conn = connectDB.getConnection();
            stmt = conn.prepareStatement(sql, stmt.RETURN_GENERATED_KEYS);
            stmt.setString(1, mindMap.getTitle());
            stmt.setInt(2, nowuser.getUserid());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Creating map failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mapId = generatedKeys.getInt(1);
                } else {
                    throw new RuntimeException("Creating map failed, no ID obtained.");
                }
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭数据库连接
            closeConn(conn, stmt);
        }

        return mapId;
    }

    public MindMap loadMap(String title, int mapId){
        MindMap newMap = new MindMap();
        Connection conn = null;
        PreparedStatement stmt = null;


        try{
            String sql = "SELECT * FROM nodes WHERE mapid = ?";
            conn = connectDB.getConnection();
            try{
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, mapId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int nodeId = rs.getInt("nodeid");
                        int parentNodeId = rs.getInt("parentNodeId");
                        String data = rs.getString("data");
                        if(nodeId == 0){
                            newMap = new MindMap(title, data,mapId);
                        }else{
                            newMap.addNode(parentNodeId,data,nodeId);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            // 关闭数据库连接
            closeConn(conn, stmt);
        }

        newMap.setIfChange(0);
        return newMap;
    }

    public boolean changeTitle(MindMap mindMap, String newTitle) {
        mindMap.setTitle(newTitle);
        int mapid = mindMap.getMapId();
        Connection conn = null;
        PreparedStatement selectStmt = null;
        PreparedStatement updateStmt = null;

        try {
            String selectSQL = "SELECT * FROM maps WHERE mapid = ?";
            conn = connectDB.getConnection();
            selectStmt = conn.prepareStatement(selectSQL);
            selectStmt.setInt(1, mapid);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    try {
                        String updateSQL = "UPDATE maps SET title = ? WHERE mapid = ?";
                        updateStmt = conn.prepareStatement(updateSQL);
                        updateStmt.setString(1, newTitle);
                        updateStmt.setInt(2, mapid);
                        int rowsAffected = updateStmt.executeUpdate();
                        if (rowsAffected == 0) {
                            return false; //更改失败
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    return false; // 查询失败
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭数据库连接和语句对象
            if (updateStmt != null) {
                try {
                    updateStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (selectStmt != null) {
                try {
                    selectStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true; // 成功
    }

    private void closeConn(Connection conn, PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}