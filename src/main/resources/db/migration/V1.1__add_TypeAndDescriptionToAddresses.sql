/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Ade
 * Created: Sep. 12, 2019
 */

ALTER TABLE `addresses` 
ADD COLUMN `type` VARCHAR(15) DEFAULT NULL;
ALTER TABLE `addresses` 
ADD COLUMN `description` VARCHAR(120) DEFAULT NULL;