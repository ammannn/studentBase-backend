
<h1 id="mc-master-student-housing-portal">Mc Master Student Housing Portal v0.0.1</h1>

> Scroll down for code samples, example requests and responses. Select a language for code samples from the tabs above or the mobile navigation menu.


# How to Run the Project

### Prerequisites

- Java JDK version 21
- Maven

### Environment Variables

Before running the project, ensure you have the following environment variables set:

- `ENVIRONMENT`: The environment in which the application is running (e.g., development, production).
- `PROJECT_ID`: The ID of the project.
- `BUCKET_NAME`: The name of the bucket for storage.
- `GOOGLE_APPLICATION_CREDENTIALS`: The path to the Google Cloud Platform service account key file.
- `ADMIN_EMAIL`: The email address of the admin user.
- `STRIPE_API_KEY`: The API key for accessing the Stripe payment gateway.
- `STRIPE_ENDPOINT_SECRET`: The endpoint secret for Stripe webhook verification.
- `PLATFORM_URL`: The URL of the platform.
- `SHEERID_PROGRAM_ID`: The ID of the SheerID program.

1. **Clone the repository:**
    ```sh
    git clone <repository-url>
    ```

2. **Navigate to the project directory:**
    ```sh
    cd <project-directory>
    ```

3. **Build the project using Maven:**
    ```sh
    mvn clean install
    ```

4. **Run the project:**
    ```sh
    mvn spring-boot:run
    ```

5. **Access the API documentation:**
   Open a web browser and navigate to `<back-end-url>/swagger-ui.html` for API documentation if Swagger is enabled.

---

**Note:** Replace placeholder text (e.g., `<repository-url>`, `<project-directory>`, `<back-end-url>`) with actual values.

---


# AuthController API Documentation

## Overview
The `AuthController` handles authentication-related requests such as user registration, login, admin login, and SheerID verification.

## Base URL
```
/api
```

## Endpoints

### 1. Register User

**Endpoint:** `/register`  
**Method:** `POST`  
**Description:** Registers a new user.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Body:**
```json
{
  "username": "string",
  "password": "string",
  "email": "string"
}
```

**Response:**
- Success: HTTP 200 with user registration details.
- Failure: HTTP 400 with error message.

---

### 2. User Login

**Endpoint:** `/login`  
**Method:** `POST`  
**Description:** Logs in a user.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response:**
- Success: HTTP 200 with login details.
- Failure: HTTP 401 with error message.

---

### 3. Admin Login

**Endpoint:** `/login/admin`  
**Method:** `POST`  
**Description:** Logs in an admin user.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Body:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response:**
- Success: HTTP 200 with admin login details.
- Failure: HTTP 401 with error message.

---

### 4. Verify on SheerID

**Endpoint:** `/sheerId/verify`  
**Method:** `POST`  
**Description:** Verifies a user on SheerID.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Body:**
```json
{
  "sheerIdToken": "string"
}
```

**Response:**
- Success: HTTP 200 with verification details.
- Failure: HTTP 400 with error message.


# ApplicationController API Documentation

## Overview
The `ApplicationController` handles operations related to rental applications, including listing applications by status, creating applications, updating application status, and managing students associated with an application.

## Base URL
```
/api/applications
```

## Endpoints

### 1. Get Applications

**Endpoint:** `/applications`  
**Method:** `GET`  
**Description:** Lists applications by status.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Parameters:**
- `status` (ApplicationStatus): Status of the applications to be listed.
- `rentalUnitId` (String, optional): ID of the rental unit.
- `lastSeen` (String, optional): Last seen application ID for pagination.
- `limit` (int, optional, default: 15): Number of applications to retrieve.

**Response:**
- Success: HTTP 200 with application details.
- Failure: HTTP 400 with error message.

---

### 2. Create Application

**Endpoint:** `/applications`  
**Method:** `POST`  
**Description:** Creates an application for a student with an initial status of `visit_requested`.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Body:**
```json
{
  "studentId": "string",
  "rentalUnitId": "string",
  "visitTime": "string",
  "additionalData": "string"
}
```

**Response:**
- Success: HTTP 201 with created application details.
- Failure: HTTP 400 with error message.

---

### 3. Get Application By ID

**Endpoint:** `/applications/{applicationId}`  
**Method:** `PUT`  
**Description:** Retrieves an application by its ID.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `applicationId` (String): ID of the application to retrieve.

**Response:**
- Success: HTTP 200 with application details.
- Failure: HTTP 404 with error message.

---

### 4. Update Application Status

**Endpoint:** `/applications/{applicationId}/status`  
**Method:** `PUT`  
**Description:** Updates the status of an application.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `applicationId` (String): ID of the application to update.

**Request Parameters:**
- `status` (ApplicationStatus): New status of the application.

**Response:**
- Success: HTTP 200 with updated application status.
- Failure: HTTP 400 with error message.

---

### 5. Update Application Status (Batch)

**Endpoint:** `/applications/status/v2`  
**Method:** `PUT`  
**Description:** Updates the status of multiple applications.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Body:**
```json
[
  "applicationId1",
  "applicationId2"
]
```

**Request Parameters:**
- `status` (ApplicationStatus): New status of the applications.

**Response:**
- Success: HTTP 200 with updated application statuses.
- Failure: HTTP 400 with error message.

---

### 6. Delete Application

**Endpoint:** `/applications/{applicationId}`  
**Method:** `DELETE`  
**Description:** Deletes an application by its ID.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `applicationId` (String): ID of the application to delete.

**Response:**
- Success: HTTP 200 with deletion confirmation.
- Failure: HTTP 404 with error message.

---

### 7. Add or Remove Students for Application

**Endpoint:** `/applications/{applicationId}/students`  
**Method:** `PUT`  
**Description:** Adds or removes students for an application. This operation can only be performed when the application status is `pending_document_upload`.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `applicationId` (String): ID of the application to update.

**Request Body:**
```json
{
  "studentIds": [
    "studentId1",
    "studentId2"
  ]
}
```

**Response:**
- Success: HTTP 200 with updated application details.
- Failure: HTTP 400 with error message.


# AdminController API Documentation

## Overview
The `AdminController` handles administrative operations related to users and rental units, including listing users and rental units by verification status, and updating the verification status of users and rental units.

## Base URL
```
/api/admin
```

## Endpoints

### 1. Get Users

**Endpoint:** `/users`  
**Method:** `GET`  
**Description:** Lists users by verification status.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Parameters:**
- `verificationStatus` (VerificationStatus, optional, default: `pending`): Verification status of the users to be listed.
- `limit` (int, optional, default: 50): Number of users to retrieve.
- `lastSeen` (String, optional): Last seen user ID for pagination.

**Response:**
- Success: HTTP 200 with user details.
- Failure: HTTP 400 with error message.

---

### 2. Get Rental Units

**Endpoint:** `/rental-units`  
**Method:** `GET`  
**Description:** Lists rental units by verification status.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Parameters:**
- `verificationStatus` (VerificationStatus, optional, default: `pending`): Verification status of the rental units to be listed.
- `limit` (int, optional, default: 50): Number of rental units to retrieve.
- `lastSeen` (String, optional): Last seen rental unit ID for pagination.

**Response:**
- Success: HTTP 200 with rental unit details.
- Failure: HTTP 400 with error message.

---

### 3. Update Rental Unit Status

**Endpoint:** `/rental-units/{rentalUnitId}/status`  
**Method:** `PUT`  
**Description:** Updates the verification status of a rental unit.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `rentalUnitId` (String): ID of the rental unit to update.

**Request Parameters:**
- `verificationStatus` (VerificationStatus, optional, default: `pending`): New verification status of the rental unit.
- `reason` (String, optional): Reason for the status update.

**Response:**
- Success: HTTP 200 with updated rental unit status.
- Failure: HTTP 400 with error message.

---

### 4. Update User Status

**Endpoint:** `/users/{userId}/status`  
**Method:** `PUT`  
**Description:** Updates the verification status of a user.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `userId` (String): ID of the user to update.

**Request Parameters:**
- `verificationStatus` (VerificationStatus, optional, default: `pending`): New verification status of the user.
- `reason` (String, optional): Reason for the status update.

