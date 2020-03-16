package com.mohsinkerai.adminlte.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxExporterConfiguration;

@Slf4j
public final class JasperReportUtils {

  /**
   * Convert Data into Jasper Prints
   */
  public static List<JasperPrint> generateJasperPrints(JasperReport inputJasper, Collection<?> data, Map<String, Object> params) throws JRException {
    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(data);
    JasperPrint jasperPrint = JasperFillManager.fillReport(inputJasper, params, ds);
    return ImmutableList.of(jasperPrint);
  }

  /**
   * Convert Data into Jasper Prints
   */
  public static List<JasperPrint> generateJasperPrints(JasperReport inputJasper, Collection<?> data) throws JRException {
    return generateJasperPrints(inputJasper, data, Maps.newHashMap());
  }

  /**
   * Given Jasper Prints, It merges all of them (In-Order of List) and generate a PDF out ot it.
   */
  public static final byte[] generatePDFReport(List<JasperPrint> jasperPrints) throws JRException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    JRPdfExporter exporter = new JRPdfExporter();
    exporter.setExporterInput(SimpleExporterInput
      .getInstance(jasperPrints)); //Set as export input my list with JasperPrint s
    exporter
      .setExporterOutput(new SimpleOutputStreamExporterOutput(out)); //or any other out streaam
    SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
    configuration.setCreatingBatchModeBookmarks(true); //add this so your bookmarks work, you may set other parameters
    configuration.setMetadataTitle("Report Title Generated by Jasper");
    exporter.setConfiguration(configuration);
    exporter.exportReport();

    return out.toByteArray();
  }

  /**
   * Given Jasper Prints, It merges all of them (In-Order of List) and generate a PDF out ot it.
   */
  public static final byte[] generateExcelReport(List<JasperPrint> jasperPrints) throws JRException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    JRXlsxExporter exporter = new JRXlsxExporter();
    exporter.setExporterInput(SimpleExporterInput
      .getInstance(jasperPrints)); //Set as export input my list with JasperPrint s
    exporter
      .setExporterOutput(new SimpleOutputStreamExporterOutput(out)); //or any other out streaam
    SimpleXlsxExporterConfiguration configuration = new SimpleXlsxExporterConfiguration();
    configuration.setMetadataTitle("Report Title Generated by Jasper");
    exporter.setConfiguration(configuration);
    exporter.exportReport();

    return out.toByteArray();
  }

  public static JasperReport loadReport(String reportName) throws JRException {
    String path = String.format("/reports/%s.jrxml", reportName);
    log.info("Report Path is {}", path);
    System.out.println(path);

    InputStream employeeReportStream = JasperReportUtils.class.getResourceAsStream(path);
    return JasperCompileManager.compileReport(employeeReportStream);
  }
}