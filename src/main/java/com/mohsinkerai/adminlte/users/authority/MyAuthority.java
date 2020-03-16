package com.mohsinkerai.adminlte.users.authority;

import com.mohsinkerai.adminlte.base.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MyAuthority extends BaseEntity implements GrantedAuthority {

  @Column(unique = true, nullable = false)
  private String name;

  @Override
  public String getAuthority() {
    return name;
  }
}
