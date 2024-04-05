#!/bin/bash

# Generar una clave secreta de 32 bytes de longitud
secretKeyBytes=$(openssl rand -base64 32)

# Imprimir la clave secreta codificada en Base64
echo "SECRET_KEY: $secretKeyBytes"