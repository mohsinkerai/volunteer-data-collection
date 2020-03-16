package com.mohsinkerai.adminlte.person;

import com.mohsinkerai.adminlte.base.SimpleBaseRepository;
import com.mohsinkerai.adminlte.report.dto.JamatkhanaSummaryDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface PersonRepository extends SimpleBaseRepository<Person> {

  List<Person> findByCreatedByAndCreatedDateBetween(String createdBy, LocalDate fromCreatedDate, LocalDate toCreatedDate);

  List<Person> findByJamatkhanaIn(Collection<com.mohsinkerai.adminlte.jamatkhana.Council> councils);

  @Query(value = "SELECT p.created_by as 'username', count(*) as 'forms' FROM person p INNER JOIN jamatkhana jk on jk.id = p.jamatkhana_id where p.created_date >=   ?2 and p.created_date <= ?3 and jk.id = ?1 group by p.created_by", nativeQuery = true)
  List<JamatkhanaSummaryDto> findByJamatkhanaAndDateBetween(Long jkId, LocalDate fromCreatedDate, LocalDate toCreatedDate);

  @Query(value = "SELECT jk.name as 'username', count(*) as 'forms' FROM person p INNER JOIN jamatkhana jk on jk.id = p.jamatkhana_id where p.created_date >=   ?1 and p.created_date <= ?2 group by jk.id, jk.name", nativeQuery = true)
  List<JamatkhanaSummaryDto> findBySummaryPerJamatkhanaBetween(LocalDate fromCreatedDate, LocalDate toCreatedDate);

  List<Person> findByCnic(String cnic);

  List<Person> findByJamatkhanaAndCreatedDateBetween(com.mohsinkerai.adminlte.jamatkhana.Council council, LocalDate fromCreatedDate, LocalDate toCreatedDate);
}