package senac.java.Services;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import senac.java.Controllers.ProductController;
import senac.java.Controllers.SalesController;
import senac.java.Controllers.UserController;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Servidor {

    public void apiServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0);

        HttpHandler userHandler = new UserController.UserHandler();
        HttpHandler salesPersonHandler = new SalesController.SalesPersonHandler();
        HttpHandler productHandler = new ProductController.ProductsHandler();

        server.createContext("/api/users", exchange -> {

            configureCorsHeaders(exchange);
            userHandler.handle((exchange));
        });
        server.createContext("/api/products", exchange -> {
            configureCorsHeaders(exchange);
            productHandler.handle((exchange));
        });
        server.createContext("/api/sales", exchange -> {
            configureCorsHeaders(exchange);
            salesPersonHandler.handle((exchange));
        });

        server.setExecutor(null);
        server.start();
    }

    // Configurando os headers do servidor.
    private void configureCorsHeaders(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();
        String requestOrigin = exchange.getRequestHeaders().getFirst("Origin");
        if(requestOrigin != null){
            headers.set("Access-Control-Allow-Origin", requestOrigin);
        }

        headers.set("Access-Control-Allow-Methods", "GET, OPTIONS, POST, PUT, DELETE");
        headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.set("Access-Control-Allow-Credentials", "true");
        headers.set("Access-Control-Max-Age", "3600");
    }
}
