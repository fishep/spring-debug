FROM trafex/php-nginx:3.6.0

# Temporary switch to root
USER root

# Install xdebug
RUN apk add --no-cache php83-pecl-xdebug

# Add configuration
COPY xdebug.ini ${PHP_INI_DIR}/conf.d/xdebug.ini

# Install composer from the official image
COPY --from=composer /usr/bin/composer /usr/bin/composer

# Run composer install to install the dependencies
#RUN composer install --optimize-autoloader --no-interaction --no-progress

# Switch back to non-root user
USER nobody