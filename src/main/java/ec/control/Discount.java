package ec.control;

import ec.model.discount.DiscountBean;
import ec.model.discount.DiscountDao;
import ec.model.discount.DiscountDaoDM;
import ec.model.product.ProductBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public class Discount extends HttpServlet {

    private DiscountDao discountDao;

    @Override
    public void init() throws ServletException {
        discountDao = new DiscountDaoDM();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    /**Metodo doPost che vuole una richiesta che contiene
     * option, che può avere valori {add, delete, retrieveByKey, retrieveAll}.
     * In base al valore di option, esso richiede i seguenti valori:
     * {add: productId, discountPercentage;
     * delete: productId;
     * retrieveByKey: productId;
     * retrieveAll: order(che può essere ASC o DESC)}.
    * */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String dis = "/ProductView.jsp";
        String errorMessage="";

        boolean result;
        String option = req.getParameter("option");
        if(option != null) switch (option) {
                case "add":
                    result = doAdd(Integer.parseInt(req.getParameter("productId")),
                            Integer.parseInt(req.getParameter("discountPercentage")));
                    //TODO: passaggio di questo caso
                    break;
                case "delete":
                    result = doDelete(Integer.parseInt(req.getParameter("productId")));
                    //TODO: passaggio di questo caso
                    break;
                case "retrieveByKey":
                    DiscountBean bean = doRetrieveByKey(Integer.parseInt(req.getParameter("productId")));
                    //TODO: passaggio di questo caso
                    break;
                case "retrieveAll":
                    Collection<DiscountBean> discounts = doRetrieveAll((String) req.getAttribute("order"));
                    //TODO: passaggio di questo caso
            }
    }

    
    private boolean doAdd(int productId, int discountPercentage){
        DiscountBean bean = new DiscountBean();
        bean.setProduct(productId);
        bean.setDiscountPercentage(discountPercentage);

        try {
            discountDao.doSave(bean);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    private boolean doDelete(int productId){
        try{
            if(discountDao.doDelete(productId))
                return true;
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            return false;
        }
    }
    
    private DiscountBean doRetrieveByKey(int productId){
        try{
            return discountDao.doRetrieveByKey(productId);
        }catch (SQLException e){
            return null;
        }
    }
    
    private Collection<DiscountBean> doRetrieveAll(String order){
        try {
            return discountDao.doRetrieveAll(order);
        }catch (SQLException e){
            return null;
        }
    }
}
