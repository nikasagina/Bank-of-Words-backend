# Bank of Words REST API Documentation

This documentation provides an overview of the REST API endpoints available in the Bank of Words application.

## AuthController

### Register User

Registers a new user in the system.

**Endpoint**: `POST /api/register`

#### Request Parameters

- `username` (string, required) - The username of the user.
- `password` (string, required) - The password of the user.
- `email` (string, required) - The email of the user.

#### Response

- Success: Returns a JSON object with the registration status.
- Error: Returns a JSON object with error details.

### Authenticate User

Authenticates a user and generates a JWT token.

**Endpoint**: `POST /api/authenticate`

#### Request Body

- `username` (string) - The username of the user.
- `password` (string) - The password of the user.

#### Response

- Success: Returns a JSON object with the generated JWT token.
- Error: Returns an unauthorized status if authentication fails.

## StartController

### Start Learning

Starts a new learning session and retrieves a question for the user.

**Endpoint**: `GET /api/question`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the question and choices for the user.
- Error: Returns an unauthorized status if authentication fails.

### Submit Answer

Submits the user's answer to a question.

**Endpoint**: `POST /api/answer`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Request Parameters

- `guess` (string, required) - The user's answer.
- `id` (number, required) - The ID of the flashcard question.

#### Response

- Success: Returns a JSON object with the correctness of the answer and the correct answer.
- Error: Returns an unauthorized status if authentication fails.

### Learn Word

Marks a word as learned by the user.

**Endpoint**: `POST /api/learn`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Request Parameters

- `word` (string, required) - The word to mark as learned.

#### Response

- Success: Returns a JSON object with the success status.
- Error: Returns an unauthorized status if authentication fails.

## StatisticsController

### Get User Success Rate for a Word

Retrieves the success rate of a user for a specific word.

**Endpoint**: `GET /api/stats/user/word-rate`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Request Parameters

- `word` (string, required) - The word to retrieve the success rate for.

#### Response

- Success: Returns a JSON object with the success rate for the word.
- Error: Returns an unauthorized status if authentication fails.

### Get User Total Guesses Count

Retrieves the total number of guesses made by a user.

**Endpoint**: `GET /api/stats/user/count/total-guesses`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the total number of guesses made by the user.
- Error: Returns an unauthorized status if authentication fails.

### Get User Successful Guesses Count

Retrieves the total number of successful guesses made by a user.

**Endpoint**: `GET /api/stats/user/count/success-guesses`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the total number of successful guesses made by the user.
- Error: Returns an unauthorized status if authentication fails.

### Get User Success Rate

Retrieves the overall success rate of a user.

**Endpoint**: `GET /api/stats/user/success-rate`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the overall success rate of the user.
- Error: Returns an unauthorized status if authentication fails.

### Get Word Most Guessed by User

Retrieves the word that has been most guessed by a user.

**Endpoint**: `GET /api/stats/user/word/most-guessed`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the most guessed word by the user.
- Error: Returns an unauthorized status if authentication fails.

### Get Word Least Guessed by User

Retrieves the word that has been least guessed by a user.

**Endpoint**: `GET /api/stats/user/word/least-guessed`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the least guessed word by the user.
- Error: Returns an unauthorized status if authentication fails.

### Get Most Guessed Word Overall

Retrieves the word that has been most guessed overall by all users.

**Endpoint**: `GET /api/stats/most-guessed`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the most guessed word overall.
- Error: Returns an unauthorized status if authentication fails.

### Get Least Guessed Word Overall

Retrieves the word that has been least guessed overall by all users.

**Endpoint**: `GET /api/stats/least-guessed`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the least guessed word overall.
- Error: Returns an unauthorized status if authentication fails.

### Get Top User

Retrieves the user with the best success rate.

**Endpoint**: `GET /api/stats/top-user`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the username of the top user.
- Error: Returns an unauthorized status if authentication fails.

### Get Top 5 Users

Retrieves the top 5 users with the best success rates.

**Endpoint**: `GET /api/stats/top-5`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with an array of the top 5 usernames.
- Error: Returns an unauthorized status if authentication fails.