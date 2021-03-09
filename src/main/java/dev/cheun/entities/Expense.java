package dev.cheun.entities;

import dev.cheun.utils.DateTimeUtil;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name="expense")
@DynamicUpdate
@SelectBeforeUpdate
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="amount_in_cents")
    private int amountInCents;

    @Column(name="reason")
    private String reason;

    @Column(name="submitted_at")
    private OffsetDateTime submittedAt;

    @Column(name="mgr_reviewed_at")
    private OffsetDateTime mgrReviewedAt;

    @Column(name="status_id")
    @JoinColumn(name="status_id")
    private int statusId;

    @Column(name="employee_id")
    @JoinColumn(name="employee_id")
    private int employeeId;

    @Column(name="manager_id")
    @JoinColumn(name="manager_id")
    private Integer managerId;

    public Expense(){}

    public Expense(int id, int amountInCents, String reason,
                   OffsetDateTime submittedAt, OffsetDateTime mgrReviewedAt,
                   int statusId, int employeeId, Integer managerId) {
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
                null, 1, employeeId, null);
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
        return this.statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getManagerId() {
        return this.managerId;
    }

    public void setManagerId(Integer managerId) {
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
