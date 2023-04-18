FROM gradle:jdk19 as builder

WORKDIR /usr/app

COPY . .
RUN ./gradlew build

FROM node:latest
WORKDIR /usr/app

COPY --from=builder /usr/app/build/compileSync/js/main/productionExecutable/kotlin .
LABEL org.opencontainers.image.source = "https://github.com/DRSchlaubi/alphabet"

ENTRYPOINT ["node", "/usr/app/build/compileSync/js/main/productionExecutable/kotlin/alphabet.js"]
