package com.example.demo.security.services;

// PATRÓN SERVICE LAYER:
// Implementa la lógica de carga de detalles del usuario para el proceso de autenticación.
// Spring Security utiliza este servicio para validar credenciales y cargar autoridades.
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return UserDetailsImpl.build(user);
    }
}