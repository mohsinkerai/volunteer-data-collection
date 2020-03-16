package com.mohsinkerai.adminlte.report.validator;

import com.mohsinkerai.adminlte.jamatkhana.Jamatkhana;
import com.mohsinkerai.adminlte.users.MyUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReportValidator {

  private final MyUserService userService;

  public boolean isJkAllowed(Jamatkhana jamatkhana) {
    return userService.getCurrentLoggedInUser()
      .getJamatkhanas().stream()
      .filter(jk -> jk.getName().equals(jamatkhana.getName()))
      .findAny()
      .isPresent();
  }
}
