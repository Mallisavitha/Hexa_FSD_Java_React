import React, { useEffect, useState } from "react";
import axios from "axios";
import { Dialog } from "primereact/dialog";
import { Button } from "primereact/button";
import { InputText } from "primereact/inputtext";
import { ConfirmDialog, confirmDialog } from "primereact/confirmdialog";
import { Chart } from 'primereact/chart';

function AdminDashboard() {
    const token = localStorage.getItem("token");
    const [appointmentsToday, setAppointmentsToday] = useState([]);
    const [doctors, setDoctors] = useState([]);
    const [patients, setPatients] = useState([]);
    const [labStaff, setLabStaff] = useState([]);
    const [departments, setDepartments] = useState([]);
    const [showDeptDialog, setShowDeptDialog] = useState(false);
    const [newDeptName, setNewDeptName] = useState("");
    const [appointmentTrend, setAppointmentTrend] = useState([]);

    useEffect(() => {
        fetchDashboardData();
        fetchAppointmentTrend();
    }, []);


    const fetchAppointmentTrend = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/appointment/last-7-days", {
                headers: { Authorization: `Bearer ${token}` }
            });
            setAppointmentTrend(res.data);
        } catch (err) {
            console.error("Failed to fetch appointment trend", err);
        }
    };

    const fetchDashboardData = async () => {
    try {
        const today = new Date();

        const apptRes = await axios.get("http://localhost:8080/api/appointment/get-all", {
            headers: { Authorization: `Bearer ${token}` }
        });

        const doctorRes = await axios.get("http://localhost:8080/api/doctor/get-all", {
            headers: { Authorization: `Bearer ${token}` }
        });

        const patientRes = await axios.get("http://localhost:8080/api/patient/get-all", {
            headers: { Authorization: `Bearer ${token}` }
        });

        const labRes = await axios.get("http://localhost:8080/api/labstaff/get-all", {
            headers: { Authorization: `Bearer ${token}` }
        });

        const deptRes = await axios.get("http://localhost:8080/api/department/get-all", {
            headers: { Authorization: `Bearer ${token}` }
        });

        // Fix date comparison: convert both to yyyy-mm-dd string
        const todayStr = today.toISOString().split("T")[0];

        setAppointmentsToday(apptRes.data.filter(appt => appt.scheduledDate === todayStr));
        setDoctors(doctorRes.data);
        setPatients(patientRes.data);
        setLabStaff(labRes.data);
        setDepartments(deptRes.data);
    } catch (err) {
        console.error("Dashboard fetch failed", err);
    }
};


    const handleAddDepartment = async () => {
        try {
            await axios.post("http://localhost:8080/api/department/add", {
                name: newDeptName
            }, {
                headers: { Authorization: `Bearer ${token}` }
            });

            setShowDeptDialog(false);
            setNewDeptName("");
            fetchDashboardData();
        } catch (err) {
            console.error("Add department failed", err);
        }
    };


    return (
        <div className="container mt-4">
            <ConfirmDialog />

            <h3 className="mb-4">Welcome, admin</h3>
            {/** Number details */}
            <div className="row text-center mb-4">
                <div className="col-md-3 mb-3">
                    <div className="card shadow">
                        <div className="card-body">
                            <h5>Today's Appointments</h5>
                            <p className="fs-4">{appointmentsToday.length}</p>
                        </div>
                    </div>
                </div>
                <div className="col-md-3 mb-3">
                    <div className="card shadow">
                        <div className="card-body">
                            <h5>Total Doctors</h5>
                            <p className="fs-4">{doctors.length}</p>
                        </div>
                    </div>
                </div>
                <div className="col-md-3 mb-3">
                    <div className="card shadow">
                        <div className="card-body">
                            <h5>Total Patients</h5>
                            <p className="fs-4">{patients.length}</p>
                        </div>
                    </div>
                </div>
                <div className="col-md-3 mb-3">
                    <div className="card shadow">
                        <div className="card-body">
                            <h5>Total Lab Staff</h5>
                            <p className="fs-4">{labStaff.length}</p>
                        </div>
                    </div>
                </div>
            </div>

            {/**Add Department */}
            <div className="d-flex justify-content-between align-items-center mb-3">

                <Button label="Add Department" icon="pi pi-plus" className="p-button-success" onClick={() => setShowDeptDialog(true)} />
            </div>

            <div className="row">
                <div className="col-lg-6">
                    {/* Last 7 Days Appointment Chart */}
                    {appointmentTrend.length > 0 && (
                        <div className="card shadow mb-4" style={{ width: "500px" }}>
                            <div className="card-body">
                                <h5 className="card-title">Appointments in Last 7 Days</h5>
                                <Chart type="bar" data={{
                                    labels: appointmentTrend.map(item => item.date),
                                    datasets: [
                                        {
                                            label: 'Appointments',
                                            data: appointmentTrend.map(item => item.count),
                                            backgroundColor: 'rgba(75, 192, 192, 0.5)',
                                            borderColor: 'rgba(75, 192, 192, 1)',
                                            borderWidth: 1
                                        }
                                    ]
                                }}
                                    options={{
                                        responsive: true, plugins: {
                                            legend: { display: false },
                                            title: {
                                                display: true, text: 'Daily Appointments Trend'
                                            }
                                        },
                                        scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
                                    }}
                                    style={{ height: '300px' }} />
                            </div>
                        </div>
                    )}
                </div>

                <div className="col-lg-6">
                    {/**Fetch Department */}
                    <div className="table-responsive mb-20" style={{ marginLeft: "50px", width: "400px" }}>
                        <h4>Departments</h4>
                        <table className="table table-bordered">
                            <thead className="table-light">
                                <tr>
                                    <th>Department Id</th>
                                    <th>Department Name</th>
                                </tr>
                            </thead>
                            <tbody>
                                {departments.map((dept, idx) => (
                                    <tr key={dept.departmentId}>
                                        <td>{idx + 1}</td>
                                        <td>{dept.name}</td>

                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            {/**Add Department dialog */}
            <Dialog header="Add Department" visible={showDeptDialog} style={{ width: '400px' }} modal onHide={() => setShowDeptDialog(false)}>
                <div className="p-fluid">
                    <div className="field">
                        <label>Department Name</label>
                        <InputText value={newDeptName} onChange={(e) => setNewDeptName(e.target.value)} />
                    </div>
                    <div className="mt-3 d-flex justify-content-end">
                        <Button label="Cancel" className="p-button-text me-2" onClick={() => setShowDeptDialog(false)} />
                        <Button label="Save" className="p-button-success" onClick={handleAddDepartment} />
                    </div>
                </div>
            </Dialog>
        </div>
    );
}

export default AdminDashboard;
