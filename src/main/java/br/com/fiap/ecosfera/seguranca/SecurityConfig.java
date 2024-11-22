package br.com.fiap.ecosfera.seguranca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/novo_usuario", "/inserir_usuario").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/analise/nova", "/analise/{id}/editar", "/analise/{id}/remover")
                        .hasAuthority("Especialista Ambiental")
                        .requestMatchers("/empresas/nova","/empresas/{id}/editar","/empresas/{id}/remover",
                                "/sensores/novo","/sensores/{id}/editar","/sensores/{id}/remover",
                                "/relatorios/novo","/relatorios/{id}/editar","/relatorios/{id}/remover",
                                "/metas/nova","/metas/{id}/editar","/metas/{id}/remover")
                        .hasAuthority("Administrador")
                        .anyRequest().authenticated())
                .formLogin((form) -> form.loginPage("/login").failureUrl("/login?erro=true")
                        .defaultSuccessUrl("/empresas", true).permitAll())
                .logout((logout) -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true").permitAll())
                .rememberMe(rememberMe -> rememberMe.key("lembrarDeMim")
                        .tokenValiditySeconds(2592000)) // 30 dias
                .exceptionHandling((exception) ->
                        exception.accessDeniedHandler((request,response,accessDeniedHandler) ->
                        {response.sendRedirect("/acesso_negado");}));

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
