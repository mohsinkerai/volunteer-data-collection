package com.mohsinkerai.adminlte.person;

import com.mohsinkerai.adminlte.base.BaseEntity;
import com.mohsinkerai.adminlte.base.SimpleBaseService;
import com.mohsinkerai.adminlte.jamatkhana.Jamatkhana;
import com.mohsinkerai.adminlte.person.updates.PersonUpdates;
import com.mohsinkerai.adminlte.report.dto.JamatkhanaSummaryDto;
import com.mohsinkerai.adminlte.users.MyUser;
import com.mohsinkerai.adminlte.users.MyUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService extends SimpleBaseService<Person> {

  private final PersonRepository personRepository;
  private final MyUserService myUserService;

  protected PersonService(
    PersonRepository personRepository, MyUserService myUserService) {
    super(personRepository);
    this.personRepository = personRepository;
    this.myUserService = myUserService;
  }

  public List<Person> findByCreatedByAndCreatedOn(String username, LocalDate fromDate,
    LocalDate toDate) {
    return personRepository.findByCreatedByAndCreatedDateBetween(username, fromDate, toDate);
  }

  public Person update(Person person) {
    if (person.getId() == null || person.getCreatedDate() == null) {
      person.setCreatedDate(LocalDate.now());
    }
    myUserService.getCurrentLoggedInUser()
      .getJamatkhanas()
      .stream()
      .filter(jk -> jk.getName().equals(person.getJamatkhana().getName()))
      .findAny().orElseThrow(() -> new RuntimeException("Invalid JK Selected !!"));
    return personRepository.save(person);
  }

  @Override
  public Person save(Person person) {
    if (person.getId() == null || person.getCreatedDate() == null) {
      person.setCreatedDate(LocalDate.now());
    } else if (!isPersonEditAllowed(person.getId())) {
      throw new RuntimeException("Person Editing Not Allowed");
    }

    myUserService.getCurrentLoggedInUser()
      .getJamatkhanas()
      .stream()
      .filter(jk -> jk.getName().equals(person.getJamatkhana().getName()))
      .findAny().orElseThrow(() -> new RuntimeException("Invalid JK Selected !!"));

    return super.save(person);
  }

  public boolean isPersonEditAllowed(Long personId) {
    Person person = findOne(personId)
      .orElseThrow(() -> new RuntimeException(String.format("Id %d Doesn't Exist", personId)));
    MyUser currentLoggedInUser = myUserService.getCurrentLoggedInUser();
    return (ChronoUnit.DAYS.between(person.getCreatedDate(), LocalDate.now()) < 1 && hasRole(currentLoggedInUser, "USER"))
      || hasRole(currentLoggedInUser, "ADMIN");
  }

  public List<Person> findByJamatkhanaIn(Collection<Jamatkhana> jamatkhanas) {
    return personRepository.findByJamatkhanaIn(jamatkhanas);
  }

  public List<JamatkhanaSummaryDto> findByJamatkhanaAndDateBetween(Jamatkhana jamatkhana, LocalDate fromCreatedDate, LocalDate toCreatedDate) {
    return personRepository.findByJamatkhanaAndDateBetween(jamatkhana.getId(), fromCreatedDate, toCreatedDate);
  }

  public List<JamatkhanaSummaryDto> findBySummaryPerJamatkhanaBetween(LocalDate fromCreatedDate, LocalDate toCreatedDate) {
    return personRepository.findBySummaryPerJamatkhanaBetween(fromCreatedDate, toCreatedDate);
  }

  public List<Person> findByJamatkhanaAndCreatedDateBetween(Jamatkhana jamatkhana, LocalDate fromCreatedDate, LocalDate toCreatedDate) {
    return personRepository.findByJamatkhanaAndCreatedDateBetween(jamatkhana, fromCreatedDate, toCreatedDate);
  }

  public List<Person> findByJamatkhanaInAndCreatedDateBetween(Collection<Jamatkhana> jamatkhanas, LocalDate fromCreatedDate, LocalDate toCreatedDate) {
    return personRepository.findByJamatkhanaInAndCreatedDateBetween(jamatkhanas, fromCreatedDate, toCreatedDate);
  }

  public List<Person> findByCreatedDateBetween(LocalDate fromCreatedDate, LocalDate toCreatedDate) {
    return personRepository.findByCreatedDateBetween(fromCreatedDate, toCreatedDate);
  }

  @Transactional
  public Person fetchUpdates(Person person) {
    List<PersonUpdates> personUpdates = person.getPersonUpdates();
    personUpdates.sort(Comparator.comparing(BaseEntity::getCreatedOn).reversed());
    return person;
  }

  private boolean hasRole(MyUser currentUser, String role) {
    return currentUser.getAuthorities()
      .stream()
      .map(GrantedAuthority::getAuthority)
      .map(String::toLowerCase)
      .filter(s -> s.equals(role.toLowerCase()))
      .findAny()
      .isPresent();
  }
}
