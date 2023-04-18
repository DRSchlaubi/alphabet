FROM gradle:jdk19 as builder

WORKDIR /usr/app

COPY . .
RUN ./gradlew build --no-daemon

FROM node:latest
WORKDIR /usr/app

COPY --from=builder /usr/app/build/compileSync/js/main/productionExecutable/kotlin .
COPY --from=builder /usr/app/build/js/packages/alphabet/package.json .
RUN npm i
LABEL org.opencontainers.image.source = "https://github.com/DRSchlaubi/alphabet"

ENTRYPOINT ["node", "/usr/app/alphabet.js"]
