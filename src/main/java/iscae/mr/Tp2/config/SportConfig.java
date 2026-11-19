package iscae.mr.Tp2.config;

import iscae.mr.Tp2.common.Coach;
import iscae.mr.Tp2.common.SwimCoach;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SportConfig {
    @Bean("aquatic")
    public Coach swimCoach() {
        return new SwimCoach();
    }
}
