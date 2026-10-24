package com.skillbridge;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SkillController {
    private final RoleRepository roles;
    public SkillController(RoleRepository roles) { this.roles = roles; }

    @GetMapping("/roles")
    public List<Role> allRoles() { return roles.findAll(); }

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public Role addRole(@RequestBody Role role) {
        if (role.getTitle() == null || role.getTitle().isBlank()
                || role.getRequiredSkills() == null || role.getRequiredSkills().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title and required skills are required");
        }
        return roles.save(role);
    }

    @DeleteMapping("/roles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable Long id) {
        if (!roles.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        roles.deleteById(id);
    }

    @PostMapping("/analyze")
    public Analysis analyze(@RequestBody AnalysisRequest request) {
        if (request.roleId() == null || request.skills() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Choose a role and enter your skills");
        Role role = roles.findById(request.roleId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        Set<String> owned = new HashSet<>();
        request.skills().stream().filter(Objects::nonNull)
            .map(String::trim).filter(s -> !s.isBlank())
            .map(s -> s.toLowerCase(Locale.ROOT)).forEach(owned::add);

        List<String> matched = new ArrayList<>(), missing = new ArrayList<>();
        for (String required : role.skillList()) {
            if (owned.contains(required.toLowerCase(Locale.ROOT))) matched.add(required);
            else missing.add(required);
        }
        int score = role.skillList().isEmpty() ? 0 : (int)Math.round(100.0 * matched.size() / role.skillList().size());
        return new Analysis(role.getTitle(), score, matched, missing,
            "Start with one missing skill, build a small project, and record your progress.");
    }

    public record AnalysisRequest(Long roleId, List<String> skills) {}
    public record Analysis(String roleTitle, int readinessPercent, List<String> matchedSkills,
                           List<String> missingSkills, String recommendation) {}
}
