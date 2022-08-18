# civalue_test
A test home task as a part of the interview process to the company ciValue

Database schema:
Shopper personalized data - collection of documents that have the following structure
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

Product meta
{
    "productId": "string, product's unique ID, e.g. BB-2144746855",
    "category": "string, custom category name, e.g. 'Babies'",
    "brand": "string, custom brand name, e.g. 'Babyom'"
}

Shopper
{
    "shopperId": "not empty string, shoper's unique ID, e.g. 'S-1000'",
    //TBD
}