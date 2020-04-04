package com.mohsinkerai.adminlte.person.updates;

import com.mohsinkerai.adminlte.base.SimpleBaseService;
import org.springframework.stereotype.Service;

@Service
public class PersonUpdatesService extends SimpleBaseService<PersonUpdates> {

  private final PersonUpdatesRepository personUpdatesRepository;

  protected PersonUpdatesService(
    PersonUpdatesRepository personUpdatesRepository) {
    super(personUpdatesRepository);
    this.personUpdatesRepository = personUpdatesRepository;
  }

  public void deleteByPersonId(long personId) {
    personUpdatesRepository.deleteByPersonId(personId);
  }
}
