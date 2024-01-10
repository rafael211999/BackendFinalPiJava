package senac.java.Domain;


import org.json.JSONObject;

import java.util.List;

public class Sales {
    int id;
    public  String usuario = "";
    public  String products = "";
    public  float valor = 0;
    public  String finishedSale;
    public float discount = 0;
    public String sale;


    //Construtor

    public Sales() {
    }

    public Sales(String usuario, String products, float valor, String finishedSale, float discount, String sale) {
        this.usuario = usuario;
        this.products = products;
        this.valor = valor;
        this.finishedSale = finishedSale;
        this.discount = discount;
        this.sale = sale;

    }

    public Sales(int id, String usuario, String products, float valor, String finishedSale, float discount, String sale) {
        this.id = id;
        this.usuario = usuario;
        this.products = products;
        this.valor = valor;
        this.finishedSale = finishedSale;
        this.discount = discount;
        this.sale = sale;

    }


    //Getters and Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getFinishedSale() {
        return finishedSale;
    }

    public void setFinishedSale(String finishedSale) {
        this.finishedSale = finishedSale;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }


    public JSONObject toJson(){
        JSONObject json = new JSONObject();

        json.put("usuario", usuario);
        json.put("produto", products);
        json.put("valor", valor);
        json.put("venda_final", finishedSale);
        json.put("desconto", discount);
        json.put("venda", sale);

        return json;
    }

    public JSONObject arrayToJson(List<Sales> salesList) {
        JSONObject json = new JSONObject();


        if (!salesList.isEmpty()) {
            int contJson = 0;
            for (Sales sale : salesList) {

                JSONObject salesJson = new JSONObject();
                salesJson.put("id", sale.getId());
                salesJson.put("usuario", sale.getUsuario());
                salesJson.put("produto", sale.getProducts());
                salesJson.put("valor", sale.getValor());
                salesJson.put("venda_final", sale.getFinishedSale());
                salesJson.put("desconto", sale.getDiscount());
                salesJson.put("venda", sale.getSale());

                json.put(String.valueOf(contJson) ,salesJson);
                contJson++;
            }
            return json;
        } else {
            return null;
        }
    }

    public static List<Sales> getAllSales(List<Sales> salesList) {
        return salesList;
    }
}
