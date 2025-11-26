 ApuestaTodoOk - Sistema de Apuestas Deportivas

Sistema web de apuestas deportivas desarrollado en Java con JPA/Hibernate, Servlets y JSP.

  Tecnologías Utilizadas

Backend
-Java: OpenJDK 17.0.16+8 (Eclipse Temurin)
-Servlets: Jakarta EE 10
-JPA/Hibernate: 6.3.1.Final
-Build Tool: Maven

Frontend  
- JSP (JavaServer Pages)
- JSTL (Jakarta Standard Tag Library)
- Bootstrap 5.x
- CSS3 y HTML5

Base de Datos
- MySQL: 10.4.32-MariaDB
- JPA/Hibernate para ORM

Servidor y Herramientas
- Apache Tomcat: 10.x
- Apache NetBeans: 26
- XAMPP: 3.3.0

Prerrequisitos

- Java JDK 17 o superior
- Apache Tomcat 10.x
- MySQL 10.4.x o MariaDB equivalente
- Maven 3.6+

Configuración de Base de Datos

1. Ejecutar XAMPP y iniciar MySQL
2. Abrir phpMyAdmin (http://localhost/phpmyadmin)
3. Crear base de datos `apuesta` (collation: utf8_general_ci)
4. Ir a Importar → Seleccionar `BaseDatos/apuesta.sql`
5. Click Importar

 Configuración en `persistence.xml`:

<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/apuesta"/>
<property name="jakarta.persistence.jdbc.user" value="root"/>
<property name="jakarta.persistence.jdbc.password" value=""/>