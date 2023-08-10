package com.sunset_cafe.sunset_cafe_backend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;


@NamedQuery(name ="User.getAllUsers" ,query = "select new com.sunset_cafe.sunset_cafe_backend.DTO.UserDTO(u.id,u.name,u.email,u.contactNumber,u.status) from User u where u.role='user'")


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "email")
    private String  email;

    @Column(name = "password")
    private String  password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;
}
