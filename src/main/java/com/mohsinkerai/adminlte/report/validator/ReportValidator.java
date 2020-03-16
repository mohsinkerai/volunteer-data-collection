package com.mohsinkerai.adminlte.report.validator;

import com.mohsinkerai.adminlte.users.MyUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReportValidator {

  private final MyUserService userService;

  public boolean isJkAllowed(com.mohsinkerai.adminlte.jamatkhana.Council council) {
    return userService.getCurrentLoggedInUser()
      .getCouncils().stream()
      .filter(jk -> jk.getName().equals(council.getName()))
      .findAny()
      .isPresent();
  }
}
