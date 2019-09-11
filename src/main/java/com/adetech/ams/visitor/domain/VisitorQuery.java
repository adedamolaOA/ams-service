/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adetech.ams.visitor.domain;

import com.google.auto.value.AutoValue;

/**
 *
 * @author Ade
 */
@AutoValue
public abstract class VisitorQuery {
    
    public abstract String firstName();
    public abstract String lastName();
    public abstract String phoneNumber();
    public abstract String email();
}
