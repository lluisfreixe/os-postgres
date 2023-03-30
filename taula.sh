#!/bin/bash
set -e
  CREATE TABLE [IF NOT EXISTS] persona (
   idpersona INT NOT NULL,
   nombre VARCHAR ( 20 ) NOT NULL,
   cedula VARCHAR ( 20 ) NOT NULL,
   table_constraints
  );
 EOSQL