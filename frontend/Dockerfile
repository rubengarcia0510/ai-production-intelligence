# Stage 1: build
FROM node:20-alpine AS build
WORKDIR /app
COPY frontend/package*.json ./
RUN npm ci --silent
COPY frontend/ .
RUN npm run build --silent

# Stage 2: serve with nginx
FROM nginx:stable-alpine
COPY --from=build /app/dist/frontend /usr/share/nginx/html
COPY frontend/nginx/default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["/usr/sbin/nginx", "-g", "daemon off;"]
