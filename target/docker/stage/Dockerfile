FROM quay.io/washpost/arc-subs-java:latest-alpine
RUN addgroup play
RUN adduser -s /bin/bash -D -G play play
RUN echo 'play ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers
WORKDIR /opt/docker
ADD opt /opt
RUN chown -R play:play .
RUN chown -R play:play /opt/certs
USER play
ENV ENV_CONF=${DEPLOY_ENV:-prod}
RUN chmod +x /opt/docker/start.sh
ENTRYPOINT ["/bin/bash", "/opt/docker/start.sh"]
EXPOSE 9000
