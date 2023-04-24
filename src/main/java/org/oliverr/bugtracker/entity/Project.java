package org.oliverr.bugtracker.entity;

public class Project {

    private Long projectId;
    private Long userId;
    private String title;
    private String description;
    private String readme;
    private int taskCount;
    private int bugCount;
    private int contributors;

    public Project() {

    }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getReadme() { return readme; }
    public void setReadme(String readme) { this.readme = readme; }

    public int getTaskCount() { return taskCount; }
    public void setTaskCount(int taskCount) { this.taskCount = taskCount; }

    public int getBugCount() { return bugCount; }
    public void setBugCount(int bugCount) { this.bugCount = bugCount; }

    public int getContributors() { return contributors; }
    public void setContributors(int contributors) { this.contributors = contributors; }

}
