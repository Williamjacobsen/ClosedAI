# ClosedAI

**ClosedAI** is a proxy-based multi-user AI chatbot interface that lets multiple users interact with a Large Language Model (LLM) (such as OpenAI’s ChatGPT) through a single centralized bot account. In essence, ClosedAI routes all user prompts to an automated browser bot (powered by Selenium) which communicates with the ChatGPT web interface, allowing many users to share one account while minimizing detection risk. It is built as a full-stack application with a React/TypeScript frontend, a Jav...

## Features

- **Multi-User Chat Interface:** Multiple concurrent users with their own chat sessions.  
- **Central Bot Mediation:** Headless browser bot automates ChatGPT.  
- **Real-Time Streaming Responses:** SSE streams AI responses live.  
- **Redis-Powered Queue and Cache:** Pub/Sub messaging with Redis.  
- **Full-Stack Web Application:** React frontend, Spring Boot backend, Python bot service.

## Tech Stack

- **Frontend:** React + TypeScript.  
- **Backend:** Java Spring Boot (in `backend2`).  
- **AI Bot Microservice:** Python 3 + Selenium + undetected-chromedriver + redis.  
- **Database/Cache:** Redis.

## Installation Instructions

**Prerequisites:** Node.js, npm, JDK 17+, Python 3, Redis.

**1. Clone the repository**  
```bash
git clone https://github.com/Williamjacobsen/ClosedAI.git
cd ClosedAI/WebApp
```

**2. Backend (Spring Boot)**  
```bash
cd backend2
./mvnw spring-boot:run
```

**3. Bot service (Python)**  
```bash
pip install -r requirements.txt
python bot_service.py
```

**4. Frontend (React)**  
```bash
cd ../frontend
npm install
npm start
```

Open [http://localhost:3000](http://localhost:3000).

## Folder Structure

- `WebApp/frontend/` – React frontend.  
- `WebApp/backend2/` – Spring Boot backend.  
- `System Design/` – Architecture docs.  
- `TODO.txt` – Planned features.

## Development Scripts

- **Frontend:**  
  - `npm start` – Dev server.  
  - `npm run build` – Production build.  
  - `npm test` – Tests.  

- **Backend:**  
  - `./mvnw spring-boot:run` – Run with Maven.  
  - `./gradlew bootRun` – Run with Gradle.

## Contribution Guide

1. Fork & clone.  
2. Create branch: `git checkout -b feature/my-feature`.  
3. Commit & push.  
4. Open Pull Request.

## License

No explicit license yet. Default copyright applies.

## Contact & Credits

Developed by **William Jacobsen** ([GitHub @Williamjacobsen](https://github.com/Williamjacobsen)).  
Please use Issues in the repo for questions.
