```mermaid
erDiagram
    PRODUCT {
        bigint id PK "상품 ID"
        bigint ref_brand_id FK "브랜드 ID"
        string name "상품 이름"
        bigint price "상품 가격"
        string description "상품 설명"
    }

    BRAND {
        bigint id PK "브랜드 ID"
        string name "브랜드 이름"
        string description "브랜드 설명"
    }

    BRAND ||--o{ PRODUCT: "referenced"
```