package com.netcraker.model.vo;

import lombok.Data;
import javax.validation.constraints.Size;

@Data
public class ChangePassword {
    @Size(min = 6) private String oldPassword;
    @Size(min = 6) private String newPassword;
}
