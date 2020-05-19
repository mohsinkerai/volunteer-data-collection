package com.mohsinkerai.adminlte.person;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mohsinkerai.adminlte.base.SimpleBaseController;
import com.mohsinkerai.adminlte.jamatkhana.Jamatkhana;
import com.mohsinkerai.adminlte.person.status.PersonStatus;
import com.mohsinkerai.adminlte.person.status.PersonStatusService;
import com.mohsinkerai.adminlte.person.updates.PersonUpdates;
import com.mohsinkerai.adminlte.person.updates.PersonUpdatesService;
import com.mohsinkerai.adminlte.users.MyUser;
import com.mohsinkerai.adminlte.users.MyUserService;
import com.mohsinkerai.adminlte.utils.StatusMapperUtils;
import com.mohsinkerai.adminlte.utils.VerificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
@RequestMapping(PersonController.URL_PATH)
public class PersonController extends SimpleBaseController<Person> {

  public static final String URL_PATH = "person";

  private final MyUserService myUserService;
  private final PersonService personService;
  private final PersonStatusService personStatusService;
  private final PersonUpdatesService personUpdatesService;

  protected PersonController(
    PersonService personService,
    MyUserService myUserService,
    PersonStatusService personStatusService,
    PersonUpdatesService personUpdatesService) {
    super(personService);
    this.myUserService = myUserService;
    this.personService = personService;
    this.personStatusService = personStatusService;
    this.personUpdatesService = personUpdatesService;
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(Date.class,
      new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true, 10));
  }

  @Override
  protected String urlPath() {
    return URL_PATH;
  }

  @Override
  protected String viewPath() {
    return URL_PATH;
  }

  protected String personUpdatesViewPath() {
    return URL_PATH + "/updates";
  }

  @Override
  protected Person getEmptyObject() {
    return new Person();
  }

  @Override
  protected Map<String, Object> getAttributes() {
    Set<Jamatkhana> jks = myUserService.getCurrentLoggedInUser().getJamatkhanas();
    ArrayList<Jamatkhana> jamatkhanas = Lists.newArrayList(jks);
    jamatkhanas.sort(Comparator.comparing(Jamatkhana::getName));
    return ImmutableMap.of("jks", jamatkhanas);
  }

  @Transactional
  @PreAuthorize("hasAuthority('LEAD')")
  @RequestMapping(value = "{person}/updates/add/save", method = RequestMethod.POST)
  public String saveComments(PersonUpdates personUpdates, BindingResult bindingResult, Model model, @PathVariable Person person, final RedirectAttributes ra) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("data", personUpdates);
      model.addAttribute("urlPath", String.format("%s/%s/updates/add", urlPath(), person.getId()));
      model.addAttribute("org.springframework.validation.BindingResult.data", bindingResult);
      return personUpdatesViewPath() + "/form";
    }

    MyUser currentLoggedInUser = myUserService.getCurrentLoggedInUser();
    VerificationUtils.hasValidJk(currentLoggedInUser, person);

    personUpdatesService.save(personUpdates);
    person.setLastRemarks(personUpdates.getRemarks());
    person.setLastStatus(personUpdates.getStatus());
    person.setLastCovidPositiveStatus(personUpdates.getCovidPositiveStatus());
    person.setLastSourceOfExposureDetails(personUpdates.getSourceOfExposureDetails());
    personService.update(person);

    ra.addFlashAttribute("formSaved", String
      .format("Added Updates on Jamati Member with Name <b><mark>%s</mark></b> and id <mark><b>%04d</b></mark>", person.getName(),
        person.getId()));

    return "redirect:/" + String.format("%s/%s/updates", urlPath(), person.getId());
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  public String list(@PathVariable Integer pageNumber, Model model) {
    List<Person> page = baseService.findAll();

    model.addAttribute("statusColor", personStatusService.getColorMap());

    int current = 1;
    int totalPages = 1;
    int begin = Math.max(1, current - PAGE_SIZE);
    int end = Math.min(begin + PAGE_SIZE, totalPages == 0 ? 1 : totalPages);

    model.addAttribute("urlPath", urlPath());

    model.addAttribute("list", page);
    model.addAttribute("beginIndex", begin);
    model.addAttribute("endIndex", end);
    model.addAttribute("currentIndex", current);

    return viewPath() + "/list";
  }

  @RequestMapping(value = "/jk", method = RequestMethod.GET)
  @PreAuthorize("hasAuthority('USER') || hasAuthority('LEAD')")
  public String jkList(Model model) {
    MyUser currentLoggedInUser = myUserService.getCurrentLoggedInUser();
    List<Person> list = personService.findByJamatkhanaIn(currentLoggedInUser.getJamatkhanas());

    model.addAttribute("statusColor", personStatusService.getColorMap());

    int current = 1;
    int totalPages = 1;
    int begin = Math.max(1, current - PAGE_SIZE);
    int end = Math.min(begin + PAGE_SIZE, totalPages == 0 ? 1 : totalPages);

    model.addAttribute("urlPath", urlPath());

    model.addAttribute("list", list);
    model.addAttribute("beginIndex", begin);
    model.addAttribute("endIndex", end);
    model.addAttribute("currentIndex", current);

    return viewPath() + "/list";
  }

  @Override
  public String edit(@PathVariable Long id, Model model) {
    boolean canEditPerson = personService.isPersonEditAllowed(id);
    if (!canEditPerson) {
      throw new RuntimeException("Person has Exceeded 2 Days and Cannot be Edited");
    }
    return super.edit(id, model);
  }

  @Override
  @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
  public String save(Person person, BindingResult bindingResult, Model model,
                     RedirectAttributes ra) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("data", person);
      model.addAttribute("org.springframework.validation.BindingResult.data", bindingResult);
      model.addAttribute("urlPath", urlPath());
      model.addAllAttributes(getAttributes());
      return viewPath() + "/form";
    }

    boolean newPerson = person.getId()==null;
    String recommendation = person.getRecommendation();
    String remarks = person.getRemarks();
    String status = StatusMapperUtils.getStatus(recommendation);
    person.setLastStatus(status);
    person.setLastRemarks(remarks);

    personService.save(person);

    if(newPerson) {
      PersonUpdates updates = new PersonUpdates();
      updates.setStatus(status);
      updates.setRemarks(remarks);
      updates.setCovidPositiveStatus(person.getLastCovidPositiveStatus());
      updates.setSourceOfExposureDetails(person.getLastSourceOfExposureDetails());
      updates.setPerson(person);
      personUpdatesService.save(updates);
    }

    ra.addFlashAttribute("formSaved", String
      .format("Successfully Saved Jamati Memeber with Name <b><mark>%s</mark></b> and id <mark><b>%04d</b></mark>", person.getName(),
        person.getId()));
    return "redirect:/" + urlPath() + "/add";
  }

  @RequestMapping("{person}/updates/add")
  @PreAuthorize("hasAuthority('LEAD')")
  public String addComments(@PathVariable Person person, Model model) {
    model.addAttribute("urlPath", String.format("%s/%s/updates/add", urlPath(), person.getId()));

    MyUser currentLoggedInUser = myUserService.getCurrentLoggedInUser();
    VerificationUtils.hasValidJk(currentLoggedInUser, person);

    PersonUpdates personUpdates = new PersonUpdates();
    personUpdates.setPerson(person);
    personUpdates.setCovidPositiveStatus(person.getLastCovidPositiveStatus());
    personUpdates.setSourceOfExposureDetails(person.getLastSourceOfExposureDetails());
    model.addAttribute("data", personUpdates);
    model.addAttribute("person", person);

    List<PersonStatus> personStatus = personStatusService.findAll();
    model.addAttribute("statuses", personStatus);
    model.addAttribute("statusColor", personStatusService.getColorMap());

    return personUpdatesViewPath() + "/form";
  }

  @RequestMapping("{person}/updates")
  @PreAuthorize("hasAuthority('LEAD')")
  public String viewUpdates(@PathVariable Person person, Model model) {
    MyUser currentLoggedInUser = myUserService.getCurrentLoggedInUser();
    VerificationUtils.hasValidJk(currentLoggedInUser, person);
    Person updatedPerson = personService.fetchUpdates(person);

    int current = 1;
    int totalPages = 1;
    int begin = Math.max(1, current - PAGE_SIZE);
    int end = Math.min(begin + PAGE_SIZE, totalPages == 0 ? 1 : totalPages);

    model.addAttribute("statusColor", personStatusService.getColorMap());

    model.addAttribute("urlPath", urlPath());

    model.addAttribute("list", updatedPerson.getPersonUpdates());
    model.addAttribute("person", updatedPerson);
    model.addAttribute("beginIndex", begin);
    model.addAttribute("endIndex", end);
    model.addAttribute("currentIndex", current);

    return personUpdatesViewPath() + "/list";
  }
}
