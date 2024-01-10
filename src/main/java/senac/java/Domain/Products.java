package senac.java.Domain;

import org.json.JSONObject;

import java.util.List;

public class Products {

    int id;
    public  String name = "";
    public  String factory = "";
    public  String quantity = "";




    //Constructor

    public  Products(){}


    public Products(String name, String factory, String quantity){
        this.name = name;
        this.factory = factory;
        this.quantity = quantity;

    }
    public Products(int id, String name, String factory, String quantity){
        this.id = id;
        this.name = name;
        this.factory = factory;
        this.quantity = quantity;

    }

    //Getters and Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }


    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory){
        this.factory = factory;
    }

    public String getQuantity(){
        return quantity;
    }

    public void setQuantity(String quantity){
        this.quantity = quantity;
    }



    public JSONObject toJson(){
        JSONObject json = new JSONObject();

        json.put("name", name);
        json.put("fabrica", factory);
        json.put("quantidade", quantity);

        return json;
    }


    public JSONObject arrayToJson(List<Products> ProductsList) {
        JSONObject json = new JSONObject();


        if (!ProductsList.isEmpty()) {
            int contJson = 0;
            for (Products products : ProductsList) {

                JSONObject productsJson = new JSONObject();
                productsJson.put("id", products.getId());
                productsJson.put("name", products.getName());
                productsJson.put("fabrica", products.getFactory());
                productsJson.put("quantidade", products.getQuantity());

                json.put(String.valueOf(contJson) ,productsJson);
                contJson++;
            }
            return json;
        } else {
            return null;
        }
    }


    public static List<Products> getAllProducts(List<Products> productsList) {
        return productsList;
    }
}
