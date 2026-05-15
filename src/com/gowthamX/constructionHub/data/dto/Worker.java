package com.gowthamX.constructionHub.data.dto;

public class Worker {

    private Long id;
    private String workerId;
    private String name;
    private String type;
    private double dailyWage;
    private WorkerStatus status;
    private Long createdAt;

    public enum WorkerStatus {
        ACTIVE,
        INACTIVE
    }

    public Worker() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getWorkerId() { return workerId; }
    public void setWorkerId(String workerId) { this.workerId = workerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getDailyWage() { return dailyWage; }
    public void setDailyWage(double dailyWage) { this.dailyWage = dailyWage; }

    public WorkerStatus getStatus() { return status; }
    public void setStatus(WorkerStatus status) { this.status = status; }

    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
}
