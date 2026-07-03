package hms.util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import java.io.IOException;
import java.util.Date;

public final class ReportCompiler {

    private ReportCompiler() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void main(String[] args) {
        String basePath = "src/hms/reports/";
        try {
            compileOccupancyReport(basePath + "occupancy_report.jasper");
            compileInvoiceReport(basePath + "invoice_report.jasper");
            System.out.println("Report compilation complete.");
        } catch (JRException | IOException e) {
            System.err.println("Failed: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private static void compileOccupancyReport(String outputPath) throws JRException, IOException {
        JasperDesign design = new JasperDesign();
        design.setName("occupancy_report");
        design.setPageWidth(595);
        design.setPageHeight(842);
        design.setColumnWidth(555);
        design.setLeftMargin(20);
        design.setRightMargin(20);
        design.setTopMargin(20);
        design.setBottomMargin(20);

        // Parameters
        JRDesignParameter startParam = new JRDesignParameter();
        startParam.setName("start_date");
        startParam.setValueClass(Date.class);
        design.addParameter(startParam);

        JRDesignParameter endParam = new JRDesignParameter();
        endParam.setName("end_date");
        endParam.setValueClass(Date.class);
        design.addParameter(endParam);

        JRDesignParameter roomTypeParam = new JRDesignParameter();
        roomTypeParam.setName("room_type");
        roomTypeParam.setValueClass(String.class);
        design.addParameter(roomTypeParam);

        // Query
        JRDesignQuery query = new JRDesignQuery();
        query.setText("SELECT r.room_type, COUNT(DISTINCT r.room_id) AS total_rooms, COUNT(DISTINCT CASE WHEN r.status = 'occupied' OR r.status = 'reserved' THEN r.room_id END) AS occupied_rooms, COALESCE(SUM(b.total_bill), 0) AS total_revenue FROM rooms r LEFT JOIN reservations res ON r.room_id = res.room_id AND res.check_in_date <= $P{end_date} AND res.check_out_date >= $P{start_date} AND res.status IN ('checked_in', 'checked_out') LEFT JOIN billing b ON res.reservation_id = b.reservation_id WHERE ($P{room_type} IS NULL OR $P{room_type} = '' OR r.room_type = $P{room_type}) GROUP BY r.room_type ORDER BY r.room_type");
        design.setQuery(query);

        // Fields
        addField(design, "room_type", String.class);
        addField(design, "total_rooms", Long.class);
        addField(design, "occupied_rooms", Long.class);
        addField(design, "total_revenue", Double.class);

        // Title band
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(60);

        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setX(0);
        titleText.setY(10);
        titleText.setWidth(555);
        titleText.setHeight(30);
        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleText.setFontSize(18f);
        titleText.setBold(true);
        titleText.setText("Occupancy & Revenue Report");
        titleBand.addElement(titleText);

        JRDesignTextField periodField = new JRDesignTextField();
        periodField.setX(0);
        periodField.setY(45);
        periodField.setWidth(555);
        periodField.setHeight(15);
        periodField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        periodField.setFontSize(10f);
        JRDesignExpression periodExpr = new JRDesignExpression();
        periodExpr.setText("\"Period: \" + $P{start_date} + \" to \" + $P{end_date}");
        periodField.setExpression(periodExpr);
        titleBand.addElement(periodField);

        design.setTitle(titleBand);

        // Column header band
        JRDesignBand colHeaderBand = new JRDesignBand();
        colHeaderBand.setHeight(20);
        colHeaderBand.addElement(createColumnHeader("Room Type", 0, 150));
        colHeaderBand.addElement(createColumnHeader("Total Rooms", 150, 100));
        colHeaderBand.addElement(createColumnHeader("Occupied Rooms", 250, 120));
        colHeaderBand.addElement(createColumnHeader("Total Revenue", 370, 185));
        design.setColumnHeader(colHeaderBand);

        // Detail band
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(20);
        detailBand.addElement(createDetailField("$F{room_type}", 0, 150, HorizontalTextAlignEnum.LEFT));
        detailBand.addElement(createDetailField("$F{total_rooms}", 150, 100, HorizontalTextAlignEnum.CENTER));
        detailBand.addElement(createDetailField("$F{occupied_rooms}", 250, 120, HorizontalTextAlignEnum.CENTER));
        JRDesignTextField revField = createDetailField("\"$\" + String.format(\"%.2f\", $F{total_revenue})", 370, 185, HorizontalTextAlignEnum.RIGHT);
        detailBand.addElement(revField);
        ((net.sf.jasperreports.engine.design.JRDesignSection) design.getDetailSection()).addBand(detailBand);

        JasperReport report = JasperCompileManager.compileReport(design);
        JRSaver.saveObject(report, outputPath);
        System.out.println("Compiled: occupancy_report -> " + outputPath);
    }

    private static void compileInvoiceReport(String outputPath) throws JRException, IOException {
        JasperDesign design = new JasperDesign();
        design.setName("invoice_report");
        design.setPageWidth(842);
        design.setPageHeight(595);
        design.setColumnWidth(802);
        design.setLeftMargin(20);
        design.setRightMargin(20);
        design.setTopMargin(20);
        design.setBottomMargin(20);

        // Parameters
        JRDesignParameter startParam = new JRDesignParameter();
        startParam.setName("start_date");
        startParam.setValueClass(Date.class);
        design.addParameter(startParam);

        JRDesignParameter endParam = new JRDesignParameter();
        endParam.setName("end_date");
        endParam.setValueClass(Date.class);
        design.addParameter(endParam);

        // Query
        JRDesignQuery query = new JRDesignQuery();
        query.setText("SELECT g.first_name, g.last_name, g.email, g.phone, res.display_id, res.check_in_date, res.check_out_date, r.room_number, r.room_type, r.base_price, b.room_charge, b.service_charge, b.other_charges, b.discount_amount, b.late_charge, b.tax_amount, b.total_bill, b.payment_status FROM reservations res JOIN guests g ON res.guest_id = g.guest_id JOIN rooms r ON res.room_id = r.room_id JOIN billing b ON res.reservation_id = b.reservation_id WHERE ($P{start_date} IS NULL OR res.check_in_date >= $P{start_date}) AND ($P{end_date} IS NULL OR res.check_in_date <= $P{end_date}) ORDER BY res.check_in_date DESC");
        design.setQuery(query);

        // Fields
        addField(design, "first_name", String.class);
        addField(design, "last_name", String.class);
        addField(design, "email", String.class);
        addField(design, "phone", String.class);
        addField(design, "display_id", String.class);
        addField(design, "check_in_date", java.sql.Date.class);
        addField(design, "check_out_date", java.sql.Date.class);
        addField(design, "room_number", String.class);
        addField(design, "room_type", String.class);
        addField(design, "base_price", Double.class);
        addField(design, "room_charge", Double.class);
        addField(design, "service_charge", Double.class);
        addField(design, "other_charges", Double.class);
        addField(design, "discount_amount", Double.class);
        addField(design, "late_charge", Double.class);
        addField(design, "tax_amount", Double.class);
        addField(design, "total_bill", Double.class);
        addField(design, "payment_status", String.class);

        // Title band
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(40);
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setX(0);
        titleText.setY(5);
        titleText.setWidth(802);
        titleText.setHeight(30);
        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleText.setFontSize(16f);
        titleText.setBold(true);
        titleText.setText("Guest Invoice Report");
        titleBand.addElement(titleText);
        design.setTitle(titleBand);

        // Column header band
        JRDesignBand colHeaderBand = new JRDesignBand();
        colHeaderBand.setHeight(20);
        int x = 0;
        colHeaderBand.addElement(createColumnHeader("Guest Name", x, 130)); x += 130;
        colHeaderBand.addElement(createColumnHeader("Reservation #", x, 100)); x += 100;
        colHeaderBand.addElement(createColumnHeader("Check-In", x, 60)); x += 60;
        colHeaderBand.addElement(createColumnHeader("Check-Out", x, 60)); x += 60;
        colHeaderBand.addElement(createColumnHeader("Room", x, 55)); x += 55;
        colHeaderBand.addElement(createColumnHeader("Room Charge", x, 55)); x += 55;
        colHeaderBand.addElement(createColumnHeader("Service Chg", x, 55)); x += 55;
        colHeaderBand.addElement(createColumnHeader("Discount", x, 50)); x += 50;
        colHeaderBand.addElement(createColumnHeader("Late Fee", x, 50)); x += 50;
        colHeaderBand.addElement(createColumnHeader("Tax", x, 50)); x += 50;
        colHeaderBand.addElement(createColumnHeader("Total Bill", x, 70)); x += 70;
        colHeaderBand.addElement(createColumnHeader("Status", x, 67));
        design.setColumnHeader(colHeaderBand);

        // Detail band
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(18);
        x = 0;
        detailBand.addElement(createDetailField("$F{first_name} + \" \" + $F{last_name}", x, 130, HorizontalTextAlignEnum.LEFT)); x += 130;
        detailBand.addElement(createDetailField("$F{display_id}", x, 100, HorizontalTextAlignEnum.LEFT)); x += 100;
        detailBand.addElement(createDetailField("$F{check_in_date}", x, 60, HorizontalTextAlignEnum.LEFT)); x += 60;
        detailBand.addElement(createDetailField("$F{check_out_date}", x, 60, HorizontalTextAlignEnum.LEFT)); x += 60;
        detailBand.addElement(createDetailField("$F{room_number}", x, 55, HorizontalTextAlignEnum.LEFT)); x += 55;
        detailBand.addElement(createDetailField("String.format(\"%.2f\", $F{room_charge})", x, 55, HorizontalTextAlignEnum.RIGHT)); x += 55;
        detailBand.addElement(createDetailField("String.format(\"%.2f\", $F{service_charge})", x, 55, HorizontalTextAlignEnum.RIGHT)); x += 55;
        detailBand.addElement(createDetailField("String.format(\"%.2f\", $F{discount_amount})", x, 50, HorizontalTextAlignEnum.RIGHT)); x += 50;
        detailBand.addElement(createDetailField("String.format(\"%.2f\", $F{late_charge})", x, 50, HorizontalTextAlignEnum.RIGHT)); x += 50;
        detailBand.addElement(createDetailField("String.format(\"%.2f\", $F{tax_amount})", x, 50, HorizontalTextAlignEnum.RIGHT)); x += 50;
        detailBand.addElement(createDetailField("String.format(\"%.2f\", $F{total_bill})", x, 70, HorizontalTextAlignEnum.RIGHT)); x += 70;
        detailBand.addElement(createDetailField("$F{payment_status}", x, 67, HorizontalTextAlignEnum.CENTER));
        ((net.sf.jasperreports.engine.design.JRDesignSection) design.getDetailSection()).addBand(detailBand);

        JasperReport report = JasperCompileManager.compileReport(design);
        JRSaver.saveObject(report, outputPath);
        System.out.println("Compiled: invoice_report -> " + outputPath);
    }

    private static void addField(JasperDesign design, String name, Class<?> valueClass) throws JRException {
        JRDesignField field = new JRDesignField();
        field.setName(name);
        field.setValueClass(valueClass);
        design.addField(field);
    }

    private static JRDesignStaticText createColumnHeader(String text, int x, int width) {
        JRDesignStaticText st = new JRDesignStaticText();
        st.setX(x);
        st.setY(0);
        st.setWidth(width);
        st.setHeight(20);
        st.setBold(true);
        st.setFontSize(8f);
        st.setText(text);
        return st;
    }

    private static JRDesignTextField createDetailField(String expression, int x, int width, HorizontalTextAlignEnum align) {
        JRDesignTextField tf = new JRDesignTextField();
        tf.setX(x);
        tf.setY(0);
        tf.setWidth(width);
        tf.setHeight(18);
        tf.setFontSize(8f);
        tf.setHorizontalTextAlign(align);
        JRDesignExpression expr = new JRDesignExpression();
        expr.setText(expression);
        tf.setExpression(expr);
        return tf;
    }
}
