package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(schema = "sc_argent_de_poche", name = "login")
public class LoginEntity extends Temporal {
    @Id
    private Long id;

    @Column("parent_id")
    private Long parentId;

    @Column("is_login_success")
    private Boolean isLoginSuccess;

    @Column("has_to_be_check")
    private Boolean hasToBeCheck;

    @Column("login_at")
    private LocalDateTime loginAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getLoginSuccess() {
        return isLoginSuccess;
    }

    public void setLoginSuccess(Boolean loginSuccess) {
        isLoginSuccess = loginSuccess;
    }

    public Boolean getHasToBeCheck() {
        return hasToBeCheck;
    }

    public void setHasToBeCheck(Boolean hasToBeCheck) {
        this.hasToBeCheck = hasToBeCheck;
    }

    public LocalDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(LocalDateTime loginAt) {
        this.loginAt = loginAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LoginEntity that = (LoginEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(parentId, that.parentId) && Objects.equals(isLoginSuccess, that.isLoginSuccess) && Objects.equals(hasToBeCheck, that.hasToBeCheck) && Objects.equals(loginAt, that.loginAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, parentId, isLoginSuccess, hasToBeCheck, loginAt);
    }

    @Override
    public String toString() {
        return "LoginEntity{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", isLoginSuccess=" + isLoginSuccess +
                ", hasToBeCheck=" + hasToBeCheck +
                ", loginAt=" + loginAt +
                '}';
    }
}
