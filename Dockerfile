FROM node:alpine
WORKDIR /usr/app

COPY build/compileSync/js/main/productionExecutable/kotlin .
COPY build/js/packages/alphabet/package.json .
RUN npm i
LABEL org.opencontainers.image.source = "https://github.com/DRSchlaubi/alphabet"

ENTRYPOINT ["node", "/usr/app/alphabet.js"]
