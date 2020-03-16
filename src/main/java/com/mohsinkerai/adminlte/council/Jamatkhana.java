package com.mohsinkerai.adminlte.jamatkhana;

import com.mohsinkerai.adminlte.base.BaseEntity;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Jamatkhana extends BaseEntity {

  private String name;
}
