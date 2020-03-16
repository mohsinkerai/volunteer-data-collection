package com.mohsinkerai.adminlte.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mohsinkerai.adminlte.base.BaseEntity;
import com.mohsinkerai.adminlte.config.ProjectConstant;
import com.mohsinkerai.adminlte.jamatkhana.Jamatkhana;
import com.mohsinkerai.adminlte.lookups.disease.Disease;
import com.mohsinkerai.adminlte.lookups.health_facility.HealthFacility;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Person extends BaseEntity {

  private String name;
  private String cnic;
  private String gender;

  @DateTimeFormat(pattern = "dd/MM/yyyy")
  private LocalDate dateOfBirth;
  private String contactNumber;
  private String residentialAddress;

  private String travelHistory;
  private String contactHistory;
  private String fever;
  private String cough;
  private String shortnessOfBreath;
  private String suspectStable; // Fever/Cough without shortness of breath
  private String suspectUnstable; // Fever/Cough without shortness of breath

  private String isolationAtHome;
  private String referToHospital;

  @ManyToOne
  @JoinColumn(name = "jamatkhana_id")
  private Jamatkhana jamatkhana;

  @DateTimeFormat(pattern = ProjectConstant.DATE_HTML_FORMAT)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ProjectConstant.DATE_FORMAT)
  private LocalDate createdDate;
}
