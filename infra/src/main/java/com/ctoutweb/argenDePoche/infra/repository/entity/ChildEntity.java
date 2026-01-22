package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table(name = "child", schema = "sc_argent_de_poche")
public class ChildEntity extends Temporal {

    @Id
    private Long id;

    @Column("child_account_id")
    private Long childAccountId;

    @Column("child_image_id")
    private Long childImageId;


    private String nickname;

    @Column("image_name")
    private String imageName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChildAccountId() {
        return childAccountId;
    }

    public void setChildAccountId(Long childAccountId) {
        this.childAccountId = childAccountId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ChildEntity that = (ChildEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(childAccountId, that.childAccountId) && Objects.equals(nickname, that.nickname) && Objects.equals(imageName, that.imageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, childAccountId, nickname, imageName);
    }

    public Long getChildImageId() {
        return childImageId;
    }

    public void setChildImageId(Long childImageId) {
        this.childImageId = childImageId;
    }

    @Override
    public String toString() {
        return "ChildEntity{" +
                "id=" + id +
                ", childAccountId=" + childAccountId +
                ", nickname='" + nickname + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
