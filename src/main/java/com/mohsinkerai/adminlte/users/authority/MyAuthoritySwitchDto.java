package com.mohsinkerai.adminlte.users.authority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyAuthoritySwitchDto {

  private MyAuthority authority;
  private boolean enabled;
}
