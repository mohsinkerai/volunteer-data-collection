package com.mohsinkerai.adminlte.report.generator;

import com.mohsinkerai.adminlte.base.report.v2.AbstractReportGenerator;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Component;

@Component
public class JamatkhanaRegistrationReportGenerator extends AbstractReportGenerator {

  public JamatkhanaRegistrationReportGenerator() throws JRException {
    super("jk-summary");
  }
}
