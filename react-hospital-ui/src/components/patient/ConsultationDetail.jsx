import axios from "axios";
import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";

function ConsultationDetail() {
    const { cid } = useParams();
    const token = localStorage.getItem("token");

    const [appointment, setAppointment] = useState({});
    const [consultation, setConsultation] = useState(null);
    const [prescriptions, setPrescriptions] = useState([]);
    const [tests, setTests] = useState([]);

    useEffect(() => {
        fetchAppointment();
        fetchConsultation();
    }, []);

    useEffect(() => {
        if (consultation?.consultationId) {
            fetchPrescriptions();
            fetchTests();
        }
    }, [consultation]);

    const fetchAppointment = async () => {
        try {
            const res = await axios.get(`http://localhost:8080/api/appointment/get-one/${cid}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setAppointment(res.data);
        } catch (err) {
            console.error("Error fetching appointment", err);
        }
    };

    const fetchConsultation = async () => {
        try {
            const res = await axios.get(`http://localhost:8080/api/consultation/patient/get/${cid}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setConsultation(res.data);

        } catch (err) {
            alert("No consultation is added")
            console.log("No consultation yet.");
        }
    };

    const fetchPrescriptions = async () => {
        try {
            const res = await axios.get(`http://localhost:8080/api/prescription/get/${consultation.consultationId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setPrescriptions(res.data);
        } catch (err) {
            console.log("No prescriptions: " + err);
        }
    };

    const fetchTests = async () => {
        try {
            const res = await axios.get(`http://localhost:8080/api/test/consultation/${consultation.consultationId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setTests(res.data);
        } catch (err) {
            console.log("No tests: " + err);
        }
    };

    return (
        <div className="container-fluid mt-4">
            <nav aria-label="breadcrumb">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item"><Link to="/patient">Patient</Link></li>
                    <li className="breadcrumb-item"><Link to="/patient/appointment/completed">Past</Link></li>
                    <li className="breadcrumb-item active" aria-current="page">Consultation Details</li>
                </ol>
            </nav>

            <h3>Appointment : {cid}</h3>
            <div className="card mb-3">
                <div className="card-body">
                    <strong>Patient:</strong> {appointment.patientName}<br />
                    <strong>Date:</strong> {appointment.scheduledDate}<br />
                    <strong>Time:</strong> {appointment.scheduledTime}<br />
                    <strong>Nature:</strong> {appointment.natureOfVisit}<br />
                    <strong>Description:</strong> {appointment.description}<br />
                </div>
            </div>

            <div className="mb-4">
                <button className="btn btn-success mx-2" data-bs-toggle="collapse" data-bs-target="#consultation">Consultation</button>
                <button className="btn btn-primary mx-2" data-bs-toggle="collapse" data-bs-target="#prescriptions">Prescriptions</button>
                <button className="btn btn-warning mx-2" data-bs-toggle="collapse" data-bs-target="#tests">Test Recommendations</button>
            </div>

            {/* Consultation Section */}
            <div className="collapse" id="consultation">
                <div className="card card-body" style={{ backgroundColor: "white" }}>
                    <h5>Consultation</h5>
                    {consultation ? (
                        <>
                            <div><strong>Symptoms:</strong> {consultation.symptoms || "N/A"}</div>
                            <div><strong>Examination:</strong> {consultation.examination || "N/A"}</div>
                            <div><strong>Treatment Plan:</strong> {consultation.treatmentPlan || "N/A"}</div>
                        </>
                    ) : (
                        <div className="text-muted">No consultation available yet.</div>
                    )}
                </div>
            </div>



            {/* Prescriptions Section */}
            <div className="collapse" id="prescriptions">
                <div className="card card-body" style={{ backgroundColor: "white" }}>
                    <h5>Prescriptions</h5>
                    {prescriptions.length > 0 ? (
                        <div className="d-flex flex-column gap-2">
                            {prescriptions.map((p, idx) => (
                                <div key={idx} className="border p-2 rounded">
                                    <div><strong>Medicine:</strong> {p.medicineName}</div>
                                    <div><strong>Dosage:</strong> {p.dosageTiming}</div>
                                    <div><strong>Meal Time:</strong> {p.mealTime}</div>
                                    <div><strong>Duration:</strong> {p.duration}</div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="text-muted">No prescriptions found.</div>
                    )}
                </div>
            </div>

            {/* Test Recommendations Section */}
            <div className="collapse" id="tests">
                <div className="card card-body" style={{ backgroundColor: "white" }}>
                    <h5>Test Recommendations</h5>
                    {tests.length > 0 ? (
                        <div className="d-flex flex-column gap-2">
                            {tests.map((t, idx) => (
                                <div key={idx} className="border p-2 rounded">
                                    <div><strong>Test:</strong> {t.testName}</div>
                                    <div><strong>Status:</strong> {t.status}</div>
                                    <div>
                                        <strong>Report:</strong>{" "}
                                        {t.reportDownload ? (
                                            <button
                                                className="btn btn-outline-info btn-sm"
                                                onClick={() => window.open(`/reports/${t.reportDownload}`, "_blank")}>
                                                View Report
                                            </button>
                                        ) : (
                                            <span className="text-muted">No report available</span>
                                        )}
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="text-muted">No test recommendations available.</div>
                    )}
                </div>
            </div>

        </div>
    );
}

export default ConsultationDetail;
