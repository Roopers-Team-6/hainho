```mermaid
classDiagram
    class Product {
        - Brand brand
        - String name
        - Price price
    }

    class Brand {
        - String name
    }

    class Price {
        - BigInt amount
    }

    Product "N" --> Brand: 참조
    Product --> Price: 소유
```