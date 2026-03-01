package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(schema = "sc_argent_de_poche", name = "jwt")
public class JwtEntity extends Temporal {
    @Id
    private Long id;

    @Column("parent_id")
    private Long parentId;

    private String email;

    @Column("jwt_token")
    private String jwtToken;

    @Column("jwt_id")
    private String jwtId;

    @Column("is_valid")
    private Boolean isValid;

    @Column("expired_at")
    private LocalDateTime expiredAt;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtId() {
        return jwtId;
    }

    public void setJwtId(String jwtId) {
        this.jwtId = jwtId;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean valid) {
        isValid = valid;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JwtEntity jwtEntity = (JwtEntity) o;
        return Objects.equals(id, jwtEntity.id) && Objects.equals(parentId, jwtEntity.parentId) && Objects.equals(email, jwtEntity.email) && Objects.equals(jwtToken, jwtEntity.jwtToken) && Objects.equals(jwtId, jwtEntity.jwtId) && Objects.equals(isValid, jwtEntity.isValid) && Objects.equals(expiredAt, jwtEntity.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, parentId, email, jwtToken, jwtId, isValid, expiredAt);
    }

    @Override
    public String toString() {
        return "JwtEntity{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", email='" + email + '\'' +
                ", jwtToken='" + jwtToken + '\'' +
                ", jwtId='" + jwtId + '\'' +
                ", isValid=" + isValid +
                ", expiredAt=" + expiredAt +
                '}';
    }
}
