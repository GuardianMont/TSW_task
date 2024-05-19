package ec.control;

import ec.model.address.AddressDaoDM;
import ec.model.address.AddressUs;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
@WebServlet ("/AddressManagement")
public class Address extends HttpServlet {
    private AddressDaoDM model;

    @Override
    public void init() throws ServletException {
        super.init();
        model = new AddressDaoDM();
    }

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String opzione = request.getParameter("opzione");
        System.out.println("Opzione: " + opzione); // Debugging
        String dis = "/Cart.jsp";
        String errorMessage="";
        ServletContext context = getServletContext();
        String value = (String) context.getAttribute("op");
        if (opzione != null) {
            switch (opzione){
                case "add":
                    if(request.getSession().getAttribute("userId")!=null) {
                        try {
                            JOptionPane.showMessageDialog(null, "sono qui");
                            handleAddAction(request,response);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
            }
        }
        if (value!=null){
            switch (value){
                case "show":
                    try {
                        request.setAttribute("addresses", model.doRetrieveAll((String) request.getSession().getAttribute("userId")));
                        dis="/Payment.jsp";
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }

        }
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(dis);
        dispatcher.forward(request, response);
    }

    private void handleAddAction(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        AddressUs ad = new AddressUs();
        ad.setCap(request.getParameter("cap"));
        ad.setCitta(request.getParameter("citta"));
        ad.setNumCiv(Integer.parseInt(request.getParameter("n_civico")));
        ad.setPreferenze(request.getParameter("preferenze"));
        ad.setVia(request.getParameter("via"));
        ad.setProvincia(request.getParameter("provincia"));
        ad.setNum_ID(1);
        model.doSave(ad, (String) request.getSession().getAttribute("userId"), ad.getNum_ID());

    }

}
