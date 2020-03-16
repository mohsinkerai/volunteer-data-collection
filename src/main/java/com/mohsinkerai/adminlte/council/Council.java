package com.mohsinkerai.adminlte.council;

import com.mohsinkerai.adminlte.base.BaseEntity;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Council extends BaseEntity {

  private String name;
}
