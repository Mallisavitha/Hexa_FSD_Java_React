import axios from "axios";
import { useEffect, useRef, useState } from "react";
import { Link } from "react-router-dom";
import { Toast } from "primereact/toast";
import { ConfirmDialog, confirmDialog } from "primereact/confirmdialog";
import { Dialog } from "primereact/dialog";
import { Button } from "primereact/button";
import '../../css/doctor.css'

function Dashboard() {
    const token = localStorage.getItem("token");
    const [appointments, setAppointments] = useState([]);
    const [doctor, setDoctor] = useState({});
    const [slotsToday, setSlotsToday] = useState([]);
    const [rescheduleData, setRescheduleData] = useState({ id: null, date: "", time: "" });
    const [showRescheduleDialog, setShowRescheduleDialog] = useState(false);

    const toast = useRef(null);

    useEffect(() => {
        fetchAppointmentsToday();
        fetchDoctorProfile();
        fetchSlotsToday();
    }, []);

    const fetchAppointmentsToday = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/appointment/doctor/new", {
                headers: { Authorization: `Bearer ${token}` }
            });
            setAppointments(res.data);
        } catch (err) {
            console.error("Error fetching appointments", err);
        }
    };

    const fetchSlotsToday = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/doctor-slot/my-slot", {
                headers: { Authorization: `Bearer ${token}` }
            });
            const today = new Date().toISOString().split("T")[0];
            const todaySlots = res.data.filter(slot => slot.date === today);
            setSlotsToday(todaySlots);
        } catch (err) {
            console.error("Error fetching today's slots", err);
        }
    };

    const fetchDoctorProfile = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/doctor/get-one", {
                headers: { Authorization: `Bearer ${token}` }
            });
            setDoctor(res.data);
        } catch (err) {
            console.error("Error fetching doctor info", err);
        }
    };

    const handleReschedule = async () => {
        try {
            const payload = {
                scheduledDate: rescheduleData.date,
                scheduledTime: rescheduleData.time,
                status: "RESCHEDULED"
            };
            await axios.put(`http://localhost:8080/api/appointment/reschedule/${rescheduleData.id}`, payload, {
                headers: { Authorization: `Bearer ${token}` }
            });
            fetchAppointmentsToday();
            toast.current.show({ severity: 'success', summary: 'Rescheduled', detail: 'Appointment rescheduled', life: 2000 });
        } catch (err) {
            console.error("Failed to reschedule", err);
            toast.current.show({ severity: 'error', summary: 'Failed', detail: 'Rescheduling failed', life: 2000 });
        }
    };

    const handleCancel = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/api/appointment/delete/${id}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            fetchAppointmentsToday();
            toast.current.show({ severity: 'info', summary: 'Cancelled', detail: 'Appointment cancelled', life: 2000 });
        } catch (err) {
            console.error("Failed to cancel", err);
            toast.current.show({ severity: 'error', summary: 'Error', detail: 'Cancel failed', life: 2000 });
        }
    };

    const confirmCancel = (id) => {
        confirmDialog({
            message: 'Are you sure you want to cancel this appointment?',
            header: 'Cancel Confirmation',
            icon: 'pi pi-exclamation-triangle',
            accept: () => handleCancel(id),
            reject: () => toast.current.show({ severity: 'warn', summary: 'Cancelled', detail: 'Cancel rejected', life: 2000 }),
        });
    };

    const confirmReschedule = (appt) => {
        setRescheduleData({
            id: appt.appointmentId,
            date: appt.scheduledDate,
            time: appt.scheduledTime
        });
        setShowRescheduleDialog(true);
    };

    return (
        <div className="dash mt-4">
            <Toast ref={toast} />
            <ConfirmDialog />

            <h3 className="mb-4"> Welcome, Dr. {doctor.fullName} - {doctor.specialization}</h3>

            <div className="row mb-4 text-center">
                <div className="col-md-3" style={{minWidth:"200px"}}>
                    <div className="card text-bg-light mb-3">
                        <div className="card-body">
                            <h5 className="card-title">Appointments Today</h5>
                            <p className="card-text fs-4">{appointments.length}</p>
                        </div>
                    </div>
                </div>
                <div className="col-md-3" style={{minWidth:"200px"}}>
                    <div className="card text-bg-light mb-3" >
                        <div className="card-body">
                            <h5 className="card-title">Slots<br/> Today</h5>
                            <p className="card-text fs-4">{slotsToday.length}</p>
                        </div>
                    </div>
                </div>
            </div>

            <div className="row mb-4">
                <div className="col-md-3" style={{minWidth:"200px"}}>
                    <Link to="/doctor/appointments/new" className="btn btn-outline-success w-100">
                        <i className="fa-solid fa-calendar"></i> Appointments
                    </Link>
                </div>
                <div className="col-md-3" style={{minWidth:"200px"}}>
                    <Link to="/doctor/slots" className="btn btn-outline-warning w-100">
                        <i className="fa fa-clone"></i> Manage Slots
                    </Link>
                </div>
                <div className="col-md-3">
                    <Link to="/doctor/profile" className="btn btn-outline-dark w-100">
                        <i className="fa-solid fa-user"></i> My Profile
                    </Link>
                </div>
            </div>

            <div className="card">
                <div className="card-header bg-primary text-black" style={{ fontWeight: "bold" }}>Today Appointments</div>
                <div className="card-body table-responsive">
                    {appointments.length === 0 ? (
                        <p>No appointments today.</p>
                    ) : (
                        <table className="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Patient</th>
                                    <th>Date</th>
                                    <th>Time</th>
                                    <th>Nature</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {appointments.map((a) => (
                                    <tr key={a.appointmentId}>
                                        <td>{a.patientName}</td>
                                        <td>{a.scheduledDate}</td>
                                        <td>{a.scheduledTime}</td>
                                        <td>{a.natureOfVisit}</td>
                                        <td>{a.status}</td>
                                        <td>
                                            <Button icon="pi pi-refresh" className="p-button-white p-button " onClick={() => confirmReschedule(a)} />
                                            <Button icon="pi pi-times" className="p-button-danger p-button-sm" onClick={() => confirmCancel(a.appointmentId)} />
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            </div>

            {/* Reschedule Dialog */}
            <Dialog header="Reschedule Appointment" visible={showRescheduleDialog} style={{ width: '400px'}} modal onHide={() => setShowRescheduleDialog(false)}>
                <div className="mb-3">
                    <label className="form-label">Date:</label>
                    <input type="date" value={rescheduleData.date} className="form-control" onChange={(e) => setRescheduleData({ ...rescheduleData, date: e.target.value })}/>
                </div>
                <div className="mb-3">
                    <label className="form-label">Time:</label>
                    <input type="time" value={rescheduleData.time} className="form-control" onChange={(e) => setRescheduleData({ ...rescheduleData, time: e.target.value })}/>
                </div>
                <div className="d-flex justify-content-end">
                    <Button label="Cancel" icon="pi pi-times" className="p-button-text me-2" onClick={() => setShowRescheduleDialog(false)} />
                    <Button label="Save" icon="pi pi-check" className="p-button-success" onClick={() => { setShowRescheduleDialog(false);
                        handleReschedule(); }} />
                </div>
            </Dialog>
        </div>
    );
}

export default Dashboard;