**Response:**
- Success: HTTP 200 with updated user status.
- Failure: HTTP 400 with error message.


# LikeAndRatingController API Documentation

## Overview
The `LikeAndRatingController` handles operations related to liking and rating rental units, including liking a rental unit, rating a rental unit, retrieving liked rental units, and retrieving reviews for a rental unit.

## Base URL
```
/api
```

## Endpoints

### 1. Like Rental Unit

**Endpoint:** `/like/rental-unit/{rentalUnitId}`  
**Method:** `POST`  
**Description:** Likes or unlikes a rental unit.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `rentalUnitId` (String): ID of the rental unit to like or unlike.

**Request Parameters:**
- `like` (boolean, optional, default: `true`): Whether to like (`true`) or unlike (`false`) the rental unit.

**Response:**
- Success: HTTP 200 with like/unlike confirmation.
- Failure: HTTP 400 with error message.

---

### 2. Rate Rental Unit

**Endpoint:** `/rating/rental-unit/{rentalUnitId}`  
**Method:** `POST`  
**Description:** Rates a rental unit.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `rentalUnitId` (String): ID of the rental unit to rate.

**Request Parameters:**
- `star` (int, optional, default: 3): Rating star value (1-5).

**Request Body:**
```json
{
  "comment": "string",
  "additionalData": "string"
}
```

**Response:**
- Success: HTTP 200 with rating confirmation.
- Failure: HTTP 400 with error message.

---

### 3. Get Liked Rental Units

**Endpoint:** `/like/rental-unit`  
**Method:** `GET`  
**Description:** Retrieves a list of liked rental units.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Response:**
- Success: HTTP 200 with list of liked rental units.
- Failure: HTTP 400 with error message.

---

### 4. Get Reviews by Rental Unit ID

**Endpoint:** `/reviews/{rentalUnitId}/rental-unit`  
**Method:** `GET`  
**Description:** Retrieves reviews for a specific rental unit.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `rentalUnitId` (String): ID of the rental unit to retrieve reviews for.

**Request Parameters:**
- `lastSeen` (String, optional): Last seen review ID for pagination.
- `limit` (int, optional, default: 100): Number of reviews to retrieve.

**Response:**
- Success: HTTP 200 with list of reviews.
- Failure: HTTP 400 with error message.


# MainController API Documentation

## Overview
The `MainController` provides basic endpoints for retrieving version information, health status, country data, time zones, and searching organizations via SheerId.

## Base URL
```
/api
```

## Endpoints

### 1. Get API Version

**Endpoint:** `/version`  
**Method:** `GET`  
**Description:** Retrieves the current version of the API.

**Response:**
- Success: HTTP 200 with version information (`v0.0.1`).
- Failure: N/A

---

### 2. Get API Health

**Endpoint:** `/health`  
**Method:** `GET`  
**Description:** Checks the health status of the API.

**Response:**
- Success: HTTP 200 with health status (`ok`).
- Failure: N/A

---

### 3. Get Countries

**Endpoint:** `/countries`  
**Method:** `GET`  
**Description:** Retrieves a list of countries from a JSON file.

**Response:**
- Success: HTTP 200 with country data.
- Failure: HTTP 500 with error message if the file cannot be read.

---

### 4. Get Time Zones

**Endpoint:** `/time-zones`  
**Method:** `GET`  
**Description:** Retrieves a list of available time zones with their GMT offsets.

**Response:**
- Success: HTTP 200 with a list of time zones.
- Failure: N/A

---

### 5. Get SheerId Organizations

**Endpoint:** `/sheerId/orgs`  
**Method:** `POST`  
**Description:** Searches for organizations via SheerId based on the search term and country.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Body:**
```json
{
  "searchTerm": "string",
  "country": "string"
}
```

**Response:**
- Success: HTTP 200 with organization search results.
- Failure: HTTP 400 with error message.


# RentalUnitController API Documentation

## Overview
The `RentalUnitController` handles operations related to rental units, including retrieving rental units, searching rental units, retrieving rental unit details, adding, updating, and deleting rental units, and fetching static data about rental unit features.

## Base URL
```
/api
```

## Endpoints

### 1. Get Rental Units

**Endpoint:** `/rental-units`  
**Method:** `GET`  
**Description:** Retrieves a list of rental units.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Parameters:**
- `limit` (int, optional, default: 10): Number of rental units to retrieve.
- `lastSeen` (String, optional): Last seen rental unit ID for pagination.
- `fetchLiveOnly` (boolean, optional): Whether to fetch only live rental units.

**Response:**
- Success: HTTP 200 with list of rental units.
- Failure: HTTP 400 with error message.

---

### 2. Search Rental Units

**Endpoint:** `/rental-units/search`  
**Method:** `POST`  
**Description:** Searches for rental units based on search criteria.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Parameters:**
- `limit` (int, optional, default: 10): Number of rental units to retrieve.
- `lastSeen` (String, optional): Last seen rental unit ID for pagination.

**Request Body:**
```json
{
  "location": "string",
  "priceRange": {
    "min": "number",
    "max": "number"
  },
  "amenities": ["string"]
}
```

**Response:**
- Success: HTTP 200 with search results.
- Failure: HTTP 400 with error message.

---

### 3. Get Rental Unit by ID

**Endpoint:** `/rental-units/{rentalUnitId}`  
**Method:** `GET`  
**Description:** Retrieves details of a rental unit by its ID.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `rentalUnitId` (String): ID of the rental unit to retrieve.

**Response:**
- Success: HTTP 200 with rental unit details.
- Failure: HTTP 400 with error message.

---

### 4. Add Rental Unit

**Endpoint:** `/rental-units`  
**Method:** `POST`  
**Description:** Adds a new rental unit.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Body:**
```json
{
  "name": "string",
  "location": "string",
  "price": "number",
  "amenities": ["string"]
}
```

**Response:**
- Success: HTTP 201 with newly added rental unit details.
- Failure: HTTP 400 with error message.

---

### 5. Update Rental Unit

**Endpoint:** `/rental-units/{rentalUnitId}`  
**Method:** `PUT`  
**Description:** Updates an existing rental unit.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `rentalUnitId` (String): ID of the rental unit to update.

**Request Body:**
```json
{
  "name": "string",
  "location": "string",
  "price": "number",
  "amenities": ["string"]
}
```

**Response:**
- Success: HTTP 200 with updated rental unit details.
- Failure: HTTP 400 with error message.

---

### 6. Delete Rental Unit

**Endpoint:** `/rental-units/{rentalUnitId}`  
**Method:** `DELETE`  
**Description:** Deletes a rental unit by its ID.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `rentalUnitId` (String): ID of the rental unit to delete.

**Response:**
- Success: HTTP 200 with deletion confirmation.
- Failure: HTTP 400 with error message.

---

### 7. Get Rental Unit Features Static Data

**Endpoint:** `/rental-units/static`  
**Method:** `GET`  
**Description:** Retrieves static data about rental unit features.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Response:**
- Success: HTTP 200 with static data.
- Failure: HTTP 400 with error message.


# UserController API Documentation

## Overview
The `UserController` provides endpoints for updating user details, retrieving user details, and searching for users based on their email.

## Base URL
```
/api
```

## Endpoints

### 1. Update User

**Endpoint:** `/users`  
**Method:** `PUT`  
**Description:** Updates user details.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Body:**
```json
{
  "name": "string",
  "email": "string",
  "phoneNumber": "string"
}
```

**Response:**
- Success: HTTP 200 with updated user details.
- Failure: HTTP 400 with error message.

---

### 2. Get User Details

**Endpoint:** `/users`  
**Method:** `GET`  
**Description:** Retrieves user details.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Response:**
- Success: HTTP 200 with user details.
- Failure: HTTP 400 with error message.

---

### 3. Search User for Application

**Endpoint:** `/users-for-applications`  
**Method:** `GET`  
**Description:** Searches for a user by email for application purposes.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Body:**
```json
{
  "email": "string"
}
```

**Response:**
- Success: HTTP 200 with user search results.
- Failure: HTTP 400 with error message.



# WebhookController API Documentation

## Overview
The `WebhookController` handles webhook requests related to payment updates.

