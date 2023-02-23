package com.example.demo;


import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private String name;
    private String uid;
    private String token;
}
