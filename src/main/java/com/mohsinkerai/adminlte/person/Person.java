package com.mohsinkerai.adminlte.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mohsinkerai.adminlte.base.BaseEntity;
import com.mohsinkerai.adminlte.config.ProjectConstant;
import com.mohsinkerai.adminlte.jamatkhana.Jamatkhana;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@Entity
public class Person extends BaseEntity {

  private String name;
  private String gender;

//  @DateTimeFormat(pattern = "dd/MM/yyyy")
//  private LocalDate dateOfBirth;
  private int age;
  private String contactNumber;
  private String residentialAddress;

  // Nature of Complaint
  private String travelHistory;
  private String contactHistory;
  private String fever;
  private String cough;
  private String shortnessOfBreath;
  private String suspectStable; // Fever/Cough without shortness of breath

  private String recommendation;
  private String nameOfHospital;

  private String enteredByName;
  private String enteredByContact;
  private String remarks;

  @ManyToOne
  @JoinColumn(name = "jamatkhana_id")
  private Jamatkhana jamatkhana;

  private String jamatkhanaName;

  private String lastStatus = PersonStatus.INITIATED.name();
  private String lastRemarks = "";

  @DateTimeFormat(pattern = ProjectConstant.DATE_HTML_FORMAT)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ProjectConstant.DATE_FORMAT)
  private LocalDate createdDate;
}
