package com.inn.cafe.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBean {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String contactNumber;
    private String status;
}
