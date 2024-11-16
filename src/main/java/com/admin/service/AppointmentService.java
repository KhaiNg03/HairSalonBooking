package com.admin.service;

import com.admin.exception.AppointmentNotFoundException; // Import exception
import com.admin.model.Appointment;
import com.admin.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Phương thức lấy danh sách tất cả các cuộc hẹn
    public List<Appointment> listAll() {
        return (List<Appointment>) appointmentRepository.findAll();
    }

    // Phương thức lưu một cuộc hẹn
    public void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    // Cập nhật Appointment
    public void update(Appointment appointment) {
        appointmentRepository.save(appointment);  // Sử dụng save để cập nhật
    }

    // Phương thức lấy một cuộc hẹn theo ID
    public Appointment get(Integer id) throws AppointmentNotFoundException {
        Optional<Appointment> result = appointmentRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new AppointmentNotFoundException("Could not find appointment with ID " + id);
    }

    // Phương thức xóa một cuộc hẹn
    public void delete(Integer id) throws AppointmentNotFoundException {
        Optional<Appointment> result = appointmentRepository.findById(id);
        if (result.isPresent()) {
            appointmentRepository.deleteById(id);
        } else {
            throw new AppointmentNotFoundException("Could not find appointment with ID " + id);
        }
    }

    // Phương thức cập nhật trạng thái cuộc hẹn
    public void updateStatus(Integer id, String status) throws AppointmentNotFoundException {
        Optional<Appointment> result = appointmentRepository.findById(id);
        if (result.isPresent()) {
            Appointment appointment = result.get();
            try {
                // Chuyển đổi String thành enum Status
                appointment.setStatus(Appointment.Status.valueOf(status));  // Chuyển đổi từ String sang Status enum
                appointmentRepository.save(appointment);
            } catch (IllegalArgumentException e) {
                // Xử lý nếu giá trị String không khớp với bất kỳ enum nào
                throw new AppointmentNotFoundException("Invalid status: " + status);
            }
        } else {
            throw new AppointmentNotFoundException("Could not find appointment with ID " + id);
        }
    }

}
