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
  private String residentialAddress;
  private String contactNumber;
  private int houseHoldMembersCount;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "health_facility_accessed",
    joinColumns = @JoinColumn(name = "person_id"),
    inverseJoinColumns = @JoinColumn(name = "health_facility_id")
  )
  private Set<HealthFacility> healthFacilities = Sets.newHashSet();

  @Size(min = 0, max = 15)
  private String otherMedicalFacilityAccessed;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "disease_diagnosed",
    joinColumns = @JoinColumn(name = "person_id"),
    inverseJoinColumns = @JoinColumn(name = "disease_id")
  )
  private Set<Disease> diseases = Sets.newHashSet();

  @Max(1)
  @Min(0)
  // In the last 12 months have you visited doctor / hospital for medical checkup?
  private int visitedDoctorForCheckup;

  @Max(1)
  @Min(0)
  // Do you smoke?
  private int smoke;

  @Max(1)
  @Min(0)
  // Insurance Coverage
  private int insuranceCoverage;

  // Self Covered Insurance
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean selfInsuranceCoverage;

  // Employer Covered Insurance
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean employerInsuranceCoverage;

  // Do you have any of the following disabilities?
  @ElementCollection
  @CollectionTable(name = "disability", joinColumns = @JoinColumn(name = "person_id"))
  @Column(name = "disability")
  private List<String> disability = Lists.newArrayList();

  private String otherDisability;

  @ManyToOne
  @JoinColumn(name = "jamatkhana_id")
  private Jamatkhana jamatkhana;

  @DateTimeFormat(pattern = ProjectConstant.DATE_HTML_FORMAT)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ProjectConstant.DATE_FORMAT)
  private LocalDate createdDate;
  private String uuid;
}
