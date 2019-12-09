package com.netcraker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private int userId;
    @NotBlank
    private String fullName;
    @Email @NotBlank
    private String email;
    @Size(min = 6)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Timestamp createdAt;
    private Boolean enabled;
    private String photoPath;
    private List<Role> roles;

    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
