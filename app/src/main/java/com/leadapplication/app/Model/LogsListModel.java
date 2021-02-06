package com.leadapplication.app.Model;

public class LogsListModel {
    private String id;
    private String lead_id;
    private String status;
    private String date;
    private String time;
    private String agent_id;
    private String old_agent_id;
    private String remarks;
    private String created_at;
    private String name;
    private String old_agent_name;
    private String title;
    private String isAgentChanged;

    public LogsListModel(String id, String lead_id, String status, String date, String time, String agent_id, String old_agent_id,
                         String remarks, String created_at, String name, String old_agent_name, String title, String isAgentChanged) {
        this.id = id;
        this.lead_id = lead_id;
        this.status = status;
        this.date = date;
        this.time = time;
        this.agent_id = agent_id;
        this.old_agent_id = old_agent_id;
        this.remarks = remarks;
        this.created_at = created_at;
        this.name = name;
        this.old_agent_name = old_agent_name;
        this.title = title;
        this.isAgentChanged = isAgentChanged;
    }

    public String getId() {
        return id;
    }

    public String getLead_id() {
        return lead_id;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public String getOld_agent_id() {
        return old_agent_id;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getName() {
        return name;
    }

    public String getOld_agent_name() {
        return old_agent_name;
    }

    public String getTitle() {
        return title;
    }

    public String getIsAgentChanged() {
        return isAgentChanged;
    }
}
