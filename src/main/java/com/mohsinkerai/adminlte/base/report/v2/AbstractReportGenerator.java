package com.mohsinkerai.adminlte.base.report.v2;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mohsinkerai.adminlte.utils.JasperReportUtils;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public abstract class AbstractReportGenerator {

  protected final JasperReport jasperReport;

  public AbstractReportGenerator(String reportName) throws JRException {
    this.jasperReport = JasperReportUtils.loadReport(reportName);
  }

  public byte[] generatePDFReport(Collection<?> data) throws JRException {
    List<JasperPrint> jasperPrints = JasperReportUtils.generateJasperPrints(jasperReport, data);
    return JasperReportUtils.generatePDFReport(jasperPrints);
  }

  public byte[] generatePDFReport(Collection<?> data, Map<String, Object> params) throws JRException {
    List<JasperPrint> jasperPrints = JasperReportUtils.generateJasperPrints(jasperReport, data, params);
    return JasperReportUtils.generatePDFReport(jasperPrints);
  }

  public byte[] generateXLSXReport(Collection<?> data, Map<String, Object> params) throws JRException {
    List<JasperPrint> jasperPrints = JasperReportUtils.generateJasperPrints(jasperReport, data, params);
    return JasperReportUtils.generateExcelReport(jasperPrints);
  }
}
