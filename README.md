## Local Setup

Clone and open the project in your IDE.
Run the main class [ProductServiceApplication](src/main/java/com/gfgtech/ProductApplication.java) as spring boot application.
On startup spring boot will read the [data.sql](src/main/resources/data.sql) file and execute the contents.

The Product Service is now ready to receive API requests.

## REST API Documentation for Products
**_Find All Products_**

Send a `GET` Request to `http://localhost:8080/products`

**_Find a Product with Id 1_**

Send a `GET` Request to `http://localhost:8080/products/1`

**_Add New Product_**

Send a `POST` request to `http://localhost:8080/products`

Sample Request Body
```json
{
    "productId": "LEVIS12345",
    "title": "Jeans",
    "description": "Regular fit jeans",
    "brand": "LEVIS",
    "price": 12000.00,
    "color": "Black"
}
```

**_Update a Product with Id 1_**

Send a `PATCH` request to `http://localhost:8080/products/1`

Sample Request Body
```json
{
    "price": 20000.00,
    "color": "BLUE"
}
```
Response
```json
{
    "id": 1,
    "productId": "LEVIS12345",
    "title": "Jeans",
    "description": "Regular fit jeans",
    "brand": "LEVIS",
    "price": 20000.00,
    "color": "BLUE"
} 
```

**_Delete a Product with Id 1_**

Send a `DELETE` request to `http://localhost:8080/products/1`

Since the `DELETE` operation is `idempotent`, it will return a **204** when the resource is deleted, or it does not exist.
