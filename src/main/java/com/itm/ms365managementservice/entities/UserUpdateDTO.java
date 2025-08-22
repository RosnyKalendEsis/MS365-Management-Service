package com.itm.ms365managementservice.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    private String userPrincipalName;
    private boolean licensed;
}
