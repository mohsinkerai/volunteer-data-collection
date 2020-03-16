package com.mohsinkerai.adminlte.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonShortDto {

  private Long id;
  private com.mohsinkerai.adminlte.jamatkhana.Council council;
  private String personName;
  private String cnic;
}
