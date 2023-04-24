package org.oliverr.bugtracker.repository;

import org.oliverr.bugtracker.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ContributorRepository {

    private DB db;

    @Autowired
    public void setDb(DB db) { this.db = db; }

    public int contributorsCount(long projectId) {
        ResultSet rs = db.executeQuery("SELECT COUNT(project_id) FROM projects_users WHERE project_id = "+projectId+";");

        int res = 0;
        try {
            while(rs.next()) {
                res = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res + 1;
    }

}
