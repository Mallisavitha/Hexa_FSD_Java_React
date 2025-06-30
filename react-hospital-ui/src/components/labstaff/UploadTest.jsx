import { useState, useEffect } from "react";
import axios from "axios";
import { Modal, Button, Form } from "react-bootstrap";

function UploadTest() {
    const [tests, setTests] = useState([]);
    const [showDialog, setShowDialog] = useState(false);
    const [selectedTestId, setSelectedTestId] = useState(null);
    const [file, setFile] = useState(null);

    const token = localStorage.getItem("token");

    useEffect(() => {
        fetchTests();
    }, []);

    const fetchTests = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/test/all", {
                headers: { Authorization: `Bearer ${token}` }
            });
            setTests(res.data);
        } catch (err) {
            console.error("Error fetching tests", err);
        }
    };

    const handleUploadClick = (testId) => {
        setSelectedTestId(testId);
        setShowDialog(true);
    };

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleUpload = async () => {
        if (!file || !selectedTestId) return;

        const formData = new FormData();
        formData.append("file", file);

        try {
            await axios.post(`http://localhost:8080/api/test/upload/report/${selectedTestId}`, formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "multipart/form-data",
                },
            });
            alert("Report uploaded successfully!");
            setShowDialog(false);
            setFile(null);
            fetchTests(); // Refresh list
        } catch (err) {
            console.error("Upload failed", err);
            alert("Upload failed. Please try again.");
        }
    };

    const handleDeleteReport = async (testId) => {
        const confirmDelete = window.confirm("Are you sure you want to delete the report?");
        if (!confirmDelete) return;

        try {
            await axios.delete(`http://localhost:8080/api/test/delete/report/${testId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            alert("Report deleted successfully!");
            fetchTests(); // Refresh list
        } catch (err) {
            console.error("Delete failed", err);
            alert("Failed to delete report.");
        }
    };

    return (
        <div className="container">
            <h2 className="my-4">Lab Test Recommendations</h2>
            <div className="row"  style={{width:"900px"}}>
                {tests.map((test) => (
                    <div className="col-md-4 mb-4" key={test.testId}>
                        <div className="card h-100">
                            <div className="card-body">
                                <h5 className="card-title">Test ID: {test.testId}</h5>
                                <p className="card-text"><strong>Test Name:</strong> {test.testName}</p>
                                <p className="card-text"><strong>Status:</strong> {test.status}</p>
                                <p className="card-text"><strong>Appointment ID:</strong> {test.consultation?.appointment?.appointmentId || 'N/A'}</p>
                                <p className="card-text"><strong>Patient Name:</strong> {test.consultation?.appointment?.patient?.fullName || 'N/A'}</p>

                                {test.status === "COMPLETED" ? (
                                    <>
                                        <p className="card-text text-success">Report Uploaded</p>
                                        <Button variant="danger" className="mt-2" onClick={() => handleDeleteReport(test.testId)}>
                                            Delete Report
                                        </Button>
                                    </>
                                ) : (
                                    <Button variant="primary" onClick={() => handleUploadClick(test.testId)} >
                                        Upload Report
                                    </Button>
                                )}
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            <Modal show={showDialog} onHide={() => setShowDialog(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Upload Report</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group controlId="formFile">
                        <Form.Label>Select PDF Report</Form.Label>
                        <Form.Control type="file" accept="application/pdf" onChange={handleFileChange} />
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDialog(false)}>
                        Cancel
                    </Button>
                    <Button variant="primary" onClick={handleUpload}>
                        Upload
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default UploadTest;
