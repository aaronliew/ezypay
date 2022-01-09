DROP TABLE IF EXISTS `subscription`;

CREATE TABLE `subscription` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `amount` BIGINT DEFAULT NULL,
    `currency` varchar(255) DEFAULT NULL,
    `subscription_type` varchar(255) DEFAULT NULL,
    `start_date` datetime DEFAULT NULL,
    `end_date` datetime DEFAULT NULL,
    `invoice_date` longtext DEFAULT NULL,
    `record_create_date` datetime DEFAULT NULL,
    `record_update_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
