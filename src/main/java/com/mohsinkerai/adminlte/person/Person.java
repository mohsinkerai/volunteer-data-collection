package com.mohsinkerai.adminlte.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mohsinkerai.adminlte.base.BaseEntity;
import com.mohsinkerai.adminlte.config.ProjectConstant;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

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
  @JoinColumn(name = "council_id")
  private com.mohsinkerai.adminlte.jamatkhana.Council council;

  @DateTimeFormat(pattern = ProjectConstant.DATE_HTML_FORMAT)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ProjectConstant.DATE_FORMAT)
  private LocalDate createdDate;
}
