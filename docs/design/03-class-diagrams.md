```mermaid
classDiagram
    class Product {
        - Brand brand
        - String name
        - Price price
        - String description
    }

    class ProductLike {
        - Product product
        - User user
    }

    class Brand {
        - String name
        - String description
    }

    class Price {
        - BigInt amount
    }

    class User {
    }

    Product "N" --> Brand: 참조
    Product --> Price: 소유
    ProductLike "N" --> Product: 참조
    ProductLike "N" --> User: 참조
```