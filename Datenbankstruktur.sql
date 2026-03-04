-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 24, 2025 at 11:48 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tafel_logging_leer`
--

-- --------------------------------------------------------

--
-- Table structure for table `altersgruppen`
--

CREATE TABLE `altersgruppen` (
  `id` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  `startAlter` int(11) NOT NULL,
  `endAlter` int(11) NOT NULL,
  `ergebnis` int(11) NOT NULL,
  `jahresergebnis` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ausgabegruppe`
--

CREATE TABLE `ausgabegruppe` (
  `ausgabegruppeId` int(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `aktiv` tinyint(1) DEFAULT NULL,
  `anzeigeFarbe` varchar(7) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `ausgabegruppe`
--

INSERT INTO `ausgabegruppe` (`ausgabegruppeId`, `name`, `aktiv`, `anzeigeFarbe`) VALUES
(240, 'purpur', 0, '#800080'),
(242, 'braun', 0, '#A52A2A'),
(243, 'grün', 0, '#008000'),
(244, 'olivgrün', 0, '#808000'),
(245, 'dunkelblau', 0, '#00008B'),
(246, 'grünblau', 0, '#008080'),
(247, 'grau', 0, '#808080'),
(248, 'silber', 0, '#C0C0C0'),
(249, 'rot', 0, '#FF0000'),
(250, 'limone', 0, '#00FF00'),
(251, 'gelb', 0, '#FFFF00'),
(252, 'blau', 0, '#0000FF'),
(253, 'Fuchsie', 0, '#FF00FF'),
(255, 'weiß', 0, '#FFFFFF'),
(256, 'dollargrün', 0, '#006400'),
(257, 'himmelblau', 0, '#87CEEB'),
(258, 'creme', 0, '#FFFDD0'),
(259, 'mittelgrau', 0, '#A9A9A9'),
(300, 'schwarz', 0, '#000000'),
(301, 'auquamarin', 0, '#7FFFD4'),
(304, 'Neongruen', 0, '#00FF00'),
(305, 'Orange', 0, '#FF9966');

-- --------------------------------------------------------

--
-- Table structure for table `ausgabegruppe_ausgabetagzeit`
--

CREATE TABLE `ausgabegruppe_ausgabetagzeit` (
  `ausgabeTagZeitId` int(10) DEFAULT NULL,
  `ausgabegruppeId` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ausgabetagzeit`
--

CREATE TABLE `ausgabetagzeit` (
  `ausgabeTagZeitId` int(10) NOT NULL,
  `ausgabeTag` int(10) NOT NULL,
  `startZeit` time DEFAULT NULL,
  `endZeit` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `berechtigung`
--

CREATE TABLE `berechtigung` (
  `berechtigungId` int(10) NOT NULL,
  `name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `berechtigung`
--

INSERT INTO `berechtigung` (`berechtigungId`, `name`) VALUES
(1, 'nicht festgelegt'),
(46, 'darf vor, ohne zu warten'),
(47, 'darf vor in Begleitung von Betreuer'),
(48, 'darf Sitzplatz beanspruchen'),
(49, 'keine');

-- --------------------------------------------------------

--
-- Table structure for table `bescheid`
--

CREATE TABLE `bescheid` (
  `bescheidId` int(10) NOT NULL,
  `personId` int(10) DEFAULT NULL,
  `bescheidartId` int(10) DEFAULT NULL,
  `gueltigAb` date DEFAULT NULL,
  `gueltigBis` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `bescheidart`
--

CREATE TABLE `bescheidart` (
  `bescheidArtId` int(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `aktiv` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `bescheidart`
--

INSERT INTO `bescheidart` (`bescheidArtId`, `name`, `aktiv`) VALUES
(4, 'ALG', 1),
(5, 'ALG II', 1),
(6, 'AsylbLg', 1),
(7, 'Ausbildung', 1),
(8, 'BAföG', 1),
(9, 'Duldung', 1),
(10, 'EU-Rente', 1),
(11, 'Geringverdiener', 1),
(13, 'Krankengeld', 1),
(14, 'SGB', 1),
(15, 'SGB II', 1),
(16, 'SGB III', 1),
(17, 'SGB X', 1),
(18, 'SGB XII', 1),
(19, 'WoGG', 1),
(20, 'Rente', 1),
(21, 'Witwenrente', 1),
(22, 'Altersrente', 1),
(23, 'Schulbescheinigung', 1),
(24, 'SKF', 1),
(25, 'SKM', 1),
(26, 'Diakonie', 1),
(27, 'Caritas', 1),
(28, 'Unterhalt', 1),
(29, 'Grundsicherung', 1),
(30, 'Waisenrente', 1),
(31, 'Kindergeld', 1),
(32, 'LingenPass', 1),
(33, 'Elterngeld', 1);

-- --------------------------------------------------------

--
-- Table structure for table `bescheidstatistik`
--

CREATE TABLE `bescheidstatistik` (
  `id` int(11) NOT NULL,
  `bescheidartName` varchar(255) NOT NULL,
  `anzahlPersonen` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bescheidstatistik`
--

INSERT INTO `bescheidstatistik` (`id`, `bescheidartName`, `anzahlPersonen`) VALUES
(50, 'ALG', 3),
(51, 'Ausbildung', 9),
(52, 'BAföG', 18),
(53, 'Duldung', 2),
(54, 'EU-Rente', 9),
(55, 'Krankengeld', 3),
(56, 'LingenPass', 3),
(64, 'Unbekannt', 0);

-- --------------------------------------------------------

--
-- Table structure for table `deleted_MemberOfTheFamily`
--

CREATE TABLE `deleted_MemberOfTheFamily` (
  `Id` int(11) NOT NULL,
  `householdId` int(11) DEFAULT NULL,
  `firstName` varchar(30) DEFAULT NULL,
  `surname` varchar(30) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `reasonDelete` varchar(255) DEFAULT NULL,
  `deletedOn` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `einkauf`
--

CREATE TABLE `einkauf` (
  `einkaufId` int(10) NOT NULL,
  `warentyp` int(10) DEFAULT NULL,
  `storniertAm` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `stornoText` text DEFAULT NULL,
  `buchungstext` text DEFAULT NULL,
  `kunde` int(10) DEFAULT NULL,
  `person` int(10) DEFAULT NULL,
  `erfassungsZeit` timestamp NULL DEFAULT NULL,
  `summeEinkauf` float DEFAULT NULL,
  `summeZahlung` float DEFAULT NULL,
  `beiVerteilstelle` int(10) DEFAULT NULL,
  `anzahlKinder` int(11) DEFAULT NULL,
  `anzahlErwachsene` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `einstellungen`
--

CREATE TABLE `einstellungen` (
  `initialID` int(11) NOT NULL DEFAULT 0,
  `kundenarchivieren` int(11) DEFAULT NULL,
  `gebuehrErwachsener` int(11) DEFAULT NULL,
  `alterErwachsener` int(11) DEFAULT NULL,
  `alterBescheid` int(11) DEFAULT NULL,
  `bescheidBenoetigt` tinyint(1) DEFAULT NULL,
  `datenschutzerklaerung` tinyint(1) DEFAULT NULL,
  `verteilstellenzugehoerigkeit` tinyint(1) DEFAULT NULL,
  `tafel_server_host` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `einstellungen`
--

INSERT INTO `einstellungen` (`initialID`, `kundenarchivieren`, `gebuehrErwachsener`, `alterErwachsener`, `alterBescheid`, `bescheidBenoetigt`, `datenschutzerklaerung`, `verteilstellenzugehoerigkeit`, `tafel_server_host`) VALUES
(0, 0, 0, 18, 18, 0, 1, 0, 'https://tafel.lin@hs-osnabrueck.de');

-- --------------------------------------------------------

--
-- Table structure for table `familienmitglied`
--

CREATE TABLE `familienmitglied` (
  `personId` int(10) NOT NULL,
  `haushaltId` int(10) DEFAULT NULL,
  `anredeId` int(10) DEFAULT NULL,
  `genderId` int(10) DEFAULT NULL,
  `vName` varchar(30) DEFAULT NULL,
  `nName` varchar(30) DEFAULT NULL,
  `gDatum` date DEFAULT NULL,
  `bemerkung` text DEFAULT NULL,
  `haushaltsVorstand` tinyint(1) DEFAULT NULL,
  `einkaufsBerechtigt` tinyint(1) DEFAULT NULL,
  `gebuehrenBefreiung` tinyint(1) DEFAULT NULL,
  `nation` int(10) DEFAULT NULL,
  `berechtigungId` int(10) DEFAULT NULL,
  `aufAusweis` tinyint(1) DEFAULT NULL,
  `dseSubmitted` tinyint(1) DEFAULT NULL,
  `hinzugefuegtAm` timestamp NULL DEFAULT current_timestamp(),
  `geaendertAm` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `guthabenstatistik`
--

CREATE TABLE `guthabenstatistik` (
  `kunde` varchar(255) DEFAULT NULL,
  `einkaufId` int(11) NOT NULL,
  `warentyp` varchar(255) NOT NULL,
  `saldo` double NOT NULL,
  `summeEinkauf` double NOT NULL,
  `summeZahlung` double NOT NULL,
  `anzahlKinder` int(11) NOT NULL,
  `anzahlErwachsene` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `haushalt`
--

CREATE TABLE `haushalt` (
  `kundennummer` int(10) NOT NULL,
  `strasse` varchar(30) DEFAULT NULL,
  `hausnummer` varchar(10) DEFAULT NULL,
  `plz` int(11) DEFAULT NULL,
  `telefonnummer` varchar(20) DEFAULT NULL,
  `mobilnummer` varchar(15) DEFAULT NULL,
  `bemerkung` text DEFAULT NULL,
  `kundeSeit` date DEFAULT NULL,
  `saldo` float DEFAULT NULL,
  `verteilstellenId` int(10) DEFAULT NULL,
  `istArchiviert` tinyint(1) DEFAULT NULL,
  `istGesperrt` tinyint(1) DEFAULT NULL,
  `ausgabeGruppeId` int(10) DEFAULT NULL,
  `belieferung` tinyint(1) DEFAULT NULL,
  `datenschutzerklaerung` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `herkunftstatistik`
--

CREATE TABLE `herkunftstatistik` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `ort` varchar(255) NOT NULL,
  `hausnummer` varchar(50) NOT NULL,
  `plz` varchar(10) NOT NULL,
  `anzahlHaushalte` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `jahresergebnisse`
--

CREATE TABLE `jahresergebnisse` (
  `year` int(11) NOT NULL,
  `jahresergebnis` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `jahresuebersichtstatistik`
--

CREATE TABLE `jahresuebersichtstatistik` (
  `monat` varchar(20) DEFAULT NULL,
  `anzahlPersonen` int(11) DEFAULT NULL,
  `gesamtUmsatzHaushalt` decimal(10,2) DEFAULT NULL,
  `gesamtUmsatzEinkauf` decimal(10,2) DEFAULT NULL,
  `neuzugaenge` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jahresuebersichtstatistik`
--

INSERT INTO `jahresuebersichtstatistik` (`monat`, `anzahlPersonen`, `gesamtUmsatzHaushalt`, `gesamtUmsatzEinkauf`, `neuzugaenge`) VALUES
('March', 15, 310.00, 33.00, 2),
('July', 15, 385.00, 5.00, 5),
('August', 15, 450.00, 0.00, 2),
('September', 15, 600.00, 0.00, 6),
('March', 16, 330.00, 35.00, 2),
('July', 16, 410.00, 5.00, 5),
('August', 16, 480.00, 0.00, 2),
('September', 16, 640.00, 0.00, 6),
('October', 0, 0.00, 0.00, 1),
('March', 17, 350.00, 37.00, 2),
('July', 17, 435.00, 5.00, 5),
('August', 17, 510.00, 0.00, 2),
('September', 17, 680.00, 0.00, 6),
('October', 17, 0.00, 0.00, 2);

-- --------------------------------------------------------

--
-- Table structure for table `kundeninfos`
--

CREATE TABLE `kundeninfos` (
  `kdNr` varchar(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `strasse` varchar(100) DEFAULT NULL,
  `plz` varchar(10) DEFAULT NULL,
  `ort` varchar(100) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `nation`
--

CREATE TABLE `nation` (
  `nationId` int(10) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `nationalitaet` varchar(50) DEFAULT NULL,
  `aktiv` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `nation`
--

INSERT INTO `nation` (`nationId`, `name`, `nationalitaet`, `aktiv`) VALUES
(1, 'Unbekannt', 'unbekannt', 1),
(51, 'Deutschland', 'deutsch', 1),
(52, 'Großbritannien', 'britisch', 1),
(53, 'Vereinigte Staaten Vereinigte Staaten von Amerika', 'amerikanisch', 1),
(54, 'Türkei', 'türkisch', 1),
(55, 'Ägypten', 'ägyptisch', 1),
(56, 'Äquatorialguinea', 'äquatorialguineisch', 0),
(57, 'Äthiopien', 'äthiopisch', 0),
(58, 'Österreich', 'österreichisch', 1),
(59, 'Afghanistan', 'afghanisch', 1),
(60, 'Albanien', 'albanisch', 1),
(61, 'Algerien', 'algerisch', 1),
(62, 'Andorra', 'andorranisch', 0),
(63, 'Angola', 'angolanisch', 0),
(64, 'Antigua und Barbuda', 'antiguanisch', 0),
(65, 'Argentinien', 'argentinisch', 0),
(66, 'Armenien', 'armenisch', 0),
(67, 'Aserbaidschan', 'aserbaidschanisch', 0),
(68, 'Australien', 'australisch', 0),
(69, 'Bahamas', 'bahamaisch', 0),
(70, 'Bahrain', 'bahrainisch', 0),
(71, 'Bangladesch', 'bangladeschisch', 0),
(72, 'Barbados', 'barbadisch', 1),
(73, 'Belarus', 'belarussisch', 1),
(74, 'Belgien', 'belgisch', 1),
(75, 'Belize', 'belizisch', 0),
(76, 'Benin', 'beninisch', 1),
(77, 'Bhutan', 'bhutanisch', 0),
(78, 'Bolivien', 'bolivianisch', 1),
(79, 'Bosnien und Herzegowina', 'bosnischherzegowinisch', 1),
(80, 'Botsuana', 'botsuanisch', 0),
(81, 'Brasilien', 'brasilianisch', 1),
(82, 'Bulgarien', 'bulgarisch', 1),
(83, 'Burkina Faso', 'burkinisch', 0),
(84, 'Burundi', 'burundisch', 0),
(85, 'Chile', 'chilenisch', 1),
(86, 'China', 'chinesisch', 1),
(87, 'Costa Rica', 'costa-ricanisch', 1),
(88, 'Côte d\'Ivoire', 'ivorisch', 0),
(89, 'Dänemark', 'dänisch', 1),
(90, 'Darussalam', 'bruneiisch', 0),
(91, 'Dominica', 'dominicanisch', 0),
(92, 'Dominikanische Republik', 'dominikanisch', 1),
(93, 'Dschibuti', 'dschibutisch', 1),
(94, 'Ecuador', 'ecuadorianisch', 1),
(95, 'El Salvador', 'salvadorianisch', 0),
(96, 'Eritrea', 'eritreisch', 1),
(97, 'Estland', 'estnisch', 1),
(98, 'Fidschi-Inseln', 'fidschianisch', 1),
(99, 'Finnland', 'finnisch', 1),
(100, 'Frankreich', 'französisch', 1),
(101, 'Gabun', 'gabunisch', 0),
(102, 'Gambia', 'gambisch', 1),
(103, 'Georgien', 'georgisch', 1),
(104, 'Ghana', 'ghanaisch', 1),
(105, 'Grenada', 'grenadisch', 0),
(106, 'Griechenland', 'griechisch', 1),
(107, 'Guatemala', 'guatemaltekisch', 1),
(108, 'Guinea', 'guineisch', 1),
(109, 'Guinea-Bissau', 'guinea-bissauisch', 0),
(110, 'Guyana', 'guyanisch', 0),
(111, 'Haiti', 'haitianisch', 1),
(112, 'Honduras', 'honduranisch', 1),
(113, 'Indien', 'indisch', 1),
(114, 'Indonesien', 'indonesisch', 1),
(115, 'Irak', 'irakisch', 1),
(116, 'Iran', 'iranisch', 1),
(117, 'Irland', 'irisch', 1),
(118, 'Island', 'isländisch', 1),
(119, 'Israel', 'israelisch', 1),
(120, 'Italien', 'italienisch', 1),
(121, 'Jamaika', 'jamaikanisch', 1),
(122, 'Japan', 'japanisch', 1),
(123, 'Jemen', 'jemenitisch', 1),
(124, 'Jordanien', 'jordanisch', 1),
(125, 'Kambodscha', 'kambodschanisch', 1),
(126, 'Kamerun', 'kamerunisch', 1),
(127, 'Kanada', 'kanadisch', 1),
(128, 'Kap Verde', 'kap-verdisch', 0),
(129, 'Kasachstan', 'kasachisch', 1),
(130, 'Katar', 'katarisch', 1),
(131, 'Kenia', 'kenianisch', 1),
(132, 'Kirgisistan', 'kirgisisch', 1),
(133, 'Kiribati', 'kiribatisch', 1),
(134, 'Kolumbien', 'kolumbianisch', 1),
(135, 'Komoren', 'komorisch', 1),
(136, 'Kongo', 'kongolesisch', 1),
(137, 'Korea', 'koreanisch', 1),
(138, 'Kosovo', 'kosovarisch ', 1),
(139, 'Kroatien', 'kroatisch', 1),
(140, 'Kuba', 'kubanisch', 1),
(141, 'Kuwait', 'kuwaitisch', 1),
(142, 'Laos', 'laotisch', 1),
(143, 'Lesotho', 'lesothisch', 0),
(144, 'Lettland', 'lettisch', 1),
(145, 'Libanon', 'libanesisch', 1),
(146, 'Liberia', 'liberianisch', 1),
(147, 'Libysch-Arabische Dschamahirija', 'libysch', 1),
(148, 'Liechtenstein', 'liechtensteinisch', 1),
(149, 'Litauen', 'litauisch', 1),
(150, 'Luxemburg', 'luxemburgisch', 1),
(151, 'Madagaskar', 'madagassisch', 1),
(152, 'Malawi', 'malawisch', 1),
(153, 'Malaysia', 'malaysisch', 1),
(154, 'Malediven', 'maledivisch', 1),
(155, 'Mali', 'malisch', 1),
(156, 'Malta', 'maltesisch', 1),
(157, 'Marokko', 'marokkanisch', 1),
(158, 'Marshallinseln', 'marshallisch', 1),
(159, 'Mauretanien', 'mauretanisch', 1),
(160, 'Mauritius', 'mauritisch', 1),
(161, 'Mazedonien', 'mazedonisch', 1),
(162, 'Mexiko', 'mexikanisch', 1),
(163, 'Mikronesien', 'mikronesisch', 1),
(164, 'Moldau', 'moldauisch', 1),
(165, 'Monaco', 'monegassisch', 1),
(166, 'Mongolei', 'mongolisch', 1),
(167, 'Montenegro', 'montenegrinisch', 1),
(168, 'Mosambik', 'mosambikanisch', 1),
(169, 'Myanmar', 'myanmarisch', 1),
(170, 'Namibia', 'namibisch', 1),
(171, 'Nauru', 'nauruisch', 1),
(172, 'Neuseeland', 'neuseeländisch', 1),
(173, 'Nicaragua', 'nicaraguanisch', 1),
(174, 'Niederlande', 'niederländisch', 1),
(175, 'Niger', 'nigrisch', 1),
(176, 'Nigeria', 'nigerianisch', 1),
(177, 'Niue', 'niueanisch', 1),
(178, 'Norwegen', 'norwegisch', 1),
(179, 'Oman', 'omanisch', 1),
(180, 'Pakistan', 'pakistanisch', 1),
(181, 'Palau', 'palauisch', 1),
(182, 'Panama', 'panamaisch', 1),
(183, 'Papua-Neuguinea', 'papuaneuguineisch', 1),
(184, 'Paraguay', 'paraguayisch', 1),
(185, 'Peru', 'peruanisch', 1),
(186, 'Philippinen', 'philippinisch', 1),
(187, 'Polen', 'polnisch', 1),
(188, 'Portugal', 'portugiesisch', 1),
(189, 'Ruanda', 'ruandisch', 1),
(190, 'Rumänien', 'rumänisch', 1),
(191, 'Russische Föderation', 'russisch', 1),
(192, 'Salomonen', 'salomonisch', 1),
(193, 'Sambia', 'sambisch', 1),
(194, 'Samoa', 'samoanisch', 1),
(195, 'San Marino', 'san-marinesisch', 1),
(196, 'Sao Tomé und Príncipe', 'sao-toméisch', 1),
(197, 'Saudi-Arabien', 'saudi-arabisch', 1),
(198, 'Schweden', 'schwedisch', 1),
(199, 'Schweiz', 'schweizerisch', 1),
(200, 'Senegal', 'senegalesisch', 1),
(201, 'Serbien', 'serbisch', 1),
(202, 'Seychellen', 'seychellisch', 1),
(203, 'Sierra Leone', 'sierra-leonisch', 1),
(204, 'Simbabwe', 'simbabwisch', 1),
(205, 'Singapur', 'singapurisch', 1),
(206, 'Slowakei', 'slowakisch', 1),
(207, 'Slowenien', 'slowenisch', 1),
(208, 'Somalia', 'somalisch', 1),
(209, 'Spanien', 'spanisch', 1),
(210, 'Sri Lanka', 'sri-lankisch', 1),
(211, 'St. Lucia', 'lucianisch', 1),
(212, 'St. Vincent und die Grenadinen', 'vincentisch', 1),
(213, 'Südafrika', 'südafrikanisch', 1),
(214, 'Sudan', 'sudanesisch', 1),
(215, 'Suriname', 'surinamisch', 1),
(216, 'Swasiland', 'swasiländisch', 1),
(217, 'Syrien', 'syrisch', 1),
(218, 'Tadschikistan', 'tadschikisch', 1),
(219, 'Tansania', 'tansanisch', 1),
(220, 'Thailand', 'thailändisch', 1),
(221, 'Togo', 'togoisch', 1),
(222, 'Tonga', 'tongaisch', 1),
(223, 'Tschad', 'tschadisch', 1),
(224, 'Tschechische Republik', 'tschechisch', 1),
(225, 'Tunesien', 'tunesisch', 1),
(226, 'Turkmenistan', 'turkmenisch', 1),
(227, 'Tuvalu', 'tuvaluisch', 1),
(228, 'Uganda', 'ugandisch', 1),
(229, 'Ukraine', 'ukrainisch', 1),
(230, 'Ungarn', 'ungarisch', 1),
(231, 'Uruguay', 'uruguayisch', 1),
(232, 'Usbekistan', 'usbekisch', 1),
(233, 'Vanuatu', 'vanuatuisch', 1),
(234, 'Venezuela', 'venezolanisch', 0),
(235, 'Vietnam', 'vietnamesisch', 1),
(236, 'Weißrussland', 'weißrussisch', 1),
(237, 'Zentralafrikanische Republik', 'zentralafrikanisch', 1),
(238, 'Zypern', 'zyprisch', 1),
(239, 'Angabe verweigert', 'Angabe verweigert', 1);

-- --------------------------------------------------------

--
-- Table structure for table `nationstatistik`
--

CREATE TABLE `nationstatistik` (
  `id` int(11) NOT NULL,
  `nationalität` varchar(255) DEFAULT NULL,
  `anzahlPersonen` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ortsteil`
--

CREATE TABLE `ortsteil` (
  `ortsteilId` int(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `plz` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `plz`
--

CREATE TABLE `plz` (
  `plzId` int(10) NOT NULL,
  `plz` varchar(5) DEFAULT NULL,
  `ort` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `recht`
--

CREATE TABLE `recht` (
  `rechtId` int(10) NOT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `Beschreibung` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `saved_queries`
--

CREATE TABLE `saved_queries` (
  `id` int(11) NOT NULL,
  `query` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `saved_queries`
--

INSERT INTO `saved_queries` (`id`, `query`) VALUES
(1, 'SELECT * FROM ausgabegruppe'),
(2, 'SELECT * FROM ausgabegruppe_ausgabe'),
(3, 'SELECT * FROM ausgabetagzeit'),
(4, 'SELECT * FROM berechtigung'),
(5, 'SELECT * FROM bescheid'),
(6, 'SELECT * FROM bescheidart'),
(7, 'SELECT * FROM deleted_MemberOfTheFamily'),
(8, 'SELECT * FROM einkauf'),
(9, 'SELECT * FROM einstellungen'),
(10, 'SELECT * FROM familienmitglied'),
(11, 'SELECT * FROM Geburtsdatum'),
(12, 'SELECT * FROM haushalt'),
(13, 'SELECT * FROM nation'),
(14, 'SELECT * FROM neue_tabelle'),
(15, 'SELECT * FROM ortsteil'),
(16, 'SELECT * FROM PIECHART'),
(17, 'SELECT * FROM plz'),
(18, 'SELECT * FROM recht'),
(19, 'SELECT * FROM statistiktool'),
(20, 'SELECT * FROM tools'),
(21, 'SELECT * FROM users'),
(22, 'SELECT * FROM verteilestelle'),
(23, 'SELECT * FROM vollmacht'),
(24, 'SELECT * FROM vorlage'),
(25, 'SELECT * FROM warentyp'),
(26, 'INSERT INTO ausgabegruppe (column1, column2) VALUES (value1, value2)'),
(27, 'UPDATE ausgabetagzeit SET column1 = value1 WHERE condition'),
(28, 'DELETE FROM berechtigung WHERE condition'),
(29, 'CREATE TABLE neue_tabelle (column1 datatype, column2 datatype)'),
(30, 'ALTER TABLE einkauf ADD column_name datatype'),
(31, 'DROP TABLE deleted_MemberOfTheFamily),
(32, ''),
(33, 'select * from altersgruppen'),
(34, 'select * from altersgruppen'),
(35, 'select * from altersgruppen'),
(36, 'show COLUMNS from altersgruppen'),
(37, 'show COLUMNS from altersgruppen'),
(38, 'SHOW COLUMNS FROM altersgruppen'),
(39, 'select * from altersgruppen'),
(40, 'select * from altersgruppen'),
(41, 'SELECT * FROM bescheid'),
(42, 'show COLUMNS from altersgruppen'),
(43, 'show COLUMNS from altersgruppen'),
(44, 'show COLUMNS from altersgruppen'),
(45, 'SHOW COLUMNS FROM altersgruppen'),
(46, 'SHOW COLUMNS FROM altersgruppen'),
(47, 'SHOW COLUMNS FROM \'altersgruppen\''),
(48, 'SHOW COLUMNS FROM PieChart'),
(49, 'show COLUMNS from altersgruppen'),
(50, 'SELECT * FROM bescheid'),
(55, 'SELECT * FROM bescheidart'),
(56, 'SELECT * FROM bescheid'),
(57, 'select * from altersgruppen'),
(64, 'select * from altersgruppen'),
(65, 'DELETE FROM altersgruppe'),
(66, 'select * from altersgruppen'),
(67, 'select * from altersgruppen'),
(79, 'SELECT * FROM users'),
(80, 'SELECT * FROM users'),
(81, 'select * from altersgruppen'),
(82, 'select * from altersgruppen'),
(83, 'select * from altersgruppen where startAlter'),
(86, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 15 AND 40'),
(87, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 25'),
(88, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 25'),
(89, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 17 AND 25'),
(90, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 25'),
(91, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 30'),
(92, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 25 AND 35'),
(93, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 30'),
(94, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 40'),
(95, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 25'),
(96, 'SHOW COLUMNS FROM PieChart'),
(97, 'SHOW COLUMNS FROM PieChart'),
(98, 'select * from altersgruppen'),
(99, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 30'),
(100, 'SELECT * FROM berechtigung'),
(101, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 15 AND 40'),
(102, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 15 AND 40'),
(103, 'SELECT * FROM berechtigung'),
(104, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 17 AND 23'),
(105, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 40'),
(106, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 30 AND 70'),
(107, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 30 AND 70'),
(108, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 30'),
(109, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 17 AND 30'),
(110, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 40'),
(111, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 40'),
(112, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 40'),
(113, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 13 AND 40'),
(114, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 18 AND 29'),
(115, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 24 OR (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 25 AND 34 OR (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 35 AND 44 AND YEAR(gDatum) = 2005'),
(116, 'Select * From altersgruppen'),
(117, 'SELECT * FROM familienmitglied WHERE (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 18 AND 24 OR (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 25 AND 34 OR (strftime(\'%Y\', \'now\') - strftime(\'%Y\', gDatum)) BETWEEN 35 AND 44 AND YEAR(gDatum) = 2005'),
(118, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 18 AND 29'),
(119, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 19 AND 27 OR (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 28 AND 37 AND YEAR(gDatum) = 1994'),
(120, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 19 AND 27 OR (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 30 AND 39 AND YEAR(gDatum) = 1995'),
(121, 'SELECT * FROM familienmitglied'),
(122, 'select * from altersgruppen'),
(123, 'select * from altersgruppen'),
(124, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 19 AND 29 OR (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 30 AND 39 AND YEAR(gDatum) = 1995'),
(125, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 19 AND 29 OR (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 30 AND 39 AND YEAR(gDatum) = 1995'),
(126, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 30 AND 39 AND YEAR(gDatum) = 1995'),
(127, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 30 AND 39 AND YEAR(gDatum) = 1995'),
(128, 'select * from altersgruppen'),
(129, 'select * from altersgruppen'),
(130, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 17 AND 27 OR (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 28 AND 29 AND YEAR(gDatum) = 1994'),
(131, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 30 AND 39 AND YEAR(gDatum) = 1995'),
(132, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 19 AND 29 OR (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 30 AND 39 AND YEAR(gDatum) = 1995'),
(133, 'select * from altersgruppen'),
(134, 'SELECT * FROM bescheid'),
(135, 'select * from altersgruppen'),
(136, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 39 AND 49 OR (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 50 AND 60 OR (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 61 AND 71 OR (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 72 AND 100 AND YEAR(gDatum) = 2005'),
(137, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 17 AND 27 OR (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 28 AND 29 AND YEAR(gDatum) = 1994'),
(138, 'select * from altersgruppen'),
(139, 'select * from altersgruppen'),
(140, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 19 AND 49 OR (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 51 AND 61 AND YEAR(gDatum) = 2005'),
(141, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 17 AND 31 AND YEAR(gDatum) = 1996'),
(142, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 17 AND 31 AND YEAR(gDatum) = 1996'),
(143, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 17 AND 31 AND YEAR(gDatum) = 1996'),
(144, 'SELECT * FROM familienmitglied WHERE (YEAR(CURDATE()) - YEAR(gDatum)) BETWEEN 17 AND 31 AND YEAR(gDatum) = 1995'),
(145, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2005\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 12 AND 33  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 44 AND 55 )'),
(146, 'SELECT * FROM familienmitglied'),
(147, 'select * from altersgruppen'),
(148, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2005\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 12 AND 33  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 44 AND 55 )'),
(149, 'SELECT * FROM einkauf'),
(150, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'1995\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 17 AND 28  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 38 AND 48 )'),
(151, 'SELECT * FROM familienmitglied'),
(152, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2005\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 34 AND 55  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 66 AND 77 )'),
(153, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2005\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 34 AND 55  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 66 AND 77 )'),
(154, 'SELECT * FROM familienmitglied'),
(155, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2005\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 34 AND 55 )'),
(156, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2005\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 23 AND 55  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 45 AND 66 )'),
(157, 'select * from altersgruppen'),
(158, 'SELECT * FROM familienmitglied'),
(159, 'SELECT * FROM familienmitglied'),
(160, 'SELECT e.einkaufId, e.warentyp, h.kundennummer, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId, f.haushaltId, v.verteilstellenId, v.bezeichnung, v.adresse, v.listennummer FROM einkauf e JOIN haushalt h ON e.kunde = h.kundennummer JOIN familienmitglied f ON f.haushaltId = h.kundennummer JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = ? AND e.summeEinkauf <= e.summeZahlung'),
(161, 'SELECT e.einkaufId, e.warentyp, h.kundennummer, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId, f.haushaltId, v.verteilstellenId, v.bezeichnung, v.adresse, v.listennummer FROM einkauf e JOIN haushalt h ON e.kunde = h.kundennummer JOIN familienmitglied f ON f.haushaltId = h.kundennummer JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = \'Hauptstelle\' AND e.summeEinkauf <= e.summeZahlung'),
(162, 'Generierte SQL-Abfrage: SELECT e.einkaufId, e.warentyp, h.kundennummer, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId, f.haushaltId, v.verteilstellenId, v.bezeichnung, v.adresse, v.listennummer FROM einkauf e JOIN haushalt h ON e.kunde = h.kundennummer JOIN familienmitglied f ON f.haushaltId = h.kundennummer JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = \'Hauptstelle\' AND e.summeEinkauf > e.summeZahlung'),
(163, 'SELECT e.einkaufId, e.warentyp, h.kundennummer, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId, f.haushaltId, v.verteilstellenId, v.bezeichnung, v.adresse, v.listennummer FROM einkauf e JOIN haushalt h ON e.kunde = h.kundennummer JOIN familienmitglied f ON f.haushaltId = h.kundennummer JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = \'Hauptstelle\' AND e.summeEinkauf > e.summeZahlung'),
(164, 'SQL Query: SELECT e.einkaufId, e.warentyp, h.kundennummer, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId, f.haushaltId, v.verteilstellenId, v.bezeichnung, v.adresse, v.listennummer FROM einkauf e JOIN haushalt h ON e.kunde = h.kundennummer JOIN familienmitglied f ON f.haushaltId = h.kundennummer JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = \'Hauptstelle\' AND e.summeEinkauf > e.summeZahlung'),
(165, 'SELECT e.einkaufId, e.warentyp, h.kundennummer, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId, f.haushaltId, v.verteilstellenId, v.bezeichnung, v.adresse, v.listennummer, h.saldo FROM einkauf e JOIN haushalt h ON e.kunde = h.kundennummer JOIN familienmitglied f ON f.haushaltId = h.kundennummer JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = \'Hauptstelle\' AND e.summeEinkauf > e.summeZahlung'),
(166, 'select * from guthabenStatistik'),
(167, 'select * from einkauf'),
(168, 'select * from haushalt'),
(169, 'SELECT e.einkaufId, e.warentyp, h.kundennummer, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId, f.haushaltId, v.verteilstellenId, v.bezeichnung, v.adresse, v.listennummer, h.saldo FROM einkauf e JOIN haushalt h ON e.kunde = h.kundennummer JOIN familienmitglied f ON f.haushaltId = h.kundennummer JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = \'Hauptstelle\' AND e.summeEinkauf <= e.summeZahlung'),
(170, 'select * from guthabenStatistik'),
(171, 'SELECT e.einkaufId, e.warentyp, h.kundennummer, CONCAT(f.vName, \' \', f.nName) AS kunde, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId, f.haushaltId, v.verteilstellenId, v.bezeichnung, v.adresse, v.listennummer, h.saldo FROM einkauf e JOIN haushalt h ON e.kunde = h.kundennummer LEFT JOIN familienmitglied f ON f.haushaltId = h.kundennummer JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = \'Hauptstelle\' AND e.summeEinkauf > e.summeZahlung'),
(172, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2005\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 12 AND 33  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 44 AND 55 )'),
(173, 'SELECT e.einkaufId, e.warentyp, h.kundennummer, CONCAT(f.vName, \' \', f.nName) AS kunde, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId, f.haushaltId, v.verteilstellenId, v.bezeichnung, v.adresse, v.listennummer, h.saldo FROM einkauf e JOIN haushalt h ON e.kunde = h.kundennummer LEFT JOIN familienmitglied f ON f.haushaltId = h.kundennummer JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = \'Hauptstelle\' AND e.summeEinkauf <= e.summeZahlung'),
(174, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2005\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 34 AND 55 )'),
(175, 'select * from guthabenStatistik'),
(176, 'SELECT * FROM archivierteKunden'),
(177, 'SELECT * FROM kundenInfos'),
(178, 'SELECT * FROM familienmitglied'),
(179, 'SELECT * FROM familienmitglied'),
(180, 'select * from familienmitglied'),
(181, 'SELECT * FROM familienmitglied'),
(182, 'SELECT * FROM haushalt'),
(183, 'SELECT * FROM bescheidart'),
(184, 'SELECT * FROM familienmitglied'),
(185, 'select * from kundenInfos'),
(186, 'select * from kundenInfos'),
(187, 'select * from haushalt'),
(188, 'SELECT * FROM kundenInfos'),
(189, 'SELECT * FROM haushalt'),
(190, 'SELECT * FROM kundenInfos'),
(191, 'SELECT * FROM haushalt'),
(192, 'SELECT * FROM kundenInfos'),
(193, 'SELECT * FROM haushalt'),
(194, 'SELECT * FROM familienmitglied'),
(195, 'SELECT * FROM warentyp'),
(196, 'SELECT * FROM einstellungen'),
(197, 'SELECT * FROM nationStatistik'),
(198, 'SELECT * FROM nationStatistik'),
(199, 'SELECT * FROM saved_queries'),
(200, 'select * from familienmitglied'),
(201, 'SELECT \n    MONTH(COALESCE(f.gDatum, h.kundeSeit, e.warentyp)) AS monat,\n    COUNT(DISTINCT f.personId) AS anzahlPersonen,\n    SUM(COALESCE(h.saldo, 0)) AS gesamtUmsatzHaushalt,\n    SUM(COALESCE(e.warentyp, 0)) AS gesamtUmsatzEinkauf,\n    COUNT(DISTINCT f2.personId) AS neuzugaenge\nFROM \n    haushalt h\nLEFT JOIN \n    familienmitglied f ON h.kundennummer = f.haushaltId AND f.gDatum IS NOT NULL AND YEAR(f.gDatum) = 2024\nLEFT JOIN \n    einkauf e ON e.kunde = h.kundennummer AND e.warentyp IS NOT NULL AND YEAR(e.warentyp) = 2024\nLEFT JOIN \n    familienmitglied f2 ON MONTH(h.kundeSeit) = MONTH(COALESCE(f.gDatum, h.kundeSeit, e.warentyp))\nWHERE \n    (YEAR(h.kundeSeit) = 2024 OR f.gDatum IS NOT NULL OR e.warentyp IS NOT NULL)\nGROUP BY \n    MONTH(COALESCE(f.gDatum, h.kundeSeit, e.warentyp))'),
(202, 'SELECT * FROM familienmitglied'),
(203, 'SELECT e.einkaufId, e.warentyp, h.kundennummer, f.vName, f.nName, w.warentypid, CONCAT(IFNULL(f.vName, \'\'), \' \', IFNULL(f.nName, \'\')) AS kunde, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId, f.haushaltId, v.verteilstellenId, v.bezeichnung, v.adresse, v.listennummer, h.saldo FROM einkauf e JOIN haushalt h ON e.kunde = h.kundennummer LEFT JOIN familienmitglied f ON f.haushaltId = h.kundennummer JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId JOIN warentyp w ON e.warentyp = w.warentypid WHERE v.bezeichnung = \'Hauptstelle\' AND e.summeEinkauf > e.summeZahlung'),
(204, 'SELECT * FROM herkunftStatistik'),
(205, 'SELECT e.einkaufId, e.warentyp, h.kundennummer, f.vName, f.nName, w.warentypid, CONCAT(IFNULL(f.vName, \'\'), \' \', IFNULL(f.nName, \'\')) AS kunde, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId, f.haushaltId, v.verteilstellenId, v.bezeichnung, v.adresse, v.listennummer, h.saldo FROM einkauf e JOIN haushalt h ON e.kunde = h.kundennummer LEFT JOIN familienmitglied f ON f.haushaltId = h.kundennummer JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId JOIN warentyp w ON e.warentyp = w.warentypid WHERE v.bezeichnung = \'Hauptstelle\' AND e.summeEinkauf > e.summeZahlung'),
(206, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'1995\''),
(207, 'SELECT * FROM familienmitglied'),
(208, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) > \'1900\''),
(209, 'SELECT * FROM kundenInfos'),
(210, 'SELECT \n    MONTH(COALESCE(f.gDatum, h.kundeSeit, e.datum)) AS monat,\n    COUNT(DISTINCT f.personId) AS anzahlPersonen,\n    SUM(COALESCE(h.saldo, 0)) AS gesamtUmsatzHaushalt,\n    SUM(COALESCE(e.betrag, 0)) AS gesamtUmsatzEinkauf,\n    COUNT(DISTINCT f2.personId) AS neuzugaenge\nFROM haushalt h\nLEFT JOIN familienmitglied f ON h.kundennummer = f.haushaltId AND YEAR(f.gDatum) = ?\nLEFT JOIN einkauf e ON e.kunde = h.kundennummer AND YEAR(e.datum) = ?\nLEFT JOIN familienmitglied f2 ON MONTH(h.kundeSeit) = MONTH(f.gDatum)\nWHERE YEAR(h.kundeSeit) = ?\nGROUP BY MONTH(COALESCE(f.gDatum, h.kundeSeit, e.datum))'),
(211, 'SELECT \n    MONTH(COALESCE(f.gDatum, h.kundeSeit, e.datum)) AS monat,\n    COUNT(DISTINCT f.personId) AS anzahlPersonen,\n    SUM(COALESCE(h.saldo, 0)) AS gesamtUmsatzHaushalt,\n    SUM(COALESCE(e.betrag, 0)) AS gesamtUmsatzEinkauf,\n    COUNT(DISTINCT f2.personId) AS neuzugaenge\nFROM haushalt h\nLEFT JOIN familienmitglied f ON h.kundennummer = f.haushaltId AND YEAR(f.gDatum) = 2024\nLEFT JOIN einkauf e ON e.kunde = h.kundennummer AND YEAR(e.datum) = 2024\nLEFT JOIN familienmitglied f2 ON MONTH(h.kundeSeit) = MONTH(f.gDatum)\nWHERE YEAR(h.kundeSeit) = 2024\nGROUP BY MONTH(COALESCE(f.gDatum, h.kundeSeit, e.datum))'),
(212, 'SELECT * FROM guthabenStatistik'),
(213, 'SELECT * FROM herkunftStatistik'),
(214, 'SELECT * FROM haushalt'),
(215, 'SELECT \n    MONTH(COALESCE(f.gDatum, h.kundeSeit, e.warentyp)) AS monat,\n    COUNT(DISTINCT f.personId) AS anzahlPersonen,\n    SUM(COALESCE(h.saldo, 0)) AS gesamtUmsatzHaushalt,\n    SUM(COALESCE(e.warentyp, 0)) AS gesamtUmsatzEinkauf,\n    COUNT(DISTINCT f2.personId) AS neuzugaenge\nFROM \n    haushalt h\nLEFT JOIN \n    familienmitglied f ON h.kundennummer = f.haushaltId AND f.gDatum IS NOT NULL AND YEAR(f.gDatum) = 2024\nLEFT JOIN \n    einkauf e ON e.kunde = h.kundennummer AND e.warentyp IS NOT NULL AND YEAR(e.warentyp) = 2024\nLEFT JOIN \n    familienmitglied f2 ON MONTH(h.kundeSeit) = MONTH(COALESCE(f.gDatum, h.kundeSeit, e.warentyp))\nWHERE \n    (YEAR(h.kundeSeit) = 2024 OR f.gDatum IS NOT NULL OR e.warentyp IS NOT NULL)\nGROUP BY \n    MONTH(COALESCE(f.gDatum, h.kundeSeit, e.warentyp))'),
(216, 'SELECT * FROM nationStatistik'),
(217, 'SELECT * FROM nationStatistik'),
(218, 'select * from familienmitglied'),
(219, 'SELECT * FROM guthabenStatistik'),
(220, 'SELECT * FROM haushalt'),
(221, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2004\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 18 AND 29  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 30 AND 39  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 40 AND 49 )'),
(222, 'SELECT * FROM altersgruppen'),
(223, 'SELECT * FROM familienmitglied WHERE (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 18 AND 29  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 30 AND 39 )'),
(224, 'SELECT * FROM altersgruppen'),
(225, 'SELECT * FROM haushalt'),
(226, 'SELECT * FROM kundenInfos'),
(227, 'SELECT * FROM nationstatistik'),
(228, 'SELECT \n    MONTH(COALESCE(f.gDatum, h.kundeSeit, e.datum)) AS monat,\n    COUNT(DISTINCT f.personId) AS anzahlPersonen,\n    SUM(COALESCE(h.saldo, 0)) AS gesamtUmsatzHaushalt,\n    SUM(COALESCE(e.betrag, 0)) AS gesamtUmsatzEinkauf,\n    COUNT(DISTINCT f2.personId) AS neuzugaenge\nFROM haushalt h\nLEFT JOIN familienmitglied f ON h.kundennummer = f.haushaltId AND YEAR(f.gDatum) = ?\nLEFT JOIN einkauf e ON e.kunde = h.kundennummer AND YEAR(e.datum) = ?\nLEFT JOIN familienmitglied f2 ON MONTH(h.kundeSeit) = MONTH(f.gDatum)\nWHERE YEAR(h.kundeSeit) = ?\nGROUP BY MONTH(COALESCE(f.gDatum, h.kundeSeit, e.datum))'),
(229, 'SELECT \n    MONTH(COALESCE(f.gDatum, h.kundeSeit, e.datum)) AS monat,\n    COUNT(DISTINCT f.personId) AS anzahlPersonen,\n    SUM(COALESCE(h.saldo, 0)) AS gesamtUmsatzHaushalt,\n    SUM(COALESCE(e.betrag, 0)) AS gesamtUmsatzEinkauf,\n    COUNT(DISTINCT f2.personId) AS neuzugaenge\nFROM haushalt h\nLEFT JOIN familienmitglied f ON h.kundennummer = f.haushaltId AND YEAR(f.gDatum) = 2024\nLEFT JOIN einkauf e ON e.kunde = h.kundennummer AND YEAR(e.datum) = 2024\nLEFT JOIN familienmitglied f2 ON MONTH(h.kundeSeit) = MONTH(f.gDatum)\nWHERE YEAR(h.kundeSeit) = 2024\nGROUP BY MONTH(COALESCE(f.gDatum, h.kundeSeit, e.datum))'),
(230, 'SELECT * FROM jahresUebersicht'),
(231, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2005\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 18 AND 29  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 23 AND 55 )'),
(232, 'SELECT * FROM familienmitglied WHERE (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 18 AND 29  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 23 AND 55 )'),
(233, 'SELECT * FROM altersgruppen'),
(234, 'SELECT * FROM nationStatistik'),
(235, 'SELECT * FROM familienmitglied WHERE AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 18 AND 25  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 26 AND 30 )'),
(236, 'SELECT * FROM familienmitglied WHERE (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 18 AND 25  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 26 AND 30 )'),
(237, 'SELECT * FROM altersgruppen'),
(238, 'SELECT * FROM nationStatistik'),
(239, 'SELECT * FROM einkauf'),
(240, 'SELECT * FROM kundenInfos'),
(241, 'SELECT * FROM haushalt'),
(242, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2004\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 12 AND 33 )'),
(243, 'SELECT * FROM familienmitglied WHERE  (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 12 AND 33 )'),
(244, 'SELECT e.einkaufId, CONCAT(IFNULL(f.vName, \'\'), \' \', IFNULL(f.nName, \'\')) AS kundeName, w.name AS warentyp, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId FROM einkauf e JOIN familienmitglied f ON e.kunde = f.personId JOIN warentyp w ON e.warentyp = w.warentypId JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = ? AND e.summeEinkauf > e.summeZahlung'),
(245, 'SELECT e.einkaufId, CONCAT(IFNULL(f.vName, \'\'), \' \', IFNULL(f.nName, \'\')) AS kundeName, w.name AS warentyp, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId FROM einkauf e JOIN familienmitglied f ON e.kunde = f.personId JOIN warentyp w ON e.warentyp = w.warentypId JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = Hauptstelle AND e.summeEinkauf > e.summeZahlung'),
(246, 'SELECT e.einkaufId, CONCAT(IFNULL(f.vName, \'\'), \' \', IFNULL(f.nName, \'\')) AS kundeName, w.name AS warentyp, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId FROM einkauf e JOIN familienmitglied f ON e.kunde = f.personId JOIN warentyp w ON e.warentyp = w.warentypId JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = \'Hauptstelle\' AND e.summeEinkauf > e.summeZahlung'),
(247, 'select * from familienmiglied Where vName = \'Fadumo\''),
(248, 'select * from familienmitglied Where vName = \'Fadumo\''),
(249, 'select * from familienmitglied Where vName = \'Noel\''),
(250, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2005\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 23 AND 33  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 34 AND 44 )'),
(251, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2005\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 23 AND 33  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 34 AND 44 )'),
(252, 'SELECT * FROM familienmitglied WHERE AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 23 AND 33  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 34 AND 44 )'),
(253, 'SELECT * FROM familienmitglied WHERE (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 23 AND 33  OR DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 34 AND 44 )'),
(254, 'SELECT * FROM nationstatistik'),
(255, 'SELECT * FROM nationstatistik'),
(256, 'SELECT * FROM nationstatistik'),
(257, 'SELECT * FROM nationstatistik'),
(258, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2004\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 12 AND 33 )'),
(259, 'SELECT * FROM familienmitglied WHERE  (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 12 AND 33 )'),
(260, 'select * from altersgruppen'),
(261, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2004\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 12 AND 33 )'),
(262, 'SELECT * FROM nationstatistik'),
(263, 'SELECT * FROM nationstatistik'),
(264, 'SELECT * FROM nationstatistik'),
(265, 'SELECT e.einkaufId, CONCAT(IFNULL(f.vName, \'\'), \' \', IFNULL(f.nName, \'\')) AS kundeName, w.name AS warentyp, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId FROM einkauf e JOIN familienmitglied f ON e.kunde = f.personId JOIN warentyp w ON e.warentyp = w.warentypId JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = ? AND e.summeEinkauf >= e.summeZahlung'),
(266, 'SELECT e.einkaufId, CONCAT(IFNULL(f.vName, \'\'), \' \', IFNULL(f.nName, \'\')) AS kundeName, w.name AS warentyp, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId FROM einkauf e JOIN familienmitglied f ON e.kunde = f.personId JOIN warentyp w ON e.warentyp = w.warentypId JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = \'Hauptstelle\' AND e.summeEinkauf >= e.summeZahlung'),
(267, 'SELECT e.einkaufId, CONCAT(IFNULL(f.vName, \'\'), \' \', IFNULL(f.nName, \'\')) AS kundeName, w.name AS warentyp, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId FROM einkauf e JOIN familienmitglied f ON e.kunde = f.personId JOIN warentyp w ON e.warentyp = w.warentypId JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = ? AND e.summeEinkauf <= e.summeZahlung'),
(268, 'SELECT e.einkaufId, CONCAT(IFNULL(f.vName, \'\'), \' \', IFNULL(f.nName, \'\')) AS kundeName, w.name AS warentyp, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId FROM einkauf e JOIN familienmitglied f ON e.kunde = f.personId JOIN warentyp w ON e.warentyp = w.warentypId JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = \'Hauptstelle\' AND e.summeEinkauf <= e.summeZahlung'),
(269, 'SELECT * FROM nationStatistik'),
(270, 'SELECT * FROM jahresuebersichtStatistik'),
(271, 'SELECT * FROM familienmitglied'),
(272, 'SELECT * FROM jahresUebersicht'),
(273, 'SELECT * FROM jahresuebersichtStatistik'),
(274, 'SELECT * FROM familienmitglied WHERE YEAR(gDatum) = \'2003\' AND (DATEDIFF(CURDATE(), gDatum) / 365 BETWEEN 34 AND 44 )'),
(275, 'SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = 2004 TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 23 AND 33 OR  TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 34 AND 44)'),
(276, 'SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = 2003)'),
(277, 'SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = 2003 TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 12 AND 33 OR  TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 34 AND 44)'),
(278, 'SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = 2003 TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 12 AND 33 OR  TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 34 AND 44)'),
(279, 'SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = 2003 TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 34 AND 44 OR  TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 45 AND 66)'),
(280, 'SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = 2003 TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 23 AND 33 OR  TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 34 AND 44)'),
(281, 'SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = 2003 TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 34 AND 55 OR  TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 56 AND 66)'),
(282, 'SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = 2004 OR ((TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 34 AND 39) OR (TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 22 UND 33)))'),
(283, 'SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = 2004 OR ((TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 34 AND 39) OR (TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 22 UND 33)))'),
(284, 'SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = 2003 OR ((TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 33 AND 44) OR (TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 22 UND 33)))'),
(285, 'SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = 2005 OR ((TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 18 AND 22) OR (TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 23 AND 28)))'),
(286, 'SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = 2005 OR ((TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 18 AND 22) OR (TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 23 AND 28)))'),
(287, 'SELECT * FROM nationStatistik'),
(288, 'SELECT * FROM familienmitglied'),
(289, 'SELECT * FROM bescheidStatistik'),
(290, 'SELECT * FROM bescheidStatistik'),
(291, 'SELECT * FROM familienmitglied'),
(292, 'SELECT * FROM bescheidStatistik'),
(293, 'SELECT * FROM jahresuebersichtStatistik'),
(294, 'SELECT e.einkaufId, CONCAT(IFNULL(f.vName, \'\'), \' \', IFNULL(f.nName, \'\')) AS kundeName, w.name AS warentyp, e.summeEinkauf, e.summeZahlung, e.anzahlKinder, e.anzahlErwachsene, f.personId FROM einkauf e JOIN familienmitglied f ON e.kunde = f.personId JOIN warentyp w ON e.warentyp = w.warentypId JOIN verteilstelle v ON e.beiVerteilstelle = v.verteilstellenId WHERE v.bezeichnung = ?'),
(295, 'SELECT * FROM familienmitglied WHERE (YEAR(gDatum) = 2005 OR ((TIMESTAMPDIFF(YEAR, gDatum, CURDATE()) BETWEEN 12 AND 33)))'),
(296, 'SELECT * FROM bescheidStatistik');

-- --------------------------------------------------------

--
-- Table structure for table `statistiktool`
--

CREATE TABLE `statistiktool` (
  `id` int(11) NOT NULL,
  `Nationalitäten` varchar(255) DEFAULT NULL,
  `Altersstatistik` varchar(255) DEFAULT NULL,
  `Guthaben_und_offene_Beiträge` double DEFAULT NULL,
  `Herkunft_der_Kunden` varchar(255) DEFAULT NULL,
  `Archivierte_Kunden` int(11) DEFAULT NULL,
  `Gesperrte_Kunden` int(11) DEFAULT NULL,
  `Anzahl_der_Haushalte` int(11) DEFAULT NULL,
  `Anzahl_der_Personen` int(11) DEFAULT NULL,
  `Beschwerde_Art_Statistik` varchar(255) DEFAULT NULL,
  `Personen_ohne_Beschwerde` int(11) DEFAULT NULL,
  `Anzahl_der_Einkäufe` int(11) DEFAULT NULL,
  `Umsatzauswertung` double DEFAULT NULL,
  `Tagesstatistik_Warengruppen` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `table_column_preferences`
--

CREATE TABLE `table_column_preferences` (
  `id` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `table_name` text NOT NULL,
  `column_id` text NOT NULL,
  `width` double NOT NULL,
  `order_index` int(11) NOT NULL,
  `visible` tinyint(1) NOT NULL,
  `sort_type` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `table_column_preferences`
--

INSERT INTO `table_column_preferences` (`id`, `userId`, `table_name`, `column_id`, `width`, `order_index`, `visible`, `sort_type`) VALUES
(15, 2, 'ErrorReportList4MemberController', 'title', 243, 0, 1, 'ASCENDING'),
(16, 2, 'ErrorReportList4MemberController', 'userDescription', 547, 1, 1, 'ASCENDING'),
(17, 2, 'ErrorReportList4MemberController', 'status', 121, 2, 1, 'ASCENDING'),
(18, 2, 'ErrorReportList4MemberController', 'createdAt', 97, 3, 1, 'ASCENDING'),
(44, 2, 'WishRequestList4MemberController', 'title', 243, 0, 1, 'ASCENDING'),
(45, 2, 'WishRequestList4MemberController', 'description', 547, 1, 1, 'ASCENDING'),
(46, 2, 'WishRequestList4MemberController', 'status', 121, 2, 1, 'ASCENDING'),
(47, 2, 'WishRequestList4MemberController', 'createdAt', 97, 3, 1, 'ASCENDING'),
(112, 2, 'KundenSucheOutput', 'Kundennummer', 77, 0, 1, 'ASCENDING'),
(113, 2, 'KundenSucheOutput', 'Nachname', 93, 1, 1, 'ASCENDING'),
(114, 2, 'KundenSucheOutput', 'Vorname', 78, 2, 1, 'ASCENDING'),
(115, 2, 'KundenSucheOutput', 'Adresse', 77, 3, 1, 'ASCENDING'),
(116, 2, 'KundenSucheOutput', 'Plz', 107, 4, 1, 'ASCENDING'),
(117, 2, 'KundenSucheOutput', 'Wohnort', 106, 5, 1, 'ASCENDING'),
(118, 2, 'KundenSucheOutput', 'NationString', 76, 6, 1, 'ASCENDING'),
(119, 2, 'KundenSucheOutput', 'BirthdayString', 76, 7, 1, 'ASCENDING'),
(120, 2, 'KundenSucheOutput', 'CustomerSince', 76, 8, 1, 'ASCENDING'),
(121, 2, 'KundenSucheOutput', 'Verteilstelle', 76, 9, 1, 'ASCENDING'),
(122, 2, 'KundenSucheOutput', 'Ausgabegruppe', 76, 10, 1, 'ASCENDING'),
(123, 2, 'KundenSucheOutput', 'BelieferungString', 76, 11, 1, 'ASCENDING'),
(127, 2, 'TableViewLogsProtokoll', 'timestamp', 152, 0, 1, 'ASCENDING'),
(128, 2, 'TableViewLogsProtokoll', 'level', 81, 1, 1, 'ASCENDING'),
(129, 2, 'TableViewLogsProtokoll', 'source', 188, 2, 1, 'ASCENDING'),
(130, 2, 'TableViewLogsProtokoll', 'message', 537, 3, 1, 'ASCENDING'),
(134, 2, 'tvBuchungenBearbeiten', 'Name', 99, 0, 1, 'ASCENDING'),
(135, 2, 'tvBuchungenBearbeiten', 'BuchungText', 99, 1, 1, 'ASCENDING'),
(136, 2, 'tvBuchungenBearbeiten', 'ShoppingOn', 99, 2, 1, 'ASCENDING'),
(137, 2, 'tvBuchungenBearbeiten', 'ErfasstAm', 98, 3, 1, 'ASCENDING'),
(138, 2, 'tvBuchungenBearbeiten', 'WarentypName', 98, 4, 1, 'ASCENDING'),
(139, 2, 'tvBuchungenBearbeiten', 'Umsatz', 98, 5, 1, 'ASCENDING'),
(140, 2, 'tvBuchungenBearbeiten', 'Zahlung', 98, 6, 1, 'ASCENDING'),
(141, 2, 'tvBuchungenBearbeiten', 'VerteilstelleName', 113, 7, 1, 'ASCENDING'),
(142, 2, 'tvBuchungenBearbeiten', 'CanceledOn', 169, 8, 1, 'ASCENDING'),
(164, 2, 'tvFamilienMitgleider', 'AnredeString', 90, 0, 1, 'ASCENDING'),
(165, 2, 'tvFamilienMitgleider', 'GenderString', 90, 1, 1, 'ASCENDING'),
(166, 2, 'tvFamilienMitgleider', 'Name', 90, 2, 1, 'ASCENDING'),
(167, 2, 'tvFamilienMitgleider', 'BirthdayString', 89, 3, 1, 'ASCENDING'),
(168, 2, 'tvFamilienMitgleider', 'Bemerkung', 89, 4, 1, 'ASCENDING'),
(169, 2, 'tvFamilienMitgleider', 'NationString', 89, 5, 1, 'ASCENDING'),
(170, 2, 'tvFamilienMitgleider', 'BerechtigungString', 89, 6, 1, 'ASCENDING'),
(171, 2, 'tvFamilienMitgleider', 'TypString', 89, 7, 1, 'ASCENDING'),
(172, 2, 'tvFamilienMitgleider', 'Gebuehren', 102, 8, 1, 'ASCENDING'),
(173, 2, 'tvFamilienMitgleider', 'Ausweis', 154, 9, 1, 'ASCENDING');

-- --------------------------------------------------------

--
-- Table structure for table `tools`
--

CREATE TABLE `tools` (
  `ID` int(11) NOT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `Age` int(11) DEFAULT NULL,
  `Geschlecht` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userId` int(11) NOT NULL,
  `userName` varchar(45) DEFAULT NULL,
  `firstName` varchar(45) DEFAULT NULL,
  `surname` varchar(45) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `userRights` varchar(45) DEFAULT NULL,
  `blockedUntil` timestamp NULL DEFAULT NULL,
  `numberOfMistrials` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userId`, `userName`, `firstName`, `surname`, `birthday`, `password`, `userRights`, `blockedUntil`, `numberOfMistrials`) VALUES
(1, 'Systemdatensatz!', 'NICHT LOESCHEN!', 'NICHT AENDERN!', '1900-01-01', 'ro0kAkPIDyG4+PHjgBnEEN20/vqzUWWOCIFhrCzVTaXYAbc6iyw2c+MlRwJ00vZtkxt2bfqP3ozXLBatsGkKUw==', 'Systemrechte', NULL, NULL),
(2, 'Administrator', 'Admin', 'Muster', '2000-01-01', 'hpHdfFJpZtJByXgxnN+kcI9pic8T9GoLNPDide7dF22QJgWTp9GHllGGvy2RwPnWSP15mCLV+uf1rhvofj+NQQ==', 'Administrator', '2018-09-24 08:43:22', 0);

-- --------------------------------------------------------

--
-- Table structure for table `verteilstelle`
--

CREATE TABLE `verteilstelle` (
  `verteilstellenId` int(10) NOT NULL,
  `bezeichnung` varchar(50) DEFAULT NULL,
  `adresse` varchar(30) DEFAULT NULL,
  `listennummer` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `verteilstelle`
--

INSERT INTO `verteilstelle` (`verteilstellenId`, `bezeichnung`, `adresse`, `listennummer`) VALUES
(1, 'Hauptstelle', '', 7);

-- --------------------------------------------------------

--
-- Table structure for table `vollmacht`
--

CREATE TABLE `vollmacht` (
  `vollmachtId` int(10) NOT NULL,
  `haushaltId` int(10) DEFAULT NULL,
  `bevollmaechtigtePersonId` int(10) DEFAULT NULL,
  `ausgestelltAm` date DEFAULT NULL,
  `ablaufDatum` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `vorlage`
--

CREATE TABLE `vorlage` (
  `vorlageId` int(10) NOT NULL,
  `templateType` varchar(30) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `autor` varchar(30) DEFAULT NULL,
  `fileVersion` varchar(15) DEFAULT NULL,
  `fileTypes` varchar(100) DEFAULT NULL,
  `defaultText` tinytext DEFAULT NULL,
  `passwort` int(11) DEFAULT NULL,
  `aktiv` tinyint(1) DEFAULT NULL,
  `daten` mediumblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `vorlage`
--

INSERT INTO `vorlage` (`vorlageId`, `templateType`, `name`, `autor`, `fileVersion`, `fileTypes`, `defaultText`, `passwort`, `aktiv`, `daten`) VALUES
(6, 'kvpAusweis', 'Erlangener Tafel', 'Robin Becker', '20170508101841', 'HTML-Datei (*.html)|*.html|', '.html', 0, 0, 0x5545734442416f41416741414145565371456f414141414141414141414141414141414641414141614852746243395153774d454367414341414141366e32545358477a453571354251414175515541414245414141426f644731734c304a6a5332524f636a55314c6d70775a2f2f592f2b414145457047535559414151454141414541415141412f39734151774144416749444167494441774d4442414d4442415549425155454241554b427763474341774b4441774c4367734c445134534541304f4551344c437841574542455446425556465177504678675746426753464255552f39734151774544424151464241554a4251554a4641304c44525155464251554642515546425155464251554642515546425155464251554642515546425155464251554642515546425155464251554642515546425155464251552f384141455167414d674130417745694141495241514d5241662f454142384141414546415145424151454241414141414141414141414241674d454251594843416b4b432f2f45414c55514141494241774d4342414d4642515145414141426651454341774145455155534954464242684e525951636963525179675a476843434e4373634556557448774a444e69636f494a4368595847426b614a53596e4b436b714e4455324e7a67354f6b4e4552555a4853456c4b55315256566c64595756706a5a47566d5a326870616e4e3064585a3365486c3667345346686f6549695971536b3553566c7065596d5a71696f36536c7071656f716171797337533174726534756272437738544678736649796372533039545631746659326472683475506b3565626e364f6e7138664c7a39505832392f6a352b762f454142384241414d42415145424151454241514541414141414141414241674d454251594843416b4b432f2f45414c555241414942416751454177514842515145414145436477414241674d52424155684d5159535156454859584554496a4b42434252436b61477877516b6a4d314c7746574a7930516f574a4454684a66455847426b614a69636f4b536f314e6a63344f547044524556475230684a536c4e5556565a5857466c615932526c5a6d646f6157707a6448563264336835656f4b44684957476834694a69704b546c4a57576c35695a6d714b6a704b576d7036697071724b7a744c57327437693575734c44784d584778386a4a79744c54314e585731396a5a32754c6a354f586d352b6a7036764c7a39505832392f6a352b762f61414177444151414345514d524144384154784e2f7a61682f3352332f414e3243764b7667662f795472396d6a2f73632f42503841366b506a4776566645332f4e71482f644866384133594b38712b422f2f4a4f763261502b787a38452f774471512b4d6141505664452f354c463842502b794d2f44722f314c39496f2f59722f414f54697644332f414849482f71764e596f30542f6b735877452f37497a384f762f557630696a3969763841354f4b38506638416367662b7138316967447972345666386d7361482f7742695a71482f414b512f45657656663231502b54697645503841335038412f774371383065764b7668562f77416d7361482f414e695a71482f704438523639562f62552f354f4b38512f397a2f2f414f71383065674436712f344a3166386b64314c2f7558662f55513043696a2f41494a3166386b64314c2f755866384131454e416f6f412b566645332f4e71482f6448662f646772797234482f77444a4f763261502b787a38452f2b70443478723158784e2f7a61682f3352332f33594b38712b422f3841795472396d6a2f73632f42502f71512b4d6141505664452f354c463842503841736a5077362f3841557630696a3969762f6b347277392f3349482f71764e596f30542f6b735877452f7743794d2f44722f7742532f534b50324b2f2b54697644332f6367662b7138316967447972345666386d7361482f324a6d6f662b6b5078487231583974542f414a4f4b38512f397a2f38412b71383065764b7668562f7961786f662f596d61682f36512f45657656663231502b54697645502f4148502f415036727a5236415071722f41494a3166386b64314c2f755866384131454e416f6f2f344a3166386b64314c2f7558662f55513043696744355638546638326f663930642f7744646772797234482f386b362f5a6f2f37485077542f414f70443478723158784e2f7a61682f3352332f414e3243764b7667662f795472396d6a2f73632f42503841366b506a476744315852502b53786641542f736a5077362f39532f534b50324b2f77446b347277392f774279422f36727a574b4e452f354c463842502b794d2f44722f314c39496f2f59722f414f54697644332f414849482f71764e596f4138712b46582f4a7247682f3841596d61682f77436b5078487231583974542f6b347278442f414e7a2f4150384171764e4872797234566638414a7247682f7744596d61682f36512f45657656663231502b54697645502f632f2f774471764e486f412b71762b4364582f4a4864532f376c332f31454e416f6f2f77434364582f4a4864532f376c332f414e5244514b4b41506c58784e2f7a61682f3352332f33594b38712b422f3841795472396d6a2f73632f42502f71512b4d614b4b41505664452f354c463842502b794d2f44722f314c39496f2f59722f414f54697644332f414849482f71764e596f6f6f4138712b46582f4a7247682f39695a71482f704438523639562f62552f774354697645502f632f2f415071764e486f6f6f412b71762b4364582f4a4864532f376c332f31454e416f6f6f6f412f396c5153774d4543674143414141415957615453554838576a6155446741416c41344141424d414141426f644731734c3078765a3238675647466d5a5777756347356e6956424f5277304b47676f414141414e5355684555674141414d67414141413843414d41414141556851576a4141414141584e535230494172733463365141414141526e51553142414143786a777638595155414141474155457855526665756365353249663365777676537266336f302b746b4565746d4566764f7076474a504f786f45504f575566535a56666e426b667a5573505364577647474f653179474f787345666d396976616c5a6643454e662f392f50693166664b5253667a5a75662f3438764b4f5266693668662f392b763775346643424d66576859503772326539394b757871454f39354a66724a6e503774336672476d502f353966373036764f54544f353248764f53532f697965667a577450336979653174477653655850616f6176336d30502f31374f7063452b392b4c50474b514f35344976336c7a76657764762f37392f2f32372b3176452f72456c7643414c7661726265317948664b4d5165746f455069346776377735507a617650764c6f506d2b6a5043444e50377a365057675875317746663779352b70684576764d6f2f7a63762b39364b666170635072446b2f5361562b392f4d666573622f764b6e766932662f57695a50336778664b5053766578654f787146666d376950616d5a2f6576632f6d2f6b664b5152767a587476697a652f724a6e7578724575746c452f4f59574f746a456539384a2f37783575746945763777342f72496d2f3772322f7650702f2f372b66336b792f2f373966576a592f7a5875664f58562f6e416a2b7873442b357a472f7a567375786f4575746e46665365594f35334b75787245502f2f2f3466437846454141414142596b744852414349425231494141414144474e745546424b513231774d4463784d67414141414e4941484f384141414d6a556c455156526f512b32616a31636153524c4851656d52475a4242516c5151527745567752394245596849514d636f6f6f7536476d5063524631316338466663624e75647333654866577658315833444c38554e2b3764752b6664792f6339746165375a36592f3031585631544e6134503945333041656d37364250445a3941336c737568396b64474c66706131564b746e67324f4636763148354f485550534b593948713549544b36675a507762692b342f5970615749484d654e535167544d6c4d536c75476a655a48703159676e56346d6965464c4443554a4a4a6d6c642b654d486f394d6434503438753835686f77475a566463636364434d4d743055574f664e44715a577335625549654c50396d4d69736a756a6d563246495a6e715a344c6a345461646f776131453633442b414d4f2b5758524f50676f64466979532f44434c387161616f544a6e6d566b493271326b573563387079496b703367377738346b596c5365483435646c5458755862794f64434c49533162484f503131543165615a4d6b6d5433366a53765346684c7058654430506233456d386f6c2b6c49364d66537556474874664e466750587965586e6d6a576a3839572f6d43544d66344164785664544d397a42534f702f354c4470424f7975565a314b692f50314d365a6b6f33516e5362326334594a317068544f6a68737433706642356b744c48526f3251433376726b6953485a4862544638434b784b6b6363694e4946726c31456e746d676a695a704f76304f4753734a5a42564f6c6e447155483134676b68666b4c4a425647384658616967783059436c5759692f664257656a4358746b4e58726155517a46657542506b676e4f7739474c43714b6a706935636d52584b7647386463424b497143326c5a52384d3777496f36454c65587447493377313248357657714d627847555056712f675459626e447151313239764a464178416e4a575136436e656a416377756b77752f7a4a794142685850347a5966596f4c6c72476538737a5274577a55556748576a7449306b4a482b686c5059686b397a324e6b497975454968454d6e524336424a34625547536335724d5a6e6b6a676b6a2b7a4d2f552f796b485761644f6b556a674e6f6763356f2f6d587041386d51397a5a596f62677a44615a6f4f35746b6d794636364c4449797345636b4b576f5570417754644d4b6c584a472b6b41555234574b50694344496b6967706a2b547954684d6c7a454635434551692f4b716b4a7047752b7767367066422f496c55777a3534394139376b434939495274486646544664353758304a344345624e35346856785545746a437776543975414b6b2b677a72526a416951433754483553466464764f41392f5567696c765361426d344232524f78517449646c7a333373373434635735485a596c7477414a3550563543714e544f484435706861456179434a6e463568627838417369374c37734870724b7737366569725165532b6c425369786e7441396e465565706a4336486a4a446c746c4f377777515136595a43574c69697a67505a6a4336306731454c686d314444334a365a5641786c676b6830794b7a707a304e45746b43756a334177696a65777a666b70726b4d517250462f2f6a59726a7052556f6c46556f4d4146536b4549794234474e5a2b676d75726c41315950735378564a425638565241353643683650703839597677785651524a426e6530516a6878476d79555133587431316448527354374d51613664654c4b6e6348454c78486c7849376d663377647969565975706267396a4a6553434f4b46506745796263586f4b6b43346362456f4c364c71514636484b6e4979556750426935464b74656b6a565548614b334946475474436f524146644154424f494d4b365338346945776e733364747430414b734d3351687538426f55464a664549514a417a373579766f3942786b4231744d6b4f47306a463569787563366b437673632b71724235464a474462715651585a59624956773267784c645966446b4c396462596c51456a534853433473756953476d674e55677a6a434557496876487a47505364352b437752434446545a777245775365344e6a314c56477542316e485067307a6b72575333414f69325a414a456c416c4d61384b326862656c454336316b686449787a6b4a6f306e78384b5464344434764e4c374c3631424f742b6a7a63524665667a6348566b7335534450515a59702b617143644f49425778586c657041434c69543150694c5a767873746f686f7a5a684f6b4c53744c2b784149514c64556b5845684a57653354354f654a7a6a49304d393063764857676f6767344e52784143314264736c2b586f6a7965486e74624c635568477475576a51484e5243614f636d6245516431494f6738443468614a367a533555335a37616d6365437745386c58686c30444f4e706e564e39734b424c4f54304a7178516f795873344f3770564f496c78456b5138734c5770325a6a754e743542736a67617142554b384872434d55786a48626c435379326d446959534434304b5374376c5967477271492b645448793131746836587768554a52363773594e3631544d342f454351706c6a58532b426a4c78506c5242792f314b6b476c3353415131497148453857456776563153616f713141466e445236515a4752354f664d786436624b75795167796944657442786e48735852394565557153434a4679306a676130454f5765583977663469797150494e4a4d50413445555738764a4c554277734e56377a37494b7056303452336541344d70587659384a4d756c4865354e78516167486165306a4761386b707939455453384f4c67564c7a53414e4b37735267326f674f4162636d667735694b574d527951434f614e39777a30676a7263487153787930454a6442784b797575497555734e5754494273644957716563356355672b7444524a354134682f6c6335316a4975465668794d484a7367744a713141694854576a464d362f7353783844685033754a64326f45516274724d4331327a6952386175342b7171694256454c30356f4b78456b384b54516b516e48474a647964685a6938354a3574422b487350566c494a3548655a48387a3867326150673841627648454c6b42776155747149544673795058556366646d4c4f39456f6e6c5148636f426a4d5a3139753851646c72484b6d4b684a684d753075573137783367444e705562514c5a4c6a41324257714c6b7735437a7a45724b786a74574d6a626a41442b49713649515a49695a6c796f4e39503742537675387938516654474c7a764e674d77734d764a6d4e634579716668586b4c68566c4c4977687956634e766e7a2b4b556c5a504a67796a394b333239436758594650477141453131744d70576f54362f474d395378464854303963374e5652303870597a354d395a637a503930756b742f7971704b4d38395061596c2f4a37506b56376a6f547a2b415a366f6d5062764e674d306f33446c55654d4135787774507054385a545847307872394653753265426630394e6259654375755044316167527078394779576d4b30534f4655724e396e754a625851476a42714d6244783646474542717576456e4a4c686543364c6748352b4c5a72776c43656279524a4438574e594b41413065496d77464442474b756a2f3061376b634d6b49744e755249532b2b78486f79615146785430716948516779413530794548304c384e6b4550737858703438542b67536635692f4c62506f4b616258382b3256685049614a49534f4e5064432f5567754238335150626d3056386b7a4c75466c712f667846386235622b696f313336766158636b633234787669665865634a37334f666d6b42676b57787230396a38345978553361494f7041663731453249736a6e3162344834752b6e336c393037514c6146635779744f385643464468733350335871526c6b564f50474a667a396270425a374347377132735a524a2f513730543768412b474f7a5a7773394b4f62632b484e7a61457357516d4539412f2b52536d6836482f796a435675553536647a7435785663692f364a744b51436a2f5741626e6a78476a7a787248376231672b2b344e77437548747378577354545875772b616a766232374475474976584c5457443447595930304f6d3845453430622f4e6862344b4d734c66344f574e57685148795279643569592b4a6e4d7278595371705a336731354c5a52643773383736476e657746654c647370376b59543070382f6c793447303569702b4e304645336d7369645138494d727157576e6f4b6947553970766f41525064324131706d577649544157746b5a6851744d576f70573175687333364261493241744b317a54525733586245774579422b767a394b4c6c5664324463515376343373546d78632b6d4632414d7a6a7877374c336c3551325a31464665395143447576453450776e3132653435433845666c50682b4e5767745139376f2f7a7133496b476e685273787a34364e364676355a635236664b4c6c7667554c413634625a6657774f586d7347317a6555382b474f302f46636e4a48626f4e346a73694571626748516b6b566738532f4f3777505471362f6d7a43714351354e4d764270386d62376751555970635269437248572f4d6637553967614558456957346c6f617a75646d6776745345593353516a6a2f75504c3632395876736e336f342b637057447767496f556467495a3759506f476764476c63376c2f2f3536344166624f6e49414b59686a6964373451764d58527553746e7264426f486846553453644d494c73526b52577355384c476248484c65695a32745a44457234794c4b57506f626474486f527a626b63385450314149596f3255517471542f464c39394d666634353241357a326b395973333371636d7a624272666433506a387533436c635241464e6b35396967586d67706548567066445959736677574461743433372b64583468706241686f654167493254364e4a436e4c7961336747534d4e4e476d364b665747506f45434141487a376a644b6f2f44764476543134424d6f652b6c6c6a774834366d37453777466e41505152386b446f7839794d67726572335341424b474e793459584875392b414e313244374347636e6b4d554c32484c5968534f4a68495044527a6a2b7953664c767549434c555078306b4634566b4a6856664a6170536c45394a2b31742b3730727335316256357674376461684a5365737649484c567a4469494666624c7132444f7438506c75525333457356787a6546746b4c2f322b4e726e72536e7571456a4366743269493742686a56795a6532346c6b63473537736e392b45446d705a317447312b6133392b7376645641694a42682f693664317433676b44783274694d56437064432f456e4236745272317363796b775458373171326c58743376486e5279744b73554e644f636a415739572b433963656d4c694767512f555953526c67784d306a2b4c41696c393849647058375965527547726e5737503845437974777455557a4d37433948594338706f46353836703267396764776636485558776546555037413267706659462b614a7a682b3447515a4e5069712b344f4339385a365a54476f38594e7862542b5a735549496649634b2b49434e39412b5a7373775763756572796a7a33677831696a73306d39464a785458456f70556530624d4b7a53724651675575354e36347a384d68435232382b52426d574b6d33586935384242644f627274576a55422f327131424d486c39375553777a32334c49644349566c69756a74313874394965416374322b4e2f34663872376746423958654d4f39545457437a73485a743638626a53396d6264442f492f704738676a30336651423662766f45384c67483843364c4832346a4e52742f754141414141456c46546b5375516d4343554573444242514141674149414a4f62576b4c3737636e514b67454141466343414141524141414161485274624339305a573177624746305a53356a63335a6c6b734671784341516875384c2b773542384e6f58384a5453736f576c6157484c5873556b303052697449776a432f756776665a466571696162475070795a6c2f78742b5a4432754c30494e6f31417a69374e436d3877427451504b396f6a434c593741395742766d475643775479527a5635556145796443395158695351572f74723036547759305864566f7841755365414d443738344b3975786162616f6d336d4c5a70504b786a596b7a494945326e734159454858776732714231434259625a4e4a39596758315930654c4c42663761696a51584b3642392b4e6f5076683235416570757759525952756a486d774139767665445339675062795241714a3733654d31336c31486e7435576a38484b3449636c786979554b36644252664c38702b616548692f65477851636c714379554b456b38364b723453574a464f5344573676797352706d6242456c5a554e3135706e50484a4456716f4c74475559514f2b73764e47546877413366726c65457052547743762f7939463952497750474c70702b536c38692f6b5055457344424251414167414941503152714570636952533441675141414f6b4b4141414e4141414164475674634778686447557561485274624c565732323762526842397467482f773451462b78534b6c685558726b77527343326c4358774c616a56466e34776c4f535158705a624d37744b4b566652372b6c6c393657666b6f624e4c5368596c32306c366f51427035374a6e37694d474c3862585a394e66336b33677a6654794174373964487278396777637a2f642f48707a352f6e673662675376657674396d456f6d464e6538464b7a772f636d5641303675645458302f666c38337073506571584d2f4f6d506671356e7853752f4b4575467655516e5472693347786865474f544945714c4d4a3569685a6d4475652f6968356e636a35367755476f58324c706a49617061684133484447546b4a5770416e72307a76713356316a522b31396549593470784a68587230397562614f7a6f362f4e37725779544e645948685361336d794658674e7954786c62347645445442745369785576624343382f62322b3364474845666674766233556e4a6d4a657947532f75683341694f53746577687373376c447a6d4c304552616e794645716548692b5679565357367946455a5a47736d496f7663416a396f2b6f6a735835666d6a6a3448307a30757959472f3944454a70726e6d6654344e6e48325a477363524756795436546270766a32526a4f70585a4e35466c4747357a7a522b63686874533464386c596d4b45664f76736b30514b436c2f54576e5a4b6b354f4e78333449345650424e556d624a79777030573675463265347a4c6f6954366d3333374f42304541715a37736f746b7a4851597266557144465446424d51465532726b4e476c7a776a464b614d4d43726a514948756361506b556f7457525a784751506270414c754f524b525a4c566357375646456c544b37336b47684b637755546f6c4255464371496b7643394a563147764c6d715245532f443256392f77736b312f4147484134755172426d65637a516b69785a316868474b4868693333714d73617175706c70716f566e636e737144526f734f55705669414b744e53616d426955532b6f6277686a6233664d555732454e7a456958633865676568424549576e79456b2f54626c49624354726c694f754e5a4946534468756d6c2f55386c5038612b4e38344566554d704b775441726230416e595549714b4b69676c52716c7444465042627131663232665a50776d7467734b556275516355486b546667647465574d4b425358782b4377444a654f5234353479516b6e516461774842734139723030736f70374e554c71474339666e34367568532b6c53743132687278506239625a6e7a57587a76554f30366331476b6d7833382b46336d3931732b2f4b722b396d754d6a7471376441597568503945336e61626d7a61697873466f71456d70624162346e49346a5a6c486f5a667539512f3662657258725a424462645146707472346f5755704d724f4a4d786168306b6a7a4d445472784c49446e7744435651374a614269594c51537455666348575663566970544a79465451394b506e7654615535356d6d6f69434d76686e78454470312b6f38446f4d6e5479497650422f43453065364f57645734662b53456267666233617248383650773553466373646e7a6e6d2f737753412f494e394b4b6569652b3632495648587347677a796b43526637715a566554364f725a6c754a736f4b6e682b613144356d78792f4835446b7a73454c39697279646941584c433170766378626e4373585453567a483338356d50335162714e73484b4a504c2f754d416e33567779384e7a73364c6c762f6175675847444c63656151767074706c6372734a4773747542713831365557646e2b6a3151696336425a5838734a7463634e704a3248646d7a62616956616539456f4b2f4f65346264764949463945517a2f426c424c4151495541416f41416741414145565371456f414141414141414141414141414141414641414141414141414141414145414141414141414141426f644731734c31424c4151495541416f4141674141414f70396b306c7873784f6175515541414c6b46414141524141414141414141414141414941414141434d414141426f644731734c304a6a5332524f636a55314c6d70775a31424c4151495541416f41416741414147466d6b306c422f466f326c413441414a514f4141415441414141414141414141414149414141414173474141426f644731734c3078765a3238675647466d5a5777756347356e554573424168514146414143414167416b35746151767674796441714151414156774941414245414141414141414141415141674141414130425141414768306257777664475674634778686447557559334e32554573424168514146414143414167412f56476f536c794a464c67434241414136516f4141413041414141414141414141514167414141414b5259414148526c625842735958526c4c6d683062577851537755474141414141415541425141744151414156686f41414a63415158563062334939556d396961573467516d566a613256794451704f5957316c50555679624746755a3256755a5849675647466d5a57774e436c5a6c636e4e70623234394d6a41784e7a41314d4467784d4445344e44454e436c52356347553961335a775158567a643256706377304b5247566d595856736445563464443075614852746241304b526d6c735a5652356347567a5055685554557774524746305a576b674b436f756148527462436c384b69356f644731736641304b5547467a63336476636e51394d413d3d),
(26, 'kvpListe', 'Kundenliste', 'Tafel Projektteam', '1', 'HTML-Datei (*.html)|*.html', '.html', 0, 1, 0x3c212d2d0a4175746f723d546166656c2050726f6a656b747465616d0a4e616d653d4b756e64656e6c697374650a56657273696f6e3d310a547970653d6b76704c697374650a46696c6554797065733d48544d4c2d446174656920282a2e68746d6c297c2a2e68746d6c0a50617373776f72743d300a44656661756c744578743d2e68746d6c0a2d2d3e0a0a3c21444f43545950452048544d4c205055424c494320222d2f2f5733432f2f4454442048544d4c20342e3031205472616e736974696f6e616c2f2f454e222022687474703a2f2f7777772e77332e6f72672f54522f68746d6c342f6c6f6f73652e647464223e0a3c68746d6c3e3c686561643e0a0a3c6d65746120687474702d65717569763d22436f6e74656e742d4c616e67756167652220636f6e74656e743d226465223e0a3c6d65746120687474702d65717569763d22436f6e74656e742d547970652220636f6e74656e743d22746578742f68746d6c3b20636861727365743d49534f2d383835392d31223e0a3c7469746c653e4b756e64656e6c697374653c2f7469746c653e0a2020202020203c7374796c6520747970653d22746578742f637373223e0a202020202020626f64790a2020202020207b0a2020202020202020666f6e742d66616d696c793a76657264616e613b0a2020202020207d0a20202020202074640a2020202020207b0a2020202020202020626f726465722d7374796c653a20736f6c69643b0a2020202020202020626f726465722d77696474683a203170783b0a2020202020202020666f6e742d66616d696c793a76657264616e613b0a2020202020202020666f6e742d73697a653a20313270783b0a2020202020207d0a2020202020202e746162656c6c650a2020202020207b0a202020202020202077696474683a203138636d3b0a2020202020202020626f726465722d7374796c653a20736f6c69643b0a2020202020202020626f726465722d77696474683a203370783b0a2020202020202020666f6e742d66616d696c793a76657264616e613b0a2020202020202020666f6e742d73697a653a20313270783b0a2020202020207d0a2020202020202e7a65696c650a2020202020207b0a20202020202020202020626f726465722d7374796c653a20736f6c69643b0a20202020202020202020626f726465722d77696474683a203170783b0a20202020202020202020666f6e742d66616d696c793a76657264616e613b0a20202020202020202020666f6e742d73697a653a20313270783b0a2020202020207d0a2020202020202e7a65696c655f6b6c65696e0a2020202020207b0a20202020202020202020626f726465722d7374796c653a20736f6c69643b0a20202020202020202020626f726465722d77696474683a203170783b0a20202020202020202020666f6e742d66616d696c793a76657264616e613b0a20202020202020202020666f6e742d73697a653a20313070783b0a2020202020207d0a3c2f7374796c653e0a3c2f686561643e0a3c626f64793e0a0a202020203c7461626c652077696474683d2237383222207374796c653d2277696474683a2032302e35636d3b20666f6e742d73697a653a20313270783b223e0a20202020202020203c74626f64793e0a2020202020202020202020203c74723e0a20202020202020202020202020203c7464207374796c653d22626f726465723a206e6f6e65222077696474683d22333939223e3c7374726f6e673e4b756e64656e6c6973746520662675756d6c3b723a203c2f7374726f6e673e203c7370616e2069643d22646973747269627574696f6e506f696e74223e3c2f7370616e3e203c2f74643e0a20202020202020202020202020203c7464207374796c653d22626f726465723a206e6f6e65222077696474683d223337312220616c69676e3d227269676874223e3c7374726f6e673e446174756d3a203c2f7374726f6e673e203c7370616e2069643d2264617465546f646179223e3c2f7370616e3e3c2f74643e0a2020202020202020202020203c2f74723e0a20202020202020203c74723e0a2020202020202020202020203c7464207374796c653d22626f726465723a206e6f6e65223e3c7374726f6e673e576172656e7479703a203c2f7374726f6e673e203c7370616e2069643d2270726f6475637454797065223e3c2f7370616e3e3c2f74643e0a20202020202020203c2f74723e0a20202020202020203c2f74626f64793e0a202020203c2f7461626c653e0a0a3c7461626c652069643d227461626c652220636c6173733d22746162656c6c65222077696474683d2237383222207374796c653d2277696474683a2032302e35636d3b22203e0a20203c74626f64793e0a202020203c74723e0a2020202020203c746420636c6173733d227a65696c655f6b6c65696e22207374796c653d2277696474683a20333070783b223e3c7374726f6e673e4e722e3c2f7374726f6e673e3c2f74643e0a2020202020203c746420636c6173733d227a65696c655f6b6c65696e22207374796c653d2277696474683a20343070783b223e3c7374726f6e673e4b756e642e4e722e3c2f7374726f6e673e3c2f74643e0a2020202020203c746420636c6173733d227a65696c655f6b6c65696e22207374796c653d2277696474683a20373170783b223e3c7374726f6e673e4e616d652c3c62723e566f726e616d653c2f7374726f6e673e3c2f74643e0a2020202020203c746420636c6173733d227a65696c655f6b6c65696e22207374796c653d2277696474683a2031323070783b223e3c7374726f6e673e416472657373653c2f7374726f6e673e3c2f74643e0a2020202020203c746420636c6173733d227a65696c655f6b6c65696e223e3c7374726f6e673e4572772e3c2f7374726f6e673e3c2f74643e0a2020202020203c746420636c6173733d227a65696c655f6b6c65696e223e3c7374726f6e673e4b696e2e3c2f7374726f6e673e3c2f74643e0a2020202020203c746420636c6173733d227a65696c655f6b6c65696e223e3c7374726f6e673e417573676162652d203c62723e6772757070653c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c655f6b6c65696e223e3c7374726f6e673e7a75203c62723e5a61686c656e3c2f7374726f6e673e3c2f74643e0a2020202020203c746420636c6173733d227a65696c655f6b6c65696e22207374796c653d2277696474683a20373070783b223e3c7374726f6e673e4b6f6e746f73742e3c2f7374726f6e673e3c2f74643e0a2020202020203c746420636c6173733d227a65696c655f6b6c65696e22207374796c653d2277696474683a20373070783b223e3c7374726f6e673e4c65747a7465723c62723e45696e6b6175663c2f7374726f6e673e3c2f74643e0a2020202020203c746420636c6173733d227a65696c655f6b6c65696e22207374796c653d2277696474683a2031303070783b223e3c7374726f6e673e42656d65726b756e67656e3c2f7374726f6e673e3c2f74643e0a202020203c2f74723e0a20203c2f74626f64793e0a3c2f7461626c653e0a0a202020203c7020616c69676e3d226c656674223e0a2020202020203c666f6e742073697a653d2232223e476573616d742d446174656e732661756d6c3b747a6520662675756d6c3b72203c7370616e202069643d22646973747269627574696f6e506f696e74486f757365686f6c6473223e3c2f7370616e3e3a203c7370616e2069643d226e756d6265724f66486f757365686f6c6473223e3c2f7370616e3e3c2f666f6e743e0a202020203c2f703e0a202020203c7020616c69676e3d226c656674223e0a2020202020203c666f6e742073697a653d2232223e0a20202020202020204b756e64656e616e7a61686c2045727761636873656e6520662675756d6c3b72203c7370616e2069643d22646973747269627574696f6e506f696e744164756c7473223e3c2f7370616e3e3a203c7370616e2069643d22746f74616c4164756c7473223e3c2f7370616e3e3c62723e0a20202020202020204b756e64656e616e7a61686c204b696e64657220662675756d6c3b72203c7370616e2069643d22646973747269627574696f6e506f696e744368696c6472656e223e3c2f7370616e3e3a203c7370616e2069643d22746f74616c4368696c6472656e223e3c2f7370616e3e203c62723e0a2020202020202020506572736f6e656e676573616d747a61686c20662675756d6c3b72203c7370616e2069643d22646973747269627574696f6e506f696e74546f74616c506572736f6e73223e3c2f7370616e3e3a203c7370616e2069643d22746f74616c506572736f6e73223e3c2f7370616e3e0a2020202020203c2f666f6e743e0a202020203c2f703e0a0a3c2f626f64793e0a0a0a3c7363726970743e0a202020207661722064617465203d206e6577204461746528293b0a202020207661722079656172203d20646174652e67657446756c6c5965617228293b0a20202020766172206d6f6e7468203d20646174652e6765744d6f6e74682829202b20313b0a2020202076617220646179203d20646174652e6765744461746528293b0a202020207661722064617465537472696e67203d20646179202b20222e22202b206d6f6e7468202b20222e22202b20796561723b0a20202020646f63756d656e742e676574456c656d656e7442794964282264617465546f64617922292e696e6e657248544d4c203d2064617465537472696e673b0a0a2020202066756e6374696f6e20616464526f7728637573746f6d657249642c207375724e616d652c2066697273744e616d652c2062697274686461792c207374726565742c20686f7573656e756d6265722c20706f7374636f64652c206c6f636174696f6e2c0a20202020202020202020202020202020202020206e756d6265724164756c74732c206e756d6265724368696c6472656e2c206f757470757447726f75702c207061792c202073616c646f2c20706f736974697653616c646f2c206c61737453686f7070696e67446174652c0a2020202020202020202020202020202020202020637573746f6d65724973426c6f636b65642c206465636973696f6e2c20636f6d6d656e74290a202020207b0a2020202020202020766172207461626c65203d20646f63756d656e742e676574456c656d656e744279496428227461626c6522293b0a202020202020202076617220726f77203d207461626c652e696e73657274526f77287461626c652e726f77732e6c656e677468293b0a0a2020202020202020726f772e696e7365727443656c6c2830292e696e6e657248544d4c203d20287461626c652e726f77732e6c656e677468202d31292e746f537472696e6728293b0a2020202020202020726f772e696e7365727443656c6c2831292e696e6e657248544d4c203d20637573746f6d657249643b0a2020202020202020726f772e696e7365727443656c6c2832292e696e6e657248544d4c203d207375724e616d65202b20222c2022202b2066697273744e616d65202b2022202822202b206269727468646179202b202229223b0a2020202020202020726f772e696e7365727443656c6c2833292e696e6e657248544d4c203d20737472656574202b20222022202b20686f7573656e756d626572202b20222c2022202b20706f7374636f6465202b20222022202b206c6f636174696f6e3b0a2020202020202020726f772e696e7365727443656c6c2834292e696e6e657248544d4c203d206e756d6265724164756c74733b0a2020202020202020726f772e696e7365727443656c6c2835292e696e6e657248544d4c203d206e756d6265724368696c6472656e3b0a2020202020202020726f772e696e7365727443656c6c2836292e696e6e657248544d4c203d206f757470757447726f75703b0a2020202020202020726f772e696e7365727443656c6c2837292e696e6e657248544d4c203d207061793b0a20202020202020206966287061727365496e7428706f736974697653616c646f29203d3d2031290a20202020202020207b0a202020202020202020202020726f772e696e7365727443656c6c2838292e696e6e657248544d4c203d2073616c646f3b0a20202020202020207d656c73650a2020202020202020202020207b0a20202020202020202020202020202020726f772e696e7365727443656c6c2838292e696e6e657248544d4c203d20223c7374726f6e673e3c7370616e207374796c653d27636f6c6f723a2023666630303030273e22202b2073616c646f202b20223c2f7370616e3e3c2f7374726f6e673e223b0a2020202020202020202020207d0a0a0a2020202020202020726f772e696e7365727443656c6c2839292e696e6e657248544d4c203d206c61737453686f7070696e67446174653b0a2020202020202020726f772e696e7365727443656c6c283130292e696e6e657248544d4c203d20223c7370616e207374796c653d27666f6e742d73697a653a2031307078273e3c7374726f6e673e203c7370616e207374796c653d27636f6c6f723a2023666630303030273e22202b637573746f6d65724973426c6f636b6564202b2020223c2f7370616e3e203c62723e22202b206465636973696f6e202b20223c2f7374726f6e673e203c62723e22202b20636f6d6d656e74202b20223c2f7370616e3e223b0a202020207d0a0a2020202066756e6374696f6e20736574506172616d6574657228646973747269627574696f6e506f696e742c2070726f64756374547970652c20746f74616c486f7573686f6c64732c20746f74616c4164756c74732c20746f74616c4368696c6472656e2c20746f74616c506572736f6e73290a202020207b0a20202020202020202020646f63756d656e742e676574456c656d656e74427949642822646973747269627574696f6e506f696e74486f757365686f6c647322292e696e6e657248544d4c203d20646973747269627574696f6e506f696e743b0a20202020202020202020646f63756d656e742e676574456c656d656e74427949642822646973747269627574696f6e506f696e7422292e696e6e657248544d4c203d20646973747269627574696f6e506f696e743b0a20202020202020202020646f63756d656e742e676574456c656d656e74427949642822646973747269627574696f6e506f696e744164756c747322292e696e6e657248544d4c203d20646973747269627574696f6e506f696e743b0a20202020202020202020646f63756d656e742e676574456c656d656e74427949642822646973747269627574696f6e506f696e744368696c6472656e22292e696e6e657248544d4c203d20646973747269627574696f6e506f696e743b0a20202020202020202020646f63756d656e742e676574456c656d656e74427949642822646973747269627574696f6e506f696e74546f74616c506572736f6e7322292e696e6e657248544d4c203d20646973747269627574696f6e506f696e743b0a20202020202020202020646f63756d656e742e676574456c656d656e7442794964282270726f647563745479706522292e696e6e657248544d4c203d2070726f64756374547970653b0a20202020202020202020646f63756d656e742e676574456c656d656e744279496428226e756d6265724f66486f757365686f6c647322292e696e6e657248544d4c203d20746f74616c486f7573686f6c64733b0a20202020202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c4164756c747322292e696e6e657248544d4c203d20746f74616c4164756c74733b0a20202020202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c4368696c6472656e22292e696e6e657248544d4c203d20746f74616c4368696c6472656e3b0a20202020202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c506572736f6e7322292e696e6e657248544d4c203d20746f74616c506572736f6e733b0a202020207d0a3c2f7363726970743e0a0a3c736372697074207372633d2268747470733a2f2f636f64652e6a71756572792e636f6d2f6a71756572792d332e332e312e736c696d2e6d696e2e6a73220a202020202020202020202020202020696e746567726974793d227368613338342d7138692f582b393635447a4f3072543761624b34314a537451494171566752567a70627a6f35736d584b703459665276482b386162745445315069366a697a6f220a20202020202020202020202020202063726f73736f726967696e3d22616e6f6e796d6f7573223e3c2f7363726970743e0a3c7363726970743e0a202020242866756e6374696f6e2028290a202020207b0a2020202020202077696e646f772e7072696e7428293b0a202020207d293b0a093c2f7363726970743e0a0a25504c414345484f4c4445525f464f525f46554e4354494f4e53250a3c62723e0a3c62723e0a3c2f68746d6c3e0a25454e445f4f465f46494c4525);
INSERT INTO `vorlage` (`vorlageId`, `templateType`, `name`, `autor`, `fileVersion`, `fileTypes`, `defaultText`, `passwort`, `aktiv`, `daten`) VALUES
(32, 'kvpKassenabrechnung', 'Kassenabrechnung (erweitert)', 'Tafel Projektteam', '1', 'HTML-Datei (*.html)|*.html', '.html', 0, 1, 0x3c212d2d0a4175746f723d546166656c2050726f6a656b747465616d0a4e616d653d4b617373656e6162726563686e756e672028657277656974657274290a56657273696f6e3d310a547970653d6b76704b617373656e6162726563686e756e670a46696c6554797065733d48544d4c2d446174656920282a2e68746d6c297c2a2e68746d6c0a50617373776f72743d300a44656661756c744578743d2e68746d6c0a2d2d3e0a0a3c21444f43545950452068746d6c205055424c494320222d2f2f5733432f2f4454442048544d4c20342e3031205472616e736974696f6e616c2f2f454e222022687474703a2f2f7777772e77332e6f72672f54522f68746d6c342f6c6f6f73652e647464223e0a3c68746d6c3e3c686561643e0a0a202020203c6d65746120687474702d65717569763d22436f6e74656e742d4c616e67756167652220636f6e74656e743d226465223e0a202020203c6d65746120687474702d65717569763d22436f6e74656e742d547970652220636f6e74656e743d22746578742f68746d6c3b20636861727365743d49534f2d383835392d31223e0a202020203c7469746c653e4b617373656e6162726563686e756e673c2f7469746c653e0a202020203c7374796c6520747970653d22746578742f637373223e0a2020202020202020626f6479207b0a20202020202020202020202077696474683a203137636d3b0a202020202020202020202020666f6e742d66616d696c793a76657264616e613b0a202020202020202020202020666f6e742d73697a653a20313270783b0a20202020202020207d0a20202020202020202e746162656c6c65207b0a20202020202020202020202077696474683a203137636d3b0a202020202020202020202020626f726465722d7374796c653a20736f6c69643b0a202020202020202020202020626f726465722d77696474683a203370783b0a20202020202020207d0a20202020202020202e7a65696c65207b0a20202020202020202020202077696474683a203137636d3b0a202020202020202020202020626f726465722d7374796c653a20736f6c69643b0a202020202020202020202020626f726465722d77696474683a203170783b0a202020202020202020202020666f6e742d73697a653a20736d616c6c3b0a20202020202020207d0a0a202020202020202074640a20202020202020207b0a20202020202020202020202077696474683a203137636d3b0a202020202020202020202020626f726465722d7374796c653a20736f6c69643b0a202020202020202020202020626f726465722d77696474683a203170783b0a202020202020202020202020666f6e742d73697a653a20736d616c6c3b0a20202020202020207d0a0a202020202020202023637573746f6d65724f766572766965770a20202020202020207b0a202020202020202020202020706f736974696f6e3a206162736f6c7574653b0a202020202020202020202020746f703a20343070783b0a2020202020202020202020206c6566743a2033343570783b0a0a20202020202020202020202077696474683a2033303070783b0a20202020202020207d0a202020203c2f7374796c653e0a3c2f686561643e0a0a3c626f64793e0a202020203c7461626c65207374796c653d2277696474683a20313030253b22203e0a20202020202020203c74723e0a2020202020202020202020203c7464207374796c653d22626f726465723a206e6f6e65223e3c7374726f6e673e4b617373656e6162726563686e756e673c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c7464207374796c653d22626f726465723a206e6f6e652220616c69676e3d227269676874223e3c7374726f6e673e447275636b646174756d3a203c2f7374726f6e673e203c7370616e2069643d2264617465546f646179223e3c2f7370616e3e3c2f74643e0a20202020202020203c2f74723e0a202020203c2f7461626c653e0a0a202020203c703e0a20202020202020203c7374726f6e673e417573647275636b2064757263683a203c2f7374726f6e673e203c7370616e2069643d22656d706c6f796565223e3c2f7370616e3e0a20202020202020203c62723e0a20202020202020203c7374726f6e673e5665727465696c7374656c6c653a203c2f7374726f6e673e203c7370616e2069643d22646973747269627574696f6e506f696e74223e3c2f7370616e3e0a20202020202020203c62723e0a20202020202020203c7374726f6e673e4c697374656e6e756d6d65723a203c2f7374726f6e673e203c7370616e2069643d226c6973744e756d626572223e3c2f7370616e3e0a20202020202020203c62723e0a20202020202020203c7374726f6e673e4162726563686e756e6720766f6d3a203c2f7374726f6e673e203c7370616e2069643d22706572696f6465223e3c2f7370616e3e0a202020203c2f703e0a202020203c62723e0a0a202020203c6669656c647365742069643d22637573746f6d65724f76657276696577223e0a20202020202020203c6c6567656e643e4b756e64656e2675756d6c3b626572736963687420766f6d3a203c7370616e2069643d2264617465223e3c2f7370616e3e3c2f6c6567656e643e0a20202020202020203c7374726f6e673e45727761636873656e653a203c2f7374726f6e673e203c7370616e2069643d22637573746f6d65724164756c7473223e303c2f7370616e3e0a20202020202020203c62723e0a20202020202020203c7374726f6e673e4b696e6465723a203c2f7374726f6e673e203c7370616e2069643d22637573746f6d65724368696c6472656e223e303c2f7370616e3e0a20202020202020203c62723e0a20202020202020203c7374726f6e673e20476573616d743a203c2f7374726f6e673e203c7370616e2069643d22637573746f6d6572546f74616c223e303c2f7370616e3e0a202020203c2f6669656c647365743e0a0a202020203c703e0a20202020202020203c7374726f6e673e556d732661756d6c3b747a652c20686575746520676562756368743a3c2f7374726f6e673e0a202020203c2f703e0a202020203c7461626c6520636c6173733d22746162656c6c65222069643d2273616c6573546f646179223e0a20202020202020203c74723e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20333070783b223e3c7374726f6e673e4e722e3c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373770783b223e3c7374726f6e673e446174756d3c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373170783b223e3c7374726f6e673e4b2e2d4e722e202f204f7274736b2e2d4e722e3c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a2031323070783b223e3c7374726f6e673e566f726e616d653c62723e4e6163686e616d653c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e576172656e7479703c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373170783b223e3c7374726f6e673e53756d6d653c62723e45696e6b6175663c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e53756d6d653c62723e5a61686c756e673c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e4b6f6e746f7374616e643c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e5665727465696c7374656c6c653c2f7374726f6e673e3c2f74643e0a20202020202020203c2f74723e0a202020203c2f7461626c653e0a0a202020203c703e0a202020202020202053756d6d652045696e6b2661756d6c3b7566653a203c7374726f6e673e203c7370616e2069643d22746f74616c53686f7070696e6753616c6573546f646179223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e0a20202020202020203c62723e0a202020202020202053756d6d65205a61686c756e67656e3a203c7374726f6e673e203c7370616e2069643d22746f74616c5061796d656e7453616c6573546f646179223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e0a202020203c2f703e0a202020203c68723e0a0a202020203c703e0a20202020202020203c7374726f6e673e556d732661756d6c3b747a652c206865757465206765627563687420756e64206865757465207769656465722073746f726e696572743a3c2f7374726f6e673e0a202020203c2f703e0a202020203c7461626c6520636c6173733d22746162656c6c65222069643d2273616c657343616e63656c6564546f646179223e0a20202020202020203c74723e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20333070783b223e3c7374726f6e673e4e722e3c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373770783b223e3c7374726f6e673e446174756d3c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373170783b223e3c7374726f6e673e4b2e2d4e722e202f204f7274736b2e2d4e723c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a2031323070783b223e3c7374726f6e673e566f726e616d653c62723e4e6163686e616d653c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e576172656e7479703c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373170783b223e3c7374726f6e673e53756d6d653c62723e45696e6b6175663c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e53756d6d653c62723e5a61686c756e673c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e4b6f6e746f7374616e643c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e5665727465696c7374656c6c653c2f7374726f6e673e3c2f74643e0a20202020202020203c2f74723e0a202020203c2f7461626c653e0a202020203c703e0a202020202020202053756d6d6520556d732661756d6c3b747a653a203c7374726f6e673e3c7370616e2069643d22746f74616c53686f70696e6753616c657343616e63656c6564546f646179223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e0a20202020202020203c62723e0a202020202020202053756d6d65205a61686c756e67656e3a203c7374726f6e673e203c7370616e2069643d22746f74616c5061796d656e7453616c657343616e63656c6564546f646179223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e0a202020203c2f703e0a202020203c68723e0a0a202020203c703e0a20202020202020203c7374726f6e673e556d732661756d6c3b747a652c2066722675756d6c3b686572206765627563687420756e642068657574652073746f726e696572743a3c2f7374726f6e673e0a202020203c2f703e0a202020203c7461626c6520636c6173733d22746162656c6c65222069643d2273616c65734561726c69657243616e63656c6564546f646179223e0a20202020202020203c74723e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20333070783b223e3c7374726f6e673e4e722e3c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373770783b223e3c7374726f6e673e446174756d3c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373170783b223e3c7374726f6e673e4b2e2d4e722e202f204f7274736b2e2d4e723c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a2031323070783b223e3c7374726f6e673e566f726e616d653c62723e4e6163686e616d653c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e576172656e7479703c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373170783b223e3c7374726f6e673e53756d6d653c62723e45696e6b6175663c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e53756d6d653c62723e5a61686c756e673c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e4b6f6e746f7374616e643c2f7374726f6e673e3c2f74643e0a2020202020202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e5665727465696c7374656c6c653c2f7374726f6e673e3c2f74643e0a20202020202020203c2f74723e0a202020203c2f7461626c653e0a0a202020203c703e0a202020202020202053756d6d6520556d732661756d6c3b747a653a203c7374726f6e673e203c7370616e2069643d22746f74616c53686f70696e6753616c65734561726c69657243616e63656c6564546f646179223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e3c62723e0a202020202020202053756d6d65205a61686c756e67656e3a203c7374726f6e673e203c7370616e2069643d22746f74616c5061796d656e7453616c65734561726c69657243616e63656c6564546f646179223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e0a202020203c2f703e0a3c703e0a202020203c62723e0a202020203c7374726f6e673e20446572204b617373656e62657374616e6420626574722661756d6c3b67743a203c7370616e2069643d2263617368496e48616e64223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e0a3c2f703e0a0a3c2f626f64793e0a0a0a3c7363726970743e0a202020207661722064617465203d206e6577204461746528293b0a202020207661722079656172203d20646174652e67657446756c6c5965617228293b0a20202020766172206d6f6e7468203d20646174652e6765744d6f6e74682829202b20313b0a2020202076617220646179203d20646174652e6765744461746528293b0a202020207661722064617465537472696e67203d20646179202b20222e22202b206d6f6e7468202b20222e22202b20796561723b0a20202020646f63756d656e742e676574456c656d656e7442794964282264617465546f64617922292e696e6e657248544d4c203d2064617465537472696e673b0a0a20202066756e6374696f6e20736574506172616d6574657228646174652c20656d706c6f7965652c20646973747269627574696f6e506f696e742c206c6973744e756d6265722c20706572696f6465290a2020207b0a20202020202020646f63756d656e742e676574456c656d656e744279496428226461746522292e20696e6e657248544d4c203d20646174653b0a20202020202020646f63756d656e742e676574456c656d656e74427949642822656d706c6f79656522292e20696e6e657248544d4c203d20656d706c6f7965653b0a20202020202020646f63756d656e742e676574456c656d656e74427949642822646973747269627574696f6e506f696e7422292e20696e6e657248544d4c203d20646973747269627574696f6e506f696e743b0a20202020202020646f63756d656e742e676574456c656d656e744279496428226c6973744e756d62657222292e20696e6e657248544d4c203d206c6973744e756d6265723b0a20202020202020646f63756d656e742e676574456c656d656e74427949642822706572696f646522292e20696e6e657248544d4c203d20706572696f64653b0a2020207d0a0a2020202066756e6374696f6e20616464526f77287461626c6549642c206e756d6265722c20646174652c20686f757365686f6c6449642c206e616d652c2070726f64756374547970652c20746f74616c53686f7070696e672c20746f74616c5061796d656e742c2062616e6b42616c616e63652c20646973747269627574696f6e506f696e742c20686f757365686f6c644469726563746f722c20746578742c206164756c74290a202020207b0a2020202020202020766172207461626c65203d20646f63756d656e742e676574456c656d656e7442794964287461626c654964293b0a2020202020202020766172206669727374526f77203d207461626c652e696e73657274526f77287461626c652e726f77732e6c656e677468293b0a2020202020202020766172207365636f6e64526f77203d207461626c652e696e73657274526f77287461626c652e726f77732e6c656e677468293b0a0a20202020202020207661722063656c6c4e756d626572203d206669727374526f772e696e7365727443656c6c2830293b0a202020202020202063656c6c4e756d6265722e726f775370616e203d20323b0a202020202020202063656c6c4e756d6265722e696e6e657248544d4c203d206e756d6265723b0a0a20202020202020206669727374526f772e696e7365727443656c6c2831292e696e6e657248544d4c203d20646174653b0a20202020202020206669727374526f772e696e7365727443656c6c2832292e696e6e657248544d4c203d20686f757365686f6c6449643b0a20202020202020206669727374526f772e696e7365727443656c6c2833292e696e6e657248544d4c203d206e616d653b0a20202020202020206669727374526f772e696e7365727443656c6c2834292e696e6e657248544d4c203d2070726f64756374547970653b0a20202020202020206669727374526f772e696e7365727443656c6c2835292e696e6e657248544d4c203d20746f74616c53686f7070696e673b0a20202020202020206669727374526f772e696e7365727443656c6c2836292e696e6e657248544d4c203d20746f74616c5061796d656e743b0a20202020202020206669727374526f772e696e7365727443656c6c2837292e696e6e657248544d4c203d2062616e6b42616c616e63653b0a20202020202020206669727374526f772e696e7365727443656c6c2838292e696e6e657248544d4c203d20646973747269627574696f6e506f696e743b0a0a20202020202020207661722063656c6c486f7573686f6c644469726563746f72203d207365636f6e64526f772e696e7365727443656c6c2830293b0a202020202020202063656c6c486f7573686f6c644469726563746f722e636f6c5370616e3d343b0a202020202020202063656c6c486f7573686f6c644469726563746f722e696e6e657248544d4c203d20223c7374726f6e673e42756368756e6720662675756d6c3b72203c2f7374726f6e673e22202b20686f757365686f6c644469726563746f723b0a0a20202020202020207661722063656c6c54657874203d207365636f6e64526f772e696e7365727443656c6c2831293b0a202020202020202063656c6c546578742e636f6c5370616e203d20343b0a202020202020202063656c6c546578742e696e6e657248544d4c203d20746578743b0a0a20202020202020206966287461626c654964203d3d202273616c6573546f64617922290a20202020202020207b0a202020202020202020202020616464437573746f6d65724f76657276696577286164756c74293b0a20202020202020207d0a202020207d0a0a2020202066756e6374696f6e20616464437573746f6d65724f76657276696577286164756c74290a202020207b0a2020202020202020766172206e756d626572546f74616c203d207061727365496e7428646f63756d656e742e676574456c656d656e74427949642822637573746f6d6572546f74616c22292e696e6e657248544d4c29202b20313b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642822637573746f6d6572546f74616c22292e696e6e657248544d4c203d206e756d626572546f74616c2e746f537472696e6728293b0a20202020202020206966287061727365496e74286164756c7429203d3d2031290a20202020202020207b0a202020202020202020202020766172206e756d6265724164756c7473203d207061727365496e7428646f63756d656e742e676574456c656d656e74427949642822637573746f6d65724164756c747322292e696e6e657248544d4c29202b20313b0a202020202020202020202020646f63756d656e742e676574456c656d656e74427949642822637573746f6d65724164756c747322292e696e6e657248544d4c203d206e756d6265724164756c74732e746f537472696e6728293b0a20202020202020207d656c73650a2020202020202020202020207b0a20202020202020202020202020202020766172206e756d6265724368696c6472656e203d207061727365496e7428646f63756d656e742e676574456c656d656e74427949642822637573746f6d65724368696c6472656e22292e696e6e657248544d4c29202b20313b0a20202020202020202020202020202020646f63756d656e742e676574456c656d656e74427949642822637573746f6d65724368696c6472656e22292e696e6e657248544d4c203d206e756d6265724368696c6472656e2e746f537472696e6728293b0a2020202020202020202020207d0a202020207d0a0a2020202066756e6374696f6e207365744361736869657256616c75652876616c7565290a202020207b0a2020202020202020646f63756d656e742e676574456c656d656e7442794964282263617368496e48616e6422292e696e6e657248544d4c203d2076616c75653b0a202020207d0a0a2020202066756e6374696f6e20736574546f74616c53686f7070696e675061796d656e7453616c6573546f64617928746f74616c53686f7070696e672c20746f74616c5061796d656e74290a202020207b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c53686f7070696e6753616c6573546f64617922292e696e6e657248544d4c203d20746f74616c53686f7070696e673b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c5061796d656e7453616c6573546f64617922292e696e6e657248544d4c203d20746f74616c5061796d656e743b0a202020207d0a0a20202066756e6374696f6e20736574546f74616c53686f7070696e675061796d656e7453616c657343616e63656c6564546f64617928746f74616c53686f7070696e672c20746f74616c5061796d656e74290a2020207b0a20202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c53686f70696e6753616c657343616e63656c6564546f64617922292e696e6e657248544d4c203d20746f74616c53686f7070696e673b0a20202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c5061796d656e7453616c657343616e63656c6564546f64617922292e696e6e657248544d4c203d20746f74616c5061796d656e743b0a2020207d0a0a20202066756e6374696f6e20736574546f74616c53686f7070696e675061796d656e7453616c65734561726c69657243616e63656c6564546f64617928746f74616c53686f7070696e672c20746f74616c5061796d656e74290a2020207b0a20202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c53686f70696e6753616c65734561726c69657243616e63656c6564546f64617922292e696e6e657248544d4c203d20746f74616c53686f7070696e673b0a20202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c5061796d656e7453616c65734561726c69657243616e63656c6564546f64617922292e696e6e657248544d4c203d20746f74616c5061796d656e743b0a2020207d0a3c2f7363726970743e0a0a3c736372697074207372633d2268747470733a2f2f636f64652e6a71756572792e636f6d2f6a71756572792d332e332e312e736c696d2e6d696e2e6a73220a202020202020202020202020202020696e746567726974793d227368613338342d7138692f582b393635447a4f3072543761624b34314a537451494171566752567a70627a6f35736d584b703459665276482b386162745445315069366a697a6f220a20202020202020202020202020202063726f73736f726967696e3d22616e6f6e796d6f7573223e3c2f7363726970743e0a3c7363726970743e0a202020242866756e6374696f6e2028290a202020207b0a2020202020202077696e646f772e7072696e7428293b0a202020207d293b0a093c2f7363726970743e0a0a25504c414345484f4c4445525f464f525f46554e4354494f4e53250a0a3c2f68746d6c3e0a25454e445f4f465f46494c4525),
(33, 'kvpKassenabrechnung', 'Kassenabrechnung', 'Tafel Projektteam', '1', 'HTML-Datei (*.html)|*.html', '.html', 0, 1, 0x3c212d2d0a4175746f723d546166656c2050726f6a656b747465616d0a4e616d653d4b617373656e6162726563686e756e670a56657273696f6e3d310a547970653d6b76704b617373656e6162726563686e756e670a46696c6554797065733d48544d4c2d446174656920282a2e68746d6c297c2a2e68746d6c0a50617373776f72743d300a44656661756c744578743d2e68746d6c0a2d2d3e0a0a3c21444f43545950452068746d6c205055424c494320222d2f2f5733432f2f4454442048544d4c20342e3031205472616e736974696f6e616c2f2f454e222022687474703a2f2f7777772e77332e6f72672f54522f68746d6c342f6c6f6f73652e647464223e0a3c68746d6c3e3c686561643e0a0a202020203c6d65746120687474702d65717569763d22436f6e74656e742d4c616e67756167652220636f6e74656e743d226465223e0a202020203c6d65746120687474702d65717569763d22436f6e74656e742d547970652220636f6e74656e743d22746578742f68746d6c3b20636861727365743d49534f2d383835392d31223e0a202020203c7469746c653e4b617373656e6162726563686e756e673c2f7469746c653e0a202020203c7374796c6520747970653d22746578742f637373223e0a2020202020202020626f6479207b0a20202020202020202020202077696474683a203137636d3b0a202020202020202020202020666f6e742d66616d696c793a76657264616e613b0a202020202020202020202020666f6e742d73697a653a20313270783b0a20202020202020207d0a20202020202020202e746162656c6c65207b0a20202020202020202020202077696474683a203137636d3b0a202020202020202020202020626f726465722d7374796c653a20736f6c69643b0a202020202020202020202020626f726465722d77696474683a203370783b0a20202020202020207d0a20202020202020202e7a65696c65207b0a20202020202020202020202077696474683a203137636d3b0a202020202020202020202020626f726465722d7374796c653a20736f6c69643b0a202020202020202020202020626f726465722d77696474683a203170783b0a202020202020202020202020666f6e742d73697a653a20736d616c6c3b0a20202020202020207d0a0a202020202020202074640a20202020202020207b0a20202020202020202020202077696474683a203137636d3b0a202020202020202020202020626f726465722d7374796c653a20736f6c69643b0a202020202020202020202020626f726465722d77696474683a203170783b0a202020202020202020202020666f6e742d73697a653a20736d616c6c3b0a20202020202020207d0a0a202020202020202023637573746f6d65724f766572766965770a20202020202020207b0a202020202020202020202020706f736974696f6e3a206162736f6c7574653b0a202020202020202020202020746f703a20343070783b0a2020202020202020202020206c6566743a2033343570783b0a0a20202020202020202020202077696474683a2033303070783b0a20202020202020207d0a202020203c2f7374796c653e0a3c2f686561643e0a0a3c626f64793e0a3c7461626c65207374796c653d2277696474683a20313030253b22203e0a202020203c74723e0a20202020202020203c7464207374796c653d22626f726465723a206e6f6e65223e3c7374726f6e673e4b617373656e6162726563686e756e673c2f7374726f6e673e3c2f74643e0a20202020202020203c7464207374796c653d22626f726465723a206e6f6e652220616c69676e3d227269676874223e3c7374726f6e673e447275636b646174756d3a203c2f7374726f6e673e203c7370616e2069643d2264617465546f646179223e3c2f7370616e3e3c2f74643e0a202020203c2f74723e0a3c2f7461626c653e0a0a3c703e0a202020203c7374726f6e673e417573647275636b2064757263683a203c2f7374726f6e673e203c7370616e2069643d22656d706c6f796565223e3c2f7370616e3e0a202020203c62723e0a202020203c7374726f6e673e5665727465696c7374656c6c653a203c2f7374726f6e673e203c7370616e2069643d22646973747269627574696f6e506f696e74223e3c2f7370616e3e0a202020203c62723e0a202020203c7374726f6e673e4c697374656e6e756d6d65723a203c2f7374726f6e673e203c7370616e2069643d226c6973744e756d626572223e3c2f7370616e3e0a202020203c62723e0a202020203c7374726f6e673e4162726563686e756e6720766f6d3a203c2f7374726f6e673e203c7370616e2069643d22706572696f6465223e3c2f7370616e3e0a3c2f703e0a3c62723e0a0a3c6669656c647365742069643d22637573746f6d65724f76657276696577223e0a202020203c6c6567656e643e4b756e64656e2675756d6c3b626572736963687420766f6d3a203c7370616e2069643d2264617465223e3c2f7370616e3e3c2f6c6567656e643e0a202020203c7374726f6e673e45727761636873656e653a203c2f7374726f6e673e203c7370616e2069643d22637573746f6d65724164756c7473223e303c2f7370616e3e0a202020203c62723e0a202020203c7374726f6e673e4b696e6465723a203c2f7374726f6e673e203c7370616e2069643d22637573746f6d65724368696c6472656e223e303c2f7370616e3e0a202020203c62723e0a202020203c7374726f6e673e20476573616d743a203c2f7374726f6e673e203c7370616e2069643d22637573746f6d6572546f74616c223e303c2f7370616e3e0a3c2f6669656c647365743e0a0a3c703e0a202020203c7374726f6e673e556d732661756d6c3b747a652c20686575746520676562756368743a3c2f7374726f6e673e0a3c2f703e0a3c7461626c6520636c6173733d22746162656c6c65222069643d2273616c6573546f646179223e0a202020203c74723e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20333070783b223e3c7374726f6e673e4e722e3c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373770783b223e3c7374726f6e673e446174756d3c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373170783b223e3c7374726f6e673e4b2e2d4e722e202f204f7274736b2e2d4e722e3c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a2031323070783b223e3c7374726f6e673e566f726e616d653c62723e4e6163686e616d653c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e576172656e7479703c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373170783b223e3c7374726f6e673e53756d6d653c62723e45696e6b6175663c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e53756d6d653c62723e5a61686c756e673c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e4b6f6e746f7374616e643c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e5665727465696c7374656c6c653c2f7374726f6e673e3c2f74643e0a202020203c2f74723e0a3c2f7461626c653e0a0a3c703e0a2020202053756d6d652045696e6b2661756d6c3b7566653a203c7374726f6e673e203c7370616e2069643d22746f74616c53686f7070696e6753616c6573546f646179223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e0a202020203c62723e0a2020202053756d6d65205a61686c756e67656e3a203c7374726f6e673e203c7370616e2069643d22746f74616c5061796d656e7453616c6573546f646179223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e0a3c2f703e0a3c68723e0a0a3c703e0a202020203c7374726f6e673e556d732661756d6c3b747a652c206865757465206765627563687420756e64206865757465207769656465722073746f726e696572743a3c2f7374726f6e673e0a3c2f703e0a3c7461626c6520636c6173733d22746162656c6c65222069643d2273616c657343616e63656c6564546f646179223e0a202020203c74723e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20333070783b223e3c7374726f6e673e4e722e3c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373770783b223e3c7374726f6e673e446174756d3c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373170783b223e3c7374726f6e673e4b2e2d4e722e202f204f7274736b2e2d4e722e3c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a2031323070783b223e3c7374726f6e673e566f726e616d653c62723e4e6163686e616d653c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e576172656e7479703c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373170783b223e3c7374726f6e673e53756d6d653c62723e45696e6b6175663c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e53756d6d653c62723e5a61686c756e673c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e4b6f6e746f7374616e643c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e5665727465696c7374656c6c653c2f7374726f6e673e3c2f74643e0a202020203c2f74723e0a3c2f7461626c653e0a3c703e0a2020202053756d6d6520556d732661756d6c3b747a653a203c7374726f6e673e3c7370616e2069643d22746f74616c53686f70696e6753616c657343616e63656c6564546f646179223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e0a202020203c62723e0a2020202053756d6d65205a61686c756e67656e3a203c7374726f6e673e203c7370616e2069643d22746f74616c5061796d656e7453616c657343616e63656c6564546f646179223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e0a3c2f703e0a3c68723e0a0a3c703e0a202020203c7374726f6e673e556d732661756d6c3b747a652c2066722675756d6c3b686572206765627563687420756e642068657574652073746f726e696572743a3c2f7374726f6e673e0a3c2f703e0a3c7461626c6520636c6173733d22746162656c6c65222069643d2273616c65734561726c69657243616e63656c6564546f646179223e0a202020203c74723e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20333070783b223e3c7374726f6e673e4e722e3c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373770783b223e3c7374726f6e673e446174756d3c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373170783b223e3c7374726f6e673e4b2e2d4e722e202f204f7274736b2e2d4e723c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a2031323070783b223e3c7374726f6e673e566f726e616d653c62723e4e6163686e616d653c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e576172656e7479703c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c6522207374796c653d2277696474683a20373170783b223e3c7374726f6e673e53756d6d653c62723e45696e6b6175663c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e53756d6d653c62723e5a61686c756e673c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e4b6f6e746f7374616e643c2f7374726f6e673e3c2f74643e0a20202020202020203c746420636c6173733d227a65696c65223e3c7374726f6e673e5665727465696c7374656c6c653c2f7374726f6e673e3c2f74643e0a202020203c2f74723e0a3c2f7461626c653e0a0a3c703e0a2020202053756d6d6520556d732661756d6c3b747a653a203c7374726f6e673e203c7370616e2069643d22746f74616c53686f70696e6753616c65734561726c69657243616e63656c6564546f646179223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e3c62723e0a2020202053756d6d65205a61686c756e67656e3a203c7374726f6e673e203c7370616e2069643d22746f74616c5061796d656e7453616c65734561726c69657243616e63656c6564546f646179223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e0a3c2f703e0a3c703e0a202020203c62723e0a202020203c7374726f6e673e20446572204b617373656e62657374616e6420626574722661756d6c3b67743a203c7370616e2069643d2263617368496e48616e64223e302c30303c2f7370616e3e204575726f3c2f7374726f6e673e0a3c2f703e0a0a3c2f626f64793e0a0a0a3c7363726970743e0a202020207661722064617465203d206e6577204461746528293b0a202020207661722079656172203d20646174652e67657446756c6c5965617228293b0a20202020766172206d6f6e7468203d20646174652e6765744d6f6e74682829202b20313b0a2020202076617220646179203d20646174652e6765744461746528293b0a202020207661722064617465537472696e67203d20646179202b20222e22202b206d6f6e7468202b20222e22202b20796561723b0a20202020646f63756d656e742e676574456c656d656e7442794964282264617465546f64617922292e696e6e657248544d4c203d2064617465537472696e673b0a0a2020202066756e6374696f6e20736574506172616d6574657228646174652c20656d706c6f7965652c20646973747269627574696f6e506f696e742c206c6973744e756d6265722c20706572696f6465290a202020207b0a2020202020202020646f63756d656e742e676574456c656d656e744279496428226461746522292e20696e6e657248544d4c203d20646174653b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642822656d706c6f79656522292e20696e6e657248544d4c203d20656d706c6f7965653b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642822646973747269627574696f6e506f696e7422292e20696e6e657248544d4c203d20646973747269627574696f6e506f696e743b0a2020202020202020646f63756d656e742e676574456c656d656e744279496428226c6973744e756d62657222292e20696e6e657248544d4c203d206c6973744e756d6265723b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642822706572696f646522292e20696e6e657248544d4c203d20706572696f64653b0a202020207d0a0a2020202066756e6374696f6e20616464526f77287461626c6549642c206e756d6265722c20646174652c20686f757365686f6c6449642c206e616d652c2070726f64756374547970652c20746f74616c53686f7070696e672c20746f74616c5061796d656e742c2062616e6b42616c616e63652c20646973747269627574696f6e506f696e742c20686f757365686f6c644469726563746f722c20746578742c206164756c74290a202020207b0a2020202020202020766172207461626c65203d20646f63756d656e742e676574456c656d656e7442794964287461626c654964293b0a2020202020202020766172206669727374526f77203d207461626c652e696e73657274526f77287461626c652e726f77732e6c656e677468293b0a0a20202020202020206669727374526f772e696e7365727443656c6c2830292e696e6e657248544d4c203d206e756d6265723b0a20202020202020206669727374526f772e696e7365727443656c6c2831292e696e6e657248544d4c203d20646174653b0a20202020202020206669727374526f772e696e7365727443656c6c2832292e696e6e657248544d4c203d20686f757365686f6c6449643b0a20202020202020206669727374526f772e696e7365727443656c6c2833292e696e6e657248544d4c203d206e616d653b0a20202020202020206669727374526f772e696e7365727443656c6c2834292e696e6e657248544d4c203d2070726f64756374547970653b0a20202020202020206669727374526f772e696e7365727443656c6c2835292e696e6e657248544d4c203d20746f74616c53686f7070696e673b0a20202020202020206669727374526f772e696e7365727443656c6c2836292e696e6e657248544d4c203d20746f74616c5061796d656e743b0a20202020202020206669727374526f772e696e7365727443656c6c2837292e696e6e657248544d4c203d2062616e6b42616c616e63653b0a20202020202020206669727374526f772e696e7365727443656c6c2838292e696e6e657248544d4c203d20646973747269627574696f6e506f696e743b0a0a0a20202020202020206966287461626c654964203d3d202273616c6573546f64617922290a20202020202020207b0a202020202020202020202020616464437573746f6d65724f76657276696577286164756c74293b0a20202020202020207d0a202020207d0a0a2020202066756e6374696f6e20616464437573746f6d65724f76657276696577286164756c74290a202020207b0a2020202020202020766172206e756d626572546f74616c203d207061727365496e7428646f63756d656e742e676574456c656d656e74427949642822637573746f6d6572546f74616c22292e696e6e657248544d4c29202b20313b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642822637573746f6d6572546f74616c22292e696e6e657248544d4c203d206e756d626572546f74616c2e746f537472696e6728293b0a20202020202020206966287061727365496e74286164756c7429203d3d2031290a20202020202020207b0a202020202020202020202020766172206e756d6265724164756c7473203d207061727365496e7428646f63756d656e742e676574456c656d656e74427949642822637573746f6d65724164756c747322292e696e6e657248544d4c29202b20313b0a202020202020202020202020646f63756d656e742e676574456c656d656e74427949642822637573746f6d65724164756c747322292e696e6e657248544d4c203d206e756d6265724164756c74732e746f537472696e6728293b0a20202020202020207d656c73650a20202020202020207b0a202020202020202020202020766172206e756d6265724368696c6472656e203d207061727365496e7428646f63756d656e742e676574456c656d656e74427949642822637573746f6d65724368696c6472656e22292e696e6e657248544d4c29202b20313b0a202020202020202020202020646f63756d656e742e676574456c656d656e74427949642822637573746f6d65724368696c6472656e22292e696e6e657248544d4c203d206e756d6265724368696c6472656e2e746f537472696e6728293b0a20202020202020207d0a202020207d0a0a2020202066756e6374696f6e207365744361736869657256616c75652876616c7565290a202020207b0a2020202020202020646f63756d656e742e676574456c656d656e7442794964282263617368496e48616e6422292e696e6e657248544d4c203d2076616c75653b0a202020207d0a0a2020202066756e6374696f6e20736574546f74616c53686f7070696e675061796d656e7453616c6573546f64617928746f74616c53686f7070696e672c20746f74616c5061796d656e74290a202020207b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c53686f7070696e6753616c6573546f64617922292e696e6e657248544d4c203d20746f74616c53686f7070696e673b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c5061796d656e7453616c6573546f64617922292e696e6e657248544d4c203d20746f74616c5061796d656e743b0a202020207d0a0a2020202066756e6374696f6e20736574546f74616c53686f7070696e675061796d656e7453616c657343616e63656c6564546f64617928746f74616c53686f7070696e672c20746f74616c5061796d656e74290a202020207b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c53686f70696e6753616c657343616e63656c6564546f64617922292e696e6e657248544d4c203d20746f74616c53686f7070696e673b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c5061796d656e7453616c657343616e63656c6564546f64617922292e696e6e657248544d4c203d20746f74616c5061796d656e743b0a202020207d0a0a2020202066756e6374696f6e20736574546f74616c53686f7070696e675061796d656e7453616c65734561726c69657243616e63656c6564546f64617928746f74616c53686f7070696e672c20746f74616c5061796d656e74290a202020207b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c53686f70696e6753616c65734561726c69657243616e63656c6564546f64617922292e696e6e657248544d4c203d20746f74616c53686f7070696e673b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642822746f74616c5061796d656e7453616c65734561726c69657243616e63656c6564546f64617922292e696e6e657248544d4c203d20746f74616c5061796d656e743b0a202020207d0a3c2f7363726970743e0a0a3c736372697074207372633d2268747470733a2f2f636f64652e6a71756572792e636f6d2f6a71756572792d332e332e312e736c696d2e6d696e2e6a73220a202020202020202020202020202020696e746567726974793d227368613338342d7138692f582b393635447a4f3072543761624b34314a537451494171566752567a70627a6f35736d584b703459665276482b386162745445315069366a697a6f220a20202020202020202020202020202063726f73736f726967696e3d22616e6f6e796d6f7573223e3c2f7363726970743e0a3c7363726970743e0a202020242866756e6374696f6e2028290a202020207b0a2020202020202077696e646f772e7072696e7428293b0a202020207d293b0a093c2f7363726970743e0a0a25504c414345484f4c4445525f464f525f46554e4354494f4e53250a0a3c2f68746d6c3e0a25454e445f4f465f46494c4525);
INSERT INTO `vorlage` (`vorlageId`, `templateType`, `name`, `autor`, `fileVersion`, `fileTypes`, `defaultText`, `passwort`, `aktiv`, `daten`) VALUES
(114, 'kvpDSGVO', 'Einverstaendniserklaerung', 'Tafel Projektteam', '1', 'HTML-Datei (*.html)|*.html', '.html', 0, 1, 0x3c212d2d0a4175746f723d546166656c2050726f6a656b747465616d0a4e616d653d45696e766572737461656e646e697365726b6c616572756e670a56657273696f6e3d310a547970653d6b7670445347564f0a46696c6554797065733d48544d4c2d446174656920282a2e68746d6c297c2a2e68746d6c0a50617373776f72743d300a44656661756c744578743d2e68746d6c0a2d2d3e0a0a0a3c21444f43545950452068746d6c205055424c494320222d2f2f5733432f2f4454442048544d4c20342e3031205472616e736974696f6e616c2f2f454e220a202020202020202022687474703a2f2f7777772e77332e6f72672f54522f68746d6c342f6c6f6f73652e647464223e0a3c68746d6c3e0a3c686561643e0a0a202020203c6d65746120687474702d65717569763d22436f6e74656e742d4c616e67756167652220636f6e74656e743d226465223e0a202020203c6d65746120687474702d65717569763d22436f6e74656e742d547970652220636f6e74656e743d22746578742f68746d6c3b20636861727365743d69736f2d383835392d31222f3e0a202020203c7469746c653e45696e76657273742661756d6c3b6e646e697365726b6c2661756d6c3b72756e673c2f7469746c653e0a0a202020203c7374796c6520747970653d22746578742f637373223e0a20202020202020207464207b0a202020202020202020202020626f726465723a2031707820736f6c696420626c61636b3b0a20202020202020202020202077696474683a2032383070783b0a20202020202020207d0a0a20202020202020207468207b0a202020202020202020202020626f726465723a2031707820736f6c696420626c61636b3b0a20202020202020202020202077696474683a20363070783b0a20202020202020207d0a0a20202020202020202e74464d207b0a20202020202020202020202077696474683a2031383070783b0a20202020202020207d0a0a20202020202020202e74464d6e72207b0a20202020202020202020202077696474683a20393070783b0a20202020202020207d0a0a20202020202020202e637573746f6d657244617461207b0a202020202020202020202020626f726465723a2031707820736f6c696420626c61636b3b0a202020202020202020202020746578742d616c69676e3a206c6566743b0a0a20202020202020207d0a0a20202020202020202e66616d696c794d656d62657273207b0a202020202020202020202020626f726465723a2031707820736f6c696420626c61636b3b0a202020202020202020202020746578742d616c69676e3a206c6566743b0a20202020202020207d0a0a20202020202020206833207b0a2020202020202020202020206d61782d77696474683a2033383070783b0a202020202020202020202020746578742d616c69676e3a2063656e7465723b0a20202020202020207d0a0a20202020202020207461626c65207b0a202020202020202020202020706f736974696f6e3a2072656c61746976653b0a2020202020202020202020206c6566743a20313070783b0a2020202020202020202020206d61782d77696474683a2036323070783b0a20202020202020207d0a0a202020202020202070207b0a202020202020202020202020706f736974696f6e3a2072656c61746976653b0a2020202020202020202020206c6566743a20313070783b0a20202020202020207d0a0a0a202020203c2f7374796c653e0a0a3c2f686561643e0a3c626f64793e0a0a25566f726c6167655f5374617274250a0a0a3c736372697074207372633d2268747470733a2f2f636f64652e6a71756572792e636f6d2f6a71756572792d332e332e312e736c696d2e6d696e2e6a73220a2020202020202020696e746567726974793d227368613338342d7138692f582b393635447a4f3072543761624b34314a537451494171566752567a70627a6f35736d584b703459665276482b386162745445315069366a697a6f220a202020202020202063726f73736f726967696e3d22616e6f6e796d6f7573223e3c2f7363726970743e0a0a0a3c6469762069643d22636f6e7461696e6572223e3c2f6469763e0a0a3c7363726970743e0a202020207661722070616765427265616b203d20303b0a2020202076617220636f756e746572203d20303b0a2020202076617220666d41727261794c6162656c73203d205b224b756e64656e6e756d6d6572222c20224e6163686e616d65222c2022566f726e616d65222c202247656275727473646174756d225d3b0a0a0a2020202066756e6374696f6e207461626c6543726561746528637573746f6d657249642c207469746c652c207375726e616d652c2066697273744e616d652c2067656e6465722c20616464726573732c20636974792c20646174654f6642697274682c206e6174696f6e616c6974792c2070686f6e654e756d6265722c20737461747573290a202020207b0a2020202020202020766172207461626c65203d20646f63756d656e742e637265617465456c656d656e7428277461626c6527292c2074722c2074643b0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e636f6c5370616e203d202232223b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d202245696e76657273742661756d6c3b6e646e697365726b6c2661756d6c3b72756e6720662675756d6c3b7220646965205370656963686572756e67205c6e20706572736f6e656e62657a6f67656e656e20446174656e2067656d2e2026736563743b346120424453472e20223b0a202020202020202074642e7374796c652e636f6c756d6e5370616e203d202232223b0a202020202020202074642e7374796c652e74657874416c69676e203d202263656e746572223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d20224b756e64656e6e756d6d65722028637573746f6d65724964293a20223b0a0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d20637573746f6d657249643b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d2022416e7265646520287469746c65293a20223b0a0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d207469746c653b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d20224e6163686e616d6520287375726e616d65293a20223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d207375726e616d653b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d2022566f726e616d6520286669727374206e616d65293a20223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d2066697273744e616d653b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d20225374726126737a6c69673b652028537472656574293a20223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d20616464726573733b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d2022504c5a2c20576f686e6f72742028706f7374616c20636f64652c2063697479293a20223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d20636974793b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d202247656275727473646174756d202864617465206f66204269727468293a20223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d20646174654f6642697274683b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d20224e6174696f6e616c69742661756d6c3b7420286e6174696f6e616c697479293a20223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d206e6174696f6e616c6974793b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d202254656c65666f6e6e756d6d6572202870686f6e65206e756d626572293a20223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d2070686f6e654e756d6265723b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d20225374617475733a20223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d207374617475733b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e636f6c5370616e203d202232223b0a202020202020202074642e7374796c652e626f72646572203d20227768697465223b0a202020202020202074642e696e6e657248544d4c203d20223c62723e223b0a202020202020202074642e7374796c652e636f6c6f72203d20227768697465223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a2020202020202020646f63756d656e742e676574456c656d656e74427949642827636f6e7461696e657227292e617070656e644368696c64287461626c65293b0a0a202020207d0a0a20202020766172207461626c654e72203d20303b0a0a202020202f2a0a2020202020202020202066756e6374696f6e206372656174654e6578745461626c6528290a202020202020202020207b0a2020202020202020202020202020766172207461626c65466d203d20646f63756d656e742e637265617465456c656d656e7428277461626c6527292c207472466d2c207464466d3b0a0a20202020202020202020202020207461626c654e722b2b3b0a2020202020202020202020202020766172207461626c654964203d20227461626c65466d22202b207461626c654e723b0a0a0a20202020202020202020202020207472466d203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020202020202020207464466d203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a20202020202020202020202020207472466d2e617070656e644368696c64287464466d293b0a20202020202020202020202020207464466d2e636f6c5370616e203d202234223b0a20202020202020202020202020207464466d2e7374796c652e666f6e74576569676874203d2022626f6c64223b0a20202020202020202020202020207464466d2e696e6e657248544d4c203d2022416c6c652046616d696c69656e6d6974676c69656465723a20223b0a20202020202020202020202020207464466d2e7374796c652e636f6c756d6e5370616e203d202234223b0a20202020202020202020202020207461626c65466d2e617070656e644368696c64287472466d293b0a0a20202020202020202020202020207472466d203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020202020202020207464466d203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a20202020202020202020202020207472466d2e617070656e644368696c64287464466d293b0a20202020202020202020202020207464466d2e7374796c652e666f6e74576569676874203d2022626f6c64223b0a20202020202020202020202020207464466d2e696e6e657248544d4c203d20224b756e64656e6e756d6d65723a20223b0a20202020202020202020202020207461626c65466d2e617070656e644368696c64287472466d293b0a20202020202020202020202020207464466d203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a20202020202020202020202020207472466d2e617070656e644368696c64287464466d293b0a20202020202020202020202020207464466d2e7374796c652e666f6e74576569676874203d2022626f6c64223b0a20202020202020202020202020207464466d2e696e6e657248544d4c203d20224e6163686e616d6d653a20223b0a20202020202020202020202020207461626c65466d2e617070656e644368696c64287472466d293b0a20202020202020202020202020207464466d203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a20202020202020202020202020207472466d2e617070656e644368696c64287464466d293b0a20202020202020202020202020207464466d2e7374796c652e666f6e74576569676874203d2022626f6c64223b0a20202020202020202020202020207464466d2e696e6e657248544d4c203d2022566f726e616d6d653a20223b0a20202020202020202020202020207461626c65466d2e617070656e644368696c64287472466d293b0a20202020202020202020202020207464466d203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a20202020202020202020202020207472466d2e617070656e644368696c64287464466d293b0a20202020202020202020202020207464466d2e7374796c652e666f6e74576569676874203d2022626f6c64223b0a20202020202020202020202020207464466d2e696e6e657248544d4c203d202247656275727473646174756d3a20223b0a20202020202020202020202020207461626c65466d2e617070656e644368696c64287472466d293b0a0a0a2020202020202020202020202020646f63756d656e742e676574456c656d656e74427949642827636f6e7461696e657227292e617070656e644368696c64287461626c65466d293b0a20202020202020202020202020207461626c65466d2e73657441747472696275746528226964222c207461626c654964293b0a2020202020202020202020202020646f63756d656e742e676574456c656d656e7442794964287461626c654964292e7374796c652e70616765427265616b4166746572203d2022616c77617973223b0a202020202020202020207d2a2f0a0a0a2020202066756e6374696f6e206372656174655461626c65466d286c6162656c732c206f626a65637473290a202020207b0a0a2020202020202020766172207461626c65203d20646f63756d656e742e637265617465456c656d656e7428277461626c6527293b0a0a20202020202020207461626c654e722b2b3b0a2020202020202020766172207461626c654964203d20227461626c65466d22202b207461626c654e723b0a0a2020202020202020766172207468656164203d20646f63756d656e742e637265617465456c656d656e742827746865616427293b0a20202020202020207661722074626f6479203d20646f63756d656e742e637265617465456c656d656e74282774626f647927293b0a0a202020202020202074686561645472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a202020202020202074686561645468203d20646f63756d656e742e637265617465456c656d656e742827746827293b0a2020202020202020746865616454722e617070656e644368696c642874686561645468293b0a2020202020202020746865616454682e636f6c5370616e203d202234223b0a2020202020202020746865616454682e7374796c652e666f6e74576569676874203d2022626f6c64223b0a2020202020202020746865616454682e696e6e657248544d4c203d2022416c6c652046616d696c69656e6d6974676c69656465723a20223b0a2020202020202020746865616454682e7374796c652e636f6c756d6e5370616e203d202234223b0a202020202020202074686561642e617070656e644368696c642874686561645472293b0a20202020202020207461626c652e617070656e644368696c64287468656164293b0a0a20202020202020207661722074686561645472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a2020202020202020666f7220287661722069203d20303b2069203c206c6162656c732e6c656e6774683b20692b2b29207b0a2020202020202020202020207661722074686561645468203d20646f63756d656e742e637265617465456c656d656e742827746827293b0a202020202020202020202020746865616454682e696e6e657248544d4c203d206c6162656c735b695d3b0a202020202020202020202020746865616454722e617070656e644368696c642874686561645468293b0a20202020202020207d0a202020202020202074686561642e617070656e644368696c642874686561645472293b0a20202020202020207461626c652e617070656e644368696c64287468656164293b0a0a2020202020202020666f722028766172206a203d20303b206a203c206f626a656374732e6c656e6774683b206a2b2b29207b0a2020202020202020202020207661722074626f64795472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a202020202020202020202020666f722028766172206b203d20303b206b203c206c6162656c732e6c656e6774683b206b2b2b29207b0a202020202020202020202020202020207661722074626f64795464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a2020202020202020202020202020202074626f647954642e696e6e657248544d4c203d206f626a656374735b6a5d5b6c6162656c735b6b5d5d3b0a2020202020202020202020202020202074626f647954722e617070656e644368696c642874626f64795464293b0a2020202020202020202020207d0a20202020202020202020202074626f64792e617070656e644368696c642874626f64795472293b0a20202020202020207d0a20202020202020207461626c652e617070656e644368696c642874626f6479293b0a0a0a202020202020202074626f64795472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a202020202020202074626f64795464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074626f647954722e617070656e644368696c642874626f64795464293b0a202020202020202074626f647954642e636f6c5370616e203d202234223b0a202020202020202074626f647954642e7374796c652e626f72646572203d20227768697465223b0a202020202020202074626f647954642e696e6e657248544d4c203d2022203c7020636c6173733d5c22746578745c223e266e6273703b3c2f703e5c6e22202b0a2020202020202020202020202220203c7020636c6173733d5c22746578745c223e49636820626573742661756d6c3b746967652064696520416e676162656e207a75206d65696e657220506572736f6e2c20646965206963682064657220254669726d656e6e616d6525207a756d205a7765636b6520646572203c62723e20446174656e657266617373756e6720696d205a7573616d6d656e68616e67206d69742064657220576172656e6162686f6c756e67207a757220566572662675756d6c3b67756e67207374656c6c652e3c2f703e5c6e22202b0a202020202020202020202020223c7020636c6173733d5c22746578745c223e4963682062696e206461722675756d6c3b62657220696e666f726d69657272742c20646173732064696520254669726d656e6e616d6525207a7572204572662675756d6c3b6c6c756e67206968726572207361747a756e677367656d2661756d6c3b26737a6c69673b656e204175666761626520756e6420756e74657220426561636874756e672064657220646174656e73636875747a72656368746c696368656e20566f7273636872696674656e20706572736f6e656e62657a6f67656e6520416e676162656e202675756d6c3b626572206d696368206d69742048696c66652064657220656c656b74726f6e69736368656e20446174656e766572617262656974756e672076657261726265697465742e3c2f703e5c6e22202b0a2020202020202020202020202220203c703e266e6273703b3c2f703e5c6e22202b0a2020202020202020202020202220203c68723e5c6e22202b0a2020202020202020202020202220203c7020636c6173733d5c22746578745c223e4f7274202f20446174756d202f20556e746572736368726966743c2f703e3c2f74643e223b0a202020202020202074626f647954642e7374796c652e626f72646572203d20227768697465223b0a202020202020202074626f647954642e7374796c652e636f6c756d6e5370616e203d202234223b0a20202020202020207461626c652e617070656e644368696c642874626f64795472293b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642827636f6e7461696e657227292e617070656e644368696c64287461626c65293b0a20202020202020207461626c652e73657441747472696275746528226964222c207461626c654964293b0a2020202020202020646f63756d656e742e676574456c656d656e7442794964287461626c654964292e7374796c652e70616765427265616b4166746572203d2022616c77617973223b0a0a0a202020202020202066616d696c796d656d6265722e6c656e677468203d20303b0a202020207d0a0a0a3c2f7363726970743e0a0a25504c414345484f4c4445525f464f525f46554e4354494f4e53250a25504c414345484f4c4445525f464f525f46554e4354494f4e535f32250a0a0a3c7363726970743e0a0a20202020242866756e6374696f6e2028290a202020207b0a202020202020202077696e646f772e7072696e7428293b0a202020207d293b0a0a3c2f7363726970743e0a0a25566f726c6167655f53746f70250a3c62723e0a3c2f626f64793e0a3c2f68746d6c3e0a25454e445f4f465f46494c4525),
(115, 'kvpAusweis', 'Ausweis_Standard', 'Tafel Projektteam', '2.0', 'HTML-Datei (*.html)|*.html', '.html', 0, 1, 0x3c212d2d0a4175746f723d546166656c2050726f6a656b747465616d0a4e616d653d417573776569735f5374616e646172640a56657273696f6e3d322e300a547970653d6b7670417573776569730a46696c6554797065733d48544d4c2d446174656920282a2e68746d6c297c2a2e68746d6c0a50617373776f72743d300a44656661756c744578743d2e68746d6c0a0a2d2d3e3c21444f43545950452048544d4c3e0a3c68746d6c3e0a3c686561643e0a3c6d65746120687474702d65717569763d22436f6e74656e742d4c616e67756167652220636f6e74656e743d226465223e0a3c6d65746120687474702d65717569763d22436f6e74656e742d547970652220636f6e74656e743d22746578742f68746d6c3b20636861727365743d5554462d38223e0a3c7469746c653e417573776569733c2f7469746c653e0a203c7374796c6520747970653d22746578742f637373223e0a20202020202020207464207b20626f726465723a2031707820626c61636b20736f6c69643b2077696474683a2031383070783b207d0a20202020202020207472207b20626f726465723a203170783b2077696474683a20363070783b207d0a20202020202020206833207b206d61782d77696474683a2033383070783b20746578742d616c69676e3a2063656e7465723b207d0a20202020202020207461626c65207b20706f736974696f6e3a2072656c61746976653b206c6566743a20313070783b20626f726465722d636f6c6c617073653a20636f6c6c617073653b207d0a2020202020202020646976207b20706f736974696f6e3a2072656c61746976653b2070616464696e673a203570782035707820357078203570783b207d0a202020203c2f7374796c653e0a3c2f686561643e0a3c626f64793e0a0a3c6469762069643d22636f6e7461696e6572223e3c2f6469763e0a0a3c7363726970743e0a202020207661722070616765427265616b203d20303b0a2020202076617220636f756e746572203d20303b0a0a202020202f2a205349474e4154555220414e474550415353543a204e696d6d74206a65747a7420616c6c6520506172616d6574657220766f6e204a61766120656e74676567656e2c2064616d6974206469652052656968656e666f6c6765207374696d6d7421202a2f0a202020202f2a20626172636f64653150617468206973742064617320766f726c65747a746520417267756d656e742e202a2f0a2020202066756e6374696f6e207461626c6543726561746528637573746f6d657249642c2066756c6c4e616d652c20646174654f6642697274682c2061646464726573732c20636974792c2067726f75704e616d652c206e756d6265724f664164756c74732c206e756d6265724f664368696c6472656e2c20626172636f646531506174682c20626172636f64653250617468290a202020207b0a2020202020202020766172207461626c65203d20646f63756d656e742e637265617465456c656d656e7428277461626c6527292c2074722c2074643b0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e636f6c5370616e203d202232223b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d202241757377656973223b0a0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e726f775370616e203d20393b0a202020202020202074642e7374796c652e7769647468203d20223332307078223b0a202020202020202074642e696e6e657248544d4c203d20223c7370616e20636c6173733d5c225374796c65335c223e44696573657220417573776569732069737420456967656e74756d2064657220266f756d6c3b72746c696368656e20546166656c2e3c62723e5c6e22202b0a202020202020202020202020222020457220697374206e69636874202675756d6c3b626572747261676261722e3c62723e5c6e22202b0a202020202020202020202020225365696e204d697373627261756368206973742073747261666261722e3c2f7370616e3e3c2f703e5c6e22202b0a20202020202020202020202022202020203c703e3c7370616e20636c6173733d5c225374796c65325c223e446572205665726c7573742069737420736f666f727420616e7a757a656967656e2e3c2f7370616e3e3c2f703e5c6e22202b0a20202020202020202020202022202020203c703e3c7370616e20636c6173733d5c225374796c65335c223e4d69742064656d20456e7466616c6c656e2064657220566f726175737365747a756e67207a756d2042657a756720646572204c65697374756e67656e202064657220546166656c20697374206469657365722041757377656973207769656465722061627a75676562656e2e3c2f7370616e3e3c2f703e5c6e22202b0a20202020202020202020202022202020203c703e5c6e22202b0a202020202020202020202020222020202020203c7370616e20636c6173733d5c225374796c65325c223e4265692041756666696e64656e206469657365732041757377656973657320626974746520616e2064696520266f756d6c3b72746c6963686520546166656c20207a75722675756d6c3b636b73656e64656e2e3c2f7370616e3e223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020202f2a204b756e64656e6e756d6d6572202a2f0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d20224b756e64656e6e756d6d65723a223b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d20637573746f6d657249643b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020202f2a204e616d65202a2f0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d20224e616d653a20223b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d2066756c6c4e616d653b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020202f2a2047656275727473646174756d202a2f0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d202247656275727473646174756d3a20223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d20646174654f6642697274683b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020202f2a2053747261c39f65202a2f0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d20225374726126737a6c69673b653a20223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d2061646464726573733b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020202f2a20576f686e6f7274202a2f0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d2022576f686e6f72743a20223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d20636974793b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020202f2a20416e7a61686c2045727761636873656e65202a2f0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d2022416e7a61686c2045727761636873656e653a223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d206e756d6265724f664164756c74733b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020202f2a20416e7a61686c204b696e646572202a2f0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e7374796c652e666f6e74576569676874203d2022626f6c64223b0a202020202020202074642e696e6e657248544d4c203d2022416e7a61686c204b696e6465723a20223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e696e6e657248544d4c203d206e756d6265724f664368696c6472656e3b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020202f2a20426172636f6465202848696572206e75747a656e2077697220626172636f646531506174682129202a2f0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e636f6c5370616e203d202232223b0a202020202020202074642e7374796c652e74657874416c69676e203d202263656e746572223b0a202020202020202074642e696e6e657248544d4c203d20223c696d67207372633d2722202b20626172636f64653150617468202b20222720616c743d5c22626172636f64655c223e223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a20202020202020207472203d20646f63756d656e742e637265617465456c656d656e742827747227293b0a20202020202020207464203d20646f63756d656e742e637265617465456c656d656e742827746427293b0a202020202020202074722e617070656e644368696c64287464293b0a202020202020202074642e636f6c5370616e203d202232223b0a202020202020202074642e7374796c652e626f72646572203d20227768697465223b0a202020202020202074642e696e6e657248544d4c203d20223c62723e223b0a202020202020202074642e7374796c652e636f6c6f72203d20227768697465223b0a20202020202020207461626c652e617070656e644368696c64287472293b0a0a202020202020202070616765427265616b2b2b3b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642827636f6e7461696e657227292e617070656e644368696c64287461626c65293b0a0a2020202020202020696620282870616765427265616b2025203329203d3d3d2030290a20202020202020207b0a202020202020202020202020636f756e7465722b2b3b0a202020202020202020202020766172207461626c655042203d20227461626c65504222202b20636f756e7465723b0a2020202020202020202020207461626c652e73657441747472696275746528226964222c207461626c655042293b0a202020202020202020202020646f63756d656e742e676574456c656d656e7442794964287461626c655042292e7374796c652e70616765427265616b4166746572203d2022616c77617973223b0a20202020202020207d0a202020207d0a202020200a202020202f2a2044756d6d7920436f6e6669672046756e6b74696f6e2c2064616d6974204a617661206b65696e656e204665686c6572207769726674202a2f0a2020202066756e6374696f6e20736574436f6e66696728612c622c6329207b7d0a0a3c2f7363726970743e0a0a25504c414345484f4c4445525f464f525f46554e4354494f4e53250a0a25417573776569735f53746f70250a0a25417573776569735f4e6575250a0a3c736372697074207372633d2268747470733a2f2f636f64652e6a71756572792e636f6d2f6a71756572792d332e332e312e736c696d2e6d696e2e6a73223e3c2f7363726970743e2020200a3c7363726970743e0a202020242866756e6374696f6e2028290a202020207b0a2020202020202077696e646f772e7072696e7428293b0a202020207d293b0a3c2f7363726970743e0a0a3c2f626f64793e0a3c2f68746d6c3e);
INSERT INTO `vorlage` (`vorlageId`, `templateType`, `name`, `autor`, `fileVersion`, `fileTypes`, `defaultText`, `passwort`, `aktiv`, `daten`) VALUES
(116, 'kvpAusweis', 'Ausweis_Modern', 'Tafel Projektteam', '2.0', 'HTML-Datei (*.html)|*.html', '.html', 0, 1, 0x3c212d2d0a4175746f723d546166656c2050726f6a656b747465616d0a4e616d653d417573776569735f4d6f6465726e0a56657273696f6e3d322e300a547970653d6b7670417573776569730a46696c6554797065733d48544d4c2d446174656920282a2e68746d6c297c2a2e68746d6c0a50617373776f72743d300a44656661756c744578743d2e68746d6c0a0a2d2d3e3c21444f43545950452048544d4c3e0a3c68746d6c3e0a3c686561643e0a202020203c6d65746120636861727365743d225554462d38223e0a202020203c7469746c653e47c3a4737465617573776569733c2f7469746c653e0a202020203c7374796c6520747970653d22746578742f637373223e0a20202020202020204070616765207b2073697a653a2041343b206d617267696e3a20303b207d0a20202020202020200a2020202020202020626f6479207b200a2020202020202020202020206d617267696e3a20303b200a20202020202020202020202070616464696e673a20303b200a202020202020202020202020666f6e742d66616d696c793a20275365676f65205549272c20417269616c2c2073616e732d73657269663b200a2020202020202020202020206261636b67726f756e643a2077686974653b200a202020202020202020202020636f6c6f723a20233030303b0a20202020202020207d0a0a202020202020202023636f6e7461696e6572207b200a202020202020202020202020646973706c61793a20626c6f636b3b200a20202020202020202020202077696474683a203231306d6d3b200a2020202020202020202020202f2a2048c3b66865207769726420647572636820496e68616c7420676566c3bc6c6c74202a2f0a20202020202020207d0a20202020202020200a20202020202020202f2a20446174756d204b6f70667a65696c652070726f205365697465202a2f0a20202020202020202e706167652d6865616465722d726f77207b0a20202020202020202020202077696474683a20313030253b0a2020202020202020202020206865696768743a2031356d6d3b202f2a20506c61747a2066c3bc7220446174756d206f62656e202a2f0a20202020202020202020202070616464696e672d6c6566743a2031306d6d3b0a20202020202020202020202070616464696e672d746f703a20356d6d3b0a202020202020202020202020626f782d73697a696e673a20626f726465722d626f783b0a202020202020202020202020666f6e742d73697a653a203970743b0a202020202020202020202020636f6c6f723a20233636363b0a20202020202020207d0a0a20202020202020202e636172642d77726170706572207b0a202020202020202020202020666c6f61743a206c6566743b0a20202020202020202020202077696474683a203530253b0a2020202020202020202020202f2a2048c3b6686520616e676570617373743a20283239376d6d202d2031356d6d2048656164657229202f2032203d2063612e203134316d6d202a2f0a2020202020202020202020206865696768743a203134316d6d3b200a202020202020202020202020626f782d73697a696e673a20626f726465722d626f783b0a20202020202020202020202070616464696e673a20356d6d3b0a20202020202020207d0a0a20202020202020202e63617264207b200a20202020202020202020202077696474683a20313030253b200a2020202020202020202020206865696768743a20313030253b0a202020202020202020202020626f782d73697a696e673a20626f726465722d626f783b0a202020202020202020202020626f726465723a203170782064617368656420233939393b200a20202020202020202020202070616464696e673a20356d6d3b200a202020202020202020202020706f736974696f6e3a2072656c61746976653b200a202020202020202020202020646973706c61793a20666c65783b200a202020202020202020202020666c65782d646972656374696f6e3a20636f6c756d6e3b200a2020202020202020202020206a7573746966792d636f6e74656e743a2073706163652d6265747765656e3b0a2020202020202020202020206f766572666c6f773a2068696464656e3b0a20202020202020207d0a0a20202020202020202f2a202d2d2d20484541444552202d2d2d202a2f0a20202020202020202e686561646572207b0a202020202020202020202020646973706c61793a20666c65783b0a2020202020202020202020206a7573746966792d636f6e74656e743a2073706163652d6265747765656e3b0a202020202020202020202020616c69676e2d6974656d733a20666c65782d73746172743b0a202020202020202020202020626f726465722d626f74746f6d3a2032707820736f6c696420233030303b0a20202020202020202020202070616464696e672d626f74746f6d3a20336d6d3b0a2020202020202020202020206865696768743a2033306d6d3b200a20202020202020207d0a0a20202020202020202e67726f75702d6261646765207b0a2020202020202020202020206261636b67726f756e643a20233030303b0a202020202020202020202020636f6c6f723a20236666663b0a20202020202020202020202077696474683a2032326d6d3b0a2020202020202020202020206865696768743a2032326d6d3b0a202020202020202020202020646973706c61793a20666c65783b0a2020202020202020202020206a7573746966792d636f6e74656e743a2063656e7465723b0a202020202020202020202020616c69676e2d6974656d733a2063656e7465723b0a202020202020202020202020666f6e742d73697a653a20333670743b0a202020202020202020202020666f6e742d7765696768743a20626f6c643b0a202020202020202020202020626f726465722d7261646975733a203470783b0a2020202020202020202020206c696e652d6865696768743a20313b0a20202020202020207d0a0a20202020202020202e6865616465722d696e666f207b0a202020202020202020202020746578742d616c69676e3a2072696768743b0a2020202020202020202020206d617267696e2d746f703a206175746f3b200a20202020202020202020202070616464696e672d626f74746f6d3a20326d6d3b0a20202020202020207d0a20202020202020202e636172642d7469746c65207b20666f6e742d73697a653a203870743b20746578742d7472616e73666f726d3a207570706572636173653b20636f6c6f723a20233535353b206c65747465722d73706163696e673a203170783b207d0a20202020202020202f2a20224e722e2220656e746665726e742c206e7572205a61686c202a2f0a20202020202020202e637573746f6d65722d6964207b20666f6e742d73697a653a20333270743b20666f6e742d7765696768743a20626f6c643b206c65747465722d73706163696e673a202d3170783b206c696e652d6865696768743a20313b207d0a0a20202020202020202f2a202d2d2d20434f4e54454e54202d2d2d202a2f0a20202020202020202e636f6e74656e74207b0a202020202020202020202020666c65783a20313b0a202020202020202020202020646973706c61793a20666c65783b0a202020202020202020202020666c65782d646972656374696f6e3a20636f6c756d6e3b0a2020202020202020202020206a7573746966792d636f6e74656e743a20666c65782d73746172743b200a20202020202020202020202070616464696e673a20346d6d20303b0a20202020202020207d0a0a20202020202020202e6e616d652d7072696d617279207b20666f6e742d73697a653a20313870743b20666f6e742d7765696768743a20626f6c643b20746578742d7472616e73666f726d3a207570706572636173653b206c696e652d6865696768743a20312e313b207d0a20202020202020202e6e616d652d7365636f6e64617279207b20666f6e742d73697a653a20313470743b20666f6e742d7765696768743a206e6f726d616c3b20636f6c6f723a20233333333b207d0a20202020202020202e616464726573732d626c6f636b207b20666f6e742d73697a653a20313070743b20636f6c6f723a20233434343b206d617267696e2d746f703a20326d6d3b206c696e652d6865696768743a20312e323b207d0a0a20202020202020202e73746174732d726f77207b0a202020202020202020202020646973706c61793a20666c65783b0a2020202020202020202020206d617267696e2d746f703a20356d6d3b0a2020202020202020202020206761703a2031306d6d3b0a20202020202020207d0a20202020202020202e737461742d6974656d207b20746578742d616c69676e3a2063656e7465723b207d0a20202020202020202e737461742d76616c7565207b20666f6e742d73697a653a20323670743b20666f6e742d7765696768743a20626f6c643b207d0a20202020202020202e737461742d6c6162656c207b20666f6e742d73697a653a203870743b20746578742d7472616e73666f726d3a207570706572636173653b20626f726465722d746f703a2031707820736f6c696420236363633b2070616464696e672d746f703a203270783b20636f6c6f723a20233636363b207d0a0a20202020202020202f2a202d2d2d205741524e554e4720262052454348544c49434845532028416e6765706173737429202d2d2d202a2f0a20202020202020202e6c6567616c2d626f78207b0a2020202020202020202020206d617267696e2d746f703a206175746f3b200a20202020202020202020202070616464696e672d746f703a20326d6d3b0a202020202020202020202020626f726465722d746f703a2031707820646f7474656420236363633b0a202020202020202020202020746578742d616c69676e3a2063656e7465723b0a202020202020202020202020666f6e742d73697a653a203870743b0a2020202020202020202020206c696e652d6865696768743a20312e333b0a202020202020202020202020636f6c6f723a20233030303b0a20202020202020207d0a20202020202020200a2020202020200a0a20202020202020202f2a202d2d2d20464f4f544552202d2d2d202a2f0a20202020202020202e666f6f746572207b0a2020202020202020202020206865696768743a2032306d6d3b0a202020202020202020202020646973706c61793a20666c65783b0a2020202020202020202020206761703a20356d6d3b0a202020202020202020202020616c69676e2d6974656d733a20666c65782d656e643b0a2020202020202020202020206d617267696e2d746f703a20326d6d3b200a20202020202020207d0a0a20202020202020202e626172636f64652d77726170706572207b0a202020202020202020202020666c65783a20313b0a202020202020202020202020746578742d616c69676e3a2063656e7465723b0a202020202020202020202020646973706c61793a20666c65783b0a202020202020202020202020666c65782d646972656374696f6e3a20636f6c756d6e3b0a2020202020202020202020206a7573746966792d636f6e74656e743a20666c65782d656e643b0a2020202020202020202020206865696768743a20313030253b0a20202020202020207d0a20202020202020200a20202020202020202e626172636f64652d7772617070657220696d67207b0a2020202020202020202020206d61782d77696474683a20313030253b0a2020202020202020202020206d61782d6865696768743a20313030253b202f2a2046c3bc6c6c742064696520426f78202a2f0a20202020202020202020202077696474683a206175746f3b0a2020202020202020202020206d617267696e3a2030206175746f3b0a20202020202020207d0a20202020202020200a20202020202020202f2a204c6162656c7320656e746665726e742077696520676577c3bc6e73636874202a2f0a0a20202020202020202f2a202d2d2d205354414e5a204d41524b4552202d2d2d202a2f0a20202020202020202e70756e63682d746172676574207b0a202020202020202020202020706f736974696f6e3a206162736f6c7574653b0a202020202020202020202020746f703a20386d6d3b206c6566743a203530253b207472616e73666f726d3a207472616e736c61746558282d353025293b0a20202020202020202020202077696474683a20386d6d3b206865696768743a20386d6d3b0a202020202020202020202020646973706c61793a20666c65783b206a7573746966792d636f6e74656e743a2063656e7465723b20616c69676e2d6974656d733a2063656e7465723b0a2020202020202020202020207a2d696e6465783a2031303b0a20202020202020207d0a20202020202020202e70742d636972636c65207b2077696474683a20356d6d3b206865696768743a20356d6d3b20626f726465723a2031707820736f6c696420236262623b20626f726465722d7261646975733a203530253b207d0a20202020202020202e70742d63726f7373207b20706f736974696f6e3a206162736f6c7574653b2077696474683a20313030253b206865696768743a203170783b206261636b67726f756e643a20236262623b207d0a20202020202020202e70742d63726f73732e76207b207472616e73666f726d3a20726f74617465283930646567293b207d0a0a20202020202020202f2a202d2d2d204144415054495645204c4f474943202d2d2d202a2f0a2020202020202020626f64792e686964652d6d61726b6572202e70756e63682d746172676574207b20646973706c61793a206e6f6e652021696d706f7274616e743b207d0a20202020202020200a2020202020202020626f64792e686964652d67726f7570202e67726f75702d6261646765207b20646973706c61793a206e6f6e652021696d706f7274616e743b207d0a2020202020202020626f64792e686964652d67726f7570202e686561646572207b20666c65782d646972656374696f6e3a20726f772d726576657273653b207d200a2020202020202020626f64792e686964652d67726f7570202e6865616465722d696e666f207b20746578742d616c69676e3a206c6566743b207d0a0a2020202020202020626f64792e686964652d626172636f646532202e626172636f64652d67726f7570207b20646973706c61793a206e6f6e652021696d706f7274616e743b207d0a2020202020202020626f64792e686964652d626172636f646532202e666f6f746572207b206a7573746966792d636f6e74656e743a2063656e7465723b207d0a2020202020202020626f64792e686964652d626172636f646532202e626172636f64652d686f757365686f6c64207b20666c65783a20302030203830253b207d200a0a20202020202020202e706167652d627265616b207b20636c6561723a20626f74683b20706167652d627265616b2d61667465723a20616c776179733b2077696474683a20313030253b206865696768743a20303b207d0a0a202020203c2f7374796c653e0a3c2f686561643e0a3c626f647920636c6173733d2273686f772d616c6c223e0a0a3c6469762069643d22636f6e7461696e6572223e3c2f6469763e0a0a3c7363726970743e0a2020202076617220636f756e746572203d20303b0a0a202020202f2a2053696368657265204a6176615363726970742046756e6b74696f6e202a2f0a2020202066756e6374696f6e207461626c6543726561746528637573746f6d657249642c2066756c6c4e616d652c20646174654f6642697274682c20616464726573732c20636974792c2067726f75704e616d652c206e756d6265724f664164756c74732c206e756d6265724f664368696c6472656e2c20626172636f646531506174682c20626172636f6465325061746829207b0a20202020202020200a20202020202020202f2a2053656974656e2d486561646572204c6f67696b3a20496d6d657220616d20416e66616e672065696e6572203465722d477275707065202853746172742065696e6572206e6575656e20536569746529202a2f0a202020202020202069662028636f756e74657220252034203d3d3d203029207b0a202020202020202020202020766172206e6f77203d206e6577204461746528293b0a202020202020202020202020766172206461746554696d65537472203d206e6f772e746f4c6f63616c65537472696e67282764652d4445272c207b20646174655374796c653a20276d656469756d272c2074696d655374796c653a202773686f727427207d293b0a2020202020202020202020200a20202020202020202020202076617220686561646572446976203d20646f63756d656e742e637265617465456c656d656e74282764697627293b0a2020202020202020202020206865616465724469762e636c6173734e616d65203d2027706167652d6865616465722d726f77273b0a2020202020202020202020206865616465724469762e696e6e657248544d4c203d2027447275636b3a2027202b206461746554696d655374723b0a202020202020202020202020646f63756d656e742e676574456c656d656e74427949642827636f6e7461696e657227292e617070656e644368696c6428686561646572446976293b0a20202020202020207d0a0a2020202020202020636f756e7465722b2b3b0a20202020202020200a20202020202020207661722067726f757043686172203d202867726f75704e616d652026262067726f75704e616d652e6c656e677468203e203029203f2067726f75704e616d652e6368617241742830292e746f5570706572436173652829203a202241223b0a20202020202020200a2020202020202020766172206e616d6573203d2066756c6c4e616d652e73706c697428222022293b0a2020202020202020766172206c6173744e616d65203d206e616d65732e6c656e677468203e2031203f206e616d65732e706f702829203a2066756c6c4e616d653b0a20202020202020207661722066697273744e616d65203d206e616d65732e6a6f696e28222022293b0a202020202020202069662866697273744e616d65203d3d3d20222229207b2066697273744e616d65203d2022223b206c6173744e616d65203d2066756c6c4e616d653b207d0a0a202020202020202076617220696d673248544d4c203d2022223b0a202020202020202069662028626172636f6465325061746820262620626172636f6465325061746820213d3d20226e756c6c2220262620626172636f6465325061746820213d3d20222229207b0a20202020202020202020202020696d673248544d4c203d200a20202020202020202020202020202020273c64697620636c6173733d22626172636f64652d7772617070657220626172636f64652d67726f7570223e27202b0a2020202020202020202020202020202020202020273c696d67207372633d2227202b20626172636f64653250617468202b20272220616c743d22477275707065223e27202b0a20202020202020202020202020202020273c2f6469763e273b0a20202020202020207d0a0a2020202020202020766172206361726448746d6c203d200a202020202020202020202020273c64697620636c6173733d2263617264223e27202b0a20202020202020202020202020202020273c64697620636c6173733d2270756e63682d746172676574223e3c64697620636c6173733d2270742d636972636c65223e3c2f6469763e3c64697620636c6173733d2270742d63726f7373223e3c2f6469763e3c64697620636c6173733d2270742d63726f73732076223e3c2f6469763e3c2f6469763e27202b0a202020202020202020202020202020200a20202020202020202020202020202020273c64697620636c6173733d22686561646572223e27202b0a2020202020202020202020202020202020202020273c64697620636c6173733d2267726f75702d6261646765223e27202b2067726f757043686172202b20273c2f6469763e27202b0a2020202020202020202020202020202020202020273c64697620636c6173733d226865616465722d696e666f223e27202b0a202020202020202020202020202020202020202020202020273c64697620636c6173733d22636172642d7469746c65223e47c3a4737465617573776569733c2f6469763e27202b0a202020202020202020202020202020202020202020202020273c64697620636c6173733d22637573746f6d65722d6964223e27202b20637573746f6d65724964202b20273c2f6469763e27202b0a2020202020202020202020202020202020202020273c2f6469763e27202b0a20202020202020202020202020202020273c2f6469763e27202b0a0a20202020202020202020202020202020273c64697620636c6173733d22636f6e74656e74223e27202b0a2020202020202020202020202020202020202020273c6469763e27202b200a202020202020202020202020202020202020202020202020273c64697620636c6173733d226e616d652d626c6f636b223e27202b0a20202020202020202020202020202020202020202020202020202020273c64697620636c6173733d226e616d652d7072696d617279223e27202b206c6173744e616d65202b20273c2f6469763e27202b0a20202020202020202020202020202020202020202020202020202020273c64697620636c6173733d226e616d652d7365636f6e64617279223e27202b2066697273744e616d65202b20273c2f6469763e27202b0a20202020202020202020202020202020202020202020202020202020273c64697620636c6173733d22616464726573732d626c6f636b223e27202b2061646472657373202b20273c62723e27202b2063697479202b20273c2f6469763e27202b0a202020202020202020202020202020202020202020202020273c2f6469763e27202b0a2020202020202020202020202020202020202020202020200a202020202020202020202020202020202020202020202020273c64697620636c6173733d2273746174732d726f77223e27202b0a20202020202020202020202020202020202020202020202020202020273c64697620636c6173733d22737461742d6974656d223e27202b0a2020202020202020202020202020202020202020202020202020202020202020273c64697620636c6173733d22737461742d76616c7565223e27202b206e756d6265724f664164756c7473202b20273c2f6469763e27202b0a2020202020202020202020202020202020202020202020202020202020202020273c64697620636c6173733d22737461742d6c6162656c223e45727761636873656e653c2f6469763e27202b0a20202020202020202020202020202020202020202020202020202020273c2f6469763e27202b0a20202020202020202020202020202020202020202020202020202020273c64697620636c6173733d22737461742d6974656d223e27202b0a2020202020202020202020202020202020202020202020202020202020202020273c64697620636c6173733d22737461742d76616c7565223e27202b206e756d6265724f664368696c6472656e202b20273c2f6469763e27202b0a2020202020202020202020202020202020202020202020202020202020202020273c64697620636c6173733d22737461742d6c6162656c223e4b696e6465723c2f6469763e27202b0a20202020202020202020202020202020202020202020202020202020273c2f6469763e27202b0a202020202020202020202020202020202020202020202020273c2f6469763e27202b0a2020202020202020202020202020202020202020273c2f6469763e27202b0a0a2020202020202020202020202020202020202020273c64697620636c6173733d226c6567616c2d626f78223e27202b0a20202020202020202020202020202020202020202020202027417573776569732069737420456967656e74756d2064657220546166656c20e280a2204e6963687420c3bc626572747261676261723c62723e27202b0a20202020202020202020202020202020202020202020202027426569205665726c757374206d656c64656e20e280a2204d69737362726175636820697374207374726166626172213c2f7370616e3e27202b0a2020202020202020202020202020202020202020273c2f6469763e27202b0a20202020202020202020202020202020273c2f6469763e27202b0a0a20202020202020202020202020202020273c64697620636c6173733d22666f6f746572223e27202b0a2020202020202020202020202020202020202020273c64697620636c6173733d22626172636f64652d7772617070657220626172636f64652d686f757365686f6c64223e27202b0a202020202020202020202020202020202020202020202020273c696d67207372633d2227202b20626172636f64653150617468202b20272220616c743d224b756e6465223e27202b0a2020202020202020202020202020202020202020273c2f6469763e27202b0a2020202020202020202020202020202020202020696d673248544d4c202b0a20202020202020202020202020202020273c2f6469763e27202b0a202020202020202020202020273c2f6469763e273b0a0a20202020202020207661722077726170706572446976203d20646f63756d656e742e637265617465456c656d656e74282764697627293b0a2020202020202020777261707065724469762e636c6173734e616d65203d2027636172642d77726170706572273b0a2020202020202020777261707065724469762e696e6e657248544d4c203d206361726448746d6c3b0a2020202020202020646f63756d656e742e676574456c656d656e74427949642827636f6e7461696e657227292e617070656e644368696c642877726170706572446976293b0a0a202020202020202069662028636f756e74657220252034203d3d3d203029207b0a202020202020202020202020766172207062203d20646f63756d656e742e637265617465456c656d656e74282764697627293b0a20202020202020202020202070622e636c6173734e616d65203d2027706167652d627265616b273b0a202020202020202020202020646f63756d656e742e676574456c656d656e74427949642827636f6e7461696e657227292e617070656e644368696c64287062293b0a20202020202020207d0a202020207d0a0a2020202066756e6374696f6e20736574436f6e6669672873686f774d61726b65722c2073686f7747726f75702c2073686f77426172636f64653229207b0a20202020202020206966282173686f774d61726b65722920646f63756d656e742e626f64792e636c6173734c6973742e6164642827686964652d6d61726b657227293b0a20202020202020206966282173686f7747726f75702920646f63756d656e742e626f64792e636c6173734c6973742e6164642827686964652d67726f757027293b0a20202020202020206966282173686f77426172636f6465322920646f63756d656e742e626f64792e636c6173734c6973742e6164642827686964652d626172636f64653227293b0a202020207d0a3c2f7363726970743e0a0a25504c414345484f4c4445525f464f525f46554e4354494f4e53250a25417573776569735f53746f70250a25417573776569735f4e6575250a0a3c736372697074207372633d2268747470733a2f2f636f64652e6a71756572792e636f6d2f6a71756572792d332e332e312e736c696d2e6d696e2e6a73223e3c2f7363726970743e0a3c7363726970743e0a202020242866756e6374696f6e202829207b0a2020202020202077696e646f772e7072696e7428293b0a2020207d293b0a3c2f7363726970743e0a0a3c2f626f64793e0a3c2f68746d6c3e);

-- --------------------------------------------------------

--
-- Table structure for table `warentyp`
--

CREATE TABLE `warentyp` (
  `warentypId` int(10) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `preisErwachsener` float DEFAULT NULL,
  `preisKinder` float DEFAULT NULL,
  `aktiv` tinyint(1) DEFAULT NULL,
  `haushaltspauschale` float DEFAULT NULL,
  `deckelbetrag` float DEFAULT NULL,
  `minbetrag` float DEFAULT NULL,
  `manuelleberechnung` tinyint(1) DEFAULT NULL,
  `zuordnungperson` int(10) DEFAULT NULL,
  `zuordnungbuchungstext` int(10) DEFAULT NULL,
  `warentyplimitanzahl` int(10) DEFAULT NULL,
  `warentyplimitart` int(10) DEFAULT NULL,
  `warentyplimitabstand` int(10) DEFAULT NULL,
  `warentyplimitabstandart` int(10) DEFAULT NULL,
  `naechsterwarentypid` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `warentyp`
--

INSERT INTO `warentyp` (`warentypId`, `name`, `preisErwachsener`, `preisKinder`, `aktiv`, `haushaltspauschale`, `deckelbetrag`, `minbetrag`, `manuelleberechnung`, `zuordnungperson`, `zuordnungbuchungstext`, `warentyplimitanzahl`, `warentyplimitart`, `warentyplimitabstand`, `warentyplimitabstandart`, `naechsterwarentypid`) VALUES
(2, 'Lebensmittel', 2, 0.5, 1, 0, 0, 0, 0, 1, 1, 2, 0, 0, 1, 2),
(3, 'Guthaben einzahlen', 0, 0, 1, 0, 0, 0, 0, 0, 2, -1, -1, -1, -1, 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `altersgruppen`
--
ALTER TABLE `altersgruppen`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `year` (`year`,`startAlter`,`endAlter`);

--
-- Indexes for table `ausgabegruppe`
--
ALTER TABLE `ausgabegruppe`
  ADD PRIMARY KEY (`ausgabegruppeId`);

--
-- Indexes for table `ausgabegruppe_ausgabetagzeit`
--
ALTER TABLE `ausgabegruppe_ausgabetagzeit`
  ADD UNIQUE KEY `uq_grp_tagzeit` (`ausgabegruppeId`,`ausgabeTagZeitId`),
  ADD KEY `ausgabegruppeId` (`ausgabegruppeId`),
  ADD KEY `ausgabeTagZeitId` (`ausgabeTagZeitId`),
  ADD KEY `idx_link_grp` (`ausgabegruppeId`),
  ADD KEY `idx_link_tagzeit` (`ausgabeTagZeitId`);

--
-- Indexes for table `ausgabetagzeit`
--
ALTER TABLE `ausgabetagzeit`
  ADD PRIMARY KEY (`ausgabeTagZeitId`),
  ADD UNIQUE KEY `uq_ausgabetagzeit` (`ausgabeTag`,`startZeit`,`endZeit`);

--
-- Indexes for table `berechtigung`
--
ALTER TABLE `berechtigung`
  ADD PRIMARY KEY (`berechtigungId`);

--
-- Indexes for table `bescheid`
--
ALTER TABLE `bescheid`
  ADD PRIMARY KEY (`bescheidId`),
  ADD KEY `bescheidartId` (`bescheidartId`),
  ADD KEY `bescheid_ibfk_1` (`personId`);

--
-- Indexes for table `bescheidart`
--
ALTER TABLE `bescheidart`
  ADD PRIMARY KEY (`bescheidArtId`);

--
-- Indexes for table `bescheidstatistik`
--
ALTER TABLE `bescheidstatistik`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `bescheidartName` (`bescheidartName`);

--
-- Indexes for table `deleted_memberofthefamily`
--
ALTER TABLE `deleted_MemberOfTheFamily`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `einkauf`
--
ALTER TABLE `einkauf`
  ADD PRIMARY KEY (`einkaufId`),
  ADD KEY `warentyp` (`warentyp`),
  ADD KEY `beiVerteilstelle` (`beiVerteilstelle`),
  ADD KEY `einkauf_ibfk_3` (`person`),
  ADD KEY `einkauf_ibfk_2` (`kunde`);

--
-- Indexes for table `einstellungen`
--
ALTER TABLE `einstellungen`
  ADD PRIMARY KEY (`initialID`);

--
-- Indexes for table `familienmitglied`
--
ALTER TABLE `familienmitglied`
  ADD PRIMARY KEY (`personId`),
  ADD KEY `berechtigungId` (`berechtigungId`),
  ADD KEY `nation` (`nation`),
  ADD KEY `familienmitglied_ibfk_1` (`haushaltId`);

--
-- Indexes for table `guthabenstatistik`
--
ALTER TABLE `guthabenstatistik`
  ADD PRIMARY KEY (`einkaufId`);

--
-- Indexes for table `haushalt`
--
ALTER TABLE `haushalt`
  ADD PRIMARY KEY (`kundennummer`),
  ADD KEY `verteilstellenId` (`verteilstellenId`),
  ADD KEY `ausgabeGruppeId` (`ausgabeGruppeId`),
  ADD KEY `plz` (`plz`);

--
-- Indexes for table `herkunftstatistik`
--
ALTER TABLE `herkunftstatistik`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `ort` (`ort`,`hausnummer`,`plz`);

--
-- Indexes for table `jahresergebnisse`
--
ALTER TABLE `jahresergebnisse`
  ADD PRIMARY KEY (`year`);

--
-- Indexes for table `kundeninfos`
--
ALTER TABLE `kundeninfos`
  ADD PRIMARY KEY (`kdNr`);

--
-- Indexes for table `nation`
--
ALTER TABLE `nation`
  ADD PRIMARY KEY (`nationId`);

--
-- Indexes for table `nationstatistik`
--
ALTER TABLE `nationstatistik`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ortsteil`
--
ALTER TABLE `ortsteil`
  ADD PRIMARY KEY (`ortsteilId`),
  ADD KEY `plz` (`plz`);

--
-- Indexes for table `plz`
--
ALTER TABLE `plz`
  ADD PRIMARY KEY (`plzId`);

--
-- Indexes for table `recht`
--
ALTER TABLE `recht`
  ADD PRIMARY KEY (`rechtId`);

--
-- Indexes for table `saved_queries`
--
ALTER TABLE `saved_queries`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `statistiktool`
--
ALTER TABLE `statistiktool`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `table_column_preferences`
--
ALTER TABLE `table_column_preferences`
  ADD PRIMARY KEY (`id`),
  ADD KEY `userId` (`userId`);

--
-- Indexes for table `tools`
--
ALTER TABLE `tools`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userId`),
  ADD UNIQUE KEY `userId_UNIQUE` (`userId`),
  ADD UNIQUE KEY `userName_UNIQUE` (`userName`);

--
-- Indexes for table `verteilstelle`
--
ALTER TABLE `verteilstelle`
  ADD PRIMARY KEY (`verteilstellenId`);

--
-- Indexes for table `vollmacht`
--
ALTER TABLE `vollmacht`
  ADD PRIMARY KEY (`vollmachtId`),
  ADD KEY `vollmacht_ibfk_1` (`haushaltId`),
  ADD KEY `vollmacht_ibfk_2` (`bevollmaechtigtePersonId`);

--
-- Indexes for table `vorlage`
--
ALTER TABLE `vorlage`
  ADD PRIMARY KEY (`vorlageId`);

--
-- Indexes for table `warentyp`
--
ALTER TABLE `warentyp`
  ADD PRIMARY KEY (`warentypId`),
  ADD KEY `naechsterwarentypid` (`naechsterwarentypid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `altersgruppen`
--
ALTER TABLE `altersgruppen`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=180;

--
-- AUTO_INCREMENT for table `ausgabegruppe`
--
ALTER TABLE `ausgabegruppe`
  MODIFY `ausgabegruppeId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=306;

--
-- AUTO_INCREMENT for table `ausgabetagzeit`
--
ALTER TABLE `ausgabetagzeit`
  MODIFY `ausgabeTagZeitId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `berechtigung`
--
ALTER TABLE `berechtigung`
  MODIFY `berechtigungId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- AUTO_INCREMENT for table `bescheid`
--
ALTER TABLE `bescheid`
  MODIFY `bescheidId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `bescheidart`
--
ALTER TABLE `bescheidart`
  MODIFY `bescheidArtId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT for table `bescheidstatistik`
--
ALTER TABLE `bescheidstatistik`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=152;

--
-- AUTO_INCREMENT for table `deleted_MemberOfTheFamily`
--
ALTER TABLE `deleted_MemberOfTheFamily`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `einkauf`
--
ALTER TABLE `einkauf`
  MODIFY `einkaufId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `familienmitglied`
--
ALTER TABLE `familienmitglied`
  MODIFY `personId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `haushalt`
--
ALTER TABLE `haushalt`
  MODIFY `kundennummer` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `herkunftstatistik`
--
ALTER TABLE `herkunftstatistik`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `nation`
--
ALTER TABLE `nation`
  MODIFY `nationId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=240;

--
-- AUTO_INCREMENT for table `nationstatistik`
--
ALTER TABLE `nationstatistik`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- AUTO_INCREMENT for table `ortsteil`
--
ALTER TABLE `ortsteil`
  MODIFY `ortsteilId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `plz`
--
ALTER TABLE `plz`
  MODIFY `plzId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `recht`
--
ALTER TABLE `recht`
  MODIFY `rechtId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `saved_queries`
--
ALTER TABLE `saved_queries`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=297;

--
-- AUTO_INCREMENT for table `statistiktool`
--
ALTER TABLE `statistiktool`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `table_column_preferences`
--
ALTER TABLE `table_column_preferences`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=174;

--
-- AUTO_INCREMENT for table `tools`
--
ALTER TABLE `tools`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `verteilstelle`
--
ALTER TABLE `verteilstelle`
  MODIFY `verteilstellenId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `vollmacht`
--
ALTER TABLE `vollmacht`
  MODIFY `vollmachtId` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `vorlage`
--
ALTER TABLE `vorlage`
  MODIFY `vorlageId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=117;

--
-- AUTO_INCREMENT for table `warentyp`
--
ALTER TABLE `warentyp`
  MODIFY `warentypId` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ausgabegruppe_ausgabetagzeit`
--
ALTER TABLE `ausgabegruppe_ausgabetagzeit`
  ADD CONSTRAINT `ausgabegruppe_ausgabeTagZeit_ibfk_1` FOREIGN KEY (`ausgabegruppeId`) REFERENCES `ausgabegruppe` (`ausgabegruppeId`),
  ADD CONSTRAINT `ausgabegruppe_ausgabeTagZeit_ibfk_2` FOREIGN KEY (`ausgabeTagZeitId`) REFERENCES `ausgabetagzeit` (`ausgabeTagZeitId`),
  ADD CONSTRAINT `fk_link_grp` FOREIGN KEY (`ausgabegruppeId`) REFERENCES `ausgabegruppe` (`ausgabegruppeId`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_link_tagzeit` FOREIGN KEY (`ausgabeTagZeitId`) REFERENCES `ausgabetagzeit` (`ausgabeTagZeitId`) ON DELETE CASCADE;

--
-- Constraints for table `bescheid`
--
ALTER TABLE `bescheid`
  ADD CONSTRAINT `bescheid_ibfk_1` FOREIGN KEY (`personId`) REFERENCES `familienmitglied` (`personId`) ON DELETE CASCADE,
  ADD CONSTRAINT `bescheid_ibfk_11` FOREIGN KEY (`bescheidartId`) REFERENCES `bescheidart` (`bescheidArtId`) ON DELETE CASCADE;

--
-- Constraints for table `einkauf`
--
ALTER TABLE `einkauf`
  ADD CONSTRAINT `einkauf_ibfk_1` FOREIGN KEY (`warentyp`) REFERENCES `warentyp` (`warentypId`),
  ADD CONSTRAINT `einkauf_ibfk_2` FOREIGN KEY (`kunde`) REFERENCES `haushalt` (`kundennummer`) ON DELETE CASCADE,
  ADD CONSTRAINT `einkauf_ibfk_3` FOREIGN KEY (`person`) REFERENCES `familienmitglied` (`personId`),
  ADD CONSTRAINT `einkauf_ibfk_4` FOREIGN KEY (`beiVerteilstelle`) REFERENCES `verteilstelle` (`verteilstellenId`);

--
-- Constraints for table `familienmitglied`
--
ALTER TABLE `familienmitglied`
  ADD CONSTRAINT `familienmitglied_ibfk_1` FOREIGN KEY (`haushaltId`) REFERENCES `haushalt` (`kundennummer`) ON DELETE CASCADE,
  ADD CONSTRAINT `familienmitglied_ibfk_2` FOREIGN KEY (`berechtigungId`) REFERENCES `berechtigung` (`berechtigungId`),
  ADD CONSTRAINT `familienmitglied_ibfk_3` FOREIGN KEY (`nation`) REFERENCES `nation` (`nationId`);

--
-- Constraints for table `haushalt`
--
ALTER TABLE `haushalt`
  ADD CONSTRAINT `haushalt_ibfk_1` FOREIGN KEY (`verteilstellenId`) REFERENCES `verteilstelle` (`verteilstellenId`),
  ADD CONSTRAINT `haushalt_ibfk_2` FOREIGN KEY (`ausgabeGruppeId`) REFERENCES `ausgabegruppe` (`ausgabegruppeId`),
  ADD CONSTRAINT `haushalt_ibfk_3` FOREIGN KEY (`plz`) REFERENCES `plz` (`plzId`);

--
-- Constraints for table `ortsteil`
--
ALTER TABLE `ortsteil`
  ADD CONSTRAINT `ortsteil_ibfk_1` FOREIGN KEY (`plz`) REFERENCES `plz` (`plzId`);

--
-- Constraints for table `table_column_preferences`
--
ALTER TABLE `table_column_preferences`
  ADD CONSTRAINT `table_column_preferences_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`);

--
-- Constraints for table `vollmacht`
--
ALTER TABLE `vollmacht`
  ADD CONSTRAINT `vollmacht_ibfk_1` FOREIGN KEY (`haushaltId`) REFERENCES `haushalt` (`kundennummer`) ON DELETE CASCADE,
  ADD CONSTRAINT `vollmacht_ibfk_2` FOREIGN KEY (`bevollmaechtigtePersonId`) REFERENCES `familienmitglied` (`personId`) ON DELETE CASCADE;

--
-- Constraints for table `warentyp`
--
ALTER TABLE `warentyp`
  ADD CONSTRAINT `warentyp_ibfk_1` FOREIGN KEY (`naechsterwarentypid`) REFERENCES `warentyp` (`warentypId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
