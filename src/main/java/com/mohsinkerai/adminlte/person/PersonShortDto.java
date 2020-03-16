package com.mohsinkerai.adminlte.person;

import com.mohsinkerai.adminlte.jamatkhana.Jamatkhana;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonShortDto {

  private Long id;
  private Jamatkhana jamatkhana;
  private String personName;
  private String cnic;
}
