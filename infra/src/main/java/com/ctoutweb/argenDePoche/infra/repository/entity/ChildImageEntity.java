package com.ctoutweb.argenDePoche.infra.repository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table(name = "child_image", schema = "sc_argent_de_poche")
public class ChildImageEntity extends Temporal {
    @Id
    private Long id;

    @Column("image_name")
    private String imageName;

    private String extension;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ChildImageEntity that = (ChildImageEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(imageName, that.imageName) && Objects.equals(extension, that.extension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, imageName, extension);
    }

    @Override
    public String toString() {
        return "ChildImageEntity{" +
                "id=" + id +
                ", imageName='" + imageName + '\'' +
                ", extension='" + extension + '\'' +
                '}';
    }
}
