# ELK

## Setup instruction

### docker-compose 

#### Create `.env` file

```shell
# Password for the 'elastic' user (at least 6 characters)
ELASTIC_PASSWORD=elastic

# Password for the 'kibana_system' user (at least 6 characters)
KIBANA_PASSWORD=elastic

# Version of Elastic products
STACK_VERSION=8.6.2

# Set the cluster name
CLUSTER_NAME=docker-cluster

# Set to 'basic' or 'trial' to automatically start the 30-day trial
LICENSE=basic
#LICENSE=trial

# Port to expose Elasticsearch HTTP API to the host
ES_PORT=9200
#ES_PORT=127.0.0.1:9200

# Port to expose Kibana to the host
KIBANA_PORT=5601
#KIBANA_PORT=80

# Increase or decrease based on the available host memory (in bytes)
MEM_LIMIT=1073741824

# Project namespace (defaults to the current folder name if not set)
#COMPOSE_PROJECT_NAME=myproject


ELASTIC_STACK_VERSION=7.12.1
```

#### Update `docker-compose.yml`

```yaml
version: "2.2"

services:
  es01:
    image: elasticsearch:${STACK_VERSION}
    volumes:
      - esdata01:/usr/share/elasticsearch/data
    ports:
      - ${ES_PORT}:9200
    environment:
      - node.name=es01
      - cluster.name=${CLUSTER_NAME}
      - cluster.initial_master_nodes=es01,es02,es03
      - discovery.seed_hosts=es02,es03
      - bootstrap.memory_lock=true
      - xpack.security.enabled=false
      - xpack.security.http.ssl.enabled=false
      - xpack.security.transport.ssl.enabled=false
    mem_limit: ${MEM_LIMIT}
    ulimits:
      memlock:
        soft: -1
        hard: -1

  es02:
    depends_on:
      - es01
    image: elasticsearch:${STACK_VERSION}
    volumes:
      - esdata02:/usr/share/elasticsearch/data
    environment:
      - node.name=es02
      - cluster.name=${CLUSTER_NAME}
      - cluster.initial_master_nodes=es01,es02,es03
      - discovery.seed_hosts=es01,es03
      - bootstrap.memory_lock=true
      - xpack.security.enabled=false
      - xpack.security.http.ssl.enabled=false
      - xpack.security.transport.ssl.enabled=false
    mem_limit: ${MEM_LIMIT}
    ulimits:
      memlock:
        soft: -1
        hard: -1

  es03:
    depends_on:
      - es02
    image: elasticsearch:${STACK_VERSION}
    volumes:
      - esdata03:/usr/share/elasticsearch/data
    environment:
      - node.name=es03
      - cluster.name=${CLUSTER_NAME}
      - cluster.initial_master_nodes=es01,es02,es03
      - discovery.seed_hosts=es01,es02
      - bootstrap.memory_lock=true
      - xpack.security.enabled=false
      - xpack.security.http.ssl.enabled=false
      - xpack.security.transport.ssl.enabled=false
    mem_limit: ${MEM_LIMIT}
    ulimits:
      memlock:
        soft: -1
        hard: -1
  kibana:
    image: kibana:${STACK_VERSION}
    volumes:
      - kibanadata:/usr/share/kibana/data
    ports:
      - ${KIBANA_PORT}:5601
    environment:
      - SERVERNAME=kibana
      - ELASTICSEARCH_HOSTS=http://es01:9200
      - ELASTICSEARCH_USERNAME=kibana_system
      - ELASTICSEARCH_PASSWORD=${KIBANA_PASSWORD}
    mem_limit: ${MEM_LIMIT}
  logstash:
    image: logstash:${STACK_VERSION}
    ports:
      - "5044:5044"
      - "9600:9600"
    environment:
      - xpack.monitoring.enabled=true
      - xpack.security.enabled=false
      - xpack.security.http.ssl.enabled=false
      - xpack.security.transport.ssl.enabled=false
      - xpack.monitoring.elasticsearch.hosts=["http://es01:9200","http://es02:9200"]
    volumes:
      - ./logstash/logstash.conf:/etc/logstash/conf.d/logstash.conf:ro
volumes:
  esdata01:
    driver: local
  esdata02:
    driver: local
  esdata03:
    driver: local
  kibanadata:
    driver: local

```

#### Start Service using `docker-compose up`

