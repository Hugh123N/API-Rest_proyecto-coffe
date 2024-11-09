package com.hugo.coffe.wraper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //getter y setter
@AllArgsConstructor
@NoArgsConstructor
public class UserWraper {

    private Integer id;
    private String name;
    private String email;
    private String contactNumber;
    private String status;

}
