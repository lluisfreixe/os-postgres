#!/bin/bash
set -e
 psql -v ON_ERROR_STOP=1 --username "postgres" --dbname "postgres" <<-EOSQL
  CREATE TABLE [IF NOT EXISTS] persona (
   idpersona INT NOT NULL,
   nombre VARCHAR ( 20 ) NOT NULL,
   cedula VARCHAR ( 20 ) NOT NULL,
   table_constraints
  );
 EOSQL