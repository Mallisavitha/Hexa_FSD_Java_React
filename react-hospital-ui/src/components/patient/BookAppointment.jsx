import React, { useState, useEffect } from "react";
import axios from "axios";
import { Modal, Button, Form } from "react-bootstrap";

function BookAppointment() {
    const [doctors, setDoctors] = useState([]);
    const [specializations, setSpecializations] = useState([]);
    const [selectedSpecialization, setSelectedSpecialization] = useState("");
    const [selectedDoctor, setSelectedDoctor] = useState(null);
    const [slots, setSlots] = useState([]);
    const [page, setPage] = useState(0);
    const [row, setRow] = useState(3);
    const [selectedSlot, setSelectedSlot] = useState("");
    const [natureOfVisit, setNatureOfVisit] = useState("");
    const [description, setDescription] = useState("");
    const [showDialog, setShowDialog] = useState(false);

    const token = localStorage.getItem("token");

    useEffect(() => {
        fetchSpecializations();
    }, []);

    useEffect(() => {
        if (selectedSpecialization) {
            fetchDoctorsBySpecialization(selectedSpecialization);
        } else {
            fetchDoctors();
        }
    }, [page, selectedSpecialization]);

    const fetchDoctors = async () => {
        try {
            const res = await axios.get(`http://localhost:8080/api/doctor/get-all?page=${page}&size=${row}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setDoctors(res.data);
        } catch (err) {
            console.error("Error fetching doctors", err);
        }
    };

    const fetchSpecializations = async () => {
        try {
            const res = await axios.get(`http://localhost:8080/api/doctor/specialization`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setSpecializations(res.data);
        } catch (err) {
            console.error("Error fetching specializations", err);
        }
    };

    const fetchDoctorsBySpecialization = async (spec) => {
        try {
            const res = await axios.get(
                `http://localhost:8080/api/doctor/specialization/${spec}`,
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setDoctors(res.data);
        } catch (err) {
            console.error("Error fetching doctors by specialization", err);
        }
    };

    const handleOpenDialog = async (doctor) => {
        setSelectedDoctor(doctor);
        setSelectedSlot("");
        setSlots([]);
        setShowDialog(true);
        try {
            const res = await axios.get(
                `http://localhost:8080/api/doctor-slot/by-doctor/${doctor.doctorId}`,
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setSlots(res.data);
        } catch (err) {
            console.error("Error fetching slots", err);
        }
    };

    const handleBookAppointment = async () => {
        try {
            await axios.post(
                `http://localhost:8080/api/appointment/book/${selectedDoctor.doctorId}`,
                {
                    doctorSlotId: selectedSlot,
                    natureOfVisit: natureOfVisit,
                    description: description,
                },
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );
            alert("Appointment booked successfully!");
            setShowDialog(false);
        } catch (err) {
            console.error("Booking failed", err);
        }
    };

    return (
        <div className="container">
            <h2 className="mb-4">Available Doctors</h2>

            <div className="row">
                {/* Specialization Filter Dropdown */}
                <div className="col-lg-6">
                    <Form.Group className="mb-4" style={{ width: "250px" }}>
                        <Form.Select
                            value={selectedSpecialization}
                            onChange={(e) => {
                                setSelectedSpecialization(e.target.value);
                                setPage(0); // Reset pagination when filtering
                            }}>
                            <option value="">All Specializations</option>
                            {specializations.map((spec, index) => (
                                <option key={index} value={spec}>
                                    {spec}
                                </option>
                            ))}
                        </Form.Select>
                    </Form.Group>
                </div>

                {/* Pagination Controls */}
                <div className="col-lg-6">
                    <nav aria-label="Page navigation example">
                        <ul className="pagination justify-content-end">
                            <li className="page-item">
                                <button className="page-link" disabled={page === 0} onClick={() => setPage(page - 1)}>
                                    Previous
                                </button>
                            </li>
                            <li className="page-item">
                                <span className="page-link">{page + 1}</span>
                            </li>
                            <li className="page-item">
                                <button className="page-link" onClick={() => setPage(page + 1)}>
                                    Next
                                </button>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>

            {/* Doctor Cards */}
            <div className="row" style={{ width: "900px" }}>
                {doctors.map((doc) => (
                    <div className="col-md-4 mb-4" key={doc.doctorId}>
                        <div className="card">
                            <img src={`../img/${doc.profilePic || "default.png"}`} className="card-img-top" alt={doc.fullName} />
                            <div className="card-body">
                                <h4 className="card-title">{doc.fullName}</h4>
                                <p className="card-text">{doc.specialization}</p>
                            </div>
                            <ul className="list-group list-group-flush">
                                <li className="list-group-item"><strong>Qualification:</strong> {doc.qualification || "N/A"}</li>
                                <li className="list-group-item"><strong>Designation:</strong> {doc.designation || "N/A"}</li>
                                <li className="list-group-item"><strong>Experience:</strong> {doc.experienceYear || "N/A"} years</li>
                                <li className="list-group-item"><strong>Contact:</strong> {doc.contact || "N/A"}</li>

                            </ul>
                            <div className="card-body">
                                <button className="btn btn-primary w-100" onClick={() => handleOpenDialog(doc)} >
                                    Book Appointment
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>


            {/* Modal for Booking */}
            <Modal show={showDialog} onHide={() => setShowDialog(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Book Appointment</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {selectedDoctor && (
                        <Form>
                            <Form.Group>
                                <Form.Label>Doctor</Form.Label>
                                <Form.Control type="text" value={selectedDoctor.fullName} disabled />
                            </Form.Group>

                            <Form.Group>
                                <Form.Label>Specialization</Form.Label>
                                <Form.Control type="text" value={selectedDoctor.specialization} disabled />
                            </Form.Group>

                            <Form.Group>
                                <Form.Label>Choose Slot</Form.Label>
                                {slots.length === 0 ? (
                                    <p className="text-danger">No available slots found</p>
                                ) : (
                                    <Form.Select value={selectedSlot} onChange={(e) => setSelectedSlot(e.target.value)} >
                                        <option value="">Select Slot</option>
                                        {slots.map((slot) => (
                                            <option key={slot.slotId} value={slot.slotId}>
                                                {slot.date} | {slot.time}
                                            </option>
                                        ))}
                                    </Form.Select>
                                )}
                            </Form.Group>

                            <Form.Group>
                                <Form.Label>Nature of Visit</Form.Label>
                                <Form.Control type="text" value={natureOfVisit} onChange={(e) => setNatureOfVisit(e.target.value)}/>
                            </Form.Group>

                            <Form.Group>
                                <Form.Label>Description</Form.Label>
                                <Form.Control as="textarea" value={description} onChange={(e) => setDescription(e.target.value)} />
                            </Form.Group>
                        </Form>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDialog(false)}>
                        Cancel
                    </Button>
                    <Button variant="primary" onClick={handleBookAppointment}>
                        Book
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default BookAppointment;
