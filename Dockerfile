FROM gradle:7.4.0-jdk17

WORKDIR /todobot

COPY ./ .

RUN gradle stage

CMD ./build/install/todobot/bin/todobot