import React, { useEffect, useState } from "react";
import axios from "axios";
import { Card } from "primereact/card";
import { Button } from "primereact/button";
import { Paginator } from "primereact/paginator";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import { Dropdown } from "primereact/dropdown";

function DoctorDetails() {
    const [doctors, setDoctors] = useState([]);
    const [selectedDeptId, setSelectedDeptId] = useState(null);
    const [departments, setDepartments] = useState([]);
    const [first, setFirst] = useState(0);
    const [rows, setRows] = useState(6);
    const [showEditDialog, setShowEditDialog] = useState(false);
    const [showAddDialog, setShowAddDialog] = useState(false);
    const [selectedDoctor, setSelectedDoctor] = useState(null);
    const [imageFile, setImageFile] = useState(null);
    const [addImageFile, setAddImageFile] = useState(null);
    const [addDoctor, setAddDoctor] = useState({
        fullName: "", contact: "", specialization: "",
        experienceYear: "", qualification: "", designation: "",
        user: { username: "", password: "" }
    });
    const [search, setSearch] = useState("");

    const token = localStorage.getItem("token");

    useEffect(() => {
        fetchDoctors();
        fetchDepartments();
    }, []);

    useEffect(() => {
        if (search.trim() === "") {
            fetchDoctors();
        } else {
            fetchDoctors(search);
        }
    }, [search]);

    const fetchDepartments = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/department/get-all", {
                headers: { Authorization: `Bearer ${token}` },
            });
            const formatted = res.data.map((dept) => ({
                deptId: dept.departmentId,
                deptName: dept.name
            }));
            setDepartments(formatted);
        } catch (err) {
            console.error("Failed to fetch departments", err);
        }
    };

    const fetchDoctors = async (searchTerm = "") => {
        try {
            const url = searchTerm
                ? `http://localhost:8080/api/doctor/search/${searchTerm}`
                : `http://localhost:8080/api/doctor/get-all`;

            const res = await axios.get(url, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setDoctors(res.data);
        } catch (err) {
            console.error("Failed to fetch doctors", err);
        }
    };

    const openEditDialog = (doctor) => {
        setSelectedDoctor(doctor);
        setImageFile(null);
        setShowEditDialog(true);
    };

    const handleEditSave = async () => {
        try {
            await axios.put(
                `http://localhost:8080/api/doctor/update/${selectedDoctor.doctorId}`,
                selectedDoctor,
                { headers: { Authorization: `Bearer ${token}` } }
            );

            if (imageFile) {
                const formData = new FormData();
                formData.append("file", imageFile);

                await axios.post(
                    `http://localhost:8080/api/doctor/upload/profile-pic/${selectedDoctor.doctorId}`,
                    formData,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "multipart/form-data"
                        },
                    }
                );
            }

            alert("Doctor updated successfully");
            setShowEditDialog(false);
            fetchDoctors();
        } catch (err) {
            console.error("Update failed", err);
            alert("Update failed. Please try again.");
        }
    };

    const handleAddDoctor = async () => {
        try {
            const res = await axios.post(
                `http://localhost:8080/api/doctor/add/${selectedDeptId}`,
                addDoctor,
                { headers: { Authorization: `Bearer ${token}` } }
            );

            const newDoc = res.data;

            if (addImageFile) {
                const formData = new FormData();
                formData.append("file", addImageFile);
                await axios.post(
                    `http://localhost:8080/api/doctor/upload/profile-pic/${newDoc.doctorId}`,
                    formData,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "multipart/form-data"
                        },
                    }
                );
            }

            alert("Doctor added successfully!");
            setShowAddDialog(false);
            setAddDoctor({
                fullName: "", contact: "", specialization: "",
                experienceYear: "", qualification: "", designation: "",
                user: { username: "", password: "" }
            });
            setAddImageFile(null);
            fetchDoctors();
        } catch (err) {
            console.error("Add doctor failed", err);
            alert("Doctor could not be added.");
        }
    };

    const onPageChange = (e) => {
        setFirst(e.first);
        setRows(e.rows);
    };

    const visibleDoctors = doctors.slice(first, first + rows);

    return (
        <div className="container mt-4">
            <h3>All Doctors</h3>

            <div className="d-flex justify-content-between align-items-center mb-3">
                <Button icon="pi pi-plus" className="p-button-success" label="Add Doctor" onClick={() => setShowAddDialog(true)} />
                <InputText placeholder="Search doctor" value={search} onChange={(e) => setSearch(e.target.value)} />
            </div>

            <div className="row" style={{ width: "900px" }}>
                {visibleDoctors.map((doc) => (
                    <div className="col-md-4 mb-4" key={doc.doctorId}>
                        <Card title={doc.fullName} subTitle={doc.specialization} header={
                                <img alt="Doctor" src={`../img/${doc.profilePic || "default.png"}`}
                                    style={{ height: '10rem', objectFit: 'cover' }} />
                            }
                            footer={
                                <div className="d-flex justify-content-between">
                                    <Button label="Edit" icon="pi pi-pencil" className="p-button-sm p-button-info" onClick={() => openEditDialog(doc)}/>
                                </div>
                            } >
                            <p className="m-0">
                                <strong>Qualification:</strong> {doc.qualification}<br />
                                <strong>Phone:</strong> {doc.contact}<br />
                                <strong>Experience:</strong> {doc.experienceYear} years
                            </p>
                        </Card>
                    </div>
                ))}
            </div>

            <Paginator first={first} rows={rows} totalRecords={doctors.length} rowsPerPageOptions={[3, 6, 9]}
                onPageChange={onPageChange} className="mt-4" style={{ backgroundColor: "transparent" }}/>

            {/* Edit Dialog */}
            <Dialog header="Edit Doctor" visible={showEditDialog} style={{ width: '450px' }} modal onHide={() => setShowEditDialog(false)}>
                {selectedDoctor && (
                    <div className="p-fluid">
                        <div className="field">
                            <label>Full Name</label>
                            <InputText value={selectedDoctor.fullName} onChange={(e) => setSelectedDoctor({ ...selectedDoctor, fullName: e.target.value })} />
                        </div>
                        <div className="field">
                            <label>Phone</label>
                            <InputText value={selectedDoctor.contact} onChange={(e) => setSelectedDoctor({ ...selectedDoctor, contact: e.target.value })} />
                        </div>
                        <div className="field">
                            <label>Specialization</label>
                            <InputText value={selectedDoctor.specialization} onChange={(e) => setSelectedDoctor({ ...selectedDoctor, specialization: e.target.value })} />
                        </div>
                        <div className="field">
                            <label>Experience (Years)</label>
                            <InputText value={selectedDoctor.experienceYear} onChange={(e) => setSelectedDoctor({ ...selectedDoctor, experienceYear: e.target.value })} />
                        </div>
                        <div className="field">
                            <label>Qualification</label>
                            <InputText value={selectedDoctor.qualification} onChange={(e) => setSelectedDoctor({ ...selectedDoctor, qualification: e.target.value })} />
                        </div>
                        <div className="field">
                            <label>Upload Profile Picture</label>
                            <input type="file" accept="image/*" onChange={(e) => setImageFile(e.target.files[0])} />
                        </div>
                        <div className="d-flex justify-content-end mt-3">
                            <Button label="Cancel" className="p-button-text me-2" onClick={() => setShowEditDialog(false)} />
                            <Button label="Save" className="p-button-success" onClick={handleEditSave} />
                        </div>
                    </div>
                )}
            </Dialog>

            {/* Add Doctor Dialog */}
            <Dialog header="Add Doctor" visible={showAddDialog} style={{ width: '450px' }} modal onHide={() => setShowAddDialog(false)}>
                <div className="p-fluid">
                    <div className="field">
                        <label>Full Name</label>
                        <InputText value={addDoctor.fullName} onChange={(e) => setAddDoctor({ ...addDoctor, fullName: e.target.value })} />
                    </div>
                    <div className="field">
                        <label>Phone</label>
                        <InputText value={addDoctor.contact} onChange={(e) => setAddDoctor({ ...addDoctor, contact: e.target.value })} />
                    </div>
                    <div className="field">
                        <label>Specialization</label>
                        <InputText value={addDoctor.specialization} onChange={(e) => setAddDoctor({ ...addDoctor, specialization: e.target.value })} />
                    </div>
                    <Dropdown value={selectedDeptId} options={departments} onChange={(e) => setSelectedDeptId(e.value)} optionValue="deptId" optionLabel="deptName" placeholder="Select Department" />
                    <div className="field">
                        <label>Experience</label>
                        <InputText value={addDoctor.experienceYear} onChange={(e) => setAddDoctor({ ...addDoctor, experienceYear: e.target.value })} />
                    </div>
                    <div className="field">
                        <label>Qualification</label>
                        <InputText value={addDoctor.qualification} onChange={(e) => setAddDoctor({ ...addDoctor, qualification: e.target.value })} />
                    </div>
                    <div className="field">
                        <label>Designation</label>
                        <InputText value={addDoctor.designation} onChange={(e) => setAddDoctor({ ...addDoctor, designation: e.target.value })} />
                    </div>
                    <div className="field">
                        <label>Profile Picture</label>
                        <input type="file" accept="image/*" onChange={(e) => setAddImageFile(e.target.files[0])} />
                    </div>
                    <div className="field">
                        <label>Username</label>
                        <InputText value={addDoctor.user?.username || ""} onChange={(e) => setAddDoctor({ ...addDoctor, user: { ...addDoctor.user, username: e.target.value } })} />
                    </div>
                    <div className="field">
                        <label>Password</label>
                        <InputText type="password" value={addDoctor.user?.password || ""} onChange={(e) => setAddDoctor({ ...addDoctor, user: { ...addDoctor.user, password: e.target.value } })} />
                    </div>
                </div>
                <div className="d-flex justify-content-end mt-3">
                    <Button label="Cancel" className="p-button-text me-2" onClick={() => setShowAddDialog(false)} />
                    <Button label="Save" className="p-button-success" onClick={handleAddDoctor} />
                </div>
            </Dialog>
        </div>
    );
}

export default DoctorDetails;
