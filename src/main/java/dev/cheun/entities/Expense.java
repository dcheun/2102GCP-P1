package dev.cheun.entities;

import dev.cheun.utils.DateTimeUtil;

import java.time.OffsetDateTime;

public class Expense {
    private int id;
    private int amountInCents;
    private String reason;
    private OffsetDateTime submittedAt;
    private OffsetDateTime mgrReviewedAt;
    private int statusId;
    private int employeeId;
    private int managerId;

    public Expense(){}

    public Expense(int id, int amountInCents, String reason,
                   OffsetDateTime submittedAt, OffsetDateTime mgrReviewedAt,
                   int statusId, int employeeId, int managerId) {
        this.id = id;
        this.amountInCents = amountInCents;
        this.reason = reason;
        this.submittedAt = submittedAt;
        this.mgrReviewedAt = mgrReviewedAt;
        this.statusId = statusId;
        this.employeeId = employeeId;
        this.managerId = managerId;
    }

    public Expense(int id, int amountInCents, int employeeId) {
        this(id, amountInCents, null, DateTimeUtil.getOffsetDateTimeUtcNow(),
                null, 1, employeeId, 0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmountInCents() {
        return amountInCents;
    }

    public void setAmountInCents(int amountInCents) {
        this.amountInCents = amountInCents;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public OffsetDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(OffsetDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public OffsetDateTime getMgrReviewedAt() {
        return mgrReviewedAt;
    }

    public void setMgrReviewedAt(OffsetDateTime mgrReviewedAt) {
        this.mgrReviewedAt = mgrReviewedAt;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", amountInCents=" + amountInCents +
                ", reason='" + reason + '\'' +
                ", submittedAt=" + submittedAt +
                ", mgrReviewedAt=" + mgrReviewedAt +
                ", statusId=" + statusId +
                ", employeeId=" + employeeId +
                ", managerId=" + managerId +
                '}';
    }
}
