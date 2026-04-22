-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 03-02-2026 a las 11:03:46
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
-- Base de datos: `proyecto_pokemon`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `casino`
--

CREATE TABLE `casino` (
  `ID_ENTRENADOR` int(3) NOT NULL,
  `ID_PARTIDA` int(3) NOT NULL,
  `TIPO_JUEGO` enum('TRAGAPERRAS','RULETA','CARTAS') NOT NULL,
  `APUESTA` int(5) DEFAULT 10,
  `GANANCIA` int(5) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `combate`
--

CREATE TABLE `combate` (
  `ID_COMBATE` int(4) NOT NULL,
  `ID_ENTRENADOR_1` int(3) NOT NULL,
  `ID_ENTRENADOR_2` int(3) NOT NULL,
  `TURNO_ACTUAL` int(2) NOT NULL DEFAULT 1,
  `ID_GANADOR` int(3) DEFAULT NULL,
  `KO_ENTRENADOR` int(1) NOT NULL DEFAULT 0,
  `KO_RIVAL` int(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `crianza`
--

CREATE TABLE `crianza` (
  `ID_POKE_CRIA` int(11) NOT NULL,
  `ID_POKE_PADRE` int(11) NOT NULL,
  `ID_POKE_MADRE` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `efectividad`
--

CREATE TABLE `efectividad` (
  `ID_TIPO_ATACANTE` int(2) NOT NULL,
  `ID_TIPO_DEFENSOR` int(2) NOT NULL,
  `MULTIPLICADOR` decimal(3,1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entrenador`
--

CREATE TABLE `entrenador` (
  `ID_ENTRENADOR` int(3) NOT NULL,
  `NOM_ENTRENADOR` varchar(20) NOT NULL,
  `TIPO_ENTRENADOR` enum('JUGADOR','ALTO_MANDO','CAMPEON') DEFAULT 'JUGADOR',
  `POKEDOLLAR` int(6) DEFAULT 0,
  `NIVEL_ENTRENADOR` int(3) DEFAULT 1,
  `PASSWORD` varchar(255) NOT NULL,
  `IMG_FRONT_ENTRENADOR` varchar(255) DEFAULT NULL,
  `IMG_TRAS_ENTRENADOR` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `liga`
--

CREATE TABLE `liga` (
  `ID_INTENTO` int(2) NOT NULL,
  `ID_ENTRENADOR` int(3) NOT NULL,
  `RECUPERACION_USADA` enum('SI','NO') NOT NULL DEFAULT 'NO',
  `PROGRESO_ESTADO` enum('EN CURSO','COMPLETADO','FALLIDO') DEFAULT 'EN CURSO',
  `COMBATE_ACTUAL` int(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mochila`
--

CREATE TABLE `mochila` (
  `ID_OBJETO` int(2) NOT NULL,
  `ID_ENTRENADOR` int(3) NOT NULL,
  `CANTIDAD` int(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `movimientos`
--

CREATE TABLE `movimientos` (
  `ID_MOVIMIENTOS` int(4) NOT NULL,
  `ID_TIPO` int(2) NOT NULL,
  `CLASE_MOVIMIENTO` enum('ATAQUE','MEJORA','ESTADO') NOT NULL,
  `NOM_MOVIMIENTO` varchar(20) NOT NULL,
  `NIVEL_APRENDIZAJE` int(3) DEFAULT NULL,
  `COSTE_ESTAMINA` int(3) DEFAULT NULL,
  `POTENCIA` int(3) DEFAULT NULL,
  `DECRIP_MOVIMIENTO` varchar(100) DEFAULT NULL,
  `PP` int(3) DEFAULT NULL,
  `NUM_TURNOS` int(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `objeto`
--

CREATE TABLE `objeto` (
  `ID_OBJETO` int(2) NOT NULL,
  `NOM_OBJETO` varchar(30) NOT NULL,
  `PRECIO_OBJETO` int(6) DEFAULT 0,
  `MOD_ATAQUE` int(2) DEFAULT 0,
  `MOD_DEFENSA` int(2) DEFAULT 0,
  `MOD_VELOCIDAD` int(2) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pokedex`
--

CREATE TABLE `pokedex` (
  `NUM_POKEDEX` int(4) NOT NULL,
  `NUM_POKEDEX_EVO` int(3) DEFAULT NULL,
  `NOM_POKEMON` varchar(50) NOT NULL,
  `NIVEL_EVO` int(3) DEFAULT NULL,
  `IMG_POKEDEX` varchar(255) DEFAULT NULL,
  `IMG_FRONT` varchar(255) DEFAULT NULL,
  `IMG_BACK` varchar(255) DEFAULT NULL,
  `SONIDO` varchar(255) DEFAULT NULL,
  `SONIDO_CRIES` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pokedex`
--

INSERT INTO `pokedex` (`NUM_POKEDEX`, `NUM_POKEDEX_EVO`, `NOM_POKEMON`, `NIVEL_EVO`, `IMG_POKEDEX`, `IMG_FRONT`, `IMG_BACK`, `SONIDO`, `SONIDO_CRIES`) VALUES
(1, 2, 'Bulbasaur', 16, 'img/img_pokedex/1.png', 'img/Front/1.png', 'img/Back/1.png', 'audio/Pokemon/001.wav', 'audio/Pokemon/cries_pokemon_legacy_1.ogg'),
(2, 3, 'Ivysaur', 32, 'img/img_pokedex/2.png', 'img/Front/2.png', 'img/Back/2.png', 'audio/Pokemon/002.wav', 'audio/Pokemon/cries_pokemon_legacy_2.ogg'),
(3, NULL, 'Venusaur', NULL, 'img/img_pokedex/3.png', 'img/Front/3.png', 'img/Back/3.png', 'audio/Pokemon/003.wav', 'audio/Pokemon/cries_pokemon_legacy_3.ogg'),
(4, 5, 'Charmander', 16, 'img/img_pokedex/4.png', 'img/Front/4.png', 'img/Back/4.png', 'audio/Pokemon/004.wav', 'audio/Pokemon/cries_pokemon_legacy_4.ogg'),
(5, 6, 'Charmeleon', 36, 'img/img_pokedex/5.png', 'img/Front/5.png', 'img/Back/5.png', 'audio/Pokemon/005.wav', 'audio/Pokemon/cries_pokemon_legacy_5.ogg'),
(6, NULL, 'Charizard', NULL, 'img/img_pokedex/6.png', 'img/Front/6.png', 'img/Back/6.png', 'audio/Pokemon/006.wav', 'audio/Pokemon/cries_pokemon_legacy_6.ogg'),
(7, 8, 'Squirtle', 16, 'img/img_pokedex/7.png', 'img/Front/7.png', 'img/Back/7.png', 'audio/Pokemon/007.wav', 'audio/Pokemon/cries_pokemon_legacy_7.ogg'),
(8, 9, 'Wartortle', 36, 'img/img_pokedex/8.png', 'img/Front/8.png', 'img/Back/8.png', 'audio/Pokemon/008.wav', 'audio/Pokemon/cries_pokemon_legacy_8.ogg'),
(9, NULL, 'Blastoise', NULL, 'img/img_pokedex/9.png', 'img/Front/9.png', 'img/Back/9.png', 'audio/Pokemon/009.wav', 'audio/Pokemon/cries_pokemon_legacy_9.ogg'),
(10, 11, 'Caterpie', 7, 'img/img_pokedex/10.png', 'img/Front/10.png', 'img/Back/10.png', 'audio/Pokemon/010.wav', 'audio/Pokemon/cries_pokemon_legacy_10.ogg'),
(11, 12, 'Metapod', 10, 'img/img_pokedex/11.png', 'img/Front/11.png', 'img/Back/11.png', 'audio/Pokemon/011.wav', 'audio/Pokemon/cries_pokemon_legacy_11.ogg'),
(12, NULL, 'Butterfree', NULL, 'img/img_pokedex/12.png', 'img/Front/12.png', 'img/Back/12.png', 'audio/Pokemon/012.wav', 'audio/Pokemon/cries_pokemon_legacy_12.ogg'),
(13, 14, 'Weedle', 7, 'img/img_pokedex/13.png', 'img/Front/13.png', 'img/Back/13.png', 'audio/Pokemon/013.wav', 'audio/Pokemon/cries_pokemon_legacy_13.ogg'),
(14, 15, 'Kakuna', 10, 'img/img_pokedex/14.png', 'img/Front/14.png', 'img/Back/14.png', 'audio/Pokemon/014.wav', 'audio/Pokemon/cries_pokemon_legacy_14.ogg'),
(15, NULL, 'Beedrill', NULL, 'img/img_pokedex/15.png', 'img/Front/15.png', 'img/Back/15.png', 'audio/Pokemon/015.wav', 'audio/Pokemon/cries_pokemon_legacy_15.ogg'),
(16, 17, 'Pidgey', 18, 'img/img_pokedex/16.png', 'img/Front/16.png', 'img/Back/16.png', 'audio/Pokemon/016.wav', 'audio/Pokemon/cries_pokemon_legacy_16.ogg'),
(17, 18, 'Pidgeotto', 36, 'img/img_pokedex/17.png', 'img/Front/17.png', 'img/Back/17.png', 'audio/Pokemon/017.wav', 'audio/Pokemon/cries_pokemon_legacy_17.ogg'),
(18, NULL, 'Pidgeot', NULL, 'img/img_pokedex/18.png', 'img/Front/18.png', 'img/Back/18.png', 'audio/Pokemon/018.wav', 'audio/Pokemon/cries_pokemon_legacy_18.ogg'),
(19, 20, 'Rattata', 20, 'img/img_pokedex/19.png', 'img/Front/19.png', 'img/Back/19.png', 'audio/Pokemon/019.wav', 'audio/Pokemon/cries_pokemon_legacy_19.ogg'),
(20, NULL, 'Raticate', NULL, 'img/img_pokedex/20.png', 'img/Front/20.png', 'img/Back/20.png', 'audio/Pokemon/020.wav', 'audio/Pokemon/cries_pokemon_legacy_20.ogg'),
(21, 22, 'Spearow', 20, 'img/img_pokedex/21.png', 'img/Front/21.png', 'img/Back/21.png', 'audio/Pokemon/021.wav', 'audio/Pokemon/cries_pokemon_legacy_21.ogg'),
(22, NULL, 'Fearow', NULL, 'img/img_pokedex/22.png', 'img/Front/22.png', 'img/Back/22.png', 'audio/Pokemon/022.wav', 'audio/Pokemon/cries_pokemon_legacy_22.ogg'),
(23, 24, 'Ekans', 22, 'img/img_pokedex/23.png', 'img/Front/23.png', 'img/Back/23.png', 'audio/Pokemon/023.wav', 'audio/Pokemon/cries_pokemon_legacy_23.ogg'),
(24, NULL, 'Arbok', NULL, 'img/img_pokedex/24.png', 'img/Front/24.png', 'img/Back/24.png', 'audio/Pokemon/024.wav', 'audio/Pokemon/cries_pokemon_legacy_24.ogg'),
(25, 26, 'Pikachu', 0, 'img/img_pokedex/25.png', 'img/Front/25.png', 'img/Back/25.png', 'audio/Pokemon/025.wav', 'audio/Pokemon/cries_pokemon_legacy_25.ogg'),
(26, NULL, 'Raichu', NULL, 'img/img_pokedex/26.png', 'img/Front/26.png', 'img/Back/26.png', 'audio/Pokemon/026.wav', 'audio/Pokemon/cries_pokemon_legacy_26.ogg'),
(27, 28, 'Sandshrew', 22, 'img/img_pokedex/27.png', 'img/Front/27.png', 'img/Back/27.png', 'audio/Pokemon/027.wav', 'audio/Pokemon/cries_pokemon_legacy_27.ogg'),
(28, NULL, 'Sandslash', NULL, 'img/img_pokedex/28.png', 'img/Front/28.png', 'img/Back/28.png', 'audio/Pokemon/028.wav', 'audio/Pokemon/cries_pokemon_legacy_28.ogg'),
(29, 30, 'Nidoran F', 16, 'img/img_pokedex/29.png', 'img/Front/29.png', 'img/Back/29.png', 'audio/Pokemon/029.wav', 'audio/Pokemon/cries_pokemon_legacy_29.ogg'),
(30, 31, 'Nidorina', 0, 'img/img_pokedex/30.png', 'img/Front/30.png', 'img/Back/30.png', 'audio/Pokemon/030.wav', 'audio/Pokemon/cries_pokemon_legacy_30.ogg'),
(31, NULL, 'Nidoqueen', NULL, 'img/img_pokedex/31.png', 'img/Front/31.png', 'img/Back/31.png', 'audio/Pokemon/031.wav', 'audio/Pokemon/cries_pokemon_legacy_31.ogg'),
(32, 33, 'Nidoran M', 16, 'img/img_pokedex/32.png', 'img/Front/32.png', 'img/Back/32.png', 'audio/Pokemon/032.wav', 'audio/Pokemon/cries_pokemon_legacy_32.ogg'),
(33, 34, 'Nidorino', 0, 'img/img_pokedex/33.png', 'img/Front/33.png', 'img/Back/33.png', 'audio/Pokemon/033.wav', 'audio/Pokemon/cries_pokemon_legacy_33.ogg'),
(34, NULL, 'Nidoking', NULL, 'img/img_pokedex/34.png', 'img/Front/34.png', 'img/Back/34.png', 'audio/Pokemon/034.wav', 'audio/Pokemon/cries_pokemon_legacy_34.ogg'),
(35, 36, 'Clefairy', 0, 'img/img_pokedex/35.png', 'img/Front/35.png', 'img/Back/35.png', 'audio/Pokemon/035.wav', 'audio/Pokemon/cries_pokemon_legacy_35.ogg'),
(36, NULL, 'Clefable', NULL, 'img/img_pokedex/36.png', 'img/Front/36.png', 'img/Back/36.png', 'audio/Pokemon/036.wav', 'audio/Pokemon/cries_pokemon_legacy_36.ogg'),
(37, 38, 'Vulpix', 0, 'img/img_pokedex/37.png', 'img/Front/37.png', 'img/Back/37.png', 'audio/Pokemon/037.wav', 'audio/Pokemon/cries_pokemon_legacy_37.ogg'),
(38, NULL, 'Ninetales', NULL, 'img/img_pokedex/38.png', 'img/Front/38.png', 'img/Back/38.png', 'audio/Pokemon/038.wav', 'audio/Pokemon/cries_pokemon_legacy_38.ogg'),
(39, 40, 'Jigglypuff', 0, 'img/img_pokedex/39.png', 'img/Front/39.png', 'img/Back/39.png', 'audio/Pokemon/039.wav', 'audio/Pokemon/cries_pokemon_legacy_39.ogg'),
(40, NULL, 'Wigglytuff', NULL, 'img/img_pokedex/40.png', 'img/Front/40.png', 'img/Back/40.png', 'audio/Pokemon/040.wav', 'audio/Pokemon/cries_pokemon_legacy_40.ogg'),
(41, 42, 'Zubat', 22, 'img/img_pokedex/41.png', 'img/Front/41.png', 'img/Back/41.png', 'audio/Pokemon/041.wav', 'audio/Pokemon/cries_pokemon_legacy_41.ogg'),
(42, NULL, 'Golbat', NULL, 'img/img_pokedex/42.png', 'img/Front/42.png', 'img/Back/42.png', 'audio/Pokemon/042.wav', 'audio/Pokemon/cries_pokemon_legacy_42.ogg'),
(43, 44, 'Oddish', 21, 'img/img_pokedex/43.png', 'img/Front/43.png', 'img/Back/43.png', 'audio/Pokemon/043.wav', 'audio/Pokemon/cries_pokemon_legacy_43.ogg'),
(44, 45, 'Gloom', 0, 'img/img_pokedex/44.png', 'img/Front/44.png', 'img/Back/44.png', 'audio/Pokemon/044.wav', 'audio/Pokemon/cries_pokemon_legacy_44.ogg'),
(45, NULL, 'Vileplume', NULL, 'img/img_pokedex/45.png', 'img/Front/45.png', 'img/Back/45.png', 'audio/Pokemon/045.wav', 'audio/Pokemon/cries_pokemon_legacy_45.ogg'),
(46, 47, 'Paras', 24, 'img/img_pokedex/46.png', 'img/Front/46.png', 'img/Back/46.png', 'audio/Pokemon/046.wav', 'audio/Pokemon/cries_pokemon_legacy_46.ogg'),
(47, NULL, 'Parasect', NULL, 'img/img_pokedex/47.png', 'img/Front/47.png', 'img/Back/47.png', 'audio/Pokemon/047.wav', 'audio/Pokemon/cries_pokemon_legacy_47.ogg'),
(48, 49, 'Venonat', 31, 'img/img_pokedex/48.png', 'img/Front/48.png', 'img/Back/48.png', 'audio/Pokemon/048.wav', 'audio/Pokemon/cries_pokemon_legacy_48.ogg'),
(49, NULL, 'Venomoth', NULL, 'img/img_pokedex/49.png', 'img/Front/49.png', 'img/Back/49.png', 'audio/Pokemon/049.wav', 'audio/Pokemon/cries_pokemon_legacy_49.ogg'),
(50, 51, 'Diglett', 26, 'img/img_pokedex/50.png', 'img/Front/50.png', 'img/Back/50.png', 'audio/Pokemon/050.wav', 'audio/Pokemon/cries_pokemon_legacy_50.ogg'),
(51, NULL, 'Dugtrio', NULL, 'img/img_pokedex/51.png', 'img/Front/51.png', 'img/Back/51.png', 'audio/Pokemon/051.wav', 'audio/Pokemon/cries_pokemon_legacy_51.ogg'),
(52, 53, 'Meowth', 28, 'img/img_pokedex/52.png', 'img/Front/52.png', 'img/Back/52.png', 'audio/Pokemon/052.wav', 'audio/Pokemon/cries_pokemon_legacy_52.ogg'),
(53, NULL, 'Persian', NULL, 'img/img_pokedex/53.png', 'img/Front/53.png', 'img/Back/53.png', 'audio/Pokemon/053.wav', 'audio/Pokemon/cries_pokemon_legacy_53.ogg'),
(54, 55, 'Psyduck', 33, 'img/img_pokedex/54.png', 'img/Front/54.png', 'img/Back/54.png', 'audio/Pokemon/054.wav', 'audio/Pokemon/cries_pokemon_legacy_54.ogg'),
(55, NULL, 'Golduck', NULL, 'img/img_pokedex/55.png', 'img/Front/55.png', 'img/Back/55.png', 'audio/Pokemon/055.wav', 'audio/Pokemon/cries_pokemon_legacy_55.ogg'),
(56, 57, 'Mankey', 28, 'img/img_pokedex/56.png', 'img/Front/56.png', 'img/Back/56.png', 'audio/Pokemon/056.wav', 'audio/Pokemon/cries_pokemon_legacy_56.ogg'),
(57, NULL, 'Primeape', NULL, 'img/img_pokedex/57.png', 'img/Front/57.png', 'img/Back/57.png', 'audio/Pokemon/057.wav', 'audio/Pokemon/cries_pokemon_legacy_57.ogg'),
(58, 59, 'Growlithe', 0, 'img/img_pokedex/58.png', 'img/Front/58.png', 'img/Back/58.png', 'audio/Pokemon/058.wav', 'audio/Pokemon/cries_pokemon_legacy_58.ogg'),
(59, NULL, 'Arcanine', NULL, 'img/img_pokedex/59.png', 'img/Front/59.png', 'img/Back/59.png', 'audio/Pokemon/059.wav', 'audio/Pokemon/cries_pokemon_legacy_59.ogg'),
(60, 61, 'Poliwag', 25, 'img/img_pokedex/60.png', 'img/Front/60.png', 'img/Back/60.png', 'audio/Pokemon/060.wav', 'audio/Pokemon/cries_pokemon_legacy_60.ogg'),
(61, 62, 'Poliwhirl', 0, 'img/img_pokedex/61.png', 'img/Front/61.png', 'img/Back/61.png', 'audio/Pokemon/061.wav', 'audio/Pokemon/cries_pokemon_legacy_61.ogg'),
(62, NULL, 'Poliwrath', NULL, 'img/img_pokedex/62.png', 'img/Front/62.png', 'img/Back/62.png', 'audio/Pokemon/062.wav', 'audio/Pokemon/cries_pokemon_legacy_62.ogg'),
(63, 64, 'Abra', 16, 'img/img_pokedex/63.png', 'img/Front/63.png', 'img/Back/63.png', 'audio/Pokemon/063.wav', 'audio/Pokemon/cries_pokemon_legacy_63.ogg'),
(64, 65, 'Kadabra', 0, 'img/img_pokedex/64.png', 'img/Front/64.png', 'img/Back/64.png', 'audio/Pokemon/064.wav', 'audio/Pokemon/cries_pokemon_legacy_64.ogg'),
(65, NULL, 'Alakazam', NULL, 'img/img_pokedex/65.png', 'img/Front/65.png', 'img/Back/65.png', 'audio/Pokemon/065.wav', 'audio/Pokemon/cries_pokemon_legacy_65.ogg'),
(66, 67, 'Machop', 28, 'img/img_pokedex/66.png', 'img/Front/66.png', 'img/Back/66.png', 'audio/Pokemon/066.wav', 'audio/Pokemon/cries_pokemon_legacy_66.ogg'),
(67, 68, 'Machoke', 0, 'img/img_pokedex/67.png', 'img/Front/67.png', 'img/Back/67.png', 'audio/Pokemon/067.wav', 'audio/Pokemon/cries_pokemon_legacy_67.ogg'),
(68, NULL, 'Machamp', NULL, 'img/img_pokedex/68.png', 'img/Front/68.png', 'img/Back/68.png', 'audio/Pokemon/068.wav', 'audio/Pokemon/cries_pokemon_legacy_68.ogg'),
(69, 70, 'Bellsprout', 21, 'img/img_pokedex/69.png', 'img/Front/69.png', 'img/Back/69.png', 'audio/Pokemon/069.wav', 'audio/Pokemon/cries_pokemon_legacy_69.ogg'),
(70, 71, 'Weepinbell', 0, 'img/img_pokedex/70.png', 'img/Front/70.png', 'img/Back/70.png', 'audio/Pokemon/070.wav', 'audio/Pokemon/cries_pokemon_legacy_70.ogg'),
(71, NULL, 'Victreebel', NULL, 'img/img_pokedex/71.png', 'img/Front/71.png', 'img/Back/71.png', 'audio/Pokemon/071.wav', 'audio/Pokemon/cries_pokemon_legacy_71.ogg'),
(72, 73, 'Tentacool', 30, 'img/img_pokedex/72.png', 'img/Front/72.png', 'img/Back/72.png', 'audio/Pokemon/072.wav', 'audio/Pokemon/cries_pokemon_legacy_72.ogg'),
(73, NULL, 'Tentacruel', NULL, 'img/img_pokedex/73.png', 'img/Front/73.png', 'img/Back/73.png', 'audio/Pokemon/073.wav', 'audio/Pokemon/cries_pokemon_legacy_73.ogg'),
(74, 75, 'Geodude', 25, 'img/img_pokedex/74.png', 'img/Front/74.png', 'img/Back/74.png', 'audio/Pokemon/074.wav', 'audio/Pokemon/cries_pokemon_legacy_74.ogg'),
(75, 76, 'Graveler', 0, 'img/img_pokedex/75.png', 'img/Front/75.png', 'img/Back/75.png', 'audio/Pokemon/075.wav', 'audio/Pokemon/cries_pokemon_legacy_75.ogg'),
(76, NULL, 'Golem', NULL, 'img/img_pokedex/76.png', 'img/Front/76.png', 'img/Back/76.png', 'audio/Pokemon/076.wav', 'audio/Pokemon/cries_pokemon_legacy_76.ogg'),
(77, 78, 'Ponyta', 40, 'img/img_pokedex/77.png', 'img/Front/77.png', 'img/Back/77.png', 'audio/Pokemon/077.wav', 'audio/Pokemon/cries_pokemon_legacy_77.ogg'),
(78, NULL, 'Rapidash', NULL, 'img/img_pokedex/78.png', 'img/Front/78.png', 'img/Back/78.png', 'audio/Pokemon/078.wav', 'audio/Pokemon/cries_pokemon_legacy_78.ogg'),
(79, 80, 'Slowpoke', 37, 'img/img_pokedex/79.png', 'img/Front/79.png', 'img/Back/79.png', 'audio/Pokemon/079.wav', 'audio/Pokemon/cries_pokemon_legacy_79.ogg'),
(80, NULL, 'Slowbro', NULL, 'img/img_pokedex/80.png', 'img/Front/80.png', 'img/Back/80.png', 'audio/Pokemon/080.wav', 'audio/Pokemon/cries_pokemon_legacy_80.ogg'),
(81, 82, 'Magnemite', 30, 'img/img_pokedex/81.png', 'img/Front/81.png', 'img/Back/81.png', 'audio/Pokemon/081.wav', 'audio/Pokemon/cries_pokemon_legacy_81.ogg'),
(82, NULL, 'Magneton', NULL, 'img/img_pokedex/82.png', 'img/Front/82.png', 'img/Back/82.png', 'audio/Pokemon/082.wav', 'audio/Pokemon/cries_pokemon_legacy_82.ogg'),
(83, NULL, 'Farfetchd', NULL, 'img/img_pokedex/83.png', 'img/Front/83.png', 'img/Back/83.png', 'audio/Pokemon/083.wav', 'audio/Pokemon/cries_pokemon_legacy_83.ogg'),
(84, 85, 'Doduo', 31, 'img/img_pokedex/84.png', 'img/Front/84.png', 'img/Back/84.png', 'audio/Pokemon/084.wav', 'audio/Pokemon/cries_pokemon_legacy_84.ogg'),
(85, NULL, 'Dodrio', NULL, 'img/img_pokedex/85.png', 'img/Front/85.png', 'img/Back/85.png', 'audio/Pokemon/085.wav', 'audio/Pokemon/cries_pokemon_legacy_85.ogg'),
(86, 87, 'Seel', 34, 'img/img_pokedex/86.png', 'img/Front/86.png', 'img/Back/86.png', 'audio/Pokemon/086.wav', 'audio/Pokemon/cries_pokemon_legacy_86.ogg'),
(87, NULL, 'Dewgong', NULL, 'img/img_pokedex/87.png', 'img/Front/87.png', 'img/Back/87.png', 'audio/Pokemon/087.wav', 'audio/Pokemon/cries_pokemon_legacy_87.ogg'),
(88, 89, 'Grimer', 38, 'img/img_pokedex/88.png', 'img/Front/88.png', 'img/Back/88.png', 'audio/Pokemon/088.wav', 'audio/Pokemon/cries_pokemon_legacy_88.ogg'),
(89, NULL, 'Muk', NULL, 'img/img_pokedex/89.png', 'img/Front/89.png', 'img/Back/89.png', 'audio/Pokemon/089.wav', 'audio/Pokemon/cries_pokemon_legacy_89.ogg'),
(90, 91, 'Shellder', 0, 'img/img_pokedex/90.png', 'img/Front/90.png', 'img/Back/90.png', 'audio/Pokemon/090.wav', 'audio/Pokemon/cries_pokemon_legacy_90.ogg'),
(91, NULL, 'Cloyster', NULL, 'img/img_pokedex/91.png', 'img/Front/91.png', 'img/Back/91.png', 'audio/Pokemon/091.wav', 'audio/Pokemon/cries_pokemon_legacy_91.ogg'),
(92, 93, 'Gastly', 25, 'img/img_pokedex/92.png', 'img/Front/92.png', 'img/Back/92.png', 'audio/Pokemon/092.wav', 'audio/Pokemon/cries_pokemon_legacy_92.ogg'),
(93, 94, 'Haunter', 0, 'img/img_pokedex/93.png', 'img/Front/93.png', 'img/Back/93.png', 'audio/Pokemon/093.wav', 'audio/Pokemon/cries_pokemon_legacy_93.ogg'),
(94, NULL, 'Gengar', NULL, 'img/img_pokedex/94.png', 'img/Front/94.png', 'img/Back/94.png', 'audio/Pokemon/094.wav', 'audio/Pokemon/cries_pokemon_legacy_94.ogg'),
(95, NULL, 'Onix', NULL, 'img/img_pokedex/95.png', 'img/Front/95.png', 'img/Back/95.png', 'audio/Pokemon/095.wav', 'audio/Pokemon/cries_pokemon_legacy_95.ogg'),
(96, 97, 'Drowzee', 26, 'img/img_pokedex/96.png', 'img/Front/96.png', 'img/Back/96.png', 'audio/Pokemon/096.wav', 'audio/Pokemon/cries_pokemon_legacy_96.ogg'),
(97, NULL, 'Hypno', NULL, 'img/img_pokedex/97.png', 'img/Front/97.png', 'img/Back/97.png', 'audio/Pokemon/097.wav', 'audio/Pokemon/cries_pokemon_legacy_97.ogg'),
(98, 99, 'Krabby', 28, 'img/img_pokedex/98.png', 'img/Front/98.png', 'img/Back/98.png', 'audio/Pokemon/098.wav', 'audio/Pokemon/cries_pokemon_legacy_98.ogg'),
(99, NULL, 'Kingler', NULL, 'img/img_pokedex/99.png', 'img/Front/99.png', 'img/Back/99.png', 'audio/Pokemon/099.wav', 'audio/Pokemon/cries_pokemon_legacy_99.ogg'),
(100, 101, 'Voltorb', 30, 'img/img_pokedex/100.png', 'img/Front/100.png', 'img/Back/100.png', 'audio/Pokemon/100.wav', 'audio/Pokemon/cries_pokemon_legacy_100.ogg'),
(101, NULL, 'Electrode', NULL, 'img/img_pokedex/101.png', 'img/Front/101.png', 'img/Back/101.png', 'audio/Pokemon/101.wav', 'audio/Pokemon/cries_pokemon_legacy_101.ogg'),
(102, 103, 'Exeggcute', 0, 'img/img_pokedex/102.png', 'img/Front/102.png', 'img/Back/102.png', 'audio/Pokemon/102.wav', 'audio/Pokemon/cries_pokemon_legacy_102.ogg'),
(103, NULL, 'Exeggutor', NULL, 'img/img_pokedex/103.png', 'img/Front/103.png', 'img/Back/103.png', 'audio/Pokemon/103.wav', 'audio/Pokemon/cries_pokemon_legacy_103.ogg'),
(104, 105, 'Cubone', 28, 'img/img_pokedex/104.png', 'img/Front/104.png', 'img/Back/104.png', 'audio/Pokemon/104.wav', 'audio/Pokemon/cries_pokemon_legacy_104.ogg'),
(105, NULL, 'Marowak', NULL, 'img/img_pokedex/105.png', 'img/Front/105.png', 'img/Back/105.png', 'audio/Pokemon/105.wav', 'audio/Pokemon/cries_pokemon_legacy_105.ogg'),
(106, NULL, 'Hitmonlee', NULL, 'img/img_pokedex/106.png', 'img/Front/106.png', 'img/Back/106.png', 'audio/Pokemon/106.wav', 'audio/Pokemon/cries_pokemon_legacy_106.ogg'),
(107, NULL, 'Hitmonchan', NULL, 'img/img_pokedex/107.png', 'img/Front/107.png', 'img/Back/107.png', 'audio/Pokemon/107.wav', 'audio/Pokemon/cries_pokemon_legacy_107.ogg'),
(108, NULL, 'Lickitung', NULL, 'img/img_pokedex/108.png', 'img/Front/108.png', 'img/Back/108.png', 'audio/Pokemon/108.wav', 'audio/Pokemon/cries_pokemon_legacy_108.ogg'),
(109, 110, 'Koffing', 35, 'img/img_pokedex/109.png', 'img/Front/109.png', 'img/Back/109.png', 'audio/Pokemon/109.wav', 'audio/Pokemon/cries_pokemon_legacy_109.ogg'),
(110, NULL, 'Weezing', NULL, 'img/img_pokedex/110.png', 'img/Front/110.png', 'img/Back/110.png', 'audio/Pokemon/110.wav', 'audio/Pokemon/cries_pokemon_legacy_110.ogg'),
(111, 112, 'Rhyhorn', 42, 'img/img_pokedex/111.png', 'img/Front/111.png', 'img/Back/111.png', 'audio/Pokemon/111.wav', 'audio/Pokemon/cries_pokemon_legacy_111.ogg'),
(112, NULL, 'Rhydon', NULL, 'img/img_pokedex/112.png', 'img/Front/112.png', 'img/Back/112.png', 'audio/Pokemon/112.wav', 'audio/Pokemon/cries_pokemon_legacy_112.ogg'),
(113, NULL, 'Chansey', NULL, 'img/img_pokedex/113.png', 'img/Front/113.png', 'img/Back/113.png', 'audio/Pokemon/113.wav', 'audio/Pokemon/cries_pokemon_legacy_113.ogg'),
(114, NULL, 'Tangela', NULL, 'img/img_pokedex/114.png', 'img/Front/114.png', 'img/Back/114.png', 'audio/Pokemon/114.wav', 'audio/Pokemon/cries_pokemon_legacy_114.ogg'),
(115, NULL, 'Kangaskhan', NULL, 'img/img_pokedex/115.png', 'img/Front/115.png', 'img/Back/115.png', 'audio/Pokemon/115.wav', 'audio/Pokemon/cries_pokemon_legacy_115.ogg'),
(116, 117, 'Horsea', 32, 'img/img_pokedex/116.png', 'img/Front/116.png', 'img/Back/116.png', 'audio/Pokemon/116.wav', 'audio/Pokemon/cries_pokemon_legacy_116.ogg'),
(117, NULL, 'Seadra', NULL, 'img/img_pokedex/117.png', 'img/Front/117.png', 'img/Back/117.png', 'audio/Pokemon/117.wav', 'audio/Pokemon/cries_pokemon_legacy_117.ogg'),
(118, 119, 'Goldeen', 33, 'img/img_pokedex/118.png', 'img/Front/118.png', 'img/Back/118.png', 'audio/Pokemon/118.wav', 'audio/Pokemon/cries_pokemon_legacy_118.ogg'),
(119, NULL, 'Seaking', NULL, 'img/img_pokedex/119.png', 'img/Front/119.png', 'img/Back/119.png', 'audio/Pokemon/119.wav', 'audio/Pokemon/cries_pokemon_legacy_119.ogg'),
(120, 121, 'Staryu', 0, 'img/img_pokedex/120.png', 'img/Front/120.png', 'img/Back/120.png', 'audio/Pokemon/120.wav', 'audio/Pokemon/cries_pokemon_legacy_120.ogg'),
(121, NULL, 'Starmie', NULL, 'img/img_pokedex/121.png', 'img/Front/121.png', 'img/Back/121.png', 'audio/Pokemon/121.wav', 'audio/Pokemon/cries_pokemon_legacy_121.ogg'),
(122, NULL, 'Mr. Mime', NULL, 'img/img_pokedex/122.png', 'img/Front/122.png', 'img/Back/122.png', 'audio/Pokemon/122.wav', 'audio/Pokemon/cries_pokemon_legacy_122.ogg'),
(123, NULL, 'Scyther', NULL, 'img/img_pokedex/123.png', 'img/Front/123.png', 'img/Back/123.png', 'audio/Pokemon/123.wav', 'audio/Pokemon/cries_pokemon_legacy_123.ogg'),
(124, NULL, 'Jynx', NULL, 'img/img_pokedex/124.png', 'img/Front/124.png', 'img/Back/124.png', 'audio/Pokemon/124.wav', 'audio/Pokemon/cries_pokemon_legacy_124.ogg'),
(125, NULL, 'Electabuzz', NULL, 'img/img_pokedex/125.png', 'img/Front/125.png', 'img/Back/125.png', 'audio/Pokemon/125.wav', 'audio/Pokemon/cries_pokemon_legacy_125.ogg'),
(126, NULL, 'Magmar', NULL, 'img/img_pokedex/126.png', 'img/Front/126.png', 'img/Back/126.png', 'audio/Pokemon/126.wav', 'audio/Pokemon/cries_pokemon_legacy_126.ogg'),
(127, NULL, 'Pinsir', NULL, 'img/img_pokedex/127.png', 'img/Front/127.png', 'img/Back/127.png', 'audio/Pokemon/127.wav', 'audio/Pokemon/cries_pokemon_legacy_127.ogg'),
(128, NULL, 'Tauros', NULL, 'img/img_pokedex/128.png', 'img/Front/128.png', 'img/Back/128.png', 'audio/Pokemon/128.wav', 'audio/Pokemon/cries_pokemon_legacy_128.ogg'),
(129, 130, 'Magikarp', 20, 'img/img_pokedex/129.png', 'img/Front/129.png', 'img/Back/129.png', 'audio/Pokemon/129.wav', 'audio/Pokemon/cries_pokemon_legacy_129.ogg'),
(130, NULL, 'Gyarados', NULL, 'img/img_pokedex/130.png', 'img/Front/130.png', 'img/Back/130.png', 'audio/Pokemon/130.wav', 'audio/Pokemon/cries_pokemon_legacy_130.ogg'),
(131, NULL, 'Lapras', NULL, 'img/img_pokedex/131.png', 'img/Front/131.png', 'img/Back/131.png', 'audio/Pokemon/131.wav', 'audio/Pokemon/cries_pokemon_legacy_131.ogg'),
(132, NULL, 'Ditto', NULL, 'img/img_pokedex/132.png', 'img/Front/132.png', 'img/Back/132.png', 'audio/Pokemon/132.wav', 'audio/Pokemon/cries_pokemon_legacy_132.ogg'),
(133, NULL, 'Eevee', 0, 'img/img_pokedex/133.png', 'img/Front/133.png', 'img/Back/133.png', 'audio/Pokemon/133.wav', 'audio/Pokemon/cries_pokemon_legacy_133.ogg'),
(134, NULL, 'Vaporeon', NULL, 'img/img_pokedex/134.png', 'img/Front/134.png', 'img/Back/134.png', 'audio/Pokemon/134.wav', 'audio/Pokemon/cries_pokemon_legacy_134.ogg'),
(135, NULL, 'Jolteon', NULL, 'img/img_pokedex/135.png', 'img/Front/135.png', 'img/Back/135.png', 'audio/Pokemon/135.wav', 'audio/Pokemon/cries_pokemon_legacy_135.ogg'),
(136, NULL, 'Flareon', NULL, 'img/img_pokedex/136.png', 'img/Front/136.png', 'img/Back/136.png', 'audio/Pokemon/136.wav', 'audio/Pokemon/cries_pokemon_legacy_136.ogg'),
(137, NULL, 'Porygon', NULL, 'img/img_pokedex/137.png', 'img/Front/137.png', 'img/Back/137.png', 'audio/Pokemon/137.wav', 'audio/Pokemon/cries_pokemon_legacy_137.ogg'),
(138, 139, 'Omanyte', 40, 'img/img_pokedex/138.png', 'img/Front/138.png', 'img/Back/138.png', 'audio/Pokemon/138.wav', 'audio/Pokemon/cries_pokemon_legacy_138.ogg'),
(139, NULL, 'Omastar', NULL, 'img/img_pokedex/139.png', 'img/Front/139.png', 'img/Back/139.png', 'audio/Pokemon/139.wav', 'audio/Pokemon/cries_pokemon_legacy_139.ogg'),
(140, 141, 'Kabuto', 40, 'img/img_pokedex/140.png', 'img/Front/140.png', 'img/Back/140.png', 'audio/Pokemon/140.wav', 'audio/Pokemon/cries_pokemon_legacy_140.ogg'),
(141, NULL, 'Kabutops', NULL, 'img/img_pokedex/141.png', 'img/Front/141.png', 'img/Back/141.png', 'audio/Pokemon/141.wav', 'audio/Pokemon/cries_pokemon_legacy_141.ogg'),
(142, NULL, 'Aerodactyl', NULL, 'img/img_pokedex/142.png', 'img/Front/142.png', 'img/Back/142.png', 'audio/Pokemon/142.wav', 'audio/Pokemon/cries_pokemon_legacy_142.ogg'),
(143, NULL, 'Snorlax', NULL, 'img/img_pokedex/143.png', 'img/Front/143.png', 'img/Back/143.png', 'audio/Pokemon/143.wav', 'audio/Pokemon/cries_pokemon_legacy_143.ogg'),
(144, NULL, 'Articuno', NULL, 'img/img_pokedex/144.png', 'img/Front/144.png', 'img/Back/144.png', 'audio/Pokemon/144.wav', 'audio/Pokemon/cries_pokemon_legacy_144.ogg'),
(145, NULL, 'Zapdos', NULL, 'img/img_pokedex/145.png', 'img/Front/145.png', 'img/Back/145.png', 'audio/Pokemon/145.wav', 'audio/Pokemon/cries_pokemon_legacy_145.ogg'),
(146, NULL, 'Moltres', NULL, 'img/img_pokedex/146.png', 'img/Front/146.png', 'img/Back/146.png', 'audio/Pokemon/146.wav', 'audio/Pokemon/cries_pokemon_legacy_146.ogg'),
(147, 148, 'Dratini', 30, 'img/img_pokedex/147.png', 'img/Front/147.png', 'img/Back/147.png', 'audio/Pokemon/147.wav', 'audio/Pokemon/cries_pokemon_legacy_147.ogg'),
(148, 149, 'Dragonair', 55, 'img/img_pokedex/148.png', 'img/Front/148.png', 'img/Back/148.png', 'audio/Pokemon/148.wav', 'audio/Pokemon/cries_pokemon_legacy_148.ogg'),
(149, NULL, 'Dragonite', NULL, 'img/img_pokedex/149.png', 'img/Front/149.png', 'img/Back/149.png', 'audio/Pokemon/149.wav', 'audio/Pokemon/cries_pokemon_legacy_149.ogg'),
(150, NULL, 'Mewtwo', NULL, 'img/img_pokedex/150.png', 'img/Front/150.png', 'img/Back/150.png', 'audio/Pokemon/150.wav', 'audio/Pokemon/cries_pokemon_legacy_150.ogg'),
(151, NULL, 'Mew', NULL, 'img/img_pokedex/151.png', 'img/Front/151.png', 'img/Back/151.png', 'audio/Pokemon/151.wav', 'audio/Pokemon/cries_pokemon_legacy_151.ogg');

INSERT INTO objeto (ID_OBJETO, NOM_OBJETO, PRECIO_OBJETO, MOD_ATAQUE, MOD_DEFENSA, MOD_VELOCIDAD) 
VALUES (1, 'Pesa', 150, 20, 20, -20);
INSERT INTO objeto (ID_OBJETO, NOM_OBJETO, PRECIO_OBJETO, MOD_ATAQUE, MOD_DEFENSA, MOD_VELOCIDAD) 
VALUES (2, 'Pluma', 100, 0, -20, 30); 
INSERT INTO objeto (ID_OBJETO, NOM_OBJETO, PRECIO_OBJETO, MOD_ATAQUE, MOD_DEFENSA, MOD_VELOCIDAD) 
VALUES (3, 'Chaleco', 200, -15, 20, -15); 
INSERT INTO objeto (ID_OBJETO, NOM_OBJETO, PRECIO_OBJETO, MOD_ATAQUE, MOD_DEFENSA, MOD_VELOCIDAD) 
VALUES (4, 'Bastón', 120, 0, 0, -15);
INSERT INTO objeto (ID_OBJETO, NOM_OBJETO, PRECIO_OBJETO, MOD_ATAQUE, MOD_DEFENSA, MOD_VELOCIDAD) 
VALUES (5, 'Pilas', 180, 0, 0, 0); 


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pokedex_tipo`
--

CREATE TABLE `pokedex_tipo` (
  `ID_TIPO` int(2) NOT NULL,
  `NUM_POKEDEX` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pokemon`
--

CREATE TABLE `pokemon` (
  `ID_POKEMON` int(11) NOT NULL,
  `ID_ENTRENADOR` int(3) DEFAULT NULL,
  `NUM_POKEDEX` int(4) DEFAULT NULL,
  `ID_OBJETO` int(3) DEFAULT NULL,
  `MOTE` varchar(20) NOT NULL,
  `VITALIDAD` int(3) DEFAULT 10,
  `ATAQUE` int(3) DEFAULT 10,
  `ATAQUE_ESPECIAL` int(3) DEFAULT 10,
  `DEFENSA` int(3) DEFAULT 10,
  `DEFENSA_ESPECIAL` int(3) DEFAULT 10,
  `VELOCIDAD` int(3) DEFAULT 10,
  `NIVEL` int(3) DEFAULT 1,
  `FERTILIDAD` int(1) DEFAULT 5,
  `ESTAMINA` int(3) DEFAULT 10,
  `EXPERIENCIA` int(3) DEFAULT 0,
  `SEXO` enum('M','H') DEFAULT NULL,
  `ESTADO` varchar(20) DEFAULT 'NORMAL',
  `UBICACION` enum('EQUIPO','CAJA') DEFAULT 'EQUIPO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pokemon_movimiento`
--

CREATE TABLE `pokemon_movimiento` (
  `ID_MOVIMIENTOS` int(4) NOT NULL,
  `ID_POKEMON` int(11) NOT NULL,
  `ACTIVO` enum('SI','NO') DEFAULT 'NO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipos_pokemon`
--

CREATE TABLE `tipos_pokemon` (
  `ID_TIPO` int(2) NOT NULL,
  `NOM_TIPO` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `turno_combate`
--

CREATE TABLE `turno_combate` (
  `ID_COMBATE` int(4) NOT NULL,
  `NUM_TURNO` int(2) NOT NULL DEFAULT 1,
  `ACCION_ENTRENADOR` varchar(100) NOT NULL,
  `ACCION_RIVAL` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Estructura de la tabla para la nueva tabla 'pokedex-movimiento'

CREATE TABLE `pokedex_movimiento` (
  `NUM_POKEDEX` int(4) NOT NULL,
  `ID_MOVIMIENTOS` int(4) NOT NULL,
  PRIMARY KEY (`NUM_POKEDEX`,`ID_MOVIMIENTOS`),
  CONSTRAINT `FK_POKEDEX_MOV` FOREIGN KEY (`NUM_POKEDEX`) REFERENCES `pokedex` (`NUM_POKEDEX`),
  CONSTRAINT `FK_MOVIMIENTO_POK` FOREIGN KEY (`ID_MOVIMIENTOS`) REFERENCES `movimientos` (`ID_MOVIMIENTOS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- NORMAL (13)
INSERT INTO `movimientos` (`ID_MOVIMIENTOS`, `ID_TIPO`, `CLASE_MOVIMIENTO`, `NOM_MOVIMIENTO`, `NIVEL_APRENDIZAJE`, `COSTE_ESTAMINA`, `POTENCIA`, `DECRIP_MOVIMIENTO`, `PP`, `NUM_TURNOS`) VALUES
(1, 13, 'ATAQUE', 'Placaje', 1, 5, 40, 'Embiste con todo el cuerpo.', 35, 0),
(2, 13, 'ATAQUE', 'Arañazo', 1, 5, 40, 'Araña con garras afiladas.', 35, 0),
(3, 13, 'ATAQUE', 'Ataque Rápido', 10, 8, 40, 'Ataque que siempre golpea primero.', 30, 0),
(4, 13, 'ESTADO', 'Canto', 15, 12, 0, 'Canta una nana que duerme al rival.', 15, 3),
(5, 13, 'ATAQUE', 'Hiperrayo', 50, 30, 150, 'Ataque brutal. Requiere descanso.', 5, 0),
(6, 13, 'MEJORA', 'Recuperación', 35, 20, 0, 'Restaura vitalidad y estamina.', 10, 0);

-- FUEGO (2)
INSERT INTO `movimientos` (`ID_MOVIMIENTOS`, `ID_TIPO`, `CLASE_MOVIMIENTO`, `NOM_MOVIMIENTO`, `NIVEL_APRENDIZAJE`, `COSTE_ESTAMINA`, `POTENCIA`, `DECRIP_MOVIMIENTO`, `PP`, `NUM_TURNOS`) VALUES
(7, 2, 'ATAQUE', 'Ascuas', 7, 10, 40, 'Lanza pequeñas llamas.', 25, 0),
(8, 2, 'ATAQUE', 'Giro Fuego', 24, 15, 35, 'Atrapa al rival en un vórtice de fuego.', 15, 0),
(9, 2, 'ATAQUE', 'Lanzallamas', 34, 20, 90, 'Potente chorro de fuego abrasador.', 15, 0),
(10, 2, 'ATAQUE', 'Llamarada', 50, 25, 110, 'Fuego intenso que lo calcina todo.', 5, 0);

-- AGUA (1)
INSERT INTO `movimientos` (`ID_MOVIMIENTOS`, `ID_TIPO`, `CLASE_MOVIMIENTO`, `NOM_MOVIMIENTO`, `NIVEL_APRENDIZAJE`, `COSTE_ESTAMINA`, `POTENCIA`, `DECRIP_MOVIMIENTO`, `PP`, `NUM_TURNOS`) VALUES
(11, 1, 'ATAQUE', 'Pistola Agua', 7, 10, 40, 'Dispara agua con fuerza.', 25, 0),
(12, 1, 'ATAQUE', 'Rayo Burbuja', 20, 15, 65, 'Dispara burbujas a gran velocidad.', 20, 0),
(13, 1, 'ATAQUE', 'Surf', 35, 20, 90, 'Crea una ola gigante y la lanza.', 15, 0),
(14, 1, 'ATAQUE', 'Hidrobomba', 50, 25, 110, 'Dispara una ráfaga de agua a presión.', 5, 0);

-- PLANTA (3)
INSERT INTO `movimientos` (`ID_MOVIMIENTOS`, `ID_TIPO`, `CLASE_MOVIMIENTO`, `NOM_MOVIMIENTO`, `NIVEL_APRENDIZAJE`, `COSTE_ESTAMINA`, `POTENCIA`, `DECRIP_MOVIMIENTO`, `PP`, `NUM_TURNOS`) VALUES
(15, 3, 'ATAQUE', 'Látigo Cepa', 7, 10, 45, 'Golpea con finas enredaderas.', 25, 0),
(16, 3, 'ESTADO', 'Drenadoras', 12, 15, 0, 'Planta semillas que roban salud.', 10, 4),
(17, 3, 'ATAQUE', 'Hoja Afilada', 22, 15, 55, 'Lanza hojas extremadamente cortantes.', 25, 0),
(18, 3, 'ATAQUE', 'Rayo Solar', 45, 30, 120, 'Absorbe luz y dispara un rayo brutal.', 10, 0);

-- ELÉCTRICO (4)
INSERT INTO `movimientos` (`ID_MOVIMIENTOS`, `ID_TIPO`, `CLASE_MOVIMIENTO`, `NOM_MOVIMIENTO`, `NIVEL_APRENDIZAJE`, `COSTE_ESTAMINA`, `POTENCIA`, `DECRIP_MOVIMIENTO`, `PP`, `NUM_TURNOS`) VALUES
(19, 4, 'ATAQUE', 'Impactrueno', 7, 10, 40, 'Ataque eléctrico de bajo voltaje.', 30, 0),
(20, 4, 'ESTADO', 'Onda Trueno', 15, 12, 0, 'Paraliza al rival con electricidad.', 20, 0),
(21, 4, 'ATAQUE', 'Rayo', 30, 20, 90, 'Fuerte descarga eléctrica.', 15, 0),
(22, 4, 'ATAQUE', 'Trueno', 50, 25, 110, 'Cae un rayo masivo sobre el rival.', 10, 0);

-- VOLADOR (15)
INSERT INTO `movimientos` (`ID_MOVIMIENTOS`, `ID_TIPO`, `CLASE_MOVIMIENTO`, `NOM_MOVIMIENTO`, `NIVEL_APRENDIZAJE`, `COSTE_ESTAMINA`, `POTENCIA`, `DECRIP_MOVIMIENTO`, `PP`, `NUM_TURNOS`) VALUES
(23, 15, 'ATAQUE', 'Picotazo', 1, 5, 35, 'Pica al objetivo.', 35, 0),
(24, 15, 'ATAQUE', 'Tornado', 15, 12, 40, 'Genera un pequeño torbellino.', 35, 0),
(25, 15, 'ATAQUE', 'Ataque Ala', 25, 15, 60, 'Golpea extendiendo las alas.', 35, 0),
(26, 15, 'ATAQUE', 'Vuelo', 40, 20, 90, 'Se eleva y cae en picado.', 15, 0);

-- TIERRA (5) Y ROCA (14)
INSERT INTO `movimientos` (`ID_MOVIMIENTOS`, `ID_TIPO`, `CLASE_MOVIMIENTO`, `NOM_MOVIMIENTO`, `NIVEL_APRENDIZAJE`, `COSTE_ESTAMINA`, `POTENCIA`, `DECRIP_MOVIMIENTO`, `PP`, `NUM_TURNOS`) VALUES
(27, 5, 'ESTADO', 'Ataque Arena', 8, 8, 0, 'Arroja arena para cegar al rival.', 15, 0),
(28, 5, 'ATAQUE', 'Excavar', 25, 15, 80, 'Se entierra y ataca desde abajo.', 10, 0),
(29, 5, 'ATAQUE', 'Terremoto', 45, 25, 100, 'Fuerte sacudida sísmica.', 10, 0),
(30, 14, 'ATAQUE', 'Lanzarrocas', 12, 12, 50, 'Lanza piedras al objetivo.', 15, 0),
(31, 14, 'ATAQUE', 'Avalancha', 35, 20, 75, 'Derrumba rocas sobre el enemigo.', 10, 0);

-- PSÍQUICO (9) Y VENENO (7)
INSERT INTO `movimientos` (`ID_MOVIMIENTOS`, `ID_TIPO`, `CLASE_MOVIMIENTO`, `NOM_MOVIMIENTO`, `NIVEL_APRENDIZAJE`, `COSTE_ESTAMINA`, `POTENCIA`, `DECRIP_MOVIMIENTO`, `PP`, `NUM_TURNOS`) VALUES
(32, 9, 'ATAQUE', 'Confusión', 10, 10, 50, 'Ataque telequinético débil.', 25, 0),
(33, 9, 'ESTADO', 'Hipnosis', 20, 15, 0, 'Duerme al rival por sugestión.', 20, 3),
(34, 9, 'ATAQUE', 'Psíquico', 40, 25, 90, 'Poderosa fuerza mental.', 10, 0),
(35, 7, 'ATAQUE', 'Picotazo Venenoso', 7, 8, 15, 'Pica con un aguijón tóxico.', 35, 0),
(36, 7, 'ATAQUE', 'Ácido', 20, 12, 40, 'Rocía ácido corrosivo.', 30, 0),
(37, 7, 'ESTADO', 'Tóxico', 35, 18, 0, 'Envenena gravemente al rival.', 10, 5);

-- LUCHA (11), HIELO (10), BICHO (6), FANTASMA (8) Y DRAGÓN (12)
INSERT INTO `movimientos` (`ID_MOVIMIENTOS`, `ID_TIPO`, `CLASE_MOVIMIENTO`, `NOM_MOVIMIENTO`, `NIVEL_APRENDIZAJE`, `COSTE_ESTAMINA`, `POTENCIA`, `DECRIP_MOVIMIENTO`, `PP`, `NUM_TURNOS`) VALUES
(38, 11, 'ATAQUE', 'Golpe Kárate', 15, 12, 50, 'Un golpe seco y cortante.', 25, 0),
(39, 11, 'ATAQUE', 'Sumisión', 35, 20, 80, 'Llave de lucha libre temeraria.', 20, 0),
(40, 10, 'ATAQUE', 'Rayo Hielo', 30, 20, 90, 'Rayo de energía helada.', 10, 0),
(41, 10, 'ATAQUE', 'Ventisca', 50, 25, 110, 'Tormenta de nieve brutal.', 5, 0),
(42, 6, 'ATAQUE', 'Chupavidas', 12, 10, 20, 'Absorbe salud del rival.', 15, 0),
(43, 8, 'ESTADO', 'Rayo Confuso', 20, 15, 0, 'Luz siniestra que confunde.', 10, 3),
(44, 8, 'ATAQUE', 'Bola Sombra', 35, 20, 80, 'Masa de sombras arrojadiza.', 15, 0),
(45, 12, 'ATAQUE', 'Furia Dragón', 30, 20, 40, 'Llama mística de dragón.', 10, 0);

--  TU EQUIPO Y SUS EVOLUCIONES DIRECTAS: Pokemons y los movimientos que pueden aprender
INSERT IGNORE INTO `pokedex_movimiento` (`NUM_POKEDEX`, `ID_MOVIMIENTOS`) VALUES
-- Pidgey, Pidgeotto, Pidgeot (Ataque Rápido, Picotazo, Tornado, Vuelo)
(16, 3), (16, 23), (16, 24), (17, 3), (17, 23), (17, 24), (17, 26), (18, 3), (18, 23), (18, 24), (18, 26),
-- Zubat, Golbat (Chupavidas, Picotazo, Rayo Confuso, Ataque Ala)
(41, 42), (41, 23), (41, 43), (42, 42), (42, 25), (42, 43), (42, 37),
-- Slowpoke, Slowbro (Pistola Agua, Confusión, Surf, Psíquico)
(79, 11), (79, 32), (79, 13), (80, 11), (80, 32), (80, 13), (80, 34),
-- Lapras (Canto, Pistola Agua, Rayo Hielo, Surf)
(131, 4), (131, 11), (131, 40), (131, 13),
-- Eevee (Placaje, Arañazo, Ataque Rápido)
(133, 1), (133, 2), (133, 3),
-- Dratini, Dragonair, Dragonite (Onda Trueno, Tornado, Furia Dragón, Hiperrayo)
(147, 20), (147, 24), (147, 45), (148, 20), (148, 24), (148, 45), (149, 20), (149, 26), (149, 45), (149, 5);

-- LOS INICIALES Y SUS LÍNEAS
INSERT IGNORE INTO `pokedex_movimiento` (`NUM_POKEDEX`, `ID_MOVIMIENTOS`) VALUES
-- Bulbasaur, Ivysaur, Venusaur
(1, 1), (1, 15), (1, 16), (2, 1), (2, 15), (2, 17), (3, 1), (3, 17), (3, 18), (3, 5),
-- Charmander, Charmeleon, Charizard
(4, 2), (4, 7), (4, 8), (5, 2), (5, 7), (5, 9), (6, 2), (6, 9), (6, 10), (6, 26),
-- Squirtle, Wartortle, Blastoise
(7, 1), (7, 11), (7, 12), (8, 1), (8, 11), (8, 12), (9, 1), (9, 13), (9, 14), (9, 5);

-- EL RESTO DE LA PRIMERA GENERACIÓN 
INSERT IGNORE INTO `pokedex_movimiento` (`NUM_POKEDEX`, `ID_MOVIMIENTOS`) VALUES
-- Bichos iniciales: Caterpie, Metapod, Butterfree / Weedle, Kakuna, Beedrill
(10, 1), (10, 42), (11, 1), (12, 32), (12, 24), (13, 35), (13, 42), (14, 35), (15, 35), (15, 24),
-- Roedores y pájaros: Rattata, Raticate / Spearow, Fearow
(19, 1), (19, 3), (20, 1), (20, 3), (20, 5), (21, 23), (21, 25), (22, 23), (22, 26),
-- Serpientes: Ekans, Arbok
(23, 35), (23, 36), (23, 28), (24, 35), (24, 36), (24, 28), (24, 37),
-- Eléctricos: Pikachu, Raichu
(25, 3), (25, 19), (25, 20), (25, 21), (26, 3), (26, 20), (26, 21), (26, 22),
-- Tierra: Sandshrew, Sandslash / Diglett, Dugtrio
(27, 2), (27, 27), (27, 28), (28, 2), (28, 28), (28, 29), (50, 2), (50, 27), (50, 28), (51, 28), (51, 29),
-- Nidorans (Hembra y Macho) y evoluciones
(29, 1), (29, 35), (30, 1), (30, 36), (31, 36), (31, 29), (32, 1), (32, 35), (33, 1), (33, 36), (34, 36), (34, 29),
-- Hadas/Normales: Clefairy, Clefable, Vulpix, Ninetales, Jigglypuff, Wigglytuff
(35, 4), (35, 1), (36, 4), (36, 5), (37, 7), (37, 8), (38, 9), (38, 10), (39, 4), (39, 1), (40, 4), (40, 5),
-- Plantas: Oddish, Gloom, Vileplume / Paras, Parasect / Venonat, Venomoth / Bellsprout, Weepinbell, Victreebel
(43, 36), (43, 16), (44, 36), (44, 18), (45, 37), (45, 18), (46, 42), (46, 16), (47, 42), (47, 18), 
(48, 1), (48, 32), (49, 32), (49, 34), (69, 15), (69, 36), (70, 17), (70, 36), (71, 17), (71, 18),
-- Gatos, patos y monos: Meowth, Persian / Psyduck, Golduck / Mankey, Primeape
(52, 2), (52, 42), (53, 2), (53, 3), (54, 2), (54, 11), (54, 32), (55, 11), (55, 34), (55, 13), 
(56, 2), (56, 38), (57, 38), (57, 39),
-- Perros y Renacuajos: Growlithe, Arcanine / Poliwag, Poliwhirl, Poliwrath
(58, 7), (58, 8), (58, 3), (59, 9), (59, 10), (59, 5), (60, 11), (60, 12), (61, 11), (61, 13), (62, 13), (62, 39),
-- Abra, Kadabra, Alakazam (Magia pura)
(63, 32), (63, 20), (64, 32), (64, 33), (64, 34), (65, 33), (65, 34), (65, 6),
-- Machop, Machoke, Machamp
(66, 38), (66, 28), (67, 38), (67, 39), (68, 38), (68, 39), (68, 29),
-- Tentacool, Tentacruel
(72, 12), (72, 36), (73, 13), (73, 37), (73, 40),
-- Rocas: Geodude, Graveler, Golem / Onix / Rhyhorn, Rhydon
(74, 1), (74, 30), (75, 30), (75, 29), (76, 30), (76, 31), (76, 29), (95, 1), (95, 30), (95, 29), 
(111, 28), (111, 30), (112, 29), (112, 31),
-- Fuego y Magnéticos: Ponyta, Rapidash / Magnemite, Magneton
(77, 7), (77, 8), (78, 9), (78, 10), (81, 1), (81, 19), (81, 20), (82, 19), (82, 21), (82, 22),
-- Aves Variadas: Farfetch'd, Doduo, Dodrio
(83, 23), (83, 25), (84, 23), (84, 3), (85, 23), (85, 3), (85, 26),
-- Agua Hielo: Seel, Dewgong / Shellder, Cloyster
(86, 1), (86, 12), (87, 13), (87, 40), (90, 1), (90, 12), (91, 13), (91, 40), (91, 41),
-- Tóxicos: Grimer, Muk / Koffing, Weezing
(88, 1), (88, 36), (89, 36), (89, 37), (109, 1), (109, 36), (110, 36), (110, 37),
-- Fantasmas y Psíquicos: Gastly, Haunter, Gengar / Drowzee, Hypno
(92, 43), (92, 33), (93, 43), (93, 44), (94, 43), (94, 44), (94, 34), (96, 32), (96, 33), (97, 32), (97, 33), (97, 34),
-- Agua: Krabby, Kingler / Horsea, Seadra / Goldeen, Seaking / Staryu, Starmie / Magikarp, Gyarados
(98, 12), (98, 28), (99, 13), (99, 5), (116, 11), (116, 12), (117, 13), (117, 14), (118, 23), (118, 12), (119, 23), (119, 13),
(120, 1), (120, 12), (120, 6), (121, 13), (121, 14), (121, 40), (129, 1), (130, 13), (130, 14), (130, 5), (130, 45),
-- Raros: Voltorb, Electrode / Exeggcute, Exeggutor / Cubone, Marowak / Lickitung / Chansey / Tangela / Kangaskhan
(100, 1), (100, 19), (101, 20), (101, 21), (102, 16), (102, 32), (103, 18), (103, 34), (104, 28), (104, 30), (105, 29), (105, 31),
(108, 1), (108, 3), (113, 1), (113, 6), (113, 4), (114, 15), (114, 16), (115, 1), (115, 5),
-- Luchadores y Finales: Hitmonlee, Hitmonchan / Mr. Mime / Scyther / Jynx / Electabuzz / Magmar / Pinsir / Tauros
(106, 38), (106, 39), (107, 38), (107, 40), (122, 32), (122, 34), (123, 3), (123, 25), (124, 40), (124, 34), 
(125, 21), (125, 22), (126, 9), (126, 10), (127, 38), (127, 28), (128, 1), (128, 29), (128, 5),
-- Evoluciones Eevee: Vaporeon, Jolteon, Flareon
(134, 11), (134, 13), (134, 14), (135, 19), (135, 20), (135, 21), (136, 7), (136, 8), (136, 9),
-- Fósiles y Raros: Porygon / Omanyte, Omastar / Kabuto, Kabutops / Aerodactyl / Snorlax / Ditto
(137, 1), (137, 20), (137, 6), (138, 11), (138, 30), (139, 13), (139, 31), (140, 2), (140, 11), (141, 28), (141, 13), 
(142, 25), (142, 26), (142, 5), (143, 1), (143, 5), (143, 6), (132, 1),
-- Aves Legendarias y Mewtwo/Mew
(144, 26), (144, 40), (144, 41), (145, 26), (145, 21), (145, 22), (146, 26), (146, 9), (146, 10),
(150, 34), (150, 6), (150, 5), (150, 44), (151, 34), (151, 6), (151, 14), (151, 10);

INSERT IGNORE INTO pokedex_movimiento (NUM_POKEDEX, ID_MOVIMIENTOS)
SELECT NUM_POKEDEX, 1 FROM pokedex;

INSERT INTO `pokedex_tipo` (`NUM_POKEDEX`, `ID_TIPO`) VALUES 
(1,3),(2,3),(3,3),(43,3),(44,3),(45,3),(69,3),(70,3),(71,3),(102,3),(103,3),(114,3);
INSERT INTO `pokedex_tipo` (`NUM_POKEDEX`, `ID_TIPO`) VALUES 
(4,2),(5,2),(6,2),(37,2),(38,2),(58,2),(59,2),(77,2),(78,2),(126,2),(136,2),(146,2);
INSERT INTO `pokedex_tipo` (`NUM_POKEDEX`, `ID_TIPO`) VALUES 
(7,1),(8,1),(9,1),(54,1),(55,1),(60,1),(61,1),(62,1),(72,1),(73,1),(79,1),(80,1),(86,1),(87,1),(90,1),(91,1),(98,1),(99,1),(116,1),(117,1),(118,1),(119,1),(120,1),(121,1),(129,1),(130,1),(131,1),(134,1);
INSERT INTO `pokedex_tipo` (`NUM_POKEDEX`, `ID_TIPO`) VALUES 
(10,6),(11,6),(12,6),(13,6),(14,6),(15,6),(46,6),(47,6),(48,6),(49,6),(123,6),(127,6);
INSERT INTO `pokedex_tipo` (`NUM_POKEDEX`, `ID_TIPO`) VALUES 
(16,13),(17,13),(18,13),(19,13),(20,13),(21,13),(22,13),(39,13),(40,13),(52,13),(53,13),(83,13),(84,13),(85,13),(108,13),(113,13),(115,13),(128,13),(132,13),(133,13),(137,13),(143,13);
INSERT INTO `pokedex_tipo` (`NUM_POKEDEX`, `ID_TIPO`) VALUES 
(25,4),(26,4),(81,4),(82,4),(100,4),(101,4),(125,4),(135,4),(145,4);
INSERT INTO `pokedex_tipo` (`NUM_POKEDEX`, `ID_TIPO`) VALUES 
(23,7),(24,7),(29,7),(30,7),(31,7),(32,7),(33,7),(34,7),(88,7),(89,7),(109,7),(110,7);
INSERT INTO `pokedex_tipo` (`NUM_POKEDEX`, `ID_TIPO`) VALUES 
(63,9),(64,9),(65,9),(96,9),(97,9),(122,9),(150,9),(151,9);
INSERT INTO `pokedex_tipo` (`NUM_POKEDEX`, `ID_TIPO`) VALUES 
(56,11),(57,11),(66,11),(67,11),(68,11),(106,11),(107,11);
INSERT INTO `pokedex_tipo` (`NUM_POKEDEX`, `ID_TIPO`) VALUES 
(92,8),(93,8),(94,8);
INSERT INTO `pokedex_tipo` (`NUM_POKEDEX`, `ID_TIPO`) VALUES 
(124,10),(144,10);
INSERT INTO `pokedex_tipo` (`NUM_POKEDEX`, `ID_TIPO`) VALUES 
(147,12),(148,12),(149,12);
INSERT INTO `pokedex_tipo` (`NUM_POKEDEX`, `ID_TIPO`) VALUES 
(27,5),(28,5),(50,5),(51,5),(74,14),(75,14),(76,14),(95,14),(104,5),(105,5),(111,5),(112,5);

INSERT IGNORE INTO `tipos_pokemon` (`ID_TIPO`, `NOM_TIPO`) VALUES
(1, 'AGUA'), (2, 'FUEGO'), (3, 'PLANTA'), (4, 'ELECTRICO'),
(5, 'TIERRA'), (6, 'BICHO'), (7, 'VENENO'), (8, 'FANTASMA'),
(9, 'PSIQUICO'), (10, 'HIELO'), (11, 'LUCHA'), (12, 'DRAGON'),
(13, 'NORMAL'), (14, 'ROCA'), (15, 'VOLADOR'); 
--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `casino`
--
ALTER TABLE `casino`
  ADD PRIMARY KEY (`ID_ENTRENADOR`,`ID_PARTIDA`);

--
-- Indices de la tabla `combate`
--
ALTER TABLE `combate`
  ADD PRIMARY KEY (`ID_COMBATE`),
  ADD KEY `FK_ENTRENADOR_1` (`ID_ENTRENADOR_1`),
  ADD KEY `FK_ENTRENADOR_2` (`ID_ENTRENADOR_2`),
  ADD KEY `COMBATE_ENTRE_FK` (`ID_GANADOR`);

--
-- Indices de la tabla `crianza`
--
ALTER TABLE `crianza`
  ADD PRIMARY KEY (`ID_POKE_CRIA`),
  ADD KEY `CRIANZA_PADRE_FK` (`ID_POKE_PADRE`),
  ADD KEY `CRIANZA_MADRE_FK` (`ID_POKE_MADRE`);

--
-- Indices de la tabla `efectividad`
--
ALTER TABLE `efectividad`
  ADD PRIMARY KEY (`ID_TIPO_ATACANTE`,`ID_TIPO_DEFENSOR`),
  ADD KEY `EFECT_DEF_FK` (`ID_TIPO_DEFENSOR`);

--
-- Indices de la tabla `entrenador`
--
ALTER TABLE `entrenador`
  ADD PRIMARY KEY (`ID_ENTRENADOR`);

--
-- Indices de la tabla `liga`
--
ALTER TABLE `liga`
  ADD PRIMARY KEY (`ID_INTENTO`,`ID_ENTRENADOR`),
  ADD KEY `LIGA_ENTRE_FK` (`ID_ENTRENADOR`);

--
-- Indices de la tabla `mochila`
--
ALTER TABLE `mochila`
  ADD PRIMARY KEY (`ID_OBJETO`,`ID_ENTRENADOR`),
  ADD KEY `MOCHILA_ENT_FK` (`ID_ENTRENADOR`);

--
-- Indices de la tabla `movimientos`
--
ALTER TABLE `movimientos`
  ADD PRIMARY KEY (`ID_MOVIMIENTOS`),
  ADD KEY `MOVIMIENTO_TIPO_FK` (`ID_TIPO`);

--
-- Indices de la tabla `objeto`
--
ALTER TABLE `objeto`
  ADD PRIMARY KEY (`ID_OBJETO`);

--
-- Indices de la tabla `pokedex`
--
ALTER TABLE `pokedex`
  ADD PRIMARY KEY (`NUM_POKEDEX`);

--
-- Indices de la tabla `pokedex_tipo`
--
ALTER TABLE `pokedex_tipo`
  ADD PRIMARY KEY (`ID_TIPO`,`NUM_POKEDEX`),
  ADD KEY `FK_POKEDEX_RELACION` (`NUM_POKEDEX`);

--
-- Indices de la tabla `pokemon`
--
ALTER TABLE `pokemon`
  ADD PRIMARY KEY (`ID_POKEMON`),
  ADD KEY `FK_POKE_ENTRENADOR` (`ID_ENTRENADOR`),
  ADD KEY `FK_POKE_POKEDEX` (`NUM_POKEDEX`);

--
-- Indices de la tabla `pokemon_movimiento`
--
ALTER TABLE `pokemon_movimiento`
  ADD PRIMARY KEY (`ID_MOVIMIENTOS`,`ID_POKEMON`),
  ADD KEY `MOV_POKEMON_FK` (`ID_POKEMON`);

--
-- Indices de la tabla `tipos_pokemon`
--
ALTER TABLE `tipos_pokemon`
  ADD PRIMARY KEY (`ID_TIPO`);

--
-- Indices de la tabla `turno_combate`
--
ALTER TABLE `turno_combate`
  ADD PRIMARY KEY (`ID_COMBATE`,`NUM_TURNO`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `entrenador`
--
ALTER TABLE `entrenador`
  MODIFY `ID_ENTRENADOR` int(3) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `pokemon`
--
ALTER TABLE `pokemon`
  MODIFY `ID_POKEMON` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `casino`
--
ALTER TABLE `casino`
  ADD CONSTRAINT `CASINO_ENTRE_FK` FOREIGN KEY (`ID_ENTRENADOR`) REFERENCES `entrenador` (`ID_ENTRENADOR`);

--
-- Filtros para la tabla `combate`
--
ALTER TABLE `combate`
  ADD CONSTRAINT `COMBATE_ENTRE_FK` FOREIGN KEY (`ID_GANADOR`) REFERENCES `entrenador` (`ID_ENTRENADOR`),
  ADD CONSTRAINT `FK_ENTRENADOR_1` FOREIGN KEY (`ID_ENTRENADOR_1`) REFERENCES `entrenador` (`ID_ENTRENADOR`),
  ADD CONSTRAINT `FK_ENTRENADOR_2` FOREIGN KEY (`ID_ENTRENADOR_2`) REFERENCES `entrenador` (`ID_ENTRENADOR`);

--
-- Filtros para la tabla `crianza`
--
ALTER TABLE `crianza`
  ADD CONSTRAINT `CRIANZA_CRIA_FK` FOREIGN KEY (`ID_POKE_CRIA`) REFERENCES `pokemon` (`ID_POKEMON`),
  ADD CONSTRAINT `CRIANZA_MADRE_FK` FOREIGN KEY (`ID_POKE_MADRE`) REFERENCES `pokemon` (`ID_POKEMON`),
  ADD CONSTRAINT `CRIANZA_PADRE_FK` FOREIGN KEY (`ID_POKE_PADRE`) REFERENCES `pokemon` (`ID_POKEMON`);

--
-- Filtros para la tabla `efectividad`
--
ALTER TABLE `efectividad`
  ADD CONSTRAINT `EFECT_ATAC_FK` FOREIGN KEY (`ID_TIPO_ATACANTE`) REFERENCES `tipos_pokemon` (`ID_TIPO`),
  ADD CONSTRAINT `EFECT_DEF_FK` FOREIGN KEY (`ID_TIPO_DEFENSOR`) REFERENCES `tipos_pokemon` (`ID_TIPO`);

--
-- Filtros para la tabla `liga`
--
ALTER TABLE `liga`
  ADD CONSTRAINT `LIGA_ENTRE_FK` FOREIGN KEY (`ID_ENTRENADOR`) REFERENCES `entrenador` (`ID_ENTRENADOR`);

--
-- Filtros para la tabla `mochila`
--
ALTER TABLE `mochila`
  ADD CONSTRAINT `MOCHILA_ENT_FK` FOREIGN KEY (`ID_ENTRENADOR`) REFERENCES `entrenador` (`ID_ENTRENADOR`),
  ADD CONSTRAINT `MOCHILA_OBJ_FK` FOREIGN KEY (`ID_OBJETO`) REFERENCES `objeto` (`ID_OBJETO`);

--
-- Filtros para la tabla `movimientos`
--
ALTER TABLE `movimientos`
  ADD CONSTRAINT `MOVIMIENTO_TIPO_FK` FOREIGN KEY (`ID_TIPO`) REFERENCES `tipos_pokemon` (`ID_TIPO`);

--
-- Filtros para la tabla `pokedex_tipo`
--
ALTER TABLE `pokedex_tipo`
  ADD CONSTRAINT `FK_POKEDEX_RELACION` FOREIGN KEY (`NUM_POKEDEX`) REFERENCES `pokedex` (`NUM_POKEDEX`),
  ADD CONSTRAINT `FK_TIPO_RELACION` FOREIGN KEY (`ID_TIPO`) REFERENCES `tipos_pokemon` (`ID_TIPO`);

--
-- Filtros para la tabla `pokemon`
--
ALTER TABLE `pokemon`
  ADD CONSTRAINT `FK_POKE_ENTRENADOR` FOREIGN KEY (`ID_ENTRENADOR`) REFERENCES `entrenador` (`ID_ENTRENADOR`),
  ADD CONSTRAINT `FK_POKE_POKEDEX` FOREIGN KEY (`NUM_POKEDEX`) REFERENCES `pokedex` (`NUM_POKEDEX`);

--
-- Filtros para la tabla `pokemon_movimiento`
--
ALTER TABLE `pokemon_movimiento`
  ADD CONSTRAINT `MOV_POKEMON_FK` FOREIGN KEY (`ID_POKEMON`) REFERENCES `pokemon` (`ID_POKEMON`),
  ADD CONSTRAINT `POKE_MOV_FK` FOREIGN KEY (`ID_MOVIMIENTOS`) REFERENCES `movimientos` (`ID_MOVIMIENTOS`);

--
-- Filtros para la tabla `turno_combate`
--
ALTER TABLE `turno_combate`
  ADD CONSTRAINT `TURNO_COMBATE_FK` FOREIGN KEY (`ID_COMBATE`) REFERENCES `combate` (`ID_COMBATE`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

