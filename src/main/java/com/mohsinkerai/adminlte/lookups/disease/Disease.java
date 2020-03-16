package com.mohsinkerai.adminlte.lookups.disease;

import com.mohsinkerai.adminlte.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Disease extends BaseEntity {

  private String name;
}
