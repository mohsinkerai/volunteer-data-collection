package com.mohsinkerai.adminlte.person.status;

import com.mohsinkerai.adminlte.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class PersonStatus extends BaseEntity {

  private String name;
  private String colorCode;
  private Integer rank;
}
