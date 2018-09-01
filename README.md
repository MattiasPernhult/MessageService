# Message Service

This service exposes a RESTful API that works as a basic message store.

### Configure .env

Navigate to the file **.env_example** in the folder `src > main > resources`.

Make a copy of this file and name the new file `.env`. 

### Start the server

#### Docker

Simply run:

```bash
make docker-run
```

To attach to the Docker container run:

```bash
make docker-run-attach
```

## API

JWT is used for authentication method and a user can obtain a token by calling
the **Create Token** endpoint. The returned token should be used in other endpoints
that require authentication, so make sure you add the obtained token to the **Authorization** header 
of the request and in the following format:

Bearer {token}.

### Overview

Name|Method|Path
---|---|---
Create User|POST|/api/v1/users
Create Token|POST|/api/v1/users/:user_id/tokens
Create Message|POST|/api/v1/users/:user_id/messages
Update Message|PUT|/api/v1/users/:user_id/messages/:message_id
Delete Message|DELETE|/api/v1/users/:user_id/messages/:message_id
Fetch All Messages|GET|/api/v1/messages


### Create User

This endpoint creates a user.

```
Method:
	POST 
Path:
	/api/v1/users
Headers:
	- Content-Type: application/json
Body:
	{
	  "name": string
	}
Body Example:
	{
	  "name": "Kalle Karlsson"
	}
Success Response:
	{
	  "id": "b5eb1e6a-25ed-49f1-82c8-4cb9ff1e40b7",
	  "name": "Kalle Karlsson"
	}
```


### Create Token

This endpoint creates a token for a certain user. The token is 
required for other endpoints to authenticate the user.

```
Method:
	POST 
Path:
	/api/v1/users/:user_id/tokens
Path Parameters:
	- user_id: Id of the user
Headers:
	- Content-Type: application/json
Body:
	{}
Success Response:
	{
	  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ...",
	}
```

### Create Message

This endpoint creates a message.

The **created_at.timestamp** field should be seconds since epoch.

The **created_at.offset** field should be seconds offset from UTC. For example in Sweden during
daylight-saving time the value would be 7200.

```
Method:
	POST 
Path:
	/api/v1/users/:user_id/messages
Path Parameters:
	- user_id: Id of the user
Headers:
	- Content-Type: application/json
	- Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ... 
Body:
	{
	  "text": string,
	  "created_at": {
	    "timestamp": int,
	    "offset": int		 
	  }
	}
Body Example:
	{
	  "text": "Example text",
	  "created_at": {
	    "timestamp": 1535738112,
	    "offset": 7200
	   }
	}
Success Response:
	{
	  "id": "56f376c6-42b6-42b6-8afc-5675fca5f02a"
	  "text": "Example text",
	  "created_at": {
	    "timestamp": 1535738112,
	    "offset": 7200
	  },
	  "user": {
	    "id": "b5eb1e6a-25ed-49f1-82c8-4cb9ff1e40b7",
	    "name": "Kalle Karlsson"
	  }
	}
```


### Update Message

This endpoint updates a message.

The **created_at.timestamp** field should be seconds since epoch.

The **created_at.offset** field should be seconds offset from UTC. For example in Sweden during
daylight-saving time the value would be 7200.

```
Method:
	PUT
Path:
	/api/v1/users/:user_id/messages/:message_id
Path Parameters:
	- user_id: Id of the user
	- message_id: Id of the message
Headers:
	- Content-Type: application/json
	- Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ... 
Body:
	{
	  "text": string,
	  "created_at": {
	    "timestamp": int,
	    "offset": int		 
	  }
	}
Body Example:
	{
	  "text": "Updated example text",
	  "created_at": {
	    "timestamp": 1535739872,
	    "offset": 7200
	  }
	}
Success Response:
	{
	  "id": "56f376c6-42b6-42b6-8afc-5675fca5f02a"
	  "text": "Updated example text",
	  "created_at": {
	    "timestamp": 1535739872,
	    "offset": 7200
	  },
	  "user": {
	    "id": "b5eb1e6a-25ed-49f1-82c8-4cb9ff1e40b7",
	    "name": "Kalle Karlsson"
	  }
	}
```

### Delete Message

This endpoint deletes a message.

```
Method:
	DELETE
Path:
	/api/v1/users/:user_id/messages/:message_id
Path Parameters:
	- user_id: Id of the user
	- message_id: Id of the message
Headers:
	- Content-Type: application/json
	- Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ... 
```

### Fetch All Messages

This endpoint returns all messages.

```
Method:
	GET
Path:
	/api/v1/messages
Headers:
	- Content-Type: application/json 
Success Response:
    {
      "messages": [
        {
          "id": "56f376c6-42b6-42b6-8afc-5675fca5f02a"
          "text": "Example text from Kalle",
          "created_at": {
            "timestamp": 1535739872,
            "offset": 7200
          },
          "user": {
            "id": "b5eb1e6a-25ed-49f1-82c8-4cb9ff1e40b7",
            "name": "Kalle Karlsson"
          }
        },
        {
          "id": "7b49b316-e8cc-46ac-be95-9055cd371dc8"
          "text": "Example text from Olle",
          "created_at": {
            "timestamp": 15357456172,
            "offset": 7200
          },
          "user": {
            "id": "3b62c987-506a-4aca-909b-9ed227ea9c48",
            "name": "Olle Andersson"
          }
        }
      ]
    }
```


