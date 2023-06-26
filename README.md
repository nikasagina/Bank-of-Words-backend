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

## QuestionController

### Start Learning

Starts a new learning session and retrieves a question for the user.

**Endpoint**: `GET /api/question`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the question and choices for the user.
- Error: Returns an unauthorized status if authentication fails.

### Start Learning Spelling

Starts a new learning session and retrieves a question about spelling for the user.

**Endpoint**: `GET /api/question/spelling`

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

## StatisticsController

### Get User Statistics

Retrieves various statistics for a user.

**Endpoint**: `GET /api/stats/user`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the following statistics:
    - `total_guesses_count` (long) - The total number of guesses made by the user.
    - `success_guesses_count` (long) - The total number of successful guesses made by the user.
    - `success_rate` (double) - The overall success rate of the user.
    - `most_guessed_word` (string) - The word that has been most guessed by the user.
    - `least_guessed_word` (string) - The word that has been least guessed by the user.
- Error: Returns an unauthorized status if authentication fails.

### Get Global Statistics

Retrieves various global statistics.

**Endpoint**: `GET /api/stats/global`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the following statistics:
    - `most_guessed_word` (string) - The word that has been most guessed overall by all users.
    - `least_guessed_word` (string) - The word that has been least guessed overall by all users.
    - `top_user` (string) - The username of the user with the best success rate.
    - `top_5` (array of strings) - An array of the top 5 usernames with the best success rates.
- Error: Returns an unauthorized status if authentication fails.

## UploadController

### Upload Word

Uploads a new word with its definition for a user.

**Endpoint**: `POST /api/upload/word`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Request Parameters

- `word` (string, required) - The word to be uploaded.
- `definition` (string, required) - The definition of the word.

#### Response

- Success: Returns a JSON object with the following:
  - `successful` (boolean) - Indicates whether the word was successfully uploaded.
- Error: Returns an unauthorized status if authentication fails.


### Upload Book

Uploads a book file and extracts unknown words from it. The unknown words are then added to the user's word collection.

**Endpoint**: `POST /api/upload/book`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Request Parameters

- `file` (file, required) - The book file to be uploaded.

#### Response

- Success: Returns a JSON object with the following:
  - `successful` (boolean) - Indicates whether the book was successfully processed.
  - `message` (string) - A success message.
  - `added words` (object) - A map of the added words and their definitions.

- Error: Returns an unauthorized status if authentication fails or a bad request status if the file format is unsupported.

## WordController

### Learn Word

Marks a word as learned by the user.

**Endpoint**: `POST /api/word/learn`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Request Parameters

- `word` (string, required) - The word to mark as learned.

#### Response

- Success: Returns a JSON object with the success status.
- Error: Returns an unauthorized status if authentication fails.

### Get Definitions

Retrieves available definitions for a word.

**Endpoint**: `GET /api/word/definitions`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Request Parameters

- `word` (string, required) - The word to get definitions for.

#### Response

- Success: Returns a JSON object with the available definitions.
- Error: Returns an unauthorized status if authentication fails.

### Get Word Info

Retrieves detailed information about a word.

**Endpoint**: `GET /api/word/info`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Request Parameters

- `word` (string, required) - The word to get information for.

#### Response

- Success: Returns a JSON object with detailed information about the word, including definition, other definitions, examples, synonyms, and antonyms.
- Error: Returns an unauthorized status if authentication fails.

### Get All Learning Words

Retrieves all words that the user is currently learning.

**Endpoint**: `GET /api/word/learning`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the list of learning words.
- Error: Returns an unauthorized status if authentication fails.

### Get All Learned Words

Retrieves all words that the user has learned.

**Endpoint**: `GET /api/word/learned`

#### Request Headers

- `Authorization` (string, required) - The JWT token for authentication.

#### Response

- Success: Returns a JSON object with the list of learned words.
- Error: Returns an unauthorized status if authentication fails.
