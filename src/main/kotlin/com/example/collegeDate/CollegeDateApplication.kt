package com.example.collegeDate


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication(exclude = [ SecurityAutoConfiguration::class])
@EnableAsync
class CollegeDateApplication

fun main(args: Array<String>) {
	runApplication<CollegeDateApplication>(*args)
}
