FROM ghcr.io/kordlib/docker:main

WORKDIR /usr/app
COPY build/bin/linuxX64/releaseExecutable/alphabet.kexe /usr/app/bot

LABEL org.opencontainers.image.source = "https://github.com/DRSchlaubi/alphabet"

ENTRYPOINT ["/usr/app/bot"]
