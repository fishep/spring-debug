FROM php:7.4-fpm

RUN apt-get update
RUN apt-get install -y git libfreetype6-dev libjpeg62-turbo-dev libpng-dev libzip-dev libbz2-dev libwebp-dev libxml2-dev

RUN docker-php-ext-configure gd --with-jpeg --with-webp --with-freetype

RUN docker-php-ext-install mysqli pdo_mysql zip bz2 bcmath pcntl gd gettext soap
#docker-php-ext-install soap

RUN pecl install redis-5.3.7 xdebug-3.1.6
RUN docker-php-ext-enable redis xdebug
#RUN pecl install xdebug-3.1.6
#RUN docker-php-ext-enable xdebug

#安装compose https://getcomposer.org/download/
RUN php -r "copy('https://getcomposer.org/installer', 'composer-setup.php');"
RUN php -r "if (hash_file('sha384', 'composer-setup.php') === 'dac665fdc30fdd8ec78b38b9800061b4150413ff2e3b6f88543c636f7cd84f6db9189d43a81e5503cda447da73c7e5b6') { echo 'Installer verified'; } else { echo 'Installer corrupt'; unlink('composer-setup.php'); } echo PHP_EOL;"
#RUN php composer-setup.php --install-dir=/usr/local/bin/ --filename=composer1 --version=1.8.6
RUN php composer-setup.php --install-dir=/usr/local/bin/ --filename=composer2 --version=2.8.1
RUN php -r "unlink('composer-setup.php');"

#RUN chmod a+x /usr/local/bin/composer1
RUN chmod a+x /usr/local/bin/composer2

#RUN ln -s /usr/local/bin/composer1 /usr/local/bin/composer
#rm /usr/local/bin/composer
RUN ln -s /usr/local/bin/composer2 /usr/local/bin/composer
RUN composer config -g repo.packagist composer https://mirrors.aliyun.com/composer/

EXPOSE 9000