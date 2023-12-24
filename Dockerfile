FROM oven/bun:alpine
WORKDIR /usr/app

COPY build/compileSync/js/main/productionExecutable/kotlin .
COPY build/js/packages/alphabet/package.json .
RUN bun i
LABEL org.opencontainers.image.source = "https://github.com/DRSchlaubi/alphabet"

ENTRYPOINT ["bun", "/usr/app/alphabet.js"]
