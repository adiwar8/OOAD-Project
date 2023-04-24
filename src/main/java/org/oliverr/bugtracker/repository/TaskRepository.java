package org.oliverr.bugtracker.repository;

import org.oliverr.bugtracker.DB;
import org.oliverr.bugtracker.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Component
public class TaskRepository {

    private DB db;

    @Autowired
    public void setDb(DB db) { this.db = db; }

    public int getTaskCount(long projectId) {
        ResultSet rs = db.executeQuery("SELECT COUNT(task_id) FROM tasks WHERE project_id = "+projectId+";");

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

    public ArrayList<Task> getAllTask(long userId) {
        ArrayList<Task> tasks = new ArrayList<>();
        ResultSet rs = db.executeQuery("SELECT * FROM tasks WHERE user_id = "+userId+" ORDER BY task_id DESC;");

        try {
            while(rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getLong(1));
                task.setProjectId(rs.getLong(2));
                task.setUserId(rs.getLong(3));
                task.setTitle(rs.getString(4));
                task.setDescription(rs.getString(5));
                task.setCreated(rs.getString(6));
                task.setStatus(rs.getString(7));

                tasks.add(task);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!tasks.isEmpty()) {
            for(Task task : tasks) {
                ResultSet rs2 = db.executeQuery("SELECT title FROM projects WHERE project_id = "+task.getProjectId()+";");
                try {
                    while (rs2.next()) {
                        task.setProjectName(rs2.getString(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return tasks;
    }

    public ArrayList<Task> getProjectTasks(long projectId) {
        ArrayList<Task> tasks = new ArrayList<>();
        ResultSet rs = db.executeQuery("SELECT * FROM tasks WHERE project_id = "+projectId+" ORDER BY task_id DESC;");

        try {
            while(rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getLong(1));
                task.setProjectId(rs.getLong(2));
                task.setUserId(rs.getLong(3));
                task.setTitle(rs.getString(4));
                task.setDescription(rs.getString(5));
                task.setCreated(rs.getString(6));
                task.setStatus(rs.getString(7));

                tasks.add(task);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!tasks.isEmpty()) {
            for(Task task : tasks) {
                ResultSet rs2 = db.executeQuery("SELECT title FROM projects WHERE project_id = "+task.getProjectId()+";");
                try {
                    while (rs2.next()) {
                        task.setProjectName(rs2.getString(1));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return tasks;
    }

    public void addTask(long projectId, long userId, String title, String description, String status) {
        if(title.length() > 50 || description.length() > 1000) {
            return;
        }
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        String current = formatter.format(date);
        try {
            PreparedStatement ps = db.conn.prepareStatement("INSERT INTO tasks(project_id, user_id, title, description, created, status) VALUES (?, ?, ?, ?, ?, ?);");
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

    public int tasksCount(long userId) {
        ResultSet rs = db.executeQuery("SELECT COUNT(task_id) FROM tasks WHERE user_id = "+userId+";");
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

    public int uncompletedTaskCount(long userId) {
        ResultSet rs = db.executeQuery("SELECT COUNT(task_id) FROM tasks WHERE user_id = "+userId+" AND (status = 'in progress' OR status = 'pending');");
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

    public Task getTaskById(long taskId) {
        Task task = null;
        ResultSet rs = db.executeQuery("SELECT * FROM tasks WHERE task_id = "+taskId+";");
        try {
            while(rs.next()) {
                task = new Task();
                task.setTaskId(rs.getLong(1));
                task.setProjectId(rs.getLong(2));
                task.setUserId(rs.getLong(3));
                task.setTitle(rs.getString(4));
                task.setDescription(rs.getString(5));
                task.setCreated(rs.getString(6));
                task.setStatus(rs.getString(7));
            }
            rs.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        if(task != null) {
            ResultSet rs2 = db.executeQuery("SELECT title FROM projects WHERE project_id = "+task.getProjectId()+";");
            try {
                while (rs2.next()) {
                    task.setProjectName(rs2.getString(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return task;
    }

    public boolean isItTheirTask(long taskId, long userId) {
        ResultSet rs = db.executeQuery("SELECT * FROM tasks WHERE task_id = "+taskId+" AND user_id = "+userId+";");
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

    public void updateTask(long taskId, long projectId, String title, String description, String status) {
        if(title.length() > 50 || description.length() > 1000) {
            return;
        }
        
        try {
            PreparedStatement ps = db.conn.prepareStatement("UPDATE tasks SET project_id = ?, title = ?, description = ?, status = ? WHERE task_id = ?;");
            ps.setLong(1, projectId);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, status);
            ps.setLong(5, taskId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(Long taskId) {
        try {
            PreparedStatement ps = db.conn.prepareStatement("DELETE FROM tasks WHERE task_id = ?;");
            ps.setLong(1, taskId);
            ps.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
