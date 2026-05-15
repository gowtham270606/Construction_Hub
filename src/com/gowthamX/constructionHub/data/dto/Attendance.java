package com.gowthamX.constructionHub.data.dto;

public class Attendance {

    private Long id;
    private Long workerId;
    private Long siteId;
    private Long date;
    private boolean present;
    private Long markedAt;

    public Attendance() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getWorkerId() { return workerId; }
    public void setWorkerId(Long workerId) { this.workerId = workerId; }

    public Long getSiteId() { return siteId; }
    public void setSiteId(Long siteId) { this.siteId = siteId; }

    public Long getDate() { return date; }
    public void setDate(Long date) { this.date = date; }

    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }

    public Long getMarkedAt() { return markedAt; }
    public void setMarkedAt(Long markedAt) { this.markedAt = markedAt; }
}
