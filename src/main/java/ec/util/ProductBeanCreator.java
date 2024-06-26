package ec.util;

import ec.model.product.ProductBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProductBeanCreator {

    public static ProductBean createProductBean(HttpServletRequest request, String uploadedFilePath)
            throws SQLException, ServletException, IOException {
        String name = request.getParameter("nome");
        String description = request.getParameter("descrizione");
        double price = Double.parseDouble(request.getParameter("prezzo"));
        int quantity = Integer.parseInt(request.getParameter("quantita"));
        double iva = Double.parseDouble(request.getParameter("iva"));
        String colore = request.getParameter("colore");
        String category = request.getParameter("categoria");
        String dimension = request.getParameter("dimensioni");
        String discount = request.getParameter("sconto");
        String isVisible = request.getParameter("isVisible");

        ProductBean bean = new ProductBean();
        bean.setNome(name);
        bean.setDescrizione(description);
        bean.setPrezzo(price);
        bean.setDisponibilita(quantity);
        bean.setFasciaIva(iva);
        bean.setColore(colore);

        if (category.equals("tavolo")){
            bean.setCategoria(request.getParameter("tipoTavolo"));
        }else if (category.equals("attrezzatura")){
            bean.setCategoria(request.getParameter("tipoAttrezzatura"));
        }else bean.setCategoria(category);

        bean.setCategoria(category);
        bean.setDimensioni(dimension);
        bean.setTemp_Url(uploadedFilePath);
        bean.setPercentualeSconto(Integer.parseInt(discount));
        bean.setVisible(Boolean.parseBoolean(isVisible));
        return bean;
    }
}
