package hms.util;

import hms.config.Constants;
import hms.database.DatabaseConnection;
import hms.exception.DatabaseException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.pdf.JRPdfExporter;

import java.io.InputStream;
import java.util.Map;

public final class ReportUtil {

    private ReportUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static JasperReport loadReport(String reportName) throws JRException {
        String path = Constants.JASPER_REPORT_PATH + reportName + ".jasper";
        InputStream reportStream = ReportUtil.class.getResourceAsStream(path);
        if (reportStream == null) {
            throw new JRException("Report not found: " + path);
        }
        return (JasperReport) JRLoader.loadObject(reportStream);
    }

    public static JasperPrint fillReport(JasperReport report, Map<String, Object> parameters)
            throws JRException, DatabaseException {
        return JasperFillManager.fillReport(
                report, parameters, DatabaseConnection.getInstance().getConnection());
    }

    public static JasperPrint fillReport(JasperReport report, Map<String, Object> parameters,
                                         JRDataSource dataSource) throws JRException {
        return JasperFillManager.fillReport(report, parameters, dataSource);
    }

    public static void exportToPdf(JasperPrint jasperPrint, String outputPath)
            throws JRException {
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputPath));
        exporter.exportReport();
    }

    public static void generatePdfReport(String reportName, Map<String, Object> parameters,
                                         String outputPath) throws JRException, DatabaseException {
        JasperReport report = loadReport(reportName);
        JasperPrint jasperPrint = fillReport(report, parameters);
        exportToPdf(jasperPrint, outputPath);
    }
}
