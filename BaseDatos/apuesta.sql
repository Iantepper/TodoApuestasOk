-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 09-11-2023 a las 23:08:29
-- Versión del servidor: 10.4.28-MariaDB
-- Versión de PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `apuesta`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `apuesta`
--

CREATE TABLE `apuesta` (
  `id_apuesta` int(11) NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `por_quien` varchar(255) DEFAULT NULL,
  `fk_id_resultado` int(11) DEFAULT NULL,
  `fk_id_usuario` int(11) DEFAULT NULL,
  `fk_id_partido` int(11) DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'N'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `apuesta`
--

INSERT INTO `apuesta` (`id_apuesta`, `monto`, `por_quien`, `fk_id_resultado`, `fk_id_usuario`, `fk_id_partido`, `estado`) VALUES
(61, 1.00, 'local', 1, 22, 1, 'N'),
(62, 1.00, 'local', 1, 22, 1, 'N'),
(63, 50.00, 'local', 1, 22, 1, 'G'),
(64, 50.00, 'local', 1, 22, 1, 'G'),
(65, 100.00, 'local', 1, 22, 1, 'G'),
(66, 50.00, 'local', 1, 22, 1, 'G'),
(67, 100.00, 'local', 1, 22, 1, 'G'),
(68, 100.00, 'local', 1, 22, 1, 'G'),
(69, 100.00, 'local', 1, 22, 1, 'G'),
(70, 50.00, 'local', 1, 22, 1, 'G'),
(71, 100.00, 'local', 1, 22, 1, 'N'),
(72, 100.00, 'local', 1, 22, 1, 'N'),
(73, 100.00, 'local', 1, 22, 1, 'N'),
(74, 100.00, 'local', 1, 22, 1, 'N'),
(75, 400.00, 'local', 1, 22, 1, 'N'),
(76, 200.00, 'local', 1, 22, 1, 'N'),
(77, 150.00, 'local', 1, 22, 1, 'N'),
(78, 1000.00, 'local', 1, 22, 1, 'N'),
(79, 1000.00, 'local', 1, 22, 1, 'N'),
(80, 1000.00, 'visitante', 1, 22, 1, 'N');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partido`
--

CREATE TABLE `partido` (
  `id_partido` int(11) NOT NULL,
  `local` varchar(255) NOT NULL,
  `visitante` varchar(255) NOT NULL,
  `fecha` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `partido`
--

INSERT INTO `partido` (`id_partido`, `local`, `visitante`, `fecha`) VALUES
(1, 'Real Madrid', 'FC Barcelona', '2023-11-01'),
(2, 'Manchester City', 'Liverpool FC', '2023-11-02'),
(3, 'Bayern Munich', 'Borussia Dortmund', '2023-11-03'),
(4, 'Paris Saint-Germain', 'AC Milan', '2023-11-04'),
(5, 'Juventus', 'Inter Milan', '2023-11-05'),
(6, 'Chelsea FC', 'Arsenal FC', '2023-11-06'),
(7, 'Atletico Madrid', 'Sevilla FC', '2023-11-07'),
(8, 'Borussia Monchengladbach', 'Hertha Berlin', '2023-11-08'),
(9, 'Ajax Amsterdam', 'PSV Eindhoven', '2023-11-09'),
(10, 'AS Roma', 'SS Lazio', '2023-11-10'),
(11, 'FC Porto', 'SL Benfica', '2023-11-11'),
(12, 'Tottenham Hotspur', 'Manchester United', '2023-11-12'),
(13, 'ACF Fiorentina', 'Napoli', '2023-11-13'),
(14, 'Everton FC', 'Leeds United', '2023-11-14'),
(15, 'Ajax Cape Town', 'Kaizer Chiefs', '2023-11-15');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `persona`
--

CREATE TABLE `persona` (
  `id_persona` int(11) NOT NULL,
  `dni` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `apellido` varchar(255) NOT NULL,
  `edad` int(11) DEFAULT NULL,
  `fk_id_usuario` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `persona`
--

INSERT INTO `persona` (`id_persona`, `dni`, `nombre`, `apellido`, `edad`, `fk_id_usuario`) VALUES
(1, '42382349', 'Nombre1', 'Apellido1', NULL, NULL),
(2, '25560981', 'Nombre2', 'Apellido2', NULL, NULL),
(3, '19632400', 'Nombre3', 'Apellido3', NULL, NULL),
(4, '33254417', 'Nombre4', 'Apellido4', NULL, NULL),
(5, '28385463', 'Nombre5', 'Apellido5', NULL, NULL),
(6, '37334279', 'Nombre6', 'Apellido6', NULL, NULL),
(7, '32529631', 'Nombre7', 'Apellido7', NULL, NULL),
(8, '35995411', 'Nombre8', 'Apellido8', NULL, NULL),
(9, '37579419', 'Nombre9', 'Apellido9', NULL, NULL),
(10, '25469541', 'Nombre10', 'Apellido10', NULL, NULL),
(11, '31799881', 'Nombre11', 'Apellido11', NULL, NULL),
(12, '41034616', 'Nombre12', 'Apellido12', NULL, NULL),
(13, '40482394', 'Nombre13', 'Apellido13', NULL, NULL),
(14, '35720751', 'Nombre14', 'Apellido14', NULL, NULL),
(15, '26382240', 'Nombre15', 'Apellido15', NULL, NULL),
(16, '2344', 'ian', 'tepper', 35, 21),
(17, '43314', 'qwe', 'qwe', 34, 22);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resultado`
--

CREATE TABLE `resultado` (
  `id_resultado` int(11) NOT NULL,
  `ganador` varchar(255) DEFAULT NULL,
  `fk_id_partido` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `resultado`
--

INSERT INTO `resultado` (`id_resultado`, `ganador`, `fk_id_partido`) VALUES
(1, 'local', 1),
(2, 'local', 2),
(3, 'local', 3),
(4, 'local', 4),
(5, 'local', 5),
(6, 'local', 6),
(7, 'local', 7),
(8, 'local', 8),
(9, 'local', 9),
(10, 'local', 10),
(11, 'local', 11),
(12, 'local', 12),
(13, 'local', 13),
(14, 'local', 14),
(15, 'local', 15);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` int(11) NOT NULL,
  `usuario` varchar(255) NOT NULL,
  `contrasenia` varchar(255) NOT NULL,
  `dinero` decimal(10,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `usuario`, `contrasenia`, `dinero`) VALUES
(1, 'Usuario1', 'Contraseña1', 100.00),
(2, 'Usuario2', 'Contraseña2', 0.00),
(3, 'Usuario3', 'Contraseña3', 0.00),
(4, 'Usuario4', 'Contraseña4', 0.00),
(5, 'Usuario5', 'Contraseña5', 0.00),
(6, 'Usuario6', 'Contraseña6', 0.00),
(7, 'Usuario7', 'Contraseña7', 0.00),
(8, 'Usuario8', 'Contraseña8', 0.00),
(9, 'Usuario9', 'Contraseña9', 0.00),
(10, 'Usuario10', 'Contraseña10', 0.00),
(11, 'Usuario11', 'Contraseña11', 0.00),
(12, 'Usuario12', 'Contraseña12', 0.00),
(13, 'Usuario13', 'Contraseña13', 0.00),
(14, 'Usuario14', 'Contraseña14', 0.00),
(15, 'Usuario15', 'Contraseña15', 0.00),
(21, 'ianalan', 'contraseña', 0.00),
(22, '1', '1', 3000.00);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `apuesta`
--
ALTER TABLE `apuesta`
  ADD PRIMARY KEY (`id_apuesta`),
  ADD KEY `fk_id_resultado` (`fk_id_resultado`),
  ADD KEY `fk_id_usuario` (`fk_id_usuario`),
  ADD KEY `fk_id_partido` (`fk_id_partido`);

--
-- Indices de la tabla `partido`
--
ALTER TABLE `partido`
  ADD PRIMARY KEY (`id_partido`);

--
-- Indices de la tabla `persona`
--
ALTER TABLE `persona`
  ADD PRIMARY KEY (`id_persona`),
  ADD KEY `fk_id_usuario` (`fk_id_usuario`);

--
-- Indices de la tabla `resultado`
--
ALTER TABLE `resultado`
  ADD PRIMARY KEY (`id_resultado`),
  ADD KEY `fk_id_partido` (`fk_id_partido`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id_usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `apuesta`
--
ALTER TABLE `apuesta`
  MODIFY `id_apuesta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=81;

--
-- AUTO_INCREMENT de la tabla `partido`
--
ALTER TABLE `partido`
  MODIFY `id_partido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `persona`
--
ALTER TABLE `persona`
  MODIFY `id_persona` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `resultado`
--
ALTER TABLE `resultado`
  MODIFY `id_resultado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `apuesta`
--
ALTER TABLE `apuesta`
  ADD CONSTRAINT `apuesta_ibfk_3` FOREIGN KEY (`fk_id_resultado`) REFERENCES `resultado` (`id_resultado`),
  ADD CONSTRAINT `apuesta_ibfk_4` FOREIGN KEY (`fk_id_usuario`) REFERENCES `usuario` (`id_usuario`),
  ADD CONSTRAINT `apuesta_ibfk_5` FOREIGN KEY (`fk_id_partido`) REFERENCES `partido` (`id_partido`),
  ADD CONSTRAINT `fk_apuesta_resultado` FOREIGN KEY (`fk_id_resultado`) REFERENCES `resultado` (`id_resultado`);

--
-- Filtros para la tabla `persona`
--
ALTER TABLE `persona`
  ADD CONSTRAINT `persona_ibfk_1` FOREIGN KEY (`fk_id_usuario`) REFERENCES `usuario` (`id_usuario`);

--
-- Filtros para la tabla `resultado`
--
ALTER TABLE `resultado`
  ADD CONSTRAINT `resultado_ibfk_1` FOREIGN KEY (`fk_id_partido`) REFERENCES `partido` (`id_partido`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
