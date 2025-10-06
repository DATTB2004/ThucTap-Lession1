package com.bksoft.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bksoft.domain.MyUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MyUserDTO implements Serializable {

    private Long id;

    private Integer idUser;

    private String userName;

    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyUserDTO)) {
            return false;
        }

        MyUserDTO myUserDTO = (MyUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, myUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MyUserDTO{" +
            "id=" + getId() +
            ", idUser=" + getIdUser() +
            ", userName='" + getUserName() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
