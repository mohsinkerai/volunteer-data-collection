package com.mohsinkerai.adminlte.report;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mohsinkerai.adminlte.jamatkhana.Jamatkhana;
import com.mohsinkerai.adminlte.person.Person;
import com.mohsinkerai.adminlte.person.PersonService;
import com.mohsinkerai.adminlte.report.dto.JamatkhanaAndDateDto;
import com.mohsinkerai.adminlte.report.generator.PersonDumpReportGenerator;
import com.mohsinkerai.adminlte.report.validator.ReportValidator;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.mohsinkerai.adminlte.report.ReportingController.REPORT_CONTROLLER_NAME;


@RequestMapping(REPORT_CONTROLLER_NAME)
@Controller
@AllArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('REPORT')")
public class ReportingController {

  public static final String REPORT_CONTROLLER_NAME = "user/report";

  private final ReportValidator reportValidator;
  private final MyUserService myUserService;
  private final PersonService personService;
  private final PersonDumpReportGenerator personDumpReportGenerator;

  @GetMapping("forms/dump")
  public String formsDumpSelectorPage(Model model) {
    JamatkhanaAndDateDto dto = new JamatkhanaAndDateDto(null, LocalDate.now(), LocalDate.now());

    Set<Jamatkhana> jks = myUserService.getCurrentLoggedInUser().getJamatkhanas();
    ArrayList<Jamatkhana> jamatkhanas = Lists.newArrayList(jks);
    jamatkhanas.sort(Comparator.comparing(Jamatkhana::getName));

    model.addAttribute("jks", jamatkhanas);
    model.addAttribute("data", dto);
    model.addAttribute("urlPath", ReportingController.REPORT_CONTROLLER_NAME + "/forms/dump");

    return "report/" + "by-jamatkhana";
  }

  @GetMapping("forms/dump/download")
  public HttpEntity<byte[]> formsDumper(JamatkhanaAndDateDto jamatkhanaAndDateDto, Model model) throws JRException {
    LocalDate fromDate = jamatkhanaAndDateDto.getFromDate();
    LocalDate toDate = jamatkhanaAndDateDto.getToDate();
    Jamatkhana jamatkhana = jamatkhanaAndDateDto.getJamatkhana();

    if (!reportValidator.isJkAllowed(jamatkhana)) {
      throw new RuntimeException("Jamatkhana you are trying to search is not allowed");
    }

    List<Person> jamatkhanaSummary = personService
      .findByJamatkhanaAndCreatedDateBetween(jamatkhana, fromDate, toDate);

    ImmutableMap<String, Object> params = ImmutableMap.of(
      "REPORT_NAME", "Entries by JK Summary",
      "FROM_DATE", Date.valueOf(fromDate),
      "TO_DATE", Date.valueOf(toDate));

    // Return Report Here
    byte[] bytes = personDumpReportGenerator.generateXLSXReport(jamatkhanaSummary, Maps.newHashMap(params));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + "persons-dump-" + jamatkhana.getName() + ".xlsx");

    return new HttpEntity<byte[]>(bytes, headers);
  }

  @GetMapping("forms/all/dump")
  public String formsDumpAllAssignedJKSelectorPage(Model model) {
    JamatkhanaAndDateDto dto = new JamatkhanaAndDateDto(null, LocalDate.now(), LocalDate.now());

    model.addAttribute("jks", Lists.newArrayList());
    model.addAttribute("data", dto);
    model.addAttribute("urlPath", ReportingController.REPORT_CONTROLLER_NAME + "/forms/all/dump");

    return "report/" + "by-jamatkhana";
  }

  @GetMapping("forms/all/dump/download")
  public HttpEntity<byte[]> formsDumperAllAssignedJK(JamatkhanaAndDateDto jamatkhanaAndDateDto, Model model) throws JRException {
    LocalDate fromDate = jamatkhanaAndDateDto.getFromDate();
    LocalDate toDate = jamatkhanaAndDateDto.getToDate();
    Set<Jamatkhana> jamatkhanas = myUserService.getCurrentLoggedInUser().getJamatkhanas();

    List<Person> jamatkhanaSummary = personService
      .findByJamatkhanaInAndCreatedDateBetween(jamatkhanas, fromDate, toDate);

    ImmutableMap<String, Object> params = ImmutableMap.of(
      "REPORT_NAME", "Entries by JK Summary",
      "FROM_DATE", Date.valueOf(fromDate),
      "TO_DATE", Date.valueOf(toDate));

    // Return Report Here
    byte[] bytes = personDumpReportGenerator.generateXLSXReport(jamatkhanaSummary, Maps.newHashMap(params));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + "persons-dump-all-assigned-jk" + ".xlsx");

    return new HttpEntity<byte[]>(bytes, headers);
  }
}
