# AquaMinder API Documentation

## Base URL
The base URL is not specified in the code. It should be configured in your Retrofit builder.

## Authentication

### 1. Register User
**Endpoint:** `POST /register`  
**Request Body:**
```json
{
  "username": "string",
  "mail": "string",
  "password": "string"
}
```
**Response:**
```json
{
  "status": 200
}
```

### 2. Login User
**Endpoint:** `POST /login`  
**Request Body:**
```json
{
  "username": "string",
  "password": "string",
  "token": "string"
}
```
**Response:**
```json
{
  "status": 200,
  "message": "string",
  "username": "string",
  "mail": "string",
  "password": "string"
}
```

## Irrigation Zones

### 3. Get Irrigation Zones
**Endpoint:** `GET /equipos`  
**Request:** No body required  
**Response:**
```json
{
  "status": 200,
  "equipos": [
    {
      "id": "string",
      "name": "string",
      "logoId": 0,
      "latitude": 0.0,
      "longitude": 0.0,
      "address": "string"
    }
  ]
}
```

### 4. Save Irrigation Zone
**Endpoint:** `POST /addEquipo`  
**Request Body:**
```json
{
  "id": "string",
  "name": "string",
  "logoId": 0,
  "latitude": 0.0,
  "longitude": 0.0,
  "address": "string"
}
```
**Response:**
```json
{
  "status": 200
}
```

### 5. Get Irrigation Zone Details
**Endpoint:** `GET /detallesEquipo`  
**Query Parameters:**
- `id`: string (required) - The ID of the irrigation zone

**Response:**
```json
{
  "status": 200,
  "id": "string",
  "name": "string",
  "logo_id": 0,
  "address": "string",
  "valves": [
    {
      "id_valvula": 0,
      "modo_control": "MANUAL|SCHEDULED|SENSOR",
      "humidity_min": 0,
      "humidity_max": 100,
      "weather_enable": true,
      "is_active": true,
      "is_watering": false,
      "schedule": {
        "modo_frecuencia": "INTERVAL_DAYS|SELECTED_DAYS",
        "interval_days": 1,
        "days_of_week": ["MONDAY", "TUESDAY"],
        "water_times": ["08:00", "20:00"],
        "duration": 30
      },
      "last_waters": [
        {
          "date": "2025-12-01",
          "time": "08:30",
          "is_skipped": false
        }
      ],
       "next_waters": [
          {
             "date": "2025-12-01",
             "time": "08:30",
             "is_skipped": false
          }
       ],
      "last_humidity": [0,10,20,30,40,50,60,70,80,90,100]
    }
  ]
}
```

### 6. Save Valves Configuration
**Endpoint:** `POST /guardarConfig`  
**Request Body:**
```json
{
  "id_equipo": "string",
  "valves": [
    {
      "id_valvula": 0,
      "modo_control": "MANUAL|SCHEDULED|SENSOR",
      "humidity_min": 0,
      "humidity_max": 100,
      "weather_enable": true,
      "is_active": true,
      "schedule": {
        "modo_frecuencia": "INTERVAL_DAYS|SELECTED_DAYS",
        "interval_days": 1,
        "days_of_week": ["MONDAY", "TUESDAY"],
        "water_times": ["08:00", "20:00"],
        "duration": 30
      }
    }
  ]
}
```
**Response:**
```json
{
  "status": 200
}
```

### 7. Get Valve Watering Status
**Endpoint:** `GET /wateringStatus`  
**Query Parameters:**
- `id_equipo`: string (required) - The ID of the irrigation zone
- `id_valvula`: integer (required) - The ID of the valve

**Response:**
```json
{
  "status": 200,
  "isWatering": true
}
```

## Weather

### 8. Get Weather
**Endpoint:** `GET /weather`  
**Query Parameters:**
- `id_equipo`: string (required) - The ID of the irrigation zone

**Response:**
```json
{
  "status": 200,
  "icon_code": 0,
  "temperature": 0,
  "description": "string",
  "humidity": 0,
   "rain_prob": 0,
  "address": "string",
  "hourly_weather": [
    {
      "time": "string",
      "temperature": 0,
      "icon_code": 0,
       "humidity": 0,
       "rain_prob": 0
    }
  ]
}
```

## Enums

### ControlMode
- `MANUAL`
- `SCHEDULED`
- `SENSOR`

### FrequencyMode
- `INTERVAL_DAYS` - Water every X days
- `SELECTED_DAYS` - Water on specific days of the week

## Notes:
1. All endpoints return a `status` field in the response. A status of 200 indicates success.
2. Error responses will include a `message` field with details about the error.
3. Time values should be in 24-hour format (e.g., "14:30" for 2:30 PM).
4. Dates should be in "yyyy-MM-dd" format.
5. The API uses standard HTTP status codes:
   - 200: Success
   - 400: Bad Request
   - 401: Unauthorized
   - 404: Not Found
   - 500: Internal Server Error

## Authentication
Most endpoints require authentication. Include the authentication token in the request header:
```
Authorization: Bearer <token>
```
