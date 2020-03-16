package com.mohsinkerai.adminlte;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown =  true)
public class LocalUser {

  private String username;
  private String password;
  private String jk;
}
