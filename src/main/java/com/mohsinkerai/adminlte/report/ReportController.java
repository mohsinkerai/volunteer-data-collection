package com.mohsinkerai.adminlte.report;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mohsinkerai.adminlte.person.Person;
import com.mohsinkerai.adminlte.person.PersonService;
import com.mohsinkerai.adminlte.report.dto.JamatkhanaAndDateDto;
import com.mohsinkerai.adminlte.report.dto.JamatkhanaSummaryDto;
import com.mohsinkerai.adminlte.report.dto.UsernameAndDateDto;
import com.mohsinkerai.adminlte.report.generator.JamatkhanaRegistrationReportGenerator;
import com.mohsinkerai.adminlte.report.generator.PersonListReportGenerator;
import com.mohsinkerai.adminlte.report.validator.ReportValidator;
import com.mohsinkerai.adminlte.users.MyUser;
import com.mohsinkerai.adminlte.users.MyUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mohsinkerai.adminlte.report.ReportController.REPORT_CONTROLLER_NAME;

@RequestMapping(REPORT_CONTROLLER_NAME)
@Controller
@AllArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('LEAD')")
public class ReportController {

  public static final String REPORT_CONTROLLER_NAME = "report";

  private final MyUserService userService;
  private final PersonService personService;
  private final PersonListReportGenerator personListReportGenerator;
  private final JamatkhanaRegistrationReportGenerator jamatkhanaRegistrationReportGenerator;
  private final ReportValidator reportValidator;

  @GetMapping("forms/by-username")
  public String findFormsFilledByUsername(Model model) {
    UsernameAndDateDto dto = new UsernameAndDateDto(null, LocalDate.now(), LocalDate.now());

    List<String> userNames = userService
      .findAll()
      .stream()
      .map(MyUser::getUsername)
      .collect(Collectors.toList());

    model.addAttribute("users", userNames);
    model.addAttribute("data", dto);
    model.addAttribute("urlPath", REPORT_CONTROLLER_NAME + "/forms/by-username");

    log.info("Dto is {}", dto);
    return REPORT_CONTROLLER_NAME + "/by-username";
  }

  @GetMapping("forms/by-username/download")
  public HttpEntity<byte[]> getFormsFilledByUsername(UsernameAndDateDto usernameAndDateDto, Model model)
    throws JRException {
    LocalDate fromDate = usernameAndDateDto.getFromDate();
    LocalDate toDate = usernameAndDateDto.getToDate();
    String username = usernameAndDateDto.getUsername();

    List<Person> persons = personService
      .findByCreatedByAndCreatedOn(username, fromDate, toDate);
    log.info("Got DTO {}, persons {}", usernameAndDateDto, persons);

    ImmutableMap<String, Object> params = ImmutableMap.of(
      "REPORT_NAME", "Entries by " + username,
      "FROM_DATE", Date.valueOf(fromDate),
      "TO_DATE", Date.valueOf(toDate));

    // Return Report Here
    byte[] bytes = personListReportGenerator.generatePDFReport(persons, Maps.newHashMap(params));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + "persons-by-username-on-time" + ".pdf");

