package uz.pdp.demo.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.demo.dto.EmployeeInfoDto;
import uz.pdp.demo.dto.SalaryHistoryDto;
import uz.pdp.demo.entity.*;
import uz.pdp.demo.entity.enums.RoleName;
import uz.pdp.demo.model.Response;
import uz.pdp.demo.repository.SalaryHistoryRepository;
import uz.pdp.demo.repository.TaskRepository;
import uz.pdp.demo.repository.TurniketRepository;
import uz.pdp.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class EmployeeService {
    UserRepository userRepository;
    TurniketRepository turniketRepository;
    TaskRepository taskRepository;
    SalaryHistoryRepository salaryHistoryRepository;

    public EmployeeService(UserRepository userRepository, TurniketRepository turniketRepository, TaskRepository taskRepository, SalaryHistoryRepository salaryHistoryRepository) {
        this.userRepository = userRepository;
        this.turniketRepository = turniketRepository;
        this.taskRepository = taskRepository;
        this.salaryHistoryRepository = salaryHistoryRepository;
    }

    public Response getSalaryHistoryByUserId(UUID userId) {
        final Optional<SalaryHistory> optionalSalaryHistory = salaryHistoryRepository.findByUserId(userId);

        if (optionalSalaryHistory.isEmpty()) {
            return new Response("User not found", false);
        }
        final SalaryHistory salaryHistory = optionalSalaryHistory.get();
        return new Response("User salary history", true, salaryHistory);
    }


    public Response pay(UUID userId, Integer salary, SalaryHistoryDto salaryHistoryDto) {
        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.setAmount(salaryHistoryDto.getAmount());
        salaryHistory.setWordStartDate(salaryHistoryDto.getWorkStartDate());
        salaryHistory.setWordStartDate(salaryHistoryDto.getWorkEndDate());

        final Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return new Response("User not found", false);
        }
        salaryHistory.setUser(optionalUser.get());

        salaryHistoryRepository.save(salaryHistory);
        return new Response("Salary history added!", true);
    }

    public List<User> getUserList() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            final User user = (User) authentication.getPrincipal();
            final Set<Role> roles = user.getRoles();
            RoleName roleName = null;
            for (Role role : roles) {
                roleName = role.getName();
            }

            assert roleName != null;
            if (roleName.equals(RoleName.DIRECTOR) || roleName.equals(RoleName.HR_MANAGER)) {
                return userRepository.findAllByRoleId(4);
            }
            return null;

        }
        return null;
    }

    public EmployeeInfoDto getEmployeeInfoById(UUID id) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            final User user = (User) authentication.getPrincipal();
            final Set<Role> roles = user.getRoles();
            RoleName roleName = null;
            for (Role role : roles) {
                roleName = role.getName();
            }

            assert roleName != null;
            if (roleName.equals(RoleName.DIRECTOR) || roleName.equals(RoleName.HR_MANAGER)) {
                EmployeeInfoDto info = new EmployeeInfoDto();
                final Optional<Turniket> turniket = turniketRepository.findByCreatedBy(id);
                final Turniket t = turniket.get();

                info.setEnterTime(t.getEnterWork());
                info.setExitTime(t.getExitWork());

                final List<Task> taskList = taskRepository.getTasksByUserId(id);
                info.setTaskList(taskList);
                return info;
            }
            return null;

        }
        return null;
    }
}
