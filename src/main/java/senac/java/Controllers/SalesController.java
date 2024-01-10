package senac.java.Controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import senac.java.DAL.SalesDal;
import senac.java.DAL.UserDal;
import senac.java.Domain.Sales;

import senac.java.Domain.Users;
import senac.java.Services.ResponseEndPoints;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SalesController {
    static ResponseEndPoints res = new ResponseEndPoints();
    static JSONObject responseJason = new JSONObject();

    private static List<Sales> salesList = new ArrayList<>();


    public static class SalesPersonHandler implements HttpHandler {

        String response = "";

        @Override
        public void handle(HttpExchange exchange) throws IOException {


            if ("GET".equals(exchange.getRequestMethod())) {
               doGet(exchange);

            } else if ("POST".equals(exchange.getRequestMethod())) {
               doPost(exchange);

            } else if ("PUT".equals(exchange.getRequestMethod())) {
                doPut(exchange);

            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                doDelete(exchange);
            } else if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                exchange.close();
                return;
            } else {
                res.enviarResponseJson(exchange, responseJason, 401);
            }
        }

        public void doGet(HttpExchange exchange) throws IOException {
            SalesDal salesDal = new SalesDal();
            Sales sale = new Sales();
            List<Sales> salesArray = new ArrayList<>();
            JSONObject json;

            try {
                salesArray = salesDal.listarSales();
                json = sale.arrayToJson(salesArray);
                res.enviarResponseJson(exchange, json, 200);
            } catch (Exception e) {
                System.out.println("O erro foi: " + e);
                response = "Ocorreu um erro ao buscar os dados";
                res.enviarResponse(exchange, response, 500);
            }

        }

        public void doPost(HttpExchange exchange) throws IOException {

            SalesDal salesDal = new SalesDal();
            int resp = 0;

            try (InputStream requestBody = exchange.getRequestBody()) {

                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));

                Sales sales = new Sales(
                        json.getString("usuario"),
                        json.getString("produto"),
                        json.getFloat("valor"),
                        json.getString("venda_final"),
                        json.getFloat("desconto"),
                        json.getString("venda")

                );
                salesList.add(sales);
                resp = salesDal.inserirSales(sales.usuario, sales.products, sales.valor, sales.finishedSale, sales.discount, sales.sale);

                if (resp == 0) {
                    response = "Houve um problema ao criar o cadastro da venda";
                    res.enviarResponse(exchange, response, 200);
                } else if (resp > 0) {
                    response = "Venda criada com sucesso";
                    res.enviarResponse(exchange, response, 200);
                }

            } catch (Exception e) {
                String resposta = e.toString();
                System.out.println("O erro foi " + resposta);
                res.enviarResponse(exchange, resposta, 200);


            }

        }

        public void doPut(HttpExchange exchange) throws IOException {
            SalesDal salesDal = new SalesDal();
            int myId = 0;
            String myUsuario = "";
            String myProducts = "";
            float myValor = 0;
            String myFinishedSale = "";
            float myDiscount = 0;
            String mySale = "";

            int resp = 0;

            try (InputStream requestBody = exchange.getRequestBody()) {
                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));
                myId = Integer.parseInt(json.getString("id"));
                myUsuario = json.getString("usuario");
                myProducts = json.getString("products");
                myValor = Float.parseFloat(json.getString("valor"));
                myFinishedSale = json.getString("venda_final");
                myDiscount = Float.parseFloat(json.getString("desconto"));
                mySale = json.getString("venda");

                salesDal.atualizarSales(myId, myUsuario, myProducts, myValor, myFinishedSale, myDiscount, mySale);

                if (resp == 0) {
                    response = "Houve um problema ao atualizar os dados da venda";
                    res.enviarResponse(exchange, response, 200);
                } else if (resp > 0) {
                    response = "Dados atualizados com sucesso";
                    res.enviarResponse(exchange, response, 200);
                }

                res.enviarResponse(exchange, response, 200);
            } catch (Exception e) {
                System.out.println("O erro foi: " + e);
                res.enviarResponse(exchange, "Erro ao atualizar vendas", 200);
            }


        }

        public void doDelete(HttpExchange exchange) throws IOException {
            SalesDal salesDal = new SalesDal();
            int myId = 0;
            int resp = 0;


            try (InputStream requestBody = exchange.getRequestBody()) {

                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));
                myId = Integer.parseInt(json.getString("id"));
                salesDal.excluirSales(myId);

                if (resp == 0) {
                    response = "Houve um problema ao deletar a venda";
                    res.enviarResponse(exchange, response, 200);
                } else if (resp > 0) {
                    response = "Venda deletada com sucesso";
                    res.enviarResponse(exchange, response, 200);
                }

            } catch (Exception e) {
                System.out.println("O erro foi: " + e);
                res.enviarResponse(exchange, "Erro ao deletar venda", 200);
            }

        }


    }
}