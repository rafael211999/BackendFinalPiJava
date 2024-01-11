package senac.java.Controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import senac.java.DAL.ProductDal;
import senac.java.DAL.UserDal;
import senac.java.Domain.Products;
import senac.java.Domain.Sales;
import senac.java.Domain.Users;
import senac.java.Services.ResponseEndPoints;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    static ResponseEndPoints res = new ResponseEndPoints();
    static JSONObject responseJson = new JSONObject();
    public static List<Products> productsList = new ArrayList<>();

    static JSONObject responseJason = new JSONObject();


    public static class ProductsHandler implements HttpHandler {
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
                res.enviarResponseJson(exchange, responseJson, 401);
            }


        }

        public void doGet(HttpExchange exchange) throws IOException {
            Products products = new Products();
            ProductDal productDal = new ProductDal();
            List<Products> productsArray = new ArrayList<>();
            JSONObject json;

            try {
                productsArray = productDal.listarProdutos();
                json = products.arrayToJson(productsArray);
                res.enviarResponseJson(exchange, json, 200);
            } catch (Exception e) {
                System.out.println("O erro foi: " + e);
                response = "Ocorreu um erro ao buscar os dados";
                res.enviarResponse(exchange, response, 500);
            }
        }

        public void doPost(HttpExchange exchange) throws IOException {

            ProductDal productDal = new ProductDal();
            int resp = 0;

            try (InputStream requestBody = exchange.getRequestBody()) {

                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));

                Products products = new Products(
                        json.getString("name"),
                        json.getString("fabrica"),
                        json.getString("quantidade")
                );
                productsList.add(products);
                resp = productDal.inserirProdutos(products.name, products.factory, products.quantity);

                if (resp == 0) {
                    response = "Houve um problema ao criar o produto";
                    res.enviarResponse(exchange, response, 200);
                } else if (resp > 0) {
                    response = "Produto criado com sucesso";
                    res.enviarResponse(exchange, response, 200);
                }

            } catch (Exception e) {
                String resposta = e.toString();
                System.out.println("O erro foi " + resposta);
                res.enviarResponse(exchange, resposta, 200);


            }

        }

        public void doPut(HttpExchange exchange) throws IOException {
            ProductDal productDal = new ProductDal();
            int myId = 0;
            String myName = "";
            String myFactory = "";
            String myQuantity = "";

            int resp = 0;

            try (InputStream requestBody = exchange.getRequestBody()) {
                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));
                myId = Integer.parseInt(json.getString("id"));
                myName = json.getString("name");
                myFactory = json.getString("fabrica");
                myQuantity = json.getString("quantidade");


                resp = productDal.atualizarProdutos(myId, myName, myFactory, myQuantity);

                if (resp == 0) {
                    response = "Houve um problema ao atualizar os dados do produto";
                    res.enviarResponse(exchange, response, 200);
                } else if (resp > 0) {
                    response = "Dados atualizados com sucesso";
                    res.enviarResponse(exchange, response, 200);
                }

                res.enviarResponse(exchange, response, 200);
            } catch (Exception e) {
                System.out.println("O erro foi: " + e);
                res.enviarResponse(exchange, "Erro ao atualizar produto", 200);
            }


        }

        public void doDelete(HttpExchange exchange) throws IOException {
            ProductDal productDal = new ProductDal();
            int myId = 0;
            int resp = 0;


            try (InputStream requestBody = exchange.getRequestBody()) {

                JSONObject json = new JSONObject(new String(requestBody.readAllBytes()));
                myId = Integer.parseInt(json.getString("id"));
                resp = productDal.excluirProdutos(myId);

                if (resp == 0) {
                    response = "Houve um problema ao deletar o produto";
                    res.enviarResponse(exchange, response, 200);
                } else if (resp > 0) {
                    response = "Produto deletado com sucesso";
                    res.enviarResponse(exchange, response, 200);
                }

            } catch (Exception e) {
                System.out.println("O erro foi: " + e);
                res.enviarResponse(exchange, "Erro ao deletar produto", 200);
            }

        }


    }
}
