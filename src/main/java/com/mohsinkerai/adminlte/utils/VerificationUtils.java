package com.mohsinkerai.adminlte.utils;

import com.mohsinkerai.adminlte.jamatkhana.Jamatkhana;
import com.mohsinkerai.adminlte.person.Person;
import com.mohsinkerai.adminlte.users.MyUser;

public class VerificationUtils {

  public static boolean hasValidJk(MyUser loggedInUser, Person person) {
    Jamatkhana personJamatkhana = person.getJamatkhana();
    return loggedInUser.getJamatkhanas().contains(personJamatkhana);
  }
}
