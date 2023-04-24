package org.oliverr.bugtracker.repository;

import org.oliverr.bugtracker.DB;
import org.oliverr.bugtracker.entity.Bug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Component
public class BugRepository {

    private DB db;

    @Autowired
    public void setDb(DB db) { this.db = db; }

    public int getBugCount(long projectId) {
        ResultSet rs = db.executeQuery("SELECT COUNT(bug_id) FROM bugs WHERE project_id = "+projectId+";");

        int res = 0;
        try {
            while(rs.next()) {
                res = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    public ArrayList<Bug> getAllBugs(long userId) {
        ArrayList<Bug> bugs = new ArrayList<>();
        ResultSet rs = db.executeQuery("SELECT * FROM bugs WHERE user_id = "+userId+" ORDER BY bug_id DESC;");

        try {
          while(rs.next()) {
              Bug bug = new Bug();
              bug.setBugId(rs.getLong(1));
              bug.setProjectId(rs.getLong(2));
              bug.setUserId(rs.getLong(3));
              bug.setTitle(rs.getString(4));
              bug.setDescription(rs.getString(5));
              bug.setCreated(rs.getString(6));
              bug.setStatus(rs.getString(7));

              bugs.add(bug);
          }
          rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!bugs.isEmpty()) {
            for(Bug bug : bugs) {
                ResultSet rs2 = db.executeQuery("SELECT title FROM projects WHERE project_id = "+bug.getProjectId()+";");
                try {
                    while (rs2.next()) {
                        bug.setProjectName(rs2.getString(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return bugs;
    }

    public ArrayList<Bug> getProjectBugs(long projectId) {
        ArrayList<Bug> bugs = new ArrayList<>();
        ResultSet rs = db.executeQuery("SELECT * FROM bugs WHERE project_id = "+projectId+" ORDER BY bug_id DESC;");

        try {
            while(rs.next()) {
                Bug bug = new Bug();
                bug.setBugId(rs.getLong(1));
                bug.setProjectId(rs.getLong(2));
                bug.setUserId(rs.getLong(3));
                bug.setTitle(rs.getString(4));
                bug.setDescription(rs.getString(5));
                bug.setCreated(rs.getString(6));
                bug.setStatus(rs.getString(7));

                bugs.add(bug);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!bugs.isEmpty()) {
            for(Bug bug : bugs) {
                ResultSet rs2 = db.executeQuery("SELECT title FROM projects WHERE project_id = "+bug.getProjectId()+";");
                try {
                    while (rs2.next()) {
                        bug.setProjectName(rs2.getString(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return bugs;
    }

    public void addBug(long projectId, long userId, String title, String description, String status) {
        if(title.length() > 50 || description.length() > 1000) {
            return;
        }
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        String current = formatter.format(date);
        try {
            PreparedStatement ps = db.conn.prepareStatement("INSERT INTO bugs(project_id, user_id, title, description, created, status) VALUES (?, ?, ?, ?, ?, ?);");
            ps.setLong(1, projectId);
            ps.setLong(2, userId);
            ps.setString(3, title);
            ps.setString(4, description);
            ps.setString(5, current);
            ps.setString(6, status);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int bugsCount(long userId) {
        ResultSet rs = db.executeQuery("SELECT COUNT(bug_id) FROM bugs WHERE user_id = "+userId+";");
        int res = 0;
        try {
            while(rs.next()) {
                res = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    public int uncompletedBugsCount(long userId) {
        ResultSet rs = db.executeQuery("SELECT COUNT(bug_id) FROM bugs WHERE user_id = "+userId+" AND (status = 'in progress' OR status = 'pending');");
        int res = 0;
        try {
            while(rs.next()) {
                res = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    public Bug getBugById(long bugId) {
        Bug bug = null;
        ResultSet rs = db.executeQuery("SELECT * FROM bugs WHERE bug_id = "+bugId+";");
        try {
            while(rs.next()) {
                bug = new Bug();
                bug.setBugId(rs.getLong(1));
                bug.setProjectId(rs.getLong(2));
                bug.setUserId(rs.getLong(3));
                bug.setTitle(rs.getString(4));
                bug.setDescription(rs.getString(5));
                bug.setCreated(rs.getString(6));
                bug.setStatus(rs.getString(7));
            }
            rs.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        if(bug != null) {
            ResultSet rs2 = db.executeQuery("SELECT title FROM projects WHERE project_id = "+bug.getProjectId()+";");
            try {
                while (rs2.next()) {
                    bug.setProjectName(rs2.getString(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bug;
    }

    public boolean isItTheirBug(long bugId, long userId) {
        ResultSet rs = db.executeQuery("SELECT * FROM bugs WHERE bug_id = "+bugId+" AND user_id = "+userId+";");
        try {
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public void updateBug(long bugId, long projectId, String title, String description, String status) {
        if(title.length() > 50 || description.length() > 1000) {
            return;
        }
        
        try {
            PreparedStatement ps = db.conn.prepareStatement("UPDATE bugs SET project_id = ?, title = ?, description = ?, status = ? WHERE bug_id = ?;");
            ps.setLong(1, projectId);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, status);
            ps.setLong(5, bugId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBug(Long bugId) {
        try {
            PreparedStatement ps = db.conn.prepareStatement("DELETE FROM bugs WHERE bug_id = ?;");
            ps.setLong(1, bugId);
            ps.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
