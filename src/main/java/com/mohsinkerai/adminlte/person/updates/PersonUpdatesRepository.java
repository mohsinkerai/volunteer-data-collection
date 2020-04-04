package com.mohsinkerai.adminlte.person.updates;

import com.mohsinkerai.adminlte.base.SimpleBaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonUpdatesRepository extends SimpleBaseRepository<PersonUpdates> {

  @Modifying
  @Query(nativeQuery = true, value = "DELETE FROM person_updates WHERE person_id = :personId")
  void deleteByPersonId(@Param("personId") long personId);
}