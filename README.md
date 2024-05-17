# Evaluación : JAVA

En este ejemplo, he utilizado H2 como base de datos en memoria y el resto de servicios web se exponen a través de Swagger-UI.
El proyecto se puede ejecutar de la siguiente manera:

```
 gradle clean
 gradle build
 gradle bootRun
```


- Pre Requisitos
```
 Puedes usar la siguiente shell script para generar una clave secreta de 32 bytes de longitud 
 y codificarla en Base64, keysecretgen.sh . Para ejecutar este script, guárdalo en un archivo, 
 por ejemplo, keysecretgen.sh, dale permisos de ejecución con :
 
 chmod +x keysecretgen.sh, 
 
 y luego ejecútalo con 
 
 ./keysecretgen.sh.

- Dependencias io.jsonwebtoken 0.12.3  
```

Una vez que el proyecto esté en funcionamiento, se puede acceder a DB, Services y Swagger-UI de la siguiente manera:

 - DB
	 - acceder a localhost:8080/signupservice/h2-console con schema jdbc:h2:mem:testdb  conectarse y ver datos de tablas.
 
 - Services

	 - User
		 - GET  localhost:8080/signupservice/user
	 	 - GET  localhost:8080/signupservice/user/{id}
		 - POST localhost:8080/signupservice/user
		 - PUT  localhost:8080/signupservice/user
		 - DELETE localhost:8080/signupservice/user/{id}	

	 - Phones
		 - GET  localhost:8080/signupservice/phones
		 - GET  localhost:8080/signupservice/phones/{id}
         - POST localhost:8080/signupservice/phones
		 - PUT  localhost:8080/signupservice/phones/{id}
		 - DELETE localhost:8080/signupservice/phones/{id} 
		 
 - Swagger-UI
	 - localhost:8080/signupservice/swagger-ui/index.html

- Spring Boot Application

- Seguridad

Esta aplicación está protegida con Spring Security utilizando autenticación básica. El acceso a los endpoints protegidos requiere credenciales de usuario válidas.

- Autenticación Básica

Para acceder a los endpoints mediante herramientas como Postman:

1. Configura el método HTTP y la URL del endpoint.
2. Ve a la pestaña `Authorization`, selecciona `Basic Auth` e introduce el nombre de usuario "admin" y  la contraseña "admin".
3. Postman añadirá automáticamente el encabezado `Authorization` a tu solicitud.
4. Envía la solicitud.

- Credenciales de acceso predeterminadas

Se están utilizando las configuraciones de seguridad predeterminadas de Spring Boot, lo cual significa que se proporciona 
una contraseña autogenerada para el usuario por defecto. Para iniciar sesión, utiliza las siguientes credenciales:
- **Username:** `user`
- **Password:** Revisa el log de la aplicación en la consola en busca de una línea similar a:
```
  Using generated security password: a4fd55fa-2d66-4818-b158-a00c01b50c73
```

- Test Junit

```
gradle test
```
- Ejecutar la Tarea de JaCoCo

Para generar el reporte de cobertura de código, ejecuta la tarea `jacocoTestReport` desde la línea de comandos:

```bash
gradle jacocoTestReport
```
Este comando ejecutará las pruebas unitarias y generará los reportes de cobertura.

- Visualizar el Reporte
Una vez que el reporte ha sido generado, puedes encontrar y visualizar los reportes en los siguientes directorios dentro de tu proyecto:

```bash
HTML: Abre el archivo index.html ubicado en build/reports/jacoco/test/html/index.html con cualquier navegador web para ver una interfaz gráfica detallada de la cobertura de tus pruebas.

XML: El reporte en formato XML está disponible en build/reports/jacoco/test/jacocoTestReport.xml para integraciones con otras herramientas o para análisis más detallado.
```
Estos reportes te proporcionarán información valiosa sobre la cobertura de tus pruebas.

- Test Postman

Descargar archivo postman para pruebas  en:


```
 /resources/postman/signupservice.postman_collection.json
```