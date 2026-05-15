package com.gowthamX.constructionHub.data.dto;

public class Allocation {

    private Long id;
    private Long workerId;
    private Long siteId;
    private Long date;
    private Long createdAt;

    public Allocation() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getWorkerId() { return workerId; }
    public void setWorkerId(Long workerId) { this.workerId = workerId; }

    public Long getSiteId() { return siteId; }
    public void setSiteId(Long siteId) { this.siteId = siteId; }

    public Long getDate() { return date; }
    public void setDate(Long date) { this.date = date; }

    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
}
