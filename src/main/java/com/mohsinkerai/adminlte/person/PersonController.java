package com.mohsinkerai.adminlte.person;

import com.google.common.collect.ImmutableMap;
import com.mohsinkerai.adminlte.base.SimpleBaseController;
import com.mohsinkerai.adminlte.lookups.disease.Disease;
import com.mohsinkerai.adminlte.lookups.disease.DiseaseService;
import com.mohsinkerai.adminlte.lookups.health_facility.HealthFacilityService;
import com.mohsinkerai.adminlte.users.MyUser;
import com.mohsinkerai.adminlte.users.MyUserService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(PersonController.URL_PATH)
public class PersonController extends SimpleBaseController<Person> {

  public static final String URL_PATH = "person";

  private final MyUserService myUserService;
  private final PersonService personService;
  private final DiseaseService diseaseService;
  private final HealthFacilityService healthFacilityService;

  protected PersonController(
    PersonService personService,
    MyUserService myUserService,
    DiseaseService diseaseService,
    HealthFacilityService healthFacilityService) {
    super(personService);
    this.healthFacilityService = healthFacilityService;
    this.myUserService = myUserService;
    this.personService = personService;
    this.diseaseService = diseaseService;
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

  @Override
  protected Person getEmptyObject() {
    return new Person();
  }

  @Override
  protected Map<String, Object> getAttributes() {
    MyUser currentUser = myUserService.getCurrentLoggedInUser();
    List<Disease> diseases = diseaseService.findAll();
    return ImmutableMap.of("jks", currentUser.getCouncils(), "diseases", diseases, "healthFacilities", healthFacilityService.findAll());
  }

  @RequestMapping(value = {"add/cnic", "cnic", "edit/cnic"})
  public ResponseEntity<List<PersonShortDto>> findPersonWithThisCnic(@RequestParam String cnic) {
    return ResponseEntity.ok(personService.findByCnic(cnic));
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  public String delete(@PathVariable Long id) {
    return super.delete(id);
  }

  @Override
  @PreAuthorize("hasAuthority('ADMIN')")
  public String list(@PathVariable Integer pageNumber, Model model) {
    return super.list(pageNumber, model);
  }

  @RequestMapping(value = "/jk", method = RequestMethod.GET)
  public String jkList(Model model) {
    MyUser currentLoggedInUser = myUserService.getCurrentLoggedInUser();
    List<Person> list = personService.findByJamatkhanaIn(currentLoggedInUser.getCouncils());

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
  public String save(Person person, BindingResult bindingResult, Model model,
                     RedirectAttributes ra) {
    PersonShortDto personExist = cnicExists(person);
    if (personExist != null || bindingResult.hasErrors()) {
      model.addAttribute("data", person);
      if (personExist != null) {
        model.addAttribute("cnicError", "CNIC Already Exist with JK Name " + personExist.getCouncil().getName() + " and Person Name " + personExist.getPersonName() + " and form No " + personExist.getId());
      }
      model.addAttribute("org.springframework.validation.BindingResult.data", bindingResult);
      model.addAttribute("urlPath", urlPath());
      model.addAllAttributes(getAttributes());
      return viewPath() + "/form";
    }
    personService.save(person);
    ra.addFlashAttribute("formSaved", String
      .format("Successfully Saved Jamati Memeber with Name <b><mark>%s</mark></b> and id <mark><b>%04d</b></mark>", person.getName(),
        person.getId()));
    return "redirect:/" + urlPath() + "/add";
  }

  private PersonShortDto cnicExists(Person person) {
    List<PersonShortDto> persons = personService.findByCnic(person.getCnic());
    return persons.stream().filter(p -> !p.getId().equals(person.getId())).findFirst().orElse(null);
  }
}
