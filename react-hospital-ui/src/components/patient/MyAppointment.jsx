import axios from "axios";
import { useEffect, useState } from "react";
import { Dialog } from "primereact/dialog";
import { Button } from "primereact/button";
import { Link } from "react-router-dom";

function MyAppointment() {
    const [appointments, setAppointments] = useState([]);
    const [selectedAppointment, setSelectedAppointment] = useState(null);
    const [showDialog, setShowDialog] = useState(false);
    const [newDate, setNewDate] = useState("");
    const [newTime, setNewTime] = useState("");
    const token = localStorage.getItem("token");

    useEffect(() => {
        fetchAppointments();
    }, []);

    const fetchAppointments = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/appointment/own/upcoming", {
                headers: { Authorization: `Bearer ${token}` }
            });
            setAppointments(res.data);
        } catch (err) {
            console.error(err);
        }
    };

    const openRescheduleDialog = (appointment) => {
        setSelectedAppointment(appointment);
        setShowDialog(true);
    };

    const confirmReschedule = async () => {
        try {
            await axios.put(`http://localhost:8080/api/appointment/reschedule/${selectedAppointment.appointmentId}`, {
                scheduledDate: newDate,
                scheduledTime: newTime
            }, {
                headers: { Authorization: `Bearer ${token}` }
            });
            alert("Rescheduled successfully!");
            setShowDialog(false);
            fetchAppointments(); // refresh list
        } catch (err) {
            alert("Cannot Rescheduled");
            console.error(err);
        }
    };

    const cancelAppointment = async (id) => {
        if (window.confirm("Are you sure to cancel this appointment?")) {
            try {
                await axios.delete(`http://localhost:8080/api/appointment/delete/${id}`, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                alert("Cancelled successfully!");
                setAppointments(appointments.filter(a => a.appointmentId !== id));
            } catch (err) {
                console.error(err);
            }
        }
    };

    return (
        <div className="container mt-6">
            <h2>Today's Appointments</h2>
            <div className="row">
                {appointments.map((a, index) => (
                    <div className="col-md-6"  key={index}>
                        <div className="card mt-3" style={{width:"300px"}}>
                            <div className="card-body">
                                <h2>{a.patientName}</h2><br/>
                                <p><strong>Date:</strong> {a.scheduledDate}</p>
                                <p><strong>Time:</strong> {a.scheduledTime}</p>
                                <p><strong>Visit:</strong> {a.natureOfVisit}</p>
                                <p><strong>Description:</strong> {a.description}</p>
                                <p><strong>Doctor:</strong> {a.doctorName}</p>
                                <p><strong>Specialization:</strong> {a.specialization}</p>
                                <Button label="Reschedule" className="p-button-info me-2" onClick={() => openRescheduleDialog(a)} />
                                <Button label="Cancel" className="p-button-danger" onClick={() => cancelAppointment(a.appointmentId)} />
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {/* Reschedule Dialog */}
            <Dialog header="Reschedule Appointment" visible={showDialog} style={{ width: '400px' }} modal onHide={() => setShowDialog(false)}>
                <div className="p-fluid">
                    <label>Select New Date:</label>
                    <input type="date" className="form-control mb-2" onChange={(e) => setNewDate(e.target.value)} />

                    <label>Select New Time:</label>
                    <input type="time" className="form-control mb-3" onChange={(e) => setNewTime(e.target.value)} />

                    <div className="d-flex justify-content-end">
                        <Button label="Cancel" className="p-button-text me-2" onClick={() => setShowDialog(false)} />
                        <Button label="Confirm" className="p-button-success" onClick={confirmReschedule} />
                    </div>
                </div>
            </Dialog>
        </div>
    );
}

export default MyAppointment;
