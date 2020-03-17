package com.mohsinkerai.adminlte.report.generator;

import com.mohsinkerai.adminlte.base.report.v2.AbstractReportGenerator;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Component;

@Component
public class PersonDumpReportGenerator extends AbstractReportGenerator {

  public PersonDumpReportGenerator() throws JRException {
    super("person-dump");
  }
}
