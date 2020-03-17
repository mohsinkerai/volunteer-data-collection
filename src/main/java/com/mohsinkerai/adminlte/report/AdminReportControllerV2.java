package com.mohsinkerai.adminlte.report;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mohsinkerai.adminlte.person.Person;
import com.mohsinkerai.adminlte.person.PersonService;
import com.mohsinkerai.adminlte.report.dto.JamatkhanaAndDateDto;
import com.mohsinkerai.adminlte.report.dto.JamatkhanaSummaryDto;
import com.mohsinkerai.adminlte.report.generator.PersonDumpReportGenerator;
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

import static com.mohsinkerai.adminlte.report.AdminReportController.REPORT_CONTROLLER_NAME;

@RequestMapping(REPORT_CONTROLLER_NAME)
@Controller
@AllArgsConstructor
@Slf4j
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminReportControllerV2 {

  private final PersonService personService;
  private final PersonDumpReportGenerator personDumpReportGenerator;

  @GetMapping("forms/dump")
  public String formsDumpSelectorPage(Model model) {
    JamatkhanaAndDateDto dto = new JamatkhanaAndDateDto(null, LocalDate.now(), LocalDate.now());

    model.addAttribute("jks", Lists.newArrayList());
    model.addAttribute("data", dto);
    model.addAttribute("urlPath", REPORT_CONTROLLER_NAME + "/forms/dump");

    return "report/" + "by-jamatkhana";
  }

  @GetMapping("forms/dump/download")
  public HttpEntity<byte[]> formsDumper(JamatkhanaAndDateDto jamatkhanaAndDateDto, Model model) throws JRException {
    LocalDate fromDate = jamatkhanaAndDateDto.getFromDate();
    LocalDate toDate = jamatkhanaAndDateDto.getToDate();

    List<Person> jamatkhanaSummary = personService
      .findByCreatedDateBetween(fromDate, toDate);

    ImmutableMap<String, Object> params = ImmutableMap.of(
      "REPORT_NAME", "Entries by JK Summary",
      "FROM_DATE", Date.valueOf(fromDate),
      "TO_DATE", Date.valueOf(toDate));

    // Return Report Here
    byte[] bytes = personDumpReportGenerator.generateXLSXReport(jamatkhanaSummary, Maps.newHashMap(params));

    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + "persons-dump" + ".xlsx");

    return new HttpEntity<byte[]>(bytes, headers);
  }
}
