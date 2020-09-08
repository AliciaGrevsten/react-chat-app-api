package com.javafullstackcourse.chatapp.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ChatUser {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer id;

    @Column(nullable = false, unique = true)
    public String username;

    @Column(nullable = false)
    public String password;

    @Column(nullable = false)
    public Date created = new java.sql.Timestamp(new Date().getTime());

    @Column(nullable = false)
    public Date modified = new java.sql.Timestamp(new Date().getTime());

    @Column
    public byte[] profileImage;

}