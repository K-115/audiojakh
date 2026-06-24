package com.makersacademy.audiojakh.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

import static java.lang.Boolean.TRUE;

@Data
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column (name = "PROFILE_PICTURE")
    private String profilePicture;
    @Column (name = "FIRST_NAME")
    private String firstName;
    private String surname;
    @Column (name = "EMAIL_ADDRESS")
    private String emailAddress;
    private String followers;
    private Date dob;


    public User(String username, String profilePicture, String firstName, String surname, String emailAddress, String followers, Date dob){
        this.username = username;
        this.profilePicture = profilePicture;
        this.firstName = firstName;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.followers = followers;
        this.dob = dob;
    }

}


