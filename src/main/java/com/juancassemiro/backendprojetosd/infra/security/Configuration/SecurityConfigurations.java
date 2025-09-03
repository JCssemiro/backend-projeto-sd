package com.juancassemiro.backendprojetosd.infra.security.Configuration;


import com.juancassemiro.backendprojetosd.infra.security.Filters.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                        .requestMatchers("/v1/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/swagger-ui-bundle.js","/v3/api-docs/swagger-ui-standalone-preset.js","/v3/api-docs/swagger-initializer.js","/v3/api-docs/swagger-config","/swagger-ui/**","/docs","/v3/api-docs/develop").permitAll()

                        .requestMatchers(HttpMethod.GET,"/prova").hasAuthority("professor")
                        .requestMatchers(HttpMethod.GET,"/prova/**").hasAnyAuthority("professor","aluno")
                        .requestMatchers(HttpMethod.GET,"/prova/aluno/**").hasAnyAuthority("professor","aluno")
                        .requestMatchers(HttpMethod.PUT,"/prova/**").hasAnyAuthority("professor")
                        .requestMatchers(HttpMethod.DELETE,"/prova/**").hasAnyAuthority("professor")
                        .requestMatchers(HttpMethod.POST,"/prova/responder-prova/**").hasAnyAuthority("aluno")
                        .requestMatchers(HttpMethod.POST,"/prova/cadastro").hasAnyAuthority("professor")

                        .requestMatchers(HttpMethod.GET,"/disciplina/**").hasAnyAuthority("professor","aluno")
                        .requestMatchers(HttpMethod.PUT,"/disciplina/**").hasAnyAuthority("professor")
                        .requestMatchers(HttpMethod.DELETE,"/disciplina/**").hasAnyAuthority("professor")
                        .requestMatchers(HttpMethod.POST,"/disciplina/**").hasAnyAuthority("professor")
                        .requestMatchers(HttpMethod.GET,"/disciplina").hasAnyAuthority("professor","aluno")

                        .requestMatchers(HttpMethod.GET,"/usuario/**").hasAnyAuthority("professor","aluno")
                        .requestMatchers(HttpMethod.GET,"/usuario/disciplina/**").hasAnyAuthority("professor","aluno")
                        .requestMatchers(HttpMethod.POST,"/usuario/cadastro").hasAnyAuthority("professor")

                        .anyRequest().authenticated()
                )
                .cors(Customizer.withDefaults())
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}