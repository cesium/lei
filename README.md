[swap-github]: https://github.com/Hackathonners/swap
[maven-guides]: https://maven.apache.org/guides/getting-started/
[maven-wrapper]: https://maven.apache.org/wrapper/

<p align="center">
    <img src="src/main/resources/logo.png" height="120">
</p>

This is a service used to calculate the maximum possible shift exchanges in the
[Swap][swap-github] platform.

## 📥 Prerequisites

The following software is required to be installed on your system:

- [Java SDK 18](https://openjdk.java.net/)
- [Maven 3.8+](https://maven.apache.org/maven-features.html)

The [Maven Wrapper][maven-wrapper] is an easy way to ensure a user of your
Maven build has everything necessary to run your Maven build. The wrapper
should work on various operating systems including:

- Linux (numerous versions, tested on Ubuntu and CentOS) [`bin/mvnw`]
- OSX / macOS [`bin/mvnw`]
- Windows (various newer versions) [`bin/mvnw.cmd`]
- Solaris (10 and 11) [`bin/mvnw`]

## 🔧 Setup

Clone the repository.

```
git clone https://github.com/cesium/swap-solver
cd swap-solver
```

Open the project in your favourite IDE. This is a Maven project. If your IDE
does not support building Maven projects by default, please refer to the
[official guides][maven-guides].

## 🔨 Development

Start the server on <http://localhost:4567>.

```
mvn compile exec:java
```

Run the project tests.

```
mvn test
```

Format the code accordingly to common guide lines.

```
mvn spotless:apply
```

Lint your code with checkstyle.

```
mvn checkstyle:check
```

### 🐳 Running inside docker

Build the docker images.

```
docker build -t swap-solver .
```

Run the image.

```
docker run -d -p 4567:4567 swap-solver mvn exec:java
```

## ⌨️ Making requests

The service is hosted by default on <http://localhost:4567>. The accepted
request is a POST method and its body should follow this structure:

```json
{
  "exchange_requests": [
    {
      "id": "a1",
      "from_shift_id": "TP1",
      "to_shift_id": "TP5",
      "created_at": 1
    },
    {
      "id": "a2",
      "from_shift_id": "TP5",
      "to_shift_id": "TP1",
      "created_at": 1
    },
    {
      "id": "a3",
      "from_shift_id": "TP2",
      "to_shift_id": "TP4",
      "created_at": 2
    }
  ]
}
```

The return is a JSON as the following:

```json
{
  "solved_exchanges": ["a1", "a2"]
}
```
