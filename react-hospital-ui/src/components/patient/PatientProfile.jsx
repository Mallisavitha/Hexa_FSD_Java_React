import axios from "axios";
import { useEffect, useState } from "react";

function PatientProfile() {
    const [patient, setPatient] = useState(null);
    const [editMode, setEditMode] = useState(false);
    const [selectedImage, setSelectedImage] = useState(null);
    const [form, setForm] = useState({
        fullName: "",
        dob: "",
        gender: "",
        contactNumber: "",
    });

    const token = localStorage.getItem("token");

    useEffect(() => {
        fetchPatient();
    }, []);

    const fetchPatient = async () => {
        try {
            const res = await axios.get("http://localhost:8080/api/patient/get-one", {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setPatient(res.data);
            setForm({
                fullName: res.data.fullName || "",
                dob: res.data.dob || "",
                gender: res.data.gender || "",
                contactNumber: res.data.contactNumber || "",
            });
        } catch (err) {
            console.error("Error fetching patient profile:", err);
        }
    };

    const handleImageChange = async (e) => {
        if (e.target.files && e.target.files[0]) {
            const image = e.target.files[0];
            setSelectedImage(image);

            const formData = new FormData();
            formData.append("file", image);

            try {
                const res = await axios.post(
                    `http://localhost:8080/api/patient/upload/profile-pic`,
                    formData,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "multipart/form-data"
                        },
                    }
                );
                setPatient(res.data);
                alert("Profile picture uploaded successfully!");
            } catch (err) {
                console.error("Image upload failed", err);
                alert("Failed to upload image");
            }
        }
    };

    const handleUpdate = async () => {
        try {
            const res = await axios.put(`http://localhost:8080/api/patient/update`, form, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setPatient(res.data);
            setEditMode(false);
            alert("Profile updated successfully!");
        } catch (err) {
            console.log("Error updating profile:", err);
        }
    };

    return (
        <div className="container-fluid min-vh-100">
            <h4 className="mt-3">Patient Profile</h4>

            <div className="row mt-3">
                <div className="col-lg-12">
                    <div className="card shadow-sm p-4" style={{ maxHeight: "98%" }}>


                        <div className="card-body">
                            <div className="pic text-center">
                                {/* Profile Picture Display */}
                                <img
                                    src={selectedImage ? URL.createObjectURL(selectedImage) : patient?.profilePic ? `../img/${patient.profilePic}` : "default.png"}
                                    alt="Profile"
                                    className="img-thumbnail mb-3"
                                    style={{ width: "150px", height: "150px", objectFit: "cover",borderRadius: "50%" }} />
                            </div>

                            {patient ? (
                                editMode ? (
                                    <div className="container">
                                        <div className="row mb-3">
                                            <div className="col-md-6">
                                                <input className="form-control" placeholder="Name" value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} />
                                            </div>
                                            <div className="col-md-6">
                                                <input className="form-control" placeholder="dob" value={form.dob} type="date" onChange={(e) => setForm({ ...form, dob: e.target.value })} />
                                            </div>
                                        </div>

                                        <div className="row mb-3">
                                            <div className="col-md-6">
                                                <input className="form-control" placeholder="Gender" value={form.gender} onChange={(e) => setForm({ ...form, gender: e.target.value })}/>
                                            </div>
                                            <div className="col-md-6">
                                                <input className="form-control" placeholder="ContactNumber" value={form.contactNumber} onChange={(e) => setForm({ ...form, contactNumber: e.target.value })}/>
                                            </div>
                                        </div>

                                        <div className="row mb-3">
                                            <div className="col-md-12">
                                                <label className="form-label">Upload Profile Picture</label>
                                                <input type="file" className="form-control" accept="image/*" onChange={handleImageChange} />
                                            </div>
                                        </div>

                                        <button className="btn btn-success" onClick={handleUpdate}>Save</button>
                                        <button className="btn btn-secondary ms-2" onClick={() => setEditMode(false)}>Cancel</button>
                                    </div>
                                ) : (
                                    <>
                                        <div className="row">
                                            <div className="col-md-6"><strong>Name:</strong> {patient.fullName}</div><br /><br />
                                            <div className="col-md-6"><strong>DOB:</strong> {patient.dob}</div><br /><br />
                                            <div className="col-md-6"><strong>Contact:</strong> {patient.contactNumber}</div><br /><br />
                                            <div className="col-md-6"><strong>Gender:</strong> {patient.gender}</div><br /><br />
                                        </div>
                                        <button className="btn btn-primary mt-3" onClick={() => setEditMode(true)}>Edit Profile</button>
                                    </>
                                )
                            ) : (
                                <p>Loading profile...</p>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default PatientProfile;
