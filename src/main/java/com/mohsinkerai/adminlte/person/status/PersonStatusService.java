package com.mohsinkerai.adminlte.person.status;

import com.mohsinkerai.adminlte.base.SimpleBaseService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonStatusService extends SimpleBaseService<PersonStatus> {

  private final PersonStatusRepository personStatusRepository;

  protected PersonStatusService(
    PersonStatusRepository personStatusRepository) {
    super(personStatusRepository);
    this.personStatusRepository = personStatusRepository;
  }

  @Override
  public List<PersonStatus> findAll() {
    return personStatusRepository.findAll(Sort.by("rank").ascending());
  }

  public Map<String, String> getColorMap() {
    List<PersonStatus> personStatus = personStatusRepository.findAll();
    return personStatus.stream().collect(Collectors.toMap(PersonStatus::getName, PersonStatus::getColorCode));
  }
}
