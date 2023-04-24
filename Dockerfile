FROM gradle:jdk17 as compile

COPY . /src
WORKDIR /src
RUN gradle distTar

FROM openjdk:17-alpine
WORKDIR /
COPY --from=compile /src/build/distributions/kemono-dl-*.tar kemono-dl.tar
RUN mkdir /kemono-dl && \
    tar xvf kemono-dl.tar -C /kemono-dl --strip-components 1 && \
    chmod +x /kemono-dl/bin/kemono-dl && \
    rm -rf kemono-dl.tar && \
    apk add findutils --no-cache
WORKDIR /app
VOLUME /app
ENTRYPOINT [ "/kemono-dl/bin/kemono-dl" ]
