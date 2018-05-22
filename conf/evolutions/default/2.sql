
INSERT INTO `holding` (`id`, `robot_id`, `exchange`, `symbol`, `qty`, `price`, `price_btc`, `price_eth`, `price_usdt`, `amount_btc`, `amount_eth`, `amount_usdt`, `amount`, `price_actual`, `amount_actual`, `created_at`, `updated_at`) VALUES
(1, 1, 'bitfinex', 'BTC', 1.00000000001, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, '2018-03-30 00:00:00.000000', '2018-03-29 00:00:00.000000'),
(2, 1, 'bitfinex', 'ETH', 1.00000000001, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, '2018-03-30 00:00:00.000000', '2018-03-29 00:00:00.000000'),
(3, 1, 'bitfinex', 'BTC', 1.00000000001, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, '2018-03-30 00:00:00.000000', '2018-03-29 00:00:00.000000'),
(4, 1, 'bitfinex', 'BTC', 1.00000000001, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, '2018-03-30 00:00:00.000000', '2018-03-29 00:00:00.000000'),
(5, 1, 'bitfinex', 'BTC', 1.00000000001, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, '2018-03-30 00:00:00.000000', '2018-03-29 00:00:00.000000'),
(6, 1, 'bitfinex', 'BTC', 1.00000000001, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, '2018-03-30 00:00:00.000000', '2018-03-29 00:00:00.000000'),
(7, 1, 'bitfinex', 'BTC', 1.00000000001, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, '2018-03-30 00:00:00.000000', '2018-03-29 00:00:00.000000'),
(8, 1, 'bitfinex', 'BTC', 1.00000000001, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, '2018-03-30 00:00:00.000000', '2018-03-29 00:00:00.000000'),
(9, 1, 'bitfinex', 'BTC', 1.00000000001, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, '2018-03-30 00:00:00.000000', '2018-03-29 00:00:00.000000'),
(10, 1, 'bitfinex', 'BTC', 1.00000000001, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, '2018-03-30 00:00:00.000000', '2018-03-29 00:00:00.000000');

INSERT INTO `crypto_currencies` (`id`, `name`, `color`) VALUES (NULL, 'BTC', 'red');
INSERT INTO `crypto_currencies` (`id`, `name`, `color`) VALUES (NULL, 'ETH', 'green');
INSERT INTO `crypto_currencies` (`id`, `name`, `color`) VALUES (NULL, 'ETC', 'blue');
INSERT INTO `crypto_currencies` (`id`, `name`, `color`) VALUES (NULL, 'BCH', 'black');
INSERT INTO `crypto_currencies` (`id`, `name`, `color`) VALUES (NULL, 'LTC', 'yellow');

INSERT INTO `exchanges` (`id`, `name`, `code`, `country`, `color`) VALUES (NULL, 'Bitfinex', NULL, NULL, NULL);
INSERT INTO `exchanges` (`id`, `name`, `code`, `country`, `color`) VALUES (NULL, 'Binance', NULL, NULL, NULL);
INSERT INTO `exchanges` (`id`, `name`, `code`, `country`, `color`) VALUES (NULL, 'Poloniex', NULL, NULL, NULL);
INSERT INTO `exchanges` (`id`, `name`, `code`, `country`, `color`) VALUES (NULL, 'Huobi', NULL, NULL, NULL);
INSERT INTO `exchanges` (`id`, `name`, `code`, `country`, `color`) VALUES (NULL, 'Hitbtc', NULL, NULL, NULL);

INSERT INTO `robot` (`id`, `uid`, `name`, `created_at`, `updated_at`) VALUES
(1, '8888', 'robotOne', '2018-04-02 00:00:00.000000', '2018-04-02 00:00:00.000000'),
(2, '999', 'robotTwo', '2018-04-02 00:00:00.000000', '2018-04-02 00:00:00.000000');