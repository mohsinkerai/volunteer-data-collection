package com.mohsinkerai.adminlte.person;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mohsinkerai.adminlte.base.SimpleBaseController;
import com.mohsinkerai.adminlte.jamatkhana.Jamatkhana;
import com.mohsinkerai.adminlte.person.updates.PersonUpdates;
import com.mohsinkerai.adminlte.person.updates.PersonUpdatesService;
import com.mohsinkerai.adminlte.users.MyUser;
import com.mohsinkerai.adminlte.users.MyUserService;
import com.mohsinkerai.adminlte.utils.VerificationUtils;
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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(PersonController.URL_PATH)
public class PersonController extends SimpleBaseController<Person> {

  public static final String URL_PATH = "person";

  private final MyUserService myUserService;
  private final PersonService personService;
  private final PersonUpdatesService personUpdatesService;

  protected PersonController(
    PersonService personService,
    MyUserService myUserService,
    PersonUpdatesService personUpdatesService) {
    super(personService);
    this.myUserService = myUserService;
    this.personService = personService;
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
    List<Person> list = personService.findByJamatkhanaIn(currentLoggedInUser.getJamatkhanas());

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
    if (bindingResult.hasErrors()) {
      model.addAttribute("data", person);
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

  @RequestMapping("{person}/updates/add")
  @PreAuthorize("hasAuthority('LEAD')")
  public String addComments(@PathVariable Person person, Model model) {
    model.addAttribute("urlPath", String.format("%s/%s/updates/add", urlPath(), person.getId()));

    MyUser currentLoggedInUser = myUserService.getCurrentLoggedInUser();
    VerificationUtils.hasValidJk(currentLoggedInUser, person);

    PersonUpdates personUpdates = new PersonUpdates();
    personUpdates.setPerson(person);

    model.addAttribute("data", personUpdates);
    return personUpdatesViewPath() + "/form";
  }

  @RequestMapping(value = "{person}/updates/add/save", method = RequestMethod.POST)
  @PreAuthorize("hasAuthority('LEAD')")
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
    personService.save(person);

    ra.addFlashAttribute("formSaved", String
      .format("Added Updates on Jamati Member with Name <b><mark>%s</mark></b> and id <mark><b>%04d</b></mark>", person.getName(),
        person.getId()));

    return "redirect:/" + String.format("%s/%s/updates", urlPath(), person.getId());
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

    model.addAttribute("urlPath", urlPath());

    model.addAttribute("list", updatedPerson.getPersonUpdates());
    model.addAttribute("person", updatedPerson);
    model.addAttribute("beginIndex", begin);
    model.addAttribute("endIndex", end);
    model.addAttribute("currentIndex", current);

    return personUpdatesViewPath() + "/list";
  }
}
