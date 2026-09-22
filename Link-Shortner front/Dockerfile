FROM nginx:1.27-alpine

COPY nginx.conf /etc/nginx/templates/default.conf.template
COPY index.html /usr/share/nginx/html/index.html
COPY style.css /usr/share/nginx/html/style.css
COPY app.js /usr/share/nginx/html/app.js

EXPOSE 80
