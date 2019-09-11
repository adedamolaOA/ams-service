/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Ade
 * Created: Aug. 25, 2019
 */

CREATE TABLE `visitors` (
  `visitorId` varchar(25) NOT NULL,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `otherName` varchar(50) DEFAULT NULL,
  `phoneNumber` varchar(50) DEFAULT NULL,
  `emailAddress` varchar(50) DEFAULT NULL,
  `companyId` varchar(25) DEFAULT NULL,
  `createdDate` bigint(13) NOT NULL,
  `createdBy` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL,
  PRIMARY KEY (`visitorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `bookings` (
  `bookingId` varchar(25) NOT NULL,
  `visitorId` varchar(25) NOT NULL,
  `purpose` varchar(25) NOT NULL,
  `type` varchar(25) NOT NULL,
  `datetime` bigint(13) NOT NULL,
  `createdDate` bigint(13) NOT NULL,
  `createdBy` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL,
  PRIMARY KEY (`bookingId`),
  CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`visitorId`) REFERENCES `visitors` (`visitorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `addresses` (
  `id` int NOT NULL AUTO_INCREMENT,
  `distictId` varchar(25) NOT NULL,
  `addressId` varchar(25) NOT NULL,
  `streetName` varchar(25) NOT NULL,
  `buildingNumber` varchar(30) DEFAULT NULL,
  `unitNumber` varchar(25) DEFAULT NULL,
  `createdDate` bigint(13) NOT NULL,
  `createdBy` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL,
  PRIMARY KEY (`id`, `distictId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `appointment_access` (
  `accessId` varchar(25) NOT NULL,
  `bookingId` varchar(25) NOT NULL,
  `accessCode` varchar(50) NOT NULL,
  `createdDate` bigint(13) NOT NULL,
  `createdBy` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL,
  PRIMARY KEY (`accessId`),
  CONSTRAINT `appointment_access_ibfk_1` FOREIGN KEY (`bookingId`) REFERENCES `bookings` (`bookingId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `appointment_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `logId` varchar(50) NOT NULL,
  `bookingId` varchar(25) NOT NULL,
  `accessCode` varchar(25) NOT NULL,
  `createdDate` bigint(13) NOT NULL,
  `createdBy` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL,
  PRIMARY KEY (`id`, `logId`),
  CONSTRAINT `appointment_log_ibfk_1` FOREIGN KEY (`bookingId`) REFERENCES `bookings` (`bookingId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `companyId` varchar(50) NOT NULL,
  `name` varchar(25) NOT NULL,
  `position` varchar(30) DEFAULT NULL,
  `active` varchar(25) NOT NULL,
  `createdDate` bigint(13) NOT NULL,
  `createdBy` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL,
  PRIMARY KEY (`id`, `companyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tag` (
  `tagId` varchar(25) NOT NULL,
  `tagNumber` varchar(25) NOT NULL,
  `description` varchar(120) DEFAULT NULL,
  `createdDate` bigint(13) NOT NULL,
  `createdBy` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL,
  PRIMARY KEY (`tagId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `appointment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `appointmentId` varchar(25) NOT NULL,
  `bookingId` varchar(25) NOT NULL,
  `accessId` varchar(25) NOT NULL,
  `tagId` varchar(25) NOT NULL,
  `createdDate` bigint(13) NOT NULL,
  `createdBy` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL,
  PRIMARY KEY (`id`, `appointmentId`),
  CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`bookingId`) REFERENCES `bookings` (`bookingId`),
  CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`accessId`) REFERENCES `appointment_access` (`accessId`),
  CONSTRAINT `appointment_ibfk_3` FOREIGN KEY (`tagId`) REFERENCES `tag` (`tagId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `department` (
  `departmentId` varchar(25) NOT NULL,
  `name` varchar(25) NOT NULL,
  `description` varchar(120) DEFAULT NULL,
  `createdDate` bigint(13) NOT NULL,
  `createdBy` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL,
  PRIMARY KEY (`departmentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `employee` (
  `employeeId` varchar(25) NOT NULL,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `otherName` varchar(50) DEFAULT NULL,
  `phoneNumber` varchar(50) DEFAULT NULL,
  `emailAddress` varchar(50) DEFAULT NULL,
  `departmentId` varchar(25) NOT NULL,
  `createdDate` bigint(13) NOT NULL,
  `createdBy` varchar(25) NOT NULL,
  `status` varchar(25) NOT NULL,
  PRIMARY KEY (`employeeId`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`departmentId`) REFERENCES `department` (`departmentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `users` (
    `userId` varchar(25) NOT NULL,
    `employeeId` varchar(25) NOT NULL,
    `username` varchar(25) NOT NULL,
    `password` varchar(100) NOT NULL,
    `createdDate` bigint(13) NOT NULL,
    `createdBy` varchar(25) NOT NULL,
    `status` varchar(25) NOT NULL,
    PRIMARY KEY(`userId`),
    CONSTRAINT `users_ibfk_1` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`employeeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `users_access` (
    `id` int NOT NULL AUTO_INCREMENT,
    `userAccessId` varchar(25) NOT NULL,
    `userId` varchar(25) NOT NULL,
    `employeeAccess` varchar(25) NOT NULL,
    `createdDate` bigint(13) NOT NULL,
    `createdBy` varchar(25) NOT NULL,
    `status` varchar(25) NOT NULL,
    PRIMARY KEY(`id`, `userAccessId`),
    CONSTRAINT `users_access_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;