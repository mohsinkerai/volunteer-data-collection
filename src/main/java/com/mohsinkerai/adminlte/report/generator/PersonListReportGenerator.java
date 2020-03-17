package com.mohsinkerai.adminlte.report.generator;

import com.mohsinkerai.adminlte.base.report.v2.AbstractReportGenerator;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Component;

//@Component
public class PersonListReportGenerator extends AbstractReportGenerator {

  public PersonListReportGenerator() throws JRException {
    super("persons-list");
  }
}
