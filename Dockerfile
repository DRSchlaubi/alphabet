FROM ghcr.io/kordlib/docker:main

WORKDIR /usr/app
ARG TARGETARCH

COPY out/alphabet-${TARGETARCH}.kexe /usr/app/bot

LABEL org.opencontainers.image.source = "https://github.com/DRSchlaubi/alphabet"

ENTRYPOINT ["/usr/app/bot"]
