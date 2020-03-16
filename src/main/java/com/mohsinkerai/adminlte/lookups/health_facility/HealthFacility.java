package com.mohsinkerai.adminlte.lookups.health_facility;

import com.mohsinkerai.adminlte.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class HealthFacility extends BaseEntity {

  private String name;
}
