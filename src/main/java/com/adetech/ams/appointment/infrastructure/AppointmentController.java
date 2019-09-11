/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.appointment.infrastructure;

import com.adetech.ams.appointment.domain.Appointment;
import com.adetech.ams.appointment.domain.AppointmentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Ade
 */
@RestController
public class AppointmentController {
    
    @Autowired
    AppointmentRepository appointmentRepository;
    
    @GetMapping("/appointments")
    public List<Appointment> findAll(){
        return appointmentRepository.findAll();
    }
    
}