## Base URL
```
/wh
```

## Endpoints

### 1. Update Payment Status

**Endpoint:** `/payment-update`  
**Method:** `POST`  
**Description:** Updates payment status based on webhook data.

**Request Headers:**
- `Stripe-Signature` (String): Stripe signature header.

**Request Body:**
- Raw string data containing payment update information.

**Response:**
- Success: HTTP 200 with payment update confirmation.
- Failure: HTTP 400 with error message.


# PaymentController API Documentation

## Overview
The `PaymentController` handles endpoints related to payment processing.


### 1. Create Payment Link

**Endpoint:** `/payment`  
**Method:** `POST`  
**Description:** Creates a payment link for processing payments.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Body:**
- Payment request details.

**Response:**
- Success: HTTP 200 with payment link details.
- Failure: HTTP 400 with error message.


# FileController API Documentation

## Overview
The `FileController` manages endpoints related to file handling, including uploading, confirming, deleting, and replacing files.

## Endpoints

### 1. Get Upload URL for File

**Endpoint:** `/files`  
**Method:** `POST`  
**Description:** Retrieves the upload URL for a file.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Request Body:**
- Details of the file to be uploaded.

**Response:**
- Success: HTTP 200 with the upload URL.
- Failure: HTTP 400 with error message.

---

### 2. Confirm File Upload

**Endpoint:** `/files/{fileId}/cnfm`  
**Method:** `POST`  
**Description:** Confirms the upload of a file.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `fileId` (String): ID of the uploaded file.

**Response:**
- Success: HTTP 200 with confirmation message.
- Failure: HTTP 400 with error message.

---

### 3. Delete File

**Endpoint:** `/files/{fileId}`  
**Method:** `DELETE`  
**Description:** Deletes a file.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `fileId` (String): ID of the file to delete.

**Response:**
- Success: HTTP 200 with deletion confirmation.
- Failure: HTTP 400 with error message.

---

### 4. Replace File

**Endpoint:** `/files/{fileId}/replace`  
**Method:** `PUT`  
**Description:** Replaces a file with a new one.

**Request Headers:**
- `requestId` (String): Unique identifier for the request.

**Path Parameters:**
- `fileId` (String): ID of the file to replace.

**Request Body:**
- Details of the new file.

**Response:**
- Success: HTTP 200 with replacement confirmation.
- Failure: HTTP 400 with error message.

---

## File Purposes
The following are the available file purposes:
- rental_unit_image
- rental_unit_poster_image
- bank_statement
- credit_score_report
- gic_certificate
- parents_bank_statement
- student_id
- national_id
- user_profile_image
- offered_lease_doc
- signed_lease_doc

## File Types
The following are the available file types:
- image
- video
- document

## Rental Unit Elements
The following are the available rental unit elements:
- living_room
- bed_room
- dining_room
- bath_room
- kitchen
- others

---
title: Mc Master Student Housing Portal v0.0.1
language_tabs:
- shell: Shell
  toc_footers: []
  includes: []
  search: true
  highlight_theme: darkula
  headingLevel: 2

---

# Backend APIs for mc master student housing portal

Base URLs:

* <a href="http://localhost:9098">http://localhost:9098</a>

# Authentication

- HTTP Authentication, scheme: bearer

<h1 id="mc-master-student-housing-portal-user-controller">user-controller</h1>

## getUserDetails

<a id="opIdgetUserDetails"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/users \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/users`

*getUserDetails*

<h3 id="getuserdetails-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|

> Example responses

> 200 Response

<h3 id="getuserdetails-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="getuserdetails-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## updateUser

<a id="opIdupdateUser"></a>

> Code samples

```shell
# You can also use wget
curl -X PUT http://localhost:9098/api/users \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`PUT /api/users`

*updateUser*

> Body parameter

```json
{
  "verificationStatus": "failed",
  "email": "string",
  "phoneNumber": "string",
  "name": "string",
  "dob": "string",
  "nationality": "string",
  "emergencyContact": "string",
  "additionalEmail": "string",
  "addresses": [
    {
      "country": {
        "label": "string",
        "value": "string"
      },
      "state": {
        "label": "string",
        "value": "string"
      },
      "zip": "string",
      "city": "string",
      "primary": true
    }
  ],
  "occupation": "string",
  "preferredModOfContact": "string"
}
```

<h3 id="updateuser-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|body|body|[UpdateUserRequestDto](#schemaupdateuserrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="updateuser-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="updateuser-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## searchUserForApplication

<a id="opIdsearchUserForApplication"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/users-for-applications?email=string \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/users-for-applications`

*searchUserForApplication*

<h3 id="searchuserforapplication-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|email|query|string|true|none|
|requestId|header|string|true|none|

> Example responses

> 200 Response

<h3 id="searchuserforapplication-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="searchuserforapplication-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

<h1 id="mc-master-student-housing-portal-calendar-controller">calendar-controller</h1>

## getVisitingSchedule

<a id="opIdgetVisitingSchedule"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/schedule \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/schedule`

<h3 id="getvisitingschedule-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|ownerId|query|string|false|none|
|requestId|header|string|true|none|

> Example responses

> 200 Response

<h3 id="getvisitingschedule-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="getvisitingschedule-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## updateVisitingSchedule

<a id="opIdupdateVisitingSchedule"></a>

> Code samples

```shell
# You can also use wget
curl -X PUT http://localhost:9098/api/schedule \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`PUT /api/schedule`

> Body parameter

```json
{
  "days": [
    {
      "dayOfWeek": "MONDAY",
      "timeSlots": [
        {
          "start": {
            "dayPeriod": "AM",
            "hour": 0,
            "minute": 0
          },
          "end": {
            "dayPeriod": "AM",
            "hour": 0,
            "minute": 0
          }
        }
      ]
    }
  ],
  "timeZone": "string"
}
```

<h3 id="updatevisitingschedule-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|body|body|[CreateUpdateVisitingScheduleRequestDto](#schemacreateupdatevisitingschedulerequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="updatevisitingschedule-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="updatevisitingschedule-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## createVisitingSchedule

<a id="opIdcreateVisitingSchedule"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/schedule \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/schedule`

> Body parameter

```json
{
  "days": [
    {
      "dayOfWeek": "MONDAY",
      "timeSlots": [
        {
          "start": {
            "dayPeriod": "AM",
            "hour": 0,
            "minute": 0
          },
          "end": {
            "dayPeriod": "AM",
            "hour": 0,
            "minute": 0
          }
        }
      ]
    }
  ],
  "timeZone": "string"
}
```

