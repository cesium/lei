# Swap Solver

This is a service used to calculate the maximum possible shift exchanges in the [Swap](https://github.com/cesium/swap) platform.

## Setup

Clone the repository.

```
git clone https://github.com/cesium/swap-solver
cd swap-solver
```

Open the project in your favourite IDE. This is a Maven project. If your IDE does not support building Maven projects by default, please refer to the [official guides](https://maven.apache.org/guides/getting-started/).


## Making requests

The service is hosted by default on http://0.0.0.0:4567. The accepted request is a POST method and its body should follow this structure:

```
{"exchange_requests":[
		{
			"id": "a1",
			"from_shift_id": "TP1",
			"to_shift_id": "TP5",
			"created_at": 1
		},
		{
			"id": "a2",
			"from_shift_id": "TP1",
			"to_shift_id": "TP3",
			"created_at": 1
		},
		{
			"id": "a3",
			"from_shift_id": "TP2",
			"to_shift_id": "TP4",
			"created_at": 2
		},
		{
			"id": "a4",
			"from_shift_id": "TP2",
			"to_shift_id": "TP6",
			"created_at": 2
		}
	]
}
```

The return is a JSON as the following:

```
{"solved_exchanges":["a2", "a5", "a13", "a12", "a11", "a9", "a14", "a7"]}
```