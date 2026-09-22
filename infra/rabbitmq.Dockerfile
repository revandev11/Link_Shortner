FROM rabbitmq:3-management

COPY rabbitmq-entrypoint.sh /usr/local/bin/rabbitmq-entrypoint.sh
RUN chmod +x /usr/local/bin/rabbitmq-entrypoint.sh

ENTRYPOINT ["/usr/local/bin/rabbitmq-entrypoint.sh"]
CMD ["rabbitmq-server"]
