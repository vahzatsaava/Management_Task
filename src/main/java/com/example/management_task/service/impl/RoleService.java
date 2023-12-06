package com.example.management_task.service.impl;

import com.example.management_task.repository.RoleRepository;
import com.example.management_task.repository.entity.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    Role findRoleByName(String role) {
        return roleRepository.findRoleByName(role)
                .orElseThrow(() -> new EntityNotFoundException("role not found by name " + role));
    }
}
