
CREATE TABLE IF NOT EXISTS tb_user(
   id INT AUTO_INCREMENT PRIMARY KEY ,
   username VARCHAR(10) NOT NULL ,
   password VARCHAR(255) NOT NULL,
   roles VARCHAR(25) NOT NULL
);

CREATE TABLE IF NOT EXISTS exchange (
   id INT AUTO_INCREMENT PRIMARY KEY ,
   type_currency VARCHAR(20) NOT NULL ,
   unit numeric(10,2) not null ,
   value_soles numeric(10,3) NOT NULL
);

CREATE TABLE IF NOT EXISTS log_exchange (
   id INT AUTO_INCREMENT PRIMARY KEY ,
   id_exchange_money int NOT NULL ,
   value_exchange_money numeric(10,2) not null ,
    id_exchange_quote int NOT NULL ,
   value_exchange_quote numeric(10,2) not null ,
    unit numeric(10,2) not null ,
   total_value numeric(10,3) NOT NULL,
   user_changed varchar(50) NOT NULL
);