# AI Production Intelligence — Architecture

## Purpose

AI Production Intelligence is an agentic production-intelligence platform for audiovisual production.

It combines production events and operational data with ClickHouse analytics and an AI reasoning layer to recommend actions when production conditions change.

## Current foundation

The repository is split into two applications:

- `frontend/`: Angular 20 web application.
- `backend/`: Spring Boot 3 / Java 21 REST API.

## Target architecture

Angular 20 is the frontend application.

Spring Boot 3 with Java 21 is the backend API.

ClickHouse is the analytical data store.

Gemini is the AI reasoning layer.

The main flow is:

Angular -> Spring Boot -> Production Event Service -> ClickHouse

The analytics and agent layer consumes structured production data and produces recommendations for the producer.

## Responsibilities

### Angular

Provides the producer-facing dashboard and interaction layer.

It sends production-event requests to the backend and displays analytics and AI recommendations.

### Spring Boot

Acts as the application boundary.

It validates requests, exposes REST endpoints, coordinates production-event persistence and analytics, and isolates the frontend from infrastructure details.

### ClickHouse

Stores production events and analytical data.

It is intended for fast aggregation and querying across scenes, actors, locations, equipment, weather, costs, delays, and related production signals.

### AI / Agent layer

Consumes structured production context and analytical results.

It reasons about constraints and alternatives and produces actionable recommendations.

The AI layer is not the source of truth for raw production facts. Those facts come from application data and ClickHouse queries.

## Initial vertical slice

The first implementation path is deliberately narrow.

Production Event -> Angular -> POST /events -> Spring Boot -> ClickHouse -> GET /events -> Angular

This establishes a working data path before introducing agentic reasoning.

## Key technical decisions

1. Java 21 + Spring Boot 3 for the backend foundation.
2. Angular 20 for the web frontend.
3. ClickHouse is the primary analytical store and selected hackathon partner technology.
4. REST/JSON is the initial frontend/backend integration boundary.
5. Environment variables are used for infrastructure configuration. Credentials must not be committed.
6. Deterministic data and analytics remain separate from AI reasoning.
7. Agentic functionality is introduced after the production-event data path is operational.

## Risks and mitigations

- ClickHouse connectivity risk: isolate connection configuration and add a deterministic health check.
- AI hallucination risk: provide structured query results and constrain recommendations to observed production context.
- Event model risk: define a minimal production-event schema first.
- Frontend/backend contract drift: keep the initial REST contract small and validate requests with backend tests.
- Demo reliability risk: use reproducible seeded production data.

## Definition of the foundation

The foundation is ready when the repository builds, the frontend/backend boundaries are clear, ClickHouse connectivity can be validated locally, and the first production-event vertical slice can be implemented without changing the overall architecture.
