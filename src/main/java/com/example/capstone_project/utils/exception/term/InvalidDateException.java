package com.example.capstone_project.utils.exception.term;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class InvalidDateException extends Exception{
    public InvalidDateException(String message){
        super(message);
    }
}
