package hms.util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRSaver;
import java.awt.Color;
import java.io.IOException;
import java.util.Date;

public final class ReportCompiler {

    private static final Color BORDER_COLOR = new Color(204, 204, 204);

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

    private static void applyBorder(JRLineBox box) {
        box.getTopPen().setLineWidth(0.5f);
        box.getTopPen().setLineColor(BORDER_COLOR);
        box.getLeftPen().setLineWidth(0.5f);
        box.getLeftPen().setLineColor(BORDER_COLOR);
        box.getBottomPen().setLineWidth(0.5f);
        box.getBottomPen().setLineColor(BORDER_COLOR);
        box.getRightPen().setLineWidth(0.5f);
        box.getRightPen().setLineColor(BORDER_COLOR);
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

        addParam(design, "start_date", Date.class);
        addParam(design, "end_date", Date.class);
        addParam(design, "room_type", String.class);
        addParam(design, "status", String.class);

        JRDesignQuery query = new JRDesignQuery();
        query.setText("SELECT r.room_type, COUNT(DISTINCT r.room_id) AS total_rooms, COUNT(DISTINCT CASE WHEN r.status = 'occupied' OR r.status = 'reserved' THEN r.room_id END) AS occupied_rooms, COALESCE(SUM(b.total_bill), 0) AS total_revenue FROM rooms r LEFT JOIN reservations res ON r.room_id = res.room_id AND ($P{end_date} IS NULL OR res.check_in_date <= $P{end_date}) AND ($P{start_date} IS NULL OR res.check_out_date >= $P{start_date}) AND res.status IN ('checked_in', 'checked_out') LEFT JOIN billing b ON res.reservation_id = b.reservation_id AND ($P{status} IS NULL OR $P{status} = '' OR b.payment_status = $P{status}) WHERE ($P{room_type} IS NULL OR $P{room_type} = '' OR r.room_type = $P{room_type}) GROUP BY r.room_type ORDER BY r.room_type");
        design.setQuery(query);

        addField(design, "room_type", String.class);
        addField(design, "total_rooms", Long.class);
        addField(design, "occupied_rooms", Long.class);
        addField(design, "total_revenue", Double.class);

        JRDesignStyle detailStyle = new JRDesignStyle();
        detailStyle.setName("detailStyle");
        detailStyle.setMode(ModeEnum.OPAQUE);
        detailStyle.setBackcolor(Color.WHITE);
        detailStyle.setFontName("SansSerif");
        detailStyle.setFontSize(10f);
        applyBorder(detailStyle.getLineBox());
        JRDesignConditionalStyle altStyle = new JRDesignConditionalStyle();
        JRDesignExpression condExpr = new JRDesignExpression();
        condExpr.setText("$V{REPORT_COUNT} % 2 == 0");
        altStyle.setConditionExpression(condExpr);
        altStyle.setBackcolor(new Color(235, 245, 251));
        detailStyle.addConditionalStyle(altStyle);
        design.addStyle(detailStyle);

        // Page header
        JRDesignBand pageHeader = new JRDesignBand();
        pageHeader.setHeight(42);
        JRDesignStaticText hotelName = new JRDesignStaticText();
        hotelName.setX(0); hotelName.setY(2); hotelName.setWidth(400); hotelName.setHeight(18);
        hotelName.setFontSize(14f); hotelName.setBold(true);
        hotelName.setForecolor(new Color(26, 46, 60));
        hotelName.setText("Hotel Management System");
        pageHeader.addElement(hotelName);
        JRDesignStaticText reportLabel = new JRDesignStaticText();
        reportLabel.setX(0); reportLabel.setY(21); reportLabel.setWidth(400); reportLabel.setHeight(14);
        reportLabel.setFontSize(9f); reportLabel.setBold(true);
        reportLabel.setForecolor(new Color(44, 62, 80));
        reportLabel.setText("Occupancy & Revenue Report");
        pageHeader.addElement(reportLabel);
        JRDesignTextField genField = new JRDesignTextField();
        genField.setX(400); genField.setY(3); genField.setWidth(155); genField.setHeight(14);
        genField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        genField.setFontSize(8f); genField.setForecolor(Color.GRAY);
        JRDesignExpression genExpr = new JRDesignExpression();
        genExpr.setText("\"Generated: \" + new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm\").format(new java.util.Date())");
        genField.setExpression(genExpr);
        pageHeader.addElement(genField);
        JRDesignLine headerLine = new JRDesignLine();
        headerLine.setX(0); headerLine.setY(38); headerLine.setWidth(555); headerLine.setHeight(1);
        headerLine.setForecolor(new Color(26, 46, 60));
        pageHeader.addElement(headerLine);
        design.setPageHeader(pageHeader);

        // Title
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(40);
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setX(0); titleText.setY(5); titleText.setWidth(555); titleText.setHeight(22);
        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleText.setFontSize(18f); titleText.setBold(true);
        titleText.setText("Occupancy & Revenue Report");
        titleBand.addElement(titleText);
        JRDesignTextField periodField = new JRDesignTextField();
        periodField.setX(0); periodField.setY(28); periodField.setWidth(555); periodField.setHeight(12);
        periodField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        periodField.setFontSize(10f); periodField.setForecolor(Color.GRAY);
        JRDesignExpression periodExpr = new JRDesignExpression();
        periodExpr.setText("($P{start_date} != null && $P{end_date} != null) ? \"Period: \" + new java.text.SimpleDateFormat(\"MMM dd, yyyy\").format($P{start_date}) + \" to \" + new java.text.SimpleDateFormat(\"MMM dd, yyyy\").format($P{end_date}) : \"All-time report\"");
        periodField.setExpression(periodExpr);
        titleBand.addElement(periodField);
        design.setTitle(titleBand);

        // Column headers
        Color teal = new Color(44, 62, 80);
        JRDesignBand colHeaderBand = new JRDesignBand();
        colHeaderBand.setHeight(26);
        colHeaderBand.addElement(headerCell("Room Type", 0, 150, teal));
        colHeaderBand.addElement(headerCell("Total Rms", 150, 135, teal));
        colHeaderBand.addElement(headerCell("Occ. Rms", 285, 135, teal));
        colHeaderBand.addElement(headerCell("Total Revenue", 420, 135, teal));
        design.setColumnHeader(colHeaderBand);

        // Detail
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(22);
        detailBand.addElement(styledDetail("$F{room_type}", 0, 150, HorizontalTextAlignEnum.LEFT, detailStyle));
        detailBand.addElement(styledDetail("$F{total_rooms}", 150, 135, HorizontalTextAlignEnum.CENTER, detailStyle));
        detailBand.addElement(styledDetail("$F{occupied_rooms}", 285, 135, HorizontalTextAlignEnum.CENTER, detailStyle));
        detailBand.addElement(styledDetail("\"LKR \" + String.format(\"%,.2f\", $F{total_revenue})", 420, 135, HorizontalTextAlignEnum.RIGHT, detailStyle));
        ((net.sf.jasperreports.engine.design.JRDesignSection) design.getDetailSection()).addBand(detailBand);

        // Page footer
        JRDesignBand pageFooter = new JRDesignBand();
        pageFooter.setHeight(20);
        JRDesignLine footerLine = new JRDesignLine();
        footerLine.setX(0); footerLine.setY(0); footerLine.setWidth(555); footerLine.setHeight(1);
        footerLine.setForecolor(new Color(26, 46, 60));
        pageFooter.addElement(footerLine);
        JRDesignTextField pageField = new JRDesignTextField();
        pageField.setX(440); pageField.setY(3); pageField.setWidth(115); pageField.setHeight(15);
        pageField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        pageField.setFontSize(8f); pageField.setForecolor(Color.GRAY);
        JRDesignExpression pageExpr = new JRDesignExpression();
        pageExpr.setText("\"Page \" + $V{PAGE_NUMBER}");
        pageField.setExpression(pageExpr);
        pageFooter.addElement(pageField);
        JRDesignStaticText footerText = new JRDesignStaticText();
        footerText.setX(0); footerText.setY(3); footerText.setWidth(250); footerText.setHeight(15);
        footerText.setFontSize(8f); footerText.setForecolor(Color.GRAY);
        footerText.setText("HMS - Occupancy & Revenue Report");
        pageFooter.addElement(footerText);
        design.setPageFooter(pageFooter);

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

        addParam(design, "start_date", Date.class);
        addParam(design, "end_date", Date.class);
        addParam(design, "status", String.class);
        addParam(design, "guest_name", String.class);

        JRDesignQuery query = new JRDesignQuery();
        query.setText("SELECT g.first_name, g.last_name, g.email, g.phone, res.display_id, res.check_in_date, res.check_out_date, r.room_number, r.room_type, r.base_price, b.room_charge, b.service_charge, b.other_charges, b.discount_amount, b.late_charge, b.tax_amount, b.total_bill, b.payment_status FROM reservations res JOIN guests g ON res.guest_id = g.guest_id JOIN rooms r ON res.room_id = r.room_id JOIN billing b ON res.reservation_id = b.reservation_id WHERE ($P{start_date} IS NULL OR res.check_in_date >= $P{start_date}) AND ($P{end_date} IS NULL OR res.check_in_date <= $P{end_date}) AND ($P{status} IS NULL OR $P{status} = '' OR b.payment_status = $P{status}) AND ($P{guest_name} IS NULL OR $P{guest_name} = '' OR LOWER(CONCAT(g.first_name, ' ', g.last_name)) LIKE LOWER(CONCAT('%', $P{guest_name}, '%'))) ORDER BY res.check_in_date DESC");
        design.setQuery(query);

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

        JRDesignStyle detailStyle = new JRDesignStyle();
        detailStyle.setName("detailStyle");
        detailStyle.setMode(ModeEnum.OPAQUE);
        detailStyle.setBackcolor(Color.WHITE);
        detailStyle.setFontName("SansSerif");
        detailStyle.setFontSize(10f);
        applyBorder(detailStyle.getLineBox());
        JRDesignConditionalStyle altStyle = new JRDesignConditionalStyle();
        JRDesignExpression condExpr = new JRDesignExpression();
        condExpr.setText("$V{REPORT_COUNT} % 2 == 0");
        altStyle.setConditionExpression(condExpr);
        altStyle.setBackcolor(new Color(255, 248, 231));
        detailStyle.addConditionalStyle(altStyle);
        design.addStyle(detailStyle);

        // Page header
        JRDesignBand pageHeader = new JRDesignBand();
        pageHeader.setHeight(42);
        JRDesignStaticText hotelName = new JRDesignStaticText();
        hotelName.setX(0); hotelName.setY(2); hotelName.setWidth(600); hotelName.setHeight(18);
        hotelName.setFontSize(14f); hotelName.setBold(true);
        hotelName.setForecolor(new Color(58, 44, 15));
        hotelName.setText("Hotel Management System");
        pageHeader.addElement(hotelName);
        JRDesignStaticText reportLabel = new JRDesignStaticText();
        reportLabel.setX(0); reportLabel.setY(21); reportLabel.setWidth(600); reportLabel.setHeight(14);
        reportLabel.setFontSize(9f); reportLabel.setBold(true);
        reportLabel.setForecolor(new Color(139, 105, 20));
        reportLabel.setText("Guest Invoice Report");
        pageHeader.addElement(reportLabel);
        JRDesignTextField genField = new JRDesignTextField();
        genField.setX(600); genField.setY(3); genField.setWidth(202); genField.setHeight(14);
        genField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        genField.setFontSize(8f); genField.setForecolor(Color.GRAY);
        JRDesignExpression genExpr = new JRDesignExpression();
        genExpr.setText("\"Generated: \" + new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm\").format(new java.util.Date())");
        genField.setExpression(genExpr);
        pageHeader.addElement(genField);
        JRDesignLine headerLine = new JRDesignLine();
        headerLine.setX(0); headerLine.setY(38); headerLine.setWidth(802); headerLine.setHeight(1);
        headerLine.setForecolor(new Color(139, 105, 20));
        pageHeader.addElement(headerLine);
        design.setPageHeader(pageHeader);

        // Title
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(40);
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setX(0); titleText.setY(5); titleText.setWidth(802); titleText.setHeight(22);
        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleText.setFontSize(18f); titleText.setBold(true);
        titleText.setText("Guest Invoice Report");
        titleBand.addElement(titleText);
        JRDesignTextField periodField = new JRDesignTextField();
        periodField.setX(0); periodField.setY(28); periodField.setWidth(802); periodField.setHeight(12);
        periodField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        periodField.setFontSize(10f); periodField.setForecolor(Color.GRAY);
        JRDesignExpression periodExpr = new JRDesignExpression();
        periodExpr.setText("($P{start_date} != null && $P{end_date} != null) ? \"Period: \" + new java.text.SimpleDateFormat(\"MMM dd, yyyy\").format($P{start_date}) + \" to \" + new java.text.SimpleDateFormat(\"MMM dd, yyyy\").format($P{end_date}) : \"All-time report\"");
        periodField.setExpression(periodExpr);
        titleBand.addElement(periodField);
        design.setTitle(titleBand);

        // Column headers (7 columns)
        Color gold = new Color(139, 105, 20);
        JRDesignBand colHeaderBand = new JRDesignBand();
        colHeaderBand.setHeight(26);
        colHeaderBand.addElement(headerCell("Guest Name", 0, 190, gold));
        colHeaderBand.addElement(headerCell("Resv #", 190, 120, gold));
        colHeaderBand.addElement(headerCell("Check-In", 310, 80, gold));
        colHeaderBand.addElement(headerCell("Check-Out", 390, 80, gold));
        colHeaderBand.addElement(headerCell("Room", 470, 80, gold));
        colHeaderBand.addElement(headerCell("Total", 550, 150, gold));
        colHeaderBand.addElement(headerCell("Status", 700, 102, gold));
        design.setColumnHeader(colHeaderBand);

        // Detail (7 columns)
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(22);
        detailBand.addElement(styledDetail("$F{first_name} + \" \" + $F{last_name}", 0, 190, HorizontalTextAlignEnum.LEFT, detailStyle));
        detailBand.addElement(styledDetail("$F{display_id}", 190, 120, HorizontalTextAlignEnum.LEFT, detailStyle));
        JRDesignTextField cinField = styledDetail("$F{check_in_date}", 310, 80, HorizontalTextAlignEnum.CENTER, detailStyle);
        cinField.setPattern("MMM dd");
        detailBand.addElement(cinField);
        JRDesignTextField coutField = styledDetail("$F{check_out_date}", 390, 80, HorizontalTextAlignEnum.CENTER, detailStyle);
        coutField.setPattern("MMM dd");
        detailBand.addElement(coutField);
        detailBand.addElement(styledDetail("$F{room_number}", 470, 80, HorizontalTextAlignEnum.CENTER, detailStyle));
        detailBand.addElement(styledDetail("\"LKR \" + String.format(\"%,.2f\", $F{total_bill})", 550, 150, HorizontalTextAlignEnum.RIGHT, detailStyle));
        detailBand.addElement(styledDetail("$F{payment_status}", 700, 102, HorizontalTextAlignEnum.CENTER, detailStyle));
        ((net.sf.jasperreports.engine.design.JRDesignSection) design.getDetailSection()).addBand(detailBand);

        // Page footer
        JRDesignBand pageFooter = new JRDesignBand();
        pageFooter.setHeight(20);
        JRDesignLine footerLine = new JRDesignLine();
        footerLine.setX(0); footerLine.setY(0); footerLine.setWidth(802); footerLine.setHeight(1);
        footerLine.setForecolor(new Color(139, 105, 20));
        pageFooter.addElement(footerLine);
        JRDesignTextField pageField = new JRDesignTextField();
        pageField.setX(650); pageField.setY(3); pageField.setWidth(152); pageField.setHeight(15);
        pageField.setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        pageField.setFontSize(8f); pageField.setForecolor(Color.GRAY);
        JRDesignExpression pageExpr = new JRDesignExpression();
        pageExpr.setText("\"Page \" + $V{PAGE_NUMBER}");
        pageField.setExpression(pageExpr);
        pageFooter.addElement(pageField);
        JRDesignStaticText footerText = new JRDesignStaticText();
        footerText.setX(0); footerText.setY(3); footerText.setWidth(250); footerText.setHeight(15);
        footerText.setFontSize(8f); footerText.setForecolor(Color.GRAY);
        footerText.setText("HMS - Guest Invoice Report");
        pageFooter.addElement(footerText);
        design.setPageFooter(pageFooter);

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

    private static void addParam(JasperDesign design, String name, Class<?> valueClass) throws JRException {
        JRDesignParameter param = new JRDesignParameter();
        param.setName(name);
        param.setValueClass(valueClass);
        design.addParameter(param);
    }

    private static JRDesignStaticText headerCell(String text, int x, int width, Color bg) {
        JRDesignStaticText st = new JRDesignStaticText();
        st.setX(x); st.setY(0); st.setWidth(width); st.setHeight(26);
        st.setMode(ModeEnum.OPAQUE);
        st.setBackcolor(bg);
        st.setForecolor(Color.WHITE);
        st.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        st.setVerticalTextAlign(net.sf.jasperreports.engine.type.VerticalTextAlignEnum.MIDDLE);
        st.setBold(true);
        st.setFontSize(10f);
        st.setText(text);
        return st;
    }

    private static JRDesignTextField styledDetail(String expression, int x, int width,
            HorizontalTextAlignEnum align, JRDesignStyle style) {
        JRDesignTextField tf = new JRDesignTextField();
        tf.setX(x); tf.setY(0); tf.setWidth(width); tf.setHeight(22);
        tf.setHorizontalTextAlign(align);
        tf.setStyle(style);
        JRDesignExpression expr = new JRDesignExpression();
        expr.setText(expression);
        tf.setExpression(expr);
        return tf;
    }
}
