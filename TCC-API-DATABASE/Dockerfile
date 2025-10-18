FROM node:24-alpine

WORKDIR /app

COPY ./package.json ./
RUN npm i

COPY . .
COPY .env .env

EXPOSE 3000

CMD [ "npm", "run", "dev" ]
