package com.gowthamX.constructionHub.data.dto;

public class Salary {

    private Long id;
    private Long workerId;
    private Long siteId;
    private int totalDays;
    private double totalAmount;
    private Long weekStart;
    private Long weekEnd;
    private Long paidAt;

    public Salary() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getWorkerId() { return workerId; }
    public void setWorkerId(Long workerId) { this.workerId = workerId; }

    public Long getSiteId() { return siteId; }
    public void setSiteId(Long siteId) { this.siteId = siteId; }

    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public Long getWeekStart() { return weekStart; }
    public void setWeekStart(Long weekStart) { this.weekStart = weekStart; }

    public Long getWeekEnd() { return weekEnd; }
    public void setWeekEnd(Long weekEnd) { this.weekEnd = weekEnd; }

    public Long getPaidAt() { return paidAt; }
    public void setPaidAt(Long paidAt) { this.paidAt = paidAt; }
}
