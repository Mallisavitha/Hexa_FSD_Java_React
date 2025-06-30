import axios from "axios";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function NewAppointments() {
    const [appointments, setAppointments] = useState([]);
    const [searchTerm, setSearchTerm] = useState(""); //  Search state
    const token = localStorage.getItem("token");

    useEffect(() => {
        fetchAppointments();
    }, []);

    const fetchAppointments = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/appointment/doctor/new", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });


            setAppointments(res.data);
        } catch (err) {
            console.error("Error fetching past appointments", err);
        }
    };

    // Filter appointments based on search term (by patient name)
    const filteredAppointments = appointments.filter(a =>
        a.patientName?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="container mt-5">
            <h2 className="mt-4">New Appointments</h2>

            {/* Search Box */}
            <div className="div" style={{width:"30%",marginLeft:"500px"}}>
                <input  type="text" placeholder="Search by patient name..." className="form-control" value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)}/>
            </div>

            <table className="table table-bordered mt-3" style={{borderRadius:"8px", border: "2px solid #212121", cellPadding:"15px", width:"100%"}}>
                <thead>
                    <tr>
                        <th>Patient</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Visit Type</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredAppointments.length === 0 ? (
                        <tr>
                            <td colSpan="6" className="text-center text-muted">No appointments found.</td>
                        </tr>
                    ) : (
                        filteredAppointments.map(a => (
                            <tr key={a.appointmentId}>
                                <td>{a.patientName}</td>
                                <td>{a.scheduledDate}</td>
                                <td>{a.scheduledTime}</td>
                                <td>{a.natureOfVisit}</td>
                                <td>{a.status}</td>
                                <td>
                                    <Link to={`/doctor/consultation/${a.appointmentId}`} className="btn btn-sm btn-outline-primary">
                                        View Consultation
                                    </Link>
                                </td>
                            </tr>
                        ))
                    )}
                </tbody>
            </table>
        </div>
    );
}

export default NewAppointments;
