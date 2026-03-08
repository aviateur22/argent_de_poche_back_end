package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table(name = "parent", schema = "sc_argent_de_poche")
public class ParentEntity extends Temporal {
    @Id
    private Long id;

    @Column("nickname")
    private String name;

    private String email;

    private String password;

    @Column("is_account_active")
    private Boolean isAccountActive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsAccountActive() {
        return isAccountActive;
    }

    public void setIsAccountActive(Boolean accountActive) {
        isAccountActive = accountActive;
    }

    @Override
    public String toString() {
        return "ParentEntity{" +
                "id=" + id +
                ", nickname='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ParentEntity that = (ParentEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, email, password);
    }
}
