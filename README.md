[swap-github]: https://github.com/Hackathonners/swap
[maven-guides]: https://maven.apache.org/guides/getting-started/

# Swap Solver

This is a service used to calculate the maximum possible shift exchanges in the
[Swap][swap-github] platform.

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

Format the code accordingly to common guide lines.

```
mvn formatter:format
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
  "solved_exchanges": [
    "a1",
    "a2"
  ]
}
```
