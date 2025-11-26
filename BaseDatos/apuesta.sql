-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 26-11-2025 a las 20:14:01
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

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
  `monto` int(11) NOT NULL,
  `por_quien` varchar(50) NOT NULL,
  `estado` char(1) DEFAULT 'A',
  `fk_id_usuario` int(11) NOT NULL,
  `fk_id_partido` int(11) NOT NULL,
  `fk_id_resultado` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `apuesta`
--

INSERT INTO `apuesta` (`id_apuesta`, `monto`, `por_quien`, `estado`, `fk_id_usuario`, `fk_id_partido`, `fk_id_resultado`) VALUES
(1, 100, 'Boca Juniors', 'G', 2, 1, 1),
(2, 50, 'River Plate', 'P', 3, 1, 1),
(3, 200, 'Barcelona', 'G', 2, 2, 2),
(4, 75, 'Real Madrid', 'P', 4, 2, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partido`
--

CREATE TABLE `partido` (
  `id_partido` int(11) NOT NULL,
  `local` varchar(50) NOT NULL,
  `visitante` varchar(50) NOT NULL,
  `fecha` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `partido`
--

INSERT INTO `partido` (`id_partido`, `local`, `visitante`, `fecha`) VALUES
(1, 'Boca Juniors', 'River Plate', '2024-12-30 20:00:00'),
(2, 'Real Madrid', 'Barcelona', '2024-12-31 16:00:00'),
(3, 'Argentina', 'Brasil', '2025-01-15 21:00:00'),
(4, 'Manchester United', 'Liverpool', '2025-01-20 15:00:00'),
(5, 'PSG', 'Bayern Munich', '2025-01-25 18:00:00'),
(6, 'Inter Miami', 'New York City', '2025-01-30 19:00:00'),
(7, 'Ajax', 'Feyenoord', '2025-02-05 20:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resultado`
--

CREATE TABLE `resultado` (
  `id_resultado` int(11) NOT NULL,
  `ganador` varchar(50) NOT NULL,
  `fk_id_partido` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `resultado`
--

INSERT INTO `resultado` (`id_resultado`, `ganador`, `fk_id_partido`) VALUES
(1, 'Boca Juniors', 1),
(2, 'Barcelona', 2),
(3, 'pendiente', 3),
(4, 'pendiente', 4),
(5, 'pendiente', 5),
(6, 'pendiente', 6),
(7, 'pendiente', 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` int(11) NOT NULL,
  `usuario` varchar(50) NOT NULL,
  `contrasenia` varchar(100) NOT NULL,
  `tipo` varchar(10) NOT NULL DEFAULT 'user',
  `dinero` decimal(10,2) DEFAULT 0.00,
  `dni` varchar(20) DEFAULT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `apellido` varchar(50) DEFAULT NULL,
  `edad` int(11) DEFAULT NULL
) ;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `usuario`, `contrasenia`, `tipo`, `dinero`, `dni`, `nombre`, `apellido`, `edad`) VALUES
(1, 'admin', 'admin', 'admin', NULL, NULL, NULL, NULL, NULL),
(2, 'juan123', 'password123', 'user', 1000.00, '30123456', 'Juan', 'Pérez', 25),
(3, 'maria88', 'password123', 'user', 750.50, '32987654', 'María', 'Gómez', 30),
(4, 'carlos22', 'password123', 'user', 1500.00, '35123456', 'Carlos', 'López', 28);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `apuesta`
--
ALTER TABLE `apuesta`
  ADD PRIMARY KEY (`id_apuesta`),
  ADD KEY `fk_id_usuario` (`fk_id_usuario`),
  ADD KEY `fk_id_partido` (`fk_id_partido`),
  ADD KEY `fk_id_resultado` (`fk_id_resultado`);

--
-- Indices de la tabla `partido`
--
ALTER TABLE `partido`
  ADD PRIMARY KEY (`id_partido`);

--
-- Indices de la tabla `resultado`
--
ALTER TABLE `resultado`
  ADD PRIMARY KEY (`id_resultado`),
  ADD UNIQUE KEY `fk_id_partido` (`fk_id_partido`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `usuario` (`usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `apuesta`
--
ALTER TABLE `apuesta`
  MODIFY `id_apuesta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `partido`
--
ALTER TABLE `partido`
  MODIFY `id_partido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `resultado`
--
ALTER TABLE `resultado`
  MODIFY `id_resultado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `apuesta`
--
ALTER TABLE `apuesta`
  ADD CONSTRAINT `apuesta_ibfk_1` FOREIGN KEY (`fk_id_usuario`) REFERENCES `usuario` (`id_usuario`),
  ADD CONSTRAINT `apuesta_ibfk_2` FOREIGN KEY (`fk_id_partido`) REFERENCES `partido` (`id_partido`),
  ADD CONSTRAINT `apuesta_ibfk_3` FOREIGN KEY (`fk_id_resultado`) REFERENCES `resultado` (`id_resultado`);

--
-- Filtros para la tabla `resultado`
--
ALTER TABLE `resultado`
  ADD CONSTRAINT `resultado_ibfk_1` FOREIGN KEY (`fk_id_partido`) REFERENCES `partido` (`id_partido`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
