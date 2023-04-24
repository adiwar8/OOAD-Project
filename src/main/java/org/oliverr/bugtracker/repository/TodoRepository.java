package org.oliverr.bugtracker.repository;

import org.oliverr.bugtracker.DB;
import org.oliverr.bugtracker.entity.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class TodoRepository {

    private DB db;
    @Autowired
    public void setDb(DB db) { this.db = db; }

    public ArrayList<Todo> getTodos(Long userId) {
        ArrayList<Todo> todos = new ArrayList<>();

        ResultSet rs = db.executeQuery("SELECT * FROM todo WHERE user_id = "+userId+" ORDER BY todo_id ASC;");
        try {
            while(rs.next()) {
                Todo todo = new Todo();
                todo.setTodoId(rs.getLong(1));
                todo.setUserId(rs.getLong(2));
                todo.setTask(rs.getString(3));
                todo.setStatus(rs.getString(4));

                todos.add(todo);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return todos;
    }

    public void addTodo(String task, Long userId) {
        if(task.length() >= 46) {
            return;
        }

        try {
            PreparedStatement ps = db.conn.prepareStatement("INSERT INTO todo(user_id, task, status) VALUES (?, ?, 'pending');");
            ps.setLong(1, userId);
            ps.setString(2, task);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeTodo(Long todoId) {
        try {
            PreparedStatement ps = db.conn.prepareStatement("DELETE FROM todo WHERE todo_id = ?;");
            ps.setLong(1, todoId);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
