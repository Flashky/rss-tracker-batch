# rss-tracker-batch
[![Build & Report](https://github.com/Flashky/rss-tracker-batch/actions/workflows/build-report.yml/badge.svg)](https://github.com/Flashky/rss-tracker-batch/actions/workflows/build-report.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/fbc88a54cea5400b9722805227eb7baf)](https://www.codacy.com/gh/Flashky/rss-tracker-batch/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Flashky/rss-tracker-batch&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/fbc88a54cea5400b9722805227eb7baf)](https://www.codacy.com/gh/Flashky/rss-tracker-batch/dashboard?utm_source=github.com&utm_medium=referral&utm_content=Flashky/rss-tracker-batch&utm_campaign=Badge_Coverage)

## Configuration properties

Before running the service, you will need to customize several properties or environment variables:

Property | Description | Default value
--|--|--
``TELEGRAM_BOT_TOKEN`` | The Telegram Bot token provided by [@BotFather](https://t.me/botfather) | None
``MONGODB_HOST`` | The MongoDB instance hostname or ip | ``localhost``
``MONGODB_PORT`` | The MongoDB port | ``27017``
``MONGODB_USERNAME`` | The MongoDB username | ``admin`` **(*)**
``MONGODB_PASSWORD`` | The MongoDB password | ``admin`` **(*)**
``MONGODB_DATABASE`` | The MongoDB database | ``rss_tracker_db``
``JOB_FIXED_DELAY_SECONDS`` | The delay in seconds between job executions | ``60``
``ADMIN_SERVER_USERNAME`` | (Optional) Username to login at the admin server | ``admin`` **(*)**
``ADMIN_SERVER_PASSWORD`` | (Optional) Password to login at the admin server | ``admin`` **(*)**
``ADMIN_SERVER_URL`` | (Optional) Admin server URL | ``http://localhost:9090``

***:** *It is highly recommended to change both default username and password.*

Please note:
- For [Docker](#docker-standalone) and [Docker Compose](#docker-compose) running modes, you might want to use a ``.env`` file to configure the previous properties.

Only on [Docker Compose](#docker-compose) running mode:
- ``MONGODB_HOST`` value is ignored when using. It will use a default hostname ``mongodb`` specified by the ``docker-compose.yml`` file. 
- ``MONGODB_PORT`` refers to the exposed MongoDB port to the Docker host (internally the container will still use ``27017``). Use this port if you need to connect with your MongoDB database manager.

## Running the service

There are several ways to run the service.

- Java Standalone
- Docker Standalone
- Docker Compose

### Java Standalone

#### Requirements

- JDK 11.
- Maven.
- MongoDB instance running with user and password.

#### Build

Open a terminal, clone the repo and build with maven:

```ssh
git clone https://github.com/Flashky/rss-tracker-batch.git
cd rss-tracker-batch
mvn clean package
```
#### Configure environment variables

Edit ``run_standalone.sh`` to modify the [configuration properties](#configuration-properties) as you need.

#### Run

Give permissions and execute:

```ssh
chmod +x run_standalone.sh
./run_standalone.sh
```

### Docker Standalone

#### Requirements

- Docker.
- MongoDB instance running with user and password.

#### Image pull

Download the latest docker image:

```shell
docker pull flashk/rss-tracker-batch:latest
```

#### Run

Basic run, asumming you have declared the needed [configuration properties](#configuration-properties) as environment variables:

```shell
docker run --name rss-tracker-batch -dp 8081:8081 flashk/rss-tracker-batch:latest
```

In case of using a ``.env`` file to configure the properties, you can run the container using the ``--env-file`` flag:

```shell
docker run --name rss-tracker-batch --env-file .env -dp 8081:8081 flashk/rss-tracker-batch:latest
```

### Docker Compose

See [rss-tracker-app](https://github.com/Flashky/rss-tracker-app) repository.