package com.example.es;

import lombok.Data;

@Data
public class UserDocument {
    private String id;
    private String name;
    private String gender;
    private Integer age;
    private String address;
}
