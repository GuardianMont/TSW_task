package ec.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public class PdfGeneratorHelper {
    public static void generatePdfInvoice(JsonObject jsonOrder, HttpServletResponse response) throws IOException, URISyntaxException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        PdfFont font = PdfFontFactory.createFont("Times-Roman");
        String imagePath = "C:\\Users\\user\\Desktop\\TSW_guardian_ver\\TSW_task\\src\\main\\webapp\\uploadFile\\logoso.png";

        Image img = new Image(ImageDataFactory.create(imagePath));
        img.setWidth(UnitValue.createPercentValue(50));
        img.setHeight(UnitValue.createPercentValue(50));
        img.setTextAlignment(TextAlignment.CENTER);
        document.add(img);

        LineSeparator ls = new LineSeparator(new SolidLine());
        ls.setMarginBottom(10);
        document.add(ls);

        document.add(new Paragraph("Codice Fattura: " + jsonOrder.get("ordineFattura").getAsString())
                .setFont(font)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER));

        LineSeparator ls_2 = new LineSeparator(new SolidLine());
        ls_2.setMarginBottom(10);
        document.add(ls_2);

        if (jsonOrder.has("ordineId")) {
            document.add(new Paragraph(
                    "Esercente TavolandoSRL"+
                            "\nOrdine ID: " + jsonOrder.get("ordineId").getAsString() +
                            "\nIn data: " + jsonOrder.get("dataOrdine") +
                            "\nCodice Fattura: " + jsonOrder.get("ordineFattura").getAsString())
                    .setFont(font)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));
        }

        LineSeparator ls_3 = new LineSeparator(new SolidLine());
        ls_3.setMarginBottom(10);
        document.add(ls_3);

        float[] columnWidths = {1, 1};
        Table tableInfo = new Table(columnWidths).useAllAvailableWidth();

        if (jsonOrder.has("address")) {
            JsonObject address = jsonOrder.getAsJsonObject("address");
            Cell cell = new Cell().add(new Paragraph("Indirizzo di spedizione: "
                    + address.get("via").getAsString() + ", " + address.get("numCiv").getAsString() +
                    "\n cita: " + address.get("citta").getAsString() + "(" + address.get("provincia").getAsString() + "),"
                    + address.get("cap").getAsString())
                    .setFont(font)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.LEFT));
            cell.setBorder(Border.NO_BORDER);
            tableInfo.addCell(cell);
        }

        if (jsonOrder.has("paymentMethod")) {
            JsonObject paymentMethod = jsonOrder.getAsJsonObject("paymentMethod");
            Cell cell = new Cell().add(new Paragraph("Numero carta: " + paymentMethod.get("numCarta").getAsString() +
                    "\ndata scedenza: " + paymentMethod.get("dataScadenza") +
                    "\ntitolare carta: " + paymentMethod.get("titolareCarta") +
                    "\ncircuito di pagamento: " + paymentMethod.get("circuito"))
                    .setFont(font)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));
            cell.setBorder(Border.NO_BORDER);
            tableInfo.addCell(cell);
        }

        document.add(tableInfo);

        if (jsonOrder.has("cartItems")) {
            JsonArray cartItems = jsonOrder.getAsJsonArray("cartItems");
            Table table = new Table(UnitValue.createPercentArray(new float[]{3, 1, 1}));
            table.setWidth(UnitValue.createPercentValue(100));

            table.addHeaderCell(new Cell().add(new Paragraph("Nome Prodotto").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Quantità").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Prezzo").setBold()));

            for (int i = 0; i < cartItems.size(); i++) {
                JsonObject item = cartItems.get(i).getAsJsonObject();
                table.addCell(new Cell().add(new Paragraph(item.get("nome").getAsString())));
                table.addCell(new Cell().add(new Paragraph(item.get("quantity").getAsString())));
                table.addCell(new Cell().add(new Paragraph(item.get("prezzoUnitario").getAsString())));
            }

            document.add(table);
        }

        if (jsonOrder.has("prezzototale")) {
            document.add(new Paragraph("Prezzo Totale: " + jsonOrder.get("prezzototale").getAsString()).setBold());
        }

        document.close();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=fattura.pdf");
        response.setContentLength(baos.size());
        ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
        out.close();
    }
}
