package lk.ijse.newslbackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling health check requests.
 * This controller provides an endpoint to verify if the application is running.
 */
@Controller
@RequestMapping("/api/v1/health")
public class HealthTestController {
    /**
     * {@code GET /api/v1/health} : Perform a health check.
     *
     * @param model the model to which the health check message is added
     * @return the name of the view to be rendered
     */
    @GetMapping
    public String healthCheck(Model model) {
        model.addAttribute("message", "NewSL BACKEND is RUNNING!!");
        return "health-check";
    }
}