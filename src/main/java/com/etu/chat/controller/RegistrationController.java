package com.etu.chat.controller;

import com.etu.chat.entity.ChatUser;
import com.etu.chat.entity.RegistrationRequest;
import com.etu.chat.repository.ChatUserRepository;
import com.etu.chat.repository.RegistrationRequestRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final ChatUserRepository userRepository;
    private final RegistrationRequestRepository registrationRequestRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String registrationForm(Model model) {
        model.addAttribute("form", new RegistrationRequest());

        return "registration";
    }

    @PostMapping
    public String createRegistrationRequest(@Valid @ModelAttribute("form") RegistrationRequest registrationRequest, Errors errors) {
        if (errors.hasErrors()) {
            return "registration";
        }

        registrationRequestRepository.save(RegistrationRequest.builder()
                .username(registrationRequest.getUsername())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
            .build());

        return "redirect: login";
    }

    @GetMapping(value = {"/list/{count:\\d+}", "/list/{count:\\d+}/{start:\\d+}"})
    public @ResponseBody Iterable<RegistrationRequest> getRegistrationRequestList(@PathVariable Integer count, @PathVariable(required = false) Long start) {
        return start != null
                ? registrationRequestRepository.findByIdBeforeOrderByIdDesc(start, Pageable.ofSize(count))
                : registrationRequestRepository.findByOrderByIdDesc(Pageable.ofSize(count));
    }

    @PostMapping("/solution/{id:\\d+}/{solution}")
    private @ResponseBody HttpStatus lookRegistrationRequest(@PathVariable Long id, @PathVariable Boolean solution) {
        registrationRequestRepository.findById(id)
                .ifPresent(req -> {
                    if (solution) {
                        userRepository.save(ChatUser.builder().name(req.getUsername()).password(req.getPassword()).build());
                    }
                    registrationRequestRepository.delete(req);
                });

        return HttpStatus.OK;
    }

}
