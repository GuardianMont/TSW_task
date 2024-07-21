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
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

public class PdfGeneratorHelper {
    public static void generatePdfInvoice(JsonObject jsonOrder, HttpServletResponse response, ServletContext servletContext) throws IOException, URISyntaxException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        try {
            PdfFont font = PdfFontFactory.createFont("Times-Roman");
            String imagePath = servletContext.getRealPath("/uploadFile/logoso.png");

            Image img = new Image(ImageDataFactory.create(imagePath));
            img.setWidth(UnitValue.createPercentValue(15));
            img.setHeight(UnitValue.createPercentValue(15));
            img.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(img);


            document.add(new Paragraph("Codice Fattura: " + jsonOrder.get("ordineFattura").getAsString())
                    .setFont(font)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER));

            SolidLine thinGrayLine = new SolidLine(0.5f);
            thinGrayLine.setColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY);
            LineSeparator ls_2 = new LineSeparator(thinGrayLine);
            ls_2.setMarginBottom(10);
            document.add(ls_2);

            if (jsonOrder.has("ordineId")) {
                document.add(new Paragraph(
                        "Esercente TavolandoSRL" +
                                "\nOrdine ID: " + jsonOrder.get("ordineId").getAsString() +
                                "\nIn data: " + jsonOrder.get("dataOrdine").getAsString() +
                                "\nCodice Fattura: " + jsonOrder.get("ordineFattura").getAsString())
                        .setFont(font)
                        .setFontSize(10)
                        .setTextAlignment(TextAlignment.RIGHT));
            }

            document.add(ls_2);

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
                        "\ndata scadenza: " + paymentMethod.get("dataScadenza").getAsString() +
                        "\ntitolare carta: " + paymentMethod.get("titolareCarta").getAsString() +
                        "\ncircuito di pagamento: " + paymentMethod.get("circuito").getAsString())
                        .setFont(font)
                        .setFontSize(10)
                        .setTextAlignment(TextAlignment.RIGHT));
                cell.setBorder(Border.NO_BORDER);
                tableInfo.addCell(cell);
            }

            document.add(tableInfo);

            document.add(ls_2);
            BigDecimal prezzoTot= new BigDecimal(0);
            if (jsonOrder.has("cartItems")) {
                JsonArray cartItems = jsonOrder.getAsJsonArray("cartItems");
                Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 1, 1, 1, 1}));
                table.setWidth(UnitValue.createPercentValue(100));

                table.addHeaderCell(new Cell().add(new Paragraph("Quantità").setFont(font).setBold().setFontSize(10)));
                table.addHeaderCell(new Cell().add(new Paragraph("Nome Prodotto").setFont(font).setBold().setFontSize(10)));
                table.addHeaderCell(new Cell().add(new Paragraph("IVA").setFont(font).setBold().setFontSize(10)));
                table.addHeaderCell(new Cell().add(new Paragraph("Sconto").setFont(font).setBold().setFontSize(10)));
                table.addHeaderCell(new Cell().add(new Paragraph("Prezzo Unitario").setFont(font).setBold().setFontSize(10)));
                table.addHeaderCell(new Cell().add(new Paragraph("Prezzo Riga").setFont(font).setBold().setFontSize(10)));

                for (int i = 0; i < cartItems.size(); i++) {
                    JsonObject item = cartItems.get(i).getAsJsonObject();
                    String quantityStr = item.get("quantity").getAsString();
                    String prezzoUnitarioStr = item.get("prezzoUnitario").getAsString();
                    int sconto  =  item.get("sconto").getAsInt();
                    BigDecimal quantity = new BigDecimal(quantityStr);
                    BigDecimal prezzoUnitario = new BigDecimal(prezzoUnitarioStr);
                    BigDecimal scontoRiga = new BigDecimal(0);
                    if (sconto >0){
                        scontoRiga = (prezzoUnitario.multiply(BigDecimal.valueOf(sconto))).divide(BigDecimal.valueOf(100));
                        scontoRiga.multiply(quantity);
                    }
                    BigDecimal prezzoRiga = (quantity.multiply(prezzoUnitario)).subtract(scontoRiga);

                    prezzoTot = prezzoTot.add(prezzoRiga);
                    table.addCell(new Cell().add(new Paragraph(quantityStr)).setFont(font).setFontSize(10).setBorder(new SolidBorder(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY, 0.5f)));
                    table.addCell(new Cell().add(new Paragraph(item.get("nome").getAsString())).setFont(font).setFontSize(10).setBorder(new SolidBorder(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY, 0.5f)));
                    table.addCell(new Cell().add(new Paragraph(item.get("iva").getAsString())).setFont(font).setFontSize(10).setBorder(new SolidBorder(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY, 0.5f)));
                    table.addCell(new Cell().add(new Paragraph(item.get("sconto").getAsString())).setFont(font).setFontSize(10).setBorder(new SolidBorder(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY, 0.5f)));
                    table.addCell(new Cell().add(new Paragraph(prezzoUnitarioStr + "€")).setFont(font).setFontSize(10).setBorder(new SolidBorder(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY, 0.5f)));
                    table.addCell(new Cell().add(new Paragraph(prezzoRiga.toString() + "€")).setFont(font).setFontSize(10).setBorder(new SolidBorder(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY, 0.5f)));
                }
                document.add(table);
            }

            if (jsonOrder.has("prezzototale")) {
                document.add(new Paragraph("Prezzo Totale: " + prezzoTot.toString()+  "€").setFont(font).setFontSize(12).setTextAlignment(TextAlignment.RIGHT).setBold());
            }

            document.add(ls_2);
            document.add(new Paragraph("grazie per l'acquisto")
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(ls_2);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=fattura.pdf");
        response.setContentLength(baos.size());
        ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
        out.close();
        baos.close();
    }
}
