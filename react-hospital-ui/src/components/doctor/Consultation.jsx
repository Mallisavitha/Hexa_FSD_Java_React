import axios from "axios";
import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";

function ConsultationDetails() {
    const { cid } = useParams(); // appointmentId
    const token = localStorage.getItem("token");

    const [appointment, setAppointment] = useState({});
    const [consultation, setConsultation] = useState(null);
    const [consultForm, setConsultForm] = useState({ symptoms: "", examination: "", treatmentPlan: "" });

    const [prescriptions, setPrescriptions] = useState([]);
    const [newPrescription, setNewPrescription] = useState({ medicineName: "", dosageTiming: "", mealTime: "", duration: "" });

    const [tests, setTests] = useState([]);
    const [newTest, setNewTest] = useState({ testName: ""});

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
            const res = await axios.get(`http://localhost:8080/api/consultation/doctor/get/${cid}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setConsultation(res.data);
            setConsultForm(res.data);
        } catch {
            console.log("No consultation yet.");
        }
    };

    const handleAddOrUpdateConsultation = async () => {
        try {
            const url = consultation
                ? `http://localhost:8080/api/consultation/update/${cid}`
                : `http://localhost:8080/api/consultation/add/${cid}`;
            const method = consultation ? axios.put : axios.post;
            const res = await method(url, consultForm, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setConsultation(res.data);
            alert(consultation ? "Consultation updated!" : "Consultation added!");
        } catch (err) {
            console.error("Error submitting consultation:", err);
        }
    };

    const fetchPrescriptions = async () => {
        const res = await axios.get(`http://localhost:8080/api/prescription/get/${consultation.consultationId}`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        setPrescriptions(res.data);
    };

    const handleAddPrescription = async () => {
        await axios.post(`http://localhost:8080/api/prescription/add/${consultation.consultationId}`, newPrescription, {
            headers: { Authorization: `Bearer ${token}` }
        });
        setNewPrescription({ medicineName: "", dosageTiming: "", mealTime: "", duration: "" });
        fetchPrescriptions();
        alert("Prescription added");
    };

    const fetchTests = async () => {
        const res = await axios.get(`http://localhost:8080/api/test/consultation/${consultation.consultationId}`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        setTests(res.data);
    };

    const handleAddTest = async () => {
        await axios.post(`http://localhost:8080/api/test/recommend/${consultation.consultationId}`, newTest, {
            headers: { Authorization: `Bearer ${token}` }
        });
        setNewTest({ testName: "" });
        fetchTests();
        alert("Test added");
    };

    return (
        <div className="container mt-4">

            <h3>Appointment : {cid}</h3>
            <div className="card mb-3">
                <div className="card-body">
                    <strong>Patient:</strong> {appointment.patientName}<br />
                    <strong>Date:</strong> {appointment.scheduledDate}<br />
                    <strong>Time:</strong> {appointment.scheduledTime}<br />
                    <strong>Nature:</strong> {appointment.natureOfVisit}<br />
                    <strong>Description:</strong> {appointment.description}<br />
                    <strong>Status:</strong> {appointment.status}
                </div>
            </div>

            <div className="mb-4">
                <button className="btn btn-success mx-2" data-bs-toggle="collapse" data-bs-target="#consultation">Consultation</button>
                <button className="btn btn-primary mx-2" data-bs-toggle="collapse" data-bs-target="#prescriptions">Prescriptions</button>
                <button className="btn btn-warning mx-2" data-bs-toggle="collapse" data-bs-target="#tests">Test Recommendations</button>
            </div>

            {/* Consultation Section */}
            <div className="collapse" id="consultation">
                <div className="card card-body">
                    <h5>{consultation ? "Update Consultation" : "Add Consultation"}</h5>
                    <input className="form-control my-2" placeholder="Symptoms"
                        value={consultForm.symptoms || ""} onChange={(e) => setConsultForm({ ...consultForm, symptoms: e.target.value })} />
                    <input className="form-control my-2" placeholder="Examination"
                        value={consultForm.examination || ""} onChange={(e) => setConsultForm({ ...consultForm, examination: e.target.value })} />
                    <input className="form-control my-2" placeholder="Treatment Plan"
                        value={consultForm.treatmentPlan || ""} onChange={(e) => setConsultForm({ ...consultForm, treatmentPlan: e.target.value })} />
                    <button className="btn btn-success" onClick={handleAddOrUpdateConsultation}>
                        {consultation ? "Update" : "Add"} Consultation
                    </button>
                </div>
            </div>

            {/* Prescription Section */}
            <div className="collapse" id="prescriptions">
                <div className="card card-body">
                    <h5>Prescriptions</h5>
                    {prescriptions.map((p, idx) => (
                        <li key={idx}>{p.medicineName} - {p.dosageTiming} - {p.mealTime} - {p.duration}</li>
                    ))}
                    <input className="form-control my-2" placeholder="Medicine Name"
                        value={newPrescription.medicineName} onChange={(e) => setNewPrescription({ ...newPrescription, medicineName: e.target.value })} />
                    <input className="form-control my-2" placeholder="DosageTiming"
                        value={newPrescription.dosageTiming} onChange={(e) => setNewPrescription({ ...newPrescription, dosageTiming: e.target.value })} />
                    <select className="form-control mb-2" value={newPrescription.mealTime}
                        onChange={(e) => setNewPrescription({ ...newPrescription, mealTime: e.target.value })}>
                        <option value="">Select Meal Time</option>
                        <option value="AF">AF</option>
                        <option value="BF">BF</option>
                    </select>
                    <input className="form-control my-2" placeholder="Duration"
                        value={newPrescription.duration} onChange={(e) => setNewPrescription({ ...newPrescription, duration: e.target.value })} />
                    <button className="btn btn-primary" onClick={handleAddPrescription}>Add Prescription</button>
                </div>
            </div>

            {/* Test Recommendations Section */}
            <div className="collapse" id="tests">
                <div className="card card-body">
                    <h5>Test Recommendations</h5>
                    {tests.map((t, idx) => (
                        <li key={idx}>{t.testName} - {t.status}</li>
                    ))}
                    <input className="form-control my-2" placeholder="Test Name"
                        value={newTest.testName} onChange={(e) => setNewTest({ ...newTest, testName: e.target.value })} />
                    <button className="btn btn-warning" onClick={handleAddTest}>Add Test</button>
                </div>
            </div>
        </div>
    );
}

export default ConsultationDetails;