    return new HttpEntity<byte[]>(bytes, headers);
  }

  @GetMapping("forms/by-username/excel")
  public HttpEntity<byte[]> getFormsFilledByUsernameInExcel(UsernameAndDateDto usernameAndDateDto, Model model)
    throws JRException {
    LocalDate fromDate = usernameAndDateDto.getFromDate();
    LocalDate toDate = usernameAndDateDto.getToDate();
    String username = usernameAndDateDto.getUsername();

    List<Person> persons = personService
      .findByCreatedByAndCreatedOn(username, fromDate, toDate);
    log.info("Got DTO {}, persons {}", usernameAndDateDto, persons);

    ImmutableMap<String, Object> params = ImmutableMap.of(
      "REPORT_NAME", "Entries by " + username,
      "FROM_DATE", Date.valueOf(fromDate),
      "TO_DATE", Date.valueOf(toDate));

    // Return Report Here
    byte[] bytes = personListReportGenerator.generateXLSXReport(persons, Maps.newHashMap(params));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + "persons-by-username-on-time" + ".xlsx");

    return new HttpEntity<byte[]>(bytes, headers);
  }

  @GetMapping("forms/by-jamatkhana")
  public String findFormsFilledByJamatkhana(Model model) {
    JamatkhanaAndDateDto dto = new JamatkhanaAndDateDto(null, LocalDate.now(), LocalDate.now());

    Set<com.mohsinkerai.adminlte.jamatkhana.Council> jks = userService.getCurrentLoggedInUser().getCouncils();

    model.addAttribute("jks", jks);
    model.addAttribute("data", dto);
    model.addAttribute("urlPath", REPORT_CONTROLLER_NAME + "/forms/by-jamatkhana");

    log.info("Dto is {}", dto);
    return REPORT_CONTROLLER_NAME + "/by-jamatkhana";
  }

  @GetMapping("forms/by-jamatkhana/download")
  public HttpEntity<byte[]> getFormsFilledByUsername(JamatkhanaAndDateDto jamatkhanaAndDateDto, Model model) throws JRException {
    LocalDate fromDate = jamatkhanaAndDateDto.getFromDate();
    LocalDate toDate = jamatkhanaAndDateDto.getToDate();
    com.mohsinkerai.adminlte.jamatkhana.Council council = jamatkhanaAndDateDto.getCouncil();

    if (!reportValidator.isJkAllowed(council)) {
      throw new RuntimeException("Jamatkhana you are tring to search is not allowed");
    }

    List<JamatkhanaSummaryDto> jamatkhanaSummary = personService
      .findByJamatkhanaAndDateBetween(council, fromDate, toDate);

    ImmutableMap<String, Object> params = ImmutableMap.of(
      "REPORT_NAME", "Entries in JK " + council.getName(),
      "FROM_DATE", Date.valueOf(fromDate),
      "TO_DATE", Date.valueOf(toDate));

    // Return Report Here
    byte[] bytes = jamatkhanaRegistrationReportGenerator.generatePDFReport(jamatkhanaSummary, Maps.newHashMap(params));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + "persons-by-jamatkhana-on-time" + ".pdf");

    return new HttpEntity<byte[]>(bytes, headers);
  }

  @GetMapping("forms/by-jamatkhana-list")
  public String findFormsFilledByJamatkhanaList(Model model) {
    JamatkhanaAndDateDto dto = new JamatkhanaAndDateDto(null, LocalDate.now(), LocalDate.now());

    Set<com.mohsinkerai.adminlte.jamatkhana.Council> jks = userService.getCurrentLoggedInUser().getCouncils();

    model.addAttribute("jks", jks);
    model.addAttribute("data", dto);
    model.addAttribute("urlPath", REPORT_CONTROLLER_NAME + "/forms/by-jamatkhana-list");

    log.info("Dto is {}", dto);
    return REPORT_CONTROLLER_NAME + "/by-jamatkhana";
  }

  @GetMapping("forms/by-jamatkhana-list/download")
  public HttpEntity<byte[]> getFormsFilledByJamatkhanaList(JamatkhanaAndDateDto jamatkhanaAndDateDto, Model model)
    throws JRException {
    LocalDate fromDate = jamatkhanaAndDateDto.getFromDate();
    LocalDate toDate = jamatkhanaAndDateDto.getToDate();
    com.mohsinkerai.adminlte.jamatkhana.Council council = jamatkhanaAndDateDto.getCouncil();

    if (!reportValidator.isJkAllowed(council)) {
      throw new RuntimeException("Jamatkhana you are tring to search is not allowed");
    }

    List<Person> persons = personService.findByJamatkhanaAndCreatedDateBetween(council, fromDate, toDate);
    log.info("Got DTO {}, persons {}", jamatkhanaAndDateDto, persons);

    ImmutableMap<String, Object> params = ImmutableMap.of(
      "REPORT_NAME", "Persons in " + council,
      "FROM_DATE", Date.valueOf(fromDate),
      "TO_DATE", Date.valueOf(toDate));

    // Return Report Here
    byte[] bytes = personListReportGenerator.generatePDFReport(persons, Maps.newHashMap(params));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + "persons-by-jamatkhana-on-time" + ".pdf");

    return new HttpEntity<byte[]>(bytes, headers);
  }

  @GetMapping("forms/by-jamatkhana-list/excel")
  public HttpEntity<byte[]> getFormsFilledByJamatkhanaListExcel(JamatkhanaAndDateDto jamatkhanaAndDateDto, Model model)
    throws JRException {
    LocalDate fromDate = jamatkhanaAndDateDto.getFromDate();
    LocalDate toDate = jamatkhanaAndDateDto.getToDate();
    com.mohsinkerai.adminlte.jamatkhana.Council council = jamatkhanaAndDateDto.getCouncil();

    if (!reportValidator.isJkAllowed(council)) {
      throw new RuntimeException("Jamatkhana you are tring to search is not allowed");
    }

    List<Person> persons = personService.findByJamatkhanaAndCreatedDateBetween(council, fromDate, toDate);
    log.info("Got DTO {}, persons {}", jamatkhanaAndDateDto, persons);

    ImmutableMap<String, Object> params = ImmutableMap.of(
      "REPORT_NAME", "Persons in " + council,
      "FROM_DATE", Date.valueOf(fromDate),
      "TO_DATE", Date.valueOf(toDate));

    // Return Report Here
    byte[] bytes = personListReportGenerator.generateXLSXReport(persons, Maps.newHashMap(params));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + "persons-by-jamatkhana-on-time" + ".xlsx");

    return new HttpEntity<byte[]>(bytes, headers);
  }
}
