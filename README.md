# Loopers Template (Spring + Java)

Loopers 에서 제공하는 스프링 자바 템플릿 프로젝트입니다.

## Getting Started

현재 프로젝트 안정성 및 유지보수성 등을 위해 아래와 같은 장치를 운용하고 있습니다. 이에 아래 명령어를 통해 프로젝트의 기반을 설치해주세요.

### Environment

`local` 프로필로 동작할 수 있도록, 필요 인프라를 `docker-compose` 로 제공합니다.

```shell
docker compose -p loopers -f ./docker/infra-compose.yml -f docker/application-compose.yml --compatibility up

docker compose -p loopers -f ./docker/infra-compose.yml --compatibility up
docker compose -p loopers -f ./docker/application-compose.yml --compatibility up


```

```shell
docker exec -it $(docker ps -qf "ancestor=mysql:8.0") mysql -uroot -proot loopers

LOAD DATA INFILE '/var/lib/mysql-files/brands.csv'
INTO TABLE brand
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(id, name, description, created_at, updated_at);
     
LOAD DATA INFILE '/var/lib/mysql-files/products.csv'
INTO TABLE product
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(brand_id, name, price, description, created_at, updated_at);

LOAD DATA INFILE '/var/lib/mysql-files/product_stock.csv'
INTO TABLE product_stock
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(product_id, quantity, created_at, updated_at);

LOAD DATA INFILE '/var/lib/mysql-files/like_product_count.csv'
INTO TABLE like_product_count
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES
(product_id, count, created_at, updated_at);
```

### Monitoring

`local` 환경에서 모니터링을 할 수 있도록, `docker-compose` 를 통해 `prometheus` 와 `grafana` 를 제공합니다.

애플리케이션 실행 이후, **http://localhost:3000** 로 접속해, admin/admin 계정으로 로그인하여 확인하실 수 있습니다.

```shell
docker-compose -f ./docker/monitoring-compose.yml up
```

## About Multi-Module Project

본 프로젝트는 멀티 모듈 프로젝트로 구성되어 있습니다. 각 모듈의 위계 및 역할을 분명히 하고, 아래와 같은 규칙을 적용합니다.

- apps : 각 모듈은 실행가능한 **SpringBootApplication** 을 의미합니다.
- modules : 특정 구현이나 도메인에 의존적이지 않고, reusable 한 configuration 을 원칙으로 합니다.
- supports : logging, monitoring 과 같이 부가적인 기능을 지원하는 add-on 모듈입니다.

```
Root
├── apps ( spring-applications )
│   └── 📦 commerce-api
├── modules ( reusable-configurations )
│   └── 📦 jpa
└── supports ( add-ons )
    ├── 📦 monitoring
    └── 📦 logging
```
