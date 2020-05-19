package com.mohsinkerai.adminlte.person.updates;

import com.mohsinkerai.adminlte.base.BaseEntity;
import com.mohsinkerai.adminlte.person.Person;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
@ToString(exclude = "person")
public class PersonUpdates extends BaseEntity {

  private String status;
  private String remarks;
  private String covidPositiveStatus;
  private String sourceOfExposureDetails;

  @ManyToOne
  @JoinColumn(name = "person_id")
  private Person person;
}
