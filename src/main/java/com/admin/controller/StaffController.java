package com.admin.controller;

import com.admin.exception.AppointmentNotFoundException;
import com.admin.model.*;
import com.admin.exception.StaffNotFoundException;
import com.admin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;


import java.util.List;
import java.util.Map;

@Controller
public class StaffController {


    @Autowired
    private StaffService staffService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AppointmentService appointmentService;


    @Autowired
    private ManagerService managerService;

    @Autowired
    private UserService userService;  // Inject the UserService
    @GetMapping("/Staff/appointments")
    public String getAllAppointmentsForStaff(Model model) {
        // Lấy toàn bộ các cuộc hẹn trong hệ thống
        List<Appointment> allAppointments = appointmentService.findAll();

        // Đưa danh sách vào model
        model.addAttribute("listAppointments", allAppointments);

        return "Staff/appointments";
    }

    // Trang Staff/customers
    @GetMapping("/Staff/customers")
    public String showStaffCustomersPage(Model model) {
        List<Customer> listCustomers = customerService.listAll(); // Lấy danh sách khách hàng

        model.addAttribute("listCustomers", listCustomers);

        return "Staff/customers";
    }








    @GetMapping("/Staff/staffDashboard")
    public String staffDashboard(Model model, HttpServletRequest request) {
        // Lấy vai trò từ session
        String role = (String) request.getSession().getAttribute("role");

        // Kiểm tra nếu không phải staff thì chuyển hướng
        if (role == null || !role.equals("staff")) {
            return "redirect:/page/login"; // Chuyển hướng đến trang Access Denied
        }

        // Lấy username của staff từ session
        String currentUsername = (String) request.getSession().getAttribute("username");
        model.addAttribute("currentUsername", currentUsername); // Truyền vào model
        return "Staff/staffDashboard"; // Trả về file giao diện
    }





    @GetMapping("/manageStaffs")
    public String showStaffList(Model model) {
        List<Staff> listStaffs = staffService.listAll();
        model.addAttribute("listStaffs", listStaffs);
        return "admin/manageStaffs";
    }
    // Hiển thị Staff theo Manager ID
    @GetMapping("/manager/{managerId}")
    public String showStaffsByManager(@PathVariable("managerId") Integer managerId, Model model) {
        List<Staff> listStaffs = staffService.listByManager(managerId);
        model.addAttribute("listStaffs", listStaffs);
        model.addAttribute("pageTitle", "Staffs Managed by Manager ID: " + managerId);
        return "admin/manageStaffs";
    }

    @GetMapping("/manageStaffs/new")
    public String showNewForm(Model model) {
        List<User> listUsers = userService.listAll();  // Lấy danh sách users từ service
        List<Manager> listManagers = managerService.listAll();  // Lấy danh sách managers
        model.addAttribute("staff", new Staff());
        model.addAttribute("listUsers", listUsers);  // Gửi danh sách users vào form
        model.addAttribute("listManagers", listManagers);
        model.addAttribute("pageTitle", "Add New Staff");
        return "admin/staff_form";
    }

    @PostMapping("/manageStaffs/save")
    public String saveStaff(Staff staff, RedirectAttributes ra) {
        staff.getUser().setRole("staff");
        // Kiểm tra và lưu User nếu chưa có ID
        if (staff.getUser() != null && staff.getUser().getId() == null) {
            userService.save(staff.getUser());  // Lưu user nếu chưa có ID
        }

        // Lưu staff sau khi lưu user (User đã có ID hoặc mới được lưu)
        staffService.save(staff);  // Lưu staff
        ra.addFlashAttribute("message", "The staff has been saved successfully.");
        return "redirect:/manageStaffs";
    }




    // Hiển thị form chỉnh sửa thông tin staff
    @GetMapping("/manageStaffs/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Staff staff = staffService.get(id);
            List<Manager> listManagers = managerService.listAll();
            model.addAttribute("staff", staff);
            model.addAttribute("listManagers", listManagers);
            model.addAttribute("pageTitle", "Edit Staff (ID: " + id + ")");
            return "admin/staff_form";
        } catch (StaffNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/manageStaffs";
        }
    }

    // Xóa staff theo ID
    @GetMapping("/manageStaffs/delete/{id}")
    public String deleteStaff(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            staffService.delete(id);
            ra.addFlashAttribute("message", "The staff ID " + id + " has been deleted.");
        } catch (StaffNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/manageStaffs";
    }
}
