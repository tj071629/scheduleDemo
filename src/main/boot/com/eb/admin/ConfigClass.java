package com.eb.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = { "classpath:etc/*.xml" })
public class ConfigClass {

}