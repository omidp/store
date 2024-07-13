## Online Grocery Store

### Overview

store is a Spring boot Java-based application designed to enable customers to buy with good promotions and discounts.


### Prerequisites
To run this project, you will need the following:

- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.6.3 or higher

### Tech Stack

- Spring boot
- Hibernate
- Drools rule engine
- H2 in-memory db

### Installation
1. Clone/Download the repository.
2. Build the project using maven.(Skip this, if you don't have maven)
```
mvn clean install
```
3. You don't have maven, download a zip file from [here](https://github.com/omidp/store/releases/download/1.0/store.zip), change the extension to jar and execute following command.
```
java --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED -jar store.jar
```
4. After building with maven to run the application, use the following command:
```
./run.sh
```
When store is successfully up & running, you will see order id which you can use to fetch the details

```
##################################################################
Creating order with id 3920167104166525682
##################################################################
```

#### Fetch product list

```
curl --location 'http://localhost:8080/products'
```

#### Fetch Order details

```
curl --location --globoff 'http://localhost:8080/orders/{orderId}/details'
```

#### Fetch Receipt

```
curl --location --globoff 'http://localhost:8080/orders/{orderId}'
```

Or you can use Postman collection attached to the project.

### Areas for Improvement

- Is total calculated amount correct after applying rules ?
- Receipt print items and how it should look like.
- Do we need to keep track of discount amount and price ? currently we keep both
- Some TODOs in the code.

### Why I chose Drools for this project, where are my rules ?

You can define rules using spreadsheets. It's more human-readable, no offence to developers.

Having a simpler and more concrete language provides us with a great advantage:
- we can include Subject Matter Experts (SME) without technical knowledge in the development cycle.
- Business requirements no longer have to be translated into technical artifacts (such as classes, rules, and so on) by the developers. The people writing these artifacts can be the people who have business expertise.

For instance : You can simply change BEERS discount percentage without compiling the code. please visit _discount_rule.xlsx_.

#### Troubleshooting 

If you get the error

```
Unable to make protected final java.lang.Class java.lang.ClassLoader.defineClass
```

make sure you have added following JVM arguments. 

```
--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED
```