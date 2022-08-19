# civalue_test
A test home task as a part of the interview process to the company ciValue
The current solution provides an POC version only. 
 
## Implementation info
Main used items:

* Java 8
* Spring Boot
* MongoDB 
* Code-writting plugin: Lombok

## Configuration details

The project has been configured to use MongoDB that is run in Docker (see **./docker-compose.yml**)
If you need to change connection parameters to MongoDB, please, edit the **spring.data.mongodb.uri parameter** in the **src\main\resources\application.properties** file

## How to run

1. Run Docker containers

    docker compose -f "docker-compose.yml" up -d --build

2. Run service

    For now use your IDE for local running (Java 8 is required)
    Class path: src\main\java\com\civalue_test\Application.java

    > !!! The service dockerization process is started but has not finished yet
    >
    > You can review commented part of the **docker-compose.yml** file 
    >
    > and other documentation in the **.info**, **.run**, **docker**, **settings** folders to got main idea

<hr>

Please, find below description of implemented APIs and technical details about used data models

## API description
### Internal APIs
#### Product data upsertion
route: **/api/i_v1/product**
request body (JSON):

    {
        "productId": "string",
        "category": "string",
        "brand": "string"
    }

*Example of usage:*

    curl --location --request POST 'localhost:3000/api/i_v1/product' \
        --header 'Content-Type: application/json' \
        --data-raw '{
            "productId": "ID4",
            "category": "TEST_CATEGORY4",
            "brand": "TEST_BRAND4"
        }'
 

#### Personalized list data upsertion
route: **/api/i_v1/personalized_list**
request body (JSON): 

    {
        "shopperId": "string",
        "shelf": [
            {
                "productId": "string",
                "relevancyScore": double
            }
        ]
    }

*Example of usage:*

    curl --location --request POST 'localhost:3000/api/i_v1/personalized_list' \
        --header 'Content-Type: application/json' \
        --data-raw '{
            "shopperId": "SID2",
            "shelf":[
                {
                    "productId": "ID2",
                    "relevancyScore": 0.1
                },
                {
                    "productId": "ID3",
                    "relevancyScore": 0.2
                },
                {
                    "productId": "ID4",
                    "relevancyScore": 0.3
                }
            ]
        }'
 

### eCommerce APIs
#### /api/ec_v1/product
Supported query parameters:
* shopperId - required, could not be bblank
* category - optional, by default is empty, if blank -ignored
* brand - optional, by default is empty, if blank -ignored
* limit - optional, by default has value 10

*Example of usage:*

    curl --location --request GET 'localhost:3000/api/ec_v1/product?shopperId=SID1&category=&brand&limit='
 
#### /api/ec_v1/shopper
Supported query parameters:
* productId - required, could not be bblank
* limit - optional, by default has value 10
 
*Example of usage:*

    curl --location --request GET 'localhost:3000/api/ec_v1/shopper?productId=ID4&limit='
 


## Data layer
Database schema:
### Shopper personalized data
collection of documents with the name "**personalized_list**" that have the following structure

    {
        "shopperId": "not empty string, shoper's unique ID, e.g. 'S-1000'",
        "shelf": [ // array of embedded documents
            {
                "productId": "string, product's unique ID, e.g. 'MB-2093193398'",
                "relevancyScore": "double, special score value, e.g.31.089209569320897"
            }
            ...
        ]
    }

### Product meta
collection of documents with the name "**product**" that have the following structure

    {
        "productId": "string, product's unique ID, e.g. BB-2144746855",
        "category": "string, custom category name, e.g. 'Babies'",
        "brand": "string, custom brand name, e.g. 'Babyom'"
    }

## Open questions:
* Finish dockerization of main service
* Data consistency (the Shooper entity should be introduced)
* Caching for eCommerce part of APIs (it require definition of caching keys and evicting policy)
* Review necessity of splitting internal and external APIs into two independent services (as they have different requirements regarding performance and security)
* Introducing logging (local loging and sending to a company monitoring layer)
* Introducing metric collection (sending to a company monitoring layer)
