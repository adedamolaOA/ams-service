/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.appointment.domain;

import java.util.List;

/**
 *
 * @author Ade
 */
public interface IAppointment {
    
    public Appointment book();
    public List<Appointment> getAppointment();
    
}
