# InvoiceKit — Backend

> GST-ready Invoicing SaaS for Indian Freelancers & Small Businesses  
> Built with Java 21 · Spring Boot 3 · PostgreSQL · Docker · AWS

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Containerised-2496ED?style=flat-square&logo=docker)](https://www.docker.com/)
[![Deployed on Render](https://img.shields.io/badge/Deployed-Render-46E3B7?style=flat-square&logo=render)](https://invoicekit-backend.onrender.com)

---

## 🔗 Live Demo

| Service | URL |
|---------|-----|
| Frontend | [invoicekit-frontend.vercel.app](https://invoicekit-frontend.vercel.app) |
| Backend API | [invoicekit-backend.onrender.com](https://invoicekit-backend.onrender.com) |
| API Docs (Swagger) | [invoicekit-backend.onrender.com/swagger-ui.html](https://invoicekit-backend.onrender.com/swagger-ui.html) |
| Frontend Repo | [github.com/jayendrakasera/invoicekit-frontend](https://github.com/jayendrakasera/invoicekit-frontend) |

---

## 📌 What is InvoiceKit?

InvoiceKit is a full-stack invoicing SaaS built specifically for the Indian market. Freelancers and small business owners can create GST-compliant invoices, send them to clients via email, auto-generate UPI QR codes for instant payment, and track payment status — all from one dashboard.

**Why I built this:** As a freelance web developer, I needed a free, India-specific invoicing tool. Everything available was either paid or missing GST + UPI support. So I built it.

---

## ✨ Features

### MVP (v1 — Live)
- 🔐 **JWT Authentication** — Register/Login with two roles: `FREELANCER` and `CLIENT`
- 👥 **Client Management** — Add clients with GST number, billing address, contact details
- 🧾 **Invoice Creation** — Line items with GST calculation, INR formatting, auto-generated invoice numbers (INV-2025-001)
- 📄 **PDF Export** — Branded invoice PDFs generated server-side using iText with GST breakup
- 📱 **UPI QR Code** — Auto-generated UPI QR (via ZXing) embedded in every invoice PDF for instant payment
- 📬 **Email Delivery** — One-click send invoice PDF to client via AWS SES
- 🔁 **Invoice Status Tracking** — State flow: `Draft → Sent → Viewed → Paid / Overdue`
- ⏰ **Automated Reminders** — Spring Scheduler auto-emails clients 3 days before and on due date if unpaid
- 📊 **Dashboard** — Revenue charts, paid vs unpaid breakdown, overdue alerts (Chart.js on frontend)

### Roadmap (v2)
- 🌐 Client portal — shareable public invoice link without login
- 🔄 Recurring invoices for retainer clients
- 💰 Expense tracker for net profit calculation
- 🌍 Multi-currency support (USD/EUR with live exchange rates)

### Roadmap (v3 — AI/ML)
- 🤖 Gemini API invoice summary
- 📈 Revenue forecasting via Python Flask microservice (Linear Regression)
- ⚡ Smart reminder timing based on client payment behaviour (ML classification)

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Security | Spring Security + JWT |
| Database | PostgreSQL |
| ORM | Spring Data JPA + Hibernate |
| DB Migrations | Flyway |
| PDF Generation | iText 7 / OpenPDF |
| QR Code | ZXing |
| Email | JavaMail + AWS SES |
| Scheduler | Spring Scheduler |
| Cloud Storage | AWS S3 |
| Deployment | Docker + Render |
| API Docs | Swagger / OpenAPI 3 |

---

## 🗄️ Database Schema

```
users         → id, name, email, password, role (FREELANCER/CLIENT)
clients       → id, name, email, phone, gst_number, address, freelancer_id
invoices      → id, invoice_number, client_id, freelancer_id, status, due_date, total, gst_amount, notes
invoice_items → id, invoice_id, description, quantity, rate, amount
payments      → id, invoice_id, paid_at, amount
```

---

## 🚀 Running Locally

### Prerequisites
- Java 21
- Maven
- PostgreSQL
- Docker (optional)

### Steps

```bash
# Clone the repo
git clone https://github.com/jayendrakasera/invoicekit-backend.git
cd invoicekit-backend/invoicekit

# Configure environment variables
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Fill in your DB credentials, JWT secret, AWS keys

# Run with Maven
./mvnw spring-boot:run
```

### Run with Docker

```bash
docker-compose up --build
```

### Environment Variables

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/invoicekit
SPRING_DATASOURCE_USERNAME=your_db_user
SPRING_DATASOURCE_PASSWORD=your_db_password
JWT_SECRET=your_jwt_secret
AWS_ACCESS_KEY=your_aws_key
AWS_SECRET_KEY=your_aws_secret
AWS_SES_FROM_EMAIL=your_verified_email
AWS_S3_BUCKET=your_bucket_name
```

---

## 📮 API Endpoints (Key)

```
POST   /api/auth/register          → Register new user
POST   /api/auth/login             → Login, returns JWT

GET    /api/clients                → List all clients
POST   /api/clients                → Add new client
PUT    /api/clients/{id}           → Update client
DELETE /api/clients/{id}           → Delete client

POST   /api/invoices               → Create invoice
GET    /api/invoices               → List all invoices
GET    /api/invoices/{id}          → Get invoice detail
PUT    /api/invoices/{id}/status   → Update status (PAID/SENT etc.)
GET    /api/invoices/{id}/pdf      → Download invoice PDF
POST   /api/invoices/{id}/send     → Send invoice email to client

GET    /api/dashboard/summary      → Revenue summary, counts
GET    /actuator/health            → Health check
```

Full API docs available at `/swagger-ui.html` on the live server.

---

## 📁 Project Structure

```
invoicekit/
├── src/main/java/com/invoicekit/
│   ├── auth/           → JWT filter, security config, auth controller
│   ├── client/         → Client entity, repository, service, controller
│   ├── invoice/        → Invoice entity, line items, status enum, service
│   ├── pdf/            → iText PDF generator, ZXing QR builder
│   ├── email/          → AWS SES config, email templates, scheduler
│   ├── dashboard/      → Summary aggregation queries
│   └── config/         → Swagger, CORS, AWS config
├── src/main/resources/
│   ├── application.properties
│   └── db/migration/   → Flyway SQL migrations
├── Dockerfile
└── docker-compose.yml
```

---

## 👨‍💻 Author

**Jayendra Kasera**  
B.Tech CSE (AI & ML) — LNCT Bhopal | Graduating 2027  
SIH 2025 Winner · Freelance Web Developer  
[GitHub](https://github.com/jayendrakasera) · [LinkedIn](https://linkedin.com/in/jayendrakasera)

---

## 📄 License

MIT License — feel free to use this project for learning or reference.
