package com.example.lmslite.service.util;


import com.example.lmslite.model.Certificate;
import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.BaseFont;
import org.openpdf.text.pdf.PdfContentByte;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class CertificatePdfRenderer {

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.of("ru"));

    private final BaseFont regular;
    private final BaseFont bold;

    public CertificatePdfRenderer() {
        try {
            this.regular = loadFont("fonts/DejaVuSans.ttf");
            this.bold    = loadFont("fonts/DejaVuSans-Bold.ttf");
        } catch (Exception e) {
            throw new IllegalStateException("Не удалось загрузить TTF-шрифты", e);
        }
    }

    private static BaseFont loadFont(String path) throws Exception
    {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            byte[] bytes = is.readAllBytes();
            return BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.EMBEDDED,
                    true, bytes, null);
        }
    }

    public byte[] render(Certificate cert)
    {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont  = new Font(bold,    28, Font.BOLD, Color.DARK_GRAY);
            Font nameFont   = new Font(bold,    24, Font.BOLD, Color.BLACK);
            Font courseFont = new Font(regular, 20, Font.NORMAL, Color.BLACK);
            Font smallFont  = new Font(regular, 12, Font.NORMAL, Color.GRAY);
            Font codeFont   = new Font(regular, 12, Font.NORMAL, Color.DARK_GRAY);

            PdfContentByte cb = writer.getDirectContent();
            Rectangle page = document.getPageSize();
            cb.setLineWidth(3f);
            cb.setColorStroke(new Color(180, 140, 60));
            cb.rectangle(20, 20, page.getWidth() - 40, page.getHeight() - 40);
            cb.stroke();
            cb.setLineWidth(1f);
            cb.rectangle(28, 28, page.getWidth() - 56, page.getHeight() - 56);
            cb.stroke();

            Paragraph title = new Paragraph("СЕРТИФИКАТ О ПРОХОЖДЕНИИ", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(60);
            title.setSpacingAfter(50);
            document.add(title);

            Paragraph confirm = new Paragraph("Настоящим подтверждается, что", smallFont);
            confirm.setAlignment(Element.ALIGN_CENTER);
            confirm.setSpacingAfter(20);
            document.add(confirm);

            String fullName = cert.getStudent().getFirstName() + " " + cert.getStudent().getLastName();
            Paragraph name = new Paragraph(fullName, nameFont);
            name.setAlignment(Element.ALIGN_CENTER);
            name.setSpacingAfter(30);
            document.add(name);

            Paragraph courseLabel = new Paragraph("успешно завершил(а) курс", smallFont);
            courseLabel.setAlignment(Element.ALIGN_CENTER);
            courseLabel.setSpacingAfter(10);
            document.add(courseLabel);

            Paragraph course = new Paragraph("\"" + cert.getCourse().getTitle() + "\"", courseFont);
            course.setAlignment(Element.ALIGN_CENTER);
            course.setSpacingAfter(60);
            document.add(course);

            String dateStr = "Дата выдачи: " + cert.getIssuedAt().format(DATE_FMT);
            Paragraph date = new Paragraph(dateStr, smallFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingBefore(40);
            document.add(date);

            Paragraph code = new Paragraph("Код сертификата: " + cert.getUniqueCode(), codeFont);
            code.setAlignment(Element.ALIGN_CENTER);
            code.setSpacingBefore(8);
            document.add(code);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create Certificate", e);
        }
    }
}