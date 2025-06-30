import axios from "axios";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function Past() {
    const [appointments, setAppointments] = useState([]);
    const [page, setPage] = useState(0);
    const [row, setRow] = useState(3);
    const [searchDate, setSearchDate] = useState("");
    const token = localStorage.getItem("token");

    useEffect(() => {
        const fetchAppointments = async () => {
            try {
                let url = "";

                if (searchDate) {
                    // If search date is present, fetch based on date
                    url = `http://localhost:8080/api/appointment/own/date?date=${searchDate}`;
                } else {
                    // Otherwise fetch paginated data
                    url = `http://localhost:8080/api/appointment/own/past?page=${page}&size=${row}`;
                }

                const res = await axios.get(url, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                setAppointments(res.data);
            } catch (err) {
                console.error("Error fetching appointments", err);
            }
        };

        fetchAppointments();
    }, [page, searchDate]); // Triggers on both page change & date input

    return (
        <>
            <h1 className="mb-2">Past Appointments</h1>
            <div className="container">
                <div className="row">
                    <div className="col-lg-6 mt-3">
                        <label><strong>Select Date:</strong></label>
                        <input type="date" className="form-control" value={searchDate}
                            onChange={(e) => {setSearchDate(e.target.value);
                                setPage(0); // Reset to first page on new search
                            }}
                            style={{ width: "200px"}}/>
                    </div>
                    <div className="col-lg-6">
                        <nav aria-label="Page navigation example" style={{ marginRight: "15%", marginBottom: "1%" }}>
                            <ul className="pagination justify-content-end">
                                <li className="page-item">
                                    <button className="page-link" disabled={page === 0} onClick={() => setPage(page - 1)} >
                                        Previous
                                    </button>
                                </li>
                                <li className="page-item"><span className="page-link">{page + 1}</span></li>
                                <li className="page-item">
                                    <button className="page-link" onClick={() => setPage(page + 1)}>Next</button>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>

                <div className="row">
                    {
                        appointments.map((c, index) => (
                            <div className="col-md-4 mb-4" key={index}>
                                <div className="card mt-3">
                                    <div className="card-body" style={{ width: "900px" }}>
                                        <h5 className="card-title">{c.patientName}</h5>
                                        <p className="card-text"><strong>Scheduled Date:</strong> {c.scheduledDate}</p>
                                        <p className="card-text"><strong>Scheduled Time:</strong> {c.scheduledTime}</p>
                                        <p className="card-text"><strong>Description:</strong> {c.description}</p>
                                        <p className="card-text"><strong>Visit:</strong> {c.natureOfVisit}</p>
                                        <p className="card-text"><strong>Doctor Name:</strong> {c.doctorName}</p>
                                        <p className="card-text"><strong>Specialization:</strong> {c.specialization}</p>
                                        <Link className="btn btn-primary" to={`/patient/consultation/${c.appointmentId}`}>View Consultation</Link>
                                    </div>
                                </div>
                            </div>
                        ))
                    }
                </div>
            </div>
        </>
    );
}

export default Past;