<h3 id="createvisitingschedule-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|body|body|[CreateUpdateVisitingScheduleRequestDto](#schemacreateupdatevisitingschedulerequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="createvisitingschedule-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseObject](#schemaapiresponseobject)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## getVisitingScheduleTemplate

<a id="opIdgetVisitingScheduleTemplate"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/schedule/template?month=JANUARY \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/schedule/template`

<h3 id="getvisitingscheduletemplate-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|month|query|string|true|none|
|requestId|header|string|true|none|

#### Enumerated Values

|Parameter|Value|
|---|---|
|month|JANUARY|
|month|FEBRUARY|
|month|MARCH|
|month|APRIL|
|month|MAY|
|month|JUNE|
|month|JULY|
|month|AUGUST|
|month|SEPTEMBER|
|month|OCTOBER|
|month|NOVEMBER|
|month|DECEMBER|

> Example responses

> 200 Response

<h3 id="getvisitingscheduletemplate-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="getvisitingscheduletemplate-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

<h1 id="mc-master-student-housing-portal-rental-unit-controller">rental-unit-controller</h1>

## getRentalUnitById

<a id="opIdgetRentalUnitById"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/rental-units/{rentalUnitId} \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/rental-units/{rentalUnitId}`

<h3 id="getrentalunitbyid-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|rentalUnitId|path|string|true|none|
|requestId|header|string|true|none|

> Example responses

> 200 Response

<h3 id="getrentalunitbyid-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseObject](#schemaapiresponseobject)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## updateRentalUnits

<a id="opIdupdateRentalUnits"></a>

> Code samples

```shell
# You can also use wget
curl -X PUT http://localhost:9098/api/rental-units/{rentalUnitId} \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`PUT /api/rental-units/{rentalUnitId}`

> Body parameter

```json
{
  "title": "string",
  "address": {
    "country": {
      "label": "string",
      "value": "string"
    },
    "state": {
      "label": "string",
      "value": "string"
    },
    "zip": "string",
    "city": "string",
    "primary": true
  },
  "rent": {
    "amount": 0,
    "currency": "usd",
    "currencySymbol": "string"
  },
  "deposit": {
    "amount": 0,
    "currency": "usd",
    "currencySymbol": "string"
  },
  "features": {
    "featuresUtilities": {
      "property1": true,
      "property2": true
    },
    "featuresAmenities": {
      "property1": true,
      "property2": true
    },
    "featuresNumbers": {
      "property1": 0.1,
      "property2": 0.1
    }
  },
  "posterImageId": "string",
  "contact": {
    "email": "string",
    "phoneNumber": "string",
    "name": "string",
    "preferredModOfContact": "string"
  },
  "description": "string",
  "leaseTerm": "string",
  "leaseStartDate": 0
}
```

<h3 id="updaterentalunits-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|rentalUnitId|path|string|true|none|
|requestId|header|string|true|none|
|body|body|[AddUpdateRentalUnitRequestDto](#schemaaddupdaterentalunitrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="updaterentalunits-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseObject](#schemaapiresponseobject)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## deleteRentalUnits

<a id="opIddeleteRentalUnits"></a>

> Code samples

```shell
# You can also use wget
curl -X DELETE http://localhost:9098/api/rental-units/{rentalUnitId} \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`DELETE /api/rental-units/{rentalUnitId}`

<h3 id="deleterentalunits-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|rentalUnitId|path|string|true|none|
|requestId|header|string|true|none|

> Example responses

> 200 Response

<h3 id="deleterentalunits-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseObject](#schemaapiresponseobject)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## getRentalUnits

<a id="opIdgetRentalUnits"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/rental-units \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/rental-units`

<h3 id="getrentalunits-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|limit|query|integer(int32)|false|none|
|lastSeen|query|string|false|none|
|fetchLiveOnly|query|boolean|false|none|

> Example responses

> 200 Response

<h3 id="getrentalunits-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseObject](#schemaapiresponseobject)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## addRentalUnit

<a id="opIdaddRentalUnit"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/rental-units \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/rental-units`

> Body parameter

```json
{
  "title": "string",
  "address": {
    "country": {
      "label": "string",
      "value": "string"
    },
    "state": {
      "label": "string",
      "value": "string"
    },
    "zip": "string",
    "city": "string",
    "primary": true
  },
  "rent": {
    "amount": 0,
    "currency": "usd",
    "currencySymbol": "string"
  },
  "deposit": {
    "amount": 0,
    "currency": "usd",
    "currencySymbol": "string"
  },
  "features": {
    "featuresUtilities": {
      "property1": true,
      "property2": true
    },
    "featuresAmenities": {
      "property1": true,
      "property2": true
    },
    "featuresNumbers": {
      "property1": 0.1,
      "property2": 0.1
    }
  },
  "posterImageId": "string",
  "contact": {
    "email": "string",
    "phoneNumber": "string",
    "name": "string",
    "preferredModOfContact": "string"
  },
  "description": "string",
  "leaseTerm": "string",
  "leaseStartDate": 0
}
```

<h3 id="addrentalunit-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|body|body|[AddUpdateRentalUnitRequestDto](#schemaaddupdaterentalunitrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="addrentalunit-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseObject](#schemaapiresponseobject)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## searchRentalUnits

<a id="opIdsearchRentalUnits"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/rental-units/search \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/rental-units/search`

> Body parameter

```json
{
  "features": {
    "featuresUtilities": {
      "property1": true,
      "property2": true
    },
    "featuresAmenities": {
      "property1": true,
      "property2": true
    },
    "featuresNumbers": {
      "property1": 0.1,
      "property2": 0.1
    }
  },
  "country": "string",
  "state": "string",
  "city": "string",
  "minRent": 0,
  "maxRent": 0
}
```

<h3 id="searchrentalunits-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|limit|query|integer(int32)|false|none|
|lastSeen|query|string|false|none|
|body|body|[SearchRentalUnitRequestDto](#schemasearchrentalunitrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="searchrentalunits-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseObject](#schemaapiresponseobject)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## getRentalUnitFeaturesStaticData

<a id="opIdgetRentalUnitFeaturesStaticData"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/rental-units/static \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/rental-units/static`

<h3 id="getrentalunitfeaturesstaticdata-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|

> Example responses

> 200 Response

<h3 id="getrentalunitfeaturesstaticdata-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseObject](#schemaapiresponseobject)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

<h1 id="mc-master-student-housing-portal-file-controller">file-controller</h1>

## replaceFile

<a id="opIdreplaceFile"></a>

> Code samples

```shell
# You can also use wget
curl -X PUT http://localhost:9098/api/files/{fileId}/replace \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`PUT /api/files/{fileId}/replace`

> Body parameter

```json
{
  "contentType": "string",
  "type": "image",
  "filePurpose": "rental_unit_image",
  "rentalUnitId": "string",
  "fileName": "string",
  "rentalUnitElement": "living_room",
  "applicationId": "string"
}
```

<h3 id="replacefile-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|fileId|path|string|true|none|
|requestId|header|string|true|none|
|body|body|[GetUploadUrlForFileRequestDto](#schemagetuploadurlforfilerequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="replacefile-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="replacefile-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## getUploadUrlForFile

<a id="opIdgetUploadUrlForFile"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/files \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/files`

> Body parameter

```json
{
  "contentType": "string",
  "type": "image",
  "filePurpose": "rental_unit_image",
  "rentalUnitId": "string",
  "fileName": "string",
  "rentalUnitElement": "living_room",
  "applicationId": "string"
}
```

<h3 id="getuploadurlforfile-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|body|body|[GetUploadUrlForFileRequestDto](#schemagetuploadurlforfilerequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="getuploadurlforfile-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseObject](#schemaapiresponseobject)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## confirmFileUpload

<a id="opIdconfirmFileUpload"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/files/{fileId}/cnfm \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/files/{fileId}/cnfm`

<h3 id="confirmfileupload-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|fileId|path|string|true|none|
|requestId|header|string|true|none|

> Example responses

> 200 Response

<h3 id="confirmfileupload-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[ApiResponseObject](#schemaapiresponseobject)|

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## deleteFile

<a id="opIddeleteFile"></a>

> Code samples

```shell
# You can also use wget
curl -X DELETE http://localhost:9098/api/files/{fileId} \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`DELETE /api/files/{fileId}`

<h3 id="deletefile-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|fileId|path|string|true|none|
|requestId|header|string|true|none|

> Example responses

> 200 Response

<h3 id="deletefile-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="deletefile-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

<h1 id="mc-master-student-housing-portal-application-controller">application-controller</h1>

## getApplicationById

<a id="opIdgetApplicationById"></a>

> Code samples

```shell
# You can also use wget
curl -X PUT http://localhost:9098/api/applications/{applicationId} \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`PUT /api/applications/{applicationId}`

*getApplicationById*

<h3 id="getapplicationbyid-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|applicationId|path|string|true|none|
|requestId|header|string|true|none|

> Example responses

> 200 Response

<h3 id="getapplicationbyid-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="getapplicationbyid-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## deleteApplication

<a id="opIddeleteApplication"></a>

> Code samples

```shell
# You can also use wget
curl -X DELETE http://localhost:9098/api/applications/{applicationId} \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`DELETE /api/applications/{applicationId}`

*deleteApplication*

<h3 id="deleteapplication-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|applicationId|path|string|true|none|
|requestId|header|string|true|none|

> Example responses

> 200 Response

<h3 id="deleteapplication-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="deleteapplication-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## addOrRemoveStudentsForApplication

<a id="opIdaddOrRemoveStudentsForApplication"></a>

> Code samples

```shell
# You can also use wget
curl -X PUT http://localhost:9098/api/applications/{applicationId}/students \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`PUT /api/applications/{applicationId}/students`

*addOrRemoveStudentsForApplication*

provide userIds,
,new students can be added when application in the status 'pending_document_upload'
, this api will replace current list of users with the new requested list of user ids

> Body parameter

```json
{
  "students": [
    "string"
  ]
}
```

<h3 id="addorremovestudentsforapplication-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|applicationId|path|string|true|none|
|requestId|header|string|true|none|
|body|body|[AddOrRemoveStudentsForApplicationRequestDto](#schemaaddorremovestudentsforapplicationrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="addorremovestudentsforapplication-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="addorremovestudentsforapplication-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## updateApplicationStatus

<a id="opIdupdateApplicationStatus"></a>

> Code samples

```shell
# You can also use wget
curl -X PUT http://localhost:9098/api/applications/{applicationId}/status?status=visit_requested \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`PUT /api/applications/{applicationId}/status`

*updateApplicationStatus*

the method is used to update status (transition application from one stage to another for example from 'pending_document_upload' to 'review_in_process' to 'approved'). following is the possible transitions

visit_requested -> view_property
view_property -> pending_document_upload
pending_document_upload -> review_in_process
review_in_process -> approved , rejected

students operation ->

from view_property to 'pending_document_upload' i.e. student can pick status='pending_document_upload' as next status if current status is view_property, (if user have viewed property next this to do is to upload docs so status 'pending_document_upload' represents status where user will upload document)

from pending_document_upload to 'review_in_process' i.e if student's document are already uploaded then user can push application to review_in_process status, this is where applications will be listed on rental unit owner

landlord's operation ->

from review_in_process to approved , as landlord will have application in status 'review_in_process' where ll can review and either accept or reject application

from review_in_process to rejected

<h3 id="updateapplicationstatus-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|applicationId|path|string|true|none|
|status|query|string|true|none|
|requestId|header|string|true|none|

#### Enumerated Values

|Parameter|Value|
|---|---|
|status|visit_requested|
|status|view_property|
|status|pending_document_upload|
|status|review_in_process|
|status|approved|
|status|rejected|
|status|lease_offered|
|status|lease_signed|
|status|payment_done|
|status|payment_failed|

> Example responses

> 200 Response

<h3 id="updateapplicationstatus-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="updateapplicationstatus-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## updateApplicationStatusV2

<a id="opIdupdateApplicationStatusV2"></a>

> Code samples

```shell
# You can also use wget
curl -X PUT http://localhost:9098/api/applications/status/v2?status=visit_requested \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`PUT /api/applications/status/v2`

> Body parameter

```json
[
  "string"
]
```

<h3 id="updateapplicationstatusv2-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|status|query|string|true|none|
|requestId|header|string|true|none|
|body|body|array[string]|true|none|

#### Enumerated Values

|Parameter|Value|
|---|---|
|status|visit_requested|
|status|view_property|
|status|pending_document_upload|
|status|review_in_process|
|status|approved|
|status|rejected|
|status|lease_offered|
|status|lease_signed|
|status|payment_done|
|status|payment_failed|

> Example responses

> 200 Response

<h3 id="updateapplicationstatusv2-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="updateapplicationstatusv2-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## getApplications

<a id="opIdgetApplications"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/applications?status=visit_requested \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/applications`

*getApplications*

api to list application by status
allowed status for landlord ->
rejected, approved ,visit_requested (to see applications where visit was requested) ,review_in_process (to see applications where landlord has to review applications to finally approve or reject)

allowed status for students ->
visit_requested - to see the application where students are still expecting visit to be approved
view_property - to see application where visit was approved
pending_document_upload - to application where document upload is pending
review_in_process
approved
rejected

<h3 id="getapplications-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|status|query|string|true|none|
|rentalUnitId|query|string|false|none|
|lastSeen|query|string|false|none|
|limit|query|integer(int32)|false|none|
|requestId|header|string|true|none|

#### Enumerated Values

|Parameter|Value|
|---|---|
|status|visit_requested|
|status|view_property|
|status|pending_document_upload|
|status|review_in_process|
|status|approved|
|status|rejected|
|status|lease_offered|
|status|lease_signed|
|status|payment_done|
|status|payment_failed|

> Example responses

> 200 Response

<h3 id="getapplications-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="getapplications-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## createApplication

<a id="opIdcreateApplication"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/applications \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/applications`

*createApplication*

create application for student, timezone and data are required to create application as application will be created with status='visit_requested' which will be listed in rental unit owner for approval of visit

> Body parameter

```json
{
  "rentalUnitId": "string",
  "date": "string",
  "timeZone": "string"
}
```

<h3 id="createapplication-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|body|body|[CreateApplicationRequestDto](#schemacreateapplicationrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="createapplication-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="createapplication-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

<h1 id="mc-master-student-housing-portal-admin-controller">admin-controller</h1>

## updateUserStatus

<a id="opIdupdateUserStatus"></a>

> Code samples

```shell
# You can also use wget
curl -X PUT http://localhost:9098/api/admin/users/{userId}/status \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`PUT /api/admin/users/{userId}/status`

<h3 id="updateuserstatus-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|userId|path|string|true|none|
|verificationStatus|query|string|false|none|
|requestId|header|string|true|none|
|reason|query|string|false|none|

#### Enumerated Values

|Parameter|Value|
|---|---|
|verificationStatus|failed|
|verificationStatus|pending|
|verificationStatus|verified|

> Example responses

> 200 Response

<h3 id="updateuserstatus-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="updateuserstatus-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## updateRentalUnitsStatus

<a id="opIdupdateRentalUnitsStatus"></a>

> Code samples

```shell
# You can also use wget
curl -X PUT http://localhost:9098/api/admin/rental-units/{rentalUnitId}/status \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`PUT /api/admin/rental-units/{rentalUnitId}/status`

<h3 id="updaterentalunitsstatus-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|rentalUnitId|path|string|true|none|
|verificationStatus|query|string|false|none|
|requestId|header|string|true|none|
|reason|query|string|false|none|

#### Enumerated Values

|Parameter|Value|
|---|---|
|verificationStatus|failed|
|verificationStatus|pending|
|verificationStatus|verified|

> Example responses

> 200 Response

<h3 id="updaterentalunitsstatus-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="updaterentalunitsstatus-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## getUsers

<a id="opIdgetUsers"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/admin/users \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/admin/users`

<h3 id="getusers-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|verificationStatus|query|string|false|none|
|requestId|header|string|true|none|
|limit|query|integer(int32)|false|none|
|lastSeen|query|string|false|none|

#### Enumerated Values

|Parameter|Value|
|---|---|
|verificationStatus|failed|
|verificationStatus|pending|
|verificationStatus|verified|

> Example responses

> 200 Response

<h3 id="getusers-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="getusers-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## getRentalUnits_1

<a id="opIdgetRentalUnits_1"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/admin/rental-units \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/admin/rental-units`

<h3 id="getrentalunits_1-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|verificationStatus|query|string|false|none|
|requestId|header|string|true|none|
|limit|query|integer(int32)|false|none|
|lastSeen|query|string|false|none|

#### Enumerated Values

|Parameter|Value|
|---|---|
|verificationStatus|failed|
|verificationStatus|pending|
|verificationStatus|verified|

> Example responses

> 200 Response

<h3 id="getrentalunits_1-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="getrentalunits_1-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

<h1 id="mc-master-student-housing-portal-webhook-controller">webhook-controller</h1>

## updatePaymentStatus

<a id="opIdupdatePaymentStatus"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/wh/payment-update \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'Stripe-Signature: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /wh/payment-update`

> Body parameter

```json
"string"
```

<h3 id="updatepaymentstatus-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|Stripe-Signature|header|string|true|none|
|body|body|string|true|none|

> Example responses

> 200 Response

<h3 id="updatepaymentstatus-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="updatepaymentstatus-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

<h1 id="mc-master-student-housing-portal-auth-controller">auth-controller</h1>

## verifyOnSheerId

<a id="opIdverifyOnSheerId"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/sheerId/verify \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/sheerId/verify`

> Body parameter

```json
{
  "email": "string",
  "birthDate": "string",
  "firstName": "string",
  "lastName": "string",
  "organization": {
    "id": 0,
    "idExtended": "string",
    "name": "string",
    "country": "string",
    "type": "string"
  },
  "phoneNumber": "string"
}
```

<h3 id="verifyonsheerid-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|body|body|[SheerIdVerificationRequestDto](#schemasheeridverificationrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="verifyonsheerid-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="verifyonsheerid-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## register

<a id="opIdregister"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/register \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/register`

> Body parameter

```json
{
  "email": "string",
  "dob": 0,
  "address": "string",
  "phoneNumber": "string",
  "country": "string",
  "gender": "male",
  "emergencyContact": "string",
  "name": "string",
  "role": "string",
  "authToken": "string"
}
```

<h3 id="register-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|body|body|[RegisterRequestDto](#schemaregisterrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="register-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="register-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## login

<a id="opIdlogin"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/login \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/login`

> Body parameter

```json
{
  "authToken": "string"
}
```

<h3 id="login-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|body|body|[LogInRequestDto](#schemaloginrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="login-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="login-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## adminLogin

<a id="opIdadminLogin"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/login/admin \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/login/admin`

> Body parameter

```json
{
  "authToken": "string"
}
```

<h3 id="adminlogin-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|body|body|[LogInRequestDto](#schemaloginrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="adminlogin-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="adminlogin-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

<h1 id="mc-master-student-housing-portal-main-controller">main-controller</h1>

## getSheerIdOrgs

<a id="opIdgetSheerIdOrgs"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/sheerId/orgs \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/sheerId/orgs`

> Body parameter

```json
{
  "searchTerm": "string",
  "country": "string"
}
```

<h3 id="getsheeridorgs-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|body|body|[SheerIdOrgSearchRequestDto](#schemasheeridorgsearchrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="getsheeridorgs-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="getsheeridorgs-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## version

<a id="opIdversion"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/version \
  -H 'Accept: */*' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/version`

> Example responses

> 200 Response

<h3 id="version-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="version-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## getTimeZones

<a id="opIdgetTimeZones"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/time-zones \
  -H 'Accept: */*' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/time-zones`

> Example responses

> 200 Response

<h3 id="gettimezones-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="gettimezones-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## health

<a id="opIdhealth"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/health \
  -H 'Accept: */*' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/health`

> Example responses

> 200 Response

<h3 id="health-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="health-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## getCountries

<a id="opIdgetCountries"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/countries \
  -H 'Accept: */*' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/countries`

> Example responses

> 200 Response

<h3 id="getcountries-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="getcountries-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

<h1 id="mc-master-student-housing-portal-like-and-rating-controller">like-and-rating-controller</h1>

## rateRentalUnit

<a id="opIdrateRentalUnit"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/rating/rental-unit/{rentalUnitId} \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/rating/rental-unit/{rentalUnitId}`

> Body parameter

```json
{
  "review": "string"
}
```

<h3 id="raterentalunit-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|rentalUnitId|path|string|true|none|
|star|query|integer(int32)|false|none|
|requestId|header|string|true|none|
|body|body|[RateRentalUnitRequestDto](#schemaraterentalunitrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="raterentalunit-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="raterentalunit-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## likeRentalUnit

<a id="opIdlikeRentalUnit"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/like/rental-unit/{rentalUnitId} \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/like/rental-unit/{rentalUnitId}`

<h3 id="likerentalunit-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|rentalUnitId|path|string|true|none|
|like|query|boolean|false|none|
|requestId|header|string|true|none|

> Example responses

> 200 Response

<h3 id="likerentalunit-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="likerentalunit-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## getReviewsByRentalUnitId

<a id="opIdgetReviewsByRentalUnitId"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/reviews/{rentalUnitId}/rental-unit \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/reviews/{rentalUnitId}/rental-unit`

<h3 id="getreviewsbyrentalunitid-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|rentalUnitId|path|string|true|none|
|requestId|header|string|true|none|
|lastSeen|query|string|false|none|
|limit|query|integer(int32)|false|none|

> Example responses

> 200 Response

<h3 id="getreviewsbyrentalunitid-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="getreviewsbyrentalunitid-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## getLikedRentalUnits

<a id="opIdgetLikedRentalUnits"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/api/like/rental-unit \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /api/like/rental-unit`

<h3 id="getlikedrentalunits-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|

> Example responses

> 200 Response

<h3 id="getlikedrentalunits-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="getlikedrentalunits-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

<h1 id="mc-master-student-housing-portal-payment-controller">payment-controller</h1>

## createPaymentLink

<a id="opIdcreatePaymentLink"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:9098/api/payment \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*' \
  -H 'requestId: string' \
  -H 'Authorization: Bearer {access-token}'

```

`POST /api/payment`

> Body parameter

```json
{
  "requestType": "payment_for_listing",
  "entityId": "string"
}
```

<h3 id="createpaymentlink-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|requestId|header|string|true|none|
|body|body|[PaymentRequestDto](#schemapaymentrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="createpaymentlink-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="createpaymentlink-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

<h1 id="mc-master-student-housing-portal-migration-controller">migration-controller</h1>

## fixUserDocsMap

<a id="opIdfixUserDocsMap"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/migration/user-doc \
  -H 'Accept: */*' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /migration/user-doc`

> Example responses

> 200 Response

<h3 id="fixuserdocsmap-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="fixuserdocsmap-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## studentVerificationStatus

<a id="opIdstudentVerificationStatus"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/migration/student-verification-status \
  -H 'Accept: */*' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /migration/student-verification-status`

> Example responses

> 200 Response

<h3 id="studentverificationstatus-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="studentverificationstatus-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## fixStorageCors

<a id="opIdfixStorageCors"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/migration/storage-cors \
  -H 'Accept: */*' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /migration/storage-cors`

> Example responses

> 200 Response

<h3 id="fixstoragecors-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="fixstoragecors-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## leasTermToString

<a id="opIdleasTermToString"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/migration/leas-term-to-string \
  -H 'Accept: */*' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /migration/leas-term-to-string`

> Example responses

> 200 Response

<h3 id="leastermtostring-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="leastermtostring-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

## updateLandlordDashboard

<a id="opIdupdateLandlordDashboard"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:9098/migration/landlord-dashboard \
  -H 'Accept: */*' \
  -H 'Authorization: Bearer {access-token}'

```

`GET /migration/landlord-dashboard`

> Example responses

> 200 Response

<h3 id="updatelandlorddashboard-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="updatelandlorddashboard-responseschema">Response Schema</h3>

<aside class="warning">
To perform this operation, you must be authenticated by means of one of the following methods:
bearer-jwt ( Scopes: read write )
</aside>

# Schemas

<h2 id="tocS_Address">Address</h2>
<!-- backwards compatibility -->
<a id="schemaaddress"></a>
<a id="schema_Address"></a>
<a id="tocSaddress"></a>
<a id="tocsaddress"></a>

```json
{
  "country": {
    "label": "string",
    "value": "string"
  },
  "state": {
    "label": "string",
    "value": "string"
  },
  "zip": "string",
  "city": "string",
  "primary": true
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|country|[LabelValueMap](#schemalabelvaluemap)|false|none|none|
|state|[LabelValueMap](#schemalabelvaluemap)|false|none|none|
|zip|string|false|none|none|
|city|string|false|none|none|
|primary|boolean|false|none|none|

<h2 id="tocS_LabelValueMap">LabelValueMap</h2>
<!-- backwards compatibility -->
<a id="schemalabelvaluemap"></a>
<a id="schema_LabelValueMap"></a>
<a id="tocSlabelvaluemap"></a>
<a id="tocslabelvaluemap"></a>

```json
{
  "label": "string",
  "value": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|label|string|false|none|none|
|value|string|false|none|none|

<h2 id="tocS_UpdateUserRequestDto">UpdateUserRequestDto</h2>
<!-- backwards compatibility -->
<a id="schemaupdateuserrequestdto"></a>
<a id="schema_UpdateUserRequestDto"></a>
<a id="tocSupdateuserrequestdto"></a>
<a id="tocsupdateuserrequestdto"></a>

```json
{
  "verificationStatus": "failed",
  "email": "string",
  "phoneNumber": "string",
  "name": "string",
  "dob": "string",
  "nationality": "string",
  "emergencyContact": "string",
  "additionalEmail": "string",
  "addresses": [
    {
      "country": {
        "label": "string",
        "value": "string"
      },
      "state": {
        "label": "string",
        "value": "string"
      },
      "zip": "string",
      "city": "string",
      "primary": true
    }
  ],
  "occupation": "string",
  "preferredModOfContact": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|verificationStatus|string|false|none|none|
|email|string|false|none|none|
|phoneNumber|string|false|none|none|
|name|string|false|none|none|
|dob|string|false|none|none|
|nationality|string|false|none|none|
|emergencyContact|string|false|none|none|
|additionalEmail|string|false|none|none|
|addresses|[[Address](#schemaaddress)]|false|none|none|
|occupation|string|false|none|none|
|preferredModOfContact|string|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|verificationStatus|failed|
|verificationStatus|pending|
|verificationStatus|verified|

<h2 id="tocS_CreateUpdateVisitingScheduleRequestDto">CreateUpdateVisitingScheduleRequestDto</h2>
<!-- backwards compatibility -->
<a id="schemacreateupdatevisitingschedulerequestdto"></a>
<a id="schema_CreateUpdateVisitingScheduleRequestDto"></a>
<a id="tocScreateupdatevisitingschedulerequestdto"></a>
<a id="tocscreateupdatevisitingschedulerequestdto"></a>

```json
{
  "days": [
    {
      "dayOfWeek": "MONDAY",
      "timeSlots": [
        {
          "start": {
            "dayPeriod": "AM",
            "hour": 0,
            "minute": 0
          },
          "end": {
            "dayPeriod": "AM",
            "hour": 0,
            "minute": 0
          }
        }
      ]
    }
  ],
  "timeZone": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|days|[[Day](#schemaday)]|false|none|none|
|timeZone|string|false|none|none|

<h2 id="tocS_Day">Day</h2>
<!-- backwards compatibility -->
<a id="schemaday"></a>
<a id="schema_Day"></a>
<a id="tocSday"></a>
<a id="tocsday"></a>

```json
{
  "dayOfWeek": "MONDAY",
  "timeSlots": [
    {
      "start": {
        "dayPeriod": "AM",
        "hour": 0,
        "minute": 0
      },
      "end": {
        "dayPeriod": "AM",
        "hour": 0,
        "minute": 0
      }
    }
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|dayOfWeek|string|false|none|none|
|timeSlots|[[TimeSlot](#schematimeslot)]|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|dayOfWeek|MONDAY|
|dayOfWeek|TUESDAY|
|dayOfWeek|WEDNESDAY|
|dayOfWeek|THURSDAY|
|dayOfWeek|FRIDAY|
|dayOfWeek|SATURDAY|
|dayOfWeek|SUNDAY|

<h2 id="tocS_Time">Time</h2>
<!-- backwards compatibility -->
<a id="schematime"></a>
<a id="schema_Time"></a>
<a id="tocStime"></a>
<a id="tocstime"></a>

```json
{
  "dayPeriod": "AM",
  "hour": 0,
  "minute": 0
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|dayPeriod|string|false|none|none|
|hour|integer(int32)|false|none|none|
|minute|integer(int32)|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|dayPeriod|AM|
|dayPeriod|PM|

<h2 id="tocS_TimeSlot">TimeSlot</h2>
<!-- backwards compatibility -->
<a id="schematimeslot"></a>
<a id="schema_TimeSlot"></a>
<a id="tocStimeslot"></a>
<a id="tocstimeslot"></a>

```json
{
  "start": {
    "dayPeriod": "AM",
    "hour": 0,
    "minute": 0
  },
  "end": {
    "dayPeriod": "AM",
    "hour": 0,
    "minute": 0
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|start|[Time](#schematime)|false|none|none|
|end|[Time](#schematime)|false|none|none|

<h2 id="tocS_AddUpdateRentalUnitRequestDto">AddUpdateRentalUnitRequestDto</h2>
<!-- backwards compatibility -->
<a id="schemaaddupdaterentalunitrequestdto"></a>
<a id="schema_AddUpdateRentalUnitRequestDto"></a>
<a id="tocSaddupdaterentalunitrequestdto"></a>
<a id="tocsaddupdaterentalunitrequestdto"></a>

```json
{
  "title": "string",
  "address": {
    "country": {
      "label": "string",
      "value": "string"
    },
    "state": {
      "label": "string",
      "value": "string"
    },
    "zip": "string",
    "city": "string",
    "primary": true
  },
  "rent": {
    "amount": 0,
    "currency": "usd",
    "currencySymbol": "string"
  },
  "deposit": {
    "amount": 0,
    "currency": "usd",
    "currencySymbol": "string"
  },
  "features": {
    "featuresUtilities": {
      "property1": true,
      "property2": true
    },
    "featuresAmenities": {
      "property1": true,
      "property2": true
    },
    "featuresNumbers": {
      "property1": 0.1,
      "property2": 0.1
    }
  },
  "posterImageId": "string",
  "contact": {
    "email": "string",
    "phoneNumber": "string",
    "name": "string",
    "preferredModOfContact": "string"
  },
  "description": "string",
  "leaseTerm": "string",
  "leaseStartDate": 0
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|title|string|false|none|none|
|address|[Address](#schemaaddress)|false|none|none|
|rent|[Amount](#schemaamount)|false|none|none|
|deposit|[Amount](#schemaamount)|false|none|none|
|features|[RentalUnitFeatures](#schemarentalunitfeatures)|false|none|none|
|posterImageId|string|false|none|none|
|contact|[Contact](#schemacontact)|false|none|none|
|description|string|false|none|none|
|leaseTerm|string|false|none|none|
|leaseStartDate|integer(int64)|false|none|none|

<h2 id="tocS_Amount">Amount</h2>
<!-- backwards compatibility -->
<a id="schemaamount"></a>
<a id="schema_Amount"></a>
<a id="tocSamount"></a>
<a id="tocsamount"></a>

```json
{
  "amount": 0,
  "currency": "usd",
  "currencySymbol": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|amount|integer(int64)|false|none|none|
|currency|string|false|none|none|
|currencySymbol|string|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|currency|usd|
|currency|inr|
|currency|cad|
|currency|gbp|

<h2 id="tocS_Contact">Contact</h2>
<!-- backwards compatibility -->
<a id="schemacontact"></a>
<a id="schema_Contact"></a>
<a id="tocScontact"></a>
<a id="tocscontact"></a>

```json
{
  "email": "string",
  "phoneNumber": "string",
  "name": "string",
  "preferredModOfContact": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|email|string|false|none|none|
|phoneNumber|string|false|none|none|
|name|string|false|none|none|
|preferredModOfContact|string|false|none|none|

<h2 id="tocS_RentalUnitFeatures">RentalUnitFeatures</h2>
<!-- backwards compatibility -->
<a id="schemarentalunitfeatures"></a>
<a id="schema_RentalUnitFeatures"></a>
<a id="tocSrentalunitfeatures"></a>
<a id="tocsrentalunitfeatures"></a>

```json
{
  "featuresUtilities": {
    "property1": true,
    "property2": true
  },
  "featuresAmenities": {
    "property1": true,
    "property2": true
  },
  "featuresNumbers": {
    "property1": 0.1,
    "property2": 0.1
  }
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|featuresUtilities|object|false|none|none|
|» **additionalProperties**|boolean|false|none|none|
|featuresAmenities|object|false|none|none|
|» **additionalProperties**|boolean|false|none|none|
|featuresNumbers|object|false|none|none|
|» **additionalProperties**|number(double)|false|none|none|

<h2 id="tocS_ApiResponseObject">ApiResponseObject</h2>
<!-- backwards compatibility -->
<a id="schemaapiresponseobject"></a>
<a id="schema_ApiResponseObject"></a>
<a id="tocSapiresponseobject"></a>
<a id="tocsapiresponseobject"></a>

```json
{
  "status": 0,
  "msg": "string",
  "data": {}
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|status|integer(int32)|false|none|none|
|msg|string|false|none|none|
|data|object|false|none|none|

<h2 id="tocS_GetUploadUrlForFileRequestDto">GetUploadUrlForFileRequestDto</h2>
<!-- backwards compatibility -->
<a id="schemagetuploadurlforfilerequestdto"></a>
<a id="schema_GetUploadUrlForFileRequestDto"></a>
<a id="tocSgetuploadurlforfilerequestdto"></a>
<a id="tocsgetuploadurlforfilerequestdto"></a>

```json
{
  "contentType": "string",
  "type": "image",
  "filePurpose": "rental_unit_image",
  "rentalUnitId": "string",
  "fileName": "string",
  "rentalUnitElement": "living_room",
  "applicationId": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|contentType|string|false|none|none|
|type|string|false|none|none|
|filePurpose|string|false|none|none|
|rentalUnitId|string|false|none|none|
|fileName|string|false|none|none|
|rentalUnitElement|string|false|none|none|
|applicationId|string|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|type|image|
|type|video|
|type|document|
|filePurpose|rental_unit_image|
|filePurpose|rental_unit_poster_image|
|filePurpose|bank_statement|
|filePurpose|credit_score_report|
|filePurpose|gic_certificate|
|filePurpose|parents_bank_statement|
|filePurpose|student_id|
|filePurpose|national_id|
|filePurpose|user_profile_image|
|filePurpose|offered_lease_doc|
|filePurpose|signed_lease_doc|
|rentalUnitElement|living_room|
|rentalUnitElement|bed_room|
|rentalUnitElement|dining_room|
|rentalUnitElement|bath_room|
|rentalUnitElement|kitchen|
|rentalUnitElement|others|

<h2 id="tocS_AddOrRemoveStudentsForApplicationRequestDto">AddOrRemoveStudentsForApplicationRequestDto</h2>
<!-- backwards compatibility -->
<a id="schemaaddorremovestudentsforapplicationrequestdto"></a>
<a id="schema_AddOrRemoveStudentsForApplicationRequestDto"></a>
<a id="tocSaddorremovestudentsforapplicationrequestdto"></a>
<a id="tocsaddorremovestudentsforapplicationrequestdto"></a>

```json
{
  "students": [
    "string"
  ]
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|students|[string]|false|none|none|

<h2 id="tocS_SheerIdUniversity">SheerIdUniversity</h2>
<!-- backwards compatibility -->
<a id="schemasheeriduniversity"></a>
<a id="schema_SheerIdUniversity"></a>
<a id="tocSsheeriduniversity"></a>
<a id="tocssheeriduniversity"></a>

```json
{
  "id": 0,
  "idExtended": "string",
  "name": "string",
  "country": "string",
  "type": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int32)|false|none|none|
|idExtended|string|false|none|none|
|name|string|false|none|none|
|country|string|false|none|none|
|type|string|false|none|none|

<h2 id="tocS_SheerIdVerificationRequestDto">SheerIdVerificationRequestDto</h2>
<!-- backwards compatibility -->
<a id="schemasheeridverificationrequestdto"></a>
<a id="schema_SheerIdVerificationRequestDto"></a>
<a id="tocSsheeridverificationrequestdto"></a>
<a id="tocssheeridverificationrequestdto"></a>

```json
{
  "email": "string",
  "birthDate": "string",
  "firstName": "string",
  "lastName": "string",
  "organization": {
    "id": 0,
    "idExtended": "string",
    "name": "string",
    "country": "string",
    "type": "string"
  },
  "phoneNumber": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|email|string|false|none|none|
|birthDate|string|false|none|none|
|firstName|string|false|none|none|
|lastName|string|false|none|none|
|organization|[SheerIdUniversity](#schemasheeriduniversity)|false|none|none|
|phoneNumber|string|false|none|none|

<h2 id="tocS_SheerIdOrgSearchRequestDto">SheerIdOrgSearchRequestDto</h2>
<!-- backwards compatibility -->
<a id="schemasheeridorgsearchrequestdto"></a>
<a id="schema_SheerIdOrgSearchRequestDto"></a>
<a id="tocSsheeridorgsearchrequestdto"></a>
<a id="tocssheeridorgsearchrequestdto"></a>

```json
{
  "searchTerm": "string",
  "country": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|searchTerm|string|false|none|none|
|country|string|false|none|none|

<h2 id="tocS_SearchRentalUnitRequestDto">SearchRentalUnitRequestDto</h2>
<!-- backwards compatibility -->
<a id="schemasearchrentalunitrequestdto"></a>
<a id="schema_SearchRentalUnitRequestDto"></a>
<a id="tocSsearchrentalunitrequestdto"></a>
<a id="tocssearchrentalunitrequestdto"></a>

```json
{
  "features": {
    "featuresUtilities": {
      "property1": true,
      "property2": true
    },
    "featuresAmenities": {
      "property1": true,
      "property2": true
    },
    "featuresNumbers": {
      "property1": 0.1,
      "property2": 0.1
    }
  },
  "country": "string",
  "state": "string",
  "city": "string",
  "minRent": 0,
  "maxRent": 0
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|features|[RentalUnitFeatures](#schemarentalunitfeatures)|false|none|none|
|country|string|false|none|none|
|state|string|false|none|none|
|city|string|false|none|none|
|minRent|integer(int64)|false|none|none|
|maxRent|integer(int64)|false|none|none|

<h2 id="tocS_RegisterRequestDto">RegisterRequestDto</h2>
<!-- backwards compatibility -->
<a id="schemaregisterrequestdto"></a>
<a id="schema_RegisterRequestDto"></a>
<a id="tocSregisterrequestdto"></a>
<a id="tocsregisterrequestdto"></a>

```json
{
  "email": "string",
  "dob": 0,
  "address": "string",
  "phoneNumber": "string",
  "country": "string",
  "gender": "male",
  "emergencyContact": "string",
  "name": "string",
  "role": "string",
  "authToken": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|email|string|false|none|none|
|dob|integer(int64)|false|none|none|
|address|string|false|none|none|
|phoneNumber|string|false|none|none|
|country|string|false|none|none|
|gender|string|false|none|none|
|emergencyContact|string|false|none|none|
|name|string|false|none|none|
|role|string|false|none|none|
|authToken|string|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|gender|male|
|gender|female|
|gender|non_binary|

<h2 id="tocS_RateRentalUnitRequestDto">RateRentalUnitRequestDto</h2>
<!-- backwards compatibility -->
<a id="schemaraterentalunitrequestdto"></a>
<a id="schema_RateRentalUnitRequestDto"></a>
<a id="tocSraterentalunitrequestdto"></a>
<a id="tocsraterentalunitrequestdto"></a>

```json
{
  "review": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|review|string|false|none|none|

<h2 id="tocS_PaymentRequestDto">PaymentRequestDto</h2>
<!-- backwards compatibility -->
<a id="schemapaymentrequestdto"></a>
<a id="schema_PaymentRequestDto"></a>
<a id="tocSpaymentrequestdto"></a>
<a id="tocspaymentrequestdto"></a>

```json
{
  "requestType": "payment_for_listing",
  "entityId": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|requestType|string|false|none|none|
|entityId|string|false|none|none|

#### Enumerated Values

|Property|Value|
|---|---|
|requestType|payment_for_listing|
|requestType|payment_for_deposit|

<h2 id="tocS_LogInRequestDto">LogInRequestDto</h2>
<!-- backwards compatibility -->
<a id="schemaloginrequestdto"></a>
<a id="schema_LogInRequestDto"></a>
<a id="tocSloginrequestdto"></a>
<a id="tocsloginrequestdto"></a>

```json
{
  "authToken": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|authToken|string|false|none|none|

<h2 id="tocS_CreateApplicationRequestDto">CreateApplicationRequestDto</h2>
<!-- backwards compatibility -->
<a id="schemacreateapplicationrequestdto"></a>
<a id="schema_CreateApplicationRequestDto"></a>
<a id="tocScreateapplicationrequestdto"></a>
<a id="tocscreateapplicationrequestdto"></a>

```json
{
  "rentalUnitId": "string",
  "date": "string",
  "timeZone": "string"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|rentalUnitId|string|false|none|none|
|date|string|false|none|none|
|timeZone|string|false|none|none|

